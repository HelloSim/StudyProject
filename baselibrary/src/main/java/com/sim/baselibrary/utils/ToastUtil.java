package com.sim.baselibrary.utils;

import android.content.Context;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;

/**
 * @Auther Sim
 * @Time 2019/4/22 1:05
 * @Description Toast工具类
 */
public class ToastUtil {
    /**
     * Toast实例，用于对本页出现的所有Toast进行处理
     */
    private static Toast myToast;

    /**
     * 此处是一个封装的Toast方法，可以取消掉上一次未完成的，直接进行下一次Toast
     *
     * @param context context
     * @param text    需要toast的内容
     */
    public static void toast(Context context, String text) {
        if (myToast != null) {
            myToast.cancel();
            myToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            myToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }
        myToast.show();
    }


    /**
     * Toast 短显示
     */
    public static void T_Success(Context context, String msg) {
        TastyToast.makeText(context, msg, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
    }

    public static void T_Warning(Context context, String msg) {
        TastyToast.makeText(context, msg, TastyToast.LENGTH_SHORT, TastyToast.WARNING);
    }

    public static void T_Error(Context context, String msg) {
        TastyToast.makeText(context, msg, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
    }

    public static void T_Info(Context context, String msg) {
        TastyToast.makeText(context, msg, TastyToast.LENGTH_SHORT, TastyToast.INFO);
    }

    public static void T_Default(Context context, String msg) {
        TastyToast.makeText(context, msg, TastyToast.LENGTH_SHORT, TastyToast.DEFAULT);
    }

    public static void T_Confusing(Context context, String msg) {
        TastyToast.makeText(context, msg, TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
    }

    /**
     * Toast 长显示
     */
    public static void T_Success_Long(Context context, String msg) {
        TastyToast.makeText(context, msg, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
    }

    public static void T_Warning_Long(Context context, String msg) {
        TastyToast.makeText(context, msg, TastyToast.LENGTH_LONG, TastyToast.WARNING);
    }

    public static void T_Error_Long(Context context, String msg) {
        TastyToast.makeText(context, msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);
    }

    public static void T_Info_Long(Context context, String msg) {
        TastyToast.makeText(context, msg, TastyToast.LENGTH_LONG, TastyToast.INFO);
    }

    public static void T_Default_Long(Context context, String msg) {
        TastyToast.makeText(context, msg, TastyToast.LENGTH_LONG, TastyToast.DEFAULT);
    }

    public static void T_Confusing_Long(Context context, String msg) {
        TastyToast.makeText(context, msg, TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
    }

}
