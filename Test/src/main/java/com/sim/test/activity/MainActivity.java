package com.sim.test.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.sim.test.AppHelper;
import com.sim.test.R;
import com.sim.test.activity.Fragment.FragmentActivity;
import com.sim.test.service.StartAVCallFloatViewService;
import com.xujiaji.happybubble.BubbleDialog;
import com.xujiaji.happybubble.BubbleLayout;
import com.xujiaji.happybubble.Util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};

    //初始化提示框
    private boolean isFirst = true;
    private BubbleDialog buddleDialog;
    private BubbleLayout bl;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        initPermission();
    }

    /**
     * 做一些初始化工作、Handler
     */
    @SuppressLint("HandlerLeak")
    private void init() {
        bl = new BubbleLayout(this);
        bl.setBubbleColor(Color.WHITE);
        bl.setShadowColor(Color.BLACK);
        bl.setLookLength(Util.dpToPx(this, 20));
        bl.setLookWidth(Util.dpToPx(this, 15));
        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case AppHelper.BUDDLE_DIAALG:
                        buddleDialog.dismiss();
                        break;
                }
            }
        };
    }

    /**
     * 判断是否首次进入，首次就显示提示语句
     */
    @SuppressLint("InflateParams")
    private void checkIsFirst() {
        if (isFirst) {
            buddleDialog = new BubbleDialog(this)
                    .addContentView(LayoutInflater.from(this).inflate(R.layout.view__first_goin, null))
                    .setOffsetY(25)
                    .setClickedView(findViewById(R.id.btn_animation))
                    .calBar(true)
                    .setBubbleLayout(bl);
            buddleDialog.show();
            handler.sendEmptyMessageDelayed(AppHelper.BUDDLE_DIAALG, 3000);
        }
        isFirst = false;
    }

    private void initPermission() {
        verifyStoragePermissions(this);
        requestPermission();
    }

    private void requestPermission(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int permission = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if(permission!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,new String[] {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.LOCATION_HARDWARE,Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_SETTINGS,Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_CONTACTS},0x0010);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (checkFloatPermission(this)) {
                Log.d(TAG, "onActivityResult: 悬浮窗权限申请成功...");
            } else {
                Log.d(TAG, "onActivityResult: 悬浮窗权限申请失败...");
                finish();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick({R.id.btn_animation, R.id.btn_fragment, R.id.btn_recycle_view,
            R.id.btn_dialog, R.id.btn_calendar, R.id.btn_play_video,
            R.id.btn_shared_preferences, R.id.btn_file_manager,
            R.id.btn_camera, R.id.btn_open_service, R.id.btn_close_service,
            R.id.btn_smzdm})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_animation:
                startActivity(new Intent(this, AnimationActivity.class));
                break;
            case R.id.btn_calendar:
                startActivity(new Intent(this, CalendarActivity.class));
                break;
            case R.id.btn_play_video:
                startActivity(new Intent(this, PlayVideoActivity.class));
                break;
            case R.id.btn_fragment:
                startActivity(new Intent(this, FragmentActivity.class));
                break;
            case R.id.btn_recycle_view:
                startActivity(new Intent(this, RecycleViewActivity.class));
                break;
            case R.id.btn_dialog:
                startActivity(new Intent(this, DialogActivity.class));
                break;
            case R.id.btn_shared_preferences:
                startActivity(new Intent(this, SharedPreferencesActivity.class));
                break;
            case R.id.btn_file_manager:
                startActivity(new Intent(this, FileManagerActivity.class));
                break;
            case R.id.btn_camera:
                startActivity(new Intent(this, CameraActivity.class));
                break;
            case R.id.btn_open_service:
                String androidSDK = Build.VERSION.SDK;
                if (Integer.parseInt(androidSDK) >= 23 && !Settings.canDrawOverlays(this)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                }
                startService(new Intent(this, StartAVCallFloatViewService.class));
                break;
            case R.id.btn_close_service:
                stopService(new Intent(this, StartAVCallFloatViewService.class));
                break;
            case R.id.btn_smzdm:
                startActivity(new Intent(this, SmzdmActivity.class));
                break;
        }
        Log.d(TAG, String.valueOf(v.getId()));
    }

    /**
     * 动态申请读写SD卡权限
     */
    public static void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 动态申请悬浮窗权限
     */
    public static boolean checkFloatPermission(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return true;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            try {
                Class cls = Class.forName("android.content.Context");
                Field declaredField = cls.getDeclaredField("APP_OPS_SERVICE");
                declaredField.setAccessible(true);
                Object obj = declaredField.get(cls);
                if (!(obj instanceof String)) {
                    return false;
                }
                String str2 = (String) obj;
                obj = cls.getMethod("getSystemService", String.class).invoke(context, str2);
                cls = Class.forName("android.app.AppOpsManager");
                Field declaredField2 = cls.getDeclaredField("MODE_ALLOWED");
                declaredField2.setAccessible(true);
                Method checkOp = cls.getMethod("checkOp", Integer.TYPE, Integer.TYPE, String.class);
                int result = (Integer) checkOp.invoke(obj, 24, Binder.getCallingUid(), context.getPackageName());
                return result == declaredField2.getInt(cls);
            } catch (Exception e) {
                return false;
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AppOpsManager appOpsMgr = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                if (appOpsMgr == null)
                    return false;
                int mode = appOpsMgr.checkOpNoThrow("android:system_alert_window", android.os.Process.myUid(), context
                        .getPackageName());
                return mode == AppOpsManager.MODE_ALLOWED || mode == AppOpsManager.MODE_IGNORED;
            } else {
                return Settings.canDrawOverlays(context);
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
            try {
                // 杀死该应用进程
                System.exit(0);
            } catch (Exception e) {
                System.exit(1);
            }
        }
    }

}
