package com.example.emoticon.adapter;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.emoticon.R;
import com.example.common.bean.Emoticon;
import com.example.common.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

public class EmoticonAdapter extends RecyclerView.Adapter<EmoticonAdapter.ViewHolder> {
    private List<Emoticon.DataBean> list = new ArrayList<>();
    private List<Bitmap> bitmaps = new ArrayList<>();
    GridLayoutManager gridLayoutManager;
    private OnItemClickListener mOnItemClickListener;


    public EmoticonAdapter(List<Emoticon.DataBean> list, GridLayoutManager gridLayoutManager) {
        this.list = list;
        this.gridLayoutManager = gridLayoutManager;
    }

    @NonNull
    @Override
    public EmoticonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.emoticon_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        Glide.with(viewHolder.img.getContext()).asBitmap().load(list.get(i).getImg_url() + ImageUtils.gifToJpg).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                viewHolder.img.setImageBitmap(resource);
                bitmaps.add(resource);
            }
        });

        viewHolder.tv.setVisibility(View.GONE);
        if (list.get(i).getImg_url().endsWith(".gif")) {
            viewHolder.gif.setVisibility(View.VISIBLE);
        } else {
            viewHolder.gif.setVisibility(View.GONE);
        }
        ViewGroup.LayoutParams parm = viewHolder.img.getLayoutParams(); //获取图片的的LayoutParams
        parm.height = gridLayoutManager.getWidth() / gridLayoutManager.getSpanCount()
                - 2 * viewHolder.img.getPaddingLeft() - 2 * ((ViewGroup.MarginLayoutParams) parm).leftMargin;//设置图片高度等于宽度

        if (mOnItemClickListener != null) {
            viewHolder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(viewHolder.img, pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv, gif;
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
            gif = itemView.findViewById(R.id.gif);
            img = itemView.findViewById(R.id.pic);
        }
    }

    public Bitmap getBitMap(int position) {
        return bitmaps.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
