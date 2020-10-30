package com.sim.record.utils;

import android.annotation.SuppressLint;
import android.icu.util.Calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther Sim
 * @Time 2020/10/20 14:54
 * @Description 系统时间获取工具
 */

@SuppressLint("NewApi")
public class TimeUtil {

    static Calendar calendar = Calendar.getInstance();

    public static Calendar getCalendar() {
        return calendar;
    }

    public static int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    public static int getMonth() {
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getDay() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getHour() {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute() {
        return calendar.get(Calendar.MINUTE);
    }

    public static int getSecond() {
        return calendar.get(Calendar.SECOND);
    }

    /**
     * 判断日期是周几
     *
     * @param dates 参数举例  String dates="2018-11-11";
     * @return
     */
    public static String getWeek(String dates) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = f.parse(dates);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(d);
        String s;
        switch (calendar.get(Calendar.DAY_OF_WEEK) - 1) {
            case 1:
                return "星期一";
            case 2:
                return "星期二";
            case 3:
                return "星期三";
            case 4:
                return "星期四";
            case 5:
                return "星期五";
            case 6:
                return "星期六";
            case 0:
                return "星期日";
            default:
                return "";
        }
    }

}
