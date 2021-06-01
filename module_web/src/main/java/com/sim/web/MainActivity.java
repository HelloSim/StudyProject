package com.sim.web;

import android.content.Intent;
import android.os.Bundle;

import com.sim.basicres.base.BaseActivity;
import com.sim.web.ui.activity.WebActivity;

public class MainActivity extends BaseActivity {
    @Override
    protected int getLayoutRes() {
        return R.layout.web_activity_main;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra("webUrl", "https://www.baidu.com/");
        intent.putExtra("title", "百度一下，你就知道");
        startActivity(intent);
    }

    @Override
    protected void initData() {

    }

}
