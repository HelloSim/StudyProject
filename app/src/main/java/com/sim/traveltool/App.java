package com.sim.traveltool;

import android.content.Context;

import androidx.multidex.MultiDex;

import com.sim.basicres.base.BaseApplication;
import com.sim.basicres.utils.CrashHandler;
import com.sim.http.APIFactory;
import com.sim.user.utils.BmobInit;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import cn.jpush.android.api.JPushInterface;

public class App extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        if (!getIsDebug()) CrashHandler.getInstance().init(getMyApplicationContext());//自定义奔溃处理类初始化

        Bugly.init(getMyApplicationContext(), "c7dbd02c92", getIsDebug());//Bugly初始化
//        Beta.autoInit = true;//启动自动初始化升级模块
//        Beta.autoCheckUpgrade = true;//自动检查升级
//        Beta.upgradeCheckPeriod = 1000 * 60;//设置升级检查周期为60s
//        Beta.initDelay = 1000 * 5;//设置启动延迟为5s

        BmobInit.init(getApplicationContext());

        JPushInterface.setDebugMode(getIsDebug());//JPush设置DebugMode
        JPushInterface.init(getMyApplicationContext());//JPush初始化

        APIFactory.getInstance().init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);//you must install multiDex whatever tinker is installed!
        Beta.installTinker();//安装tinker
    }

}
