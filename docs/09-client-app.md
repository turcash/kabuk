# Kabuk Client (Android app)

Package `com.kabuk`. A terminal for the phone-server: it opens mosh or ssh
sessions, keeps several tabs, and has a built-in web view for the agent UI.

Prebuilt: `apps/prebuilt/kabuk-client-1.1.1.apk`.

> **Source note.** The original Kotlin sources were lost to an accidental
> directory deletion. `apps/client/recovered-java/` contains the app classes
> recovered from the released APK with `jadx`, which is why some constructs look
> mechanical (`Runnable` lambdas, synthetic `$` classes, no comments). Behaviour
> is identical to the shipped build. See [12-design-decisions.md](12-design-decisions.md).

## No root, no Termux

The interesting part is that the client does not need root or Termux. It ships
its own tiny Linux userland as APK assets and JNI libraries:

| Asset | Purpose |
|---|---|
| `libldmusl.so` | musl dynamic loader, executed directly as the program |
| `libssh_bin.so`, `libmoshclient_bin.so` | static `ssh` and `mosh-client` binaries |
| `libkabukpw.so` | preloaded shim (`fakepw.c`) that fakes `getpwnam`/`getpwuid` |
| `manifest.tsv` | asset → `files/lib` symlink map |
| `terminfo/x/xterm-256color` | terminal database for ncurses apps |

`NativeEnv` copies the assets to `files/lib`, symlinks them under their real
names (`ssh`, `mosh-client`, …) and builds the command line:

```sh
libldmusl.so --library-path files/lib --preload libkabukpw.so \
             --argv0 ssh files/lib/ssh -F files/ssh/config …
```

`fakepw.c` exists because Android's bionic has no `/etc/passwd`: musl's
`getpwuid` returns NULL and `ssh` refuses to start. The preload returns a
synthetic `kabuk` user with a valid home directory.

## SSH keys

There is no key file to manage. `KeyAgent`:

1. Generates an ECDSA P-256 key pair on first launch.
2. Stores the private key in the **Android Keystore** (`kabuk-ssh-ecdsa`), so it
   is non-exportable and hardware-backed where available.
3. Runs a tiny SSH agent on a local `LocalServerSocket` and exports
   `SSH_AUTH_SOCK` to the child process.
4. Shows the public line (`ecdsa-sha2-nistp256 AAAA… kabuk@<MODEL>`) so you can
   paste it into `kabuk authkey`.

`ssh_config` is generated with `IdentityAgent`, `IdentityFile none`,
`StrictHostKeyChecking accept-new`, `ServerAliveInterval 15` and
`ConnectTimeout 15`.

## Profiles

A profile is `{id, name, host, port, user, session, mode, color, notify}`:

| Field | Meaning |
|---|---|
| `host` | `note10` (MagicDNS) or the tailnet IP |
| `port` | 22 |
| `user` | `dev` |
| `session` | tmux session name passed to `kabuk-session`, default `claude` |
| `mode` | `mosh` (default) or `ssh` |
| `color` | tab colour, one of five presets |
| `notify` | show a notification when a command finishes |

`mosh` degrades gracefully: if the bootstrap fails (`MoshUnavailable`), the tab
reconnects over ssh instead of dying.

## Tabs and sessions

`Sessions` owns the list of tabs. Each tab is a `TerminalSession` from the
Termux terminal-view library (Apache-2.0, see `NOTICE.md`).

* Opening a tab runs `ssh … "--" "mosh-server new -c 256 -l LANG=C.UTF-8 -- kabuk-session <session>"`,
  parses `MOSH_KEY`, then starts `mosh-client`.
* `kabuk-session` attaches to the named tmux session or creates it, so closing
  the tab, the app or the phone's screen never ends the work.
* Scrollback in a remote tab is provided by tmux (`history-limit 50000`,
  `mouse on`, `tmux-256color`); scrolling up enters tmux copy-mode instead of
  fighting the local screen. This is the 1.1.1 fix: the mouse wheel is used
  rather than sending an `↑` key.
* UTF-8 is forced (`-u`, `LANG=C.UTF-8`) because mosh/ssh sessions often have no
  locale, and tmux would otherwise replace every Turkish character with `_`.

## Agents screen

`AgentActivity` is a `WebView` that loads `http://<host>:8787`, the Kabuk Agent
UI. External links open in the system browser (`shouldOverrideUrlLoading`), so
the chat page never navigates away from the agent.

Because the request comes from a tailnet device, `tailscale whois` authorises it
without any login.

## Service

`KabukService` is a foreground service that keeps the SSH agent socket alive and
shows the active tab count; it is what makes notifications and reconnection work
when the app is in the background. It does not need root.

## Build

The original Gradle project (`build.gradle.kts`, `client/build.gradle.kts`,
`native/bundle.sh`) is documented in [11-troubleshooting.md](11-troubleshooting.md)
and [12-design-decisions.md](12-design-decisions.md); it was not recovered.

To rebuild from what is here you need:

* JDK 17, Android SDK 34, NDK (for `fakepw.c` and the bundled binaries)
* the Termux `terminal-view` / `terminal-emulator` sources (Apache-2.0)
* the static `ssh` and `mosh-client` binaries built for `aarch64` against musl

A local build must be signed with your own key; Android only updates an
installed app when the signature matches.

## Permissions

| Permission | Why |
|---|---|
| `INTERNET` | network sessions and the agent web view |
| `FOREGROUND_SERVICE` | keep sessions alive |
| `POST_NOTIFICATIONS` | command-finished notifications |
| `ACCESS_NETWORK_STATE` | wait for a network before reconnecting |
