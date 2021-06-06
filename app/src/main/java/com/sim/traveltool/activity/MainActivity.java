package com.sim.traveltool.activity;

import android.Manifest;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.sim.basicres.base.BaseActivity;
import com.sim.basicres.constant.ArouterUrl;
import com.sim.traveltool.R;

@Route(path = ArouterUrl.app_main)
public class MainActivity extends BaseActivity {

    private BubbleNavigationLinearView bubbleNavigationLinearView;

    private Fragment busFragment, publicFragment, wangyiFragment, recordFragment, mineFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction mFragmentTransaction;

    @Override
    protected int getLayoutRes() {
        return R.layout.app_activity_main;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {
        bubbleNavigationLinearView = findViewById(R.id.bottom_navigation_view_linear);
        bubbleNavigationLinearView.setTypeface(Typeface.createFromAsset(getAssets(), "rubik.ttf"));
        bubbleNavigationLinearView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                showFragment(position);
            }
        });
    }

    @Override
    protected void initData() {
        requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x001);

        wangyiFragment = (Fragment) ARouter.getInstance().build(ArouterUrl.Wangyi.wangyi_fragment).navigation();
        publicFragment = (Fragment) ARouter.getInstance().build(ArouterUrl.Public.public_fragment).navigation();
        busFragment = (Fragment) ARouter.getInstance().build(ArouterUrl.Bus.bus_fragment).navigation();
        recordFragment = (Fragment) ARouter.getInstance().build(ArouterUrl.Record.record_fragment).navigation();
        mineFragment = (Fragment) ARouter.getInstance().build(ArouterUrl.Mine.user_fragment).navigation();
    }

    @Override
    protected void initView() {
        fragmentManager = getSupportFragmentManager();
        mFragmentTransaction = fragmentManager.beginTransaction();

        mFragmentTransaction.add(R.id.frameLayout, wangyiFragment);
        mFragmentTransaction.add(R.id.frameLayout, publicFragment);
        mFragmentTransaction.add(R.id.frameLayout, busFragment);
        mFragmentTransaction.add(R.id.frameLayout, recordFragment);
        mFragmentTransaction.add(R.id.frameLayout, mineFragment);

        mFragmentTransaction.hide(busFragment);
        mFragmentTransaction.hide(publicFragment);
        mFragmentTransaction.hide(recordFragment);
        mFragmentTransaction.hide(mineFragment);

        mFragmentTransaction.commit();
    }

    /**
     * 隐藏所有的fragment再显示需要的fragment
     *
     * @param type
     */
    private void showFragment(int type) {
        mFragmentTransaction = fragmentManager.beginTransaction();
        if (busFragment != null) {
            mFragmentTransaction.hide(busFragment);
        }
        if (publicFragment != null) {
            mFragmentTransaction.hide(publicFragment);
        }
        if (wangyiFragment != null) {
            mFragmentTransaction.hide(wangyiFragment);
        }
        if (recordFragment != null) {
            mFragmentTransaction.hide(recordFragment);
        }
        if (mineFragment != null) {
            mFragmentTransaction.hide(mineFragment);
        }
        switch (type) {
            case 0:
                mFragmentTransaction.show(wangyiFragment);
                break;
            case 1:
                mFragmentTransaction.show(publicFragment);
                break;
            case 2:
                mFragmentTransaction.show(busFragment);
                break;
            case 3:
                mFragmentTransaction.show(recordFragment);
                break;
            case 4:
                mFragmentTransaction.show(mineFragment);
                break;
        }
        mFragmentTransaction.commit();
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
