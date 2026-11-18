# Kabuk

**حوّل هاتفًا أندرويد مُروَّتًا (root) إلى خادم Claude Code يعمل 24 ساعة — ووصل إليه من أي مكان عبر Tailscale.**

لا سحابة، لا وسيط، لا منافذ مفتوحة. الخادم هو الهاتف نفسه: داخل chroot من Alpine يعمل
OpenSSH و mosh و tmux وطرفية ويب وإشعارات و Claude Code؛ ومراقب (watchdog) يطلقه Magisk
يعتني بالشحن والحرارة والشبكة؛ وتطبيق أندرويد يمنحك طرفية حقيقية دون Termux ودون root.

> 🇬🇧 [English](README.md) · 🇹🇷 [Türkçe](README.tr.md) · 🇨🇳 [简体中文](README.zh-CN.md) · 🇷🇺 [Русский](README.ru.md) · 🇩🇪 [Deutsch](README.de.md) · 🇵🇹 [Português (BR)](README.pt-BR.md) · 🇮🇳 [हिन्दी](README.hi.md) · 🇮🇩 [Bahasa Indonesia](README.id.md) · 🇪🇸 [Español](README.es.md) · 🇻🇳 [Tiếng Việt](README.vi.md) · 🇫🇷 [Français](README.fr.md)

## المكوّنات

| الطبقة | ما يعمل |
|---|---|
| الهاتف (Android 12، Magisk) | أداة `kabuk`، إدارة التحميل (mount)، المراقب، جدار حماية الشبكة المحلية، حدّ الشحن |
| chroot (Alpine 3.24، aarch64) | `tailscaled`، `sshd`، `taildrop`، `ttyd`، `ntfy`، `kabuk-agent`، Claude Code |
| العميل | تطبيق طرفية أندرويد (mosh/ssh، تبويبات متعددة) + واجهات المتصفح |

الجهاز المرجعي: **Xiaomi Redmi Note 10 5G** (`camellia`، Dimensity 700، ذاكرة 4 GB)،
Android 12 / MIUI 13، Magisk 31.0، ويُستخدم بهذه الإعدادات كخادم تطوير يومي.

## الحالة

| | |
|---|---|
| الجهاز | Redmi Note 10 5G — M2103K19C، `camellia` |
| الروت | Magisk 31.0، صورة boot الأصلية، SELinux enforcing |
| البيئة | Alpine 3.24.4 aarch64، `chroot` حقيقي، المستخدم `dev` (uid 8000) |
| المشرف | runit (`runsvdir`)، 6 خدمات |
| الشبكة | Tailscale فقط — جدار الحماية يُسقط أي اتصال جديد على `wlan+`/`eth+` |
| الطاقة | الشحن بين 75–80 %، تخفيض الأداء عند 40 °C |
| الوصول | ssh، mosh، sftp، taildrop، طرفية ويب (7681)، واجهة الوكيل (8787) |
| التوثيق | 12 فصلًا، 12 لغة |

## المزايا

* **chroot حقيقي لا محاكاة.** `git` و `npm` و `tmux` و PTY و `rm -rf .git` تتصرف كما في أي لينكس عادي.
* **يصمد أمام إعادة التشغيل وتغيّر الشبكة وقفل الشاشة.** يبدأ Magisk البيئة قبل فتح القفل؛ يحافظ mosh على الجلسة عند الانتقال بين Wi-Fi و LTE؛ ويصلح المراقب قاعدة توجيه Tailscale خلال 30 ثانية.
* **واجهة محادثة واحدة لـ Claude Code.** يشغّل `kabuk-agent` الأمر `claude` لكل محادثة، ويبثّ الأحداث عبر SSE ويكتبها على القرص أولًا، فلا يضيع شيء عند انقطاع الاتصال، وتُستأنف الجلسة في الرسالة التالية بـ `--resume`.
* **رفيق للبطارية.** نافذة شحن، wakelock فقط عند وجود عمل، cpuset + nice فوق 40 °C، وإعادة تشغيل Wi-Fi عند فقد الشبكة.
* **لا شيء مكشوف.** كل الخدمات تُربط بواجهة tailnet؛ جدار حماية الشبكة المحلية خط الدفاع الثاني؛ والوكيل يتحقق من كل طلب عبر `tailscale whois`.
* **إيقاف طارئ.** `kabuk disable` يقطع كل وصول بعيد مع إبقاء عملك الجاري.

## البدء السريع

```sh
# على الهاتف، مرة واحدة
# 1. ثبّت Kabuk Server، امنح الروت، اضغط "Kur / Onar"
# 2. أضف العقدة إلى tailnet
su -c '/data/kabuk/bin/kabuk ts-up'
# 3. أضف مفتاحك العام
su -c '/data/kabuk/bin/kabuk authkey "ssh-ed25519 AAAA... you@laptop"'
# 4. تحقّق
su -c '/data/kabuk/bin/kabuk test'
```

```sh
# من الحاسوب المحمول
ssh dev@note10 -t kabuk-session     # جلسة tmux دائمة + Claude Code
mosh dev@note10 -- kabuk-session    # نفس الشيء دون انقطاع عند تغيّر الشبكة
open http://note10:8787               # واجهة محادثة Claude Code
```

الدليل الكامل: [docs/03-install.md](docs/03-install.md).

## بنية المستودع

```text
runtime/         الشجرة التي تُنشر في /data/kabuk
  bin/           أداة kabuk، boot، enter، servermode
  etc/           kabuk.conf
  magisk/        module.prop، service.sh، uninstall.sh
  rootfs/        مساعدات chroot + الوكيل (server.js، index.html، presets.json)
  sv/            تعريفات خدمات runit
  tests/         stage2.sh
apps/
  client/        recovered Java (client)
  server/        recovered Java (server)
  prebuilt/      signed APK + SHA-256
docs/            01 البنية … 12 قرارات التصميم
```

## التوثيق

| # | الفصل |
|---|---|
| 01 | [البنية](docs/01-architecture.md) |
| 02 | [المتطلبات](docs/02-requirements.md) |
| 03 | [التثبيت](docs/03-install.md) |
| 04 | [مرجع الأوامر](docs/04-cli-reference.md) |
| 05 | [الخدمات](docs/05-services.md) |
| 06 | [الوصول البعيد](docs/06-remote-access.md) |
| 07 | [الأمان](docs/07-security.md) |
| 08 | [Kabuk Agent](docs/08-agent.md) |
| 09 | [Kabuk Client](docs/09-client-app.md) |
| 10 | [Kabuk Server](docs/10-server-app.md) |
| 11 | [حل المشكلات](docs/11-troubleshooting.md) |
| 12 | [قرارات التصميم](docs/12-design-decisions.md) |

## الأمان في فقرة واحدة

لم يتلقَّ الهاتف تحديثًا أمنيًا منذ نيسان/أبريل 2022، لذا يجب ألا يكون متاحًا من شبكات
غير موثوقة. وهو كذلك فعلًا: لا منفذ داخل على Wi-Fi أو Ethernet أو بيانات الجوال، ولا
مصادقة بكلمة مرور، ولا ربط بـ `0.0.0.0`، ولا وسيط سحابي. كل شيء يمر عبر tailnet
Tailscale الخاص بك، والوكيل يعيد التحقق من كل طلب مقابل مالك الـ tailnet. الـ chroot
حدّ تعبئة وليس صندوق رمل — اقرأ [docs/07-security.md](docs/07-security.md) قبل تفعيل
`FULL_ROOT=1` أو وضع `full` في الوكيل.

## الترخيص

MIT — راجع [LICENSE](LICENSE). المكوّنات الخارجية (Termux terminal-view، musl، mosh،
OpenSSH، حزم Alpine، Tailscale، ttyd، ntfy) مذكورة في [NOTICE.md](NOTICE.md) وتحتفظ
بتراخيصها.

لا علاقة له بـ Google أو Xiaomi أو Magisk أو Anthropic. الروت يُلغي الضمان، وهاتف
يعمل 24/7 على الشاحن مسؤوليتك أنت.