package com.sim.test.utils;

import android.util.Log;

import com.sim.test.BuildConfig;


/**
 * @Time: 2020/9/21 23:57
 * @Author: HelloSim
 * @Description :Log统一管理类
 */
public class LogUtil {

    // 是否需要打印bug，可以在application的onCreate函数里面初始化
    public static boolean isDebug = BuildConfig.DEBUG;

    private static final String TAG = "LogUtil";

    private LogUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    // 下面四个是默认tag的函数
    public static void v(String msg) {
        if (isDebug) Log.v(TAG, msg);
    }

    public static void d(String msg) {
        if (isDebug) Log.d(TAG, msg);
    }

    public static void i(String msg) {
        if (isDebug) Log.i(TAG, msg);
    }

    public static void e(String msg) {
        if (isDebug) Log.e(TAG, msg);
    }

    // 下面是传入自定义tag的函数
    public static void v(String tag, String msg) {
        if (isDebug) Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug) Log.i(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (isDebug) Log.i(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (isDebug) Log.i(tag, msg);
    }

}
