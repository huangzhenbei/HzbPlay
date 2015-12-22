package com.sino.hzb.play.view.setting;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sino.hzb.play.R;
import com.sino.hzb.play.model.PlayDataBean;
import com.sino.hzb.play.model.PlayDataType;
import com.sino.hzb.play.util.NetworkUtils;
import com.sino.hzb.play.util.StringUtils;
import com.sino.hzb.play.util.ToastManager;
import com.sino.hzb.play.view.CustomeDialog;
import com.sino.hzb.play.view.DialogUtils;
import com.sino.hzb.play.view.VideoChapter;
import com.sino.hzb.play.view.VideoPlayView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by hzb on 15/12/16.
 */
public abstract class PlaySetting extends AppCompatActivity implements IPlaySetting {

    private Activity mActivity;

    /**
     * popuwindow的view
     */
    public View mContentView;

    /**
     * popupwindow控制器
     */
    public VideoPlayPupWindowSetting mIVideoPlayPupWindowSetting;

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

    @Override
    public void intPlay(Activity mActivity, int videoPlayViewID, List<PlayDataBean> playDataBeanList) {
        this.mActivity = mActivity;
        this.videoPlayView = (VideoPlayView) findViewById(videoPlayViewID);
        this.playDataBeanList = playDataBeanList;

        this.currentPlay = playDataBeanList.get(0);
        videoPlayView.setOnPlayCompletedListener(onPlayCompletedListener);
        videoPlayView.initPlay(playDataBeanList.get(0), this);
        initVideoView();

        List<VideoChapter> anchors = new ArrayList<VideoChapter>();
        VideoChapter courseChapter = new VideoChapter();
        courseChapter.anchor_time = 20;
        courseChapter.anchor_title = "aaaaaaaa";
        anchors.add(courseChapter);

        courseChapter = new VideoChapter();
        courseChapter.anchor_time = 40;
        courseChapter.anchor_title = "bbbbbbb";
        anchors.add(courseChapter);

        courseChapter = new VideoChapter();
        courseChapter.anchor_time = 60;
        courseChapter.anchor_title = "啊啊啊啊啊啊";
        anchors.add(courseChapter);

        courseChapter = new VideoChapter();
        courseChapter.anchor_time = 80;
        courseChapter.anchor_title = "阿拉山口大陆架三打两建";
        anchors.add(courseChapter);

        this.videoPlayView.hotPointParent.setAnchors(anchors);
    }

    /**
     * 初始化video控件
     */
    @Override
    public void initVideoView() {
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
    @Override
    public PlayDataBean getNextBoughtCourse(List<PlayDataBean> videos, int id) {
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
                if (videos.get(i) != null) {
                    return videos.get(i);
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
    @Override
    public boolean playCourse(PlayDataBean playDataBean) {
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
    @Override
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
    @Override
    public void showPortraitScreenFunction() {
        if (currentPlay.getPlayDataType() == PlayDataType.ImageAdvertisement || currentPlay.getPlayDataType() == PlayDataType.VideoAdvertisement) {
            videoPlayView.hidePlayControllBar();
        }
    }

    /**
     * 切换为横屏
     */
    @Override
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
    @Override
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
        if (currentPlay.getPlayDataType() == PlayDataType.ImageAdvertisement || currentPlay.getPlayDataType() == PlayDataType.VideoAdvertisement) {
            videoPlayView.hidePlayControllBar();
        }
        videoPlayView.hotPointParent.hideAnchorWindow();
        videoPlayView.hotPointParent.setVisibility(videoPlayView.isFullScreen ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (currentPlay != null) {
            if (videoPlayView != null) {
                if (currentPlay.getPlayDataType().ImageAdvertisement == PlayDataType.ImageAdvertisement || currentPlay.getPlayDataType().VideoAdvertisement == PlayDataType.VideoAdvertisement) {
                    videoPlayView.onDestroy();
                    videoPlayView.handleStop();
                    videoPlayView.showStatus(VideoPlayView.Status.Init);
                } else {
                    if (videoPlayView.isPlaying()) {
                        videoPlayView.handleStop();
                        videoPlayView.showStatus(VideoPlayView.Status.Pause);
                    }
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (currentPlay != null) {
            if (currentPlay.getPlayDataType().ImageAdvertisement == PlayDataType.ImageAdvertisement || currentPlay.getPlayDataType().VideoAdvertisement == PlayDataType.VideoAdvertisement) {
                videoPlayView.onDestroy();
                //initPlayData();
                videoPlayView.initPlay(playDataBeanList.get(0), this);
                videoPlayView.hidePlayControllBar();
                videoPlayView.showStatus(VideoPlayView.Status.Init);
                return;
            } else if (currentPlay.getPlayDataType() == PlayDataType.Video) {
                return;
            } else {
                videoPlayView.hidePlayControllBar();
                videoPlayView.showStatus(VideoPlayView.Status.Init);
                return;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { //按下的如果是BACK，同时没有重复
            showPortraitScreen();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 标题栏，左侧按钮
     */
    @Override
    public void onTitleLeftImageBackOnClick() {
        videoPlayView.showVideoSmallScreen();
    }

    /**
     * 标题栏，右侧按钮
     */
    @Override
    public void onTitleRightTextOnClick() {
        mIVideoPlayPupWindowSetting.popupWindonViewSetting();
    }

    /**
     * 暂停或继续播放
     */
    @Override
    public void onStopOrPause() {
        if (NetworkUtils.getNetworkType(mActivity) == 0) {
            ToastManager.getInstance().showToast(mActivity, "找不到网络....T.T");
            videoPlayView.isStarted = false;
            videoPlayView.handleStop();
        }
        if (videoPlayView.isStarted) {
            videoPlayView.isStarted = false;
            videoPlayView.handleStop();
        } else {
            String strNetworkTypeName = NetworkUtils.getNetWorkTypeName(mActivity);
            if (!StringUtils.isNull(strNetworkTypeName)) {
                DialogUtils.dialogMessage(mActivity, "友情提醒", "您当前是" + strNetworkTypeName + "会产生流量费用,请选择是否继续播放", "确定", "取消",
                        new CustomeDialog.OnCustomeDialogClickListener() {
                            @Override
                            public void onCustomeDialogClick(CustomeDialog.CustomeDialogClickType type) {
                                switch (type) {
                                    case Ok:
                                        videoPlayView.isStarted = true;
                                        videoPlayView.handlePlay();
                                        break;
                                    case Cancel:
                                        videoPlayView.isStarted = false;
                                        videoPlayView.handleStop();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        });
            } else {
                videoPlayView.isStarted = true;
                videoPlayView.handlePlay();
            }
        }
    }

    /**
     * 第一播放视频
     */
    @Override
    public void onFirstPlay() {
        if (NetworkUtils.getNetworkType(mActivity) == 0) {
            Toast.makeText(mActivity, "找不到网络....T.T", Toast.LENGTH_SHORT).show();
            return;
        }
        String strNetworkTypeName = NetworkUtils.getNetWorkTypeName(mActivity);
        if (!StringUtils.isNull(strNetworkTypeName)) {
            DialogUtils.dialogMessage(mActivity, "友情提醒", "您当前是" + strNetworkTypeName + "会产生流量费用,请选择是否继续播放", "确定", "取消",
                    new CustomeDialog.OnCustomeDialogClickListener() {
                        @Override
                        public void onCustomeDialogClick(CustomeDialog.CustomeDialogClickType type) {
                            switch (type) {
                                case Ok:
                                    videoPlayView.bigPlayBtn.setVisibility(View.GONE);
                                    videoPlayView.firstPlay();
                                    break;
                                case Cancel:
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
        } else {
            videoPlayView.bigPlayBtn.setVisibility(View.GONE);
            videoPlayView.firstPlay();
        }
        /**
         * 隐藏横屏，时间进度view
         */
        videoPlayView.fullDuration.setVisibility(View.GONE);
        videoPlayView.currentDuration.setVisibility(View.GONE);
    }

    /**
     * 时间轴
     *
     * @param bytime 当前时间
     */
    @Override
    public void updateHadByTime(int bytime) {
        if (bytime == 10 || bytime == 30 || bytime == 50 || bytime == 70) {
            ToastManager.getInstance().showToast(this, "时间轴：" + bytime, 1);
        }
    }

    @Override
    public Activity getActivity() {
        return mActivity;
    }
}
