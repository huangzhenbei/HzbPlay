package com.sino.hzb.play.model;

/**
 * 视频信息
 */
public class PlayDataBean {

    /**
     * 视频id
     */
    private int videoId;
    /**
     * 视频标题
     */
    private String videoTitle;
    /**
     * 视频地址
     */
    private String voidStringUrl;
    /**
     *视频未播放时,显示的图片
     */
    private String imageStringUrl;
    /**
     * 播放的数据类型
     */
    private PlayDataType playDataType;// 播放的数据类型
    /**
     *图片广告的时间总长度
     */
    private int imageAdvertisementDuration;
    /**
     * 图片广告的时间当前长度
     */
    private int imageAdvertisementPosition;
    /**
     * 图片广告的URL
     */
    private String imageAdvertisementUrl;

    /**
     *视频信息
     * @param videoId   视频id
     * @param videoTitle    视频标题
     * @param voidStringUrl 视频地址
     * @param imageStringUrl    视频默认图片
     * @param playDataType  视频类型
     * @param imageAdvertisementDuration    广告当前时间
     * @param imageAdvertisementPosition    广告总时间
     * @param imageAdvertisementUrl     图片广告地址
     */
    public PlayDataBean(int videoId,
                        String videoTitle,
                        String voidStringUrl,
                        String imageStringUrl,
                        PlayDataType playDataType,
                        int imageAdvertisementDuration,
                        int imageAdvertisementPosition, String imageAdvertisementUrl) {
        super();
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.voidStringUrl = voidStringUrl;
        this.imageStringUrl = imageStringUrl;
        this.playDataType = playDataType;
        this.imageAdvertisementDuration = imageAdvertisementDuration;
        this.imageAdvertisementPosition = imageAdvertisementPosition;
        this.imageAdvertisementUrl = imageAdvertisementUrl;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVoidStringUrl() {
        return voidStringUrl;
    }

    public void setVoidStringUrl(String voidStringUrl) {
        this.voidStringUrl = voidStringUrl;
    }

    public String getImageStringUrl() {
        return imageStringUrl;
    }

    public void setImageStringUrl(String imageStringUrl) {
        this.imageStringUrl = imageStringUrl;
    }

    public PlayDataType getPlayDataType() {
        return playDataType;
    }

    public void setPlayDataType(PlayDataType playDataType) {
        this.playDataType = playDataType;
    }

    public int getImageAdvertisementDuration() {
        return imageAdvertisementDuration;
    }

    public void setImageAdvertisementDuration(int imageAdvertisementDuration) {
        this.imageAdvertisementDuration = imageAdvertisementDuration;
    }

    public int getImageAdvertisementPosition() {
        return imageAdvertisementPosition;
    }

    public void setImageAdvertisementPosition(int imageAdvertisementPosition) {
        this.imageAdvertisementPosition = imageAdvertisementPosition;
    }

    public String getImageAdvertisementUrl() {
        return imageAdvertisementUrl;
    }

    public void setImageAdvertisementUrl(String imageAdvertisementUrl) {
        this.imageAdvertisementUrl = imageAdvertisementUrl;
    }
}
