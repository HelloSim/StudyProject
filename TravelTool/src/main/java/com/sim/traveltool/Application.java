package com.sim.traveltool;

import android.content.Context;

import androidx.multidex.MultiDex;

import com.sim.baselibrary.utils.CrashHandler;
import com.sim.traveltool.internet.APIFactory;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import cn.bmob.v3.Bmob;
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
        CrashHandler.getInstance().init(getApplicationContext());//自定义奔溃处理类初始化

        APIFactory.getInstance().init(this);

        Bugly.init(this, AppHelper.Bugly_APPID, BuildConfig.DEBUG);//Bugly初始化
        JPushInterface.setDebugMode(BuildConfig.DEBUG);//JPush设置是否debug
        JPushInterface.init(this);//JPush初始化
        Bmob.initialize(this, AppHelper.Bmob_ApplicationID);//Bmob初始化
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);//you must install multiDex whatever tinker is installed!
        Beta.installTinker();//安装tinker
    }

}
