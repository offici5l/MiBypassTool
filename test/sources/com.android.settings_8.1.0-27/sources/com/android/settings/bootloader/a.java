package com.android.settings.bootloader;

import android.content.Context;
import android.os.AsyncTask;

/* compiled from: BootloaderStatusActivity */
class a extends AsyncTask<Context, Void, d> {
    final /* synthetic */ BootloaderStatusActivity bCS;

    a(BootloaderStatusActivity bootloaderStatusActivity) {
        this.bCS = bootloaderStatusActivity;
    }

    /* access modifiers changed from: protected */
    /* renamed from: bvP */
    public d doInBackground(Context... contextArr) {
        Context context = contextArr[0];
        d dVar = new d();
        try {
            return b.bvS(context);
        } catch (Utils$AccountExcepiton e) {
            dVar.bCY = 1;
            return dVar;
        } catch (Exception e2) {
            dVar.bCY = 3;
            return dVar;
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: bvQ */
    public void onPostExecute(d dVar) {
        this.bCS.bCO.setEnabled(true);
        this.bCS.bCO.setTextColor(-872415232);
        this.bCS.bvI(dVar.bCY, dVar.bCZ);
    }
}
