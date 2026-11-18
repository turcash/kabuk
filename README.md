# Kabuk

**Turn a rooted Android phone into an always-on Claude Code server — and reach it from anywhere over Tailscale.**

No cloud, no relay, no open ports. The phone is the server: an Alpine chroot runs
OpenSSH, mosh, tmux, a web terminal, notifications and Claude Code; a Magisk-
started watchdog keeps it charged, cool and online; an Android client gives you
a real terminal without Termux or root.

> 🇹🇷 [Türkçe](README.tr.md) · 🇨🇳 [简体中文](README.zh-CN.md) · 🇷🇺 [Русский](README.ru.md) · 🇩🇪 [Deutsch](README.de.md) · 🇵🇹 [Português (BR)](README.pt-BR.md) · 🇮🇳 [हिन्दी](README.hi.md) · 🇮🇩 [Bahasa Indonesia](README.id.md) · 🇪🇸 [Español](README.es.md) · 🇻🇳 [Tiếng Việt](README.vi.md) · 🇸🇦 [العربية](README.ar.md) · 🇫🇷 [Français](README.fr.md)

## What it actually is

| Layer | What runs |
|---|---|
| Phone (Android 12, Magisk) | `kabuk` CLI, mount manager, watchdog, LAN firewall, charge limiter |
| chroot (Alpine 3.24, aarch64) | `tailscaled`, `sshd`, `taildrop`, `ttyd`, `ntfy`, `kabuk-agent`, Claude Code |
| Client | Android terminal app (mosh/ssh, multi-tab) + browser UIs |

Reference device: **Xiaomi Redmi Note 10 5G** (`camellia`, Dimensity 700,
4 GB RAM), Android 12 / MIUI 13, Magisk 31.0. It has been running as a daily
development server in this configuration.

## Status

| | |
|---|---|
| Device | Redmi Note 10 5G — M2103K19C, `camellia` |
| Root | Magisk 31.0, stock boot image, SELinux enforcing |
| Runtime | Alpine 3.24.4 aarch64, real `chroot`, user `dev` (uid 8000) |
| Supervisor | runit (`runsvdir`), 6 services |
| Network | Tailscale only — LAN firewall drops every new connection on `wlan+`/`eth+` |
| Power | charging held at 75–80 %, thermal capping at 40 °C |
| Access | ssh, mosh, sftp, taildrop, web terminal (7681), agent UI (8787) |
| Docs | 12 chapters, 12 languages |

## Features

* **Real chroot, not emulation.** `git`, `npm`, `tmux`, PTYs, `rm -rf .git` — all
  behave like on a normal Linux box.
* **Survives reboots, network changes and closing the lid.** Magisk starts the
  runtime before you unlock the screen; mosh keeps sessions alive when the
  phone hops between Wi-Fi and LTE; the watchdog repairs the Tailscale routing
  rule within 30 seconds.
* **One chat UI for Claude Code.** `kabuk-agent` spawns `claude` per chat, streams
  events over SSE, persists them to disk so a reconnect loses nothing, and
  resumes the session on the next message.
* **Battery-aware.** Charge window, wakelock only when work is running,
  cpuset + nice above 40 °C, Wi-Fi reset if the network is gone.
* **Nothing exposed.** Every service binds the tailnet interface; the LAN
  firewall is the second line of defence; the agent authorises each request
  with `tailscale whois`.
* **Emergency stop.** `kabuk disable` cuts all remote access while keeping your
  running work intact.

## Quick start

```sh
# on the phone, once
# 1. install Kabuk Server, grant root, tap "Kur / Onar"
# 2. log the node into your tailnet
su -c '/data/kabuk/bin/kabuk ts-up'
# 3. add your public key
su -c '/data/kabuk/bin/kabuk authkey "ssh-ed25519 AAAA... you@laptop"'
# 4. verify
su -c '/data/kabuk/bin/kabuk test'
```

```sh
# from your laptop
ssh dev@note10 -t kabuk-session     # persistent tmux + Claude Code
mosh dev@note10 -- kabuk-session    # same, survives network changes
open http://note10:8787               # chat UI for Claude Code
```

Full instructions: [docs/03-install.md](docs/03-install.md).

## CLI at a glance

```text
kabuk enter [user]      shell inside the chroot (cleanup on exit)
kabuk run <user> <cmd>  run one command inside the chroot
kabuk status            services, battery, mounts, wakelock, backups
kabuk install           first-time setup / repair
kabuk deploy <dir>      ship a runtime tree to the phone
kabuk backup|restore    snapshot / roll back the rootfs
kabuk test              ~20 regression checks
kabuk start|stop|restart|enable|disable
kabuk sv <cmd> [svc]    control runit services
kabuk ts-up|ts          Tailscale login / CLI
kabuk authkey <key>     add an SSH public key
kabuk fw on|off|status  the LAN firewall
kabuk notify <text>     Android notification + ntfy
kabuk servermode on|off disable non-server Android apps (reversible)
```

## Repository layout

```text
runtime/         the tree that is deployed to /data/kabuk
  bin/           kabuk CLI, boot, enter, servermode
  etc/           kabuk.conf
  magisk/        module.prop, service.sh, uninstall.sh
  rootfs/        chroot helpers + the agent (server.js, index.html, presets.json)
  sv/            runit service definitions
  tests/         stage2.sh
docs/            01 architecture … 12 design decisions
```

## Documentation

| # | Chapter |
|---|---|
| 01 | [Architecture](docs/01-architecture.md) |
| 02 | [Requirements](docs/02-requirements.md) |
| 03 | [Installation](docs/03-install.md) |
| 04 | [CLI reference](docs/04-cli-reference.md) |
| 05 | [Services](docs/05-services.md) |
| 06 | [Remote access](docs/06-remote-access.md) |
| 07 | [Security](docs/07-security.md) |
| 08 | [Kabuk Agent](docs/08-agent.md) |
| 09 | [Kabuk Client](docs/09-client-app.md) |
| 10 | [Kabuk Server](docs/10-server-app.md) |
| 11 | [Troubleshooting](docs/11-troubleshooting.md) |
| 12 | [Design decisions](docs/12-design-decisions.md) |

## Security in one paragraph

The phone has not received a security update since April 2022, so it must not
be reachable from untrusted networks. Nothing is: no inbound port on Wi-Fi,
Ethernet or mobile data, no password authentication, no wildcard binds, no cloud
relay. Everything goes through your Tailscale tailnet, and the agent
re-authorises each request against the tailnet owner. The chroot is a packaging
boundary, not a sandbox — read [docs/07-security.md](docs/07-security.md) before
enabling `FULL_ROOT=1` or the agent's `full` permission mode.

## License

MIT — see [LICENSE](LICENSE). Third-party components (Termux terminal-view,
musl, mosh, OpenSSH, Alpine packages, Tailscale, ttyd, ntfy) are listed in
[NOTICE.md](NOTICE.md) and keep their own licenses.

Not affiliated with Google, Xiaomi, Magisk or Anthropic. Rooting voids your
warranty; a phone that runs 24/7 behind a charger is your responsibility.