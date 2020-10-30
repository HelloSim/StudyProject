package com.sim.record;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.sim.record.greendao.db.DaKaRecordDao;
import com.sim.record.greendao.db.DaoMaster;
import com.sim.record.greendao.db.DaoSession;

/**
 * @Auther Sim
 * @Time 2020/10/20 15:26
 * @Description
 */
public class Application extends android.app.Application {

    public static Context mContext;

    public static DaKaRecordDao daKaRecordDao;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "DaKaRecord.db");
        SQLiteDatabase sqLiteDatabase = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
        DaoSession daoSession = daoMaster.newSession();
        daKaRecordDao = daoSession.getDaKaRecordDao();
    }

}
