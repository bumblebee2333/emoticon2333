package com.example.emoticon.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.RetroClient;
import com.example.common.app.ResourcesManager;
import com.example.common.base.BaseActivity;
import com.example.common.bean.Emoticon;
import com.example.common.bean.EmoticonType;
import com.example.common.bean.StatusResult;
import com.example.common.retrofit.EmoticonProtocol;
import com.example.common.retrofit.EmoticonTypeProtocol;
import com.example.common.utils.HttpUtils;
import com.example.common.utils.ToastUtils;
import com.example.common.widget.Topbar;
import com.example.emoticon.R;
import com.example.emoticon.adapter.EmoticonAdapter;
import com.example.emoticon.adapter.EmoticonTypeAdapter;
import com.example.emoticon.utils.IntentUtil;
import com.example.emoticon.widget.EmoticonLookDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpecificActivity extends BaseActivity {
    private String title;//SearchActivity传递的表情标类型名称
    private int type;//SearchActivity传递的搜索类型
    @BindView(R.id.toolbar)
    public Topbar toolbar;

    @BindView(R.id.recyclerview_specific)
    public RecyclerView recyclerView;

    private List<Emoticon> list = new ArrayList<>();
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
            adapter.setOnItemClickListener((view, position) -> {
                EmoticonLookDialog.newInstance(list.get(position).getImgUrl()).show(getSupportFragmentManager(), "emoticon_look");
            });
        }else {
            typeAdapter = new EmoticonTypeAdapter(typeList, gridLayoutManager);
            recyclerView.setAdapter(typeAdapter);
            typeAdapter.setOnItemClickListener((view, position) -> {
                Intent intent = new Intent(SpecificActivity.this,EmoticonAddActivity.class);
                intent.putExtra("title",typeList.get(position).getTitle());
                intent.putExtra("id",typeList.get(position).getId());
                intent.putExtra("addType",addType);
                setResult(Activity.RESULT_OK,intent);
                finish();
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

        EmoticonTypeProtocol emoticonTypeProtocol = RetroClient.getServices(EmoticonTypeProtocol.class);
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

        EmoticonProtocol emoticonProtocol = RetroClient.getServices(EmoticonProtocol.class);
        final Call<StatusResult<List<Emoticon>>> emoticonCall = emoticonProtocol.getEmoticonList(title, 30, 0);

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
/*        emoticonCall.enqueue(new Callback<Emoticon>() {
            @Override
            public void onResponse(@NonNull Call<Emoticon> call, @NonNull Response<Emoticon> response) {
                //System.out.println(dataBean.getImgUrl());
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
        });*/
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
