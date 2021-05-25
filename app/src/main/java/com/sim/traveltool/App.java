package com.sim.traveltool;

import com.luojilab.component.componentlib.log.ILogger;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.sim.common.base.BaseApplication;

import org.github.jimu.msg.EventManager;

/**
 * @ author: Sim
 * @ time： 2021/5/25 16:13
 * @ description：
 */
public class App extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        ILogger.logger.setDefaultTag("JIMU");
        EventManager.init(this);
        UIRouter.enableDebug();
        UIRouter.getInstance().registerUI("bus");
        UIRouter.getInstance().registerUI("record");
        UIRouter.getInstance().registerUI("wangyi");
        UIRouter.getInstance().registerUI("user");
    }
}
