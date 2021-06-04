package com.sim.mine.bean;

import android.util.Log;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;
import com.sim.mine.utils.CallBack;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 新的插入数据库的打卡记录实体
 */
@SmartTable(name = "打卡详情")
public class RecordBean extends BmobObject {

    private static final String TAG = "【【【Sim_" + RecordBean.class.getSimpleName() + "】】】";

    // 仅在客户端使用，不希望被gson序列化提交到后端云，用transient修饰

    private BmobUser user;//打卡人
    @SmartColumn(id = 1, name = "日期", fixed = true)
    private String date;//日期 2020-12-31
    private String yearAndMonth;//年月
    @SmartColumn(id = 2, name = "星期")
    private String week;//周几 星期四
    @SmartColumn(id = 3, name = "上班时间")
    private String startTime;//上班卡时间 09:00
    @SmartColumn(id = 4, name = "下班时间")
    private String endTime;//下班卡时间 19:00
    private boolean isLate;//是否迟到 false
    private boolean isLeaveEarly;//是否早退
    @SmartColumn(id = 5, name = "备忘")
    private String other;//其他

    public BmobUser getUser() {
        return user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getYearAndMonth() {
        return yearAndMonth;
    }

    public void setYearAndMonth(String yearAndMonth) {
        this.yearAndMonth = yearAndMonth;
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

    @Override
    public String toString() {
        return "RecordData{" +
                "username='" + user + '\'' +
                ", date='" + date + '\'' +
                ", week='" + week + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", isLate=" + isLate +
                ", isLeaveEarly=" + isLeaveEarly +
                ", other='" + other + '\'' +
                '}';
    }

    public RecordBean(String date) {
        this.user = BmobUser.getCurrentUser(BmobUser.class);
        this.date = date;
        this.week = getWeek(date);
    }

    public RecordBean(String date, String startTime, String endTime, String other) {
        this.user = BmobUser.getCurrentUser(BmobUser.class);
        this.date = date;
        this.yearAndMonth = getYM(date);
        this.week = getWeek(date);
        this.startTime = startTime;
        this.endTime = endTime;
        if (startTime != null && startTime.length() > 0) {
            this.isLate = isLate(startTime);
        }
        if (endTime != null && endTime.length() > 0) {
            this.isLeaveEarly = isLeaveEarly(endTime);
        }
        this.other = other;
    }


    /**
     * 查询指定用户和年月日数据
     *
     * @param date
     * @param callBack
     */
    public static void queryForDay(String date, CallBack callBack) {
        BmobQuery<RecordBean> bmobQuery = new BmobQuery<RecordBean>();
        bmobQuery.addWhereEqualTo("user", BmobUser.getCurrentUser(BmobUser.class));
        bmobQuery.addWhereEqualTo("date", date);
        bmobQuery.findObjects(new FindListener<RecordBean>() {
            @Override
            public void done(List<RecordBean> list, BmobException e) {
                if (e == null) {
                    callBack.success(list);
                } else {
                    callBack.fail(e.getMessage());
                    Log.e(TAG, "done: ");
                    Log.e(TAG, "查询指定用户和年月日数据error：" + e.getMessage());
                }
            }
        });
    }

    /**
     * 查询指定用户和年月数据
     *
     * @param yearAndMonth
     * @param callBack
     */
    public static void queryForMonth(String yearAndMonth, CallBack callBack) {
        BmobQuery<RecordBean> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("user", BmobUser.getCurrentUser(BmobUser.class));
        bmobQuery.addWhereEqualTo("yearAndMonth", yearAndMonth);
        bmobQuery.findObjects(new FindListener<RecordBean>() {
            @Override
            public void done(List<RecordBean> list, BmobException e) {
                if (e == null) {
                    callBack.success(list);
                } else {
                    callBack.fail(e.getMessage());
                    Log.e(TAG, "查询指定用户和年月数据error：" + e.getMessage());
                }
            }
        });
    }

    public static void saveBean(String date, String startTime, String endTime, String other, CallBack callBack) {
        RecordBean bean = new RecordBean(date, startTime, endTime, other);
        bean.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    callBack.success();
                } else {
                    callBack.fail(e.getMessage());
                    Log.e(TAG, "保存打卡数据error：" + e.getMessage());
                }
            }
        });
    }

    public static void updateBean(String date, String startTime, String endTime, String other, CallBack callBack) {
        queryForDay(date, new CallBack() {
            @Override
            public void success(Object... values) {
                List<RecordBean> list = (List<RecordBean>) values[0];
                if (list != null && list.size() > 0) {
                    if (startTime != null) {
                        list.get(0).startTime = startTime;
                        list.get(0).isLate = isLate(startTime);
                    }
                    if (endTime != null) {
                        list.get(0).endTime = endTime;
                        list.get(0).isLeaveEarly = isLeaveEarly(endTime);
                    }
                    if (other != null) {
                        list.get(0).other = other;
                    }
                    list.get(0).update(BmobUser.getCurrentUser(BmobUser.class).getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                callBack.success();
                            } else {
                                callBack.fail(e.getMessage());
                                Log.e(TAG, "修改打卡数据error：" + e.getLocalizedMessage());
                            }
                        }
                    });
                } else {
                    saveBean(date, startTime, endTime, other, callBack);
                }
            }

            @Override
            public void fail(String values) {

            }
        });
    }


    /**
     * 判断日期是周几
     *
     * @param dates 参数举例  String dates="2018-11-11";
     * @return
     */
    private static String getWeek(String dates) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = f.parse(dates);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(d);
        String s;
        switch (calendar.get(Calendar.DAY_OF_WEEK) - 1) {
            case 1:
                return "星期一";
            case 2:
                return "星期二";
            case 3:
                return "星期三";
            case 4:
                return "星期四";
            case 5:
                return "星期五";
            case 6:
                return "星期六";
            case 0:
                return "星期日";
            default:
                return "";
        }
    }

    /**
     * 获取年月
     *
     * @param date 2021-6-3
     */
    private static String getYM(String date) {
        String[] array = date.split("-");
        System.out.println(array.toString());
        return "" + array[0] + array[1];
    }

    /**
     * 判断是否迟到
     *
     * @param startTime
     * @return
     */
    private static boolean isLate(String startTime) {
        int hours, minute;
        if (startTime != null && startTime.length() > 0) {
            String[] split = startTime.split(":");
            if (split != null && split.length != 0) {
                hours = Integer.parseInt(split[0]);
                minute = Integer.parseInt(split[1]);
                return hours > 9 || (hours == 9 && minute > 30);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 判断是否早退
     *
     * @param endTime
     * @return
     */
    private static boolean isLeaveEarly(String endTime) {
        int hours, minute;
        if (endTime != null && endTime.length() > 0) {
            String[] split = endTime.split(":");
            if (split != null && split.length != 0) {
                hours = Integer.parseInt(split[0]);
                minute = Integer.parseInt(split[1]);
                return hours < 18 || (hours == 18 && minute < 30);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
