package com.example.emoticon.editmodule.Graffiti;

import android.graphics.Canvas;
import android.os.Parcelable;

/*基础类 画所有图形*/
public abstract class DoodleAction implements Parcelable {

    protected int color;

    protected  float strokeWidth;

    public DoodleAction(){}

    public int getColor(){
        return color;
    }

    public void setColor(int color){
        this.color = color;
    }

    public float getStrokeWidth(){
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth){
        this.strokeWidth = strokeWidth;
    }

    public String toString(){
        return "DoodleAction{" + ", color=" + color +
                ", strokeWidth=" + strokeWidth + '}';
    }

    /**
     * 绘制当前动作内容
     *
     * @param canvas 新画布
     */
    public abstract void draw(Canvas canvas);

    /**
     * 根据手指移动坐标进行绘制
     *
     * @param x
     * @param y
     */
    public abstract void move(float x,float y);
}
