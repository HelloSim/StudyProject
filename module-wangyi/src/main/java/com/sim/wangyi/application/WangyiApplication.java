package com.sim.wangyi.application;

import android.app.Application;

import com.sim.basicres.base.BaseApplication;
import com.sim.http.APIFactory;

/**
 * @ author: Sim
 * @ time： 2021/5/26 16:37
 * @ description：
 */
public class WangyiApplication extends BaseApplication {

    private static Application application;

    public static Application getApplication() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        APIFactory.getInstance().init(this);
    }

}
