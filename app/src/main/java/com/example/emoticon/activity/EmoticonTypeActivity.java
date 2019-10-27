package com.example.emoticon.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.common.RetroClient;
import com.example.common.app.ResourcesManager;
import com.example.common.base.BaseActivity;
import com.example.common.bean.Emoticon;
import com.example.common.bean.StatusResult;
import com.example.common.retrofit.EmoticonProtocol;
import com.example.common.utils.HttpUtils;
import com.example.common.utils.ToastUtils;
import com.example.common.widget.Topbar;
import com.example.emoticon.R;
import com.example.emoticon.adapter.EmoticonAdapter;
import com.example.emoticon.widget.EmoticonLookDialog;
import com.example.qrcode.BitmapUtils;
import com.example.qrcode.QrCodeUtils;
import com.example.qrcode.activity.ShareActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class EmoticonTypeActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private Topbar toolbar;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int id;
    private String title;
    private    List<Emoticon> list = new ArrayList<>();
    private EmoticonAdapter adapter;

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
        toolbar.setRightButtonOneShow(true);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorBlue));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new EmoticonAdapter(list, gridLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((v, position) -> {
            EmoticonLookDialog.newInstance(list.get(position).getImgUrl()).show(getSupportFragmentManager(), "emoticon_look");
        });

        toolbar.right1.setOnClickListener(v -> {
//            int screenWidth = ScreenUtils.getScreenWidth(v.getContext());
//            int screenHeight = ScreenUtils.getScreenHeight(v.getContext());
//            int width = screenWidth < screenHeight ? screenWidth : screenHeight;
            Bitmap bitmap = QrCodeUtils.createPoster(id + title);
            String path = BitmapUtils.saveBitmapToCacheDir(bitmap);
            if (path != null) {
                ShareActivity.Companion.startActivity(this, path);
            }
//            EmoticonLookDialog.newInstance(bitmap).show(getSupportFragmentManager(), "emoticon_look");
        });
    }

    private void getData() {
        swipeRefreshLayout.setRefreshing(true);
        EmoticonProtocol emoticonProtocol = RetroClient.getServices(EmoticonProtocol.class);
        Call<StatusResult<List<Emoticon>>> emoticonCall = emoticonProtocol.getEmoticonList(id, 30, 0);
        HttpUtils.doRequest(emoticonCall, result -> {
            swipeRefreshLayout.setRefreshing(false);
            if (result == null) return;
            if (!result.isSuccess()) {
                String str = ResourcesManager.getRes().getString(com.example.emotion.user.R.string.request_error, result.getMsg());
                ToastUtils.showToast(str);
                return;
            }
            if (result.getData() != null) {
                list.clear();
                list.addAll(result.getData());
                adapter.notifyDataSetChanged();
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
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    public static void startActivity(Context context, int id, String title) {
        Intent intent = new Intent(context, EmoticonTypeActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    public void onRefresh() {
        getData();
    }
}
