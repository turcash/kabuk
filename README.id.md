# Kabuk

**Ubah ponsel Android yang sudah di-root menjadi server Claude Code yang hidup 24/7 — dan akses dari mana saja lewat Tailscale.**

Tanpa cloud, tanpa relay, tanpa port terbuka. Servernya adalah ponsel itu sendiri: di
dalam chroot Alpine berjalan OpenSSH, mosh, tmux, terminal web, notifikasi dan Claude
Code; watchdog yang dijalankan Magisk mengurus pengisian daya, suhu dan jaringan;
klien Android memberi terminal sungguhan tanpa Termux dan tanpa root.

> 🇬🇧 [English](README.md) · 🇹🇷 [Türkçe](README.tr.md) · 🇨🇳 [简体中文](README.zh-CN.md) · 🇷🇺 [Русский](README.ru.md) · 🇩🇪 [Deutsch](README.de.md) · 🇵🇹 [Português (BR)](README.pt-BR.md) · 🇮🇳 [हिन्दी](README.hi.md) · 🇪🇸 [Español](README.es.md) · 🇻🇳 [Tiếng Việt](README.vi.md) · 🇸🇦 [العربية](README.ar.md) · 🇫🇷 [Français](README.fr.md)

## Isinya apa

| Lapisan | Yang berjalan |
|---|---|
| Ponsel (Android 12, Magisk) | CLI `kabuk`, manajer mount, watchdog, firewall LAN, batas pengisian |
| chroot (Alpine 3.24, aarch64) | `tailscaled`, `sshd`, `taildrop`, `ttyd`, `ntfy`, `kabuk-agent`, Claude Code |
| Klien | aplikasi terminal Android (mosh/ssh, banyak tab) + antarmuka web |

Perangkat acuan: **Xiaomi Redmi Note 10 5G** (`camellia`, Dimensity 700, 4 GB RAM),
Android 12 / MIUI 13, Magisk 31.0 — dipakai sebagai server pengembangan harian.

## Status

| | |
|---|---|
| Perangkat | Redmi Note 10 5G — M2103K19C, `camellia` |
| Root | Magisk 31.0, boot image stok, SELinux enforcing |
| Runtime | Alpine 3.24.4 aarch64, `chroot` sungguhan, pengguna `dev` (uid 8000) |
| Supervisor | runit (`runsvdir`), 6 layanan |
| Jaringan | hanya Tailscale — firewall LAN menjatuhkan koneksi baru di `wlan+`/`eth+` |
| Daya | pengisian ditahan 75–80 %, throttling pada 40 °C |
| Akses | ssh, mosh, sftp, taildrop, terminal web (7681), UI agen (8787) |
| Dokumentasi | 12 bab, 12 bahasa |

## Fitur

* **chroot sungguhan, bukan emulasi.** `git`, `npm`, `tmux`, PTY dan `rm -rf .git` berperilaku seperti di Linux biasa.
* **Tahan reboot, ganti jaringan dan layar terkunci.** Magisk menjalankan runtime sebelum layar dibuka; mosh menjaga sesi saat berpindah Wi-Fi ↔ LTE; watchdog memperbaiki aturan routing Tailscale dalam 30 detik.
* **Satu UI obrolan untuk Claude Code.** `kabuk-agent` menjalankan `claude` per obrolan, mengalirkan peristiwa lewat SSE dan menuliskannya ke disk lebih dulu, jadi koneksi putus tidak menghilangkan apa pun dan pesan berikutnya melanjutkan dengan `--resume`.
* **Ramah baterai.** Jendela pengisian, wakelock hanya saat ada pekerjaan, cpuset + nice di atas 40 °C, reset Wi-Fi bila jaringan hilang.
* **Tidak ada yang terbuka.** Semua layanan mengikat antarmuka tailnet; firewall LAN adalah lapis kedua; agen memeriksa setiap permintaan dengan `tailscale whois`.
* **Berhenti darurat.** `kabuk disable` memutus semua akses jauh tanpa menghentikan pekerjaan yang sedang berjalan.

## Mulai cepat

```sh
# di ponsel, sekali
# 1. pasang Kabuk Server, beri root, tekan "Kur / Onar"
# 2. daftarkan node ke tailnet
su -c '/data/kabuk/bin/kabuk ts-up'
# 3. tambahkan kunci publik Anda
su -c '/data/kabuk/bin/kabuk authkey "ssh-ed25519 AAAA... anda@laptop"'
# 4. verifikasi
su -c '/data/kabuk/bin/kabuk test'
```

```sh
# dari laptop
ssh dev@note10 -t kabuk-session     # tmux persisten + Claude Code
mosh dev@note10 -- kabuk-session    # sama, tahan ganti jaringan
open http://note10:8787               # UI obrolan Claude Code
```

Panduan lengkap: [docs/03-install.md](docs/03-install.md).

## Struktur repositori

```text
runtime/         pohon yang dipasang ke /data/kabuk
  bin/           CLI kabuk, boot, enter, servermode
  etc/           kabuk.conf
  magisk/        module.prop, service.sh, uninstall.sh
  rootfs/        pembantu chroot + agen (server.js, index.html, presets.json)
  sv/            definisi layanan runit
  tests/         stage2.sh
apps/
  client/        recovered Java (client)
  server/        recovered Java (server)
  prebuilt/      signed APK + SHA-256
docs/            01 arsitektur … 12 keputusan desain
```

## Dokumentasi

| # | Bab |
|---|---|
| 01 | [Arsitektur](docs/01-architecture.md) |
| 02 | [Kebutuhan](docs/02-requirements.md) |
| 03 | [Instalasi](docs/03-install.md) |
| 04 | [Referensi CLI](docs/04-cli-reference.md) |
| 05 | [Layanan](docs/05-services.md) |
| 06 | [Akses jarak jauh](docs/06-remote-access.md) |
| 07 | [Keamanan](docs/07-security.md) |
| 08 | [Kabuk Agent](docs/08-agent.md) |
| 09 | [Kabuk Client](docs/09-client-app.md) |
| 10 | [Kabuk Server](docs/10-server-app.md) |
| 11 | [Pemecahan masalah](docs/11-troubleshooting.md) |
| 12 | [Keputusan desain](docs/12-design-decisions.md) |

## Keamanan dalam satu paragraf

Ponsel ini tidak menerima pembaruan keamanan sejak April 2022, jadi tidak boleh
terjangkau dari jaringan tak tepercaya. Dan memang tidak: tidak ada port masuk di
Wi-Fi, Ethernet atau data seluler, tidak ada login kata sandi, tidak ada bind
`0.0.0.0`, tidak ada relay cloud. Semua lewat tailnet Tailscale Anda, dan agen
memverifikasi ulang setiap permintaan terhadap pemilik tailnet. chroot adalah batas
pengemasan, bukan sandbox — baca [docs/07-security.md](docs/07-security.md) sebelum
mengaktifkan `FULL_ROOT=1` atau mode `full` pada agen.

## Lisensi

MIT — lihat [LICENSE](LICENSE). Komponen pihak ketiga (Termux terminal-view, musl,
mosh, OpenSSH, paket Alpine, Tailscale, ttyd, ntfy) tercantum di [NOTICE.md](NOTICE.md)
dan tetap memakai lisensinya sendiri.

Tidak berafiliasi dengan Google, Xiaomi, Magisk atau Anthropic. Root membatalkan
garansi; ponsel yang menyala 24/7 di atas charger adalah tanggung jawab Anda.