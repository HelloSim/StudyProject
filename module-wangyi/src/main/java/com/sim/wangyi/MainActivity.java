package com.sim.wangyi;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sim.basicres.base.BaseActivity;
import com.sim.basicres.utils.ToastUtil;
import com.sim.user.bean.User;
import com.sim.user.utils.CallBack;
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
        findViewById(R.id.btn_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!User.isLogin()) {
                    User.loginByAccount("HelloSim", "123", new CallBack() {
                        @Override
                        public void success(Object... values) {
                            ToastUtil.toast(MainActivity.this, "登录成功！");
                            mFragmentManager = getSupportFragmentManager();
                            mFragmentTransaction = mFragmentManager.beginTransaction();
                            mFragmentTransaction.hide(wangyiFragment);
                            wangyiFragment = null;
                            wangyiFragment = new WangyiFragment();
                            mFragmentTransaction.add(R.id.frameLayout, wangyiFragment);
                            mFragmentTransaction.show(wangyiFragment);
                            mFragmentTransaction.commit();
                        }

                        @Override
                        public void fail(String values) {
                            ToastUtil.toast(MainActivity.this, "登录失败：" + values);
                            mFragmentManager = getSupportFragmentManager();
                            mFragmentTransaction = mFragmentManager.beginTransaction();
                            mFragmentTransaction.hide(wangyiFragment);
                            wangyiFragment = null;
                            wangyiFragment = new WangyiFragment();
                            mFragmentTransaction.add(R.id.frameLayout, wangyiFragment);
                            mFragmentTransaction.show(wangyiFragment);
                            mFragmentTransaction.commit();
                        }
                    });
                }
            }
        });
        findViewById(R.id.btn_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (User.isLogin()) {
                    User.logout();
                    mFragmentManager = getSupportFragmentManager();
                    mFragmentTransaction = mFragmentManager.beginTransaction();
                    mFragmentTransaction.hide(wangyiFragment);
                    wangyiFragment = null;
                    wangyiFragment = new WangyiFragment();
                    mFragmentTransaction.add(R.id.frameLayout, wangyiFragment);
                    mFragmentTransaction.show(wangyiFragment);
                    mFragmentTransaction.commit();
                }
            }
        });
    }

    @Override
    protected void initView() {
        User.logout();
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