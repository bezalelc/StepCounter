package com.yehonatand_bezalelc.stepcounter;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;

public class NotificationHandler {
    public enum NOTIFICATION_TYPE {
        NOTIFICATION_TYPE_LOW_BATTERY(1,
                "Low Battery",
                "Your battery level is low. Please charge your device.",
                R.drawable.pngwing,
                false, false),
        NOTIFICATION_TYPE_SERVICE_RUNNING(2,
                "Step Counter",
                "Counting your steps",
                R.drawable.ic_stat_directions_run,
                true, true);

        private final int id, iconId;
        private final boolean ongoing, foreground;
        private final String title, msg;

        NOTIFICATION_TYPE(int id, String title, String msg, int iconId, boolean ongoing, boolean foreground) {
            this.id = id;
            this.title = title;
            this.msg = msg;
            this.iconId = iconId;
            this.ongoing = ongoing;
            this.foreground = foreground;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getMsg() {
            return msg;
        }

        public int getIconId() {
            return iconId;
        }

        public boolean isOngoing() {
            return ongoing;
        }

        public boolean isForeground() {
            return foreground;
        }
    }

    private static final String CHANNEL_ID = "step_counter_channel";
    private static final String CHANNEL_NAME = "Step Counter";


    private static NotificationHandler instance;
    private final StepCounterService stepCounterService;
    private final NotificationManager notificationManager;

    public NotificationHandler(StepCounterService context) {
        this.stepCounterService = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createNotificationChannel();
    }

//    public static synchronized NotificationHandler getInstance(StepCounterService context) {
//        if (instance == null) {
//            instance = new NotificationHandler(context);
//        }
//        return instance;
//    }

    public Notification generateNotification(NOTIFICATION_TYPE notificationType) {
        Notification.Builder builder = new Notification.Builder(stepCounterService, CHANNEL_ID)
                .setContentTitle(notificationType.getTitle())
                .setContentText(notificationType.getMsg())
                .setSmallIcon(notificationType.getIconId())
                .setAutoCancel(true)
                .setOngoing(notificationType.isOngoing()); // Set the notification as ongoing

        return builder.build();
    }

    public void showNotification(NOTIFICATION_TYPE notificationType) {
        Notification notification = generateNotification(notificationType);
        if (notificationType.isForeground()) {
            stepCounterService.startForeground(notificationType.getId(), notification);
        } else {
            notificationManager.notify(notificationType.getId(), notification);
        }
    }

    public void cancelNotification(NOTIFICATION_TYPE notificationType) {
        notificationManager.cancel(notificationType.getId());
    }

    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("App Notifications");
        channel.enableLights(true);
        channel.setLightColor(Color.BLUE);
        notificationManager.createNotificationChannel(channel);
    }
}
