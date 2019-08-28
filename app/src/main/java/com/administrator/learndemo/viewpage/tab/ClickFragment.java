package com.administrator.learndemo.viewpage.tab;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.administrator.learndemo.R;

public class ClickFragment extends PagerFragment {
    private View mViewRoot;
    private Button mBtnClick;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("Snser", "ClickFragment onCreateView");
        mViewRoot = inflater.inflate(R.layout.viewpager_fragment_click, container, false);
        initView(mViewRoot);
        return mViewRoot;
    }
    
    private void initView(View root) {
        mBtnClick = (Button)root.findViewById(R.id.viewpager_fragment_click_btn);
        mBtnClick.setOnClickListener(new MultiClickListener());
    }
    
    private static class MultiClickListener implements View.OnClickListener {
        private int mCount = 0;
        private long mLastClickTime = 0;
        
        private static final long MAX_CLICK_COUNT = 4;
        private static final long TIMEOUT_CLICK = 500;
        private static final int MSG_TIMEOUT_CLICK = 0x1001;
        
        private static Handler sHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MSG_TIMEOUT_CLICK && msg.obj instanceof Button) {
                    Log.d("Snser", "MultiClickListener 连续点击超时");
                    ((Button)msg.obj).setText("点我");
                }
            }
        };
        
        @Override
        public void onClick(View v) {
            if (v instanceof Button) {
                final long time = System.currentTimeMillis();
                if (mCount == 0 || time - mLastClickTime < TIMEOUT_CLICK) {
                    //这是连续点击
                    Log.d("Snser", "MultiClickListener 连续点击 mCount=" + (mCount + 1));
                    ++mCount;
                    mLastClickTime = time;
                    sHandler.removeMessages(MSG_TIMEOUT_CLICK);
                    sHandler.sendMessageDelayed(Message.obtain(sHandler, MSG_TIMEOUT_CLICK, v), TIMEOUT_CLICK);
                    if (mCount < MAX_CLICK_COUNT) {
                        ((Button)v).setText("点我   +" + mCount);
                    } else {
                        ((Button)v).setText("死机了！");
                    }
                } else {
                    //这是新的点击
                    Log.d("Snser", "MultiClickListener 新的点击 mCount=0");
                    mCount = 0;
                    onClick(v);
                }
            }
        }
    }
    
}
