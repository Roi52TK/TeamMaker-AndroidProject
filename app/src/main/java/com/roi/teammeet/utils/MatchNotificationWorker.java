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
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "match_notifications_channel")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Match Reminder: " + matchTitle)
                .setContentText(userName + ", your match is scheduled at " + matchTime)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // Use the match ID's hashCode as a unique notification ID
        int notificationId = matchId.hashCode();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(notificationId, builder.build());
    }
}