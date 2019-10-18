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

import com.example.common.app.ResourcesManager;
import com.example.common.base.BaseActivity;
import com.example.common.bean.Emoticon;
import com.example.common.bean.User;
import com.example.common.fragment.BottomMenuFragmentDialog;
import com.example.common.manager.UserManager;
import com.example.common.utils.ToastUtils;
import com.example.emotion.user.R;
import com.example.emotion.user.adapter.EmoticonAdapter;
import com.example.emotion.user.contract.UserEmoticonsContract;
import com.example.emotion.user.presenter.UserEmoticonsPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户表情页面
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/4/20.
 * PS:
 */
public class UserEmoticonsActivity extends BaseActivity implements UserEmoticonsContract.EmoticonView {
    User user;
    RecyclerView recyclerView;
    private List<Emoticon> list;
    private EmoticonAdapter adapter;
    private UserEmoticonsContract.Presenter presenter;
    int skip = 0;
    private boolean next = false;

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
        getData();
    }


    private void initViews() {
        presenter = new UserEmoticonsPresenter(this);
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
                if (layoutManager == null) return;
                int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                if (firstCompletelyVisibleItemPosition == 0) {
                    //Log.i(TAG, "滑动到顶部");
                }
                int lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                if (lastCompletelyVisibleItemPosition == layoutManager.getItemCount() - 1) {
                    skip = skip + 40;
                    next = true;
                    getData();
                }
            }
        });
    }

    private void getData() {
        if (!next) skip = 0;
        presenter.loadData(user.getId(), skip);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, UserEmoticonsActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onError(Exception e) {
        String str = ResourcesManager.getRes().getString(com.example.emotion.user.R.string.request_error, e.getMessage());
        ToastUtils.showToast(str);
    }

    @Override
    public void onFinish(List<Emoticon> emoticonList) {
        if (!next) list.clear();
        list.addAll(emoticonList);
        adapter.notifyItemRangeInserted(adapter.getItemCount(), emoticonList.size());
        if (emoticonList.size() == 0) {
            skip = skip - 40;
        } else {
            adapter.notifyItemRangeInserted(adapter.getItemCount(), emoticonList.size());
        }
    }

    @Override
    public void setPresenter(UserEmoticonsContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
