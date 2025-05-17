package com.roi.teammeet;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Configuration;
import androidx.work.Data;
import androidx.work.WorkManager;

import com.roi.teammeet.models.Match;
import com.roi.teammeet.services.DatabaseService;

import java.util.List;

public class MyApplication extends Application implements Configuration.Provider {

    private static final String TAG = "MyApplication";
    public static final int AGE_LIMIT = 12;
    private DatabaseService databaseService;

    @Override
    public void onCreate() {
        super.onCreate();

        databaseService = DatabaseService.getInstance();

        // Create the notification channel
        createNotificationChannel();
        deleteExpiredMatches();
    }

    private void deleteExpiredMatches() {
        databaseService.getMatchList(new DatabaseService.DatabaseCallback<List<Match>>() {
            @Override
            public void onCompleted(List<Match> object) {
                Log.d(TAG, "onCompleted: Matches received successfully");
                for(Match m: object){
                    if(m.isExpired()){
                        databaseService.deleteMatch(m.getId(), new DatabaseService.DatabaseCallback<Void>() {
                            @Override
                            public void onCompleted(Void object) {
                                Log.d(TAG, "onCompleted: Deleted expired match successfully");
                            }

                            @Override
                            public void onFailed(Exception e) {
                                Log.e(TAG, "onFailed: Failed to delete match", e);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
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