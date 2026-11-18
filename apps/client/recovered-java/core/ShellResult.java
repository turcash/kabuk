package com.kabuk.core;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: RootShell.kt */
@Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\f\b\u0086\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\u000f\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\u0011\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u0012\u001a\u00020\n2\b\u0010\u0013\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0014\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0015\u001a\u00020\u0005HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n8F¢\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e¨\u0006\u0016"}, d2 = {"Lcom/kabuk/core/ShellResult;", "", "code", "", "output", "", "(ILjava/lang/String;)V", "getCode", "()I", "ok", "", "getOk", "()Z", "getOutput", "()Ljava/lang/String;", "component1", "component2", "copy", "equals", "other", "hashCode", "toString", "core_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
/* loaded from: classes.dex */
public final /* data */ class ShellResult {
    private final int code;
    private final String output;

    public static /* synthetic */ ShellResult copy$default(ShellResult shellResult, int r1, String str, int r3, Object obj) {
        if ((r3 & 1) != 0) {
            r1 = shellResult.code;
        }
        if ((r3 & 2) != 0) {
            str = shellResult.output;
        }
        return shellResult.copy(r1, str);
    }

    /* renamed from: component1, reason: from getter */
    public final int getCode() {
        return this.code;
    }

    /* renamed from: component2, reason: from getter */
    public final String getOutput() {
        return this.output;
    }

    public final ShellResult copy(int code, String output) {
        Intrinsics.checkNotNullParameter(output, "output");
        return new ShellResult(code, output);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ShellResult)) {
            return false;
        }
        ShellResult shellResult = (ShellResult) other;
        return this.code == shellResult.code && Intrinsics.areEqual(this.output, shellResult.output);
    }

    public int hashCode() {
        return (Integer.hashCode(this.code) * 31) + this.output.hashCode();
    }

    public String toString() {
        return "ShellResult(code=" + this.code + ", output=" + this.output + ")";
    }

    public ShellResult(int r2, String output) {
        Intrinsics.checkNotNullParameter(output, "output");
        this.code = r2;
        this.output = output;
    }

    public final int getCode() {
        return this.code;
    }

    public final String getOutput() {
        return this.output;
    }

    public final boolean getOk() {
        return this.code == 0;
    }
}
