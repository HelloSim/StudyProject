package com.sim.user.utils;

import android.util.Log;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class SMSUtil {

    private static final String TAG = "【【【Sim】】】" + SMSUtil.class.getSimpleName();

    /**
     * 向指定手机号码发送验证码短信
     *
     * @param phone    指定号码
     * @param callBack 结果监听
     */
    public static void requestSMSCode(String phone, CallBack callBack) {
        //template 替换控制台设置的自定义短信模板名称；如果没有，则使用默认短信模板，默认模板名称为空字符串""。
        BmobSMS.requestSMSCode(phone, "", new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    callBack.success();
                } else {
                    callBack.fail(e.getMessage());
                    Log.e(TAG, "向指定手机发送验证码短信出错---code:" + e.getErrorCode() + ";message:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 向用户绑定的手机号码发送短信验证码
     *
     * @param callBack 结果监听
     */
    public static void requestSMSCode(CallBack callBack) {
        //template 如果是自定义短信模板，此处替换为你在控制台设置的自定义短信模板名称；
        //如果没有对应的自定义短信模板，则使用默认短信模板，模板名称为空字符串""。
        BmobUser user = BmobUser.getCurrentUser(BmobUser.class);
        if (user.getMobilePhoneNumberVerified()) {
            BmobSMS.requestSMSCode(user.getMobilePhoneNumber(), "", new QueryListener<Integer>() {
                @Override
                public void done(Integer smsId, BmobException e) {
                    if (e == null) {
                        callBack.success();
                    } else {
                        callBack.fail(e.getMessage());
                        Log.e(TAG, "向用户绑定的手机发送验证码失败---code:" + e.getErrorCode() + ";message:" + e.getMessage());
                    }
                }
            });
        } else {
            callBack.fail("手机号码未验证！");
        }
    }

    /**
     * 手机绑定
     *
     * @param phone
     * @param code
     * @param callBack
     */
    public static void phoneVerify(String phone, String code, CallBack callBack) {
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
                            if (e == null) {
                                callBack.success();
                            } else {
                                callBack.fail(e.getMessage());
                            }
                        }
                    });
                } else {
                    callBack.fail(e.getMessage());
                }
            }
        });
    }

}
