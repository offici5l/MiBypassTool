package com.android.settings.bootloader;

import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemProperties;
import android.provider.MiuiSettings;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.settings.search.provider.SettingsProvider;
import java.util.Locale;
import miui.app.Activity;
import miui.app.AlertDialog;
import miui.os.Build;

public class BootloaderStatusActivity extends Activity implements AccountManagerCallback<Bundle> {
    private static final String TAG = BootloaderStatusActivity.class.getSimpleName();
    /* access modifiers changed from: private */
    public Button bCO;
    private boolean bCP = false;
    private boolean bCQ = true;
    private Toast bCR = null;

    /* JADX WARNING: type inference failed for: r3v0, types: [android.content.Context, com.android.settings.bootloader.BootloaderStatusActivity, miui.app.Activity] */
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        int i;
        BootloaderStatusActivity.super.onCreate(bundle);
        if (!"locked".equals(SystemProperties.get("ro.secureboot.lockstate", (String) null))) {
            setContentView(R.layout.bootloader_status_unlocked);
            return;
        }
        setContentView(R.layout.bootloader_status_locked);
        TextView textView = (TextView) findViewById(R.id.answer);
        if (c.bwc()) {
            i = R.string.bootloader_locked_answer_2;
        } else {
            i = R.string.bootloader_locked_answer_2_no_sim;
        }
        textView.setText(i);
        this.bCO = (Button) findViewById(R.id.button);
        this.bCO.setOnClickListener(new f(this));
        if (!bvJ(this)) {
            Intent intent = new Intent("miui.intent.action.PRIVACY_AUTHORIZATION_DIALOG");
            intent.putExtra(SettingsProvider.ARGS_KEY, getPackageName());
            startActivityForResult(intent, 1);
        }
        if (Build.IS_INTERNATIONAL_BUILD) {
            bvK();
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 1 && i2 != -1) {
            finish();
        }
        BootloaderStatusActivity.super.onActivityResult(i, i2, intent);
    }

    private boolean bvJ(Context context) {
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

    /* JADX WARNING: type inference failed for: r8v0, types: [android.content.Context, com.android.settings.bootloader.BootloaderStatusActivity] */
    private void bvK() {
        String format = String.format("http://www.miui.com/res/doc/privacy.html?region=%s&lang=%s", new Object[]{Build.getRegion(), Locale.getDefault().toString()});
        String string = getString(R.string.bootloader_status_privacy_dialog_url_text);
        SpannableString spannableString = new SpannableString(getString(R.string.bootloader_status_privacy_dialog_message, new Object[]{string}));
        int indexOf = spannableString.toString().indexOf(string);
        int length = string.length() + indexOf;
        spannableString.setSpan(new URLSpan(format), indexOf, length, 33);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.bootloader_url)), indexOf, length, 33);
        new AlertDialog.Builder(this).setTitle(R.string.bootloader_status_privacy_dialog_title).setMessage(spannableString).setCancelable(false).setPositiveButton(R.string.bootloader_status_privacy_dialog_yes, new g(this)).setNegativeButton(R.string.bootloader_status_privacy_dialog_no, new h(this)).show().getMessageView().setMovementMethod(LinkMovementMethod.getInstance());
    }

    /* JADX WARNING: type inference failed for: r7v0, types: [android.content.Context, com.android.settings.bootloader.BootloaderStatusActivity] */
    private void bvL() {
        String string = getString(R.string.bootloader_tips_warning);
        SpannableString spannableString = new SpannableString(getString(R.string.bootloader_tips, new Object[]{string}));
        int indexOf = spannableString.toString().indexOf(string);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.bootloader_warning)), indexOf, string.length() + indexOf, 17);
        new AlertDialog.Builder(this).setMessage(spannableString).setCancelable(false).setNegativeButton(R.string.bootloader_tips_ok, new i(this)).show();
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: android.content.Context[]} */
    /* access modifiers changed from: private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void bvH() {
        /*
            r5 = this;
            r4 = 1
            r3 = 0
            r2 = 0
            boolean r0 = com.android.settings.bootloader.c.bwc()
            if (r0 == 0) goto L_0x0014
            int r0 = com.android.settings.bootloader.c.bwd(r5)
            if (r4 != r0) goto L_0x0014
            r0 = 7
            r5.bvI(r0, r2)
            return
        L_0x0014:
            boolean r0 = com.android.settings.bootloader.c.bwe(r5)
            if (r0 != 0) goto L_0x0028
            boolean r0 = r5.bCQ
            if (r0 == 0) goto L_0x0024
            r5.bCQ = r3
            com.android.settings.bootloader.c.bwf(r5, r5)
            return
        L_0x0024:
            r5.bvI(r4, r2)
            return
        L_0x0028:
            boolean r0 = com.android.settings.bootloader.c.bwg(r5)
            if (r0 != 0) goto L_0x0033
            r0 = 2
            r5.bvI(r0, r2)
            return
        L_0x0033:
            boolean r0 = com.android.settings.bootloader.c.bwc()
            if (r0 == 0) goto L_0x0047
            boolean r0 = com.android.settings.bootloader.c.bwh(r5)
            r0 = r0 ^ 1
            if (r0 == 0) goto L_0x0047
            r0 = 11
            r5.bvI(r0, r2)
            return
        L_0x0047:
            android.widget.Button r0 = r5.bCO
            r0.setEnabled(r3)
            android.widget.Button r0 = r5.bCO
            r1 = 863010960(0x33708090, float:5.599628E-8)
            r0.setTextColor(r1)
            r0 = 6
            r5.bvI(r0, r2)
            com.android.settings.bootloader.a r0 = new com.android.settings.bootloader.a
            r0.<init>(r5)
            android.content.Context[] r1 = new android.content.Context[r4]
            r1[r3] = r5
            r0.execute(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.bootloader.BootloaderStatusActivity.bvH():void");
    }

    /* access modifiers changed from: private */
    public void bvI(int i, String str) {
        int i2 = R.string.bootloader_device_bind_fail;
        if (!this.bCQ) {
            this.bCQ = true;
        }
        if (this.bCR != null) {
            this.bCR.cancel();
        }
        if (i == 1) {
            i2 = R.string.bootloader_device_bind_no_account;
        } else if (i == 2) {
            i2 = R.string.bootloader_device_bind_no_network;
        } else if (!(i == 3 || i == 5)) {
            if (i == 6) {
                i2 = R.string.bootloader_device_bind_msg;
            } else if (i == 4) {
                this.bCR = Toast.makeText(getApplicationContext(), str, 1);
                this.bCR.show();
                return;
            } else if (i == 7) {
                i2 = R.string.bootloader_device_bind_no_sim;
            } else if (i == 8) {
                i2 = R.string.bootloader_device_bind_sim_invalid;
            } else if (i == 9) {
                i2 = R.string.bootloader_device_bind_exceed_sim_limit;
            } else if (i == 11) {
                i2 = R.string.bootloader_device_bind_no_data_network;
            } else if (i == 401) {
                if (!this.bCP) {
                    i2 = R.string.bootloader_device_bind_server_token_expired_1;
                    this.bCP = true;
                } else {
                    i2 = R.string.bootloader_device_bind_server_token_expired_2;
                }
            } else if (i == 10) {
                this.bCR = Toast.makeText(getApplicationContext(), getString(R.string.bootloader_device_bind_server_code) + str, 1);
                this.bCR.show();
                return;
            } else if (i == 0) {
                i2 = R.string.bootloader_device_bind_already;
                HeartbeatJobService.bvF(getApplicationContext());
                if (!isFinishing() && (!isDestroyed())) {
                    bvL();
                }
            }
        }
        this.bCR = Toast.makeText(getApplicationContext(), i2, 0);
        this.bCR.show();
    }

    public void run(AccountManagerFuture<Bundle> accountManagerFuture) {
        boolean z;
        try {
            Bundle result = accountManagerFuture.getResult();
            z = result != null ? result.getBoolean("booleanResult") : false;
        } catch (Exception e) {
            e.printStackTrace();
            z = false;
        }
        if (!z) {
            bvI(1, (String) null);
        } else {
            bvH();
        }
    }
}
