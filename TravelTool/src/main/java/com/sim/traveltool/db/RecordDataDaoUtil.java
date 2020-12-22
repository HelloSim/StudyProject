package com.sim.traveltool.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.haibin.calendarview.Calendar;
import com.sim.baselibrary.utils.TimeUtil;
import com.sim.sqlitelibrary.bean.RecordDataBean;
import com.sim.sqlitelibrary.greendao.db.DaoMaster;
import com.sim.sqlitelibrary.greendao.db.DaoSession;
import com.sim.sqlitelibrary.greendao.db.RecordDataBeanDao;
import com.sim.traveltool.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @Auther Sim
 * @Time 2020/11/26 22:44
 * @Description 新的数据库打卡表操作工具类。使用前必先init
 */
public class RecordDataDaoUtil {

    //类加载时就初始化
    private static final RecordDataDaoUtil instance = new RecordDataDaoUtil();

    private RecordDataDaoUtil() {
    }

    public static RecordDataDaoUtil getInstance() {
        return instance;
    }

    private RecordDataBeanDao recordDataBeanDao;

    public void init(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, "RecordDataBean.db");
        SQLiteDatabase sqLiteDatabase = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
        DaoSession daoSession = daoMaster.newSession();
        recordDataBeanDao = daoSession.getRecordDataBeanDao();
    }

    /**
     * 获取年月日
     *
     * @param calendar
     * @return
     */
    public String getYMD(Context context, Calendar calendar) {
        if (recordDataBeanDao == null) init(context);
        return String.valueOf(calendar.getYear()) + calendar.getMonth() + calendar.getDay();
    }

    /**
     * 获取年月日
     *
     * @param calendar
     * @return
     */
    public String getYearMonthDay(Context context, Calendar calendar) {
        if (recordDataBeanDao == null) init(context);
        return calendar.getYear() + "-" + calendar.getMonth() + "-" + calendar.getDay();
    }

    /**
     * 获取年月
     *
     * @param calendar
     * @return
     */
    public String getYearMonth(Context context, Calendar calendar) {
        if (recordDataBeanDao == null) init(context);
        return calendar.getYear() + "-" + calendar.getMonth();
    }

    /**
     * 获取年
     *
     * @param calendar
     * @return
     */
    public String getYear(Context context, Calendar calendar) {
        if (recordDataBeanDao == null) init(context);
        return String.valueOf(calendar.getYear());
    }

    /**
     * 获取月
     *
     * @param calendar
     * @return
     */
    public String getMonth(Context context, Calendar calendar) {
        if (recordDataBeanDao == null) init(context);
        return String.valueOf(calendar.getMonth());
    }

    /**
     * 获取日
     *
     * @param calendar
     * @return
     */
    public String getDay(Context context, Calendar calendar) {
        if (recordDataBeanDao == null) init(context);
        return String.valueOf(calendar.getDay());
    }


    /**
     * ---------------------------------------------------------------------------------------------
     * 数据库操作
     */

    /**
     * 插入当月所有的天数的数据库数据
     *
     * @param calendar
     */
    public void insertDataForMonth(Context context, Calendar calendar) {
        if (recordDataBeanDao == null) init(context);
        List<RecordDataBean> list = queryRecordForMonth(context, calendar);
        if (list == null || list.size() <= 0) {
            int dayForMonth = TimeUtil.getDaysByYearMonth(Integer.parseInt(RecordDataDaoUtil.getInstance().getYear(context, calendar)),
                    Integer.parseInt(RecordDataDaoUtil.getInstance().getMonth(context, calendar)));
            for (int i = 1; i <= dayForMonth; i++) {
                recordDataBeanDao.insert(new RecordDataBean(getYear(context, calendar), getMonth(context, calendar),
                        String.valueOf(i), TimeUtil.getWeek(getYearMonth(context, calendar) + "-" + i),
                        context.getString(R.string.record_no), context.getString(R.string.record_no), false, false, null));
            }
        }
    }

    /**
     * 查询指定年份月份的打卡记录
     *
     * @param calendar
     * @return
     */
    public List<RecordDataBean> queryRecordForMonth(Context context, Calendar calendar) {
        if (recordDataBeanDao == null) init(context);
        return recordDataBeanDao.queryBuilder().where(
                RecordDataBeanDao.Properties.Year.eq(getYear(context, calendar)),
                RecordDataBeanDao.Properties.Month.eq(getMonth(context, calendar)))
                .build().list();
    }

    /**
     * 查询指定日期的打卡记录
     *
     * @param calendar
     * @return
     */
    public List<RecordDataBean> queryRecordForDay(Context context, Calendar calendar) {
        if (recordDataBeanDao == null) init(context);
        return recordDataBeanDao.queryBuilder().where(
                RecordDataBeanDao.Properties.Year.eq(getYear(context, calendar)),
                RecordDataBeanDao.Properties.Month.eq(getMonth(context, calendar)),
                RecordDataBeanDao.Properties.Day.eq(getDay(context, calendar)))
                .build().list();
    }

    /**
     * 修改指定日期的正常的上班卡记录
     *
     * @param context
     * @param calendar
     * @return 返回是否迟到
     */
    public boolean updataStartTime(Context context, Calendar calendar) {
        boolean isLate = TimeUtil.getHour() > 9 || (TimeUtil.getHour() == 9 && TimeUtil.getMinute() > 30);
        if (recordDataBeanDao == null) init(context);
        if (queryRecordForMonth(context, calendar) == null || queryRecordForMonth(context, calendar).size() == 0) {
            insertDataForMonth(context, calendar);
        }
        List<RecordDataBean> list = queryRecordForDay(context, calendar);
        if (list != null && list.size() != 0) {
            RecordDataBean recordDataBean = list.get(0);
            recordDataBean.setStartTime(new SimpleDateFormat("HH:mm").format(System.currentTimeMillis()));
            recordDataBean.setIsLate(isLate);
            recordDataBeanDao.update(recordDataBean);
        }
        return isLate;
    }

    /**
     * 修改指定日期的正常的下班卡记录
     *
     * @param context
     * @param calendar
     * @return 返回是否早退
     */
    public boolean updataEndTime(Context context, Calendar calendar) {
        boolean isLeaveEarly = TimeUtil.getHour() < 18 || (TimeUtil.getHour() == 18 && TimeUtil.getMinute() < 30);
        if (recordDataBeanDao == null) init(context);
        List<RecordDataBean> list = queryRecordForDay(context, calendar);
        if (list != null && list.size() != 0) {
            RecordDataBean recordDataBean = list.get(0);
            recordDataBean.setEndTime(new SimpleDateFormat("HH:mm").format(System.currentTimeMillis()));
            recordDataBean.setIsLeaveEarly(isLeaveEarly);
            recordDataBeanDao.update(recordDataBean);
        }
        return isLeaveEarly;
    }

    /**
     * 修改工作日备忘
     *
     * @param context
     * @param calendar
     * @param other
     */
    public void updataRecordOther(Context context, Calendar calendar, String other) {
        if (recordDataBeanDao == null) init(context);
        if (queryRecordForMonth(context, calendar) == null || queryRecordForMonth(context, calendar).size() == 0) {
            insertDataForMonth(context, calendar);
        }
        List<RecordDataBean> list = queryRecordForDay(context, calendar);
        if (list != null && list.size() != 0) {
            RecordDataBean recordDataBean = list.get(0);
            recordDataBean.setOther(other);
            recordDataBeanDao.update(recordDataBean);
        }
    }

}
