package com.sino.hzb.play;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.sino.hzb.play.model.PlayDataBean;
import com.sino.hzb.play.model.PlayDataType;
import com.sino.hzb.play.util.DisplayUtils;
import com.sino.hzb.play.util.ImageRender;
import com.sino.hzb.play.view.setting.PlaySetting;
import com.sino.hzb.play.view.setting.VideoPlayPupWindowSetting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends PlaySetting {

    public final static String imageLoaderPath = "/fanXQ/image/cache/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化ImageLoader
        ImageRender.getInstance().init(this);

        initData();
        intPlay(this, R.id.course_main_videoplayview, playDataBeanList);

        initPopupWindow();
    }

    /**
     * 视频数据
     */
    private void initData() {
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


    /**
     * 为videoPlayView设置popuwindow的显示
     */
    private void initPopupWindow() {
        mContentView = this.getLayoutInflater().inflate(R.layout.view_popupwindow_everydayvideo, null);
        final PopupWindow mPopupWindow = new PopupWindow(mContentView, DisplayUtils.dpToPx(this, 270),
                LinearLayout.LayoutParams.MATCH_PARENT);


        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // videoPlayStatus = 1;
            }
        });
        videoPlayView
                .setVideoPlayPupupWindow(mIVideoPlayPupWindowSetting = new VideoPlayPupWindowSetting(mPopupWindow) {
                    @Override
                    public void popupWindonViewSetting() {

                        ListView popupListView;
                        PopupWindowAdapter adapter;

                        List<Map<Object, Object>> tjqS = new ArrayList<Map<Object, Object>>();
                        Map<Object, Object> map=new HashMap<Object, Object>();

                        map.put("img","http://img20.360buyimg.com/da/jfs/t2578/2/885808803/92072/b4f3c332/566fb362Nb4837357.jpg");
                        map.put("title","测试1");
                        map.put("name","测试1.1");
                        map.put("isTime","1");
                        tjqS.add(map);

                        map.clear();
                        map.put("img","http://img20.360buyimg.com/da/jfs/t2578/2/885808803/92072/b4f3c332/566fb362Nb4837357.jpg");
                        map.put("title","测试2");
                        map.put("name","测试2.2");
                        map.put("isTime","2");
                        tjqS.add(map);

                        map.clear();
                        map.put("img","http://img20.360buyimg.com/da/jfs/t2578/2/885808803/92072/b4f3c332/566fb362Nb4837357.jpg");
                        map.put("title","测试3");
                        map.put("name","测试3.3");
                        map.put("isTime","1");
                        tjqS.add(map);


                        /**
                         * 初始化PopupWindow布局,根据自己的需要重写
                         */
                        mPopupWindow.setFocusable(true);
                        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
                        mPopupWindow.showAtLocation(videoPlayView.titleBar, Gravity.RIGHT, 0, 0);

                        videoPlayView.hidePlayControllBar();

                        /**
                         * 为PopupWindow布局填充数据，根据自己的需要重写
                         */
                        popupListView = (ListView) mContentView.findViewById(R.id.lv_popup);
                        adapter = new PopupWindowAdapter(MainActivity.this, tjqS);
                        popupListView.setAdapter(adapter);
                        popupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            }
                        });
                    }
                });
    }

}
