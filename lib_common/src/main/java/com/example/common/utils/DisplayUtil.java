package com.example.common.utils;

import android.content.Context;

/*dp、sp 转化为 px 的工具*/
public class DisplayUtil {
    /*将dp、dip值转化为px值*/
    public static int dip2px(Context context,float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }

    public static int sp2px(Context context,float spValue){
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
