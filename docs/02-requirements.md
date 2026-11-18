# Requirements

## Hardware

| Item | Minimum | Used in development |
|---|---|---|
| Phone | Any arm64 Android with unlocked bootloader and ≥3 GB RAM | Xiaomi Redmi Note 10 5G (`camellia`, M2103K19C), Dimensity 700, 4 GB |
| Storage | 8 GB free on `/data` | 6–10 GB used after Alpine, Node and Claude Code |
| USB | One cable for the initial deployment; not needed afterwards | — |
| Power | A charger that stays connected | — |

The phone is expected to run 24/7. In our setup the battery is held between
75 % and 80 % by `input_suspend` and the wakelock is only held while work is
running, which keeps the pack cool and the SoC out of thermal throttling.

## Software

| Requirement | Notes |
|---|---|
| Android 11 or newer | Developed on Android 12 / MIUI 13; needs `device_config`, `cmd appops`, `input_suspend` |
| Unlocked bootloader | Required for Magisk |
| Magisk 26+ (31.0 tested) | Provides root and the `late_start` module hook |
| SELinux enforcing | Supported; the runtime never switches to permissive |
| Alpine minirootfs (aarch64) | Downloaded once on the phone during install |
| Internet on the phone | For `apk add`, the Claude Code installer and Tailscale login |
| Tailscale account | Free tier is enough; every client device must be in the same tailnet |
| adb (optional) | Only for the first `kabuk deploy`; can be done from a root shell |

No custom kernel is needed. No GSI is needed on the phone that runs the server;
the runtime works on top of the stock MIUI system (see the companion repository
`redmi-note10-5g-root` if you also want to replace the system with a GSI and
keep Magisk).

## Disk layout created at runtime

```
/data/kabuk/
├── bin/          scripts deployed from the APK or from runtime/
├── etc/          kabuk.conf, service state, ttyd.cred, sshd drop-in
├── share/        payload staging area (rootfs/, sv/, tests/)
├── rootfs/       Alpine chroot (several GB after apk add)
├── log/          boot.log, health.csv, per-service runit logs
├── run/          pid and lock files
└── backups/      rootfs-YYYYmmdd-HHMMSS.tar.gz (last three kept)
```

`/data/kabuk` is deliberately outside the chroot: wiping the rootfs never
touches the configuration, and `kabuk backup` snapshots only `rootfs/`.

## Accounts and identifiers

* The chroot user is `dev` (uid 8000). Change `USERNAME`/`USER_UID` in
  `runtime/bin/kabuk` before the first install if you want another name.
* The Tailscale hostname defaults to `note10` (`TS_HOSTNAME` in
  `kabuk.conf`).
* The agent runs as the same unprivileged user and listens on port 8787.

## Ports that are opened

Only on the tailnet interface:

| Port | Service |
|---|---|
| 22/tcp | sshd (key only) |
| 7681/tcp | ttyd web terminal |
| 8088/tcp | optional web panel, if installed |
| 8787/tcp | Kabuk Agent |
| 8090/tcp | ntfy, if you run the bundled service |
| 60000–61000/udp | mosh |

The LAN firewall drops every new connection on `wlan+`/`eth+` except
established traffic, DHCP, mDNS, ICMP and Tailscale's UDP/41641. See
[07-security.md](07-security.md).
