package com.example.emoticon.utils;

public class ImageUtils {
    public static final String gifToJpg = "?x-oss-process=image/format,jpg";//Gif转jpg
    //修改GIF大小
    public static String reSize(int size){
        return "?x-oss-process=image/resize,w_"+size+"/format,gif";
    }
}
