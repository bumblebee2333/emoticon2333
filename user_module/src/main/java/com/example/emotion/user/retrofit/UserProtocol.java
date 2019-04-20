package com.example.emotion.user.retrofit;

import com.example.common.bean.User;
import com.example.common.bean.Emoticon;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/4/11.
 * PS: 用户相关网络请求
 */
public interface UserProtocol {

    /**
     * 登录请求
     * @param email 邮箱
     * @param pwd 密码
     * @return 返回User
     */
    @POST("v1/user/login")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    @FormUrlEncoded
    Call<User> login(@Field("email") String email, @Field("pwd") String pwd);


    /**
     * 注册请求
     * @param email 注册邮箱
     * @param username 注册用户名
     * @param pwd 密码
     * @return 返回JsonObject
     */
    @POST("v1/user/register")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    @FormUrlEncoded
    Call<JsonObject> register(@Field("email") String email, @Field("name") String username, @Field("pwd") String pwd);

    /**
     * 按分类id获取
     * @param id 分类Id
     * @param limit 获取几条数据
     * @param skip 跳过几条数据
     * @return 返回 Emoticon
     */
    @GET("v1/user/emoticon")
    Call<Emoticon> getEmoticonList(@Query("uid") int id, @Query("limit") int limit, @Query("skip") int skip);

}
