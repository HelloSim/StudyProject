package com.sim.user.utils;

import android.util.Log;

import com.sim.user.bean.NewsBean;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @ author: Sim
 * @ time： 2021/6/2 10:58
 * @ description：
 */
public class UserUtil {

    private final String TAG = "【【【Sim】】】" + this.getClass().getSimpleName();

    private UserUtil() {
    }

    public static UserUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final UserUtil INSTANCE = new UserUtil();
    }


    private BmobUser user = BmobUser.getCurrentUser(BmobUser.class);


    public boolean isLogin() {
        return user.isLogin();
    }

    public BmobUser getUser() {
        if (user.isLogin())
            return user;
        else
            return null;
    }

    /**
     * 同步控制台数据到缓存中
     */
    public void fetchUserInfo() {
        user.fetchUserInfo(new FetchUserInfoListener<BmobUser>() {
            @Override
            public void done(BmobUser user, BmobException e) {
                if (e == null) {
                    Log.d(TAG, "更新用户本地缓存信息成功");
                } else {
                    Log.e(TAG, "更新用户本地缓存信息失败---code:" + e.getErrorCode() + ";message:" + e.getMessage());
                }
            }
        });
    }


    /**
     * 向指定手机号码发送验证码短信
     */
    public void requestSMSCode(String phone, SuccessOrFailListener successOrFailListener) {
        //template 替换控制台设置的自定义短信模板名称；如果没有，则使用默认短信模板，默认模板名称为空字符串""。
        BmobSMS.requestSMSCode(phone, "", new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    successOrFailListener.success();
                } else {
                    successOrFailListener.fail(e.getMessage());
                    Log.e(TAG, "向指定手机发送验证码短信出错---code:" + e.getErrorCode() + ";message:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 向用户绑定的手机号码发送短信验证码
     *
     * @param successOrFailListener
     */
    public void requestSMSCode(SuccessOrFailListener successOrFailListener) {
        //template 如果是自定义短信模板，此处替换为你在控制台设置的自定义短信模板名称；
        //如果没有对应的自定义短信模板，则使用默认短信模板，模板名称为空字符串""。
        if (user.getMobilePhoneNumberVerified()) {
            BmobSMS.requestSMSCode(user.getMobilePhoneNumber(), "", new QueryListener<Integer>() {
                @Override
                public void done(Integer smsId, BmobException e) {
                    if (e == null) {
                        successOrFailListener.success();
                    } else {
                        successOrFailListener.fail(e.getMessage());
                        Log.e(TAG, "向用户绑定的手机发送验证码失败---code:" + e.getErrorCode() + ";message:" + e.getMessage());
                    }
                }
            });
        } else {
            successOrFailListener.fail("手机号码未验证！");
        }
    }

    /**
     * 手机号密码注册
     *
     * @param username
     * @param password
     */
    public void registerUser(String mobilePhoneNumber, String code, String password, String username, SuccessOrFailListener successOrFailListener) {
        BmobUser bmobUser = new BmobUser();
        if (username != null) bmobUser.setUsername(username);
        if (password != null) bmobUser.setPassword(password);
        if (mobilePhoneNumber != null) bmobUser.setMobilePhoneNumber(mobilePhoneNumber);
        bmobUser.signUp(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser user, BmobException e) {
                if (e == null) {
                    phoneVerify(mobilePhoneNumber, code, new SuccessOrFailListener() {
                        @Override
                        public void success(Object... values) {
                            successOrFailListener.success();
                        }

                        @Override
                        public void fail(String values) {
                            successOrFailListener.fail("验证码验证失败！");
                            user.delete(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                }
                            });
                        }
                    });
                } else if (e.getMessage().contains("mobilePhoneNumber") && e.getMessage().contains("already taken")) {
                    successOrFailListener.fail("手机号码已被注册！");
                } else {
                    successOrFailListener.fail(e.getMessage());
                    Log.e(TAG, "注册失败---coed:" + e.getErrorCode() + ";message:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 手机绑定
     *
     * @param phone
     * @param code
     * @param successOrFailListener
     */
    public void phoneVerify(String phone, String code, SuccessOrFailListener successOrFailListener) {
        BmobSMS.verifySmsCode(phone, code, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    BmobUser user = BmobUser.getCurrentUser(BmobUser.class);
                    user.setMobilePhoneNumber(BmobUser.getCurrentUser(BmobUser.class).getMobilePhoneNumber());
                    user.setMobilePhoneNumberVerified(true);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            fetchUserInfo();
                        }
                    });
                    successOrFailListener.success();
                } else {
                    successOrFailListener.fail(e.getMessage());
                }
            }
        });
    }


    /**
     * 手机号码+密码登录
     */
    public void loginByAccount(String phoneNum, String password, SuccessOrFailListener successOrFailListener) {
        BmobUser.loginByAccount(phoneNum, password, new LogInListener<Object>() {
            @Override
            public void done(Object o, BmobException e) {
                if (e == null) {
                    successOrFailListener.success();
                    fetchUserInfo();
                } else {
                    if (e.getMessage().contains("username or password incorrect")) {
                        successOrFailListener.fail("用户名或密码不正确！");
                    } else {
                        successOrFailListener.fail(e.getMessage());
                        Log.e(TAG, "登录出错---code:" + e.getErrorCode() + ";message:" + e.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 验证码登录
     */
    public void loginBySMSCode(String phone, String code, SuccessOrFailListener successOrFailListener) {
        BmobUser.signOrLoginByMobilePhone(phone, code, new LogInListener<Object>() {
            @Override
            public void done(Object o, BmobException e) {
                if (e == null) {
                    successOrFailListener.success();
                    fetchUserInfo();
                } else {
                    successOrFailListener.fail(e.getMessage());
                    Log.e(TAG, "登录出错---code:" + e.getErrorCode() + ";message:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 修改邮箱
     *
     * @param userName
     */
    public void updateUserInfo(String userName, SuccessOrFailListener successOrFailListener) {
        user.setUsername(userName);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    successOrFailListener.success();
                    fetchUserInfo();
                } else {
                    successOrFailListener.fail(e.getMessage());
                    Log.e(TAG, "修改用户邮箱信息失败---code:" + e.getErrorCode() + ";message:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 提供旧密码修改密码
     *
     * @param oldPassword
     * @param newPassword
     * @param successOrFailListener
     */
    public void updatePassword(String oldPassword, String newPassword, SuccessOrFailListener successOrFailListener) {
        BmobUser.updateCurrentUserPassword(oldPassword, newPassword, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    successOrFailListener.success();
                    fetchUserInfo();
                } else {
                    if (e.getMessage().contains("old password incorrect")) {
                        successOrFailListener.fail("密码错误！");
                    } else {
                        successOrFailListener.fail(e.getMessage());
                    }
                    Log.e(TAG, "修改用户密码信息失败---code:" + e.getErrorCode() + ";message:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 验证码修改密码
     *
     * @param code
     * @param newPassword
     * @param successOrFailListener
     */
    public void resetPasswordBySMSCode(String code, String newPassword, SuccessOrFailListener successOrFailListener) {
        BmobUser.resetPasswordBySMSCode(code, newPassword, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    successOrFailListener.success();
                    fetchUserInfo();
                } else {
                    successOrFailListener.fail(e.getMessage());
                    Log.e(TAG, "验证码重置密码失败---code:" + e.getErrorCode() + ";message:" + e.getMessage());
                }
            }
        });
    }

}
