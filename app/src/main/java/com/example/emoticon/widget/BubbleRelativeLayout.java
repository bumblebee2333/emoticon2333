package com.example.emoticon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import com.example.emoticon.R;

public class BubbleRelativeLayout extends RelativeLayout {
    public static final int LEFT = 1;
    public static final int TOP = 2;
    public static final int RIGHT = 3;
    public static final int BOTTOM =4;

    private int mRadius;//圆角大小

    private int mDirection;//三角形的方向

    private Paint mPaint;

    private Path mPath;

    private int mOffset;

    public BubbleRelativeLayout(Context context) {
        this(context,null);
    }

    public BubbleRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs){
        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.BubbleRelativeLayout);
        //背景颜色
        int backgroundColor = ta.getColor(R.styleable.BubbleRelativeLayout_background_color,Color.WHITE);
        //阴影颜色
        int shadowColor = ta.getColor(R.styleable.BubbleRelativeLayout_shadow_color,Color.parseColor("#999999"));
        int defShadowSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,4,getResources().getDisplayMetrics());
        //阴影尺寸大小
        int shawSize = ta.getDimensionPixelSize(R.styleable.BubbleRelativeLayout_shadow_size,defShadowSize);
        //圆角的大小
        mRadius = ta.getDimensionPixelSize(R.styleable.BubbleRelativeLayout_radius,0);
        //圆角默认方向
        mDirection = ta.getInt(R.styleable.BubbleRelativeLayout_direction,0);
        mOffset = ta.getDimensionPixelSize(R.styleable.BubbleRelativeLayout_offset,0);
        ta.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(backgroundColor);
        mPaint.setShadowLayer(shawSize,0,0,shadowColor);

        mPath = new Path();

        setWillNotDraw(false);
        //关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE,null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        switch (mDirection){
            case LEFT:
                drawLeftTriangle(canvas,width,height);
                break;
            case RIGHT:
                drawRightTriangle(canvas,width,height);
                break;
            case TOP:
                drawTopTriangle(canvas,width,height);
                break;
            case BOTTOM:
                drawBottomTriangle(canvas,width,height);
                break;
        }
    }

    private void drawLeftTriangle(Canvas canvas,int width,int height){
        int triangleLength = getPaddingLeft();
        int halfLength = triangleLength/2;
        if(triangleLength == 0){
            return;
        }

        mPath.addRoundRect(new RectF(triangleLength,triangleLength,width-triangleLength,height-triangleLength),mRadius,mRadius,Path.Direction.CW);
        mPath.moveTo(triangleLength,triangleLength+mOffset);
        mPath.lineTo(0,triangleLength+mOffset+halfLength);
        mPath.lineTo(triangleLength,2*triangleLength+mOffset);
        mPath.close();
        canvas.drawPath(mPath,mPaint);
    }

    private void drawTopTriangle(Canvas canvas,int width,int height){
        int triangleLength = getPaddingTop();
        int halfLength = triangleLength/2;
        if(triangleLength == 0){
            return;
        }

        mPath.addRoundRect(new RectF(triangleLength,triangleLength,width-triangleLength,height-triangleLength),mRadius,mRadius,Path.Direction.CW);
        mPath.moveTo(triangleLength+mOffset,triangleLength);
        mPath.lineTo(triangleLength+mOffset+halfLength,0);
        mPath.lineTo(2*triangleLength+mOffset,triangleLength);
        mPath.close();
        canvas.drawPath(mPath,mPaint);
    }

    private void drawRightTriangle(Canvas canvas,int width,int height){
        int triangleLength = getPaddingRight();
        int halfLength = triangleLength/2;
        if(triangleLength == 0){
            return;
        }

        RectF mRect = new RectF(triangleLength,triangleLength,width-triangleLength,height-triangleLength);
        float mWidth = mRect.width();
        mPath.addRoundRect(mRect,mRadius,mRadius,Path.Direction.CW);
        mPath.moveTo(triangleLength+mWidth,triangleLength+mOffset);
        mPath.lineTo(2*triangleLength+mWidth,triangleLength+mOffset+halfLength);
        mPath.lineTo(triangleLength+mWidth,2*triangleLength+mOffset);
        mPath.close();
        canvas.drawPath(mPath,mPaint);
    }

    private void drawBottomTriangle(Canvas canvas,int width,int height){
        int triangleLength = getPaddingBottom();
        int halfLength = triangleLength/2;
        if(triangleLength == 0){
            return;
        }

        RectF mRect = new RectF(triangleLength,triangleLength,width-triangleLength,height-triangleLength);
        float mHeight = mRect.height();
        mPath.addRoundRect(mRect,mRadius,mRadius,Path.Direction.CW);
        mPath.moveTo(triangleLength+mOffset,mHeight+triangleLength);
        mPath.lineTo(triangleLength+mOffset+halfLength,2*triangleLength+mHeight);
        mPath.lineTo(2*triangleLength+mOffset,mHeight+triangleLength);
        mPath.close();
        canvas.drawPath(mPath,mPaint);
    }
}
