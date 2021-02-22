package com.sim.baselibrary.utils;

import android.app.ActivityManager;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Sim --- App常用工具类
 */
public class AppUtil {

    /**
     * 打开指定APP
     *
     * @param context
     * @param app_package
     */
    public static void openApp(Context context, String app_package) {
        PackageManager pm = context.getPackageManager();
        //检查包名
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(app_package, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //包名存在则启动app
        if (packageInfo != null) {
            Intent intent = pm.getLaunchIntentForPackage(app_package);
            context.startActivity(intent);
        }
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        int statusBarHeight = sbar;
        return statusBarHeight;
    }

    /**
     * 发送键盘按下指令
     *
     * @param keyCode 码值
     */
    public static void sendKeyCode(final int keyCode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 创建一个Instrumentation对象
                    Instrumentation inst = new Instrumentation();
                    // 调用inst对象的按键模拟方法
                    inst.sendKeyDownUpSync(keyCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 获取所有APP
     *
     * @return
     */
    public static List<PackageInfo> getAllAppInfo(Context context) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> allAppInfo = pm.getInstalledPackages(PackageManager.GET_ACTIVITIES);
        return allAppInfo;
    }

    /**
     * 判断activity是否栈顶运行
     *
     * @param mContext
     * @param activityClassName
     * @return
     */
    public static boolean isActivityRunning(Context mContext, String activityClassName) {
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> info = activityManager.getRunningTasks(1);
        if (info != null && info.size() > 0) {
            ComponentName component = info.get(0).topActivity;
            if (activityClassName.equals(component.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否系统应用
     *
     * @param pkgName
     * @return
     */
    public static boolean isSystemApp(Context context, String pkgName) {
        boolean isSystemApp = false;
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getApplicationContext().getPackageManager();
            pi = pm.getPackageInfo(pkgName, 0);
        } catch (Throwable t) {
            LogUtil.e(AppUtil.class, "判断是否系统应用出错：" + t);
        }
        // 是系统中已安装的应用
        if (pi != null) {
            boolean isSysApp = (pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1;
            boolean isSysUpd = (pi.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 1;
            isSystemApp = isSysApp || isSysUpd;
        }
        return isSystemApp;
    }

    /**
     * 通过包名获取应用程序的名称。
     *
     * @param context     Context对象。
     * @param packageName 包名。
     * @return 返回包名所对应的应用程序的名称。
     */
    public static String getProgramNameByPackageName(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        String name = null;
        try {
            name = pm.getApplicationLabel(pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 通过包名获取应用程序的Icon。
     *
     * @param context     Context对象。
     * @param packageName 包名。
     * @return 返回包名所对应的应用程序的Icon。
     */
    public static Drawable getIconByPackageName(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        Drawable icon = null;
        try {
            icon = pm.getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return icon;
    }

    /**
     * 方法描述：判断某一应用是否正在运行
     *
     * @param context     上下文
     * @param packageName 应用的包名
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isAppRunning(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        if (list.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.baseActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取已安装应用的 uid，-1 表示未安装此应用或程序异常
     */
    public static int getPackageUid(Context context, String packageName) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(packageName, 0);
            if (applicationInfo != null) {
//                Logger.d(applicationInfo.uid);
                return applicationInfo.uid;
            }
        } catch (Exception e) {
            return -1;
        }
        return -1;
    }

    /**
     * 判断某一 uid 的程序是否有正在运行的进程，即是否存活
     *
     * @param context 上下文
     * @param uid     已安装应用的 uid
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isProcessRunning(Context context, int uid) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = am.getRunningServices(200);
        if (runningServiceInfos.size() > 0) {
            for (ActivityManager.RunningServiceInfo appProcess : runningServiceInfos) {
                if (uid == appProcess.uid) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 根据包名强制关闭一个应用，不管前台应用还是后台进程，需要share systemuid
     * 需要权限 FORCE_STOP_PACKAGES
     *
     * @param context
     * @param packageName
     */
    public static boolean stopApps(Context context, String packageName) {
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            Method forceStopPackage = am.getClass().getDeclaredMethod("forceStopPackage", String.class);
            forceStopPackage.setAccessible(true);
            forceStopPackage.invoke(am, packageName);
            LogUtil.i(AppUtil.class, "stopApps: successful");
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtil.i(AppUtil.class, "stopApps: error");
            return false;
        }
    }

}
