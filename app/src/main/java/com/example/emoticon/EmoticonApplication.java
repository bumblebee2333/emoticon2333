package com.example.emoticon;

import android.app.Application;

import com.example.common.app.ResourcesManager;
import com.example.common.utils.UserManager;

/**
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/9/7.
 * PS:
 */
public class EmoticonApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ResourcesManager.init(getApplicationContext());
        UserManager.init(getApplicationContext());
    }
}
