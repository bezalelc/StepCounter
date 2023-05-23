package com.yehonatand_bezalelc.stepcounter;

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
import android.os.IBinder;

import java.util.ArrayList;
import java.util.List;

public class StepCounterService extends Service implements SensorEventListener, StepCountObservable {

    private SensorManager sensorManager;
    private Sensor stepSensor;
    private int stepCount = 0;
    private final List<StepCountObserver> observers = new ArrayList<>();
    private final IBinder binder = new StepCounterBinder();
    private BroadcastReceiver batteryReceiver;
    private NotificationHandler notificationHandler;
    public static final int BATTERY_LEVEL_THRESHOLD = 99;

    public class StepCounterBinder extends Binder {
        StepCounterService getService() {
            return StepCounterService.this;
        }

        public int getStepCount() {
            return getService().stepCount;
        }

        public void removeRunningNotification() {
            getService().notificationHandler.cancelNotification(NotificationHandler.NOTIFICATION_TYPE.NOTIFICATION_TYPE_SERVICE_RUNNING);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
            notificationHandler = NotificationHandler.getInstance(this);
            registerBatteryReceiver();
            notificationHandler.showNotification(NotificationHandler.NOTIFICATION_TYPE.NOTIFICATION_TYPE_SERVICE_RUNNING);
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
        stopService();
    }

    private void stopService() {
        unregisterBatteryReceiver();
        sensorManager.unregisterListener(this, stepSensor);
        notificationHandler.cancelNotification(NotificationHandler.NOTIFICATION_TYPE.NOTIFICATION_TYPE_SERVICE_RUNNING);
        stopForeground(STOP_FOREGROUND_REMOVE);
        stopSelf();
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

    private void registerBatteryReceiver() {
        batteryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                    int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                    int batteryLevel = (int) (level / (float) scale * 100);

                    if (batteryLevel <= BATTERY_LEVEL_THRESHOLD) {
                        NotificationHandler.NOTIFICATION_TYPE notificationType =
                                NotificationHandler.NOTIFICATION_TYPE.NOTIFICATION_TYPE_LOW_BATTERY;
                        notificationHandler.showNotification(notificationType);
                        stopService();
                    }
                }
            }
        };

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, filter);
    }

    private void unregisterBatteryReceiver() {
        if (batteryReceiver != null) {
            unregisterReceiver(batteryReceiver);
            batteryReceiver = null;
        }
    }
}
