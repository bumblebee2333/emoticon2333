package com.example.emoticon.editmodule.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.example.common.utils.ScreenUtils;

public class DrawBitmap extends View {
    private Bitmap bitmap;
    private Context context;
    private Paint mPaint;
    private Rect mSrcRect;
    private Rect mDestRect;

    public DrawBitmap(Context context,Bitmap bitmap){
        this(context);
        this.bitmap = bitmap;
        this.context = context;
    }

    public DrawBitmap(Context context) {
        super(context);
        init();
    }
    public DrawBitmap(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawBitmap(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setFilterBitmap(true);
        mPaint.setDither(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        mSrcRect = new Rect(0,0,width,height);
        mDestRect = new Rect(0,0,width,height);
        canvas.translate(0,ScreenUtils.getScreenHeight(context)/2-200);
        canvas.drawBitmap(bitmap,mSrcRect,mDestRect,mPaint);
    }
}
