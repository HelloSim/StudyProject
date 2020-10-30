package com.sim.record.utils;

import android.content.Context;

import com.sdsmdg.tastytoast.TastyToast;

/**
 * @Auther Sim
 * @Time 2020/10/21 9:52
 * @Description Toast 工具
 */
public class ToastUtil {

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
