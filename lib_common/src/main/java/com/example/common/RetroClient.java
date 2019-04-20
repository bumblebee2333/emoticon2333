package com.example.common;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {
    //服务器地址
    private static final String ROOT_URL = "http://140.143.154.18:8080/";

    //获取Retrofit
    public static Retrofit getRetroClient() {
        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())//Gson
                .build();
    }
}
