package com.roi.teammeet.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    private static final String TAG = "DateUtil";


    public static Date convertStringToDate(String str){
        if(str==null)
            return null;

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = format.parse(str);
            Log.d(TAG, "successfully converted String to Date");
        } catch (ParseException e) {
            Log.e(TAG, "failed to convert String to Date");
        }

        return date;
    }

    public static int getYear(String str){
        Date date = convertStringToDate(str);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.YEAR);
    }
    public static int getMonth(String str){
        Date date = convertStringToDate(str);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Log.d(TAG, calendar.get(Calendar.MONTH)+1+" month");
        return calendar.get(Calendar.MONTH);
    }
    public static int getDay(String str){
        Date date = convertStringToDate(str);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static String getToday(){
        Log.d(TAG, "today is " + new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
    }
}
