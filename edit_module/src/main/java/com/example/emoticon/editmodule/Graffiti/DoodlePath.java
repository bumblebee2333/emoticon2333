package com.example.emoticon.editmodule.Graffiti;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Parcel;

/**
 * 自由曲线
 */

public class DoodlePath extends DoodleAction {
    private Path mPath;

    private float mPrevX;

    private float mPrevY;

    private Paint mPaint;

    public DoodlePath(){
        this(0,0,0,10.0f);
    }

    public DoodlePath(float startX,float startY){
        this(startX,startY,0,10.0f);
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

    //内容描述接口 基本不用管
    @Override
    public int describeContents() {
        return 0;
    }
    //写入接口函数，打包
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(color);
        out.writeFloat(strokeWidth);
    }

    private DoodlePath(Parcel in){
        color = in.readInt();
        strokeWidth = in.readFloat();
    }

    //读取接口，目的是要从Parcel中构造一个实现了Parcelable接口的类的实例
    //因为实现类在这里是不可知的 所有需要用到模板的方式 继承类名通过模板参数传入
    public static final Creator<DoodlePath> CREATOR = new Creator<DoodlePath>() {
        @Override
        public DoodlePath createFromParcel(Parcel in) {
            return new DoodlePath(in);
        }

        @Override
        public DoodlePath[] newArray(int size) {
            return new DoodlePath[size];
        }
    };
}
