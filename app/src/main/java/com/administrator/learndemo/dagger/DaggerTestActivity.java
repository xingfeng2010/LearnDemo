package com.administrator.learndemo.dagger;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
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
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
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

//        testRxjava();
//        testMap();
//        testJustBuffer();

//        testZip();

//        testThread();

//        testSimple();

//        testObserver();

//        helloworldComplexDisposable();

//        helloworldPlus();
        
        testFilter();
    }

    /**
     * RxJava提供了大量的操作符来完成对数据的处理，这些操作符也可以理解为函数。如果把RxJava比作一条数据流水线，
     * 那么操作符就是一道工序，数据通过这些工序的加工变换，组装，最后生产出我们需要的数据。
     *
     * 记住：操作符都是对被观察发出数据的操作。
     */
    private void testFilter() {
        Observable.just("姚明","阿联","摇头叹琦","大侄子")
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        Log.d(TAG, "test: " + s);
                        return s.equals("摇头叹琦"); // 只检查出摇头叹琦
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "accept: " + s);
                    }
                });
    }

    /**
     * 使用Create方法创建被观察者，并实现subscribe方法，
     * 接收一个ObservableEmitter对象，即被观察者的发射器，发射器能够发出数据和事件
     * 使用链式调用，让代码看起来更加整洁，
     * 上面发出数据和事件，下面接收数据和事件
     *
     * 观察者还是原来的观察者，被观察者则使用create()的方法创建出来，并实现了subscribe()方法，
     * 接收一个ObservableEmitter对象，即被观察者的发射器，发射器能够发出数据和事件。
     */
    private void helloworldPlus() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.d(TAG, "subscribe: send hello world");
                emitter.onNext("hello world");
                Log.d(TAG, "subscribe: send No");
                emitter.onNext("No");
                Log.d(TAG, "subscribe: send No");
                emitter.onNext("No");
                Log.d(TAG, "subscribe: send complete");
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext: " + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: ");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        });
    }

    private void helloworldComplexDisposable() {
        Observer<String> observer = new Observer<String>() {
            Disposable disposable;

            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext: 2，" + s);
                if (s.equals("NO")) {
                    disposable.dispose();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: 3，");
            }
        };

        Observable.just("Hello world", "NO", "EDD").subscribe(observer);
    }

    private void testObserver() {
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                d.dispose();
                Log.d(TAG, "onSubscribe isDisposed: " + d.isDisposed());
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onSubscribe onNext: " + s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onSubscribe onComplete: ");
            }
        };

        Observable.just("Hello World!").subscribe(observer);
    }

    private void testSimple() {
        // 创建消费者，消费者接收一个String类型的事件
        Consumer<String> consumer = new Consumer<String>() {

            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "accept: 0," + s);
            }
        };
        Observable.just("Hello WOrld").subscribe(consumer);
    }

    /**
     * 在不指定线程的情况下， RxJava 遵循的是线程不变的原则，即：在哪个线程调用 subscribe()，就在哪个线程生产事件；
     * 在哪个线程生产事件，就在哪个线程消费事件。如果需要切换线程，就需要用到 Scheduler 。
     * <p>
     * 在RxJava 中，Scheduler，相当于线程控制器，RxJava 通过它来指定每一段代码应该运行在什么样的线程。RxJava 已经内置
     * 了几个Scheduler ，它们已经适合大多数的使用场景：
     * Schedulers.immediate(): 直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler。
     * Schedulers.newThread(): 总是启用新线程，并在新线程执行操作。
     * Schedulers.io(): I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。行为模式和newThread()差不多，
     * 区别在于io()的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下io()比newThread()更有效率。
     * 不要把计算工作放在io()中，可以避免创建不必要的线程。
     * Schedulers.computation(): 计算所使用的Scheduler。这个计算指的是CPU密集型计算，即不会被I/O 等操作限制性能的操作，
     * 例如图形的计算。这个Scheduler使用的固定的线程池，大小为 CPU 核数。不要把 I/O 操作放在computation()中，否则I/O操作
     * 的等待时间会浪费 CPU。
     * <p>
     * 另外， Android 还有一个专用的 AndroidSchedulers.mainThread()，它指定的操作将在 Android 主线程运行。
     * 有了这几个 Scheduler ，就可以使用 subscribeOn() 和 observeOn() 两个方法来对线程进行控制了。
     * <p>
     * *subscribeOn(): 指定subscribe()所发生的线程，即Observable.OnSubscribe被激活时所处的线程。或者叫做事件产生的线程。
     * *observeOn(): 指定Subscriber 所运行在的线程。或者叫做事件消费的线程。
     */
    private void testThread() {
        Observable.just("aa", "bb", "cc")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "onNext :" + s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 组合的过程是分别从 两根水管里各取出一个事件 来进行组合, 并且一个事件只能被使用一次, 组合的顺序
     * 是严格按照事件发送的顺利 来进行的, 也就是说不会出现圆形1 事件和三角形B 事件进行合并, 也不可能出现
     * 圆形2 和三角形A 进行合并的情况.
     * 最终下游收到的事件数量 是和上游中发送事件最少的那一根水管的事件数量 相同. 这个也很好理解, 因为是
     * 从每一根水管 里取一个事件来进行合并, 最少的 那个肯定就最先取完 , 这个时候其他的水管尽管还有事件 ,
     * 但是已经没有足够的事件来组合了, 因此下游就不会收到剩余的事件了.
     */
    private void testZip() {
        Observable observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                Log.d(TAG, "1");
                e.onNext(1);
                SystemClock.sleep(1000);
                Log.d(TAG, "2");
                e.onNext(2);
                SystemClock.sleep(1000);
                Log.d(TAG, "3");
                e.onNext(3);
                SystemClock.sleep(1000);
                Log.d(TAG, "4");
                e.onNext(4);
                SystemClock.sleep(1000);
                Log.d(TAG, "onComplete");
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());

        Observable observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                Log.d(TAG, "A");
                e.onNext("A");
                SystemClock.sleep(1000);
                Log.d(TAG, "B");
                e.onNext("B");
                SystemClock.sleep(1000);
                Log.d(TAG, "C");
                e.onNext("C");
                SystemClock.sleep(1000);
                Log.d(TAG, "onComplete");
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer a, String b) throws Exception {
                return a + b;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String o) throws Exception {
                Log.d(TAG, "SystemClock:" + o);
            }
        });

        Observable.zip(new ObservableSource<String>() {

                           @Override
                           public void subscribe(Observer<? super String> observer) {
                               observer.onNext("henan");
                           }
                       }, new ObservableSource<String>() {

                           @Override
                           public void subscribe(Observer<? super String> observer) {
                               observer.onNext("zhengzhou");
                           }
                       }, new BiFunction<String, String, String>() {

                           @Override
                           public String apply(String s1, String s2) throws Exception {
                               Log.e(TAG, "testZip: s1：" + s1);
                               return s1 + s2;
                           }
                       }
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        Log.e(TAG, "testZip: 成功：" + s + "\n");
                    }
                });
    }

    private void testJustBuffer() {
        Observable.just("test1", "test2", "test3", "test4", "test5", "test6").buffer(2)
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        Log.e(TAG, "SINGLE ARRAY");
                        for (String str : strings) {
                            Log.e(TAG, "testJustBuffer:" + str);
                        }
                    }
                });
    }

    /**
     * 两者都可以实现数据集合中一对多事件的转换，后者会按发送的顺序获取接收结果，前者可能是乱序接收（不确定哪个事件先完成）。
     * <p>
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

    /**
     * map操作符能够完成数据类型的转换。被观察者发送出一个student，而观察者想要接收一个developer(两个都是简单的实体类)，
     * 那么在student发送给观察者之前，需要对student进行一些培训，让它转换成一个developer。
     */
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
