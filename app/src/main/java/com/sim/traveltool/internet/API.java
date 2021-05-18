package com.sim.traveltool.internet;

/**
 * @ author: Sim
 * @ time： 2021/5/18 16:52
 * @ description：BaseUrl
 */
public class API {

//    public static final String smzdm_BASE_URL = "https://homepage-api.smzdm.com";//什么值得买服务器地址
//    @GET("v1/home")
//    Observable <SmzdmDataBean> getHome(@Query("page") String page, @Query("limit") String limit, @Query("time") String time);

    public final static String wangyi_base_url = "https://api.apiopen.top";
    public final static String wangyi = "urlname:wangyi";
    public final static String wangyi_getNews = "/getWangYiNews";

    public final static String bus_base_url = "http://www.zhbuswx.com";
    public final static String bus = "urlname:bus";
    public final static String bus_getLineListByLineName = "/Handlers/BusQuery.ashx";

    public final static String route_base_url = "http://restapi.amap.com";
    public final static String route = "urlname:route";
    public final static String route_getStartOrEndLocation = "/v3/assistant/inputtips";
    public final static String route_getLocation = "/v3/place/text";
    public final static String route_getRoute = "/v3/direction/transit/integrated";

}
