package com.sim.traveltool;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sim.basicres.base.BaseActivity;
import com.sim.basicres.constant.AppHelper;
import com.sim.basicres.constant.ArouterUrl;
import com.sim.basicres.utils.LogUtil;
import com.sim.basicres.utils.SPUtil;
import com.sim.bean.User;
import com.sim.sharedlibrary.base.ServiceFactory;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;

/**
 * @author Sim --- MainActivity
 */
@Route(path = ArouterUrl.app_main)
public class MainActivity extends BaseActivity {

    private LinearLayout left_bar;

    private RadioButton rbBottomBarBus, rbBottomBarWangyi, rbBottomBarRecord;

    private Fragment busFragment, wangyiFragment, recordFragment;
    private View leftView;

    private FragmentManager fragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private Handler handler;
    private int count = 0;

    private User user;

    @Override
    protected int getLayoutRes() {
        return R.layout.app_activity_main;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {
        left_bar = findViewById(R.id.left_bar);
        rbBottomBarBus = findViewById(R.id.rb_bottom_bar_bus);
        rbBottomBarWangyi = findViewById(R.id.rb_bottom_bar_wangyi);
        rbBottomBarRecord = findViewById(R.id.rb_bottom_bar_record);
        setViewClick(rbBottomBarBus, rbBottomBarWangyi, rbBottomBarRecord);
    }

    @Override
    protected void initData() {
        requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x001);

        if (SPUtil.contains(this, AppHelper.userSpName, AppHelper.userSpStateKey) &&
                ((boolean) SPUtil.get(this, AppHelper.userSpName, AppHelper.userSpStateKey, false)) &&
                BmobUser.isLogin()) {
            user = BmobUser.getCurrentUser(User.class);
        }

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                count = 0;
            }
        };

        fragmentManager = getSupportFragmentManager();
        mFragmentTransaction = fragmentManager.beginTransaction();
//        wangyiFragment = ServiceFactory.getInstance().getWangyiService().getWangyiFragment();
//        busFragment = ServiceFactory.getInstance().getBusService().getBusFragment();
//        recordFragment = ServiceFactory.getInstance().getRecordService().getRecordFragment();
        wangyiFragment = (Fragment) ARouter.getInstance().build(ArouterUrl.wangyi_fragment).navigation();
        busFragment = (Fragment) ARouter.getInstance().build(ArouterUrl.bus_fragment).navigation();
        recordFragment = (Fragment) ARouter.getInstance().build(ArouterUrl.record_fragment).navigation();
        mFragmentTransaction.add(R.id.frameLayout, wangyiFragment);
        mFragmentTransaction.add(R.id.frameLayout, busFragment);
        mFragmentTransaction.add(R.id.frameLayout, recordFragment);
        mFragmentTransaction.commit();
    }

    @Override
    protected void initView() {
        leftView = ServiceFactory.getInstance().getUserService().getUserView(this);
        left_bar.addView(leftView);
        rbBottomBarWangyi.performClick();
    }

    @Override
    public void onMultiClick(View view) {
        if (view == rbBottomBarWangyi) {
            showFragment(1);
        } else if (view == rbBottomBarBus) {
            showFragment(2);
        } else if (view == rbBottomBarRecord) {
            showFragment(3);
        } else {
            super.onMultiClick(view);
        }
    }

    /**
     * 隐藏所有的fragment再显示需要的fragment
     *
     * @param type 1:公交fragment     2：网易fragment    3：打卡fragment
     */
    private void showFragment(int type) {
        mFragmentTransaction = fragmentManager.beginTransaction();
        if (busFragment != null) {
            mFragmentTransaction.hide(busFragment);
        }
        if (wangyiFragment != null) {
            mFragmentTransaction.hide(wangyiFragment);
        }
        if (recordFragment != null) {
            mFragmentTransaction.hide(recordFragment);
        }
        switch (type) {
            case 1:
                mFragmentTransaction.show(wangyiFragment);
                break;
            case 2:
                mFragmentTransaction.show(busFragment);
                break;
            case 3:
                mFragmentTransaction.show(recordFragment);
                break;
        }
        mFragmentTransaction.commit();
    }

    /**
     * 按6下进入隐藏界面（间隔不能超过1s）
     */
    private void clickMark() {
        if (count != 5) {
            handler.removeMessages(1001);
            count++;
            handler.sendEmptyMessageDelayed(1001, 1000);
        } else {
//            startActivity(new Intent(this, HideActivity.class));
            count = 0;
        }
    }

    /**
     * 获取控制台最新JSON数据，不同步到缓存中
     */
    private void fetchUserJsonInfo() {
        BmobUser.fetchUserJsonInfo(new FetchUserInfoListener<String>() {
            @Override
            public void done(String json, BmobException e) {
                if (e == null) {
                    LogUtil.e(getClass(), "更新用户本地缓存信息成功");
                } else {
                    LogUtil.e(getClass(), "更新用户本地缓存信息失败---code:" + e.getErrorCode() + ";message:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 按两次退出程序
     */
    private long exitTime;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序！", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            // 杀死该应用进程
            try {
                System.exit(0);
            } catch (Exception e) {
                System.exit(1);
            }
        }
    }

}
