package com.sim.traveltool.internet;


import com.sim.traveltool.bean.user.UserInfo;
import com.sim.traveltool.bean.news.WangYiNewsBean;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Grugsum on 2019/4/22.
 * 接口API类
 */
public interface APIService {

    /**
     * 网易新闻的请求API
     */
    @POST("/getWangYiNews")
    Observable<WangYiNewsBean> getWangYiNews(@Query("page") String page, @Query("count") String count);

    /**
     * 用户登录
     */
    @POST("/loginUser")
    Observable<UserInfo> logIn(@Query("apikey") String apikey, @Query("name") String name, @Query("passwd") String passwd);

    /**
     * 用户注册
     */
    @POST("/registerUser")
    Observable<UserInfo> registerUser(@Query("apikey") String apikey, @Query("name") String name, @Query("passwd") String passwd,
                                      @Query("headerImg") String headerImg, @Query("nikeName") String nikeName, @Query("autograph") String autograph,
                                      @Query("phone") String phone, @Query("email") String email, @Query("remarks") String remarks, @Query("vipGrade") String vipGrade);

    /**
     * 用户更新
     */
    @POST("/updateUserInfo")
    Observable<UserInfo> updateUserInfo(@Query("apikey") String apikey, @Query("name") String name, @Query("passwd") String passwd,
                                      @Query("headerImg") String headerImg, @Query("nikeName") String nikeName, @Query("autograph") String autograph,
                                      @Query("phone") String phone, @Query("email") String email, @Query("remarks") String remarks, @Query("vipGrade") String vipGrade);

}