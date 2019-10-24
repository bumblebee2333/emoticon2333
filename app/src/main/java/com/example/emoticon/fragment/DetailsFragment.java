package com.example.emoticon.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.example.common.utils.ImageUtils;
import com.example.common.utils.ToastUtils;
import com.example.emoticon.R;
import com.example.emoticon.adapter.EmoticonAdapter;
import com.example.emoticon.editmodule.activity.EditActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class DetailsFragment extends Fragment {
    private List<Emoticon> list = new ArrayList<>();
    private EmoticonAdapter adapter;
    public RecyclerView recyclerView;

    public static DetailsFragment newInstance(String title, int id) {
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putInt("id", id);
        detailsFragment.setArguments(bundle);
        return detailsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_garfield, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        adapter = new EmoticonAdapter(list, gridLayoutManager);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);



        EmoticonProtocol emoticonProtocol = RetroClient.getServices(EmoticonProtocol.class);
        final Call<StatusResult<List<Emoticon>>> emoticonCall = emoticonProtocol.getEmoticonList(getArguments().getInt("id"), 30, 0);
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
//        emoticonCall.enqueue(new Callback<Emoticon>() {
//            @Override
//            public void onResponse(@NonNull Call<Emoticon> call, @NonNull Response<Emoticon> response) {
//                list.addAll(response.body().getData());
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<Emoticon> call, @NonNull Throwable t) {
//                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//        EmoticonTypeProtocol emoticonProtocol = RetroClient.getServices(EmoticonTypeProtocol.class);
//        final Call<Emoticon> emoticonCall = emoticonProtocol.getEmoticonList(getArguments().getInt("id"),30, 0);
//        emoticonCall.enqueue(new Callback<Emoticon>() {
//            @Override
//            public void onResponse(Call<Emoticon> call, Response<Emoticon> response) {
//                for (Emoticon dataBean : response.body().getData()) {
//                    list.add(dataBean);
//                }
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        adapter.notifyDataSetChanged();
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(Call<Emoticon> call, Throwable t) {
//                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });

        adapter.setOnItemClickListener(new EmoticonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), EditActivity.class);
                String image = list.get(position).getImgUrl() + ImageUtils.gifToJpg;
                intent.putExtra("picture", image);
                intent.putExtra("bitmap", adapter.getBitMap(position));
                startActivity(intent);
            }
        });
        return view;
    }
}
