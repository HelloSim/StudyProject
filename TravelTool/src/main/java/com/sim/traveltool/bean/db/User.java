package com.sim.traveltool.bean.db;

import com.sim.baselibrary.utils.LogUtil;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;

/**
 * @author Sim --- 存取数据库的用户实体类
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

    /**
     * 同步控制台数据到缓存中
     */
    public static void fetchUserInfo() {
        BmobUser.fetchUserInfo(new FetchUserInfoListener<BmobUser>() {
            @Override
            public void done(BmobUser user, BmobException e) {
                if (e == null) {
                    LogUtil.d(getClass(), "更新用户本地缓存信息成功");
                } else {
                    LogUtil.e(getClass(), "更新用户本地缓存信息失败---code:" + e.getErrorCode() + ";message:" + e.getMessage());
                }
            }
        });
    }

}
