package com.example.emoticon.utils;

import android.content.Context;

import com.example.emoticon.app.Config;
import com.example.emoticon.service.OssService;

import java.io.File;

public class HttpUtils {
    public static void upLoadImage(Context context, String ossFolder, File imgFile, OssService.ProgressCallback progressCallback){
        Long time = System.currentTimeMillis();//获取时间戳
        String fileName = time + imgFile.getName().substring(imgFile.getName().lastIndexOf("."));
        String name = ossFolder + fileName;//拼接对象名
        //初始化OssService类，参数分别是Content，accessKeyId，accessKeySecret，endpoint，bucketName（后4个参数是阿里云oss中参数）
        OssService ossService = new OssService(context, Config.endpoint, Config.bucketName);
        //初始化OSSClient
        ossService.initOSSClient();
        //开始上传，参数分别为content，上传的文件名，上传的文件路径
        ossService.imageUpload(context, name, imgFile.getPath());
        //上传的进度回调
        ossService.setProgressCallback(progressCallback);
        /*ossService.setProgressCallback(new OssService.ProgressCallback() {
            @Override
            public void onProgressCallback(final double progress) {
                progressDialog.setProgress((int) progress);
                if (progress == 100) {
                    if (addType) {
                        addType(url);
                    }else {
                        submit(url);
                    }
                }
            }
        });*/
    }
}
