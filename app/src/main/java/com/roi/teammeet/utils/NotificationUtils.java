package com.roi.teammeet.utils;

import android.content.Context;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.roi.teammeet.R;

public class NotificationUtils {

    // This method can be called from any Activity to schedule a notification
    public static void scheduleMatchNotification(Context context, String matchTitle, String matchTime, String userName, String matchId) {
        // Use a WorkManager to schedule the notification, so it can persist even if the app is closed
        Data inputData = new Data.Builder()
                .putString("matchTitle", matchTitle)
                .putString("matchTime", matchTime)
                .putString("userName", userName)
                .putString("matchId", matchId)
                .build();

        // Create the WorkRequest
        WorkRequest notificationWorkRequest = new OneTimeWorkRequest.Builder(MatchNotificationWorker.class)
                .setInputData(inputData)
                .build();

        // Enqueue the work request
        WorkManager.getInstance(context).enqueue(notificationWorkRequest);
    }

    // This method can be used to show a simple notification immediately (not persistent across app closures)
    public static void showSimpleNotification(Context context, String matchTitle, String matchTime, String userName, String matchId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "match_notifications_channel")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Match Reminder: " + matchTitle)
                .setContentText(userName + ", your match is scheduled at " + matchTime)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // Use the match ID's hashCode as a unique notification ID
        int notificationId = matchId.hashCode();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());
    }
}
