package com.sim.sqlitelibrary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.haibin.calendarview.Calendar;
import com.sim.baselibrary.utils.HttpUtil;
import com.sim.baselibrary.utils.TimeUtil;
import com.sim.sqlitelibrary.bean.DaKaRecord;
import com.sim.sqlitelibrary.greendao.db.DaKaRecordDao;
import com.sim.sqlitelibrary.greendao.db.DaoMaster;
import com.sim.sqlitelibrary.greendao.db.DaoSession;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @Auther Sim
 * @Time 2020/10/25 1:37
 * @Description 数据库打卡表操作工具类。使用前必先init
 */
public class DaKaRecordDaoUtil {

    //类加载时就初始化
    private static final DaKaRecordDaoUtil instance = new DaKaRecordDaoUtil();

    private DaKaRecordDaoUtil() {
    }

    public static DaKaRecordDaoUtil getInstance() {
        return instance;
    }

    public DaKaRecordDao daKaRecordDao;

    public void init(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, "DaKaRecord.db");
        SQLiteDatabase sqLiteDatabase = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
        DaoSession daoSession = daoMaster.newSession();
        daKaRecordDao = daoSession.getDaKaRecordDao();
    }

    /**
     * 获取年月日
     *
     * @param calendar
     * @return
     */
    public String getYMD(Context context, Calendar calendar) {
        if (daKaRecordDao == null) init(context);
        return String.valueOf(calendar.getYear()) + calendar.getMonth() + calendar.getDay();
    }

    /**
     * 获取年月日
     *
     * @param calendar
     * @return
     */
    public String getYearMonthDay(Context context, Calendar calendar) {
        if (daKaRecordDao == null) init(context);
        return calendar.getYear() + "-" + calendar.getMonth() + "-" + calendar.getDay();
    }

    /**
     * 获取年月
     *
     * @param calendar
     * @return
     */
    public String getYearMonth(Context context, Calendar calendar) {
        if (daKaRecordDao == null) init(context);
        return calendar.getYear() + "-" + calendar.getMonth();
    }

    /**
     * 获取年
     *
     * @param calendar
     * @return
     */
    public String getYear(Context context, Calendar calendar) {
        if (daKaRecordDao == null) init(context);
        return String.valueOf(calendar.getYear());
    }

    /**
     * 获取月
     *
     * @param calendar
     * @return
     */
    public String getMonth(Context context, Calendar calendar) {
        if (daKaRecordDao == null) init(context);
        return String.valueOf(calendar.getMonth());
    }

    /**
     * 获取日
     *
     * @param calendar
     * @return
     */
    public String getDay(Context context, Calendar calendar) {
        if (daKaRecordDao == null) init(context);
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
    public List<DaKaRecord> queryRecordForMonth(Context context, String year, String month) {
        if (daKaRecordDao == null) init(context);
        return daKaRecordDao.queryBuilder().where(DaKaRecordDao.Properties.Year.eq(year),
                DaKaRecordDao.Properties.Month.eq(month)).build().list();
    }

    /**
     * 查询指定年份月份的打卡记录
     *
     * @param calendar
     * @return
     */
    public List<DaKaRecord> queryRecordForMonth(Context context, Calendar calendar) {
        if (daKaRecordDao == null) init(context);
        return daKaRecordDao.queryBuilder().where(DaKaRecordDao.Properties.Year.eq(getYear(context, calendar)),
                DaKaRecordDao.Properties.Month.eq(getMonth(context, calendar))).build().list();
    }

    /**
     * 查询指定日期的打卡记录
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public List<DaKaRecord> queryRecordForDay(Context context, String year, String month, String day) {
        if (daKaRecordDao == null) init(context);
        return daKaRecordDao.queryBuilder().where(DaKaRecordDao.Properties.Year.eq(year),
                DaKaRecordDao.Properties.Month.eq(month), DaKaRecordDao.Properties.Day.eq(day)).build().list();
    }

    /**
     * 查询指定日期的打卡记录
     *
     * @param calendar
     * @return
     */
    public List<DaKaRecord> queryRecordForDay(Context context, Calendar calendar) {
        if (daKaRecordDao == null) init(context);
        return daKaRecordDao.queryBuilder().where(DaKaRecordDao.Properties.Year.eq(getYear(context, calendar)),
                DaKaRecordDao.Properties.Month.eq(getMonth(context, calendar)), DaKaRecordDao.Properties.Day.eq(getDay(context, calendar))).build().list();
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
    public void updataRecordForDayStartNormal(Context context, String year, String month, String day) {
        if (daKaRecordDao == null) init(context);
        List<DaKaRecord> list = queryRecordForDay(context, year, month, day);
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
    public void updataRecordForDayStartNormal(Context context, Calendar calendar) {
        if (daKaRecordDao == null) init(context);
        List<DaKaRecord> list = queryRecordForDay(context, getYear(context, calendar), getMonth(context, calendar), getDay(context, calendar));
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
    public void updataRecordForDayStartLate(Context context, String year, String month, String day) {
        if (daKaRecordDao == null) init(context);
        List<DaKaRecord> list = queryRecordForDay(context, year, month, day);
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
    public void updataRecordForDayStartLate(Context context, Calendar calendar) {
        if (daKaRecordDao == null) init(context);
        List<DaKaRecord> list = queryRecordForDay(context, getYear(context, calendar), getMonth(context, calendar), getDay(context, calendar));
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
    public void updataRecordForDayEndNormal(Context context, String year, String month, String day) {
        if (daKaRecordDao == null) init(context);
        List<DaKaRecord> list = queryRecordForDay(context, year, month, day);
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
    public void updataRecordForDayEndNormal(Context context, Calendar calendar) {
        if (daKaRecordDao == null) init(context);
        List<DaKaRecord> list = queryRecordForDay(context, getYear(context, calendar), getMonth(context, calendar), getDay(context, calendar));
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
    public void updataRecordForDayEndLeaveEarly(Context context, String year, String month, String day) {
        if (daKaRecordDao == null) init(context);
        List<DaKaRecord> list = queryRecordForDay(context, year, month, day);
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
    public void updataRecordForDayEndLeaveEarly(Context context, Calendar calendar) {
        if (daKaRecordDao == null) init(context);
        List<DaKaRecord> list = queryRecordForDay(context, getYear(context, calendar), getMonth(context, calendar), getDay(context, calendar));
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
    public void insertStartNormal(Context context, Calendar calendar) {
        if (daKaRecordDao == null) init(context);
        List<DaKaRecord> list = queryRecordForDay(context, calendar);
        if (list != null && list.size() > 0) {
            updataRecordForDayStartNormal(context, calendar);
        } else {
            daKaRecordDao.insert(new DaKaRecord(getYear(context, calendar), getMonth(context, calendar), getDay(context, calendar),
                    TimeUtil.getWeek(getYearMonthDay(context, calendar)), getIsWorkingDay(context, calendar),
                    new SimpleDateFormat("HH:mm").format(System.currentTimeMillis()), null,
                    "0", "0", null));
        }
    }

    /**
     * 插入或修改迟到的上班卡记录
     *
     * @param calendar
     */
    public void insertStartLate(Context context, Calendar calendar) {
        if (daKaRecordDao == null) init(context);
        List<DaKaRecord> list = queryRecordForDay(context, calendar);
        if (list != null && list.size() > 0) {
            updataRecordForDayStartLate(context, calendar);
        } else {
            daKaRecordDao.insert(new DaKaRecord(getYear(context, calendar), getMonth(context, calendar), getDay(context, calendar),
                    TimeUtil.getWeek(getYearMonthDay(context, calendar)), getIsWorkingDay(context, calendar),
                    new SimpleDateFormat("HH:mm").format(System.currentTimeMillis()), null,
                    "1", "0", null));
        }
    }

    /**
     * 插入或修改正常的下班卡记录
     *
     * @param calendar
     */
    public void insertEndNormal(Context context, Calendar calendar) {
        if (daKaRecordDao == null) init(context);
        List<DaKaRecord> list = queryRecordForDay(context, calendar);
        if (list != null && list.size() > 0) {
            updataRecordForDayEndNormal(context, calendar);
        } else {
            daKaRecordDao.insert(new DaKaRecord(getYear(context, calendar), getMonth(context, calendar), getDay(context, calendar),
                    TimeUtil.getWeek(getYearMonthDay(context, calendar)), getIsWorkingDay(context, calendar),
                    null, new SimpleDateFormat("HH:mm").format(System.currentTimeMillis()),
                    "0", "0", null));
        }
    }

    /**
     * 插入或修改早退的下班卡记录
     *
     * @param calendar
     */
    public void insertEndLeaveEarly(Context context, Calendar calendar) {
        if (daKaRecordDao == null) init(context);
        List<DaKaRecord> list = queryRecordForDay(context, calendar);
        if (list != null && list.size() > 0) {
            updataRecordForDayEndLeaveEarly(context, calendar);
        } else {
            daKaRecordDao.insert(new DaKaRecord(getYear(context, calendar), getMonth(context, calendar), getDay(context, calendar),
                    TimeUtil.getWeek(getYearMonthDay(context, calendar)), getIsWorkingDay(context, calendar),
                    null, new SimpleDateFormat("HH:mm").format(System.currentTimeMillis()),
                    "0", "1", null));
        }
    }


    public String getIsWorkingDay(Context context, Calendar calendar) {
        if (daKaRecordDao == null) init(context);
        String baseUrl = "http://tool.bitefu.net/jiari?d=";
        String apiurl = null;
        apiurl = baseUrl + getYMD(context, calendar);
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
