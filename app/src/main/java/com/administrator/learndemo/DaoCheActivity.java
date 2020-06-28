package com.administrator.learndemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.administrator.learndemo.opengl.util.MatrixHelper;
import com.administrator.learndemo.view.BezierEvaluator;
import com.administrator.learndemo.view.DaoCheView;
import com.administrator.learndemo.view.Point;

import androidx.appcompat.app.AppCompatActivity;

public class DaoCheActivity extends AppCompatActivity implements ValueAnimator.AnimatorUpdateListener{

    private DaoCheView mDaoCheView;

    private float[] matrix = new float[16];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_dao_che_view);

        mDaoCheView = (DaoCheView)this.findViewById(R.id.daocheview);

        MatrixHelper.perspectiveM(matrix, 45, 1080/720, 1f, 10f);
        Log.i("","");
    }

    public void startAnim(View view) {
        Point controllPoint = new Point(360, 360);

        Point startPosition = new Point(0, 720);
        Point endPosition = new Point(720, 720);

        BezierEvaluator bezierEvaluator = new BezierEvaluator(controllPoint);
        ValueAnimator anim = ValueAnimator.ofObject(bezierEvaluator, startPosition, endPosition);
        anim.addUpdateListener(DaoCheActivity.this);
        anim.setDuration(4000);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

            }
        });
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        Point point = (Point)valueAnimator.getAnimatedValue();
        mDaoCheView.setCenterX(point.x);
        mDaoCheView.setCenterY(point.y);
        mDaoCheView.invalidate();
    }
}
