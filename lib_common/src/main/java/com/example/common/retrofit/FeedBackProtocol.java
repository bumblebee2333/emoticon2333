package com.example.common.retrofit;

import com.example.common.bean.Status;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/4/10.
 * PS: 反馈提交相关
 */
public interface FeedBackProtocol {
    /**
     * 提交反馈
     * @param contactNum 联系方式
     * @param content 反馈内容
     * @return 返回Status
     */
    @POST("v1/feedback")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    @FormUrlEncoded
    Call<Status> feedBack(@Field("contactNum") String contactNum, @Field("contactNum") String content);
}
