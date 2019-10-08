package com.example.common.retrofit;

import com.example.common.bean.StatusResult;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/10/5.
 * PS: 举报提交相关
 */
public interface ReportProtocol {
    /**
     * 举报表情
     * @param id 表情ID
     * @param content 举报原因
     * @return 请求状态
     */
    @POST("v1/report")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    @FormUrlEncoded
    Call<StatusResult> reportEmoticonSubmit(@Field("emoticonId") int id,@Field("content") CharSequence content);
}
