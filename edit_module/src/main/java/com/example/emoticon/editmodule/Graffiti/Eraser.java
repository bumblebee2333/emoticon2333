package com.example.emoticon.editmodule.Graffiti;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.emoticon.editmodule.R;

public class Eraser extends View {

    private Paint mPaint;

    private Path mPath;

    private Bitmap decodeResourceSRC;
    private Bitmap createBitmapDST;

    private float mDownX;
    private float mDownY;

    public Eraser(Context context) {
        super(context);
        init();
    }
    public Eraser(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public Eraser(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(45);
        mPaint.setDither(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setAlpha(0);

        //生成手指图像目标圆
        decodeResourceSRC = BitmapFactory.decodeResource(getResources(),
                R.drawable.circle_2);
        //目标
        createBitmapDST = Bitmap.createBitmap(decodeResourceSRC.getWidth(),
                decodeResourceSRC.getHeight(),Bitmap.Config.ARGB_8888);

        //mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mPath = new Path();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(x,y);
                mDownX = x;
                mDownY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.quadTo(mDownX,mDownY,(x+mDownX)/2,(y+mDownY)/2);
                mDownX = x;
                mDownY = y;
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
                default:
                    break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //分层绘制
        int saveLayer = canvas.saveLayer(0,0,getWidth(),getHeight(),
                null,Canvas.ALL_SAVE_FLAG);

        Canvas canvas2 = new Canvas(createBitmapDST);
        canvas2.drawPath(mPath,mPaint);

        canvas.drawBitmap(createBitmapDST,100,200,null);
        //计算图像区域
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        canvas.drawBitmap(decodeResourceSRC,0,0,mPaint);

        mPaint.setXfermode(null);
        canvas.restoreToCount(saveLayer);
    }
}
