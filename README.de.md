# Kabuk

**Mach aus einem gerooteten Android-Telefon einen dauerhaft laufenden Claude-Code-Server — und erreiche ihn von überall über Tailscale.**

Keine Cloud, kein Relay, keine offenen Ports. Der Server ist das Telefon selbst: In
einem Alpine-Chroot laufen OpenSSH, mosh, tmux, ein Web-Terminal, Benachrichtigungen
und Claude Code; ein von Magisk gestarteter Watchdog kümmert sich um Ladung,
Temperatur und Netz; ein Android-Client liefert ein echtes Terminal ohne Termux und
ohne Root.

> 🇬🇧 [English](README.md) · 🇹🇷 [Türkçe](README.tr.md) · 🇨🇳 [简体中文](README.zh-CN.md) · 🇷🇺 [Русский](README.ru.md) · 🇵🇹 [Português (BR)](README.pt-BR.md) · 🇮🇳 [हिन्दी](README.hi.md) · 🇮🇩 [Bahasa Indonesia](README.id.md) · 🇪🇸 [Español](README.es.md) · 🇻🇳 [Tiếng Việt](README.vi.md) · 🇸🇦 [العربية](README.ar.md) · 🇫🇷 [Français](README.fr.md)

## Aufbau

| Ebene | Was läuft |
|---|---|
| Telefon (Android 12, Magisk) | `kabuk`-CLI, Mount-Verwaltung, Watchdog, LAN-Firewall, Ladelimit |
| chroot (Alpine 3.24, aarch64) | `tailscaled`, `sshd`, `taildrop`, `ttyd`, `ntfy`, `kabuk-agent`, Claude Code |
| Client | Android-Terminal (mosh/ssh, mehrere Tabs) + Browser-Oberflächen |

Referenzgerät: **Xiaomi Redmi Note 10 5G** (`camellia`, Dimensity 700, 4 GB RAM),
Android 12 / MIUI 13, Magisk 31.0 — in dieser Konfiguration als täglicher
Entwicklungsserver im Einsatz.

## Status

| | |
|---|---|
| Gerät | Redmi Note 10 5G — M2103K19C, `camellia` |
| Root | Magisk 31.0, Stock-Boot, SELinux enforcing |
| Laufzeit | Alpine 3.24.4 aarch64, echtes `chroot`, Benutzer `dev` (uid 8000) |
| Supervisor | runit (`runsvdir`), 6 Dienste |
| Netz | nur Tailscale — die LAN-Firewall verwirft neue Verbindungen auf `wlan+`/`eth+` |
| Energie | Ladung zwischen 75–80 %, Drosselung ab 40 °C |
| Zugriff | ssh, mosh, sftp, taildrop, Web-Terminal (7681), Agent-UI (8787) |
| Doku | 12 Kapitel, 12 Sprachen |

## Funktionen

* **Echtes chroot, keine Emulation.** `git`, `npm`, `tmux`, PTYs und `rm -rf .git` verhalten sich wie auf einem normalen Linux-Rechner.
* **Übersteht Neustart, Netzwechsel und geschlossenen Deckel.** Magisk startet die Laufzeit, bevor der Bildschirm entsperrt ist; mosh hält Sitzungen beim Wechsel zwischen WLAN und LTE; der Watchdog repariert die Tailscale-Route binnen 30 Sekunden.
* **Eine Chat-Oberfläche für Claude Code.** `kabuk-agent` startet `claude` pro Chat, streamt Ereignisse per SSE und schreibt sie zuerst auf die Platte — ein Reconnect verliert nichts, die nächste Nachricht setzt mit `--resume` fort.
* **Akkuschonend.** Ladefenster, Wakelock nur bei Arbeit, cpuset + nice über 40 °C, WLAN-Reset bei Netzausfall.
* **Nichts ist exponiert.** Alle Dienste binden die Tailnet-Schnittstelle; die LAN-Firewall ist die zweite Linie; der Agent prüft jede Anfrage mit `tailscale whois`.
* **Not-Aus.** `kabuk disable` kappt jeden Fernzugriff, laufende Arbeit bleibt erhalten.

## Schnellstart

```sh
# auf dem Telefon, einmalig
# 1. Kabuk Server installieren, Root erlauben, "Kur / Onar" tippen
# 2. Knoten im Tailnet anmelden
su -c '/data/kabuk/bin/kabuk ts-up'
# 3. öffentlichen Schlüssel hinzufügen
su -c '/data/kabuk/bin/kabuk authkey "ssh-ed25519 AAAA... du@laptop"'
# 4. prüfen
su -c '/data/kabuk/bin/kabuk test'
```

```sh
# vom Laptop
ssh dev@note10 -t kabuk-session     # persistente tmux-Sitzung + Claude Code
mosh dev@note10 -- kabuk-session    # dasselbe, übersteht Netzwechsel
open http://note10:8787               # Chat-Oberfläche für Claude Code
```

Vollständige Anleitung: [docs/03-install.md](docs/03-install.md).

## Repository

```text
runtime/         der Baum, der nach /data/kabuk deployed wird
  bin/           kabuk-CLI, boot, enter, servermode
  etc/           kabuk.conf
  magisk/        module.prop, service.sh, uninstall.sh
  rootfs/        chroot-Helfer + Agent (server.js, index.html, presets.json)
  sv/            runit-Definitionen
  tests/         stage2.sh
apps/
  client/        recovered Java (client)
  server/        recovered Java (server)
  prebuilt/      signed APK + SHA-256
docs/            01 Architektur … 12 Designentscheidungen
```

## Dokumentation

| # | Kapitel |
|---|---|
| 01 | [Architektur](docs/01-architecture.md) |
| 02 | [Voraussetzungen](docs/02-requirements.md) |
| 03 | [Installation](docs/03-install.md) |
| 04 | [CLI-Referenz](docs/04-cli-reference.md) |
| 05 | [Dienste](docs/05-services.md) |
| 06 | [Fernzugriff](docs/06-remote-access.md) |
| 07 | [Sicherheit](docs/07-security.md) |
| 08 | [Kabuk Agent](docs/08-agent.md) |
| 09 | [Kabuk Client](docs/09-client-app.md) |
| 10 | [Kabuk Server](docs/10-server-app.md) |
| 11 | [Fehlersuche](docs/11-troubleshooting.md) |
| 12 | [Designentscheidungen](docs/12-design-decisions.md) |

## Sicherheit in einem Absatz

Das Telefon erhält seit April 2022 keine Sicherheitsupdates und darf deshalb aus
nicht vertrauenswürdigen Netzen nicht erreichbar sein. Es ist es auch nicht: kein
eingehender Port in WLAN, Ethernet oder Mobilfunk, keine Passwort-Anmeldung, keine
`0.0.0.0`-Bindungen, kein Cloud-Relay. Alles läuft über dein Tailscale-Tailnet, und
der Agent prüft jede Anfrage erneut gegen den Tailnet-Eigentümer. Das chroot ist eine
Paketgrenze, keine Sandbox — lies [docs/07-security.md](docs/07-security.md), bevor du
`FULL_ROOT=1` oder den `full`-Modus des Agenten aktivierst.

## Lizenz

MIT — siehe [LICENSE](LICENSE). Drittanbieter (Termux terminal-view, musl, mosh,
OpenSSH, Alpine-Pakete, Tailscale, ttyd, ntfy) sind in [NOTICE.md](NOTICE.md)
aufgeführt und behalten ihre Lizenzen.

Nicht verbunden mit Google, Xiaomi, Magisk oder Anthropic. Rooten kostet die
Garantie; ein 24/7 am Ladegerät laufendes Telefon liegt in deiner Verantwortung.