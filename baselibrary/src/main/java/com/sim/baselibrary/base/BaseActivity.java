package com.sim.baselibrary.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.sim.baselibrary.R;
import com.sim.baselibrary.callback.DialogInterface;
import com.sim.baselibrary.callback.OnMultiClickListener;
import com.sim.baselibrary.utils.ScreenUtil;
import com.sim.baselibrary.views.DialogBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sim --- 封装权限请求、dialog。项目模块BaseActivity继承此类
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected int widthPixels;//屏幕宽度
    protected int heightPixels;//屏幕高度

    private int REQUEST_CODE_PERMISSION = 0x00000;//权限请求码

    protected abstract @LayoutRes
    int getLayoutRes();//指定布局

    protected abstract void bindViews(Bundle savedInstanceState);//绑定控件

    protected abstract void initView();//View的一些初始化

    protected abstract void initData();//Data的一些初始化

    /**
     * 控件绑定监听事件
     *
     * @param vs
     */
    protected void setViewClick(View... vs) {
        for (View v : vs) {
            v.setOnClickListener(this);
        }
    }

    protected void setViewClick(int... vs) {
        for (int v : vs) {
            findViewById(v).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (OnMultiClickListener.isNoFastClick()) {
            onMultiClick(v);
        }
    }

    /**
     * 防止多次点击
     *
     * @param view
     */
    public void onMultiClick(View view) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 在第一次创建活动时调用。这是您应进行所有常规静态设置的地方：创建视图，将数据绑定到列表等。
     * 此方法还为您提供了一个包含活动先前冻结状态（如果存在）的捆绑包。
     * 始终紧随其后onStart()。
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        bindViews(savedInstanceState);
        initData();
        initView();
    }

    /**
     * 在您的活动停止之后，再次开始之前调用。
     * 总是跟着 onStart()
     */
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    /**
     * 当活动对用户可见时调用。
     * onResume()如果活动进入前台或被onStop()隐藏，则紧随其后。
     */
    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * 当活动将开始与用户互动时调用。在这一点上，您的活动位于其活动堆栈的顶部，其中有用户输入。
     * 始终紧随其后onPause()。
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 当活动失去前台状态，不再可聚焦或在过渡到停止/隐藏或销毁状态之前调用。该活动仍对用户可见，因此建议保持其视觉活动状态并继续更新UI。
     * 此方法的实现必须非常快，因为在此方法返回之前，下一个活动将不会恢复。
     * onResume()如果该活动返回到最前面，或者onStop()对于用户来说是不可见的，则紧随其后。
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 当活动不再对用户可见时调用。发生这种情况的原因可能是因为一项新的活动正在最上面开始，
     * 一个现有的活动正在该活动的前面，或者该活动正在被销毁。通常用于停止动画和刷新UI等。
     * onRestart()如果此活动返回与用户交互，或者onDestroy()该活动消失， 则紧随其后。
     */
    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 活动销毁之前收到的最后一个回调。这可能是由于活动即将结束（有人对其进行的调用Activity#finish）
     * 或系统暂时销毁该活动实例以节省空间而发生的。您可以使用Activity#isFinishing方法区分这两种情况。
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 请求权限
     *
     * @param permissions 请求的权限
     * @param requestCode 请求权限的请求码
     */
    public void requestPermission(String[] permissions, int requestCode) {
        this.REQUEST_CODE_PERMISSION = requestCode;
        if (checkPermissions(permissions)) {
            permissionSuccess(REQUEST_CODE_PERMISSION);
        } else {
            List<String> needPermissions = getDeniedPermissions(permissions);
            ActivityCompat.requestPermissions(this, needPermissions.toArray(new String[needPermissions.size()]), REQUEST_CODE_PERMISSION);
        }
    }

    /**
     * 检测所有的权限是否都已授权
     *
     * @param permissions
     * @return
     */
    private boolean checkPermissions(String[] permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     */
    private List<String> getDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) !=
                    PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                needRequestPermissionList.add(permission);
            }
        }
        return needRequestPermissionList;
    }

    /**
     * 系统请求权限回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (verifyPermissions(grantResults)) {
                permissionSuccess(REQUEST_CODE_PERMISSION);
            } else {
                permissionFail(REQUEST_CODE_PERMISSION);
            }
        }
    }

    /**
     * 确认所有的权限是否都已授权
     *
     * @param grantResults
     * @return
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    //无权限提示跳转到当前应用设置页面
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    //获取权限成功
    public void permissionSuccess(int requestCode) {

    }

    //权限获取失败
    public void permissionFail(int requestCode) {
        //无权限显示提示对话框,需自行修改
        showDialog("提示信息", "缺少必要权限软件无法正常使用!如若恢复正常使用，请单击【确定】按钮前往设置进行权限授权",
                "确定", "取消", new DialogInterface() {
                    @Override
                    public void sureOnClick() {
                        startAppSettings();
                    }

                    @Override
                    public void cancelOnClick() {

                    }
                });
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    private int getScreenWidth() {
        if (widthPixels == 0 || heightPixels == 0) {
            getScreenSize();
        }
        return widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    private int getHeightPixels() {
        if (widthPixels == 0 || heightPixels == 0) {
            getScreenSize();
        }
        return heightPixels;
    }

    private void getScreenSize() {
        //获取屏幕宽高相关
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        widthPixels = (int) (width / density);  // 屏幕宽度(dp)
        heightPixels = (int) (height / density);// 屏幕高度(dp)
    }

    /**
     * 显示popupWindow
     *
     * @param view        布局
     * @param pupDPWidth  宽度
     * @param pupDPHeight 高度
     * @return
     */
    public PopupWindow showPopupWindow(View view, int pupDPWidth, int pupDPHeight) {
        PopupWindow popupWindow = new PopupWindow(this);
        popupWindow.setContentView(view);//设置主体布局
        popupWindow.setWidth(ScreenUtil.dip2px(this, pupDPWidth));//宽度
        popupWindow.setHeight(ScreenUtil.dip2px(this, pupDPHeight));//高度
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());//设置空白背景
        popupWindow.setAnimationStyle(R.style.popwindow_anim_style);//动画
        return popupWindow;
    }

    /**
     * dialog显示
     *
     * @param title           标题
     * @param message         提示信息
     * @param sureText        确认按钮
     * @param cancelText      取消按钮
     * @param dialogInterface 点击事件监听
     */
    public void showDialog(String title, String message, String sureText, String cancelText,
                           final DialogInterface dialogInterface) {
        DialogBuilder dialogBuilder;
        dialogBuilder = new DialogBuilder(this);
        if (title != null) {
            dialogBuilder.title(title);
        }
        if (message != null) {
            dialogBuilder.message(message);
        }
        if (sureText != null) {
            dialogBuilder.sureText(sureText);
        }
        if (cancelText != null) {
            dialogBuilder.cancelText(cancelText);
        }
        if (dialogInterface != null) {
            dialogBuilder.setSureOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogInterface.sureOnClick();
                }
            });
            dialogBuilder.setCancelOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogInterface.cancelOnClick();
                }
            });
        }
        dialogBuilder.build().show();
    }

}
