package com.sim.record.greendao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * @Auther Sim
 * @Time 2020/10/25 1:05
 * @Description 插入数据库的打卡记录实体
 */
@Entity
public class DaKaRecord {

    @Id(autoincrement = true)
    private Long id;//自增长id
    private String year;//年
    private String month;//月
    private String day;//日
    private String week;//周几
    private String startTime;//上班卡时间
    private String endTime;//下班卡时间
    private String isLate;//是否迟到：0未迟到、1迟到
    private String isLeaveEarly;//是否早退：0未早退、1早退
    private String other;//其他

    @Generated(hash = 17334052)
    public DaKaRecord(Long id, String year, String month, String day, String week, String startTime, String endTime, String isLate,
                      String isLeaveEarly, String other) {
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

    @Generated(hash = 187742052)
    public DaKaRecord() {
    }

    public DaKaRecord(String year, String month, String day, String week, String startTime, String endTime,
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getOther() {
        return this.other;
    }

    public void setOther(String other) {
        this.other = other;
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

}
