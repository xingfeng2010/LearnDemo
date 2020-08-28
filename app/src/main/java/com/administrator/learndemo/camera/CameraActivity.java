package com.administrator.learndemo.camera;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.administrator.learndemo.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
