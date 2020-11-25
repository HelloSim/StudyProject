package com.sim.traveltool.internet;


import com.sim.traveltool.bean.UserInfo;
import com.sim.traveltool.bean.NewsWangYiBean;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Grugsum on 2019/4/22.
 * 网易、用户 接口API类
 */
public interface UserAPIService {

    /**
     * 网易新闻的请求API
     *
     * @param page
     * @param count
     * @return
     */
    @POST("/getWangYiNews")
    Observable<NewsWangYiBean> getWangYiNews(@Query("page") String page, @Query("count") String count);

    /**
     * 用户登录
     *
     * @param apikey
     * @param name
     * @param passwd
     * @return
     */
    @POST("/loginUser")
    Observable<UserInfo> logIn(@Query("apikey") String apikey, @Query("name") String name, @Query("passwd") String passwd);

    /**
     * 用户注册
     *
     * @param apikey
     * @param name
     * @param passwd
     * @param headerImg
     * @param nikeName
     * @param autograph
     * @param phone
     * @param email
     * @param remarks
     * @param vipGrade
     * @return
     */
    @POST("/registerUser")
    Observable<UserInfo> registerUser(@Query("apikey") String apikey, @Query("name") String name, @Query("passwd") String passwd,
                                      @Query("headerImg") String headerImg, @Query("nikeName") String nikeName, @Query("autograph") String autograph,
                                      @Query("phone") String phone, @Query("email") String email, @Query("remarks") String remarks, @Query("vipGrade") String vipGrade);

    /**
     * 用户更新
     *
     * @param apikey
     * @param name
     * @param passwd
     * @param headerImg
     * @param nikeName
     * @param autograph
     * @param phone
     * @param email
     * @param remarks
     * @param vipGrade
     * @return
     */
    @POST("/updateUserInfo")
    Observable<UserInfo> updateUserInfo(@Query("apikey") String apikey, @Query("name") String name, @Query("passwd") String passwd,
                                        @Query("headerImg") String headerImg, @Query("nikeName") String nikeName, @Query("autograph") String autograph,
                                        @Query("phone") String phone, @Query("email") String email, @Query("remarks") String remarks, @Query("vipGrade") String vipGrade);

}