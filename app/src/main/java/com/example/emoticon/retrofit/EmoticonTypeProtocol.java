package com.example.emoticon.retrofit;

import com.example.common.bean.EmoticonType;

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
 * PS: 表情分类
 */
public interface EmoticonTypeProtocol {

    /**
     * 获取全部分类
     * @param limit 获取几条数据
     * @param skip 跳过几条数据
     * @return 返回 EmoticonType.EmoticonTypeList
     */
    @GET("v1/type/all")
    Call<EmoticonType.EmoticonTypeList> getEmoticonTypeList(@Query("limit") int limit, @Query("skip") int skip);


    /**
     * 通过Id获取详情内容
     * @param id 分类Id
     * @param limit 获取几条数据
     * @param skip 跳过几条数据
     * @return 返回 EmoticonType
     */
    @GET("v1/type/info")
    Call<EmoticonType> getEmoticonList(@Query("id") int id, @Query("limit") int limit, @Query("skip") int skip);


    /**
     * 通过关键词获取
     * @param key 关键词
     * @param limit 获取几条数据
     * @param skip 跳过几条数据
     * @return 返回 EmoticonType.EmoticonTypeList
     */
    @GET("v1/type/key")
    Call<EmoticonType.EmoticonTypeList> getEmoticonTypeList(@Query("key") String key, @Query("limit") int limit, @Query("skip") int skip);


    /**
     * 分类添加
     * @param token 用户Token
     * @param title 分类标题
     * @param icon 分类图标链接
     * @return 返回 EmoticonType.Data
     */
    @POST("v1/type/add")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8") //添加
    @FormUrlEncoded
    Call<EmoticonType.Data> addEmoticonType(@Field("token") String token, @Field("title") String title, @Field("icon") String icon);
}
