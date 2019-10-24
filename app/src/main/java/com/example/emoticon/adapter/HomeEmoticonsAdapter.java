package com.example.emoticon.adapter;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.common.bean.Emoticon;
import com.example.common.bean.EmoticonType;
import com.example.emoticon.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/5/11.
 * PS:
 */
public class HomeEmoticonsAdapter extends RecyclerView.Adapter<HomeEmoticonsAdapter.ViewHolder> {
    private List<EmoticonType.DataBean> list;
    private List<Bitmap> bitmaps = new ArrayList<>();

    private HomeEmoticonsAdapter.OnItemClickListener onItemClickListener;


    public HomeEmoticonsAdapter(List<EmoticonType.DataBean> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public HomeEmoticonsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_emoticon_item, viewGroup, false);
        return new HomeEmoticonsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeEmoticonsAdapter.ViewHolder viewHolder, int i) {
        viewHolder.title.setText(list.get(i).getTitle());
        List<Emoticon> emoticons = new ArrayList<>();
        for (int j = 0; j < list.get(i).getEmoticons().size(); j++) {
            if (j >= 8) {
                break;
            } else {
                emoticons.add(list.get(i).getEmoticons().get(j));
            }
        }
        EmoticonAdapter emoticonAdapter = new EmoticonAdapter(emoticons, viewHolder.gridLayoutManager);
        viewHolder.recyclerView.setAdapter(emoticonAdapter);

        if (onItemClickListener != null) {
            viewHolder.recyclerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    onItemClickListener.OnItemClick(viewHolder.title, pos);
                }
            });
            viewHolder.bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    onItemClickListener.OnItemClick(viewHolder.title, pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;//, gif;
        RecyclerView recyclerView;
        GridLayoutManager gridLayoutManager;
        View bg;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            bg = itemView;
            title = itemView.findViewById(R.id.title);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            gridLayoutManager = new GridLayoutManager(title.getContext(), 4);
            recyclerView.setLayoutManager(gridLayoutManager);
//            gif = itemView.findViewById(R.id.gif);
//            img = itemView.findViewById(R.id.pic);
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    public void setOnItemClickListener(HomeEmoticonsAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public Bitmap getBitMap(int position) {
        return bitmaps.get(position);
    }
}
