package com.example.emoticon.editmodule.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.example.common.utils.RectUtil;
import com.example.emoticon.editmodule.R;

public class TextEditBox extends View{
    //字体的大小
    public final int TEXT_SIZE_DEFAULT = getResources().getDimensionPixelSize(R.dimen.fontsize_default);
    public final int PADDING = 20;
    public final int PADDING_TOP = 30;
    public final int STICKER_BIN_HALF_SIZE = 40;
    //为文本赋初值
    private String mText = getResources().getString(R.string.input_hint);
    //初始画笔
    private TextPaint mTextPaint = new TextPaint();//写字的画笔
    private Paint mPaint = new Paint();//文本框的画笔
    //文字框没有边距
    private Rect mTextRect = new Rect();
    //文本框
    private RectF mTextEditBox = new RectF();

    //图片 截图的大小
    private Rect mDeleteRect = new Rect();
    private Rect mRotateRect = new Rect();
    private Rect mScaleRect = new Rect();
    //将图片进行缩放
    private RectF mDeleteDsRect = new RectF();
    private RectF mRotateDsRect = new RectF();
    private RectF mScaleDsRect = new RectF();

    private Bitmap mDeleteBitmap;
    private Bitmap mRotateBitmap;
    private Bitmap mScaleBitmap;

    private EditText mEditText;//输入控件

    private int mAlpha  = 255;//设置画笔的透明度

    public int layout_x = 0;
    public int layout_y = 0;

    public float mRotateAngle = 0;
    public float mScale = 1;
    private boolean isInitLayout = true;
    private boolean isShowEditBox = true;//是否重新绘制TextEditBox的边框

    private int mCurrentMode = NORMAL_MODE;
    //控件的几种模式
    public static final int NORMAL_MODE = 1;//正常
    public static final int ROTATE_MODE = 2;//旋转
    public static final int SCALE_MODE = 3;//缩放
    public static final int MOVE_MODE = 4;//移动
    public static final int DELETE_MODE = 5;//删除
    public static final int TWO_FINGER = 6;//双指移动

    public int last_x = 0;//点击屏幕时 相对Android坐标
    public int last_y = 0;

    public float last_X = 0;
    public float last_Y = 0;

    private float dx = 0;
    private float dy = 0;

    private static final int TOP_LEFT = 1;//左上
    private static final int BOTTOM_LEFT = 2;//左下
    private static final int UPPER_RIGHT = 3;//右上
    private static final int BOTTOM_RIGHT = 4;//右下

    private boolean isScaled = false;

    private long currentTime;//移动消费时间

    /*多点触碰的变量定义*/
    private final static int INVALID_ID = -1;
    private int mActivePointerId = INVALID_ID;
    private int mSecondaryPointerId = INVALID_ID;
    private float mPrimaryLastX = -1;
    private float mPrimaryLastY = -1;
    private float mSecondaryLastX = -1;
    private float mSecondaryLastY = -1;
    private float newDis = 0;
    private float oldDis = 0;

    public TextEditBox(Context context) {
        super(context, null);
        initView(context);
    }

    public TextEditBox(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initView(context);
    }

    public TextEditBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void setmEditText(EditText textView){
        this.mEditText = textView;
    }

    @SuppressLint("ResourceAsColor")
    private void initView(Context context){
        //初始化图片数据
        mDeleteBitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.delete);
        mRotateBitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.rotate);
        mScaleBitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.scale);
        //将矩形的坐标设置为指定的值
        mDeleteRect.set(0,0,mDeleteBitmap.getWidth(),mDeleteBitmap.getHeight());
        mRotateRect.set(0,0,mRotateBitmap.getWidth(),mRotateBitmap.getHeight());
        mScaleRect.set(0,0,mScaleBitmap.getWidth(),mScaleBitmap.getHeight());

        mDeleteDsRect = new RectF(0,0,STICKER_BIN_HALF_SIZE << 1,
                STICKER_BIN_HALF_SIZE << 1);
        mRotateDsRect = new RectF(0,0,STICKER_BIN_HALF_SIZE << 1,
                STICKER_BIN_HALF_SIZE << 1);
        mScaleDsRect = new RectF(0,0,STICKER_BIN_HALF_SIZE << 1,
                STICKER_BIN_HALF_SIZE << 1);
        //初始化字体画笔
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextAlign(Paint.Align.CENTER);//设置文字的对齐方式
        mTextPaint.setTextSize(TEXT_SIZE_DEFAULT);
        mTextPaint.setAntiAlias(true);//平滑绘制边缘
        mTextPaint.setAlpha(mAlpha);
        //设置画文本框的画笔
        mPaint.setColor(Color.parseColor("#4f94cd"));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(4);

        setClickable(true);
    }

    public void resetView(){
        //int height = ScreenUtils.getScreenHeight(getContext());
        layout_x = getMeasuredWidth()/2;
        layout_y = getMeasuredHeight()/2;
        mRotateAngle = 0;
        mScale = 1;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(isInitLayout){
            isInitLayout = false;
            resetView();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(TextUtils.isEmpty(mText))
            return;

        drawContent(canvas);
    }

    private void drawContent(Canvas canvas){
        drawText(canvas);

        int offsetValue = ((int)mDeleteDsRect.width()) >> 1;
        mDeleteDsRect.offsetTo(mTextEditBox.left - offsetValue,mTextEditBox.top - offsetValue);
        mScaleDsRect.offsetTo(mTextEditBox.right-offsetValue,mTextEditBox.bottom-offsetValue);
        mRotateDsRect.offsetTo(mTextEditBox.left - offsetValue,mTextEditBox.bottom-offsetValue);

        RectUtil.rotateRect(mRotateDsRect,mTextEditBox.centerX(),mTextEditBox.centerY(),mRotateAngle);
        RectUtil.rotateRect(mDeleteDsRect,mTextEditBox.centerX(),mTextEditBox.centerY(),mRotateAngle);
        RectUtil.rotateRect(mScaleDsRect,mTextEditBox.centerX(),mTextEditBox.centerY(),mRotateAngle);

        if(isScaled){
            RectUtil.moveRect(mDeleteDsRect,dx,dy,1);
            RectUtil.moveRect(mRotateDsRect,dx,dy,2);
            RectUtil.moveRect(mScaleDsRect,dx,dy,4);
        }

        if(!isShowEditBox)
            return;

        canvas.save();
        canvas.rotate(mRotateAngle,mTextEditBox.centerX(),mTextEditBox.centerY());
        canvas.scale(mScale,mScale,mTextEditBox.centerX(),mTextEditBox.centerY());
        canvas.drawRoundRect(mTextEditBox,10,10,mPaint);
        canvas.restore();

        if(!isShowEditBox)
            return;

        canvas.drawBitmap(mDeleteBitmap,mDeleteRect,mDeleteDsRect,null);
        canvas.drawBitmap(mRotateBitmap,mRotateRect,mRotateDsRect,null);
        canvas.drawBitmap(mScaleBitmap,mScaleRect,mScaleDsRect,null);
    }
    //绘制出文字的位置 固定处TextEditBox的边框的存在位置
    private void drawText(Canvas canvas){
        drawText(canvas,layout_x,layout_y,mScale,mRotateAngle);
    }

    public void drawText(Canvas canvas,int _x,int _y,float  scale,float rotate){
        if(TextUtils.isEmpty(mText))
            return;

        int x = _x;
        int y = _y;

        mTextPaint.getTextBounds(mText,0,mText.length(),mTextRect);
        mTextRect.offset(x-(mTextRect.width() >> 1),y);

        mTextEditBox.set(mTextRect.left-PADDING,mTextRect.top-PADDING_TOP,
                mTextRect.right+PADDING,mTextRect.bottom+PADDING_TOP);

        RectUtil.scaleRect(mTextEditBox,scale);

        canvas.save();//保存当前画布状态 快照
        canvas.scale(scale,scale,mTextEditBox.centerX(),mTextEditBox.centerY());//对画布进行缩放
        canvas.rotate(rotate,mTextEditBox.centerX(),mTextEditBox.centerY());
        canvas.drawText(mText,x,y,mTextPaint);
        canvas.restore();//回滚 调用后恢复到调用之前的坐标状态
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();

        switch (action & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                if(mRotateDsRect.contains(x,y)){//旋转模式
                    isShowEditBox = true;
                    mCurrentMode = ROTATE_MODE;
                    last_x= (int)event.getRawX();
                    last_y = (int)event.getRawY();

                    currentTime = System.currentTimeMillis();

                }else if(mScaleDsRect.contains(x,y)){//缩放模式
                    isShowEditBox = true;
                    mCurrentMode = SCALE_MODE;
                    last_X = mScaleDsRect.centerX();
                    last_Y = mScaleDsRect.centerY();

                    currentTime = System.currentTimeMillis();

                }else if(mTextEditBox.contains(x,y)){//移动模式
                    isShowEditBox = true;
                    mCurrentMode = MOVE_MODE;
                    last_X = x;
                    last_Y = y;

                    //一个手指触碰
                    //获得第一个point id
                    mActivePointerId = event.getPointerId(event.getActionIndex());
                    mPrimaryLastX = x;
                    mPrimaryLastY = y;

                    currentTime = System.currentTimeMillis();
                    //invalidate();
                }else if(mDeleteDsRect.contains(x,y)){//删除模式
                    isShowEditBox = true;
                    mCurrentMode = DELETE_MODE;
                    setVisibility(TextEditBox.GONE);
                }else {
                    isShowEditBox = false;
                    invalidate();
                }

                if(mCurrentMode == DELETE_MODE){
                    mCurrentMode = NORMAL_MODE;
                    //clearTextContent();
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                mCurrentMode = TWO_FINGER;
                mSecondaryPointerId = event.getPointerId(event.getActionIndex());
                try{
                    mSecondaryLastX = x;
                    mSecondaryLastY = y;
                    oldDis = spacing(mPrimaryLastX,mPrimaryLastY,mSecondaryLastX,mSecondaryLastY);
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if(mCurrentMode == ROTATE_MODE){
                    float width = mTextEditBox.right - mTextEditBox.left;
                    float height = mTextEditBox.bottom - mTextEditBox.top;
                    int center_x = (int)(mTextEditBox.left+width/2);
                    int  center_y = (int)(mTextEditBox.top+height/2);
                    Point center = new Point(center_x,center_y);
                    Point first = new Point(last_x,last_y);
                    Point second = new Point((int)event.getRawX(),(int)event.getRawY());

                    mRotateAngle += calculateAngle(center,first,second);

                    invalidate();

                    last_x = (int)event.getRawX();
                    last_y = (int)event.getRawY();
                }else if(mCurrentMode == SCALE_MODE){
                    dx = x - last_X;
                    dy = y - last_Y;

                    if(dx > 0 && dy > 0){
                        mScale = calculateScaling(dx,dy);
                        isScaled = true;

                        //scaleMatrix.setScale(scale,scale,mTextEditBox.centerX(),mTextEditBox.centerY());

                        invalidate();
                    }
                    last_x = (int)event.getRawX();
                    last_y = (int)event.getRawY();
                }else if(mCurrentMode == MOVE_MODE){
                    float _dx = event.getX() - last_X;
                    float _dy = event.getY() - last_Y;

                    //scaleMatrix.postTranslate(dx, dy);

                    layout_x += _dx;
                    layout_y += _dy;

                    invalidate();

                    last_X = x;
                    last_Y = y;
                }
                else if(mCurrentMode == TWO_FINGER){
                    float first_x,first_y,sec_x,sec_y;
                    try{
                        int firstIndex = event.findPointerIndex(mActivePointerId);
                        int secondIndex = event.findPointerIndex(mSecondaryPointerId);
                        first_x = event.getX(firstIndex);
                        first_y = event.getY(firstIndex);
                        sec_x = event.getX(secondIndex);
                        sec_y = event.getY(secondIndex);
                        newDis = spacing(first_x,first_y,sec_x,sec_y);
                    }catch (IllegalArgumentException e){
                        e.printStackTrace();
                    }
                    mScale = newDis / oldDis;
                    isScaled = true;
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_UP:
                //判断是否单机编辑框
                long elapsedTime = System.currentTimeMillis() - currentTime;//移动等事件
                //判断是否继续传递信号
                if(elapsedTime <= 200 && (dx <= 1 || dy <= 1)){
                    //点击事件 自己消费
                    if(mOnEditClickListener != null){
                        mOnEditClickListener.onTextEditClick(this);
                    }
                    return true;
                }
                mCurrentMode = NORMAL_MODE;
                break;

            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = INVALID_ID;
                mPrimaryLastX = -1;
                mPrimaryLastY = -1;
                break;
        }
        return super.onTouchEvent(event);
    }

    public float calculateAngle(Point cen, Point first, Point second){
        float dx1, dx2, dy1, dy2;

        dx1 = first.x - cen.x;
        dy1 = first.y - cen.y;
        dx2 = second.x - cen.x;
        dy2 = second.y - cen.y;

        //计算三边的平方
        float ab = (second.x - first.x)*(second.x - first.x)+(second.y - first.y)*(second.y - first.y);
        float bc = dx1*dx1+dy1*dy1;
        float ac = dx2*dx2+dy2*dy2;

        //根据两向量的叉乘来判断顺逆时针
        boolean isClockwise = (dx1 * dy2 - dy1 * dx2) > 0;

        //根据余弦定理计算旋转角的余弦值
        double cosDegree = (bc + ac - ab)/(2*Math.sqrt(bc)*Math.sqrt(ac));

        //异常处理 因为算出来会有误差绝对值可能会超过一 处理一下
        if(cosDegree > 1){
            cosDegree = 1;
        }
        else if(cosDegree < -1){
            cosDegree = -1;
        }

        //计算弧度
        double radian = Math.acos(cosDegree);

        //计算旋转过的角度，顺时针为正，逆时针为负
        return (float)(isClockwise ? Math.toDegrees(radian) : -Math.toDegrees(radian));
    }

    public float spacing(float First_X,float First_Y,float Sec_X,float Sec_Y){
        float dx = First_X - Sec_X;
        float dy = First_Y - Sec_Y;
        float distance = (float)Math.sqrt(dx * dx + dy *dy);
        return distance;
    }

    public float calculateScaling(final float dx , final  float dy){
        float box_x = mTextEditBox.centerX();
        float box_y = mTextEditBox.centerY();

        float scale_x = mScaleDsRect.centerX();
        float scale_y = mScaleDsRect.centerY();

        float n_x = scale_x + dx;
        float n_y = scale_y + dy;

        float xa = scale_x - box_x;
        float ya = scale_y - box_y;

        float xb = n_x - box_x;
        float yb = n_y - box_y;

        float srcLen = (float)Math.sqrt(xa * xa + ya * ya);
        float curLen = (float)Math.sqrt(xb * xb + yb * yb);

        float scale = curLen / srcLen;

        mScale *= scale;

        return scale;
    }

    public void clearTextContent(){
        if(mEditText != null){
            mEditText.setText(null);
        }
    }

    public void setText(String text){
        this.mText = text;
        invalidate();
    }

    public String getmText() {
        return mText;
    }

    //定义一个接口对象listener /*向外部提供监听事件*/
    private OnTextEditClickListener mOnEditClickListener;

    public interface OnTextEditClickListener{
        void onTextEditClick(TextEditBox textEditBox);
    }

    public void setOnEditClickListener(OnTextEditClickListener listener){
        mOnEditClickListener = listener;
    }
}
