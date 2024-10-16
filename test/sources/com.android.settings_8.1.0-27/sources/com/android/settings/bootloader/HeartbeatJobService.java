package com.android.settings.bootloader;

import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.Context;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import com.android.settings.dG;
import com.miui.activityutil.b;

public class HeartbeatJobService extends JobService {
    public boolean onStartJob(JobParameters jobParameters) {
        new e(this, jobParameters).start();
        return true;
    }

    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    public static void bvF(Context context) {
        String bwb = c.bwb(context);
        if (!TextUtils.isEmpty(bwb)) {
            if (!bwb.equals(PreferenceManager.getDefaultSharedPreferences(context).getString("bootloader_account", b.d))) {
                PreferenceManager.getDefaultSharedPreferences(context).edit().putString("bootloader_account", bwb).commit();
            }
            dG.cxf(context, 44012, true);
        }
    }

    public static void bvD(Context context) {
        ((JobScheduler) context.getSystemService("jobscheduler")).cancel(44012);
    }

    /* access modifiers changed from: private */
    public boolean bvC(Context context) {
        if (!"locked".equals(SystemProperties.get("ro.secureboot.lockstate", (String) null))) {
            return false;
        }
        String bwb = c.bwb(context);
        if (!TextUtils.isEmpty(bwb) && bwb.equals(getSharedPreferences(bvE(context), 4).getString("bootloader_account", b.d))) {
            return true;
        }
        return false;
    }

    private String bvE(Context context) {
        return context.getPackageName() + "_preferences";
    }
}
