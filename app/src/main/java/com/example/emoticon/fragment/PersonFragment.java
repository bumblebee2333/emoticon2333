package com.example.emoticon.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.common.bean.User;
import com.example.common.utils.DrawableTintUtil;
import com.example.common.utils.UserManager;
import com.example.common.widget.HeadZoomScrollView;
import com.example.emoticon.R;
import com.example.emoticon.activity.SettingActivity;
import com.example.emotion.user.activity.LoginActivity;
import com.example.emotion.user.activity.UserEmoticonsActivity;
import com.example.emotion.user.activity.UserInfoActivity;

public class PersonFragment extends Fragment implements View.OnClickListener {
    View rootView;
    TextView userName, hint, title;
    ImageView userIcon;
    private ImageView bgImg;

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
        bgImg = rootView.findViewById(R.id.bgImg);
        HeadZoomScrollView headZoomScrollView = rootView.findViewById(R.id.headZoomView);
        headZoomScrollView.setZoomView(bgImg);
        rootView.findViewById(R.id.useredit).setOnClickListener(this);
        LinearLayout personMenu = rootView.findViewById(R.id.person_menu);
        personMenu.addView(personMenuItem("我的提交", R.drawable.content, getResources().getColor(R.color.colorBlue), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserManager.getUser() != null) {
                    UserEmoticonsActivity.startActivity(getContext());
                } else {
                    LoginActivity.startActivity(getContext());
                }
            }
        }));
        personMenu.addView(personMenuItem("我的收藏", R.drawable.collection, getResources().getColor(R.color.colorGold), null));
    }

    private View personMenuItem(String title, int icon, int color, View.OnClickListener onClickListener) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.person_menu, null);
        TextView tvTitle = view.findViewById(R.id.item_title);
        ImageView imgIcon = view.findViewById(R.id.item_icon);
        tvTitle.setText(title);
        Drawable originBitmap = ContextCompat.getDrawable(this.title.getContext(), icon);

        imgIcon.setImageDrawable(DrawableTintUtil.tintDrawable(originBitmap, color));
        if (onClickListener != null) {
            view.findViewById(R.id.bg).setOnClickListener(onClickListener);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.useredit) {
            if (UserManager.getUser() != null) {
                UserInfoActivity.startActivity(getContext());
            } else {
                LoginActivity.startActivity(getContext());
            }
        }
        if (id == R.id.setting) {
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        title.setText("");
        userData();
    }

    private void userData() {
        User user = UserManager.getUser();
        if (user != null) {
            userName.setText(user.getName());
            hint.setText(user.getEmail());
            String icon = user.getIcon();
            if (!TextUtils.isEmpty(icon)) Glide.with(getContext()).load(icon).into(userIcon);
            if (!TextUtils.isEmpty(icon)) Glide.with(getContext()).load(icon).into(bgImg);
            else userIcon.setImageResource(R.drawable.uicon);
        } else {
            userName.setText("未登录");
            hint.setText("点击头像登录");

            Glide.with(this).load("file:///android_asset/ubg.jpeg").into(bgImg);
            Glide.with(this).load("file:///android_asset/uicon.jpg").into(userIcon);
        }
    }
}
