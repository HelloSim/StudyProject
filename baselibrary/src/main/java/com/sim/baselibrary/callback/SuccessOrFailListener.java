package com.sim.baselibrary.callback;

/**
 * @Auther Sim
 * @Time 2020/12/22 11:29
 * @Description 自定义 成功/失败 回调接口
 */
public interface SuccessOrFailListener {

    public void success(Object... values);

    public void fail(Object... values);

}
