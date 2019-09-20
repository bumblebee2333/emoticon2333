package com.example.emoticon.utils;

import android.content.Context;
import android.text.TextUtils;

import com.example.common.app.Config;
import com.example.emoticon.service.OssService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OSSHttpUtils {
    //图片上传有问题
    private static List<String> urls = new ArrayList<>();
    private static int i = 0;

    public static void upLoadImages(final Context context, final String ossFolder, final List<String> imgUrls, final OssService.FinishCallback finishCallback) {
        final List<String> imgUrl = new ArrayList<>(imgUrls);
        if (imgUrl.size() <= 0) {
            //上传完毕
            finishCallback.onFinish(urls);
            urls.clear();
            i = 0;
            return;
        }
        String url = imgUrl.get(0);
        if (TextUtils.isEmpty(url)) {
            imgUrl.remove(0);
            upLoadImages(context, ossFolder, imgUrl, finishCallback);
            return;
        }
        File imgFile = new File(url);

        if (!imgFile.exists()) {
            imgUrl.remove(0);
            upLoadImages(context, ossFolder, imgUrl, finishCallback);
            return;
        }

        Long time = System.currentTimeMillis();//获取时间戳
        String fileName = time + imgFile.getName().substring(imgFile.getName().lastIndexOf("."));
        String name = ossFolder + fileName;//拼接对象名
        //初始化OssService类，参数分别是Content，accessKeyId，accessKeySecret，END_POINT，BUCKET_NAME（后4个参数是阿里云oss中参数）
        OssService ossService = new OssService(context, Config.END_POINT, Config.BUCKET_NAME);
        //初始化OSSClient
        ossService.initOSSClient();
        //开始上传，参数分别为content，上传的文件名，上传的文件路径
        ossService.imageUpload(context, name, imgFile.getPath());
        //上传的进度回调
        ossService.setProgressCallback(new OssService.ProgressCallback() {
            @Override
            public void onProgressCallback(double progress, String url) {
                if (progress == 100) {
                    urls.add(url);
                    imgUrl.remove(0);
                    i++;
                    if (finishCallback != null) {
                        finishCallback.onProgressCallback(progress, i);
                    }
                    upLoadImages(context, ossFolder, imgUrl, finishCallback);
                }

            }
        });
    }
}
