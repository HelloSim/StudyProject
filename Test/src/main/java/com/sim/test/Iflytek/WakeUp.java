package com.sim.test.Iflytek;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.sim.test.R;
import com.sim.test.utils.JsonParser;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.VoiceWakeuper;
import com.iflytek.cloud.WakeuperListener;
import com.iflytek.cloud.WakeuperResult;
import com.iflytek.cloud.util.ResourceUtil;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Sim on 2019/10/29
 */
public abstract class WakeUp {
    private static final String TAG = "Sim_WakeUp";
    private Context mContext;

    //引擎类型
    private String mEngineType = SpeechConstant.TYPE_LOCAL;

    // 语音唤醒对象
    private VoiceWakeuper mIvw;
    //唤醒结果内容
    private String resultString;
    //识别结果内容
    private String recoString;
    //设置门限值
    private final static int MAX = 3000;
    private final static int MIN = 1500;
    private int curThresh = MIN;


    /**
     * 唤醒的回调
     */
    public abstract void wakeUp();

    public WakeUp(Context context) {
        mContext = context;
        // 初始化唤醒对象
        mIvw = VoiceWakeuper.createWakeuper(context, null);
        setWakeUp();
    }

    /**
     * 获取唤醒词资源路径
     */
    private String getResourcePath() {
        final String resPath = ResourceUtil.generateResourcePath(mContext, ResourceUtil.RESOURCE_TYPE.assets, "ivw/" + mContext.getString(R.string.IflytekAPP_id) + ".jet");
        return resPath;
    }

    /**
     * 设置唤醒参数
     */
    public boolean setWakeUp() {
        mIvw = VoiceWakeuper.getWakeuper();
        // 非空判断，防止因空指针使程序崩溃
        if (mIvw != null) {
            // 清空参数
            mIvw.setParameter(SpeechConstant.PARAMS, null);
            //设置引擎
            mIvw.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
            // 设置唤醒资源路径
            mIvw.setParameter(SpeechConstant.IVW_RES_PATH, getResourcePath());
            // 唤醒门限值，根据资源携带的唤醒词个数按照“id:门限;id:门限”的格式传入
            mIvw.setParameter(SpeechConstant.IVW_THRESHOLD, "0:" + curThresh);
            // 设置唤醒模式,唤醒+识别模式是oneshot
            mIvw.setParameter(SpeechConstant.IVW_SST, "wakeup");
            // 设置开启优化功能
            mIvw.setParameter(SpeechConstant.IVW_NET_MODE, "1");
            // 设置持续进行唤醒
            mIvw.setParameter(SpeechConstant.KEEP_ALIVE, "1");
            //设置返回结果格式
            mIvw.setParameter(SpeechConstant.RESULT_TYPE, "json");
            return true;
        } else {
            Log.d(TAG, "wake: 唤醒未初始化");
            return false;
        }
    }

    /**
     * 开启唤醒
     */
    public boolean startWake() {
        mIvw = VoiceWakeuper.getWakeuper();
        if (mIvw != null) {
            mIvw.startListening(mWakeuperListener);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 停止唤醒
     */
    public boolean stopWake() {
        mIvw = VoiceWakeuper.getWakeuper();
        if (mIvw != null) {
            mIvw.stopListening();
            Log.d(TAG, "stopWake: 停止唤醒");
            return true;
        } else {
            Log.d(TAG, "wake: 唤醒未初始化");
            return false;
        }
    }

    /**
     * 是否处于可唤醒状态
     *
     * @return
     */
    public boolean isWakeStatus() {
        return mIvw.isListening();
    }

    /**
     * 释放
     */
    public void destroy() {
        if (mIvw != null) {
            mIvw.stopListening();
            mIvw.destroy();
        }
    }

    /**
     * 语音唤醒识别监听
     */
    private WakeuperListener mWakeuperListener = new WakeuperListener() {
        /**
         * 返回结果 返回的结果可能为null
         * @param result
         */
        @Override
        public void onResult(WakeuperResult result) {
            try {
                String text = result.getResultString();
                JSONObject object;
                object = new JSONObject(text);
//                StringBuffer buffer = new StringBuffer();
//                buffer.append("【RAW】 " + text);
//                buffer.append("\n");
//                buffer.append("【操作类型】" + object.optString("sst"));
//                buffer.append("\n");
//                buffer.append("【唤醒词id】" + object.optString("id"));
//                buffer.append("\n");
//                buffer.append("【得分】" + object.optString("score"));
//                buffer.append("\n");
//                buffer.append("【前端点】" + object.optString("bos"));
//                buffer.append("\n");
//                buffer.append("【尾端点】" + object.optString("eos"));
                resultString = object.optString("keyword");
                Log.d(TAG, "onResult: 唤醒结果内容:" + resultString);
                wakeUp();
            } catch (JSONException e) {
                resultString = "唤醒结果解析出错";
                Log.d(TAG, "onResult: " + resultString);
                e.printStackTrace();
            }
        }

        /**
         * 错误回调 当此函数回调时，说明当次会话出现错误，会话自动结束，录音也会停止。
         * @param error
         */
        @Override
        public void onError(SpeechError error) {
            Log.i(TAG, error.getPlainDescription(true));
            setWakeUp();
        }

        /**
         * 开始说话 在录音模式(音频源参数设为 > -1时 )下，调用开始录音函数后，会自动开启系统的录音机，
         * 并在录音机开启后，会回调此函数（这中间的过程应该在几毫秒内，可以忽略，除非系统响应很慢）。
         */
        @Override
        public void onBeginOfSpeech() {
            Log.d(TAG, "开始唤醒");
        }

        /**
         * 事件 扩展用接口，唤醒的主要事件是音频事件，以及在唤醒识别时，返回识别结果（在唤醒结果之后返回）。
         * @param eventType
         * @param isLast
         * @param arg2
         * @param obj
         */
        @Override
        public void onEvent(int eventType, int isLast, int arg2, Bundle obj) {
            Log.d(TAG, "onEvent: " + eventType);
            if (eventType == SpeechEvent.EVENT_IVW_RESULT) {
                RecognizerResult result = (RecognizerResult) obj.get(SpeechEvent.KEY_EVENT_IVW_RESULT);
                recoString += JsonParser.parseGrammarResult(result.getResultString());
                Log.d(TAG, "onEvent: " + recoString);
            }
        }

        /**
         * 音量变化 当开始录音，到停止录音（停止写入音频流）为止，音量变化时，会多次通过此函数回调，告知应用层当前的音量值。
         * @param i
         */
        @Override
        public void onVolumeChanged(int i) {

        }
    };

}
