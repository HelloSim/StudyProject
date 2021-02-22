package com.sim.baselibrary.utils;

import android.util.Log;

import com.sim.baselibrary.BuildConfig;

/**
 * @author Sim --- Log工具类
 */
public class LogUtil {

    /**
     * @param className 类名
     * @param msg       打印信息
     */
    public static void v(Class className, String msg) {
        if (BuildConfig.DEBUG) {
            Log.v("【【【Sim_" + className.getSimpleName() + "】】】", msg);
        }
    }

    public static void d(Class className, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d("【【【Sim_" + className.getSimpleName() + "】】】", msg);
        }
    }

    public static void i(Class className, String msg) {
        if (BuildConfig.DEBUG) {
            Log.i("【【【Sim_" + className.getSimpleName() + "】】】", msg);
        }
    }

    public static void w(Class className, String msg) {
        if (BuildConfig.DEBUG) {
            Log.w("【【【Sim_" + className.getSimpleName() + "】】】", msg);
        }
    }

    public static void e(Class className, String msg) {
        if (BuildConfig.DEBUG) {
            Log.e("【【【Sim_" + className.getSimpleName() + "】】】", msg);
        }
    }

}
