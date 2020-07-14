package com.administrator.learndemo.dagger;

import com.administrator.learndemo.dagger.retrofit.FaceppService;
import com.administrator.learndemo.dagger.retrofit.UserInfoService;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class CommonModule {

    private ICommonView iView;
    private String iName;

    public CommonModule(ICommonView iView, String iName) {
        this.iView = iView;
        this.iName = iName;
    }

    @Provides
    public ICommonView provideIcommonView() {
        return this.iView;
    }

    @Provides
    public String provideName() {
        return this.iName;
    }

    @Provides
    FaceppService providesFacepp() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api-cn.faceplusplus.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit.create(FaceppService.class);
    }

    @Provides
    UserInfoService providesUserInfo() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://mytest.com.cn/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit.create(UserInfoService.class);
    }
}
