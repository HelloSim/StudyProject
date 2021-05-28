package com.sim.user.ui.view;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sim.basicres.bean.EventMessage;
import com.sim.basicres.constant.AppHelper;
import com.sim.basicres.constant.ArouterUrl;
import com.sim.basicres.utils.ToastUtil;
import com.sim.bean.User;
import com.sim.user.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.bmob.v3.BmobUser;

/**
 * @ author: Sim
 * @ time： 2021/5/25 14:30
 * @ description：
 */
//@Route(path = ArouterUrl.user_view)
public class UserView extends RelativeLayout {

    private Context context;

    private User user;

    private TextView tvUserName;

    public UserView(Context context) {
        super(context);
        this.context = context;
        try {
            user = BmobUser.getCurrentUser(User.class);
        } catch (Exception e) {

        }
        EventBus.getDefault().register(this);
        initView();
    }

    public void initView() {
        View.inflate(context, R.layout.user_view_left_bar, this);
        findViewById(R.id.rl_user).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    ARouter.getInstance().build(ArouterUrl.user_activity_info).navigation();
                } else {
                    ARouter.getInstance().build(ArouterUrl.user_activity_login).navigation();
                }
            }
        });
        findViewById(R.id.rl_user_collect).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    ARouter.getInstance().build(ArouterUrl.user_activity_collect).navigation();
                } else {
                    ToastUtil.toast(context, "未登录");
                }
            }
        });
        findViewById(R.id.rl_update_version).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.toast(context, "未开发");
            }
        });
        findViewById(R.id.rl_user_setting).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.toast(context, "未开发");
            }
        });

        tvUserName = findViewById(R.id.tv_user_name);
        if (user != null) {
            tvUserName.setText(user.getUsername());
        } else {
            tvUserName.setText("用户登录");
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 接收消息事件
     *
     * @param eventMessage
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventMessage eventMessage) {
        if (eventMessage.type == AppHelper.USER_IsLogIn) {
            user = BmobUser.getCurrentUser(User.class);
            User.fetchUserInfo();
            tvUserName.setText(user.getUsername());
        } else if (eventMessage.type == AppHelper.USER_noLogIn) {
            user = null;
            tvUserName.setText("用户登录");
        }
    }

}
