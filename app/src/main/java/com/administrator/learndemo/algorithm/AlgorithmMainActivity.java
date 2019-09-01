package com.administrator.learndemo.algorithm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.administrator.learndemo.DaoCheActivity;
import com.administrator.learndemo.ImageCompressActivity;
import com.administrator.learndemo.R;
import com.administrator.learndemo.VelocityTracker.VelocityTrackerActivity;
import com.administrator.learndemo.camera.CameraActivity;
import com.administrator.learndemo.content.TestProviderActivity;
import com.administrator.learndemo.keystore.KeyStoreActivity;
import com.administrator.learndemo.map.MainActivity;
import com.administrator.learndemo.map.NewRoutePlanActivity;
import com.administrator.learndemo.mediaplayer.MediaActivity;
import com.administrator.learndemo.mp4.Mp4Activity;
import com.administrator.learndemo.opengl.OpenGLActivity;
import com.administrator.learndemo.opengl.image.ImageRenderActivity;
import com.administrator.learndemo.video.VideoActivity;
import com.administrator.learndemo.view.ViewActivity;
import com.administrator.learndemo.viewpage.change.ViewPagerActivity;
import com.administrator.learndemo.viewpage.tab.ViewPagerFragmentActivity;
import com.administrator.learndemo.zhiwen.ZhiWenActivity;

public class AlgorithmMainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView mListView;
    private LayoutInflater mLayoutInflator;
    private Class[] classes = new Class[]{
            AlgorithmActivity.class,
            BroadcaseCoverActivity.class,
            WideFirstActivity.class,
            DynaicPlanActivity.class,
            Fibonacci.class,
            ArrayGrow.class
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(AlgorithmMainActivity.this, classes[i]);
        AlgorithmMainActivity.this.startActivity(intent);
    }

    private class MyBaseAdapter extends BaseAdapter {
        private String[] classDescription = new String[]{
                "0/1背包贪心算法",
                "广播集合覆盖问题",
                "广度优先搜素算法",
                "动态规划商品问题",
                "Fibonacci动态规划",
                "数组最大不连续递增子序列"
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
}
