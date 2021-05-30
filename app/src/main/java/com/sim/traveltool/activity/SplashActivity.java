package com.sim.traveltool.activity;

import android.os.Bundle;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sim.basicres.base.BaseActivity;
import com.sim.basicres.constant.ArouterUrl;
import com.sim.traveltool.R;

import me.wangyuwei.particleview.ParticleView;

/**
 * @Auther Sim
 * @Time 2021/5/30 20:14
 * @Description
 */
public class SplashActivity extends BaseActivity {

    ParticleView particleView;

    @Override
    protected int getLayoutRes() {
        return R.layout.app_activity_splash;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {
        particleView = findViewById(R.id.particleview);
    }

    @Override
    protected void initView() {
        //这里可以处理事务，处理完执行这个动画结束跳转
        particleView.setOnParticleAnimListener(new ParticleView.ParticleAnimListener() {
            @Override
            public void onAnimationEnd() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ARouter.getInstance().build(ArouterUrl.app_main).navigation();
                finish();
            }
        });
        particleView.startAnim();
    }

    @Override
    protected void initData() {

    }

}
