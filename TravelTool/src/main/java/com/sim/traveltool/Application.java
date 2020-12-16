package com.sim.traveltool;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.sim.baselibrary.utils.CrashHandler;
import com.sim.traveltool.internet.APIFactory;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

/**
 * @Auther Sim
 * @Time 2020/4/27 1:05
 * @Description Application
 */
public class Application extends android.app.Application {
    private static final String TAG = "Sim_Application";

    public static Context context;

    @SuppressLint("ApplySharedPref")
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        CrashHandler.getInstance().init(context);
        APIFactory.getInstance().init(this);
        Bugly.init(this, AppHelper.Bugly_APPID, BuildConfig.DEBUG);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);
        // 安装tinker
        Beta.installTinker();
    }

}
