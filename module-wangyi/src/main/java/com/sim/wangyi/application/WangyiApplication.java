package com.sim.wangyi.application;

import android.app.Application;

import com.sim.basicres.base.BaseApplication;
import com.sim.http.APIFactory;
import com.sim.user.utils.BmobInit;

public class WangyiApplication extends BaseApplication {

    private static Application application;

    public static Application getApplication() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        APIFactory.getInstance().init(this);
        BmobInit.init(getApplicationContext());
    }

}
