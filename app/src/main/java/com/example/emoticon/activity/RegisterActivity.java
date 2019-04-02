package com.example.emoticon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.emoticon.R;
import com.example.emoticon.RetroClient;
import com.example.common.base.BaseActivity;
import com.example.emoticon.retrofit.UserProtocol;
import com.example.emoticon.utils.Utils;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private EditText email, name, pwd, pwdagain;
    private Button register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
    }

    private void initViews() {
        pwd = findViewById(R.id.pwd);
        pwdagain = findViewById(R.id.pwd2);
        name = findViewById(R.id.username);
        email = findViewById(R.id.email);
        register = findViewById(R.id.register);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                if (TextUtils.isEmpty(email.getText().toString()) | TextUtils.isEmpty(name.getText().toString())
                        | TextUtils.isEmpty(pwdagain.getText().toString()) | TextUtils.isEmpty(pwd.getText().toString())) {
                    Toast.makeText(this, "不能有空内容", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!Utils.isEmail(email.getText().toString())) {
                    Toast.makeText(this, "邮箱不合法", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!pwd.getText().toString().equals(pwdagain.getText().toString())) {
                    Toast.makeText(this, "两次密码不相同", Toast.LENGTH_SHORT).show();
                    return;
                }
                register();
                break;
        }
    }

    private void register() {
        UserProtocol userProtocol = RetroClient.getRetroClient().create(UserProtocol.class);
        Call<JsonObject> call = userProtocol.register(email.getText().toString(), name.getText().toString(), pwd.getText().toString());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    JsonObject jsonObject = response.body();
                    String s = jsonObject.get("msg").getAsString();
                    Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_SHORT).show();
                    if (jsonObject.get("status").getAsInt() == 200) {

                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
