package com.sim.baselibrary.callback;

/**
 * @author Sim --- 自定义 成功/失败 回调接口
 */
public interface SuccessOrFailListener {

    void success(Object... values);

    void fail(Object... values);

}
