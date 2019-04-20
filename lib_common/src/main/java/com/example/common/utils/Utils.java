package com.example.common.utils;

import android.content.res.Resources;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final float DENSITY = Resources.getSystem().getDisplayMetrics().density;

    /**
     *   dp转换px
     *   @param dp   dp值
     *   @return  转换后的px值
     */
    public static int dp2Px(int dp) {
        return Math.round(dp * DENSITY);
    }

    /**
     *
     * @param string 字符串
     * @return 是否合法
     */
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
