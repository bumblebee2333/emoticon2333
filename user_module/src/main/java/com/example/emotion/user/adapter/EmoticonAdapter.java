package com.example.emotion.user.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.common.bean.Emoticon;
import com.example.common.utils.ImageUtils;
import com.example.emotion.user.R;

import java.util.ArrayList;
import java.util.List;

public class EmoticonAdapter extends RecyclerView.Adapter<EmoticonAdapter.ViewHolder> {
    private List<Emoticon> list = new ArrayList<>();
    GridLayoutManager gridLayoutManager;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public EmoticonAdapter(List<Emoticon> list, GridLayoutManager gridLayoutManager) {
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
    public void onBindViewHolder(@NonNull final EmoticonAdapter.ViewHolder viewHolder, int i) {
        Glide.with(viewHolder.img.getContext()).load(list.get(i).getImgUrl()+ ImageUtils.gifToJpg).into(viewHolder.img);
        viewHolder.tv.setVisibility(View.GONE);
        if (list.get(i).getImgUrl().endsWith(".gif")) {
            viewHolder.gif.setVisibility(View.VISIBLE);
        } else {
            viewHolder.gif.setVisibility(View.GONE);
        }
        ViewGroup.LayoutParams params = viewHolder.img.getLayoutParams(); //获取图片的的LayoutParams
        params.height = gridLayoutManager.getWidth() / gridLayoutManager.getSpanCount()
                - 2 * viewHolder.img.getPaddingLeft() - 2 * ((ViewGroup.MarginLayoutParams) params).leftMargin;//设置图片高度等于宽度

        if (mOnItemClickListener!=null){
            viewHolder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(viewHolder.img,pos);
                }
            });
        }

        if (mOnItemLongClickListener!=null){
            viewHolder.img.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                    mOnItemLongClickListener.onItemLongClick(viewHolder.img,pos);
                    return false;
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

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
    public interface OnItemLongClickListener{
        void onItemLongClick(View view, int position);
    }
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener){
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }
}
