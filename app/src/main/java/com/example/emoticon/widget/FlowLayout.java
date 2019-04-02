package com.example.emoticon.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emoticon.R;
import com.example.emoticon.activity.SearchActivity;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.*;

public class FlowLayout extends ViewGroup{

    private OnTabClickListener mOnTabClickListener;
    private OnTabLongClickListener mOnTabLongClickListener;

    //存储所有子View
    private List<List<View>> mAllChildViews = new ArrayList<List<View>>();
    //每一行的高度
    private List<Integer> mLineHeight = new ArrayList<Integer>();

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //父控件传进来的宽度和高度以及对应的测量模式
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        //如果当前ViewGroup的宽高为wrap_content的情况
        int width = 0;//自己测量的宽度
        int height = 0;//自己测量的高度

        int lineWidth = 0;//每一行的宽度
        int lineHeight = 0;//每一行的高度

        int childCount = getChildCount();//获取子View的个数

        for(int i=0;i<childCount;i++){
            View child = getChildAt(i);
            //测量子View的高和宽
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
            //得到LayoutParams
            MarginLayoutParams lp = (MarginLayoutParams)getLayoutParams();
            //得到子View占据的宽度
            int childWidth = child.getMeasuredWidth() + lp.leftMargin
                    +lp.rightMargin;
            //得到子View占据的高度
            int childHeight = child.getMeasuredHeight() + lp.topMargin
                    +lp.bottomMargin;
            if(lineWidth +childWidth > sizeWidth){
                width = Math.max(width,lineWidth);//得到最大宽度
                lineWidth = childWidth;//重置lineWidth
                height += lineHeight;//得到高度
                lineHeight = childHeight;//重置lineHeight
            }else {
                lineWidth += childWidth;//叠加行宽
                lineHeight = Math.max(lineHeight,childHeight);
            }
            if( i == childCount-1){//处理最后一个子View
                width = Math.max(width,lineWidth);
                height += lineHeight;
            }
        }

        setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY?sizeWidth
        :width,modeHeight == MeasureSpec.EXACTLY?sizeHeight:height);
    }

        @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllChildViews.clear();
        mLineHeight.clear();

        int width = getWidth();//获取当前ViewGroup宽度
        int lineWidth = 0;
        int lineHeight = 0;

        List<View> lineViews = new ArrayList<View>();//记录当前行的View
        int childCount = getChildCount();
        for(int i = 0 ;i<childCount;i++){
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams)child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            //需要换行
            if(lineWidth+childWidth+lp.leftMargin+lp.rightMargin>width){
                mLineHeight.add(lineHeight);//记录lineHeight;
                mAllChildViews.add(lineViews);//记录当前行的Views
                //重置行的宽高
                lineWidth = 0;
                lineHeight = childHeight+lp.topMargin+lp.bottomMargin;
                //重置当前行的View集合
                lineViews = new ArrayList<View>();
            }

            lineWidth += childWidth+lp.leftMargin+lp.rightMargin;
            lineHeight = Math.max(lineHeight,childHeight+lp.topMargin+lp.bottomMargin);
            lineViews.add(child);
        }
        //处理最后一行
        mLineHeight.add(lineHeight);
        mAllChildViews.add(lineViews);
        //设置子View的位置
        int left = 0;
        int top = 0;
        //获取行数
        int lineCount = mAllChildViews.size();
        for(int i=0;i<lineCount;i++){
            //当前行的高度和Views
            lineViews = mAllChildViews.get(i);
            lineHeight = mLineHeight.get(i);
            for(int j=0;j<lineViews.size();j++){
                View child = lineViews.get(j);
                //判断是否显示
                if(child.getVisibility() == View.GONE){
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams)child.getLayoutParams();
                int mLeft = left + lp.leftMargin;
                int mTop = top + lp.topMargin;
                int mRight = mLeft + child.getMeasuredWidth();
                int mBottom = mTop + child.getMeasuredHeight();
                //子View进行布局
                child.layout(mLeft,mTop,mRight,mBottom);
                left += child.getMeasuredWidth()+lp.leftMargin+
                        lp.rightMargin;
            }
            left = 0;
            top += lineHeight;
        }
    }
    // 与当前ViewGroup对应的LayoutParams
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @SuppressLint("NewApi")
    public void initData(List<String> list){
        removeAllViews();
        ViewGroup.MarginLayoutParams lp = new ViewPager.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT,
                ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        //规定每个textview之间的间距
        lp.leftMargin = 20;
        lp.rightMargin = 20;
        lp.topMargin = 10;
        lp.bottomMargin = 10;

        int size = list.size();
        for(int i=0;i<size;i++){
            final TextView tv = new TextView(getContext());
            tv.setText(list.get(i));
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(Color.BLACK);
            tv.setBackground(getResources().getDrawable(R.drawable.hot_word_selector));
            setOnClick(i,tv);
            setOnLongClick(i,tv);
            addView(tv,lp);
        }
    }

    public interface OnTabClickListener{
        void onTabClick(int position, TextView textView);
    }
    public interface OnTabLongClickListener{
        void onTabLongClick(int position, TextView textView);
    }

    public void setOnTabClickListener(OnTabClickListener mOnTabClickListener){
        this.mOnTabClickListener = mOnTabClickListener;
    }
    public void setOnTabLongClickListener(OnTabLongClickListener mOnTabLongClickListener){
        this.mOnTabLongClickListener = mOnTabLongClickListener;
    }
    public void setOnClick(final int position, final TextView textView){
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnTabClickListener.onTabClick(position,textView);
            }
        });
    }
    public void setOnLongClick(final int position, final TextView textView){
        textView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mOnTabLongClickListener.onTabLongClick(position,textView);
                return true;
            }
        });
    }
}
