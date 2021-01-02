package com.sim.traveltool.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.sim.baselibrary.base.BaseActivity;
import com.sim.baselibrary.bean.EventMessage;
import com.sim.baselibrary.utils.LogUtil;
import com.sim.baselibrary.utils.ToastUtil;
import com.sim.traveltool.AppHelper;
import com.sim.traveltool.R;
import com.sim.traveltool.db.bean.User;

import org.greenrobot.eventbus.EventBus;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * @Auther Sim
 * @Time 2020/4/29 1:05
 * @Description 登陆页面
 */
public class UserLogInActivity extends BaseActivity {

    private ImageView back;
    private EditText etUserName;
    private EditText etPassword;
    private Button btnRegistered;
    private Button btnLogIn;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_user_login;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {
        back = findViewById(R.id.back);
        etUserName = findViewById(R.id.et_user_name);
        etPassword = findViewById(R.id.et_password);
        btnRegistered = findViewById(R.id.btn_registered);
        btnLogIn = findViewById(R.id.btn_log_in);
        setViewClick(back, btnRegistered, btnLogIn);
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
            Intent intent = new Intent(this, UserRegisterActivity.class);
            startActivity(intent);
        } else if (view == btnLogIn) {
            if (etUserName.getText().toString().length() > 0 && etPassword.getText().toString().length() > 0) {
                loginByAccount();
            } else {
                if (etUserName.getText().toString().length() > 0) {
                    ToastUtil.T_Info(UserLogInActivity.this, "请输入密码！");
                } else {
                    ToastUtil.T_Info(UserLogInActivity.this, "请输入用户名！");
                }
            }
        } else {
            super.onMultiClick(view);
        }
    }

    /**
     * 账号密码登录
     */
    private void loginByAccount() {
        BmobUser.loginByAccount(etUserName.getText().toString(), etPassword.getText().toString(), new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    ToastUtil.T_Success(UserLogInActivity.this, "登录成功！");
                    EventBus.getDefault().post(new EventMessage(AppHelper.USER_IsLogIn));
                    finish();
                } else {
                    ToastUtil.T_Error(UserLogInActivity.this, "登录出错！");
                    LogUtil.e(this.getClass(), "登录出错---code:" + e.getErrorCode() + ";message:" + e.getMessage());
                }
            }
        });
    }

}
