package com.administrator.learndemo.rxjava.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.administrator.learndemo.R;
import com.administrator.learndemo.rxjava.ui.operator.AsyncSubjectExampleActivity;
import com.administrator.learndemo.rxjava.ui.operator.BehaviorSubjectExampleActivity;
import com.administrator.learndemo.rxjava.ui.operator.BufferExampleActivity;
import com.administrator.learndemo.rxjava.ui.operator.CompletableObserverExampleActivity;
import com.administrator.learndemo.rxjava.ui.operator.ConcatExampleActivity;
import com.administrator.learndemo.rxjava.ui.operator.DebounceExampleActivity;
import com.administrator.learndemo.rxjava.ui.operator.DeferExampleActivity;
import com.administrator.learndemo.rxjava.ui.operator.DelayExampleActivity;
import com.administrator.learndemo.rxjava.ui.operator.DisposableExampleActivity;
import com.administrator.learndemo.rxjava.ui.operator.DistinctExampleActivity;
import com.administrator.learndemo.rxjava.ui.operator.FilterExampleActivity;
import com.administrator.learndemo.rxjava.ui.operator.FlowableExampleActivity;
import com.administrator.learndemo.rxjava.ui.operator.IntervalExampleActivity;
import com.administrator.learndemo.rxjava.ui.operator.LastOperatorExampleActivity;
import com.administrator.learndemo.rxjava.ui.operator.MapExampleActivity;
import com.administrator.learndemo.rxjava.ui.operator.MergeExampleActivity;
import com.administrator.learndemo.rxjava.ui.operator.PublishSubjectExampleActivity;
import com.administrator.learndemo.rxjava.ui.operator.ReduceExampleActivity;
import com.administrator.learndemo.rxjava.ui.operator.ReplayExampleActivity;
import com.administrator.learndemo.rxjava.ui.operator.ReplaySubjectExampleActivity;
import com.administrator.learndemo.rxjava.ui.operator.ScanExampleActivity;
import com.administrator.learndemo.rxjava.ui.operator.SimpleExampleActivity;
import com.administrator.learndemo.rxjava.ui.operator.SingleObserverExampleActivity;
import com.administrator.learndemo.rxjava.ui.operator.SkipExampleActivity;
import com.administrator.learndemo.rxjava.ui.operator.SwitchMapExampleActivity;
import com.administrator.learndemo.rxjava.ui.operator.TakeExampleActivity;
import com.administrator.learndemo.rxjava.ui.operator.TakeUntilExampleActivity;
import com.administrator.learndemo.rxjava.ui.operator.TakeWhileExampleActivity;
import com.administrator.learndemo.rxjava.ui.operator.ThrottleFirstExampleActivity;
import com.administrator.learndemo.rxjava.ui.operator.ThrottleLastExampleActivity;
import com.administrator.learndemo.rxjava.ui.operator.TimerExampleActivity;
import com.administrator.learndemo.rxjava.ui.operator.WindowExampleActivity;
import com.administrator.learndemo.rxjava.ui.operator.ZipExampleActivity;

import androidx.appcompat.app.AppCompatActivity;

public class OperatorsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operators);
    }

    public void startSimpleActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, SimpleExampleActivity.class));
    }

    public void startMapActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, MapExampleActivity.class));
    }

    public void startZipActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, ZipExampleActivity.class));
    }

    public void startDisposableActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, DisposableExampleActivity.class));
    }

    public void startTakeActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, TakeExampleActivity.class));
    }

    public void startTimerActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, TimerExampleActivity.class));
    }

    public void startIntervalActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, IntervalExampleActivity.class));
    }

    public void startSingleObserverActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, SingleObserverExampleActivity.class));
    }

    public void startCompletableObserverActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, CompletableObserverExampleActivity.class));
    }

    public void startFlowableActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, FlowableExampleActivity.class));
    }

    public void startReduceActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, ReduceExampleActivity.class));
    }

    public void startBufferActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, BufferExampleActivity.class));
    }

    public void startFilterActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, FilterExampleActivity.class));
    }

    public void startSkipActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, SkipExampleActivity.class));
    }

    public void startScanActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, ScanExampleActivity.class));
    }

    public void startReplayActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, ReplayExampleActivity.class));
    }

    public void startConcatActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, ConcatExampleActivity.class));
    }

    public void startMergeActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, MergeExampleActivity.class));
    }

    public void startDeferActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, DeferExampleActivity.class));
    }

    public void startDistinctActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, DistinctExampleActivity.class));
    }

    public void startLastOperatorActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, LastOperatorExampleActivity.class));
    }

    public void startReplaySubjectActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, ReplaySubjectExampleActivity.class));
    }

    public void startPublishSubjectActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, PublishSubjectExampleActivity.class));
    }

    public void startBehaviorSubjectActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, BehaviorSubjectExampleActivity.class));
    }

    public void startAsyncSubjectActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, AsyncSubjectExampleActivity.class));
    }

    public void startThrottleFirstActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, ThrottleFirstExampleActivity.class));
    }

    public void startThrottleLastActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, ThrottleLastExampleActivity.class));
    }

    public void startDebounceActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, DebounceExampleActivity.class));
    }

    public void startWindowActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, WindowExampleActivity.class));
    }

    public void startDelayActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, DelayExampleActivity.class));
    }

    public void startSwitchMapActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, SwitchMapExampleActivity.class));
    }

    public void startTakeWhileActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, TakeWhileExampleActivity.class));
    }

    public void startTakeUntilActivity(View view) {
        startActivity(new Intent(OperatorsActivity.this, TakeUntilExampleActivity.class));
    }
}
