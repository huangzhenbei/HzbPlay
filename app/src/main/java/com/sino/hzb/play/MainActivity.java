package com.sino.hzb.play;

import android.os.Bundle;

import com.sino.hzb.play.R;
import com.sino.hzb.play.model.PlayDataBean;
import com.sino.hzb.play.model.PlayDataType;
import com.sino.hzb.play.util.ImageRender;
import com.sino.hzb.play.view.setting.PlaySetting;

import java.util.ArrayList;


public class MainActivity extends PlaySetting {

    public final static String imageLoaderPath = "/fanXQ/image/cache/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化ImageLoader
        ImageRender.getInstance().init(this);

        initData();
        intPlay(this,R.id.course_main_videoplayview,playDataBeanList);
    }

    private void initData(){
        //视频集合
        playDataBeanList = new ArrayList<PlayDataBean>();

        /**
         * 如果想让广告视频，在正式视频之前播放，需要在list集合里面排序，让广告视频的index大于正是视频的index
         */

        //视频广告
        PlayDataBean playDataBean = new PlayDataBean(1, "视频广告", "http://video.sinosns.cn/fx7/2.mp4",
                "http://img20.360buyimg.com/da/jfs/t2578/2/885808803/92072/b4f3c332/566fb362Nb4837357.jpg",
                PlayDataType.VideoAdvertisement, 0, 0, "");
        playDataBeanList.add(playDataBean);

        //视频内容
        playDataBean = new PlayDataBean(2, "测试1", "http://video.sinosns.cn/fx7/3.mp4",
                "http://img20.360buyimg.com/da/jfs/t2578/2/885808803/92072/b4f3c332/566fb362Nb4837357.jpg",
                PlayDataType.Video, 0, 0, "");
        playDataBeanList.add(playDataBean);
    }

}
