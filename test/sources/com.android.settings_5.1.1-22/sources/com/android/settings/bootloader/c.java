package com.android.settings.bootloader;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.android.settings.bootloader.Utils;
import com.xiaomi.accountsdk.account.data.ExtendedAuthToken;
import com.xiaomi.security.devicecredential.SecurityDeviceCredentialManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import miui.cloud.net.XHttpClient;
import miui.os.Build;
import org.json.JSONObject;

/* compiled from: CloudDeviceStatus */
class c {
    private static final Uri UL = Uri.parse("content://com.miui.securitycenter.provider");
    private static final Uri UM = Uri.withAppendedPath(UL, "getserinum");
    private static d UN = new d();

    public static d aG(Context context) {
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        try {
            hashMap.put("cloudsp_fid", SecurityDeviceCredentialManager.getSecurityDeviceId());
            String aH = aH(context);
            if (aH == null) {
                return UN;
            }
            String aK = Utils.aK(context);
            if (aK == null) {
                UN.UO = 1;
                return UN;
            }
            hashMap.put("cloudsp_nonce", aH);
            hashMap.put("cloudsp_devId", Utils.getDeviceId(context));
            hashMap.put("cloudsp_cpuId", aI(context));
            hashMap.put("cloudsp_product", Build.DEVICE);
            hashMap.put("userId", aK);
            hashMap.put("cloudsp_userId", aK);
            byte[] a = a((Map) hashMap);
            if (a == null) {
                throw new Utils.ParameterException("get paramter error: sign");
            }
            hashMap.put("cloudp_sign", Utils.f(a).toLowerCase());
            a(context, hashMap2);
            Log.d("CloudDeviceStatus", "args: " + hashMap);
            Log.d("CloudDeviceStatus", "headers: " + hashMap2);
            try {
                if (a(new XHttpClient().syncPost("https://deviceapi.micloud.xiaomi.net/mic/binding/v1/identified/device/account", hashMap2, hashMap))) {
                    UN.UO = 0;
                    return UN;
                }
            } catch (Exception e) {
                UN.UO = 2;
                Log.d("CloudDeviceStatus", "post server error!");
            }
            return UN;
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new Utils.ParameterException("get paramter error: fid");
        }
    }

    private static boolean a(XHttpClient.HttpResponse httpResponse) {
        Log.d("CloudDeviceStatus", "stateCode: " + httpResponse.stateCode);
        Log.d("CloudDeviceStatus", "content: " + httpResponse.content);
        UN.UO = 2;
        if (httpResponse.stateCode != 200) {
            return false;
        }
        if (httpResponse.content == null) {
            if (httpResponse.error != null) {
                UN.UO = 3;
                Log.d("CloudDeviceStatus", "eror: " + httpResponse.error.toString());
            } else {
                UN.UO = 2;
            }
            return false;
        }
        try {
            JSONObject jSONObject = (JSONObject) httpResponse.content;
            if ("ok".equals(jSONObject.getString("result"))) {
                return true;
            }
            UN.UO = 4;
            UN.UP = jSONObject.getInt("code") + ": " + jSONObject.getString("description");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String aH(Context context) {
        String aK = Utils.aK(context);
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        hashMap.put("cloudsp_devId", Utils.getDeviceId(context));
        if (aK != null) {
            hashMap.put("userId", aK);
            hashMap.put("cloudsp_userId", aK);
        }
        try {
            hashMap.put("cloudsp_fid", SecurityDeviceCredentialManager.getSecurityDeviceId());
            try {
                XHttpClient.HttpResponse syncGet = new XHttpClient().syncGet(Utils.a("https://deviceapi.micloud.xiaomi.net/mic/find/v4/anonymous/challenge", (Map) hashMap), hashMap2);
                if (a(syncGet)) {
                    return ((JSONObject) syncGet.content).getJSONObject("data").getString("nonce");
                }
                return null;
            } catch (Exception e) {
                UN.UO = 2;
                Log.d("CloudDeviceStatus", "getNonce error!");
                return null;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new Utils.ParameterException("get paramter error: fid");
        }
    }

    private static byte[] a(Map map) {
        StringBuilder sb = new StringBuilder();
        sb.append("POST&");
        sb.append("/mic/binding/v1/identified/device/account");
        for (Map.Entry entry : new TreeMap(map).entrySet()) {
            if (((String) entry.getKey()).startsWith("cloudsp_")) {
                sb.append("&" + ((String) entry.getKey()) + "=" + ((String) entry.getValue()));
            }
        }
        try {
            return SecurityDeviceCredentialManager.signWithDeviceCredential(sb.toString().getBytes("UTF-8"), true);
        } catch (Exception e) {
            Log.d("CloudDeviceStatus", "getSignData error!");
            e.printStackTrace();
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0026 A[SYNTHETIC, Splitter:B:10:0x0026] */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0035  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0061 A[SYNTHETIC, Splitter:B:32:0x0061] */
    /* JADX WARNING: Removed duplicated region for block: B:42:? A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String aI(android.content.Context r8) {
        /*
            r7 = 0
            java.lang.String r6 = ""
            android.content.ContentResolver r0 = r8.getContentResolver()     // Catch:{ Exception -> 0x004b, all -> 0x005d }
            android.net.Uri r1 = UM     // Catch:{ Exception -> 0x004b, all -> 0x005d }
            r2 = 0
            r3 = 0
            r4 = 0
            r5 = 0
            android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5)     // Catch:{ Exception -> 0x004b, all -> 0x005d }
            if (r1 == 0) goto L_0x0073
            boolean r0 = r1.moveToFirst()     // Catch:{ Exception -> 0x0071 }
            if (r0 == 0) goto L_0x0073
            java.lang.String r0 = "seriNum"
            int r0 = r1.getColumnIndex(r0)     // Catch:{ Exception -> 0x0071 }
            java.lang.String r6 = r1.getString(r0)     // Catch:{ Exception -> 0x0071 }
            r0 = r6
        L_0x0024:
            if (r1 == 0) goto L_0x0029
            r1.close()     // Catch:{ Exception -> 0x0046 }
        L_0x0029:
            java.lang.String r1 = r0.toLowerCase()
            java.lang.String r2 = "0x"
            boolean r1 = r1.startsWith(r2)
            if (r1 == 0) goto L_0x0045
            r1 = 16
            java.math.BigInteger r2 = new java.math.BigInteger     // Catch:{ Exception -> 0x006a }
            r3 = 2
            java.lang.String r3 = r0.substring(r3)     // Catch:{ Exception -> 0x006a }
            r2.<init>(r3, r1)     // Catch:{ Exception -> 0x006a }
            java.lang.String r0 = r2.toString()     // Catch:{ Exception -> 0x006a }
        L_0x0045:
            return r0
        L_0x0046:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0029
        L_0x004b:
            r0 = move-exception
            r1 = r7
        L_0x004d:
            r0.printStackTrace()     // Catch:{ all -> 0x006f }
            if (r1 == 0) goto L_0x0055
            r1.close()     // Catch:{ Exception -> 0x0057 }
        L_0x0055:
            r0 = r6
            goto L_0x0029
        L_0x0057:
            r0 = move-exception
            r0.printStackTrace()
            r0 = r6
            goto L_0x0029
        L_0x005d:
            r0 = move-exception
            r1 = r7
        L_0x005f:
            if (r1 == 0) goto L_0x0064
            r1.close()     // Catch:{ Exception -> 0x0065 }
        L_0x0064:
            throw r0
        L_0x0065:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0064
        L_0x006a:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0045
        L_0x006f:
            r0 = move-exception
            goto L_0x005f
        L_0x0071:
            r0 = move-exception
            goto L_0x004d
        L_0x0073:
            r0 = r6
            goto L_0x0024
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.bootloader.c.aI(android.content.Context):java.lang.String");
    }

    private static void a(Context context, Map map) {
        try {
            ExtendedAuthToken aM = Utils.aM(context);
            String aN = Utils.aN(context);
            ArrayList arrayList = new ArrayList();
            arrayList.add("serviceToken=" + aM.authToken + ";cUserId=" + aN);
            map.put("Cookie", arrayList);
        } catch (Utils.AccountExcepiton e) {
            throw e;
        }
    }
}
