package com.administrator.learndemo.viewpage.tab;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.administrator.learndemo.R;

import androidx.fragment.app.Fragment;

public class TabFragment extends Fragment {
    private View mRootView;
    
    private TextView mTxtTabApp;
    private TextView mTxtTabHome;
    private TextView mTxtTabUser;
    
    private OnTabClickInternalListener mTabClickInternalListener = new OnTabClickInternalListener();
    private OnTabClickListenser mOnTabClickListenser;
    
    private int mDefaultTab = 0;
    private int mCurrentTab = -1;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("Snser", "TabFragment onCreateView");
        mRootView = inflater.inflate(R.layout.viewpager_fragment_tab, container, false);
        initView(mRootView);
        return mRootView;
    }
    
    private void initView(View root) {
        mTxtTabApp = (TextView)root.findViewById(R.id.viewpager_fragment_tab_app);
        mTxtTabApp.setTag(Integer.valueOf(0));
        mTxtTabApp.setOnClickListener(mTabClickInternalListener);
        mTxtTabHome = (TextView)root.findViewById(R.id.viewpager_fragment_tab_home);
        mTxtTabHome.setTag(Integer.valueOf(1));
        mTxtTabHome.setOnClickListener(mTabClickInternalListener);
        mTxtTabUser = (TextView)root.findViewById(R.id.viewpager_fragment_tab_user);
        mTxtTabUser.setTag(Integer.valueOf(2));
        mTxtTabUser.setOnClickListener(mTabClickInternalListener);
        setCurrentTab(mDefaultTab);
    }
    
    public interface OnTabClickListenser {
        public void onTabClick(int tab);
    }
    
    private class OnTabClickInternalListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v != null && v.getTag() instanceof Integer) {
                final int tab = (Integer)v.getTag();
                if (tab != mCurrentTab && mOnTabClickListenser != null) {
                    setCurrentTab(tab);
                    mOnTabClickListenser.onTabClick(tab);
                }
            }
        }
    }
    
    public void setOnTabClickListenser(OnTabClickListenser listenser) {
        mOnTabClickListenser = listenser;
    }
    
    public void setCurrentTab(int tab) {
        if (mTxtTabApp != null && mTxtTabHome != null && mTxtTabUser != null) {
            mCurrentTab = tab;
            mTxtTabApp.setTextColor(getResources().getColor(R.color.viewpager_fragment_tab_normal));
            mTxtTabHome.setTextColor(getResources().getColor(R.color.viewpager_fragment_tab_normal));
            mTxtTabUser.setTextColor(getResources().getColor(R.color.viewpager_fragment_tab_normal));
            switch (tab) {
                case 0:
                    mTxtTabApp.setTextColor(getResources().getColor(R.color.viewpager_fragment_tab_current));
                    break;
                case 1:
                    mTxtTabHome.setTextColor(getResources().getColor(R.color.viewpager_fragment_tab_current));
                    break;
                case 2:
                    mTxtTabUser.setTextColor(getResources().getColor(R.color.viewpager_fragment_tab_current));
                    break;
                default:
                    break;
            }
        } else {
            //TabFragment在Activity的onResume之后才会onCreateView
            //setCurrentTab的时候控件还没初始化，存一下初始值在initView里再初始化
            mDefaultTab = tab;
        }
    }
}
