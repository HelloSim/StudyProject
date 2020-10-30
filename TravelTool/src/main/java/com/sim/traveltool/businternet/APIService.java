package com.sim.traveltool.businternet;

import com.sim.traveltool.bean.BusRealTimeDataBean;
import com.sim.traveltool.bean.BusRealTimeLineDataBean;
import com.sim.traveltool.bean.BusRealTimeByLineDataBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Grugsum on 2019/4/22.
 * 接口API类
 */
public interface APIService {

    /**
     * 实时公交路线查询
     */
    @GET("/Handlers/BusQuery.ashx")
    Observable<BusRealTimeLineDataBean> getLineListByLineName(@Query("handlerName") String handlerName, @Query("key") String key,
                                                              @Query("_") String time);

    /**
     * 公交路线查询
     */
    @GET("/Handlers/BusQuery.ashx")
    Observable<BusRealTimeByLineDataBean> getStationList(@Query("handlerName") String handlerName, @Query("lineId") String lineId,
                                                         @Query("_") String time);

    /**
     * 实时公交查询
     */
    @GET("/Handlers/BusQuery.ashx")
    Observable<BusRealTimeDataBean> getBusListOnRoad(@Query("handlerName") String handlerName, @Query("lineName") String lineName,
                                                     @Query("fromStation") String fromStation, @Query("_") String time);

}