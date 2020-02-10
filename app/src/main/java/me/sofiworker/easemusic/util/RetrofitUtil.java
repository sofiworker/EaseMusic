package me.sofiworker.easemusic.util;

import java.util.concurrent.TimeUnit;

import me.sofiworker.easemusic.Api;
import me.sofiworker.easemusic.CommonInterceptor;
import me.sofiworker.easemusic.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author sofiworker
 * @version 1.0.0
 * @date 2019/11/26 22:18
 * @description retrofit工具类
 */
public class RetrofitUtil {

    //超时时间
    private static final int TOME_OUT = 100;

    private static Api mInstance = null;

    private RetrofitUtil() {
    }


    public static Api getInstance() {
        if (mInstance == null) {
            synchronized (Retrofit.class) {
                if (mInstance == null) {
                    mInstance = new Retrofit.Builder()
                            .baseUrl(Constants.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build().create(Api.class);
                }
            }
        }
        return mInstance;
    }

//    public static Api getApiService(){
//        return new Retrofit.Builder()
//                .baseUrl(Constants.BASE_URL)
//                .client(getHttpClient())
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .build().create(Api.class);
//    }

    private static OkHttpClient getHttpClient(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(TOME_OUT, TimeUnit.SECONDS)
                .readTimeout(TOME_OUT, TimeUnit.SECONDS)
                .addInterceptor(new CommonInterceptor());
        return builder.build();
    }
}
