package com.sim.constant;

/**
 * @author Sim --- api
 */
public class BaseUrl {

    //网易新闻
    public static class base_wangyi {
        public static final String base = "https://api.apiopen.top";

        public static final String getNews = "/getWangYiNews";//获取网易新闻
    }

    //珠海公交
    public static class base_bus {
        public static final String base = "http://www.zhbuswx.com";

        public static final String getLineListByLineName = "/Handlers/BusQuery.ashx";
        public static final String getStationList = "/Handlers/BusQuery.ashx";
        public static final String getBusListOnRoad = "/Handlers/BusQuery.ashx";
    }

    //出行路线
    public static class base_route {
        public static final String base = "http://restapi.amap.com";

        public static final String getStartOrEndLocation = "/v3/assistant/inputtips";//输入提示
        public static final String getLocation = "/v3/place/text";//关键字搜索
        public static final String getRoute = "/v3/direction/transit/integrated";//公交路径规划
    }

    //wanandroid
    public static class base_wanandroid {
        public static final String base = "https://www.wanandroid.com";

        public static final String getBanner = "/banner/json";//导航图
        public static final String getPublicAuthor = "/wxarticle/chapters/json";//获取公众号作者
        public static final String getPublicArticle = "/wxarticle/list/408/1/json";//获取指定公众号的文章列表
    }


}
