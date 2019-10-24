package com.example.emoticon.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.common.RetroClient;
import com.example.common.app.ResourcesManager;
import com.example.common.bean.Emoticon;
import com.example.common.bean.StatusResult;
import com.example.common.retrofit.EmoticonProtocol;
import com.example.common.utils.HttpUtils;
import com.example.common.utils.ToastUtils;
import com.example.common.manager.UserManager;
import com.example.common.widget.Toolbar;
import com.example.emoticon.R;
import com.example.emoticon.activity.EmoticonAddActivity;
import com.example.emoticon.adapter.EmoticonAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class CreativeFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private List<Emoticon> list = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    int skip = 0;
    EmoticonAdapter adapter;
    private Toolbar toolbar;

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
        View view = inflater.inflate(R.layout.creative_fragment, container, false);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        adapter = new EmoticonAdapter(list, gridLayoutManager);
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.right1.setImageResource(R.drawable.add_black);
        toolbar.right1.setOnClickListener(this);
        toolbar.setTitle("创意工坊");

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorBlue));
        getData(false);

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
                //Log.i(TAG, "lastCompletelyVisibleItemPosition: "+lastCompletelyVisibleItemPosition);
                if (lastCompletelyVisibleItemPosition == layoutManager.getItemCount() - 1) {
                    skip = skip + 40;
                    getData(true);
                }
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
                    list.addAll(result.getData());
                    adapter.notifyDataSetChanged();
                    if (result.getData().size() == 0) {
                        skip = skip - 40;
                        ToastUtils.showToast("我也是有底线的..（T_T)");
                    }
                }
            }
        });
        /*emoticonCall.enqueue(new Callback<Emoticon>() {
            @Override
            public void onResponse(Call<Emoticon> call, Response<Emoticon> response) {
                if (!next) list.clear();
                for (Emoticon dataBean : response.body().getData()) {
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
        });*/
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.right1) {
            EmoticonAddActivity.startActivity(getActivity());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UserManager.getUser() == null) {
            toolbar.setRightButtonOneShow(false);
        } else {
            toolbar.setRightButtonOneShow(true);
        }
    }

    @Override
    public void onRefresh() {
        getData(false);
    }
}
