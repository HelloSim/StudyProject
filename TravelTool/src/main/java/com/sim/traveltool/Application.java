package com.sim.traveltool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.sim.traveltool.bean.greendao.db.DaKaRecordDao;
import com.sim.traveltool.bean.greendao.db.DaoMaster;
import com.sim.traveltool.bean.greendao.db.DaoSession;

/**
 * Created by Sim on 2020/4/24
 */
public class Application extends android.app.Application {

    public static Context context;

    public static DaKaRecordDao daKaRecordDao;

    @SuppressLint("ApplySharedPref")
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "DaKaRecord.db");
        SQLiteDatabase sqLiteDatabase = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
        DaoSession daoSession = daoMaster.newSession();
        daKaRecordDao = daoSession.getDaKaRecordDao();

        com.sim.traveltool.internet.APIFactory.getInstance().init(this);
        com.sim.traveltool.businternet.APIFactory.getInstance().init(this);
    }

}
