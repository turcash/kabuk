package com.kabuk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.UUID;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.text.StringsKt;

/* compiled from: MainActivity.kt */
@Metadata(d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u001e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010H\u0002J\u0012\u0010\u0012\u001a\u00020\u00112\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0002J\u0012\u0010\u0015\u001a\u00020\u00112\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017H\u0014J\b\u0010\u0018\u001a\u00020\u0011H\u0014J\u0010\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001aH\u0002J\b\u0010\u001c\u001a\u00020\u0011H\u0002J\u0010\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\r\u001a\u00020\u000eH\u0002J\u0010\u0010\u001f\u001a\u00020\u001e2\u0006\u0010\r\u001a\u00020\u000eH\u0002R\u001b\u0010\u0003\u001a\u00020\u00048BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006R\u000e\u0010\t\u001a\u00020\nX\u0082.¢\u0006\u0002\n\u0000¨\u0006 "}, d2 = {"Lcom/kabuk/MainActivity;", "Landroid/app/Activity;", "()V", "d", "", "getD", "()F", "d$delegate", "Lkotlin/Lazy;", "list", "Landroid/widget/LinearLayout;", "button", "Landroid/widget/Button;", "s", "", "f", "Lkotlin/Function0;", "", "edit", "p", "Lcom/kabuk/Profile;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onResume", "px", "", "v", "render", "text", "Landroid/widget/TextView;", "title", "client_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
/* loaded from: classes.dex */
public final class MainActivity extends Activity {

    /* renamed from: d$delegate, reason: from kotlin metadata */
    private final Lazy d = LazyKt.lazy(new Function0<Float>() { // from class: com.kabuk.MainActivity$d$2
        {
            super(0);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // kotlin.jvm.functions.Function0
        public final Float invoke() {
            return Float.valueOf(MainActivity.this.getResources().getDisplayMetrics().density);
        }
    });
    private LinearLayout list;

    private final float getD() {
        return ((Number) this.d.getValue()).floatValue();
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        Object m10constructorimpl;
        super.onCreate(savedInstanceState);
        MainActivity mainActivity = this;
        KabukService.INSTANCE.start(mainActivity);
        Sessions.INSTANCE.init(mainActivity);
        LinearLayout linearLayout = new LinearLayout(mainActivity);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(px(16), px(16), px(16), px(16));
        linearLayout.setBackgroundColor(-15723496);
        linearLayout.addView(title("Uzak cihazlar"));
        LinearLayout linearLayout2 = new LinearLayout(mainActivity);
        linearLayout2.setOrientation(1);
        this.list = linearLayout2;
        linearLayout.addView(linearLayout2);
        linearLayout.addView(button("+ Cihaz ekle", new Function0<Unit>() { // from class: com.kabuk.MainActivity$onCreate$2
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
                MainActivity.this.edit(null);
            }
        }));
        linearLayout.addView(button("Yerel terminal", new Function0<Unit>() { // from class: com.kabuk.MainActivity$onCreate$3
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
                MainActivity.this.startActivity(new Intent(MainActivity.this, (Class<?>) TerminalActivity.class).putExtra(TerminalActivity.EXTRA_LOCAL, true));
            }
        }));
        linearLayout.addView(title("SSH anahtarim"));
        linearLayout.addView(text("Android Keystore'da uretilir, cihazdan cikarilamaz. Note 10'a bir kez ekle:\nKabuk Sunucu → 'SSH anahtari ekle' (yapistir) veya\nsu -c '/data/kabuk/bin/kabuk authkey \"<anahtar>\"'"));
        try {
            Result.Companion companion = Result.INSTANCE;
            MainActivity mainActivity2 = this;
            KeyAgent.INSTANCE.ensureKey();
            m10constructorimpl = Result.m10constructorimpl(KeyAgent.INSTANCE.publicKeyLine());
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            m10constructorimpl = Result.m10constructorimpl(ResultKt.createFailure(th));
        }
        Throwable m13exceptionOrNullimpl = Result.m13exceptionOrNullimpl(m10constructorimpl);
        if (m13exceptionOrNullimpl != null) {
            m10constructorimpl = "anahtar hatasi: " + m13exceptionOrNullimpl.getMessage();
        }
        final String str = (String) m10constructorimpl;
        Log.i("KabukKey", str);
        TextView text = text(str);
        text.setTypeface(Typeface.MONOSPACE);
        text.setTextIsSelectable(true);
        text.setTextSize(11.0f);
        linearLayout.addView(text);
        linearLayout.addView(button("Anahtari kopyala", new Function0<Unit>() { // from class: com.kabuk.MainActivity$onCreate$5
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
                ((ClipboardManager) MainActivity.this.getSystemService(ClipboardManager.class)).setPrimaryClip(ClipData.newPlainText("ssh", str));
                Toast.makeText(MainActivity.this, "Kopyalandi", 0).show();
            }
        }));
        linearLayout.addView(button("Anahtari paylas", new Function0<Unit>() { // from class: com.kabuk.MainActivity$onCreate$6
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
                MainActivity.this.startActivity(Intent.createChooser(new Intent("android.intent.action.SEND").setType("text/plain").putExtra("android.intent.extra.TEXT", str), "SSH anahtari"));
            }
        }));
        ScrollView scrollView = new ScrollView(mainActivity);
        scrollView.addView(linearLayout);
        setContentView(scrollView);
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        render();
    }

    private final void render() {
        LinearLayout linearLayout = this.list;
        if (linearLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("list");
            linearLayout = null;
        }
        linearLayout.removeAllViews();
        MainActivity mainActivity = this;
        for (final Profile profile : Profiles.INSTANCE.all(mainActivity)) {
            LinearLayout linearLayout2 = new LinearLayout(mainActivity);
            linearLayout2.setOrientation(1);
            linearLayout2.setPadding(px(14), px(12), px(14), px(12));
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(-14933208);
            gradientDrawable.setCornerRadius(10 * getD());
            gradientDrawable.setStroke(px(3), profile.getColor());
            linearLayout2.setBackground(gradientDrawable);
            linearLayout2.setOnClickListener(new View.OnClickListener() { // from class: com.kabuk.MainActivity$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    MainActivity.render$lambda$9$lambda$7(MainActivity.this, profile, view);
                }
            });
            linearLayout2.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.kabuk.MainActivity$$ExternalSyntheticLambda1
                @Override // android.view.View.OnLongClickListener
                public final boolean onLongClick(View view) {
                    boolean render$lambda$9$lambda$8;
                    render$lambda$9$lambda$8 = MainActivity.render$lambda$9$lambda$8(MainActivity.this, profile, view);
                    return render$lambda$9$lambda$8;
                }
            });
            TextView textView = new TextView(mainActivity);
            textView.setText("● " + profile.getName());
            textView.setTextColor(profile.getColor());
            textView.setTextSize(18.0f);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            linearLayout2.addView(textView);
            linearLayout2.addView(text(profile.getUser() + "@" + profile.getHost() + ":" + profile.getPort() + "  ·  " + profile.getMode() + "  ·  tmux '" + profile.getSession() + "'" + (profile.getNotify() ? "  ·  bildirim acik" : "")));
            LinearLayout linearLayout3 = new LinearLayout(mainActivity);
            linearLayout3.setOrientation(0);
            linearLayout3.addView(button("💬 Ajanlar (sohbet)", new Function0<Unit>() { // from class: com.kabuk.MainActivity$render$2
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
                    MainActivity.this.startActivity(new Intent(MainActivity.this, (Class<?>) AgentActivity.class).putExtra("profile", profile.getId()));
                }
            }), new LinearLayout.LayoutParams(0, -2, 1.0f));
            linearLayout3.addView(button("⌨ Terminal", new Function0<Unit>() { // from class: com.kabuk.MainActivity$render$3
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
                    MainActivity.this.startActivity(new Intent(MainActivity.this, (Class<?>) TerminalActivity.class).putExtra("profile", profile.getId()));
                }
            }), new LinearLayout.LayoutParams(0, -2, 1.0f));
            linearLayout2.addView(linearLayout3);
            TextView text = text("Basili tut: duzenle");
            text.setTextSize(11.0f);
            text.setTextColor(-8418671);
            linearLayout2.addView(text);
            LinearLayout linearLayout4 = this.list;
            if (linearLayout4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("list");
                linearLayout4 = null;
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
            layoutParams.setMargins(0, px(8), 0, px(4));
            Unit unit = Unit.INSTANCE;
            linearLayout4.addView(linearLayout2, layoutParams);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void render$lambda$9$lambda$7(MainActivity this$0, Profile p, View view) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(p, "$p");
        this$0.startActivity(new Intent(this$0, (Class<?>) AgentActivity.class).putExtra("profile", p.getId()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean render$lambda$9$lambda$8(MainActivity this$0, Profile p, View view) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(p, "$p");
        this$0.edit(p);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void edit(final Profile p) {
        String str;
        String str2;
        String host;
        String name;
        MainActivity mainActivity = this;
        LinearLayout linearLayout = new LinearLayout(mainActivity);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(px(20), px(8), px(20), 0);
        final EditText edit$field$default = edit$field$default(this, linearLayout, "Ad (ornek: note10)", (p == null || (name = p.getName()) == null) ? "" : name, false, 16, null);
        final EditText edit$field$default2 = edit$field$default(this, linearLayout, "Host (tailnet adi: note10 veya note10.xxx.ts.net)", (p == null || (host = p.getHost()) == null) ? "" : host, false, 16, null);
        if (p == null || (str = p.getUser()) == null) {
            str = "dev";
        }
        final EditText edit$field$default3 = edit$field$default(this, linearLayout, "Kullanici", str, false, 16, null);
        final EditText edit$field = edit$field(this, linearLayout, "Port", String.valueOf(p != null ? p.getPort() : 22), true);
        if (p == null || (str2 = p.getSession()) == null) {
            str2 = "claude";
        }
        final EditText edit$field$default4 = edit$field$default(this, linearLayout, "tmux oturumu", str2, false, 16, null);
        final RadioGroup radioGroup = new RadioGroup(mainActivity);
        radioGroup.setOrientation(0);
        RadioButton radioButton = new RadioButton(mainActivity);
        radioButton.setText("mosh (onerilen)");
        radioButton.setId(1);
        RadioButton radioButton2 = new RadioButton(mainActivity);
        radioButton2.setText("ssh");
        radioButton2.setId(2);
        radioGroup.addView(radioButton);
        radioGroup.addView(radioButton2);
        radioGroup.check(Intrinsics.areEqual(p != null ? p.getMode() : null, "ssh") ? 2 : 1);
        linearLayout.addView(radioGroup);
        final CheckBox checkBox = new CheckBox(mainActivity);
        checkBox.setText("Claude bildirimlerini al (ntfy)");
        checkBox.setChecked(p != null ? p.getNotify() : true);
        linearLayout.addView(checkBox);
        final Ref.IntRef intRef = new Ref.IntRef();
        intRef.element = p != null ? p.getColor() : Profile.INSTANCE.getCOLORS()[Profiles.INSTANCE.all(mainActivity).size() % Profile.INSTANCE.getCOLORS().length];
        final Button button = new Button(mainActivity);
        button.setText("Renk");
        button.setBackgroundColor(intRef.element);
        button.setTextColor(-1);
        button.setOnClickListener(new View.OnClickListener() { // from class: com.kabuk.MainActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.edit$lambda$21(Ref.IntRef.this, button, view);
            }
        });
        linearLayout.addView(button);
        AlertDialog.Builder title = new AlertDialog.Builder(mainActivity).setTitle(p == null ? "Cihaz ekle" : "Duzenle");
        ScrollView scrollView = new ScrollView(mainActivity);
        scrollView.addView(linearLayout);
        AlertDialog.Builder negativeButton = title.setView(scrollView).setPositiveButton("Kaydet", new DialogInterface.OnClickListener() { // from class: com.kabuk.MainActivity$$ExternalSyntheticLambda4
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int r14) {
                MainActivity.edit$lambda$24(edit$field$default, edit$field$default2, this, p, edit$field, edit$field$default3, radioGroup, edit$field$default4, intRef, checkBox, dialogInterface, r14);
            }
        }).setNegativeButton("Vazgec", (DialogInterface.OnClickListener) null);
        if (p != null) {
            negativeButton.setNeutralButton("Sil", new DialogInterface.OnClickListener() { // from class: com.kabuk.MainActivity$$ExternalSyntheticLambda5
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int r4) {
                    MainActivity.edit$lambda$25(MainActivity.this, p, dialogInterface, r4);
                }
            });
        }
        negativeButton.show();
    }

    private static final EditText edit$field(MainActivity mainActivity, LinearLayout linearLayout, String str, String str2, boolean z) {
        EditText editText = new EditText(mainActivity);
        editText.setHint(str);
        editText.setText(str2);
        if (z) {
            editText.setInputType(2);
        }
        linearLayout.addView(editText);
        return editText;
    }

    static /* synthetic */ EditText edit$field$default(MainActivity mainActivity, LinearLayout linearLayout, String str, String str2, boolean z, int r5, Object obj) {
        if ((r5 & 16) != 0) {
            z = false;
        }
        return edit$field(mainActivity, linearLayout, str, str2, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void edit$lambda$21(Ref.IntRef color, Button colorBtn, View view) {
        Intrinsics.checkNotNullParameter(color, "$color");
        Intrinsics.checkNotNullParameter(colorBtn, "$colorBtn");
        color.element = Profile.INSTANCE.getCOLORS()[(ArraysKt.indexOf(Profile.INSTANCE.getCOLORS(), color.element) + 1) % Profile.INSTANCE.getCOLORS().length];
        colorBtn.setBackgroundColor(color.element);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void edit$lambda$24(EditText name, EditText host, MainActivity this$0, Profile profile, EditText port, EditText user, RadioGroup mode, EditText sess, Ref.IntRef color, CheckBox notify, DialogInterface dialogInterface, int r34) {
        String str;
        Intrinsics.checkNotNullParameter(name, "$name");
        Intrinsics.checkNotNullParameter(host, "$host");
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(port, "$port");
        Intrinsics.checkNotNullParameter(user, "$user");
        Intrinsics.checkNotNullParameter(mode, "$mode");
        Intrinsics.checkNotNullParameter(sess, "$sess");
        Intrinsics.checkNotNullParameter(color, "$color");
        Intrinsics.checkNotNullParameter(notify, "$notify");
        Editable text = name.getText();
        Intrinsics.checkNotNullExpressionValue(text, "getText(...)");
        if (StringsKt.isBlank(text)) {
            return;
        }
        Editable text2 = host.getText();
        Intrinsics.checkNotNullExpressionValue(text2, "getText(...)");
        if (StringsKt.isBlank(text2)) {
            return;
        }
        Profiles profiles = Profiles.INSTANCE;
        MainActivity mainActivity = this$0;
        if (profile == null || (str = profile.getId()) == null) {
            str = UUID.randomUUID().toString();
            Intrinsics.checkNotNullExpressionValue(str, "toString(...)");
        }
        String str2 = str;
        String obj = StringsKt.trim((CharSequence) name.getText().toString()).toString();
        String obj2 = StringsKt.trim((CharSequence) host.getText().toString()).toString();
        Integer intOrNull = StringsKt.toIntOrNull(port.getText().toString());
        int intValue = intOrNull != null ? intOrNull.intValue() : 22;
        String obj3 = StringsKt.trim((CharSequence) user.getText().toString()).toString();
        String str3 = mode.getCheckedRadioButtonId() == 2 ? "ssh" : "mosh";
        String obj4 = StringsKt.trim((CharSequence) sess.getText().toString()).toString();
        if (obj4.length() == 0) {
            obj4 = "claude";
        }
        profiles.upsert(mainActivity, new Profile(str2, obj, obj2, intValue, obj3, str3, obj4, color.element, notify.isChecked()));
        KabukService.INSTANCE.start(mainActivity);
        this$0.render();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void edit$lambda$25(MainActivity this$0, Profile profile, DialogInterface dialogInterface, int r3) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Profiles.INSTANCE.delete(this$0, profile.getId());
        this$0.render();
    }

    private final int px(int v) {
        return (int) (v * getD());
    }

    private final TextView title(String s) {
        TextView textView = new TextView(this);
        textView.setText(s);
        textView.setTextSize(20.0f);
        textView.setTextColor(-1);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setPadding(0, px(12), 0, px(4));
        return textView;
    }

    private final TextView text(String s) {
        TextView textView = new TextView(this);
        textView.setText(s);
        textView.setTextColor(-3550246);
        textView.setTextSize(13.0f);
        return textView;
    }

    private final Button button(String s, final Function0<Unit> f) {
        Button button = new Button(this);
        button.setText(s);
        button.setAllCaps(false);
        button.setOnClickListener(new View.OnClickListener() { // from class: com.kabuk.MainActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.button$lambda$29$lambda$28(Function0.this, view);
            }
        });
        return button;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void button$lambda$29$lambda$28(Function0 f, View view) {
        Intrinsics.checkNotNullParameter(f, "$f");
        f.invoke();
    }
}
