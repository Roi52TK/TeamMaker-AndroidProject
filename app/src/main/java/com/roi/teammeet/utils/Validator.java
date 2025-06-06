package com.roi.teammeet.utils;

import android.util.Patterns;

import com.roi.teammeet.MyApplication;

public class Validator {

    public static boolean isEmailValid(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPasswordValid(String password) {
        return password != null && password.length() >= 6;
    }

    public static boolean isUsernameValid(String username) {
        return username != null && username.length() >= 3;
    }

    public static boolean isPhoneValid(String phone){
        return phone.length() == 10;
    }

    public static boolean isBirthDateValid(String chosenDate) {
        return DateUtil.getAge(chosenDate) >= MyApplication.AGE_LIMIT;
    }
}