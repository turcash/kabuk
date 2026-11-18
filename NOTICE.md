# Third-party notices

Kabuk itself is MIT (see `LICENSE`). It builds on, embeds or installs the
following components. Nothing here is relicensed.

## Bundled in this repository

| Component | Where | License |
|---|---|---|
| Termux `terminal-view` / `terminal-emulator` | inside the client APK (`apps/prebuilt/`, referenced from `apps/client`) | Apache-2.0 |
| `mosh-client` static binary | inside the client APK | GPL-3.0 |
| `ssh` (OpenSSH static binary) | inside the client APK | BSD-style (OpenSSH) |
| musl `ld.so` | inside the client APK (`libldmusl.so`) | MIT |
| `fakepw.c` shim | client native source (original work, no external code) | MIT (this project) |

Because the client APK ships GPL-3.0 `mosh-client`, the corresponding source is
available from <https://mosh.org> (and from the Alpine/musl static build recipe
used to produce the binary). If you redistribute a modified build, you must
comply with the GPL for that binary.

## Installed on the device at runtime (not redistributed here)

The Alpine minirootfs and every package inside it are downloaded by
`kabuk install` from `dl-cdn.alpinelinux.org`. They are not part of this
repository and keep their own licenses:

| Package | License |
|---|---|
| Alpine Linux base | various (mostly MIT, BSD, GPL) |
| runit | BSD-3-Clause |
| OpenSSH, `openssh-sftp-server` | BSD-style |
| mosh | GPL-3.0 |
| Tailscale | BSD-3-Clause |
| ttyd | MIT |
| ntfy | Apache-2.0 / GPL-2.0 (dual) |
| Node.js | MIT |
| git | GPL-2.0 |
| tmux | ISC |
| ripgrep | MIT / Unlicense |
| jq | MIT |
| Python | PSF-2.0 |
| bash, coreutils, util-linux, procps | GPL family |
| sudo | ISC |
| musl libc | MIT |
| Claude Code | proprietary, installed by Anthropic's official installer; not distributed by this project |

## Trademarks

Android, Magisk and Claude are trademarks of their respective owners. This
project is not affiliated with Google, Xiaomi, Topjohnwu or Anthropic.
