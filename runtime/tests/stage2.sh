#!/system/bin/sh
# Asama 2 dayaniklilik testleri (root). Kullanim: su -c 'sh asama2.sh'
K=/data/kabuk/bin/kabuk; R=/data/kabuk/rootfs; C=/data/kabuk/etc/kabuk.conf
inroot() { # comm adi -> rootfs icindeki ilk PID
  ls -l /proc/[0-9]*/root 2>/dev/null | grep -E -- "-> $R\$" | sed -E 's|.*/proc/([0-9]+)/root.*|\1|' |
  while read -r p; do [ "$(cat /proc/$p/comm 2>/dev/null)" = "$1" ] && { echo "$p"; break; }; done
}
wpid() { ps -A -o PID,ARGS | grep '[k]abuk watchdog' | awk '{print $1}' | head -1; }
wait_for() { i=0; while [ $i -lt $2 ]; do eval "$1" && { echo "${i}s"; return 0; }; sleep 1; i=$((i+1)); done; echo "ZAMAN ASIMI"; return 1; }
up() { $K sv status 2>/dev/null | grep -q '^run: /etc/kabuk/service/sshd: (pid [0-9]*) [0-9]*s; run: log'; }
FAIL=0; res() { case "$1" in *ASIMI*) FAIL=$((FAIL+1)); echo "FAIL $2 ($1)";; *) echo "OK   $2 ($1)";; esac; }

old=$(inroot sshd); kill -9 "$old"
res "$(wait_for '[ -n "$(inroot sshd)" ] && [ "$(inroot sshd)" != "$old" ] && up' 20)" "sshd olunce runit yeniden baslatir"

kill -9 "$(inroot runsvdir)"
res "$(wait_for '[ -n "$(inroot runsvdir)" ] && up' 50)" "runsvdir olunce watchdog agaci temiz yeniden kurar"
sleep 6
res "$(wait_for 'up' 5)" "yeniden kurulumdan sonra sshd kararli (flap yok)"

w=$(wpid); kill -9 "$w"
res "$(wait_for '[ -n "$(wpid)" ] && [ "$(wpid)" != "$w" ]' 30)" "watchdog olunce devralinir"
sleep 25
B=$(cat /proc/sys/kernel/random/boot_id)
n=$(sed -n "/$B/,\$p" /data/kabuk/log/boot.log | grep -c 'watchdog cikti')
[ "$n" -le 1 ] && res "0s" "watchdog cikis dongusu yok" || res "ASIMI" "watchdog cikis dongusu ($n)"

chroot $R /bin/su dev -c '/bin/sleep 90' & sp=$!
res "$(wait_for 'grep -qw kabuk /sys/power/wake_lock' 45)" "is varken wakelock tutulur"
kill $sp 2>/dev/null; for p in $(ps -A -o UID,PID,NAME | awk '$1==8000 && $3=="sleep"{print $2}'); do kill $p; done
res "$(wait_for '! grep -qw kabuk /sys/power/wake_lock' 45)" "is bitince wakelock birakilir"

cp $C $C.bak; sed -i 's/^HOT_C=.*/HOT_C=25/' $C
res "$(wait_for '[ "$(cat /dev/cpuset/kabuk/cpus)" = 0-3 ]' 45)" "sicaklik esigi: kucuk cekirdeklere kisma"
mv $C.bak $C
res "$(wait_for '[ "$(cat /dev/cpuset/kabuk/cpus)" = 0-7 ]' 45)" "soguyunca tam hiz"

cap=$(cat /sys/class/power_supply/battery/capacity); s=$(cat /sys/class/power_supply/battery/input_suspend)
ok=0
if [ "$cap" -ge 80 ]; then [ "$s" = 1 ] && ok=1
elif [ "$cap" -le 75 ]; then [ "$s" = 0 ] && ok=1
else ok=1; fi   # 76-79: histerezis bolgesi, iki durum da gecerli
if [ $ok = 1 ]; then
  res "0s" "sarj siniri tutarli (pil %$cap, input_suspend=$s)"; else res "ASIMI" "sarj siniri (pil %$cap, input_suspend=$s)"; fi

echo "sonuc: $FAIL hata"; [ $FAIL = 0 ]
