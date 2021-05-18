package com.sim.traveltool.bean.db;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;
import com.sim.baselibrary.utils.TimeUtil;

import cn.bmob.v3.BmobObject;

/**
 * @author Sim --- 新的插入数据库的打卡记录实体
 */
@SmartTable(name = "打卡详情")
public class RecordBean extends BmobObject {

    // 仅在客户端使用，不希望被gson序列化提交到后端云，用transient修饰

    private User user;//打卡人
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

    public RecordBean(String date) {
        this.date = date;
        this.week = TimeUtil.getWeek(date);
//        BmobACL bmobACL = new BmobACL();
//        bmobACL.setWriteAccess(user, true);//设置此帖子为当前用户可写
//        bmobACL.setReadAccess(user, true);//设置此帖子为某种角色可读
//        this.setACL(bmobACL);
    }

    public RecordBean(User user, String date, String yearAndMonth, String startTime, String endTime, String other) {
        this.user = user;
        this.date = date;
        this.yearAndMonth = yearAndMonth;
        this.week = TimeUtil.getWeek(date);
        this.startTime = startTime;
        this.endTime = endTime;
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
        this.other = other;
//        BmobACL bmobACL = new BmobACL();
//        bmobACL.setWriteAccess(user, true);//设置为当前用户可写
//        bmobACL.setReadAccess(user, true);//设置为当前用户可读
//        this.setACL(bmobACL);
    }


    public User getUser() {
        return user;
    }

    public void setUsername(User user) {
        this.user = user;
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

}
