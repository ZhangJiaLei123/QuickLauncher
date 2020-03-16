package com.heneng.launcher.view.activity;

import android.os.Bundle;

import com.heneng.launcher.R;
import com.heneng.launcher.model.MediaModel;
import com.heneng.launcher.view.base.BaseActivity;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class VideoActivity extends BaseActivity {

    private JCVideoPlayerStandard jcVideoPlayerStandard;
    public static final String VIDEO = "Video";
    private MediaModel mMediaModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        mMediaModel = getIntent().getParcelableExtra(VideoActivity.VIDEO);

        JCVideoPlayer.TOOL_BAR_EXIST = false;
        JCVideoPlayer.ACTION_BAR_EXIST = false;

        jcVideoPlayerStandard = (JCVideoPlayerStandard) findViewById(R.id.video_player);

        jcVideoPlayerStandard.setUp(mMediaModel.getVideoUrl()
                , JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, mMediaModel.getTitle());
        jcVideoPlayerStandard.startPlayLocic();
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
}
