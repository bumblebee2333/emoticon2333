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
import com.example.emotion.user.activity.LoginActivity;
import com.example.emoticon.activity.SettingActivity;
import com.example.common.bean.User;
import com.example.common.utils.UserManager;
import com.example.emotion.user.activity.UserEmoticonsActivity;

public class PersonFragment extends Fragment implements View.OnClickListener {
    View rootView;
    TextView userName, hint, title;
    ImageView userIcon;
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
        rootView = inflater.inflate(R.layout.person_fragment, container, false);
        initViews();
        return rootView;
    }

    private void initViews() {
        rootView.findViewById(R.id.setting).setOnClickListener(this);
        userName = rootView.findViewById(R.id.username);
        userIcon = rootView.findViewById(R.id.usericon);
        title = rootView.findViewById(R.id.title);
        hint = rootView.findViewById(R.id.hint);
        rootView.findViewById(R.id.useredit).setOnClickListener(this);
        rootView.findViewById(R.id.my_submit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.useredit) {
            if (new UserManager(getContext()).getUser() != null) {
                //new UserManager(getContext()).logout();
                //loginbt.setText("登陆");
            } else {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        }
        if (id == R.id.setting) {
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            startActivity(intent);
        }
        if (id == R.id.my_submit){
            UserEmoticonsActivity.startActivity(getContext());
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
            userName.setText(user.getName());

            hint.setVisibility(View.VISIBLE);
            hint.setText(user.getEmail());
            String icon = user.getIcon();
            if (!TextUtils.isEmpty(icon)) Glide.with(getContext()).load(icon).into(userIcon);
            else userIcon.setImageResource(R.drawable.ic_topic_place_holder);
        } else {
            userName.setText("请登录");
            hint.setText("");
            hint.setVisibility(View.GONE);
            userIcon.setImageResource(R.drawable.ic_topic_place_holder);
        }
    }
}
