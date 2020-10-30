package com.sim.traveltool.internet;


import com.sim.traveltool.bean.UserInfo;
import com.sim.traveltool.bean.WangYiNewsBean;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Grugsum on 2019/4/22.
 * 单例
 */

public class APIFactory extends RetrofitUtil {

    private APIFactory() {
    }

    public static APIFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final APIFactory INSTANCE = new APIFactory();
    }

    /**
     * NewsData的网络请求
     */
    public void getWangYiNew(Subscriber<WangYiNewsBean> subscriber, String page, String count) {
        Observable observable = apiService.getWangYiNews(page, count);
        toSubscribe(observable, subscriber);
    }

    /**
     * 用户登录的网络请求
     */
    public void logIn(Subscriber<UserInfo> subscriber, String apikey, String name, String passwd) {
        Observable observable = apiService.logIn(apikey, name, passwd);
        toSubscribe(observable, subscriber);
    }

    /**
     * 用户注册的网络请求
     */
    public void registerUser(Subscriber<UserInfo> subscriber, String apikey, String name, String passwd,
                             String headerImg, String nikeName, String autograph,
                             String phone, String email, String remarks, String vipGrade) {
        Observable observable = apiService.registerUser(apikey, name, passwd, headerImg, nikeName, autograph, phone, email, remarks, vipGrade);
        toSubscribe(observable, subscriber);
    }

    /**
     * 用户更新的网络请求
     */
    public void updateUserInfo(Subscriber<UserInfo> subscriber, String apikey, String name, String passwd,
                             String headerImg, String nikeName, String autograph,
                             String phone, String email, String remarks, String vipGrade) {
        Observable observable = apiService.updateUserInfo(apikey, name, passwd, headerImg, nikeName, autograph, phone, email, remarks, vipGrade);
        toSubscribe(observable, subscriber);
    }

}
