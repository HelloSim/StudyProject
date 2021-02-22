package com.sim.traveltool.internet;


import com.sim.traveltool.bean.BusLocationDataBean;
import com.sim.traveltool.bean.BusLocationDesignatedDataBean;
import com.sim.traveltool.bean.BusRealTimeBusStopDataBean;
import com.sim.traveltool.bean.BusRealTimeDataBean;
import com.sim.traveltool.bean.BusRealTimeLineDataBean;
import com.sim.traveltool.bean.BusRouteDataBean;
import com.sim.traveltool.bean.NewsWangYiBean;

import rx.Observable;
import rx.Subscriber;

/**
 * @author Sim --- 使用网络请求的单例
 */
public class APIFactory extends RetrofitUtil {

    private APIFactory() {
    }

    public static APIFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final APIFactory INSTANCE = new APIFactory();
    }

    /**
     * NewsData的网络请求
     */
    public void getWangYiNew(Subscriber<NewsWangYiBean> subscriber, String page, String count) {
        Observable observable = getWangyiApiService().getWangYiNews(page, count);
        toSubscribe(observable, subscriber);
    }


    /**
     * 搜索位置的网络请求
     */
    public void getStartOrEndLocation(Subscriber<BusLocationDataBean> subscriber, String keywords) {
        Observable observable = getRouteAPIService().getStartOrEndLocation("rsv3", "ceb54024fae4694f734b1006e8dc8324", "0756",
                "false", "", "JS", "2.0", "1.3", "http://www.zhbuswx.com/busline/BusQuery.html?v=2.01#/",
                "759CACE2-2197-4E0A-ADCB-1456B16775DA", keywords);
        toSubscribe(observable, subscriber);
    }

    /**
     * 起始位置和终点位置的位置信息请求
     */
    public void getLocation(Subscriber<BusLocationDesignatedDataBean> subscriber, String keywords) {
        Observable observable = getRouteAPIService().getLocation("rsv3", "", "ceb54024fae4694f734b1006e8dc8324",
                "all", "1", "10", "珠海", "zh_cn", "", "JS",
                "2.0", "1.3", "http://www.zhbuswx.com/busline/BusQuery.html?v=2.01#/",
                "759CACE2-2197-4E0A-ADCB-1456B16775DA", keywords);
        toSubscribe(observable, subscriber);
    }

    /**
     * 出行方案的网络请求
     */
    public void getRoute(Subscriber<BusRouteDataBean> subscriber, String origin, String destination) {
        Observable observable = getRouteAPIService().getRoute(origin, destination, "珠海", "0", "0",
                "", "rsv3", "珠海", "ceb54024fae4694f734b1006e8dc8324", "", "JS",
                "2.0", "1.3", "http://www.zhbuswx.com/busline/BusQuery.html?v=2.01#/",
                "759CACE2-2197-4E0A-ADCB-1456B16775DA");
        toSubscribe(observable, subscriber);
    }


    /**
     * 实时公交路线查询
     */
    public void getLineListByLineName(Subscriber<BusRealTimeLineDataBean> subscriber, String handlerName, String key, String time) {
        Observable observable = getBusAPIService().getLineListByLineName(handlerName, key, time);
        toSubscribe(observable, subscriber);
    }

    /**
     * 公交路线查询
     */
    public void getStationList(Subscriber<BusRealTimeBusStopDataBean> subscriber, String handlerName, String lineId, String time) {
        Observable observable = getBusAPIService().getStationList(handlerName, lineId, time);
        toSubscribe(observable, subscriber);
    }

    /**
     * 实时公交查询
     */
    public void getBusListOnRoad(Subscriber<BusRealTimeDataBean> subscriber, String handlerName, String lineName, String fromStation, String time) {
        Observable observable = getBusAPIService().getBusListOnRoad(handlerName, lineName, fromStation, time);
        toSubscribe(observable, subscriber);
    }

}
