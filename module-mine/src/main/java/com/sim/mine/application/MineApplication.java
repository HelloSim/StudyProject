package com.sim.mine.application;

import com.sim.basicres.base.BaseApplication;
import com.sim.http.APIFactory;
import com.sim.mine.utils.BmobInit;

/**
 * @ author: Sim
 * @ time： 2021/5/26 16:37
 * @ description：
 */
public class MineApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        APIFactory.getInstance().init(this);
        BmobInit.init(getApplicationContext());
    }

}
