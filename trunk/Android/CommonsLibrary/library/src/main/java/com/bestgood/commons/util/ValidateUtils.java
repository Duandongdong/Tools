package com.bestgood.commons.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dengdingchun on 16/4/11.
 */
public class ValidateUtils {

    public static boolean validatePhone(String phoneNumber) {
        String phoneRegex = "^1\\d{10}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

}
