package com.sim.traveltool;

/**
 * @author Sim --- 常量类
 */
public class AppHelper {

    public static final String SUCCESS = "0";


    /**
     * API key
     */
    public static final String Bugly_APPID = "c7dbd02c92";//bugly
    public static final String jPush_APPID = "63c1f262d4358f4acf9a0712";//极光推送
    public static final String Bmob_ApplicationID = "62550b32bf5600010781ceeebc0e92ac";//Bmob后端云


    /**
     * 服务器地址
     */
    public static final String BUS_BASE_URL = "http://www.zhbuswx.com";//公交服务器地址
    public static final String ROUTE_BASE_URL = "http://restapi.amap.com";//出行路线服务器地址

    public static final String WANGYI_BASE_URL = "https://api.apiopen.top";//网易新闻服务器地址
    public static final String WANGYI_API_KEY = "cf9fb9e5ff32ad787d9d2ed3910ccc94";//网易新闻服务器key

//    public static final String smzdm_BASE_URL = "https://homepage-api.smzdm.com";//什么值得买服务器地址
//    @GET("v1/home")
//    Observable <SmzdmDataBean> getHome(@Query("page") String page, @Query("limit") String limit, @Query("time") String time);


    /**
     * 页面跳转
     */
    public static final int RESULT_BUS = 1000;//跳转实时公交路线搜索
    public static final int RESULT_START_STATION = 10001;//跳转出行路线起点位置搜索
    public static final int RESULT_END_STATION = 1002;//跳转出行路线终点位置搜索


    /**
     * user用户信息相关
     */
    public static final String userSpName = "userState";//sp文件名
    public static final String userSpStateKey = "isLogIn";//sp键名-是否登录
    public static final String userSpAccountNumber = "accountNumber";//sp键名-用户密码
    public static final String userSpPasswordKey = "password";//sp键名-用户密码
    public static final String userSpUserInfoKey = "userInfo";//sp键名-用户信息
    public static final int USER_IsLogIn = 2001;//已登录
    public static final int USER_noLogIn = 2002;//未登录
    public static final int USER_UpDate = 2003;//修改信息

}
