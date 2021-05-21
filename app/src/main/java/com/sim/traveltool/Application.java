package com.sim.traveltool;

import android.content.Context;

import androidx.multidex.MultiDex;

import com.sim.common.base.BaseApplication;
import com.sim.traveltool.http.APIFactory;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import cn.bmob.v3.Bmob;
import cn.jpush.android.api.JPushInterface;

/**
 * @author Sim --- Application
 */
public class Application extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        Bugly.init(this, AppHelper.Bugly_APPID, getIsDebug());//Bugly初始化
//        Beta.autoInit = true;//启动自动初始化升级模块
//        Beta.autoCheckUpgrade = true;//自动检查升级
//        Beta.upgradeCheckPeriod = 1000 * 60;//设置升级检查周期为60s
//        Beta.initDelay = 1000 * 5;//设置启动延迟为5s

        JPushInterface.setDebugMode(getIsDebug());//JPush设置DebugMode
        JPushInterface.init(this);//JPush初始化

        Bmob.initialize(this, AppHelper.Bmob_ApplicationID);//Bmob初始化

        APIFactory.getInstance().init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);//you must install multiDex whatever tinker is installed!
        Beta.installTinker();//安装tinker
    }

}
