package com.administrator.learndemo.dagger.retrofit;



import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserInfoService {
    /**
     * @param token
     * @param clientId
     * @return
     */
    @GET("facepp/v3/detect/user_info")
    Observable<ResponseBody> getUserInfo(@Query("access_token") String token,
                                         @Query("client_id") String clientId);
}
