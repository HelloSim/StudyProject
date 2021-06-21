package com.sim.http;

import com.sim.bean.BannerBean;
import com.sim.bean.BusLocationBean;
import com.sim.bean.BusLocationDesignatedBean;
import com.sim.bean.BusRealTimeBusStopBean;
import com.sim.bean.BusRealTimeBean;
import com.sim.bean.BusRealTimeLineBean;
import com.sim.bean.BusRouteBean;
import com.sim.bean.PublicArticleBean;
import com.sim.bean.PublicAuthorBean;
import com.sim.bean.WangyiBean;
import com.sim.util.RetrofitUtil;

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
    public void getWangYiNew(Subscriber<WangyiBean> subscriber, String page) {
        Observable observable = getApiService().getWangYiNews(page);
        toSubscribe(observable, subscriber);
    }

    /**
     * 轮播图图片获取
     */
    public void getBanner(Subscriber<BannerBean> subscriber) {
        Observable observable = getApiService().getBanner();
        toSubscribe(observable, subscriber);
    }

    /**
     * 公众号获取
     */
    public void getPublicAuthor(Subscriber<PublicAuthorBean> subscriber) {
        Observable observable = getApiService().getPublicAuthor();
        toSubscribe(observable, subscriber);
    }

    public void getPublicArticle(Subscriber<PublicArticleBean> subscriber) {
        Observable observable = getApiService().getPublicArticle();
        toSubscribe(observable, subscriber);
    }


    /**
     * 实时公交路线查询
     */
    public void getLineListByLineName(Subscriber<BusRealTimeLineBean> subscriber, String handlerName, String key) {
        Observable observable = getApiService().getLineListByLineName(handlerName, key);
        toSubscribe(observable, subscriber);
    }

    /**
     * 公交路线查询
     */
    public void getStationList(Subscriber<BusRealTimeBusStopBean> subscriber, String handlerName, String lineId) {
        Observable observable = getApiService().getStationList(handlerName, lineId);
        toSubscribe(observable, subscriber);
    }

    /**
     * 实时公交查询
     */
    public void getBusListOnRoad(Subscriber<BusRealTimeBean> subscriber, String handlerName, String lineName, String fromStation) {
        Observable observable = getApiService().getBusListOnRoad(handlerName, lineName, fromStation);
        toSubscribe(observable, subscriber);
    }


    /**
     * 搜索位置的网络请求
     */
    public void getStartOrEndLocation(Subscriber<BusLocationBean> subscriber, String keywords) {
        Observable observable = getApiService().getStartOrEndLocation("false", keywords);
        toSubscribe(observable, subscriber);
    }

    /**
     * 起始位置和终点位置的位置信息请求
     *
     * @param subscriber
     * @param keywords   搜索关键字
     */
    public void getLocation(Subscriber<BusLocationDesignatedBean> subscriber, String keywords) {
        Observable observable = getApiService().getLocation("", "all", "1", "10", "zh_cn", keywords);
        toSubscribe(observable, subscriber);
    }

    /**
     * 出行方案的网络请求
     *
     * @param subscriber
     * @param origin      起点
     * @param destination 终点
     */
    public void getRoute(Subscriber<BusRouteBean> subscriber, String origin, String destination) {
        Observable observable = getApiService().getRoute(origin, destination, "珠海", "0", "");
        toSubscribe(observable, subscriber);
    }

}
