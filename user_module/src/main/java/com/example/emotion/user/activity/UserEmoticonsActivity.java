package com.example.emotion.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.common.RetroClient;
import com.example.common.base.BaseActivity;
import com.example.common.bean.Emoticon;
import com.example.common.bean.User;
import com.example.common.utils.UserManager;
import com.example.emotion.user.R;
import com.example.emotion.user.adapter.EmoticonAdapter;
import com.example.emotion.user.retrofit.UserProtocol;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 用户表情页面
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/4/20.
 * PS:
 */
public class UserEmoticonsActivity extends BaseActivity {
    User.DataBean user;
    RecyclerView recyclerView;
    private List<Emoticon.DataBean> list;
    private EmoticonAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_emoticon);
        init();
    }

    private void init() {
        user = new UserManager(this).getUser();
        if (user == null){
            Toast.makeText(this, "请登录", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        initViews();
        getData();
    }

    private void getData() {
       Retrofit retrofit = RetroClient.getRetroClient();
       UserProtocol userProtocol = retrofit.create(UserProtocol.class);
        Call<Emoticon> emoticonCall = userProtocol.getEmoticonList(user.getId(), 40, 0);
        emoticonCall.enqueue(new Callback<Emoticon>() {
            @Override
            public void onResponse(@NonNull Call<Emoticon> call, @NonNull Response<Emoticon> response) {
//                if (!next) list.clear();
                if (response.body().getData() == null){
                    Toast.makeText(UserEmoticonsActivity.this, "空", Toast.LENGTH_SHORT).show();
                    return;
                }
                list.addAll(response.body().getData());
                adapter.notifyDataSetChanged();
//                swipeRefreshLayout.setRefreshing(false);
//                if (response.body().getData().size() == 0) {
//                    skip = skip - 40;
//                    Toast.makeText(getActivity(), "我也是有底线的..（T_T)", Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onFailure(@NonNull Call<Emoticon> call, @NonNull Throwable t) {
                Toast.makeText(UserEmoticonsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        list = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        adapter = new EmoticonAdapter(list, gridLayoutManager);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context, UserEmoticonsActivity.class);
        context.startActivity(intent);
    }
}
