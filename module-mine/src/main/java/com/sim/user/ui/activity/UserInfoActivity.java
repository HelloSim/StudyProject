package com.sim.user.ui.activity;

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
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sim.basicres.base.BaseActivity;
import com.sim.basicres.bean.EventMessage;
import com.sim.basicres.callback.DialogInterface;
import com.sim.basicres.constant.AppHelper;
import com.sim.basicres.constant.ArouterUrl;
import com.sim.basicres.utils.ToastUtil;
import com.sim.basicres.views.TitleView;
import com.sim.user.R;
import com.sim.user.bean.User;
import com.sim.user.utils.CallBack;

import org.greenrobot.eventbus.EventBus;

@Route(path = ArouterUrl.Mine.user_activity_info)
public class UserInfoActivity extends BaseActivity {

    private Context context;

    private TitleView titleView;
    private LinearLayout parent;

    private RelativeLayout rlUserName, rlPassword, rlMobilePhoneNumber;

    private TextView tvUserName, tvMobilePhoneNumber;
    private Button btnLogOut;

    private PopupWindow updateUserNamePopupWindow;//弹窗
    private View updateUserNameLayout;//布局
    private EditText etNewUserName;
    private Button btnUserNameCancel, btnUserNameConfirm;

    @Override
    protected int getLayoutRes() {
        return R.layout.mine_activity_info;
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
        tvUserName.setText(User.getUsername());
        tvMobilePhoneNumber.setText(User.getMobilePhoneNumber());

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        updateUserNameLayout = inflater.inflate(R.layout.mine_view_popup_update_name, null);
        updateUserNamePopupWindow = showPopupWindow(updateUserNameLayout, 350, 230);
        etNewUserName = updateUserNameLayout.findViewById(R.id.et_new_user_name);
        btnUserNameCancel = updateUserNameLayout.findViewById(R.id.btn_user_name_cancel);
        btnUserNameConfirm = updateUserNameLayout.findViewById(R.id.btn_user_name_confirm);
        etNewUserName.setText(User.getUsername());
        setViewClick(btnUserNameCancel, btnUserNameConfirm);
    }

    @Override
    public void onMultiClick(View view) {
        if (view == btnLogOut) {
            showDialog("退出登录", "是否确认退出？", "确认", "取消",
                    new DialogInterface() {
                        @Override
                        public void sureOnClick() {
                            User.logout();
                            EventBus.getDefault().post(new EventMessage(AppHelper.USER_noLogIn));
                            finish();
                        }

                        @Override
                        public void cancelOnClick() {

                        }
                    });
        } else if (view == rlUserName) {
            updateUserNamePopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        } else if (view == btnUserNameCancel) {
            etNewUserName.setText(User.getUsername());
            updateUserNamePopupWindow.dismiss();
        } else if (view == btnUserNameConfirm) {
            User.updateUserInfo(etNewUserName.getText().toString(), new CallBack() {
                @Override
                public void success(Object... values) {
                    updateUserNamePopupWindow.dismiss();
                    tvUserName.setText(User.getUsername());
                    etNewUserName.setText(User.getUsername());
                }

                @Override
                public void fail(String values) {
                    ToastUtil.toast(context, "修改失败:" + values);
                }
            });
        } else if (view == rlPassword) {
            ARouter.getInstance().build(ArouterUrl.Mine.user_activity_updatepws).navigation();
        } else if (view == rlMobilePhoneNumber) {

        } else {
            super.onMultiClick(view);
        }
    }

}
