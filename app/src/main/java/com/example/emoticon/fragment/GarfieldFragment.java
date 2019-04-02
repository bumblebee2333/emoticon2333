package com.example.emoticon.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.emoticon.R;
import com.example.emoticon.RetroClient;
import com.example.emoticon.editmodule.activity.EditActivity;
import com.example.emoticon.adapter.EmoticonAdapter;
import com.example.emoticon.model.Emoticon;
import com.example.emoticon.retrofit.EmoticonProtocol;
import com.example.emoticon.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GarfieldFragment extends Fragment {
    private List<Emoticon.DataBean> list = new ArrayList<>();
    private EmoticonAdapter adapter;
    public RecyclerView recyclerView;

    public static GarfieldFragment newInstance(String title,int id){
        GarfieldFragment garfieldFragment = new GarfieldFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        bundle.putInt("id",id);
        garfieldFragment.setArguments(bundle);
        return garfieldFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_garfield,container,false);

        recyclerView = view.findViewById(R.id.recy_garfield);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),4);
        adapter = new EmoticonAdapter(list,gridLayoutManager);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);


        Retrofit retrofit = RetroClient.getRetroClient();
        EmoticonProtocol emoticonProtocol = retrofit.create(EmoticonProtocol.class);
        final Call<Emoticon> emoticonCall = emoticonProtocol.getEmoticonList(getArguments().getInt("id"), 30, 0);
        emoticonCall.enqueue(new Callback<Emoticon>() {
            @Override
            public void onResponse(Call<Emoticon> call, Response<Emoticon> response) {
                for (Emoticon.DataBean dataBean : response.body().getData()) {
                    list.add(dataBean);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Emoticon> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
//
//        Retrofit retrofit = RetroClient.getRetroClient();
//        EmoticonTypeProtocol emoticonProtocol = retrofit.create(EmoticonTypeProtocol.class);
//        final Call<Emoticon> emoticonCall = emoticonProtocol.getEmoticonList(getArguments().getInt("id"),30, 0);
//        emoticonCall.enqueue(new Callback<Emoticon>() {
//            @Override
//            public void onResponse(Call<Emoticon> call, Response<Emoticon> response) {
//                for (Emoticon.DataBean dataBean : response.body().getData()) {
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
               Intent intent = new Intent(getActivity(),EditActivity.class);
                String image = list.get(position).getImg_url()+ImageUtils.gifToJpg;
                intent.putExtra("picture",image);
                startActivity(intent);
            }
        });
        return view;
    }
}
