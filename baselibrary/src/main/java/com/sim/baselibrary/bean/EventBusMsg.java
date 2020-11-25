package com.sim.baselibrary.bean;

/**
 * @Auther Sim
 * @Time 2020/4/28 1:05
 * @Description EventBus用到的bean类
 */
public class EventBusMsg {

    public String msg;
    public String other;

    public EventBusMsg(String msg) {
        this.msg = msg;
    }

    public EventBusMsg(String msg, String other) {
        this.msg = msg;
        this.other = other;
    }

}
