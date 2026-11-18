package com.kabuk.server;

import android.os.Handler;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.io.encoding.Base64;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

/* compiled from: MainActivity.kt */
@Metadata(d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "invoke"}, k = Base64.bytesPerGroup, mv = {1, 9, 0}, xi = 48)
/* loaded from: classes.dex */
final class MainActivity$onCreate$6 extends Lambda implements Function0<Unit> {
    final /* synthetic */ MainActivity this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    MainActivity$onCreate$6(MainActivity mainActivity) {
        super(0);
        this.this$0 = mainActivity;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void invoke$lambda$0(MainActivity this$0) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.addKeyDialog();
    }

    @Override // kotlin.jvm.functions.Function0
    public /* bridge */ /* synthetic */ Unit invoke() {
        invoke2();
        return Unit.INSTANCE;
    }

    /* renamed from: invoke, reason: avoid collision after fix types in other method */
    public final void invoke2() {
        Handler handler;
        handler = this.this$0.ui;
        final MainActivity mainActivity = this.this$0;
        handler.post(new Runnable() { // from class: com.kabuk.server.MainActivity$onCreate$6$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity$onCreate$6.invoke$lambda$0(MainActivity.this);
            }
        });
    }
}
