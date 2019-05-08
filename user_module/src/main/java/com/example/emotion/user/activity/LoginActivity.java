package com.example.emotion.user.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.common.RetroClient;
import com.example.common.base.BaseActivity;
import com.example.common.bean.User;
import com.example.common.widget.Toolbar;
import com.example.emotion.user.R;
import com.example.emotion.user.retrofit.UserProtocol;
import com.example.common.utils.UserManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    EditText email,pwd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(" ");
        email = findViewById(R.id.email);
        pwd = findViewById(R.id.pwd);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.back.setImageResource(R.drawable.close);

        /*Drawable drawable1 = getResources().getDrawable(R.drawable.account_outline);
        drawable1.setBounds(0, 0, 80, 80);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        Drawable drawable2 = getResources().getDrawable(R.drawable.lock_outline);
        drawable2.setBounds(0, 0, 80, 80);//第一0是距左边距离，第二0是距上边距离，40分别是长宽

        email.setCompoundDrawables(drawable1, null, null, null);//只放左边
        pwd.setCompoundDrawables(drawable2, null, null, null);//只放左边*/

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit =  RetroClient.getRetroClient();
                UserProtocol userProtocol = retrofit.create(UserProtocol.class);
                Call<User> call = userProtocol.login(email.getText().toString(), pwd.getText().toString());
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                        if (response.body() != null) {
                            if (response.body().getStatus() == 200){
                                new UserManager(LoginActivity.this).saveUser(response.body().getData());
                                Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                                shortcuts();
                                finish();
                            }else {
                                Toast.makeText(LoginActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                        Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.register) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private void shortcuts(){
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

            for (int i = 0; i<titles.length; i++) {

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
