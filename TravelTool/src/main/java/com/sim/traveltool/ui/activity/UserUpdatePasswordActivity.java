package com.sim.traveltool.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.sim.baselibrary.base.BaseActivity;
import com.sim.baselibrary.callback.DialogInterface;
import com.sim.baselibrary.callback.SuccessOrFailListener;
import com.sim.baselibrary.utils.LogUtil;
import com.sim.baselibrary.utils.ToastUtil;
import com.sim.traveltool.R;
import com.sim.traveltool.bean.db.User;
import com.sim.traveltool.ui.view.TitleView;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author Sim ---
 */
public class UserUpdatePasswordActivity extends BaseActivity {

    private Context context;

    private TitleView titleView;
    private LinearLayout parent;
    private RelativeLayout rlUpdatePasswordByOld, rlResetPasswordByPhone;

    //修改密码弹窗、验证码重置密码弹窗
    private PopupWindow updatePasswordPopupWindow, resetPasswordPopupWindow;//弹窗
    private View updatePasswordLayout, resetPasswordLayout;//布局
    private EditText etOldPassword, etNewPassword, etNewPasswordAgain, etSMSCode, etNewPasswordPhone;
    private Button btnPasswordCancel, btnPasswordConfirm, btnPasswordPhoneCancel, btnPasswordPhoneConfirm;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_user_update_password;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {
        titleView = findViewById(R.id.titleView);
        parent = findViewById(R.id.parent);
        rlUpdatePasswordByOld = findViewById(R.id.rl_update_password_by_old);
        rlResetPasswordByPhone = findViewById(R.id.rl_reset_password_by_phone);
        setViewClick(rlUpdatePasswordByOld, rlResetPasswordByPhone);
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
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        updatePasswordLayout = inflater.inflate(R.layout.view_popup_update_password, null);
        updatePasswordPopupWindow = showPopupWindow(updatePasswordLayout, 300, 330);
        etOldPassword = updatePasswordLayout.findViewById(R.id.et_old_password);
        etNewPassword = updatePasswordLayout.findViewById(R.id.et_new_password);
        etNewPasswordAgain = updatePasswordLayout.findViewById(R.id.et_new_password_again);
        btnPasswordCancel = updatePasswordLayout.findViewById(R.id.btn_password_cancel);
        btnPasswordConfirm = updatePasswordLayout.findViewById(R.id.btn_password_confirm);

        resetPasswordLayout = inflater.inflate(R.layout.view_popup_reset_password, null);
        resetPasswordPopupWindow = showPopupWindow(resetPasswordLayout, 300, 260);
        etSMSCode = resetPasswordLayout.findViewById(R.id.et_SMSCode);
        etNewPasswordPhone = resetPasswordLayout.findViewById(R.id.et_new_password_phone);
        btnPasswordPhoneCancel = resetPasswordLayout.findViewById(R.id.btn_password_phone_cancel);
        btnPasswordPhoneConfirm = resetPasswordLayout.findViewById(R.id.btn_password_phone_confirm);

        setViewClick(btnPasswordCancel, btnPasswordConfirm, btnPasswordPhoneCancel, btnPasswordPhoneConfirm);
    }


    @Override
    public void onMultiClick(View view) {
        if (view == rlUpdatePasswordByOld) {
            etOldPassword.setText("");
            etNewPassword.setText("");
            etNewPasswordAgain.setText("");
            updatePasswordPopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        } else if (view == rlResetPasswordByPhone) {
            showDialog("通过短信验证码重置密码", "发送短信验证码", "确认", "取消",
                    new DialogInterface() {
                        @Override
                        public void sureOnClick() {
                            if (BmobUser.getCurrentUser(User.class).getMobilePhoneNumberVerified()) {
                                requestSMSCode(new SuccessOrFailListener() {
                                    @Override
                                    public void success(Object... values) {
                                        ToastUtil.toast(context, "发送验证码成功！");
                                        resetPasswordPopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
                                    }

                                    @Override
                                    public void fail(Object... values) {
                                        ToastUtil.toast(context, "发送验证码失败！");
                                    }
                                });
                            } else {
                                ToastUtil.toast(context, "手机号码未验证");
                            }
                        }

                        @Override
                        public void cancelOnClick() {

                        }
                    });
        } else if (view == btnPasswordCancel) {
            updatePasswordPopupWindow.dismiss();
        } else if (view == btnPasswordConfirm) {
            updatePasswordPopupWindow.dismiss();
            if (etOldPassword.getText().length() > 0 && etNewPassword.getText().length() > 0 && etNewPasswordAgain.getText().length() > 0) {
                if (etNewPassword.getText().toString().equals(etNewPasswordAgain.getText().toString())) {
                    updatePassword(etOldPassword.getText().toString(), etNewPassword.getText().toString(), new SuccessOrFailListener() {
                        @Override
                        public void success(Object... values) {
                            User.fetchUserInfo();
                        }

                        @Override
                        public void fail(Object... values) {
                            BmobException e = (BmobException) values[0];
                            if (e.getMessage().contains("old password incorrect")) {
                                ToastUtil.toast(context, "密码错误！");
                            } else {
                                ToastUtil.toast(context, "修改失败！");
                            }
                        }
                    });
                } else {
                    ToastUtil.toast(context, "新密码输入不一致！");
                }
            } else {
                ToastUtil.toast(context, "密码不能为空！");
            }
        } else if (view == btnPasswordPhoneCancel) {
            resetPasswordPopupWindow.dismiss();
        } else if (view == btnPasswordPhoneConfirm) {
            if (etNewPasswordPhone.getText().length() > 0) {
                resetPasswordBySMSCode(etSMSCode.getText().toString(), etNewPasswordPhone.getText().toString(), new SuccessOrFailListener() {
                    @Override
                    public void success(Object... values) {
                        resetPasswordPopupWindow.dismiss();
                    }

                    @Override
                    public void fail(Object... values) {
                        ToastUtil.toast(context, "修改失败！");
                    }
                });
            } else {
                ToastUtil.toast(context, "密码不能为空！");
            }
        } else {
            super.onMultiClick(view);
        }
    }

    /**
     * 提供旧密码修改密码
     *
     * @param oldPassword
     * @param newPassword
     * @param successOrFailListener
     */
    private void updatePassword(String oldPassword, String newPassword, SuccessOrFailListener successOrFailListener) {
        BmobUser.updateCurrentUserPassword(oldPassword, newPassword, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    successOrFailListener.success();
                } else {
                    successOrFailListener.fail(e);
                    LogUtil.e(getClass(), "修改用户信息失败---code:" + e.getErrorCode() + ";message:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 发送短信验证码
     *
     * @param successOrFailListener
     */
    private void requestSMSCode(SuccessOrFailListener successOrFailListener) {
        /**
         * TODO template 如果是自定义短信模板，此处替换为你在控制台设置的自定义短信模板名称；如果没有对应的自定义短信模板，则使用默认短信模板，模板名称为空字符串""。
         */
        BmobSMS.requestSMSCode(BmobUser.getCurrentUser(User.class).getMobilePhoneNumber(), "", new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException e) {
                if (e == null) {
                    successOrFailListener.success();
                } else {
                    successOrFailListener.fail();
                    LogUtil.e(getClass(), "发送验证码失败---code:" + e.getErrorCode() + ";message:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 验证码修改密码
     *
     * @param code
     * @param newPassword
     * @param successOrFailListener
     */
    private void resetPasswordBySMSCode(String code, String newPassword, SuccessOrFailListener successOrFailListener) {
        BmobUser.resetPasswordBySMSCode(code, newPassword, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    successOrFailListener.success();
                } else {
                    successOrFailListener.fail(e);
                    LogUtil.e(getClass(), "验证码重置密码失败---code:" + e.getErrorCode() + ";message:" + e.getMessage());
                }
            }
        });
    }

}
