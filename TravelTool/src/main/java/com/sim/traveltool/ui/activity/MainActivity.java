package com.sim.traveltool.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.sim.baselibrary.base.BaseActivity;
import com.sim.baselibrary.utils.SPUtil;
import com.sim.baselibrary.utils.ToastUtil;
import com.sim.traveltool.R;
import com.sim.traveltool.bean.UserInfo;
import com.sim.traveltool.ui.fragment.BusFragment;
import com.sim.traveltool.ui.fragment.RecordFragment;
import com.sim.traveltool.ui.fragment.WangyiFragment;

public class MainActivity extends BaseActivity {

    RadioGroup rgBottomBar;
    RadioButton rbBottomBarBus;
    RadioButton rbBottomBarWangyi;
    RadioButton rbBottomBarRecord;

    RelativeLayout rlUserLogIn;
    RelativeLayout rlUserDetail;
    RelativeLayout rlUserCollect;
    RelativeLayout rlUserSetting;

    DrawerLayout drawerLayout;
    ImageView ivUserImage;
    TextView tvUserNikeName;

    private String spName = "userState";
    private String spStateKey = "isLogIn";
    private String spUserInfoKey = "userInfo";
    private boolean isLogIn = false;

    private UserInfo userInfo;

    private final int LogInNum = 1001;

    private BusFragment busFragment;
    private WangyiFragment wangyiFragment;
    private RecordFragment recordFragment;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private Handler handler;
    private int count = 0;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {
        rgBottomBar = findViewById(R.id.rg_bottom_bar);
        rbBottomBarBus = findViewById(R.id.rb_bottom_bar_bus);
        rbBottomBarWangyi = findViewById(R.id.rb_bottom_bar_wangyi);
        rbBottomBarRecord = findViewById(R.id.rb_bottom_bar_record);
        rlUserLogIn = findViewById(R.id.rl_user_log_in);
        rlUserDetail = findViewById(R.id.rl_user_detail);
        rlUserCollect = findViewById(R.id.rl_user_collect);
        rlUserSetting = findViewById(R.id.rl_user_setting);
        drawerLayout = findViewById(R.id.dl_drawer);
        ivUserImage = findViewById(R.id.iv_user);
        tvUserNikeName = findViewById(R.id.tv_user_nike_name);
        setViewClick(rbBottomBarBus, rbBottomBarWangyi, rbBottomBarRecord, rlUserLogIn, rlUserDetail, rlUserCollect, rlUserSetting);
    }

    @Override
    protected void initView() {
        rbBottomBarBus.performClick();
        if (isLogIn) {
            rlUserLogIn.setVisibility(View.GONE);
            rlUserDetail.setVisibility(View.VISIBLE);
            if (SPUtil.contains(this, spName, spUserInfoKey)) {
                userInfo = new Gson().fromJson((String) SPUtil.get(this, spName, spUserInfoKey, ""), UserInfo.class);
                if (userInfo != null) {
                    if (userInfo.getResult().getHeaderImg() != null) {
                        Glide.with(this).load(userInfo.getResult().getHeaderImg()).into(ivUserImage);
                    }
                    if (userInfo.getResult().getName() != null) {
                        tvUserNikeName.setText(userInfo.getResult().getName());
                    }
                } else {
                    isLogIn = false;
                    rlUserDetail.setVisibility(View.GONE);
                    rlUserLogIn.setVisibility(View.VISIBLE);
                }
            } else {
                isLogIn = false;
                rlUserDetail.setVisibility(View.GONE);
                rlUserLogIn.setVisibility(View.VISIBLE);
            }
        } else {
            rlUserDetail.setVisibility(View.GONE);
            rlUserLogIn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData() {
        requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x001);
        if (!SPUtil.contains(this, spName, spStateKey)) {
            SPUtil.put(this, spName, spStateKey, isLogIn);
        } else {
            isLogIn = (boolean) SPUtil.get(this, spName, spStateKey, false);
        }

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                count = 0;
            }
        };
    }

    @Override
    public void onMultiClick(View view) {
        if (view == rbBottomBarBus) {
            showFragment(1);
        } else if (view == rbBottomBarWangyi) {
            showFragment(2);
        } else if (view == rbBottomBarRecord) {
            showFragment(3);
        } else if (view == rlUserLogIn) {
            drawerLayout.close();
            startActivityForResult(new Intent(this, UserLogInActivity.class), LogInNum);
        } else if (view == rlUserDetail) {
            drawerLayout.close();
            startActivityForResult(new Intent(this, UserUpdateActivity.class), LogInNum);
        } else if (view == rlUserCollect) {
            if (isLogIn) {
                drawerLayout.close();
                startActivity(new Intent(this, NewsCollectActivity.class));
            } else {
                ToastUtil.T_Error(this, "未登录！");
            }
        } else if (view == rlUserSetting) {
            clickMark();
        }
        super.onMultiClick(view);
    }

    /**
     * 隐藏所有的fragment再显示需要的fragment
     *
     * @param type 1:公交fragment     2：网易fragment    3：打卡fragment
     */
    private void showFragment(int type) {
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
        switch (type) {
            case 1:
                if (busFragment == null) {
                    busFragment = new BusFragment();
                    mFragmentTransaction.add(R.id.frameLayout, busFragment);
                } else {
                    mFragmentTransaction.show(busFragment);
                }
                break;
            case 2:
                if (wangyiFragment == null) {
                    wangyiFragment = new WangyiFragment();
                    mFragmentTransaction.add(R.id.frameLayout, wangyiFragment);
                } else {
                    mFragmentTransaction.show(wangyiFragment);
                }
                break;
            case 3:
                if (recordFragment == null) {
                    recordFragment = new RecordFragment();
                    mFragmentTransaction.add(R.id.frameLayout, recordFragment);
                } else {
                    mFragmentTransaction.show(recordFragment);
                }
                break;
        }
        mFragmentTransaction.commit();
    }

    //按6下进入隐藏界面（间隔不能超过1s）
    private void clickMark() {
        if (count != 5) {
            handler.removeMessages(1001);
            count++;
            handler.sendEmptyMessageDelayed(1001, 1000);
        } else {
            startActivity(new Intent(this, HideActivity.class));
            count = 0;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == LogInNum) {
            isLogIn = (boolean) SPUtil.get(this, spName, spStateKey, false);
            if (isLogIn) {
                rlUserLogIn.setVisibility(View.GONE);
                rlUserDetail.setVisibility(View.VISIBLE);
                if (SPUtil.contains(this, spName, spUserInfoKey)) {
                    userInfo = new Gson().fromJson((String) SPUtil.get(this, spName, spUserInfoKey, ""), UserInfo.class);
                    if (userInfo.getResult().getHeaderImg() != null) {
                        Glide.with(this).load(userInfo.getResult().getHeaderImg()).into(ivUserImage);
                    }
                    if (userInfo.getResult().getName() != null) {
                        tvUserNikeName.setText(userInfo.getResult().getName());
                    }
                } else {
                    isLogIn = false;
                    rlUserDetail.setVisibility(View.GONE);
                    rlUserLogIn.setVisibility(View.VISIBLE);
                }
            } else {
                rlUserDetail.setVisibility(View.GONE);
                rlUserLogIn.setVisibility(View.VISIBLE);
            }
        }
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
