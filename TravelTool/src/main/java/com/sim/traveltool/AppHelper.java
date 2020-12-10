package com.sim.traveltool;

/**
 * @Auther Sim
 * @Time 2020/12/8 10:21
 * @Description 常量类
 */
public class AppHelper {

    //bugly
    public static final String Bugly_APPID = "c7dbd02c92";
    public static final String Bugly_APPKEY = "34b65563-6853-4b74-93c1-8d3175a815d0";

    //服务器地址
    public static final String smzdm_BASE_URL = "https://homepage-api.smzdm.com";//什么值得买服务器地址
    //    @GET("v1/home")
//    Observable <SmzdmDataBean> getHome(@Query("page") String page, @Query("limit") String limit, @Query("time") String time);
    public static final String USER_BASE_URL = "https://api.apiopen.top";//用户服务器地址
    public static final String BUS_BASE_URL = "http://www.zhbuswx.com";//公交服务器地址
    public static final String ROUTE_BASE_URL = "http://restapi.amap.com";//出行路线服务器地址
//    public static final String IMAGE_BASE_URL = "https://api.dongmanxingkong.com";//随机图片服务器地址
    public static final String IMAGE_BASE_URL = "https://gank.io/api/v2/data/category/Girl/type/Girl/page/1/count/10";//随机图片服务器地址

    //BUS搜索页面跳转
    public static final int RESULT_BUS = 1000;//跳转实时公交路线搜索
    public static final int RESULT_START_STATION = 10001;//跳转出行路线起点位置搜索
    public static final int RESULT_END_STATION = 1002;//跳转出行路线终点位置搜索

}
