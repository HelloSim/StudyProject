package com.sim.wangyi.serviceimp;

import com.luojilab.component.componentlib.applicationlike.IApplicationLike;
import com.luojilab.component.componentlib.router.Router;
import com.luojilab.component.componentlib.router.ui.UIRouter;

/**
 * @ author: Sim
 * @ time： 2021/5/25 15:34
 * @ description：
 */
public class WangyiLike implements IApplicationLike {

    Router router = Router.getInstance();
    UIRouter uiRouter = UIRouter.getInstance();

    @Override
    public void onCreate() {
        uiRouter.registerUI("wangyi");
        router.addService(WangyiServiceImp.class.getSimpleName(), new WangyiServiceImp());
    }

    @Override
    public void onStop() {
        uiRouter.unregisterUI("set");
        router.removeService(WangyiServiceImp.class.getSimpleName());
    }

}
