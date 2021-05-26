package com.sim.bus.ui.activity;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sim.bus.R;
import com.sim.bus.ui.fragment.BusFragment;
import com.sim.common.AppHelper;
import com.sim.common.base.BaseActivity;
import com.sim.common.utils.SPUtil;

public class MainActivity extends BaseActivity {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private BusFragment busFragment;

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