package com.example.common.app;

import android.content.Context;
import android.content.res.Resources;

/**
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/9/7.
 * PS: 资源管理类
 */
public class ResourcesManager {
    private static Context sAppContext;
    private static Resources sRes;

    public static void init(Context context){
        sAppContext = context.getApplicationContext();
        sRes = context.getResources();
    }

    public static Context getAppContext() {
        return sAppContext;
    }

    public static Resources getRes() {
        return sRes;
    }
}
