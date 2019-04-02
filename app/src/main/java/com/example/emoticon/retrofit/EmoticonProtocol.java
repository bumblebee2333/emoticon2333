package com.example.emoticon.retrofit;

import android.app.AlertDialog;

import com.example.emoticon.model.Emoticon;
import com.example.emoticon.model.Status;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EmoticonProtocol {

    //按分类id获取
    @GET("v1/emoticon/type")
    Call<Emoticon> getEmoticonList(@Query("id") int id, @Query("limit") int limit, @Query("skip") int skip);

    //按标签获取
    @GET("v1/emoticon/label")
    Call<Emoticon> getEmoticonList(@Query("key") String key, @Query("limit") int limit, @Query("skip") int skip);

    //获取全部表情
    @GET("v1/emoticon/all")
    Call<Emoticon> getEmoticonList(@Query("limit") int limit, @Query("skip") int skip);

    //提交
    @POST("v1/emoticon/add")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8") //添加
    @FormUrlEncoded
    Call<Status> addEmoticon(@Field("token") String token, @Field("typeid") int typeid, @Field("url") String url, @Field("label") String label);

}
