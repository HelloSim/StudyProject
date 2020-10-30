package com.sim.traveltool;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * Created by Sim on 2020/4/24
 */
public class Application extends android.app.Application {

    public static Context context;

    @SuppressLint("ApplySharedPref")
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        com.sim.traveltool.internet.APIFactory.getInstance().init(this);
        com.sim.traveltool.businternet.APIFactory.getInstance().init(this);
    }

}
