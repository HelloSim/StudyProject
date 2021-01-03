package com.sim.traveltool.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.sim.baselibrary.base.BaseActivity;
import com.sim.baselibrary.utils.LogUtil;
import com.sim.baselibrary.utils.RegexUtil;
import com.sim.baselibrary.utils.ToastUtil;
import com.sim.traveltool.R;
import com.sim.traveltool.db.bean.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * @Auther Sim
 * @Time 2020/4/29 1:05
 * @Description 用户注册页面
 */
public class UserRegisterActivity extends BaseActivity {

    private ImageView back;
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
        back = findViewById(R.id.back);
        etUserName = findViewById(R.id.et_user_name);
        etPassword = findViewById(R.id.et_password);
        etMobilePhoneNumber = findViewById(R.id.et_mobile_phone_number);
        etEmail = findViewById(R.id.et_email);
        btnRegistered = findViewById(R.id.btn_registered);
        setViewClick(back, btnRegistered);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    public void onMultiClick(View view) {
        if (view == back) {
            finish();
        } else if (view == btnRegistered) {
            if (etUserName.getText().toString().length() <= 0) {
                ToastUtil.T_Info(UserRegisterActivity.this, getString(R.string.username_no_null));
                return;
            }
            if (etPassword.getText().toString().length() <= 0) {
                ToastUtil.T_Info(UserRegisterActivity.this,  getString(R.string.password_no_null));
                return;
            }
            if (!RegexUtil.checkPhone(etMobilePhoneNumber.getText().toString())) {
                ToastUtil.T_Info(UserRegisterActivity.this, getString(R.string.phone_no_null));
                return;
            }
            if (!RegexUtil.email(etEmail.getText().toString())) {
                ToastUtil.T_Info(UserRegisterActivity.this, getString(R.string.email_no_null));
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
                    ToastUtil.T_Success(UserRegisterActivity.this, getString(R.string.register_success));
                    finish();
                } else {
                    if (e.getMessage().contains("username") && e.getMessage().contains("already taken")) {
                        ToastUtil.T_Error(UserRegisterActivity.this, getString(R.string.register_fail_username));
                    } else if (e.getMessage().contains("mobilePhoneNumber") && e.getMessage().contains("already taken")) {
                        ToastUtil.T_Error(UserRegisterActivity.this, getString(R.string.register_fail_phone));
                    } else if (e.getMessage().contains("email") && e.getMessage().contains("already taken")) {
                        ToastUtil.T_Error(UserRegisterActivity.this, getString(R.string.register_fail_email));
                    } else {
                        ToastUtil.T_Error(UserRegisterActivity.this, getString(R.string.register_fail));
                        LogUtil.e(this.getClass(), "注册失败---coed:" + e.getErrorCode() + ";message:" + e.getMessage());
                    }
                }
            }
        });
    }

}
