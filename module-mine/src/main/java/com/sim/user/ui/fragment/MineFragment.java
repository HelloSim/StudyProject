package com.sim.user.ui.fragment;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sim.basicres.base.BaseFragment;
import com.sim.basicres.bean.EventMessage;
import com.sim.basicres.constant.AppHelper;
import com.sim.basicres.constant.ArouterUrl;
import com.sim.basicres.utils.ToastUtil;
import com.sim.basicres.views.TitleView;
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
@Route(path = ArouterUrl.Mine.user_fragment)
public class MineFragment extends BaseFragment {

    private User user;

    private TitleView titleView;
    private TextView tvUserName;
    private LinearLayout userLogin, userCollect, updateVersion, project, author;

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.mine_fragment_mine;
    }

    @Override
    protected void bindViews(View view) {
        user = BmobUser.getCurrentUser(User.class);
        EventBus.getDefault().register(this);

        titleView = view.findViewById(R.id.titleView);
        titleView.setRightClickListener(new TitleView.RightClickListener() {
            @Override
            public void onClick(View rightView) {
                ToastUtil.toast(getContext(), "未开发");
            }
        });

        tvUserName = view.findViewById(R.id.tv_user_name);

        userLogin = view.findViewById(R.id.user_login);
        userCollect = view.findViewById(R.id.user_collect);
        updateVersion = view.findViewById(R.id.update_version);
        project = view.findViewById(R.id.project);
        author = view.findViewById(R.id.author);
        setViewClick(userLogin, userCollect, updateVersion, project, author);
    }

    @Override
    protected void initView(View view) {
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

    @Override
    public void onMultiClick(View view) {
        if (view == userLogin) {
            if (user != null) {
                ARouter.getInstance().build(ArouterUrl.Mine.user_activity_info).navigation();
            } else {
                ARouter.getInstance().build(ArouterUrl.Mine.user_activity_login).navigation();
            }
        } else if (view == userCollect) {
            if (user != null) {
                ARouter.getInstance().build(ArouterUrl.Mine.user_activity_collect).navigation();
            } else {
                ToastUtil.toast(getContext(), "未登录");
            }
        } else if (view == updateVersion) {
            ToastUtil.toast(getContext(), "未开发");
        } else if (view == project) {
            ARouter.getInstance()
                    .build(ArouterUrl.Web.web_activity)
                    .withSerializable("webUrl", "https://github.com/HelloSim/TravelTool")
                    .navigation();
        } else if (view == author) {
            ARouter.getInstance()
                    .build(ArouterUrl.Web.web_activity)
                    .withSerializable("webUrl", "https://github.com/HelloSim")
                    .navigation();
        } else {
            super.onMultiClick(view);
        }
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
