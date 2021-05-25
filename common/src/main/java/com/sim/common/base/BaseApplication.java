package com.sim.common.base;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.sim.common.AppHelper;
import com.sim.common.utils.CrashHandler;
import com.sim.common.utils.LogUtil;
import com.sim.http.APIFactory;

import cn.bmob.v3.Bmob;

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
        isDebug = context.getApplicationInfo() != null && (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        LogUtil.d(getClass(), isDebug ? "DeBug模式" : "Release版本");

        if (!isDebug) {
            CrashHandler.getInstance().init(context);//自定义奔溃处理类初始化
        }
        APIFactory.getInstance().init(context);

        Bmob.initialize(this, AppHelper.Bmob_ApplicationID);//Bmob初始化
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public static Context getMyApplicationContext() {
        return context;
    }

    public static Boolean getIsDebug() {
        return isDebug;
    }

}
