package com.sim.traveltool.utils;

import android.annotation.SuppressLint;
import android.icu.util.Calendar;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    public static String timeFormat(long timeMillis, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        return format.format(new Date(timeMillis));
    }

    public static String formatPhotoDate(long time) {
        return timeFormat(time, "yyyy-MM-dd");
    }

    public static String formatPhotoDate(String path) {
        File file = new File(path);
        if (file.exists()) {
            long time = file.lastModified();
            return formatPhotoDate(time);
        }
        return "1970-01-01";
    }

    public static String getNowTimeAll() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS");
        return sDateFormat.format(new Date());
    }

    public static String getNowTimeDayHourMinuteSecond() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        return sDateFormat.format(new Date());
    }

    public static String getNowTimeDayHourMinute() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
        return sDateFormat.format(new Date());
    }

    public static String getNowTimeDay() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return sDateFormat.format(new Date());
    }

    public static String getNowTimeHourMinuteSecond() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("HH-mm-ss");
        return sDateFormat.format(new Date());
    }
    public static long getCurTimeLong(){//获取时间戳
        long time= System.currentTimeMillis();
        return time;
    }

}
