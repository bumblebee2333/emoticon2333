package com.example.common.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.common.R;


public class SearchTopView extends LinearLayout implements View.OnClickListener {

    public EditText editText;
    private ImageView editClear;
    public TextView cancel;
    public SearchTopView(Context context) {
        super(context);
        initViews();
    }

    public SearchTopView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public SearchTopView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SearchTopView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews();
    }

    private void initViews() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.search_top, this, false);
        editText = view.findViewById(R.id.edit_text);
        editClear = view.findViewById(R.id.edit_clear);
        cancel = view.findViewById(R.id.cancel);
        editClear.setVisibility(GONE);
        addView(view);
        editClear.setOnClickListener(this);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int visibility = s.length() > 0 ? VISIBLE : GONE;
                editClear.setVisibility(visibility);
                if (s.length()>0){
                    cancel.setText(R.string.search);
                }else {
                    cancel.setText(R.string.cancel);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.edit_clear) {
            editText.setText("");
        }
    }
}
