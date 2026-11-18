# Kabuk

**Превратите рутированный Android-телефон в сервер Claude Code, работающий 24/7, и подключайтесь к нему откуда угодно через Tailscale.**

Ни облака, ни релея, ни открытых портов. Сервер — это сам телефон: в chroot с Alpine
работают OpenSSH, mosh, tmux, веб-терминал, уведомления и Claude Code; watchdog,
запускаемый Magisk, следит за зарядкой, температурой и сетью; Android-клиент даёт
настоящий терминал без Termux и без root.

> 🇬🇧 [English](README.md) · 🇹🇷 [Türkçe](README.tr.md) · 🇨🇳 [简体中文](README.zh-CN.md) · 🇩🇪 [Deutsch](README.de.md) · 🇵🇹 [Português (BR)](README.pt-BR.md) · 🇮🇳 [हिन्दी](README.hi.md) · 🇮🇩 [Bahasa Indonesia](README.id.md) · 🇪🇸 [Español](README.es.md) · 🇻🇳 [Tiếng Việt](README.vi.md) · 🇸🇦 [العربية](README.ar.md) · 🇫🇷 [Français](README.fr.md)

## Из чего состоит

| Слой | Что работает |
|---|---|
| Телефон (Android 12, Magisk) | CLI `kabuk`, менеджер монтирований, watchdog, LAN-фаервол, ограничение заряда |
| chroot (Alpine 3.24, aarch64) | `tailscaled`, `sshd`, `taildrop`, `ttyd`, `ntfy`, `kabuk-agent`, Claude Code |
| Клиент | Android-терминал (mosh/ssh, вкладки) + веб-интерфейсы |

Референсное устройство: **Xiaomi Redmi Note 10 5G** (`camellia`, Dimensity 700,
4 ГБ RAM), Android 12 / MIUI 13, Magisk 31.0. В такой конфигурации телефон
используется как повседневный сервер разработки.

## Состояние

| | |
|---|---|
| Устройство | Redmi Note 10 5G — M2103K19C, `camellia` |
| Root | Magisk 31.0, штатный boot, SELinux enforcing |
| Среда | Alpine 3.24.4 aarch64, настоящий `chroot`, пользователь `dev` (uid 8000) |
| Супервизор | runit (`runsvdir`), 6 сервисов |
| Сеть | только Tailscale — LAN-фаервол отбрасывает новые соединения на `wlan+`/`eth+` |
| Питание | заряд держится в диапазоне 75–80 %, троттлинг при 40 °C |
| Доступ | ssh, mosh, sftp, taildrop, веб-терминал (7681), интерфейс агента (8787) |
| Документация | 12 глав, 12 языков |

## Возможности

* **Настоящий chroot, а не эмуляция.** `git`, `npm`, `tmux`, PTY и `rm -rf .git` ведут себя как на обычном Linux.
* **Переживает перезагрузку, смену сети и закрытую крышку.** Magisk запускает среду до разблокировки экрана; mosh сохраняет сессии при переключении Wi-Fi ↔ LTE; watchdog исправляет правило маршрутизации Tailscale за 30 секунд.
* **Один чат-интерфейс для Claude Code.** `kabuk-agent` запускает `claude` для каждого чата, отдаёт события по SSE и сначала пишет их на диск, поэтому переподключение ничего не теряет, а следующее сообщение продолжает сессию через `--resume`.
* **Бережёт батарею.** Окно заряда, wakelock только при работе, cpuset + nice выше 40 °C, перезапуск Wi-Fi при потере сети.
* **Ничего не выставлено наружу.** Все сервисы слушают интерфейс tailnet; LAN-фаервол — вторая линия защиты; агент проверяет каждый запрос через `tailscale whois`.
* **Аварийная остановка.** `kabuk disable` мгновенно отключает удалённый доступ, не убивая текущую работу.

## Быстрый старт

```sh
# на телефоне, один раз
# 1. установите Kabuk Server, дайте root, нажмите "Kur / Onar"
# 2. подключите узел к tailnet
su -c '/data/kabuk/bin/kabuk ts-up'
# 3. добавьте свой публичный ключ
su -c '/data/kabuk/bin/kabuk authkey "ssh-ed25519 AAAA... you@laptop"'
# 4. проверьте
su -c '/data/kabuk/bin/kabuk test'
```

```sh
# с ноутбука
ssh dev@note10 -t kabuk-session     # постоянная tmux-сессия + Claude Code
mosh dev@note10 -- kabuk-session    # то же, не рвётся при смене сети
open http://note10:8787               # чат-интерфейс Claude Code
```

Полная инструкция: [docs/03-install.md](docs/03-install.md).

## Структура репозитория

```text
runtime/         дерево, которое разворачивается в /data/kabuk
  bin/           kabuk CLI, boot, enter, servermode
  etc/           kabuk.conf
  magisk/        module.prop, service.sh, uninstall.sh
  rootfs/        помощники chroot + агент (server.js, index.html, presets.json)
  sv/            определения сервисов runit
  tests/         stage2.sh
docs/            01 архитектура … 12 проектные решения
```

## Документация

| # | Глава |
|---|---|
| 01 | [Архитектура](docs/01-architecture.md) |
| 02 | [Требования](docs/02-requirements.md) |
| 03 | [Установка](docs/03-install.md) |
| 04 | [Справочник CLI](docs/04-cli-reference.md) |
| 05 | [Сервисы](docs/05-services.md) |
| 06 | [Удалённый доступ](docs/06-remote-access.md) |
| 07 | [Безопасность](docs/07-security.md) |
| 08 | [Kabuk Agent](docs/08-agent.md) |
| 09 | [Клиент](docs/09-client-app.md) |
| 10 | [Сервер](docs/10-server-app.md) |
| 11 | [Диагностика](docs/11-troubleshooting.md) |
| 12 | [Проектные решения](docs/12-design-decisions.md) |

## Безопасность в одном абзаце

Телефон не получает обновлений безопасности с апреля 2022 года, поэтому он не должен
быть доступен из недоверенных сетей. Он и не доступен: ни одного входящего порта в
Wi-Fi, Ethernet или мобильной сети, нет паролей, нет привязки к `0.0.0.0`, нет облачного
релея. Всё идёт через ваш tailnet Tailscale, и агент заново подтверждает права на каждый
запрос. chroot — это граница упаковки, а не песочница: прочитайте
[docs/07-security.md](docs/07-security.md) перед включением `FULL_ROOT=1` или режима
`full` у агента.

## Лицензия

MIT — см. [LICENSE](LICENSE). Сторонние компоненты (Termux terminal-view, musl, mosh,
OpenSSH, пакеты Alpine, Tailscale, ttyd, ntfy) перечислены в [NOTICE.md](NOTICE.md)
и сохраняют свои лицензии.

Проект не связан с Google, Xiaomi, Magisk или Anthropic. Root лишает гарантии;
телефон, работающий 24/7 на зарядке, — ваша ответственность.