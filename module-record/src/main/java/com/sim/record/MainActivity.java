package com.sim.record;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sim.basicres.base.BaseActivity;
import com.sim.basicres.utils.ToastUtil;
import com.sim.mine.bean.User;
import com.sim.mine.utils.CallBack;
import com.sim.record.ui.fragment.RecordFragment;

//这里单独跑时，要把user模块的AndroidManifest的BmobContentProvider的authorities修改为com.sim.record.BmobContentProvider
public class MainActivity extends BaseActivity {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private RecordFragment recordFragment;

    @Override
    protected int getLayoutRes() {
        return R.layout.record_activity_main;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!User.isLogin()) {
                    User.loginByAccount("HelloSim", "123", new CallBack() {
                        @Override
                        public void success(Object... values) {
                            ToastUtil.toast(MainActivity.this, "登录成功！");
                            mFragmentManager = getSupportFragmentManager();
                            mFragmentTransaction = mFragmentManager.beginTransaction();
                            mFragmentTransaction.hide(recordFragment);
                            recordFragment = null;
                            recordFragment = new RecordFragment();
                            mFragmentTransaction.add(R.id.frameLayout, recordFragment);
                            mFragmentTransaction.show(recordFragment);
                            mFragmentTransaction.commit();
                        }

                        @Override
                        public void fail(String values) {
                            ToastUtil.toast(MainActivity.this, "登录失败：" + values);
                            mFragmentManager = getSupportFragmentManager();
                            mFragmentTransaction = mFragmentManager.beginTransaction();
                            mFragmentTransaction.hide(recordFragment);
                            recordFragment = null;
                            recordFragment = new RecordFragment();
                            mFragmentTransaction.add(R.id.frameLayout, recordFragment);
                            mFragmentTransaction.show(recordFragment);
                            mFragmentTransaction.commit();
                        }
                    });
                } else {
                    User.logout();
                }
            }
        });
    }

    @Override
    protected void initView() {
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        if (recordFragment == null) {
            recordFragment = new RecordFragment();
            mFragmentTransaction.add(R.id.frameLayout, recordFragment);
        } else {
            mFragmentTransaction.show(recordFragment);
        }
        mFragmentTransaction.commit();
    }

    @Override
    protected void initData() {

    }

}
