package com.administrator.learndemo;

import android.content.Intent;
import android.os.Bundle;
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
import com.administrator.learndemo.camera.CameraActivity;
import com.administrator.learndemo.content.TestProviderActivity;
import com.administrator.learndemo.coroutines.XieChengActivity;
import com.administrator.learndemo.dagger.DaggerTestActivity;
import com.administrator.learndemo.download.ImageDownLoadActivity;
import com.administrator.learndemo.dynamic.TestDynamic;
import com.administrator.learndemo.jetpack.JetpackActivity;
import com.administrator.learndemo.keystore.KeyStoreActivity;
import com.administrator.learndemo.kotlin.KotlinActivity;
import com.administrator.learndemo.map.MainActivity;
import com.administrator.learndemo.map.NewRoutePlanActivity;
import com.administrator.learndemo.mediaplayer.MediaActivity;
import com.administrator.learndemo.mp4.Mp4Activity;
import com.administrator.learndemo.mvvm.DataBindActivity;
import com.administrator.learndemo.mvvm.ImageActivity;
import com.administrator.learndemo.opengl.OpenGLActivity;
import com.administrator.learndemo.opengl.image.ImageRenderActivity;
import com.administrator.learndemo.retrofit.RetrofitActivity;
import com.administrator.learndemo.rxjava.RxjavaActivity;
import com.administrator.learndemo.testenum.AppShowState;
import com.administrator.learndemo.testenum.TestAppState;
import com.administrator.learndemo.video.VideoActivity;
import com.administrator.learndemo.view.ViewActivity;
import com.administrator.learndemo.viewpage.change.ViewPagerActivity;
import com.administrator.learndemo.viewpage.tab.ViewPagerFragmentActivity;
import com.administrator.learndemo.webview.WebviewCameraActivity;
import com.administrator.learndemo.zhiwen.ZhiWenActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class StartMainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
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
            ImageDownLoadActivity.class,
            TestActivity.class,
            WebviewCameraActivity.class,
            DaggerTestActivity.class,
            KotlinActivity.class,
            RetrofitActivity.class,
            ImageActivity.class,
            JetpackActivity.class,
            RxjavaActivity.class,
            XieChengActivity.class
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_main);

        mListView = (ListView) findViewById(R.id.list_view);

        mListView.setAdapter(new MyBaseAdapter());
        mListView.setOnItemClickListener(this);

        mLayoutInflator = LayoutInflater.from(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        TestAppState testAppState = new TestAppState();
        testAppState.setAppState(AppShowState.RUNNING);

        testDynamicPropxy();

        Log.i("LISHIXING","StartMain onPostResume");
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
                "图片下载",
                "Test ASM",
                "WebView拍照",
                "DAGGER示例",
                "Kotlin示例",
                "RETROFIT示例",
                "MVVM",
                "JETPACK TEST",
                "RXJAVA学习",
                "Kotlin协程"
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
