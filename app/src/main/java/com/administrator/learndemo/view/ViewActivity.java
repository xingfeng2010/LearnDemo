package com.administrator.learndemo.view;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;

import com.administrator.learndemo.R;

public class ViewActivity extends AppCompatActivity {

    private Button mButton;
    private Button mAnimButton;
    private Button mWrapperutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        mButton = (Button) this.findViewById(R.id.button_transition);
        TransitionDrawable drawable = (TransitionDrawable)mButton.getBackground();
        drawable.startTransition(2000);

        mAnimButton = (Button) this.findViewById(R.id.button_anim);
        mWrapperutton = (Button)this.findViewById(R.id.button_object_anim);
    }

    public void buttonClick(View view) {
        switch (view.getId()) {
            case R.id.button_anim:
                startAnim();
                break;
            case R.id.button_object_anim:
                performWrapperAnim();
                break;
        }
    }

    private void performWrapperAnim() {
        AnimWrapper wrapper = new AnimWrapper(mWrapperutton);
        ValueAnimator anim = ObjectAnimator.ofInt(wrapper,"width", 0, 2000);
        anim.setDuration(2000);
        anim.start();
    }


    private void startAnim() {
        ValueAnimator objec = ObjectAnimator.ofInt(mAnimButton,"backgroundColor",Color.GREEN,Color.DKGRAY);
        objec.setDuration(2000);
        objec.setInterpolator(new AccelerateDecelerateInterpolator());
        objec.setEvaluator(new ArgbEvaluator());
        objec.setRepeatCount(ValueAnimator.INFINITE);
        objec.setRepeatMode(ValueAnimator.REVERSE);
        objec.start();
    }

    private class AnimWrapper {
        private View mTargeView;
        public AnimWrapper(View view) {
            mTargeView = view;
        }

        public int getWidth() {
            return mTargeView.getLayoutParams().width;
        }

        public void setWidth(int width) {
            mTargeView.getLayoutParams().width = width;
            mTargeView.requestLayout();
        }
    }
}
