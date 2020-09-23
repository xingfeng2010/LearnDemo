package com.administrator.learndemo.rxjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.administrator.learndemo.LearnApplication;
import com.administrator.learndemo.R;
import com.administrator.learndemo.rxjava.ui.cache.CacheExampleActivity;
import com.administrator.learndemo.rxjava.ui.compose.ComposeOperatorExampleActivity;
import com.administrator.learndemo.rxjava.ui.pagination.PaginationActivity;
import com.administrator.learndemo.rxjava.ui.rxbus.RxBusActivity;
import com.administrator.learndemo.rxjava.ui.OperatorsActivity;
import com.administrator.learndemo.rxjava.ui.search.SearchActivity;

public class RxjavaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
    }

    public void startOperatorsActivity(View view) {
        startActivity(new Intent(RxjavaActivity.this, OperatorsActivity.class));
    }

    public void startNetworkingActivity(View view) {
       // startActivity(new Intent(RxjavaActivity.this, NetworkingActivity.class));
    }

    public void startCacheActivity(View view) {
        startActivity(new Intent(RxjavaActivity.this, CacheExampleActivity.class));
    }

    public void startRxBusActivity(View view) {
        ((LearnApplication) getApplication()).sendAutoEvent();
        startActivity(new Intent(RxjavaActivity.this, RxBusActivity.class));
    }

    public void startPaginationActivity(View view) {
        startActivity(new Intent(RxjavaActivity.this, PaginationActivity.class));
    }

    public void startComposeOperator(View view) {
        startActivity(new Intent(RxjavaActivity.this, ComposeOperatorExampleActivity.class));
    }

    public void startSearchActivity(View view) {
        startActivity(new Intent(RxjavaActivity.this, SearchActivity.class));
    }
}
