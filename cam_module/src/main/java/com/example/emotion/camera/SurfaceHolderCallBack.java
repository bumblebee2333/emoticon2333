package com.example.emotion.camera;


import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Environment;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

/**
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/5/10.
 * PS:
 */
public class SurfaceHolderCallBack implements SurfaceHolder.Callback {
    private final String path;
    SurfaceView surfaceView;
    SurfaceHolder mHolder;
    Camera mCamera;
    MediaRecorder mediaRecorder;
    Context mContext;
    public SurfaceHolderCallBack(Context context, SurfaceView surfaceView){
        mContext = context;
        this.surfaceView = surfaceView;
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/hello.3gp";

        mHolder = surfaceView.getHolder();
        mCamera = Camera.open();
        mediaRecorder = new MediaRecorder();
        Camera.Parameters parameters = mCamera.getParameters();// 获得相机参数
        parameters.setPreviewSize(surfaceView.getWidth(), surfaceView.getWidth()); // 设置预览图像大小
        parameters.set("orientation", "portrait");
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains("continuous-video")) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }
        mCamera.getParameters().getSupportedPreviewSizes();
        mCamera.getParameters().getSupportedPictureSizes();

        mCamera.setParameters(parameters);// 设置相机参数
        doChanged(surfaceView.getHolder());
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mHolder = surfaceView.getHolder();
        mCamera = Camera.open();
        mediaRecorder = new MediaRecorder();
        Camera.Parameters parameters = mCamera.getParameters();// 获得相机参数
        parameters.setPreviewSize(surfaceView.getWidth(), surfaceView.getWidth()); // 设置预览图像大小
        parameters.set("orientation", "portrait");
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains("continuous-video")) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }
        mCamera.getParameters().getSupportedPreviewSizes();
        mCamera.getParameters().getSupportedPictureSizes();

        mCamera.setParameters(parameters);// 设置相机参数
        doChanged(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        doChanged(holder);
    }



    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private void doChanged(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.setDisplayOrientation(getDegree());
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecord(){
        mediaRecorder.release();
        mCamera.release();
        mediaRecorder = null;
        mCamera = Camera.open(1);
        mediaRecorder = new MediaRecorder();
        doChanged(surfaceView.getHolder());
    }

    public void startRecord() {
        mCamera.unlock();
        mediaRecorder.setCamera(mCamera);

        mediaRecorder.setPreviewDisplay(mHolder.getSurface());
        //设置音频的来源  麦克风
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //设置视频的来源
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        //设置视频的输出格式  3gp
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        //设置视频中的声音和视频的编码格式
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
        //设置保存的路径
        mediaRecorder.setOutputFile(path);
        //开始录制
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private int getDegree() {
        //获取当前屏幕旋转的角度
        int rotating = ((Activity)mContext).getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        //根据手机旋转的角度，来设置surfaceView的显示的角度
        switch (rotating) {
            case Surface.ROTATION_0:
                degree = 90;
                break;
            case Surface.ROTATION_90:
                degree = 0;
                break;
            case Surface.ROTATION_180:
                degree = 270;
                break;
            case Surface.ROTATION_270:
                degree = 180;
                break;
        }
        return degree;
    }
}
