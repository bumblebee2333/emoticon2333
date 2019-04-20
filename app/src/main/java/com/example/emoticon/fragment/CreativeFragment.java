package com.example.emoticon.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.emoticon.R;
import com.example.common.RetroClient;
import com.example.emoticon.activity.EmoticonAddActivity;
import com.example.emoticon.adapter.EmoticonAdapter;
import com.example.common.bean.Emoticon;
import com.example.emoticon.retrofit.EmoticonProtocol;
import com.example.common.utils.UserManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreativeFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private List<Emoticon.DataBean> list = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    int skip = 0;
    EmoticonAdapter adapter;
    private View view;

    public static CreativeFragment newInstance(String title) {
        CreativeFragment fragment = new CreativeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.creative_fragment, container, false);
        view.findViewById(R.id.add).setOnClickListener(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        adapter = new EmoticonAdapter(list, gridLayoutManager);
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerview);


        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorBlue));
        getData(false);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // Log.i(TAG, "--------------------------------------");
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                //Log.i(TAG, "firstCompletelyVisibleItemPosition: "+firstCompletelyVisibleItemPosition);
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


        return view;
    }

    private void getData(final boolean next) {
        if (!next) skip = 0;
        Retrofit retrofit = RetroClient.getRetroClient();
        EmoticonProtocol emoticonProtocol = retrofit.create(EmoticonProtocol.class);
        final Call<Emoticon> emoticonCall = emoticonProtocol.getEmoticonList(40, skip);
        emoticonCall.enqueue(new Callback<Emoticon>() {
            @Override
            public void onResponse(Call<Emoticon> call, Response<Emoticon> response) {
                if (!next) list.clear();
                for (Emoticon.DataBean dataBean : response.body().getData()) {
                    list.add(dataBean);
                }
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                if (response.body().getData().size() == 0) {
                    skip = skip - 40;
                    Toast.makeText(getActivity(), "我也是有底线的..（T_T)", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Emoticon> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                EmoticonAddActivity.startActivity(getActivity());
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (new UserManager(getContext()).getUser() == null) {
            view.findViewById(R.id.add).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.add).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRefresh() {
        getData(false);
    }
}
