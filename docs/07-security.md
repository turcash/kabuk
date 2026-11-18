# Security

## The premise

This phone runs Android 12 with a security patch from April 2022. It no longer
receives updates. Any service reachable from an untrusted network is a liability
we cannot patch.

The design answer is simple and absolute:

> **No port is opened to the LAN or to the internet. Everything is reachable
> only from inside the tailnet.**

That single rule removes the entire class of "unauthenticated internet-facing
daemon" problems that usually sink projects like this.

## Threat model

| Adversary | In scope? | Mitigation |
|---|---|---|
| Someone on the same Wi-Fi | Yes | `KABUK_IN` firewall drops new connections on `wlan+`/`eth+` |
| Someone on the internet | Yes | No inbound port; carrier CGNAT; Tailscale only |
| A stolen client device | Yes | Revoke the node in the Tailscale admin console; SSH keys stay on the phone |
| Malicious code run by Claude Code or a build | Partly | The chroot is not a sandbox (see below); `FULL_ROOT=0` keeps Android off-limits |
| A hostile webpage in the agent UI | Yes | Requests are authorised by `tailscale whois`; the UI is static and same-origin |
| Physical access to the phone | No | Out of scope: an unlocked, rooted phone is owned by whoever holds it |

## Network controls

### LAN firewall

Two chains, IPv4 and IPv6:

```
KABUK_IN:
  -m state --state ESTABLISHED,RELATED        RETURN
  -p icmp / -p ipv6-icmp                      RETURN   (diagnostics, NDP)
  -p udp --sport 67 --dport 68                RETURN   (DHCP client)
  -p udp --sport 547 --dport 546              RETURN   (DHCPv6 client)
  -p udp --dport 41641                        RETURN   (Tailscale direct)
  -p udp --dport 5353                         RETURN   (mDNS)
  -p tcp --dport $FW_ALLOW_TCP                RETURN   (empty by default)
  -p tcp                                      DROP
  -p udp                                      DROP
```

It is inserted at the top of `INPUT` for `wlan+` and `eth+` only. Mobile data
(`rmnet*`) is intentionally untouched so IMS/VoLTE keeps working; the carrier
already drops inbound connections there.

`FIREWALL=1` in `kabuk.conf` makes the watchdog re-install the chain whenever it
is missing — a factory reset of iptables cannot silently reopen the phone.

### Services bind the interface, not `0.0.0.0`

Every `run` script resolves `tailscale0` (`kabuk-tsip`) and binds that address
explicitly. The firewall is the second line of defence, not the first. If a
service cannot resolve the tailnet address it waits, it does not fall back to a
wildcard bind.

### sshd

```sshconfig
PasswordAuthentication no
KbdInteractiveAuthentication no
PermitRootLogin no
AllowUsers dev
PubkeyAuthentication yes
```

Only `ssh-ed25519`, `ecdsa-sha2-nistp256` and `sk-*` keys are accepted by
`kabuk authkey`; the key file is mode 600 and the directory 700, which
`kabuk test` re-checks.

### Tailscale

* `--netfilter-mode=off`: Tailscale never edits Android's iptables.
* `--accept-routes=false`: the phone does not become a router for the tailnet.
* `--accept-dns=false`: no resolver hijacking.
* The tailnet ACL should only allow your own devices; Kabuk assumes a
  single-owner tailnet.

## What the chroot is and is not

The chroot is a **packaging boundary**, not a security boundary.

* It cleanly separates Alpine from Android: no library conflicts, no accidental
  writes to `/system`, package management that just works.
* It does **not** confine a process that is already root inside it. A root
  process in the chroot can `chroot /proc/1/root` and be on Android. That is
  exactly how the `android` helper works.

So the security question is not "can the chroot escape" but "who is root
inside it":

* `FULL_ROOT=0` (default): the user `dev` (uid 8000) has no sudo, and no
  process inside the chroot runs as Android root except the kernel-shared ones.
  The `/usr/bin` suid bind is not performed.
* `FULL_ROOT=1`: `dev` gets `NOPASSWD: ALL` plus the `android` command. This is
  a deliberate, documented choice for a single-user device. Everything that runs
  as `dev` — including Claude Code and therefore any code it writes and
  executes — can then do anything root can do on the phone.

If you enable `FULL_ROOT=1`, tell Claude Code about it; `kabuk install` writes a
marked block into `~/.claude/CLAUDE.md` that says exactly that and asks it to
confirm destructive actions.

### SELinux

SELinux stays enforcing. The runtime runs in the Magisk context, which is why
Android tools (`pm`, `settings`, `cmd`, `dumpsys`) work from inside the chroot
when `FULL_ROOT=1`. Kabuk never calls `setenforce 0`.

## Credentials and secrets

| Secret | Location | Mode |
|---|---|---|
| SSH host keys | `/etc/ssh/ssh_host_*` in the rootfs | 600 |
| User keys | `/home/dev/.ssh/authorized_keys` | 600 (dir 700) |
| ttyd password | `/etc/kabuk/ttyd.cred` | 600 |
| Claude Code credentials | `~/.claude/.credentials.json` | 600 (dir 700) |
| Tailscale state | `/var/lib/tailscale/tailscaled.state` | 600 |
| Agent chats | `~/.kabuk-agent/` | 700 |

`kabuk test` verifies the 600/700 modes of `.claude` and `.ssh` on every run, so
a bad `cp` or a container-extracted archive cannot silently widen them.

`kabuk.conf` itself is set to mode 600 by `kabuk deploy`.

## The agent

`tailscale whois` is the authorisation mechanism, and it is checked on **every**
request, not just at login:

1. The server reads the peer address of the TCP connection (not a header).
2. It calls `tailscale whois --json <ip>` and compares the result with the
   tailnet owner (`tailscale status --json`).
3. Only the owner's devices pass.

Consequences:

* No token, no cookie, no session to steal.
* A device removed from the tailnet instantly loses access.
* The port can be published in this README without harm.

Two caveats worth stating plainly:

* The agent runs Claude Code with permission modes up to
  `bypassPermissions`. That mode is a loaded gun: it lets the model run
  arbitrary commands without confirmation. Use `plan`/`edit` unless you are
  watching, and prefer `FULL_ROOT=0`.
* Prompt injection is real. Anything the model reads (a web page, a README, an
  issue) can contain instructions. The `FULL_ROOT=0` default exists mostly to
  limit the blast radius of that.

## Emergency stop

```sh
su -c '/data/kabuk/bin/kabuk disable'
```

creates `/data/kabuk/DISABLED`. The watchdog exits at the end of its current
iteration, the next boot does nothing, and no new remote session can be
established. Already running tmux/Claude sessions keep working locally — the
point is to cut the network, not to kill your work. Undo with `kabuk enable`.

If even the shell is unreachable: reboot into safe mode or use Magisk's
"disable modules" from recovery — `service.sh` then never starts, and
`uninstall.sh` re-enables charging and releases the wakelock.

## Reporting

This is a personal, single-user project published for reference. If you find a
problem that affects other users of this code, open an issue; please do not
publish the phone's tailnet address or any key material.
