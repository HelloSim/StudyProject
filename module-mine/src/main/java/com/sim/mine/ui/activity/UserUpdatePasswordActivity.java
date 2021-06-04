package com.sim.mine.ui.activity;

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

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sim.basicres.base.BaseActivity;
import com.sim.basicres.callback.DialogInterface;
import com.sim.basicres.constant.ArouterUrl;
import com.sim.basicres.utils.ToastUtil;
import com.sim.basicres.views.TitleView;
import com.sim.mine.R;
import com.sim.user.bean.User;
import com.sim.user.utils.SMSUtil;
import com.sim.user.utils.CallBack;

@Route(path = ArouterUrl.Mine.user_activity_updatepws)
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
        return R.layout.mine_activity_update_password;
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
        updatePasswordLayout = inflater.inflate(R.layout.mine_view_popup_update_password, null);
        updatePasswordPopupWindow = showPopupWindow(updatePasswordLayout, 300, 330);
        etOldPassword = updatePasswordLayout.findViewById(R.id.et_old_password);
        etNewPassword = updatePasswordLayout.findViewById(R.id.et_new_password);
        etNewPasswordAgain = updatePasswordLayout.findViewById(R.id.et_new_password_again);
        btnPasswordCancel = updatePasswordLayout.findViewById(R.id.btn_password_cancel);
        btnPasswordConfirm = updatePasswordLayout.findViewById(R.id.btn_password_confirm);

        resetPasswordLayout = inflater.inflate(R.layout.mine_view_popup_reset_password, null);
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
                            SMSUtil.requestSMSCode(new CallBack() {
                                @Override
                                public void success(Object... values) {
                                    ToastUtil.toast(context, "发送验证码成功！");
                                    resetPasswordPopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
                                }

                                @Override
                                public void fail(String values) {
                                    ToastUtil.toast(context, "发送失败：" + values);
                                }
                            });
                        }

                        @Override
                        public void cancelOnClick() {

                        }
                    });
        } else if (view == btnPasswordCancel) {
            updatePasswordPopupWindow.dismiss();
        } else if (view == btnPasswordConfirm) {
            if (etOldPassword.getText().length() > 0 && etNewPassword.getText().length() > 0 && etNewPasswordAgain.getText().length() > 0) {
                if (etNewPassword.getText().toString().equals(etNewPasswordAgain.getText().toString())) {
                    User.updatePassword(etOldPassword.getText().toString(), etNewPassword.getText().toString(), new CallBack() {
                        @Override
                        public void success(Object... values) {
                            updatePasswordPopupWindow.dismiss();
                            ToastUtil.toast(context,"修改成功！");
                        }

                        @Override
                        public void fail(String values) {
                            ToastUtil.toast(context, "修改失败：" + values);
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
                User.resetPasswordBySMSCode(etSMSCode.getText().toString(), etNewPasswordPhone.getText().toString(), new CallBack() {
                    @Override
                    public void success(Object... values) {
                        resetPasswordPopupWindow.dismiss();
                        ToastUtil.toast(context,"修改成功！");
                    }

                    @Override
                    public void fail(String values) {
                        ToastUtil.toast(context, "修改失败：" + values);
                    }
                });
            } else {
                ToastUtil.toast(context, "密码不能为空！");
            }
        } else {
            super.onMultiClick(view);
        }
    }

}
