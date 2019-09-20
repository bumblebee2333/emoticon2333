package com.example.common.utils;

import android.support.annotation.NonNull;

import com.example.common.R;
import com.example.common.bean.StatusResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/9/7.
 * PS:
 */
public class HttpUtils {
    public interface RequestFinishCallback<T>{
        void getRequest(StatusResult<T> result);
    }
    public static <T> void doRequest(Call<StatusResult<T>> call, final RequestFinishCallback<T> callback){
        call.enqueue(new Callback<StatusResult<T>>() {
            @Override
            public void onResponse(@NonNull Call<StatusResult<T>> call, @NonNull Response<StatusResult<T>> response) {
                StatusResult<T> result = response.body();
                if(null != callback && null != result){
                    callback.getRequest(result);
                }
            }

            @Override
            public void onFailure(@NonNull Call<StatusResult<T>> call, @NonNull Throwable t) {
                t.printStackTrace();
                ToastUtils.showToast(R.string.server_error);
                if (callback != null) {
                    callback.getRequest(null);
                }
            }
        });
    }
}
