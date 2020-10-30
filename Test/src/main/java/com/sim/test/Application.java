package com.sim.test;


import android.content.Context;

import com.sim.test.R;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * 当应用程序进程被启动时，这个类在其他任何应用程序组件被实例化之前实例化。
 *
 */

public class Application extends android.app.Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID + "=" + getString(R.string.IflytekAPP_id));
    }

    public static Context getContext(){
        return context;
    }

}
