package com.sim.traveltool.internet;

import com.sim.traveltool.bean.NewsWangYiBean;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @Author： Sim
 * @Time： 2019/4/22 1:05
 * @Description： 网易接口API类
 */
public interface NewsAPIService {

    /**
     * 网易新闻的请求API
     *
     * @param page
     * @param count
     * @return
     */
    @POST("/getWangYiNews")
    Observable<NewsWangYiBean> getWangYiNews(@Query("page") String page, @Query("count") String count);

}