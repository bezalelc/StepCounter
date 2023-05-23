package com.yehonatand_bezalelc.stepcounter;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.List;

public class StepCounterService extends Service implements SensorEventListener, StepCountObservable {

    private SensorManager sensorManager;
    private Sensor stepSensor;
    private int stepCount = 0;
    private final List<StepCountObserver> observers = new ArrayList<>();
    private final IBinder binder = new StepCounterBinder();
    private BroadcastReceiver batteryReceiver;
    private static final int BATTERY_LEVEL_THRESHOLD = 30;
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "step_counter_channel";
    private static final String CHANNEL_NAME = "Step Counter";

    public class StepCounterBinder extends Binder {
        StepCounterService getService() {
            return StepCounterService.this;
        }

        public int getStepCount() {
            return getService().stepCount;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
            registerBatteryReceiver();
            startForeground(NOTIFICATION_ID, createNotification());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterBatteryReceiver();
        sensorManager.unregisterListener(this, stepSensor);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.cancel(NOTIFICATION_ID);
        }
        stopForeground(STOP_FOREGROUND_REMOVE);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            stepCount = (int) event.values[0];
            notifyStepCountChanged(stepCount);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


    @Override
    public void addObserver(StepCountObserver observer) {
        observers.add(observer);
        notifyStepCountChanged(stepCount);
    }

    @Override
    public void removeObserver(StepCountObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyStepCountChanged(int stepCount) {
        for (StepCountObserver observer : observers) {
            observer.onStepCountChanged(stepCount);
        }
    }

    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
        channel.setSound(null, null);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }


    private Notification createNotification() {
        createNotificationChannel();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle(CHANNEL_NAME).setContentText("Counting steps...").setSmallIcon(R.drawable.ic_stat_directions_run).setContentIntent(pendingIntent);

        return builder.build();
    }


    private void registerBatteryReceiver() {
        batteryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                    int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                    int batteryLevel = (int) (level / (float) scale * 100);

                    if (batteryLevel < BATTERY_LEVEL_THRESHOLD) {
                        stopForeground(STOP_FOREGROUND_REMOVE);
                        stopSelf();
                    }
                }
            }
        };

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, filter);
    }

    private void unregisterBatteryReceiver() {
        unregisterReceiver(batteryReceiver);
    }
}
