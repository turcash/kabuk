#!/system/bin/sh
# Modul Magisk'ten kaldirilinca: sarji ac, wakelock birak. /data/kabuk (projeler) KORUNUR.
echo 0 > /sys/class/power_supply/battery/input_suspend 2>/dev/null
echo kabuk > /sys/power/wake_unlock 2>/dev/null
