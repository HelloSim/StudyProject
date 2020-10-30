package com.sim.test.utils;

import android.content.Context;
import android.widget.Toast;

import com.sim.test.Application;


/**
 * @Time: 2020/9/21 23:55
 * @Author: HelloSim
 * @Description :Toast统一管理类
 */
public class ToastUtil {

    private ToastUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isShow = true;

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(CharSequence message) {
        if (isShow)
            Toast.makeText(Application.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(Context context, int message) {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(int message) {
        if (isShow)
            Toast.makeText(Application.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showLong(CharSequence message) {
        if (isShow)
            Toast.makeText(Application.getContext(), message, Toast.LENGTH_LONG).show();
    }

    public static void showLong(Context context, int message) {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showLong(int message) {
        if (isShow)
            Toast.makeText(Application.getContext(), message, Toast.LENGTH_LONG).show();
    }

}