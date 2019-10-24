package com.example.emotion.user.contract;

import androidx.annotation.NonNull;

import com.example.common.base.BasePresenter;
import com.example.common.base.BaseView;
import com.example.common.bean.User;

/**
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/10/18.
 * PS:
 */
public interface UserContract {
    interface RegisterView extends BaseView<UserContract.Presenter> {
        /**
         * 获取成功回调
         *
         * @param s 获取的内容
         */
        void onFinish(@NonNull String s);

        /**
         * 获取失败回调
         *
         * @param e 错误内容
         */
        void onError(@NonNull Exception e);
    }

    interface LoginView extends BaseView<UserContract.Presenter> {
        /**
         * 获取成功回调
         *
         * @param user 获取的用户信息
         */
        void onFinish(@NonNull User user);

        /**
         * 获取失败回调
         *
         * @param e 错误内容
         */
        void onError(@NonNull Exception e);
    }

    /**
     * UserPresenter
     */
    interface Presenter extends BasePresenter<User> {
        /**
         * 登陆
         *
         * @param account  用户名或邮箱
         * @param password 密码
         */
        void login(@NonNull String account, @NonNull String password);

        /**
         * 注册
         *
         * @param email    邮箱
         * @param name     用户名
         * @param password 密码
         */
        void register(@NonNull String email, @NonNull String name, @NonNull String password);
    }
}
