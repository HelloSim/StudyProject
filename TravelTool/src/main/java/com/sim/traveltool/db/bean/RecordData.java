package com.sim.traveltool.db.bean;

import android.content.Context;

import com.haibin.calendarview.Calendar;
import com.sim.baselibrary.callback.SuccessOrFailListener;
import com.sim.baselibrary.utils.LogUtil;
import com.sim.baselibrary.utils.TimeUtil;
import com.sim.traveltool.db.RecordDataDaoUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @Auther Sim
 * @Time 2020/11/26 22:41
 * @Description 新的插入数据库的打卡记录实体
 */
public class RecordData extends BmobObject {

    private String date;//日期 2020-12-31
    private Integer year;//年 2020
    private Integer month;//月 12
    private Integer day;//日 31
    private String week;//周几 星期四
    private String startTime;//上班卡时间 09:00
    private String endTime;//下班卡时间 19:00
    private boolean isLate;//是否迟到 false
    private boolean isLeaveEarly;//是否早退
    private String other;//其他

    public RecordData(String date) {
        this.date = date;
        if (date != null && date.length() > 0) {
            String[] split = date.split("-");
            if (split != null && split.length != 0) {
                this.year = Integer.valueOf(split[0]);
                this.month = Integer.valueOf(split[1]);
                this.day = Integer.valueOf(split[2]);
            }
        }
        this.week = TimeUtil.getWeek(date);
    }

    public RecordData(String date, String startTime, String endTime, String other) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.other = other;
        if (date != null && date.length() > 0) {
            String[] split = date.split("-");
            if (split != null && split.length != 0) {
                this.year = Integer.valueOf(split[0]);
                this.month = Integer.valueOf(split[1]);
                this.day = Integer.valueOf(split[2]);
            }
        }
        this.week = TimeUtil.getWeek(date);
        if (startTime != null && startTime.length() > 0) {
            String[] split = startTime.split(":");
            if (split != null && split.length != 0) {
                int hours = Integer.parseInt(split[0]);
                int minute = Integer.parseInt(split[1]);
                this.isLate = hours > 9 || (hours == 9 && minute > 30);
            }
        }
        if (endTime != null && endTime.length() > 0) {
            String[] split = endTime.split(":");
            if (split != null && split.length != 0) {
                int hours = Integer.parseInt(split[0]);
                int minute = Integer.parseInt(split[1]);
                this.isLeaveEarly = hours < 18 || (hours == 18 && minute < 30);
            }
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isLate() {
        return isLate;
    }

    public void setLate(boolean late) {
        isLate = late;
    }

    public boolean isLeaveEarly() {
        return isLeaveEarly;
    }

    public void setLeaveEarly(boolean leaveEarly) {
        isLeaveEarly = leaveEarly;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public boolean judgeIsLate() {
        return false;
    }

    @Override
    public String toString() {
        return "RecordData{" +
                "date='" + date + '\'' +
                ", year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", day='" + day + '\'' +
                ", week='" + week + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", isLate=" + isLate +
                ", isLeaveEarly=" + isLeaveEarly +
                ", other='" + other + '\'' +
                '}';
    }

    //--------------------------------------------------------------------------------------------------

    public static class Dao {
        /**
         * 插入一条数据
         *
         * @param recordData
         * @param successOrFailListener
         */
        public static void insert(RecordData recordData, SuccessOrFailListener successOrFailListener) {
            recordData.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        successOrFailListener.success(s);
                    } else {
                        successOrFailListener.fail(e.getMessage());
                    }
                }
            });
        }

        /**
         * 批量插入数据
         *
         * @param recordDataList
         * @param successOrFailListener
         */
        public static void insert(List<BmobObject> recordDataList, SuccessOrFailListener successOrFailListener) {
            new BmobBatch().insertBatch(recordDataList).doBatch(new QueryListListener<BatchResult>() {
                @Override
                public void done(List<BatchResult> o, BmobException e) {
                    if (e == null) {
                        for (int i = 0; i < o.size(); i++) {
                            BatchResult result = o.get(i);
                            BmobException ex = result.getError();
                            if (ex == null) {
                                successOrFailListener.success(result);
                            } else {
                                successOrFailListener.fail(ex.getMessage());
                            }
                        }
                    } else {
                        successOrFailListener.fail(e.getMessage());
                    }
                }
            });
        }

        /**
         * 批量插入当月所有天数的空数据
         *
         * @param context
         * @param calendar
         * @param successOrFailListener
         */
        public static void insertForMonthAll(Context context, Calendar calendar, SuccessOrFailListener successOrFailListener) {
            int dayForMonth = TimeUtil.getDaysByYearMonth(Integer.parseInt(RecordDataDaoUtil.getInstance().getYear(context, calendar)),
                    Integer.parseInt(RecordDataDaoUtil.getInstance().getMonth(context, calendar)));
            List<BmobObject> recordDataArrayList = new ArrayList<>();
            for (int i = 1; i <= dayForMonth; i++) {
                RecordData recordData = new RecordData(calendar.getYear() + "-" + calendar.getMonth() + "-" + i);
                recordDataArrayList.add(recordData);
            }
            insert(recordDataArrayList, successOrFailListener);
        }

        /**
         * 删除指定ObjectId的数据
         *
         * @param recordData
         * @param successOrFailListener
         */
        public static void delete(RecordData recordData, SuccessOrFailListener successOrFailListener) {
            recordData.setObjectId(recordData.getObjectId());
            recordData.delete(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        successOrFailListener.success();
                    } else {
                        successOrFailListener.fail(e.getMessage());
                    }
                }
            });
        }

        /**
         * 修改指定ObjectId的数据
         *
         * @param recordData
         * @param successOrFailListener
         */
        public static void update(RecordData recordData, SuccessOrFailListener successOrFailListener) {
            recordData.update(recordData.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        successOrFailListener.success();
                    } else {
                        successOrFailListener.fail(e.getMessage());
                    }
                }
            });
        }

        /**
         * 查询指定ObjectId的数据
         *
         * @param recordData
         * @param successOrFailListener
         */
        public static void query(RecordData recordData, SuccessOrFailListener successOrFailListener) {
            BmobQuery<RecordData> bmobQuery = new BmobQuery<RecordData>();
            bmobQuery.getObject(recordData.getObjectId(), new QueryListener<RecordData>() {
                @Override
                public void done(RecordData object, BmobException e) {
                    if (e == null) {
                        successOrFailListener.success(object);
                    } else {
                        successOrFailListener.fail(e.getMessage());
                    }
                }
            });
        }

        /**
         * 复合查询 - 查询指定年月数据
         *
         * @param year
         * @param month
         * @param successOrFailListener
         */
        public static void query(Integer year, Integer month, SuccessOrFailListener successOrFailListener) {
            BmobQuery<RecordData> bmobQuery = new BmobQuery<RecordData>();
            bmobQuery.addWhereEqualTo("year", year);
//            bmobQuery.addWhereEqualTo("month", month);
//            bmobQuery.addWhereEqualTo("day", queryDay);
            bmobQuery.findObjects(new FindListener<RecordData>() {
                @Override
                public void done(List<RecordData> list, BmobException e) {
                    if (e == null) {
                        successOrFailListener.success(list);
                    } else {
                        LogUtil.d(RecordData.class, "数据库查询失败:" + e.getMessage());
                        successOrFailListener.fail(e.getMessage());
                    }
                }
            });
        }

        /**
         * 复合查询 - 查询指定年月日数据
         *
         * @param year
         * @param month
         * @param day
         * @param successOrFailListener
         */
        public static void query(Integer year, Integer month, Integer day, SuccessOrFailListener successOrFailListener) {
            BmobQuery<RecordData> bmobQuery = new BmobQuery<RecordData>();
            bmobQuery.addWhereEqualTo("year", year);
            bmobQuery.addWhereEqualTo("month", month);
            bmobQuery.addWhereEqualTo("day", day);
            bmobQuery.findObjects(new FindListener<RecordData>() {
                @Override
                public void done(List<RecordData> list, BmobException e) {
                    if (e == null) {
                        successOrFailListener.success(list);
                    } else {
                        successOrFailListener.fail(e.getMessage());
                    }
                }
            });
        }

    }

}
