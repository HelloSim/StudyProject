package com.sim.traveltool.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sim.baselibrary.base.BaseActivity;
import com.sim.baselibrary.callback.SuccessOrFailListener;
import com.sim.baselibrary.utils.LogUtil;
import com.sim.baselibrary.utils.RegexUtil;
import com.sim.baselibrary.utils.ToastUtil;
import com.sim.baselibrary.views.SplitEditText;
import com.sim.traveltool.R;
import com.sim.traveltool.bean.db.User;
import com.sim.traveltool.ui.view.TitleView;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author Sim --- 用户注册页面
 */
public class UserRegisterActivity extends BaseActivity {

    private Context context;

    private TitleView titleView;
    private EditText etMobilePhoneNumber, etPassword, etUserName;
    private SplitEditText etSMSCode;
    private Button btnSMSCode, btnRegistered;

    private User user = new User();

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_user_register;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {
        titleView = findViewById(R.id.titleView);
        etMobilePhoneNumber = findViewById(R.id.et_mobile_phone_number);
        etPassword = findViewById(R.id.et_password);
        etSMSCode = findViewById(R.id.et_SMS_code);
        etUserName = findViewById(R.id.et_user_name);
        btnSMSCode = findViewById(R.id.btn_SMS_code);
        btnRegistered = findViewById(R.id.btn_registered);
        setViewClick(btnSMSCode, btnRegistered);
        titleView.setLeftClickListener(new TitleView.LeftClickListener() {
            @Override
            public void onClick(View leftView) {
                finish();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void initData() {
        context = this;
    }

    @Override
    protected void initView() {

    }


    @Override
    public void onMultiClick(View view) {
        if (view == btnSMSCode) {
            if (!RegexUtil.checkPhone(etMobilePhoneNumber.getText().toString())) {
                ToastUtil.toast(context, "请输入正确的手机号码！");
                return;
            }
            new TimeCount(60000, 1000).start();
            requestSMSCode(etMobilePhoneNumber.getText().toString());
        } else if (view == btnRegistered) {
            if (etUserName.getText().toString().length() <= 0) {
                ToastUtil.toast(context, "用户名不能为空！");
                return;
            }
            if (etPassword.getText().toString().length() <= 0) {
                ToastUtil.toast(context, "密码不能为空！");
                return;
            }
            if (!RegexUtil.checkPhone(etMobilePhoneNumber.getText().toString())) {
                ToastUtil.toast(context, "请输入正确的手机号码！");
                return;
            }
            registerUser(etMobilePhoneNumber.getText().toString(), etSMSCode.getText().toString(),
                    etPassword.getText().toString(), etUserName.getText().toString());
        } else {
            super.onMultiClick(view);
        }
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btnSMSCode.setClickable(false);
            btnSMSCode.setText(String.valueOf(millisUntilFinished / 1000));
            btnSMSCode.setBackground(getResources().getDrawable(R.drawable.button_bg_black));
        }

        @Override
        public void onFinish() {
            btnSMSCode.setClickable(true);
            btnSMSCode.setText("验证码");
            btnSMSCode.setBackground(getResources().getDrawable(R.drawable.button_bg_blue));
        }
    }

    /**
     * 手机号密码注册
     *
     * @param username
     * @param password
     */
    private void registerUser(String mobilePhoneNumber, String code, String password, String username) {
        if (username != null) user.setUsername(username);
        if (password != null) user.setPassword(password);
        if (mobilePhoneNumber != null) user.setMobilePhoneNumber(mobilePhoneNumber);
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    phoneVerify(mobilePhoneNumber, code, new SuccessOrFailListener() {
                        @Override
                        public void success(Object... values) {
                            ToastUtil.toast(context, "注册成功！");
                            finish();
                        }

                        @Override
                        public void fail(Object... values) {
                            ToastUtil.toast(context, "验证码验证失败！");
                            user.delete(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        LogUtil.e(getClass(), "删除用户成功！");
                                    } else {
                                        LogUtil.e(getClass(), "删除用户失败---coed:" + e.getErrorCode() + ";message:" + e.getMessage());
                                    }
                                }
                            });
                        }
                    });
                } else if (e.getMessage().contains("mobilePhoneNumber") && e.getMessage().contains("already taken")) {
                    ToastUtil.toast(context, "手机号码已被注册！");
                } else {
                    ToastUtil.toast(context, "注册失败！");
                    LogUtil.e(getClass(), "注册失败---coed:" + e.getErrorCode() + ";message:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 发送验证码短信
     */
    private void requestSMSCode(String phone) {
        //template 替换控制台设置的自定义短信模板名称；如果没有，则使用默认短信模板，默认模板名称为空字符串""。
        BmobSMS.requestSMSCode(phone, "", new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException e) {
                if (e == null) {
                    ToastUtil.toast(context, "发送验证码成功！");
                } else {
                    LogUtil.e(getClass(), "发送验证码失败---code:" + e.getErrorCode() + ";message:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 手机绑定
     *
     * @param code
     */
    private void phoneVerify(String phone, String code, SuccessOrFailListener successOrFailListener) {
        BmobSMS.verifySmsCode(phone, code, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    LogUtil.d(getClass(), "验证码验证成功！");
                    User user = BmobUser.getCurrentUser(User.class);
                    user.setMobilePhoneNumber(BmobUser.getCurrentUser(User.class).getMobilePhoneNumber());
                    user.setMobilePhoneNumberVerified(true);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                successOrFailListener.success();
                            } else {
                                successOrFailListener.fail();
                                LogUtil.e(getClass(), "绑定手机号码失败---code:" + e.getErrorCode() + ";message:" + e.getMessage());
                            }
                        }
                    });
                } else {
                    LogUtil.d(getClass(), "验证码验证失败！");
                    successOrFailListener.fail();
                }
            }
        });
    }

}
