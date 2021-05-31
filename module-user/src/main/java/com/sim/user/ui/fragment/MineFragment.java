package com.sim.user.ui.fragment;

import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sim.basicres.base.BaseFragment;
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
 * @ time： 2021/5/31 15:39
 * @ description：
 */
@Route(path = ArouterUrl.user_fragment)
public class MineFragment extends BaseFragment {

    private User user;

    private TextView tvUserName;

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.user_fragment_mine;
    }

    @Override
    protected void bindViews(View view) {
        user = BmobUser.getCurrentUser(User.class);
        EventBus.getDefault().register(this);

    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.rl_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    ARouter.getInstance().build(ArouterUrl.user_activity_info).navigation();
                } else {
                    ARouter.getInstance().build(ArouterUrl.user_activity_login).navigation();
                }
            }
        });
        view.findViewById(R.id.rl_user_collect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    ARouter.getInstance().build(ArouterUrl.user_activity_collect).navigation();
                } else {
                    ToastUtil.toast(getContext(), "未登录");
                }
            }
        });
        view.findViewById(R.id.rl_update_version).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.toast(getContext(), "未开发");
            }
        });
        view.findViewById(R.id.rl_user_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.toast(getContext(), "未开发");
            }
        });

        tvUserName = view.findViewById(R.id.tv_user_name);
        if (user != null) {
            tvUserName.setText(user.getUsername());
        } else {
            tvUserName.setText("用户登录");
        }
    }

    @Override
    protected void initData() {
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
