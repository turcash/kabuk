# Kabuk

**Convierte un teléfono Android rooteado en un servidor de Claude Code encendido 24/7 — y accede a él desde cualquier lugar mediante Tailscale.**

Sin nube, sin relé, sin puertos abiertos. El servidor es el propio teléfono: dentro de
un chroot Alpine corren OpenSSH, mosh, tmux, una terminal web, notificaciones y Claude
Code; un watchdog iniciado por Magisk gestiona carga, temperatura y red; un cliente
Android ofrece una terminal real sin Termux y sin root.

> 🇬🇧 [English](README.md) · 🇹🇷 [Türkçe](README.tr.md) · 🇨🇳 [简体中文](README.zh-CN.md) · 🇷🇺 [Русский](README.ru.md) · 🇩🇪 [Deutsch](README.de.md) · 🇵🇹 [Português (BR)](README.pt-BR.md) · 🇮🇳 [हिन्दी](README.hi.md) · 🇮🇩 [Bahasa Indonesia](README.id.md) · 🇻🇳 [Tiếng Việt](README.vi.md) · 🇸🇦 [العربية](README.ar.md) · 🇫🇷 [Français](README.fr.md)

## Qué hay dentro

| Capa | Qué se ejecuta |
|---|---|
| Teléfono (Android 12, Magisk) | CLI `kabuk`, gestor de montajes, watchdog, cortafuegos LAN, límite de carga |
| chroot (Alpine 3.24, aarch64) | `tailscaled`, `sshd`, `taildrop`, `ttyd`, `ntfy`, `kabuk-agent`, Claude Code |
| Cliente | app Android de terminal (mosh/ssh, varias pestañas) + interfaces web |

Dispositivo de referencia: **Xiaomi Redmi Note 10 5G** (`camellia`, Dimensity 700,
4 GB de RAM), Android 12 / MIUI 13, Magisk 31.0, usado como servidor de desarrollo
diario con esta configuración.

## Estado

| | |
|---|---|
| Dispositivo | Redmi Note 10 5G — M2103K19C, `camellia` |
| Root | Magisk 31.0, imagen boot original, SELinux enforcing |
| Entorno | Alpine 3.24.4 aarch64, `chroot` real, usuario `dev` (uid 8000) |
| Supervisor | runit (`runsvdir`), 6 servicios |
| Red | solo Tailscale — el cortafuegos LAN descarta conexiones nuevas en `wlan+`/`eth+` |
| Energía | carga mantenida entre 75–80 %, limitación a partir de 40 °C |
| Acceso | ssh, mosh, sftp, taildrop, terminal web (7681), UI del agente (8787) |
| Documentación | 12 capítulos, 12 idiomas |

## Características

* **chroot real, no emulación.** `git`, `npm`, `tmux`, las PTY y `rm -rf .git` se comportan como en un Linux normal.
* **Sobrevive a reinicios, cambios de red y a la pantalla bloqueada.** Magisk arranca el entorno antes de que desbloquees; mosh mantiene la sesión al pasar de Wi-Fi a LTE; el watchdog repara la ruta de Tailscale en 30 segundos.
* **Una sola interfaz de chat para Claude Code.** `kabuk-agent` lanza `claude` por conversación, emite eventos por SSE y los guarda en disco antes de enviarlos: reconectar no pierde nada y el siguiente mensaje continúa con `--resume`.
* **Pensado para la batería.** Ventana de carga, wakelock solo cuando hay trabajo, cpuset + nice por encima de 40 °C, reinicio del Wi-Fi si se cae la red.
* **Nada expuesto.** Todos los servicios escuchan en la interfaz del tailnet; el cortafuegos LAN es la segunda línea; el agente valida cada petición con `tailscale whois`.
* **Parada de emergencia.** `kabuk disable` corta todo acceso remoto sin matar el trabajo en curso.

## Inicio rápido

```sh
# en el teléfono, una vez
# 1. instala Kabuk Server, concede root, pulsa "Kur / Onar"
# 2. une el nodo al tailnet
su -c '/data/kabuk/bin/kabuk ts-up'
# 3. añade tu clave pública
su -c '/data/kabuk/bin/kabuk authkey "ssh-ed25519 AAAA... tu@portatil"'
# 4. verifica
su -c '/data/kabuk/bin/kabuk test'
```

```sh
# desde el portátil
ssh dev@note10 -t kabuk-session     # sesión tmux persistente + Claude Code
mosh dev@note10 -- kabuk-session    # lo mismo, sin caerse al cambiar de red
open http://note10:8787               # interfaz de chat de Claude Code
```

Guía completa: [docs/03-install.md](docs/03-install.md).

## Estructura del repositorio

```text
runtime/         el árbol que se despliega en /data/kabuk
  bin/           CLI kabuk, boot, enter, servermode
  etc/           kabuk.conf
  magisk/        module.prop, service.sh, uninstall.sh
  rootfs/        ayudantes del chroot + agente (server.js, index.html, presets.json)
  sv/            definiciones de servicio runit
  tests/         stage2.sh
apps/
  client/        recovered Java (client)
  server/        recovered Java (server)
  prebuilt/      signed APK + SHA-256
docs/            01 arquitectura … 12 decisiones de diseño
```

## Documentación

| # | Capítulo |
|---|---|
| 01 | [Arquitectura](docs/01-architecture.md) |
| 02 | [Requisitos](docs/02-requirements.md) |
| 03 | [Instalación](docs/03-install.md) |
| 04 | [Referencia de la CLI](docs/04-cli-reference.md) |
| 05 | [Servicios](docs/05-services.md) |
| 06 | [Acceso remoto](docs/06-remote-access.md) |
| 07 | [Seguridad](docs/07-security.md) |
| 08 | [Kabuk Agent](docs/08-agent.md) |
| 09 | [Kabuk Client](docs/09-client-app.md) |
| 10 | [Kabuk Server](docs/10-server-app.md) |
| 11 | [Solución de problemas](docs/11-troubleshooting.md) |
| 12 | [Decisiones de diseño](docs/12-design-decisions.md) |

## Seguridad en un párrafo

El teléfono no recibe actualizaciones de seguridad desde abril de 2022, así que no
debe ser alcanzable desde redes no confiables. Y no lo es: ningún puerto de entrada en
Wi-Fi, Ethernet o datos móviles, sin autenticación por contraseña, sin binds a
`0.0.0.0`, sin relé en la nube. Todo pasa por tu tailnet de Tailscale, y el agente
revalida cada petición contra el propietario del tailnet. El chroot es una frontera de
empaquetado, no una sandbox: lee [docs/07-security.md](docs/07-security.md) antes de
activar `FULL_ROOT=1` o el modo `full` del agente.

## Licencia

MIT — ver [LICENSE](LICENSE). Los componentes de terceros (Termux terminal-view, musl,
mosh, OpenSSH, paquetes Alpine, Tailscale, ttyd, ntfy) figuran en [NOTICE.md](NOTICE.md)
y conservan sus licencias.

Sin relación con Google, Xiaomi, Magisk ni Anthropic. Rootear anula la garantía; un
teléfono encendido 24/7 sobre el cargador es responsabilidad tuya.