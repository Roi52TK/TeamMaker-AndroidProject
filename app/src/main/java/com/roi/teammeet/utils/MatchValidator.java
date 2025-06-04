package com.roi.teammeet.utils;

import com.roi.teammeet.MyApplication;

import java.time.Year;
import java.util.Date;

public class MatchValidator {

    public static final int TITLE_MIN_LENGTH = 4;
    public static final int TITLE_MAX_LENGTH = 20;
    public static final int DESCRIPTION_MAX_LENGTH = 50;

    public static boolean isTitleValid(String title){
        int length = title.length();
        return length >= TITLE_MIN_LENGTH && length <= TITLE_MAX_LENGTH;
    }
    public static boolean isDescriptionValid(String description){
        return description.length() <= DESCRIPTION_MAX_LENGTH;
    }

    public static boolean isDateTimeValid(String chosenDate, String chosenTime) {
        return !DateUtil.isExpired(chosenDate, chosenTime);
    }

    public static boolean isAddressValid(String address){
        return address != null;
    }

    public static boolean isMinAgeValid(String min){
        return !min.isEmpty() && Integer.parseInt(min) >= MyApplication.AGE_LIMIT;
    }
    public static boolean isAgeRangeValid(String min, String max){
        if(max.isEmpty()){
            return false;
        }
        return Integer.parseInt(min) <= Integer.parseInt(max);
    }

    public static boolean isUserAgeValid(String min, String max, String birthDate) {
        int age = DateUtil.getAge(birthDate);

        return age >= Integer.parseInt(min) && age <= Integer.parseInt(max);
    }

    public static boolean isSizeValid(String size){
        if(size.isEmpty()){
            return false;
        }
        int sizeNum = Integer.parseInt(size);
        return sizeNum > 1 && sizeNum < 100;
    }
}
