# Services

Everything long-running lives inside the chroot and is supervised by
[runit](https://smarden.org/runit/). The supervisor itself is
`runsvdir -P /etc/kabuk/service`, started by `kabuk start`.

## Why runit

* ~10 KB of RSS per service, no D-Bus, no Python, no systemd.
* Restart-on-crash is built in, with a configurable 1 s delay.
* `sv` gives `status`, `up`, `down`, `restart`, `force-stop` with no daemon to
  talk to beyond the filesystem.
* `svlogd` handles log rotation per service (1 MB × 5 files).

## Enabling and disabling

`SERVICES` in `/data/kabuk/etc/kabuk.conf` decides what runs:

```sh
SERVICES="tailscaled sshd taildrop ttyd ntfy kabuk-agent"
```

`sv_sync` creates a symlink in `/etc/kabuk/service/` for every name in the
list that has a directory in `/etc/kabuk/sv/`, and removes links for anything
left out. Then:

```sh
su -c '/data/kabuk/bin/kabuk sv status'
su -c '/data/kabuk/bin/kabuk restart'
```

## The bundled services

### tailscaled

The Tailscale daemon. `TS_MODE=tun` (default) creates a real `tailscale0`
interface; `TS_MODE=userspace` falls back to userspace networking when the
kernel has no TUN. It runs with `--netfilter-mode=off` (set by `kabuk ts-up`) so
it never rewrites Android's iptables — the firewall is Kabuk's job.

State lives in `/var/lib/tailscale/tailscaled.state`, the socket in
`/run/tailscale/tailscaled.sock`.

### sshd

OpenSSH, key-only, `PermitRootLogin no`, `AllowUsers dev`. The listen address
is resolved at start time:

* `SSH_LISTEN` empty → wait for the `tailscale0` address and listen on it
  (IPv4 and, if present, IPv6), so sshd never listens before the tailnet is up.
* `SSH_LISTEN` set → listen exactly there (used as `127.0.0.1` in userspace
  mode).

Host keys are generated on first start with `ssh-keygen -A`.

### taildrop

Runs `tailscale file get --wait` in a loop into `/home/dev/Taildrop` (mode
700), so `tailscale file cp <file> note10:` from any of your devices just lands
in that directory.

### ttyd

A browser terminal on the tailnet interface only, protected by basic auth from
`/etc/kabuk/ttyd.cred` (generated once, mode 600) and attached to
`kabuk-session` (the `claude` tmux session).

```
http://note10:7681        (credentials: cat /data/kabuk/rootfs/etc/kabuk/ttyd.cred)
```

In userspace mode it binds `lo` instead, so you reach it through an SSH tunnel.

### ntfy

A self-hosted notification server on the tailnet address, port 8090, with a
48 h cache. `kabuk notify` and the Claude Code hooks push to it, and the client
app subscribes. Nothing leaves the tailnet.

### kabuk-agent

The Node chat server described in [08-agent.md](08-agent.md). It binds the
tailnet address on port 8787, runs as `dev` with the
`aid_inet`/`aid_net_raw` supplementary groups (Android's "paranoid network"
requires them to open sockets), and drops to normal OOM priority so it is not
killed before the user's builds.

## Environment handed to services

`sv_sync` writes `/etc/kabuk/env` before the supervisor starts:

```sh
TS_MODE=tun
SSH_LISTEN=
TTYD_PORT=7681
```

Every `run` script starts with `. /etc/kabuk/env`, then either binds
`tailscale0` or falls back to `127.0.0.1`. This is the single reason no service
can accidentally be reachable from the LAN: the address is resolved from the
interface, not from a wildcard bind.

## Logs

```
/data/kabuk/rootfs/var/log/kabuk/<service>/current
```

Each service runs `svlogd -tt` with a 1 MB × 5 rotation policy. The supervisor's
own output and the boot sequence go to `/data/kabuk/log/boot.log`, rotated at
1 MB (3 files).

## Adding your own service

1. Create `/data/kabuk/share/sv/myservice/run`:

   ```sh
   #!/bin/sh
   exec 2>&1
   . /etc/kabuk/env
   cd /home/dev/projeler/api
   exec /usr/bin/chpst -u dev:dev:aid_inet node server.js
   ```

2. Add `myservice` to `SERVICES` in `kabuk.conf`.
3. `su -c '/data/kabuk/bin/kabuk restart'`

`sv_sync` copies the `run` file in, creates the `log/run` symlink to `svlogd`
and the log directory. Use `chpst -u user` (from `runit`) to drop privileges —
`su -c` starts a new session and does not play well with supervision.

Services that need a port must bind the tailnet address, never `0.0.0.0`:
the LAN firewall is a safety net, not a substitute for binding correctly.
