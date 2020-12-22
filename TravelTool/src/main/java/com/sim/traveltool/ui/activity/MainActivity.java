package com.sim.traveltool.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.sim.baselibrary.utils.SPUtil;
import com.sim.baselibrary.utils.ToastUtil;
import com.sim.traveltool.R;
import com.sim.traveltool.base.AppActivity;
import com.sim.traveltool.bean.UserInfo;
import com.sim.traveltool.ui.fragment.BusFragment;
import com.sim.traveltool.ui.fragment.RecordFragment;
import com.sim.traveltool.ui.fragment.WangyiFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends AppActivity {
    private static final String TAG = "Sim_MainActivity";

    @BindView(R.id.bottom_bar_bus)
    RadioButton barHome;

    @BindView(R.id.dl_drawer)
    DrawerLayout dl_drawer;
    @BindView(R.id.user_log_in)
    RelativeLayout userLogIn;
    @BindView(R.id.user_detail)
    RelativeLayout userDetail;
    @BindView(R.id.user_image)
    ImageView userImage;
    @BindView(R.id.user_nike_name)
    TextView userNikeName;
    @BindView(R.id.user_autograph)
    TextView userAutograph;

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
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    protected void initData() {
        requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x001);
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

    protected void initView() {
        barHome.performClick();
        userImage = findViewById(R.id.user_image);
        userNikeName = findViewById(R.id.user_nike_name);
        userAutograph = findViewById(R.id.user_autograph);
        if (isLogIn) {
            userLogIn.setVisibility(View.GONE);
            userDetail.setVisibility(View.VISIBLE);
            if (SPUtil.contains(this, spName, spUserInfoKey)) {
                userInfo = new Gson().fromJson((String) SPUtil.get(this, spName, spUserInfoKey, ""), UserInfo.class);
                if (userInfo != null) {
                    if (userInfo.getResult().getHeaderImg() != null) {
                        Glide.with(this).load(userInfo.getResult().getHeaderImg()).into(userImage);
                    }
                    if (userInfo.getResult().getName() != null) {
                        userNikeName.setText(userInfo.getResult().getName());
                    }
                    if (userInfo.getResult().getAutograph() != null) {
                        userAutograph.setText(userInfo.getResult().getAutograph());
                    }
                } else {
                    isLogIn = false;
                    userDetail.setVisibility(View.GONE);
                    userLogIn.setVisibility(View.VISIBLE);
                }
            } else {
                isLogIn = false;
                userDetail.setVisibility(View.GONE);
                userLogIn.setVisibility(View.VISIBLE);
            }
        } else {
            userDetail.setVisibility(View.GONE);
            userLogIn.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏所有的fragment再显示需要的fragment
     *
     * @param type 1:公交fragment     2：网易fragment    3：打卡fragment
     */
    private void hideAllFragment(int type) {
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

    @OnClick({R.id.bottom_bar_radioGroup, R.id.bottom_bar_bus, R.id.bottom_bar_wangyi, R.id.bottom_bar_record,
            R.id.user_log_in, R.id.user_detail, R.id.user_collect, R.id.user_setting})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_bar_bus:
                hideAllFragment(1);
                break;
            case R.id.bottom_bar_wangyi:
                hideAllFragment(2);
                break;
            case R.id.bottom_bar_record:
                hideAllFragment(3);
                break;
            case R.id.user_log_in:
                dl_drawer.close();
                startActivityForResult(new Intent(this, UserLogInActivity.class), LogInNum);
                break;
            case R.id.user_detail:
                dl_drawer.close();
                startActivityForResult(new Intent(this, UserUpdateActivity.class), LogInNum);
                break;
            case R.id.user_collect:
                if (isLogIn) {
                    dl_drawer.close();
                    startActivity(new Intent(this, NewsCollectActivity.class));
                } else {
                    ToastUtil.T_Error(this, "未登录！");
                }
                break;
            case R.id.user_setting:
                clickMark();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == LogInNum) {
            isLogIn = (boolean) SPUtil.get(this, spName, spStateKey, false);
            if (isLogIn) {
                userLogIn.setVisibility(View.GONE);
                userDetail.setVisibility(View.VISIBLE);
                if (SPUtil.contains(this, spName, spUserInfoKey)) {
                    userInfo = new Gson().fromJson((String) SPUtil.get(this, spName, spUserInfoKey, ""), UserInfo.class);
                    if (userInfo.getResult().getHeaderImg() != null) {
                        Glide.with(this).load(userInfo.getResult().getHeaderImg()).into(userImage);
                    }
                    if (userInfo.getResult().getName() != null) {
                        userNikeName.setText(userInfo.getResult().getName());
                    }
                    if (userInfo.getResult().getAutograph() != null) {
                        userAutograph.setText(userInfo.getResult().getAutograph());
                    }
                } else {
                    isLogIn = false;
                    userDetail.setVisibility(View.GONE);
                    userLogIn.setVisibility(View.VISIBLE);
                }
            } else {
                userDetail.setVisibility(View.GONE);
                userLogIn.setVisibility(View.VISIBLE);
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
