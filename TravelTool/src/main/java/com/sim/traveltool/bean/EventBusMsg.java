package com.sim.traveltool.bean;

/**
 * EventBus用到的bean类
 * Created by Sim on 2020/4/28
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
