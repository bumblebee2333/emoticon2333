package com.example.emoticon.retrofit;

import com.example.emoticon.model.User;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserProtocol {
    // 登录
    @POST("v1/user/login")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    @FormUrlEncoded
    Call<User> login(@Field("email") String email, @Field("pwd") String pwd);

    // 注册
    @POST("v1/user/register")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    @FormUrlEncoded
    Call<JsonObject> register(@Field("email") String email, @Field("name") String username, @Field("pwd") String pwd);

}
