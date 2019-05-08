package com.example.emoticon.widget;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.emoticon.R;

import java.util.Objects;

/**
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/4/11.
 * PS:
 */
public class EmoticonLookDialog extends DialogFragment {

    public static EmoticonLookDialog newInstance(String url) {
        EmoticonLookDialog emoticonLookDialog = new EmoticonLookDialog();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        emoticonLookDialog.setArguments(bundle);
        return emoticonLookDialog;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.emoticon_look_dialog, null);
        ImageView imageView = view.findViewById(R.id.image);
        assert getArguments() != null;
        Glide.with(Objects.requireNonNull(getActivity())).load(getArguments().getString("url")).into(imageView);
        return view;
    }
}
