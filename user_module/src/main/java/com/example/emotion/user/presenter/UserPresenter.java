package com.example.emotion.user.presenter;

import android.os.Build;
import android.support.annotation.NonNull;

import com.example.common.RetroClient;
import com.example.common.app.Config;
import com.example.common.app.ResourcesManager;
import com.example.common.bean.StatusResult;
import com.example.common.bean.User;
import com.example.common.utils.HttpUtils;
import com.example.emotion.user.R;
import com.example.emotion.user.contract.UserContract;
import com.example.emotion.user.retrofit.UserProtocol;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/10/18.
 * PS: UserPresenter  MVP Presenter
 */
public class UserPresenter implements UserContract.Presenter {
    private UserContract.LoginView loginView;
    private UserContract.RegisterView registerView;

    public UserPresenter(UserContract.LoginView view) {
        this.loginView = view;
        view.setPresenter(this);
    }

    public UserPresenter(UserContract.RegisterView view) {
        this.registerView = view;
        view.setPresenter(this);
    }

    @Override
    public void login(String account, String password) {
        String device = Build.MODEL;
        UserProtocol userProtocol = RetroClient.getServices(UserProtocol.class);
        Call<StatusResult<User>> call = userProtocol.login(account, Config.getSessionKey(password), device);
        HttpUtils.doRequest(call, new HttpUtils.RequestFinishCallback<User>() {
            @Override
            public void getRequest(StatusResult<User> result) {
                if (result == null) return;
                if (!result.isSuccess()) {
                    Exception e = new Exception(result.getMsg());
                    loginView.onError(e);
                    return;
                }
                if (result.getData() != null) {
                    loginView.onFinish(result.getData());
                }
            }
        });
    }

    @Override
    public void register(String email, String name, String password) {
        UserProtocol userProtocol = RetroClient.getRetroClient().create(UserProtocol.class);
        Call<JsonObject> call = userProtocol.register(email, name, password);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    JsonObject jsonObject = response.body();
                    String s = jsonObject.get("msg").getAsString();
                    if (jsonObject.get("status").getAsInt() == 200) {
                        registerView.onFinish(s);
                    } else {
                        registerView.onError(new Exception(s));
                    }
                } else {
                    String str = ResourcesManager.getRes().getString(R.string.server_error);
                    registerView.onError(new Exception(str));
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                registerView.onError(new Exception(t.getMessage()));
            }
        });
    }

}
