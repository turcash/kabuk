# Architecture

Kabuk turns a rooted Android phone into an always-on development server and
gives you two ways to reach it: a native terminal app (Kabuk Client) and a chat
UI for Claude Code (Kabuk Agent). Everything runs on the phone itself; there is
no cloud component and no relay server.

## Layers

```
┌──────────────────────────────────────────────────────────────────────┐
│  Your laptop / workstation                                           │
│    Kabuk Client (Android APK, terminal + mosh/ssh)                   │
│    or ssh / mosh / browser over Tailscale                            │
└───────────────────────────────┬──────────────────────────────────────┘
                                │ Tailscale (WireGuard), tailnet only
┌───────────────────────────────▼──────────────────────────────────────┐
│  Android 12 – Redmi Note 10 5G (camellia)                            │
│                                                                      │
│  Magisk module `kabuk-sunucu`                                        │
│    service.sh → kabuk-boot → watchdog loop (30 s)                    │
│                                                                      │
│  /data/kabuk/bin/kabuk      CLI, mount manager, watchdog, firewall    │
│  /data/kabuk/etc/kabuk.conf settings                                 │
│  /data/kabuk/share/         runtime payload deployed from the APK     │
│  /data/kabuk/rootfs/        Alpine 3.24 aarch64 chroot                │
│  /data/kabuk/log/           boot.log, health.csv                     │
│  /data/kabuk/backups/       rootfs snapshots                         │
│                                                                      │
│  chroot /data/kabuk/rootfs                                           │
│    runit (runsvdir) supervises:                                      │
│      tailscaled  sshd  taildrop  ttyd  ntfy  kabuk-agent             │
│    user: dev (uid 8000, groups aid_inet/aid_net_raw/wheel)          │
│    Claude Code, Node, Python, git, tmux, Alpine packages             │
└──────────────────────────────────────────────────────────────────────┘
```

## Process model

There are two separate worlds and Kabuk keeps them apart on purpose.

**Android side** (`/system/bin/sh`, root via Magisk, SELinux enforcing) owns
battery, thermals, wakelocks, iptables and the runit supervisor process. The
`kabuk` script lives here. It is started by `kabuk-boot`, which is started by
the Magisk module at `late_start`, i.e. before you unlock the screen and without
opening any app.

**Chroot side** (Alpine, musl, uid 8000 user) runs everything a developer needs:
sshd, mosh, tmux, a web terminal, notifications, the agent, and Claude Code.
Nothing inside the chroot can touch the Android side unless `FULL_ROOT=1`
explicitly grants passwordless `sudo` plus the `android` helper.

## Why a chroot instead of proot or a VM

* Real `chroot(2)` with real bind mounts: `tmux`, `git`, `npm`, PTYs and
  `rm -rf .git` behave exactly like on a normal Linux host.
* No emulation, no `proot` syscall interception: overhead is near zero, which
  matters on a Dimensity 700.
* The kernel is shared with Android, so the memory cost is only the Alpine
  userland (a few hundred MB), not a second Android.

The price is that `/data` is mounted `nosuid`, so `sudo` cannot work with a
plain bind mount. When `FULL_ROOT=1` the mount manager additionally bind-mounts
`/usr/bin` with `remount,bind,suid`; the rest of the rootfs stays non-suid.

## Boot sequence

1. Magisk `late_start` runs `magisk/service.sh`.
2. `service.sh` exits immediately if `/data/kabuk/DISABLED` exists, otherwise
   `setsid`s `kabuk-boot` in the background.
3. `kabuk-boot` waits for `sys.boot_completed=1`, then forces
   `input_suspend=0` (fail-safe: charging is never left disabled), runs
   `kabuk tweaks` (Android power/phantom-process settings) and `kabuk start`.
4. `kabuk start` installs the LAN firewall first, syncs the Tailscale policy
   routing rule, performs the idempotent mount set, copies `share/` into the
   rootfs, then launches `runsvdir -P /etc/kabuk/service` inside the chroot.
5. `kabuk-boot` then loops on `kabuk watchdog`; if the watchdog dies it is
   restarted 10 seconds later and charging is re-enabled first.

## Watchdog

One `flock`-guarded process, one iteration every 30 seconds:

| Tick | What it does |
|---|---|
| supervisor | restarts `runsvdir` if it disappeared, re-applies the firewall if the chain was flushed |
| routing | re-points the Tailscale `fwmark` rule at the current default network table (Wi-Fi ⇄ mobile) |
| charging | `input_suspend` hysteresis between `CHARGE_START` and `CHARGE_STOP`, thermal charge cut |
| thermal | above `HOT_C` restricts the cgroup to the little cores and `nice`s user processes |
| wakelock | holds `kabuk` only when work is running or a charge window is open |
| network | if there is no route/ping for `NET_RESET_AFTER` seconds, power-cycles Wi-Fi (max once per 15 min) |
| health | one CSV row per 5 minutes into `log/health.csv`, rotated at 2 MB |

## Data flow of the agent

```
browser ──► kabuk-agent (Node, :8787 on tailscale0)
                 │  every request is authorised with `tailscale whois`
                 ├── spawn claude -p --resume <session> --output-format stream-json
                 ├── append events to ~/.kabuk-agent/chats/<id>.jsonl
                 └── SSE stream to the browser, resumable with ?after=N
```

Because events are appended to disk before they are sent, closing the tab or
losing the mobile connection does not interrupt the run; reopening the page
replays everything you missed.

## Directory layout of this repository

```
runtime/                 everything that ends up on the phone
  bin/kabuk              main CLI (Android side)
  bin/kabuk-boot         boot orchestrator
  bin/kabuk-enter        convenience wrapper
  bin/kabuk-servermode   disables non-server apps (reversible)
  etc/kabuk.conf         settings, read by the watchdog every 30 s
  magisk/                Magisk module that starts everything at boot
  rootfs/usr/local/bin/  helpers copied into the chroot
  rootfs/opt/kabuk-agent/ Node chat server + single-page UI
  sv/<service>/run       runit service definitions
  tests/stage2.sh        on-device regression checks
apps/
  client/recovered-java  Kabuk Client, recovered from the released APK
  server/recovered-java  Kabuk Server, recovered from the released APK
  prebuilt/              signed APKs
docs/                    this documentation
```

See [12-design-decisions.md](12-design-decisions.md) for the reasoning behind
each of these choices.
