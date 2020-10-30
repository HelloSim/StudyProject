package com.sim.traveltool.businternet;

/**
 * Created by Grugsum on 2019/4/22.
 * 自定义异常处理
 */

public class APIException extends RuntimeException {

    public String code;
    public String massage;

    public APIException(String code, String massage) {
        this.code = code;
        this.massage = massage;
    }

    @Override
    public String toString() {
        return "APIException{" + "code='" + code + '\'' + ", massage='" + massage + '\'' + '}';
    }

    public String getCode() {
        return code;
    }

    public String getMassage() {
        return massage;
    }
}
