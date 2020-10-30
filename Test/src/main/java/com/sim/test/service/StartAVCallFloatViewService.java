package com.sim.test.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.sim.test.Iflytek.SpeechRecognition;
import com.sim.test.Iflytek.SpeechSynthesis;
import com.sim.test.Iflytek.WakeUp;
import com.sim.test.R;
import com.sim.test.view.SuspensionBall.FloatWindowManager;

/**
 * 启动悬浮球的服务
 */
@SuppressLint("Registered")
public class StartAVCallFloatViewService extends Service {

    private String TAG = "Sim";
    private WakeUp wakeUp;
    private SpeechSynthesis speechSynthesis;
    private SpeechRecognition speechRecognition;

    private String[] wake_up_responds;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FloatWindowManager.getInstance().showWindow(this);
        wake_up_responds = getResources().getStringArray(R.array.wakeUp_respond);
        initIfytek();
        wakeUp.startWake();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        wakeUp.destroy();
        speechSynthesis.destroy();
        speechRecognition.destroy();
        FloatWindowManager.getInstance().dismissWindow();
    }

    private void initIfytek() {
        wakeUp = new WakeUp(this) {
            @Override
            public void wakeUp() {
                Log.d(TAG, "wakeUp:唤醒成功,开始语音合成");
                wakeUp.stopWake();
                speechSynthesis.startSpeechSynthesizer(wake_up_responds[(int) (0 + Math.random() * wake_up_responds.length)]);
            }
        };
        speechSynthesis = new SpeechSynthesis(this) {
            @Override
            public void SpeechSynthesis() {
                Log.d(TAG, "SpeechSynthesis: 合成结束，开始语音识别");
                speechSynthesis.stopSpeechSynthesizer();
                speechRecognition.startSpeechRecognition();
            }
        };
        speechRecognition = new SpeechRecognition(this) {
            @Override
            public void SpeechRecognition() {
                Log.d(TAG, "SpeechRecognition: 识别结束,开始唤醒");
                speechRecognition.stopSpeechRecognition();
                wakeUp.startWake();
            }
        };
    }

}
