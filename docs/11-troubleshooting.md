# Troubleshooting

Every item here actually happened on the device; each one cost hours, so they
are written down in the order you are likely to hit them.

## Installation

### `kabuk: root gerekli (su -c kabuk ...)`

The script must run as root. From an adb shell:

```sh
adb shell
su
/data/kabuk/bin/kabuk status
```

If `su` itself does nothing, Magisk lost its daemon (see the next item).

### Magisk asks for root but never grants it

A repackaged/hidden Magisk can lose the connection to its daemon while the
prompt stays "pending". Install the official Magisk APK of the same version over
it (data is kept), and remove other Magisk forks (`io.github.huskydg.magisk` and
friends) — two daemons cannot own `/data/adb`.

### `kabuk install` stops at `KABUK_TARBALL ... gerekli`

The rootfs is absent and no tarball was provided. Either use the app's
**Kur / Onar** (it downloads and verifies the archive), or pass both variables:

```sh
KABUK_TARBALL=/data/local/tmp/alpine-minirootfs-3.24.4-aarch64.tar.gz \
KABUK_SHA256=<from the Alpine release page> \
/data/kabuk/bin/kabuk install
```

### `apk add` fails with "temporary error" or hangs

DNS inside the chroot. `install` writes `1.1.1.1`/`9.9.9.9` to
`rootfs/etc/resolv.conf`; if your network blocks them, change that file. Also
check that the mount set exists (`kabuk status` → `mounts:` should be > 5).

### Claude Code install fails

`curl -fsSL https://claude.ai/install.sh | bash` runs as `dev`. If it fails,
the usual cause is a missing `aid_inet` group membership — the Android
"paranoid network" kernel setting refuses sockets to processes without
`AID_INET` (3003) even when they are root.

```sh
su -c '/data/kabuk/bin/kabuk enter root'
grep aid_inet /etc/group          # must list dev and root
```

### Install finishes but `runsvdir:` is empty

Look at `/data/kabuk/log/boot.log`. The two common reasons are a leftover
`/data/kabuk/DISABLED` file (`kabuk enable`) and a stale mount namespace —
`kabuk` handles the latter by re-executing through `nsenter -t 1 -m`.

## Runtime

### `mount: '--bind' is not supported` / weird loop device errors

Android's toybox `mount --bind` treats a character device as a loop image. The
script works around this by creating device nodes with `mknod` directly. If you
edit `do_mount`, do not "simplify" it back to `mount --bind` for `/dev/*` nodes.

### Processes stay visible in `/proc` but the mount set is gone

`do_umount` refuses to unmount while `users_of_rootfs` finds a process whose
`/proc/<pid>/root` points at the chroot. That is intentional: closing one tab
must not break another. If a process is genuinely stuck, `kabuk umount force`.

### `flock: bad file descriptor` in a helper script

mksh (Android's shell) sets `close-on-exec` on file descriptors opened with
`exec 8>file`, so an external `flock` cannot see them. Take the lock in a
subshell redirection instead:

```sh
( flock -n 9 || exit 1; do_work ) 9>/path/to/lock
```

This is why both `kabuk-boot` and `kabuk watchdog` look slightly unusual.

### Settings written from the chroot fail with "Failed transaction"

`cmd`, `settings` and `device_config` pass their file descriptors to
`system_server`, which runs in a SELinux domain that is not allowed to write to
`/data/kabuk`. Never redirect their output to a file inside the chroot; pipe it
to a shell function that appends, exactly like `logw` in `kabuk-boot`.

### `sudo: effective uid is not 0` even as root

`/data` is mounted `nosuid`, so a bind mount inherits `nosuid` and the `sudo`
binary's setuid bit is ignored. Set `FULL_ROOT=1` and `kabuk restart`: the mount
manager then also bind-mounts `/usr/bin` with `remount,bind,suid`, and only that
subtree is suid.

### Home directories are `2700` and `sudo` complains

busybox `adduser` creates the home directory setgid, and the mode propagates to
subdirectories. `install` fixes it:

```sh
find /home/dev -type d -perm -2000 -exec chmod g-s {} +
```

### SSH refuses the key with `Permission denied (publickey)`

Check that the account is not locked. `passwd -l` writes `!` into
`/etc/shadow`, and OpenSSH (without PAM) rejects a locked account even for key
auth. Kabuk sets `*` instead:

```sh
grep '^dev:' /etc/shadow     # must be dev:*:…
usermod -p '*' dev
```

Also verify ownership: a key in `authorized_keys` owned by root is ignored by
`StrictModes`.

### Turkish characters show as `_` in tmux

Locale detection fails over mosh/ssh, and tmux replaces every non-ASCII
character with `_`. Kabuk forces `LANG=C.UTF-8` and always calls
`tmux -u`. If you start tmux yourself, use `tmux -u` or export
`LC_CTYPE=C.UTF-8`.

### `kabuk test` fails on "servisler LAN'da dinlemiyor"

A service is bound to `0.0.0.0` or to the Wi-Fi address. Look at
`ss -lntup` on the Android side; the usual culprit is a service you added
without `kabuk-tsip`. Fix the `run` script, not the firewall.

## Tailscale

### `tailscaled` runs but nothing is reachable

Check the routing rule first:

```sh
su -c 'ip rule | grep 5240'
# 5240: from all fwmark 0x80000/0xff0000 lookup wlan0
```

If it points at a table that no longer exists, wait one watchdog tick (30 s) or
run `kabuk start`. The rule must match the table Android's own
`29000: … iif lo lookup <table>` rule uses.

### `tailscale up` prints a login URL every time

The state file was lost (`/var/lib/tailscale/tailscaled.state`). That happens
after `kabuk restore` from a backup taken before login, or if you deleted the
rootfs. Log in again; the node name is reused.

### Works on Wi-Fi, dies on mobile data

Expected without the routing rule: Android moves the default network to
`rmnet_dataN`, and a stale `lookup wlan0` rule blackholes Tailscale's own
traffic. `ts_route_sync` fixes it within 30 seconds. If it does not, check that
`ip rule` still shows the 29000 rule — some MIUI builds rewrite rules while
roaming.

### `mosh` connects but the screen never updates

Userspace mode. `mosh` needs a directly reachable UDP endpoint; with
`TS_MODE=userspace` it cannot work. Either restore TUN:

```sh
su -c 'ls -l /dev/net/tun'
su -c 'cat /proc/misc | grep tun'
```

or use ssh.

## Charging and thermals

### Battery sticks at 80 % and never charges

That is the design (`CHARGE_STOP=80`, resume at 75). To check the state:

```sh
su -c 'cat /sys/class/power_supply/battery/input_suspend'   # 1 = charging paused
su -c '/data/kabuk/bin/kabuk status'                        # shows both
```

If the phone is stuck paused with the watchdog dead, write `0` to
`input_suspend`; `kabuk-boot` does this on every boot precisely to avoid a
permanently paused charger.

### It charges again only after a reboot

`CHARGE_FAILSAFE` (70 %) forces charging below that level, but the watchdog has
to be running to apply it. `kabuk status` → `watchdog` and `runsvdir:` must both
be alive; otherwise `kabuk start`.

### The phone gets hot and everything slows down

Above `HOT_C` (40 °C) the watchdog pins the service cgroup to the little cores
(`/dev/cpuset/kabuk`) and `nice`s user processes; above
`HOT_STOP_CHARGE_C` (45 °C) charging stops. If you see this often, lower
`HOT_C`, or raise the phone so the back can breathe. A phone in a case on a
blanket will throttle no matter what the software does.

## Agent

### `403 {"error":"yalniz tailnet sahibi"}`

The request did not come from a device owned by the tailnet owner. Check:

```sh
su -c 'kabuk ts status'
su -c '/data/kabuk/bin/kabuk enter root'
tailscale whois --json <your-ip>
```

The check requires the `whois` capability, which the tailnet node has by
default. If you connected through a subnet router, the peer address is the
router, not your device — connect directly.

### The chat starts and immediately shows `500`

`claude` is not on the service user's `PATH`. The service sets
`PATH=/home/dev/.local/bin:…`; verify with:

```sh
su -c '/data/kabuk/bin/kabuk run dev "command -v claude"'
```

### A chat is stuck in `running`

The process tree died without writing its final event (usually a reboot). The
server marks such chats `interrupted` at startup. If it happens repeatedly,
look at `var/log/kabuk/kabuk-agent/current` for OOM kills: Claude Code is the
biggest consumer on a 4 GB phone.

### Preset costs more tokens than expected

The preset is applied per message with `--settings`, not stored in the session,
so its prompt cost is paid every turn. `tam` (≈50 k) is genuinely expensive; use
`hafif` for questions and switch per chat.

## Client

### `Host key verification failed`

The client uses `accept-new` with `known_hosts` in its private home. After a
rootfs reinstall the host key changes: delete the app's `files/home/.ssh/known_hosts`
(or clear app data).

### mosh tab falls back to ssh

That is the designed degradation. The log line in the tab says
`MoshUnavailable` — usually the server is in userspace mode, or UDP
60000–61000 is blocked on the network you are on.

### The keyboard covers the terminal

The terminal view resizes to the visible area; if you use a system-level
gesture navigation, check that the app is not in "resize = pan" mode in the
display settings.

## Build

### Gradle cannot find the SDK

`local.properties` must contain `sdk.dir=…`. That file is machine-specific and
must not be committed (it is in `.gitignore`).

### The APK installs but ssh cannot execute

The native binaries must be built for `aarch64` against **musl**, not glibc, and
must be packaged as `lib*.so` in `jniLibs` so Android extracts them. A binary
named `ssh` inside `assets/` is not executable.

### `libldmusl.so` fails with "not executable: 64-bit ELF"

The APK was built for the wrong ABI. Only `arm64-v8a` is supported.
