package com.sim.mine.ui.fragment;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sim.basicres.base.BaseFragment;
import com.sim.basicres.constant.ArouterUrl;
import com.sim.basicres.utils.ToastUtil;
import com.sim.basicres.views.TitleView;
import com.sim.basicres.views.WaveView;
import com.sim.mine.R;
import com.sim.user.bean.User;

@Route(path = ArouterUrl.Mine.user_fragment)
public class MineFragment extends BaseFragment {

    private WaveView waveview;
    private LinearLayout llHead;
    private TitleView titleView;
    private TextView tvUserName, tvId;
    private LinearLayout userLogin, userCollect, updateVersion, project, author;

    @Override
    protected int getLayoutRes() {
        return R.layout.mine_fragment_mine;
    }

    @Override
    protected void bindViews(View view) {
        titleView = view.findViewById(R.id.titleView);
        titleView.setRightClickListener(new TitleView.RightClickListener() {
            @Override
            public void onClick(View rightView) {
                ToastUtil.toast(getContext(), "未开发");
            }
        });

        llHead = view.findViewById(R.id.llHead);
        waveview = view.findViewById(R.id.waveview);

        tvUserName = view.findViewById(R.id.tv_user_name);
        tvId = view.findViewById(R.id.tvId);

        userLogin = view.findViewById(R.id.user_login);
        userCollect = view.findViewById(R.id.user_collect);
        updateVersion = view.findViewById(R.id.update_version);
        project = view.findViewById(R.id.project);
        author = view.findViewById(R.id.author);
        setViewClick(userLogin, userCollect, updateVersion, project, author);
    }

    @Override
    protected void initView(View view) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llHead.getLayoutParams();
        waveview.setOnWaveAnimationListener(y -> {
            layoutParams.setMargins(0, (int) y, 0, 0);
            llHead.setLayoutParams(layoutParams);
        });
        if (User.isLogin()) {
            tvUserName.setText(User.getUsername());
            tvId.setText("ID:" + User.getMobilePhoneNumber());
        } else {
            tvUserName.setText("用户登录");
            tvId.setText("");
        }
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onResume() {
        super.onResume();
        if (User.isLogin()) {
            tvUserName.setText(User.getUsername());
            tvId.setText("ID:" + User.getMobilePhoneNumber());
        } else {
            tvUserName.setText("用户登录");
            tvId.setText("");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onMultiClick(View view) {
        if (view == userLogin) {
            if (User.isLogin()) {
                ARouter.getInstance().build(ArouterUrl.Mine.user_activity_info).navigation();
            } else {
                ARouter.getInstance().build(ArouterUrl.Mine.user_activity_login).navigation();
            }
        } else if (view == userCollect) {
            if (User.isLogin()) {
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

}
