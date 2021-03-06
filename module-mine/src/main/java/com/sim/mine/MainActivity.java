package com.sim.mine;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sim.basicres.base.BaseActivity;
import com.sim.mine.ui.fragment.MineFragment;
import com.sim.user.bean.User;

//这里单独跑时，要把user模块的AndroidManifest的BmobContentProvider的authorities修改为com.sim.mine.BmobContentProvider
public class MainActivity extends BaseActivity {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private Fragment mineFragment;

    @Override
    protected int getLayoutRes() {
        return R.layout.mine_activity_main;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        User.logout();
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        if (mineFragment == null) {
            mineFragment = new MineFragment();
            mFragmentTransaction.add(R.id.frameLayout, mineFragment);
        } else {
            mFragmentTransaction.show(mineFragment);
        }
        mFragmentTransaction.commit();
    }

    @Override
    protected void initData() {

    }

}