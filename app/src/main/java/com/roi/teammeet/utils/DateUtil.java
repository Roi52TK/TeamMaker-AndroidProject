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

    public static boolean isExpired(String date, String time){
        String today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        Date current_date = convertStringToDate(today);
        Date expiration_date = convertStringToDate(date);

        if(current_date.after(expiration_date)){
            return true;
        }
        if(current_date.equals(expiration_date)){
            return hasTimePassed(time);
        }
        return false;
    }

    public static boolean hasTimePassed(String timeString) {
        try {
            // Parse the input time string
            SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date inputTime = format.parse(timeString);

            // Get current time in hh:mm format
            String nowStr = format.format(new Date());
            Date nowTime = format.parse(nowStr);

            Log.d(TAG, nowStr + " , " + nowTime);

            // Compare times
            return nowTime.after(inputTime);
        } catch (Exception e) {
            e.printStackTrace();
            return false; // fallback if parsing fails
        }
    }

    public static int getAge(String birthDateStr) {
        Date birthDate = convertStringToDate(birthDateStr);
        if (birthDate == null) return -1;

        Calendar birthCal = Calendar.getInstance();
        birthCal.setTime(birthDate);

        Calendar todayCal = Calendar.getInstance();

        int age = todayCal.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR);

        if (todayCal.get(Calendar.DAY_OF_YEAR) < birthCal.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age;
    }
}
