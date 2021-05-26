package com.sim.traveltool.ui.activity;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.luojilab.component.componentlib.router.Router;
import com.sim.bean.User;
import com.sim.common.AppHelper;
import com.sim.common.base.BaseActivity;
import com.sim.common.utils.LogUtil;
import com.sim.common.utils.SPUtil;
import com.sim.router.inteface.BusFragmentService;
import com.sim.router.inteface.RecordFragmentService;
import com.sim.router.inteface.WangyiFragmentService;
import com.sim.traveltool.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;

/**
 * @author Sim --- MainActivity
 */
public class MainActivity extends BaseActivity {

    private DrawerLayout drawerLayout;

    private RadioButton rbBottomBarBus, rbBottomBarWangyi, rbBottomBarRecord;

    private Fragment busFragment;
    private Fragment wangyiFragment;
    private Fragment recordFragment;

    private FragmentManager fragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private Handler handler;
    private int count = 0;

    private User user;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {
        drawerLayout = findViewById(R.id.dl_drawer);
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

        Router router = Router.getInstance();
        Log.d("Sim", "initData: " + router);
        if (router.getService(WangyiFragmentService.class.getSimpleName()) != null) {
            WangyiFragmentService service = (WangyiFragmentService) router.getService(WangyiFragmentService.class.getSimpleName());
            wangyiFragment = service.getWangyiFragment();
            mFragmentTransaction.add(R.id.frameLayout, wangyiFragment);
            Log.d("Sim", "initData: "+wangyiFragment);
        }else{
            Log.d("Sim", "initData: null");
        }
        if (router.getService(BusFragmentService.class.getSimpleName()) != null) {
            BusFragmentService service = (BusFragmentService) router.getService(BusFragmentService.class.getSimpleName());
            busFragment = service.getBusFragment();
            mFragmentTransaction.add(R.id.frameLayout, busFragment);
        }
        if (router.getService(RecordFragmentService.class.getSimpleName()) != null) {
            RecordFragmentService service = (RecordFragmentService) router.getService(RecordFragmentService.class.getSimpleName());
            recordFragment = service.getRecordFragment();
            mFragmentTransaction.add(R.id.frameLayout, recordFragment);
        }
        mFragmentTransaction.commit();
    }

    @Override
    protected void initView() {
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
//        switch (type) {
//            case 1:
//                mFragmentTransaction.show(wangyiFragment);
//                break;
//            case 2:
//                mFragmentTransaction.show(busFragment);
//                break;
//            case 3:
//                mFragmentTransaction.show(recordFragment);
//                break;
//        }
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
