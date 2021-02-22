package com.sim.baselibrary.utils;

import android.annotation.SuppressLint;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * @author Sim --- 系统时间获取工具
 */
@SuppressLint("NewApi")
public class TimeUtil {

    static Calendar calendar = Calendar.getInstance();

    public static Calendar getCalendar() {
        return calendar;
    }

    /**
     * 获取当前年份
     *
     * @return
     */
    public static int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取当前月份
     *
     * @return
     */
    public static int getMonth() {
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当天日期
     *
     * @return
     */
    public static int getDay() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前小时
     *
     * @return
     */
    public static int getHour() {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取当前分钟
     *
     * @return
     */
    public static int getMinute() {
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 获取当前秒
     *
     * @return
     */
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

    /**
     * 获取指定年月的所有周六日
     *
     * @param year  2020
     * @param month 11
     */
    public static void getWeekDate(int year, int month) {
        month--;
        Calendar calendar = new GregorianCalendar(year, month, 1);
        int i = 1;
        while (calendar.get(Calendar.MONTH) < month + 1) {
            calendar.set(Calendar.WEEK_OF_YEAR, i++);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            if (calendar.get(Calendar.MONTH) == month) {
                System.out.println(calendar.getTime());
            }
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            if (calendar.get(Calendar.MONTH) == month) {
                System.out.println(calendar.getTime());
            }
        }
    }

    /**
     * 获取当月天数
     *
     * @return
     */
    public static int getDayListOfMonth() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        return calendar.getActualMaximum(Calendar.DATE);
    }

    /**
     * 根据年 月 获取对应的月份 天数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
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

    public static long getCurTimeLong() {//获取时间戳
        long time = System.currentTimeMillis();
        return time;
    }

}
