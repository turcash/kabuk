# Kabuk Server — recovered sources

Java recovered with [jadx](https://github.com/skylot/jadx) 1.5.1 from
`apps/prebuilt/kabuk-server-1.0.0.apk`.

The original Gradle project was lost together with the client's. The app is a
thin control panel: it deploys the runtime, runs one command as root and shows
the output. It owns no daemon and keeps no state, so the phone keeps working
when the app is force-stopped.

The namespace in this copy is `com.kabuk.server` (the extraction renamed it);
the signed APK keeps its original package id.

## What is here

| File | Role |
|---|---|
| `MainActivity.java` | the whole UI: status, buttons, log pane, task intents |
| `MainActivity$onCreate$6.java`, `MainActivity$onCreate$9.java` | button handlers |
| `MainActivity$tick$1.java` | periodic status refresh |
| `core/KabukPaths.java`, `core/RootShell.java`, `core/ShellResult.java` | paths, root shell, command results |

`R.java` is generated and therefore omitted.

## Behaviour

* Buttons: status, restart services, logs, backup, test, Tailscale login,
  install/repair, emergency stop, re-enable.
* `am start -n <package>/.MainActivity -e task backup` runs the same actions
  from a terminal or from another app.
* Every shell argument is quoted; the app never parses output for secrets.
* No telemetry, no auto-update, no background service.

Details: [docs/10-server-app.md](../../docs/10-server-app.md).
