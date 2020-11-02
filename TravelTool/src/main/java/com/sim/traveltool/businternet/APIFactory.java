package com.sim.traveltool.businternet;

import com.sim.traveltool.bean.bus.BusRealTimeDataBean;
import com.sim.traveltool.bean.bus.BusRealTimeLineDataBean;
import com.sim.traveltool.bean.bus.BusRealTimeByLineDataBean;

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

    public void getLineListByLineName(Subscriber<BusRealTimeLineDataBean> subscriber, String handlerName, String key, String time) {
        Observable observable = apiService.getLineListByLineName(handlerName, key, time);
        toSubscribe(observable, subscriber);
    }

    public void getStationList(Subscriber<BusRealTimeByLineDataBean> subscriber, String handlerName, String lineId, String time) {
        Observable observable = apiService.getStationList(handlerName, lineId, time);
        toSubscribe(observable, subscriber);
    }

    public void getBusListOnRoad(Subscriber<BusRealTimeDataBean> subscriber, String handlerName, String lineName, String fromStation, String time) {
        Observable observable = apiService.getBusListOnRoad(handlerName, lineName, fromStation, time);
        toSubscribe(observable, subscriber);
    }

}
