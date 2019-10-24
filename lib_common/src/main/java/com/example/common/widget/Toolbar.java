package com.example.common.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.common.R;
import com.example.common.utils.DensityUtil;


public class Toolbar extends LinearLayout implements View.OnClickListener {
    TextView title;
    public ImageView right1, right2, back;

    public Toolbar(Context context) {
        super(context);
        initViews(context,null);
    }

    public Toolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }

    public Toolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Toolbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context, attrs);
    }

    private void initViews(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Toolbar);
        boolean line = array.getBoolean(R.styleable.Toolbar_line, true);
        boolean back_visibility = array.getBoolean(R.styleable.Toolbar_back_visibility, true);
        array.recycle();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.widget_toolbar, this, false);
        addView(view);
        title = findViewById(R.id.title);
        right1 = findViewById(R.id.right1);
        right2 = findViewById(R.id.right2);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);

        View lineView = findViewById(R.id.line);
        lineView.setVisibility(line?VISIBLE:GONE);
        back.setVisibility(back_visibility?VISIBLE:GONE);
        int left = line?0: DensityUtil.dp2px(10);
        title.setPadding(left,0,0,0);

        if (((Activity) getContext()).getTitle() != null) {//判断该Activity标题是否为空，不为空设置到标题
            title.setText(((Activity) getContext()).getTitle());
        }
        setRightButtonOneShow(false);//按钮不可见（GONE）
        setRightButtonTwoShow(false);//按钮不可见（GONE）
    }

    //设置标题
    public void setTitle(String title) {
        this.title.setText(title);
    }

    //设置分享按钮是否显示
    public void setRightButtonOneShow(boolean visibility) {
        int i = visibility ? View.VISIBLE : View.GONE;
        right1.setVisibility(i);
    }
    public void setRightButtonTwoShow(boolean visibility) {
        int i = visibility ? View.VISIBLE : View.GONE;
        right2.setVisibility(i);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.back) {
            ((Activity) getContext()).finish();

        }
    }
}
