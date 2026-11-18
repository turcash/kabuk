# Installation

Two paths are supported: the guided one through the Kabuk Server app (what we
use), and a manual one from a shell when you prefer to keep apps out of the
picture.

## 0. Prerequisites

* Unlocked bootloader, Magisk installed and working (`su -c id` returns
  `uid=0`). See the companion repository `redmi-note10-5g-root` for the Redmi
  Note 10 5G walkthrough.
* Phone connected to the internet (Wi-Fi is fine; mobile data works too).
* A Tailscale account.

## 1. Guided install (Kabuk Server app)

1. Install `apps/prebuilt/kabuk-server-1.0.0.apk`.
2. Open it and grant the Magisk root prompt (permanently).
3. Tap **Kur / Onar** (*Install / Repair*). The app then, in order:
   * extracts `assets/{bin,magisk,etc,sv,rootfs,tests}` into its cache directory,
   * runs `/data/kabuk/bin/kabuk deploy <cache>/runtime`,
   * checks for `/data/kabuk/rootfs/bin/busybox`; if missing, downloads
     `alpine-minirootfs-3.24.4-aarch64.tar.gz` and verifies its SHA-256,
   * runs `kabuk install` with `KABUK_TARBALL`/`KABUK_SHA256` in the environment.
4. The install output is streamed into the app's log pane. Expect this to take
   5–15 minutes: `apk add` fetches the full toolchain and then the official
   Claude Code installer runs inside the chroot.
5. When it finishes, tap **Durum** (*Status*); `rootfs:` must show
   `3.24.x`, and `runsvdir:` must have a PID.

The app's buttons map directly onto the CLI, so everything below also works
from a root shell:

| Button | Command |
|---|---|
| Durum | `kabuk status` |
| Kur / Onar | `kabuk deploy … && kabuk install` |
| Test | `kabuk test` |
| Yedek al | `kabuk backup` |
| Yeniden başlat | `kabuk restart` |
| ACİL DURDUR | `kabuk disable` |
| Anahtar ekle | `kabuk authkey '<public key>'` |
| Tailscale girişi | `kabuk ts-up` |

## 2. Manual install

Deploy the `runtime/` tree to the phone and let the CLI do the rest.

```sh
# from this repository
adb push runtime /data/local/tmp/kabuk-runtime
adb shell su -c '/system/bin/sh /data/local/tmp/kabuk-runtime/bin/kabuk deploy /data/local/tmp/kabuk-runtime'

# download the minirootfs on the phone and install
adb shell su -c 'cd /data/local/tmp && \
  curl -fLO https://dl-cdn.alpinelinux.org/alpine/v3.24/releases/aarch64/alpine-minirootfs-3.24.4-aarch64.tar.gz && \
  KABUK_TARBALL=/data/local/tmp/alpine-minirootfs-3.24.4-aarch64.tar.gz \
  KABUK_SHA256=<sha256> /data/kabuk/bin/kabuk install'
```

`kabuk deploy` copies `bin/` to `/data/kabuk/bin`, replaces
`/data/kabuk/share/{sv,rootfs,tests}`, installs the Magisk module into
`/data/adb/modules/kabuk-sunucu` and creates `kabuk.conf` only if it does not
exist yet. Re-deploying is safe and is the normal way to ship a runtime update.

`kabuk install` unpacks the minirootfs (only if `rootfs/bin/busybox` is
missing), writes DNS and hostname, mounts, runs `apk add` for the package list,
creates the `dev` user with uid 8000, writes `/etc/profile.d/kabuk.sh`,
installs Claude Code, syncs `share/` into the chroot and finally hooks Claude
Code notifications.

## 3. Tailscale

```sh
su -c '/data/kabuk/bin/kabuk ts-up'
```

It prints a `https://login.tailscale.com/...` URL (also sent as a notification);
open it once and approve the device. Afterwards:

```sh
su -c '/data/kabuk/bin/kabuk ts status'
```

The node appears as `note10` (change `TS_HOSTNAME` in `kabuk.conf` before
logging in if you want another name).

## 4. SSH key

The chroot rejects passwords; only public keys work.

```sh
su -c '/data/kabuk/bin/kabuk authkey "ssh-ed25519 AAAA... you@laptop"'
```

Accepted types are `ssh-ed25519`, `ecdsa-sha2-nistp256` and `sk-*` hardware
keys. Then, from any tailnet machine:

```sh
ssh dev@note10
```

## 5. Verify

```sh
su -c '/data/kabuk/bin/kabuk status'   # services, battery, wakelock, size
su -c '/data/kabuk/bin/kabuk test'     # ~20 regression checks
su -c '/data/kabuk/bin/kabuk health'   # last CSV line
```

`kabuk test` covers: real tmpfs `/tmp`, `rm -rf .git`, the uid-8000 user and
home permissions, DNS/HTTPS, `claude --version`, the toolchain, PTY + tmux,
`/dev/null`, `/dev/urandom`, `/dev/net/tun`, sudo policy, OOM adjustment,
600/700 permissions on credentials, the LAN firewall, and that no service
listens outside loopback/tailnet.

## 6. Client app

Install `apps/prebuilt/kabuk-client-1.1.1.apk` on the device you type on. Add a
profile with host `note10`, user `dev` and your key; the client opens a mosh
or ssh session and keeps several tabs. See [09-client-app.md](09-client-app.md).

## 7. Updating

* Runtime only: `kabuk deploy <new runtime dir>` then `kabuk restart`.
* Everything else inside the chroot: `kabuk enter`, then `sudo apk upgrade -U`.
* Claude Code: `kabuk run dev 'claude update'` or the official installer again.

Take a backup before a large upgrade:

```sh
su -c '/data/kabuk/bin/kabuk backup'
```

## Uninstall

```sh
su -c '/data/kabuk/bin/kabuk disable'      # cuts all remote access immediately
# then remove the Magisk module and delete /data/kabuk if you want the space back
```

`uninstall.sh` of the module re-enables charging and releases the wakelock but
never deletes `/data/kabuk`.
