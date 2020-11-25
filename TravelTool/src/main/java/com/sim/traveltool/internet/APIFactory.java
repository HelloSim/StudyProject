package com.sim.traveltool.internet;


import com.sim.traveltool.bean.BusRealTimeByLineDataBean;
import com.sim.traveltool.bean.BusRealTimeDataBean;
import com.sim.traveltool.bean.BusRealTimeLineDataBean;
import com.sim.traveltool.bean.NewsWangYiBean;
import com.sim.traveltool.bean.UserInfo;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Grugsum on 2019/4/22.
 * 单例
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
     * 实时公交路线查询
     *
     * @param subscriber
     * @param handlerName
     * @param key
     * @param time
     */
    public void getLineListByLineName(Subscriber<BusRealTimeLineDataBean> subscriber, String handlerName, String key, String time) {
        Observable observable = getBusAPIService().getLineListByLineName(handlerName, key, time);
        toSubscribe(observable, subscriber);
    }

    /**
     * 公交路线查询
     *
     * @param subscriber
     * @param handlerName
     * @param lineId
     * @param time
     */
    public void getStationList(Subscriber<BusRealTimeByLineDataBean> subscriber, String handlerName, String lineId, String time) {
        Observable observable = getBusAPIService().getStationList(handlerName, lineId, time);
        toSubscribe(observable, subscriber);
    }

    /**
     * 实时公交查询
     *
     * @param subscriber
     * @param handlerName
     * @param lineName
     * @param fromStation
     * @param time
     */
    public void getBusListOnRoad(Subscriber<BusRealTimeDataBean> subscriber, String handlerName, String lineName, String fromStation, String time) {
        Observable observable = getBusAPIService().getBusListOnRoad(handlerName, lineName, fromStation, time);
        toSubscribe(observable, subscriber);
    }


    /**
     * NewsData的网络请求
     *
     * @param subscriber
     * @param page
     * @param count
     */
    public void getWangYiNew(Subscriber<NewsWangYiBean> subscriber, String page, String count) {
        Observable observable = getUserApiService().getWangYiNews(page, count);
        toSubscribe(observable, subscriber);
    }


    /**
     * 用户登录的网络请求
     *
     * @param subscriber
     * @param apikey
     * @param name
     * @param passwd
     */
    public void logIn(Subscriber<UserInfo> subscriber, String apikey, String name, String passwd) {
        Observable observable = getUserApiService().logIn(apikey, name, passwd);
        toSubscribe(observable, subscriber);
    }

    /**
     * 用户注册的网络请求
     *
     * @param subscriber
     * @param apikey
     * @param name
     * @param passwd
     * @param headerImg
     * @param nikeName
     * @param autograph
     * @param phone
     * @param email
     * @param remarks
     * @param vipGrade
     */
    public void registerUser(Subscriber<UserInfo> subscriber, String apikey, String name, String passwd,
                             String headerImg, String nikeName, String autograph,
                             String phone, String email, String remarks, String vipGrade) {
        Observable observable = getUserApiService().registerUser(apikey, name, passwd, headerImg, nikeName, autograph, phone, email, remarks, vipGrade);
        toSubscribe(observable, subscriber);
    }

    /**
     * 用户更新的网络请求
     *
     * @param subscriber
     * @param apikey
     * @param name
     * @param passwd
     * @param headerImg
     * @param nikeName
     * @param autograph
     * @param phone
     * @param email
     * @param remarks
     * @param vipGrade
     */
    public void updateUserInfo(Subscriber<UserInfo> subscriber, String apikey, String name, String passwd,
                               String headerImg, String nikeName, String autograph,
                               String phone, String email, String remarks, String vipGrade) {
        Observable observable = getUserApiService().updateUserInfo(apikey, name, passwd, headerImg, nikeName, autograph, phone, email, remarks, vipGrade);
        toSubscribe(observable, subscriber);
    }

}
