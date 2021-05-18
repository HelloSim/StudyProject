package com.sim.traveltool.internet;

import com.sim.traveltool.bean.BusLocationDataBean;
import com.sim.traveltool.bean.BusLocationDesignatedDataBean;
import com.sim.traveltool.bean.BusRealTimeBusStopDataBean;
import com.sim.traveltool.bean.BusRealTimeDataBean;
import com.sim.traveltool.bean.BusRealTimeLineDataBean;
import com.sim.traveltool.bean.BusRouteDataBean;
import com.sim.traveltool.bean.NewsWangYiBean;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author Sim --- Bus 接口API类
 */
public interface ApiService {

    @Headers({API.wangyi})
    @POST(API.wangyi_getNews)
    Observable<NewsWangYiBean> getWangYiNews(
            @Query("page") String page, @Query("count") String count);


    /**
     * 实时公交路线查询
     */
    @Headers({API.bus})
    @GET(API.bus_getLineListByLineName)
    Observable<BusRealTimeLineDataBean> getLineListByLineName(
            @Query("handlerName") String handlerName, @Query("key") String key, @Query("_") String time);

    /**
     * 公交路线查询
     */
    @Headers({API.bus})
    @GET(API.bus_getLineListByLineName)
    Observable<BusRealTimeBusStopDataBean> getStationList(
            @Query("handlerName") String handlerName, @Query("lineId") String lineId, @Query("_") String time);

    /**
     * 实时公交查询
     */
    @Headers({API.bus})
    @GET(API.bus_getLineListByLineName)
    Observable<BusRealTimeDataBean> getBusListOnRoad(
            @Query("handlerName") String handlerName, @Query("lineName") String lineName, @Query("fromStation") String fromStation,
            @Query("_") String time);


    /**
     * 搜索位置的网络请求
     */
    @Headers({API.route})
    @GET(API.route_getStartOrEndLocation)
    Observable<BusLocationDataBean> getStartOrEndLocation(
            @Query("s") String s, @Query("key") String key, @Query("city") String city,
            @Query("citylimit") String citylimit, @Query("callback") String callback, @Query("platform") String platform,
            @Query("logversion") String logversion, @Query("sdkversion") String sdkversion, @Query("appname") String appname,
            @Query("csid") String csid, @Query("keywords") String keywords);

    /**
     * 起始位置和终点位置的位置信息请求
     */
    @Headers({API.route})
    @GET(API.route_getLocation)
    Observable<BusLocationDesignatedDataBean> getLocation(
            @Query("s") String s, @Query("children") String children, @Query("key") String key,
            @Query("extensions") String extensions, @Query("page") String page, @Query("offset") String offset,
            @Query("city") String city, @Query("language") String language, @Query("callback") String callback,
            @Query("platform") String platform, @Query("logversion") String logversion, @Query("sdkversion") String sdkversion,
            @Query("appname") String appname, @Query("csid") String csid, @Query("keywords") String keywords);

    /**
     * 出行方案的网络请求
     */
    @Headers({API.route})
    @GET(API.route_getRoute)
    Observable<BusRouteDataBean> getRoute(
            @Query("origin") String origin, @Query("destination") String destination, @Query("city") String city,
            @Query("strategy") String strategy, @Query("nightflag") String nightflag, @Query("extensions") String extensions,
            @Query("s") String s, @Query("cityd") String cityd, @Query("key") String key,
            @Query("callback") String callback, @Query("platform") String platform, @Query("logversion") String logversion,
            @Query("sdkversion") String sdkversion, @Query("appname") String appname, @Query("csid") String csid);

}