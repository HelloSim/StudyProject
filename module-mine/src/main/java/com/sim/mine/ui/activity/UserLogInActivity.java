package com.sim.mine.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sim.basicres.base.BaseActivity;
import com.sim.basicres.bean.EventMessage;
import com.sim.basicres.constant.AppHelper;
import com.sim.basicres.constant.ArouterUrl;
import com.sim.basicres.utils.RegexUtil;
import com.sim.basicres.utils.SPUtil;
import com.sim.basicres.utils.ToastUtil;
import com.sim.basicres.views.TitleView;
import com.sim.mine.R;
import com.sim.user.bean.User;
import com.sim.user.utils.SMSUtil;
import com.sim.user.utils.CallBack;

import org.greenrobot.eventbus.EventBus;


@Route(path = ArouterUrl.Mine.user_activity_login)
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
        return R.layout.mine_activity_login;
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
        moreLayout = inflater.inflate(R.layout.mine_view_popup_login_more, null);

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
                User.loginByAccount(etMobilePhoneNumber.getText().toString(), etPassword.getText().toString(), new CallBack() {
                    @Override
                    public void success(Object... values) {
                        EventBus.getDefault().post(new EventMessage(AppHelper.USER_IsLogIn));
                        finish();
                    }

                    @Override
                    public void fail(String values) {
                        ToastUtil.toast(context, "登录失败：" + values);
                    }
                });
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
            SMSUtil.requestSMSCode(etMobilePhoneNumber2.getText().toString(), new CallBack() {
                @Override
                public void success(Object... values) {
                    ToastUtil.toast(context, "发送验证码成功！");
                }

                @Override
                public void fail(String values) {
                    ToastUtil.toast(context, "发送失败：" + values);
                }
            });
            new TimeCount(60000, 1000).start();
        } else if (view == btnLogIn2) {
            if (etMobilePhoneNumber2.getText().toString().length() > 0 && etSMSCode.getText().toString().length() > 0) {
                User.loginBySMSCode(etMobilePhoneNumber2.getText().toString(), etSMSCode.getText().toString(), new CallBack() {
                    @Override
                    public void success(Object... values) {
                        EventBus.getDefault().post(new EventMessage(AppHelper.USER_IsLogIn));
                        SPUtil.put(context, AppHelper.userSpName, AppHelper.userSpStateKey, true);
                        finish();
                    }

                    @Override
                    public void fail(String values) {
                        ToastUtil.toast(context, "登录失败：" + values);
                    }
                });
            } else {
                if (etMobilePhoneNumber2.getText().toString().length() > 0) {
                    ToastUtil.toast(context, "请输入验证码！");
                } else {
                    ToastUtil.toast(context, "请输入手机号码！");
                }
            }
        } else if (view == btnRegistered) {
            morePopupWindow.dismiss();
            ARouter.getInstance().build(ArouterUrl.Mine.user_activity_register).navigation();
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
            btnSMSCode.setBackground(getResources().getDrawable(R.drawable.common_button_bg_black));
        }

        @Override
        public void onFinish() {
            btnSMSCode.setClickable(true);
            btnSMSCode.setText("验证码");
            btnSMSCode.setBackground(getResources().getDrawable(R.drawable.common_button_bg_blue));
        }
    }

}
