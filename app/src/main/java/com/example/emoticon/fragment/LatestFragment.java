package com.example.emoticon.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.common.RetroClient;
import com.example.common.app.ResourcesManager;
import com.example.common.bean.Emoticon;
import com.example.common.bean.StatusResult;
import com.example.common.retrofit.EmoticonProtocol;
import com.example.common.utils.HttpUtils;
import com.example.common.utils.ToastUtils;
import com.example.emoticon.R;
import com.example.emoticon.adapter.EmoticonAdapter;
import com.example.emoticon.widget.EmoticonLookDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class LatestFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private List<Emoticon> list = new ArrayList<>();
    EmoticonAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    int skip = 0;
    private RecyclerView recyclerView;

    public static LatestFragment newInstance(String title) {
        LatestFragment fragment = new LatestFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.latest_fragment, container, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        adapter = new EmoticonAdapter(list, gridLayoutManager);
        recyclerView = view.findViewById(R.id.recyclerView);

        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorBlue));
//        getData(false);
        adapter.setOnItemClickListener(new EmoticonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                EmoticonLookDialog.newInstance(list.get(position).getImgUrl()).show(getChildFragmentManager(), "emoticon_look");
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
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

        EmoticonProtocol emoticonProtocol = RetroClient.getServices(EmoticonProtocol.class);
        final Call<StatusResult<List<Emoticon>>> emoticonCall = emoticonProtocol.getEmoticonList(40, skip);
        HttpUtils.doRequest(emoticonCall, new HttpUtils.RequestFinishCallback<List<Emoticon>>() {
            @Override
            public void getRequest(StatusResult<List<Emoticon>> result) {
                swipeRefreshLayout.setRefreshing(false);
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
                    adapter.notifyDataSetChanged();
                    if (result.getData().size() == 0) {
                        skip = skip - 40;
                    } else {
                        adapter.notifyItemRangeInserted(adapter.getItemCount(), data.size());
//                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

       /* emoticonCall.enqueue(new Callback<Emoticon>() {
            @Override
            public void onResponse(@NonNull Call<Emoticon> call, @NonNull Response<Emoticon> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (null != response.body()) {
                    List<Emoticon> data = response.body().getData();
                    if (!next) list.clear();
                    list.addAll(data);
                    if (response.body().getData().size() == 0) {
                        skip = skip - 40;
                        Toast.makeText(getActivity(), "我也是有底线的..（T_T)", Toast.LENGTH_SHORT).show();
                    } else {
                        adapter.notifyItemRangeInserted(adapter.getItemCount(),data.size());
//                        adapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<Emoticon> call, @NonNull Throwable e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });*/
    }

    @Override
    public void onRefresh() {
        getData(false);
    }

    @Override
    public void onResume() {
        getData(false);
        super.onResume();
    }
}
