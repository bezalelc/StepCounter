package com.yehonatand_bezalelc.stepcounter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "service", Toast.LENGTH_SHORT).show();
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()) || Intent.ACTION_SHUTDOWN.equals(intent.getAction())) {
            Intent stopCounterService = new Intent(context, StepCounterService.class);
            context.startService(stopCounterService);
        }
    }
}
