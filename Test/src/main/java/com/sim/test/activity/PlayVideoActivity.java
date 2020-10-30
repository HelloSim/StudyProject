package com.sim.test.activity;

import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.Nullable;

import com.sim.test.R;
import com.sim.test.activity.base.BaseActivity;

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * 引用第三方库播放视频
 */

public class PlayVideoActivity extends BaseActivity {

    private JCVideoPlayerStandard jcVideoPlayerStandard;
    private String videoPath = Environment.getExternalStorageDirectory()+"/Sim/白色.avi";
    private  String videoName = getFileName(videoPath);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        jcVideoPlayerStandard = findViewById(R.id.videoplay);
        jcVideoPlayerStandard.setUp(videoPath, JCVideoPlayerStandard.CURRENT_STATE_NORMAL, videoName);
        jcVideoPlayerStandard.onCompletion();
        jcVideoPlayerStandard.prepareMediaPlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCMediaManager.instance().mediaPlayer.pause();
    }

    /**
     * 获取文件名
     *
     * @param pathandname
     * @return
     */
    public String getFileName(String pathandname) {
        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, end);
        } else {
            return null;
        }
    }

}
