package com.example.emotion.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.common.app.ResourcesManager;
import com.example.common.base.BaseActivity;
import com.example.common.bean.User;
import com.example.common.utils.ToastUtils;
import com.example.common.utils.UserManager;
import com.example.emotion.user.R;

/**
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/4/20.
 * PS:
 */
public class UserInfoActivity extends BaseActivity {
    User user;
    TextView userName,userEmail;
    ImageView userProfile;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        getData();
    }

    private void getData() {
        user = UserManager.getUser();

        if (user == null){
            ToastUtils.showToast("请登录");
            finish();
            return;
        }
        initViews();
//        userEmail.setText(user.getEmail());
//        userName.setText(user.getName());
        Glide.with(this).load(user.getIcon()).into(userProfile);
    }

    private void initViews() {
        userProfile = findViewById(R.id.user_profile);
        LinearLayout linearLayout = findViewById(R.id.content);
        linearLayout.addView(item(ResourcesManager.getRes().getString(R.string.user_name),user.getName(),null));
        linearLayout.addView(item(ResourcesManager.getRes().getString(R.string.email),user.getEmail(),null));
        linearLayout.addView(item(ResourcesManager.getRes().getString(R.string.register_date),user.getCreateTime(),null));
//        userName = findViewById(R.id.user_name);
//        userEmail = findViewById(R.id.user_email);
    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context, UserInfoActivity.class);
        context.startActivity(intent);
    }

    public View item(CharSequence title, CharSequence value, View.OnClickListener listener){
        View view = LayoutInflater.from(this).inflate(R.layout.user_info_item, null, false);
        if(listener!=null) view.setOnClickListener(listener);
        TextView tv1 = view.findViewById(R.id.title);
        TextView tv2 = view.findViewById(R.id.value);
        tv1.setText(title);
        tv2.setText(value);
        return view;
    }
}
