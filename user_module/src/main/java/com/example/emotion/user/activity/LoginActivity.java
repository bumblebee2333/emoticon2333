package com.example.emotion.user.activity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.example.common.app.ResourcesManager;
import com.example.common.base.BaseActivity;
import com.example.common.bean.User;
import com.example.common.utils.ToastUtils;
import com.example.common.manager.UserManager;
import com.example.common.widget.Toolbar;
import com.example.emotion.user.R;
import com.example.emotion.user.contract.UserContract;
import com.example.emotion.user.presenter.UserPresenter;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity implements View.OnClickListener, UserContract.LoginView {
    private EditText email, pwd;
    private UserContract.Presenter presenter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    private void initViews() {
        presenter = new UserPresenter(this);
        setTitle("");
        email = findViewById(R.id.email);
        pwd = findViewById(R.id.pwd);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.back.setImageResource(R.drawable.close);
        findViewById(R.id.login).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.register) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.login) {
            if (pwd.getText().toString().isEmpty() || email.getText().toString().isEmpty()) {
                ToastUtils.showToast("不能有空的内容");
                return;
            }
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在登陆。。");
            progressDialog.show();
            presenter.login(email.getText().toString(), pwd.getText().toString());
        }

    }

    //启动本Activity
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onFinish(User user) {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
        UserManager.saveUser(user);
        ToastUtils.showToast("登录成功");
        shortcuts();
        finish();
    }

    @Override
    public void onError(Exception e) {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
        String str = ResourcesManager.getRes().getString(R.string.request_error, e.getMessage());
        ToastUtils.showToast(str);
    }

    @Override
    public void setPresenter(UserContract.Presenter presenter) {
        this.presenter = presenter;
    }


    //桌面图标长按 ShortCuts
    private void shortcuts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            Intent intent1 = new Intent(LoginActivity.this, UserEmoticonsActivity.class);
            intent1.setAction(Intent.ACTION_VIEW);

            Intent intent2 = new Intent();
            intent2.setComponent(new ComponentName("com.example.emoticon", "com.example.emoticon.activity.EmoticonAddActivity"));
            intent2.setAction(Intent.ACTION_VIEW);

            String[] titles = {"我的表情", "添加表情"};
            String[] ids = {"person", "addIcon"};
            int[] icons = {R.drawable.person_shortcuts, R.drawable.add_shortcuts};
            Intent[] intents = {intent1, intent2};
            List<ShortcutInfo> list = new ArrayList<>();
            ShortcutManager shortcutsManager = getSystemService(ShortcutManager.class);
            for (int i = 0; i < titles.length; i++) {
                ShortcutInfo.Builder infoBuild = new ShortcutInfo.Builder(LoginActivity.this, ids[i]);
                infoBuild.setShortLabel(titles[i]);
                infoBuild.setLongLabel(titles[i]);
                infoBuild.setIcon(Icon.createWithResource(LoginActivity.this, icons[i]));
                infoBuild.setIntent(intents[i]);
                ShortcutInfo shortcutInfo = infoBuild.build();
                list.add(shortcutInfo);
            }
            shortcutsManager.setDynamicShortcuts(list);
        }
    }

}
