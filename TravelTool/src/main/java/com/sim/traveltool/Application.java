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

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        //自定义奔溃处理类初始化
        CrashHandler.getInstance().init(getApplicationContext());

        APIFactory.getInstance().init(this);

        //Bugly初始化
        if (!BuildConfig.DEBUG) {
            Bugly.init(this, AppHelper.Bugly_APPID, false);
            Beta.checkUpgrade(false,false);
        }

        //JPush初始化
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(this);

        //Bmob初始化
        Bmob.initialize(this, AppHelper.Bmob_ApplicationID);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);//you must install multiDex whatever tinker is installed!
        Beta.installTinker();//安装tinker
    }

    public static Context getMyApplicationContext() {
        return context;
    }

}
