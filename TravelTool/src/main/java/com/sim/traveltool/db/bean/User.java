package com.sim.traveltool.db.bean;

import cn.bmob.v3.BmobUser;

/**
 * @Auther Sim
 * @Time 2021/1/2 19:54
 * @Description 存取数据库的用户实体类
 */
public class User extends BmobUser {

    //系统属性
    //username	用户名/账号/用户唯一标志，可以是邮箱、手机号码、第三方平台的用户唯一标志
    //password	用户密码
    //email	用户邮箱
    //emailVerified	用户邮箱认证状态
    //mobilePhoneNumber	用户手机号码
    //mobilePhoneNumberVerified	用户手机号码认证状态

    public User() {
    }

}
