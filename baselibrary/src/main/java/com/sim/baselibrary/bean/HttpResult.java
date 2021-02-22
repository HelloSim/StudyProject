package com.sim.baselibrary.bean;

import com.sim.baselibrary.constant.Constant;

/**
 * @author Sim --- 网络请求返回的数据统一格式bean类
 */
public class HttpResult<T> {

    public String error_code;
    public String error_msg;
    public T content;

    public boolean isSuccess() {
        return error_code.equals(Constant.SUCCESS);
    }

}
