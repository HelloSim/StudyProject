package com.sim.bus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sim.basicres.base.BaseActivity;
import com.sim.basicres.constant.AppHelper;
import com.sim.basicres.utils.SPUtil;
import com.sim.bus.ui.fragment.BusFragment;

public class MainActivity extends BaseActivity {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private Fragment busFragment;

    @Override
    protected int getLayoutRes() {
        return R.layout.bus_activity_main;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        SPUtil.put(MainActivity.this, AppHelper.userSpName, AppHelper.userSpStateKey, true);
        if (busFragment == null) {
            busFragment = new BusFragment();
//            busFragment = (Fragment) ARouter.getInstance().build(ArouterUrl.bus_fragment).navigation();
            mFragmentTransaction.add(R.id.frameLayout, busFragment);
        } else {
            mFragmentTransaction.show(busFragment);
        }
        mFragmentTransaction.commit();
    }

    @Override
    protected void initData() {

    }

}