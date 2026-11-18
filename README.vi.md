# Kabuk

**Biến một chiếc điện thoại Android đã root thành máy chủ Claude Code chạy 24/7 — và truy cập từ bất cứ đâu qua Tailscale.**

Không đám mây, không relay, không mở cổng. Máy chủ chính là điện thoại: trong chroot
Alpine chạy OpenSSH, mosh, tmux, terminal web, thông báo và Claude Code; một watchdog
do Magisk khởi động lo việc sạc, nhiệt độ và mạng; ứng dụng Android cho bạn terminal
thật mà không cần Termux và không cần root.

> 🇬🇧 [English](README.md) · 🇹🇷 [Türkçe](README.tr.md) · 🇨🇳 [简体中文](README.zh-CN.md) · 🇷🇺 [Русский](README.ru.md) · 🇩🇪 [Deutsch](README.de.md) · 🇵🇹 [Português (BR)](README.pt-BR.md) · 🇮🇳 [हिन्दी](README.hi.md) · 🇮🇩 [Bahasa Indonesia](README.id.md) · 🇪🇸 [Español](README.es.md) · 🇸🇦 [العربية](README.ar.md) · 🇫🇷 [Français](README.fr.md)

## Thành phần

| Tầng | Chạy gì |
|---|---|
| Điện thoại (Android 12, Magisk) | CLI `kabuk`, quản lý mount, watchdog, tường lửa LAN, giới hạn sạc |
| chroot (Alpine 3.24, aarch64) | `tailscaled`, `sshd`, `taildrop`, `ttyd`, `ntfy`, `kabuk-agent`, Claude Code |
| Máy khách | ứng dụng terminal Android (mosh/ssh, nhiều tab) + giao diện web |

Thiết bị tham chiếu: **Xiaomi Redmi Note 10 5G** (`camellia`, Dimensity 700, 4 GB RAM),
Android 12 / MIUI 13, Magisk 31.0, được dùng làm máy chủ phát triển hằng ngày.

## Trạng thái

| | |
|---|---|
| Thiết bị | Redmi Note 10 5G — M2103K19C, `camellia` |
| Root | Magisk 31.0, boot gốc, SELinux enforcing |
| Môi trường | Alpine 3.24.4 aarch64, `chroot` thật, người dùng `dev` (uid 8000) |
| Giám sát | runit (`runsvdir`), 6 dịch vụ |
| Mạng | chỉ Tailscale — tường lửa LAN chặn mọi kết nối mới trên `wlan+`/`eth+` |
| Nguồn | sạc giữ trong khoảng 75–80 %, giảm xung khi 40 °C |
| Truy cập | ssh, mosh, sftp, taildrop, terminal web (7681), giao diện agent (8787) |
| Tài liệu | 12 chương, 12 ngôn ngữ |

## Tính năng

* **chroot thật, không phải giả lập.** `git`, `npm`, `tmux`, PTY và `rm -rf .git` hoạt động như trên Linux bình thường.
* **Chịu được khởi động lại, đổi mạng và khóa màn hình.** Magisk khởi động môi trường trước khi bạn mở khóa; mosh giữ phiên khi chuyển Wi-Fi ↔ LTE; watchdog sửa quy tắc định tuyến Tailscale trong 30 giây.
* **Một giao diện chat cho Claude Code.** `kabuk-agent` chạy `claude` cho mỗi cuộc trò chuyện, đẩy sự kiện qua SSE và ghi ra đĩa trước, nên mất kết nối không mất dữ liệu và tin nhắn sau tiếp tục bằng `--resume`.
* **Thân thiện với pin.** Cửa sổ sạc, wakelock chỉ khi có việc, cpuset + nice khi trên 40 °C, khởi động lại Wi-Fi nếu mất mạng.
* **Không phơi bày gì.** Mọi dịch vụ chỉ bind giao diện tailnet; tường lửa LAN là lớp thứ hai; agent xác thực từng yêu cầu bằng `tailscale whois`.
* **Dừng khẩn cấp.** `kabuk disable` cắt toàn bộ truy cập từ xa mà không dừng công việc đang chạy.

## Bắt đầu nhanh

```sh
# trên điện thoại, một lần
# 1. cài Kabuk Server, cấp root, bấm "Kur / Onar"
# 2. đưa node vào tailnet
su -c '/data/kabuk/bin/kabuk ts-up'
# 3. thêm khóa công khai của bạn
su -c '/data/kabuk/bin/kabuk authkey "ssh-ed25519 AAAA... ban@laptop"'
# 4. kiểm tra
su -c '/data/kabuk/bin/kabuk test'
```

```sh
# từ laptop
ssh dev@note10 -t kabuk-session     # tmux bền + Claude Code
mosh dev@note10 -- kabuk-session    # như trên, không đứt khi đổi mạng
open http://note10:8787               # giao diện chat Claude Code
```

Hướng dẫn đầy đủ: [docs/03-install.md](docs/03-install.md).

## Cấu trúc kho

```text
runtime/         cây được triển khai vào /data/kabuk
  bin/           CLI kabuk, boot, enter, servermode
  etc/           kabuk.conf
  magisk/        module.prop, service.sh, uninstall.sh
  rootfs/        tiện ích chroot + agent (server.js, index.html, presets.json)
  sv/            định nghĩa dịch vụ runit
  tests/         stage2.sh
docs/            01 kiến trúc … 12 quyết định thiết kế
```

## Tài liệu

| # | Chương |
|---|---|
| 01 | [Kiến trúc](docs/01-architecture.md) |
| 02 | [Yêu cầu](docs/02-requirements.md) |
| 03 | [Cài đặt](docs/03-install.md) |
| 04 | [Tham chiếu CLI](docs/04-cli-reference.md) |
| 05 | [Dịch vụ](docs/05-services.md) |
| 06 | [Truy cập từ xa](docs/06-remote-access.md) |
| 07 | [Bảo mật](docs/07-security.md) |
| 08 | [Kabuk Agent](docs/08-agent.md) |
| 09 | [Kabuk Client](docs/09-client-app.md) |
| 10 | [Kabuk Server](docs/10-server-app.md) |
| 11 | [Xử lý sự cố](docs/11-troubleshooting.md) |
| 12 | [Quyết định thiết kế](docs/12-design-decisions.md) |

## Bảo mật trong một đoạn

Điện thoại không nhận bản vá bảo mật từ tháng 4/2022, nên không được phép truy cập từ
mạng không tin cậy. Và đúng là như vậy: không có cổng vào trên Wi-Fi, Ethernet hay dữ
liệu di động, không đăng nhập bằng mật khẩu, không bind `0.0.0.0`, không relay đám mây.
Mọi thứ đi qua tailnet Tailscale của bạn, và agent xác thực lại từng yêu cầu với chủ
tailnet. chroot là ranh giới đóng gói, không phải sandbox — hãy đọc
[docs/07-security.md](docs/07-security.md) trước khi bật `FULL_ROOT=1` hoặc chế độ
`full` của agent.

## Giấy phép

MIT — xem [LICENSE](LICENSE). Các thành phần bên thứ ba (Termux terminal-view, musl,
mosh, OpenSSH, gói Alpine, Tailscale, ttyd, ntfy) được liệt kê trong
[NOTICE.md](NOTICE.md) và giữ giấy phép riêng.

Không liên kết với Google, Xiaomi, Magisk hay Anthropic. Root làm mất bảo hành; một
chiếc điện thoại chạy 24/7 trên củ sạc là trách nhiệm của bạn.