package com.roi.teammeet.utils;

import android.content.Context;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.roi.teammeet.R;

public class MatchNotificationWorker extends Worker {

    public MatchNotificationWorker(Context context, WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @Override
    public Result doWork() {
        // Retrieve match data from input data
        String matchTitle = getInputData().getString("matchTitle");
        String matchTime = getInputData().getString("matchTime");
        String userName = getInputData().getString("userName");
        String matchId = getInputData().getString("matchId");

        // Create and show the notification
        showNotification(matchTitle, matchTime, userName, matchId);

        // Indicate that the work has completed successfully
        return Result.success();
    }

    private void showNotification(String matchTitle, String matchTime, String userName, String matchId) {
        // Check if the channel is created only once
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            android.app.NotificationChannel channel = new android.app.NotificationChannel(
                    "match_notifications_channel",
                    "Match Notifications",
                    android.app.NotificationManager.IMPORTANCE_DEFAULT
            );
            android.app.NotificationManager manager = getApplicationContext().getSystemService(android.app.NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "match_notifications_channel")
                //.setSmallIcon(R.drawable.ic_notification)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Match Reminder: " + matchTitle)
                .setContentText(userName + ", your match is scheduled at " + matchTime)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        int notificationId = matchId.hashCode();
        NotificationManagerCompat.from(getApplicationContext()).notify(notificationId, builder.build());
    }
}