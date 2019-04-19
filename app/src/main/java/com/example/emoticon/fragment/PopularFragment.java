package com.example.emoticon.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.emoticon.R;
import com.example.common.RetroClient;
import com.example.emoticon.activity.EmoticonTypeActivity;
import com.example.emoticon.adapter.EmoticonTypeAdapter;
import com.example.common.bean.EmoticonType;
import com.example.emoticon.retrofit.EmoticonTypeProtocol;
import com.example.emoticon.viewHolder.BannerViewHolder;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PopularFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private List<EmoticonType.DataBean> list = new ArrayList<>();
    private List<String> banner_url = new ArrayList<>();
    MZBannerView mzBannerView;
    EmoticonTypeAdapter adapter;;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static PopularFragment newInstance(String title) {
        PopularFragment fragment = new PopularFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popular_fragment, container, false);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        adapter = new EmoticonTypeAdapter(list, gridLayoutManager);
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        //解决数据加载完成后, 没有停留在顶部的问题
        recyclerView.setFocusable(false);


        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorBlue));


        mzBannerView = view.findViewById(R.id.banner);
        mzBannerView.setIndicatorVisible(false);//不显示指示器
        mzBannerView.setDelayedTime(5000);//轮播时间间隔
        banner_url.add("https://shuike.oss-cn-beijing.aliyuncs.com/emoticon/banner/2.14.jpg");
        banner_url.add("https://shuike.oss-cn-beijing.aliyuncs.com/emoticon/banner/2.15.jpg");
        banner_url.add("https://shuike.oss-cn-beijing.aliyuncs.com/emoticon/banner/2.151.jpg");

        mzBannerView.setPages(banner_url, new MZHolderCreator() {
            @Override
            public MZViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        getData();

        adapter.setOnItemClickListener(new EmoticonTypeAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                EmoticonTypeActivity.startActivity(getActivity(), list.get(position).getId(), list.get(position).getTitle());
            }
        });
        mzBannerView.start();
        return view;
    }

    private void getData() {

        Retrofit retrofit = RetroClient.getRetroClient();
        EmoticonTypeProtocol emoticonProtocol = retrofit.create(EmoticonTypeProtocol.class);
        final Call<EmoticonType.EmoticonTypeList> emoticonCall = emoticonProtocol.getEmoticonTypeList(30, 0);
        emoticonCall.enqueue(new Callback<EmoticonType.EmoticonTypeList>() {
            @Override
            public void onResponse(Call<EmoticonType.EmoticonTypeList> call, Response<EmoticonType.EmoticonTypeList> response) {
                list.clear();
                for (EmoticonType.DataBean dataBean : response.body().getDataList()) {
                    list.add(dataBean);
                }
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<EmoticonType.EmoticonTypeList> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mzBannerView.pause();
    }

    @Override
    public void onRefresh() {
        getData();
    }
}
