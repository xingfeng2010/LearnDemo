package com.administrator.learndemo.retrofit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import android.app.ListActivity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.administrator.learndemo.R;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.N)
public class RetrofitActivity extends ListActivity {

    private static final String TAG = "Retrofit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestHttpData();
    }


    private void requestHttpData() {
        String apiKey = "0df993c66c0c636e29ecbb5344252a4a";
        // 获取Observable对象，查询排名前25的电影
        Observable<MovieBean> movieBeanObservable = MovieRetrofit.getInstance().getDoubanMovieService().listTop250(0, 10, apiKey);
        movieBeanObservable.subscribeOn(Schedulers.io())
                .map(new Function<MovieBean, List<String>>() {

                    @Override
                    public List<String> apply(MovieBean movieBean) throws Exception {
                        List<String> array = new ArrayList<String>();
                        for (int i = 0; i < movieBean.getSubjects().size(); i++) {
                            String title = movieBean.getSubjects().get(i).getTitle();
                            Log.d(TAG, "apply: " + title);
                            array.add(title);
                        }
                        return array;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<String> array) {
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(RetrofitActivity.this,
                                android.R.layout.simple_list_item_1, array);
                        setListAdapter(arrayAdapter); // 设置Adapter刷新列表
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(RetrofitActivity.this, "onError", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onError: " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(RetrofitActivity.this, "onComplete", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
