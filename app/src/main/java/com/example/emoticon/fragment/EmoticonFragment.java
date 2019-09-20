package com.example.emoticon.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.emoticon.R;
import com.example.emoticon.activity.SearchActivity;
import com.example.emoticon.adapter.MainPageAdapter;

public class EmoticonFragment extends Fragment {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    public static EmoticonFragment newInstance(String title) {
        EmoticonFragment fragment = new EmoticonFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.emoticon_fragment, container, false);

        mTabLayout = view.findViewById(R.id.tabLayout);
        mViewPager = view.findViewById(R.id.viewPager);

        MainPageAdapter adapter = new MainPageAdapter(getChildFragmentManager());
        adapter.addFragment(PopularFragment.newInstance("最新"));
        adapter.addFragment(LatestFragment.newInstance("热门"));
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);//TabLayout绑定ViewPager

        View searchView = view.findViewById(R.id.top_search);
        //跳转到搜索界面
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchActivity.startActivity(getContext(), SearchActivity.EMOTICON);

            }
        });
        return view;
    }
}
