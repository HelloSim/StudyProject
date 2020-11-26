package com.sim.sqlitelibrary.greendao.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.sim.sqlitelibrary.bean.RecordDataBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "RECORD_DATA_BEAN".
*/
public class RecordDataBeanDao extends AbstractDao<RecordDataBean, Long> {

    public static final String TABLENAME = "RECORD_DATA_BEAN";

    /**
     * Properties of entity RecordDataBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Year = new Property(1, String.class, "year", false, "YEAR");
        public final static Property Month = new Property(2, String.class, "month", false, "MONTH");
        public final static Property Day = new Property(3, String.class, "day", false, "DAY");
        public final static Property Week = new Property(4, String.class, "week", false, "WEEK");
        public final static Property IsWorkingDay = new Property(5, String.class, "isWorkingDay", false, "IS_WORKING_DAY");
        public final static Property StartTime = new Property(6, String.class, "startTime", false, "START_TIME");
        public final static Property EndTime = new Property(7, String.class, "endTime", false, "END_TIME");
        public final static Property IsLate = new Property(8, String.class, "isLate", false, "IS_LATE");
        public final static Property IsLeaveEarly = new Property(9, String.class, "isLeaveEarly", false, "IS_LEAVE_EARLY");
        public final static Property Other = new Property(10, String.class, "other", false, "OTHER");
    }


    public RecordDataBeanDao(DaoConfig config) {
        super(config);
    }
    
    public RecordDataBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"RECORD_DATA_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"YEAR\" TEXT," + // 1: year
                "\"MONTH\" TEXT," + // 2: month
                "\"DAY\" TEXT," + // 3: day
                "\"WEEK\" TEXT," + // 4: week
                "\"IS_WORKING_DAY\" TEXT," + // 5: isWorkingDay
                "\"START_TIME\" TEXT," + // 6: startTime
                "\"END_TIME\" TEXT," + // 7: endTime
                "\"IS_LATE\" TEXT," + // 8: isLate
                "\"IS_LEAVE_EARLY\" TEXT," + // 9: isLeaveEarly
                "\"OTHER\" TEXT);"); // 10: other
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"RECORD_DATA_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, RecordDataBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String year = entity.getYear();
        if (year != null) {
            stmt.bindString(2, year);
        }
 
        String month = entity.getMonth();
        if (month != null) {
            stmt.bindString(3, month);
        }
 
        String day = entity.getDay();
        if (day != null) {
            stmt.bindString(4, day);
        }
 
        String week = entity.getWeek();
        if (week != null) {
            stmt.bindString(5, week);
        }
 
        String isWorkingDay = entity.getIsWorkingDay();
        if (isWorkingDay != null) {
            stmt.bindString(6, isWorkingDay);
        }
 
        String startTime = entity.getStartTime();
        if (startTime != null) {
            stmt.bindString(7, startTime);
        }
 
        String endTime = entity.getEndTime();
        if (endTime != null) {
            stmt.bindString(8, endTime);
        }
 
        String isLate = entity.getIsLate();
        if (isLate != null) {
            stmt.bindString(9, isLate);
        }
 
        String isLeaveEarly = entity.getIsLeaveEarly();
        if (isLeaveEarly != null) {
            stmt.bindString(10, isLeaveEarly);
        }
 
        String other = entity.getOther();
        if (other != null) {
            stmt.bindString(11, other);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, RecordDataBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String year = entity.getYear();
        if (year != null) {
            stmt.bindString(2, year);
        }
 
        String month = entity.getMonth();
        if (month != null) {
            stmt.bindString(3, month);
        }
 
        String day = entity.getDay();
        if (day != null) {
            stmt.bindString(4, day);
        }
 
        String week = entity.getWeek();
        if (week != null) {
            stmt.bindString(5, week);
        }
 
        String isWorkingDay = entity.getIsWorkingDay();
        if (isWorkingDay != null) {
            stmt.bindString(6, isWorkingDay);
        }
 
        String startTime = entity.getStartTime();
        if (startTime != null) {
            stmt.bindString(7, startTime);
        }
 
        String endTime = entity.getEndTime();
        if (endTime != null) {
            stmt.bindString(8, endTime);
        }
 
        String isLate = entity.getIsLate();
        if (isLate != null) {
            stmt.bindString(9, isLate);
        }
 
        String isLeaveEarly = entity.getIsLeaveEarly();
        if (isLeaveEarly != null) {
            stmt.bindString(10, isLeaveEarly);
        }
 
        String other = entity.getOther();
        if (other != null) {
            stmt.bindString(11, other);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public RecordDataBean readEntity(Cursor cursor, int offset) {
        RecordDataBean entity = new RecordDataBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // year
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // month
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // day
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // week
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // isWorkingDay
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // startTime
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // endTime
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // isLate
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // isLeaveEarly
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10) // other
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, RecordDataBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setYear(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setMonth(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setDay(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setWeek(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setIsWorkingDay(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setStartTime(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setEndTime(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setIsLate(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setIsLeaveEarly(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setOther(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(RecordDataBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(RecordDataBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(RecordDataBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
