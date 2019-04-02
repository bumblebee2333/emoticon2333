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
import com.example.emoticon.model.EmoticonType;
import com.example.emoticon.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

public class EmoticonTypeAdapter extends RecyclerView.Adapter<EmoticonTypeAdapter.ViewHolder> {
    private List<EmoticonType.DataBean> list = new ArrayList<>();
    GridLayoutManager gridLayoutManager;

    public EmoticonTypeAdapter(List<EmoticonType.DataBean> list, GridLayoutManager gridLayoutManager) {
        this.list = list;
        this.gridLayoutManager = gridLayoutManager;
    }

    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public EmoticonTypeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.emoticon_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmoticonTypeAdapter.ViewHolder viewHolder, final int i) {
        if (list.get(i).getId() == -1) {
            viewHolder.img.setImageResource(R.drawable.add_the_pic);
        } else {
            Glide.with(viewHolder.img.getContext()).load(list.get(i).getIcon() + ImageUtils.gifToJpg).into(viewHolder.img);
        }
        viewHolder.tv.setText(list.get(i).getTitle());
        viewHolder.tv.setVisibility(View.VISIBLE);
        if (null != list.get(i).getIcon()) {
            if (list.get(i).getIcon().endsWith(".gif")) {
                viewHolder.gif.setVisibility(View.VISIBLE);
            } else {
                viewHolder.gif.setVisibility(View.GONE);
            }
            viewHolder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.OnItemClick(v, i);
                }
            });
        }
        ViewGroup.LayoutParams parm = viewHolder.img.getLayoutParams(); //获取图片的的LayoutParams
        parm.height = gridLayoutManager.getWidth() / gridLayoutManager.getSpanCount()
                - 2 * viewHolder.img.getPaddingLeft() - 2 * ((ViewGroup.MarginLayoutParams) parm).leftMargin;//设置图片高度等于宽度

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

    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
