package com.sim.traveltool;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.sim.common.AppHelper;
import com.sim.common.base.BaseApplication;
import com.sim.common.utils.CrashHandler;
import com.sim.common.utils.LogUtil;
import com.sim.http.APIFactory;
import com.sim.sharedlibrary.base.AppConfig;
import com.sim.sharedlibrary.base.IComponentApplication;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import cn.bmob.v3.Bmob;
import cn.jpush.android.api.JPushInterface;

/**
 * @ author: Sim
 * @ time： 2021/5/25 16:13
 * @ description：
 */
public class App extends BaseApplication implements IComponentApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        if (!getIsDebug()) CrashHandler.getInstance().init(getMyApplicationContext());//自定义奔溃处理类初始化

        Bugly.init(getMyApplicationContext(), AppHelper.Bugly_APPID, getIsDebug());//Bugly初始化
//        Beta.autoInit = true;//启动自动初始化升级模块
//        Beta.autoCheckUpgrade = true;//自动检查升级
//        Beta.upgradeCheckPeriod = 1000 * 60;//设置升级检查周期为60s
//        Beta.initDelay = 1000 * 5;//设置启动延迟为5s

        initialize(this);

        Bmob.initialize(getMyApplicationContext(), AppHelper.Bmob_ApplicationID);//Bmob初始化

        JPushInterface.setDebugMode(getIsDebug());//JPush设置DebugMode
        JPushInterface.init(getMyApplicationContext());//JPush初始化

        APIFactory.getInstance().init(getApplicationContext());
    }

    /**
     * 这里其实就是反射拿到各个模块的Application，调用其中的initialize方法
     *
     * @param application
     */
    @Override
    public void initialize(Application application) {
        for (String cpnt : AppConfig.Components) {
            try {
                Class<?> clz = Class.forName(cpnt);
                Object obj = clz.newInstance();
                if (obj instanceof IComponentApplication) {
                    ((IComponentApplication) obj).initialize(this);
                }
            } catch (Exception e) {
                LogUtil.e(this.getClass(), e.getMessage());
            }
        }
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
