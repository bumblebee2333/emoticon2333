package com.example.emoticon.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.emoticon.R;
import com.example.emoticon.model.Emoticon;
import com.example.emoticon.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

public class EmoticonAdapter extends RecyclerView.Adapter<EmoticonAdapter.ViewHolder> {
    private List<Emoticon.DataBean> list = new ArrayList<>();
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
    public void onBindViewHolder(@NonNull final EmoticonAdapter.ViewHolder viewHolder, int i) {
        Glide.with(viewHolder.img.getContext()).load(list.get(i).getImg_url()+ImageUtils.gifToJpg).into(viewHolder.img);
        viewHolder.tv.setVisibility(View.GONE);
        if (list.get(i).getImg_url().endsWith(".gif")) {
            viewHolder.gif.setVisibility(View.VISIBLE);
        } else {
            viewHolder.gif.setVisibility(View.GONE);
        }
        ViewGroup.LayoutParams parm = viewHolder.img.getLayoutParams(); //获取图片的的LayoutParams
        parm.height = gridLayoutManager.getWidth() / gridLayoutManager.getSpanCount()
                - 2 * viewHolder.img.getPaddingLeft() - 2 * ((ViewGroup.MarginLayoutParams) parm).leftMargin;//设置图片高度等于宽度

        if (mOnItemClickListener!=null){
            viewHolder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(viewHolder.img,pos);
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
        void onItemClick(View view,int position);
    }
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
