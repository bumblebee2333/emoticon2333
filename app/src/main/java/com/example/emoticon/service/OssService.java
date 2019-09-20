package com.example.emoticon.service;

import android.content.Context;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSAuthCredentialsProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.example.common.app.Config;
import com.example.common.utils.ToastUtils;

import java.util.List;

public class OssService {

    private OSS oss;
    private String bucketName;
    private String endpoint;
    private Context context;
    private ProgressCallback progressCallback;

    public OssService(Context context, String endpoint, String bucketName) {
        this.context = context;
        this.endpoint = endpoint;
        this.bucketName = bucketName;
    }

    public void initOSSClient() {
        //OSSAuthCredentialsProvider类的值为服务端链接
        OSSCredentialProvider credentialProvider = new OSSAuthCredentialsProvider(Config.STS_SERVER);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(8); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        // oss为全局变量，endpoint是一个OSS区域地址
        oss = new OSSClient(context, endpoint, credentialProvider, conf);
    }

    public void imageUpload(Context context, String filename, String path) {
        final String url = Config.IMG_HOST +filename;//拼接图片链接，之后提交时会用到
        //objectName为oss对象名
        String objectName = filename;
        if (objectName == null || objectName.equals("")) {
            ToastUtils.showToast("文件名不能为空");
            return;
        }
        //下面3个参数依次为bucket名，Object名，上传文件路径
        PutObjectRequest put = new PutObjectRequest(bucketName, objectName, path);
        if (path == null || path.equals("")) {
            ToastUtils.showToast("请选择图片....");
            return;
        }

        // 异步上传，设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                double progress = currentSize * 1.0 / totalSize * 100.f;
                if (progressCallback != null) {
                    progressCallback.onProgressCallback(progress, url);
                }
            }
        });

        @SuppressWarnings("rawtypes")
        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                //System.out.print("上传成功");
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常;
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                }
            }
        });
        //task.cancel(); // 可以取消任务
        //task.waitUntilFinished(); // 可以等待直到任务完成
    }


    public ProgressCallback getProgressCallback() {
        return progressCallback;
    }

    public void setProgressCallback(ProgressCallback progressCallback) {
        this.progressCallback = progressCallback;
    }

    public interface ProgressCallback {
        void onProgressCallback(double progress,String url);
    }
    public interface FinishCallback {
        void onFinish(List<String> urls);
        void onProgressCallback(double progress,int position);
    }

}