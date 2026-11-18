package com.kabuk;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.termux.terminal.TerminalEmulator;
import com.termux.terminal.TerminalSession;
import com.termux.terminal.TerminalSessionClient;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: Sessions.kt */
@Metadata(d1 = {"\u0000=\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u000f\u0010\u0002\u001a\u0004\u0018\u00010\u0003H\u0016¢\u0006\u0002\u0010\u0004J\u001c\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\u0010\t\u001a\u0004\u0018\u00010\bH\u0016J\u001c\u0010\n\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\u0010\t\u001a\u0004\u0018\u00010\bH\u0016J\u001c\u0010\u000b\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\u0010\t\u001a\u0004\u0018\u00010\bH\u0016J\"\u0010\f\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\u000e\u0010\r\u001a\n\u0018\u00010\u000ej\u0004\u0018\u0001`\u000fH\u0016J,\u0010\u0010\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\u0010\t\u001a\u0004\u0018\u00010\b2\u000e\u0010\r\u001a\n\u0018\u00010\u000ej\u0004\u0018\u0001`\u000fH\u0016J\u001c\u0010\u0011\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\u0010\t\u001a\u0004\u0018\u00010\bH\u0016J\u001c\u0010\u0012\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\u0010\t\u001a\u0004\u0018\u00010\bH\u0016J\u0010\u0010\u0013\u001a\u00020\u00062\u0006\u0010\u0014\u001a\u00020\u0015H\u0016J\u0010\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0014\u001a\u00020\u0015H\u0016J\u001a\u0010\u0017\u001a\u00020\u00062\u0006\u0010\u0014\u001a\u00020\u00152\b\u0010\u0018\u001a\u0004\u0018\u00010\bH\u0016J\u0012\u0010\u0019\u001a\u00020\u00062\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0016J\u0010\u0010\u001a\u001a\u00020\u00062\u0006\u0010\u0014\u001a\u00020\u0015H\u0016J\u0010\u0010\u001b\u001a\u00020\u00062\u0006\u0010\u001c\u001a\u00020\u001dH\u0016J\u0010\u0010\u001e\u001a\u00020\u00062\u0006\u0010\u0014\u001a\u00020\u0015H\u0016J\u0010\u0010\u001f\u001a\u00020\u00062\u0006\u0010\u0014\u001a\u00020\u0015H\u0016¨\u0006 "}, d2 = {"com/kabuk/Sessions$client$1", "Lcom/termux/terminal/TerminalSessionClient;", "getTerminalCursorStyle", "", "()Ljava/lang/Integer;", "logDebug", "", "tag", "", "m", "logError", "logInfo", "logStackTrace", "e", "Ljava/lang/Exception;", "Lkotlin/Exception;", "logStackTraceWithMessage", "logVerbose", "logWarn", "onBell", "s", "Lcom/termux/terminal/TerminalSession;", "onColorsChanged", "onCopyTextToClipboard", "text", "onPasteTextFromClipboard", "onSessionFinished", "onTerminalCursorStateChange", "state", "", "onTextChanged", "onTitleChanged", "client_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
/* loaded from: classes.dex */
public final class Sessions$client$1 implements TerminalSessionClient {
    final /* synthetic */ Tab $t;

    @Override // com.termux.terminal.TerminalSessionClient
    public Integer getTerminalCursorStyle() {
        return null;
    }

    @Override // com.termux.terminal.TerminalSessionClient
    public void logDebug(String tag, String m) {
    }

    @Override // com.termux.terminal.TerminalSessionClient
    public void logInfo(String tag, String m) {
    }

    @Override // com.termux.terminal.TerminalSessionClient
    public void logVerbose(String tag, String m) {
    }

    @Override // com.termux.terminal.TerminalSessionClient
    public void onBell(TerminalSession s) {
        Intrinsics.checkNotNullParameter(s, "s");
    }

    @Override // com.termux.terminal.TerminalSessionClient
    public void onColorsChanged(TerminalSession s) {
        Intrinsics.checkNotNullParameter(s, "s");
    }

    @Override // com.termux.terminal.TerminalSessionClient
    public void onTerminalCursorStateChange(boolean state) {
    }

    @Override // com.termux.terminal.TerminalSessionClient
    public void onTitleChanged(TerminalSession s) {
        Intrinsics.checkNotNullParameter(s, "s");
    }

    Sessions$client$1(Tab tab) {
        this.$t = tab;
    }

    @Override // com.termux.terminal.TerminalSessionClient
    public void onTextChanged(TerminalSession s) {
        Function1<TerminalSession, Unit> onScreen;
        Intrinsics.checkNotNullParameter(s, "s");
        if (!Intrinsics.areEqual(Sessions.INSTANCE.getCurrent(), this.$t) || (onScreen = Sessions.INSTANCE.getOnScreen()) == null) {
            return;
        }
        onScreen.invoke(s);
    }

    @Override // com.termux.terminal.TerminalSessionClient
    public void onSessionFinished(TerminalSession s) {
        Handler handler;
        Intrinsics.checkNotNullParameter(s, "s");
        if (this.$t.getClosedByUser() || !Intrinsics.areEqual(s, this.$t.getSession())) {
            return;
        }
        int exitStatus = s.getExitStatus();
        if (!this.$t.getRemote() || exitStatus == 0) {
            handler = Sessions.main;
            final Tab tab = this.$t;
            handler.post(new Runnable() { // from class: com.kabuk.Sessions$client$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    Sessions$client$1.onSessionFinished$lambda$0(Tab.this);
                }
            });
            return;
        }
        Sessions.INSTANCE.scheduleRetry(this.$t, "cikis " + exitStatus);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onSessionFinished$lambda$0(Tab t) {
        Intrinsics.checkNotNullParameter(t, "$t");
        Sessions.INSTANCE.close(t);
    }

    @Override // com.termux.terminal.TerminalSessionClient
    public void onCopyTextToClipboard(TerminalSession s, String text) {
        Context context;
        Intrinsics.checkNotNullParameter(s, "s");
        context = Sessions.app;
        if (context == null) {
            Intrinsics.throwUninitializedPropertyAccessException("app");
            context = null;
        }
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(ClipboardManager.class);
        if (text == null) {
            text = "";
        }
        clipboardManager.setPrimaryClip(ClipData.newPlainText(r0, text));
    }

    @Override // com.termux.terminal.TerminalSessionClient
    public void onPasteTextFromClipboard(TerminalSession s) {
        Context context;
        ClipData.Item itemAt;
        Context context2;
        String obj;
        TerminalEmulator emulator;
        context = Sessions.app;
        Context context3 = null;
        if (context == null) {
            Intrinsics.throwUninitializedPropertyAccessException("app");
            context = null;
        }
        ClipData primaryClip = ((ClipboardManager) context.getSystemService(ClipboardManager.class)).getPrimaryClip();
        if (primaryClip == null || (itemAt = primaryClip.getItemAt(0)) == null) {
            return;
        }
        context2 = Sessions.app;
        if (context2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("app");
        } else {
            context3 = context2;
        }
        CharSequence coerceToText = itemAt.coerceToText(context3);
        if (coerceToText == null || (obj = coerceToText.toString()) == null || s == null || (emulator = s.getEmulator()) == null) {
            return;
        }
        emulator.paste(obj);
    }

    @Override // com.termux.terminal.TerminalSessionClient
    public void logError(String tag, String m) {
        if (m == null) {
            m = "";
        }
        Log.e(tag, m);
    }

    @Override // com.termux.terminal.TerminalSessionClient
    public void logWarn(String tag, String m) {
        if (m == null) {
            m = "";
        }
        Log.w(tag, m);
    }

    @Override // com.termux.terminal.TerminalSessionClient
    public void logStackTraceWithMessage(String tag, String m, Exception e) {
        Log.e(tag, m, e);
    }

    @Override // com.termux.terminal.TerminalSessionClient
    public void logStackTrace(String tag, Exception e) {
        Log.e(tag, "", e);
    }
}
