# Kabuk

**Rootlu bir Android telefonu 7/24 çalışan bir Claude Code sunucusuna çevirin — ve ona Tailscale üzerinden her yerden erişin.**

Bulut yok, röle yok, açık port yok. Sunucu telefonun kendisi: Alpine chroot içinde
OpenSSH, mosh, tmux, web terminali, bildirimler ve Claude Code çalışır; Magisk ile
başlayan bir watchdog şarjı, sıcaklığı ve bağlantıyı yönetir; Android istemcisi
Termux ya da root gerektirmeden gerçek bir terminal verir.

> 🇬🇧 [English](README.md) · 🇨🇳 [简体中文](README.zh-CN.md) · 🇷🇺 [Русский](README.ru.md) · 🇩🇪 [Deutsch](README.de.md) · 🇵🇹 [Português (BR)](README.pt-BR.md) · 🇮🇳 [हिन्दी](README.hi.md) · 🇮🇩 [Bahasa Indonesia](README.id.md) · 🇪🇸 [Español](README.es.md) · 🇻🇳 [Tiếng Việt](README.vi.md) · 🇸🇦 [العربية](README.ar.md) · 🇫🇷 [Français](README.fr.md)

## Ne var?

| Katman | Çalışanlar |
|---|---|
| Telefon (Android 12, Magisk) | `kabuk` CLI, mount yöneticisi, watchdog, LAN güvenlik duvarı, şarj sınırı |
| chroot (Alpine 3.24, aarch64) | `tailscaled`, `sshd`, `taildrop`, `ttyd`, `ntfy`, `kabuk-agent`, Claude Code |
| İstemci | Android terminal uygulaması (mosh/ssh, çok sekme) + tarayıcı arayüzleri |

Referans cihaz: **Xiaomi Redmi Note 10 5G** (`camellia`, Dimensity 700, 4 GB RAM),
Android 12 / MIUI 13, Magisk 31.0. Bu yapılandırmayla günlük geliştirme sunucusu
olarak kullanılıyor.

## Durum

| | |
|---|---|
| Cihaz | Redmi Note 10 5G — M2103K19C, `camellia` |
| Root | Magisk 31.0, stok boot imajı, SELinux enforcing |
| Çalışma ortamı | Alpine 3.24.4 aarch64, gerçek `chroot`, kullanıcı `dev` (uid 8000) |
| Süreç denetimi | runit (`runsvdir`), 6 servis |
| Ağ | yalnız Tailscale — LAN güvenlik duvarı `wlan+`/`eth+` üzerindeki yeni bağlantıları düşürür |
| Güç | şarj %75–80 aralığında tutulur, 40 °C'de termal kısıtlama |
| Erişim | ssh, mosh, sftp, taildrop, web terminali (7681), ajan arayüzü (8787) |
| Belgeler | 12 bölüm, 12 dil |

## Özellikler

* **Gerçek chroot, emülasyon değil.** `git`, `npm`, `tmux`, PTY'ler ve
  `rm -rf .git` normal bir Linux makinesindeki gibi çalışır.
* **Yeniden başlatmaya, ağ değişimine ve ekran kilidine dayanır.** Magisk runtime'ı
  ekran kilidini açmadan başlatır; mosh oturumları Wi-Fi/LTE geçişinde yaşar;
  watchdog Tailscale yönlendirme kuralını 30 saniye içinde onarır.
* **Claude Code için tek sohbet arayüzü.** `kabuk-agent` her sohbet için `claude`
  başlatır, olayları SSE ile akıtır, diske yazdığı için bağlantı kopsa da kayıp
  olmaz ve sonraki mesajda oturumu `--resume` ile sürdürür.
* **Pil dostu.** Şarj penceresi, yalnız iş varken wakelock, 40 °C üzerinde
  cpuset + nice, ağ yoksa Wi-Fi reset.
* **Hiçbir şey açıkta değil.** Tüm servisler tailnet arayüzüne bağlanır; LAN
  güvenlik duvarı ikinci savunma hattıdır; ajan her isteği `tailscale whois` ile
  doğrular.
* **Acil durdurma.** `kabuk disable` tüm uzaktan erişimi keser, çalışan işiniz
  yerelde devam eder.

## Hızlı başlangıç

```sh
# telefonda, bir kez
# 1. Kabuk Server kurun, root verin, "Kur / Onar"a basın
# 2. düğümü tailnet'e bağlayın
su -c '/data/kabuk/bin/kabuk ts-up'
# 3. açık anahtarınızı ekleyin
su -c '/data/kabuk/bin/kabuk authkey "ssh-ed25519 AAAA... siz@dizustu"'
# 4. doğrulayın
su -c '/data/kabuk/bin/kabuk test'
```

```sh
# dizüstünden
ssh dev@note10 -t kabuk-session     # kalıcı tmux + Claude Code
mosh dev@note10 -- kabuk-session    # aynısı, ağ değişse de kopmaz
open http://note10:8787               # Claude Code sohbet arayüzü
```

Ayrıntılı kurulum: [docs/03-install.md](docs/03-install.md).

## CLI özeti

```text
kabuk enter [kullanici]     chroot içinde kabuk (çıkışta temizlik)
kabuk run <kullanici> <komut>  chroot içinde tek komut
kabuk status                servisler, pil, mount'lar, wakelock, yedekler
kabuk install               ilk kurulum / onarım
kabuk deploy <dizin>        runtime ağacını telefona gönder
kabuk backup|restore        rootfs yedeği / geri yükleme
kabuk test                  ~20 regresyon testi
kabuk start|stop|restart|enable|disable
kabuk sv <komut> [servis]   runit servislerini yönet
kabuk ts-up|ts              Tailscale girişi / CLI
kabuk authkey <anahtar>     SSH açık anahtarı ekle
kabuk fw on|off|status      LAN güvenlik duvarı
kabuk notify <metin>        Android bildirimi + ntfy
kabuk servermode on|off     sunucu dışı Android uygulamalarını kapat (geri alınabilir)
```

## Depo yapısı

```text
runtime/         /data/kabuk altına kurulan ağaç
  bin/           kabuk CLI, boot, enter, servermode
  etc/           kabuk.conf
  magisk/        module.prop, service.sh, uninstall.sh
  rootfs/        chroot yardımcıları + ajan (server.js, index.html, presets.json)
  sv/            runit servis tanımları
  tests/         stage2.sh
docs/            01 mimari … 12 tasarım kararları
```

## Belgeler

| # | Bölüm |
|---|---|
| 01 | [Mimari](docs/01-architecture.md) |
| 02 | [Gereksinimler](docs/02-requirements.md) |
| 03 | [Kurulum](docs/03-install.md) |
| 04 | [CLI başvurusu](docs/04-cli-reference.md) |
| 05 | [Servisler](docs/05-services.md) |
| 06 | [Uzaktan erişim](docs/06-remote-access.md) |
| 07 | [Güvenlik](docs/07-security.md) |
| 08 | [Kabuk Ajan](docs/08-agent.md) |
| 09 | [Kabuk İstemci](docs/09-client-app.md) |
| 10 | [Kabuk Sunucu](docs/10-server-app.md) |
| 11 | [Sorun giderme](docs/11-troubleshooting.md) |
| 12 | [Tasarım kararları](docs/12-design-decisions.md) |

## Güvenlik, tek paragraf

Telefon Nisan 2022'den beri güvenlik güncellemesi almıyor; bu yüzden güvenilmeyen
ağlardan erişilebilir olmamalı. Hiçbir yerden erişilemiyor: Wi-Fi, Ethernet veya
mobil veride gelen port yok, parola ile kimlik doğrulama yok, `0.0.0.0`'a bağlanan
servis yok, bulut rölesi yok. Her şey kendi Tailscale tailnet'inizden geçer ve ajan
her isteği tailnet sahibine karşı yeniden doğrular. chroot bir paketleme sınırıdır,
sandbox değildir — `FULL_ROOT=1` veya ajanın `full` izin modunu açmadan önce
[docs/07-security.md](docs/07-security.md) okuyun.

## Lisans

MIT — [LICENSE](LICENSE). Üçüncü taraf bileşenler (Termux terminal-view, musl,
mosh, OpenSSH, Alpine paketleri, Tailscale, ttyd, ntfy) [NOTICE.md](NOTICE.md)
içinde listelenmiştir ve kendi lisanslarını korur.

Google, Xiaomi, Magisk veya Anthropic ile bağlantılı değildir. Root garanti dışı
bırakır; şarjda 7/24 çalışan bir telefon sizin sorumluluğunuzdadır.