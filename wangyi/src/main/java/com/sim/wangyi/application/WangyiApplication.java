package com.sim.wangyi.application;

import android.app.Application;
import android.util.Log;

import com.sim.common.base.BaseApplication;
import com.sim.http.APIFactory;
import com.sim.sharedlibrary.base.IComponentApplication;
import com.sim.sharedlibrary.base.ServiceFactory;
import com.sim.wangyi.impservice.WangyiService;

/**
 * @ author: Sim
 * @ time： 2021/5/26 16:37
 * @ description：
 */
public class WangyiApplication extends BaseApplication implements IComponentApplication {

    private static Application application;

    public static Application getApplication() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        APIFactory.getInstance().init(this);
    }

    @Override
    public void initialize(Application app) {
        application = app;
        Log.d("Sim", "initialize: setWangyiService");
        ServiceFactory.getInstance().setWangyiService(new WangyiService());
    }

}
