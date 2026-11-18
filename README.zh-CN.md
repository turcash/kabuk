# Kabuk

**把一台已 root 的 Android 手机变成 7×24 运行的 Claude Code 服务器，并通过 Tailscale 从任何地方访问它。**

没有云端、没有中继、没有对外开放的端口。服务器就是手机本身:Alpine chroot 中运行
OpenSSH、mosh、tmux、网页终端、通知和 Claude Code;由 Magisk 启动的 watchdog 负责
充电、温度与网络;Android 客户端无需 Termux、无需 root 即可提供真正的终端。

> 🇬🇧 [English](README.md) · 🇹🇷 [Türkçe](README.tr.md) · 🇷🇺 [Русский](README.ru.md) · 🇩🇪 [Deutsch](README.de.md) · 🇵🇹 [Português (BR)](README.pt-BR.md) · 🇮🇳 [हिन्दी](README.hi.md) · 🇮🇩 [Bahasa Indonesia](README.id.md) · 🇪🇸 [Español](README.es.md) · 🇻🇳 [Tiếng Việt](README.vi.md) · 🇸🇦 [العربية](README.ar.md) · 🇫🇷 [Français](README.fr.md)

## 组成

| 层 | 运行内容 |
|---|---|
| 手机 (Android 12, Magisk) | `kabuk` 命令行、挂载管理、watchdog、局域网防火墙、充电上限 |
| chroot (Alpine 3.24, aarch64) | `tailscaled`、`sshd`、`taildrop`、`ttyd`、`ntfy`、`kabuk-agent`、Claude Code |
| 客户端 | Android 终端应用 (mosh/ssh, 多标签) + 浏览器界面 |

参考设备:**Xiaomi Redmi Note 10 5G**(`camellia`,Dimensity 700,4 GB RAM),
Android 12 / MIUI 13,Magisk 31.0,以此配置作为日常开发服务器运行。

## 状态

| | |
|---|---|
| 设备 | Redmi Note 10 5G — M2103K19C,`camellia` |
| Root | Magisk 31.0,原厂 boot 镜像,SELinux enforcing |
| 运行环境 | Alpine 3.24.4 aarch64,真正的 `chroot`,用户 `dev` (uid 8000) |
| 进程监管 | runit (`runsvdir`),6 个服务 |
| 网络 | 仅 Tailscale — 局域网防火墙丢弃 `wlan+`/`eth+` 上的所有新连接 |
| 电源 | 电量维持 75–80 %,40 °C 触发降频 |
| 访问 | ssh、mosh、sftp、taildrop、网页终端 (7681)、Agent 界面 (8787) |
| 文档 | 12 章,12 种语言 |

## 特性

* **真正的 chroot,不是模拟。** `git`、`npm`、`tmux`、PTY、`rm -rf .git` 与普通 Linux 主机完全一致。
* **经得起重启、换网和锁屏。** Magisk 在解锁屏幕之前启动运行环境;mosh 会话在 Wi-Fi 与 LTE 之间切换时不会断开;watchdog 在 30 秒内修复 Tailscale 路由规则。
* **Claude Code 的统一聊天界面。** `kabuk-agent` 为每个会话启动 `claude`,通过 SSE 推送事件,先落盘再发送,断线重连不丢内容,下一条消息用 `--resume` 继续。
* **对电池友好。** 充电窗口、仅在有任务时持有 wakelock、超过 40 °C 限制到小核并降优先级、断网自动重置 Wi-Fi。
* **不暴露任何东西。** 所有服务只绑定 tailnet 接口;局域网防火墙是第二道防线;Agent 对每个请求都用 `tailscale whois` 鉴权。
* **紧急停止。** `kabuk disable` 立即切断所有远程访问,正在运行的工作不受影响。

## 快速开始

```sh
# 在手机上(只需一次)
# 1. 安装 Kabuk Server,授予 root,点 "Kur / Onar"
# 2. 让节点加入 tailnet
su -c '/data/kabuk/bin/kabuk ts-up'
# 3. 添加你的公钥
su -c '/data/kabuk/bin/kabuk authkey "ssh-ed25519 AAAA... you@laptop"'
# 4. 验证
su -c '/data/kabuk/bin/kabuk test'
```

```sh
# 在笔记本上
ssh dev@note10 -t kabuk-session     # 持久 tmux + Claude Code
mosh dev@note10 -- kabuk-session    # 同上,换网不中断
open http://note10:8787               # Claude Code 聊天界面
```

完整步骤:[docs/03-install.md](docs/03-install.md)。

## 仓库结构

```text
runtime/         部署到 /data/kabuk 的目录树
  bin/           kabuk 命令行、boot、enter、servermode
  etc/           kabuk.conf
  magisk/        module.prop, service.sh, uninstall.sh
  rootfs/        chroot 辅助脚本 + Agent (server.js, index.html, presets.json)
  sv/            runit 服务定义
  tests/         stage2.sh
apps/
  client/        recovered Java (client)
  server/        recovered Java (server)
  prebuilt/      signed APK + SHA-256
docs/            01 架构 … 12 设计决策
```

## 文档

| # | 章节 |
|---|---|
| 01 | [架构](docs/01-architecture.md) |
| 02 | [环境要求](docs/02-requirements.md) |
| 03 | [安装](docs/03-install.md) |
| 04 | [命令行参考](docs/04-cli-reference.md) |
| 05 | [服务](docs/05-services.md) |
| 06 | [远程访问](docs/06-remote-access.md) |
| 07 | [安全](docs/07-security.md) |
| 08 | [Kabuk Agent](docs/08-agent.md) |
| 09 | [Kabuk 客户端](docs/09-client-app.md) |
| 10 | [Kabuk 服务器](docs/10-server-app.md) |
| 11 | [故障排查](docs/11-troubleshooting.md) |
| 12 | [设计决策](docs/12-design-decisions.md) |

## 一段话讲安全

这台手机自 2022 年 4 月起没有安全更新,因此绝不能从不受信任的网络访问。
它也确实不可达:Wi-Fi、以太网、移动数据上都没有入站端口,没有密码认证,没有
`0.0.0.0` 绑定,没有云端中继。一切都经由你自己的 Tailscale tailnet,Agent 对每个
请求都重新核对 tailnet 所有者。chroot 是打包边界,不是沙箱 —— 启用 `FULL_ROOT=1`
或 Agent 的 `full` 权限模式之前请阅读 [docs/07-security.md](docs/07-security.md)。

## 许可证

MIT — 见 [LICENSE](LICENSE)。第三方组件(Termux terminal-view、musl、mosh、
OpenSSH、Alpine 软件包、Tailscale、ttyd、ntfy)列于 [NOTICE.md](NOTICE.md),各自保留其许可证。

与 Google、Xiaomi、Magisk 或 Anthropic 无关。root 会使保修失效;让手机长期插电
7×24 运行是使用者的责任。