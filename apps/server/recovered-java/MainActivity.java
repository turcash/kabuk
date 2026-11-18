package com.kabuk.server;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.kabuk.core.RootShell;
import com.kabuk.core.ShellResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.io.ByteStreamsKt;
import kotlin.io.CloseableKt;
import kotlin.io.FilesKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.MatchResult;
import kotlin.text.Regex;
import kotlin.text.StringsKt;

/* compiled from: MainActivity.kt */
@Metadata(d1 = {"\u0000\u0093\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010$\n\u0000\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\r*\u0001\u0019\u0018\u0000 Q2\u00020\u0001:\u0001QB\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u001d\u001a\u00020\u001eH\u0002J\u0018\u0010\u001f\u001a\u00020\u00122\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020!H\u0002J\b\u0010#\u001a\u00020\u001eH\u0002J\u0018\u0010$\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020!2\u0006\u0010&\u001a\u00020'H\u0002J\u0010\u0010(\u001a\u00020!2\u0006\u0010)\u001a\u00020*H\u0002J\u0012\u0010+\u001a\u00020\u001e2\b\u0010,\u001a\u0004\u0018\u00010-H\u0002J\b\u0010.\u001a\u00020\u001eH\u0002J\u0010\u0010/\u001a\u00020\u00042\u0006\u0010)\u001a\u00020!H\u0002J\b\u00100\u001a\u00020\u001eH\u0002J\u0012\u00101\u001a\u00020\u001e2\b\u00102\u001a\u0004\u0018\u000103H\u0014J\b\u00104\u001a\u00020\u001eH\u0014J\u0010\u00105\u001a\u00020\u001e2\u0006\u00106\u001a\u00020-H\u0014J\b\u00107\u001a\u00020\u001eH\u0014J\b\u00108\u001a\u00020\u001eH\u0014J\u001c\u00109\u001a\u000e\u0012\u0004\u0012\u00020!\u0012\u0004\u0012\u00020!0:2\u0006\u0010)\u001a\u00020!H\u0002J\u0010\u0010;\u001a\u00020<2\u0006\u0010=\u001a\u00020<H\u0002J\u001c\u0010>\u001a\u00020\u001e2\u0012\u0010?\u001a\u000e\u0012\u0004\u0012\u00020!\u0012\u0004\u0012\u00020!0:H\u0002J\"\u0010@\u001a\u00020\u00142\u0006\u0010A\u001a\u00020!2\u0006\u0010=\u001a\u00020!2\b\u0010\"\u001a\u0004\u0018\u00010!H\u0002J\u001e\u0010B\u001a\u00020\u001e2\u0006\u0010C\u001a\u00020!2\f\u0010D\u001a\b\u0012\u0004\u0012\u00020\u001e0EH\u0002J\u0018\u0010F\u001a\u00020\u00142\u0006\u0010G\u001a\u00020!2\u0006\u0010H\u001a\u00020\u0014H\u0002J\u0010\u0010I\u001a\u00020\u00042\u0006\u0010J\u001a\u00020\u0004H\u0002J&\u0010K\u001a\u00020\u001e2\u0006\u0010L\u001a\u00020!2\u0014\b\u0002\u0010M\u001a\u000e\u0012\u0004\u0012\u00020!\u0012\u0004\u0012\u00020!0:H\u0002J\u0010\u0010N\u001a\u00020!2\u0006\u0010O\u001a\u00020'H\u0002J\b\u0010P\u001a\u00020\u001eH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u001b\u0010\b\u001a\u00020\t8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\f\u0010\r\u001a\u0004\b\n\u0010\u000bR\u0016\u0010\u000e\u001a\n \u0010*\u0004\u0018\u00010\u000f0\u000fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082.¢\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u00020\u0019X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u001aR\u000e\u0010\u001b\u001a\u00020\u001cX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006R"}, d2 = {"Lcom/kabuk/server/MainActivity;", "Landroid/app/Activity;", "()V", "busy", "", "buttons", "", "Landroid/widget/Button;", "d", "", "getD", "()F", "d$delegate", "Lkotlin/Lazy;", "io", "Ljava/util/concurrent/ExecutorService;", "kotlin.jvm.PlatformType", "out", "Landroid/widget/TextView;", "panel", "Landroid/widget/LinearLayout;", "resumed", "scroll", "Landroid/widget/ScrollView;", "tick", "com/kabuk/server/MainActivity$tick$1", "Lcom/kabuk/server/MainActivity$tick$1;", "ui", "Landroid/os/Handler;", "addKeyDialog", "", "card", "text", "", "color", "confirmStop", "download", "url", "dst", "Ljava/io/File;", "dur", "s", "", "handleTaskIntent", "i", "Landroid/content/Intent;", "installOrRepair", "line", "logs", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onNewIntent", "intent", "onPause", "onResume", "parse", "", "px", "", "v", "render", "m", "row", "k", "runTask", "label", "action", "Lkotlin/Function0;", "section", "title", "body", "setButtons", "enabled", "sh", "cmd", "env", "sha256", "f", "tsLogin", "Companion", "server_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
/* loaded from: classes.dex */
public final class MainActivity extends Activity {
    public static final String ALPINE_FILE = "alpine-minirootfs-3.24.4-aarch64.tar.gz";
    public static final String ALPINE_SHA256 = "4b14c18f7e90a063b61462ea1462a5732c154bf6adad594974fe6487cd98a8de";
    public static final String ALPINE_URL = "https://dl-cdn.alpinelinux.org/alpine/v3.24/releases/aarch64/alpine-minirootfs-3.24.4-aarch64.tar.gz";
    private boolean busy;
    private TextView out;
    private LinearLayout panel;
    private boolean resumed;
    private ScrollView scroll;
    private final ExecutorService io = Executors.newSingleThreadExecutor();
    private final Handler ui = new Handler(Looper.getMainLooper());
    private final List<Button> buttons = new ArrayList();

    /* renamed from: d$delegate, reason: from kotlin metadata */
    private final Lazy d = LazyKt.lazy(new Function0<Float>() { // from class: com.kabuk.server.MainActivity$d$2
        {
            super(0);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // kotlin.jvm.functions.Function0
        public final Float invoke() {
            return Float.valueOf(MainActivity.this.getResources().getDisplayMetrics().density);
        }
    });
    private final MainActivity$tick$1 tick = new MainActivity$tick$1(this);

    private final float getD() {
        return ((Number) this.d.getValue()).floatValue();
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity mainActivity = this;
        LinearLayout linearLayout = new LinearLayout(mainActivity);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(px(12), px(12), px(12), px(12));
        linearLayout.setBackgroundColor(Color.parseColor("#101418"));
        LinearLayout linearLayout2 = new LinearLayout(mainActivity);
        linearLayout2.setOrientation(1);
        this.panel = linearLayout2;
        linearLayout.addView(linearLayout2);
        LinearLayout onCreate$bar = onCreate$bar(this, linearLayout);
        onCreate$btn$default(onCreate$bar, this, "Servisleri yeniden başlat", null, new Function0<Unit>() { // from class: com.kabuk.server.MainActivity$onCreate$2
            {
                super(0);
            }

            @Override // kotlin.jvm.functions.Function0
            public /* bridge */ /* synthetic */ Unit invoke() {
                invoke2();
                return Unit.INSTANCE;
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2() {
                MainActivity.sh$default(MainActivity.this, "/data/kabuk/bin/kabuk restart", null, 2, null);
            }
        }, 4, null);
        onCreate$btn$default(onCreate$bar, this, "Log'lar", null, new Function0<Unit>() { // from class: com.kabuk.server.MainActivity$onCreate$3
            {
                super(0);
            }

            @Override // kotlin.jvm.functions.Function0
            public /* bridge */ /* synthetic */ Unit invoke() {
                invoke2();
                return Unit.INSTANCE;
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2() {
                MainActivity.this.logs();
            }
        }, 4, null);
        onCreate$btn$default(onCreate$bar, this, "Yedek al", null, new Function0<Unit>() { // from class: com.kabuk.server.MainActivity$onCreate$4
            {
                super(0);
            }

            @Override // kotlin.jvm.functions.Function0
            public /* bridge */ /* synthetic */ Unit invoke() {
                invoke2();
                return Unit.INSTANCE;
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2() {
                MainActivity.sh$default(MainActivity.this, "/data/kabuk/bin/kabuk backup", null, 2, null);
            }
        }, 4, null);
        onCreate$btn$default(onCreate$bar, this, "Test", null, new Function0<Unit>() { // from class: com.kabuk.server.MainActivity$onCreate$5
            {
                super(0);
            }

            @Override // kotlin.jvm.functions.Function0
            public /* bridge */ /* synthetic */ Unit invoke() {
                invoke2();
                return Unit.INSTANCE;
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2() {
                MainActivity.sh$default(MainActivity.this, "/data/kabuk/bin/kabuk test", null, 2, null);
            }
        }, 4, null);
        LinearLayout onCreate$bar2 = onCreate$bar(this, linearLayout);
        onCreate$btn$default(onCreate$bar2, this, "SSH anahtarı ekle", null, new MainActivity$onCreate$6(this), 4, null);
        onCreate$btn$default(onCreate$bar2, this, "Tailscale giriş", null, new Function0<Unit>() { // from class: com.kabuk.server.MainActivity$onCreate$7
            {
                super(0);
            }

            @Override // kotlin.jvm.functions.Function0
            public /* bridge */ /* synthetic */ Unit invoke() {
                invoke2();
                return Unit.INSTANCE;
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2() {
                MainActivity.this.tsLogin();
            }
        }, 4, null);
        onCreate$btn$default(onCreate$bar2, this, "Kur / Onar", null, new Function0<Unit>() { // from class: com.kabuk.server.MainActivity$onCreate$8
            {
                super(0);
            }

            @Override // kotlin.jvm.functions.Function0
            public /* bridge */ /* synthetic */ Unit invoke() {
                invoke2();
                return Unit.INSTANCE;
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2() {
                MainActivity.this.installOrRepair();
            }
        }, 4, null);
        LinearLayout onCreate$bar3 = onCreate$bar(this, linearLayout);
        onCreate$btn(onCreate$bar3, this, "ACİL DURDUR", Integer.valueOf(Color.parseColor("#B3261E")), new MainActivity$onCreate$9(this));
        onCreate$btn(onCreate$bar3, this, "Yeniden aç", Integer.valueOf(Color.parseColor("#2E7D32")), new Function0<Unit>() { // from class: com.kabuk.server.MainActivity$onCreate$10
            {
                super(0);
            }

            @Override // kotlin.jvm.functions.Function0
            public /* bridge */ /* synthetic */ Unit invoke() {
                invoke2();
                return Unit.INSTANCE;
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2() {
                MainActivity.sh$default(MainActivity.this, "/data/kabuk/bin/kabuk enable", null, 2, null);
            }
        });
        TextView textView = new TextView(mainActivity);
        textView.setTypeface(Typeface.MONOSPACE);
        textView.setTextSize(11.0f);
        textView.setTextColor(Color.parseColor("#D7E3EA"));
        textView.setTextIsSelectable(true);
        this.out = textView;
        ScrollView scrollView = new ScrollView(mainActivity);
        TextView textView2 = this.out;
        if (textView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("out");
            textView2 = null;
        }
        scrollView.addView(textView2);
        this.scroll = scrollView;
        linearLayout.addView(scrollView, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        setContentView(linearLayout);
        handleTaskIntent(getIntent());
    }

    private static final LinearLayout onCreate$bar(MainActivity mainActivity, LinearLayout linearLayout) {
        MainActivity mainActivity2 = mainActivity;
        LinearLayout linearLayout2 = new LinearLayout(mainActivity2);
        linearLayout2.setOrientation(0);
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(mainActivity2);
        horizontalScrollView.addView(linearLayout2);
        linearLayout.addView(horizontalScrollView);
        return linearLayout2;
    }

    private static final Button onCreate$btn(LinearLayout linearLayout, final MainActivity mainActivity, final String str, Integer num, final Function0<Unit> function0) {
        Button button = new Button(mainActivity);
        button.setText(str);
        button.setAllCaps(false);
        if (num != null) {
            button.setBackgroundColor(num.intValue());
            button.setTextColor(-1);
        }
        button.setOnClickListener(new View.OnClickListener() { // from class: com.kabuk.server.MainActivity$$ExternalSyntheticLambda7
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.onCreate$btn$lambda$7$lambda$6(MainActivity.this, str, function0, view);
            }
        });
        mainActivity.buttons.add(button);
        linearLayout.addView(button);
        return button;
    }

    static /* synthetic */ Button onCreate$btn$default(LinearLayout linearLayout, MainActivity mainActivity, String str, Integer num, Function0 function0, int r5, Object obj) {
        if ((r5 & 4) != 0) {
            num = null;
        }
        return onCreate$btn(linearLayout, mainActivity, str, num, function0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onCreate$btn$lambda$7$lambda$6(MainActivity this$0, String label, Function0 action, View view) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(label, "$label");
        Intrinsics.checkNotNullParameter(action, "$action");
        this$0.runTask(label, action);
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        this.resumed = true;
        this.ui.removeCallbacks(this.tick);
        this.ui.post(this.tick);
    }

    @Override // android.app.Activity
    protected void onPause() {
        this.resumed = false;
        this.ui.removeCallbacks(this.tick);
        super.onPause();
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        this.io.shutdownNow();
        super.onDestroy();
    }

    @Override // android.app.Activity
    protected void onNewIntent(Intent intent) {
        Intrinsics.checkNotNullParameter(intent, "intent");
        super.onNewIntent(intent);
        handleTaskIntent(intent);
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue
    java.lang.NullPointerException: Cannot invoke "java.util.List.iterator()" because the return value of "jadx.core.dex.visitors.regions.SwitchOverStringVisitor$SwitchData.getNewCases()" is null
    	at jadx.core.dex.visitors.regions.SwitchOverStringVisitor.restoreSwitchOverString(SwitchOverStringVisitor.java:109)
    	at jadx.core.dex.visitors.regions.SwitchOverStringVisitor.visitRegion(SwitchOverStringVisitor.java:66)
    	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:77)
    	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:82)
     */
    private final void handleTaskIntent(Intent i) {
        String stringExtra = i != null ? i.getStringExtra("task") : null;
        if (stringExtra != null) {
            switch (stringExtra.hashCode()) {
                case -1396673086:
                    if (stringExtra.equals("backup")) {
                        runTask("Yedek al", new Function0<Unit>() { // from class: com.kabuk.server.MainActivity$handleTaskIntent$4
                            {
                                super(0);
                            }

                            @Override // kotlin.jvm.functions.Function0
                            public /* bridge */ /* synthetic */ Unit invoke() {
                                invoke2();
                                return Unit.INSTANCE;
                            }

                            /* renamed from: invoke, reason: avoid collision after fix types in other method */
                            public final void invoke2() {
                                MainActivity.sh$default(MainActivity.this, "/data/kabuk/bin/kabuk backup", null, 2, null);
                            }
                        });
                        break;
                    }
                    break;
                case -892481550:
                    if (stringExtra.equals("status")) {
                        runTask("Durum", new Function0<Unit>() { // from class: com.kabuk.server.MainActivity$handleTaskIntent$1
                            {
                                super(0);
                            }

                            @Override // kotlin.jvm.functions.Function0
                            public /* bridge */ /* synthetic */ Unit invoke() {
                                invoke2();
                                return Unit.INSTANCE;
                            }

                            /* renamed from: invoke, reason: avoid collision after fix types in other method */
                            public final void invoke2() {
                                MainActivity.this.line(RootShell.run$default(RootShell.INSTANCE, "/data/kabuk/bin/kabuk status", 0L, null, null, 14, null).getOutput());
                            }
                        });
                        break;
                    }
                    break;
                case 3556498:
                    if (stringExtra.equals("test")) {
                        runTask("Test", new Function0<Unit>() { // from class: com.kabuk.server.MainActivity$handleTaskIntent$3
                            {
                                super(0);
                            }

                            @Override // kotlin.jvm.functions.Function0
                            public /* bridge */ /* synthetic */ Unit invoke() {
                                invoke2();
                                return Unit.INSTANCE;
                            }

                            /* renamed from: invoke, reason: avoid collision after fix types in other method */
                            public final void invoke2() {
                                MainActivity.sh$default(MainActivity.this, "/data/kabuk/bin/kabuk test", null, 2, null);
                            }
                        });
                        break;
                    }
                    break;
                case 1097506319:
                    if (stringExtra.equals("restart")) {
                        runTask("Yeniden başlat", new Function0<Unit>() { // from class: com.kabuk.server.MainActivity$handleTaskIntent$5
                            {
                                super(0);
                            }

                            @Override // kotlin.jvm.functions.Function0
                            public /* bridge */ /* synthetic */ Unit invoke() {
                                invoke2();
                                return Unit.INSTANCE;
                            }

                            /* renamed from: invoke, reason: avoid collision after fix types in other method */
                            public final void invoke2() {
                                MainActivity.sh$default(MainActivity.this, "/data/kabuk/bin/kabuk restart", null, 2, null);
                            }
                        });
                        break;
                    }
                    break;
                case 1957569947:
                    if (stringExtra.equals("install")) {
                        runTask("Kur / Onar", new Function0<Unit>() { // from class: com.kabuk.server.MainActivity$handleTaskIntent$2
                            {
                                super(0);
                            }

                            @Override // kotlin.jvm.functions.Function0
                            public /* bridge */ /* synthetic */ Unit invoke() {
                                invoke2();
                                return Unit.INSTANCE;
                            }

                            /* renamed from: invoke, reason: avoid collision after fix types in other method */
                            public final void invoke2() {
                                MainActivity.this.installOrRepair();
                            }
                        });
                        break;
                    }
                    break;
            }
        }
        if (i != null) {
            i.removeExtra("task");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Map<String, String> parse(String s) {
        List<String> lines = StringsKt.lines(s);
        ArrayList arrayList = new ArrayList();
        for (String str : lines) {
            Integer valueOf = Integer.valueOf(StringsKt.indexOf$default((CharSequence) str, '=', 0, false, 6, (Object) null));
            Pair pair = null;
            if (valueOf.intValue() <= 0) {
                valueOf = null;
            }
            if (valueOf != null) {
                int intValue = valueOf.intValue();
                String substring = str.substring(0, intValue);
                Intrinsics.checkNotNullExpressionValue(substring, "substring(...)");
                String substring2 = str.substring(intValue + 1);
                Intrinsics.checkNotNullExpressionValue(substring2, "substring(...)");
                pair = TuplesKt.to(substring, StringsKt.trim((CharSequence) substring2).toString());
            }
            if (pair != null) {
                arrayList.add(pair);
            }
        }
        return MapsKt.toMap(arrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:142:0x0544  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void render(java.util.Map<java.lang.String, java.lang.String> r31) {
        /*
            Method dump skipped, instructions count: 1364
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.kabuk.server.MainActivity.render(java.util.Map):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void render$lambda$21$lambda$20$lambda$19(MainActivity this$0, String url, View view) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(url, "$url");
        this$0.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
    }

    private final String dur(long s) {
        if (s >= 86400) {
            long j = 86400;
            return (s / j) + " g " + ((s % j) / 3600) + " sa";
        }
        if (s >= 3600) {
            long j2 = 3600;
            return (s / j2) + " sa " + ((s % j2) / 60) + " dk";
        }
        if (s >= 60) {
            return (s / 60) + " dk";
        }
        return s + " sn";
    }

    private final LinearLayout section(String title, LinearLayout body) {
        MainActivity mainActivity = this;
        LinearLayout linearLayout = new LinearLayout(mainActivity);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(px(10), px(6), px(10), px(8));
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(Color.parseColor("#1A2127"));
        gradientDrawable.setCornerRadius(8 * getD());
        linearLayout.setBackground(gradientDrawable);
        TextView textView = new TextView(mainActivity);
        textView.setText(title);
        textView.setTextColor(Color.parseColor("#8B98A5"));
        textView.setTextSize(12.0f);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        linearLayout.addView(textView);
        linearLayout.addView(body);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.setMargins(0, 0, 0, px(6));
        linearLayout.setLayoutParams(layoutParams);
        return linearLayout;
    }

    private final LinearLayout row(String k, String v, String color) {
        MainActivity mainActivity = this;
        LinearLayout linearLayout = new LinearLayout(mainActivity);
        linearLayout.setOrientation(0);
        TextView textView = new TextView(mainActivity);
        textView.setText(k);
        textView.setTextSize(13.0f);
        textView.setTextColor(color != null ? Integer.valueOf(Color.parseColor(color)).intValue() : Color.parseColor("#C9D3DA"));
        linearLayout.addView(textView, new LinearLayout.LayoutParams(0, -2, 0.42f));
        TextView textView2 = new TextView(mainActivity);
        textView2.setText(v);
        textView2.setTextSize(13.0f);
        textView2.setTextColor(-1);
        linearLayout.addView(textView2, new LinearLayout.LayoutParams(0, -2, 0.58f));
        return linearLayout;
    }

    private final TextView card(String text, String color) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextColor(-1);
        textView.setTextSize(15.0f);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setPadding(px(10), px(10), px(10), px(10));
        textView.setBackgroundColor(Color.parseColor(color));
        return textView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void confirmStop() {
        new AlertDialog.Builder(this).setTitle("Acil durdur").setMessage("Tailscale, SSH, mosh, web terminali ve bildirimler kapanır; açık bağlantılar kesilir. tmux'taki işler telefonda sürer. Yeniden açılışta da başlamaz (\"Yeniden aç\" ile geri alınır).").setPositiveButton("DURDUR", new DialogInterface.OnClickListener() { // from class: com.kabuk.server.MainActivity$$ExternalSyntheticLambda6
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int r3) {
                MainActivity.confirmStop$lambda$36(MainActivity.this, dialogInterface, r3);
            }
        }).setNegativeButton("Vazgeç", (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void confirmStop$lambda$36(final MainActivity this$0, DialogInterface dialogInterface, int r2) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.runTask("ACİL DURDUR", new Function0<Unit>() { // from class: com.kabuk.server.MainActivity$confirmStop$1$1
            {
                super(0);
            }

            @Override // kotlin.jvm.functions.Function0
            public /* bridge */ /* synthetic */ Unit invoke() {
                invoke2();
                return Unit.INSTANCE;
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2() {
                MainActivity.sh$default(MainActivity.this, "/data/kabuk/bin/kabuk disable", null, 2, null);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void addKeyDialog() {
        MainActivity mainActivity = this;
        final EditText editText = new EditText(mainActivity);
        editText.setHint("ssh-ed25519 AAAA... veya ecdsa-sha2-nistp256 AAAA...");
        editText.setMinLines(3);
        new AlertDialog.Builder(mainActivity).setTitle("SSH anahtarı ekle (dev)").setView(editText).setPositiveButton("Ekle", new DialogInterface.OnClickListener() { // from class: com.kabuk.server.MainActivity$$ExternalSyntheticLambda1
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int r4) {
                MainActivity.addKeyDialog$lambda$38(editText, this, dialogInterface, r4);
            }
        }).setNegativeButton("Vazgeç", (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void addKeyDialog$lambda$38(EditText e, final MainActivity this$0, DialogInterface dialogInterface, int r9) {
        Intrinsics.checkNotNullParameter(e, "$e");
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        final String replace$default = StringsKt.replace$default(StringsKt.trim((CharSequence) e.getText().toString()).toString(), "\n", " ", false, 4, (Object) null);
        this$0.runTask("Anahtar ekle", new Function0<Unit>() { // from class: com.kabuk.server.MainActivity$addKeyDialog$1$1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(0);
            }

            @Override // kotlin.jvm.functions.Function0
            public /* bridge */ /* synthetic */ Unit invoke() {
                invoke2();
                return Unit.INSTANCE;
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2() {
                MainActivity.sh$default(MainActivity.this, "/data/kabuk/bin/kabuk authkey " + RootShell.INSTANCE.quote(replace$default), null, 2, null);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void tsLogin() {
        ShellResult run$default = RootShell.run$default(RootShell.INSTANCE, "/data/kabuk/bin/kabuk ts-up", 60L, null, null, 12, null);
        line(StringsKt.trimEnd((CharSequence) run$default.getOutput()).toString());
        final MatchResult find$default = Regex.find$default(new Regex("https://login\\.tailscale\\.com/\\S+"), run$default.getOutput(), 0, 2, null);
        if (find$default != null) {
            this.ui.post(new Runnable() { // from class: com.kabuk.server.MainActivity$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    MainActivity.tsLogin$lambda$40$lambda$39(MainActivity.this, find$default);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void tsLogin$lambda$40$lambda$39(MainActivity this$0, MatchResult m) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(m, "$m");
        this$0.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(m.getValue())));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void logs() {
        sh$default(this, "tail -n 40 /data/kabuk/log/boot.log; for s in /data/kabuk/rootfs/var/log/kabuk/*/current; do echo \"--- $s\"; tail -n 8 $s; done", null, 2, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void installOrRepair() {
        File file = new File(getCacheDir(), "runtime");
        FilesKt.deleteRecursively(file);
        file.mkdirs();
        for (String str : CollectionsKt.listOf((Object[]) new String[]{"bin", "magisk", "etc", "sv", "rootfs", "tests"})) {
            String[] list = getAssets().list(str);
            if (list == null) {
                list = new String[0];
            }
            if (!(list.length == 0)) {
                installOrRepair$copy(this, str, new File(file, str));
            }
        }
        RootShell rootShell = RootShell.INSTANCE;
        RootShell rootShell2 = RootShell.INSTANCE;
        String absolutePath = file.getAbsolutePath();
        Intrinsics.checkNotNullExpressionValue(absolutePath, "getAbsolutePath(...)");
        String quote = rootShell2.quote(absolutePath);
        RootShell rootShell3 = RootShell.INSTANCE;
        String absolutePath2 = file.getAbsolutePath();
        Intrinsics.checkNotNullExpressionValue(absolutePath2, "getAbsolutePath(...)");
        ShellResult run$default = RootShell.run$default(rootShell, "/system/bin/sh " + quote + "/bin/kabuk deploy " + rootShell3.quote(absolutePath2), 0L, null, null, 14, null);
        line(StringsKt.trimEnd((CharSequence) run$default.getOutput()).toString());
        FilesKt.deleteRecursively(file);
        if (run$default.getOk()) {
            boolean ok = RootShell.run$default(RootShell.INSTANCE, "[ -x /data/kabuk/rootfs/bin/busybox ]", 0L, null, null, 14, null).getOk();
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            if (!ok) {
                File file2 = new File(getCacheDir(), ALPINE_FILE);
                line("indiriliyor: https://dl-cdn.alpinelinux.org/alpine/v3.24/releases/aarch64/alpine-minirootfs-3.24.4-aarch64.tar.gz");
                download(ALPINE_URL, file2);
                String sha256 = sha256(file2);
                if (!Intrinsics.areEqual(sha256, ALPINE_SHA256)) {
                    line("HATA: sha256 uyuşmuyor (" + sha256 + ")");
                    file2.delete();
                    return;
                } else {
                    line("sha256 doğru");
                    String absolutePath3 = file2.getAbsolutePath();
                    Intrinsics.checkNotNullExpressionValue(absolutePath3, "getAbsolutePath(...)");
                    linkedHashMap.put("KABUK_TARBALL", absolutePath3);
                    linkedHashMap.put("KABUK_SHA256", ALPINE_SHA256);
                }
            }
            sh("/data/kabuk/bin/kabuk install", linkedHashMap);
            new File(getCacheDir(), ALPINE_FILE).delete();
        }
    }

    private static final void installOrRepair$copy(MainActivity mainActivity, String str, File file) {
        String[] list = mainActivity.getAssets().list(str);
        if (list == null) {
            list = new String[0];
        }
        if (list.length == 0) {
            File parentFile = file.getParentFile();
            if (parentFile != null) {
                parentFile.mkdirs();
            }
            FileOutputStream open = mainActivity.getAssets().open(str);
            try {
                InputStream inputStream = open;
                open = new FileOutputStream(file);
                try {
                    Intrinsics.checkNotNull(inputStream);
                    ByteStreamsKt.copyTo$default(inputStream, open, 0, 2, null);
                    CloseableKt.closeFinally(open, null);
                    CloseableKt.closeFinally(open, null);
                } finally {
                }
            } finally {
            }
        } else {
            file.mkdirs();
            for (String str2 : list) {
                installOrRepair$copy(mainActivity, str + "/" + str2, new File(file, str2));
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    static /* synthetic */ void sh$default(MainActivity mainActivity, String str, Map map, int r3, Object obj) {
        if ((r3 & 2) != 0) {
            map = MapsKt.emptyMap();
        }
        mainActivity.sh(str, map);
    }

    private final void sh(String cmd, Map<String, String> env) {
        String str;
        line("$ " + cmd);
        ShellResult run = RootShell.INSTANCE.run(cmd, 1800L, env, new Function1<String, Unit>() { // from class: com.kabuk.server.MainActivity$sh$r$1
            {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(String str2) {
                invoke2(str2);
                return Unit.INSTANCE;
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(String it) {
                Intrinsics.checkNotNullParameter(it, "it");
                MainActivity.this.line(it);
            }
        });
        if (run.getOk()) {
            str = "[tamam]";
        } else {
            str = "[çıkış kodu " + run.getCode() + "]";
        }
        line(str);
    }

    private final void runTask(String label, final Function0<Unit> action) {
        this.busy = true;
        setButtons(false);
        line("\n== " + label);
        this.io.execute(new Runnable() { // from class: com.kabuk.server.MainActivity$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.runTask$lambda$46(Function0.this, this);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void runTask$lambda$46(Function0 action, final MainActivity this$0) {
        Intrinsics.checkNotNullParameter(action, "$action");
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        try {
            action.invoke();
        } catch (Exception e) {
            this$0.line("HATA: " + e.getMessage());
        }
        this$0.busy = false;
        this$0.ui.post(new Runnable() { // from class: com.kabuk.server.MainActivity$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.runTask$lambda$46$lambda$45(MainActivity.this);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void runTask$lambda$46$lambda$45(MainActivity this$0) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.setButtons(true);
    }

    private final boolean setButtons(final boolean enabled) {
        return this.ui.post(new Runnable() { // from class: com.kabuk.server.MainActivity$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.setButtons$lambda$48(MainActivity.this, enabled);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void setButtons$lambda$48(MainActivity this$0, boolean z) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Iterator<T> it = this$0.buttons.iterator();
        while (it.hasNext()) {
            ((Button) it.next()).setEnabled(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean line(final String s) {
        return this.ui.post(new Runnable() { // from class: com.kabuk.server.MainActivity$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.line$lambda$50(s, this);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void line$lambda$50(String s, final MainActivity this$0) {
        Intrinsics.checkNotNullParameter(s, "$s");
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Log.i("KabukSunucu", s);
        TextView textView = this$0.out;
        ScrollView scrollView = null;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("out");
            textView = null;
        }
        textView.append(s + "\n");
        ScrollView scrollView2 = this$0.scroll;
        if (scrollView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("scroll");
        } else {
            scrollView = scrollView2;
        }
        scrollView.post(new Runnable() { // from class: com.kabuk.server.MainActivity$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.line$lambda$50$lambda$49(MainActivity.this);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void line$lambda$50$lambda$49(MainActivity this$0) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        ScrollView scrollView = this$0.scroll;
        if (scrollView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("scroll");
            scrollView = null;
        }
        scrollView.fullScroll(130);
    }

    private final void download(String url, File dst) {
        URLConnection openConnection = new URL(url).openConnection();
        Intrinsics.checkNotNull(openConnection, "null cannot be cast to non-null type java.net.HttpURLConnection");
        HttpURLConnection httpURLConnection = (HttpURLConnection) openConnection;
        httpURLConnection.setConnectTimeout(20000);
        httpURLConnection.setReadTimeout(60000);
        FileOutputStream inputStream = httpURLConnection.getInputStream();
        try {
            InputStream inputStream2 = inputStream;
            inputStream = new FileOutputStream(dst);
            try {
                Intrinsics.checkNotNull(inputStream2);
                ByteStreamsKt.copyTo$default(inputStream2, inputStream, 0, 2, null);
                CloseableKt.closeFinally(inputStream, null);
                CloseableKt.closeFinally(inputStream, null);
            } finally {
            }
        } finally {
        }
    }

    private final String sha256(File f) {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        FileInputStream fileInputStream = new FileInputStream(f);
        try {
            FileInputStream fileInputStream2 = fileInputStream;
            byte[] bArr = new byte[65536];
            while (true) {
                int read = fileInputStream2.read(bArr);
                if (read < 0) {
                    Unit unit = Unit.INSTANCE;
                    CloseableKt.closeFinally(fileInputStream, null);
                    byte[] digest = messageDigest.digest();
                    Intrinsics.checkNotNullExpressionValue(digest, "digest(...)");
                    return ArraysKt.joinToString$default(digest, (CharSequence) "", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) new Function1<Byte, CharSequence>() { // from class: com.kabuk.server.MainActivity$sha256$2
                        public final CharSequence invoke(byte b) {
                            String format = String.format("%02x", Arrays.copyOf(new Object[]{Byte.valueOf(b)}, 1));
                            Intrinsics.checkNotNullExpressionValue(format, "format(...)");
                            return format;
                        }

                        @Override // kotlin.jvm.functions.Function1
                        public /* bridge */ /* synthetic */ CharSequence invoke(Byte b) {
                            return invoke(b.byteValue());
                        }
                    }, 30, (Object) null);
                }
                messageDigest.update(bArr, 0, read);
            }
        } finally {
        }
    }

    private final int px(int v) {
        return (int) (v * getD());
    }
}
