package com.administrator.learndemo.dns;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.administrator.learndemo.R;
import com.administrator.learndemo.algorithm.AlgorithmActivity;
import com.administrator.learndemo.algorithm.ArrayGrow;
import com.administrator.learndemo.algorithm.BroadcaseCoverActivity;
import com.administrator.learndemo.algorithm.DynaicPlanActivity;
import com.administrator.learndemo.algorithm.Fibonacci;
import com.administrator.learndemo.algorithm.IlandActivity;
import com.administrator.learndemo.algorithm.SparseArrayActivity;
import com.administrator.learndemo.algorithm.WideFirstActivity;

public class DNSMainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView mListView;
    private LayoutInflater mLayoutInflator;
    private Class[] classes = new Class[]{
            DNSFoundActivity.class,
            DNSServerActivity.class,
            FrontPage.class
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
        Intent intent = new Intent(DNSMainActivity.this, classes[i]);
        DNSMainActivity.this.startActivity(intent);
    }

    private class MyBaseAdapter extends BaseAdapter {
        private String[] classDescription = new String[]{
                "mDNS服务发现",
                "mDNS服务注册",
                "FronPage"
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
