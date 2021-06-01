package com.sim.http;

import com.sim.bean.BannerRes;
import com.sim.bean.BusLocationBean;
import com.sim.bean.BusLocationDesignatedBean;
import com.sim.bean.BusRealTimeBean;
import com.sim.bean.BusRealTimeBusStopBean;
import com.sim.bean.BusRealTimeLineBean;
import com.sim.bean.BusRouteBean;
import com.sim.bean.WangyiBean;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author Sim --- Bus 接口API类
 */
public interface ApiService {

    /**
     * 网易新闻
     */
    @Headers({"host:wangyi"})
    @GET("/getWangYiNews")
    Observable<WangyiBean> getWangYiNews(@Query("page") String page);


    /**
     * 轮播图
     */
    @Headers({"host:wanandroid"})
    @GET("banner/json")
    Observable<BannerRes> getBanner();


    /**
     * 实时公交路线查询
     */
    @Headers({"host:bus"})
    @GET("/Handlers/BusQuery.ashx")
    Observable<BusRealTimeLineBean> getLineListByLineName(@Query("handlerName") String handlerName, @Query("key") String key);

    /**
     * 公交路线查询
     */
    @Headers({"host:bus"})
    @GET("/Handlers/BusQuery.ashx")
    Observable<BusRealTimeBusStopBean> getStationList(@Query("handlerName") String handlerName, @Query("lineId") String lineId);

    /**
     * 实时公交查询
     */
    @Headers({"host:bus"})
    @GET("/Handlers/BusQuery.ashx")
    Observable<BusRealTimeBean> getBusListOnRoad(@Query("handlerName") String handlerName, @Query("lineName") String lineName,
                                                 @Query("fromStation") String fromStation);


    /**
     * 搜索位置的网络请求
     */
    @Headers({"host:route"})
    @GET("/v3/assistant/inputtips")
    Observable<BusLocationBean> getStartOrEndLocation(@Query("citylimit") String citylimit, @Query("keywords") String keywords);

    /**
     * 起始位置和终点位置的位置信息请求
     */
    @Headers({"host:route"})
    @GET("/v3/place/text")
    Observable<BusLocationDesignatedBean> getLocation(
            @Query("children") String children, @Query("extensions") String extensions,
            @Query("page") String page, @Query("offset") String offset,
            @Query("language") String language, @Query("keywords") String keywords);

    /**
     * 出行方案的网络请求
     */
    @Headers({"host:route"})
    @GET("/v3/direction/transit/integrated")
    Observable<BusRouteBean> getRoute(
            @Query("origin") String origin, @Query("destination") String destination,
            @Query("strategy") String strategy, @Query("nightflag") String nightflag,
            @Query("extensions") String extensions);

}