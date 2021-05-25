package com.sim.record.serviceimp;

import com.luojilab.component.componentlib.applicationlike.IApplicationLike;
import com.luojilab.component.componentlib.router.Router;
import com.luojilab.component.componentlib.router.ui.UIRouter;

/**
 * @ author: Sim
 * @ time： 2021/5/25 15:37
 * @ description：
 */
public class RecordLike implements IApplicationLike {

    Router router = Router.getInstance();
    UIRouter uiRouter = UIRouter.getInstance();

    @Override
    public void onCreate() {
        uiRouter.registerUI("record");
        router.addService(RecordServiceImp.class.getSimpleName(), new RecordServiceImp());
    }

    @Override
    public void onStop() {
        uiRouter.unregisterUI("record");
        router.removeService(RecordServiceImp.class.getSimpleName());
    }

}
