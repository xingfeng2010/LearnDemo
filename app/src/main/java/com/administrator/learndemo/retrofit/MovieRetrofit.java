package com.administrator.learndemo.retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRetrofit {
    private static MovieRetrofit sMovieRetrofit;
    private final DoubanMovieService mApi;

    // 使用单例模式
    public static MovieRetrofit getInstance() {
        if (sMovieRetrofit == null) {
            synchronized (MovieRetrofit.class) {
                if (sMovieRetrofit == null) {
                    sMovieRetrofit = new MovieRetrofit();
                }
            }
        }
        return sMovieRetrofit;
    }

    private MovieRetrofit() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://api.douban.com/v2/movie/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        // 创建网络接口代理
        mApi = retrofit.create(DoubanMovieService.class);
    }

    // 返回API接口对象的实现
    public DoubanMovieService getDoubanMovieService() {
        return mApi;
    }
}
