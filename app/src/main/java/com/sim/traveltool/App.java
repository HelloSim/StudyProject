package com.sim.traveltool;

import android.content.Context;

import androidx.multidex.MultiDex;

import com.sim.basicres.base.BaseApplication;
import com.sim.basicres.utils.CrashHandler;
import com.sim.http.APIFactory;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import cn.bmob.v3.Bmob;
import cn.jpush.android.api.JPushInterface;

/**
 * @ author: Sim
 * @ time： 2021/5/25 16:13
 * @ description：
 */
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

        Bmob.initialize(getMyApplicationContext(), "62550b32bf5600010781ceeebc0e92ac");//Bmob初始化

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
