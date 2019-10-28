package com.example.emoticon.widget;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

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
    public static EmoticonLookDialog newInstance(Bitmap bitmap) {
        EmoticonLookDialog emoticonLookDialog = new EmoticonLookDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("bitmap", bitmap);
        emoticonLookDialog.setArguments(bundle);
        return emoticonLookDialog;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.emoticon_look_dialog, container, false);
        view.setOnClickListener(v ->{
            dismiss();
        });
        ImageView imageView = view.findViewById(R.id.image);
        if (getArguments() != null) {
            String url = getArguments().getString("url");
            if(url != null && !url.isEmpty()){
                Glide.with(Objects.requireNonNull(getActivity())).load(url).into(imageView);
            }else{
                Parcelable bitmap = getArguments().getParcelable("bitmap");
                if (bitmap != null){
                    imageView.setImageBitmap((Bitmap) bitmap);
                }
            }
        }
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        if(getDialog() == null) return;
        Window window = getDialog().getWindow();
        if(window == null) return;
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

}
