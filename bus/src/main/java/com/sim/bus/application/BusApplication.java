package com.sim.bus.application;

import android.app.Application;
import android.util.Log;

import com.sim.bus.impservice.BusService;
import com.sim.common.base.BaseApplication;
import com.sim.http.APIFactory;
import com.sim.sharedlibrary.base.IComponentApplication;
import com.sim.sharedlibrary.base.ServiceFactory;

/**
 * @ author: Sim
 * @ time： 2021/5/26 16:37
 * @ description：
 */
public class BusApplication extends BaseApplication implements IComponentApplication {

    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        APIFactory.getInstance().init(this);
    }

    public static Application getApplication() {
        return application;
    }

    @Override
    public void initialize(Application app) {
        application = app;
        Log.d("Sim", "initialize: setBusService");
        ServiceFactory.getInstance().setBusService(new BusService());
    }

}
