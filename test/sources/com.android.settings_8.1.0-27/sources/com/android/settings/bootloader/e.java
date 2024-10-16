package com.android.settings.bootloader;

import android.app.job.JobParameters;
import android.util.Log;

/* compiled from: HeartbeatJobService */
final class e extends Thread {
    final /* synthetic */ HeartbeatJobService bDa;
    final /* synthetic */ JobParameters bDb;

    e(HeartbeatJobService heartbeatJobService, JobParameters jobParameters) {
        this.bDa = heartbeatJobService;
        this.bDb = jobParameters;
    }

    public void run() {
        boolean bvG = this.bDa.bvC(this.bDa);
        boolean z = !bvG;
        if (bvG && b.bvR(this.bDa) >= 30) {
            z = true;
        }
        if (z) {
            Log.d("bootloader_heartbeat", "cancel job");
            HeartbeatJobService.bvD(this.bDa);
        }
        this.bDa.jobFinished(this.bDb, false);
    }
}
