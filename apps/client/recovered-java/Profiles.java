package com.kabuk;

import android.content.Context;
import android.content.SharedPreferences;
import com.kabuk.Profile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.IntIterator;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import org.json.JSONArray;
import org.json.JSONObject;

/* compiled from: Profiles.kt */
@Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u0007J\u0016\u0010\b\u001a\u00020\t2\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000bJ\u0018\u0010\f\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000bJ\u0018\u0010\r\u001a\n \u000f*\u0004\u0018\u00010\u000e0\u000e2\u0006\u0010\u0006\u001a\u00020\u0007H\u0002J\u001c\u0010\u0010\u001a\u00020\t2\u0006\u0010\u0006\u001a\u00020\u00072\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004J\u0016\u0010\u0012\u001a\u00020\t2\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u0013\u001a\u00020\u0005¨\u0006\u0014"}, d2 = {"Lcom/kabuk/Profiles;", "", "()V", "all", "", "Lcom/kabuk/Profile;", "c", "Landroid/content/Context;", "delete", "", "id", "", "get", "prefs", "Landroid/content/SharedPreferences;", "kotlin.jvm.PlatformType", "save", "list", "upsert", "p", "client_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
/* loaded from: classes.dex */
public final class Profiles {
    public static final Profiles INSTANCE = new Profiles();

    private Profiles() {
    }

    private final SharedPreferences prefs(Context c) {
        return c.getSharedPreferences("profiles", 0);
    }

    public final List<Profile> all(Context c) {
        Intrinsics.checkNotNullParameter(c, "c");
        String string = prefs(c).getString("list", null);
        if (string == null) {
            List<Profile> listOf = CollectionsKt.listOf(new Profile(null, "note10", "note10", 0, null, null, null, 0, false, 505, null));
            save(c, listOf);
            return listOf;
        }
        JSONArray jSONArray = new JSONArray(string);
        IntRange until = RangesKt.until(0, jSONArray.length());
        ArrayList arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(until, 10));
        Iterator<Integer> it = until.iterator();
        while (it.hasNext()) {
            int nextInt = ((IntIterator) it).nextInt();
            Profile.Companion companion = Profile.INSTANCE;
            JSONObject jSONObject = jSONArray.getJSONObject(nextInt);
            Intrinsics.checkNotNullExpressionValue(jSONObject, "getJSONObject(...)");
            arrayList.add(companion.from(jSONObject));
        }
        return arrayList;
    }

    public final Profile get(Context c, String id) {
        Object obj;
        Intrinsics.checkNotNullParameter(c, "c");
        Intrinsics.checkNotNullParameter(id, "id");
        Iterator<T> it = all(c).iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            if (Intrinsics.areEqual(((Profile) obj).getId(), id)) {
                break;
            }
        }
        return (Profile) obj;
    }

    public final void save(Context c, List<Profile> list) {
        Intrinsics.checkNotNullParameter(c, "c");
        Intrinsics.checkNotNullParameter(list, "list");
        SharedPreferences.Editor edit = prefs(c).edit();
        List<Profile> list2 = list;
        ArrayList arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list2, 10));
        Iterator<T> it = list2.iterator();
        while (it.hasNext()) {
            arrayList.add(((Profile) it.next()).toJson());
        }
        edit.putString("list", new JSONArray((Collection) arrayList).toString()).apply();
    }

    public final void upsert(Context c, Profile p) {
        Intrinsics.checkNotNullParameter(c, "c");
        Intrinsics.checkNotNullParameter(p, "p");
        List<Profile> all = all(c);
        ArrayList arrayList = new ArrayList();
        for (Object obj : all) {
            if (!Intrinsics.areEqual(((Profile) obj).getId(), p.getId())) {
                arrayList.add(obj);
            }
        }
        save(c, CollectionsKt.plus((Collection<? extends Profile>) arrayList, p));
    }

    public final void delete(Context c, String id) {
        Intrinsics.checkNotNullParameter(c, "c");
        Intrinsics.checkNotNullParameter(id, "id");
        List<Profile> all = all(c);
        ArrayList arrayList = new ArrayList();
        for (Object obj : all) {
            if (!Intrinsics.areEqual(((Profile) obj).getId(), id)) {
                arrayList.add(obj);
            }
        }
        save(c, arrayList);
    }
}
