package com.example.emoticon.retrofit;


import com.example.common.bean.Emoticon;
import com.example.common.bean.Status;

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
    Call<Emoticon> getEmoticonList(@Query("id") int id, @Query("limit") int limit, @Query("skip") int skip);


    /**
     * 按标签关键词获取
     * @param key 关键词
     * @param limit 获取几条数据
     * @param skip 跳过几条数据
     * @return 返回 Emoticon
     */
    @GET("v1/emoticon/label")
    Call<Emoticon> getEmoticonList(@Query("key") String key, @Query("limit") int limit, @Query("skip") int skip);


    /**
     * 获取全部表情
     * @param limit 获取几条数据
     * @param skip 跳过几条数据
     * @return 返回 Emoticon
     */
    @GET("v1/emoticon/all")
    Call<Emoticon> getEmoticonList(@Query("limit") int limit, @Query("skip") int skip);


    /**
     * 提交表情
     * @param token 用户Token
     * @param typeId 分类Id
     * @param url 图片地址
     * @param label 标签
     * @return 返回 Status
     */
    @POST("v1/emoticon/add")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8") //添加
    @FormUrlEncoded
    Call<Status> addEmoticon(@Field("token") String token, @Field("typeId") int typeId, @Field("url") String url, @Field("label") String label);

}
