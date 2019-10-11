package com.example.common.utils;

public class ImageUtils {
    public static final String gifToJpg = "?x-oss-process=image/format,jpg";//Gif转jpg

    /**
     * 修改GIF大小
     * @param size 大小
     * @return 链接后缀
     */
    public static String reSize(int size) {
        return "?x-oss-process=image/resize,w_" + size + "/format,gif";
    }

}
