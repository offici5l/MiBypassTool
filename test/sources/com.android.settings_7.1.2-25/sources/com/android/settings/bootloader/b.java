package com.android.settings.bootloader;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/* compiled from: BootloaderStatusActivity */
class b extends AsyncTask {
    final /* synthetic */ BootloaderStatusActivity aLY;

    b(BootloaderStatusActivity bootloaderStatusActivity) {
        this.aLY = bootloaderStatusActivity;
    }

    /* access modifiers changed from: protected */
    /* renamed from: ayX */
    public d doInBackground(Context... contextArr) {
        Context context = contextArr[0];
        d dVar = new d();
        try {
            return a.ayM(context);
        } catch (Utils$AccountExcepiton e) {
            dVar.aLZ = 1;
            return dVar;
        } catch (Utils$ParameterException e2) {
            Log.d(BootloaderStatusActivity.TAG, "Parameter error: " + e2.toString());
            dVar.aLZ = 3;
            return dVar;
        } catch (Exception e3) {
            dVar.aLZ = 3;
            return dVar;
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: ayY */
    public void onPostExecute(d dVar) {
        this.aLY.aLW.setEnabled(true);
        this.aLY.aLW.setTextColor(-872415232);
        this.aLY.ayS(dVar.aLZ, dVar.aMa);
    }
}
