package com.android.settings.bootloader;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.android.settings.bootloader.Utils;

/* compiled from: BootloaderStatusActivity */
class b extends AsyncTask {
    final /* synthetic */ BootloaderStatusActivity UK;

    b(BootloaderStatusActivity bootloaderStatusActivity) {
        this.UK = bootloaderStatusActivity;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public d doInBackground(Context... contextArr) {
        Context context = contextArr[0];
        d dVar = new d();
        try {
            return c.aG(context);
        } catch (Utils.AccountExcepiton e) {
            dVar.UO = 1;
            return dVar;
        } catch (Utils.ParameterException e2) {
            Log.d(BootloaderStatusActivity.TAG, "Parameter error: " + e2.toString());
            dVar.UO = 3;
            return dVar;
        } catch (Exception e3) {
            dVar.UO = 3;
            return dVar;
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onPostExecute(d dVar) {
        this.UK.UH.setEnabled(true);
        this.UK.UH.setTextColor(-872415232);
        this.UK.b(dVar.UO, dVar.UP);
    }
}
