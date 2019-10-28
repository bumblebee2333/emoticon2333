package com.example.emotion.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ComActivity extends AppCompatActivity implements Camera.AutoFocusCallback {
    FrameLayout fragmentView;
    Camera camera;
    private CameraPreView cameraPreView;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);
        fragmentView = findViewById(R.id.fragmentView);
//        path = Environment.getExternalStorageDirectory().getAbsolutePath() + System.currentTimeMillis()+".jpg";

        camera = getCameraInistance();
        cameraPreView = new CameraPreView(this, camera);

        final ViewGroup.LayoutParams layoutParams = fragmentView.getLayoutParams();
        fragmentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layoutParams.height = fragmentView.getWidth();
                fragmentView.setLayoutParams(layoutParams);
            }
        });

        fragmentView.addView(cameraPreView);
        /*fragmentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

    }
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = Uti.getOutputMediaFile(1);
            if (pictureFile == null){//对象为空，则文件不存在，直接返回
//                Log.d(TAG, "Error creating media file, check storage permissions: " +
//                        e.getMessage());
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d("", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("", "Error accessing file: " + e.getMessage());
            }
        }
    };
    public static Camera getCameraInistance(){
        Camera camera = null;
        camera = Camera.open(1);
        return camera;
    }
    public void pic(View view){
        camera.takePicture(null,null, new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                File pictureFile = Uti.getOutputMediaFile(1);
                if (pictureFile == null){//对象为空，则文件不存在，直接返回
//                Log.d(TAG, "Error creating media file, check storage permissions: " +
//                        e.getMessage());
                    return;
                }

                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();
                } catch (FileNotFoundException e) {
//                Log.d(TAG, "File not found: " + e.getMessage());
                } catch (IOException e) {
//                Log.d(TAG, "Error accessing file: " + e.getMessage());
                }
            }
        });
    }
    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {

    }
}
