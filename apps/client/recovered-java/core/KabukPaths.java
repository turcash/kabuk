package com.kabuk.core;

import kotlin.Metadata;

/* compiled from: RootShell.kt */
@Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Lcom/kabuk/core/KabukPaths;", "", "()V", "BACKUPS", "", "BASE", "BIN", "KABUK", "ROOTFS", "core_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
/* loaded from: classes.dex */
public final class KabukPaths {
    public static final String BACKUPS = "/data/kabuk/backups";
    public static final String BASE = "/data/kabuk";
    public static final String BIN = "/data/kabuk/bin";
    public static final KabukPaths INSTANCE = new KabukPaths();
    public static final String KABUK = "/data/kabuk/bin/kabuk";
    public static final String ROOTFS = "/data/kabuk/rootfs";

    private KabukPaths() {
    }
}
