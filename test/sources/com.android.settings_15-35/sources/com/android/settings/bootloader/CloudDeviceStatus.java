package com.android.settings.bootloader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.android.settings.bootloader.Utils;
import com.miui.maml.util.ConfigFile;
import com.xiaomi.accountsdk.account.data.ExtendedAuthToken;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import miui.cloud.net.XHttpClient;
import miui.os.Build;
import miui.securityspace.XSpaceUtils;
import org.json.JSONObject;

abstract class CloudDeviceStatus {
    private static final String HOST;
    private static final Uri SECURITY_CENTER_CONTENT_URI;
    private static final Uri SECURITY_CENTER_GET_SERINUM_URI;
    private static int sHeartbeatCount = 0;
    private static Utils.RetType sResult = new Utils.RetType();

    static {
        String str;
        if (Build.IS_INTERNATIONAL_BUILD) {
            str = "https://unlock.update.intl.miui.com";
        } else {
            str = "https://unlock.update.miui.com";
        }
        HOST = str;
        Uri parse = Uri.parse("content://com.miui.securitycenter.provider");
        SECURITY_CENTER_CONTENT_URI = parse;
        SECURITY_CENTER_GET_SERINUM_URI = Uri.withAppendedPath(parse, "getserinum");
    }

    private static String getHMacSign(String str, String str2) {
        try {
            Mac instance = Mac.getInstance("HmacSHA1");
            instance.init(new SecretKeySpec("10f29ff413c89c8de02349cb3eb9a5f510f29ff413c89c8de02349cb3eb9a5f5".getBytes(), instance.getAlgorithm()));
            return Utils.binToHex(instance.doFinal(("POST\n" + str + "\n" + "data=" + str2 + "&sid=" + "miui_sec_android").getBytes())).toLowerCase();
        } catch (Exception unused) {
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x00c3  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00d0  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00d8  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x019b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.android.settings.bootloader.Utils.RetType bindAccountWithDevice(android.content.Context r10) {
        /*
            java.lang.String r0 = "CloudDeviceStatus"
            java.util.HashMap r1 = new java.util.HashMap
            r1.<init>()
            java.util.HashMap r2 = new java.util.HashMap
            r2.<init>()
            java.lang.String r3 = com.android.settings.bootloader.Utils.getAccountName(r10)
            if (r3 != 0) goto L_0x0018
            com.android.settings.bootloader.Utils$RetType r10 = sResult
            r0 = 1
            r10.retCode = r0
            return r10
        L_0x0018:
            boolean r4 = com.android.settings.bootloader.Utils.needSimCard()
            r5 = 2
            if (r4 == 0) goto L_0x004c
            boolean r4 = com.android.settings.bootloader.Utils.isNetworkConnected(r10)
            if (r4 != 0) goto L_0x002a
            com.android.settings.bootloader.Utils$RetType r10 = sResult
            r10.retCode = r5
            return r10
        L_0x002a:
            boolean r4 = com.android.settings.bootloader.Utils.isMobileConnected(r10)
            if (r4 != 0) goto L_0x0037
            com.android.settings.bootloader.Utils$RetType r10 = sResult
            r0 = 11
            r10.retCode = r0
            return r10
        L_0x0037:
            java.lang.String r4 = com.android.settings.bootloader.Utils.getImsi(r10)
            boolean r6 = android.text.TextUtils.isEmpty(r4)
            if (r6 == 0) goto L_0x0047
            com.android.settings.bootloader.Utils$RetType r10 = sResult
            r0 = 7
            r10.retCode = r0
            return r10
        L_0x0047:
            java.lang.String r6 = "imsi1"
            r1.put(r6, r4)
        L_0x004c:
            java.lang.String r4 = "userId"
            r1.put(r4, r3)
            java.lang.String r4 = "device"
            java.lang.String r6 = com.android.settings.bootloader.Utils.getModDevice()
            r1.put(r4, r6)
            java.lang.String r4 = android.os.Build.VERSION.INCREMENTAL
            java.lang.String r6 = "rom_version"
            r1.put(r6, r4)
            java.lang.String r6 = "heartbeat_mode"
            java.lang.String r7 = java.lang.String.valueOf(r5)
            r1.put(r6, r7)
            java.lang.String r6 = "cloudsp_devId"
            java.lang.String r7 = com.android.settings.bootloader.Utils.getDeviceId(r10)
            r1.put(r6, r7)
            java.lang.String r6 = "cloudsp_cpuId"
            java.lang.String r7 = getHardwardId(r10)
            r1.put(r6, r7)
            java.lang.String r6 = "cloudsp_product"
            java.lang.String r7 = miui.os.Build.DEVICE
            r1.put(r6, r7)
            java.lang.String r6 = "cloudsp_userId"
            r1.put(r6, r3)
            java.lang.String r3 = "cloudsp_romVersion"
            r1.put(r3, r4)
            r3 = 0
            com.xiaomi.security.devicecredential.SecurityDeviceCredentialAbility r4 = new com.xiaomi.security.devicecredential.SecurityDeviceCredentialAbility     // Catch:{ Exception -> 0x00b5, all -> 0x00b2 }
            r4.<init>(r10)     // Catch:{ Exception -> 0x00b5, all -> 0x00b2 }
            java.lang.String r3 = r4.getSecurityDeviceId()     // Catch:{ Exception -> 0x00b0 }
            java.lang.String r6 = "cloudsp_fid"
            r1.put(r6, r3)     // Catch:{ Exception -> 0x00b0 }
            java.lang.String r6 = "cloudsp_nonce"
            java.lang.String r3 = getNonce(r10, r3)     // Catch:{ Exception -> 0x00b0 }
            r1.put(r6, r3)     // Catch:{ Exception -> 0x00b0 }
            r4.release()
            r3 = 0
            goto L_0x00c8
        L_0x00ac:
            r10 = move-exception
            r3 = r4
            goto L_0x0199
        L_0x00b0:
            r3 = move-exception
            goto L_0x00b9
        L_0x00b2:
            r10 = move-exception
            goto L_0x0199
        L_0x00b5:
            r4 = move-exception
            r9 = r4
            r4 = r3
            r3 = r9
        L_0x00b9:
            r3.printStackTrace()     // Catch:{ all -> 0x00ac }
            java.lang.String r3 = "get paramter error: fid"
            android.util.Log.e(r0, r3)     // Catch:{ all -> 0x00ac }
            if (r4 == 0) goto L_0x00c6
            r4.release()
        L_0x00c6:
            r3 = 20090(0x4e7a, float:2.8152E-41)
        L_0x00c8:
            byte[] r4 = getSignData(r10, r1)
            java.lang.String r6 = "cloudp_sign"
            if (r4 != 0) goto L_0x00d8
            java.lang.String r3 = ""
            r1.put(r6, r3)
            r3 = 20091(0x4e7b, float:2.8153E-41)
            goto L_0x00e3
        L_0x00d8:
            java.lang.String r4 = com.android.settings.bootloader.Utils.binToHex(r4)
            java.lang.String r4 = r4.toLowerCase()
            r1.put(r6, r4)
        L_0x00e3:
            org.json.JSONObject r4 = new org.json.JSONObject
            r4.<init>(r1)
            java.lang.String r1 = "error_code"
            r4.put(r1, r3)     // Catch:{ Exception -> 0x00ed }
        L_0x00ed:
            java.util.HashMap r1 = new java.util.HashMap
            r1.<init>()
            java.lang.String r3 = "sid"
            java.lang.String r6 = "miui_sec_android"
            r1.put(r3, r6)
            java.lang.String r3 = "data"
            java.lang.String r6 = r4.toString()
            r1.put(r3, r6)
            java.lang.String r3 = r4.toString()
            java.lang.String r6 = "/v1/unlock/applyBind"
            java.lang.String r3 = getHMacSign(r6, r3)
            java.lang.String r7 = "sign"
            r1.put(r7, r3)
            getCookie(r10, r2)
            com.android.settings.bootloader.LogEncryptor r3 = new com.android.settings.bootloader.LogEncryptor     // Catch:{ Exception -> 0x0155 }
            r3.<init>()     // Catch:{ Exception -> 0x0155 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0155 }
            r7.<init>()     // Catch:{ Exception -> 0x0155 }
            java.lang.String r8 = "args: "
            r7.append(r8)     // Catch:{ Exception -> 0x0155 }
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x0155 }
            java.lang.String r4 = r3.wrapEncryptMsg(r0, r4)     // Catch:{ Exception -> 0x0155 }
            r7.append(r4)     // Catch:{ Exception -> 0x0155 }
            java.lang.String r4 = r7.toString()     // Catch:{ Exception -> 0x0155 }
            android.util.Log.i(r0, r4)     // Catch:{ Exception -> 0x0155 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0155 }
            r4.<init>()     // Catch:{ Exception -> 0x0155 }
            java.lang.String r7 = "headers: "
            r4.append(r7)     // Catch:{ Exception -> 0x0155 }
            java.lang.String r7 = r2.toString()     // Catch:{ Exception -> 0x0155 }
            java.lang.String r3 = r3.wrapEncryptMsg(r0, r7)     // Catch:{ Exception -> 0x0155 }
            r4.append(r3)     // Catch:{ Exception -> 0x0155 }
            java.lang.String r3 = r4.toString()     // Catch:{ Exception -> 0x0155 }
            android.util.Log.i(r0, r3)     // Catch:{ Exception -> 0x0155 }
            goto L_0x016e
        L_0x0155:
            r3 = move-exception
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r7 = "encrypt error:"
            r4.append(r7)
            java.lang.String r3 = r3.getMessage()
            r4.append(r3)
            java.lang.String r3 = r4.toString()
            android.util.Log.e(r0, r3)
        L_0x016e:
            miui.cloud.net.XHttpClient r3 = new miui.cloud.net.XHttpClient     // Catch:{ Exception -> 0x018c }
            r3.<init>()     // Catch:{ Exception -> 0x018c }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x018c }
            r4.<init>()     // Catch:{ Exception -> 0x018c }
            java.lang.String r7 = HOST     // Catch:{ Exception -> 0x018c }
            r4.append(r7)     // Catch:{ Exception -> 0x018c }
            r4.append(r6)     // Catch:{ Exception -> 0x018c }
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x018c }
            miui.cloud.net.XHttpClient$HttpResponse r1 = r3.syncPost((java.lang.String) r4, (java.util.Map<java.lang.String, java.util.List<java.lang.String>>) r2, (java.lang.Object) r1)     // Catch:{ Exception -> 0x018c }
            analysisResponse(r1, r10)     // Catch:{ Exception -> 0x018c }
            goto L_0x0196
        L_0x018c:
            com.android.settings.bootloader.Utils$RetType r10 = sResult
            r10.retCode = r5
            java.lang.String r10 = "post server error!"
            android.util.Log.d(r0, r10)
        L_0x0196:
            com.android.settings.bootloader.Utils$RetType r10 = sResult
            return r10
        L_0x0199:
            if (r3 == 0) goto L_0x019e
            r3.release()
        L_0x019e:
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.bootloader.CloudDeviceStatus.bindAccountWithDevice(android.content.Context):com.android.settings.bootloader.Utils$RetType");
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0015  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x001b  */
    /* JADX WARNING: Removed duplicated region for block: B:19:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void forceReload(android.content.Context r2) {
        /*
            r0 = 0
            com.xiaomi.security.devicecredential.SecurityDeviceCredentialAbility r1 = new com.xiaomi.security.devicecredential.SecurityDeviceCredentialAbility     // Catch:{ Exception -> 0x0019, all -> 0x0012 }
            r1.<init>(r2)     // Catch:{ Exception -> 0x0019, all -> 0x0012 }
            r1.forceReload()     // Catch:{ Exception -> 0x0010, all -> 0x000d }
            r1.release()
            goto L_0x001e
        L_0x000d:
            r2 = move-exception
            r0 = r1
            goto L_0x0013
        L_0x0010:
            r0 = r1
            goto L_0x0019
        L_0x0012:
            r2 = move-exception
        L_0x0013:
            if (r0 == 0) goto L_0x0018
            r0.release()
        L_0x0018:
            throw r2
        L_0x0019:
            if (r0 == 0) goto L_0x001e
            r0.release()
        L_0x001e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.bootloader.CloudDeviceStatus.forceReload(android.content.Context):void");
    }

    private static boolean analysisResponse(XHttpClient.HttpResponse httpResponse, Context context) {
        Log.d("CloudDeviceStatus", "stateCode: " + httpResponse.stateCode);
        Log.d("CloudDeviceStatus", "content: " + httpResponse.content);
        Utils.RetType retType = sResult;
        retType.retCode = 2;
        Object obj = httpResponse.content;
        if (obj == null) {
            if (httpResponse.error != null) {
                retType.retCode = 3;
                Log.e("CloudDeviceStatus", "error: " + httpResponse.error.toString());
            } else {
                retType.retCode = 2;
            }
            return false;
        }
        try {
            JSONObject jSONObject = (JSONObject) obj;
            String optString = jSONObject.optString("result");
            int optInt = jSONObject.optInt("code", 5);
            if (!"ok".equals(optString)) {
                if (optInt != 0) {
                    Utils.RetType retType2 = sResult;
                    retType2.retCode = 4;
                    retType2.retMsg = String.valueOf(optInt);
                    if (Utils.isChineseLocale()) {
                        sResult.retMsg = jSONObject.optString("descCN");
                    } else {
                        sResult.retMsg = jSONObject.optString("descEN");
                    }
                    if (optInt == 401) {
                        sResult.retCode = 401;
                        Utils.invalidateAuthToken(context);
                    } else if (optInt == 20086) {
                        forceReload(context);
                    }
                    return false;
                }
            }
            sResult.retCode = 0;
            sHeartbeatCount = jSONObject.optInt("count", 0);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getNonce(Context context, String str) {
        String accountName = Utils.getAccountName(context);
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        hashMap.put("cloudsp_devId", Utils.getDeviceId(context));
        hashMap.put("cloudsp_fid", str);
        if (accountName != null) {
            hashMap.put(XSpaceUtils.EXTRA_XSPACE_ACTUAL_USERID, accountName);
            hashMap.put("cloudsp_userId", accountName);
        }
        try {
            XHttpClient.HttpResponse syncGet = new XHttpClient().syncGet(Utils.encodeGetParamsToUrl(HOST + "/v1/micloud/nonce", hashMap), hashMap2);
            if (analysisResponse(syncGet, context)) {
                return ((JSONObject) syncGet.content).optJSONObject(ConfigFile.DATA).optString("nonce");
            }
            return "";
        } catch (Exception unused) {
            return "";
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0086  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x008c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static byte[] getSignData(android.content.Context r4, java.util.Map r5) {
        /*
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "POST&"
            r0.append(r1)
            java.lang.String r1 = "/mic/binding/v1/identified/device/account"
            r0.append(r1)
            java.util.TreeMap r1 = new java.util.TreeMap
            r1.<init>(r5)
            java.util.Set r5 = r1.entrySet()
            java.util.Iterator r5 = r5.iterator()
        L_0x001c:
            boolean r1 = r5.hasNext()
            if (r1 == 0) goto L_0x005f
            java.lang.Object r1 = r5.next()
            java.util.Map$Entry r1 = (java.util.Map.Entry) r1
            java.lang.Object r2 = r1.getKey()
            java.lang.String r2 = (java.lang.String) r2
            java.lang.String r3 = "cloudsp_"
            boolean r2 = r2.startsWith(r3)
            if (r2 == 0) goto L_0x001c
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "&"
            r2.append(r3)
            java.lang.Object r3 = r1.getKey()
            java.lang.String r3 = (java.lang.String) r3
            r2.append(r3)
            java.lang.String r3 = "="
            r2.append(r3)
            java.lang.Object r1 = r1.getValue()
            java.lang.String r1 = (java.lang.String) r1
            r2.append(r1)
            java.lang.String r1 = r2.toString()
            r0.append(r1)
            goto L_0x001c
        L_0x005f:
            r5 = 0
            com.xiaomi.security.devicecredential.SecurityDeviceCredentialAbility r1 = new com.xiaomi.security.devicecredential.SecurityDeviceCredentialAbility     // Catch:{ Exception -> 0x007f, all -> 0x007d }
            r1.<init>(r4)     // Catch:{ Exception -> 0x007f, all -> 0x007d }
            java.lang.String r4 = r0.toString()     // Catch:{ Exception -> 0x007b }
            java.lang.String r0 = "UTF-8"
            byte[] r4 = r4.getBytes(r0)     // Catch:{ Exception -> 0x007b }
            r0 = 1
            byte[] r4 = r1.signWithDeviceCredential(r4, r0)     // Catch:{ Exception -> 0x007b }
            r1.release()
            return r4
        L_0x0078:
            r4 = move-exception
            r5 = r1
            goto L_0x008a
        L_0x007b:
            r4 = move-exception
            goto L_0x0081
        L_0x007d:
            r4 = move-exception
            goto L_0x008a
        L_0x007f:
            r4 = move-exception
            r1 = r5
        L_0x0081:
            r4.printStackTrace()     // Catch:{ all -> 0x0078 }
            if (r1 == 0) goto L_0x0089
            r1.release()
        L_0x0089:
            return r5
        L_0x008a:
            if (r5 == 0) goto L_0x008f
            r5.release()
        L_0x008f:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.bootloader.CloudDeviceStatus.getSignData(android.content.Context, java.util.Map):byte[]");
    }

    private static String getHardwardId(Context context) {
        String hardwareIdFromLocal = Utils.getHardwareIdFromLocal();
        if (TextUtils.isEmpty(hardwareIdFromLocal)) {
            Cursor cursor = null;
            try {
                Cursor query = context.getContentResolver().query(SECURITY_CENTER_GET_SERINUM_URI, (String[]) null, (String) null, (String[]) null, (String) null);
                if (query != null && query.moveToFirst()) {
                    hardwareIdFromLocal = query.getString(query.getColumnIndex("seriNum"));
                }
                if (query != null) {
                    try {
                        query.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Throwable th) {
                if (cursor != null) {
                    try {
                        cursor.close();
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                }
                throw th;
            }
        }
        if (!hardwareIdFromLocal.toLowerCase().startsWith("0x")) {
            return hardwareIdFromLocal;
        }
        try {
            return new BigInteger(hardwareIdFromLocal.substring(2), 16).toString();
        } catch (Exception e4) {
            e4.printStackTrace();
            return hardwareIdFromLocal;
        }
    }

    private static void getCookie(Context context, Map map) {
        ExtendedAuthToken authToken = Utils.getAuthToken(context);
        String encryptedAccountName = Utils.getEncryptedAccountName(context);
        ArrayList arrayList = new ArrayList();
        arrayList.add("serviceToken=" + authToken.authToken + ";cUserId=" + encryptedAccountName);
        map.put("Cookie", arrayList);
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x0073  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0079  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static byte[] getsignDataForHeartbeat(android.content.Context r6, java.util.Map r7) {
        /*
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.util.TreeMap r1 = new java.util.TreeMap
            r1.<init>(r7)
            java.util.Set r7 = r1.entrySet()
            java.util.Iterator r7 = r7.iterator()
            r1 = 1
            r2 = r1
        L_0x0014:
            boolean r3 = r7.hasNext()
            if (r3 == 0) goto L_0x004d
            java.lang.Object r3 = r7.next()
            java.util.Map$Entry r3 = (java.util.Map.Entry) r3
            if (r2 == 0) goto L_0x0024
            r2 = 0
            goto L_0x0029
        L_0x0024:
            java.lang.String r4 = "&"
            r0.append(r4)
        L_0x0029:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.Object r5 = r3.getKey()
            java.lang.String r5 = (java.lang.String) r5
            r4.append(r5)
            java.lang.String r5 = "="
            r4.append(r5)
            java.lang.Object r3 = r3.getValue()
            java.lang.String r3 = (java.lang.String) r3
            r4.append(r3)
            java.lang.String r3 = r4.toString()
            r0.append(r3)
            goto L_0x0014
        L_0x004d:
            r7 = 0
            com.xiaomi.security.devicecredential.SecurityDeviceCredentialAbility r2 = new com.xiaomi.security.devicecredential.SecurityDeviceCredentialAbility     // Catch:{ Exception -> 0x006c, all -> 0x006a }
            r2.<init>(r6)     // Catch:{ Exception -> 0x006c, all -> 0x006a }
            java.lang.String r6 = r0.toString()     // Catch:{ Exception -> 0x0068 }
            java.lang.String r0 = "UTF-8"
            byte[] r6 = r6.getBytes(r0)     // Catch:{ Exception -> 0x0068 }
            byte[] r6 = r2.signWithDeviceCredential(r6, r1)     // Catch:{ Exception -> 0x0068 }
            r2.release()
            return r6
        L_0x0065:
            r6 = move-exception
            r7 = r2
            goto L_0x0077
        L_0x0068:
            r6 = move-exception
            goto L_0x006e
        L_0x006a:
            r6 = move-exception
            goto L_0x0077
        L_0x006c:
            r6 = move-exception
            r2 = r7
        L_0x006e:
            r6.printStackTrace()     // Catch:{ all -> 0x0065 }
            if (r2 == 0) goto L_0x0076
            r2.release()
        L_0x0076:
            return r7
        L_0x0077:
            if (r7 == 0) goto L_0x007c
            r7.release()
        L_0x007c:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.bootloader.CloudDeviceStatus.getsignDataForHeartbeat(android.content.Context, java.util.Map):byte[]");
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0017  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x001d  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0027 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x002a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int sendHeartbeat(android.content.Context r7) {
        /*
            r0 = 0
            com.xiaomi.security.devicecredential.SecurityDeviceCredentialAbility r1 = new com.xiaomi.security.devicecredential.SecurityDeviceCredentialAbility     // Catch:{ Exception -> 0x0013, all -> 0x0011 }
            r1.<init>(r7)     // Catch:{ Exception -> 0x0013, all -> 0x0011 }
            java.lang.String r2 = r1.getSecurityDeviceId()     // Catch:{ Exception -> 0x001b, all -> 0x000e }
            r1.release()
            goto L_0x0021
        L_0x000e:
            r7 = move-exception
            r0 = r1
            goto L_0x0015
        L_0x0011:
            r7 = move-exception
            goto L_0x0015
        L_0x0013:
            r1 = r0
            goto L_0x001b
        L_0x0015:
            if (r0 == 0) goto L_0x001a
            r0.release()
        L_0x001a:
            throw r7
        L_0x001b:
            if (r1 == 0) goto L_0x0020
            r1.release()
        L_0x0020:
            r2 = r0
        L_0x0021:
            boolean r1 = android.text.TextUtils.isEmpty(r2)
            if (r1 == 0) goto L_0x002a
            r7 = -20090(0xffffffffffffb186, float:NaN)
            return r7
        L_0x002a:
            java.lang.String r1 = com.android.settings.bootloader.Utils.getAccountName(r7)
            r3 = -1
            if (r1 != 0) goto L_0x0032
            return r3
        L_0x0032:
            java.util.HashMap r4 = new java.util.HashMap
            r4.<init>()
            java.lang.String r5 = "cpuId"
            java.lang.String r6 = getHardwardId(r7)
            r4.put(r5, r6)
            java.lang.String r5 = "fid"
            r4.put(r5, r2)
            java.lang.String r2 = "product"
            java.lang.String r5 = miui.os.Build.DEVICE
            r4.put(r2, r5)
            java.lang.String r2 = "uid"
            r4.put(r2, r1)
            byte[] r1 = getsignDataForHeartbeat(r7, r4)
            if (r1 != 0) goto L_0x005c
            r7 = -20091(0xffffffffffffb185, float:NaN)
            return r7
        L_0x005c:
            java.lang.String r1 = com.android.settings.bootloader.Utils.binToHex(r1)
            java.lang.String r1 = r1.toLowerCase()
            java.lang.String r2 = "tzSign"
            r4.put(r2, r1)
            org.json.JSONObject r1 = new org.json.JSONObject
            r1.<init>(r4)
            java.util.HashMap r2 = new java.util.HashMap
            r2.<init>()
            java.lang.String r4 = "sid"
            java.lang.String r5 = "miui_sec_android"
            r2.put(r4, r5)
            java.lang.String r4 = "data"
            java.lang.String r5 = r1.toString()
            r2.put(r4, r5)
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "data: "
            r4.append(r5)
            java.lang.String r5 = r1.toString()
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            java.lang.String r5 = "CloudDeviceStatus"
            android.util.Log.d(r5, r4)
            java.lang.String r1 = r1.toString()
            java.lang.String r4 = "/v1/unlock/deviceHeartbeat"
            java.lang.String r1 = getHMacSign(r4, r1)
            java.lang.String r5 = "sign"
            r2.put(r5, r1)
            miui.cloud.net.XHttpClient r1 = new miui.cloud.net.XHttpClient     // Catch:{ Exception -> 0x00d4 }
            r1.<init>()     // Catch:{ Exception -> 0x00d4 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00d4 }
            r5.<init>()     // Catch:{ Exception -> 0x00d4 }
            java.lang.String r6 = HOST     // Catch:{ Exception -> 0x00d4 }
            r5.append(r6)     // Catch:{ Exception -> 0x00d4 }
            r5.append(r4)     // Catch:{ Exception -> 0x00d4 }
            java.lang.String r4 = r5.toString()     // Catch:{ Exception -> 0x00d4 }
            miui.cloud.net.XHttpClient$HttpResponse r0 = r1.syncPost((java.lang.String) r4, (java.util.Map<java.lang.String, java.util.List<java.lang.String>>) r0, (java.lang.Object) r2)     // Catch:{ Exception -> 0x00d4 }
            boolean r7 = analysisResponse(r0, r7)     // Catch:{ Exception -> 0x00d4 }
            if (r7 == 0) goto L_0x00d3
            int r7 = sHeartbeatCount     // Catch:{ Exception -> 0x00d4 }
            return r7
        L_0x00d3:
            return r3
        L_0x00d4:
            r7 = -2
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.bootloader.CloudDeviceStatus.sendHeartbeat(android.content.Context):int");
    }
}
