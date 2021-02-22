package com.sim.baselibrary.utils;

import android.app.Instrumentation;
import android.widget.EditText;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author Sim --- 不常用的几个工具类
 */
public class MachineUtil {

    private static final String POWER_OFF_STM8 = "android.intent.action.POWER_OFF_STM8"; //通知stm8进行关机操作
    private static final String POWER_OFF_AMPLIFIER = "android.intent.action.POWER_OFF_AMPLIFIER"; //关闭功放操作


    /**
     * 关机
     *
     * @param
     */
    public static void shutdown() {
        String cmd[] = new String[]{"su", "-c", "reboot -p "};
        procCmd(cmd);
    }

    public static void reboot() {
        String cmd[] = new String[]{"su", "-c", "reboot"};
        procCmd(cmd);
    }

    /**
     * 直接运行shell命令
     *
     * @param cmd
     * @return
     */
    public static int procCmd(String cmd[]) {
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            try {
                proc.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return -1;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
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

    public static void disableShowInput(EditText et) {
        Class<EditText> cls = EditText.class;
        Method method;
        try {
            //setShowSoftInputOnFocus方法是EditText从TextView继承来的的
            //可以用来设置当EditText获得焦点时软键盘是否可见
            method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            method.setAccessible(true);
            method.invoke(et, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
