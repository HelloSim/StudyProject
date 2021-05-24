package com.sim.common;

/**
 * @author Sim --- 常量类
 */
public class AppHelper {

    public static final String Bugly_APPID = "c7dbd02c92";//bugly
    public static final String jPush_APPID = "63c1f262d4358f4acf9a0712";//极光推送
    public static final String Bmob_ApplicationID = "62550b32bf5600010781ceeebc0e92ac";//Bmob后端云


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
    public static final int USER_IsLogIn = 2001;//已登录
    public static final int USER_noLogIn = 2002;//未登录

}
