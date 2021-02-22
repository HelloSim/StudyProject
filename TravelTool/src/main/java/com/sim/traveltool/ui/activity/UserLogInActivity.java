package com.sim.traveltool.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sim.baselibrary.base.BaseActivity;
import com.sim.baselibrary.bean.EventMessage;
import com.sim.baselibrary.utils.LogUtil;
import com.sim.baselibrary.utils.RegexUtil;
import com.sim.baselibrary.utils.SPUtil;
import com.sim.baselibrary.utils.ToastUtil;
import com.sim.traveltool.AppHelper;
import com.sim.traveltool.R;
import com.sim.traveltool.bean.db.User;
import com.sim.traveltool.ui.view.TitleView;

import org.greenrobot.eventbus.EventBus;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * @author Sim --- 登陆页面
 */
public class UserLogInActivity extends BaseActivity {

    private Context context;

    private TitleView titleView;
    private LinearLayout llLoginByAccount, llLoginBySMSCode;
    private EditText etMobilePhoneNumber, etPassword, etMobilePhoneNumber2, etSMSCode;
    private Button btnLogIn, btnSMSCode, btnLogIn2;
    private TextView tvLoginBySMSCode, tvLoginByAccount;

    //更多弹窗
    private PopupWindow morePopupWindow;//弹窗
    private View moreLayout;//布局
    private Button btnRegistered;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_user_login;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {
        titleView = findViewById(R.id.titleView);
        llLoginByAccount = findViewById(R.id.ll_login_by_account);
        llLoginBySMSCode = findViewById(R.id.ll_login_by_SMS_code);
        etMobilePhoneNumber = findViewById(R.id.et_mobile_phone_number);
        etPassword = findViewById(R.id.et_password);
        btnLogIn = findViewById(R.id.btn_log_in);
        tvLoginBySMSCode = findViewById(R.id.tv_login_by_SMS_code);
        etMobilePhoneNumber2 = findViewById(R.id.et_mobile_phone_number2);
        btnSMSCode = findViewById(R.id.btn_SMS_code);
        etSMSCode = findViewById(R.id.et_SMS_code);
        btnLogIn2 = findViewById(R.id.btn_log_in2);
        tvLoginByAccount = findViewById(R.id.tv_login_by_account);

        setViewClick(btnLogIn, btnSMSCode, btnLogIn2, tvLoginBySMSCode, tvLoginByAccount);
        titleView.setClickListener(new TitleView.ClickListener() {
            @Override
            public void left(View leftView) {
                finish();
            }

            @Override
            public void right(View right) {
                morePopupWindow.showAsDropDown(titleView, titleView.getWidth(), 0);
            }
        });
    }

    @Override
    protected void initData() {
        context = this;
    }

    @Override
    protected void initView() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        moreLayout = inflater.inflate(R.layout.view_popup_login_more, null);

        morePopupWindow = showPopupWindow(moreLayout, 120, 70);
        btnRegistered = moreLayout.findViewById(R.id.btn_registered);
        setViewClick(btnRegistered);
    }

    @Override
    public void onMultiClick(View view) {
        if (view == tvLoginBySMSCode) {
            llLoginBySMSCode.setVisibility(View.VISIBLE);
            llLoginByAccount.setVisibility(View.GONE);
        } else if (view == tvLoginByAccount) {
            llLoginByAccount.setVisibility(View.VISIBLE);
            llLoginBySMSCode.setVisibility(View.GONE);
        } else if (view == btnLogIn) {
            if (etMobilePhoneNumber.getText().toString().length() > 0 && etPassword.getText().toString().length() > 0) {
                loginByAccount();
            } else {
                if (etMobilePhoneNumber.getText().toString().length() > 0) {
                    ToastUtil.toast(context, "请输入密码！");
                } else {
                    ToastUtil.toast(context, "请输入账号！");
                }
            }
        } else if (view == btnSMSCode) {
            if (!RegexUtil.checkPhone(etMobilePhoneNumber2.getText().toString())) {
                ToastUtil.toast(context, "请输入正确的手机号码！");
                return;
            }
            requestSMSCode(etMobilePhoneNumber2.getText().toString());
            new TimeCount(60000, 1000).start();
        } else if (view == btnLogIn2) {
            if (etMobilePhoneNumber2.getText().toString().length() > 0 && etSMSCode.getText().toString().length() > 0) {
                loginBySMSCode(etMobilePhoneNumber2.getText().toString(), etSMSCode.getText().toString());
            } else {
                if (etMobilePhoneNumber2.getText().toString().length() > 0) {
                    ToastUtil.toast(context, "请输入验证码！");
                } else {
                    ToastUtil.toast(context, "请输入手机号码！");
                }
            }
        } else if (view == btnRegistered) {
            morePopupWindow.dismiss();
            Intent intent = new Intent(this, UserRegisterActivity.class);
            startActivity(intent);
        } else {
            super.onMultiClick(view);
        }
    }

    /**
     * 手机号码+密码登录
     */
    private void loginByAccount() {
        BmobUser.loginByAccount(etMobilePhoneNumber.getText().toString(), etPassword.getText().toString(), new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    EventBus.getDefault().post(new EventMessage(AppHelper.USER_IsLogIn));
                    SPUtil.put(context, AppHelper.userSpName, AppHelper.userSpStateKey, true);
                    finish();
                } else {
                    if (e.getMessage().contains("username or password incorrect")) {
                        ToastUtil.toast(context, "用户名或密码不正确！");
                    } else {
                        ToastUtil.toast(context, "登录出错！");
                        LogUtil.e(getClass(), "登录出错---code:" + e.getErrorCode() + ";message:" + e.getMessage());
                    }
                }
            }
        });
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

    private void loginBySMSCode(String phone, String code) {
        BmobUser.signOrLoginByMobilePhone(phone, code, new LogInListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if (e == null) {
                    EventBus.getDefault().post(new EventMessage(AppHelper.USER_IsLogIn));
                    SPUtil.put(context, AppHelper.userSpName, AppHelper.userSpStateKey, true);
                    finish();
                } else {
                    ToastUtil.toast(context, "登录出错！");
                    LogUtil.e(getClass(), "登录出错---code:" + e.getErrorCode() + ";message:" + e.getMessage());
                }
            }
        });
    }

}
