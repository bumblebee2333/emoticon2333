package com.example.emotion.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.common.base.BaseActivity;
import com.example.common.bean.User;
import com.example.common.utils.UserManager;
import com.example.emotion.user.R;

/**
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/4/20.
 * PS:
 */
public class UserInfoActivity extends BaseActivity {
    User.DataBean user;
    TextView userName,userEmail;
    ImageView userProfile;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        getData();
    }

    private void getData() {
        user = new UserManager(this).getUser();
        if (user == null){
            Toast.makeText(this, "请登录", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        initViews();
        userEmail.setText(user.getEmail());
        userName.setText(user.getName());
        Glide.with(this).load(user.getIcon()).into(userProfile);
    }

    private void initViews() {
        userProfile = findViewById(R.id.user_profile);
        userName = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.user_email);
    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context, UserInfoActivity.class);
        context.startActivity(intent);
    }
}
