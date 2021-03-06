package com.example.common.base;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public abstract class BaseFragment extends Fragment {
    private boolean isFragmentVisible;//Fragment是否可见

    private boolean isFirstLoad = true;//是否第一次开启网络加载

    private boolean isPrepared = true;//标志位 view已经完成初始化

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isFirstLoad = true;
        if(view == null){
            view = inflater.inflate(getLayoutResource(),container,false);
        }
        isPrepared = true;
        lazyLoad(view);
        return view;
    }
    //初始化数据
    public abstract void initViews(View view);
    //初始化布局id
    public abstract int getLayoutResource();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser){
        super.setUserVisibleHint(isVisibleToUser);

        if(view == null)
            return;
        if(getUserVisibleHint()){
            onVisible();
        }else {
            onInVisible();
        }
    }

    protected void onVisible(){
        isFragmentVisible = true;
        lazyLoad(view);
    }

    protected void onInVisible(){
        isFragmentVisible = false;
    }

    protected void lazyLoad(View view){
        if(isPrepared && isFragmentVisible){
            if(isFirstLoad){
                isFirstLoad = false;
                initViews(view);
                onFragmentVisibleChange(true);
            }
        }
    }
    //ture 不可见->可见 false 可见->不可见
    protected void onFragmentVisibleChange(boolean isVisible){
    }

    public void onDestroyView() {
        super.onDestroyView();
        isPrepared = false;
    }
    private void setTopPadding(View view) {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
            view.setPadding(0, result, 0, 0);
        }
    }

    private void setTopMargin(View view) {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(view.getLayoutParams());
            lp.setMargins(0, result, 0, 0);
            view.setLayoutParams(lp);
        }
    }

}
