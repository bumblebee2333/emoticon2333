package com.example.emotion.camera;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Author: shuike,
 * Email: shuike007@126.com,
 * Date: 2019/5/10.
 * PS:
 */
public class CameraPreView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder surfaceHolder;
    private Camera mCamera;

   /* public CameraPreView(Context context) {
        super(context);
    }

    public CameraPreView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CameraPreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CameraPreView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }*/

    public CameraPreView(Context context, Camera camera) {
        super(context);
        initView(context, camera);
    }

    private void initView(Context context, Camera camera) {
        /*final ViewGroup.LayoutParams layoutParams = getLayoutParams();
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layoutParams.height = getWidth();
                setLayoutParams(layoutParams);
            }
        });*/
        mCamera = camera;
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPictureFormat(PixelFormat.JPEG);
        parameters.setRotation(90);
        parameters.setPictureSize(parameters.getSupportedPictureSizes().get(0).width, parameters.getSupportedPictureSizes().get(0).height);

//        parameters.setPictureSize(getWidth()*2,getWidth()*2);
        mCamera.setParameters(parameters);

        mCamera.setDisplayOrientation(90);
//        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (surfaceHolder.getSurface() == null) {
            return;
        }

        mCamera.stopPreview();
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //根据当前朝向修改保存图片的旋转角度
    private void updateCameraOrientation(){
        if(mCamera!=null){
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setRotation(90);//生成的图片转90°
            //预览图片旋转90°
            mCamera.setDisplayOrientation(90);//预览转90°
            mCamera.setParameters(parameters);
        }
    }
}
