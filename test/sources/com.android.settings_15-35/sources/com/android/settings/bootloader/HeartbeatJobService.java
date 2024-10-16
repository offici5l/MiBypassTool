package com.android.settings.bootloader;

import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.Context;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;

public class HeartbeatJobService extends JobService {
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    public boolean onStartJob(final JobParameters jobParameters) {
        new Thread() {
            public void run() {
                HeartbeatJobService heartbeatJobService = HeartbeatJobService.this;
                boolean r0 = heartbeatJobService.canSendHeartbeat(heartbeatJobService);
                boolean z = !r0;
                if (r0 && CloudDeviceStatus.sendHeartbeat(HeartbeatJobService.this) >= 30) {
                    z = true;
                }
                if (z) {
                    Log.d("bootloader_heartbeat", "cancel job");
                    HeartbeatJobService.cancelHeartbeatJob(HeartbeatJobService.this);
                }
                HeartbeatJobService.this.jobFinished(jobParameters, false);
            }
        }.start();
        return true;
    }

    public static void cancelHeartbeatJob(Context context) {
        ((JobScheduler) context.getSystemService("jobscheduler")).cancel(44012);
    }

    /* access modifiers changed from: private */
    public boolean canSendHeartbeat(Context context) {
        if (!"locked".equals(SystemProperties.get("ro.secureboot.lockstate", (String) null))) {
            return false;
        }
        String accountName = Utils.getAccountName(context);
        if (!TextUtils.isEmpty(accountName) && accountName.equals(getSharedPreferences(getDefaultSharedPreferencesName(context), 4).getString("bootloader_account", ""))) {
            return true;
        }
        return false;
    }

    private String getDefaultSharedPreferencesName(Context context) {
        return context.getPackageName() + "_preferences";
    }
}
