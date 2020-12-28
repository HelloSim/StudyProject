package com.sim.traveltool.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sim.baselibrary.base.BaseActivity;
import com.sim.baselibrary.constant.Constant;
import com.sim.baselibrary.utils.LogUtil;
import com.sim.baselibrary.utils.SPUtil;
import com.sim.traveltool.R;
import com.sim.traveltool.bean.UserInfo;
import com.sim.traveltool.internet.APIFactory;

import rx.Subscriber;

/**
 * @Auther Sim
 * @Time 2020/4/29 1:05
 * @Description 登陆页面
 */
public class UserLogInActivity extends BaseActivity {

    ImageView back;
    EditText etUserName;
    EditText etPassword;
    Button btnRegistered;
    Button btnLogIn;

    private String spName = "userState";
    private String spStateKey = "isLogIn";
    private String spUserInfoKey = "userInfo";

    private UserInfo userInfoBean;

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
            Intent intent = new Intent(this, UserRegisterActivity.class);
            startActivity(intent);
        } else if (view == btnLogIn) {
            if (etUserName.getText().toString().length() > 0 && etPassword.getText().toString().length() > 0) {
                logIn();
            } else {
                if (etUserName.getText().toString().length() > 0) {
                    Toast.makeText(this, "请输入密码！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "请输入用户名！", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onMultiClick(view);
        }
    }

    /**
     * 用户登陆的网络请求
     */
    private void logIn() {
        APIFactory.getInstance().logIn(new Subscriber<UserInfo>() {
            @Override
            public void onCompleted() {
                if (userInfoBean.getCode() == 200) {
                    SPUtil.put(UserLogInActivity.this, spName, spStateKey, true);
                    SPUtil.put(UserLogInActivity.this, spName, "etPassword", etPassword.getText().toString());
                    SPUtil.put(UserLogInActivity.this, spName, spUserInfoKey, new Gson().toJson(userInfoBean));
                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(UserLogInActivity.this, userInfoBean.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(UserLogInActivity.class, "用户登陆出错: " + e);
            }

            @Override
            public void onNext(UserInfo userInfo) {
                userInfoBean = userInfo;
            }
        }, Constant.API_KEY, etUserName.getText().toString(), etPassword.getText().toString());
    }

}
