package com.sim.baselibrary.bean;

/**
 * @Author: Sim
 * @Time： 2020/4/28 1:05
 * @Description： EventBus用到的bean类
 */
public class EventMessage {

    public int type;
    public String message;

    public EventMessage(int type) {
        this.type = type;
    }

    public EventMessage(int type, String message) {
        this.type = type;
        this.message = message;
    }

}
