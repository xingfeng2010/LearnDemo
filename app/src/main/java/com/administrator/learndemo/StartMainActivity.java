package com.administrator.learndemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.administrator.learndemo.VelocityTracker.VelocityTrackerActivity;
import com.administrator.learndemo.activitymanager.TestActivity;
import com.administrator.learndemo.algorithm.AlgorithmMainActivity;
import com.administrator.learndemo.aop.AopEntryActivity;
import com.administrator.learndemo.camera.CameraActivity;
import com.administrator.learndemo.content.TestProviderActivity;
import com.administrator.learndemo.coroutines.FlowActivity;
import com.administrator.learndemo.coroutines.KotlinJobActivity;
import com.administrator.learndemo.coroutines.XieChengActivity;
import com.administrator.learndemo.coroutines.delegate.KotlinDelegateActivity;
import com.administrator.learndemo.dagger.DaggerTestActivity;
import com.administrator.learndemo.dns.DNSFoundActivity;
import com.administrator.learndemo.dns.DNSMainActivity;
import com.administrator.learndemo.dns.DNSServerActivity;
import com.administrator.learndemo.dynamic.TestDynamic;
import com.administrator.learndemo.jetpack.JetpackActivity;
import com.administrator.learndemo.keystore.KeyStoreActivity;
import com.administrator.learndemo.kotlin.KotlinActivity;
import com.administrator.learndemo.liefcycle.CustomObserver;
import com.administrator.learndemo.map.MainActivity;
import com.administrator.learndemo.map.NewRoutePlanActivity;
import com.administrator.learndemo.mediaplayer.MediaActivity;
import com.administrator.learndemo.mp4.Mp4Activity;
import com.administrator.learndemo.mvvm.ImageActivity;
import com.administrator.learndemo.opengl.OpenGLActivity;
import com.administrator.learndemo.opengl.image.ImageRenderActivity;
import com.administrator.learndemo.render.HelloShader;
import com.administrator.learndemo.retrofit.RetrofitActivity;
import com.administrator.learndemo.rxjava.RxjavaActivity;
import com.administrator.learndemo.testenum.AppShowState;
import com.administrator.learndemo.testenum.TestAppState;
import com.administrator.learndemo.util.EncryUtil;
import com.administrator.learndemo.video.VideoActivity;
import com.administrator.learndemo.view.ViewActivity;
import com.administrator.learndemo.viewpage.change.ViewPagerActivity;
import com.administrator.learndemo.viewpage.tab.ViewPagerFragmentActivity;
import com.administrator.learndemo.webview.MultWebviewActivity;
import com.administrator.learndemo.webview.WebviewCameraActivity;
import com.administrator.learndemo.wifi.TestNetworkActivity;
import com.administrator.learndemo.zhiwen.ZhiWenActivity;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kotlin.jvm.internal.Intrinsics;

public class StartMainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    static final String PUB_KEY="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDlOJu6TyygqxfWT7eLtGDwajtNFOb9I5XRb6khyfD1Yt3YiCgQWMNW649887VGJiGr/L5i2osbl8C9+WJTeucF+S76xFxdU6jE0NQ+Z+zEdhUTooNRaY5nZiu5PgDB0ED/ZKBUSLKL7eibMxZtMlUDHjm4gwQco1KRMDSmXSMkDwIDAQAB";
    static final String ENCRY_CONTENT = "{\n" +
            "  \"ip\": \"123.456.789\",\n" +
            "  \"port\": \"456\"\n" +
            "}";

    private ListView mListView;
    private LayoutInflater mLayoutInflator;
    private Class[] classes = new Class[]{
            MainActivity.class,
            NewRoutePlanActivity.class,
            VelocityTrackerActivity.class,
            MediaActivity.class,
            ViewActivity.class,
            OpenGLActivity.class,
            TestProviderActivity.class,
            ImageRenderActivity.class,
            CameraActivity.class,
            VideoActivity.class,
            Mp4Activity.class,
            ZhiWenActivity.class,
            KeyStoreActivity.class,
            DaoCheActivity.class,
            ImageCompressActivity.class,
            ViewPagerActivity.class,
            ViewPagerFragmentActivity.class,
            AlgorithmMainActivity.class,
            TestActivity.class,
            WebviewCameraActivity.class,
            DaggerTestActivity.class,
            KotlinActivity.class,
            RetrofitActivity.class,
            ImageActivity.class,
            JetpackActivity.class,
            RxjavaActivity.class,
            XieChengActivity.class,
            FlowActivity.class,
            KotlinDelegateActivity.class,
            MultWebviewActivity.class,
            KotlinJobActivity.class,
            AopEntryActivity.class,
            TestNetworkActivity.class,
            HelloShader.class,
            DNSMainActivity.class
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_main);

        mListView = (ListView) findViewById(R.id.list_view);

        mListView.setAdapter(new MyBaseAdapter());
        mListView.setOnItemClickListener(this);

        mLayoutInflator = LayoutInflater.from(this);

        this.getLifecycle().addObserver(new CustomObserver());
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        TestAppState testAppState = new TestAppState();
        testAppState.setAppState(AppShowState.RUNNING);

        testDynamicPropxy();

        Log.i("LISHIXING","StartMain onPostResume");

        CompositeDisposable disposable = new CompositeDisposable();

       Disposable posable = Observable.just("bevent")
               .delay(500, TimeUnit.MILLISECONDS)
                .map(aLong -> {
                    Log.i("LISHIXING","StartMain onPostResume aLong:" + aLong);
                      return 3;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                    Log.i("LISHIXING","StartMain onPostResume value:" + value);
                });
      //  disposable.add(posable);

        String conteng = null;
        try {
            conteng = EncryUtil.encrypt(ENCRY_CONTENT, PUB_KEY);
            Log.i("LISHIXING","StartMain onPostResume conteng:" + conteng);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i("LISHIXING","StartMain onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.i("LISHIXING","StartMain onStop");
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(StartMainActivity.this, classes[i]);
        StartMainActivity.this.startActivity(intent);
    }

    private class MyBaseAdapter extends BaseAdapter {
        private String[] classDescription = new String[]{
                "地图定位",
                "路径规划",
                "VelocityTracker",
                "MediaPlayer",
                "View和自定义动画",
                "OpenGL和GLSurfaceView",
                "TestProvider",
                "ImageRender",
                "Camera和GLSurfaceView",
                "TextureView",
                "Mp4 EGL",
                "指纹",
                "KeyStore",
                "DaoCheView",
                "图片压缩",
                "ViewPager+Fragment",
                "ViewPagerFragment",
                "算法",
                "Test ASM",
                "WebView拍照",
                "DAGGER示例",
                "Kotlin示例",
                "RETROFIT示例",
                "MVVM",
                "JETPACK TEST",
                "RXJAVA学习",
                "Kotlin协程",
                "Kotlin Flow",
                "Kotlin 委托",
                "多WebView展示",
                "Kotlin Job",
                "AOP 示例",
                "测试网络连接",
                "JPCT渲染示例",
                "mDNS示例"
        };

        @Override
        public int getCount() {
            return classDescription.length;
        }

        @Override
        public String getItem(int i) {
            return classDescription[i];
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            ViewHolder viewHolder;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = mLayoutInflator.inflate(R.layout.class_item, parent, false);
                viewHolder.textView = (TextView) view.findViewById(R.id.item_tv);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.textView.setText(classDescription[position]);

            return view;
        }
    }

    private static class ViewHolder {
        private TextView textView;
    }

    /**
     * 测试动态代理
     */
    private void testDynamicPropxy() {
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        TestDynamic testDynamic = new TestDynamic();
        testDynamic.testDynamicPropxy();
    }

    private void testHttp() {

    }
}
