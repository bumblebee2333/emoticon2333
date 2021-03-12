package com.example.emoticon.editmodule.Operate;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

public class ImageObject {
    //当前ImageObject是否可以被选中
    private boolean mSelected;
    //是否TextObject
    protected boolean isTextObject;
    protected float mScale = 1.0f;//初始化缩放的大小
    protected Bitmap srcBitmap;
    protected Bitmap rotateBm;
    protected Bitmap deleteBm;
    protected Bitmap scaleBm;
    protected Canvas canvas = null;
    protected Paint paint = new Paint();
    protected Point mPoint = new Point();
    protected float mRotation;
    protected boolean flipVertical;
    protected boolean flipHorizontal;
    protected int resizeBoxSize = 100;//这个到底是谁的大小???

    public ImageObject(){

    }

    public ImageObject(String text){

    }

    public ImageObject(Bitmap srcBm,Bitmap rotateBm,Bitmap deleteBm){
        this.srcBitmap = Bitmap.createBitmap(srcBm.getWidth(),
                srcBm.getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(srcBitmap);
        canvas.drawBitmap(srcBm,0,0,paint);
        this.rotateBm = rotateBm;
        this.deleteBm = deleteBm;
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
    }

    /**
     * 构造方法
     * @param srcBm 源图片
     * @param x 图片初始化x坐标
     * @param y 图片初始化y坐标
     * @param rotateBm 旋转图片
     * @param deleteBm 删除图片
     *
     */

    public ImageObject(Bitmap srcBm,int x,int y,Bitmap rotateBm,Bitmap deleteBm){
        this.srcBitmap = Bitmap.createBitmap(srcBm.getWidth(),srcBm.getHeight(),
                Bitmap.Config.ARGB_8888);
        canvas = new Canvas(this.srcBitmap);
        canvas.drawBitmap(srcBitmap,0,0,paint);
        mPoint.x = x;
        mPoint.y = y;
        this.rotateBm = rotateBm;
        this.deleteBm = deleteBm;
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
    }

    public int getWidth(){
        if(srcBitmap != null){
            return srcBitmap.getWidth();
        }
        return 0;
    }

    public int getHeight(){
        if(srcBitmap != null){
            return srcBitmap.getHeight();
        }
        return 0;
    }

    public void moveBy(int x,int y){
        mPoint.x += x;
        mPoint.y += y;
        setCenter();
    }

    /**
     * 判断点是否在多边形内
     */
    public boolean contains(float pointx,float pointy){
        Lasso lasso = null;
        List<PointF> listPoints = new ArrayList<>();
        listPoints.add(getPointLeftTop());
        listPoints.add(getPointRightTop());
        listPoints.add(getPointLeftBottom());
        listPoints.add(getPointRightBottom());
        lasso = new Lasso(listPoints);
        return lasso.contains(pointx,pointy);
    }

    public void draw(Canvas canvas){
        int sc1 = canvas.save();
        canvas.translate(mPoint.x,mPoint.y);
        canvas.scale(mScale,mScale);
        int sc2 = canvas.save();
        canvas.rotate(mRotation);
        canvas.scale((flipHorizontal ? -1:1),(flipVertical ? -1:1));
        canvas.drawBitmap(srcBitmap,-getWidth()/2,-getHeight()/2,paint);
        canvas.restoreToCount(sc2);
        canvas.restoreToCount(sc1);
    }

    /**
     * 绘画删除 旋转 缩放的图标
     */
    public void drawIcon(Canvas canvas){
        PointF deletePF = getPointLeftTop();
        canvas.drawBitmap(deleteBm,deletePF.x - deleteBm.getWidth()/2,
                deletePF.y-deleteBm.getHeight()/2,paint);

        PointF rotatePF = getPointRightBottom();
        canvas.drawBitmap(rotateBm,rotatePF.x-rotateBm.getWidth()/2,
                rotatePF.y - rotateBm.getHeight()/2,paint);
    }

    /**
     * 获取矩形框左上角的点
     */
    protected PointF getPointLeftTop(){
        PointF pointF = getPointByRotation(centerRotation - 180);
        return pointF;
    }

    /**
     * 获取矩形图片左上角在画布中的点
     */
    protected PointF getPointLeftTopInCanvas(){
        PointF pointF = getPointByRotationInCanvas(centerRotation - 180);
        return pointF;
    }

    /**
     * 获取矩形右上角的点
     */
    protected  PointF getPointRightTop(){
        PointF pointF = getPointByRotation(-centerRotation);
        return pointF;
    }

    /**
     * 获取矩形图片右上角在画布中的点
     */
    protected PointF getPointRightTopInCanvas(){
        PointF pointF = getPointByRotationInCanvas(-centerRotation);
        return pointF;
    }


    /**
     * 获取矩形框右下角的点
     */
    protected PointF getPointRightBottom(){
        PointF pointF = getPointByRotation(centerRotation);
        return pointF;
    }

    /**
     * 获取矩形图片右下角在画布中的点
     */
    protected PointF getPointRightBottomInCanvas(){
        PointF pointF = getPointByRotationInCanvas(centerRotation);
        return pointF;
    }

    /**
     * 获取图片左下角的点
     */
    protected PointF getPointLeftBottom(){
        PointF pointF = getPointByRotation(-centerRotation + 180);
        return pointF;
    }

    /**
     * 获取矩形图片左下角在画布中的点
     */
    protected PointF getPointLeftBottomInCanvas(){
        PointF pointF = getPointByRotationInCanvas(-centerRotation + 180);
        return pointF;
    }

    private float centerRotation;
    private float R;
    /**
     * 计算中心点的坐标
     */
    protected void setCenter(){
        double delX = getWidth() * mScale / 2;
        double delY = getHeight() * mScale / 2;
        R = (float) Math.sqrt(delX * delX + delY * delY);
        centerRotation = (float) Math.toDegrees(Math.atan(delY/delX));
    }

    /**
     * 根据旋转角度获取顶点坐标
     */
    protected PointF getPointByRotation(float rotation){
        PointF pointF = new PointF();
        double rot = (mRotation + rotation) * Math.PI / 180;
        pointF.x = getPoint().x + (float)(R*Math.cos(rot));
        pointF.y = getPoint().y + (float)(R*Math.sin(rot));
        return pointF;
    }

    protected PointF getPointByRotationInCanvas(float rotation){
        PointF pointF = new PointF();
        double rot = (mRotation + rotation) * Math.PI / 180;
        pointF.x = (float) (R * Math.cos(rot));
        pointF.y = (float) (R * Math.sin(rot));
        return pointF;
    }

    /**
     * 获取缩放和旋转点
     */
    protected PointF getResizeAndRotatePoint(){
        PointF pointF = new PointF();
        double width = getWidth();
        double height = getHeight();
        double r = Math.sqrt(width * width + height * height) / 2 * mScale;
        double temp = Math.toDegrees(Math.atan(height / width));
        double degrees = (mRotation + temp) * Math.PI / 180;
        pointF.x = (float) (r * Math.cos(degrees));
        pointF.y = (float) (r * Math.sin(degrees));
        return pointF;
    }

    /**
     * 判断点击是否在边角按钮上
     * @param x 触点的横坐标
     * @param y 触点的纵坐标
     * @param type 四角的位置
     */
    public boolean isPointOnCorner(float x,float y,int type){
        PointF point = null;
        float delX = 0;
        float delY = 0;
        if(OperateConstants.LEFTTOP == type){
            point = getPointLeftTop();
            delX = x - (point.x - deleteBm.getWidth() / 2);
            delY = y - (point.y - deleteBm.getHeight() / 2);
        }else if(OperateConstants.RIGHTBOTTOM == type){
            point = getPointRightBottom();
            delX = x - (point.x + rotateBm.getWidth() / 2);
            delY = y - (point.y + rotateBm.getHeight() / 2);
        }
        float diff = (float) Math.sqrt((delX * delX + delY  * delY));
        if(Math.abs(diff) <= resizeBoxSize)
            return true;
        else
            return false;
    }

    public Point getPoint(){
        return mPoint;
    }

    public Point getPositon(){
        return mPoint;
    }

    public void setScale(float scale){
        if(getWidth() * scale >= resizeBoxSize / 2
                && getHeight() * scale >= resizeBoxSize / 2){
            this.mScale = scale;
            setCenter();
        }
    }

    public float getScale(){
        return mScale;
    }

    public void setRotation(float rotation){
        this.mRotation = rotation;
    }

    public float getRotation(){
        return mRotation;
    }

    public void setTextObject(boolean isTextObject){
        this.isTextObject = isTextObject;
    }

    public boolean isTextObject(){
        return isTextObject;
    }

    public void setSelected(boolean isSelected){
        mSelected = isSelected;
    }

    public boolean isSelected(){
        return mSelected;
    }
}

