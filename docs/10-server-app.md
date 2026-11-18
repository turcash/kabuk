# Kabuk Server (Android app)

Package `com.kabuk.server`. It is a thin, deliberately boring control
panel: it owns no daemon, holds no state, and does nothing that the CLI cannot
do. Its whole job is to deploy the runtime, ask Magisk for root, run a command
and show the output.

Prebuilt: `apps/prebuilt/kabuk-server-1.0.0.apk`.

> **Source note.** As with the client, the Kotlin sources were lost and
> `apps/server/recovered-java/` holds the app classes recovered from the
> released APK. Behaviour matches the shipped build.

## Why an app at all

The services run under Magisk and are fully headless. The app exists for the
three moments when a terminal on the phone is inconvenient:

1. **First install** — it can fetch and verify the Alpine minirootfs itself.
2. **Repair** — re-deploy the runtime after an update without adb.
3. **Emergency** — one button that cuts all remote access (`kabuk disable`).

Everything else (status, tests, backups, keys) is the same commands with a
scrollable log pane, which is handy when your laptop is not with you.

## Install / repair flow

`installOrRepair()`:

```
cacheDir/runtime/  ← assets: bin magisk etc sv rootfs tests
       │
       ├── sh  <cache>/bin/kabuk deploy <cache>/runtime
       │
       ├── [ -x /data/kabuk/rootfs/bin/busybox ] ?  ── yes ──┐
       │                                                     │
       │   no: download alpine-minirootfs-3.24.4-aarch64     │
       │       verify SHA-256 (must match the constant)      │
       │       KABUK_TARBALL=… KABUK_SHA256=…                │
       │                                                     ▼
       └──────────────────────────────────────────► kabuk install
```

The minirootfs download goes through `DownloadManager` into the app cache and is
deleted afterwards. If the checksum does not match, the install aborts and says
so — the app never unpacks an unverified archive.

Root comes from `RootShell.run()` in `:core`, which shells out to `su -c` and
quotes every argument. The app never parses shell output for secrets; it only
displays it.

## Buttons

| Button | Command |
|---|---|
| Durum | `kabuk status` |
| Kur / Onar | `deploy` + `install` (above) |
| Test | `kabuk test` |
| Yedek al | `kabuk backup` |
| Yeniden başlat | `kabuk restart` |
| SSH anahtarı ekle | `kabuk authkey <key>` (dialog; key types validated) |
| Tailscale giriş | `kabuk ts-up` (opens the printed URL with `ACTION_VIEW`) |
| ACİL DURDUR | confirmation dialog → `kabuk disable` |
| etkinleştir | `kabuk enable` (shown while stopped) |

`runTask(label, action)` disables the buttons while a task runs and streams
output line by line into the log pane, so a 10-minute `apk add` shows progress
instead of freezing.

## Status polling

While the activity is resumed, a handler runs a tick every few seconds:

* `kabuk panel` → `key=value` lines → rendered as cards,
* battery, services, Tailscale state, sessions, work flag.

`kabuk panel` is the only structured output the app consumes, and it is a stable
interface: adding a key is backwards compatible, removing one degrades that card
to "—".

## Intents

`handleTaskIntent` lets other tools drive the app, which is how automation on the
phone triggers an action without a terminal:

```sh
am start -n com.kabuk.server/.MainActivity -e task backup
```

Supported `task` values mirror the buttons: `status`, `install`, `test`,
`backup`, `restart`. `onNewIntent` handles the case where the activity is already
running.

## Foreground / Doze behaviour

The app itself does not need to stay alive: the services are children of Magisk,
not of the app. `kabuk tweaks` still whitelists the app from Doze and allows
background execution, because the **notification channel** and the status
widgets are useful, and because MIUI otherwise kills the app the moment you
leave it.

## What it deliberately does not do

* No auto-update, no telemetry, no network access except the Alpine download.
* No background service of its own — adding one would duplicate the watchdog and
  fight it for battery.
* No secrets stored: the SSH key dialog hands the string straight to `kabuk
  authkey`; nothing is persisted in the app.

## Build

Original project: `server/build.gradle.kts`, Kotlin 1.9, minSdk 31, target 34,
signed with the project keystore. Not recovered; see
[12-design-decisions.md](12-design-decisions.md).
