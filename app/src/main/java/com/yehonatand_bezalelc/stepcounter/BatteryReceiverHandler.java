package com.yehonatand_bezalelc.stepcounter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

public class BatteryReceiverHandler extends BroadcastReceiver {
    private final StepCounterService stepCounterService;
    private final int batteryLevelTresHold;

    public BatteryReceiverHandler(StepCounterService stepCounterService, int batteryLevelTresHold) {
        this.stepCounterService = stepCounterService;
        this.batteryLevelTresHold = batteryLevelTresHold;
//        checkBatteryLevel();
        stepCounterService.registerReceiver(this, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    private static boolean isBatteryLow(Intent batteryStatus, int batteryLevelTresHold) {
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        return (level / (float) scale * 100) <= batteryLevelTresHold;
    }

    @Override
    public void onReceive(Context context, Intent batteryStatus) {
        if (Intent.ACTION_BATTERY_CHANGED.equals(batteryStatus.getAction())) {
            if (isBatteryLow(batteryStatus, batteryLevelTresHold)) {
                NotificationHandler.NOTIFICATION_TYPE notificationType =
                        NotificationHandler.NOTIFICATION_TYPE.NOTIFICATION_TYPE_LOW_BATTERY;
                stepCounterService.getNotificationHandler().showNotification(notificationType);
                stepCounterService.stopService();
            }
        }
    }

    public static boolean checkBatteryLevel(Context context, int batteryLevelTresHold) {
        Intent batteryStatus = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        return isBatteryLow(batteryStatus, batteryLevelTresHold);
    }

    public void unregister() {
        stepCounterService.unregisterReceiver(this);
    }
}
