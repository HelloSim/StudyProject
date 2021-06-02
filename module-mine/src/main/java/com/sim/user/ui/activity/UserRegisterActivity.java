package com.sim.user.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sim.basicres.base.BaseActivity;
import com.sim.basicres.constant.ArouterUrl;
import com.sim.basicres.utils.RegexUtil;
import com.sim.basicres.utils.ToastUtil;
import com.sim.basicres.views.SplitEditText;
import com.sim.basicres.views.TitleView;
import com.sim.user.R;
import com.sim.user.utils.SuccessOrFailListener;
import com.sim.user.utils.UserUtil;

/**
 * @author Sim --- 用户注册页面
 */
@Route(path = ArouterUrl.Mine.user_activity_register)
public class UserRegisterActivity extends BaseActivity {

    private Context context;

    private TitleView titleView;
    private EditText etMobilePhoneNumber, etPassword, etUserName;
    private SplitEditText etSMSCode;
    private Button btnSMSCode, btnRegistered;

    @Override
    protected int getLayoutRes() {
        return R.layout.mine_activity_register;
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
            UserUtil.getInstance().requestSMSCode(etMobilePhoneNumber.getText().toString(), new SuccessOrFailListener() {
                @Override
                public void success(Object... values) {

                }

                @Override
                public void fail(String values) {
                    ToastUtil.toast(UserRegisterActivity.this, "发送失败：" + values);
                }
            });
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
            UserUtil.getInstance().registerUser(etMobilePhoneNumber.getText().toString(), etSMSCode.getText().toString(),
                    etPassword.getText().toString(), etUserName.getText().toString(), new SuccessOrFailListener() {
                        @Override
                        public void success(Object... values) {
                            ToastUtil.toast(UserRegisterActivity.this, "注册成功");
                        }

                        @Override
                        public void fail(String values) {
                            ToastUtil.toast(UserRegisterActivity.this, "注册失败：" + values);
                        }
                    });
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
