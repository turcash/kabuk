package com.kabuk.server;

import android.os.Handler;
import com.kabuk.core.RootShell;
import com.kabuk.core.ShellResult;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: MainActivity.kt */
@Metadata(d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, d2 = {"com/kabuk/server/MainActivity$tick$1", "Ljava/lang/Runnable;", "run", "", "server_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
/* loaded from: classes.dex */
public final class MainActivity$tick$1 implements Runnable {
    final /* synthetic */ MainActivity this$0;

    MainActivity$tick$1(MainActivity mainActivity) {
        this.this$0 = mainActivity;
    }

    @Override // java.lang.Runnable
    public void run() {
        boolean z;
        boolean z2;
        Handler handler;
        ExecutorService executorService;
        z = this.this$0.resumed;
        if (z) {
            z2 = this.this$0.busy;
            if (!z2) {
                executorService = this.this$0.io;
                final MainActivity mainActivity = this.this$0;
                executorService.execute(new Runnable() { // from class: com.kabuk.server.MainActivity$tick$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        MainActivity$tick$1.run$lambda$1(MainActivity.this);
                    }
                });
            }
            handler = this.this$0.ui;
            handler.postDelayed(this, 5000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void run$lambda$1(final MainActivity this$0) {
        Handler handler;
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        final ShellResult run$default = RootShell.run$default(RootShell.INSTANCE, "/data/kabuk/bin/kabuk panel", 20L, null, null, 12, null);
        handler = this$0.ui;
        handler.post(new Runnable() { // from class: com.kabuk.server.MainActivity$tick$1$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity$tick$1.run$lambda$1$lambda$0(MainActivity.this, run$default);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void run$lambda$1$lambda$0(MainActivity this$0, ShellResult r) {
        Map parse;
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(r, "$r");
        parse = this$0.parse(r.getOutput());
        this$0.render(parse);
    }
}
