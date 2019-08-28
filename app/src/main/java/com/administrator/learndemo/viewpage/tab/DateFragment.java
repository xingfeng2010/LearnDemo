package com.administrator.learndemo.viewpage.tab;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.administrator.learndemo.R;

public class DateFragment extends PagerFragment {

    private View mViewRoot;
    private TextView mTxtDate;
    
    private SimpleDateFormat mFormat = new SimpleDateFormat("yyyy/MM/dd\nHH:mm:ss", Locale.CHINA);
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("Snser", "DateFragment onCreateView");
        mViewRoot = inflater.inflate(R.layout.viewpager_fragment_date, container, false);
        initView(mViewRoot);
        return mViewRoot;
    }
    
    @Override
    public void onPageIn() {
        super.onPageIn();
        refreshDate();
    }
    
    private void initView(View root) {
        mTxtDate = (TextView)root.findViewById(R.id.viewpager_fragment_date_txt);
        refreshDate();
    }
    
    private void refreshDate() {
        if (mTxtDate != null) {
            mTxtDate.setText(mFormat.format(new Date()));
        }
    }
    
}
