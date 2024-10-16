package com.android.settings.bootloader;

import android.content.DialogInterface;

/* compiled from: BootloaderStatusActivity */
final class h implements DialogInterface.OnClickListener {
    final /* synthetic */ BootloaderStatusActivity bDe;

    h(BootloaderStatusActivity bootloaderStatusActivity) {
        this.bDe = bootloaderStatusActivity;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        this.bDe.finish();
    }
}
