package com.sim.traveltool.bean;


import com.sim.traveltool.constant.Constant;

/**
 * 网络请求返回的数据统一格式bean类
 * Created by Grugsum on 2019/4/22.
 */

public class HttpResult<T> {

    public String error_code;
    public String error_msg;
    public T content;

    public boolean isSuccess() {
        return Constant.SUCCESS.equals( error_code );
    }

}
