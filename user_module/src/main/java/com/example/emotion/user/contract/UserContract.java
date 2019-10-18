package com.example.emotion.user.contract;

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
        void onFinish(String s);
        void onError(Exception e);
    }

    interface LoginView extends BaseView<UserContract.Presenter> {
        void onFinish(User user);
        void onError(Exception e);
    }

    /**
     *
     */
    interface Presenter extends BasePresenter<User> {
        /**
         * 登陆
         *
         * @param account  用户名或邮箱
         * @param password 密码
         */
        void login(String account, String password);

        /**
         * 注册
         *
         * @param email    邮箱
         * @param name     用户名
         * @param password 密码
         */
        void register(String email, String name, String password);
    }
}
