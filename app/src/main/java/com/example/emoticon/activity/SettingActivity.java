package com.example.emoticon.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.common.base.BaseActivity;
import com.example.common.utils.UserManager;
import com.example.emoticon.R;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        View logout = findViewById(R.id.logout);
        logout.setOnClickListener(this);
        if (UserManager.getUser() == null) {
            logout.setVisibility(View.GONE);
        }else {
            logout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logout:
                UserManager.logout();
                finish();
                break;
        }
    }
}
