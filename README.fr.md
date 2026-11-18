# Kabuk

**Transformez un téléphone Android rooté en serveur Claude Code allumé 24 h/24 — et accédez-y de partout via Tailscale.**

Pas de cloud, pas de relais, pas de port ouvert. Le serveur, c'est le téléphone :
dans un chroot Alpine tournent OpenSSH, mosh, tmux, un terminal web, les
notifications et Claude Code ; un watchdog lancé par Magisk gère la charge, la
température et le réseau ; un client Android offre un vrai terminal sans Termux ni root.

> 🇬🇧 [English](README.md) · 🇹🇷 [Türkçe](README.tr.md) · 🇨🇳 [简体中文](README.zh-CN.md) · 🇷🇺 [Русский](README.ru.md) · 🇩🇪 [Deutsch](README.de.md) · 🇵🇹 [Português (BR)](README.pt-BR.md) · 🇮🇳 [हिन्दी](README.hi.md) · 🇮🇩 [Bahasa Indonesia](README.id.md) · 🇪🇸 [Español](README.es.md) · 🇻🇳 [Tiếng Việt](README.vi.md) · 🇸🇦 [العربية](README.ar.md)

## De quoi s'agit-il

| Couche | Ce qui tourne |
|---|---|
| Téléphone (Android 12, Magisk) | CLI `kabuk`, gestion des montages, watchdog, pare-feu LAN, limite de charge |
| chroot (Alpine 3.24, aarch64) | `tailscaled`, `sshd`, `taildrop`, `ttyd`, `ntfy`, `kabuk-agent`, Claude Code |
| Client | application Android de terminal (mosh/ssh, plusieurs onglets) + interfaces web |

Appareil de référence : **Xiaomi Redmi Note 10 5G** (`camellia`, Dimensity 700,
4 Go de RAM), Android 12 / MIUI 13, Magisk 31.0 — utilisé comme serveur de
développement quotidien dans cette configuration.

## État

| | |
|---|---|
| Appareil | Redmi Note 10 5G — M2103K19C, `camellia` |
| Root | Magisk 31.0, image boot d'origine, SELinux enforcing |
| Environnement | Alpine 3.24.4 aarch64, `chroot` réel, utilisateur `dev` (uid 8000) |
| Superviseur | runit (`runsvdir`), 6 services |
| Réseau | Tailscale uniquement — le pare-feu LAN rejette toute nouvelle connexion sur `wlan+`/`eth+` |
| Énergie | charge maintenue entre 75 et 80 %, bridage à 40 °C |
| Accès | ssh, mosh, sftp, taildrop, terminal web (7681), interface de l'agent (8787) |
| Documentation | 12 chapitres, 12 langues |

## Fonctionnalités

* **Un vrai chroot, pas de l'émulation.** `git`, `npm`, `tmux`, les PTY et `rm -rf .git` se comportent comme sur une machine Linux normale.
* **Survit aux redémarrages, aux changements de réseau et au verrouillage.** Magisk démarre l'environnement avant que vous déverrouilliez ; mosh garde la session lors du passage Wi-Fi ↔ LTE ; le watchdog répare la route Tailscale en 30 secondes.
* **Une seule interface de chat pour Claude Code.** `kabuk-agent` lance `claude` par conversation, diffuse les événements en SSE et les écrit d'abord sur disque : une reconnexion ne perd rien, et le message suivant reprend avec `--resume`.
* **Économe en batterie.** Fenêtre de charge, wakelock seulement pendant le travail, cpuset + nice au-delà de 40 °C, redémarrage du Wi-Fi si le réseau disparaît.
* **Rien d'exposé.** Tous les services écoutent sur l'interface du tailnet ; le pare-feu LAN est la seconde ligne ; l'agent revalide chaque requête avec `tailscale whois`.
* **Arrêt d'urgence.** `kabuk disable` coupe tout accès distant sans tuer le travail en cours.

## Démarrage rapide

```sh
# sur le téléphone, une fois
# 1. installez Kabuk Server, accordez le root, appuyez sur "Kur / Onar"
# 2. rattachez le nœud au tailnet
su -c '/data/kabuk/bin/kabuk ts-up'
# 3. ajoutez votre clé publique
su -c '/data/kabuk/bin/kabuk authkey "ssh-ed25519 AAAA... vous@portable"'
# 4. vérifiez
su -c '/data/kabuk/bin/kabuk test'
```

```sh
# depuis le portable
ssh dev@note10 -t kabuk-session     # session tmux persistante + Claude Code
mosh dev@note10 -- kabuk-session    # idem, sans coupure en changeant de réseau
open http://note10:8787               # interface de chat Claude Code
```

Guide complet : [docs/03-install.md](docs/03-install.md).

## Structure du dépôt

```text
runtime/         l'arborescence déployée dans /data/kabuk
  bin/           CLI kabuk, boot, enter, servermode
  etc/           kabuk.conf
  magisk/        module.prop, service.sh, uninstall.sh
  rootfs/        assistants du chroot + agent (server.js, index.html, presets.json)
  sv/            définitions de services runit
  tests/         stage2.sh
apps/
  client/        recovered Java (client)
  server/        recovered Java (server)
  prebuilt/      signed APK + SHA-256
docs/            01 architecture … 12 décisions de conception
```

## Documentation

| # | Chapitre |
|---|---|
| 01 | [Architecture](docs/01-architecture.md) |
| 02 | [Prérequis](docs/02-requirements.md) |
| 03 | [Installation](docs/03-install.md) |
| 04 | [Référence CLI](docs/04-cli-reference.md) |
| 05 | [Services](docs/05-services.md) |
| 06 | [Accès distant](docs/06-remote-access.md) |
| 07 | [Sécurité](docs/07-security.md) |
| 08 | [Kabuk Agent](docs/08-agent.md) |
| 09 | [Kabuk Client](docs/09-client-app.md) |
| 10 | [Kabuk Server](docs/10-server-app.md) |
| 11 | [Dépannage](docs/11-troubleshooting.md) |
| 12 | [Décisions de conception](docs/12-design-decisions.md) |

## La sécurité en un paragraphe

Le téléphone ne reçoit plus de mises à jour de sécurité depuis avril 2022 : il ne doit
donc pas être joignable depuis un réseau non fiable. Il ne l'est pas : aucun port
entrant en Wi-Fi, Ethernet ou données mobiles, aucune authentification par mot de passe,
aucun bind sur `0.0.0.0`, aucun relais cloud. Tout passe par votre tailnet Tailscale, et
l'agent revalide chaque requête face au propriétaire du tailnet. Le chroot est une
frontière de packaging, pas une sandbox — lisez [docs/07-security.md](docs/07-security.md)
avant d'activer `FULL_ROOT=1` ou le mode `full` de l'agent.

## Licence

MIT — voir [LICENSE](LICENSE). Les composants tiers (Termux terminal-view, musl, mosh,
OpenSSH, paquets Alpine, Tailscale, ttyd, ntfy) sont listés dans [NOTICE.md](NOTICE.md)
et conservent leurs licences.

Sans lien avec Google, Xiaomi, Magisk ou Anthropic. Le root annule la garantie ; un
téléphone allumé 24 h/24 sur secteur est votre responsabilité.