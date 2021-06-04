package com.sim.bus.application;

import android.app.Application;

import com.sim.basicres.base.BaseApplication;
import com.sim.http.APIFactory;

public class BusApplication extends BaseApplication {

    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        APIFactory.getInstance().init(this);
    }

    public static Application getApplication() {
        return application;
    }

}
