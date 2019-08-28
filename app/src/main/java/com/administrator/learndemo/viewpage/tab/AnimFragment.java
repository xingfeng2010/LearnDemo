package com.administrator.learndemo.viewpage.tab;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.administrator.learndemo.R;

public class AnimFragment extends PagerFragment {
    private View mViewRoot;
    private ImageView mImg;
    
    private Animation mAnim;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("Snser", "AnimFragment onCreateView");
        mViewRoot = inflater.inflate(R.layout.viewpager_fragment_anim, container, false);
        mAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_fade_in);
        mAnim.setInterpolator(new LinearInterpolator());
        initView(mViewRoot);
        return mViewRoot;
    }
    
    private void initView(View root) {
        mImg = (ImageView)root.findViewById(R.id.viewpager_fragment_anim_img);
        startAnim();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAnim();
    }
    
    @Override
    public void onPageIn() {
        super.onPageIn();
        startAnim();
    }
    
    @Override
    public void onPageOut() {
        super.onPageOut();
        stopAnim();
    }
    
    private void startAnim() {
        if (mImg != null && mAnim != null) {
            mImg.startAnimation(mAnim);
        }
    }
    
    private void stopAnim() {
        if (mImg != null && mAnim != null) {
            mImg.clearAnimation();
        }
    }
    
}
