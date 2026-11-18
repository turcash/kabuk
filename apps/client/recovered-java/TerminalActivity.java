package com.kabuk;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import com.termux.terminal.TerminalEmulator;
import com.termux.terminal.TerminalSession;
import com.termux.view.TerminalView;
import com.termux.view.TerminalViewClient;
import java.util.Iterator;
import java.util.List;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.Charsets;

/* compiled from: TerminalActivity.kt */
@Metadata(d1 = {"\u0000y\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004*\u0001\u0018\u0018\u0000 72\u00020\u0001:\u00017B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001dH\u0002J\b\u0010\u001e\u001a\u00020\u001fH\u0002J\u0012\u0010 \u001a\u00020\u001b2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0002J\u0010\u0010#\u001a\u00020\u001b2\u0006\u0010$\u001a\u00020\u001fH\u0002J\u0012\u0010%\u001a\u00020\u001b2\b\u0010&\u001a\u0004\u0018\u00010'H\u0014J\u0010\u0010(\u001a\u00020\u001b2\u0006\u0010)\u001a\u00020\"H\u0014J\b\u0010*\u001a\u00020\u001bH\u0014J\b\u0010+\u001a\u00020\u001bH\u0014J\b\u0010,\u001a\u00020\u001bH\u0002J\u0010\u0010-\u001a\u00020\u00042\u0006\u0010\u001c\u001a\u00020\u001dH\u0002J\u0010\u0010.\u001a\u00020\u001b2\u0006\u0010!\u001a\u00020\u0016H\u0002J$\u0010/\u001a\u0002002\u0006\u00101\u001a\u0002022\u0012\u00103\u001a\u000e\u0012\u0004\u0012\u00020\u001f\u0012\u0004\u0012\u00020\u001b04H\u0002J\u0010\u00105\u001a\u00020\u001b2\u0006\u00106\u001a\u00020\u0004H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u001b\u0010\n\u001a\u00020\u000b8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u000e\u0010\u000f\u001a\u0004\b\f\u0010\rR\u000e\u0010\u0010\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u00020\u0018X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0019¨\u00068"}, d2 = {"Lcom/kabuk/TerminalActivity;", "Landroid/app/Activity;", "()V", "alt", "", "banner", "Landroid/widget/TextView;", "ctrl", "scrollAcc", "", "scrollDetector", "Landroid/view/GestureDetector;", "getScrollDetector", "()Landroid/view/GestureDetector;", "scrollDetector$delegate", "Lkotlin/Lazy;", "scrolling", "tabBar", "Landroid/widget/LinearLayout;", "term", "Lcom/termux/view/TerminalView;", "textSize", "", "viewClient", "com/kabuk/TerminalActivity$viewClient$1", "Lcom/kabuk/TerminalActivity$viewClient$1;", "cancelToView", "", "ev", "Landroid/view/MotionEvent;", "extraKeys", "Landroid/view/View;", "handleIntent", "i", "Landroid/content/Intent;", "newTabMenu", "anchor", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onNewIntent", "intent", "onPause", "onResume", "refresh", "remoteScroll", "resetMod", "smallBtn", "Landroid/widget/Button;", "label", "", "onClick", "Lkotlin/Function1;", "wheel", "up", "Companion", "client_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
/* loaded from: classes.dex */
public final class TerminalActivity extends Activity {
    public static final String EXTRA_LOCAL = "local";
    public static final String EXTRA_PROFILE = "profile";
    private boolean alt;
    private TextView banner;
    private boolean ctrl;
    private float scrollAcc;
    private boolean scrolling;
    private LinearLayout tabBar;
    private TerminalView term;
    private int textSize;

    /* renamed from: scrollDetector$delegate, reason: from kotlin metadata */
    private final Lazy scrollDetector = LazyKt.lazy(new Function0<GestureDetector>() { // from class: com.kabuk.TerminalActivity$scrollDetector$2
        {
            super(0);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // kotlin.jvm.functions.Function0
        public final GestureDetector invoke() {
            TerminalActivity terminalActivity = TerminalActivity.this;
            final TerminalActivity terminalActivity2 = TerminalActivity.this;
            return new GestureDetector(terminalActivity, new GestureDetector.SimpleOnGestureListener() { // from class: com.kabuk.TerminalActivity$scrollDetector$2.1
                @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx, float dy) {
                    TerminalView terminalView;
                    TerminalView terminalView2;
                    float f;
                    float f2;
                    float f3;
                    float f4;
                    float f5;
                    TerminalEmulator emulator;
                    Intrinsics.checkNotNullParameter(e2, "e2");
                    terminalView = TerminalActivity.this.term;
                    TerminalView terminalView3 = null;
                    if (terminalView == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("term");
                        terminalView = null;
                    }
                    TerminalSession currentSession = terminalView.getCurrentSession();
                    int r2 = (currentSession == null || (emulator = currentSession.getEmulator()) == null) ? 24 : emulator.mRows;
                    terminalView2 = TerminalActivity.this.term;
                    if (terminalView2 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("term");
                    } else {
                        terminalView3 = terminalView2;
                    }
                    float coerceAtLeast = RangesKt.coerceAtLeast(terminalView3.getHeight() / r2, 8.0f);
                    TerminalActivity terminalActivity3 = TerminalActivity.this;
                    f = terminalActivity3.scrollAcc;
                    terminalActivity3.scrollAcc = f + dy;
                    while (true) {
                        f2 = TerminalActivity.this.scrollAcc;
                        if (Math.abs(f2) < coerceAtLeast) {
                            return true;
                        }
                        TerminalActivity terminalActivity4 = TerminalActivity.this;
                        f3 = terminalActivity4.scrollAcc;
                        terminalActivity4.wheel(f3 < 0.0f);
                        TerminalActivity terminalActivity5 = TerminalActivity.this;
                        f4 = terminalActivity5.scrollAcc;
                        f5 = TerminalActivity.this.scrollAcc;
                        terminalActivity5.scrollAcc = f4 + (f5 < 0.0f ? coerceAtLeast : -coerceAtLeast);
                    }
                }

                @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
                public boolean onFling(MotionEvent e1, MotionEvent e2, float vx, float vy) {
                    Intrinsics.checkNotNullParameter(e2, "e2");
                    int coerceAtMost = RangesKt.coerceAtMost((int) (Math.abs(vy) / 400.0f), 40);
                    TerminalActivity terminalActivity3 = TerminalActivity.this;
                    int r0 = 0;
                    while (true) {
                        boolean z = true;
                        if (r0 >= coerceAtMost) {
                            return true;
                        }
                        if (vy <= 0.0f) {
                            z = false;
                        }
                        terminalActivity3.wheel(z);
                        r0++;
                    }
                }
            });
        }
    });
    private final TerminalActivity$viewClient$1 viewClient = new TerminalViewClient() { // from class: com.kabuk.TerminalActivity$viewClient$1
        @Override // com.termux.view.TerminalViewClient
        public void copyModeChanged(boolean copyMode) {
        }

        @Override // com.termux.view.TerminalViewClient
        public boolean isTerminalViewSelected() {
            return true;
        }

        @Override // com.termux.view.TerminalViewClient
        public void logDebug(String tag, String message) {
        }

        @Override // com.termux.view.TerminalViewClient
        public void logError(String tag, String message) {
        }

        @Override // com.termux.view.TerminalViewClient
        public void logInfo(String tag, String message) {
        }

        @Override // com.termux.view.TerminalViewClient
        public void logStackTrace(String tag, Exception e) {
        }

        @Override // com.termux.view.TerminalViewClient
        public void logStackTraceWithMessage(String tag, String message, Exception e) {
        }

        @Override // com.termux.view.TerminalViewClient
        public void logVerbose(String tag, String message) {
        }

        @Override // com.termux.view.TerminalViewClient
        public void logWarn(String tag, String message) {
        }

        @Override // com.termux.view.TerminalViewClient
        public boolean onCodePoint(int codePoint, boolean ctrlDown, TerminalSession session) {
            return false;
        }

        @Override // com.termux.view.TerminalViewClient
        public void onEmulatorSet() {
        }

        @Override // com.termux.view.TerminalViewClient
        public boolean onKeyDown(int keyCode, KeyEvent e, TerminalSession session) {
            return false;
        }

        @Override // com.termux.view.TerminalViewClient
        public boolean onKeyUp(int keyCode, KeyEvent e) {
            return false;
        }

        @Override // com.termux.view.TerminalViewClient
        public boolean onLongPress(MotionEvent event) {
            return false;
        }

        @Override // com.termux.view.TerminalViewClient
        public boolean readFnKey() {
            return false;
        }

        @Override // com.termux.view.TerminalViewClient
        public boolean readShiftKey() {
            return false;
        }

        @Override // com.termux.view.TerminalViewClient
        public boolean shouldBackButtonBeMappedToEscape() {
            return false;
        }

        @Override // com.termux.view.TerminalViewClient
        public boolean shouldEnforceCharBasedInput() {
            return true;
        }

        @Override // com.termux.view.TerminalViewClient
        public boolean shouldUseCtrlSpaceWorkaround() {
            return false;
        }

        @Override // com.termux.view.TerminalViewClient
        public float onScale(float scale) {
            int r1;
            TerminalView terminalView;
            int r0;
            int r02;
            if (scale >= 0.9f && scale <= 1.1f) {
                return scale;
            }
            TerminalActivity terminalActivity = TerminalActivity.this;
            r1 = terminalActivity.textSize;
            terminalActivity.textSize = RangesKt.coerceIn((int) (r1 * scale), 12, 80);
            terminalView = TerminalActivity.this.term;
            if (terminalView == null) {
                Intrinsics.throwUninitializedPropertyAccessException("term");
                terminalView = null;
            }
            r0 = TerminalActivity.this.textSize;
            terminalView.setTextSize(r0);
            SharedPreferences.Editor edit = TerminalActivity.this.getSharedPreferences("ui", 0).edit();
            r02 = TerminalActivity.this.textSize;
            edit.putInt("textSize", r02).apply();
            return 1.0f;
        }

        @Override // com.termux.view.TerminalViewClient
        public void onSingleTapUp(MotionEvent e) {
            TerminalView terminalView;
            TerminalView terminalView2;
            terminalView = TerminalActivity.this.term;
            TerminalView terminalView3 = null;
            if (terminalView == null) {
                Intrinsics.throwUninitializedPropertyAccessException("term");
                terminalView = null;
            }
            terminalView.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) TerminalActivity.this.getSystemService(InputMethodManager.class);
            terminalView2 = TerminalActivity.this.term;
            if (terminalView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("term");
            } else {
                terminalView3 = terminalView2;
            }
            inputMethodManager.showSoftInput(terminalView3, 0);
        }

        @Override // com.termux.view.TerminalViewClient
        public boolean readControlKey() {
            boolean z;
            z = TerminalActivity.this.ctrl;
            if (z) {
                TerminalActivity.this.ctrl = false;
                TerminalActivity.this.resetMod(2);
            }
            return z;
        }

        @Override // com.termux.view.TerminalViewClient
        public boolean readAltKey() {
            boolean z;
            z = TerminalActivity.this.alt;
            if (z) {
                TerminalActivity.this.alt = false;
                TerminalActivity.this.resetMod(3);
            }
            return z;
        }
    };

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TerminalActivity terminalActivity = this;
        KabukService.INSTANCE.start(terminalActivity);
        Sessions.INSTANCE.init(terminalActivity);
        float f = getResources().getDisplayMetrics().density;
        this.textSize = getSharedPreferences("ui", 0).getInt("textSize", (int) (13 * f));
        LinearLayout linearLayout = new LinearLayout(terminalActivity);
        linearLayout.setOrientation(1);
        linearLayout.setBackgroundColor(-16777216);
        LinearLayout linearLayout2 = new LinearLayout(terminalActivity);
        linearLayout2.setOrientation(0);
        linearLayout2.setBackgroundColor(-15394532);
        LinearLayout linearLayout3 = new LinearLayout(terminalActivity);
        linearLayout3.setOrientation(0);
        this.tabBar = linearLayout3;
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(terminalActivity);
        LinearLayout linearLayout4 = this.tabBar;
        TerminalView terminalView = null;
        if (linearLayout4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tabBar");
            linearLayout4 = null;
        }
        horizontalScrollView.addView(linearLayout4);
        linearLayout2.addView(horizontalScrollView, new LinearLayout.LayoutParams(0, -2, 1.0f));
        linearLayout2.addView(smallBtn("+", new Function1<View, Unit>() { // from class: com.kabuk.TerminalActivity$onCreate$3
            {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(View view) {
                invoke2(view);
                return Unit.INSTANCE;
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(View v) {
                Intrinsics.checkNotNullParameter(v, "v");
                TerminalActivity.this.newTabMenu(v);
            }
        }));
        linearLayout2.addView(smallBtn("≡", new Function1<View, Unit>() { // from class: com.kabuk.TerminalActivity$onCreate$4
            {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(View view) {
                invoke2(view);
                return Unit.INSTANCE;
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(View it) {
                Intrinsics.checkNotNullParameter(it, "it");
                TerminalActivity.this.startActivity(new Intent(TerminalActivity.this, (Class<?>) MainActivity.class));
            }
        }));
        linearLayout.addView(linearLayout2);
        TextView textView = new TextView(terminalActivity);
        int r5 = (int) (8 * f);
        int r11 = (int) (2 * f);
        textView.setPadding(r5, r11, r5, r11);
        textView.setTextColor(-1);
        textView.setTextSize(12.0f);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setOnClickListener(new View.OnClickListener() { // from class: com.kabuk.TerminalActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TerminalActivity.onCreate$lambda$6$lambda$5(TerminalActivity.this, view);
            }
        });
        this.banner = textView;
        linearLayout.addView(textView, new LinearLayout.LayoutParams(-1, -2));
        TerminalView terminalView2 = new TerminalView(terminalActivity, null);
        terminalView2.setTerminalViewClient(this.viewClient);
        terminalView2.setTextSize(this.textSize);
        terminalView2.setTypeface(Typeface.MONOSPACE);
        terminalView2.setFocusable(true);
        terminalView2.setFocusableInTouchMode(true);
        terminalView2.setKeepScreenOn(false);
        this.term = terminalView2;
        linearLayout.addView(terminalView2, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        TerminalView terminalView3 = this.term;
        if (terminalView3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("term");
        } else {
            terminalView = terminalView3;
        }
        terminalView.setOnTouchListener(new View.OnTouchListener() { // from class: com.kabuk.TerminalActivity$$ExternalSyntheticLambda4
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean onCreate$lambda$8;
                onCreate$lambda$8 = TerminalActivity.onCreate$lambda$8(TerminalActivity.this, view, motionEvent);
                return onCreate$lambda$8;
            }
        });
        linearLayout.addView(extraKeys());
        setContentView(linearLayout);
        handleIntent(getIntent());
        if (Sessions.INSTANCE.getTabs().isEmpty()) {
            Sessions.INSTANCE.open((Profile) CollectionsKt.firstOrNull((List) Profiles.INSTANCE.all(terminalActivity)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onCreate$lambda$6$lambda$5(TerminalActivity this$0, View view) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Tab current = Sessions.INSTANCE.getCurrent();
        if (current == null || !current.getRemote()) {
            return;
        }
        Sessions.INSTANCE.reconnect(current);
        this$0.refresh();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean onCreate$lambda$8(TerminalActivity this$0, View view, MotionEvent motionEvent) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNull(motionEvent);
        return this$0.remoteScroll(motionEvent);
    }

    @Override // android.app.Activity
    protected void onNewIntent(Intent intent) {
        Intrinsics.checkNotNullParameter(intent, "intent");
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private final void handleIntent(Intent i) {
        Object obj = null;
        if (i == null || !i.getBooleanExtra(EXTRA_LOCAL, false)) {
            if ((i != null ? i.getStringExtra("profile") : null) != null) {
                String stringExtra = i.getStringExtra("profile");
                Intrinsics.checkNotNull(stringExtra);
                Iterator<T> it = Sessions.INSTANCE.getTabs().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Object next = it.next();
                    Profile profile = ((Tab) next).getProfile();
                    if (Intrinsics.areEqual(profile != null ? profile.getId() : null, stringExtra)) {
                        obj = next;
                        break;
                    }
                }
                Tab tab = (Tab) obj;
                if (tab != null) {
                    Sessions.INSTANCE.setCurrent(tab);
                } else {
                    Profile profile2 = Profiles.INSTANCE.get(this, stringExtra);
                    if (profile2 != null) {
                        Sessions.INSTANCE.open(profile2);
                    }
                }
            }
        } else {
            Sessions.INSTANCE.open(null);
        }
        if (i != null) {
            i.removeExtra("profile");
        }
        if (i != null) {
            i.removeExtra(EXTRA_LOCAL);
        }
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        Sessions.INSTANCE.setOnChange(new Function0<Unit>() { // from class: com.kabuk.TerminalActivity$onResume$1
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
                TerminalActivity.this.refresh();
            }
        });
        Sessions.INSTANCE.setOnScreen(new Function1<TerminalSession, Unit>() { // from class: com.kabuk.TerminalActivity$onResume$2
            {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(TerminalSession terminalSession) {
                invoke2(terminalSession);
                return Unit.INSTANCE;
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(TerminalSession s) {
                TerminalView terminalView;
                TerminalView terminalView2;
                Intrinsics.checkNotNullParameter(s, "s");
                terminalView = TerminalActivity.this.term;
                TerminalView terminalView3 = null;
                if (terminalView == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("term");
                    terminalView = null;
                }
                if (Intrinsics.areEqual(terminalView.getCurrentSession(), s)) {
                    terminalView2 = TerminalActivity.this.term;
                    if (terminalView2 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("term");
                    } else {
                        terminalView3 = terminalView2;
                    }
                    terminalView3.onScreenUpdated();
                }
            }
        });
        refresh();
    }

    @Override // android.app.Activity
    protected void onPause() {
        Sessions.INSTANCE.setOnChange(null);
        Sessions.INSTANCE.setOnScreen(null);
        super.onPause();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void refresh() {
        String str;
        float f = getResources().getDisplayMetrics().density;
        LinearLayout linearLayout = this.tabBar;
        TerminalView terminalView = null;
        TextView textView = null;
        if (linearLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tabBar");
            linearLayout = null;
        }
        linearLayout.removeAllViews();
        for (final Tab tab : Sessions.INSTANCE.getTabs()) {
            boolean areEqual = Intrinsics.areEqual(tab, Sessions.INSTANCE.getCurrent());
            LinearLayout linearLayout2 = this.tabBar;
            if (linearLayout2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("tabBar");
                linearLayout2 = null;
            }
            TextView textView2 = new TextView(this);
            textView2.setText((tab.getRemote() ? "● " : "◆ ") + tab.getLabel());
            textView2.setTextColor(areEqual ? -1 : -5195586);
            textView2.setTypeface(areEqual ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
            int r8 = (int) (12 * f);
            int r9 = (int) (8 * f);
            textView2.setPadding(r8, r9, r8, r9);
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(areEqual ? tab.getColor() : (tab.getColor() & 16777215) | 1426063360);
            gradientDrawable.setCornerRadius(6 * f);
            textView2.setBackground(gradientDrawable);
            textView2.setOnClickListener(new View.OnClickListener() { // from class: com.kabuk.TerminalActivity$$ExternalSyntheticLambda5
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    TerminalActivity.refresh$lambda$14$lambda$12(Tab.this, this, view);
                }
            });
            textView2.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.kabuk.TerminalActivity$$ExternalSyntheticLambda6
                @Override // android.view.View.OnLongClickListener
                public final boolean onLongClick(View view) {
                    boolean refresh$lambda$14$lambda$13;
                    refresh$lambda$14$lambda$13 = TerminalActivity.refresh$lambda$14$lambda$13(Tab.this, view);
                    return refresh$lambda$14$lambda$13;
                }
            });
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
            int r82 = (int) (4 * f);
            layoutParams.setMargins((int) (3 * f), r82, 0, r82);
            Unit unit = Unit.INSTANCE;
            linearLayout2.addView(textView2, layoutParams);
        }
        Tab current = Sessions.INSTANCE.getCurrent();
        if (current == null) {
            TextView textView3 = this.banner;
            if (textView3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("banner");
                textView3 = null;
            }
            textView3.setText("Sekme yok — + ile ac");
            TextView textView4 = this.banner;
            if (textView4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("banner");
            } else {
                textView = textView4;
            }
            textView.setBackgroundColor(-13421773);
            return;
        }
        TextView textView5 = this.banner;
        if (textView5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("banner");
            textView5 = null;
        }
        textView5.setBackgroundColor(current.getColor());
        TextView textView6 = this.banner;
        if (textView6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("banner");
            textView6 = null;
        }
        if (current.getRemote()) {
            String label = current.getLabel();
            Profile profile = current.getProfile();
            Intrinsics.checkNotNull(profile);
            String user = profile.getUser();
            String host = current.getProfile().getHost();
            String status = current.getStatus();
            TerminalSession session = current.getSession();
            str = "UZAK: " + label + "  (" + user + "@" + host + ")  ·  " + status + ((session == null || !session.isRunning()) ? "  ·  yeniden baglanmak icin dokun" : "");
        }
        textView6.setText(str);
        TerminalSession session2 = current.getSession();
        if (session2 != null) {
            TerminalView terminalView2 = this.term;
            if (terminalView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("term");
                terminalView2 = null;
            }
            if (Intrinsics.areEqual(terminalView2.getCurrentSession(), session2)) {
                TerminalView terminalView3 = this.term;
                if (terminalView3 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("term");
                    terminalView3 = null;
                }
                terminalView3.onScreenUpdated();
            } else {
                TerminalView terminalView4 = this.term;
                if (terminalView4 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("term");
                    terminalView4 = null;
                }
                terminalView4.attachSession(session2);
            }
        }
        TerminalView terminalView5 = this.term;
        if (terminalView5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("term");
        } else {
            terminalView = terminalView5;
        }
        terminalView.requestFocus();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void refresh$lambda$14$lambda$12(Tab t, TerminalActivity this$0, View view) {
        Intrinsics.checkNotNullParameter(t, "$t");
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Sessions.INSTANCE.setCurrent(t);
        this$0.refresh();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean refresh$lambda$14$lambda$13(Tab t, View view) {
        Intrinsics.checkNotNullParameter(t, "$t");
        Sessions.INSTANCE.close(t);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void newTabMenu(View anchor) {
        TerminalActivity terminalActivity = this;
        PopupMenu popupMenu = new PopupMenu(terminalActivity, anchor);
        popupMenu.getMenu().add(0, 0, 0, "Yerel terminal");
        final List<Profile> all = Profiles.INSTANCE.all(terminalActivity);
        int r2 = 0;
        for (Object obj : all) {
            int r5 = r2 + 1;
            if (r2 < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            Profile profile = (Profile) obj;
            popupMenu.getMenu().add(0, r5, r5, profile.getName() + " (" + profile.getMode() + ")");
            r2 = r5;
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: com.kabuk.TerminalActivity$$ExternalSyntheticLambda0
            @Override // android.widget.PopupMenu.OnMenuItemClickListener
            public final boolean onMenuItemClick(MenuItem menuItem) {
                boolean newTabMenu$lambda$18;
                newTabMenu$lambda$18 = TerminalActivity.newTabMenu$lambda$18(all, menuItem);
                return newTabMenu$lambda$18;
            }
        });
        popupMenu.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean newTabMenu$lambda$18(List ps, MenuItem menuItem) {
        Intrinsics.checkNotNullParameter(ps, "$ps");
        if (menuItem.getItemId() == 0) {
            Sessions.INSTANCE.open(null);
        } else {
            Sessions.INSTANCE.open((Profile) ps.get(menuItem.getItemId() - 1));
        }
        return true;
    }

    private final Button smallBtn(String label, final Function1<? super View, Unit> onClick) {
        Button button = new Button(this);
        button.setText(label);
        button.setAllCaps(false);
        button.setMinWidth(0);
        button.setMinimumWidth(0);
        button.setOnClickListener(new View.OnClickListener() { // from class: com.kabuk.TerminalActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TerminalActivity.smallBtn$lambda$20$lambda$19(Function1.this, view);
            }
        });
        return button;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void smallBtn$lambda$20$lambda$19(Function1 tmp0, View view) {
        Intrinsics.checkNotNullParameter(tmp0, "$tmp0");
        tmp0.invoke(view);
    }

    private final GestureDetector getScrollDetector() {
        return (GestureDetector) this.scrollDetector.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void wheel(boolean up) {
        TerminalSession session;
        Tab current = Sessions.INSTANCE.getCurrent();
        if (current == null || (session = current.getSession()) == null) {
            return;
        }
        byte[] bytes = ("\u001b[<" + (up ? 64 : 65) + ";1;1M").getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        session.write(bytes, 0, bytes.length);
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x002b, code lost:
    
        if (r3 != 3) goto L26;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final boolean remoteScroll(android.view.MotionEvent r6) {
        /*
            r5 = this;
            com.kabuk.Sessions r0 = com.kabuk.Sessions.INSTANCE
            com.kabuk.Tab r0 = r0.getCurrent()
            r1 = 0
            if (r0 == 0) goto L4a
            boolean r0 = r0.getRemote()
            r2 = 1
            if (r0 != r2) goto L4a
            int r0 = r6.getPointerCount()
            if (r0 <= r2) goto L17
            goto L4a
        L17:
            android.view.GestureDetector r0 = r5.getScrollDetector()
            boolean r0 = r0.onTouchEvent(r6)
            int r3 = r6.getActionMasked()
            if (r3 == 0) goto L44
            if (r3 == r2) goto L3a
            r4 = 2
            if (r3 == r4) goto L2e
            r6 = 3
            if (r3 == r6) goto L3a
            goto L41
        L2e:
            if (r0 == 0) goto L41
            boolean r0 = r5.scrolling
            if (r0 != 0) goto L39
            r5.scrolling = r2
            r5.cancelToView(r6)
        L39:
            return r2
        L3a:
            boolean r6 = r5.scrolling
            if (r6 == 0) goto L41
            r5.scrolling = r1
            return r2
        L41:
            boolean r6 = r5.scrolling
            return r6
        L44:
            r5.scrolling = r1
            r6 = 0
            r5.scrollAcc = r6
            return r1
        L4a:
            r5.scrolling = r1
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.kabuk.TerminalActivity.remoteScroll(android.view.MotionEvent):boolean");
    }

    private final void cancelToView(MotionEvent ev) {
        MotionEvent obtain = MotionEvent.obtain(ev);
        obtain.setAction(3);
        TerminalView terminalView = this.term;
        if (terminalView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("term");
            terminalView = null;
        }
        terminalView.onTouchEvent(obtain);
        obtain.recycle();
    }

    private final View extraKeys() {
        final LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(0);
        linearLayout.setBackgroundColor(-15394532);
        extraKeys$key(linearLayout, this, "ESC", new Function0<Unit>() { // from class: com.kabuk.TerminalActivity$extraKeys$1
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
                TerminalActivity.extraKeys$send(TerminalActivity.this, 111);
            }
        });
        extraKeys$key(linearLayout, this, "TAB", new Function0<Unit>() { // from class: com.kabuk.TerminalActivity$extraKeys$2
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
                TerminalActivity.extraKeys$send(TerminalActivity.this, 61);
            }
        });
        extraKeys$key(linearLayout, this, "CTRL", new Function0<Unit>() { // from class: com.kabuk.TerminalActivity$extraKeys$3
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
                boolean z;
                boolean z2;
                TerminalActivity terminalActivity = TerminalActivity.this;
                z = terminalActivity.ctrl;
                terminalActivity.ctrl = !z;
                View childAt = linearLayout.getChildAt(2);
                z2 = TerminalActivity.this.ctrl;
                childAt.setBackgroundColor(z2 ? -12874032 : 0);
            }
        });
        extraKeys$key(linearLayout, this, "ALT", new Function0<Unit>() { // from class: com.kabuk.TerminalActivity$extraKeys$4
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
                boolean z;
                boolean z2;
                TerminalActivity terminalActivity = TerminalActivity.this;
                z = terminalActivity.alt;
                terminalActivity.alt = !z;
                View childAt = linearLayout.getChildAt(3);
                z2 = TerminalActivity.this.alt;
                childAt.setBackgroundColor(z2 ? -12874032 : 0);
            }
        });
        extraKeys$key(linearLayout, this, "←", new Function0<Unit>() { // from class: com.kabuk.TerminalActivity$extraKeys$5
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
                TerminalActivity.extraKeys$send(TerminalActivity.this, 21);
            }
        });
        extraKeys$key(linearLayout, this, "↓", new Function0<Unit>() { // from class: com.kabuk.TerminalActivity$extraKeys$6
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
                TerminalActivity.extraKeys$send(TerminalActivity.this, 20);
            }
        });
        extraKeys$key(linearLayout, this, "↑", new Function0<Unit>() { // from class: com.kabuk.TerminalActivity$extraKeys$7
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
                TerminalActivity.extraKeys$send(TerminalActivity.this, 19);
            }
        });
        extraKeys$key(linearLayout, this, "→", new Function0<Unit>() { // from class: com.kabuk.TerminalActivity$extraKeys$8
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
                TerminalActivity.extraKeys$send(TerminalActivity.this, 22);
            }
        });
        return linearLayout;
    }

    private static final void extraKeys$key(LinearLayout linearLayout, TerminalActivity terminalActivity, String str, final Function0<Unit> function0) {
        TextView textView = new TextView(terminalActivity);
        textView.setText(str);
        textView.setGravity(17);
        textView.setTextColor(-1);
        textView.setTextSize(14.0f);
        textView.setPadding(0, 22, 0, 22);
        textView.setOnClickListener(new View.OnClickListener() { // from class: com.kabuk.TerminalActivity$$ExternalSyntheticLambda7
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TerminalActivity.extraKeys$key$lambda$23$lambda$22(Function0.this, view);
            }
        });
        linearLayout.addView(textView, new LinearLayout.LayoutParams(0, -2, 1.0f));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void extraKeys$key$lambda$23$lambda$22(Function0 act, View view) {
        Intrinsics.checkNotNullParameter(act, "$act");
        act.invoke();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void extraKeys$send(TerminalActivity terminalActivity, int r3) {
        TerminalView terminalView = terminalActivity.term;
        if (terminalView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("term");
            terminalView = null;
        }
        terminalView.onKeyDown(r3, new KeyEvent(0, r3));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void resetMod(final int i) {
        runOnUiThread(new Runnable() { // from class: com.kabuk.TerminalActivity$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                TerminalActivity.resetMod$lambda$24(TerminalActivity.this, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void resetMod$lambda$24(TerminalActivity this$0, int r5) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        TerminalView terminalView = this$0.term;
        TerminalView terminalView2 = null;
        if (terminalView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("term");
            terminalView = null;
        }
        ViewParent parent = terminalView.getParent();
        Intrinsics.checkNotNull(parent, "null cannot be cast to non-null type android.widget.LinearLayout");
        LinearLayout linearLayout = (LinearLayout) parent;
        TerminalView terminalView3 = this$0.term;
        if (terminalView3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("term");
        } else {
            terminalView2 = terminalView3;
        }
        Intrinsics.checkNotNull(terminalView2.getParent(), "null cannot be cast to non-null type android.widget.LinearLayout");
        View childAt = linearLayout.getChildAt(((LinearLayout) r4).getChildCount() - 1);
        Intrinsics.checkNotNull(childAt, "null cannot be cast to non-null type android.widget.LinearLayout");
        ((LinearLayout) childAt).getChildAt(r5).setBackgroundColor(0);
    }
}
