package com.sim.sqlitelibrary.bean;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @Auther Sim
 * @Time 2020/11/26 22:41
 * @Description 新的插入数据库的打卡记录实体
 */
@Entity
@SmartTable(name = "打卡详情")
public class RecordDataBean {

    @Id(autoincrement = true)
    private Long id;//自增长id

    private String year;//年

    private String month;//月

    @SmartColumn(id = 1, name = "日期", fixed = true)
    private String day;//日

    @SmartColumn(id = 2, name = "星期")
    private String week;//周几

//    private String isWorkingDay;//0工作日 1 假日 2节日

    @SmartColumn(id = 3, name = "上班卡时间")
    private String startTime;//上班卡时间

    @SmartColumn(id = 4, name = "下班卡时间")
    private String endTime;//下班卡时间

    private String isLate;//是否迟到：0未迟到、1迟到

    private String isLeaveEarly;//是否早退：0未早退、1早退

    @SmartColumn(id = 5, name = "其他")
    private String other;//其他

    @Generated(hash = 1816004749)
    public RecordDataBean(Long id, String year, String month, String day, String week,
            String startTime, String endTime, String isLate, String isLeaveEarly,
            String other) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.week = week;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isLate = isLate;
        this.isLeaveEarly = isLeaveEarly;
        this.other = other;
    }

    public RecordDataBean(String year, String month, String day,
                          String week, String startTime, String endTime,
                          String isLate, String isLeaveEarly, String other) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.week = week;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isLate = isLate;
        this.isLeaveEarly = isLeaveEarly;
        this.other = other;
    }

    @Generated(hash = 1002308030)
    public RecordDataBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getYear() {
        return this.year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return this.month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return this.day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getWeek() {
        return this.week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getIsLate() {
        return this.isLate;
    }

    public void setIsLate(String isLate) {
        this.isLate = isLate;
    }

    public String getIsLeaveEarly() {
        return this.isLeaveEarly;
    }

    public void setIsLeaveEarly(String isLeaveEarly) {
        this.isLeaveEarly = isLeaveEarly;
    }

    public String getOther() {
        return this.other;
    }

    public void setOther(String other) {
        this.other = other;
    }

}
