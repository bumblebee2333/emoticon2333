package com.example.emoticon.editmodule.Operate;

import android.graphics.PointF;

import java.util.List;

/**
 * 多边形类
 */
public class Lasso {
    private float[] mPolyX,mPolyY;
    private int mPolySize;

    /**
     * 构造方法
     */
    public Lasso(List<PointF> pointFS){
        mPolySize = pointFS.size();

        mPolyX = new float[mPolySize];
        mPolyY = new float[mPolySize];

        for(int i=0;i<mPolySize;i++){
            mPolyX[i] = pointFS.get(i).x;
            mPolyY[i] = pointFS.get(i).y;
        }
    }

    /**
     * 判断多边形是否包含点
     */
    public boolean contains(float x,float y){
        boolean result = false;
        for(int i=0,j=mPolySize-1;i<mPolySize;j=i++){
            if((mPolyY[i] < y && mPolyY[j] >= y
                    || (mPolyY[j] < y && mPolyY[i] >= y))){
                if(mPolyX[i] + (y - mPolyY[i]) / (mPolyY[j] - mPolyY[i])
                        * (mPolyX[j] - mPolyX[i]) < x){
                    result = !result;
                }
            }
        }
        return result;
    }
}
