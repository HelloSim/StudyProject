package com.sim.traveltool.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.haibin.calendarview.Calendar;
import com.sim.baselibrary.utils.TimeUtil;
import com.sim.sqlitelibrary.bean.RecordDataBean;
import com.sim.sqlitelibrary.greendao.db.RecordDataBeanDao;
import com.sim.sqlitelibrary.greendao.db.DaoMaster;
import com.sim.sqlitelibrary.greendao.db.DaoSession;
import com.sim.traveltool.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @Auther Sim
 * @Time 2020/11/26 22:44
 * @Description 新的数据库打卡表操作工具类。使用前必先init
 */
public class RecoedDataDaoUtil {

    //类加载时就初始化
    private static final RecoedDataDaoUtil instance = new RecoedDataDaoUtil();

    private RecoedDataDaoUtil() {
    }

    public static RecoedDataDaoUtil getInstance() {
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
     * 数据库查询操作
     */

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
     *  ---------------------------------------------------------------------------------------------
     * 数据库修改操作
     * /

     /**
     *
     */
     public void updataRecordOtherForDay(Context context, Calendar calendar,String other){
         if (recordDataBeanDao == null) init(context);
         List<RecordDataBean> list = queryRecordForDay(context, calendar);
         if (list != null && list.size() != 0) {
             RecordDataBean recordDataBean = list.get(0);
             recordDataBean.setOther(other);
             recordDataBeanDao.update(recordDataBean);
         }
     }

    /**
     * 修改指定日期的正常的上班卡记录
     *
     * @param calendar
     */
    public void updataRecordForDayStartNormal(Context context, Calendar calendar) {
        if (recordDataBeanDao == null) init(context);
        List<RecordDataBean> list = queryRecordForDay(context, calendar);
        if (list != null && list.size() != 0) {
            RecordDataBean recordDataBean = list.get(0);
            recordDataBean.setStartTime(new SimpleDateFormat("HH:mm").format(System.currentTimeMillis()));
            recordDataBean.setIsLate("0");
            recordDataBeanDao.update(recordDataBean);
        }
    }

    /**
     * 修改指定日期的迟到的上班卡记录
     *
     * @param calendar
     */
    public void updataRecordForDayStartLate(Context context, Calendar calendar) {
        if (recordDataBeanDao == null) init(context);
        List<RecordDataBean> list = queryRecordForDay(context, calendar);
        if (list != null && list.size() != 0) {
            RecordDataBean recordDataBean = list.get(0);
            recordDataBean.setStartTime(new SimpleDateFormat("HH:mm").format(System.currentTimeMillis()));
            recordDataBean.setIsLate("1");
            recordDataBeanDao.update(recordDataBean);
        }
    }

    /**
     * 修改指定日期的正常的下班卡记录
     *
     * @param calendar
     */
    public void updataRecordForDayEndNormal(Context context, Calendar calendar) {
        if (recordDataBeanDao == null) init(context);
        List<RecordDataBean> list = queryRecordForDay(context, calendar);
        if (list != null && list.size() != 0) {
            RecordDataBean recordDataBean = list.get(0);
            recordDataBean.setEndTime(new SimpleDateFormat("HH:mm").format(System.currentTimeMillis()));
            recordDataBean.setIsLeaveEarly("0");
            recordDataBeanDao.update(recordDataBean);
        }
    }

    /**
     * 修改指定日期的早退的下班卡记录
     *
     * @param calendar
     */
    public void updataRecordForDayEndLeaveEarly(Context context, Calendar calendar) {
        if (recordDataBeanDao == null) init(context);
        List<RecordDataBean> list = queryRecordForDay(context, calendar);
        if (list != null && list.size() != 0) {
            RecordDataBean recordDataBean = list.get(0);
            recordDataBean.setEndTime(new SimpleDateFormat("HH:mm").format(System.currentTimeMillis()));
            recordDataBean.setIsLeaveEarly("1");
            recordDataBeanDao.update(recordDataBean);
        }
    }


    /**
     * ---------------------------------------------------------------------------------------------
     * 数据库插入操作
     */

    /**
     * 修改或插入正常的上班卡记录
     *
     * @param calendar
     */
    public void insertStartNormal(Context context, Calendar calendar) {
        if (recordDataBeanDao == null) init(context);
        List<RecordDataBean> list = queryRecordForDay(context, calendar);
        if (list != null && list.size() > 0) {
            updataRecordForDayStartNormal(context, calendar);
        } else {
            recordDataBeanDao.insert(new RecordDataBean(getYear(context, calendar), getMonth(context, calendar),
                    getDay(context, calendar), TimeUtil.getWeek(getYearMonthDay(context, calendar)),
                    new SimpleDateFormat("HH:mm").format(System.currentTimeMillis()), null,
                    "0", "0", null));
        }
    }

    /**
     * 修改或插入迟到的上班卡记录
     *
     * @param calendar
     */
    public void insertStartLate(Context context, Calendar calendar) {
        if (recordDataBeanDao == null) init(context);
        List<RecordDataBean> list = queryRecordForDay(context, calendar);
        if (list != null && list.size() > 0) {
            updataRecordForDayStartLate(context, calendar);
        } else {
            recordDataBeanDao.insert(new RecordDataBean(getYear(context, calendar), getMonth(context, calendar),
                    getDay(context, calendar), TimeUtil.getWeek(getYearMonthDay(context, calendar)),
                    new SimpleDateFormat("HH:mm").format(System.currentTimeMillis()), null,
                    "1", "0", null));
        }
    }

    /**
     * 修改或插入正常的下班卡记录
     *
     * @param calendar
     */
    public void insertEndNormal(Context context, Calendar calendar) {
        if (recordDataBeanDao == null) init(context);
        List<RecordDataBean> list = queryRecordForDay(context, calendar);
        if (list != null && list.size() > 0) {
            updataRecordForDayEndNormal(context, calendar);
        } else {
            recordDataBeanDao.insert(new RecordDataBean(getYear(context, calendar), getMonth(context, calendar),
                    getDay(context, calendar), TimeUtil.getWeek(getYearMonthDay(context, calendar)),
                    null, new SimpleDateFormat("HH:mm").format(System.currentTimeMillis()),
                    "0", "0", null));
        }
    }

    /**
     * 修改或插入早退的下班卡记录
     *
     * @param calendar
     */
    public void insertEndLeaveEarly(Context context, Calendar calendar) {
        if (recordDataBeanDao == null) init(context);
        List<RecordDataBean> list = queryRecordForDay(context, calendar);
        if (list != null && list.size() > 0) {
            updataRecordForDayEndLeaveEarly(context, calendar);
        } else {
            recordDataBeanDao.insert(new RecordDataBean(getYear(context, calendar), getMonth(context, calendar),
                    getDay(context, calendar), TimeUtil.getWeek(getYearMonthDay(context, calendar)),
                    null, new SimpleDateFormat("HH:mm").format(System.currentTimeMillis()),
                    "0", "1", null));
        }
    }

    /**
     * 插入当月所有的天数的数据库数据
     *
     * @param calendar
     */
    public void insertDataForMonth(Context context, Calendar calendar) {
        if (recordDataBeanDao == null) init(context);
        List<RecordDataBean> list = queryRecordForMonth(context, calendar);
        if (list == null || list.size() <= 0) {
            int dayForMonth = TimeUtil.getDaysByYearMonth(Integer.parseInt(RecoedDataDaoUtil.getInstance().getYear(context, calendar)),
                    Integer.parseInt(RecoedDataDaoUtil.getInstance().getMonth(context, calendar)));
            for (int i = 1; i <= dayForMonth; i++) {
                recordDataBeanDao.insert(new RecordDataBean(getYear(context, calendar), getMonth(context, calendar),
                        String.valueOf(i), TimeUtil.getWeek(getYearMonth(context, calendar) + "-" + i),
                        context.getString(R.string.no), context.getString(R.string.no), "0", "0", null));
            }
        }
    }

}
