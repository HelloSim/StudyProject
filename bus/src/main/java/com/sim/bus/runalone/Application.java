package com.sim.bus.runalone;

import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.sim.common.base.BaseApplication;

public class Application extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        UIRouter.getInstance().registerUI("bus");
    }

}
