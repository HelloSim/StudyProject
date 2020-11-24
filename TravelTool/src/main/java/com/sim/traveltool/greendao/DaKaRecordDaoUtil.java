package com.sim.traveltool.greendao;

import com.haibin.calendarview.Calendar;
import com.sim.traveltool.greendao.db.DaKaRecordDao;
import com.sim.traveltool.bean.record.DaKaRecord;
import com.sim.traveltool.utils.HttpUtil;
import com.sim.traveltool.utils.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.List;

import static com.sim.traveltool.Application.daKaRecordDao;


/**
 * @Auther Sim
 * @Time 2020/10/25 1:37
 * @Description
 */
public class DaKaRecordDaoUtil {

    /**
     * 获取年月日
     *
     * @param calendar
     * @return
     */
    public static String getYMD(Calendar calendar) {
        return String.valueOf(calendar.getYear()) + calendar.getMonth() + calendar.getDay();
    }

    /**
     * 获取年月日
     *
     * @param calendar
     * @return
     */
    public static String getYearMonthDay(Calendar calendar) {
        return calendar.getYear() + "-" + calendar.getMonth() + "-" + calendar.getDay();
    }

    /**
     * 获取年月
     *
     * @param calendar
     * @return
     */
    public static String getYearMonth(Calendar calendar) {
        return calendar.getYear() + "-" + calendar.getMonth();
    }

    /**
     * 获取年
     *
     * @param calendar
     * @return
     */
    public static String getYear(Calendar calendar) {
        return String.valueOf(calendar.getYear());
    }

    /**
     * 获取月
     *
     * @param calendar
     * @return
     */
    public static String getMonth(Calendar calendar) {
        return String.valueOf(calendar.getMonth());
    }

    /**
     * 获取日
     *
     * @param calendar
     * @return
     */
    public static String getDay(Calendar calendar) {
        return String.valueOf(calendar.getDay());
    }


    /**
     * ---------------------------------------------------------------------------------------------
     * 数据库查询操作
     */

    /**
     * 查询指定年份月份的打卡记录
     *
     * @param year
     * @param month
     * @return
     */
    public static List<DaKaRecord> queryRecordForMonth(String year, String month) {
        return daKaRecordDao.queryBuilder().where(DaKaRecordDao.Properties.Year.eq(year),
                DaKaRecordDao.Properties.Month.eq(month)).build().list();
    }

    /**
     * 查询指定年份月份的打卡记录
     *
     * @param calendar
     * @return
     */
    public static List<DaKaRecord> queryRecordForMonth(Calendar calendar) {
        return daKaRecordDao.queryBuilder().where(DaKaRecordDao.Properties.Year.eq(getYear(calendar)),
                DaKaRecordDao.Properties.Month.eq(getMonth(calendar))).build().list();
    }

    /**
     * 查询指定日期的打卡记录
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static List<DaKaRecord> queryRecordForDay(String year, String month, String day) {
        return daKaRecordDao.queryBuilder().where(DaKaRecordDao.Properties.Year.eq(year),
                DaKaRecordDao.Properties.Month.eq(month), DaKaRecordDao.Properties.Day.eq(day)).build().list();
    }

    /**
     * 查询指定日期的打卡记录
     *
     * @param calendar
     * @return
     */
    public static List<DaKaRecord> queryRecordForDay(Calendar calendar) {
        return daKaRecordDao.queryBuilder().where(DaKaRecordDao.Properties.Year.eq(getYear(calendar)),
                DaKaRecordDao.Properties.Month.eq(getMonth(calendar)), DaKaRecordDao.Properties.Day.eq(getDay(calendar))).build().list();
    }


    /**
     * ---------------------------------------------------------------------------------------------
     * 数据库修改操作
     */

    /**
     * 修改指定日期的正常的上班卡记录
     *
     * @param year
     * @param month
     * @param day
     */
    public static void updataRecordForDayStartNormal(String year, String month, String day) {
        List<DaKaRecord> list = queryRecordForDay(year, month, day);
        if (list != null && list.size() != 0) {
            DaKaRecord daKaRecord = list.get(0);
            daKaRecord.setStartTime(new SimpleDateFormat("HH:mm").format(System.currentTimeMillis()));
            daKaRecord.setIsLate("0");
            daKaRecordDao.update(daKaRecord);
        }
    }

    /**
     * 修改指定日期的正常的上班卡记录
     *
     * @param calendar
     */
    public static void updataRecordForDayStartNormal(Calendar calendar) {
        List<DaKaRecord> list = queryRecordForDay(getYear(calendar), getMonth(calendar), getDay(calendar));
        if (list != null && list.size() != 0) {
            DaKaRecord daKaRecord = list.get(0);
            daKaRecord.setStartTime(new SimpleDateFormat("HH:mm").format(System.currentTimeMillis()));
            daKaRecord.setIsLate("0");
            daKaRecordDao.update(daKaRecord);
        }
    }


    /**
     * 修改指定日期的迟到的上班卡记录
     *
     * @param year
     * @param month
     * @param day
     */
    public static void updataRecordForDayStartLate(String year, String month, String day) {
        List<DaKaRecord> list = queryRecordForDay(year, month, day);
        if (list != null && list.size() != 0) {
            DaKaRecord daKaRecord = list.get(0);
            daKaRecord.setStartTime(new SimpleDateFormat("HH:mm").format(System.currentTimeMillis()));
            daKaRecord.setIsLate("1");
            daKaRecordDao.update(daKaRecord);
        }
    }

    /**
     * 修改指定日期的迟到的上班卡记录
     *
     * @param calendar
     */
    public static void updataRecordForDayStartLate(Calendar calendar) {
        List<DaKaRecord> list = queryRecordForDay(getYear(calendar), getMonth(calendar), getDay(calendar));
        if (list != null && list.size() != 0) {
            DaKaRecord daKaRecord = list.get(0);
            daKaRecord.setStartTime(new SimpleDateFormat("HH:mm").format(System.currentTimeMillis()));
            daKaRecord.setIsLate("1");
            daKaRecordDao.update(daKaRecord);
        }
    }

    /**
     * 修改指定日期的正常的下班卡记录
     *
     * @param year
     * @param month
     * @param day
     */
    public static void updataRecordForDayEndNormal(String year, String month, String day) {
        List<DaKaRecord> list = queryRecordForDay(year, month, day);
        if (list != null && list.size() != 0) {
            DaKaRecord daKaRecord = list.get(0);
            daKaRecord.setEndTime(new SimpleDateFormat("HH:mm").format(System.currentTimeMillis()));
            daKaRecord.setIsLeaveEarly("0");
            daKaRecordDao.update(daKaRecord);
        }
    }

    /**
     * 修改指定日期的正常的下班卡记录
     *
     * @param calendar
     */
    public static void updataRecordForDayEndNormal(Calendar calendar) {
        List<DaKaRecord> list = queryRecordForDay(getYear(calendar), getMonth(calendar), getDay(calendar));
        if (list != null && list.size() != 0) {
            DaKaRecord daKaRecord = list.get(0);
            daKaRecord.setEndTime(new SimpleDateFormat("HH:mm").format(System.currentTimeMillis()));
            daKaRecord.setIsLeaveEarly("0");
            daKaRecordDao.update(daKaRecord);
        }
    }

    /**
     * 修改指定日期的早退的下班卡记录
     *
     * @param year
     * @param month
     * @param day
     */
    public static void updataRecordForDayEndLeaveEarly(String year, String month, String day) {
        List<DaKaRecord> list = queryRecordForDay(year, month, day);
        if (list != null && list.size() != 0) {
            DaKaRecord daKaRecord = list.get(0);
            daKaRecord.setEndTime(new SimpleDateFormat("HH:mm").format(System.currentTimeMillis()));
            daKaRecord.setIsLeaveEarly("1");
            daKaRecordDao.update(daKaRecord);
        }
    }

    /**
     * 修改指定日期的早退的下班卡记录
     *
     * @param calendar
     */
    public static void updataRecordForDayEndLeaveEarly(Calendar calendar) {
        List<DaKaRecord> list = queryRecordForDay(getYear(calendar), getMonth(calendar), getDay(calendar));
        if (list != null && list.size() != 0) {
            DaKaRecord daKaRecord = list.get(0);
            daKaRecord.setEndTime(new SimpleDateFormat("HH:mm").format(System.currentTimeMillis()));
            daKaRecord.setIsLeaveEarly("1");
            daKaRecordDao.update(daKaRecord);
        }
    }


    /**
     * ---------------------------------------------------------------------------------------------
     * 数据库插入操作
     */
    /**
     * 插入或修改正常的上班卡记录
     *
     * @param calendar
     */
    public static void insertStartNormal(Calendar calendar) {
        List<DaKaRecord> list = queryRecordForDay(calendar);
        if (list != null && list.size() > 0) {
            updataRecordForDayStartNormal(calendar);
        } else {
            daKaRecordDao.insert(new DaKaRecord(getYear(calendar), getMonth(calendar), getDay(calendar),
                    TimeUtil.getWeek(getYearMonthDay(calendar)), getIsWorkingDay(calendar),
                    new SimpleDateFormat("HH:mm").format(System.currentTimeMillis()), null,
                    "0", "0", null));
        }
    }

    /**
     * 插入或修改迟到的上班卡记录
     *
     * @param calendar
     */
    public static void insertStartLate(Calendar calendar) {
        List<DaKaRecord> list = queryRecordForDay(calendar);
        if (list != null && list.size() > 0) {
            updataRecordForDayStartLate(calendar);
        } else {
            daKaRecordDao.insert(new DaKaRecord(getYear(calendar), getMonth(calendar), getDay(calendar),
                    TimeUtil.getWeek(getYearMonthDay(calendar)), getIsWorkingDay(calendar),
                    new SimpleDateFormat("HH:mm").format(System.currentTimeMillis()), null,
                    "1", "0", null));
        }
    }

    /**
     * 插入或修改正常的下班卡记录
     *
     * @param calendar
     */
    public static void insertEndNormal(Calendar calendar) {
        List<DaKaRecord> list = queryRecordForDay(calendar);
        if (list != null && list.size() > 0) {
            updataRecordForDayEndNormal(calendar);
        } else {
            daKaRecordDao.insert(new DaKaRecord(getYear(calendar), getMonth(calendar), getDay(calendar),
                    TimeUtil.getWeek(getYearMonthDay(calendar)), getIsWorkingDay(calendar),
                    null, new SimpleDateFormat("HH:mm").format(System.currentTimeMillis()),
                    "0", "0", null));
        }
    }

    /**
     * 插入或修改早退的下班卡记录
     *
     * @param calendar
     */
    public static void insertEndLeaveEarly(Calendar calendar) {
        List<DaKaRecord> list = queryRecordForDay(calendar);
        if (list != null && list.size() > 0) {
            updataRecordForDayEndLeaveEarly(calendar);
        } else {
            daKaRecordDao.insert(new DaKaRecord(getYear(calendar), getMonth(calendar), getDay(calendar),
                    TimeUtil.getWeek(getYearMonthDay(calendar)), getIsWorkingDay(calendar),
                    null, new SimpleDateFormat("HH:mm").format(System.currentTimeMillis()),
                    "0", "1", null));
        }
    }


    public static String getIsWorkingDay(Calendar calendar) {
        String baseUrl = "http://tool.bitefu.net/jiari?d=";
        String apiurl = null;
        apiurl = baseUrl + DaKaRecordDaoUtil.getYMD(calendar);
        final String[] isWorkingDay = new String[1];
        HttpUtil.doGetAsyn(apiurl, new HttpUtil.CallBack() {
            @Override
            public void onRequestComplete(String result) {
                isWorkingDay[0] = result;
            }

            @Override
            public void onRequestError(String result) {

            }
        });
        return isWorkingDay[0];
    }

}
