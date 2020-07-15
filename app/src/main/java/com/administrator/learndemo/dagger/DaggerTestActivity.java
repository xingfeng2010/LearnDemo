package com.administrator.learndemo.dagger;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.administrator.learndemo.R;
import com.administrator.learndemo.dagger.bean.Student;
import com.administrator.learndemo.dagger.retrofit.FaceppService;
import com.administrator.learndemo.dagger.retrofit.UserInfoService;

import org.json.JSONException;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class DaggerTestActivity extends AppCompatActivity implements ICommonView {

    @BindView(R.id.btn_login)
    Button btn;
    @Inject
    LoginPresenter loginPresenter;
    @Inject
    UserInfoService userInfoService;
    @Inject
    FaceppService faceppService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dagger_test);
        ButterKnife.bind(this);
        DaggerCommonComponent.
                builder().
                commonModule(new CommonModule(this, "DAGGER")).
                build().
                inject(this);


        testRxjava();

        testMap();
    }

    private void testMap() {
        Observable.fromArray(1, 2, 3, 4, 5)
                .flatMap(new Function<Integer, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(@NonNull Integer integer) throws Exception {

                        int delay = 0;
                        if (integer == 3) {
                            delay = 500;//延迟500ms
                        }
                        return Observable.just(integer * 10).delay(delay, TimeUnit.MILLISECONDS);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.e("tag", "accept:" + integer);
                    }
                });
    }

    private void testRxjava() {
        Student[] students = new Student[10];
        for (int i = 0; i < students.length; i++) {
            Student student = new Student();
            student.setName("name" + i);
            students[i] = student;
        }

        Observable.fromArray(students)
                .map((Student student) -> {
                    return student.getName();
                })
                .subscribe((String s) -> {
                    Log.i("DEBUG_TEST", "testUserInfo:" + s);
                });
    }

    @OnClick(R.id.btn_login)
    void login() {
        //Toast.makeText(this, "LGON_IN", Toast.LENGTH_SHORT).show();

        loginPresenter.login(new User());
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void testUserInfo() {
        String token = "123dfd";
        String id = "kdkkdkddkdkkd";
        userInfoService.getUserInfo(token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ResponseBody body) {
                        //handleDetectResult(photo,faceppBean);
                        try {
                            String response = body.string();
                            Log.i("DEBUG_TEST", "testUserInfo:" + response);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
