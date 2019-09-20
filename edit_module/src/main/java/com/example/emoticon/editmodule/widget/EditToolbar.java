package com.example.emoticon.editmodule.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.emoticon.editmodule.R;

public class EditToolbar extends LinearLayout implements View.OnClickListener{
    private TextView mRight;
    private ImageView mBack;
    private OnRightClickListener mRightClickListener;

    public interface OnRightClickListener{
        void doRight();
    }

    public EditToolbar(Context context) {
        super(context);
        init();
    }
    public EditToolbar(Context context,AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public EditToolbar(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_edit_toolbar,null);
        addView(view,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
        mRight = view.findViewById(R.id.right);
        mBack = view.findViewById(R.id.back_edit);
        mBack.setOnClickListener(this);
    }

    public void setRightClickListener(OnRightClickListener mRightClickListener) {
        this.mRightClickListener = mRightClickListener;
    }

    public void setTextViewText(String text){
        mRight.setText(text);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.back_edit) {
            ((Activity)getContext()).finish();
        }
    }
}
