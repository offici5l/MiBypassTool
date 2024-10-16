package com.android.settings.bootloader;

import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemProperties;
import android.provider.MiuiSettings;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import com.android.settings.R;
import com.android.settings.search.provider.SettingsProvider;
import miui.app.Activity;
import miui.os.Build;

public class BootloaderStatusActivity extends Activity implements AccountManagerCallback {
    /* access modifiers changed from: private */
    public static final String TAG = BootloaderStatusActivity.class.getSimpleName();
    private boolean aLV = true;
    /* access modifiers changed from: private */
    public Button aLW;
    private Toast aLX = null;

    /* JADX WARNING: type inference failed for: r3v0, types: [android.content.Context, com.android.settings.bootloader.BootloaderStatusActivity, miui.app.Activity] */
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        BootloaderStatusActivity.super.onCreate(bundle);
        String str = SystemProperties.get("ro.secureboot.lockstate", (String) null);
        if ("unlocked".equals(str) || str == null) {
            setContentView(R.layout.bootloader_status_unlocked);
            return;
        }
        setContentView(R.layout.bootloader_status_locked);
        this.aLW = (Button) findViewById(R.id.button);
        this.aLW.setOnClickListener(new e(this));
        if (!ayT(this)) {
            Intent intent = new Intent("miui.intent.action.PRIVACY_AUTHORIZATION_DIALOG");
            intent.putExtra(SettingsProvider.ARGS_KEY, getPackageName());
            startActivityForResult(intent, 1);
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 1 && i2 != -1) {
            finish();
        }
        BootloaderStatusActivity.super.onActivityResult(i, i2, intent);
    }

    private boolean ayT(Context context) {
        if (!Build.IS_INTERNATIONAL_BUILD) {
            return true;
        }
        try {
            return MiuiSettings.Privacy.isEnabled(context, context.getPackageName());
        } catch (Exception e) {
            Log.e(TAG, "get privacy status error: " + e.getMessage());
            return false;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: android.content.Context[]} */
    /* access modifiers changed from: private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void ayR() {
        /*
            r5 = this;
            r4 = 1
            r3 = 0
            r2 = 0
            boolean r0 = com.android.settings.bootloader.c.azf(r5)
            if (r0 != 0) goto L_0x0017
            boolean r0 = r5.aLV
            if (r0 == 0) goto L_0x0013
            r5.aLV = r2
            com.android.settings.bootloader.c.azg(r5, r5)
            return
        L_0x0013:
            r5.ayS(r4, r3)
            return
        L_0x0017:
            boolean r0 = com.android.settings.bootloader.c.azh(r5)
            if (r0 != 0) goto L_0x0022
            r0 = 2
            r5.ayS(r0, r3)
            return
        L_0x0022:
            android.widget.Button r0 = r5.aLW
            r0.setEnabled(r2)
            android.widget.Button r0 = r5.aLW
            r1 = 863010960(0x33708090, float:5.599628E-8)
            r0.setTextColor(r1)
            r0 = 6
            r5.ayS(r0, r3)
            com.android.settings.bootloader.b r0 = new com.android.settings.bootloader.b
            r0.<init>(r5)
            android.content.Context[] r1 = new android.content.Context[r4]
            r1[r2] = r5
            r0.execute(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.bootloader.BootloaderStatusActivity.ayR():void");
    }

    /* access modifiers changed from: private */
    public void ayS(int i, String str) {
        int i2 = R.string.bootloader_device_bind_fail;
        if (!this.aLV) {
            this.aLV = true;
        }
        if (this.aLX != null) {
            this.aLX.cancel();
        }
        if (i == 1) {
            i2 = R.string.bootloader_device_bind_no_account;
        } else if (i == 2) {
            i2 = R.string.bootloader_device_bind_no_network;
        } else if (!(i == 3 || i == 5)) {
            if (i == 6) {
                i2 = R.string.bootloader_device_bind_msg;
            } else if (i == 4) {
                this.aLX = Toast.makeText(getApplicationContext(), str, 1);
                this.aLX.show();
                return;
            } else {
                i2 = R.string.bootloader_device_bind_already;
            }
        }
        this.aLX = Toast.makeText(getApplicationContext(), i2, 0);
        this.aLX.show();
    }

    public void run(AccountManagerFuture accountManagerFuture) {
        boolean z;
        try {
            Bundle bundle = (Bundle) accountManagerFuture.getResult();
            if (bundle != null) {
                z = bundle.getBoolean("booleanResult");
            } else {
                z = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            z = false;
        }
        if (!z) {
            ayS(1, (String) null);
        } else {
            ayR();
        }
    }
}
