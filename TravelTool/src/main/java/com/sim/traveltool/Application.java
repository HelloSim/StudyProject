package com.sim.traveltool;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import androidx.multidex.MultiDex;

import com.sim.baselibrary.utils.CrashHandler;
import com.sim.traveltool.internet.APIFactory;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import cn.bmob.v3.Bmob;
import cn.jpush.android.api.JPushInterface;

/**
 * @Author： Sim
 * @Time： 2020/4/27 1:05
 * @Description： Application
 */
public class Application extends android.app.Application {

    private static Context context;
    private static Boolean isDebug = null;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        isDebug = context.getApplicationInfo() != null &&
                (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;

        APIFactory.getInstance().init(this);

        if (!isDebug) {
            CrashHandler.getInstance().init(getApplicationContext());//自定义奔溃处理类初始化

            //Bugly初始化
            Bugly.init(this, AppHelper.Bugly_APPID, false);
            Beta.autoInit = true;//启动自动初始化升级模块
            Beta.autoCheckUpgrade = true;//自动检查升级
            Beta.upgradeCheckPeriod = 1000 * 60;//设置升级检查周期为60s
            Beta.initDelay = 1000 * 5;//设置启动延迟为1s

            //JPush设置DebugMode
            JPushInterface.setDebugMode(false);
        }
        //JPush初始化
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

    public static Boolean getIsDebug() {
        return isDebug;
    }

}
