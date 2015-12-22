package com.sino.hzb.play.view;

import android.content.Context;

import java.util.List;

import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.sino.hzb.play.R;
import com.sino.hzb.play.model.PlayDataBean;
import com.sino.hzb.play.model.PlayDataType;
import com.sino.hzb.play.util.DisplayUtils;
import com.sino.hzb.play.util.ImageRender;
import com.sino.hzb.play.util.NetworkUtils;
import com.sino.hzb.play.util.ParamsUtil;
import com.sino.hzb.play.util.StringUtils;
import com.sino.hzb.play.util.ToastManager;
import com.sino.hzb.play.view.setting.PlaySetting;
import com.sino.hzb.play.view.setting.VideoPlayPupWindowSetting;


/**
 * Created by hzb on 15/12/9.
 */
public class VideoPlayView extends RelativeLayout implements HotPointParent.HotPointListener {
    public HotPointParent hotPointParent;

    public interface OnScreenChangedListener {
        public void onScreenChanged(boolean isFullScreen);
    }

    public interface OnPlayCompletedListener {
        public boolean onPlayNext(int videoId);

        public boolean isHasNext(int videoId);

        public boolean isNeedPlayLoop();

        public void onStartPlay(int videoId);
    }

    public static enum Status {
        Init, Loading, Playing, Pause, Finished, Error;
    }

    private static final String TAG = VideoPlayView.class.getSimpleName();
    private static int SWIPE_THRESHOLD;
    private static int DISPLAY_WIDTH;
    private static int DISPLAY_HEIGHT;

    private static final String START_DEFAULT_POSITION = "00:00";
    private static final String DEFAULT_PROGRESS = "00:00 / 00:00";
    private static final String PROGRESS_FORMAT = "%s / %s";
    private static final double DEFAULT_HEIGHT_RATIO = 0.5625;
    private double heightRatio = DEFAULT_HEIGHT_RATIO;

    // 视频播放布
    private SurfaceView playSurfaceView;
    private SurfaceHolder playSurfaceHolder;
    // 视频默认图片
    public ImageView coverImageView;
    // 标题view
    public View titleBar;
    // 返回键view
    private View backButtonContainer;
    // 返回键按钮
    private ImageButton backButton;
    // 标题text
    private TextView titleView;
    // 功能栏view
    public View playControllBar;
    private View playOrPauseContainer;
    private ImageButton playOrPauseBtn;
    // 播放进度条
    private SeekBar playSeekBar;
    public TextView currentDuration;
    public TextView fullDuration;
    public TextView playProgressText;
    private View fullScreenContainer;
    private ImageButton fullScreenBtn;
    public ImageButton bigPlayBtn;
    private View playBufferProgressBar;
    private View gestureContainer, gestureContainer2;
    private ImageView gestureTypeIcon;
    private TextView gestureTip, gestureTip2;
    // 媒体类
    public MediaPlayer player;
    private View errorViewContainer;
    private View maskView;
    // 广告按钮
    public RelativeLayout rl_music;
    // 静音按钮
    public ImageView iv_music;
    // 倒计时按钮和popupwindow发现按钮
    public TextView tv_countdown, tv_popuwindow;
    // 倒计时布局和放大缩小布局
    public LinearLayout ll_advertisement_time, ll_advertisement_screen;
    // 放大缩小按钮
    public ImageButton ib_advertisement_controll_full_screen;
    // 是否显示右侧的pupupWindow
    private boolean isShowPupupWindow;
    // 视频类型id
    private PlayDataBean playDataBean;
    // private int videoTypeIdColick;
    private boolean isSurfaceHolderCreated = false;
    public boolean isStarted = false;
    private boolean isPrepared;
    private boolean isSeekbarTouching;
    // 是否播放完毕
    private boolean isPlayFinished;
    // 现在时间
    private int currentPosition;// current play position
    // 总时间
    private int duration;// video total duration
    private int scrollToPosition = -1;
    private AudioManager audioManager;
    private int currentVolume;

    private Handler playerHandler;
    private CountDownTimer countDownTimer;

    private OnScreenChangedListener onScreenChangedListener;
    public boolean isFullScreen;
    private OnPlayCompletedListener onPlayCompletedListener;

    private PowerManager.WakeLock wakeLock;
    private boolean isFullScreenDisabled = false;

    /**
     * 当前亮度
     */
    private float mBrightness = -1f;
    // hzb
    private Context mContext;
    private PlaySetting playSetting;

    private int Max_X;
    private int Max_y;

    private VideoPlayPupWindowSetting playPupupWindowSetting;

    public VideoPlayView(Context context) {
        super(context);
        this.mContext = context;
    }

    public VideoPlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public VideoPlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    /**
     * 获取当前视频信息
     *
     * @return
     */
    public PlayDataBean getPlayDataBean() {
        return playDataBean;
    }

    /**
     * 设置popupwindow的布局和显示的数据
     *
     * @param playPupupWindowSetting
     */
    public void setVideoPlayPupupWindow(VideoPlayPupWindowSetting playPupupWindowSetting) {
        this.playPupupWindowSetting = playPupupWindowSetting;
    }


    public void showErrorStatus() {
        showStatus(Status.Error);
    }

    public MediaPlayer getPlayer() {
        return player;
    }

    public void setPlayer(MediaPlayer player) {
        this.player = player;
    }

    public void showLoadingStatus() {
        showStatus(Status.Loading);
    }

    public void startPlay() {
        handlePlay();
    }


    /**
     * 初始化播放视频信息
     *
     * @param playDataBean
     */
    public void initPlay(PlayDataBean playDataBean, PlaySetting playSetting) {
        if (playDataBean == null) {
            return;
        }
        this.playSetting = playSetting;
        this.playDataBean = playDataBean;
        if (isSurfaceHolderCreated && this.isStarted) {
            initPlayInfo();
            initPlayHander();
        }
        if (TextUtils.isEmpty(playDataBean.getImageStringUrl())) {
            coverImageView.setVisibility(View.GONE);
        } else {
            if (player != null && player.isPlaying()) {
                coverImageView.setVisibility(View.GONE);
            } else {
                coverImageView.setVisibility(View.VISIBLE);
            }
        }
        ImageRender.getInstance().setImage(coverImageView, playDataBean.getImageStringUrl(), 0);
        titleView.setText(playDataBean.getVideoTitle());


    }

    public void startPlay(PlayDataBean playDataBean) {
        this.isStarted = true;
        this.isPlayFinished = false;
        if (this.playDataBean.getVideoId() == playDataBean.getVideoId()) {
            this.currentPosition = 0;
            handlePlay(true);
        } else {
            this.playDataBean = playDataBean;
            this.playDataBean.setVideoId(playDataBean.getVideoId());
            this.playDataBean.setVoidStringUrl(playDataBean.getVoidStringUrl());
            this.currentPosition = playDataBean.getImageAdvertisementPosition();
            playSeekBar.setProgress(0);
            currentDuration.setText(START_DEFAULT_POSITION);
            playProgressText.setText(DEFAULT_PROGRESS);
            startPlayNext();
        }

    }

    private void startPlayNext() {
        this.isStarted = true;
        if (this.player == null) {
            startPlay();
        } else {
            if (!TextUtils.isEmpty(this.playDataBean.getVoidStringUrl()) || isLocalPlay()) {
                initPlayInfo();
                initPlayHander();
                showStatus(Status.Loading);
            } else {
                if (this.player.isPlaying()) {
                    this.player.pause();
                }
                this.player.reset();
                this.isPrepared = false;
                showStatus(Status.Init);
                if (!checkPlayAvailable()) {
                    return;
                }
            }
        }
    }

    public void setHeightRatio(double ratio) {
        if (ratio != heightRatio) {
            heightRatio = ratio;
            requestLayout();
        }
    }

    public double getHeightRatio() {
        return heightRatio;
    }

    public void disableFullScreenMode() {
        isFullScreenDisabled = true;
        fullScreenContainer.setVisibility(View.INVISIBLE);
    }

    public void enableFullScreenMode() {
        isFullScreenDisabled = false;
        fullScreenContainer.setVisibility(View.VISIBLE);
    }

    public boolean isFullScreenEnable() {
        return isFullScreenDisabled;
    }

    // cacel playing, not buy
    public void cancelPlay() {
        this.isStarted = false;
        showStatus(Status.Init);
    }

    public void hideFullScreenBtn() {
        if (this.fullScreenBtn != null) {
            this.fullScreenBtn.setVisibility(View.GONE);
        }
    }

    public void setOnScreenChangedListener(OnScreenChangedListener listener) {
        this.onScreenChangedListener = listener;
    }

    public void setOnPlayCompletedListener(OnPlayCompletedListener listener) {
        this.onPlayCompletedListener = listener;
    }

    public void onConfigurationChanged(boolean isLandscape) {
        isFullScreen = isLandscape;
        LayoutParams params = getScreenSizeParams(1, isFullScreen);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        playSurfaceView.setLayoutParams(params);
        if (!isFullScreen) {
            if (bigPlayBtn.getVisibility() != View.VISIBLE) {
                showlayControllBar(true);
                // playControllBar.setVisibility(View.VISIBLE);

            }
            fullScreenBtn.setImageResource(R.drawable.player_inline_fullscreen_btn_selector);
            titleBar.setVisibility(View.GONE);
            playProgressText.setVisibility(View.VISIBLE);
            currentDuration.setVisibility(View.GONE);
            fullDuration.setVisibility(View.GONE);
            // CourserApplication.getStatistical().onEvent(Statistical.KEY_COURSER_VIDEO_VIDEOSMALL);
        } else {
            // playControllBar.setVisibility(View.VISIBLE);
            showlayControllBar(true);
            bigPlayBtn.setVisibility(View.GONE);
            if (playControllBar.getVisibility() == View.VISIBLE) {
                postDelayHidePlayControllerBar();
            }
            fullScreenBtn.setImageResource(R.drawable.player_inline_smallscreen_btn_selector);
            titleBar.setVisibility(View.VISIBLE);
            playProgressText.setVisibility(View.GONE);
            currentDuration.setVisibility(View.VISIBLE);
            fullDuration.setVisibility(View.VISIBLE);
        }

        hotPointParent.hideAnchorWindow();
        hotPointParent.setVisibility(isFullScreen ? VISIBLE : GONE);
        setControllView();
    }


    /**
     * 清空资源
     */
    public void clearPlayData() {
        this.currentPosition = 0;
        if (playDataBean == null) {
            return;
        }
        this.playDataBean = null;
    }


    /**
     * 设置当前时间
     *
     * @param duration
     */
    public void setCurrentDuration(int duration) {
        currentPosition = duration;
    }

    /**
     * 获取视频播放地址
     *
     * @return
     */
    public String getRealVideoId() {
        return this.playDataBean.getVoidStringUrl();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initViews();
    }

    private void onResume() {
        Log.e("TestData", "onResume_video");
        if (this.isFullScreen) {
            bigPlayBtn.setVisibility(View.GONE);
            titleBar.setVisibility(View.VISIBLE);
            showlayControllBar(true);
            // playControllBar.setVisibility(View.VISIBLE);
        }
        if (player == null) {
            showStatus(Status.Init);
        }
    }

    /**
     * 暂停
     */
    public void onPause() {
        isStarted = false;
        if (player != null) {
            currentPosition = player.getCurrentPosition();
            duration = player.getDuration();
            hotPointParent.setTotalDuration(duration);
            if (player.isPlaying()) {
                showStatus(Status.Pause);
                pausePlaying();
            }
        }
        // } else {
        // showStatus(Status.Init);
        // }
        stopTimer();
        releasePowerLock();
    }

    /**
     * 销毁方法
     */
    public void onDestroy() {
        releasePlayer();
    }

    public boolean isPlaying() {
        return isStarted && !TextUtils.isEmpty(this.playDataBean.getVoidStringUrl());
    }

    /**
     * 获取当前播放时间，如果播放完毕，返回总时间
     *
     * @return
     */
    public int getCurrentPosition() {
        if (isPlayFinished) {
            return duration;
        }
        // if (player != null) {
        // try {
        // return player.getCurrentPosition();
        // } catch (Exception e) {
        // if (MyLogger.isDebug) {
        // e.printStackTrace();
        // }
        // }
        // }
        return currentPosition;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    // public void onEvent(VideoPlayChangeProgressEvent event) {
    // if (event == null ||
    // !String.valueOf(this.videoId).equals(event.getEventKey())) {
    // return;
    // }
    // currentPosition = (event.currentPosition + 1) * 1000;
    // // if (MyLogger.isDebug) {
    // // MyLogger.d("Progress", String.format("id = %d progress = %d",
    // // videoId, currentPosition));
    // // }
    // isStarted = true;
    // isPlayFinished = false;
    // handlePlay(true);
    // }

    // public void onEvent(VisibleHideEvent event) {
    // if (event == null || player == null || !player.isPlaying()) {
    // return;
    // }
    // handleStop();
    // }

    @Override
    protected void onDetachedFromWindow() {
        releasePlayer();
        setBufferProgressVisisibility(View.GONE);
        super.onDetachedFromWindow();
    }

    /**
     * 销毁、释放、重置资源
     */
    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
        isPrepared = false;
        stopTimer();
        currentPosition = 0;
        duration = 0;
        playSeekBar.setProgress(0);
    }

    /**
     * 更新进度条的时间轴
     */
    public void updatePosition() {
        if (playDataBean == null) {
            return;
        }
        int countdown = 0;
        if (duration > 0) {
            countdown = (int) (duration - currentPosition) / 1000;
            if (playDataBean.getPlayDataType() == PlayDataType.ImageAdvertisement || playDataBean.getPlayDataType() == PlayDataType.VideoAdvertisement) {
                isViewShow(true);

                tv_countdown.setText(countdown + "");
                if (countdown <= 0) {
                    hidePlayControllBar();
                }

            } else {
                playSetting.updateHadByTime(currentPosition / 1000);
                isViewShow(false);
            }
            long pos = playSeekBar.getMax() * currentPosition / duration;
            currentDuration.setText(ParamsUtil.millsecondsToStr(currentPosition));
            playProgressText.setText(String.format(PROGRESS_FORMAT, ParamsUtil.millsecondsToStr(currentPosition),
                    ParamsUtil.millsecondsToStr(duration)));
            setProgress((int) pos);

        } else {
            currentDuration.setText(START_DEFAULT_POSITION);
            playProgressText.setText(DEFAULT_PROGRESS);
            setProgress(0);
        }

    }

    /**
     * 初始化播放进度条
     */
    private void initPlayHander() {
        playerHandler = new Handler() {
            public void handleMessage(Message msg) {

                if (player == null || !isPrepared || !player.isPlaying() || isPlayFinished) {
                    return;
                }

                // 更新播放进度
                currentPosition = player.getCurrentPosition();
                duration = player.getDuration();

                hotPointParent.setTotalDuration(duration);

                updatePosition();
            }

            ;
        };
    }

    /**
     * 进度条计时器
     */
    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        countDownTimer = new CountDownTimer(Integer.MAX_VALUE, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                if (playDataBean == null) {
                    return;
                }
                if (!isPrepared && playDataBean.getPlayDataType() == PlayDataType.Video) {
                    return;
                }
                if (playerHandler != null) {
                    playerHandler.sendEmptyMessage(0);
                }
            }

            @Override
            public void onFinish() {
            }

        };
        countDownTimer.start();
    }

    /**
     * 停止进度条计时器
     */
    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    /**
     * 初始化播放器信息
     */
    private void initPlayInfo() {
        try {
            isPrepared = false;
            if (player != null) {
                if (player.isPlaying()) {
                    player.stop();
                }
                player.reset();
            } else {
                player = new MediaPlayer();
            }
            String localPlayPath = getLocalPlayPath();
            if (TextUtils.isEmpty(localPlayPath)) {
                if (!checkPlayAvailable()) {
                    return;
                }
            }
            initPlayerListener();
            if (!TextUtils.isEmpty(localPlayPath)) {
                player.setDataSource(localPlayPath);
            } else {
                player.setDataSource(this.playDataBean.getVoidStringUrl());
            }
            player.prepareAsync();
            if (isStarted) {
                showStatus(Status.Loading);
            }
        } catch (Exception e) {
        }
    }

    private boolean checkPlayAvailable() {
        if (!NetworkUtils.isConnected(getContext())) {
            if (isStarted) {
                // ToastManager.getInstance().showToast(getContext(),
                // getContext().getString(R.string.no_internet));
                isStarted = false;
                showStatus(Status.Error);
            }
            return false;
        }
        // if (SettingPrefHelper.getInstance(getContext()).isOnlyWifiPlay() &&
        // !NetworkUtils.isWifiConnected(getContext())) {
        // if (isStarted) {
        // Toast.makeText(getContext(),
        // getContext().getString(R.string.no_internet),
        // Toast.LENGTH_SHORT).show();
        // }
        // return false;
        // }
        return true;
    }

    /**
     * 获取播放地址
     *
     * @return
     */
    private String getLocalPlayPath() {
        // DownloadInfo downloadInfo = DataSet.getDownloadByVideoId(videoId);
        // if (downloadInfo != null && downloadInfo.getStatus() ==
        // DownloadManager.STATUS_SUCCESSFUL
        // && !TextUtils.isEmpty(downloadInfo.getVideoFilePath())) {
        // File file = new File(downloadInfo.getVideoFilePath());
        // if (file.exists()) {
        // return file.getAbsolutePath();
        // }
        // }
        return null;
    }

    /**
     * 判断是否有本地地址
     *
     * @return
     */
    private boolean isLocalPlay() {
        String path = getLocalPlayPath();
        return !TextUtils.isEmpty(path);
    }

    private void handlePlay() {
        handlePlay(false);
    }

    private void handlePlay(boolean isSeekPosition) {
        boolean isLocalPlay = isLocalPlay();
        if (player == null || (TextUtils.isEmpty(this.playDataBean.getVoidStringUrl()) && !isLocalPlay)) {
            if (!TextUtils.isEmpty(this.playDataBean.getVoidStringUrl()) || isLocalPlay()) {
                initPlayInfo();
                initPlayHander();
                showStatus(Status.Loading);
            } else {
                if (!checkPlayAvailable()) {
                    return;
                }
            }
            return;
        }
        if (!isPrepared) {
            if (!checkPlayAvailable() && !isLocalPlay) {
                releasePlayer();
                showStatus(Status.Error);
                return;
            }
            try {
                initPlayInfo();
                initPlayHander();
                // player.prepareAsync();
                // showStatus(Status.Loading);
                return;
            } catch (Exception e) {
                // if (MyLogger.isDebug) {
                // e.printStackTrace();
                // }
                this.handleError();
                return;
            }
        }

        // if (isPlayFinished || isSeekPosition) {
        // isPlayFinished = false;
        if (isSeekPosition) {
            player.seekTo(currentPosition);
            playSeekBar.setEnabled(false);
            if (isStarted) {
                showStatus(Status.Loading);
            }
        } else {
            startPlaying();
            isPlayFinished = false;
            showStatus(Status.Playing);
        }
    }

    public void handleStop() {
        pausePlaying();
        isStarted = false;
        showStatus(Status.Pause);
    }

    private void handleError() {
        if (isSurfaceHolderCreated) {
            showStatus(Status.Error);
        }
        if (this.isFullScreen) {
            showVideoSmallScreen();
        }
        releasePlayer();
    }

    /**
     * 初始化播放监听
     */
    private void initPlayerListener() {
        if (player == null) {
            return;
        }
        player.setOnErrorListener(onErrorListener);
        player.setDisplay(playSurfaceHolder);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        // 设置一个监听当播放网络的数据流的buffer发生变化的时候发出通知
        player.setOnBufferingUpdateListener(onBufferingUpdateListener);
        // 设置一个监听，当准备完成的时候发出通知
        player.setOnPreparedListener(onPreparedListener);
        // 设置一个监听，当seek定位操作完成后发送通知。
        player.setOnSeekCompleteListener(onSeekCompleteListener);
        // 设置一个监听，当一个媒体是播放完毕的时候发出通知。
        player.setOnCompletionListener(onCompletionListener);
    }

    private void showNonWifiTip() {
        // if (!GlobalData.getInstance().isShowedNonWifiTip() &&
        // !NetworkUtils.isWifiConnected(getContext())
        // && NetworkUtils.isConnected(getContext())) {
        // ToastManager.getInstance().showToast(getContext(),
        // getContext().getResources().getString(R.string.non_wifi_tip));
        // GlobalData.getInstance().setShowedNonWifiTip(true);
        // }
    }

    OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_advertisement_time:
                    onPlayCompletedListener.onPlayNext(playDataBean.getVideoId());
                    isViewShow(false);
                    break;
                case R.id.ib_advertisement_controll_full_screen:
                    if (isFullScreen) {
                        playSetting.showPortraitScreen();
                        ib_advertisement_controll_full_screen.setImageResource(R.drawable.player_inline_fullscreen_btn_selector);
                    } else {
                        playSetting.showLandscapeFullScreen();
                        ib_advertisement_controll_full_screen.setImageResource(R.drawable.player_inline_smallscreen_btn_selector);
                    }
                    isFullScreen = !isFullScreen;
                    break;
                case R.id.tv_popuwindow:
                    playSetting.onTitleRightTextOnClick();
                    break;
                // case R.id.video_player_controll_play_container:
                /**
                 * 暂停或者恢复播放
                 */
                case R.id.video_player_controll_play_btn:
                    if (NetworkUtils.getNetworkType(playSetting) == 0) {
                        ToastManager.getInstance().showToast(getContext(), "小七找不到网络....T.T");
                        return;
                    }
                    ;

                    if (isStarted) {
                        isStarted = false;
                        handleStop();
                    } else {
                        String strNetworkTypeName = NetworkUtils.getNetWorkTypeName(playSetting);
                        if (!StringUtils.isNull(strNetworkTypeName)) {
                            DialogUtils.dialogMessage(playSetting, "友情提醒", "您当前是" + strNetworkTypeName + "会产生流量费用,请选择是否继续播放", "确定", "取消",
                                    new CustomeDialog.OnCustomeDialogClickListener() {
                                        @Override
                                        public void onCustomeDialogClick(CustomeDialog.CustomeDialogClickType type) {
                                            switch (type) {
                                                case Ok:
                                                    isStarted = true;
                                                    handlePlay();
                                                    break;
                                                case Cancel:
                                                    break;
                                                default:
                                                    break;
                                            }
                                        }
                                    });
                        } else {
                            isStarted = true;
                            handlePlay();
                        }
                    }
                    break;
                case R.id.video_player_controll_full_screen_container:
                case R.id.video_player_controll_full_screen:
                    if (fullScreenBtn.getVisibility() == View.VISIBLE) {
                        onVideoScreenSizeChanged();
                    }
                    break;
                case R.id.video_player_back_btn_container:
                    playSetting.onTitleLeftImageBackOnClick();
                    break;
                /**
                 * 未播放时，点击播放按钮(第一次)
                 */
                case R.id.video_player_play_btn:

                    if (NetworkUtils.getNetworkType(playSetting) == 0) {
                        Toast.makeText(mContext, "小七找不到网络....T.T", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ;
                    if (playSetting == null) {
                        return;
                    }
                    String strNetworkTypeName = NetworkUtils.getNetWorkTypeName(playSetting);


                    if (!StringUtils.isNull(strNetworkTypeName)) {
                        DialogUtils.dialogMessage(playSetting, "友情提醒", "您当前是" + strNetworkTypeName + "会产生流量费用,请选择是否继续播放", "确定", "取消",
                                new CustomeDialog.OnCustomeDialogClickListener() {
                                    @Override
                                    public void onCustomeDialogClick(CustomeDialog.CustomeDialogClickType type) {
                                        switch (type) {
                                            case Ok:
                                                bigPlayBtn.setVisibility(View.GONE);
                                                firstPlay();
                                                break;
                                            case Cancel:
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                });
                    } else {
                        bigPlayBtn.setVisibility(View.GONE);
                        firstPlay();
                    }
                    /**
                     * 隐藏横屏，时间进度view
                     */
                    fullDuration.setVisibility(View.GONE);
                    currentDuration.setVisibility(View.GONE);

                    break;
            }
        }

    };

    public void firstPlay() {
        if (playDataBean == null) {
            return;
        }
        if (playDataBean != null) {
            playSetting.showPortraitScreen();
            if (playDataBean.getPlayDataType() == PlayDataType.Video || playDataBean.getPlayDataType() == PlayDataType.VideoAdvertisement) {
                // playSetting.btb_top.setVisibility(View.GONE);
                isStarted = true;
                if (isPlayFinished) {
                    isPlayFinished = false;
                    currentPosition = 0;
                }
                showNonWifiTip();
                handlePlay(true);
                if (player != null) {
                    // showVideoFullScreen();
                }
                setControllView();
            } else {
                // 切换为竖屏
                ImageRender.getInstance().setImage(coverImageView, playDataBean.getImageStringUrl(), 0);
                coverImageView.setVisibility(View.GONE);
                playerHandler = new Handler() {
                    public void handleMessage(Message msg) {
                        // 更新播放进度
                        currentPosition = playDataBean.getImageAdvertisementPosition();
                        duration = playDataBean.getImageAdvertisementDuration();

                        int countdown = (int) (duration - currentPosition);
                        tv_countdown.setText(countdown + "");

                        if (currentPosition == duration) {
                            stopTimer();
                            onPlayCompletedListener.onPlayNext(playDataBean.getVideoId());
                            isViewShow(false);
                        }
                        playDataBean.setImageAdvertisementPosition(++currentPosition);
                    }
                };
                startTimer();

            }
        }
    }

    public void showVideoFullScreen() {
        if (!isFullScreenDisabled && !isFullScreen) {
            onVideoScreenSizeChanged();
        }
    }

    public void showVideoSmallScreen() {
        if (isFullScreen) {
            onVideoScreenSizeChanged();
        }
    }

    private void onVideoScreenSizeChanged() {
        if (onScreenChangedListener != null) {
            onScreenChangedListener.onScreenChanged(!isFullScreen);
        }
        isFullScreen = !isFullScreen;
        hotPointParent.hideAnchorWindow();
        hotPointParent.setVisibility(isFullScreen ? VISIBLE : GONE);
    }

    SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            isSurfaceHolderCreated = true;
            try {
                if (player != null) {
                    if (isPrepared) {
                        if (isStarted) {
                            startPlaying();
                        }
                    } else {
                        initPlayerListener();
                    }
                } else {
                    if (!TextUtils.isEmpty(playDataBean.getVoidStringUrl()) || isLocalPlay()) {
                        initPlayInfo();
                        initPlayHander();
                    }
                }

            } catch (Exception e) {
                // if (MyLogger.isDebug) {
                // e.printStackTrace();
                // }
            }
            // MyLogger.i(TAG, "surface created");
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            holder.setFixedSize(width, height);
            if (player != null) {
                player.setDisplay(holder);
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            isSurfaceHolderCreated = false;
            if (player == null) {
                showStatus(Status.Init);
                return;
            }
            showStatus(Status.Pause);
            pausePlaying();
            // player.stop();
            // // player.reset();
            // releasePlayer();
            // isPrepared = false;
            isStarted = false;
        }
    };

    /**
     * 设置一个监听当播放网络的数据流的buffer发生变化的时候发出通知
     */
    OnBufferingUpdateListener onBufferingUpdateListener = new OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            playSeekBar.setSecondaryProgress(percent);
        }
    };

    /**
     * 设置一个监听，当准备完成的时候发出通知
     */
    MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            isPrepared = true;
            playSeekBar.setEnabled(true);
            setBufferProgressVisisibility(View.GONE);
            int maxProgress = playSeekBar.getMax();
            if (maxProgress <= 0) {
                maxProgress = 100;
            }
            if (isStarted) {
                if (playSeekBar.getProgress() > 0) {
                    currentPosition = playSeekBar.getProgress() * player.getDuration() / maxProgress;
                } else {
                    if (currentPosition >= player.getDuration()) {
                        currentPosition = 0;
                    }
                    if (currentPosition >= 0 && player.getDuration() > 0) {
                        setProgress(currentPosition * maxProgress / player.getDuration());
                    }
                }

                if (currentPosition != player.getCurrentPosition()) {
                    player.seekTo(currentPosition);
                    playSeekBar.setEnabled(false);
                    showStatus(Status.Loading);
                } else {
                    startPlaying();
                    showStatus(Status.Playing);
                }
            }

            LayoutParams params = getScreenSizeParams(1, isFullScreen);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            playSurfaceView.setLayoutParams(params);
            fullDuration.setText(ParamsUtil.millsecondsToStr(player.getDuration()));
            if (playDataBean == null) {
                return;
            }
            if (playDataBean.getPlayDataType() == PlayDataType.VideoAdvertisement || playDataBean.getPlayDataType() == PlayDataType.ImageAdvertisement) {
                hidePlayControllBar();
            }
        }
    };

    MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer player, int what, int extra) {
            // MyLogger.e(TAG, "arg1==" + what + " ; arg2==" + extra);
            handleError();
            return false;
        }
    };

    OnSeekBarChangeListener onSeekBarChangeListener = new OnSeekBarChangeListener() {
        int realTime = 0;

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (player == null) {
                return;
            }
            if (player != null && isPrepared) {
                currentPosition = realTime;
                player.seekTo(currentPosition);
                playSeekBar.setEnabled(false);
                if (isStarted) {
                    showStatus(Status.Loading);
                }
            } else {
                handlePlay();
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            isSeekbarTouching = true;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (player == null) {
                return;
            }
            int maxProgress = seekBar.getMax();
            if (maxProgress <= 0) {
                maxProgress = 100;
            }
            if (progress > maxProgress * 0.98) {
                progress = (int) (maxProgress * 0.98);
            }
            this.realTime = progress * player.getDuration() / seekBar.getMax();
            hotPointParent.setCurrTime(this.realTime);
            isSeekbarTouching = false;
            postDelayHidePlayControllerBar();
        }
    };
    /**
     * 设置一个监听，当seek定位操作完成后发送通知。
     */
    private OnSeekCompleteListener onSeekCompleteListener = new OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mp) {
            setBufferProgressVisisibility(View.GONE);
            playSeekBar.setEnabled(true);
            if (isStarted) {
                coverImageView.setVisibility(View.GONE);
                if (player != null && isPrepared) {
                    startPlaying();
                }
            }
        }
    };
    /**
     * 设置一个监听，当一个媒体是播放完毕的时候发出通知。
     */
    private OnCompletionListener onCompletionListener = new OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            if (onPlayCompletedListener == null || !onPlayCompletedListener.isNeedPlayLoop()) {// finished
                isPlayFinished = true;
                isStarted = false;
                currentPosition = duration;
                setProgress(0);
                playSeekBar.setEnabled(true);
                stopTimer();
                showStatus(Status.Finished);
                showVideoSmallScreen();
            } else {// start play next
                if (!onPlayCompletedListener.isHasNext(playDataBean.getVideoId())) {// replay
                    isStarted = true;
                    player.seekTo(0);
                    setProgress(0);
                    playSeekBar.setEnabled(false);
                    showStatus(Status.Loading);
                } else {
                    onPlayCompletedListener.onPlayNext(playDataBean.getVideoId());
                }
            }
        }
    };

    private LayoutParams getScreenSizeParams(int position, boolean isLandscape) {
        int width = isLandscape ? Math.max(DISPLAY_WIDTH, DISPLAY_HEIGHT) : Math.min(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        int height = isLandscape ? Math.min(DISPLAY_WIDTH, DISPLAY_HEIGHT) : heightRatio > 0.0 ? (int) (width * heightRatio) : getContext()
                .getResources().getDimensionPixelSize(R.dimen.coursemain_videoplay_height);
        if (player != null && width > 0 && height > 0) {
            int vWidth = player.getVideoWidth();
            int vHeight = player.getVideoHeight();
            // if (MyLogger.isDebug) {
            // MyLogger.d(TAG, "video width=" + vWidth + "; height=" + vHeight);
            // }

            if (vWidth > width || vHeight > height) {
                float wRatio = (float) vWidth / (float) width;
                float hRatio = (float) vHeight / (float) height;
                float ratio = Math.max(wRatio, hRatio);
                if (ratio > 0) {
                    width = (int) Math.ceil((float) vWidth / ratio);
                    height = (int) Math.ceil((float) vHeight / ratio);
                }
            } else {
                if (vWidth > 0 && vHeight > 0) {
                    float wRatio = (float) width / (float) vWidth;
                    float hRatio = (float) height / (float) vHeight;
                    float ratio = Math.min(wRatio, hRatio);

                    width = (int) Math.ceil((float) vWidth * ratio);
                    height = (int) Math.ceil((float) vHeight * ratio);
                }
            }
        }
        LayoutParams params = new LayoutParams(width, height);
        return params;
    }

    private OnTouchListener touchListener = new OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            final int action = event.getAction();
            boolean isHandled = gestureDetector.onTouchEvent(event);
            switch (action & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_UP:
                    getParent().requestDisallowInterceptTouchEvent(false);
                case MotionEvent.ACTION_DOWN:
                    getParent().requestDisallowInterceptTouchEvent(true);
                case MotionEvent.ACTION_CANCEL:
                    onEndScroll();
                    break;
            }
            return isHandled;
        }
    };

    // 滑动
    private GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        private int scrollType = -1;

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (player != null) {
                if (!isFullScreen) {
                    // 切换横屏
                    playSetting.showLandscapeFullScreen();
                    // 双击暂停
                    // handleStop();
                } else {
                    // if (isPrepared && !isPlayFinished) {
                    // isStarted = true;
                    // 双击开始
                    // 切换横屏
                    playSetting.showPortraitScreen();

                    // handlePlay();
                    // }
                }
            }
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            handleShowOrHideBar();
            //EveryDayVideoFragment act = (EveryDayVideoFragment) playSetting;
            // act.goneEditView();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (bigPlayBtn.getVisibility() == View.VISIBLE) {
                return false;
            }
            float deltaY = e2.getY() - e1.getY();
            float deltaX = e2.getX() - e1.getX();
            float mOldY = e1.getY();
            float mNewY = e2.getY();
            if (playSetting == null) {
                return false;
            }

            int st = playSetting.getResources().getConfiguration().orientation;

            if (playDataBean == null) {
                return false;
            }

            if (playDataBean.getPlayDataType() == PlayDataType.Video) {
                // 等于横屏
                if (st == Configuration.ORIENTATION_LANDSCAPE) {
                    if (Math.abs(deltaX) > Math.abs(deltaY)) {
                        if (Math.abs(deltaX) > SWIPE_THRESHOLD && scrollType != 1) {
                            onHorizontalScroll(deltaX);
                            scrollType = 0;
                        }
                    } else {
                        if (Math.abs(deltaY) > SWIPE_THRESHOLD && scrollType != 0) {
                            if (e1.getX() >= (Max_y / 2)) {
                                onVerticalScroll(deltaY);
                            } else {
                                onHorizontalScrollSettingBrightness((mOldY - mNewY) / Max_y);
                            }
                            scrollType = 1;
                        }
                    }
                } else {
                    if (Math.abs(deltaX) > Math.abs(deltaY)) {
                        if (Math.abs(deltaX) > SWIPE_THRESHOLD && scrollType != 1) {
                            onHorizontalScroll(deltaX);
                            scrollType = 0;
                        }
                    } else {
                        if (Math.abs(deltaY) > SWIPE_THRESHOLD && scrollType != 0) {
                            if (e1.getX() >= (Max_X / 2)) {
                                onVerticalScroll(deltaY);
                            } else {
                                onHorizontalScrollSettingBrightness((mOldY - mNewY) / Max_y);
                            }
                            scrollType = 1;
                        }

                        // if (Math.abs(deltaY) > SWIPE_THRESHOLD &&
                        // scrollType
                        // != 0) {
                        // onVerticalScroll(deltaY);
                        // scrollType = 1;
                        // }
                    }
                }
            }

            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            scrollType = -1;
            scrollToPosition = -1;
            currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            return super.onDown(e);
        }

    });

    /**
     * 是否显示popupWindow 默认为不显示
     */
    public void isShowPopupWindow(boolean isShowPopupWindon) {
        isShowPupupWindow = isShowPopupWindon;
    }

    ;

    /**
     * 播放进度控制
     *
     * @param distance
     */
    private void onHorizontalScroll(float distance) {
        this.mCircleProgress.setVisibility(GONE);
        if (this.isPlayFinished) {
            return;
        }
        // 降低进度设置速度
        distance = distance / 9f;

        int width = this.isFullScreen ? DISPLAY_WIDTH : DISPLAY_HEIGHT;
        if (this.duration > 0 && width > 0) {
            this.gestureContainer.setVisibility(View.VISIBLE);
            scrollToPosition = currentPosition + (int) (distance * duration / width);
            scrollToPosition = scrollToPosition < 0 ? 0 : scrollToPosition > duration ? duration : scrollToPosition;
            int maxProgress = playSeekBar.getMax();
            long pos = playSeekBar.getMax() * scrollToPosition / duration;
            if (pos > maxProgress * 0.98) {
                pos = (int) (maxProgress * 0.98);
            }
            String progress = String.format(PROGRESS_FORMAT, ParamsUtil.millsecondsToStr(scrollToPosition), ParamsUtil.millsecondsToStr(duration));
            currentDuration.setText(ParamsUtil.millsecondsToStr(scrollToPosition));
            playProgressText.setText(progress);
            setProgress((int) pos);
            this.gestureTip.setText(progress);
            if (distance > 0) {
                this.gestureTypeIcon.setImageResource(R.mipmap.play_gesture_forward);
            } else {
                this.gestureTypeIcon.setImageResource(R.mipmap.play_gesture_backoff);
            }
        }
    }

    /**
     * 调节亮度
     *
     * @param distance
     */
    private void onHorizontalScrollSettingBrightness(float distance) {

        if (mBrightness < 0) {
            mBrightness = playSetting.getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;
        }

        WindowManager.LayoutParams lpa = playSetting.getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + distance;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;

        playSetting.getWindow().setAttributes(lpa);

        mCircleProgress.setVisibility(VISIBLE);
        mCircleProgress.n = (int) (lpa.screenBrightness * 360);
        mCircleProgress.invalidate();

        try {
            gestureContainer2.setVisibility(View.VISIBLE);
            gestureTip2.setText("亮度:" + (int) (lpa.screenBrightness * 100));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 音量控制
     *
     * @param distance
     */
    private void onVerticalScroll(float distance) {
        this.mCircleProgress.setVisibility(GONE);
        int width = this.isFullScreen ? DISPLAY_WIDTH : heightRatio > 0.0 ? (int) (DISPLAY_WIDTH * heightRatio) : getContext().getResources()
                .getDimensionPixelSize(R.dimen.coursemain_videoplay_height);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        if (width > 0 && maxVolume > 0) {
            this.gestureContainer.setVisibility(View.VISIBLE);
            float scrollToVolume = currentVolume - (int) (distance * maxVolume / width);
            scrollToVolume = scrollToVolume < 0 ? 0 : scrollToVolume > maxVolume ? maxVolume : scrollToVolume;
            int progress = (int) (scrollToVolume * 100 / (float) maxVolume);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) scrollToVolume, 0);
            this.gestureTip.setText(progress + " %");
            if (scrollToVolume > 0) {
                this.gestureTypeIcon.setImageResource(R.mipmap.play_gesture_voice);
            } else {
                this.gestureTypeIcon.setImageResource(R.mipmap.play_gesture_quiet);
            }
        }
    }

    public void playStreamMute(boolean isStreamMute) {
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, isStreamMute);
    }

    private void onEndScroll() {
        if (player == null)
            return;
        if (this.gestureContainer.getVisibility() == View.VISIBLE || this.gestureContainer2.getVisibility() == View.VISIBLE) {
            this.gestureContainer.setVisibility(View.GONE);
            this.gestureContainer2.setVisibility(View.GONE);
            if (this.isPlayFinished) {
                return;
            }
            if (scrollToPosition >= 0) {
                scrollToPosition = scrollToPosition < 0 ? 0 : scrollToPosition >= (int) (duration * 0.98) ? (int) (duration * 0.98)
                        : scrollToPosition;
                currentPosition = scrollToPosition;
                player.seekTo(currentPosition);

                if (playSeekBar != null) {
                    playSeekBar.setEnabled(false);
                }
                if (isStarted) {
                    showStatus(Status.Loading);
                }
            }
        }
    }

    private void handleShowOrHideBar() {
        if (playDataBean == null)
            return;
        if (playDataBean.getPlayDataType() == PlayDataType.VideoAdvertisement || playDataBean.getPlayDataType() == PlayDataType.ImageAdvertisement) {
            setControllView();
        } else {
            if (bigPlayBtn.getVisibility() == View.VISIBLE) {
                return;
            }
            removeCallbacks(hideBarRunnable);
            if (playControllBar.getVisibility() == View.VISIBLE) {
                hidePlayControllBar();
            } else {
                if (isFullScreen) {
                    titleBar.setVisibility(View.VISIBLE);
                }
                showlayControllBar(true);
                // playControllBar.setVisibility(View.VISIBLE);
                postDelayHidePlayControllerBar();
            }
        }
    }


    /**
     * 如果当前播放的data是视频广告或者图片广告则隐藏控制栏
     */
    public void dismissControllVisibility() {
        if (playDataBean == null) {
            return;
        }
        if (playDataBean.getPlayDataType() == PlayDataType.Video) {

        }
        hidePlayControllBar();
    }

    /**
     * 隐藏标题栏和控制栏
     */
    public void hidePlayControllBar() {
        titleBar.setVisibility(View.GONE);
        showlayControllBar(false);
        hotPointParent.hideAnchorWindow();
    }

    /**
     * 是否显示(静音、视频倒计时、了解详情、放大/缩小按钮)
     *
     * @param isShowMusic 是否显示静音按钮
     * @param isAdTime    是否显示倒计时
     * @param isAdSc      是否显示放大缩小
     */
    public void dismissAdvertisement(boolean isShowMusic, boolean isAdTime, boolean isAdSc) {
        rl_music.setVisibility(isShowMusic ? View.VISIBLE : View.GONE);
        ll_advertisement_time.setVisibility(isAdTime ? View.VISIBLE : View.GONE);
        ll_advertisement_screen.setVisibility(isAdSc ? View.VISIBLE : View.GONE);
    }

    /**
     * 显示标题栏和控制栏
     */
    public void showlayControllBar() {
        titleBar.setVisibility(View.VISIBLE);
        showlayControllBar(true);
        // playControllBar.setVisibility(View.VISIBLE);
    }

    /**
     * 显示标题栏和控制栏
     *
     * @param bl 是否显示
     */
    public void showlayControllBar(boolean bl) {
        if (playDataBean == null) {
            return;
        }
        playControllBar.setVisibility(bl && playDataBean.getPlayDataType() == PlayDataType.Video ? View.VISIBLE : View.GONE);
    }

    /**
     * 显示标题栏和控制栏
     *
     * @param playControllBarVisiable 是否显示
     */
    public void showlayControllBar(int playControllBarVisiable) {
        if (playDataBean == null) {
            return;
        }
        playControllBar.setVisibility(playControllBarVisiable == 0 && playDataBean.getPlayDataType() == PlayDataType.Video ? View.VISIBLE : View.GONE);
    }

    /**
     * 隐藏标题栏和控制栏
     */
    private void postDelayHidePlayControllerBar() {
        removeCallbacks(hideBarRunnable);
        postDelayed(hideBarRunnable, 3000);
    }

    private Runnable hideBarRunnable = new Runnable() {
        @Override
        public void run() {
            if (playControllBar != null && playControllBar.getVisibility() == View.VISIBLE && isFullScreen && !isSeekbarTouching) {
                hidePlayControllBar();
            }
        }
    };

    /**
     * 初始化视图
     */
    private void initViews() {
        this.playSurfaceView = (SurfaceView) findViewById(R.id.video_player_surfaceView);
        this.playSurfaceHolder = this.playSurfaceView.getHolder();
        this.playSurfaceHolder.addCallback(this.surfaceCallback);
        // added this for android 2.3
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            this.playSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        // hzb
        this.tv_popuwindow = (TextView) findViewById(R.id.tv_popuwindow);
        this.rl_music = (RelativeLayout) findViewById(R.id.rl_music);
        this.iv_music = (ImageView) findViewById(R.id.iv_music);
        this.tv_countdown = (TextView) findViewById(R.id.tv_countdown);
        this.ll_advertisement_time = (LinearLayout) findViewById(R.id.ll_advertisement_time);
        this.ll_advertisement_screen = (LinearLayout) findViewById(R.id.ll_advertisement_screen);
        this.ib_advertisement_controll_full_screen = (ImageButton) findViewById(R.id.ib_advertisement_controll_full_screen);

        this.coverImageView = (ImageView) findViewById(R.id.video_player_cover_view);
        this.titleBar = findViewById(R.id.video_player_title_bar);
        this.backButtonContainer = findViewById(R.id.video_player_back_btn_container);
        this.backButton = (ImageButton) findViewById(R.id.video_player_back_btn);
        this.titleView = (TextView) findViewById(R.id.video_player_title);
        this.playControllBar = findViewById(R.id.video_player_controll_bar);
        this.playOrPauseContainer = findViewById(R.id.video_player_controll_play_container);
        this.playOrPauseBtn = (ImageButton) findViewById(R.id.video_player_controll_play_btn);
        this.currentDuration = (TextView) findViewById(R.id.video_player_controll_play_duration);
        this.playSeekBar = (SeekBar) findViewById(R.id.video_player_controll_seekbar);
        this.fullScreenContainer = findViewById(R.id.video_player_controll_full_screen_container);
        this.fullDuration = (TextView) findViewById(R.id.video_player_controll_full_duration);
        this.fullScreenBtn = (ImageButton) findViewById(R.id.video_player_controll_full_screen);
        this.playProgressText = (TextView) findViewById(R.id.video_player_controll_playing_progress);
        this.bigPlayBtn = (ImageButton) findViewById(R.id.video_player_play_btn);
        this.playBufferProgressBar = findViewById(R.id.video_player_progressbar);
        this.hotPointParent = (HotPointParent) findViewById(R.id.hot_point_parent);
        this.gestureContainer = findViewById(R.id.video_player_gesture_container);
        this.gestureContainer2 = findViewById(R.id.video_player_gesture_container2);
        this.gestureTypeIcon = (ImageView) findViewById(R.id.video_player_gesture_type_icon);
        this.gestureTip = (TextView) findViewById(R.id.video_player_gesture_type_tip);
        this.gestureTip2 = (TextView) findViewById(R.id.video_player_gesture_type_tip2);
        this.errorViewContainer = findViewById(R.id.video_player_no_net);
        this.maskView = findViewById(R.id.video_player_mask_view);

        this.tv_popuwindow.setOnClickListener(onClickListener);
        this.backButtonContainer.setOnClickListener(onClickListener);
        this.backButton.setOnClickListener(onClickListener);
        this.playSurfaceView.setOnTouchListener(touchListener);
        this.playSurfaceView.setClickable(true);
        this.playOrPauseContainer.setOnClickListener(onClickListener);
        this.playOrPauseBtn.setOnClickListener(onClickListener);
        this.fullScreenContainer.setOnClickListener(onClickListener);
        this.fullScreenBtn.setOnClickListener(onClickListener);
        this.bigPlayBtn.setOnClickListener(onClickListener);
        this.ll_advertisement_time.setOnClickListener(onClickListener);
        this.ib_advertisement_controll_full_screen.setOnClickListener(onClickListener);
        this.playSeekBar.setEnabled(false);
        this.playSeekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);

        int playSeekBarPaddingLeft = playSeekBar.getPaddingLeft();
        int playSeekBarPaddingRight = playSeekBar.getPaddingRight();
        FrameLayout.LayoutParams hotPointParentLayoutParams = (FrameLayout.LayoutParams) hotPointParent.getLayoutParams();
        hotPointParentLayoutParams.leftMargin = playSeekBarPaddingLeft;
        hotPointParentLayoutParams.rightMargin = playSeekBarPaddingRight;
        hotPointParent.setLayoutParams(hotPointParentLayoutParams);
        hotPointParent.requestLayout();
        hotPointParent.setListener(this);
        // init view
        showStatus(Status.Init);
        SWIPE_THRESHOLD = DisplayUtils.dpToPx(getContext(), 10);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        DISPLAY_WIDTH = metrics.widthPixels;
        DISPLAY_HEIGHT = metrics.heightPixels;
        audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);

        // 默认不显示pupuowindow
        isShowPupupWindow = false;

        this.mCircleProgress = (MyCircleProgress) findViewById(R.id.MyCircleProgress);
        mCircleProgress.setVisibility(GONE);
    }

    private MyCircleProgress mCircleProgress;

    /**
     * 显示状态提示框
     *
     * @param status
     */
    public void showStatus(Status status) {
        this.errorViewContainer.setVisibility(View.GONE);
        switch (status) {
            case Init:
                showStatusView(View.GONE, View.VISIBLE, View.GONE, View.VISIBLE, 2, View.GONE);
                break;
            case Loading:
                showStatusView(playControllBar.getVisibility(), View.GONE, View.VISIBLE, View.GONE, 3, View.GONE);
                break;
            case Playing:
                showStatusView(isFullScreen ? View.GONE : View.VISIBLE, View.GONE, View.GONE, View.GONE, 3, View.GONE);
                break;
            case Pause:
                showStatusView(View.VISIBLE, View.GONE, View.GONE, this.isPrepared ? View.GONE : View.VISIBLE, 2, View.GONE);
                break;
            case Finished:
                currentDuration.setText(START_DEFAULT_POSITION);
                showStatusView(View.GONE, View.VISIBLE, View.GONE, View.GONE, 2, View.GONE);
                break;
            case Error:
                showStatusView(View.GONE, View.VISIBLE, View.GONE, View.GONE, 2, View.GONE);
                this.errorViewContainer.setVisibility(View.VISIBLE);
                this.bigPlayBtn.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 显示播放器提示框
     *
     * @param playControllBarVisiable 功能栏是否显示
     * @param bigPlayVisible          播放按钮是否显示
     * @param progressVisible         播放进度条是否显示
     * @param coverImageVisible       默认图片是否显示
     * @param isAD                    1.显示,2.不显示,3.根据视频类型
     * @param gestureContainerVisible 是否显示亮度或者快进快退提示框
     */
    private void showStatusView(int playControllBarVisiable, int bigPlayVisible, int progressVisible,
                                int coverImageVisible, int isAD, int gestureContainerVisible) {
        // 暂停恢复显示的图片
        playOrPauseBtn.setImageResource(isStarted ? R.drawable.player_inline_pause_btn_selector
                : R.drawable.player_inline_play_btn_selector);
        // 播放控制栏
        playControllBar.setVisibility(playControllBarVisiable);
        // 判断横竖屏
        if (isFullScreen) {
            // 横屏
            titleBar.setVisibility(playControllBarVisiable);
        } else {
            // 竖屏
            titleBar.setVisibility(View.GONE);
        }
        // 热点
        if (playControllBarVisiable == GONE) {
            hotPointParent.hideAnchorWindow();
        }
        // 功能栏
        maskView.setVisibility(bigPlayVisible);
        // 播放按钮
        bigPlayBtn.setVisibility(bigPlayVisible);
        // 网络加载
        setBufferProgressVisisibility(progressVisible);
        // 默认图片
        coverImageView.setVisibility(coverImageVisible);
        // 显示广告时的展示效果
        isViewShow(isAD);
        // 是否显示亮度或者快进快退提示框
        setGestureContainerVisisibility(gestureContainerVisible);
    }

    private void setGestureContainerVisisibility(int visible) {
        gestureContainer.setVisibility(visible);
        gestureContainer2.setVisibility(visible);
    }

    /**
     * 网络加载提示框
     *
     * @param visible
     */
    private void setBufferProgressVisisibility(int visible) {
        playBufferProgressBar.setVisibility(visible);
    }

    /**
     * 是否显示(静音、视频倒计时、了解详情、放大/缩小按钮) 根据当前播放的视频状态，设置显示和隐藏
     *
     * @param ad 1.显示,2.不显示,3.根据视频类型
     */
    private void isViewShow(int ad) {

        boolean bl = false;

        switch (ad) {
            case 1:
                bl = true;
                break;
            case 2:
                bl = false;
                break;
            case 3:
                if (playDataBean == null) {
                    isViewShow(false);
                    return;
                }
                bl = playDataBean.getPlayDataType() == PlayDataType.ImageAdvertisement || playDataBean.getPlayDataType() == PlayDataType.VideoAdvertisement ? true : false;
                break;

            default:
                bl = false;
                break;
        }

        isViewShow(bl);
    }

    /**
     * 是否显示(静音、视频倒计时、了解详情、放大/缩小按钮)
     *
     * @param bl true=显示,false=不显示
     */
    private void isViewShow(boolean bl) {
        if (StringUtils.isNull(tv_countdown.getText())) {
            bl = false;
        }
        rl_music.setVisibility(bl ? View.VISIBLE : View.GONE);
        ll_advertisement_time.setVisibility(bl ? View.VISIBLE : View.GONE);
        ll_advertisement_screen.setVisibility(bl ? View.VISIBLE : View.GONE);
    }

    public void setAnchors(List<VideoChapter> anchors) {
        if (anchors != null && anchors.size() > 0) {
            hotPointParent.setAnchors(anchors);
        }
    }

    /**
     * 更新进度条
     *
     * @param progress
     */
    private void setProgress(int progress) {
        playSeekBar.setProgress(progress);
        if (!isSeekbarTouching) {
            hotPointParent.setCurrTime(player.getCurrentPosition());
        }
    }

    /**
     * 是否需要隐藏控制栏
     */
    public void setControllView() {
        if (playDataBean == null) {
            return;
        }
        if (playDataBean.getPlayDataType() == PlayDataType.ImageAdvertisement || playDataBean.getPlayDataType() == PlayDataType.VideoAdvertisement) {
            hidePlayControllBar();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (heightRatio > 0.0) {
            if (!isFullScreen) {
                int width = MeasureSpec.getSize(widthMeasureSpec);
                int height = (int) (width * heightRatio);
                super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
            } else {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public void onHotPointClick(VideoChapter anchor) {
        postDelayHidePlayControllerBar();
    }

    @Override
    public void onDisplayPointClick(VideoChapter anchor) {
        hotPointParent.hideAnchorWindow();
    }

    private void startPlaying() {
        if (player != null && !player.isPlaying()) {
            player.start();
            acquirePowerLock();
            startTimer();
            if (this.onPlayCompletedListener != null) {
                this.onPlayCompletedListener.onStartPlay(playDataBean.getVideoId());
            }
        }
    }

    private void pausePlaying() {
        if (player != null && player.isPlaying()) {
            player.pause();

            releasePowerLock();
            stopTimer();
        }
    }

    @SuppressWarnings("deprecation")
    private void acquirePowerLock() {
        if (wakeLock == null) {
            wakeLock = ((PowerManager) getContext().getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
                    "VideoPlayer");
        }
        wakeLock.acquire();
    }

    private void releasePowerLock() {
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
    }
}
