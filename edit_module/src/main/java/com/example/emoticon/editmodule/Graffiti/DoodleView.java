package com.example.emoticon.editmodule.Graffiti;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class DoodleView extends SurfaceView implements SurfaceHolder.Callback {
    private  SurfaceHolder mHolder = null;
    /**
     * 同步锁标记 是否执行
     */
    private boolean mDrawFlag = true;
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
    private int mCurColor = Color.BLACK;

    /**
     * 当前笔的size
     */
    private float mCurStrokeWidth = 10.0f;

    /**
     * 涂鸦功能是否可用
     */
    private boolean mIsDoodleEnabled;

    /**
     * 绘制类型
     */
    private DoodleShapeType mType = DoodleShapeType.Path;
    /**
     * 手指按下时坐标
     */
    private float mDownX,mDownY;

    private Paint mPaint;

    public DoodleView(Context context) {
        super(context,null,0);
        init(context);
    }

    public DoodleView(Context context, AttributeSet attrs) {
        super(context, attrs,0);
        init(context);
    }

    public DoodleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mIsDoodleEnabled = true;//涂鸦功能可用
        mDoodleActionList = new ArrayList<>();//创建记录绘画动作的List
        mHolder = getHolder();//初始化一个surfaceholder对象
        mHolder.addCallback(this);//注册surfaceholder回调方法
        setFocusable(true);
        //setFocusableInTouchMode(true);
        //this.setKeepScreenOn(true);
        //this.setZOrderOnTop(true);//设置背景透明
        //PixelFormat 指定图像中每个像素的颜色数据的格式 系统选择支持透明度的格式
        mHolder.setFormat(PixelFormat.TRANSPARENT);
        //TypedArray typedArray = getResources().obtainAttributes(attributeSet, R.styleable.DoodleView);
        //typedArray.getColor(R.styleable.DoodleView_paintColor,Color.BLACK);
        //typedArray.getFloat(R.styleable.DoodleView_paintStrokeWidth,10.0f);
        //typedArray.recycle();
        mPaint = new Paint();
        mPaint.setColor(mCurColor);
        mPaint.setStrokeWidth(mCurStrokeWidth);
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
                mDownX = x;
                mDownY = y;
                setCurDooldleAction(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
//                Canvas canvas = mHolder.lockCanvas();
//                restorePreAction(canvas);//首先恢复之前绘制的内容
//                mCurAction.move(x,y);
//                mCurAction.draw(canvas);//绘制当前Action
//                mHolder.unlockCanvasAndPost(canvas);
                synchronized (this){
                    if(mDrawFlag){
                        Canvas canvas = mHolder.lockCanvas();
                        if(canvas != null) {
                            try {
                                restorePreAction(canvas);
                                mCurAction.move(x, y);
                                mCurAction.draw(canvas);
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    mHolder.unlockCanvasAndPost(canvas);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if(mDownX == x && mDownY == y){

                }else {
                    //只有手指完成滑动的动作 才会添加并发送动作
                    mDoodleActionList.add(mCurAction);//添加当前动作
                }
                mCurAction = null;
                break;
        }
        return true;
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
        synchronized (this){
            mDrawFlag = false;
        }
    }

    /**
     * 保存状态
     */
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SaveState ss = new SaveState(superState);
        ss.setDoodleActionList(mDoodleActionList);
        ss.setStart(mIsDoodleEnabled);
        return ss;
    }

    /**
     * 读取保存状态
     */
    protected  void onRestoreInstanceState(Parcelable state){
        SaveState ss = (SaveState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mDoodleActionList = ss.getDoodleActionList();
        mIsDoodleEnabled = ss.isStart();
        mHolder = getHolder();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                Canvas canvas = mHolder.lockCanvas();
                restorePreAction(canvas);
                mHolder.unlockCanvasAndPost(canvas);
            }
        },160);
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

    /**
     * 设置当前绘制动作类型
     */
    private void setCurDooldleAction(float startX,float startY){
        switch (mType){
            case Path:
                mCurAction = new DoodlePath(startX,startY,mCurColor,mCurStrokeWidth);
                break;
            case Eraser:
                break;
        }
        mCurAction.setColor(mCurColor);
        mCurAction.setStrokeWidth(mCurStrokeWidth);
    }
    /**
     * 绘制动作类型
     */
    public enum DoodleShapeType{
        Path,
        Eraser
    }

    public void enableDoodle(boolean enable){
        mIsDoodleEnabled = enable;
    }

    public void setDoodleShapeType(DoodleShapeType type){
        mType = type;
    }

    public void setCurStrokeWidth(float strokeWidth){
        mCurStrokeWidth = strokeWidth;
    }

    public ArrayList<DoodleAction> getDoodleActionList(){
        return mDoodleActionList;
    }

    public void setPaintColor(int color){
        mCurColor = color;
    }

    public static class SaveState extends BaseSavedState{
        private boolean isStart;
        private ArrayList<DoodleAction> doodleActionList;

        public SaveState(Parcelable superState) {
            super(superState);
        }

        public void writeToParcel(Parcel out,int flags){
            super.writeToParcel(out, flags);
            out.writeList(doodleActionList);
            out.writeInt(isStart ? 1 : 0);
        }

        public static Creator<SaveState> CREATOR = new Creator<SaveState>() {
            @Override
            public SaveState createFromParcel(Parcel in) {
                return new SaveState(in);
            }

            @Override
            public SaveState[] newArray(int size) {
                return new SaveState[size];
            }
        };

        private void setDoodleActionList(ArrayList<DoodleAction> list){
            doodleActionList = list;
        }

        private ArrayList<DoodleAction> getDoodleActionList(){
            return doodleActionList;
        }

        private boolean isStart(){
            return isStart;
        }

        private void setStart(boolean isStart){
            this.isStart = isStart;
        }

        private SaveState(Parcel in){
            super(in);
            in.readList(doodleActionList,List.class.getClassLoader());
            setDoodleActionList(doodleActionList);
            isStart = in.readInt() == 1;
        }
    }

    /**
     * 撤销画板
     */
    public void undoAction(){
        int size = mDoodleActionList.size();
        if(size > 0){
            mDoodleActionList.remove(size - 1);
            Canvas canvas = mHolder.lockCanvas();
            restorePreAction(canvas);
            mHolder.unlockCanvasAndPost(canvas);
        }
    }

    /**
     * 清空画板
     */
    public void cleanBoard(){
        if(mDoodleActionList != null && mDoodleActionList.size() > 0){
            mDoodleActionList.clear();
            Canvas canvas = mHolder.lockCanvas();
            restorePreAction(canvas);
            mHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void recycle(){
        if(mDoodleActionList != null){
            mDoodleActionList.clear();
            mDoodleActionList = null;
        }
    }
}




