package com.sim.bus.serviceimp;

import com.luojilab.component.componentlib.applicationlike.IApplicationLike;
import com.luojilab.component.componentlib.router.Router;
import com.luojilab.component.componentlib.router.ui.UIRouter;

/**
 * @ author: Sim
 * @ time： 2021/5/25 15:38
 * @ description：
 */
public class BusLike implements IApplicationLike {

    Router router = Router.getInstance();
    UIRouter uiRouter = UIRouter.getInstance();

    @Override
    public void onCreate() {
        uiRouter.registerUI("bus");
        router.addService(BusServiceImp.class.getSimpleName(), new BusServiceImp());
    }

    @Override
    public void onStop() {
        uiRouter.unregisterUI("bus");
        router.removeService(BusServiceImp.class.getSimpleName());
    }

}
