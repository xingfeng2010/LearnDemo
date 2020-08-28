package com.administrator.learndemo.webview;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.administrator.learndemo.R;
import com.tbruyelle.rxpermissions2.RxPermissions;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class WebviewCameraActivity extends AppCompatActivity {
    private static final String TAG = "WebviewCameraActivity";

    private EditText mUrlEdt;
    private Button mWebviewBtn;
    private Button mLocationBtn;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_camera);

        onePermission();//申请运行时权限

        initViews();
        initEvents();
    }


    private void initViews() {
        mUrlEdt = (EditText) findViewById(R.id.url_edt);
        mWebviewBtn = (Button) findViewById(R.id.webview_btn);
        mLocationBtn = (Button) findViewById(R.id.location_btn);
    }

    private void initEvents() {
        mWebviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String openUrl = "";
                if (!TextUtils.isEmpty(mUrlEdt.getText().toString())) {
                    openUrl = mUrlEdt.getText().toString();
                }
                Intent intent = new Intent(WebviewCameraActivity.this, MyWebviewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("urlKey", openUrl);
                intent.putExtras(bundle);
                WebviewCameraActivity.this.startActivity(intent);
            }
        });

        mLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String openUrl = "";
                Intent intent = new Intent(WebviewCameraActivity.this, MyWebviewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("urlKey", openUrl);
                intent.putExtras(bundle);
                WebviewCameraActivity.this.startActivity(intent);
            }
        });
    }


    /**
     * 只有一个运行时权限申请的情况
     */
    private void onePermission() {
        RxPermissions rxPermissions = new RxPermissions(WebviewCameraActivity.this); // where this is an Activity instance
        rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE) //权限名称，多个权限之间逗号分隔开
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        Log.e(TAG, "{accept}granted=" + granted);//执行顺序——1【多个权限的情况，只有所有的权限均允许的情况下granted==true】
                        if (granted) { // 在android 6.0之前会默认返回true
                            // 已经获取权限
                            Toast.makeText(WebviewCameraActivity.this, "已经获取权限", Toast.LENGTH_SHORT).show();
                        } else {
                            // 未获取权限
                            Toast.makeText(WebviewCameraActivity.this, "您没有授权该权限，请在设置中打开授权", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "{accept}");//可能是授权异常的情况下的处理
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.e(TAG, "{run}");//执行顺序——2
                    }
                });
    }
}
