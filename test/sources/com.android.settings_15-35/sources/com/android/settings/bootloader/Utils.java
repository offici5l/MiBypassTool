package com.android.settings.bootloader;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemProperties;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.xiaomi.accountsdk.account.data.ExtendedAuthToken;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import miui.accounts.ExtraAccountManager;
import miui.cloud.Constants;
import miui.cloud.common.XDeviceInfo;
import miui.cloud.sync.providers.PersonalAssistantSyncInfoProvider;
import miui.telephony.SubscriptionManager;
import miui.util.FeatureParser;
import miuix.core.util.IOUtils;

abstract class Utils {

    public static class RetType {
        public int retCode = 3;
        public String retMsg = null;
    }

    public static class AccountExcepiton extends Exception {
        public AccountExcepiton(String str) {
            super(str);
        }
    }

    public static boolean isAccountLogined(Context context) {
        return getAccount(context) != null;
    }

    public static String getAccountName(Context context) {
        Account account = getAccount(context);
        if (account != null) {
            return account.name;
        }
        return null;
    }

    public static Account getAccount(Context context) {
        Account[] accountsByType = AccountManager.get(context).getAccountsByType(Constants.XIAOMI_ACCOUNT_TYPE);
        if (accountsByType.length > 0) {
            return accountsByType[0];
        }
        return null;
    }

    public static void addAccount(Context context, AccountManagerCallback accountManagerCallback) {
        AccountManager accountManager = AccountManager.get(context);
        if (accountManagerCallback != null) {
            accountManager.addAccount(Constants.XIAOMI_ACCOUNT_TYPE, (String) null, (String[]) null, (Bundle) null, (Activity) context, accountManagerCallback, (Handler) null);
        }
    }

    public static String getDeviceId(Context context) {
        XDeviceInfo syncGet = XDeviceInfo.syncGet(context);
        if (syncGet != null) {
            return syncGet.deviceId;
        }
        return null;
    }

    public static boolean isNetworkConnected(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isMobileConnected(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.getType() == 0;
    }

    public static String encodeGetParamsToUrl(String str, Map map) {
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

    public static String binToHex(byte[] bArr) {
        char[] cArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        StringBuilder sb = new StringBuilder();
        for (byte b : bArr) {
            sb.append(cArr[(b >> 4) & 15]);
            sb.append(cArr[b & 15]);
        }
        return sb.toString();
    }

    public static void invalidateAuthToken(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        if (accountManager != null) {
            accountManager.invalidateAuthToken(Constants.XIAOMI_ACCOUNT_TYPE, "micloudfind");
        }
    }

    public static ExtendedAuthToken getAuthToken(Context context) {
        Account account = getAccount(context);
        if (account != null) {
            try {
                String string = AccountManager.get(context).getAuthToken(account, "micloudfind", true, (AccountManagerCallback) null, (Handler) null).getResult(30000, TimeUnit.MILLISECONDS).getString("authtoken");
                if (TextUtils.isEmpty(string)) {
                    return null;
                }
                return ExtendedAuthToken.parse(string);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            throw new AccountExcepiton("not found xiaomi account");
        }
    }

    public static String getEncryptedAccountName(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getAccount(context);
        if (account != null) {
            return accountManager.getUserData(account, ExtraAccountManager.KEY_ENCRYPTED_USER_ID);
        }
        throw new AccountExcepiton("not found xiaomi account");
    }

    public static String getHardwareIdFromLocal() {
        String str = SystemProperties.get("ro.boot.cpuid", "");
        if (TextUtils.isEmpty(str)) {
            BufferedReader bufferedReader = null;
            try {
                BufferedReader bufferedReader2 = new BufferedReader(new FileReader("/proc/serial_num"), 256);
                try {
                    str = bufferedReader2.readLine();
                    IOUtils.closeQuietly((Reader) bufferedReader2);
                } catch (Exception unused) {
                    bufferedReader = bufferedReader2;
                    IOUtils.closeQuietly((Reader) bufferedReader);
                    return str;
                } catch (Throwable th) {
                    th = th;
                    bufferedReader = bufferedReader2;
                    IOUtils.closeQuietly((Reader) bufferedReader);
                    throw th;
                }
            } catch (Exception unused2) {
                IOUtils.closeQuietly((Reader) bufferedReader);
                return str;
            } catch (Throwable th2) {
                th = th2;
                IOUtils.closeQuietly((Reader) bufferedReader);
                throw th;
            }
        }
        return str;
    }

    public static String getModDevice() {
        String str = SystemProperties.get("ro.product.mod_device", "");
        return TextUtils.isEmpty(str) ? Build.DEVICE : str;
    }

    private static String getHash(String str, String str2) {
        try {
            MessageDigest instance = MessageDigest.getInstance(str2);
            instance.update(str.getBytes());
            byte[] digest = instance.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                String hexString = Integer.toHexString(b & TransitionInfo.INIT);
                if (hexString.length() == 1) {
                    sb.append(PersonalAssistantSyncInfoProvider.RECORD_SYNCED);
                }
                sb.append(hexString);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getSimState(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        int phoneCount = telephonyManager.getPhoneCount();
        for (int i = 0; i < phoneCount; i++) {
            if (telephonyManager.getSimState(i) == 5) {
                return 3;
            }
        }
        return 1;
    }

    public static String getImsi(Context context) {
        int defaultDataSlotId = SubscriptionManager.getDefault().getDefaultDataSlotId();
        if (defaultDataSlotId < 0 || defaultDataSlotId > 1) {
            return null;
        }
        String subscriberId = ((TelephonyManager) context.getSystemService("phone")).getSubscriberId(SubscriptionManager.getDefault().getSubscriptionIdForSlot(defaultDataSlotId));
        if (TextUtils.isEmpty(subscriberId)) {
            return null;
        }
        return getHash(subscriberId + "2jkkewm2OPMBEz7yhl1nZ995OMjOKr6q7gm1Dl0T3EwxmycEIcwr8W3tQIwPLqhm", "SHA-256");
    }

    public static boolean needSimCard() {
        if (!FeatureParser.getBoolean("is_pad", false) || !SystemProperties.getBoolean("ro.radio.noril", false)) {
            return true;
        }
        return false;
    }

    public static boolean isChineseLocale() {
        return Locale.CHINESE.getLanguage().equals(Locale.getDefault().getLanguage());
    }
}
