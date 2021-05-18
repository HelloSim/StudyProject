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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sim.baselibrary.base.BaseActivity;
import com.sim.baselibrary.bean.EventMessage;
import com.sim.baselibrary.callback.DialogInterface;
import com.sim.baselibrary.callback.SuccessOrFailListener;
import com.sim.baselibrary.utils.LogUtil;
import com.sim.baselibrary.utils.SPUtil;
import com.sim.baselibrary.utils.ToastUtil;
import com.sim.traveltool.AppHelper;
import com.sim.traveltool.R;
import com.sim.traveltool.bean.db.User;
import com.sim.traveltool.ui.view.TitleView;

import org.greenrobot.eventbus.EventBus;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author Sim --- 显示用户信息的页面
 */
public class UserInfoActivity extends BaseActivity {

    private Context context;

    private TitleView titleView;
    private LinearLayout parent;

    private RelativeLayout rlUserName,rlPassword,rlMobilePhoneNumber;

    private TextView tvUserName,tvMobilePhoneNumber;
    private Button btnLogOut;

    private PopupWindow updateUserNamePopupWindow;//弹窗
    private View updateUserNameLayout;//布局
    private EditText etNewUserName;
    private Button btnUserNameCancel,btnUserNameConfirm;

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
        tvUserName = findViewById(R.id.tv_user_name);
        tvMobilePhoneNumber = findViewById(R.id.tv_mobile_phone_number);
        btnLogOut = findViewById(R.id.btn_log_out);
        setViewClick(rlUserName, rlPassword, rlMobilePhoneNumber, btnLogOut);
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

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        updateUserNameLayout = inflater.inflate(R.layout.view_popup_update_user_name, null);
        updateUserNamePopupWindow = showPopupWindow(updateUserNameLayout, 350, 230);
        etNewUserName = updateUserNameLayout.findViewById(R.id.et_new_user_name);
        btnUserNameCancel = updateUserNameLayout.findViewById(R.id.btn_user_name_cancel);
        btnUserNameConfirm = updateUserNameLayout.findViewById(R.id.btn_user_name_confirm);
        etNewUserName.setText(BmobUser.getCurrentUser(User.class).getUsername());
        setViewClick(btnUserNameCancel, btnUserNameConfirm);
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
            updateUserNamePopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        } else if (view == btnUserNameCancel) {
            etNewUserName.setText(BmobUser.getCurrentUser(User.class).getUsername());
            updateUserNamePopupWindow.dismiss();
        } else if (view == btnUserNameConfirm) {
            User.fetchUserInfo();
            updateUserInfo(etNewUserName.getText().toString(), new SuccessOrFailListener() {
                @Override
                public void success(Object... values) {
                    updateUserNamePopupWindow.dismiss();
                    tvUserName.setText(BmobUser.getCurrentUser(User.class).getUsername());
                    etNewUserName.setText(BmobUser.getCurrentUser(User.class).getUsername());
                    EventBus.getDefault().post(new EventMessage(AppHelper.USER_IsLogIn));
                }

                @Override
                public void fail(Object... values) {
                    ToastUtil.toast(context, "修改失败！");
                }
            });
        } else if (view == rlPassword) {
            startActivity(new Intent(this, UserUpdatePasswordActivity.class));
        } else if (view == rlMobilePhoneNumber) {

        } else {
            super.onMultiClick(view);
        }
    }

    /**
     * 修改邮箱
     *
     * @param userName
     */
    private void updateUserInfo(String userName, SuccessOrFailListener successOrFailListener) {
        User user = BmobUser.getCurrentUser(User.class);
        user.setUsername(userName);
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

}
