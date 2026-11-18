# Design decisions

Each entry: what was decided, why, and how it was verified. Written while
building the project on a Redmi Note 10 5G, November 2026.

## Device and base system

**Target is a Redmi Note 10 5G (`camellia`, M2103K19C), not a Samsung.**
MediaTek Dimensity 700, 4 GB RAM, Android 12 / MIUI 13, kernel 4.14.186,
security patch 2022-04-01, Magisk 31.0, SELinux enforcing.

*Verified:* `getprop ro.product.device` → `camellia`, `ro.boot.flash.locked=0`.

**Root is Magisk on the stock boot image.** No custom kernel: the runtime needs
nothing the stock kernel lacks (TUN, cgroups, iptables are all present).

**A broken hidden Magisk was replaced with the official build.** A repackaged
Magisk (`com.topjohnwu.magisk` v1.0) had lost the daemon, so `su` requests hung
forever. Installing the official v31.0 APK over it restored root and kept data.
Two Magisk forks cannot coexist — the other one was removed.

## The chroot

**Real `chroot` into Alpine 3.24.4 (aarch64, musl), not proot.**

* proot intercepts syscalls with `ptrace`, which costs 10–40 % on this SoC and
  breaks `tmux`, `git` and `npm` in subtle ways.
* A VM would need 1.5 GB of the 4 GB just to boot.
* A real chroot behaves like a normal Linux box: `rm -rf .git` works, PTYs work,
  `/proc` is the real one.

*Verified:* `rm -rf .git` inside the chroot, `tmux` with 50 000 lines of
history, `git clone` + `npm install` on a React project.

**`/data/kabuk` lives outside the rootfs.** Wiping or restoring the rootfs never
touches configuration, logs or backups, and `kabuk backup` only has to snapshot
one directory.

**Mounts are idempotent and reference-counted by inspection.** `do_mount` is
guarded by `flock` and checks `/proc/mounts` before every mount, so calling it
twice is free. `do_umount` first checks whether any process still has its root
in the chroot and does nothing if so — the alternative (unmount on exit) breaks
the second terminal.

**A private `/dev` is built per mount.** Binding Android's `/dev` leaks devices
and breaks `rm -rf .git` (Android's `/dev` has no `/dev/shm` of the right kind).
Kabuk mounts a 1 MB tmpfs and creates `null`, `zero`, `full`, `random`,
`urandom`, `tty`, `ptmx` with `mknod`, binds `/dev/pts`, adds a tmpfs
`/dev/shm`, and creates `/dev/net/tun` with `mknod c 10 200` — toybox
`mount --bind` mistakes a character device for a loop image.

**`FULL_ROOT` is off by default and is a single switch.** Turning it on adds
passwordless `sudo` and the `android` helper; turning it off removes both. Doing
it with one boolean (instead of "install sudo and hope") made the security
posture auditable: `kabuk test` asserts `! sudo -n true` when it is off.

## Supervision

**runit, not systemd or a hand-rolled loop.** 10 KB RSS per service, restart on
crash, `sv status` for the panel, `svlogd` for rotation. Verified by killing
`sshd` and `tailscaled` and watching them come back within a second.

**The watchdog is a separate process from the supervisor.** `runsvdir` only
knows about services; charging, thermals, wakelocks, the firewall and the
Tailscale routing rule are Android-side concerns and need a loop that can touch
`/sys`. The loop is `flock`ed so two copies cannot fight, and `kabuk-boot`
restarts it 10 seconds after it dies.

**Health is a CSV, not a metrics stack.** One row every 5 minutes, rotated at
2 MB. A Prometheus/Grafana stack on a 4 GB phone would cost more than the
workload it measures.

## Power

**Charging is capped with `input_suspend`.** `capacity` + `input_suspend` with
hysteresis 75/80 % keeps the pack at ~50 % average state of charge, which is
where Li-ion degrades slowest. `charge_control_limit` is a thermal level on
this platform, not a percentage, so it was rejected.

**Fail-safes everywhere.** `kabuk-boot` writes `input_suspend=0` before anything
else and again if the watchdog exits; the Magisk `uninstall.sh` does it too; a
`TERM` trap in the watchdog does it. A phone that cannot charge because a script
died is worse than a phone at 100 %.

**The wakelock is conditional.** `WAKELOCK=auto` holds it only while a
non-idle process of the service user exists or a charge window is open. A phone
that never sleeps is a phone that melts; a phone that sleeps mid-`npm install`
is a phone that lies about being a server.

**Thermal response is a cpuset, not a shutdown.** Above 40 °C the service
cgroup is restricted to the little cores and user processes are `nice`d. The
workload continues, slower. Only charging is cut at 45 °C.

## Network

**Everything through Tailscale; nothing else is exposed.** See
[07-security.md](07-security.md). This decision drove the rest: services bind
`tailscale0` explicitly, the LAN firewall exists as a second line, and `ttyd`
and `ntfy` are self-hosted inside the tailnet rather than using public services.

**`--netfilter-mode=off`, and Kabuk owns iptables.** Tailscale's default mode
rewrites Android's tables and fights MIUI's own rules. Kabuk clears that
responsibility and installs one small chain.

**A `fwmark` rule at preference 5240 fixes Android routing.** Tailscale marks
its own traffic `0x80000` and looks it up in `main`, where Android has no
default route. Copying Android's own 29000 rule into 5240 fixes it without
patching Tailscale. The watchdog recomputes it every 30 s, which also covers
Wi-Fi ⇄ LTE handovers.

**mosh is the default client transport.** The server moves between networks; a
TCP session dies with the network, mosh does not. `mosh-server` is wrapped to
force `-i <tailnet ip>` so the UDP port is never on the LAN.

**sshd reads its listen address from the interface, not from config.** Waiting
for `kabuk-tsip` before exec'ing sshd means there is no window where it listens
on a wildcard address. In userspace mode the address becomes `127.0.0.1`.

## Agent

**`tailscale whois` instead of a login system.** No token to leak, no password
to reuse, revocation is "remove the device from the tailnet". Checked on every
request with a 10-minute cache.

**Chats are event logs on disk.** `claude` runs detached and every parsed event
is appended to `chats/<id>.jsonl` before it is streamed. Closing the browser or
losing the connection therefore cannot lose output: the SSE endpoint replays
with `?after=N`. Verified by killing the browser mid-run and reconnecting.

**`--resume` per chat.** Without it every message would restart the model with
no context; with it, a chat is a real multi-turn session, and a reboot only
costs one turn.

**Presets instead of installing plugins per chat.** All plugins are installed
once and globally disabled; a chat enables its set with
`--settings '{"enabledPlugins":{…}}'`. Cheap chats stay at ~2 k tokens of prompt
instead of ~50 k. The trade-off is that the preset cost is paid on every
message, which is why the UI shows the token estimate next to each preset.

**One process, no npm dependencies.** `server.js` uses only Node built-ins. On a
phone, every dependency is both a supply-chain risk and a few hundred files to
keep updated for years on an unpatched system.

## Apps

**The client needs no root and no Termux.** It ships a musl loader, static
`ssh`/`mosh-client` and a `getpwnam` shim as JNI libraries. The alternative
(Termux + a shell script) would mean two apps and a user who has to learn a
package manager before typing a password.

**The SSH key lives in the Android Keystore.** Non-exportable, hardware-backed
where available, exposed through a local agent socket. `IdentityFile none` in
the generated `ssh_config` guarantees the key is never copied to disk.

**The server app is a control panel, not a daemon.** Duplicating the watchdog
inside an app would fight Magisk's supervisor for battery and lifecycle. The app
deploys, runs one command and shows output; the phone works with the app
force-stopped.

## Repository

**Documentation and code in English; READMEs in eleven more languages.** The
project is published for an international audience; the original working notes
were Turkish and were translated.

**The runtime is published as the real deployed tree.** `runtime/` mirrors what
`kabuk deploy` puts on the phone, including `sv/` and `rootfs/opt/kabuk-agent/`,
so the repository is the source of truth rather than an illustration of it.

**The Android app sources are not published.** An accidental case-insensitive
directory deletion removed the Gradle project and its `.git`. The built apps used
a personal package id, and a decompiled copy would carry it unchanged, so neither
the binaries nor the recovered classes are part of this release. Their design,
behaviour and layout are documented in [09-client-app.md](09-client-app.md) and
[10-server-app.md](10-server-app.md). The runtime, the agent and this
documentation are the original, current sources.

## What was rejected

| Idea | Why not |
|---|---|
| Termux as the server runtime | No real chroot, wake-lock policy and charging control belong to Android anyway |
| proot-distro | 10–40 % overhead and syscall emulation bugs on this SoC |
| Running sshd on a high port with port-forwarding | Still internet-facing, still unpatched |
| Cloudflare Tunnel / ngrok | Puts a third party in the path and needs a public endpoint |
| systemd-nspawn / LXC | Not available in an Android kernel |
| Prometheus + Grafana | Costs more RAM than the workload |
| Storing the SSH key as a file | Exportable, backup-able, leak-able |
