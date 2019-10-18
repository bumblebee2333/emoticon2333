package com.example.emotion.user.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.common.base.BaseActivity;
import com.example.common.utils.ToastUtils;
import com.example.common.utils.Utils;
import com.example.common.widget.Toolbar;
import com.example.emotion.user.R;
import com.example.emotion.user.contract.UserContract;
import com.example.emotion.user.presenter.UserPresenter;

public class RegisterActivity extends BaseActivity implements View.OnClickListener, UserContract.RegisterView {
    private EditText email, name, pwd, pwdagain;
    private ProgressDialog progressDialog;
    UserContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
    }

    private void initViews() {
        presenter = new UserPresenter(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.back.setImageResource(R.drawable.close);

        pwd = findViewById(R.id.pwd);
        pwdagain = findViewById(R.id.pwd2);
        name = findViewById(R.id.username);
        email = findViewById(R.id.email);
        Button register = findViewById(R.id.register);
        register.setOnClickListener(this);
/*
        Drawable drawable1 = getResources().getDrawable(R.drawable.account_outline);
        drawable1.setBounds(0, 0, 80, 80);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        Drawable drawable2 = getResources().getDrawable(R.drawable.lock_outline);
        drawable2.setBounds(0, 0, 80, 80);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        Drawable drawable3 = getResources().getDrawable(R.drawable.email_outline);
        drawable3.setBounds(0, 0, 80, 80);//第一0是距左边距离，第二0是距上边距离，40分别是长宽

        name.setCompoundDrawables(drawable1, null, null, null);//只放左边
        email.setCompoundDrawables(drawable3, null, null, null);//只放左边
        pwd.setCompoundDrawables(drawable2, null, null, null);//只放左边
        pwdagain.setCompoundDrawables(drawable2, null, null, null);//只放左边
*/
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.register) {
            if (TextUtils.isEmpty(email.getText().toString())
                    | TextUtils.isEmpty(name.getText().toString())
                    | TextUtils.isEmpty(pwdagain.getText().toString())
                    | TextUtils.isEmpty(pwd.getText().toString())) {
                ToastUtils.showToast("不能有空内容");
                return;
            } else if (!Utils.isEmail(email.getText().toString())) {
                ToastUtils.showToast("邮箱不合法");
                return;
            } else if (!pwd.getText().toString().equals(pwdagain.getText().toString())) {
                ToastUtils.showToast("两次密码不相同");
                return;
            }
            register();
        }
        if (v.getId() == R.id.login) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void register() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在注册。。");
        progressDialog.setCancelable(false);
        progressDialog.show();
        presenter.register(
                email.getText().toString(),
                name.getText().toString(),
                pwd.getText().toString()
        );

    }

    @Override
    public void onFinish(String s) {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onError(Exception e) {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
        ToastUtils.showToast(e.getMessage());
    }

    @Override
    public void setPresenter(UserContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
