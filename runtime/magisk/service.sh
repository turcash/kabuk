#!/system/bin/sh
# Magisk late_start service: kilit acilmadan calisir. Bloklamamak icin arka plana at.
[ -f /data/kabuk/DISABLED ] && exit 0
[ -x /data/kabuk/bin/kabuk-boot ] || exit 0
setsid /system/bin/sh /data/kabuk/bin/kabuk-boot </dev/null >/dev/null 2>&1 &
