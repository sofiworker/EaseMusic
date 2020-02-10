package me.sofiworker.easemusic;

import java.util.Map;

import io.reactivex.Observable;
import me.sofiworker.easemusic.bean.BannerBean;
import me.sofiworker.easemusic.bean.SongListBean;
import me.sofiworker.easemusic.bean.UserBean;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * @author sofiworker
 * @version 1.0.0
 * @date 2019/11/27 18:09
 * @description retrofit服务接口
 */
public interface Api {

    @POST("login/cellphone")
    Observable<UserBean> userLogin(@QueryMap Map<String, String> userInfo);

    @POST("user/playlist")
    Observable<SongListBean> getPlayList(@Query("uid") int uid);

    @GET("/banner")
    Observable<BannerBean> getBanner(@Query("type") int type);
}
