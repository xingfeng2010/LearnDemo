package com.administrator.learndemo.rxjava.ui.operator;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.administrator.learndemo.R;
import com.administrator.learndemo.rxjava.model.User;
import com.administrator.learndemo.rxjava.util.AppConstant;
import com.administrator.learndemo.rxjava.util.Utils;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ZipExampleActivity extends AppCompatActivity {
    private static final String TAG = ZipExampleActivity.class.getSimpleName();
    Button btn;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        btn = findViewById(R.id.btn);
        textView = findViewById(R.id.textView);

        btn.setOnClickListener((View view)-> doSomeWork());
    }

    private void doSomeWork() {
        Observable.zip(getCricketFansObservable(), getFootbalFansObservable(),
                (cricketFans, footballFans) -> Utils.filterUserWhoLovesBoth(cricketFans, footballFans))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    private Observable<List<User>> getCricketFansObservable() {
        return Observable.create((ObservableEmitter<List<User>> emitter) -> {
            if (!emitter.isDisposed()) {
                emitter.onNext(Utils.getUserListWhoLovesCricket());
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());
    }

    private Observable<List<User>> getFootbalFansObservable() {
        return Observable.create((ObservableOnSubscribe<List<User>>) emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onNext(Utils.getUserListWhoLovesFootball());
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());
    }

    private Observer<List<User>> getObserver() {
        return new Observer<List<User>>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, " onSubscribe : " + d.isDisposed());
            }

            @Override
            public void onNext(List<User> userList) {
                textView.append(" onNext");
                textView.append(AppConstant.LINE_SEPARATOR);
                for (User user : userList) {
                    textView.append(" firstname : " + user.firstname);
                    textView.append(AppConstant.LINE_SEPARATOR);
                }
                Log.d(TAG, " onNext : " + userList.size());
            }

            @Override
            public void onError(Throwable e) {
                textView.append(" onError : " + e.getMessage());
                textView.append(AppConstant.LINE_SEPARATOR);
                Log.d(TAG, " onError : " + e.getMessage());
            }

            @Override
            public void onComplete() {
                textView.append(" onComplete");
                textView.append(AppConstant.LINE_SEPARATOR);
                Log.d(TAG, " onComplete");
            }
        };
    }

}
