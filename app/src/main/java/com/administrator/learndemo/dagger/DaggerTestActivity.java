package com.administrator.learndemo.dagger;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.administrator.learndemo.R;
import com.administrator.learndemo.dagger.retrofit.FaceppService;
import com.administrator.learndemo.dagger.retrofit.UserInfoService;

import org.json.JSONException;

import java.io.IOException;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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
                            Log.i("DEBUG_TEST","testUserInfo:" + response);
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
