package com.kabuk;

import com.termux.terminal.TerminalSession;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: Sessions.kt */
@Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\f\u0018\u00002\u00020\u0001B\u000f\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0004R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u0011\u0010\u000b\u001a\u00020\f8F¢\u0006\u0006\u001a\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\b\"\u0004\b\u0011\u0010\nR\u0011\u0010\u0012\u001a\u00020\u00138F¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u0018\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\u0019\u0010\bR\u001a\u0010\u001a\u001a\u00020\fX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u000e\"\u0004\b\u001c\u0010\u001dR\u001c\u0010\u001e\u001a\u0004\u0018\u00010\u001fX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b \u0010!\"\u0004\b\"\u0010#R\u001a\u0010$\u001a\u00020\u0013X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b%\u0010\u0015\"\u0004\b&\u0010'R\u001a\u0010(\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b)\u0010\b\"\u0004\b*\u0010\n¨\u0006+"}, d2 = {"Lcom/kabuk/Tab;", "", "profile", "Lcom/kabuk/Profile;", "(Lcom/kabuk/Profile;)V", "closedByUser", "", "getClosedByUser", "()Z", "setClosedByUser", "(Z)V", "color", "", "getColor", "()I", "connecting", "getConnecting", "setConnecting", "label", "", "getLabel", "()Ljava/lang/String;", "getProfile", "()Lcom/kabuk/Profile;", "remote", "getRemote", "retries", "getRetries", "setRetries", "(I)V", "session", "Lcom/termux/terminal/TerminalSession;", "getSession", "()Lcom/termux/terminal/TerminalSession;", "setSession", "(Lcom/termux/terminal/TerminalSession;)V", "status", "getStatus", "setStatus", "(Ljava/lang/String;)V", "usingSsh", "getUsingSsh", "setUsingSsh", "client_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
/* loaded from: classes.dex */
public final class Tab {
    private boolean closedByUser;
    private volatile boolean connecting;
    private final Profile profile;
    private int retries;
    private TerminalSession session;
    private String status = "baslatiliyor";
    private boolean usingSsh;

    public Tab(Profile profile) {
        this.profile = profile;
        this.usingSsh = Intrinsics.areEqual(profile != null ? profile.getMode() : null, "ssh");
    }

    public final Profile getProfile() {
        return this.profile;
    }

    public final TerminalSession getSession() {
        return this.session;
    }

    public final void setSession(TerminalSession terminalSession) {
        this.session = terminalSession;
    }

    public final String getStatus() {
        return this.status;
    }

    public final void setStatus(String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.status = str;
    }

    public final boolean getClosedByUser() {
        return this.closedByUser;
    }

    public final void setClosedByUser(boolean z) {
        this.closedByUser = z;
    }

    public final boolean getConnecting() {
        return this.connecting;
    }

    public final void setConnecting(boolean z) {
        this.connecting = z;
    }

    public final int getRetries() {
        return this.retries;
    }

    public final void setRetries(int r1) {
        this.retries = r1;
    }

    public final boolean getUsingSsh() {
        return this.usingSsh;
    }

    public final void setUsingSsh(boolean z) {
        this.usingSsh = z;
    }

    public final String getLabel() {
        String name;
        Profile profile = this.profile;
        return (profile == null || (name = profile.getName()) == null) ? "yerel" : name;
    }

    public final int getColor() {
        Profile profile = this.profile;
        return profile != null ? profile.getColor() : Profile.LOCAL_COLOR;
    }

    public final boolean getRemote() {
        return this.profile != null;
    }
}
