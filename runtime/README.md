# runtime/

Exactly what `kabuk deploy` places on the phone.

```
bin/
  kabuk              main CLI: mount manager, install, watchdog, firewall, backup
  kabuk-boot         boot orchestrator started by the Magisk module
  kabuk-enter        wrapper: enter the chroot with mount cleanup on exit
  kabuk-servermode   disable/enable non-server Android packages (reversible)
etc/
  kabuk.conf         settings, re-read by the watchdog every 30 s
magisk/
  module.prop        Magisk module metadata (id: kabuk-sunucu)
  service.sh         late_start hook: background kabuk-boot
  uninstall.sh       re-enable charging, release the wakelock, keep /data/kabuk
rootfs/
  usr/local/bin/
    kabuk-session      attach/create the tmux session (default: claude)
    kabuk-notify       notification helper for Claude Code hooks
    kabuk-plugins-setup  install one of the agent presets
    kabuk-tsip         print the tailscale0 address
    mosh-server        wrapper forcing -i <tailnet ip>
    android            run a command on the Android side as root (FULL_ROOT=1)
  opt/kabuk-agent/
    server.js          chat server for Claude Code (Node, no dependencies)
    index.html         single-page UI
    presets.json       plugin sets (hafif / gelistirme / tasarim / tam)
sv/
  tailscaled/run  sshd/run  taildrop/run  ttyd/run  ntfy/run  kabuk-agent/run
tests/
  stage2.sh          on-device regression checks
```

## Deploy

```sh
su -c '/data/kabuk/bin/kabuk deploy /path/to/runtime'
```

* `bin/*` → `/data/kabuk/bin/`
* `magisk/*` → `/data/adb/modules/kabuk-sunucu/`
* `sv/`, `rootfs/`, `tests/` → `/data/kabuk/share/`
* `etc/kabuk.conf` → `/data/kabuk/etc/` only if it does not exist yet

`sv_sync` (called by `kabuk start`) then copies `share/rootfs/**` into the
chroot and creates `/etc/kabuk/sv/<name>` + `/etc/kabuk/service/<name>` from
`share/sv/*/run`. Only services listed in `SERVICES` in `kabuk.conf` are enabled.

## Conventions

* Every script is `/bin/sh` (Android side) or `/bin/sh` + Alpine (chroot side).
* Every service binds the tailnet address; none binds `0.0.0.0`.
* Locking uses `flock` on a file descriptor opened by the same shell
  (mksh marks `exec N>file` descriptors close-on-exec).
* No secrets in this tree. `ttyd.cred` and SSH keys are generated on the phone.
