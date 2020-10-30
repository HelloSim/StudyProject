package com.sim.test.Iflytek;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.sim.test.R;
import com.sim.test.utils.NetUtils;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.util.ResourceUtil;

/**
 * Created by Sim on 2019/10/29
 */
public abstract class SpeechSynthesis {
    private static final String TAG = "Sim_SpeechSynthesis";
    private Context mContext;

    private WakeUp wakeUp;

    private String[] wake_up_responds;

    //引擎类型
    private String mEngineType;

    // 语音合成对象
    private SpeechSynthesizer mTts;
    // 默认发音人
    private String voicer = "xiaoyan";
    // 缓冲进度
    private int mPercentForBuffering = 0;
    // 播放进度
    private int mPercentForPlaying = 0;

//    //云端语法文件
//    private String mCloudGrammar = null;
//    //云端语法文件ID
//    private String mCloudGrammarID;
//    //本地语法文件ID
//    private String mLocalGrammarID;
//    //本地语法文件
//    private String mLocalGrammar = null;
//    //本地语法构建路径
////    private String grmPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/test";//getResource()

    /**
     * 合成的回调
     */
    public abstract void SpeechSynthesis();

    public SpeechSynthesis(Context context) {
        this.mContext = context;

        wake_up_responds = mContext.getResources().getStringArray(R.array.wakeUp_respond);

        //根据网络状态设置引擎模式
        if (NetUtils.isNetworkConnected(mContext)) {
            mEngineType = SpeechConstant.TYPE_CLOUD;
        } else {
            mEngineType = SpeechConstant.TYPE_LOCAL;
        }
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener);
        setSpeechSynthesis();
        wakeUp = new WakeUp(mContext) {
            @Override
            public void wakeUp() {
                Log.d(TAG, "wakeUp:唤醒成功,开始语音合成");
                wakeUp.stopWake();
                startSpeechSynthesizer(wake_up_responds[(int) (0 + Math.random() * wake_up_responds.length)]);
            }
        };
    }

    /**
     * 获取发音人资源路径
     */
    private String getResourcePath() {
        StringBuffer tempBuffer = new StringBuffer();
        //合成通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(mContext, ResourceUtil.RESOURCE_TYPE.assets, "tts/common.jet"));
        tempBuffer.append(";");
        //发音人资源
        tempBuffer.append(ResourceUtil.generateResourcePath(mContext, ResourceUtil.RESOURCE_TYPE.assets, "tts/" + voicer + ".jet"));
        return tempBuffer.toString();
    }

    /**
     * 语音合成参数设置
     *
     * @return
     */
    private void setSpeechSynthesis() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            //设置引擎云端
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
//            //支持实时音频返回，仅在synthesizeToUri条件下支持
//            mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY, "1");
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, "50");
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, "50");
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, "50");
        } else {
            //设置引擎本地
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
//            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            mTts.setParameter(ResourceUtil.TTS_RES_PATH, getResourcePath());
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, "50");
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, "50");
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, "50");
        }
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
        //淡入淡出
        mTts.setParameter(SpeechConstant.TTS_FADING, "true");
//        //设置播放器音频流类型
//        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
//        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
//        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "pcm");
//        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.pcm");
    }

    /**
     * 开始合成
     *
     * @param texts
     * @return
     */
    public boolean startSpeechSynthesizer(String texts) {
        Log.d(TAG, "startSpeechSynthesizer: " + mEngineType);
        mTts = SpeechSynthesizer.getSynthesizer();
        if (mTts != null) {
            mTts.startSpeaking(texts, mTtsListener);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 停止合成
     */
    public boolean stopSpeechSynthesizer() {
        mTts = SpeechSynthesizer.getSynthesizer();
        if (mTts.isSpeaking()) {
            mTts.stopSpeaking();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否处于合成状态
     *
     * @return
     */
    public boolean isSpeechSynehesisStatus() {
        return mTts.isSpeaking();
    }

    /**
     * 释放
     */
    public void destroy() {
        if (mTts != null) {
            mTts.stopSpeaking();
            mTts.destroy();
        }
    }

    /**
     * 初始化语音合成状态监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Log.d(TAG, "onInit: 合成状态错误码：" + code);
            }
        }
    };

    /**
     * 语音合成回调监听
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {
            Log.d(TAG, "onSpeakBegin: 开始播放");
        }

        @Override
        public void onSpeakPaused() {
            Log.d(TAG, "onSpeakPaused: 暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            Log.d(TAG, "onSpeakResumed: 继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
            // 合成进度
            mPercentForBuffering = percent;
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            mPercentForPlaying = percent;
        }

        @Override
        public void onCompleted(SpeechError speechError) {
            if (speechError == null) {
                Log.d(TAG, "onCompleted: 播放完成");
                SpeechSynthesis();
            } else {
                Log.d(TAG, "onCompleted: " + speechError.toString());
                if (speechError.getErrorCode() == ErrorCode.ERROR_NO_NETWORK || speechError.getErrorCode() == ErrorCode.ERROR_NETWORK_TIMEOUT
                        || speechError.getErrorCode() == ErrorCode.ERROR_NET_EXCEPTION) {
                    Toast.makeText(mContext, mContext.getString(R.string.check_net), Toast.LENGTH_SHORT).show();
                    wakeUp.startWake();
                } else if (speechError.getErrorCode() == ErrorCode.ERROR_EMPTY_UTTERANCE) {//文本无效
                    wakeUp.startWake();
                }
                wakeUp.startWake();
            }
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

}
