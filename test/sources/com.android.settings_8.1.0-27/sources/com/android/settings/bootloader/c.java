package com.android.settings.bootloader;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.miui.activityutil.b;
import com.miui.activityutil.n;
import com.xiaomi.accountsdk.account.data.a;
import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import miui.app.Activity;
import miui.cloud.common.f;
import miui.os.SystemProperties;
import miui.telephony.SubscriptionManager;
import miui.util.FeatureParser;
import miui.util.IOUtils;

/* compiled from: Utils */
class c {
    c() {
    }

    public static boolean bwe(Context context) {
        return bwr(context) != null;
    }

    public static String bwb(Context context) {
        Account bwr = bwr(context);
        if (bwr != null) {
            return bwr.name;
        }
        return null;
    }

    public static Account bwr(Context context) {
        Account[] accountsByType = AccountManager.get(context).getAccountsByType("com.xiaomi");
        if (accountsByType.length > 0) {
            return accountsByType[0];
        }
        return null;
    }

    public static void bwf(Context context, AccountManagerCallback<Bundle> accountManagerCallback) {
        AccountManager accountManager = AccountManager.get(context);
        if (accountManagerCallback != null) {
            accountManager.addAccount("com.xiaomi", (String) null, (String[]) null, (Bundle) null, (Activity) context, accountManagerCallback, (Handler) null);
        }
    }

    public static String bwl(Context context) {
        f feq = f.feq(context);
        if (feq != null) {
            return feq.deviceId;
        }
        return null;
    }

    public static boolean bwg(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            return activeNetworkInfo.isConnected();
        }
        return false;
    }

    public static boolean bwh(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.getType() == 0;
    }

    public static String bwn(String str, Map<String, String> map) {
        ArrayList arrayList = new ArrayList();
        for (Map.Entry entry : map.entrySet()) {
            try {
                arrayList.add(URLEncoder.encode((String) entry.getKey(), "utf-8") + "=" + URLEncoder.encode((String) entry.getValue(), "utf-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return str + "?" + TextUtils.join("&", arrayList.toArray(new String[0]));
    }

    public static String bwi(byte[] bArr) {
        char[] cArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bArr.length; i++) {
            sb.append(cArr[(bArr[i] >> 4) & 15]).append(cArr[bArr[i] & 15]);
        }
        return sb.toString();
    }

    public static void bwm(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        if (accountManager != null) {
            accountManager.invalidateAuthToken("com.xiaomi", "micloudfind");
        }
    }

    public static a bwp(Context context) {
        Account bwr = bwr(context);
        if (bwr == null) {
            throw new Utils$AccountExcepiton("not found xiaomi account");
        }
        try {
            String string = AccountManager.get(context).getAuthToken(bwr, "micloudfind", true, (AccountManagerCallback) null, (Handler) null).getResult(30000, TimeUnit.MILLISECONDS).getString("authtoken");
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            return a.cXn(string);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String bwq(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account bwr = bwr(context);
        if (bwr != null) {
            return accountManager.getUserData(bwr, "encrypted_user_id");
        }
        throw new Utils$AccountExcepiton("not found xiaomi account");
    }

    public static String bwo() {
        BufferedReader bufferedReader;
        String str = SystemProperties.get("ro.boot.cpuid", b.d);
        if (TextUtils.isEmpty(str)) {
            BufferedReader bufferedReader2 = null;
            try {
                bufferedReader = new BufferedReader(new FileReader("/proc/serial_num"), 256);
            } catch (Exception e) {
                bufferedReader = null;
                IOUtils.closeQuietly(bufferedReader);
                return str;
            } catch (Throwable th) {
                th = th;
                IOUtils.closeQuietly(bufferedReader2);
                throw th;
            }
            try {
                String readLine = bufferedReader.readLine();
                IOUtils.closeQuietly(bufferedReader);
                return readLine;
            } catch (Exception e2) {
                IOUtils.closeQuietly(bufferedReader);
                return str;
            } catch (Throwable th2) {
                Throwable th3 = th2;
                bufferedReader2 = bufferedReader;
                th = th3;
                IOUtils.closeQuietly(bufferedReader2);
                throw th;
            }
        }
        return str;
    }

    public static String bwk() {
        String str = SystemProperties.get("ro.product.mod_device", b.d);
        if (TextUtils.isEmpty(str)) {
            return Build.DEVICE;
        }
        return str;
    }

    private static String bws(String str, String str2) {
        try {
            MessageDigest instance = MessageDigest.getInstance(str2);
            instance.update(str.getBytes());
            byte[] digest = instance.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                String hexString = Integer.toHexString(b & 255);
                if (hexString.length() == 1) {
                    sb.append(n.f155a);
                }
                sb.append(hexString);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int bwd(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        int phoneCount = telephonyManager.getPhoneCount();
        for (int i = 0; i < phoneCount; i++) {
            if (telephonyManager.getSimState(i) == 5) {
                return 3;
            }
        }
        return 1;
    }

    public static String bwj(Context context) {
        int defaultDataSlotId = SubscriptionManager.getDefault().getDefaultDataSlotId();
        if (defaultDataSlotId < 0 || defaultDataSlotId > 1) {
            return null;
        }
        String subscriberId = ((TelephonyManager) context.getSystemService("phone")).getSubscriberId(SubscriptionManager.getDefault().getSubscriptionIdForSlot(defaultDataSlotId));
        if (TextUtils.isEmpty(subscriberId)) {
            return null;
        }
        return bws(subscriberId + "2jkkewm2OPMBEz7yhl1nZ995OMjOKr6q7gm1Dl0T3EwxmycEIcwr8W3tQIwPLqhm", "SHA-256");
    }

    public static boolean bwc() {
        if (!FeatureParser.getBoolean("is_pad", false) || !SystemProperties.getBoolean("ro.radio.noril", false)) {
            return true;
        }
        return false;
    }
}
