package com.sim.record;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sim.basicres.base.BaseActivity;
import com.sim.basicres.bean.EventMessage;
import com.sim.basicres.constant.AppHelper;
import com.sim.basicres.utils.SPUtil;
import com.sim.record.ui.fragment.RecordFragment;

import org.greenrobot.eventbus.EventBus;

import cn.bmob.v3.BmobUser;

/**
 * @ author: Sim
 * @ time： 2021/5/24 16:12
 * @ description：
 */
public class MainActivity extends BaseActivity {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private RecordFragment recordFragment;

    private boolean isLogig = false;

    @Override
    protected int getLayoutRes() {
        return R.layout.record_activity_main;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLogig) {
//                    BmobUser.loginByAccount("HelloSim", "123", new LogInListener<User>() {
//                        @Override
//                        public void done(User user, BmobException e) {
//                            if (e == null) {
//                                Log.d("Sim", "done: " + "登录成功");
//                                isLogig = true;
//                                EventBus.getDefault().post(new EventMessage(2001));
//                            } else {
//                                Log.e("Sim", "登录出错---code:" + e.getErrorCode() + ";message:" + e.getMessage());
//                            }
//                        }
//                    });
                } else {
                    BmobUser.logOut();
                    isLogig = false;
                    EventBus.getDefault().post(new EventMessage(2002));
                }
            }
        });
    }

    @Override
    protected void initView() {
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        SPUtil.put(MainActivity.this, AppHelper.userSpName, AppHelper.userSpStateKey, true);
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
