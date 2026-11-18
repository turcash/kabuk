package com.kabuk;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* compiled from: AgentActivity.kt */
@Metadata(d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 \u00132\u00020\u0001:\u0001\u0013B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u00020\bH\u0002J\u0012\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0002J\b\u0010\r\u001a\u00020\nH\u0017J\u0012\u0010\u000e\u001a\u00020\n2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0015J\u0010\u0010\u0011\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\fH\u0014R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.¢\u0006\u0002\n\u0000¨\u0006\u0014"}, d2 = {"Lcom/kabuk/AgentActivity;", "Landroid/app/Activity;", "()V", "profile", "Lcom/kabuk/Profile;", "web", "Landroid/webkit/WebView;", "base", "Landroid/net/Uri;", "load", "", "i", "Landroid/content/Intent;", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onNewIntent", "intent", "Companion", "client_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
/* loaded from: classes.dex */
public final class AgentActivity extends Activity {
    public static final int AGENT_PORT = 8787;
    public static final String EXTRA_CHAT = "chat";
    public static final String EXTRA_PROFILE = "profile";
    private Profile profile;
    private WebView web;

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        Profile profile;
        super.onCreate(savedInstanceState);
        AgentActivity agentActivity = this;
        KabukService.INSTANCE.start(agentActivity);
        String stringExtra = getIntent().getStringExtra("profile");
        if (stringExtra == null || (profile = Profiles.INSTANCE.get(agentActivity, stringExtra)) == null) {
            profile = (Profile) CollectionsKt.first((List) Profiles.INSTANCE.all(agentActivity));
        }
        this.profile = profile;
        float f = getResources().getDisplayMetrics().density;
        LinearLayout linearLayout = new LinearLayout(agentActivity);
        linearLayout.setOrientation(1);
        linearLayout.setBackgroundColor(-16777216);
        LinearLayout linearLayout2 = new LinearLayout(agentActivity);
        linearLayout2.setOrientation(0);
        Profile profile2 = this.profile;
        Profile profile3 = null;
        if (profile2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("profile");
            profile2 = null;
        }
        linearLayout2.setBackgroundColor(profile2.getColor());
        linearLayout2.setPadding((int) (10 * f), 0, (int) (4 * f), 0);
        TextView textView = new TextView(agentActivity);
        Profile profile4 = this.profile;
        if (profile4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("profile");
        } else {
            profile3 = profile4;
        }
        textView.setText("UZAK: " + profile3.getName() + " · Ajanlar");
        textView.setTextColor(-1);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setTextSize(13.0f);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2, 1.0f);
        layoutParams.gravity = 16;
        Unit unit = Unit.INSTANCE;
        linearLayout2.addView(textView, layoutParams);
        Button button = new Button(agentActivity);
        button.setText("Terminal");
        button.setAllCaps(false);
        button.setTextSize(12.0f);
        button.setOnClickListener(new View.OnClickListener() { // from class: com.kabuk.AgentActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AgentActivity.onCreate$lambda$6$lambda$5(AgentActivity.this, view);
            }
        });
        linearLayout2.addView(button);
        Button button2 = new Button(agentActivity);
        button2.setText("≡");
        button2.setMinWidth(0);
        button2.setMinimumWidth(0);
        button2.setOnClickListener(new View.OnClickListener() { // from class: com.kabuk.AgentActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AgentActivity.onCreate$lambda$8$lambda$7(AgentActivity.this, view);
            }
        });
        linearLayout2.addView(button2);
        linearLayout.addView(linearLayout2, new LinearLayout.LayoutParams(-1, -2));
        WebView webView = new WebView(agentActivity);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setTextZoom(100);
        webView.setBackgroundColor(-15789290);
        webView.setWebViewClient(new WebViewClient() { // from class: com.kabuk.AgentActivity$onCreate$6$1
            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView v, WebResourceRequest r) {
                Uri base;
                Intrinsics.checkNotNullParameter(v, "v");
                Intrinsics.checkNotNullParameter(r, "r");
                String host = r.getUrl().getHost();
                base = AgentActivity.this.base();
                if (Intrinsics.areEqual(host, base.getHost())) {
                    return false;
                }
                AgentActivity.this.startActivity(new Intent("android.intent.action.VIEW", r.getUrl()));
                return true;
            }

            @Override // android.webkit.WebViewClient
            public void onReceivedError(WebView v, WebResourceRequest r, WebResourceError e) {
                Profile profile5;
                Uri base;
                Intrinsics.checkNotNullParameter(v, "v");
                Intrinsics.checkNotNullParameter(r, "r");
                Intrinsics.checkNotNullParameter(e, "e");
                if (r.isForMainFrame()) {
                    profile5 = AgentActivity.this.profile;
                    if (profile5 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("profile");
                        profile5 = null;
                    }
                    String name = profile5.getName();
                    CharSequence description = e.getDescription();
                    base = AgentActivity.this.base();
                    v.loadDataWithBaseURL(null, StringsKt.trimIndent("\n                        <body style=\"background:#0f1316;color:#e6ebef;font-family:sans-serif;padding:24px\">\n                        <h3>" + name + " ajan sunucusuna ulaşılamadı</h3>\n                        <p>" + ((Object) description) + "</p>\n                        <p>Tailscale açık mı? Note 10 tailnet'te mi?</p>\n                        <button onclick=\"location.href='" + base + "'\" style=\"padding:12px 18px;font-size:16px\">Tekrar dene</button></body>\n                    "), "text/html", "utf-8", null);
                }
            }
        });
        this.web = webView;
        linearLayout.addView(webView, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        setContentView(linearLayout);
        load(getIntent());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onCreate$lambda$6$lambda$5(AgentActivity this$0, View view) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intent intent = new Intent(this$0, (Class<?>) TerminalActivity.class);
        Profile profile = this$0.profile;
        if (profile == null) {
            Intrinsics.throwUninitializedPropertyAccessException("profile");
            profile = null;
        }
        this$0.startActivity(intent.putExtra("profile", profile.getId()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onCreate$lambda$8$lambda$7(AgentActivity this$0, View view) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.startActivity(new Intent(this$0, (Class<?>) MainActivity.class));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Uri base() {
        Profile profile = this.profile;
        if (profile == null) {
            Intrinsics.throwUninitializedPropertyAccessException("profile");
            profile = null;
        }
        Uri parse = Uri.parse("http://" + profile.getHost() + ":8787/");
        Intrinsics.checkNotNullExpressionValue(parse, "parse(...)");
        return parse;
    }

    private final void load(Intent i) {
        WebView webView = null;
        String stringExtra = i != null ? i.getStringExtra(EXTRA_CHAT) : null;
        WebView webView2 = this.web;
        if (webView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("web");
        } else {
            webView = webView2;
        }
        webView.loadUrl(base().toString() + (stringExtra != null ? "#" + stringExtra : ""));
    }

    @Override // android.app.Activity
    protected void onNewIntent(Intent intent) {
        Intrinsics.checkNotNullParameter(intent, "intent");
        super.onNewIntent(intent);
        String stringExtra = intent.getStringExtra("profile");
        if (stringExtra != null) {
            Profile profile = this.profile;
            if (profile == null) {
                Intrinsics.throwUninitializedPropertyAccessException("profile");
                profile = null;
            }
            if (!Intrinsics.areEqual(stringExtra, profile.getId())) {
                setIntent(intent);
                recreate();
                return;
            }
        }
        if (intent.getStringExtra(EXTRA_CHAT) != null) {
            load(intent);
        }
    }

    @Override // android.app.Activity
    @Deprecated(message = "Deprecated in Java")
    public void onBackPressed() {
        WebView webView = this.web;
        if (webView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("web");
            webView = null;
        }
        webView.evaluateJavascript("(typeof current!=='undefined' && current) ? (showList(),'1') : '0'", new ValueCallback() { // from class: com.kabuk.AgentActivity$$ExternalSyntheticLambda2
            @Override // android.webkit.ValueCallback
            public final void onReceiveValue(Object obj) {
                AgentActivity.onBackPressed$lambda$10(AgentActivity.this, (String) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onBackPressed$lambda$10(AgentActivity this$0, String str) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        if (str == null || !StringsKt.contains$default((CharSequence) str, (CharSequence) "1", false, 2, (Object) null)) {
            super.onBackPressed();
        }
    }
}
