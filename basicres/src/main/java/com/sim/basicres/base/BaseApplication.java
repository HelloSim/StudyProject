package com.sim.basicres.base;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.sim.basicres.utils.LogUtil;

/**
 * @ author: Sim
 * @ time： 2021/5/21 11:51
 * @ description：
 */
public class BaseApplication extends Application {

    private static Context context;
    private static Boolean isDebug = null;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        isDebug = context.getApplicationInfo() != null && (context.getApplicationInfo().flags
                & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        LogUtil.d(getClass(), isDebug ? "DeBug模式" : "Release版本");
    }

    public static Context getMyApplicationContext() {
        return context;
    }

    public static Boolean getIsDebug() {
        return isDebug;
    }

}
