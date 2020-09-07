package com.administrator.learndemo.jetpack;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.OnLifecycleEvent;

import android.os.Bundle;
import android.util.Log;

import com.administrator.learndemo.R;

public class JetpackActivity extends AppCompatActivity {
    private static final String TAG = "JetpackActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jetpack);

        testLifecycle();

        testLiveData();
    }

    private void testLiveData() {
        MutableLiveData<String> liveString = new MutableLiveData<>();
        liveString.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String s) {
                Log.d(TAG, "onChanged() called with: s = [" + s + "]");
            }
        });

        liveString.postValue("程序亦非猿");
    }

    private void testLifecycle() {
        getLifecycle().addObserver(new LifecycleObserver() {

            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            void onResume() {
                Log.d(TAG, "LifecycleObserver onResume() called");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }
}
