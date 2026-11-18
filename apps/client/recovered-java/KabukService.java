package com.kabuk;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.IBinder;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.io.CloseableKt;
import kotlin.io.ConstantsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import org.json.JSONObject;

/* compiled from: KabukService.kt */
@Metadata(d1 = {"\u0000W\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003*\u0001\u0006\u0018\u0000 !2\u00020\u0001:\u0001!B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u000e\u001a\u00020\u000fH\u0002J\u0014\u0010\u0010\u001a\u0004\u0018\u00010\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0016J\b\u0010\u0014\u001a\u00020\u000fH\u0016J\b\u0010\u0015\u001a\u00020\u000fH\u0016J\"\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u0019\u001a\u00020\u00172\u0006\u0010\u001a\u001a\u00020\u0017H\u0016J\u0018\u0010\u001b\u001a\u00020\u000f2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001fH\u0002J\u0010\u0010 \u001a\u00020\u000f2\u0006\u0010\u001c\u001a\u00020\u001dH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0007R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000bX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\""}, d2 = {"Lcom/kabuk/KabukService;", "Landroid/app/Service;", "()V", "cm", "Landroid/net/ConnectivityManager;", "netCb", "com/kabuk/KabukService$netCb$1", "Lcom/kabuk/KabukService$netCb$1;", "running", "", "workers", "", "", "Ljava/lang/Thread;", "kickSubscribers", "", "onBind", "Landroid/os/IBinder;", "i", "Landroid/content/Intent;", "onCreate", "onDestroy", "onStartCommand", "", "intent", "flags", "startId", "show", "p", "Lcom/kabuk/Profile;", "o", "Lorg/json/JSONObject;", "subscribe", "Companion", "client_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
/* loaded from: classes.dex */
public final class KabukService extends Service {
    public static final String CH_ALERT = "kabuk-izin";
    public static final String CH_FG = "kabuk-fg";
    public static final String CH_INFO = "kabuk-bitti";

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private ConnectivityManager cm;
    private final Map<String, Thread> workers = new LinkedHashMap();
    private volatile boolean running = true;
    private final KabukService$netCb$1 netCb = new ConnectivityManager.NetworkCallback() { // from class: com.kabuk.KabukService$netCb$1
        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(Network network) {
            Intrinsics.checkNotNullParameter(network, "network");
            Sessions.INSTANCE.onNetworkAvailable();
            KabukService.this.kickSubscribers();
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(Network network) {
            Intrinsics.checkNotNullParameter(network, "network");
            Sessions.INSTANCE.setNetworkUp(false);
        }
    };

    @Override // android.app.Service
    public IBinder onBind(Intent i) {
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        KabukService kabukService = this;
        INSTANCE.channels(kabukService);
        startForeground(1, new Notification.Builder(kabukService, CH_FG).setSmallIcon(R.drawable.ic_stat).setContentTitle("Kabuk").setContentText("Uzak oturumlar ve bildirim koprusu acik").setContentIntent(PendingIntent.getActivity(kabukService, 0, new Intent(kabukService, (Class<?>) TerminalActivity.class), 67108864)).build());
        Sessions.INSTANCE.init(kabukService);
        Object systemService = getSystemService(ConnectivityManager.class);
        Intrinsics.checkNotNullExpressionValue(systemService, "getSystemService(...)");
        ConnectivityManager connectivityManager = (ConnectivityManager) systemService;
        this.cm = connectivityManager;
        if (connectivityManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("cm");
            connectivityManager = null;
        }
        connectivityManager.registerDefaultNetworkCallback(this.netCb);
        kickSubscribers();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        kickSubscribers();
        return 1;
    }

    @Override // android.app.Service
    public void onDestroy() {
        this.running = false;
        try {
            Result.Companion companion = Result.INSTANCE;
            KabukService kabukService = this;
            ConnectivityManager connectivityManager = this.cm;
            if (connectivityManager == null) {
                Intrinsics.throwUninitializedPropertyAccessException("cm");
                connectivityManager = null;
            }
            connectivityManager.unregisterNetworkCallback(this.netCb);
            Result.m10constructorimpl(Unit.INSTANCE);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            Result.m10constructorimpl(ResultKt.createFailure(th));
        }
        Iterator<T> it = this.workers.values().iterator();
        while (it.hasNext()) {
            ((Thread) it.next()).interrupt();
        }
        super.onDestroy();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final synchronized void kickSubscribers() {
        List<Profile> all = Profiles.INSTANCE.all(this);
        ArrayList<Profile> arrayList = new ArrayList();
        for (Object obj : all) {
            if (((Profile) obj).getNotify()) {
                arrayList.add(obj);
            }
        }
        for (final Profile profile : arrayList) {
            Thread thread = this.workers.get(profile.getId());
            if (thread == null || !thread.isAlive()) {
                Map<String, Thread> map = this.workers;
                String id = profile.getId();
                Thread thread2 = new Thread(new Runnable() { // from class: com.kabuk.KabukService$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        KabukService.kickSubscribers$lambda$3(KabukService.this, profile);
                    }
                }, "ntfy-" + profile.getName());
                thread2.setDaemon(true);
                thread2.start();
                map.put(id, thread2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void kickSubscribers$lambda$3(KabukService this$0, Profile p) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(p, "$p");
        this$0.subscribe(p);
    }

    private final void subscribe(Profile p) {
        BufferedReader bufferedReader;
        Throwable th;
        long j;
        String readLine;
        SharedPreferences sharedPreferences = getSharedPreferences("ntfy", 0);
        long j2 = 2000;
        while (this.running) {
            try {
                String hostAddress = InetAddress.getByName(p.getHost()).getHostAddress();
                String string = sharedPreferences.getString("since-" + p.getId(), null);
                if (string == null) {
                    string = "10m";
                }
                URLConnection openConnection = new URL("http://" + hostAddress + ":8090/kabuk/json?since=" + string).openConnection();
                Intrinsics.checkNotNull(openConnection, "null cannot be cast to non-null type java.net.HttpURLConnection");
                HttpURLConnection httpURLConnection = (HttpURLConnection) openConnection;
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setReadTimeout(120000);
                InputStream inputStream = httpURLConnection.getInputStream();
                Intrinsics.checkNotNullExpressionValue(inputStream, "getInputStream(...)");
                Reader inputStreamReader = new InputStreamReader(inputStream, Charsets.UTF_8);
                bufferedReader = inputStreamReader instanceof BufferedReader ? (BufferedReader) inputStreamReader : new BufferedReader(inputStreamReader, ConstantsKt.DEFAULT_BUFFER_SIZE);
                try {
                    BufferedReader bufferedReader2 = bufferedReader;
                    while (this.running && (readLine = bufferedReader2.readLine()) != null) {
                        try {
                            Intrinsics.checkNotNull(readLine);
                            JSONObject jSONObject = new JSONObject(readLine);
                            if (Intrinsics.areEqual(jSONObject.optString("event"), "message")) {
                                sharedPreferences.edit().putString("since-" + p.getId(), jSONObject.getString("id")).apply();
                                show(p, jSONObject);
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            j = 2000;
                            try {
                                throw th;
                            } catch (Throwable th3) {
                                try {
                                    CloseableKt.closeFinally(bufferedReader, th);
                                    throw th3;
                                } catch (Exception e) {
                                    e = e;
                                    j2 = j;
                                    Log.i("KabukNtfy", p.getName() + ": " + e.getMessage());
                                    Thread.sleep(j2);
                                    j2 = Math.min(j2 * 2, 60000L);
                                }
                            }
                        }
                    }
                    Unit unit = Unit.INSTANCE;
                } catch (Throwable th4) {
                    long j3 = j2;
                    th = th4;
                    j = j3;
                }
            } catch (Exception e2) {
                e = e2;
            }
            try {
                try {
                    CloseableKt.closeFinally(bufferedReader, null);
                    j2 = 2000;
                } catch (Exception e3) {
                    e = e3;
                    j2 = 2000;
                    Log.i("KabukNtfy", p.getName() + ": " + e.getMessage());
                    Thread.sleep(j2);
                    j2 = Math.min(j2 * 2, 60000L);
                }
                Thread.sleep(j2);
                j2 = Math.min(j2 * 2, 60000L);
            } catch (InterruptedException unused) {
                return;
            }
        }
    }

    private final void show(Profile p, JSONObject o) {
        KabukService kabukService = this;
        Intent addFlags = new Intent(kabukService, (Class<?>) AgentActivity.class).putExtra("profile", p.getId()).addFlags(805306368);
        Intrinsics.checkNotNullExpressionValue(addFlags, "addFlags(...)");
        Notification build = new Notification.Builder(kabukService, o.optInt("priority", 3) >= 4 ? CH_ALERT : CH_INFO).setSmallIcon(R.drawable.ic_stat).setColor(p.getColor()).setContentTitle(o.optString("title", p.getName())).setContentText(o.optString("message")).setStyle(new Notification.BigTextStyle().bigText(o.optString("message"))).setAutoCancel(true).setContentIntent(PendingIntent.getActivity(kabukService, p.getId().hashCode(), addFlags, 201326592)).build();
        Intrinsics.checkNotNullExpressionValue(build, "build(...)");
        ((NotificationManager) getSystemService(NotificationManager.class)).notify(o.optString("id").hashCode(), build);
    }

    /* compiled from: KabukService.kt */
    @Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u0010\u0010\u000b\u001a\u0004\u0018\u00010\f2\u0006\u0010\t\u001a\u00020\nR\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lcom/kabuk/KabukService$Companion;", "", "()V", "CH_ALERT", "", "CH_FG", "CH_INFO", "channels", "", "c", "Landroid/content/Context;", "start", "Landroid/content/ComponentName;", "client_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final void channels(Context c) {
            Intrinsics.checkNotNullParameter(c, "c");
            NotificationManager notificationManager = (NotificationManager) c.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(KabukService.CH_FG, "Arka plan baglantisi", 1));
            notificationManager.createNotificationChannel(new NotificationChannel(KabukService.CH_ALERT, "Claude izin istiyor", 4));
            notificationManager.createNotificationChannel(new NotificationChannel(KabukService.CH_INFO, "Is bitti", 3));
        }

        public final ComponentName start(Context c) {
            Intrinsics.checkNotNullParameter(c, "c");
            return c.startForegroundService(new Intent(c, (Class<?>) KabukService.class));
        }
    }
}
