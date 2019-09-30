package com.example.common;

import com.example.common.app.Config;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {
    //服务器地址
    public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCkDtr9lR2GLoGeJWLmXHbf1HSEvOzH+Wgz7+Dc9nTEKNXnSq1g8c5UmG4YbXwUtuFwJMgR06GWkMCX3yqXBeXKM/wibQLRr/5UGVNJgUo8jpajwScpuzqb1QLohluyhRd0TKp7JJDqpOIWsIKrc6c4qx/MeDZYJg2cXwH829T7HQIDAQAB";
    private static final OkHttpClient client = new OkHttpClient.Builder().
            connectTimeout(60, TimeUnit.SECONDS).
            readTimeout(60, TimeUnit.SECONDS).
            callTimeout(60,TimeUnit.SECONDS).
            writeTimeout(60, TimeUnit.SECONDS).build();

    //获取Retrofit
    public static Retrofit getRetroClient() {
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(Config.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())//Gson
                .build();
    }

    public static <T> T getServices(Class<T> cls){
        Retrofit retrofit = RetroClient.getRetroClient();
        return retrofit.create(cls);
    }
}
