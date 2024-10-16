package com.android.settings.bootloader;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import com.xiaomi.accountsdk.account.data.ExtendedAuthToken;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import miui.app.Activity;
import miui.cloud.common.XDeviceInfo;

/* compiled from: Utils */
class c {
    c() {
    }

    public static boolean azf(Context context) {
        return azi(context) != null;
    }

    public static String ayZ(Context context) {
        Account azi = azi(context);
        if (azi != null) {
            return azi.name;
        }
        return null;
    }

    public static Account azi(Context context) {
        Account[] accountsByType = AccountManager.get(context).getAccountsByType("com.xiaomi");
        if (accountsByType.length > 0) {
            return accountsByType[0];
        }
        return null;
    }

    public static void azg(Context context, AccountManagerCallback accountManagerCallback) {
        AccountManager accountManager = AccountManager.get(context);
        if (accountManagerCallback != null) {
            accountManager.addAccount("com.xiaomi", (String) null, (String[]) null, (Bundle) null, (Activity) context, accountManagerCallback, (Handler) null);
        }
    }

    public static String aza(Context context) {
        XDeviceInfo syncGet = XDeviceInfo.syncGet(context);
        if (syncGet != null) {
            return syncGet.deviceId;
        }
        return null;
    }

    public static boolean azh(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            return activeNetworkInfo.isConnected();
        }
        return false;
    }

    public static String azc(String str, Map map) {
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

    public static String azb(byte[] bArr) {
        char[] cArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bArr.length; i++) {
            sb.append(cArr[(bArr[i] >> 4) & 15]).append(cArr[bArr[i] & 15]);
        }
        return sb.toString();
    }

    public static ExtendedAuthToken azd(Context context) {
        Account azi = azi(context);
        if (azi == null) {
            throw new Utils$AccountExcepiton("not found xiaomi account");
        }
        try {
            String string = AccountManager.get(context).getAuthToken(azi, "micloudfind", true, (AccountManagerCallback) null, (Handler) null).getResult(30000, TimeUnit.MILLISECONDS).getString("authtoken");
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            return ExtendedAuthToken.parse(string);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String aze(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account azi = azi(context);
        if (azi != null) {
            return accountManager.getUserData(azi, "encrypted_user_id");
        }
        throw new Utils$AccountExcepiton("not found xiaomi account");
    }
}
