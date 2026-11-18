package com.kabuk.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.MapsKt;
import kotlin.io.CloseableKt;
import kotlin.io.ConstantsKt;
import kotlin.io.TextStreamsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;

/* compiled from: RootShell.kt */
@Metadata(d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\t\n\u0000\n\u0002\u0010$\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004J \u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\nJ\u000e\u0010\f\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\nJF\u0010\u000e\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\n2\b\b\u0002\u0010\u0010\u001a\u00020\u00112\u0014\b\u0002\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0\u00132\u0016\b\u0002\u0010\u0014\u001a\u0010\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u0016\u0018\u00010\u0015¨\u0006\u0017"}, d2 = {"Lcom/kabuk/core/RootShell;", "", "()V", "available", "", "install", "Lcom/kabuk/core/ShellResult;", "src", "Ljava/io/File;", "dst", "", "mode", "quote", "s", "run", "script", "timeoutSec", "", "env", "", "onLine", "Lkotlin/Function1;", "", "core_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
/* loaded from: classes.dex */
public final class RootShell {
    public static final RootShell INSTANCE = new RootShell();

    private RootShell() {
    }

    public final boolean available() {
        ShellResult run$default = run$default(this, "id -u", 15L, null, null, 12, null);
        return run$default.getOk() && Intrinsics.areEqual(StringsKt.trim((CharSequence) run$default.getOutput()).toString(), "0");
    }

    public static /* synthetic */ ShellResult run$default(RootShell rootShell, String str, long j, Map map, Function1 function1, int r12, Object obj) {
        if ((r12 & 2) != 0) {
            j = 600;
        }
        long j2 = j;
        if ((r12 & 4) != 0) {
            map = MapsKt.emptyMap();
        }
        Map map2 = map;
        if ((r12 & 8) != 0) {
            function1 = null;
        }
        return rootShell.run(str, j2, map2, function1);
    }

    public final ShellResult run(String script, long timeoutSec, Map<String, String> env, final Function1<? super String, Unit> onLine) {
        String sb;
        Intrinsics.checkNotNullParameter(script, "script");
        Intrinsics.checkNotNullParameter(env, "env");
        try {
            final Process start = new ProcessBuilder("su").redirectErrorStream(true).start();
            OutputStream outputStream = start.getOutputStream();
            Intrinsics.checkNotNullExpressionValue(outputStream, "getOutputStream(...)");
            Writer outputStreamWriter = new OutputStreamWriter(outputStream, Charsets.UTF_8);
            BufferedWriter bufferedWriter = outputStreamWriter instanceof BufferedWriter ? (BufferedWriter) outputStreamWriter : new BufferedWriter(outputStreamWriter, ConstantsKt.DEFAULT_BUFFER_SIZE);
            try {
                BufferedWriter bufferedWriter2 = bufferedWriter;
                for (Map.Entry<String, String> entry : env.entrySet()) {
                    bufferedWriter2.write("export " + entry.getKey() + "=" + INSTANCE.quote(entry.getValue()) + "\n");
                }
                bufferedWriter2.write(script);
                bufferedWriter2.write("\nexit $?\n");
                Unit unit = Unit.INSTANCE;
                CloseableKt.closeFinally(bufferedWriter, null);
                final StringBuilder sb2 = new StringBuilder();
                Thread thread = new Thread(new Runnable() { // from class: com.kabuk.core.RootShell$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        RootShell.run$lambda$3(start, sb2, onLine);
                    }
                });
                thread.start();
                boolean waitFor = start.waitFor(timeoutSec, TimeUnit.SECONDS);
                if (!waitFor) {
                    start.destroyForcibly();
                }
                thread.join(2000L);
                int exitValue = waitFor ? start.exitValue() : 124;
                synchronized (sb2) {
                    sb = sb2.toString();
                    Intrinsics.checkNotNullExpressionValue(sb, "toString(...)");
                }
                return new ShellResult(exitValue, sb);
            } finally {
            }
        } catch (Exception e) {
            return new ShellResult(127, "su calistirilamadi: " + e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void run$lambda$3(Process process, final StringBuilder sb, final Function1 function1) {
        Intrinsics.checkNotNullParameter(sb, "$sb");
        InputStream inputStream = process.getInputStream();
        Intrinsics.checkNotNullExpressionValue(inputStream, "getInputStream(...)");
        Reader inputStreamReader = new InputStreamReader(inputStream, Charsets.UTF_8);
        TextStreamsKt.forEachLine(inputStreamReader instanceof BufferedReader ? (BufferedReader) inputStreamReader : new BufferedReader(inputStreamReader, ConstantsKt.DEFAULT_BUFFER_SIZE), new Function1<String, Unit>() { // from class: com.kabuk.core.RootShell$run$reader$1$1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            /* JADX WARN: Multi-variable type inference failed */
            {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(String str) {
                invoke2(str);
                return Unit.INSTANCE;
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(String line) {
                Intrinsics.checkNotNullParameter(line, "line");
                StringBuilder sb2 = sb;
                synchronized (sb2) {
                    StringBuilder append = sb2.append(line);
                    Intrinsics.checkNotNullExpressionValue(append, "append(...)");
                    Intrinsics.checkNotNullExpressionValue(append.append('\n'), "append(...)");
                }
                Function1<String, Unit> function12 = function1;
                if (function12 != null) {
                    function12.invoke(line);
                }
            }
        });
    }

    public static /* synthetic */ ShellResult install$default(RootShell rootShell, File file, String str, String str2, int r4, Object obj) {
        if ((r4 & 4) != 0) {
            str2 = "755";
        }
        return rootShell.install(file, str, str2);
    }

    public final ShellResult install(File src, String dst, String mode) {
        Intrinsics.checkNotNullParameter(src, "src");
        Intrinsics.checkNotNullParameter(dst, "dst");
        Intrinsics.checkNotNullParameter(mode, "mode");
        String parent = new File(dst).getParent();
        Intrinsics.checkNotNull(parent);
        String quote = quote(parent);
        String absolutePath = src.getAbsolutePath();
        Intrinsics.checkNotNullExpressionValue(absolutePath, "getAbsolutePath(...)");
        return run$default(this, "mkdir -p " + quote + " && cp " + quote(absolutePath) + " " + quote(dst) + " && chmod " + mode + " " + quote(dst), 0L, null, null, 14, null);
    }

    public final String quote(String s) {
        Intrinsics.checkNotNullParameter(s, "s");
        return "'" + StringsKt.replace$default(s, "'", "'\\''", false, 4, (Object) null) + "'";
    }
}
