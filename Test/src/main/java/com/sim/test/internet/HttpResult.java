package com.sim.test.internet;

/**
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
