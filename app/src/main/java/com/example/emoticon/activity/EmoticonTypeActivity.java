package com.example.emoticon.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.example.common.RetroClient;
import com.example.common.app.ResourcesManager;
import com.example.common.base.BaseActivity;
import com.example.common.bean.Emoticon;
import com.example.common.bean.StatusResult;
import com.example.common.retrofit.EmoticonProtocol;
import com.example.common.utils.HttpUtils;
import com.example.common.utils.ToastUtils;
import com.example.common.widget.Toolbar;
import com.example.emoticon.R;
import com.example.emoticon.adapter.EmoticonAdapter;
import com.example.emoticon.widget.EmoticonLookDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class EmoticonTypeActivity extends BaseActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    int id;
    String title;
    List<Emoticon> list = new ArrayList<>();
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
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new EmoticonAdapter(list, gridLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new EmoticonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                EmoticonLookDialog.newInstance(list.get(position).getImgUrl()).show(getSupportFragmentManager(), "emoticon_look");
            }
        });
    }

    private void getData() {

        EmoticonProtocol emoticonProtocol = RetroClient.getServices(EmoticonProtocol.class);
        Call<StatusResult<List<Emoticon>>> emoticonCall = emoticonProtocol.getEmoticonList(id, 30, 0);
        HttpUtils.doRequest(emoticonCall, new HttpUtils.RequestFinishCallback<List<Emoticon>>() {
            @Override
            public void getRequest(StatusResult<List<Emoticon>> result) {
                if (result == null) return;
                if (!result.isSuccess()) {
                    String str = ResourcesManager.getRes().getString(com.example.emotion.user.R.string.request_error, result.getMsg());
                    ToastUtils.showToast(str);
                    return;
                }
                if (result.getData() != null) {
                    list.addAll(result.getData());
                    adapter.notifyDataSetChanged();
                }
            }
        });
        /*emoticonCall.enqueue(new Callback<StatusResult<List<Emoticon>>>() {
            @Override
            public void onResponse(Call<StatusResult<List<Emoticon>>> call, Response<StatusResult<List<Emoticon>>> response) {
                list.addAll(response.body().getData());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<StatusResult<List<Emoticon>>> call, Throwable t) {
                Toast.makeText(EmoticonTypeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
*/
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
