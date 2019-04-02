package com.example.emoticon.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

//SharedPreferences 工具类 为了实现历史记录的List集合存储数据
public class SharedPreferencesUtil {

    private static SharedPreferencesUtil util;
    private static SharedPreferences sp;

    private SharedPreferencesUtil(Context context,String name){
        sp = context.getSharedPreferences(name,Context.MODE_PRIVATE);
    }

    public static void getInstance(Context context, String name) {
        if (util == null) {
            util = new SharedPreferencesUtil(context, name);
        }
    }

    public static <T> boolean putListData(String key, List<T> list){
        boolean result;
        String type = list.get(0).getClass().getSimpleName();
        SharedPreferences.Editor editor = sp.edit();
        JsonArray array = new JsonArray();
        try {
            switch (type){
                case "String":
                    for(int i=0;i<list.size();i++){
                        array.add((String) list.get(i));
                    }
                    break;
                case "Integer":
                    for(int i=0;i<list.size();i++){
                        array.add((Integer) list.get(i));
                    }
                    break;
                default:
                    Gson gson = new Gson();
                    for(int i=0;i<list.size();i++){
                        JsonElement obj = gson.toJsonTree(list.get(i));
                        array.add(obj);
                    }
                    break;

            }
            editor.putString(key,array.toString());
            result = true;
        }catch (Exception e){
            result = false;
            e.printStackTrace();
        }
        editor.apply();
        return result;
    }
    //获取保存的List
    public static <T> List<T> getListData(String key,Class<T> cls){
        List<T> list = new ArrayList<>();
        String json = sp.getString(key,"");
        if(!json.equals("") && json.length()>0){
            Gson gson = new Gson();
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for(JsonElement element:array){
                list.add(gson.fromJson(element,cls));
            }
        }
        return list;
    }

    public static void clear(){
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}

