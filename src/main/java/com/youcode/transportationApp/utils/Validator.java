package com.youcode.transportationApp.utils;

import java.util.regex.Pattern;

public class Validator {

    public static boolean validateEmail(String email) {
         Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
         return VALID_EMAIL_ADDRESS_REGEX.matcher(email).matches();
    }

    public static boolean validatePhoneNumber(String phoneNumber) {
        Pattern VALID_PHONE_NUMBER_REGEX = Pattern.compile("^(05|06|07)\\d{8}$");
        return VALID_PHONE_NUMBER_REGEX.matcher(phoneNumber).matches();
    }
}

