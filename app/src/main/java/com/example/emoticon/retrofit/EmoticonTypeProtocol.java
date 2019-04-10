package com.example.emoticon.retrofit;

import com.example.emoticon.model.EmoticonType;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface EmoticonTypeProtocol {

    //获取全部分类
    @GET("v1/type/all")
    Call<EmoticonType.EmoticonTypeList> getEmoticonTypeList(@Query("limit") int limit, @Query("skip") int skip);

    //获取详情内容
    @GET("v1/type/info")
    Call<EmoticonType> getEmoticonList(@Query("id") int id, @Query("limit") int limit, @Query("skip") int skip);

    //通过关键词获取
    @GET("v1/type/key")
    Call<EmoticonType.EmoticonTypeList> getEmoticonTypeList(@Query("key") String key, @Query("limit") int limit, @Query("skip") int skip);

    //提交
    @POST("v1/type/add")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8") //添加
    @FormUrlEncoded
    Call<EmoticonType.Data> addEmoticonType(@Field("token") String token, @Field("title") String title, @Field("icon") String icon);
}
