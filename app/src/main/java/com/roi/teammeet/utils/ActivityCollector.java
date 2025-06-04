package com.roi.teammeet.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollector {
    private static final List<Activity> ACTIVITIES = new ArrayList<>();

    public static void addActivity(Activity activity) {
        ACTIVITIES.add(activity);
    }

    public static void removeActivity(Activity activity) {
        ACTIVITIES.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : ACTIVITIES) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        ACTIVITIES.clear();
    }
}

