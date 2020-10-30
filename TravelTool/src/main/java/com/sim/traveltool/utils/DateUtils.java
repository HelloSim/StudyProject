package com.sim.traveltool.utils;

import android.util.Log;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @Author: Sim
 * @Time: 2019/10/11 0:41
 * @Description :时间工具类
 */

public class DateUtils {

    public static Date dateUtil(String s) {
        String dateString = s.replaceAll("T", " ");
        Date date = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = df.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("InventoryInformationAda", "date:" + date);
        return date;
    }

    public static String stringDateUtil() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    private static Calendar c;
    private static int year;
    private static int month;
    private static int day;
    private static int hour;
    private static int minute;
    private static int second;
    private static int millisecond;


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
        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        second = c.get(Calendar.SECOND);
        millisecond = c.get(Calendar.MILLISECOND);
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

}
