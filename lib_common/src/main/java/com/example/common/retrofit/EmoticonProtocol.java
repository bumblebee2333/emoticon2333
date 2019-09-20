package com.example.common.retrofit;


import com.example.common.bean.Emoticon;
import com.example.common.bean.StatusResult;

import java.util.List;

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
 * Date: 2019/4/10.
 * PS: 表情请求相关
 */
public interface EmoticonProtocol {

    /**
     * 按分类id获取
     * @param id 分类Id
     * @param limit 获取几条数据
     * @param skip 跳过几条数据
     * @return 返回 Emoticon
     */
    @GET("v1/emoticon/type")
    Call<StatusResult<List<Emoticon>>> getEmoticonList(@Query("id") int id, @Query("limit") int limit, @Query("skip") int skip);


    /**
     * 按标签关键词获取
     * @param key 关键词
     * @param limit 获取几条数据
     * @param skip 跳过几条数据
     * @return 返回 Emoticon
     */
    @GET("v1/emoticon/label")
    Call<StatusResult<List<Emoticon>>> getEmoticonList(@Query("key") String key, @Query("limit") int limit, @Query("skip") int skip);


    /**
     * 获取全部表情
     * @param limit 获取几条数据
     * @param skip 跳过几条数据
     * @return 返回 Emoticon
     */
    @GET("v1/emoticon/all")
    Call<StatusResult<List<Emoticon>>> getEmoticonList(@Query("limit") int limit, @Query("skip") int skip);


    /**
     * 提交表情
     * @param token 用户Token
     * @param data 提交的数据
     * @return 返回
     */
    @POST("v1/emoticon/add")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8") //添加
    @FormUrlEncoded
    Call<StatusResult<String>> addEmoticon(@Field("token") String token, @Field("data") String data);

    /**
     *
     * @param token 用户Token
     * @param id 表情ID
     * @return　状态
     */
    @POST("v1/emoticon/delete")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8") //添加
    @FormUrlEncoded
    Call<StatusResult> deleteEmoticon(@Field("token") String token, @Field("id") int id);

}
