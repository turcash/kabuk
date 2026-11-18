package com.kabuk;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.json.JSONObject;

/* compiled from: Profiles.kt */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u001b\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\b\u0018\u0000 ,2\u00020\u0001:\u0001,B[\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0007\u0012\b\b\u0002\u0010\f\u001a\u00020\r¢\u0006\u0002\u0010\u000eJ\t\u0010\u001b\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001c\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001d\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001e\u001a\u00020\u0007HÆ\u0003J\t\u0010\u001f\u001a\u00020\u0003HÆ\u0003J\t\u0010 \u001a\u00020\u0003HÆ\u0003J\t\u0010!\u001a\u00020\u0003HÆ\u0003J\t\u0010\"\u001a\u00020\u0007HÆ\u0003J\t\u0010#\u001a\u00020\rHÆ\u0003Jc\u0010$\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u00072\b\b\u0002\u0010\f\u001a\u00020\rHÆ\u0001J\u0013\u0010%\u001a\u00020\r2\b\u0010&\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010'\u001a\u00020\u0007HÖ\u0001J\u000e\u0010(\u001a\n **\u0004\u0018\u00010)0)J\t\u0010+\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u000b\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0012R\u0011\u0010\t\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0012R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0012R\u0011\u0010\f\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0010R\u0011\u0010\n\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0012R\u0011\u0010\b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0012¨\u0006-"}, d2 = {"Lcom/kabuk/Profile;", "", "id", "", "name", "host", "port", "", "user", "mode", "session", "color", "notify", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;IZ)V", "getColor", "()I", "getHost", "()Ljava/lang/String;", "getId", "getMode", "getName", "getNotify", "()Z", "getPort", "getSession", "getUser", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toJson", "Lorg/json/JSONObject;", "kotlin.jvm.PlatformType", "toString", "Companion", "client_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
/* loaded from: classes.dex */
public final /* data */ class Profile {
    public static final int LOCAL_COLOR = -13722021;
    private final int color;
    private final String host;
    private final String id;
    private final String mode;
    private final String name;
    private final boolean notify;
    private final int port;
    private final String session;
    private final String user;

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final int[] COLORS = {-2061778, -2534577, -6596170, -12874032, -3693312};

    /* renamed from: component1, reason: from getter */
    public final String getId() {
        return this.id;
    }

    /* renamed from: component2, reason: from getter */
    public final String getName() {
        return this.name;
    }

    /* renamed from: component3, reason: from getter */
    public final String getHost() {
        return this.host;
    }

    /* renamed from: component4, reason: from getter */
    public final int getPort() {
        return this.port;
    }

    /* renamed from: component5, reason: from getter */
    public final String getUser() {
        return this.user;
    }

    /* renamed from: component6, reason: from getter */
    public final String getMode() {
        return this.mode;
    }

    /* renamed from: component7, reason: from getter */
    public final String getSession() {
        return this.session;
    }

    /* renamed from: component8, reason: from getter */
    public final int getColor() {
        return this.color;
    }

    /* renamed from: component9, reason: from getter */
    public final boolean getNotify() {
        return this.notify;
    }

    public final Profile copy(String id, String name, String host, int port, String user, String mode, String session, int color, boolean notify) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(host, "host");
        Intrinsics.checkNotNullParameter(user, "user");
        Intrinsics.checkNotNullParameter(mode, "mode");
        Intrinsics.checkNotNullParameter(session, "session");
        return new Profile(id, name, host, port, user, mode, session, color, notify);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Profile)) {
            return false;
        }
        Profile profile = (Profile) other;
        return Intrinsics.areEqual(this.id, profile.id) && Intrinsics.areEqual(this.name, profile.name) && Intrinsics.areEqual(this.host, profile.host) && this.port == profile.port && Intrinsics.areEqual(this.user, profile.user) && Intrinsics.areEqual(this.mode, profile.mode) && Intrinsics.areEqual(this.session, profile.session) && this.color == profile.color && this.notify == profile.notify;
    }

    public int hashCode() {
        return (((((((((((((((this.id.hashCode() * 31) + this.name.hashCode()) * 31) + this.host.hashCode()) * 31) + Integer.hashCode(this.port)) * 31) + this.user.hashCode()) * 31) + this.mode.hashCode()) * 31) + this.session.hashCode()) * 31) + Integer.hashCode(this.color)) * 31) + Boolean.hashCode(this.notify);
    }

    public String toString() {
        return "Profile(id=" + this.id + ", name=" + this.name + ", host=" + this.host + ", port=" + this.port + ", user=" + this.user + ", mode=" + this.mode + ", session=" + this.session + ", color=" + this.color + ", notify=" + this.notify + ")";
    }

    public Profile(String id, String name, String host, int r5, String user, String mode, String session, int r9, boolean z) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(host, "host");
        Intrinsics.checkNotNullParameter(user, "user");
        Intrinsics.checkNotNullParameter(mode, "mode");
        Intrinsics.checkNotNullParameter(session, "session");
        this.id = id;
        this.name = name;
        this.host = host;
        this.port = r5;
        this.user = user;
        this.mode = mode;
        this.session = session;
        this.color = r9;
        this.notify = z;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public /* synthetic */ Profile(java.lang.String r14, java.lang.String r15, java.lang.String r16, int r17, java.lang.String r18, java.lang.String r19, java.lang.String r20, int r21, boolean r22, int r23, kotlin.jvm.internal.DefaultConstructorMarker r24) {
        /*
            r13 = this;
            r0 = r23
            r1 = r0 & 1
            if (r1 == 0) goto L15
            java.util.UUID r1 = java.util.UUID.randomUUID()
            java.lang.String r1 = r1.toString()
            java.lang.String r2 = "toString(...)"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r1, r2)
            r4 = r1
            goto L16
        L15:
            r4 = r14
        L16:
            r1 = r0 & 8
            if (r1 == 0) goto L1e
            r1 = 22
            r7 = r1
            goto L20
        L1e:
            r7 = r17
        L20:
            r1 = r0 & 16
            if (r1 == 0) goto L28
            java.lang.String r1 = "dev"
            r8 = r1
            goto L2a
        L28:
            r8 = r18
        L2a:
            r1 = r0 & 32
            if (r1 == 0) goto L32
            java.lang.String r1 = "mosh"
            r9 = r1
            goto L34
        L32:
            r9 = r19
        L34:
            r1 = r0 & 64
            if (r1 == 0) goto L3c
            java.lang.String r1 = "claude"
            r10 = r1
            goto L3e
        L3c:
            r10 = r20
        L3e:
            r1 = r0 & 128(0x80, float:1.794E-43)
            if (r1 == 0) goto L47
            r1 = -2061778(0xffffffffffe08a2e, float:NaN)
            r11 = r1
            goto L49
        L47:
            r11 = r21
        L49:
            r0 = r0 & 256(0x100, float:3.59E-43)
            if (r0 == 0) goto L50
            r0 = 1
            r12 = r0
            goto L52
        L50:
            r12 = r22
        L52:
            r3 = r13
            r5 = r15
            r6 = r16
            r3.<init>(r4, r5, r6, r7, r8, r9, r10, r11, r12)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.kabuk.Profile.<init>(java.lang.String, java.lang.String, java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, int, boolean, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    public final String getId() {
        return this.id;
    }

    public final String getName() {
        return this.name;
    }

    public final String getHost() {
        return this.host;
    }

    public final int getPort() {
        return this.port;
    }

    public final String getUser() {
        return this.user;
    }

    public final String getMode() {
        return this.mode;
    }

    public final String getSession() {
        return this.session;
    }

    public final int getColor() {
        return this.color;
    }

    public final boolean getNotify() {
        return this.notify;
    }

    public final JSONObject toJson() {
        return new JSONObject().put("id", this.id).put("name", this.name).put("host", this.host).put("port", this.port).put("user", this.user).put("mode", this.mode).put("session", this.session).put("color", this.color).put("notify", this.notify);
    }

    /* compiled from: Profiles.kt */
    @Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0015\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fR\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u000e\u0010\u0007\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lcom/kabuk/Profile$Companion;", "", "()V", "COLORS", "", "getCOLORS", "()[I", "LOCAL_COLOR", "", "from", "Lcom/kabuk/Profile;", "o", "Lorg/json/JSONObject;", "client_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final Profile from(JSONObject o) {
            Intrinsics.checkNotNullParameter(o, "o");
            String string = o.getString("id");
            Intrinsics.checkNotNullExpressionValue(string, "getString(...)");
            String string2 = o.getString("name");
            Intrinsics.checkNotNullExpressionValue(string2, "getString(...)");
            String string3 = o.getString("host");
            Intrinsics.checkNotNullExpressionValue(string3, "getString(...)");
            int optInt = o.optInt("port", 22);
            String optString = o.optString("user", "dev");
            Intrinsics.checkNotNullExpressionValue(optString, "optString(...)");
            String optString2 = o.optString("mode", "mosh");
            Intrinsics.checkNotNullExpressionValue(optString2, "optString(...)");
            String optString3 = o.optString("session", "claude");
            Intrinsics.checkNotNullExpressionValue(optString3, "optString(...)");
            return new Profile(string, string2, string3, optInt, optString, optString2, optString3, o.optInt("color", -2061778), o.optBoolean("notify", true));
        }

        public final int[] getCOLORS() {
            return Profile.COLORS;
        }
    }
}
