package com.sim.traveltool.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sim.baselibrary.constant.Constant;
import com.sim.baselibrary.utils.LogUtil;
import com.sim.baselibrary.utils.SPUtil;
import com.sim.traveltool.R;
import com.sim.traveltool.base.AppActivity;
import com.sim.traveltool.bean.UserInfo;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * @Auther Sim
 * @Time 2020/4/29 1:05
 * @Description 登陆页面
 */
public class UserLogInActivity extends AppActivity {

    @BindView(R.id.et_user_name)
    EditText userName;
    @BindView(R.id.et_password)
    EditText password;

    private String spName = "userState";
    private String spStateKey = "isLogIn";
    private String spUserInfoKey = "userInfo";

    private UserInfo userInfoBean;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_user_login;
    }

    /**
     * 用户登陆的网络请求
     */
    private void logIn() {
        retrofitUtil.logIn(new Subscriber<UserInfo>() {
            @Override
            public void onCompleted() {
                if (userInfoBean.getCode() == 200) {
                    SPUtil.put(UserLogInActivity.this, spName, spStateKey, true);
                    SPUtil.put(UserLogInActivity.this, spName, "password", password.getText().toString());
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
        }, Constant.API_KEY, userName.getText().toString(), password.getText().toString());
    }

    @OnClick({R.id.back, R.id.btn_registered, R.id.btn_log_in})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_registered:
                Intent intent = new Intent(this, UserRegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_log_in:
                if (userName.getText().toString().length() > 0 && password.getText().toString().length() > 0) {
                    logIn();
                } else {
                    if (userName.getText().toString().length() > 0) {
                        Toast.makeText(this, "请输入密码！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "请输入用户名！", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

}
