package com.sim.traveltool.receiver;

import android.content.Context;

import cn.jpush.android.service.WakedResultReceiver;

/**
 * @author Sim --- JPush 自定义 Receiver，复写onWake(int wakeType)或 onWake(Context context, int wakeType)方法以监听被拉起
 * 需要继承 JPush 提供的 WakedResultReceiver类
 */
public class MyWakedResultReceiver extends WakedResultReceiver {

    @Override
    public void onWake(Context context, int i) {
        super.onWake(context, i);
    }

}
