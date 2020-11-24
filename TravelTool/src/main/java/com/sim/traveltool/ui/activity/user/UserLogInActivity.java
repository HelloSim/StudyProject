package com.sim.traveltool.ui.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.sim.traveltool.R;
import com.sim.traveltool.bean.user.UserInfo;
import com.sim.traveltool.constant.Constant;
import com.sim.traveltool.ui.activity.BaseActivity;
import com.sim.traveltool.utils.SPUtil;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * 登陆页面
 * Created by Sim on 2020/4/29
 */
public class UserLogInActivity extends BaseActivity {

    private Context context;

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.et_user_name)
    EditText userName;
    @BindView(R.id.et_password)
    EditText password;
    @BindView(R.id.btn_registered)
    Button registered;
    @BindView(R.id.btn_log_in)
    Button logIn;

    private String spName = "userState";
    private String spStateKey = "isLogIn";
    private String spUserInfoKey = "userInfo";

    private UserInfo userInfoBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_log_in);
        ButterKnife.bind(this);
        context = this;
    }

    @OnClick({R.id.back, R.id.btn_registered, R.id.btn_log_in})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_registered:
                Intent intent = new Intent(context, UserRegisteredActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_log_in:
                if (userName.getText().toString().length() > 0 && password.getText().toString().length() > 0) {
                    logIn();
                } else {
                    if (userName.getText().toString().length() > 0) {
                        Toast.makeText(context, "请输入密码！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "请输入用户名！", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    /**
     * 用户登陆的网络请求
     */
    private void logIn() {
        retrofitUtil.logIn(new Subscriber<UserInfo>() {
            @Override
            public void onCompleted() {
                if (userInfoBean.getCode() == 200) {
                    SPUtil.put(context, spName, spStateKey, true);
                    SPUtil.put(context, spName, "password", password.getText().toString());
                    SPUtil.put(context, spName, spUserInfoKey, new Gson().toJson(userInfoBean));
                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(context, userInfoBean.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d("Sim", "getWangYiNewOnError: " + e);
            }

            @Override
            public void onNext(UserInfo userInfo) {
                userInfoBean = userInfo;
            }
        }, Constant.API_KEY, userName.getText().toString(), password.getText().toString());
    }

}
