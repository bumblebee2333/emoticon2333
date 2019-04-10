package com.example.emoticon.editmodule.Graffiti;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

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
    }

    private void init(AttributeSet attributeSet){
        mHolder = getHolder();//初始化一个surfaceholder对象
        mHolder.addCallback(this);//注册surfaceholder回调方法
    }

    //在surfaceview初始化的时候回调
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //surfaceview是双缓冲机制 管理两个画布 一个front 一个back 绘制的内容在back上 lockCanvas获取到画布
        Canvas canvas = mHolder.lockCanvas();
        restorePreAction(canvas);
        mHolder.unlockCanvasAndPost(canvas);
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
        canvas.drawColor(Color.TRANSPARENT,PorterDuff.Mode.CLEAR);//加载之前内容前清空画布
        if(mDoodleActionList != null && mDoodleActionList.size() > 0){
            for(DoodleAction action: mDoodleActionList){
                action.draw(canvas);
            }
        }
    }
}




