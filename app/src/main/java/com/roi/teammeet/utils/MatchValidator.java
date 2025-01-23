package com.roi.teammeet.utils;

import java.util.Date;

public class MatchValidator {

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
    /*public static boolean isTimeValid(String time){

    }
    public static boolean isCityValid(String city){

    }*/
    public static boolean isAgeRangeValid(String min, String max){
        int minNum = Integer.parseInt(min);
        int maxNum = Integer.parseInt(max);

        return minNum >= 12 && minNum <= maxNum;
    }
    public static boolean isSizeValid(String size){
        int sizeNum = Integer.parseInt(size);
        return sizeNum > 1 && sizeNum < 100;
    }
}
