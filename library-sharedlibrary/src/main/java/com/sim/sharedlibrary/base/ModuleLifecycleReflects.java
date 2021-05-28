package com.sim.sharedlibrary.base;

/**
 * @ author: Sim
 * @ time： 2021/5/28 10:08
 * @ description：
 */
public class ModuleLifecycleReflects {

    /**
     * 各个组件
     */
    private static final String WangyiModule = "com.sim.wangyi.application.WangyiApplication";
    private static final String BusModule = "com.sim.bus.application.BusApplication";
    private static final String RecordModule = "com.sim.record.application.RecordApplication";
    private static final String UserModule = "com.sim.user.application.UserApplication";

    public static String[] initModuleNames = {WangyiModule, BusModule, RecordModule, UserModule};

}
