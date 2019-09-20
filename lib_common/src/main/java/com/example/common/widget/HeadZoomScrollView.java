package com.example.common.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ScrollView;

/**
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/7/26.
 * PS:
 */
public class HeadZoomScrollView extends ScrollView {

    private float y = 0f;//下拉位置
    private int zoomViewWidth = 0;//宽度
    private int zoomViewHeight = 0;//高度
    private boolean mScaling = false;//是否正在放大
    private View zoomView;
    private float mScaleRatio = 0.4f;//放大系数
    private float mScaleTimes = 2f;//放大倍数
    private float mReplyRatio = 0.5f;//回弹系数

    public void setmScaleRatio(float mScaleRatio) {
        this.mScaleRatio = mScaleRatio;
    }

    public void setmScaleTimes(float mScaleTimes) {
        this.mScaleTimes = mScaleTimes;
    }

    public void setmReplyRatio(float mReplyRatio) {
        this.mReplyRatio = mReplyRatio;
    }

    public void setZoomView(View zoomView) {
        this.zoomView = zoomView;
    }

    public HeadZoomScrollView(Context context) {
        super(context);
    }

    public HeadZoomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeadZoomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HeadZoomScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setOverScrollMode(OVER_SCROLL_NEVER);
        if (getChildAt(0) != null && getChildAt(0) instanceof ViewGroup && zoomView == null) {
            ViewGroup viewGroup = (ViewGroup) getChildAt(0);
            if (viewGroup.getChildCount() > 0) {
                zoomView = viewGroup.getChildAt(0);
            }
        }
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        if (zoomViewWidth <= 0 || zoomViewHeight <= 0) {
//            zoomViewWidth = zoomView.getMeasuredWidth();
//            zoomViewHeight = zoomView.getMeasuredHeight();
//        }
//        if (zoomView == null || zoomViewWidth <= 0 || zoomViewHeight <= 0) {
//            return super.onInterceptTouchEvent(ev);
//        }
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_MOVE:
//                if (!mScaling) {
//                    if (getScrollY() == 0) {
//                        y = ev.getY();
//                    } else {
//                        break;
//                    }
//                }
//                int distance = (int) ((ev.getY() - y) * mScaleRatio);
//                if (distance < 0) break;
//                mScaling = true;
//                setZoom(distance);
//                return true;
//            case MotionEvent.ACTION_UP:
//                mScaling = false;
//                replyView();
//                break;
//        }
//        return super.onInterceptTouchEvent(ev);
//    }

    private boolean mScrolling;
    private float touchDownX;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDownX = event.getX();
                mScrolling = false;
                break;
            case MotionEvent.ACTION_MOVE:
                mScrolling = Math.abs(touchDownX - event.getX()) >= ViewConfiguration.get(
                        getContext()).getScaledTouchSlop();
                break;
            case MotionEvent.ACTION_UP:
                mScrolling = false;
                break;
        }
        return mScrolling;
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (zoomViewWidth <= 0 || zoomViewHeight <= 0) {
            zoomViewWidth = zoomView.getMeasuredWidth();
            zoomViewHeight = zoomView.getMeasuredHeight();
        }
        if (zoomView == null || zoomViewWidth <= 0 || zoomViewHeight <= 0) {
            return super.onTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (!mScaling) {
                    if (getScrollY() == 0) {
                        y = ev.getY();
                    } else {
                        break;
                    }
                }
                int distance = (int) ((ev.getY() - y) * mScaleRatio);
                if (distance < 0) break;
                mScaling = true;
                setZoom(distance);
                return true;
            case MotionEvent.ACTION_UP:
                mScaling = false;
                replyView();
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void replyView() {
        float distance = zoomView.getMeasuredWidth() - zoomViewWidth;

        ValueAnimator animator = ObjectAnimator.ofFloat(distance, 0.0f).setDuration((long) (distance * mReplyRatio));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setZoom((float) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    private void setZoom(float distance) {
        float scaleTimes = (float) ((zoomViewWidth + distance) / (zoomViewWidth * 1.0));
        if (scaleTimes > mScaleTimes) return;
        ViewGroup.LayoutParams layoutParams = zoomView.getLayoutParams();
        layoutParams.width = (int) (zoomViewWidth + distance);
        layoutParams.height = (int) (zoomViewHeight * ((zoomViewHeight + distance) / zoomViewHeight));
        ((MarginLayoutParams) layoutParams).setMargins(-(layoutParams.width - zoomViewWidth) / 2, 0, 0, 0);
        zoomView.setLayoutParams(layoutParams);
    }

    private OnScrollListener onScrollListener;

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollListener != null) onScrollListener.onScroll(l, t, oldl, oldt);
    }
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }
    public interface OnScrollListener {
        void onScroll(int x, int y, int ox, int oy);
    }
}
