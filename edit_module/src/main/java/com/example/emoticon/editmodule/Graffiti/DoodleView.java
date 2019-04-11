package com.example.emoticon.editmodule.Graffiti;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.emoticon.editmodule.R;

import java.util.ArrayList;

public class DoodleView extends SurfaceView implements SurfaceHolder.Callback {
    private  SurfaceHolder mHolder = null;
    //子线程标志位
    private boolean mIsDrawing;
    /**
     * 记录每次绘画动作
     */
    private ArrayList<DoodleAction> mDoodleActionList;

    /**
     * 当前绘制动作
     */
    private DoodleAction mCurAction;

    /**
     * 当前绘制颜色
     */
    private int mCurColor;

    /**
     * 当前笔的size
     */
    private float mCurStrokeWidth;

    /**
     * 涂鸦功能是否可用
     */
    private boolean mIsDoodleEnabled;

    /**
     * 绘制类型
     */

    /**
     * 手指按下时坐标
     */
    private float mDownX,mDownY;

    public DoodleView(Context context) {
        this(context,null,0);
    }

    public DoodleView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DoodleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attributeSet){
        mIsDoodleEnabled = true;//涂鸦功能可用
        mDoodleActionList = new ArrayList<>();//创建记录绘画动作的List
        mHolder = getHolder();//初始化一个surfaceholder对象
        mHolder.addCallback(this);//注册surfaceholder回调方法
        this.setFocusable(true);
        this.setZOrderOnTop(true);//设置背景透明
        //PixelFormat 指定图像中每个像素的颜色数据的格式 系统选择支持透明度的格式
        mHolder.setFormat(PixelFormat.TRANSPARENT);
        TypedArray typedArray = getResources().obtainAttributes(attributeSet, R.styleable.DoodleView);
        mCurColor = typedArray.getColor(R.styleable.DoodleView_paintColor,Color.BLACK);
        mCurStrokeWidth = typedArray.getFloat(R.styleable.DoodleView_paintStrokeWidth,10.0f);
        typedArray.recycle();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                if(!mIsDoodleEnabled)
                    return false;
        }
        return super.onTouchEvent(event);
    }

    //在surfaceview初始化的时候回调
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //surfaceview是双缓冲机制 管理两个画布 一个front 一个back 绘制的内容在back上 lockCanvas获取到画布
        Canvas canvas = mHolder.lockCanvas();
        restorePreAction(canvas);
        mHolder.unlockCanvasAndPost(canvas);//结束锁定画图，并提交改变
        postDelayed(new Runnable() {//解决surfaceview restore不完全问题
            @Override
            public void run() {
                Canvas canvas = mHolder.lockCanvas();
                restorePreAction(canvas);
                mHolder.unlockCanvasAndPost(canvas);
            }
        },100);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }
    //在surfaceview销毁时调用 比如点击home键app进入后台时会调用这个方法
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /**
     * 重新加载之前绘制的内容
     */
    private void restorePreAction(Canvas canvas){
        if(canvas == null){
            return;
        }
        //PorterDuff.Mode类主要用于图形合成时的图像过度模式计算(合成图像数字计算)
        canvas.drawColor(Color.TRANSPARENT,PorterDuff.Mode.CLEAR);//加载之前内容前清空画布
        if(mDoodleActionList != null && mDoodleActionList.size() > 0){
            for(DoodleAction action: mDoodleActionList){
                action.draw(canvas);
            }
        }
    }
}




