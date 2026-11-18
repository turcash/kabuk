# Remote access

All remote access goes through one WireGuard mesh: Tailscale. There is no
port forward, no dynamic DNS, no tunnel service and no relay in the middle.
If a device is not in your tailnet, it cannot reach the phone at all.

## Tailscale

### First login

```sh
su -c '/data/kabuk/bin/kabuk ts-up'
```

`ts-up` runs:

```sh
tailscale up --hostname=note10 --accept-dns=false --netfilter-mode=off --accept-routes=false
```

If authentication is needed, the login URL is printed and also sent as an
Android notification. Open it once on any device and approve.

`--accept-dns=false` is deliberate: the phone keeps its own resolver, so
restarting `tailscaled` never breaks DNS for the rest of the system.

### The Android routing problem

This is the one non-obvious piece of the project and the reason Tailscale works
reliably across Wi-Fi/mobile handovers on this phone.

`tailscaled` marks its own sockets with `fwmark 0x80000` and then looks the mark
up in the routing rules. On a normal Linux host it finds the default route. On
Android the default route is not in the `main` table; each network gets its own
table (`wlan0`, `rmnet_data1`, …) and Android points at the right one with a
rule at preference 29000 (`iif lo lookup <table>`).

So Kabuk adds a rule at preference 5240:

```
5240: from all fwmark 0x80000/0xff0000 lookup <current default table>
```

`ts_route_sync` recomputes `<current default table>` from the existing 29000
rule. The watchdog calls it every 30 seconds, which means a switch from Wi-Fi to
mobile data is repaired within half a minute, without touching the interface or
restarting the daemon.

### Userspace fallback

If the kernel has no TUN (or `mknod /dev/net/tun` fails), set:

```sh
TS_MODE=userspace
```

in `/data/kabuk/etc/kabuk.conf` and `kabuk restart`. Then sshd, ttyd, ntfy and
the agent bind `127.0.0.1`, and you reach them through an SSH tunnel. `mosh`
does **not** work in this mode, because it needs a directly reachable UDP
endpoint.

## From a laptop

```sh
ssh dev@note10                      # plain shell
ssh dev@note10 -t kabuk-session     # attach/create the 'claude' tmux session
mosh dev@note10 -- kabuk-session    # survives IP changes and sleep
sftp dev@note10                     # file transfer
rsync -av ./proje/ dev@note10:projeler/proje/
```

`note10` resolves through MagicDNS. The full name
`note10.<your-tailnet>.ts.net` always works too.

### Why mosh is the default

The phone changes networks (Wi-Fi → LTE → another Wi-Fi) and the carrier drops
idle connections. SSH dies with it; mosh keeps the session and re-syncs the
screen, so an interactive Claude Code session survives a walk to the kitchen.

The bundled `mosh-server` wrapper forces `-i <tailnet ip>` so the UDP port is
never opened on the LAN interface.

## File transfer

Three options, in order of convenience:

| Method | Command |
|---|---|
| Taildrop | `tailscale file cp rapor.pdf note10:` → lands in `~/Taildrop` |
| rsync over ssh | `rsync -av --delete ./site/ dev@note10:projeler/site/` |
| sftp | `sftp dev@note10` (internal-sftp subsystem) |

## Browser terminal

`http://note10:7681` gives a terminal in any browser, protected by basic auth:

```sh
su -c 'cat /data/kabuk/rootfs/etc/kabuk/ttyd.cred'
```

It attaches to the same tmux session as ssh and mosh, so all three can watch the
same Claude Code run.

## Agent UI

`http://note10:8787` is the chat interface for Claude Code. Every request is
authenticated with `tailscale whois`, so only the tailnet owner's devices can
use it — there is no password to leak. See [08-agent.md](08-agent.md).

## android side access

`adb` over USB is independent of the tailnet and is the escape hatch if the
tailnet is broken. Wireless adb can be allowed through the LAN firewall if you
really need it:

```sh
FW_ALLOW_TCP="5555"
su -c '/data/kabuk/bin/kabuk restart'
```

## Notifications

The phone pushes to itself and to your other devices over ntfy:

```sh
su -c '/data/kabuk/bin/kabuk notify "yedek alindi"'
```

Claude Code hooks call `kabuk-notify` on `Notification` and `Stop`, so you get a
ping when a long task needs permission or finishes.

## What is *not* exposed

* No inbound port on Wi-Fi or Ethernet (firewall drops new connections).
* No inbound port on mobile data (carrier CGNAT plus no firewall exception).
* No root SSH, no password authentication.
* No service bound to `0.0.0.0`.
* No cloud relay: the agent, ntfy and ttyd all live on the phone.

The single exception is UDP/41641, which Tailscale needs for direct peer
connections. If it is blocked, Tailscale falls back to DERP relays
automatically; nothing breaks, traffic is just slower.
