package com.sim.traveltool.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sim.traveltool.R;
import com.sim.traveltool.ui.fragment.CommunityFragment;
import com.sim.traveltool.ui.fragment.HomeFragment;
import com.sim.traveltool.ui.fragment.MeFragment;
import com.sim.traveltool.ui.fragment.SpeedFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.bottom_bar_radioGroup)
    RadioGroup barRadioGroup;
    @BindView(R.id.bottom_bar_home)
    RadioButton barHome;
    @BindView(R.id.bottom_bar_speed)
    RadioButton barSpeed;
    @BindView(R.id.bottom_bar_community)
    RadioButton barCommunity;
    @BindView(R.id.bottom_bar_me)
    RadioButton barMe;

    private HomeFragment homeFragment;
    private SpeedFragment speedFragment;
    private CommunityFragment communityFragment;
    private MeFragment meFragment;

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

    @OnClick({R.id.bottom_bar_radioGroup, R.id.bottom_bar_home, R.id.bottom_bar_speed, R.id.bottom_bar_community, R.id.bottom_bar_me})
    public void onClick(View v) {
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();

        if (homeFragment != null) {
            mFragmentTransaction.hide(homeFragment);
        }
        if (speedFragment != null) {
            mFragmentTransaction.hide(speedFragment);
        }
        if (communityFragment != null) {
            mFragmentTransaction.hide(communityFragment);
        }
        if (meFragment != null) {
            mFragmentTransaction.hide(meFragment);
        }

        switch (v.getId()) {
            case R.id.bottom_bar_home:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    mFragmentTransaction.add(R.id.frameLayout, homeFragment);
                } else {
                    mFragmentTransaction.show(homeFragment);
                }
                break;
            case R.id.bottom_bar_speed:
                if (speedFragment == null) {
                    speedFragment = new SpeedFragment();
                    mFragmentTransaction.add(R.id.frameLayout, speedFragment);
                } else {
                    mFragmentTransaction.show(speedFragment);
                }
                break;
            case R.id.bottom_bar_community:
                if (communityFragment == null) {
                    communityFragment = new CommunityFragment();
                    mFragmentTransaction.add(R.id.frameLayout, communityFragment);
                } else {
                    mFragmentTransaction.show(communityFragment);
                }
                break;
            case R.id.bottom_bar_me:
                if (meFragment == null) {
                    meFragment = new MeFragment();
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
