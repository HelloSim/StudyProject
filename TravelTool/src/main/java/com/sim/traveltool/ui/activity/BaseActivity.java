package com.sim.traveltool.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sim.traveltool.internet.APIFactory;
import com.sim.baselibrary.utils.ToastUtil;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * @Auther Sim
 * @Time 2020/4/24 1:05
 * @Description BaseActivity
 */
@RuntimePermissions
public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "Sim_BaseActivity";

    protected int screenWidth;
    protected int heightPixels;

    public static final APIFactory retrofitUtil = APIFactory.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseActivityPermissionsDispatcher.getReadAndWriteExternalStoragePermissionsWithPermissionCheck(this);
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

    //获取了相应的权限之后就会执行这个方法
    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void getReadAndWriteExternalStoragePermissions() {
    }

    //申请权限失败之后的处理
    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void noGetReadAndWriteExternalStoragePermissions() {
        Toast.makeText(this, "拒绝权限可能导致应用异常", Toast.LENGTH_SHORT).show();
//        BaseActivityPermissionsDispatcher.getReadAndWriteExternalStoragePermissionsWithPermissionCheck(this);
    }

    //只有当第一次请求权限被用户拒绝，下次请求权限之前会调用
    @OnShowRationale({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void showRationale(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage("使用功能需要相应权限，下一步将继续请求权限")
                .setPositiveButton("下一步", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();//继续执行请求
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                request.cancel();//取消执行请求
            }
        })
                .show();
    }

    //用户选中了授权窗口中的不再询问复选框后并拒绝了权限请求时需要调用的方法
    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void multiNeverAsk() {
        ToastUtil.T_Info(this, "缺少必要权限软件无法正常使用!如若恢复正常使用，请前往设置进行权限授权");
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BaseActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

}
