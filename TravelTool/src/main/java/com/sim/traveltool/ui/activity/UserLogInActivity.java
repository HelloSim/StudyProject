package com.sim.traveltool.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.sim.baselibrary.base.BaseActivity;
import com.sim.baselibrary.bean.EventMessage;
import com.sim.baselibrary.utils.LogUtil;
import com.sim.baselibrary.utils.SPUtil;
import com.sim.baselibrary.utils.ToastUtil;
import com.sim.traveltool.AppHelper;
import com.sim.traveltool.R;
import com.sim.traveltool.db.bean.User;
import com.sim.traveltool.ui.view.TitleView;

import org.greenrobot.eventbus.EventBus;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * @Auther Sim
 * @Time 2020/4/29 1:05
 * @Description 登陆页面
 */
public class UserLogInActivity extends BaseActivity {

    private Context context;

    private TitleView titleView;
    private EditText etUserName;
    private EditText etPassword;
    private Button btnLogIn;

    //更多弹窗
    private PopupWindow morePopupWindow;//弹窗
    private View moreLayout;//布局
    private Button btnRegistered;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_user_login;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {
        titleView = findViewById(R.id.titleView);
        etUserName = findViewById(R.id.et_user_name);
        etPassword = findViewById(R.id.et_password);
        btnLogIn = findViewById(R.id.btn_log_in);
        setViewClick(btnLogIn);
        titleView.setClickListener(new TitleView.ClickListener() {
            @Override
            public void left(View leftView) {
                finish();
            }

            @Override
            public void right(View right) {
                morePopupWindow.showAsDropDown(right, 0, 0);
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
        moreLayout = inflater.inflate(R.layout.view_popup_login_more, null);

        morePopupWindow = showPopupWindow(moreLayout, 150, 100);
        btnRegistered = moreLayout.findViewById(R.id.btn_registered);
        setViewClick(btnRegistered);
    }

    @Override
    public void onMultiClick(View view) {
        if (view == btnLogIn) {
            if (etUserName.getText().toString().length() > 0 && etPassword.getText().toString().length() > 0) {
                loginByAccount();
            } else {
                if (etUserName.getText().toString().length() > 0) {
                    ToastUtil.T_Info(context, "请输入密码！");
                } else {
                    ToastUtil.T_Info(context, "请输入用户名！");
                }
            }
        } else if (view == btnRegistered) {
            morePopupWindow.dismiss();
            Intent intent = new Intent(this, UserRegisterActivity.class);
            startActivity(intent);
        } else {
            super.onMultiClick(view);
        }
    }

    /**
     * 账号密码登录
     */
    private void login() {
        final User user = new User();
        user.setUsername(etUserName.getText().toString());
        user.setPassword(etPassword.getText().toString());
        user.login(new SaveListener<User>() {
            @Override
            public void done(User bmobUser, BmobException e) {
                if (e == null) {
                    ToastUtil.T_Success(context, "登录成功！");
                    EventBus.getDefault().post(new EventMessage(AppHelper.USER_IsLogIn));
                    SPUtil.put(context, AppHelper.userSpName, AppHelper.userSpStateKey, true);
                    finish();
                } else {
                    if (e.getMessage().contains("username or password incorrect")) {
                        ToastUtil.T_Error(context, "用户名或密码不正确！");
                    } else {
                        ToastUtil.T_Error(context, "登录出错！");
                        LogUtil.e(getClass(), "登录出错---code:" + e.getErrorCode() + ";message:" + e.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 账号/手机号码/邮箱+密码登录
     */
    private void loginByAccount() {
        //此处替换为你的用户名密码
        BmobUser.loginByAccount(etUserName.getText().toString(), etPassword.getText().toString(), new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    ToastUtil.T_Success(context, "登录成功！");
                    EventBus.getDefault().post(new EventMessage(AppHelper.USER_IsLogIn));
                    SPUtil.put(context, AppHelper.userSpName, AppHelper.userSpStateKey, true);
                    finish();
                } else {
                    if (e.getMessage().contains("username or password incorrect")) {
                        ToastUtil.T_Error(context, "用户名或密码不正确！");
                    } else {
                        ToastUtil.T_Error(context, "登录出错！");
                        LogUtil.e(getClass(), "登录出错---code:" + e.getErrorCode() + ";message:" + e.getMessage());
                    }
                }
            }
        });
    }

}
