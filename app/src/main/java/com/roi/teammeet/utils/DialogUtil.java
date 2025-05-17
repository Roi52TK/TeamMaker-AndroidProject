package com.roi.teammeet.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.Locale;

public class DialogUtil {
    private static final String TAG = "DialogUtil";

    public static void openMapOptions(Context context, double lat, double lang) {
        String uri = String.format(Locale.ENGLISH, "geo:%f,%f?q=%f,%f", lat, lang, lat, lang);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        context.startActivity(Intent.createChooser(intent, "פתח באמצעות"));
    }
}
