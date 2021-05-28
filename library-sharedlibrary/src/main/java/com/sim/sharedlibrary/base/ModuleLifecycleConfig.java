package com.sim.sharedlibrary.base;

import android.app.Application;

import androidx.annotation.Nullable;

/**
 * @ author: Sim
 * @ time： 2021/5/28 10:07
 * @ description：
 */
public class ModuleLifecycleConfig {

    private ModuleLifecycleConfig() {
    }

    private static class SingleHolder {
        private static ModuleLifecycleConfig instance = new ModuleLifecycleConfig();
    }

    public static ModuleLifecycleConfig getInstance() {
        return SingleHolder.instance;
    }

    /**
     * 模块初始化
     */
    public void initModuleAhead(@Nullable Application application) {
        for (String moduleName : ModuleLifecycleReflects.initModuleNames) {
            try {
                Class<?> clazz = Class.forName(moduleName);
                IComponentApplication init = (IComponentApplication) clazz.newInstance();
                init.initialize(application);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
