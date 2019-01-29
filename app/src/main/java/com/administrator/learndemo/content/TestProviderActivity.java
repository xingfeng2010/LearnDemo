package com.administrator.learndemo.content;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.administrator.learndemo.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

public class TestProviderActivity extends AppCompatActivity {

    private static String[] PERMISSIONS_LOCATION = {"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_LOCATION_EXTRA_COMMANDS", "android.permission.BLUETOOTH_PRIVILEGED"};
    private static String[] PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_LOCATION_EXTRA_COMMANDS",
            "android.permission.BLUETOOTH_PRIVILEGED"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_provider);


        requestPermission();
    }

    private void requestPermission() {
        //判断系统版本
        if (Build.VERSION.SDK_INT >= 23) {
            //检测当前app是否拥有某个权限
            int checkCallPhonePermission = this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
            //判断这个权限是否已经授权过
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                //判断是否需要 向用户解释，为什么要申请该权限
                this.requestPermissions(PERMISSIONS_LOCATION, 1);
                this.requestPermissions(PERMISSIONS_STORAGE, 1 + 1);
            }
        }
    }

    public void startTest(View view) {
        try {
            AssetFileDescriptor afd = getContentResolver().openAssetFileDescriptor(
                    Uri.parse("content://com.test.provider/h3c.txt"), "wr");
            OutputStream out = afd.createOutputStream();
            FileInputStream in = new FileInputStream(new File("/mnt/sdcard/h3c2.txt"));

            Log.i(TestContentProvider.TAG, "size : " + in.available());

            byte[] b = new byte[1024 * 5]; // 5KB
            int len;
            while ((len = in.read(b)) != -1) {
                out.write(b, 0, len);
            }
            out.flush();

            in.close();
            out.close();
            Log.i(TestContentProvider.TAG, "file close normal");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
