package com.sim.traveltool.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.sim.baselibrary.constant.Constant;
import com.sim.traveltool.R;
import com.sim.traveltool.bean.UserInfo;
import com.sim.traveltool.ui.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * 用户注册页面
 * Created by Sim on 2020/4/29
 */
public class UserRegisterActivity extends BaseActivity {

    private Context context;

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.iv_user_image)
    ImageView ivUserImage;
    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_nike_name)
    EditText etNikeName;
    @BindView(R.id.et_autograph)
    EditText etAutograph;
    @BindView(R.id.btn_registered)
    Button registered;

    private UserInfo userInfoBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        ButterKnife.bind(this);
        context = this;
    }

    @OnClick({R.id.back, R.id.btn_registered})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_registered:
                if (etUserName.getText().toString().length() > 0 && etPassword.getText().toString().length() > 0) {
                    registerUser(etUserName.getText().toString(), etPassword.getText().toString(),
                            null, etNikeName.getText().toString(), etAutograph.getText().toString(), null, null, null, null);
                } else {
                    if (etUserName.getText().toString().length() <= 0) {
                        Toast.makeText(context, "用户名不能为空！", Toast.LENGTH_SHORT).show();
                    } else if (etPassword.getText().toString().length() <= 0) {
                        Toast.makeText(context, "密码不能为空！", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
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
        retrofitUtil.registerUser(new Subscriber<UserInfo>() {
            @Override
            public void onCompleted() {
                Toast.makeText(context, userInfoBean.getMessage(), Toast.LENGTH_SHORT).show();
                if (userInfoBean.getCode() == 200) {
                    finish();
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
        }, Constant.API_KEY, name, passwd, headerImg, nikeName, autograph, phone, email, remarks, vipGrade);
    }

}
