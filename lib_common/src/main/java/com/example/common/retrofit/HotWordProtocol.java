package com.example.common.retrofit;

import com.example.common.bean.StatusResult;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/9/27.
 * PS:
 */
public interface HotWordProtocol {
    @GET("v1/search/keyword")
    Call<StatusResult<String[]>> getHotSearchWord();
}
