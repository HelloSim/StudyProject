package com.sim.traveltool.ui.activity;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.sim.baselibrary.utils.RequestPermission;
import com.sim.traveltool.internet.APIFactory;

/**
 * @Auther Sim
 * @Time 2020/4/24 1:05
 * @Description BaseActivity
 */
public class BaseActivity extends RequestPermission {
    private static final String TAG = "Sim_BaseActivity";

    protected int screenWidth;
    protected int heightPixels;

    public static final APIFactory retrofitUtil = APIFactory.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x001);
        /**
         * 获取屏幕宽高相关
         */
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        heightPixels = (int) (height / density);// 屏幕高度(dp)
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
