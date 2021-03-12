package com.example.emoticon.editmodule.Operate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class OperateView extends View {
    private static final String TAG = "OperateView";
    private Bitmap bgBitmap;
    private Rect mCanvasLimits;
    private Paint paint = new Paint();
    //这个集合中用来存放所有添加在图片上的控件
    private List<ImageObject> lists = new ArrayList<>();
    //表示是否可以添加多个图片或文字
    private boolean isMultiAdd;
    //添加图片的大小要比正常的背景图片小
    private float picScale = 0.4f;

    public OperateView(Context context, Bitmap resizeBmp){
        super(context);

        bgBitmap = resizeBmp;
        int width = bgBitmap.getWidth();
        int height = bgBitmap.getHeight();
        mCanvasLimits = new Rect(0,0,width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int sc = canvas.save();
        canvas.clipRect(mCanvasLimits);
        canvas.drawBitmap(bgBitmap,0,0,paint);
        drawImages(canvas);
        canvas.restoreToCount(sc);
        for(ImageObject imgObj : lists){
            if(imgObj != null && imgObj.isSelected()){
                imgObj.drawIcon(canvas);
            }
        }
    }

    /**
     * 将图片对象添加到View中
     */
    public void addItem(ImageObject imgObject){
        if(imgObject == null)
            return;
        if(!isMultiAdd && lists != null)
            lists.clear();
        imgObject.setSelected(true);
        if(!imgObject.isTextObject())
            imgObject.setScale(picScale);
        ImageObject tempObject = null;
        for(int i=0;i<lists.size();i++){
            tempObject = lists.get(i);
            tempObject.setSelected(false);
        }
        lists.add(imgObject);
        invalidate();
    }

    /**
     * 循环画图像
     */
    private void drawImages(Canvas canvas){
        for(ImageObject imgObj : lists){
            if(imgObj != null){
                imgObj.draw(canvas);
            }
        }
    }

    public void save(){
        ImageObject imgObj = getSelected();
        if(imgObj != null){
            imgObj.setSelected(false);
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getPointerCount() == 1){
            handleSingleTouchManipulateEvent(event);
        }else {
            handleMultiTouchManipulateEvent(event);
        }
        invalidate();

        super.onTouchEvent(event);
        return true;
    }

    private boolean mMoveSinceDown = false;
    private boolean mResizeAndRotateSinceDown = false;
    private float mStartDistance = 0.0f;
    private float mStartScale = 0.0f;
    private float mStartRot = 0.0f;
    private float mPrevRot = 0.0f;
    public static final double ROTATION_STEP = 2.0;
    public static final double ZOOM_STEP = 0.01;
    public static final double CANVAS_SCALE_MIN = 0.25f;
    public static final double CANVAS_SCALE_MAX = 3.0f;
    private Point mPreviousPos = new Point(0,0);
    float diff;//双指触控 两点间的距离
    float rot;

    /**
     * 多点触控操作
     */
    private void handleMultiTouchManipulateEvent(MotionEvent event){
        switch (event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                float x1 = event.getX(0);
                float x2 = event.getX(1);
                float y1 = event.getY(0);
                float y2 = event.getY(1);
                float delX = x2 - x1;
                float delY = y2 - y1;
                diff = (float) Math.sqrt((delX * delX + delY * delY));
                mStartDistance = diff;
                mPrevRot = (float)Math.toDegrees(Math.atan2(delX,delY));
                for(ImageObject imgObj : lists){
                    if(imgObj.isSelected()){
                        mStartScale = imgObj.getScale();
                        mStartRot = imgObj.getRotation();
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                x1 = event.getX(0);
                x2 = event.getX(1);
                y1 = event.getY(0);
                y2 = event.getY(1);
                delX = x2 - x1;
                delY = y2 - y1;
                diff = (float)Math.sqrt(delX * delX + delY * delY);
                float scale = diff / mStartDistance;
                float newscale = mStartScale * scale;
                rot = (float)Math.toDegrees(Math.atan2(delX,delY));
                float rotdiff = mPrevRot - rot;
                for(ImageObject imgObj : lists){
                    if(imgObj.isSelected() && newscale < 10.0f && newscale > 0.1f){
                        float newrot = Math.round((mStartRot + rotdiff) / 1.0f);
                        if(Math.abs((newscale - imgObj.getScale()) * ROTATION_STEP) >
                                Math.abs(newrot - imgObj.getRotation())){
                            imgObj.setScale(newscale);
                        }else{
                            imgObj.setRotation(newrot % 360);
                        }
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
    }

    private long selectTime = 0;
    /**
     * 单点触控操作
     */
    private void handleSingleTouchManipulateEvent(MotionEvent event){
        long currentTime = 0;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mMoveSinceDown = false;
                mResizeAndRotateSinceDown = false;
                int selectedId = -1;

                //这部分判断是否点击弹出修改字的弹窗 这里实现的是双击
                for(int i = lists.size() - 1;i >= 0;i--){
                    ImageObject imgObj = lists.get(i);
                    if(imgObj.contains(event.getX(),event.getY())
                            || imgObj.isPointOnCorner(event.getX(),event.getY(),
                            OperateConstants.LEFTTOP)
                            || imgObj.isPointOnCorner(event.getX(),event.getY(),
                            OperateConstants.RIGHTBOTTOM)){
                        imgObj.setSelected(true);
                        lists.remove(i);
                        lists.add(imgObj);
                        selectedId = lists.size()-1;
                        currentTime = System.currentTimeMillis();
                        Log.e(TAG+"-current",String.valueOf(currentTime));
                        if(currentTime - selectTime < 300){
                            currentTime = 0;
                            if(myListener != null){
                                if(getSelected().isTextObject()){
                                    myListener.onClick((TextObject)getSelected());
                                }
                            }
                        }
                        selectTime = currentTime;
                        Log.e(TAG+"-select",String.valueOf(selectTime));
                        break;
                    }
                }
                //特殊情况
                if(selectedId < 0){
                    for(int i=lists.size()-1;i>=0;i--){
                        ImageObject imgObj = lists.get(i);
                        if(imgObj.contains(event.getX(),event.getY())
                                || imgObj.isPointOnCorner(event.getX(),event.getY(),
                                OperateConstants.LEFTTOP)
                                || imgObj.isPointOnCorner(event.getX(),event.getY(),
                                OperateConstants.RIGHTBOTTOM)){
                            imgObj.setSelected(true);
                            lists.remove(i);
                            lists.add(imgObj);
                            selectedId = lists.size()-1;
                            break;
                        }
                    }
                }
                for(int i=0;i<lists.size();i++){
                    ImageObject imgObj = lists.get(i);
                    if(i != selectedId){
                        imgObj.setSelected(false);
                    }
                }
                ImageObject imgObj = getSelected();
                if(imgObj != null){
                    if(imgObj.isPointOnCorner(event.getX(),event.getY()
                            ,OperateConstants.LEFTTOP)){
                        lists.remove(imgObj);
                    }else if(imgObj.isPointOnCorner(event.getX(),event.getY(),
                            OperateConstants.RIGHTBOTTOM)){
                        mResizeAndRotateSinceDown = true;
                        float x = event.getX();
                        float y = event.getY();
                        float delX = x - imgObj.getPoint().x;
                        float delY = y - imgObj.getPoint().y;
                        diff = (float)Math.sqrt(delX * delX + delY * delY);
                        mStartDistance = diff;
                        mPrevRot = (float)Math.toDegrees(Math.atan2(delX,delY));
                        mStartScale = imgObj.getScale();
                        mStartRot = imgObj.getRotation();
                    }else if(imgObj.contains(event.getX(),event.getY())){
                        mMoveSinceDown = true;
                        mPreviousPos.x = (int) event.getX();
                        mPreviousPos.y = (int) event.getY();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(mMoveSinceDown){
                    int curX = (int)event.getX();
                    int curY = (int)event.getY();
                    int diffX = curX - mPreviousPos.x;
                    int diffY = curY - mPreviousPos.y;
                    mPreviousPos.x = curX;
                    mPreviousPos.y = curY;
                    imgObj = getSelected();
                    Point p = imgObj.getPositon();
                    int x = p.x + diffX;
                    int y = p.y + diffY;
                    if(p.x + diffX >= mCanvasLimits.left
                            && p.x + diffX <= mCanvasLimits.right
                            && p.y + diffY >= mCanvasLimits.top
                            && p.y + diffY <= mCanvasLimits.bottom){
                        imgObj.moveBy((int) diffX,(int) diffY);
                    }
                }

                //旋转和缩放
                if(mResizeAndRotateSinceDown){
                    imgObj = getSelected();
                    float x = event.getX();
                    float y = event.getY();
                    float delX = x - imgObj.getPoint().x;
                    float delY = y - imgObj.getPoint().y;
                    diff = (float) Math.sqrt(delX * delX + delY * delY);
                    float scale = diff / mStartDistance;
                    float newscale = mStartScale * scale;
                    rot = (float) Math.toDegrees(Math.atan2(delX,delY));
                    float rotdiff = mPrevRot - rot;
                    if(newscale < 10.0f && newscale > 0.1f){
                        float newrot = Math.round((mStartRot + rotdiff) / 1.0f);
                        if(Math.abs((newscale - imgObj.getScale()) * ROTATION_STEP)
                                > Math.abs(newrot - imgObj.getRotation())){
                            imgObj.setScale(newscale);
                        }else {
                            imgObj.setRotation(newrot % 360);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mMoveSinceDown = false;
                mResizeAndRotateSinceDown = false;
                //currentTime = 0;
                break;
        }
    }

    /**
     * 获取选中的对象ImageObject
     */
    private ImageObject getSelected(){
        for(ImageObject imgObj : lists){
            if(imgObj.isSelected()){
                return imgObj;
            }
        }
        return null;
    }

    public void setMultiAdd(boolean isMultiAdd){
        this.isMultiAdd = isMultiAdd;
    }

    private void setScale(float picScale){
        this.picScale = picScale;
    }

    /**
     * 向外部提供双击监听事件
     */
    MyListener myListener;

    public interface MyListener{
        public void onClick(TextObject textObject);
    }

    public void setOnClickListener(MyListener myListener){
        this.myListener = myListener;
    }
}
