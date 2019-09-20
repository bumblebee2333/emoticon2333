package com.example.emoticon.adapter;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.common.bean.Emoticon;
import com.example.common.fragment.BottomMenuFragmentDialog;
import com.example.common.utils.ImageUtils;
import com.example.emoticon.R;

import java.util.ArrayList;
import java.util.List;

public class EmoticonAdapter extends RecyclerView.Adapter<EmoticonAdapter.ViewHolder> {
    private List<Emoticon> list;
    private List<Bitmap> bitmaps = new ArrayList<>();
    private GridLayoutManager gridLayoutManager;
    private OnItemClickListener mOnItemClickListener;


    public EmoticonAdapter(List<Emoticon> list, GridLayoutManager gridLayoutManager) {
        this.list = list;
        this.gridLayoutManager = gridLayoutManager;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public EmoticonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.emoticon_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, @NonNull int position) {
        viewHolder.tv.setVisibility(View.GONE);
        if (list.get(position).getImgUrl().endsWith(".gif")) {
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
        if (!list.get(position).getImgUrl().equals(viewHolder.img.getTag(R.id.pic))) {
            Glide.with(viewHolder.img.getContext()).clear(viewHolder.itemView);
            // 加载图片
            Glide.with(viewHolder.img.getContext()).asBitmap().load(list.get(position).getImgUrl() + ImageUtils.gifToJpg).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    viewHolder.img.setImageBitmap(resource);
                    bitmaps.add(resource);
                }
            });
            viewHolder.img.setTag(R.id.pic, list.get(position).getImgUrl());
        }
        final int i = position;
        viewHolder.img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int pos = viewHolder.getLayoutPosition();
                v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                BottomMenuFragmentDialog bottomMenuFragmentDialog = BottomMenuFragmentDialog.newInstance(list.get(i));
                AppCompatActivity a = ((AppCompatActivity) (viewHolder.img.getContext()));
                bottomMenuFragmentDialog.show(a.getSupportFragmentManager(),"emoticon");
//                mOnItemLongClickListener.onItemLongClick(viewHolder.img,pos);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv, gif;
        ImageView img;

        ViewHolder(@NonNull View itemView) {
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
