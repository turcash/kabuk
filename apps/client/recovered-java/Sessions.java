package com.kabuk;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.termux.terminal.TerminalSession;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.io.ConstantsKt;
import kotlin.io.TextStreamsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import kotlin.text.MatchResult;
import kotlin.text.Regex;
import kotlin.text.StringsKt;

/* compiled from: Sessions.kt */
@Metadata(d1 = {"\u0000q\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010!\n\u0002\b\u0004\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005*\u0001+\bÆ\u0002\u0018\u00002\u00020\u0001:\u0001?B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\b\u0010)\u001a\u00020\u0012H\u0002J\u0015\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020\bH\u0002¢\u0006\u0002\u0010-J\u000e\u0010.\u001a\u00020\u00192\u0006\u0010,\u001a\u00020\bJ\u0010\u0010/\u001a\u00020\u00192\u0006\u0010,\u001a\u00020\bH\u0002J\u000e\u00100\u001a\u00020\u00192\u0006\u00101\u001a\u00020\u0006J2\u00102\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u0004032\f\u00104\u001a\b\u0012\u0004\u0012\u00020\u0004052\u0006\u00106\u001a\u00020\u00042\u0006\u00107\u001a\u00020\u0004H\u0002J\u0006\u00108\u001a\u00020\u0019J\u0010\u00109\u001a\u00020\b2\b\u0010:\u001a\u0004\u0018\u00010;J\u000e\u0010<\u001a\u00020\u00192\u0006\u0010,\u001a\u00020\bJ\u0018\u0010=\u001a\u00020\u00192\u0006\u0010,\u001a\u00020\b2\u0006\u0010>\u001a\u00020\u0004H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.¢\u0006\u0002\n\u0000R\u001c\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082.¢\u0006\u0002\n\u0000R\u001a\u0010\u0011\u001a\u00020\u0012X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\"\u0010\u0017\u001a\n\u0012\u0004\u0012\u00020\u0019\u0018\u00010\u0018X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001dR(\u0010\u001e\u001a\u0010\u0012\u0004\u0012\u00020 \u0012\u0004\u0012\u00020\u0019\u0018\u00010\u001fX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\"\"\u0004\b#\u0010$R\u0017\u0010%\u001a\b\u0012\u0004\u0012\u00020\b0&¢\u0006\b\n\u0000\u001a\u0004\b'\u0010(¨\u0006@"}, d2 = {"Lcom/kabuk/Sessions;", "", "()V", "TAG", "", "app", "Landroid/content/Context;", "current", "Lcom/kabuk/Tab;", "getCurrent", "()Lcom/kabuk/Tab;", "setCurrent", "(Lcom/kabuk/Tab;)V", "main", "Landroid/os/Handler;", "native", "Lcom/kabuk/NativeEnv;", "networkUp", "", "getNetworkUp", "()Z", "setNetworkUp", "(Z)V", "onChange", "Lkotlin/Function0;", "", "getOnChange", "()Lkotlin/jvm/functions/Function0;", "setOnChange", "(Lkotlin/jvm/functions/Function0;)V", "onScreen", "Lkotlin/Function1;", "Lcom/termux/terminal/TerminalSession;", "getOnScreen", "()Lkotlin/jvm/functions/Function1;", "setOnScreen", "(Lkotlin/jvm/functions/Function1;)V", "tabs", "", "getTabs", "()Ljava/util/List;", "changed", "client", "com/kabuk/Sessions$client$1", "t", "(Lcom/kabuk/Tab;)Lcom/kabuk/Sessions$client$1;", "close", "connect", "init", "ctx", "moshBootstrap", "Lkotlin/Pair;", "common", "", "ip", "session", "onNetworkAvailable", "open", "p", "Lcom/kabuk/Profile;", "reconnect", "scheduleRetry", "why", "MoshUnavailable", "client_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
/* loaded from: classes.dex */
public final class Sessions {
    private static final String TAG = "KabukSessions";
    private static Context app;
    private static Tab current;
    private static NativeEnv native;
    private static Function0<Unit> onChange;
    private static Function1<? super TerminalSession, Unit> onScreen;
    public static final Sessions INSTANCE = new Sessions();
    private static final List<Tab> tabs = new ArrayList();
    private static final Handler main = new Handler(Looper.getMainLooper());
    private static volatile boolean networkUp = true;

    private Sessions() {
    }

    public final List<Tab> getTabs() {
        return tabs;
    }

    public final Tab getCurrent() {
        return current;
    }

    public final void setCurrent(Tab tab) {
        current = tab;
    }

    public final Function0<Unit> getOnChange() {
        return onChange;
    }

    public final void setOnChange(Function0<Unit> function0) {
        onChange = function0;
    }

    public final Function1<TerminalSession, Unit> getOnScreen() {
        return onScreen;
    }

    public final void setOnScreen(Function1<? super TerminalSession, Unit> function1) {
        onScreen = function1;
    }

    public final boolean getNetworkUp() {
        return networkUp;
    }

    public final void setNetworkUp(boolean z) {
        networkUp = z;
    }

    public final void init(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        if (app != null) {
            return;
        }
        Context applicationContext = ctx.getApplicationContext();
        Intrinsics.checkNotNullExpressionValue(applicationContext, "getApplicationContext(...)");
        app = applicationContext;
        Context context = app;
        NativeEnv nativeEnv = null;
        if (context == null) {
            Intrinsics.throwUninitializedPropertyAccessException("app");
            context = null;
        }
        NativeEnv nativeEnv2 = new NativeEnv(context);
        nativeEnv2.prepare();
        native = nativeEnv2;
        KeyAgent keyAgent = KeyAgent.INSTANCE;
        NativeEnv nativeEnv3 = native;
        if (nativeEnv3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("native");
        } else {
            nativeEnv = nativeEnv3;
        }
        keyAgent.start(nativeEnv.getAgentSock());
    }

    public final Tab open(Profile p) {
        Tab tab = new Tab(p);
        tabs.add(tab);
        current = tab;
        connect(tab);
        changed();
        return tab;
    }

    public final void close(Tab t) {
        Intrinsics.checkNotNullParameter(t, "t");
        t.setClosedByUser(true);
        TerminalSession session = t.getSession();
        if (session != null) {
            session.finishIfRunning();
        }
        List<Tab> list = tabs;
        list.remove(t);
        if (Intrinsics.areEqual(current, t)) {
            current = (Tab) CollectionsKt.lastOrNull((List) list);
        }
        changed();
    }

    public final void reconnect(Tab t) {
        Intrinsics.checkNotNullParameter(t, "t");
        t.setRetries(0);
        TerminalSession session = t.getSession();
        if (session != null && session.isRunning()) {
            t.setClosedByUser(true);
            session.finishIfRunning();
            t.setClosedByUser(false);
        }
        connect(t);
    }

    public final void onNetworkAvailable() {
        TerminalSession session;
        networkUp = true;
        List<Tab> list = tabs;
        ArrayList arrayList = new ArrayList();
        for (Object obj : list) {
            Tab tab = (Tab) obj;
            if (tab.getRemote() && !tab.getClosedByUser() && ((session = tab.getSession()) == null || !session.isRunning())) {
                arrayList.add(obj);
            }
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            INSTANCE.connect((Tab) it.next());
        }
    }

    private final boolean changed() {
        return main.post(new Runnable() { // from class: com.kabuk.Sessions$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                Sessions.changed$lambda$4();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void changed$lambda$4() {
        Function0<Unit> function0 = onChange;
        if (function0 != null) {
            function0.invoke();
        }
    }

    private final void connect(final Tab t) {
        final Profile profile = t.getProfile();
        if (profile == null) {
            NativeEnv nativeEnv = native;
            if (nativeEnv == null) {
                Intrinsics.throwUninitializedPropertyAccessException("native");
                nativeEnv = null;
            }
            String path = nativeEnv.getHome().getPath();
            String[] strArr = {"/system/bin/sh", "-l"};
            NativeEnv nativeEnv2 = native;
            if (nativeEnv2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("native");
                nativeEnv2 = null;
            }
            t.setSession(new TerminalSession("/system/bin/sh", path, strArr, NativeEnv.env$default(nativeEnv2, null, 1, null), 5000, client(t)));
            t.setStatus("yerel");
            changed();
            return;
        }
        if (t.getConnecting()) {
            return;
        }
        t.setConnecting(true);
        t.setStatus("baglaniyor");
        changed();
        new Thread(new Runnable() { // from class: com.kabuk.Sessions$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                Sessions.connect$lambda$7(Profile.this, t);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void connect$lambda$7(Profile profile, final Tab t) {
        Pair pair;
        Intrinsics.checkNotNullParameter(t, "$t");
        try {
            String hostAddress = InetAddress.getByName(profile.getHost()).getHostAddress();
            if (hostAddress == null) {
                throw new IllegalStateException(profile.getHost() + " cozulemedi");
            }
            List<String> listOf = CollectionsKt.listOf((Object[]) new String[]{"-p", String.valueOf(profile.getPort()), "-o", "HostKeyAlias=" + profile.getHost(), "-l", profile.getUser()});
            NativeEnv nativeEnv = null;
            if (!t.getUsingSsh()) {
                Pair<String, String> moshBootstrap = INSTANCE.moshBootstrap(listOf, hostAddress, profile.getSession());
                String component1 = moshBootstrap.component1();
                String component2 = moshBootstrap.component2();
                NativeEnv nativeEnv2 = native;
                if (nativeEnv2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("native");
                    nativeEnv2 = null;
                }
                String[] moshClient = nativeEnv2.moshClient(CollectionsKt.listOf((Object[]) new String[]{hostAddress, component1}));
                NativeEnv nativeEnv3 = native;
                if (nativeEnv3 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("native");
                } else {
                    nativeEnv = nativeEnv3;
                }
                pair = TuplesKt.to(moshClient, nativeEnv.env(MapsKt.mapOf(TuplesKt.to("MOSH_KEY", component2), TuplesKt.to("MOSH_PREDICTION_DISPLAY", "adaptive"))));
            } else {
                NativeEnv nativeEnv4 = native;
                if (nativeEnv4 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("native");
                    nativeEnv4 = null;
                }
                String[] ssh = nativeEnv4.ssh(CollectionsKt.plus((Collection) listOf, (Iterable) CollectionsKt.listOf((Object[]) new String[]{"-t", hostAddress, "kabuk-session " + profile.getSession()})));
                NativeEnv nativeEnv5 = native;
                if (nativeEnv5 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("native");
                    nativeEnv5 = null;
                }
                pair = TuplesKt.to(ssh, NativeEnv.env$default(nativeEnv5, null, 1, null));
            }
            final String[] strArr = (String[]) pair.component1();
            final String[] strArr2 = (String[]) pair.component2();
            main.post(new Runnable() { // from class: com.kabuk.Sessions$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    Sessions.connect$lambda$7$lambda$5(Tab.this, strArr, strArr2);
                }
            });
        } catch (Exception e) {
            Log.w(TAG, "baglanti hatasi " + profile.getName() + ": " + e.getMessage());
            t.setConnecting(false);
            if (!t.getUsingSsh() && Intrinsics.areEqual(profile.getMode(), "mosh") && (e instanceof MoshUnavailable)) {
                t.setUsingSsh(true);
                main.post(new Runnable() { // from class: com.kabuk.Sessions$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        Sessions.connect$lambda$7$lambda$6(Tab.this);
                    }
                });
                return;
            }
            Sessions sessions = INSTANCE;
            String message = e.getMessage();
            if (message == null) {
                message = "hata";
            }
            sessions.scheduleRetry(t, message);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void connect$lambda$7$lambda$5(Tab t, String[] argv, String[] env) {
        Intrinsics.checkNotNullParameter(t, "$t");
        Intrinsics.checkNotNullParameter(argv, "$argv");
        Intrinsics.checkNotNullParameter(env, "$env");
        t.setConnecting(false);
        if (t.getClosedByUser()) {
            return;
        }
        Sessions sessions = INSTANCE;
        if (tabs.contains(t)) {
            NativeEnv nativeEnv = native;
            NativeEnv nativeEnv2 = null;
            if (nativeEnv == null) {
                Intrinsics.throwUninitializedPropertyAccessException("native");
                nativeEnv = null;
            }
            String loader = nativeEnv.getLoader();
            NativeEnv nativeEnv3 = native;
            if (nativeEnv3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("native");
            } else {
                nativeEnv2 = nativeEnv3;
            }
            t.setSession(new TerminalSession(loader, nativeEnv2.getHome().getPath(), argv, env, 5000, sessions.client(t)));
            t.setRetries(0);
            t.setStatus(t.getUsingSsh() ? "ssh" : "mosh");
            sessions.changed();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void connect$lambda$7$lambda$6(Tab t) {
        Intrinsics.checkNotNullParameter(t, "$t");
        INSTANCE.connect(t);
    }

    /* compiled from: Sessions.kt */
    @Metadata(d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00060\u0001j\u0002`\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005¨\u0006\u0006"}, d2 = {"Lcom/kabuk/Sessions$MoshUnavailable;", "Ljava/lang/Exception;", "Lkotlin/Exception;", "msg", "", "(Ljava/lang/String;)V", "client_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class MoshUnavailable extends Exception {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public MoshUnavailable(String msg) {
            super(msg);
            Intrinsics.checkNotNullParameter(msg, "msg");
        }
    }

    private final Pair<String, String> moshBootstrap(List<String> common, String ip, String session) {
        String sb;
        NativeEnv nativeEnv = native;
        String str = null;
        if (nativeEnv == null) {
            Intrinsics.throwUninitializedPropertyAccessException("native");
            nativeEnv = null;
        }
        String[] ssh = nativeEnv.ssh(CollectionsKt.plus((Collection) common, (Iterable) CollectionsKt.listOf((Object[]) new String[]{"-n", "-tt", ip, "--", "mosh-server new -c 256 -l LANG=C.UTF-8 -- kabuk-session " + session})));
        ProcessBuilder processBuilder = new ProcessBuilder((String[]) Arrays.copyOf(ssh, ssh.length));
        processBuilder.environment().clear();
        NativeEnv nativeEnv2 = native;
        if (nativeEnv2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("native");
            nativeEnv2 = null;
        }
        for (String str2 : NativeEnv.env$default(nativeEnv2, null, 1, null)) {
            int indexOf$default = StringsKt.indexOf$default((CharSequence) str2, '=', 0, false, 6, (Object) null);
            Map<String, String> environment = processBuilder.environment();
            Intrinsics.checkNotNullExpressionValue(environment, "environment(...)");
            String substring = str2.substring(0, indexOf$default);
            Intrinsics.checkNotNullExpressionValue(substring, "substring(...)");
            String substring2 = str2.substring(indexOf$default + 1);
            Intrinsics.checkNotNullExpressionValue(substring2, "substring(...)");
            environment.put(substring, substring2);
        }
        processBuilder.redirectErrorStream(true);
        final Process start = processBuilder.start();
        final StringBuilder sb2 = new StringBuilder();
        Thread thread = new Thread(new Runnable() { // from class: com.kabuk.Sessions$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                Sessions.moshBootstrap$lambda$9(start, sb2);
            }
        });
        thread.start();
        if (!start.waitFor(25L, TimeUnit.SECONDS)) {
            start.destroyForcibly();
        }
        thread.join(1000L);
        synchronized (sb2) {
            sb = sb2.toString();
        }
        Intrinsics.checkNotNullExpressionValue(sb, "synchronized(...)");
        String str3 = sb;
        MatchResult find$default = Regex.find$default(new Regex("MOSH CONNECT (\\d+) ([A-Za-z0-9/+]{22})"), str3, 0, 2, null);
        if (find$default == null) {
            if (start.exitValue() != 255) {
                throw new MoshUnavailable(StringsKt.take(sb, 200));
            }
            List<String> lines = StringsKt.lines(str3);
            ListIterator<String> listIterator = lines.listIterator(lines.size());
            while (true) {
                if (!listIterator.hasPrevious()) {
                    break;
                }
                String previous = listIterator.previous();
                if (!StringsKt.isBlank(previous)) {
                    str = previous;
                    break;
                }
            }
            String str4 = str;
            if (str4 == null) {
                str4 = "ssh hatasi";
            }
            throw new IllegalStateException(str4);
        }
        return TuplesKt.to(find$default.getGroupValues().get(1), find$default.getGroupValues().get(2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void moshBootstrap$lambda$9(Process process, final StringBuilder out) {
        Intrinsics.checkNotNullParameter(out, "$out");
        InputStream inputStream = process.getInputStream();
        Intrinsics.checkNotNullExpressionValue(inputStream, "getInputStream(...)");
        Reader inputStreamReader = new InputStreamReader(inputStream, Charsets.UTF_8);
        TextStreamsKt.forEachLine(inputStreamReader instanceof BufferedReader ? (BufferedReader) inputStreamReader : new BufferedReader(inputStreamReader, ConstantsKt.DEFAULT_BUFFER_SIZE), new Function1<String, Unit>() { // from class: com.kabuk.Sessions$moshBootstrap$rd$1$1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(String str) {
                invoke2(str);
                return Unit.INSTANCE;
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(String it) {
                Intrinsics.checkNotNullParameter(it, "it");
                StringBuilder sb = out;
                synchronized (sb) {
                    StringBuilder append = sb.append(it);
                    Intrinsics.checkNotNullExpressionValue(append, "append(...)");
                    Intrinsics.checkNotNullExpressionValue(append.append('\n'), "append(...)");
                    Unit unit = Unit.INSTANCE;
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void scheduleRetry(final Tab t, String why) {
        if (t.getClosedByUser() || !tabs.contains(t)) {
            return;
        }
        t.setRetries(t.getRetries() + 1);
        long min = Math.min(30000L, (1 << Math.min(t.getRetries(), 4)) * 2000);
        t.setStatus("koptu: " + why + " — " + (min / 1000) + " sn sonra tekrar");
        changed();
        main.postDelayed(new Runnable() { // from class: com.kabuk.Sessions$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                Sessions.scheduleRetry$lambda$13(Tab.this);
            }
        }, min);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void scheduleRetry$lambda$13(Tab t) {
        Intrinsics.checkNotNullParameter(t, "$t");
        Sessions sessions = INSTANCE;
        if (networkUp && !t.getClosedByUser() && tabs.contains(t)) {
            TerminalSession session = t.getSession();
            if (session == null || !session.isRunning()) {
                sessions.connect(t);
            }
        }
    }

    private final Sessions$client$1 client(Tab t) {
        return new Sessions$client$1(t);
    }
}
