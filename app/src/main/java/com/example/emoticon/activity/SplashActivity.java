package com.example.emoticon.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.common.base.BaseActivity;
import com.example.emoticon.MainActivity;

public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态栏
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        SharedPreferences sharedPreferences = getSharedPreferences("guide", MODE_PRIVATE);
        boolean first = sharedPreferences.getBoolean("firsopen", true);
        if (first) {
            Intent intent = new Intent(this, GuideActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
