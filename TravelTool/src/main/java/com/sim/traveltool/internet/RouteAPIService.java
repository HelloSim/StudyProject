package com.sim.traveltool.internet;

import com.sim.traveltool.bean.BusLocationDataBean;
import com.sim.traveltool.bean.BusLocationDesignatedDataBean;
import com.sim.traveltool.bean.BusRouteDataBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @Author： Sim
 * @Time： 2020/11/25 17:40
 * @Description： 位置、路线的网络请求
 */
public interface RouteAPIService {

    /**
     * 搜索位置的网络请求
     *
     * @param s
     * @param key
     * @param city
     * @param citylimit
     * @param callback
     * @param platform
     * @param logversion
     * @param sdkversion
     * @param appname
     * @param csid
     * @param keywords
     * @return
     */
    @GET("/v3/assistant/inputtips")
    Observable<BusLocationDataBean> getStartOrEndLocation(@Query("s") String s, @Query("key") String key,
                                                     @Query("city") String city, @Query("citylimit") String citylimit,
                                                     @Query("callback") String callback, @Query("platform") String platform,
                                                     @Query("logversion") String logversion, @Query("sdkversion") String sdkversion,
                                                     @Query("appname") String appname, @Query("csid") String csid,
                                                     @Query("keywords") String keywords);

    /**
     * 起始位置和终点位置的位置信息请求
     *
     * @param s
     * @param children
     * @param key
     * @param extensions
     * @param page
     * @param offset
     * @param city
     * @param language
     * @param callback
     * @param platform
     * @param logversion
     * @param sdkversion
     * @param appname
     * @param csid
     * @param keywords
     * @return
     */
    @GET("/v3/place/text")
    Observable<BusLocationDesignatedDataBean> getLocation(@Query("s") String s, @Query("children") String children,
                                                          @Query("key") String key, @Query("extensions") String extensions,
                                                          @Query("page") String page, @Query("offset") String offset,
                                                          @Query("city") String city, @Query("language") String language,
                                                          @Query("callback") String callback, @Query("platform") String platform,
                                                          @Query("logversion") String logversion, @Query("sdkversion") String sdkversion,
                                                          @Query("appname") String appname, @Query("csid") String csid,
                                                          @Query("keywords") String keywords);

    /**
     * 出行方案的网络请求
     *
     * @param origin
     * @param destination
     * @param city
     * @param strategy
     * @param nightflag
     * @param extensions
     * @param s
     * @param cityd
     * @param key
     * @param callback
     * @param platform
     * @param logversion
     * @param sdkversion
     * @param appname
     * @param csid
     * @return
     */
    @GET("/v3/direction/transit/integrated")
    Observable<BusRouteDataBean> getRoute(@Query("origin") String origin, @Query("destination") String destination,
                                          @Query("city") String city, @Query("strategy") String strategy,
                                          @Query("nightflag") String nightflag, @Query("extensions") String extensions,
                                          @Query("s") String s, @Query("cityd") String cityd,
                                          @Query("key") String key, @Query("callback") String callback,
                                          @Query("platform") String platform, @Query("logversion") String logversion,
                                          @Query("sdkversion") String sdkversion, @Query("appname") String appname,
                                          @Query("csid") String csid);

}
