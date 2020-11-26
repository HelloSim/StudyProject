package com.sim.traveltool.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.haibin.calendarview.Calendar;
import com.sim.sqlitelibrary.greendao.db.DaoMaster;
import com.sim.sqlitelibrary.greendao.db.DaoSession;
import com.sim.sqlitelibrary.greendao.db.RecordDataBeanDao;

/**
 * @Auther Sim
 * @Time 2020/11/26 22:44
 * @Description
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
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, "DaKaRecord.db");
        SQLiteDatabase sqLiteDatabase = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
        DaoSession daoSession = daoMaster.newSession();
        recordDataBeanDao = daoSession.getRecordDataBeanDao();
    }

    public int getYear(Calendar calendar){
        return calendar.getYear();
    }

    public int getMonth(Calendar calendar){
        return calendar.getMonth();
    }

    public int getDay(Calendar calendar){
        return calendar.getDay();
    }

}
