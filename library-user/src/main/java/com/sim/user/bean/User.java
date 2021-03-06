package com.sim.user.bean;

import android.util.Log;

import com.sim.user.utils.SMSUtil;
import com.sim.user.utils.CallBack;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class User extends BmobUser {

    private static final String TAG = "【【【Sim_" + User.class.getSimpleName() + "】】】";

    public static String sessionToken() {
        return User.getCurrentUser(User.class).getSessionToken();
    }

    public static String userName() {
        return User.getCurrentUser(User.class).getUsername();
    }

    public static String email() {
        return User.getCurrentUser(User.class).getEmail();
    }

    public static boolean emailVerified() {
        return User.getCurrentUser(User.class).getEmailVerified();
    }

    public static String mobilePhoneNumber() {
        return User.getCurrentUser(User.class).getMobilePhoneNumber();
    }

    public static boolean mobilePhoneNumberVerified() {
        return User.getCurrentUser(User.class).getMobilePhoneNumberVerified();
    }

    public static void logout() {
        logOut();
        fetchUserInfo();
    }

    /**
     * 同步控制台数据到缓存中
     */
    public static void fetchUserInfo() {
        BmobUser.fetchUserInfo(new FetchUserInfoListener<BmobUser>() {
            @Override
            public void done(BmobUser user, BmobException e) {
                if (e == null) {
                    Log.d(TAG, "更新用户本地缓存信息成功");
                } else {
                    Log.e(TAG, "更新用户本地缓存信息error:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 手机号密码注册
     *
     * @param mobilePhoneNumber
     * @param code
     * @param password
     * @param username
     * @param callBack
     */
    public static void registerUser(String mobilePhoneNumber, String code, String password, String username, CallBack callBack) {
        User user = User.getCurrentUser(User.class);
        if (username != null) user.setUsername(username);
        if (password != null) user.setPassword(password);
        if (mobilePhoneNumber != null) user.setMobilePhoneNumber(mobilePhoneNumber);
        user.signUp(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser user, BmobException e) {
                if (e == null) {
                    SMSUtil.phoneVerify(mobilePhoneNumber, code, new CallBack() {
                        @Override
                        public void success(Object... values) {
                            callBack.success();
                        }

                        @Override
                        public void fail(String values) {
                            callBack.fail("验证码验证失败！");
                            Log.e(TAG, "注册发送短信error：" + e.getMessage());
                            user.delete(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                }
                            });
                        }
                    });
                } else if (e.getMessage().contains("mobilePhoneNumber") && e.getMessage().contains("already taken")) {
                    callBack.fail("手机号码已被注册！");
                } else {
                    callBack.fail(e.getMessage());
                    Log.e(TAG, "注册error：" + e.getMessage());
                }
            }
        });
    }

    /**
     * 手机号码+密码登录
     *
     * @param phoneNum
     * @param password
     * @param callBack
     */
    public static void loginByAccount(String phoneNum, String password, CallBack callBack) {
        loginByAccount(phoneNum, password, new LogInListener<Object>() {
            @Override
            public void done(Object o, BmobException e) {
                if (e == null) {
                    callBack.success();
                    fetchUserInfo();
                } else {
                    if (e.getMessage().contains("username or password incorrect")) {
                        callBack.fail("用户名或密码不正确！");
                    } else {
                        callBack.fail(e.getMessage());
                        Log.e(TAG, "密码登录error：" + e.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 验证码登录
     *
     * @param phone
     * @param code
     * @param callBack
     */
    public static void loginBySMSCode(String phone, String code, CallBack callBack) {
        signOrLoginByMobilePhone(phone, code, new LogInListener<Object>() {
            @Override
            public void done(Object o, BmobException e) {
                if (e == null) {
                    callBack.success();
                    fetchUserInfo();
                } else {
                    callBack.fail(e.getMessage());
                    Log.e(TAG, "验证码登录error：" + e.getMessage());
                }
            }
        });
    }

    /**
     * 提供旧密码修改密码
     *
     * @param oldPassword
     * @param newPassword
     * @param callBack
     */
    public static void updatePassword(String oldPassword, String newPassword, CallBack callBack) {
        User.getCurrentUser(User.class).updateCurrentUserPassword(oldPassword, newPassword, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    callBack.success();
                    fetchUserInfo();
                } else {
                    if (e.getMessage().contains("old password incorrect")) {
                        callBack.fail("密码错误！");
                    } else {
                        callBack.fail(e.getMessage());
                        Log.e(TAG, "旧密码修改密码error：" + e.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 验证码修改密码
     *
     * @param code
     * @param newPassword
     * @param callBack
     */
    public static void resetPasswordBySMSCode(String code, String newPassword, CallBack callBack) {
        User.getCurrentUser(User.class).resetPasswordBySMSCode(code, newPassword, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    callBack.success();
                    fetchUserInfo();
                } else {
                    callBack.fail(e.getMessage());
                    Log.e(TAG, "验证码重置密码error：" + e.getMessage());
                }
            }
        });
    }

    /**
     * 修改用户名
     *
     * @param userName
     * @param callBack
     */
    public static void updateUserInfo(String userName, CallBack callBack) {
        User user = User.getCurrentUser(User.class);
        user.setUsername(userName);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    callBack.success();
                    fetchUserInfo();
                } else {
                    callBack.fail(e.getMessage());
                    Log.e(TAG, "修改邮箱error：" + e.getMessage());
                }
            }
        });
    }

}
