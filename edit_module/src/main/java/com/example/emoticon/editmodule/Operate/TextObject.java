package com.example.emoticon.editmodule.Operate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class TextObject extends ImageObject {
    private int textSize = 80;
    private int color = Color.BLACK;
    private String text;
    private Paint paint = new Paint();
    private RectF mTextBox = new RectF();
    private Context context;

    /**
     * 构造方法
     * @param context 上下文
     * @param text 输入的文字
     * @param x 位置x坐标
     * @param y 位置y坐标
     * @param deleteBm 删除按钮图标
     * @param rotateBm 旋转按钮图标
     *
     */
    public TextObject(Context context, String text, int x, int y,
                      Bitmap deleteBm,Bitmap rotateBm){
        super(text);
        this.context = context;
        this.text = text;
        mPoint.x = x;
        mPoint.y = y;
        this.deleteBm = deleteBm;
        this.rotateBm = rotateBm;
        drawTextAndRect();
    }

    /**
     * 绘制出字体和它外面的边框
     */
    public void drawTextAndRect(){
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setDither(true);
        paint.setFlags(Paint.SUBPIXEL_TEXT_FLAG);
        String[] lines = text.split("\n");
        int textWidth = 0;
        for(String str : lines){
            int temp = (int) paint.measureText(str);
            if(temp > textWidth)
                textWidth = temp;
        }
        if(textWidth < 1)
            textWidth = 1;
        if(srcBitmap != null){
            srcBitmap.recycle();
        }
        srcBitmap = Bitmap.createBitmap(textWidth,textSize * (lines.length) + 8,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(srcBitmap);
        canvas.drawARGB(0,0,0,0);
        for(int i=1;i<=lines.length;i++){
            canvas.drawText(lines[i-1],0,i * textSize,paint);
        }
        setCenter();
    }

    /**
     * 设置属性值后，提交方法
     */
    public void commit(){
        drawTextAndRect();
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }

    public int getX(){
        return mPoint.x;
    }

    public int getY(){
        return mPoint.y;
    }

    public void setX(int x){
        this.mPoint.x = x;
    }

    public void setY(int y){
        this.mPoint.y = y;
    }

    public int getTextSize(){
        return textSize;
    }

    public void setTextSize(int textSize){
        this.textSize = textSize;
    }
}
