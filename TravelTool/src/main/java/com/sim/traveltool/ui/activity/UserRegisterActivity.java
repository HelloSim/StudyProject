package com.sim.traveltool.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.sim.baselibrary.base.BaseActivity;
import com.sim.baselibrary.constant.Constant;
import com.sim.baselibrary.utils.LogUtil;
import com.sim.baselibrary.utils.ToastUtil;
import com.sim.traveltool.R;
import com.sim.traveltool.bean.UserInfo;
import com.sim.traveltool.internet.APIFactory;

import rx.Subscriber;

/**
 * @Auther Sim
 * @Time 2020/4/29 1:05
 * @Description 用户注册页面
 */
public class UserRegisterActivity extends BaseActivity {

    ImageView back;
    EditText etUserName;
    EditText etPassword;
    EditText etNikeName;
    EditText etAutograph;
    Button btnRegistered;

    private UserInfo userInfoBean;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_user_register;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {
        back = findViewById(R.id.back);
        etUserName = findViewById(R.id.et_user_name);
        etPassword = findViewById(R.id.et_password);
        etNikeName = findViewById(R.id.et_nike_name);
        etAutograph = findViewById(R.id.et_autograph);
        btnRegistered = findViewById(R.id.btn_registered);
        setViewClick(back, btnRegistered);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onMultiClick(View view) {
        if (view == back) {
            finish();
        } else if (view == btnRegistered) {
            if (etUserName.getText().toString().length() > 0 && etPassword.getText().toString().length() > 0) {
                registerUser(etUserName.getText().toString(), etPassword.getText().toString(),
                        null, etNikeName.getText().toString(), etAutograph.getText().toString(), null, null, null, null);
            } else {
                if (etUserName.getText().toString().length() <= 0) {
                    ToastUtil.T_Info(UserRegisterActivity.this, "用户名不能为空！");
                } else if (etPassword.getText().toString().length() <= 0) {
                    ToastUtil.T_Info(UserRegisterActivity.this, "密码不能为空！");
                }
            }
        } else {
            super.onMultiClick(view);
        }
    }

    /**
     * 注册用户的网络请求
     *
     * @param name
     * @param passwd
     * @param headerImg
     * @param nikeName
     * @param autograph
     * @param phone
     * @param email
     * @param remarks
     * @param vipGrade
     */
    private void registerUser(String name, String passwd, String headerImg, String nikeName, String autograph, String phone, String email, String remarks, String vipGrade) {
        APIFactory.getInstance().registerUser(new Subscriber<UserInfo>() {
            @Override
            public void onCompleted() {
                if (userInfoBean.getCode() == 200) {
                    ToastUtil.T_Success(UserRegisterActivity.this, "注册成功！");
                    finish();
                } else ToastUtil.T_Error(UserRegisterActivity.this, "注册失败！");
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.T_Error(UserRegisterActivity.this, "注册失败！");
                LogUtil.e(UserRegisterActivity.class, "注册用户出错: " + e);
            }

            @Override
            public void onNext(UserInfo userInfo) {
                userInfoBean = userInfo;
            }
        }, Constant.API_KEY, name, passwd, headerImg, nikeName, autograph, phone, email, remarks, vipGrade);
    }

}
