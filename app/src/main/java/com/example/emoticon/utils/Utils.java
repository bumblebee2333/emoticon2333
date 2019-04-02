package com.example.emoticon.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static boolean isEmail(String string) {
        if (string == null)
            return false;
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

        Pattern p = Pattern.compile(regEx1);
        Matcher m = p.matcher(string);
        if (m.matches())
            return true;
        else
            return false;
    }
}
