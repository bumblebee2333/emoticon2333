package com.example.emoticon.editmodule.Operate;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.emoticon.editmodule.R;

/**
 * 添加文字图片工具类
 */

public class OperateUtils {
    private Activity activity;
    private int screenWidth;//手机屏幕的宽(像素)
    private int screenHeight;//手机屏幕的高度(像素)

    public static final int LEFTTOP = 1;
    public static final int RIGHTTOP = 2;
    public static final int LEFTBOTTOM = 3;
    public static final int RIGHTBOTTOM = 4;
    public static final int CENTER = 5;

    public OperateUtils(Activity activity){
        this.activity = activity;
        if(screenWidth == 0){
            DisplayMetrics metric = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
            screenWidth = metric.widthPixels;
            screenHeight = metric.heightPixels;
        }
    }

    /**
     * 根据路径获取图片并且压缩，适应view
     *
     * @param filePath 图片路径
     * @param contentView 适应的view
     * @return Bitmap 压缩后的图片
     */
    public Bitmap compressionFilter(String filePath, View contentView){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath,options);
        int layoutHeight = contentView.getHeight();
        float scale = 0f;
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        scale = bitmapHeight > bitmapWidth
                ? layoutHeight / (bitmapHeight * 1f)
                : screenWidth / (bitmapWidth * 1f);
        Bitmap resizeBmp;
        if(scale != 0){
            Matrix matrix = new Matrix();
            matrix.postScale(scale,scale);
            resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmapWidth,bitmapHeight,
                    matrix,true);
        }else {
            resizeBmp = bitmap;
        }
        return resizeBmp;
    }

    /**
     * 添加文字的方法
     */
    public TextObject getTextObject(String text){
        TextObject textObject = null;

        Bitmap deleteBm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.delete);
        Bitmap rotateBm = BitmapFactory.decodeResource(activity.getResources(),R.drawable.rotate);

        textObject = new TextObject(activity,text,150,150,deleteBm,rotateBm);
        textObject.setTextObject(true);
        return textObject;
    }

    /**
     *
     */
    public TextObject getTextObject(String text,OperateView operateView,
                                    int quadrant,int x,int y){
        TextObject textObject = null;
        int width = operateView.getWidth();
        int height = operateView.getHeight();
        switch (quadrant){
            case LEFTTOP:
                break;
            case RIGHTTOP:
                x = width - x;
                break;
            case LEFTBOTTOM:
                y = height - y;
                break;
            case RIGHTBOTTOM:
                x = width - x;
                y = height - y;
                break;
            case CENTER:
                x = width / 2;
                y = height / 2;
                break;
            default:
                break;
        }
        Bitmap rotateBm = BitmapFactory.decodeResource(activity.getResources(),R.drawable.rotate);
        Bitmap deleteBm = BitmapFactory.decodeResource(activity.getResources(),R.drawable.delete);
        textObject = new TextObject(activity,text,x,y,deleteBm,rotateBm);
        textObject.setTextObject(true);
        return textObject;
    }
}
