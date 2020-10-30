package com.sim.test.Iflytek;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.sim.test.R;
import com.sim.test.utils.JsonParser;
import com.sim.test.utils.NetUtils;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.util.ResourceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Sim on 2019/10/29
 */
public abstract class SpeechRecognition {
    private static final String TAG = "Sim_SpeechRecognition";
    private Context mContext;

    private WakeUp wakeUp;
    private String[] wake_up_responds;

    private SpeechSynthesis speechSynthesis;

    //引擎类型
    private String mEngineType;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    //语音识别对象
    private SpeechRecognizer mIat;
    private String[] speechRecognition_responds;

    /**
     * 识别的回调
     */
    public abstract void SpeechRecognition();

    public SpeechRecognition(Context context) {
        this.mContext = context;
        wake_up_responds = mContext.getResources().getStringArray(R.array.wakeUp_respond);
        speechRecognition_responds = mContext.getResources().getStringArray(R.array.speechRecognition_respond);
        //根据网络状态设置引擎模式
        if (NetUtils.isNetworkConnected(mContext)) {
            mEngineType = SpeechConstant.TYPE_CLOUD;
        } else {
            mEngineType = SpeechConstant.TYPE_LOCAL;
        }
        //初始化识别无UI识别对象
        mIat = SpeechRecognizer.createRecognizer(mContext, null);
        setSpeechRecognition();
        speechSynthesis = new SpeechSynthesis(mContext) {
            @Override
            public void SpeechSynthesis() {
                Log.d(TAG, "SpeechSynthesis: 合成结束，开始语音识别");
                startSpeechRecognition();
            }
        };
        wakeUp = new WakeUp(mContext) {
            @Override
            public void wakeUp() {
                Log.d(TAG, "wakeUp:唤醒成功,开始语音合成");
                wakeUp.stopWake();
                speechSynthesis.startSpeechSynthesizer(wake_up_responds[(int) (0 + Math.random() * wake_up_responds.length)]);
            }
        };
    }

    /**
     * 设置语音识别
     */
    private void setSpeechRecognition() {
        // 应用领域
        mIat.setParameter(SpeechConstant.PARAMS, "iat");
        //设置中文
//        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        //普通话
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
        //引擎设置
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        //离线本地资源路径
        mIat.setParameter(ResourceUtil.ASR_RES_PATH, getResourcePath());
        //返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入，=自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        //mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav")
    }

    /**
     * 获取离线听写的听写资源
     */
    private String getResourcePath() {
        StringBuffer tempBuffer = new StringBuffer();
        //识别通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(mContext, ResourceUtil.RESOURCE_TYPE.assets, "iat/common.jet"));
        tempBuffer.append(";");
        tempBuffer.append(ResourceUtil.generateResourcePath(mContext, ResourceUtil.RESOURCE_TYPE.assets, "iat/sms_16k.jet"));
        return tempBuffer.toString();
    }

    /**
     * 开始识别
     */
    public boolean startSpeechRecognition() {
        Log.d(TAG, "startSpeechRecognition: " + mEngineType);
        mIat = SpeechRecognizer.getRecognizer();
        if (mIat != null) {
            mIat.startListening(mRecoListener);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 停止识别
     */
    public boolean stopSpeechRecognition() {
        mIat = SpeechRecognizer.getRecognizer();
        if (mIat.isListening()) {
            mIat.stopListening();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否处于识别状态
     *
     * @return
     */
    public boolean isSpeechRecgnitionStatus() {
        return mIat.isListening();
    }

    /**
     * 释放
     */
    public void destroy() {
        if (mIat != null) {
            mIat.stopListening();
            mIat.destroy();
        }
    }

    /**
     * 初始化语音识别监听
     */
    private RecognizerListener mRecoListener = new RecognizerListener() {

        //音量0-30
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {
        }

        //开始录音
        @Override
        public void onBeginOfSpeech() {
            Log.d(TAG, "onBeginOfSpeech: 开始识别");
        }

        //结束录音
        @Override
        public void onEndOfSpeech() {
            mIat.stopListening();
            Log.d(TAG, "onBeginOfSpeech: 结束录音");
        }

        //返回结果
        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            Log.d(TAG, "onResult: 获取结果" + recognizerResult.getResultString());
            if (NetUtils.isNetworkConnected(mContext)) {
                respond(recognizerResult);
            } else {
                speechSynthesis.startSpeechSynthesizer(mContext.getResources().getStringArray(R.array.net)[0]);
            }
        }

        @Override
        public void onError(SpeechError speechError) {
            SpeechRecognition();
            wakeUp.startWake();
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {
        }
    };

    /**
     * 语音识别输出结果
     *
     * @param results
     */
    private void respond(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());
        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        Log.d(TAG, "printResult: text: " + text);
        Log.d(TAG, "printResult: resultBuffer: " + resultBuffer.toString());

        if (resultBuffer.toString().contains("你") && resultBuffer.toString().contains("什么") && resultBuffer.toString().contains("用")) {
            speechSynthesis.startSpeechSynthesizer(mContext.getResources().getStringArray(R.array.speechRecognition_respond_success)[8]);
        } else if (resultBuffer.toString().contains("你在干嘛")) {
            speechSynthesis.startSpeechSynthesizer(mContext.getResources().getStringArray(R.array.speechRecognition_respond_success)[9]);
        } else {
            speechSynthesis.startSpeechSynthesizer(speechRecognition_responds[(int) (0 + Math.random() * speechRecognition_responds.length)]);
        }
    }

}
