package com.example.emoticon.editmodule.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ColorButton extends View {

    private int colorBlack = Color.BLACK;
    private int colorGrey = Color.GRAY;
    private int colorRed = Color.RED;
    private int colorOrange = Color.parseColor("#FF9224");
    private int colorYellow = Color.parseColor("#FFE153");
    private int colorPink = Color.parseColor("#FF79BC");
    private int colorGreen = Color.parseColor("#8CEA00");
    private int colorGreenL = Color.parseColor("#A8FF24");
    private int colorBlue = Color.parseColor("#00FFFF");
    private int colorPurple = Color.parseColor("#7373B9");
    /**
     * 颜色方块
     */
    RectF rectBlack;
    RectF rectGrey;
    RectF rectRed;
    RectF rectOrange;
    RectF rectYellow;
    RectF rectPink;
    RectF rectGreen;
    RectF rectGreenL;
    RectF rectBlue;
    RectF rectPurple;

    Paint paintBlack;
    Paint paintGrey;
    Paint paintRed;
    Paint paintOrange;
    Paint paintYellow;
    Paint paintPink;
    Paint paintGreen;
    Paint paintGreenL;
    Paint paintBlue;
    Paint paintPurple;

    RectF rectFBlack = new RectF();
    RectF rectFGrey = new RectF();
    RectF rectFOrange = new RectF();
    RectF rectFYellow = new RectF();
    RectF rectFPink = new RectF();
    RectF rectFGreen = new RectF();
    RectF rectFGreenL = new RectF();
    RectF rectFPurple = new RectF();
    RectF rectFBlue = new RectF();
    RectF rectFRed = new RectF();

    Paint mPaint;
    /**
     * 外轮廓方块
     */
    RectF rectF = new RectF();
    /**
     * 是否选中
     */
    private boolean isChecked = false;

    private int PADDING = 8;

    private boolean isBlack = true;
    private boolean isGrey = false;
    private boolean isRed = false;
    private boolean isOrange = false;
    private boolean isYellow = false;
    private boolean isPink = false;
    private boolean isGreen = false;
    private boolean isGreenL = false;
    private boolean isBlue = false;
    private boolean isPurple = false;

    public ColorButton(Context context) {
        super(context);
        init();
    }

    public ColorButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init(){
        setLayerType(LAYER_TYPE_SOFTWARE,null);
        isChecked = false;
        mPaint = new Paint();

        paintBlack = new Paint();
        paintBlack.setColor(colorBlack);
        paintBlack.setStrokeWidth(5);
        paintBlack.setStyle(Paint.Style.FILL);
        paintBlack.setAntiAlias(true);
        paintBlack.setDither(true);

        paintGrey = new Paint();
        paintGrey.setColor(colorGrey);
        paintGrey.setStrokeWidth(5);
        paintGrey.setStyle(Paint.Style.FILL);
        paintGrey.setAntiAlias(true);
        paintGrey.setDither(true);

        paintRed = new Paint();
        paintRed.setColor(colorRed);
        paintRed.setStrokeWidth(5);
        paintRed.setStyle(Paint.Style.FILL);
        paintRed.setAntiAlias(true);
        paintRed.setDither(true);

        paintOrange = new Paint();
        paintOrange.setColor(colorOrange);
        paintOrange.setStrokeWidth(5);
        paintOrange.setStyle(Paint.Style.FILL);
        paintOrange.setAntiAlias(true);
        paintOrange.setDither(true);

        paintYellow = new Paint();
        paintYellow.setColor(colorYellow);
        paintYellow.setStrokeWidth(5);
        paintYellow.setStyle(Paint.Style.FILL);
        paintYellow.setAntiAlias(true);
        paintYellow.setDither(true);

        paintPink = new Paint();
        paintPink.setColor(colorPink);
        paintPink.setStrokeWidth(5);
        paintPink.setStyle(Paint.Style.FILL);
        paintPink.setAntiAlias(true);
        paintPink.setDither(true);

        paintBlue = new Paint();
        paintBlue.setColor(colorBlue);
        paintBlue.setStrokeWidth(5);
        paintBlue.setStyle(Paint.Style.FILL);
        paintBlue.setAntiAlias(true);
        paintBlue.setDither(true);

        paintPurple = new Paint();
        paintPurple.setColor(colorPurple);
        paintPurple.setStrokeWidth(5);
        paintPurple.setStyle(Paint.Style.FILL);
        paintPurple.setAntiAlias(true);
        paintPurple.setDither(true);

        paintGreen = new Paint();
        paintGreen.setColor(colorGreen);
        paintGreen.setStrokeWidth(5);
        paintGreen.setStyle(Paint.Style.FILL);
        paintGreen.setAntiAlias(true);
        paintGreen.setDither(true);

        paintGreenL = new Paint();
        paintGreenL.setColor(colorGreenL);
        paintGreenL.setStrokeWidth(5);
        paintGreenL.setStyle(Paint.Style.FILL);
        paintGreenL.setAntiAlias(true);
        paintGreenL.setDither(true);
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//
//    }
//
//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
//
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //mPaint.setColor(Color.RED);
        rectBlack = new RectF(40,30,100,90);
        canvas.drawRoundRect(rectBlack,16,16,paintBlack);

        rectGrey = new RectF(140,30,200,90);
        canvas.drawRoundRect(rectGrey,16,16,paintGrey);

        rectRed = new RectF(240,30,300,90);
        canvas.drawRoundRect(rectRed,16,16,paintRed);

        rectOrange = new RectF(340,30,400,90);
        canvas.drawRoundRect(rectOrange,16,16,paintOrange);

        rectYellow = new RectF(440,30,500,90);
        canvas.drawRoundRect(rectYellow,16,16,paintYellow);

        rectPink = new RectF(540,30,600,90);
        canvas.drawRoundRect(rectPink,16,16,paintPink);

        rectGreen = new RectF(640,30,700,90);
        canvas.drawRoundRect(rectGreen,16,16,paintGreen);

        rectGreenL = new RectF(740,30,800,90);
        canvas.drawRoundRect(rectGreenL,16,16,paintGreenL);

        rectBlue = new RectF(840,30,900,90);
        canvas.drawRoundRect(rectBlue,16,16,paintBlue);

        rectPurple = new RectF(940,30,1000,90);
        canvas.drawRoundRect(rectPurple,16,16,paintPurple);

        if(isBlack==true || isGrey==true || isRed==true || isOrange==true || isYellow==true || isPink==true || isGreen==true || isGreenL==true || isBlue==true || isPurple==true){
            drawCheck(canvas);
        }
    }

    private void drawCheck(Canvas canvas){
        mPaint.setColor(Color.parseColor("#66B3FF"));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeWidth(4);

        if(isBlack){
            rectFBlack.set(rectBlack.left-PADDING,rectBlack.top-PADDING,rectBlack.right+PADDING,rectBlack.bottom+PADDING);
            canvas.drawRoundRect(rectFBlack,16,16,mPaint);
        }

        if(isGrey){
            rectFGrey.set(rectGrey.left-PADDING,rectGrey.top-PADDING,rectGrey.right+PADDING,rectGrey.bottom+PADDING);
            canvas.drawRoundRect(rectFGrey,16,16,mPaint);
        }

        if(isRed){
            rectFRed.set(rectRed.left-PADDING,rectRed.top-PADDING,rectRed.right+PADDING,rectRed.bottom+PADDING);
            canvas.drawRoundRect(rectFRed,16,16,mPaint);
        }

        if(isOrange){
            rectFOrange.set(rectOrange.left-PADDING,rectOrange.top-PADDING,rectOrange.right+PADDING,rectOrange.bottom+PADDING);
            canvas.drawRoundRect(rectFOrange,16,16,mPaint);
        }

        if(isYellow){
            rectFYellow.set(rectYellow.left-PADDING,rectYellow.top-PADDING,rectYellow.right+PADDING,rectYellow.bottom+PADDING);
            canvas.drawRoundRect(rectFYellow,16,16,mPaint);
        }

        if(isPink){
            rectFPink.set(rectPink.left-PADDING,rectPink.top-PADDING,rectPink.right+PADDING,rectPink.bottom+PADDING);
            canvas.drawRoundRect(rectFPink,16,16,mPaint);
        }

        if(isGreen){
            rectFGreen.set(rectGreen.left-PADDING,rectGreen.top-PADDING,rectGreen.right+PADDING,rectGreen.bottom+PADDING);
            canvas.drawRoundRect(rectFGreen,16,16,mPaint);
        }

        if(isGreenL){
            rectFGreenL.set(rectGreenL.left-PADDING,rectGreenL.top-PADDING,rectGreenL.right+PADDING,rectGreenL.bottom+PADDING);
            canvas.drawRoundRect(rectFGreenL,16,16,mPaint);
        }

        if(isBlue){
            rectFBlue.set(rectBlue.left-PADDING,rectBlue.top-PADDING,rectBlue.right+PADDING,rectBlue.bottom+PADDING);
            canvas.drawRoundRect(rectFBlue,16,16,mPaint);
        }

        if(isPurple){
            rectFPurple.set(rectPurple.left-PADDING,rectPurple.top-PADDING,rectPurple.right+PADDING,rectPurple.bottom+PADDING);
            canvas.drawRoundRect(rectFPurple,16,16,mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(rectBlack.contains(x,y)){
                    isGrey = false;
                    isRed = false;
                    isOrange = false;
                    isYellow = false;
                    isGreen = false;
                    isGreenL = false;
                    isPink = false;
                    isBlue = false;
                    isPurple = false;
                    isBlack = true;
                    invalidate();
                }
                else if(rectGrey.contains(x,y)){
                    isBlack = false;
                    isRed = false;
                    isOrange = false;
                    isYellow = false;
                    isGreen = false;
                    isGreenL = false;
                    isPink = false;
                    isBlue = false;
                    isPurple = false;
                    isGrey = true;
                    invalidate();
                }
                else if(rectRed.contains(x,y)){
                    isBlack = false;
                    isGrey = false;
                    isOrange = false;
                    isYellow = false;
                    isGreen = false;
                    isGreenL = false;
                    isPink = false;
                    isBlue = false;
                    isPurple = false;
                    isRed = true;
                    invalidate();
                }
                else if(rectOrange.contains(x,y)){
                    isBlack = false;
                    isGrey = false;
                    isRed = false;
                    isOrange = false;
                    isYellow = false;
                    isGreen = false;
                    isGreenL = false;
                    isPink = false;
                    isBlue = false;
                    isPurple = false;
                    isOrange = true;
                    invalidate();
                }
                else if(rectYellow.contains(x,y)){
                    isBlack = false;
                    isGrey = false;
                    isRed = false;
                    isOrange = false;
                    isGreen = false;
                    isGreenL = false;
                    isPink = false;
                    isBlue = false;
                    isPurple = false;
                    isYellow = true;
                    invalidate();
                }
                else if(rectPink.contains(x,y)){
                    isBlack = false;
                    isGrey = false;
                    isRed = false;
                    isOrange = false;
                    isYellow = false;
                    isGreen = false;
                    isGreenL = false;
                    isBlue = false;
                    isPurple = false;
                    isPink = true;
                    invalidate();
                }else if(rectGreen.contains(x,y)){
                    isBlack = false;
                    isGrey = false;
                    isRed = false;
                    isOrange = false;
                    isYellow = false;
                    isGreenL = false;
                    isPink = false;
                    isBlue = false;
                    isPurple = false;
                    isGreen = true;
                    invalidate();
                }else if(rectGreenL.contains(x,y)){
                    isBlack = false;
                    isGrey = false;
                    isRed = false;
                    isOrange = false;
                    isYellow = false;
                    isGreen = false;
                    isPink = false;
                    isBlue = false;
                    isPurple = false;
                    isGreenL = true;
                    invalidate();
                }else if(rectBlue.contains(x,y)){
                    isBlack = false;
                    isGrey = false;
                    isRed = false;
                    isOrange = false;
                    isYellow = false;
                    isGreen = false;
                    isGreenL = false;
                    isPink = false;
                    isPurple = false;
                    isBlue = true;
                    invalidate();
                }else if(rectPurple.contains(x,y)){
                    isBlack = false;
                    isGrey = false;
                    isRed = false;
                    isOrange = false;
                    isYellow = false;
                    isGreen = false;
                    isGreenL = false;
                    isPink = false;
                    isBlue = false;
                    isPurple = true;
                    invalidate();
                }
                else{
                    isBlack = false;
                    isGrey = false;
                    isRed = false;
                    isOrange = false;
                    isYellow = false;
                    isPink = false;
                    isGreen = false;
                    isGreenL = false;
                    isBlue = false;
                    isPurple = false;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }
}