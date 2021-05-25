package com.sim.user.serviceimp;

import com.luojilab.component.componentlib.applicationlike.IApplicationLike;
import com.luojilab.component.componentlib.router.Router;
import com.luojilab.component.componentlib.router.ui.UIRouter;

/**
 * @ author: Sim
 * @ time： 2021/5/25 15:36
 * @ description：
 */
public class UserLike implements IApplicationLike {

    Router router = Router.getInstance();
    UIRouter uiRouter = UIRouter.getInstance();

    @Override
    public void onCreate() {
        uiRouter.registerUI("user");
        router.addService(UserServiceImp.class.getSimpleName(), new UserServiceImp());
    }

    @Override
    public void onStop() {
        uiRouter.unregisterUI("user");
        router.removeService(UserServiceImp.class.getSimpleName());
    }

}
