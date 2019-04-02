package com.example.emoticon.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.emoticon.R;
import com.example.emoticon.activity.LoginActivity;
import com.example.emoticon.activity.SettingActivity;
import com.example.emoticon.model.User;
import com.example.emoticon.utils.UserManager;

public class PersonFragment extends Fragment implements View.OnClickListener {
    View rootview;
    TextView username, hint, title;
    ImageView usericon;
    //Button loginbt;

    public static PersonFragment newInstance(String title) {
        PersonFragment fragment = new PersonFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.person_fragment, container, false);
        initViews();
        //((BaseActivity)getActivity()).setStatusBarFullTransparent();
        return rootview;
    }

    private void initViews() {
        rootview.findViewById(R.id.setting).setOnClickListener(this);
        username = rootview.findViewById(R.id.username);
        usericon = rootview.findViewById(R.id.usericon);
        title = rootview.findViewById(R.id.title);
        hint = rootview.findViewById(R.id.hint);
        rootview.findViewById(R.id.useredit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.useredit) {
            if (new UserManager(getContext()).getUser() != null) {
                //new UserManager(getContext()).logout();
                //loginbt.setText("登陆");
            } else {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        }
        if (v.getId() == R.id.setting) {
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String t = new UserManager(getContext()).getUser() != null ? "我的" : "请登录";
        title.setText(t);
        //onResume();
        userData();
    }

    private void userData() {
        User.DataBean user = new UserManager(getContext()).getUser();
        if (user != null) {
            username.setText(user.getName());

            hint.setVisibility(View.VISIBLE);
            hint.setText(user.getEmail());
            String icon = user.getIcon();
            if (!TextUtils.isEmpty(icon)) Glide.with(getContext()).load(icon).into(usericon);
            else usericon.setImageResource(R.drawable.ic_topic_place_holder);
        } else {
            username.setText("请登录");
            hint.setText("");
            hint.setVisibility(View.GONE);
            usericon.setImageResource(R.drawable.ic_topic_place_holder);
        }
    }
}
