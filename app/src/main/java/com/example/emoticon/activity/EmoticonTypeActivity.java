package com.example.emoticon.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.common.widget.Toolbar;
import com.example.emoticon.R;
import com.example.common.RetroClient;
import com.example.emoticon.adapter.EmoticonAdapter;
import com.example.common.base.BaseActivity;
import com.example.common.bean.Emoticon;
import com.example.emoticon.retrofit.EmoticonProtocol;
import com.example.emoticon.widget.EmoticonLookDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EmoticonTypeActivity extends BaseActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    int id;
    String title;
    List<Emoticon.DataBean> list = new ArrayList<>();
    EmoticonAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emoticon_type);
        initView();
        getData();
    }


    private void initView() {
        id = getIntent().getIntExtra("id", 0);
        title = getIntent().getStringExtra("title");
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new EmoticonAdapter(list, gridLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new EmoticonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                EmoticonLookDialog.newInstance(list.get(position).getImg_url()).show(getSupportFragmentManager(), "emoticon_look");
            }
        });
    }

    private void getData() {
        Retrofit retrofit = RetroClient.getRetroClient();
        EmoticonProtocol emoticonProtocol = retrofit.create(EmoticonProtocol.class);
        final Call<Emoticon> emoticonCall = emoticonProtocol.getEmoticonList(id, 30, 0);
        emoticonCall.enqueue(new Callback<Emoticon>() {
            @Override
            public void onResponse(Call<Emoticon> call, Response<Emoticon> response) {
                for (Emoticon.DataBean dataBean : response.body().getData()) {
                    list.add(dataBean);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Emoticon> call, Throwable t) {
                Toast.makeText(EmoticonTypeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    public static void startActivity(Context context, int id, String title) {
        Intent intent = new Intent(context, EmoticonTypeActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }
}
