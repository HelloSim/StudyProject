package com.sim.baselibrary.utils;

import android.util.Log;

import com.sim.baselibrary.BuildConfig;

/**
 * @Auther Sim
 * @Time 2019/4/22 1:05
 * @Description Log工具类
 */
public class LogUtil {

    /**
     * @param classSimpleName 类名
     * @param msg             打印信息
     */
    public static void v(String classSimpleName, String msg) {
        if (BuildConfig.DEBUG) {
            Log.v("Sim_" + classSimpleName, msg);
        }
    }

    public static void d(String classSimpleName, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d("Sim_" + classSimpleName, msg);
        }
    }

    public static void i(String classSimpleName, String msg) {
        if (BuildConfig.DEBUG) {
            Log.i("Sim_" + classSimpleName, msg);
        }
    }

    public static void w(String classSimpleName, String msg) {
        if (BuildConfig.DEBUG) {
            Log.w("Sim_" + classSimpleName, msg);
        }
    }

    public static void e(String classSimpleName, String msg) {
        if (BuildConfig.DEBUG) {
            Log.e("Sim_" + classSimpleName, msg);
        }
    }

}
