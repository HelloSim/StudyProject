package com.sim.mine.utils;

/**
 * @author Sim --- 自定义 成功/失败 回调接口
 */
public interface CallBack {

    void success(Object... values);

    void fail(String values);

}
