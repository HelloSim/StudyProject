package com.sim.baselibrary.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sim --- 正则表达式工具类
 */
public class RegexUtil {

    /**
     * Email 校验
     *
     * @param value
     * @return
     */
    public static boolean email(String value) {
        String regex = "^[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.])+[A-Za-z\\d]{2,4}$";
        return regex(value, regex);
    }

    /**
     * IP地址 校验
     *
     * @param value
     * @return
     */
    public static boolean ip(String value) {
        String regex = "((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))";
        return regex(value, regex);
    }

    /**
     * 验证手机号码
     *
     * @param phone
     * @return
     */
    public static boolean checkPhone(String phone) {
        String regex = "^((1[3,5,8][0-9])|(14[5,7])|(17[0,6,7,8])|(19[7]))\\d{8}$";
        return regex(phone,regex);
//        if (phone == null || phone.length() != 11) {
//            return false;
//        } else {
//            return true;
//        }
    }

    private static boolean regex(String value, String regex) {
        if (TextUtils.isEmpty(value)) {
            return false;
        }
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(value);
        boolean isMatch = m.matches();
        return isMatch;
    }

}
