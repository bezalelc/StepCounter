package com.yehonatand_bezalelc.stepcounter;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class StepCounterService extends Service implements SensorEventListener, StepCountObservable {

    private SensorManager sensorManager;
    private Sensor stepSensor;
    private final static UserData userData = UserData.getInstance();
    private final List<StepCountObserver> observers = new ArrayList<>();
    private final IBinder binder = new StepCounterBinder();
    private BatteryReceiverHandler batteryReceiverHandler = null;
    private NotificationHandler notificationHandler;

    public class StepCounterBinder extends Binder {
        StepCounterService getService() {
            return StepCounterService.this;
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
            notificationHandler = new NotificationHandler(this);
            batteryReceiverHandler = new BatteryReceiverHandler(this, userData.getSaveBatteryThreshold());
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

    public void stopService() {
        if (batteryReceiverHandler != null) {
            batteryReceiverHandler.unregister();
            batteryReceiverHandler = null;
        }
        sensorManager.unregisterListener(this, stepSensor);
        notificationHandler.cancelNotification(NotificationHandler.NOTIFICATION_TYPE.NOTIFICATION_TYPE_SERVICE_RUNNING);
        stopForeground(STOP_FOREGROUND_REMOVE);
        stopSelf();
    }

    public NotificationHandler getNotificationHandler() {
        return notificationHandler;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            userData.updateSteps((int) event.values[0]);
            notifyStepCountChanged();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void addObserver(StepCountObserver observer) {
        observers.add(observer);
        notifyStepCountChanged();
    }

    @Override
    public void removeObserver(StepCountObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyStepCountChanged() {
        for (StepCountObserver observer : observers) {
            observer.onStepCountChanged();
        }
    }
}
