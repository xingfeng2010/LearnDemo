package com.administrator.learndemo.camera;

import android.Manifest;
import android.content.pm.PackageManager;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;

import com.administrator.learndemo.R;

public class CameraActivity extends AppCompatActivity {

    private MyGLSurfaceView mSurface;

    private Runnable initView = new Runnable() {
        @Override
        public void run() {
            setContentView(R.layout.activity_camera2);
            mSurface = (MyGLSurfaceView) findViewById(R.id.surface);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PackageManager.PERMISSION_GRANTED == this.
                    checkSelfPermission(Manifest.permission.CAMERA)) {
                initView.run();
            } else {
                this.requestPermissions(new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
            }
        } else {
            initView.run();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 10 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initView.run();
        }
    }
}
