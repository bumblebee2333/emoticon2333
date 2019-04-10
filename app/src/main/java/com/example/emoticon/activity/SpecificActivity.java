package com.example.emoticon.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.common.widget.Toolbar;
import com.example.emoticon.R;
import com.example.emoticon.RetroClient;
import com.example.emoticon.adapter.EmoticonAdapter;
import com.example.common.base.BaseActivity;
import com.example.emoticon.adapter.EmoticonTypeAdapter;
import com.example.emoticon.model.Emoticon;
import com.example.emoticon.model.EmoticonType;
import com.example.emoticon.retrofit.EmoticonProtocol;
import com.example.emoticon.retrofit.EmoticonTypeProtocol;
import com.example.emoticon.utils.IntentUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SpecificActivity extends BaseActivity {
    private String title;//SearchActivity传递的表情标类型名称
    private int type;//SearchActivity传递的搜索类型
    @BindView(R.id.toolBar)
    public Toolbar toolbar;

    @BindView(R.id.recyclerview_specific)
    public RecyclerView recyclerView;

    private List<Emoticon.DataBean> list = new ArrayList<>();
    private List<EmoticonType.DataBean> typeList = new ArrayList<>();

    private EmoticonAdapter adapter;
    private EmoticonTypeAdapter typeAdapter;

    boolean addType = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific);
        initIntent();
        initViews();
        getData();
    }

    /**
     * 获取Intent
     */
    private void initIntent() {
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        type = intent.getIntExtra("type",SearchActivity.EMOTICON);
    }

    private void initViews() {
        ButterKnife.bind(this);//绑定Activity
        title = IntentUtil.get().receiveActivityPassing(SpecificActivity.this);
        toolbar.setTitle(title);//设置到Toolbar中
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(gridLayoutManager);
        if (type == SearchActivity.EMOTICON) {
            adapter = new EmoticonAdapter(list, gridLayoutManager);
            recyclerView.setAdapter(adapter);

        }else {
            typeAdapter = new EmoticonTypeAdapter(typeList, gridLayoutManager);
            recyclerView.setAdapter(typeAdapter);
            typeAdapter.setOnItemClickListener(new EmoticonTypeAdapter.OnItemClickListener() {
                @Override
                public void OnItemClick(View view, int position) {
                    Intent intent = new Intent(SpecificActivity.this,EmoticonAddActivity.class);
                    intent.putExtra("title",typeList.get(position).getTitle());
                    intent.putExtra("id",typeList.get(position).getId());
                    intent.putExtra("addType",addType);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }
            });
        }

    }

    private void getData(){
        if (type == SearchActivity.EMOTICON) {
            getEmoticon();
        }else{
            getEmoticonType();
        }
    }

    private void getEmoticonType() {
        Retrofit retrofit = RetroClient.getRetroClient();
        EmoticonTypeProtocol emoticonTypeProtocol = retrofit.create(EmoticonTypeProtocol.class);
        Call<EmoticonType.EmoticonTypeList> l = emoticonTypeProtocol.getEmoticonTypeList(title, 10, 0);
        l.enqueue(new Callback<EmoticonType.EmoticonTypeList>() {
            @Override
            public void onResponse(@NonNull Call<EmoticonType.EmoticonTypeList> call, @NonNull Response<EmoticonType.EmoticonTypeList> response) {
                typeList.clear();
                if (response.body() != null) {
                    typeList.addAll(response.body().getDataList());
                    if (typeList.size() == 0){
                        addType = true;
                        EmoticonType.DataBean dataBean = new EmoticonType.DataBean();
                        dataBean.setTitle(title + "表情包");
                        dataBean.setIcon("https://shuike.oss-cn-beijing.aliyuncs.com/emoticon/material/add.jpg");
                        dataBean.setId(-1);
                        typeList.add(dataBean);
                    }else {
                        addType = false;
                    }
                    int visibility = typeList.size() == 0 ? View.VISIBLE : View.GONE;
                    //toastView.setText(R.string.search_toast);
                    //toastView.setVisibility(visibility);
                    typeAdapter.notifyDataSetChanged();
                } else {
                    int visibility = typeList.size() == 0 ? View.VISIBLE : View.GONE;
//                    toastView.setText(R.string.connection_fail);
//                    toastView.setVisibility(visibility);
                    typeAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(@NonNull Call<EmoticonType.EmoticonTypeList> call, @NonNull Throwable t) {
                typeList.clear();
                typeAdapter.notifyDataSetChanged();
//                toastView.setText(R.string.connection_fail);
//                toastView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getEmoticon() {
        Retrofit retrofit = RetroClient.getRetroClient();
        EmoticonProtocol emoticonProtocol = retrofit.create(EmoticonProtocol.class);
        final Call<Emoticon> emoticonCall = emoticonProtocol.getEmoticonList(title, 30, 0);
        emoticonCall.enqueue(new Callback<Emoticon>() {
            @Override
            public void onResponse(@NonNull Call<Emoticon> call, @NonNull Response<Emoticon> response) {
                //System.out.println(dataBean.getImg_url());
                if (response.body() != null) {
                    list.addAll(response.body().getData());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<Emoticon> call, @NonNull Throwable t) {
                Toast.makeText(SpecificActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void startActivity(Context context, String title, int type){
        Intent intent = new Intent(context, SpecificActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("type",type);
        if (type == SearchActivity.EMOTICON_TYPE) {
            ((Activity) context).startActivityForResult(intent, 1);
        }else {
            context.startActivity(intent);
        }
    }

}
