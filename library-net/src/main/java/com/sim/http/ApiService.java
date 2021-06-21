package com.sim.http;

import com.sim.bean.BannerBean;
import com.sim.bean.RouteLocationBean;
import com.sim.bean.RouteLocationDesignatedBean;
import com.sim.bean.BusRealTimeBean;
import com.sim.bean.BusRealTimeBusStopBean;
import com.sim.bean.BusRealTimeLineBean;
import com.sim.bean.RoutesBean;
import com.sim.bean.PublicArticleBean;
import com.sim.bean.PublicAuthorBean;
import com.sim.bean.WangyiBean;
import com.sim.constant.BaseUrl;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author Sim --- Bus 接口API类
 */
public interface ApiService {

    /**
     * 获取网易新闻列表
     */
    @Headers({"host:wangyi"})
    @GET(BaseUrl.base_wangyi.getNews)
    Observable<WangyiBean> getWangYiNews(@Query("page") String page);





    /**
     * 获取轮播图列表
     */
    @Headers({"host:wanandroid"})
    @GET(BaseUrl.base_wanandroid.getBanner)
    Observable<BannerBean> getBanner();

    /**
     * 获取公众号列表
     */
    @Headers({"host:wanandroid"})
    @GET(BaseUrl.base_wanandroid.getPublicAuthor)
    Observable<PublicAuthorBean> getPublicAuthor();

    /**
     * 获取指定公众号文章列表
     */
    @Headers({"host:wanandroid"})
    @GET(BaseUrl.base_wanandroid.getPublicArticle)
    Observable<PublicArticleBean> getPublicArticle();





    /**
     * 实时公交路线查询
     */
    @Headers({"host:bus"})
    @GET(BaseUrl.base_bus.getLineListByLineName)
    Observable<BusRealTimeLineBean> getLineListByLineName(@Query("handlerName") String handlerName, @Query("key") String key);

    /**
     * 公交路线查询
     */
    @Headers({"host:bus"})
    @GET(BaseUrl.base_bus.getStationList)
    Observable<BusRealTimeBusStopBean> getStationList(@Query("handlerName") String handlerName, @Query("lineId") String lineId);

    /**
     * 实时公交查询
     */
    @Headers({"host:bus"})
    @GET(BaseUrl.base_bus.getBusListOnRoad)
    Observable<BusRealTimeBean> getBusListOnRoad(@Query("handlerName") String handlerName, @Query("lineName") String lineName,
                                                 @Query("fromStation") String fromStation);





    /**
     * 搜索位置 输入提示的请求
     */
    @Headers({"host:route"})
    @GET(BaseUrl.base_route.getStartOrEndLocation)
    Observable<RouteLocationBean> getStartOrEndLocation(@Query("citylimit") String citylimit, @Query("keywords") String keywords);

    /**
     * 起始位置和终点位置的位置信息请求
     */
    @Headers({"host:route"})
    @GET(BaseUrl.base_route.getLocation)
    Observable<RouteLocationDesignatedBean> getLocation(
            @Query("children") String children, @Query("extensions") String extensions,
            @Query("page") String page, @Query("offset") String offset,
            @Query("language") String language, @Query("keywords") String keywords);

    /**
     * 公交路径规划的网络请求
     */
    @Headers({"host:route"})
    @GET(BaseUrl.base_route.getRoute)
    Observable<RoutesBean> getRoute(
            @Query("origin") String origin, @Query("destination") String destination,
            @Query("strategy") String strategy, @Query("nightflag") String nightflag,
            @Query("extensions") String extensions);

}