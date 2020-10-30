package com.sim.test.internet;

import com.sim.test.bean.SmzdmDataBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Grugsum on 2019/4/22.
 * 接口API类
 */
public interface APIService {
    /**
     * Home的Data请求API
     */
    @GET("v1/home")
    Observable <SmzdmDataBean> getHome(@Query("page") String page, @Query("limit") String limit, @Query("time") String time);

}