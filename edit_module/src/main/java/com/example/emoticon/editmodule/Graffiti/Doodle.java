package com.example.emoticon.editmodule.Graffiti;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Doodle extends SurfaceView implements SurfaceHolder.Callback,Runnable {
    private SurfaceHolder mSurfaceHolder;
    /**
     * 子线程开启、结束标志位
     */
    private boolean isDrawing;

    private int mCurColor;

    private float mCurStrokeWidth;

    private Paint mPaint;

    private Path mPath;

    private Canvas mCanvas;

    public Doodle(Context context) {
        super(context);
        init();
    }

    public Doodle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Doodle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isDrawing = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDrawing = false;
    }

    private void init() {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);

        this.setZOrderOnTop(true);
        this.mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);

        mPath = new Path();

        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(15);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setDither(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:

                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        while (isDrawing) {
            drawSomeThing();
        }
        long end = System.currentTimeMillis();
        if (end - start < 100) {
            try {
                Thread.sleep(100 - (end - start));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void drawSomeThing() {
        try {
            mCanvas = mSurfaceHolder.lockCanvas();
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            mCanvas.drawPath(mPath, mPaint);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null) {
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    private void setCurColor(int color) {
        mCurColor = color;
    }

    private int getCurColor() {
        return mCurColor;
    }

    private void setCurStrokeWidth(float strokeWidth) {
        mCurStrokeWidth = strokeWidth;
    }

    private float getCurStrokeWidth() {
        return mCurStrokeWidth;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heihgtSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(widthMeasureSpec, heihgtSpecSize);
    }

    public void clean(){
        init();
    }
}
