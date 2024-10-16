package com.android.settings.bootloader;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import com.android.settings.C0242az;
import com.xiaomi.security.devicecredential.SecurityDeviceCredentialManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import miui.cloud.net.a;
import miui.os.Build;
import org.json.JSONObject;

/* compiled from: CloudDeviceStatus */
class b {
    private static final String bCT;
    private static final Uri bCU = Uri.parse("content://com.miui.securitycenter.provider");
    private static final Uri bCV = Uri.withAppendedPath(bCU, "getserinum");
    private static int bCW = 0;
    private static d bCX = new d();

    b() {
    }

    static {
        String str;
        if (Build.IS_INTERNATIONAL_BUILD) {
            str = "https://unlock.update.intl.miui.com";
        } else {
            str = "https://unlock.update.miui.com";
        }
        bCT = str;
    }

    private static String bvW(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append("POST\n").append(str).append("\n").append("data=").append(str2).append("&sid=").append("miui_sec_android");
        try {
            Mac instance = Mac.getInstance("HmacSHA1");
            instance.init(new SecretKeySpec("10f29ff413c89c8de02349cb3eb9a5f510f29ff413c89c8de02349cb3eb9a5f5".getBytes(), instance.getAlgorithm()));
            return c.bwi(instance.doFinal(sb.toString().getBytes())).toLowerCase();
        } catch (Exception e) {
            return null;
        }
    }

    public static d bvS(Context context) {
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        String bwb = c.bwb(context);
        if (bwb == null) {
            bCX.bCY = 1;
            return bCX;
        }
        if (c.bwc()) {
            if (!c.bwg(context)) {
                bCX.bCY = 2;
                return bCX;
            } else if (!c.bwh(context)) {
                bCX.bCY = 11;
                return bCX;
            } else {
                String bwj = c.bwj(context);
                if (TextUtils.isEmpty(bwj)) {
                    bCX.bCY = 7;
                    return bCX;
                }
                hashMap.put("imsi1", bwj);
            }
        }
        hashMap.put("userId", bwb);
        hashMap.put("device", c.bwk());
        hashMap.put("rom_version", Build.VERSION.INCREMENTAL);
        hashMap.put("heartbeat_mode", String.valueOf(2));
        hashMap.put("cloudsp_devId", c.bwl(context));
        hashMap.put("cloudsp_cpuId", bvX(context));
        hashMap.put("cloudsp_product", miui.os.Build.DEVICE);
        hashMap.put("cloudsp_userId", bwb);
        int i = 0;
        try {
            String securityDeviceId = SecurityDeviceCredentialManager.getSecurityDeviceId();
            hashMap.put("cloudsp_fid", securityDeviceId);
            hashMap.put("cloudsp_nonce", bvY(context, securityDeviceId));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("CloudDeviceStatus", "get paramter error: fid");
            i = 20090;
        }
        byte[] bvZ = bvZ(hashMap);
        if (bvZ == null) {
            i = 20091;
            hashMap.put("cloudp_sign", com.miui.activityutil.b.d);
        } else {
            hashMap.put("cloudp_sign", c.bwi(bvZ).toLowerCase());
        }
        JSONObject jSONObject = new JSONObject(hashMap);
        try {
            jSONObject.put("error_code", i);
        } catch (Exception e2) {
        }
        HashMap hashMap3 = new HashMap();
        hashMap3.put("sid", "miui_sec_android");
        hashMap3.put("data", jSONObject.toString());
        hashMap3.put("sign", bvW("/v1/unlock/applyBind", jSONObject.toString()));
        bvV(context, hashMap2);
        try {
            String bQU = C0242az.bQU();
            Log.i("CloudDeviceStatus", "args: " + C0242az.bQT(hashMap.toString(), bQU));
            Log.i("CloudDeviceStatus", "headers: " + C0242az.bQT(hashMap2.toString(), bQU));
        } catch (Exception e3) {
            Log.e("CloudDeviceStatus", "encrypt error:" + e3.getMessage());
        }
        try {
            bvT(new a().fdD(bCT + "/v1/unlock/applyBind", hashMap2, hashMap3), context);
        } catch (Exception e4) {
            bCX.bCY = 2;
            Log.d("CloudDeviceStatus", "post server error!");
        }
        return bCX;
    }

    private static void bvU() {
        try {
            SecurityDeviceCredentialManager.forceReload();
        } catch (Exception e) {
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean bvT(miui.cloud.net.b r5, android.content.Context r6) {
        /*
            r3 = 2
            r4 = 0
            java.lang.String r0 = "CloudDeviceStatus"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "stateCode: "
            java.lang.StringBuilder r1 = r1.append(r2)
            int r2 = r5.fwt
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            android.util.Log.d(r0, r1)
            java.lang.String r0 = "CloudDeviceStatus"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "content: "
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.Object r2 = r5.fwu
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            android.util.Log.d(r0, r1)
            com.android.settings.bootloader.d r0 = bCX
            r0.bCY = r3
            java.lang.Object r0 = r5.fwu
            if (r0 != 0) goto L_0x0071
            java.lang.Exception r0 = r5.fwv
            if (r0 == 0) goto L_0x006c
            com.android.settings.bootloader.d r0 = bCX
            r1 = 3
            r0.bCY = r1
            java.lang.String r0 = "CloudDeviceStatus"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "error: "
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.Exception r2 = r5.fwv
            java.lang.String r2 = r2.toString()
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            android.util.Log.e(r0, r1)
        L_0x006b:
            return r4
        L_0x006c:
            com.android.settings.bootloader.d r0 = bCX
            r0.bCY = r3
            goto L_0x006b
        L_0x0071:
            java.lang.Object r0 = r5.fwu     // Catch:{ Exception -> 0x00bc }
            org.json.JSONObject r0 = (org.json.JSONObject) r0     // Catch:{ Exception -> 0x00bc }
            java.lang.String r1 = "result"
            java.lang.String r1 = r0.optString(r1)     // Catch:{ Exception -> 0x00bc }
            java.lang.String r2 = "code"
            r3 = 5
            int r2 = r0.optInt(r2, r3)     // Catch:{ Exception -> 0x00bc }
            java.lang.String r3 = "ok"
            boolean r1 = r3.equals(r1)     // Catch:{ Exception -> 0x00bc }
            if (r1 != 0) goto L_0x008f
            if (r2 != 0) goto L_0x00a0
        L_0x008f:
            com.android.settings.bootloader.d r1 = bCX     // Catch:{ Exception -> 0x00bc }
            r2 = 0
            r1.bCY = r2     // Catch:{ Exception -> 0x00bc }
            java.lang.String r1 = "count"
            r2 = 0
            int r0 = r0.optInt(r1, r2)     // Catch:{ Exception -> 0x00bc }
            bCW = r0     // Catch:{ Exception -> 0x00bc }
            r0 = 1
            return r0
        L_0x00a0:
            com.android.settings.bootloader.d r0 = bCX     // Catch:{ Exception -> 0x00bc }
            r1 = 10
            r0.bCY = r1     // Catch:{ Exception -> 0x00bc }
            com.android.settings.bootloader.d r0 = bCX     // Catch:{ Exception -> 0x00bc }
            java.lang.String r1 = java.lang.String.valueOf(r2)     // Catch:{ Exception -> 0x00bc }
            r0.bCZ = r1     // Catch:{ Exception -> 0x00bc }
            switch(r2) {
                case 401: goto L_0x00b2;
                case 20052: goto L_0x00c1;
                case 20086: goto L_0x00c8;
                default: goto L_0x00b1;
            }     // Catch:{ Exception -> 0x00bc }
        L_0x00b1:
            return r4
        L_0x00b2:
            com.android.settings.bootloader.d r0 = bCX     // Catch:{ Exception -> 0x00bc }
            r1 = 401(0x191, float:5.62E-43)
            r0.bCY = r1     // Catch:{ Exception -> 0x00bc }
            com.android.settings.bootloader.c.bwm(r6)     // Catch:{ Exception -> 0x00bc }
            goto L_0x00b1
        L_0x00bc:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x00b1
        L_0x00c1:
            com.android.settings.bootloader.d r0 = bCX     // Catch:{ Exception -> 0x00bc }
            r1 = 9
            r0.bCY = r1     // Catch:{ Exception -> 0x00bc }
            goto L_0x00b1
        L_0x00c8:
            bvU()     // Catch:{ Exception -> 0x00bc }
            goto L_0x00b1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.bootloader.b.bvT(miui.cloud.net.b, android.content.Context):boolean");
    }

    private static String bvY(Context context, String str) {
        String bwb = c.bwb(context);
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        hashMap.put("cloudsp_devId", c.bwl(context));
        hashMap.put("cloudsp_fid", str);
        if (bwb != null) {
            hashMap.put("userId", bwb);
            hashMap.put("cloudsp_userId", bwb);
        }
        try {
            miui.cloud.net.b fdv = new a().fdv(c.bwn(bCT + "/v1/micloud/nonce", hashMap), hashMap2);
            return bvT(fdv, context) ? ((JSONObject) fdv.fwu).optJSONObject("data").optString("nonce") : com.miui.activityutil.b.d;
        } catch (Exception e) {
            return com.miui.activityutil.b.d;
        }
    }

    private static byte[] bvZ(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("POST&");
        sb.append("/mic/binding/v1/identified/device/account");
        for (Map.Entry entry : new TreeMap(map).entrySet()) {
            if (((String) entry.getKey()).startsWith("cloudsp_")) {
                sb.append("&").append((String) entry.getKey()).append("=").append((String) entry.getValue());
            }
        }
        try {
            return SecurityDeviceCredentialManager.signWithDeviceCredential(sb.toString().getBytes("UTF-8"), true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x003f  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x006c A[SYNTHETIC, Splitter:B:34:0x006c] */
    /* JADX WARNING: Removed duplicated region for block: B:44:? A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String bvX(android.content.Context r8) {
        /*
            r6 = 0
            java.lang.String r7 = com.android.settings.bootloader.c.bwo()
            boolean r0 = android.text.TextUtils.isEmpty(r7)
            if (r0 == 0) goto L_0x0061
            android.content.ContentResolver r0 = r8.getContentResolver()     // Catch:{ Exception -> 0x0057, all -> 0x0068 }
            android.net.Uri r1 = bCV     // Catch:{ Exception -> 0x0057, all -> 0x0068 }
            r2 = 0
            r3 = 0
            r4 = 0
            r5 = 0
            android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5)     // Catch:{ Exception -> 0x0057, all -> 0x0068 }
            if (r1 == 0) goto L_0x0050
            boolean r0 = r1.moveToFirst()     // Catch:{ Exception -> 0x007c }
            if (r0 == 0) goto L_0x007e
            java.lang.String r0 = "seriNum"
            int r0 = r1.getColumnIndex(r0)     // Catch:{ Exception -> 0x007c }
            java.lang.String r7 = r1.getString(r0)     // Catch:{ Exception -> 0x007c }
            r0 = r7
        L_0x002d:
            if (r1 == 0) goto L_0x0032
            r1.close()     // Catch:{ Exception -> 0x0052 }
        L_0x0032:
            java.lang.String r1 = r0.toLowerCase()
            java.lang.String r2 = "0x"
            boolean r1 = r1.startsWith(r2)
            if (r1 == 0) goto L_0x004f
            r1 = 16
            java.math.BigInteger r2 = new java.math.BigInteger     // Catch:{ Exception -> 0x0075 }
            r3 = 2
            java.lang.String r3 = r0.substring(r3)     // Catch:{ Exception -> 0x0075 }
            r2.<init>(r3, r1)     // Catch:{ Exception -> 0x0075 }
            java.lang.String r0 = r2.toString()     // Catch:{ Exception -> 0x0075 }
        L_0x004f:
            return r0
        L_0x0050:
            r0 = r7
            goto L_0x002d
        L_0x0052:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0032
        L_0x0057:
            r0 = move-exception
            r1 = r6
        L_0x0059:
            r0.printStackTrace()     // Catch:{ all -> 0x007a }
            if (r1 == 0) goto L_0x0061
            r1.close()     // Catch:{ Exception -> 0x0063 }
        L_0x0061:
            r0 = r7
            goto L_0x0032
        L_0x0063:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0061
        L_0x0068:
            r0 = move-exception
            r1 = r6
        L_0x006a:
            if (r1 == 0) goto L_0x006f
            r1.close()     // Catch:{ Exception -> 0x0070 }
        L_0x006f:
            throw r0
        L_0x0070:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x006f
        L_0x0075:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x004f
        L_0x007a:
            r0 = move-exception
            goto L_0x006a
        L_0x007c:
            r0 = move-exception
            goto L_0x0059
        L_0x007e:
            r0 = r7
            goto L_0x002d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.bootloader.b.bvX(android.content.Context):java.lang.String");
    }

    private static void bvV(Context context, Map<String, List<String>> map) {
        try {
            com.xiaomi.accountsdk.account.data.a bwp = c.bwp(context);
            String bwq = c.bwq(context);
            ArrayList arrayList = new ArrayList();
            arrayList.add("serviceToken=" + bwp.dmr + ";cUserId=" + bwq);
            map.put("Cookie", arrayList);
        } catch (Utils$AccountExcepiton e) {
            throw e;
        }
    }

    private static byte[] bwa(Map<String, String> map) {
        boolean z;
        StringBuilder sb = new StringBuilder();
        boolean z2 = true;
        for (Map.Entry entry : new TreeMap(map).entrySet()) {
            if (z2) {
                z = false;
            } else {
                sb.append("&");
                z = z2;
            }
            sb.append((String) entry.getKey()).append("=").append((String) entry.getValue());
            z2 = z;
        }
        try {
            return SecurityDeviceCredentialManager.signWithDeviceCredential(sb.toString().getBytes("UTF-8"), true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int bvR(Context context) {
        String str = null;
        try {
            str = SecurityDeviceCredentialManager.getSecurityDeviceId();
        } catch (Exception e) {
        }
        if (TextUtils.isEmpty(str)) {
            return -20090;
        }
        String bwb = c.bwb(context);
        if (bwb == null) {
            return -1;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("cpuId", bvX(context));
        hashMap.put("fid", str);
        hashMap.put("product", miui.os.Build.DEVICE);
        hashMap.put("uid", bwb);
        byte[] bwa = bwa(hashMap);
        if (bwa == null) {
            return -20091;
        }
        hashMap.put("tzSign", c.bwi(bwa).toLowerCase());
        JSONObject jSONObject = new JSONObject(hashMap);
        HashMap hashMap2 = new HashMap();
        hashMap2.put("sid", "miui_sec_android");
        hashMap2.put("data", jSONObject.toString());
        Log.d("CloudDeviceStatus", "data: " + jSONObject.toString());
        hashMap2.put("sign", bvW("/v1/unlock/deviceHeartbeat", jSONObject.toString()));
        try {
            if (bvT(new a().fdD(bCT + "/v1/unlock/deviceHeartbeat", (Map<String, List<String>>) null, hashMap2), context)) {
                return bCW;
            }
            return -1;
        } catch (Exception e2) {
            return -2;
        }
    }
}
