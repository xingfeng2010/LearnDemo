package com.administrator.learndemo.retrofit;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DoubanMovieService {
    @GET("in_theaters")
    Observable<MovieBean> listTop250(@Query("start") int start,
                                     @Query("count") int count,
                                     @Query("apikey") String apiKey);
}
