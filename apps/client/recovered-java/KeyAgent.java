package com.kabuk;

import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.util.Base64;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.Key;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECPoint;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.ByteCompanionObject;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;

/* compiled from: KeyAgent.kt */
@Metadata(d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0006\u0010\n\u001a\u00020\u000bJ\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J\u0010\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\rH\u0002J\u0010\u0010\u0012\u001a\n \u0014*\u0004\u0018\u00010\u00130\u0013H\u0002J\b\u0010\u0015\u001a\u00020\u0016H\u0002J\b\u0010\u0017\u001a\u00020\u0018H\u0002J\u0006\u0010\u0019\u001a\u00020\rJ\u0006\u0010\u001a\u001a\u00020\u0004J\u0010\u0010\u001b\u001a\u00020\u00012\u0006\u0010\u001c\u001a\u00020\u0007H\u0002J\u0010\u0010\u001d\u001a\u00020\r2\u0006\u0010\u001e\u001a\u00020\rH\u0002J\u000e\u0010\u001f\u001a\u00020\u000b2\u0006\u0010 \u001a\u00020!J\u0010\u0010\"\u001a\u00020\r2\u0006\u0010#\u001a\u00020\rH\u0002J\u0010\u0010$\u001a\u00020\r2\u0006\u0010%\u001a\u00020&H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006'"}, d2 = {"Lcom/kabuk/KeyAgent;", "", "()V", "ALIAS", "", "KT", "bound", "Landroid/net/LocalSocket;", "server", "Landroid/net/LocalServerSocket;", "ensureKey", "", "fixed32", "", "x", "Ljava/math/BigInteger;", "handle", "m", "ks", "Ljava/security/KeyStore;", "kotlin.jvm.PlatformType", "priv", "Ljava/security/PrivateKey;", "pub", "Ljava/security/interfaces/ECPublicKey;", "publicBlob", "publicKeyLine", "serve", "c", "sign", "data", "start", "sock", "Ljava/io/File;", "str", "b", "u32", "v", "", "client_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
/* loaded from: classes.dex */
public final class KeyAgent {
    private static final String ALIAS = "kabuk-ssh-ecdsa";
    public static final KeyAgent INSTANCE = new KeyAgent();
    private static final String KT = "ecdsa-sha2-nistp256";
    private static volatile LocalSocket bound;
    private static volatile LocalServerSocket server;

    private KeyAgent() {
    }

    private final KeyStore ks() {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        return keyStore;
    }

    public final void ensureKey() {
        if (ks().containsAlias(ALIAS)) {
            return;
        }
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", "AndroidKeyStore");
        keyPairGenerator.initialize(new KeyGenParameterSpec.Builder(ALIAS, 4).setAlgorithmParameterSpec(new ECGenParameterSpec("secp256r1")).setDigests("SHA-256").build());
        keyPairGenerator.generateKeyPair();
    }

    private final ECPublicKey pub() {
        PublicKey publicKey = ks().getCertificate(ALIAS).getPublicKey();
        Intrinsics.checkNotNull(publicKey, "null cannot be cast to non-null type java.security.interfaces.ECPublicKey");
        return (ECPublicKey) publicKey;
    }

    private final PrivateKey priv() {
        Key key = ks().getKey(ALIAS, null);
        Intrinsics.checkNotNull(key, "null cannot be cast to non-null type java.security.PrivateKey");
        return (PrivateKey) key;
    }

    private final byte[] u32(int v) {
        return new byte[]{(byte) (v >>> 24), (byte) (v >>> 16), (byte) (v >>> 8), (byte) v};
    }

    private final byte[] str(byte[] b) {
        return ArraysKt.plus(u32(b.length), b);
    }

    private final byte[] fixed32(BigInteger x) {
        byte[] byteArray = x.toByteArray();
        if (byteArray.length > 32) {
            Intrinsics.checkNotNull(byteArray);
            byteArray = ArraysKt.copyOfRange(byteArray, byteArray.length - 32, byteArray.length);
        }
        byte[] bArr = new byte[32 - byteArray.length];
        Intrinsics.checkNotNull(byteArray);
        return ArraysKt.plus(bArr, byteArray);
    }

    public final byte[] publicBlob() {
        ECPoint w = pub().getW();
        BigInteger affineX = w.getAffineX();
        Intrinsics.checkNotNullExpressionValue(affineX, "getAffineX(...)");
        byte[] plus = ArraysKt.plus(new byte[]{4}, fixed32(affineX));
        BigInteger affineY = w.getAffineY();
        Intrinsics.checkNotNullExpressionValue(affineY, "getAffineY(...)");
        byte[] plus2 = ArraysKt.plus(plus, fixed32(affineY));
        byte[] bytes = KT.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        byte[] str = str(bytes);
        byte[] bytes2 = "nistp256".getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes2, "getBytes(...)");
        return ArraysKt.plus(ArraysKt.plus(str, str(bytes2)), str(plus2));
    }

    public final String publicKeyLine() {
        String encodeToString = Base64.encodeToString(publicBlob(), 2);
        String MODEL = Build.MODEL;
        Intrinsics.checkNotNullExpressionValue(MODEL, "MODEL");
        return "ecdsa-sha2-nistp256 " + encodeToString + " kabuk@" + StringsKt.replace$default(MODEL, ' ', '-', false, 4, (Object) null);
    }

    private final byte[] sign(byte[] data) {
        Signature signature = Signature.getInstance("SHA256withECDSA");
        signature.initSign(INSTANCE.priv());
        signature.update(data);
        byte[] sign = signature.sign();
        Ref.IntRef intRef = new Ref.IntRef();
        intRef.element = 2;
        if ((sign[1] & ByteCompanionObject.MIN_VALUE) != 0) {
            intRef.element += sign[1] & ByteCompanionObject.MAX_VALUE;
        }
        BigInteger sign$int = sign$int(sign, intRef);
        BigInteger sign$int2 = sign$int(sign, intRef);
        byte[] byteArray = sign$int.toByteArray();
        Intrinsics.checkNotNullExpressionValue(byteArray, "toByteArray(...)");
        byte[] str = str(byteArray);
        byte[] byteArray2 = sign$int2.toByteArray();
        Intrinsics.checkNotNullExpressionValue(byteArray2, "toByteArray(...)");
        byte[] plus = ArraysKt.plus(str, str(byteArray2));
        byte[] bytes = KT.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        return ArraysKt.plus(str(bytes), str(plus));
    }

    private static final BigInteger sign$int(byte[] bArr, Ref.IntRef intRef) {
        if (bArr[intRef.element] != 2) {
            throw new IllegalStateException("Check failed.".toString());
        }
        byte b = bArr[intRef.element + 1];
        Intrinsics.checkNotNull(bArr);
        BigInteger bigInteger = new BigInteger(ArraysKt.copyOfRange(bArr, intRef.element + 2, intRef.element + 2 + b));
        intRef.element += b + 2;
        return bigInteger;
    }

    public final synchronized void start(File sock) {
        Intrinsics.checkNotNullParameter(sock, "sock");
        if (server != null) {
            return;
        }
        ensureKey();
        sock.delete();
        LocalSocket localSocket = new LocalSocket(2);
        localSocket.bind(new LocalSocketAddress(sock.getPath(), LocalSocketAddress.Namespace.FILESYSTEM));
        final LocalServerSocket localServerSocket = new LocalServerSocket(localSocket.getFileDescriptor());
        server = localServerSocket;
        bound = localSocket;
        Thread thread = new Thread(new Runnable() { // from class: com.kabuk.KeyAgent$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                KeyAgent.start$lambda$5(localServerSocket);
            }
        }, "kabuk-agent");
        thread.setDaemon(true);
        thread.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void start$lambda$5(LocalServerSocket srv) {
        Intrinsics.checkNotNullParameter(srv, "$srv");
        while (true) {
            try {
                final LocalSocket accept = srv.accept();
                Thread thread = new Thread(new Runnable() { // from class: com.kabuk.KeyAgent$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        KeyAgent.start$lambda$5$lambda$3(accept);
                    }
                }, "kabuk-agent-conn");
                thread.setDaemon(true);
                thread.start();
            } catch (Exception unused) {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void start$lambda$5$lambda$3(LocalSocket localSocket) {
        KeyAgent keyAgent = INSTANCE;
        Intrinsics.checkNotNull(localSocket);
        keyAgent.serve(localSocket);
    }

    private final Object serve(LocalSocket c) {
        try {
            LocalSocket localSocket = c;
            try {
                LocalSocket localSocket2 = localSocket;
                DataInputStream dataInputStream = new DataInputStream(c.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(c.getOutputStream());
                while (true) {
                    try {
                        byte[] bArr = new byte[dataInputStream.readInt()];
                        dataInputStream.readFully(bArr);
                        byte[] handle = INSTANCE.handle(bArr);
                        dataOutputStream.writeInt(handle.length);
                        dataOutputStream.write(handle);
                        dataOutputStream.flush();
                    } catch (Exception unused) {
                        Unit unit = Unit.INSTANCE;
                        CloseableKt.closeFinally(localSocket, null);
                        return Unit.INSTANCE;
                    }
                }
            } finally {
            }
        } catch (Exception e) {
            return Integer.valueOf(Log.w("KabukAgent", "baglanti: " + e.getMessage()));
        }
    }

    private final byte[] handle(byte[] m) {
        ByteBuffer wrap = ByteBuffer.wrap(m);
        byte b = wrap.get();
        if (b != 11) {
            if (b == 13) {
                byte[] bArr = new byte[wrap.getInt()];
                wrap.get(bArr);
                byte[] bArr2 = new byte[wrap.getInt()];
                wrap.get(bArr2);
                return !Arrays.equals(bArr, publicBlob()) ? new byte[]{5} : ArraysKt.plus(new byte[]{14}, str(sign(bArr2)));
            }
            return new byte[]{5};
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(12);
        byteArrayOutputStream.write(u32(1));
        byteArrayOutputStream.write(str(publicBlob()));
        byte[] bytes = "kabuk-keystore".getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        byteArrayOutputStream.write(str(bytes));
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        Intrinsics.checkNotNull(byteArray);
        return byteArray;
    }
}
