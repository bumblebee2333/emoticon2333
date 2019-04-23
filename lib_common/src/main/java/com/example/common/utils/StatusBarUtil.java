package com.example.common.utils;

import android.content.Context;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

public class StatusBarUtil {
    private Context mContext;

    public StatusBarUtil(Context context){
        mContext = context;
    }
    public static void setWhiteBlack(Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
    }
}
