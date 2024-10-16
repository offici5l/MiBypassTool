package com.android.settings.bootloader;

import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.os.Bundle;
import android.os.SystemProperties;
import android.widget.Button;
import android.widget.Toast;
import miui.app.Activity;

public class BootloaderStatusActivity extends Activity implements AccountManagerCallback {
    /* access modifiers changed from: private */
    public static final String TAG = BootloaderStatusActivity.class.getSimpleName();
    /* access modifiers changed from: private */
    public Button UH;
    private boolean UI = true;
    private Toast UJ = null;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        BootloaderStatusActivity.super.onCreate(bundle);
        String str = SystemProperties.get("ro.secureboot.lockstate", (String) null);
        if ("unlocked".equals(str) || str == null) {
            setContentView(R.layout.bootloader_status_unlocked);
            return;
        }
        setContentView(R.layout.bootloader_status_locked);
        this.UH = (Button) findViewById(R.id.button);
        this.UH.setOnClickListener(new a(this));
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: android.content.Context[]} */
    /* access modifiers changed from: private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void oF() {
        /*
            r5 = this;
            r4 = 1
            r3 = 0
            r2 = 0
            boolean r0 = com.android.settings.bootloader.Utils.aJ(r5)
            if (r0 != 0) goto L_0x0017
            boolean r0 = r5.UI
            if (r0 == 0) goto L_0x0013
            r5.UI = r2
            com.android.settings.bootloader.Utils.a((android.content.Context) r5, (android.accounts.AccountManagerCallback) r5)
        L_0x0012:
            return
        L_0x0013:
            r5.b(r4, r3)
            goto L_0x0012
        L_0x0017:
            boolean r0 = com.android.settings.bootloader.Utils.isNetworkConnected(r5)
            if (r0 != 0) goto L_0x0022
            r0 = 2
            r5.b(r0, r3)
            goto L_0x0012
        L_0x0022:
            android.widget.Button r0 = r5.UH
            r0.setEnabled(r2)
            android.widget.Button r0 = r5.UH
            r1 = 863010960(0x33708090, float:5.599628E-8)
            r0.setTextColor(r1)
            r0 = 6
            r5.b(r0, r3)
            com.android.settings.bootloader.b r0 = new com.android.settings.bootloader.b
            r0.<init>(r5)
            android.content.Context[] r1 = new android.content.Context[r4]
            r1[r2] = r5
            r0.execute(r1)
            goto L_0x0012
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.bootloader.BootloaderStatusActivity.oF():void");
    }

    /* access modifiers changed from: private */
    public void b(int i, String str) {
        int i2 = R.string.bootloader_device_bind_fail;
        if (!this.UI) {
            this.UI = true;
        }
        if (this.UJ != null) {
            this.UJ.cancel();
        }
        if (i == 1) {
            i2 = R.string.bootloader_device_bind_no_account;
        } else if (i == 2) {
            i2 = R.string.bootloader_device_bind_no_network;
        } else if (!(i == 3 || i == 5)) {
            if (i == 6) {
                i2 = R.string.bootloader_device_bind_msg;
            } else if (i == 4) {
                this.UJ = Toast.makeText(getApplicationContext(), str, 1);
                this.UJ.show();
                return;
            } else {
                i2 = R.string.bootloader_device_bind_already;
            }
        }
        this.UJ = Toast.makeText(getApplicationContext(), i2, 0);
        this.UJ.show();
    }

    public void run(AccountManagerFuture accountManagerFuture) {
        boolean z;
        try {
            Bundle bundle = (Bundle) accountManagerFuture.getResult();
            z = bundle != null && bundle.getBoolean("booleanResult");
        } catch (Exception e) {
            e.printStackTrace();
            z = false;
        }
        if (!z) {
            b(1, (String) null);
        } else {
            oF();
        }
    }
}
