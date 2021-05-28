package com.sim.sharedlibrary.base;

import com.sim.sharedlibrary.moduleservice.BusService;
import com.sim.sharedlibrary.moduleservice.RecordService;
import com.sim.sharedlibrary.moduleservice.UserService;
import com.sim.sharedlibrary.moduleservice.WangyiService;

/**
 * @ author: Sim
 * @ time： 2021/5/26 16:40
 * @ description：
 */
public class ServiceFactory {

    private static final ServiceFactory instance = new ServiceFactory();

    private WangyiService wangyiService;
    private BusService busService;
    private RecordService recordService;
    private UserService userService;

    /**
     * 构造单例
     */
    private ServiceFactory() {
    }

    public static ServiceFactory getInstance() {
        return instance;
    }

    /**
     * wangyi模块
     */
    public WangyiService getWangyiService() {
        return wangyiService;
    }

    public void setWangyiService(WangyiService wangyiService) {
        this.wangyiService = wangyiService;
    }

    /**
     * Bus模块
     */
    public BusService getBusService() {
        return busService;
    }

    public void setBusService(BusService busService) {
        this.busService = busService;
    }

    /**
     * Record模块
     */
    public RecordService getRecordService() {
        return recordService;
    }

    public void setRecordService(RecordService recordService) {
        this.recordService = recordService;
    }

    /**
     * User模块
     */
    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
