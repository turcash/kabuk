package com.kabuk;

import android.content.Context;
import android.system.Os;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.io.ByteStreamsKt;
import kotlin.io.CloseableKt;
import kotlin.io.ConstantsKt;
import kotlin.io.FilesKt;
import kotlin.io.TextStreamsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;

/* compiled from: NativeEnv.kt */
@Metadata(d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010$\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J/\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00100\u00192\u0006\u0010\u001a\u001a\u00020\u00102\u0006\u0010\u001b\u001a\u00020\u00102\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00100\u001d¢\u0006\u0002\u0010\u001eJ\u0018\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\u00102\u0006\u0010\"\u001a\u00020\u0006H\u0002J'\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00100\u00192\u0014\b\u0002\u0010$\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00100%¢\u0006\u0002\u0010&J\u000e\u0010'\u001a\n \u0011*\u0004\u0018\u00010\u00100\u0010J\u001f\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00100\u00192\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00100\u001d¢\u0006\u0002\u0010)J\u0006\u0010*\u001a\u00020 J\u001f\u0010+\u001a\b\u0012\u0004\u0012\u00020\u00100\u00192\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00100\u001d¢\u0006\u0002\u0010)R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\t\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\bR\u0011\u0010\u000b\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\bR\u0011\u0010\r\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\bR\u0016\u0010\u000f\u001a\n \u0011*\u0004\u0018\u00010\u00100\u0010X\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0012\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\bR\u0011\u0010\u0014\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\bR\u0011\u0010\u0016\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\b¨\u0006,"}, d2 = {"Lcom/kabuk/NativeEnv;", "", "ctx", "Landroid/content/Context;", "(Landroid/content/Context;)V", "agentSock", "Ljava/io/File;", "getAgentSock", "()Ljava/io/File;", "home", "getHome", "libDir", "getLibDir", "linkDir", "getLinkDir", "loader", "", "kotlin.jvm.PlatformType", "sshConfig", "getSshConfig", "sshDir", "getSshDir", "terminfo", "getTerminfo", "cmd", "", "argv0", "bin", "args", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/util/List;)[Ljava/lang/String;", "copyAssetDir", "", "name", "dst", "env", "extra", "", "(Ljava/util/Map;)[Ljava/lang/String;", "loaderPath", "moshClient", "(Ljava/util/List;)[Ljava/lang/String;", "prepare", "ssh", "client_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
/* loaded from: classes.dex */
public final class NativeEnv {
    private final File agentSock;
    private final Context ctx;
    private final File home;
    private final File libDir;
    private final File linkDir;
    private final String loader;
    private final File sshConfig;
    private final File sshDir;
    private final File terminfo;

    public NativeEnv(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        this.ctx = ctx;
        File file = new File(ctx.getApplicationInfo().nativeLibraryDir);
        this.libDir = file;
        this.linkDir = new File(ctx.getFilesDir(), "lib");
        File file2 = new File(ctx.getFilesDir(), "home");
        this.home = file2;
        File file3 = new File(file2, ".ssh");
        this.sshDir = file3;
        this.sshConfig = new File(file3, "config");
        this.terminfo = new File(ctx.getFilesDir(), "terminfo");
        this.agentSock = new File(ctx.getFilesDir(), "agent.sock");
        this.loader = new File(file, "libldmusl.so").getPath();
    }

    public final File getLibDir() {
        return this.libDir;
    }

    public final File getLinkDir() {
        return this.linkDir;
    }

    public final File getHome() {
        return this.home;
    }

    public final File getSshDir() {
        return this.sshDir;
    }

    public final File getSshConfig() {
        return this.sshConfig;
    }

    public final File getTerminfo() {
        return this.terminfo;
    }

    public final File getAgentSock() {
        return this.agentSock;
    }

    public final void prepare() {
        File file = new File(this.ctx.getFilesDir(), ".native-stamp");
        String valueOf = String.valueOf(this.ctx.getPackageManager().getPackageInfo(this.ctx.getPackageName(), 0).lastUpdateTime);
        if (!file.exists() || !Intrinsics.areEqual(FilesKt.readText$default(file, null, 1, null), valueOf) || !this.linkDir.exists()) {
            FilesKt.deleteRecursively(this.linkDir);
            this.linkDir.mkdirs();
            InputStream open = this.ctx.getAssets().open("manifest.tsv");
            Intrinsics.checkNotNullExpressionValue(open, "open(...)");
            Reader inputStreamReader = new InputStreamReader(open, Charsets.UTF_8);
            TextStreamsKt.forEachLine(inputStreamReader instanceof BufferedReader ? (BufferedReader) inputStreamReader : new BufferedReader(inputStreamReader, ConstantsKt.DEFAULT_BUFFER_SIZE), new Function1<String, Unit>() { // from class: com.kabuk.NativeEnv$prepare$1
                {
                    super(1);
                }

                @Override // kotlin.jvm.functions.Function1
                public /* bridge */ /* synthetic */ Unit invoke(String str) {
                    invoke2(str);
                    return Unit.INSTANCE;
                }

                /* renamed from: invoke, reason: avoid collision after fix types in other method */
                public final void invoke2(String l) {
                    Intrinsics.checkNotNullParameter(l, "l");
                    List split$default = StringsKt.split$default((CharSequence) l, new char[]{'\t'}, false, 0, 6, (Object) null);
                    if (split$default.size() == 2) {
                        Os.symlink(new File(NativeEnv.this.getLibDir(), (String) split$default.get(1)).getPath(), new File(NativeEnv.this.getLinkDir(), (String) split$default.get(0)).getPath());
                    }
                }
            });
            copyAssetDir("terminfo", this.terminfo);
            FilesKt.writeText$default(file, valueOf, null, 2, null);
        }
        this.sshDir.mkdirs();
        Os.chmod(this.home.getPath(), Integer.parseInt("700", CharsKt.checkRadix(8)));
        Os.chmod(this.sshDir.getPath(), Integer.parseInt("700", CharsKt.checkRadix(8)));
        FilesKt.writeText$default(this.sshConfig, StringsKt.trimIndent("\n            Host *\n              StrictHostKeyChecking accept-new\n              UserKnownHostsFile " + new File(this.sshDir, "known_hosts").getPath() + "\n              IdentityAgent " + this.agentSock.getPath() + "\n              IdentityFile none\n              PasswordAuthentication no\n              KbdInteractiveAuthentication no\n              ServerAliveInterval 15\n              ServerAliveCountMax 3\n              ConnectTimeout 15\n              LogLevel ERROR\n            ") + "\n", null, 2, null);
        Os.chmod(this.sshConfig.getPath(), Integer.parseInt("600", CharsKt.checkRadix(8)));
    }

    private final void copyAssetDir(String name, File dst) {
        String[] list = this.ctx.getAssets().list(name);
        if (list == null) {
            list = new String[0];
        }
        if (list.length == 0) {
            File parentFile = dst.getParentFile();
            if (parentFile != null) {
                parentFile.mkdirs();
            }
            FileOutputStream open = this.ctx.getAssets().open(name);
            try {
                InputStream inputStream = open;
                open = new FileOutputStream(dst);
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
            dst.mkdirs();
            for (String str : list) {
                copyAssetDir(name + "/" + str, new File(dst, str));
            }
        }
    }

    public final String[] cmd(String argv0, String bin, List<String> args) {
        Intrinsics.checkNotNullParameter(argv0, "argv0");
        Intrinsics.checkNotNullParameter(bin, "bin");
        Intrinsics.checkNotNullParameter(args, "args");
        return (String[]) CollectionsKt.plus((Collection) CollectionsKt.listOf((Object[]) new String[]{this.loader, "--library-path", this.linkDir.getPath(), "--preload", new File(this.libDir, "libkabukpw.so").getPath(), "--argv0", argv0, new File(this.libDir, bin).getPath()}), (Iterable) args).toArray(new String[0]);
    }

    public final String[] ssh(List<String> args) {
        Intrinsics.checkNotNullParameter(args, "args");
        return cmd("ssh", "libssh_bin.so", CollectionsKt.plus((Collection) CollectionsKt.listOf((Object[]) new String[]{"-F", this.sshConfig.getPath()}), (Iterable) args));
    }

    public final String[] moshClient(List<String> args) {
        Intrinsics.checkNotNullParameter(args, "args");
        return cmd("mosh-client", "libmoshclient_bin.so", args);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ String[] env$default(NativeEnv nativeEnv, Map map, int r2, Object obj) {
        if ((r2 & 1) != 0) {
            map = MapsKt.emptyMap();
        }
        return nativeEnv.env(map);
    }

    public final String[] env(Map<String, String> extra) {
        Intrinsics.checkNotNullParameter(extra, "extra");
        LinkedHashMap linkedMapOf = MapsKt.linkedMapOf(TuplesKt.to("HOME", this.home.getPath()), TuplesKt.to("USER", "kabuk"), TuplesKt.to("TERM", "xterm-256color"), TuplesKt.to("TERMINFO", this.terminfo.getPath()), TuplesKt.to("LANG", "C.UTF-8"), TuplesKt.to("LC_ALL", "C.UTF-8"), TuplesKt.to("SSH_AUTH_SOCK", this.agentSock.getPath()), TuplesKt.to("TMPDIR", this.ctx.getCacheDir().getPath()), TuplesKt.to("PATH", "/system/bin:/system/xbin"), TuplesKt.to("ANDROID_ROOT", "/system"), TuplesKt.to("ANDROID_DATA", "/data"));
        linkedMapOf.putAll(extra);
        LinkedHashMap linkedHashMap = linkedMapOf;
        ArrayList arrayList = new ArrayList(linkedHashMap.size());
        for (Map.Entry entry : linkedHashMap.entrySet()) {
            arrayList.add(entry.getKey() + "=" + entry.getValue());
        }
        return (String[]) arrayList.toArray(new String[0]);
    }

    /* renamed from: loaderPath, reason: from getter */
    public final String getLoader() {
        return this.loader;
    }
}
