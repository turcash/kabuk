# Kabuk Client — recovered sources

Java recovered with [jadx](https://github.com/skylot/jadx) 1.5.1 from
`apps/prebuilt/kabuk-client-1.1.1.apk`.

The original Gradle project (Kotlin) was lost to an accidental
case-insensitive directory deletion. The APK survived, so the app classes were
decompiled and are published here. They are mechanical: `Runnable` lambdas,
synthetic `$` classes, no comments. Behaviour matches the shipped build.

The namespace in this copy is `com.kabuk` (the extraction renamed it); the
signed APK keeps its original package id.

## What is here

| File | Role |
|---|---|
| `MainActivity.java` | device list, add/edit/delete, key export and sharing |
| `Profiles.java`, `Profile.java` | persisted connection profiles and colours |
| `Sessions.java`, `Tab.java`, `Sessions$client$1.java` | terminal sessions, tabs, mosh/ssh process handling |
| `TerminalActivity.java` | terminal view, extra keys, scroll gestures, copy/paste |
| `AgentActivity.java` | WebView wrapper for the agent UI |
| `KabukService.java` | foreground service, network changes, reconnects |
| `KeyAgent.java` | SSH agent backed by the Android Keystore |
| `NativeEnv.java` | unpacks and runs the bundled musl/ssh/mosh binaries |
| `core/KabukPaths.java`, `core/RootShell.java`, `core/ShellResult.java` | paths, root shell, command results |

`R.java` is generated and therefore omitted; Android Studio recreates it.

## Not recoverable from the APK

* Gradle files (`build.gradle.kts`, `client/build.gradle.kts`) and the wrapper.
* Kotlin sources and their comments.
* Native sources (the `fakepw.c` libc shim) and the scripts that built the
  bundled `ssh` / `mosh-client` binaries.
* Resources beyond what the APK contains (unused drawables, translations).

The APK in [`../prebuilt/`](../prebuilt/) is complete and runnable.

## Design

The client needs neither root nor Termux. It ships its own tiny Linux userland
as APK assets and JNI libraries (`libldmusl.so`, `libssh_bin.so`,
`libmoshclient_bin.so`), starts `sshd`/`mosh-server` on the phone-server side and
talks to it over the tailnet. The SSH key lives in the Android Keystore and is
exposed through a local agent socket, never written to disk.

Details: [docs/09-client-app.md](../../docs/09-client-app.md).
