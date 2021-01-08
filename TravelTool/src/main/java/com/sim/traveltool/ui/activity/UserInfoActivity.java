package com.sim.traveltool.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sim.baselibrary.base.BaseActivity;
import com.sim.baselibrary.bean.EventMessage;
import com.sim.baselibrary.callback.DialogInterface;
import com.sim.baselibrary.callback.SuccessOrFailListener;
import com.sim.baselibrary.utils.LogUtil;
import com.sim.baselibrary.utils.RegexUtil;
import com.sim.baselibrary.utils.SPUtil;
import com.sim.baselibrary.utils.ToastUtil;
import com.sim.traveltool.AppHelper;
import com.sim.traveltool.R;
import com.sim.traveltool.db.bean.User;
import com.sim.traveltool.ui.view.TitleView;

import org.greenrobot.eventbus.EventBus;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @Auther Sim
 * @Time 2020/4/29 1:05
 * @Description 显示用户信息的页面
 */
public class UserInfoActivity extends BaseActivity {

    private Context context;

    private TitleView titleView;
    private LinearLayout parent;

    private RelativeLayout rlUserName;
    private RelativeLayout rlPassword;
    private RelativeLayout rlMobilePhoneNumber;
    private RelativeLayout rlEmail;

    private TextView tvUserName;
    //    private TextView tvPassword;
    private TextView tvMobilePhoneNumber;
    private TextView tvEmail;
    private Button btnLogOut;

    private PopupWindow updateEmailPopupWindow;//弹窗
    private View updateEmailLayout;//布局
    private TextView tvOldEmail;
    private TextView tvOldEmailVerified;
    private ImageView ivRefresh;
    private EditText etNewEmail;
    private Button btnEmailCancel;
    private Button btnEmailConfirm;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {
        titleView = findViewById(R.id.titleView);
        parent = findViewById(R.id.parent);
        rlUserName = findViewById(R.id.rl_user_name);
        rlPassword = findViewById(R.id.rl_password);
        rlMobilePhoneNumber = findViewById(R.id.rl_mobile_phone_number);
        rlEmail = findViewById(R.id.rl_email);
        tvUserName = findViewById(R.id.tv_user_name);
        tvMobilePhoneNumber = findViewById(R.id.tv_mobile_phone_number);
        tvEmail = findViewById(R.id.tv_email);
        btnLogOut = findViewById(R.id.btn_log_out);
        setViewClick(rlUserName, rlPassword, rlMobilePhoneNumber, rlEmail, btnLogOut);
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
        tvUserName.setText(BmobUser.getCurrentUser(User.class).getUsername());
        tvMobilePhoneNumber.setText(BmobUser.getCurrentUser(User.class).getMobilePhoneNumber());
        tvEmail.setText(BmobUser.getCurrentUser(User.class).getEmail());

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        updateEmailLayout = inflater.inflate(R.layout.view_popup_update_email, null);
        updateEmailPopupWindow = showPopupWindow(updateEmailLayout, 350, 330);
        tvOldEmail = updateEmailLayout.findViewById(R.id.tv_old_email);
        tvOldEmailVerified = updateEmailLayout.findViewById(R.id.tv_old_email_verified);
        ivRefresh = updateEmailLayout.findViewById(R.id.iv_refresh);
        etNewEmail = updateEmailLayout.findViewById(R.id.et_new_email);
        btnEmailCancel = updateEmailLayout.findViewById(R.id.btn_email_cancel);
        btnEmailConfirm = updateEmailLayout.findViewById(R.id.btn_email_confirm);
        tvOldEmail.setText(BmobUser.getCurrentUser(User.class).getEmail());
        tvOldEmailVerified.setText(BmobUser.getCurrentUser(User.class).getEmailVerified() ? "当前邮箱已验证" : "点击验证邮箱");

        setViewClick(tvOldEmailVerified, ivRefresh, btnEmailCancel, btnEmailConfirm);
    }

    @Override
    public void onMultiClick(View view) {
        if (view == btnLogOut) {
            showDialog("退出登录", "是否确认退出？", "确认", "取消",
                    new DialogInterface() {
                        @Override
                        public void sureOnClick() {
                            BmobUser.logOut();
                            EventBus.getDefault().post(new EventMessage(AppHelper.USER_noLogIn));
                            SPUtil.put(context, AppHelper.userSpName, AppHelper.userSpStateKey, false);
                            finish();
                        }

                        @Override
                        public void cancelOnClick() {

                        }
                    });
        } else if (view == rlUserName) {

        } else if (view == rlPassword) {
            startActivity(new Intent(this, UserUpdatePasswordActivity.class));
        } else if (view == rlMobilePhoneNumber) {

        } else if (view == rlEmail) {
            updateEmailPopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        } else if (view == tvOldEmailVerified) {
            if (tvOldEmailVerified.getText().equals("点击验证邮箱")) {
                emailVerify(new SuccessOrFailListener() {
                    @Override
                    public void success(Object... values) {
                        ToastUtil.T_Success(context, "请求验证邮件成功！请到您的邮箱中进行验证！");
                    }

                    @Override
                    public void fail(Object... values) {
                        ToastUtil.T_Error(context, "请求验证邮件失败！");
                    }
                });
            }
        } else if (view == ivRefresh) {
            if (tvOldEmailVerified.getText().equals("点击验证邮箱")) {
                fetchUserInfo();
            }
        } else if (view == btnEmailCancel) {
            updateEmailPopupWindow.dismiss();
        } else if (view == btnEmailConfirm) {
            if (etNewEmail.getText().length() > 0 && !RegexUtil.email(etNewEmail.getText().toString())) {
                ToastUtil.T_Info(context, "请输入正确的电子邮箱！");
            } else {
                updateUserInfo(etNewEmail.getText().toString(), new SuccessOrFailListener() {
                    @Override
                    public void success(Object... values) {
                        ToastUtil.T_Success(context, "修改成功！");
                        updateEmailPopupWindow.dismiss();
                        fetchUserInfo();
                    }

                    @Override
                    public void fail(Object... values) {
                        ToastUtil.T_Error(context, "修改失败！");
                    }
                });
            }
        } else {
            super.onMultiClick(view);
        }
    }

    /**
     * 同步控制台数据到缓存中
     */
    private void fetchUserInfo() {
        BmobUser.fetchUserInfo(new FetchUserInfoListener<BmobUser>() {
            @Override
            public void done(BmobUser user, BmobException e) {
                if (e == null) {
                    tvUserName.setText(BmobUser.getCurrentUser(User.class).getUsername());
                    tvMobilePhoneNumber.setText(BmobUser.getCurrentUser(User.class).getMobilePhoneNumber());
                    tvEmail.setText(BmobUser.getCurrentUser(User.class).getEmail());
                    tvOldEmail.setText(BmobUser.getCurrentUser(User.class).getEmail());
                    tvOldEmailVerified.setText(BmobUser.getCurrentUser(User.class).getEmailVerified() ? "当前邮箱已验证" : "点击验证邮箱");
                    LogUtil.d(getClass(), "更新用户本地缓存信息成功");
                } else {
                    LogUtil.e(getClass(), "更新用户本地缓存信息失败;message:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 修改邮箱
     *
     * @param email
     */
    private void updateUserInfo(String email, SuccessOrFailListener successOrFailListener) {
        User user = BmobUser.getCurrentUser(User.class);
        user.setEmail(email);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    successOrFailListener.success();
                } else {
                    successOrFailListener.fail();
                    LogUtil.e(getClass(), "修改用户信息失败---code:" + e.getErrorCode() + ";message:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 发送验证邮件
     */
    private void emailVerify(SuccessOrFailListener successOrFailListener) {
        User user = BmobUser.getCurrentUser(User.class);
        BmobUser.requestEmailVerify(user.getEmail(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    successOrFailListener.success();
                } else {
                    successOrFailListener.fail();
                    LogUtil.e(getClass(), "修改用户信息失败---code:" + e.getErrorCode() + ";message:" + e.getMessage());
                }
            }
        });
    }

}
