package com.example.emotion.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.common.base.BaseActivity;
import com.example.common.bean.User;
import com.example.common.utils.UserManager;
import com.example.emotion.user.R;

/**
 * 用户表情页面
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/4/20.
 * PS:
 */
public class UserEmoticonsActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_emoticon);
        getData();
    }

    private void getData() {
        User.DataBean user = new UserManager(this).getUser();

    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context, UserEmoticonsActivity.class);
        context.startActivity(intent);
    }
}
