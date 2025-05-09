package com.roi.teammeet;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.work.Configuration;
import androidx.work.WorkManager;

public class MyApplication extends Application implements Configuration.Provider {

    @Override
    public void onCreate() {
        super.onCreate();
        // Create the notification channel
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        // Create the notification channel only for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(
                    "match_notifications_channel", // Channel ID
                    "Match Notifications",         // Channel name
                    NotificationManager.IMPORTANCE_DEFAULT // Importance level
            );
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public Configuration getWorkManagerConfiguration() {
        // Optional: You can specify WorkManager's logging level or other configurations here
        return new Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.DEBUG)
                .build();
    }
}