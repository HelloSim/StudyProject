package com.sim.mine.application;

import com.sim.basicres.base.BaseApplication;
import com.sim.http.APIFactory;
import com.sim.user.utils.BmobInit;


public class MineApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        APIFactory.getInstance().init(this);
        BmobInit.init(getApplicationContext());
    }

}
