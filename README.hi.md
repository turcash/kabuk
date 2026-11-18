# Kabuk

**रूट किए गए Android फ़ोन को 24×7 चलने वाले Claude Code सर्वर में बदलें — और Tailscale के ज़रिए कहीं से भी उस तक पहुँचें।**

न क्लाउड, न रिले, न कोई खुला पोर्ट। सर्वर खुद फ़ोन है: Alpine chroot में OpenSSH,
mosh, tmux, वेब टर्मिनल, सूचनाएँ और Claude Code चलते हैं; Magisk से शुरू होने वाला
watchdog चार्जिंग, तापमान और नेटवर्क संभालता है; Android क्लाइंट बिना Termux और
बिना root के असली टर्मिनल देता है।

> 🇬🇧 [English](README.md) · 🇹🇷 [Türkçe](README.tr.md) · 🇨🇳 [简体中文](README.zh-CN.md) · 🇷🇺 [Русский](README.ru.md) · 🇩🇪 [Deutsch](README.de.md) · 🇵🇹 [Português (BR)](README.pt-BR.md) · 🇮🇩 [Bahasa Indonesia](README.id.md) · 🇪🇸 [Español](README.es.md) · 🇻🇳 [Tiếng Việt](README.vi.md) · 🇸🇦 [العربية](README.ar.md) · 🇫🇷 [Français](README.fr.md)

## क्या-क्या है

| परत | क्या चलता है |
|---|---|
| फ़ोन (Android 12, Magisk) | `kabuk` CLI, माउंट मैनेजर, watchdog, LAN फ़ायरवॉल, चार्ज सीमा |
| chroot (Alpine 3.24, aarch64) | `tailscaled`, `sshd`, `taildrop`, `ttyd`, `ntfy`, `kabuk-agent`, Claude Code |
| क्लाइंट | Android टर्मिनल ऐप (mosh/ssh, कई टैब) + ब्राउज़र UI |

संदर्भ डिवाइस: **Xiaomi Redmi Note 10 5G** (`camellia`, Dimensity 700, 4 GB RAM),
Android 12 / MIUI 13, Magisk 31.0 — इसी कॉन्फ़िगरेशन में रोज़ के डेवलपमेंट सर्वर की तरह।

## स्थिति

| | |
|---|---|
| डिवाइस | Redmi Note 10 5G — M2103K19C, `camellia` |
| Root | Magisk 31.0, स्टॉक boot इमेज, SELinux enforcing |
| रनटाइम | Alpine 3.24.4 aarch64, असली `chroot`, उपयोगकर्ता `dev` (uid 8000) |
| सुपरवाइज़र | runit (`runsvdir`), 6 सेवाएँ |
| नेटवर्क | केवल Tailscale — LAN फ़ायरवॉल `wlan+`/`eth+` पर नए कनेक्शन गिरा देता है |
| बिजली | चार्ज 75–80 % पर सीमित, 40 °C पर थर्मल कैप |
| पहुँच | ssh, mosh, sftp, taildrop, वेब टर्मिनल (7681), एजेंट UI (8787) |
| दस्तावेज़ | 12 अध्याय, 12 भाषाएँ |

## विशेषताएँ

* **असली chroot, एमुलेशन नहीं।** `git`, `npm`, `tmux`, PTY और `rm -rf .git` सामान्य Linux की तरह व्यवहार करते हैं।
* **रीबूट, नेटवर्क बदलाव और स्क्रीन लॉक के बाद भी चलता रहता है।** Magisk स्क्रीन खुलने से पहले रनटाइम शुरू करता है; Wi-Fi और LTE के बीच बदलाव पर mosh सत्र बचाता है; watchdog 30 सेकंड में Tailscale रूटिंग नियम ठीक कर देता है।
* **Claude Code के लिए एक चैट UI।** `kabuk-agent` हर चैट के लिए `claude` चलाता है, घटनाएँ SSE से भेजता है, पहले डिस्क पर लिखता है — कनेक्शन टूटने पर कुछ नहीं खोता और अगला संदेश `--resume` से जारी रहता है।
* **बैटरी के अनुकूल।** चार्ज विंडो, काम के दौरान ही wakelock, 40 °C से ऊपर cpuset + nice, नेटवर्क जाने पर Wi-Fi रीसेट।
* **कुछ भी खुला नहीं।** सभी सेवाएँ tailnet इंटरफ़ेस पर बाइंड होती हैं; LAN फ़ायरवॉल दूसरी रक्षा पंक्ति है; एजेंट हर अनुरोध `tailscale whois` से जाँचता है।
* **आपातकालीन रोक।** `kabuk disable` सारी रिमोट पहुँच काट देता है, चल रहा काम बना रहता है।

## तेज़ शुरुआत

```sh
# फ़ोन पर, एक बार
# 1. Kabuk Server इंस्टॉल करें, root दें, "Kur / Onar" दबाएँ
# 2. नोड को tailnet में जोड़ें
su -c '/data/kabuk/bin/kabuk ts-up'
# 3. अपनी पब्लिक की जोड़ें
su -c '/data/kabuk/bin/kabuk authkey "ssh-ed25519 AAAA... you@laptop"'
# 4. जाँचें
su -c '/data/kabuk/bin/kabuk test'
```

```sh
# लैपटॉप से
ssh dev@note10 -t kabuk-session     # स्थायी tmux + Claude Code
mosh dev@note10 -- kabuk-session    # वही, नेटवर्क बदलने पर भी नहीं टूटता
open http://note10:8787               # Claude Code चैट UI
```

पूरी विधि: [docs/03-install.md](docs/03-install.md)।

## रिपॉज़िटरी संरचना

```text
runtime/         जो /data/kabuk में तैनात होता है
  bin/           kabuk CLI, boot, enter, servermode
  etc/           kabuk.conf
  magisk/        module.prop, service.sh, uninstall.sh
  rootfs/        chroot हेल्पर + एजेंट (server.js, index.html, presets.json)
  sv/            runit सेवा परिभाषाएँ
  tests/         stage2.sh
apps/
  client/        recovered Java (client)
  server/        recovered Java (server)
  prebuilt/      signed APK + SHA-256
docs/            01 आर्किटेक्चर … 12 डिज़ाइन निर्णय
```

## दस्तावेज़

| # | अध्याय |
|---|---|
| 01 | [आर्किटेक्चर](docs/01-architecture.md) |
| 02 | [आवश्यकताएँ](docs/02-requirements.md) |
| 03 | [इंस्टॉलेशन](docs/03-install.md) |
| 04 | [CLI संदर्भ](docs/04-cli-reference.md) |
| 05 | [सेवाएँ](docs/05-services.md) |
| 06 | [रिमोट एक्सेस](docs/06-remote-access.md) |
| 07 | [सुरक्षा](docs/07-security.md) |
| 08 | [Kabuk Agent](docs/08-agent.md) |
| 09 | [Kabuk Client](docs/09-client-app.md) |
| 10 | [Kabuk Server](docs/10-server-app.md) |
| 11 | [समस्या निवारण](docs/11-troubleshooting.md) |
| 12 | [डिज़ाइन निर्णय](docs/12-design-decisions.md) |

## एक पैराग्राफ़ में सुरक्षा

फ़ोन को अप्रैल 2022 के बाद सुरक्षा अपडेट नहीं मिले, इसलिए वह असुरक्षित नेटवर्क से
पहुँच के योग्य नहीं होना चाहिए। वह है भी नहीं: Wi-Fi, Ethernet या मोबाइल डेटा पर कोई
इनबाउंड पोर्ट नहीं, पासवर्ड लॉगिन नहीं, `0.0.0.0` बाइंड नहीं, क्लाउड रिले नहीं। सब
कुछ आपके Tailscale tailnet से जाता है और एजेंट हर अनुरोध को tailnet owner के विरुद्ध
फिर से जाँचता है। chroot पैकेजिंग सीमा है, सैंडबॉक्स नहीं — `FULL_ROOT=1` या एजेंट का
`full` मोड चालू करने से पहले [docs/07-security.md](docs/07-security.md) पढ़ें।

## लाइसेंस

MIT — देखें [LICENSE](LICENSE)। तृतीय-पक्ष घटक (Termux terminal-view, musl, mosh,
OpenSSH, Alpine पैकेज, Tailscale, ttyd, ntfy) [NOTICE.md](NOTICE.md) में सूचीबद्ध हैं
और अपने लाइसेंस रखते हैं।

Google, Xiaomi, Magisk या Anthropic से कोई संबंध नहीं। रूट करने से वारंटी जाती रहती है;
चार्जर पर 24×7 चलने वाला फ़ोन आपकी ज़िम्मेदारी है।