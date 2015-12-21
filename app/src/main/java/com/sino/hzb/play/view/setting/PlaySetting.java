package com.sino.hzb.play.view.setting;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sino.hzb.play.R;
import com.sino.hzb.play.model.PlayDataBean;
import com.sino.hzb.play.model.PlayDataType;
import com.sino.hzb.play.view.VideoPlayView;

import java.util.List;


/**
 * Created by hzb on 15/12/16.
 */
public abstract class PlaySetting extends AppCompatActivity {

    private Activity mActivity;

    /**
     * 视频播放控件
     */
    public VideoPlayView videoPlayView;

    /**
     * 视频信息集合
     */
    public List<PlayDataBean> playDataBeanList;

    /**
     * 当前播放的视频信息
     */
    public PlayDataBean currentPlay;

    public void intPlay(Activity mActivity,int videoPlayViewID,List<PlayDataBean> playDataBeanList) {
        this.mActivity=mActivity;
        this.playDataBeanList=playDataBeanList;
        this.currentPlay=playDataBeanList.get(0);
        this.videoPlayView = (VideoPlayView) findViewById(videoPlayViewID);
        videoPlayView.setOnPlayCompletedListener(onPlayCompletedListener);
        videoPlayView.initPlay(playDataBeanList.get(0), this);
        initVideoView();
    }


    /**
     * 初始化video控件
     */
    private void initVideoView() {
        /**
         * 设置屏幕旋转的监听
         */
        videoPlayView.setOnScreenChangedListener(new VideoPlayView.OnScreenChangedListener() {
            @Override
            public void onScreenChanged(boolean isFullScreen) {
                if (isFullScreen) {
                    showLandscapeFullScreen();
                } else {
                    showPortraitScreen();
                }
            }
        });

        videoPlayView.rl_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (currentPlay.getPlayDataType() == PlayDataType.ImageAdvertisement || currentPlay.getPlayDataType() == PlayDataType.VideoAdvertisement) {
                    videoPlayView.iv_music.setImageResource(R.mipmap.video_btn_mute_verticalscreen);
                    videoPlayView.playStreamMute(true);
                } else {
                    videoPlayView.iv_music.setImageResource(R.mipmap.video_btn_voice_verticalscreen);
                    videoPlayView.playStreamMute(false);
                }
            }
        });
    }

    /**
     * 连续播放的监听
     */
    private VideoPlayView.OnPlayCompletedListener onPlayCompletedListener = new VideoPlayView.OnPlayCompletedListener() {

        @Override
        public boolean onPlayNext(int videoId) {
            if (playDataBeanList == null) {
                videoPlayView.onDestroy();
                //initVideoView(data);
                showPortraitScreen();
                videoPlayView.showStatus(VideoPlayView.Status.Init);
                return false;
            }
            if (playDataBeanList.get(playDataBeanList.size() - 1).getVideoId() == videoId) {
                videoPlayView.onDestroy();
                //initVideoView(data);
                showPortraitScreen();
                videoPlayView.showStatus(VideoPlayView.Status.Init);
                return false;
            }

            PlayDataBean yyPlayVideoBean = getNextBoughtCourse(playDataBeanList, videoId);
            if (yyPlayVideoBean == null) {
                return false;
            }
            currentPlay = yyPlayVideoBean;
            return playCourse(currentPlay);
        }

        @Override
        public boolean isHasNext(int videoId) {
            PlayDataBean yyPlayVideoBean = getNextBoughtCourse(playDataBeanList, videoId);
            return yyPlayVideoBean != null && yyPlayVideoBean.getVideoId() != videoId;
        }

        @Override
        public boolean isNeedPlayLoop() {
            return true;
        }

        @Override
        public void onStartPlay(int videoId) {
        }
    };


    /**
     * 获得下一个要播放的视频
     *
     * @param videos
     * @param id
     * @return
     */
    private PlayDataBean getNextBoughtCourse(List<PlayDataBean> videos, int id) {
        if (videos == null || videos.size() <= 0) {
            return null;
        }
        int index = 0;
        if (id >= 0) {
            for (PlayDataBean playDataBean : videos) {
                if (playDataBean != null && playDataBean.getVideoId() == id) {
                    index++;
                    break;
                } else {
                    index++;
                }
            }
        }
        if (index <= 0 || index >= videos.size()) {
            for (PlayDataBean playDataBean : videos) {
                if (playDataBean != null) {
                    return playDataBean;
                }
            }
            return null;
        } else {
            for (int i = index; i < videos.size(); i++) {
                PlayDataBean playDataBean = videos.get(i);
                if (playDataBean != null) {
                    return playDataBean;
                }
            }
            for (int i = 0; i < index; i++) {
                PlayDataBean playDataBean = videos.get(i);
                if (playDataBean != null) {
                    return playDataBean;
                }
            }
        }
        return null;
    }

    /**
     * 设置要播放的视频
     *
     * @param playDataBean 要播放的视频信息
     * @return
     */
    private boolean playCourse(PlayDataBean playDataBean) {
        if (playDataBean != null) {
            currentPlay = playDataBean;
            videoPlayView.startPlay(playDataBean);

            if (videoPlayView.isFullScreen) {
                videoPlayView.currentDuration.setVisibility(View.VISIBLE);
                videoPlayView.fullDuration.setVisibility(View.VISIBLE);
            } else {
                videoPlayView.playProgressText.setVisibility(View.VISIBLE);
            }

            if (playDataBean.getPlayDataType() == PlayDataType.ImageAdvertisement || playDataBean.getPlayDataType() == PlayDataType.VideoAdvertisement) {
                videoPlayView.playStreamMute(false);
            }
            return true;
        }
        return false;
    }


    /**
     * 切换为竖屏
     */
    public void showPortraitScreen() {
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mActivity.getWindow().setAttributes(params);
        mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        videoPlayView.ib_advertisement_controll_full_screen
                .setImageResource(R.drawable.player_inline_fullscreen_btn_selector);
        showPortraitScreenFunction();

    }

    /**
     * 切换为竖屏之后，触发的事件
     */
    public void showPortraitScreenFunction() {
        if (currentPlay.getPlayDataType() == PlayDataType.ImageAdvertisement || currentPlay.getPlayDataType() == PlayDataType.VideoAdvertisement) {
            videoPlayView.hidePlayControllBar();
        }
    }

    /**
     * 切换为横屏
     */
    public void showLandscapeFullScreen() {
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        mActivity.getWindow().setAttributes(params);
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        videoPlayView.setControllView();
        videoPlayView.ib_advertisement_controll_full_screen
                .setImageResource(R.drawable.player_inline_smallscreen_btn_selector);
        showLandscapeFullScreenFunction();
    }

    /**
     * 切换为横屏之后，触发的方法
     */
    public void showLandscapeFullScreenFunction() {
    }

    /**
     * 横竖屏切换之后，视频布局
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            videoPlayView.setLayoutParams(layoutParams);
            videoPlayView.onConfigurationChanged(true);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    getResources().getDimensionPixelSize(R.dimen.coursemain_videoplay_height));
            videoPlayView.setLayoutParams(layoutParams);
            videoPlayView.onConfigurationChanged(false);
        }
        if (currentPlay.getPlayDataType()== PlayDataType.ImageAdvertisement||currentPlay.getPlayDataType()== PlayDataType.VideoAdvertisement) {
            videoPlayView.hidePlayControllBar();
        }
    }

    public void updateHadByTime(int bytime) {
    }
}
