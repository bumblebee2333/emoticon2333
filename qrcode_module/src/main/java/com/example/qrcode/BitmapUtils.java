package com.example.qrcode;

import android.graphics.Bitmap;

import com.example.common.app.ResourcesManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/10/24.
 * PS:
 */
public class BitmapUtils {
    public static String saveBitmapToCacheDir(Bitmap bitmap){
        File cacheDir = ResourcesManager.getAppContext().getExternalCacheDir();
        String name = Long.toString(System.currentTimeMillis());
        File file = new File(cacheDir, name);
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            fileOutputStream.close();
            return file.getPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
