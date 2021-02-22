package com.sim.baselibrary.utils;

import android.text.TextUtils;
import android.widget.TextView;

import java.util.Random;


/**
 * @author Sim --- 防止取值为空的工具类
 */
public class StringUtil {

    /**
     * 获取对象的值得方法,避免空指向
     *
     * @param o
     * @return
     */
    public static String getContent(Object o) {
        if (o == null) {
            return "";
        } else {
            return o.toString().trim();
        }
    }

    /**
     * 获取对象的值得方法,避免空指向
     *
     * @param tv
     * @return
     */
    public static String getContent(TextView tv) {
        if (tv == null) {
            return "";
        }
        return getContent(tv.getText().toString().trim());
    }

    /**
     * 获取对象的值得方法,避免空指向
     *
     * @param o
     * @return
     */
    public static String getContentNum(Object o) {
        if (o == null) {
            return "0";
        } else if (o instanceof TextView) {
            return getContentNum(StringUtil.getContent((TextView) o));
        } else {
            if (TextUtils.isEmpty(o.toString())) {
                return "0";
            }
            return o.toString().trim();
        }
    }

    /**
     * 返回长度为【strLength】的随机数，在前面补0
     *
     * @param strLength
     * @return
     */
    public static String getFixLenthString(int strLength) {
        Random rm = new Random();
        // 获得随机数
        double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);
        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(pross);
        // 返回固定的长度的随机数
        return fixLenthString.substring(1, strLength + 1);
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

}
