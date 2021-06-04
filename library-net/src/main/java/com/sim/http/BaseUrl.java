package com.sim.http;

/**
 * @author Sim --- api
 */
public class BaseUrl {

    //网易新闻
    public static class base_wangyi {
        public static final String base = "https://api.apiopen.top";

        public static final String getNews = "/getWangYiNews";
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

        public static final String getStartOrEndLocation = "/v3/assistant/inputtips";
        public static final String getLocation = "/v3/place/text";
        public static final String getRoute = "/v3/direction/transit/integrated";
    }

    //wanandroid
    public static class base_wanandroid {
        public static final String base = "https://www.wanandroid.com";

        public static final String getBanner = "/banner/json";
    }


}
