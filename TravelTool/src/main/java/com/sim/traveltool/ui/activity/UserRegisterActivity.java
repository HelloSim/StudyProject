package com.sim.traveltool.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sim.baselibrary.base.BaseActivity;
import com.sim.baselibrary.utils.LogUtil;
import com.sim.baselibrary.utils.RegexUtil;
import com.sim.baselibrary.utils.ToastUtil;
import com.sim.traveltool.R;
import com.sim.traveltool.db.bean.User;
import com.sim.traveltool.ui.view.TitleView;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @Auther Sim
 * @Time 2020/4/29 1:05
 * @Description 用户注册页面
 */
public class UserRegisterActivity extends BaseActivity {

    private Context context;

    private TitleView titleView;
    private EditText etUserName;
    private EditText etPassword;
    private EditText etMobilePhoneNumber;
    private EditText etEmail;
    private Button btnRegistered;

    private User user = new User();

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_user_register;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {
        titleView = findViewById(R.id.titleView);
        etUserName = findViewById(R.id.et_user_name);
        etPassword = findViewById(R.id.et_password);
        etMobilePhoneNumber = findViewById(R.id.et_mobile_phone_number);
        etEmail = findViewById(R.id.et_email);
        btnRegistered = findViewById(R.id.btn_registered);
        setViewClick(btnRegistered);
        titleView.setLeftClickListener(new TitleView.LeftClickListener() {
            @Override
            public void onClick(View leftView) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        context = this;
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onMultiClick(View view) {
        if (view == btnRegistered) {
            if (etUserName.getText().toString().length() <= 0) {
                ToastUtil.T_Info(context, "用户名不能为空！");
                return;
            }
            if (etPassword.getText().toString().length() <= 0) {
                ToastUtil.T_Info(context, "密码不能为空！");
                return;
            }
            if (!RegexUtil.checkPhone(etMobilePhoneNumber.getText().toString())) {
                ToastUtil.T_Info(context, "请输入正确的手机号码！");
                return;
            }
            if (!RegexUtil.email(etEmail.getText().toString())) {
                ToastUtil.T_Info(context, "请输入正确的电子邮箱！");
                return;
            }
            registerUser(etUserName.getText().toString(), etPassword.getText().toString(),
                    etMobilePhoneNumber.getText().toString(), etEmail.getText().toString());
        } else {
            super.onMultiClick(view);
        }
    }

    /**
     * 账号密码注册
     *
     * @param username
     * @param password
     */
    private void registerUser(String username, String password, String mobilePhoneNumber, String email) {
        if (username != null) user.setUsername(username);
        if (password != null) user.setPassword(password);
        if (mobilePhoneNumber != null) user.setMobilePhoneNumber(mobilePhoneNumber);
        if (email != null) user.setEmail(email);
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    ToastUtil.T_Success(context, "注册成功！");
                    finish();
                } else {
                    if (e.getMessage().contains("username") && e.getMessage().contains("already taken")) {
                        ToastUtil.T_Error(context, "账号已被注册！");
                    } else if (e.getMessage().contains("mobilePhoneNumber") && e.getMessage().contains("already taken")) {
                        ToastUtil.T_Error(context, "手机号码已被注册！");
                    } else if (e.getMessage().contains("email") && e.getMessage().contains("already taken")) {
                        ToastUtil.T_Error(context, "电子邮箱已被注册！");
                    } else {
                        ToastUtil.T_Error(context, "注册失败！");
                        LogUtil.e(getClass(), "注册失败---coed:" + e.getErrorCode() + ";message:" + e.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 发送验证码短信
     */
    private void requestSMSCode() {
        //template 如果是自定义短信模板，此处替换为你在控制台设置的自定义短信模板名称；如果没有对应的自定义短信模板，则使用默认短信模板，默认模板名称为空字符串""。
        BmobSMS.requestSMSCode(BmobUser.getCurrentUser(User.class).getMobilePhoneNumber(), "", new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException e) {
                if (e == null) {
                    ToastUtil.T_Success(context, "发送验证码成功！");
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
    private void phoneVerify(String code) {
        BmobSMS.verifySmsCode(BmobUser.getCurrentUser(User.class).getMobilePhoneNumber(), code, new UpdateListener() {
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
                                ToastUtil.T_Success(context, "绑定手机号码成功！");
                            } else {
                                ToastUtil.T_Success(context, "绑定手机号码失败！");
                                LogUtil.e(getClass(), "绑定/解绑手机号码失败---code:" + e.getErrorCode() + ";message:" + e.getMessage());
                            }
                        }
                    });
                } else {
                    LogUtil.d(getClass(), "验证码验证失败！");
                }
            }
        });
    }

}
