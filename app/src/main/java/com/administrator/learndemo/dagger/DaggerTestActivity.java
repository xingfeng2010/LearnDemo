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
import java.util.List;
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
    private static final String TAG = "RXJAVA";

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

        testJustBuffer();
    }

    private void testJustBuffer() {
        Observable.just("test1","test2","test3","test4","test5","test6").buffer(2)
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        Log.e(TAG, "SINGLE ARRAY");
                        for (String str: strings) {
                            Log.e(TAG, "testJustBuffer:" + str);
                        }
                    }
                });
    }

    /**
     * 两者都可以实现数据集合中一对多事件的转换，后者会按发送的顺序获取接收结果，前者可能是乱序接收（不确定哪个事件先完成）。
     *
     * 一对多事件转换：在flatMap集合中例如可以操作一个公司实体，并转换为单个部门实体，返回后在后续的accept中，
     * 又可以使用单个部门实体对每个成员进行逻辑处理。
     */
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
                        Log.e(TAG, "flatMap:" + integer);
                    }
                });

        Observable.fromArray(1, 2, 3, 4, 5)
                .concatMap(new Function<Integer, ObservableSource<Integer>>() {
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
                        Log.e(TAG, "concatMap:" + integer);
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
                    Log.i(TAG, "testUserInfo:" + s);
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
