package com.example.emoticon.editmodule.Graffiti;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * 自由曲线
 */

public class DoodlePath extends DoodleAction {
    private Path mPath;

    private float mPrevX;

    private float mPrevY;

    private Paint mPaint;

    public DoodlePath(){

    }

    public DoodlePath(float startX,float startY){

    }

    public DoodlePath(float startX,float startY,int color,float strokeWidth){
        this.color = color;
        this.strokeWidth = strokeWidth;
        mPath = new Path();
        mPath.moveTo(startX,startY);
        mPrevX = startX;
        mPrevY = startY;
        initPaint();
    }

    private void initPaint(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//平滑绘制内容的边缘
        mPaint.setDither(true);//setFlags（）的助手，设置或清除DITHER_FLAG位抖动会影响精度高于器件的颜色的下采样方式。
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);//设置油漆的帽子 始末端
        mPaint.setStrokeJoin(Paint.Join.ROUND);//设置油漆的加入 拐角
    }

    @Override
    public void setColor(int color) {
        super.setColor(color);
        mPaint.setColor(color);
    }

    @Override
    public void setStrokeWidth(float strokeWidth) {
        super.setStrokeWidth(strokeWidth);
        mPaint.setStrokeWidth(strokeWidth);
    }

    @Override
    public void draw(Canvas canvas) {
        if(canvas != null){
            canvas.drawPath(mPath,mPaint);
        }
    }

    @Override
    public void move(float x, float y) {
        mPath.quadTo(mPrevX,mPrevY,(x+mPrevX)/2,(y+mPrevY)/2);
        mPrevX = x;
        mPrevY = y;
    }

    public void moveTo(float startX,float startY){
        mPath.moveTo(startX,startY);
        mPrevX = startX;
        mPrevY = startY;
    }


}
