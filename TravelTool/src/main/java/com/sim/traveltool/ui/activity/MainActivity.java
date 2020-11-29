package com.sim.traveltool.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sim.traveltool.R;
import com.sim.traveltool.ui.fragment.RecordFragment;
import com.sim.traveltool.ui.fragment.BusFragment;
import com.sim.traveltool.ui.fragment.UserFragment;
import com.sim.traveltool.ui.fragment.WangyiFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    private static final String TAG = "Sim_MainActivity";

    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.bottom_bar_radioGroup)
    RadioGroup barRadioGroup;
    @BindView(R.id.bottom_bar_bus)
    RadioButton barHome;
    @BindView(R.id.bottom_bar_wangyi)
    RadioButton barSpeed;
    @BindView(R.id.bottom_bar_record)
    RadioButton barCommunity;
    @BindView(R.id.bottom_bar_user)
    RadioButton barMe;

    private BusFragment busFragment;
    private WangyiFragment wangyiFragment;
    private RecordFragment recordFragment;
    private UserFragment meFragment;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        barHome.performClick();
    }

    @OnClick({R.id.bottom_bar_radioGroup, R.id.bottom_bar_bus, R.id.bottom_bar_wangyi, R.id.bottom_bar_record, R.id.bottom_bar_user})
    public void onClick(View v) {
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();

        if (busFragment != null) {
            mFragmentTransaction.hide(busFragment);
        }
        if (wangyiFragment != null) {
            mFragmentTransaction.hide(wangyiFragment);
        }
        if (recordFragment != null) {
            mFragmentTransaction.hide(recordFragment);
        }
        if (meFragment != null) {
            mFragmentTransaction.hide(meFragment);
        }

        switch (v.getId()) {
            case R.id.bottom_bar_bus:
                if (busFragment == null) {
                    busFragment = new BusFragment();
                    mFragmentTransaction.add(R.id.frameLayout, busFragment);
                } else {
                    mFragmentTransaction.show(busFragment);
                }
                break;
            case R.id.bottom_bar_wangyi:
                if (wangyiFragment == null) {
                    wangyiFragment = new WangyiFragment();
                    mFragmentTransaction.add(R.id.frameLayout, wangyiFragment);
                } else {
                    mFragmentTransaction.show(wangyiFragment);
                }
                break;
            case R.id.bottom_bar_record:
                if (recordFragment == null) {
                    recordFragment = new RecordFragment();
                    mFragmentTransaction.add(R.id.frameLayout, recordFragment);
                } else {
                    mFragmentTransaction.show(recordFragment);
                }
                break;
            case R.id.bottom_bar_user:
                if (meFragment == null) {
                    meFragment = new UserFragment();
                    mFragmentTransaction.add(R.id.frameLayout, meFragment);
                } else {
                    mFragmentTransaction.show(meFragment);
                }
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
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
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
