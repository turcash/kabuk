# CLI reference

`/data/kabuk/bin/kabuk` is the only entry point on the Android side. It must run
as root (`su -c '/data/kabuk/bin/kabuk …'`). If it is started from an app that
lives in a different mount namespace, it re-executes itself through
`nsenter -t 1 -m` so that mounts are visible system-wide.

```
kabuk <command> [arguments]
```

## Session commands

### `kabuk enter [user]`

Mounts everything (idempotent), opens a login shell in the chroot and unmounts
on exit if no other process is still inside. Default user is `dev`;
`kabuk enter root` gives a root shell inside Alpine.

```sh
su -c '/data/kabuk/bin/kabuk enter'
su -c '/data/kabuk/bin/kabuk enter root'
```

### `kabuk run <user> <command…>`

Runs one command in the chroot. For non-root users it loads `/etc/profile`
explicitly, because busybox `su -l -c` skips it.

```sh
su -c '/data/kabuk/bin/kabuk run dev "claude --version"'
su -c '/data/kabuk/bin/kabuk run dev "cd ~/projeler/api && npm test"'
su -c '/data/kabuk/bin/kabuk run root "apk add htop"'
```

### `kabuk mount` / `kabuk umount [force]`

`mount` installs the mount set under a `flock`:

| Target | How |
|---|---|
| `/proc` | proc |
| `/sys` | sysfs, read-only |
| `/dev` | private tmpfs with `null zero full random urandom tty ptmx` nodes |
| `/dev/pts` | bind from Android |
| `/dev/shm` | tmpfs 1777 |
| `/dev/net/tun` | `mknod c 10 200` when the kernel has TUN |
| `/tmp` | tmpfs 1777, 1 GB |
| `/mnt/telefon` | bind of `/data/media/0` (shared storage) |
| `/dev/bus/usb` | bind, only if present (USB devices) |
| `/usr/bin` | bind + `remount,bind,suid`, only when `FULL_ROOT=1` |

`umount` does nothing while other processes are still inside the chroot, so
closing one terminal never pulls the filesystem out from under another. Use
`kabuk umount force` to override.

### `kabuk status`

One screen: rootfs version, mount count, process count, `runsvdir` PID, per
service state, emergency-stop flag, battery level/temperature/status/
`input_suspend`, wakelock, rootfs size and the list of backups.

### `kabuk panel`

The same information as `key=value` lines, for the app and for scripts
(`svc.sshd=run 1234`, `ts.state=Running`, `bat.level=78`, …). This is what the
server app polls.

## Lifecycle

| Command | Effect |
|---|---|
| `kabuk start` | firewall → Tailscale route rule → mounts → `share/` sync → `runsvdir`, then creates the persistent tmux session |
| `kabuk stop` | `sv force-stop` on every service, `SIGHUP` to `runsvdir`; `kabuk stop all` also kills tmux and force-unmounts |
| `kabuk restart` | stop, then start |
| `kabuk enable` | removes `/data/kabuk/DISABLED` and starts |
| `kabuk disable` | **emergency stop**: creates `/data/kabuk/DISABLED`, so the watchdog exits and the next boot does nothing. Work already running in tmux continues locally |
| `kabuk sv <cmd> [service]` | passes the command to `sv` (`status`, `restart`, `up`, `down`, …) |
| `kabuk watchdog` | runs the watchdog loop in the foreground (single instance, `flock`) |

## Maintenance

### `kabuk install`

First-time setup and repair. Requires `KABUK_TARBALL` (path to
`alpine-minirootfs-*.tar.gz`) and optionally `KABUK_SHA256` when the rootfs is
not there yet. Everything is idempotent: running it again updates packages and
re-syncs `share/`.

### `kabuk deploy <directory>`

Installs a runtime tree that has `bin/`:

* `bin/*` → `/data/kabuk/bin/` (mode 755)
* `magisk/*` → `/data/adb/modules/kabuk-sunucu/`, clearing `remove`/`disable`
* `sv/`, `rootfs/`, `tests/` → `/data/kabuk/share/`
* `etc/kabuk.conf` → only if no config exists yet

### `kabuk backup` / `kabuk restore <file>`

`backup` bind-mounts `rootfs/` to a staging directory (so nested mounts are not
included), writes `backups/rootfs-YYYYmmdd-HHMMSS.tar.gz` with mode 600 and
keeps the three newest. `restore` stops everything, moves the current rootfs to
`rootfs.old-<timestamp>`, unpacks the archive and rolls back automatically if
unpacking fails.

### `kabuk tweaks`

Android settings that must survive a reboot: disables the phantom-process
killer, whitelists the server app from Doze/MIUI restrictions, allows background
execution, and sets `wifi_sleep_policy=2`. `kabuk-boot` calls it on every boot.

### `kabuk health`

Appends one CSV row and prints it:

```
time,level,level_c,cpu_c,status,input_suspend,wakelock,runsvdir,services_up,load1,mem_avail_mb,network
```

## Network and access

### `kabuk ts <arguments…>`

Runs the `tailscale` CLI inside the chroot against
`/run/tailscale/tailscaled.sock`, e.g. `kabuk ts status`, `kabuk ts netcheck`.

### `kabuk ts-up`

Brings the tailnet up with the project's flags
(`--hostname=… --accept-dns=false --netfilter-mode=off --accept-routes=false`).
If login is required it prints the `login.tailscale.com` URL and sends it as a
notification.

### `kabuk authkey <public key>`

Appends an `ssh-ed25519`, `ecdsa-sha2-nistp256` or `sk-*` key to
`/home/dev/.ssh/authorized_keys` with correct ownership and mode 600. Other
key types are rejected.

### `kabuk fw <on|off|status>`

`on` installs the `KABUK_IN` chain on `iptables` and `ip6tables` for `wlan+` and
`eth+`; `off` removes it; `status` prints the rules. The watchdog re-installs it
when `FIREWALL=1` and the chain is missing.

### `kabuk notify <text>`

Posts an Android notification through `cmd notification post` and, if the
chroot is mounted, forwards the same text to ntfy via `kabuk-notify`.

## Helpers inside the chroot

| Command | Purpose |
|---|---|
| `kabuk-session [name]` | attach to (or create) the persistent tmux session, default `claude` |
| `kabuk-notify <event>` | notification helper used by Claude Code hooks |
| `kabuk-tsip [4\|6]` | prints the `tailscale0` address, empty + exit 1 if absent |
| `mosh-server` | wrapper that forces `-i <tailnet ip>` so mosh never listens on the LAN |
| `android <command>` | runs a command on the Android side as root (needs `FULL_ROOT=1`) |
| `kabuk-plugins-setup <preset>` | installs one of the agent presets (`hafif`, `gelistirme`, `tasarim`, `tam`) |

## Android-side helper

`/data/kabuk/bin/kabuk-servermode` turns the phone into a dedicated server and
is fully reversible:

```sh
su -c '/data/kabuk/bin/kabuk-servermode on'                # disable ~60 non-server packages
su -c '/data/kabuk/bin/kabuk-servermode extra com.foo.bar' # disable more (with consent)
su -c '/data/kabuk/bin/kabuk-servermode status'
su -c '/data/kabuk/bin/kabuk-servermode off'               # restore everything
```

Packages are disabled with `pm disable-user` (data is preserved), settings are
recorded before being changed, and third-party apps get
`RUN_ANY_IN_BACKGROUND ignore` so they cannot compete with the server for RAM.
