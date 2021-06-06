package com.sim.apublic.application;

import com.sim.basicres.base.BaseApplication;
import com.sim.http.APIFactory;

public class APublicApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        APIFactory.getInstance().init(this);
//        BmobInit.init(getApplicationContext());
    }

}
