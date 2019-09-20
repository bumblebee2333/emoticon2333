package com.example.common.utils;

import android.content.Context;
import android.widget.Toast;

import com.example.common.app.ResourcesManager;

/**
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/9/7.
 * PS:
 */
public class ToastUtils {

    public static void showToast(int resId) {
        Context context = ResourcesManager.getAppContext();
        Toast toast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
        toast.setText(context.getText(resId));
        toast.show();
    }

    public static void showToast(CharSequence text) {
        Context context = ResourcesManager.getAppContext();
        Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        toast.setText(text);
        toast.show();
    }
}
