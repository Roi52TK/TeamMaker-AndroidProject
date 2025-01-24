package com.roi.teammeet.utils;

import java.util.Date;

public class MatchValidator {
    public static final int MIN_AGE = 12;

    public static boolean isTitleValid(String title){
        int length = title.length();
        return length >= 4 && length <= 12;
    }
    public static boolean isDetailsValid(String details){
        return details.length() <= 20;
    }
    public static boolean isDateValid(Date date){
        Date today = new Date();
        return date.after(today) || date == today;
    }
    /*public static boolean isDateValid(String date){

    }
    public static boolean isTimeValid(String time){

    }*/
    public static boolean isCityValid(String city){
        return city.length() >= 3;
    }
    public static boolean isMinAgeValid(String min){
        return !min.isEmpty() && Integer.parseInt(min) >= MIN_AGE;
    }
    public static boolean isAgeRangeValid(String min, String max){
        if(max.isEmpty()){
            return false;
        }
        return Integer.parseInt(min) <= Integer.parseInt(max);
    }
    public static boolean isSizeValid(String size){
        if(size.isEmpty()){
            return false;
        }
        int sizeNum = Integer.parseInt(size);
        return sizeNum > 1 && sizeNum < 100;
    }
}
