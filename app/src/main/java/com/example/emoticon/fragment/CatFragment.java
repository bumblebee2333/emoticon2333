package com.example.emoticon.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.emoticon.R;
import com.example.emoticon.RetroClient;
import com.example.emoticon.editmodule.activity.EditActivity;
import com.example.emoticon.adapter.EmoticonAdapter;
import com.example.common.base.BaseFragment;
import com.example.emoticon.model.Emoticon;
import com.example.emoticon.retrofit.EmoticonProtocol;
import com.example.emoticon.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CatFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private List<Emoticon.DataBean> list = new ArrayList<>();
    private EmoticonAdapter adapter;

    public static CatFragment newInstance(String title){
        CatFragment catFragment = new CatFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        catFragment.setArguments(bundle);
        return catFragment;
    }

    @Override
    public void initViews(View view) {
        recyclerView = view.findViewById(R.id.recy_cat);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragmet_cat;
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);

        if(isVisible){
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),4);
            adapter = new EmoticonAdapter(list,gridLayoutManager);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(adapter);
            Retrofit retrofit = RetroClient.getRetroClient();
            EmoticonProtocol emoticonProtocol = retrofit.create(EmoticonProtocol.class);
            final Call<Emoticon> emoticonCall = emoticonProtocol.getEmoticonList("猫咪",30, 0);
            emoticonCall.enqueue(new Callback<Emoticon>() {
                @Override
                public void onResponse(Call<Emoticon> call, Response<Emoticon> response) {
                    for (Emoticon.DataBean dataBean : response.body().getData()) {
                        list.add(dataBean);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }

                @Override
                public void onFailure(Call<Emoticon> call, Throwable t) {
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            adapter.setOnItemClickListener(new EmoticonAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(getActivity(),EditActivity.class);
                    String image = list.get(position).getImg_url()+ImageUtils.gifToJpg;
                    intent.putExtra("picture",image);
                    startActivity(intent);
                }
            });
        }
    }
}
