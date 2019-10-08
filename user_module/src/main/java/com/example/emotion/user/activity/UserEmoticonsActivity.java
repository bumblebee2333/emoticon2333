package com.example.emotion.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.common.RetroClient;
import com.example.common.app.ResourcesManager;
import com.example.common.base.BaseActivity;
import com.example.common.bean.Emoticon;
import com.example.common.bean.StatusResult;
import com.example.common.bean.User;
import com.example.common.fragment.BottomMenuFragmentDialog;
import com.example.common.utils.HttpUtils;
import com.example.common.utils.ToastUtils;
import com.example.common.utils.UserManager;
import com.example.emotion.user.R;
import com.example.emotion.user.adapter.EmoticonAdapter;
import com.example.emotion.user.retrofit.UserProtocol;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * 用户表情页面
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/4/20.
 * PS:
 */
public class UserEmoticonsActivity extends BaseActivity {
    User user;
    RecyclerView recyclerView;
    private List<Emoticon> list;
    private EmoticonAdapter adapter;
    int skip = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_emoticon);
        init();
    }

    private void init() {
        user = UserManager.getUser();
        if (user == null) {
            ToastUtils.showToast("请登录");
            finish();
            return;
        }
        initViews();
        getData(false);
    }

    private void getData(final boolean next) {
        if (!next) skip = 0;

        UserProtocol userProtocol = RetroClient.getServices(UserProtocol.class);
        Call<StatusResult<List<Emoticon>>> emoticonCall = userProtocol.getEmoticonList(user.getId(), 40, skip);

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
                    if (!next) list.clear();
                    List<Emoticon> data = result.getData();
                    list.addAll(data);
//                    adapter.notifyDataSetChanged();

                    adapter.notifyItemRangeInserted(adapter.getItemCount(), data.size());
                    if (result.getData().size() == 0) {
                        skip = skip - 40;
                    } else {
                        adapter.notifyItemRangeInserted(adapter.getItemCount(), data.size());
//                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        list = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        adapter = new EmoticonAdapter(list, gridLayoutManager);
        adapter.setOnItemLongClickListener(new EmoticonAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final int position) {
                BottomMenuFragmentDialog bottomMenuFragmentDialog = BottomMenuFragmentDialog.newInstance(list.get(position));
//                bottomMenuFragmentDialog.setContext(UserEmoticonsActivity.this);
                bottomMenuFragmentDialog.setCallbackListener(new BottomMenuFragmentDialog.CallbackListener() {
                    @Override
                    public void onDelete(boolean value) {
                        list.remove(position);
                        adapter.notifyItemRemoved(position);
                    }

                    @Override
                    public void onReport(boolean value) {

                    }
                });
                bottomMenuFragmentDialog.show(getSupportFragmentManager(), "user");
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                if (firstCompletelyVisibleItemPosition == 0) {
                    //Log.i(TAG, "滑动到顶部");
                }

                int lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                //Log.i(TAG, "lastCompletelyVisibleItemPosition: "+lastCompletelyVisibleItemPosition);
                if (lastCompletelyVisibleItemPosition == layoutManager.getItemCount() - 1) {
                    skip = skip + 40;
                    getData(true);
                }
                //Log.i(TAG, "滑动到底部");
            }
        });
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, UserEmoticonsActivity.class);
        context.startActivity(intent);
    }

}
