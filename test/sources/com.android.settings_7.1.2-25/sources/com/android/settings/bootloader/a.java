package com.android.settings.bootloader;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.android.settings.search.FunctionColumns;
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
class a {
    private static final String aLR;
    private static final Uri aLS = Uri.parse("content://com.miui.securitycenter.provider");
    private static final Uri aLT = Uri.withAppendedPath(aLS, "getserinum");
    private static d aLU = new d();

    a() {
    }

    static {
        String str;
        if (Build.IS_INTERNATIONAL_BUILD) {
            str = "https://us.deviceapi.micloud.xiaomi.net";
        } else {
            str = "https://deviceapi.micloud.xiaomi.net";
        }
        aLR = str;
    }

    public static d ayM(Context context) {
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        try {
            hashMap.put("cloudsp_fid", SecurityDeviceCredentialManager.getSecurityDeviceId());
            String ayP = ayP(context);
            if (ayP == null) {
                return aLU;
            }
            String ayZ = c.ayZ(context);
            if (ayZ == null) {
                aLU.aLZ = 1;
                return aLU;
            }
            hashMap.put("cloudsp_nonce", ayP);
            hashMap.put("cloudsp_devId", c.aza(context));
            hashMap.put("cloudsp_cpuId", ayO(context));
            hashMap.put("cloudsp_product", Build.DEVICE);
            hashMap.put("userId", ayZ);
            hashMap.put("cloudsp_userId", ayZ);
            byte[] ayQ = ayQ(hashMap);
            if (ayQ == null) {
                throw new Utils$ParameterException("get paramter error: sign");
            }
            hashMap.put("cloudp_sign", c.azb(ayQ).toLowerCase());
            String str = aLR + "/mic/binding/v1/identified/device/account";
            ayN(context, hashMap2);
            Log.d("CloudDeviceStatus", "args: " + hashMap);
            Log.d("CloudDeviceStatus", "headers: " + hashMap2);
            try {
                if (ayL(new XHttpClient().syncPost(str, hashMap2, hashMap))) {
                    aLU.aLZ = 0;
                    return aLU;
                }
            } catch (Exception e) {
                aLU.aLZ = 2;
                Log.d("CloudDeviceStatus", "post server error!");
            }
            return aLU;
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new Utils$ParameterException("get paramter error: fid");
        }
    }

    private static boolean ayL(XHttpClient.HttpResponse httpResponse) {
        Log.d("CloudDeviceStatus", "stateCode: " + httpResponse.stateCode);
        Log.d("CloudDeviceStatus", "content: " + httpResponse.content);
        aLU.aLZ = 2;
        if (httpResponse.stateCode != 200) {
            return false;
        }
        if (httpResponse.content == null) {
            if (httpResponse.error != null) {
                aLU.aLZ = 3;
                Log.d("CloudDeviceStatus", "eror: " + httpResponse.error.toString());
            } else {
                aLU.aLZ = 2;
            }
            return false;
        }
        try {
            JSONObject jSONObject = (JSONObject) httpResponse.content;
            if ("ok".equals(jSONObject.getString("result"))) {
                return true;
            }
            aLU.aLZ = 4;
            aLU.aMa = jSONObject.getInt("code") + ": " + jSONObject.getString(FunctionColumns.DESCRIPTION);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String ayP(Context context) {
        String ayZ = c.ayZ(context);
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        hashMap.put("cloudsp_devId", c.aza(context));
        if (ayZ != null) {
            hashMap.put("userId", ayZ);
            hashMap.put("cloudsp_userId", ayZ);
        }
        try {
            hashMap.put("cloudsp_fid", SecurityDeviceCredentialManager.getSecurityDeviceId());
            try {
                XHttpClient.HttpResponse syncGet = new XHttpClient().syncGet(c.azc(aLR + "/mic/find/v4/anonymous/challenge", hashMap), hashMap2);
                if (ayL(syncGet)) {
                    return ((JSONObject) syncGet.content).getJSONObject("data").getString("nonce");
                }
                return null;
            } catch (Exception e) {
                aLU.aLZ = 2;
                Log.d("CloudDeviceStatus", "getNonce error!");
                return null;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new Utils$ParameterException("get paramter error: fid");
        }
    }

    private static byte[] ayQ(Map map) {
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
            Log.d("CloudDeviceStatus", "getSignData error!");
            e.printStackTrace();
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0038  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0065 A[SYNTHETIC, Splitter:B:33:0x0065] */
    /* JADX WARNING: Removed duplicated region for block: B:43:? A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String ayO(android.content.Context r8) {
        /*
            r6 = 0
            java.lang.String r7 = ""
            android.content.ContentResolver r0 = r8.getContentResolver()     // Catch:{ Exception -> 0x0050, all -> 0x0061 }
            android.net.Uri r1 = aLT     // Catch:{ Exception -> 0x0050, all -> 0x0061 }
            r2 = 0
            r3 = 0
            r4 = 0
            r5 = 0
            android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5)     // Catch:{ Exception -> 0x0050, all -> 0x0061 }
            if (r1 == 0) goto L_0x0049
            boolean r0 = r1.moveToFirst()     // Catch:{ Exception -> 0x0075 }
            if (r0 == 0) goto L_0x0077
            java.lang.String r0 = "seriNum"
            int r0 = r1.getColumnIndex(r0)     // Catch:{ Exception -> 0x0075 }
            java.lang.String r7 = r1.getString(r0)     // Catch:{ Exception -> 0x0075 }
            r0 = r7
        L_0x0026:
            if (r1 == 0) goto L_0x002b
            r1.close()     // Catch:{ Exception -> 0x004b }
        L_0x002b:
            java.lang.String r1 = r0.toLowerCase()
            java.lang.String r2 = "0x"
            boolean r1 = r1.startsWith(r2)
            if (r1 == 0) goto L_0x0048
            r1 = 16
            java.math.BigInteger r2 = new java.math.BigInteger     // Catch:{ Exception -> 0x006e }
            r3 = 2
            java.lang.String r3 = r0.substring(r3)     // Catch:{ Exception -> 0x006e }
            r2.<init>(r3, r1)     // Catch:{ Exception -> 0x006e }
            java.lang.String r0 = r2.toString()     // Catch:{ Exception -> 0x006e }
        L_0x0048:
            return r0
        L_0x0049:
            r0 = r7
            goto L_0x0026
        L_0x004b:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x002b
        L_0x0050:
            r0 = move-exception
            r1 = r6
        L_0x0052:
            r0.printStackTrace()     // Catch:{ all -> 0x0073 }
            if (r1 == 0) goto L_0x005a
            r1.close()     // Catch:{ Exception -> 0x005c }
        L_0x005a:
            r0 = r7
            goto L_0x002b
        L_0x005c:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x005a
        L_0x0061:
            r0 = move-exception
            r1 = r6
        L_0x0063:
            if (r1 == 0) goto L_0x0068
            r1.close()     // Catch:{ Exception -> 0x0069 }
        L_0x0068:
            throw r0
        L_0x0069:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0068
        L_0x006e:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0048
        L_0x0073:
            r0 = move-exception
            goto L_0x0063
        L_0x0075:
            r0 = move-exception
            goto L_0x0052
        L_0x0077:
            r0 = r7
            goto L_0x0026
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.bootloader.a.ayO(android.content.Context):java.lang.String");
    }

    private static void ayN(Context context, Map map) {
        try {
            ExtendedAuthToken azd = c.azd(context);
            String aze = c.aze(context);
            ArrayList arrayList = new ArrayList();
            arrayList.add("serviceToken=" + azd.authToken + ";cUserId=" + aze);
            map.put("Cookie", arrayList);
        } catch (Utils$AccountExcepiton e) {
            throw e;
        }
    }
}
