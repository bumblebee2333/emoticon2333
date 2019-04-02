package com.example.emoticon.viewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.emoticon.R;
import com.zhouwei.mzbanner.holder.MZViewHolder;

public class BannerViewHolder implements MZViewHolder<String> {
    private ImageView imageView;
    @Override
    public View createView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.banner_item,null);
        imageView = view.findViewById(R.id.banner_image);
        return view;
    }

    @Override
    public void onBind(Context context, int i, String url) {
        Glide.with(context).load(url).into(imageView);
    }
}
