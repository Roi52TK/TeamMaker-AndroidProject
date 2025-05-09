package com.roi.teammeet.utils;

import android.content.Context;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ReminderUtils {

    public static void scheduleMeetingReminder(Context context, long meetingTimeMillis, String title, String time, String username, String matchId) {
        long currentTime = System.currentTimeMillis();
        //long delay = meetingTimeMillis - currentTime - 60 * 60 * 1000; // 1 hour before

        long delay = meetingTimeMillis;

        if (delay > 0) {
            Data inputData = new Data.Builder()
                    .putString("matchTitle", title)
                    .putString("matchTime", time)
                    .putString("username", username)
                    .putString("matchId", matchId)
                    .build();

            OneTimeWorkRequest reminderRequest = new OneTimeWorkRequest.Builder(MatchNotificationWorker.class)
                    .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                    .setInputData(inputData)
                    .build();

            WorkManager.getInstance(context).enqueue(reminderRequest);
        }
    }

    public static long dateTimeToMilliseconds(String date, String time) {
        String dateTimeStr = date + " " + time; // e.g., "12/05/2025 15:30"
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        try {
            Date targetDate = sdf.parse(dateTimeStr);
            if (targetDate != null) {
                long now = System.currentTimeMillis();
                return targetDate.getTime() - now;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1; // error
    }
}
