package com.sim.wangyi;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sim.basicres.base.BaseActivity;
import com.sim.wangyi.ui.fragment.WangyiFragment;

public class MainActivity extends BaseActivity {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private Fragment wangyiFragment;

    @Override
    protected int getLayoutRes() {
        return R.layout.wangyi_activity_main;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void initView() {
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        if (wangyiFragment == null) {
            wangyiFragment = new WangyiFragment();
            mFragmentTransaction.add(R.id.frameLayout, wangyiFragment);
        } else {
            mFragmentTransaction.show(wangyiFragment);
        }
        mFragmentTransaction.commit();
    }

    @Override
    protected void initData() {

    }

}