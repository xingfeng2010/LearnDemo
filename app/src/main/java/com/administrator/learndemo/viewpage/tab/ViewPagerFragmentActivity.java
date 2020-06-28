package com.administrator.learndemo.viewpage.tab;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;

import com.administrator.learndemo.R;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

//  各个Fragment的加载顺序：
//      如果 DEFAULT_PAGE = 1：
//          ViewPagerFragmentActivity onCreate
//          ViewPagerFragmentActivity onCreate setContentView
//          TitleFragment onCreateView (声明在layout布局中的Fragment在setContentView之后就会被加载)
//          ViewPagerFragmentActivity onCreate initView
//          TabFragment onCreateView (动态replace的Fragment在replace之后、onResume之前被加载)
//          ViewPagerFragmentActivity onResume
//          DateFragment onCreateView (ViewPager最先加载默认页)
//          ClickFragment onCreateView (ViewPager默认页左边的页面会被预加载)
//          AnimFragment onCreateView (ViewPager默认页右边的页面会被预加载)
//      如果 DEFAULT_PAGE = 0：
//          ViewPagerFragmentActivity onCreate
//          ViewPagerFragmentActivity onCreate setContentView
//          TitleFragment onCreateView (声明在layout布局中的Fragment在setContentView之后就会被加载)
//          ViewPagerFragmentActivity onCreate initView
//          TabFragment onCreateView (动态replace的Fragment在replace之后、onResume之前被加载)
//          ViewPagerFragmentActivity onResume
//          ClickFragment onCreateView (ViewPager最先加载默认页)
//          DateFragment onCreateView(ViewPager默认页右边的页面会被预加载)
//          AnimFragment onCreateView(ViewPager切换到DateFragment时才会预加载AnimFragment)

public class ViewPagerFragmentActivity extends FragmentActivity {
    private FragmentManager mManager = getSupportFragmentManager();
    
    private TitleFragment mTitleFragment;
    private TabFragment mTabFragment;
    private ViewPager mPager;
    
    private ViewPagerAdapter mAdapter;
    
    private static final int DEFAULT_PAGE = 1; //默认页面
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Snser", "ViewPagerFragmentActivity onCreate");
        super.onCreate(savedInstanceState);
        Log.d("Snser", "ViewPagerFragmentActivity onCreate setContentView");
        setContentView(R.layout.viewpager_fragment);
        Log.d("Snser", "ViewPagerFragmentActivity onCreate initView");
        initView();
    }
    
    private void initView() {
        mTitleFragment = (TitleFragment)mManager.findFragmentById(R.id.viewpager_fragment_title);
        mTabFragment = new TabFragment();
        mTabFragment.setOnTabClickListenser(new ViewPageTabClickListenser());
        mManager.beginTransaction().replace(R.id.viewpager_fragment_container, mTabFragment).commit();
        mPager = (ViewPager)findViewById(R.id.viewpager_fragment_pager);
        mPager.setAdapter(mAdapter = new ViewPagerAdapter(mManager));
        mPager.setOnPageChangeListener(new ViewPageChangeListener());
        setCurrentItem(DEFAULT_PAGE);
    }
    
    @Override
    protected void onResume() {
        Log.d("Snser", "ViewPagerFragmentActivity onResume");
        super.onResume();
    }
    
    private class ViewPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            setCurrentItem(position);
        }
        
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }
        
        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
    
    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<PagerFragment> mFragments = new ArrayList<PagerFragment>();
        
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragments.add(new ClickFragment());
            mFragments.add(new DateFragment());
            mFragments.add(new AnimFragment());
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
    
    private class ViewPageTabClickListenser implements TabFragment.OnTabClickListenser {
        @Override
        public void onTabClick(int tab) {
            setCurrentItem(tab);
        }
    }
       
    private void setCurrentItem(int item) {
        if (item == mPager.getCurrentItem()) {
            //此时是源于initView或onPageSelected的调用
            notifyPageChangeToFragments(item);
        } else {
            //此时是源于initView或onTabClick的调用，后续会自动触发一次onPageSelected
            mPager.setCurrentItem(item);
        }
    }
    
    private void notifyPageChangeToFragments(int item) {
        for (int page = 0; page != mAdapter.getCount(); ++page) {
            final Fragment fragment = mAdapter.getItem(page);
            if (fragment instanceof PagerFragment) {
                if (page == item) {
                    ((PagerFragment)fragment).onPageIn();
                } else {
                    ((PagerFragment)fragment).onPageOut();
                }
            }
        }
        mTitleFragment.setCurrentTab(item);
        mTabFragment.setCurrentTab(item);
    }
}
