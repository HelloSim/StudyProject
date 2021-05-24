package com.sim.record;

import com.sim.common.AppHelper;

import cn.bmob.v3.Bmob;

/**
 * @ author: Sim
 * @ time： 2021/5/24 16:11
 * @ description：
 */
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, AppHelper.Bmob_ApplicationID);//Bmob初始化
    }

}
