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

import com.example.common.RetroClient;
import com.example.common.app.ResourcesManager;
import com.example.common.base.BaseActivity;
import com.example.common.bean.StatusResult;
import com.example.common.bean.User;
import com.example.common.utils.HttpUtils;
import com.example.common.utils.ToastUtils;
import com.example.common.utils.UserManager;
import com.example.common.widget.Toolbar;
import com.example.emotion.user.R;
import com.example.emotion.user.retrofit.UserProtocol;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

import it.sauronsoftware.base64.Base64;
import retrofit2.Call;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    EditText email, pwd;

    private HttpUtils.RequestFinishCallback<User> mLoginCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    private void initViews() {
        setTitle("");
        email = findViewById(R.id.email);
        pwd = findViewById(R.id.pwd);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.back.setImageResource(R.drawable.close);
        findViewById(R.id.login).setOnClickListener(this);
    }


    //密码公钥加密
    private String getSessionKey(String pwd) {
        String sessionKey = "";
        try {
            //公钥加密、私钥解密 ---- 加密
            X509EncodedKeySpec x509EncodedKeySpec2 = new X509EncodedKeySpec(it.sauronsoftware.base64.Base64.decode(RetroClient.PUBLIC_KEY.getBytes()));
            KeyFactory keyFactory2 = KeyFactory.getInstance("RSA");
            PublicKey publicKey2 = keyFactory2.generatePublic(x509EncodedKeySpec2);
            Cipher cipher2 = Cipher.getInstance("RSA");
            cipher2.init(Cipher.ENCRYPT_MODE, publicKey2);
            byte[] result2 = cipher2.doFinal(pwd.getBytes());
            sessionKey = new String(Base64.encode(result2));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sessionKey;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.register) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.login) {
            if(pwd.getText().toString().isEmpty() || email.getText().toString().isEmpty()){
                ToastUtils.showToast("不能以空的内容");
                return;
            }
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在登陆。。");
            progressDialog.show();

            String device = Build.MODEL;
            String sessionKey = getSessionKey(pwd.getText().toString());
            UserProtocol userProtocol = RetroClient.getServices(UserProtocol.class);
            Call<StatusResult<User>> call = userProtocol.login(email.getText().toString(), sessionKey, device);
            HttpUtils.doRequest(call, new HttpUtils.RequestFinishCallback<User>() {
                @Override
                public void getRequest(StatusResult<User> result) {
                    progressDialog.dismiss();
                    if (result == null) return;
                    if (!result.isSuccess()) {
                        String str = ResourcesManager.getRes().getString(R.string.request_error, result.getMsg());
                        ToastUtils.showToast(str);
                        return;
                    }
                    if (result.getData() != null) {
                        UserManager.saveUser(result.getData());
                        ToastUtils.showToast("登录成功");
                        shortcuts();
                        finish();
                    }
                }
            });
        }

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

    //启动本Activity
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
