package com.sim.traveltool;

import android.content.Context;

import androidx.multidex.MultiDex;

import com.sim.baselibrary.utils.CrashHandler;
import com.sim.traveltool.internet.APIFactory;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import cn.jpush.android.api.JPushInterface;

/**
 * @Auther Sim
 * @Time 2020/4/27 1:05
 * @Description Application
 */
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(getApplicationContext());
        APIFactory.getInstance().init(this);
        Bugly.init(this, AppHelper.Bugly_APPID, BuildConfig.DEBUG);
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);//you must install multiDex whatever tinker is installed!
        Beta.installTinker();//安装tinker
    }

}
