package com.sim.basicres.constant;

/**
 * @ author: Sim
 * @ time： 2021/5/28 10:27
 * @ description：路由路径
 */
public class ArouterUrl {

    /**
     * mainActivity
     */
    public static final String app_main = "/app/main";

    /**
     * wangyi模块
     */
    public static class Wangyi {
        public static final String base = "/module_wangyi";

        public static final String wangyi_fragment = base + "/fragment/main";
        public static final String wangyi_activity_detail = base + "/detail";
    }

    /**
     * bus模块
     */
    public static class Bus {
        public static final String base = "/module_bus";

        public static final String bus_fragment = base + "/fragment/main";
        public static final String bus_fragment_realtime = base + "/fragment/realtime";
        public static final String bus_fragment_route = base + "/fragment/route";
        public static final String bus_fragment_station = base + "/fragment/station";
        public static final String bus_activity_search = base + "/activity/search";
        public static final String bus_activity_realtime = base + "/activity/station";
        public static final String bus_activity_route = base + "/activity/route";
        public static final String bus_activity_route_detail = base + "/activity/route/detail";
    }

    /**
     * recoed模块
     */
    public static class Record {
        public static final String base = "/module_record";

        public static final String record_fragment = base + "/fragment/main";
        public static final String record_activity_all = base + "/activity/all";
    }

    /**
     * mine模块
     */
    public static class Mine{
        public static final String base = "/module_mine";

        public static final String user_fragment = base +"/fragment";
        public static final String user_activity_login = base +"/activity/login";
        public static final String user_activity_info = base +"/activity/info";
        public static final String user_activity_register = base +"/activity/register";
        public static final String user_activity_updatepws = base +"/activity/updatepwd";
        public static final String user_activity_collect = base +"/activity/collect";
    }

}
