package com.sim.baselibrary.callback;

/**
 * @Author: Sim
 * @Time： 2020/12/22 11:29
 * @Description： 自定义 成功/失败 回调接口
 */
public interface SuccessOrFailListener {

    void success(Object... values);

    void fail(Object... values);

}
