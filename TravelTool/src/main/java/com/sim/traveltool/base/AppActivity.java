package com.sim.traveltool.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.sim.baselibrary.base.BaseActivity;
import com.sim.traveltool.internet.APIFactory;

import butterknife.ButterKnife;

/**
 * @Auther Sim
 * @Time 2020/4/24 1:05
 * @Description AppActivity
 */
public abstract class AppActivity extends BaseActivity {

    //网络请求单例
    public static final APIFactory retrofitUtil = APIFactory.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        ButterKnife.bind(this);
        initData();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected abstract int getContentViewId();//布局文件id

    protected void initData() {
    }

    protected void initView() {
    }

}
