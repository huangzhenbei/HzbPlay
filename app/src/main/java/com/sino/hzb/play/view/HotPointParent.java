package com.sino.hzb.play.view;

import java.util.List;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.sino.hzb.play.R;


/**
 * Created by ymr on 15-3-5.
 */
public class HotPointParent extends FrameLayout {
    private static final int UPDATE_HOT_POINT = 0;
    private static final String TAG = "HotPointParent";

    private List<VideoChapter> mAnchors;
    private int mTotalDuration;

    private Context mContext;
    private WindowManager mWindowManager;
    private HotPointListener listener;

    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
            case UPDATE_HOT_POINT:
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                if (displayMetrics.heightPixels < displayMetrics.widthPixels) {
                    createHotPoints();
                }
                break;
            }
        }
    };

    public void setListener(HotPointListener listener) {
        this.listener = listener;
    }

    public static interface HotPointListener {
        void onHotPointClick(VideoChapter anchor);

        void onDisplayPointClick(VideoChapter anchor);
    }

    private OnClickListener mHotPointClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            showAnchorWindow(v);
        }
    };
    private WindowManager.LayoutParams mLayoutParams;
    private View mFloatPanel;
    private TextView mHotTitle;
    private View mPositionFlag;

    private void showAnchorWindow(View v) {
        VideoChapter anchor = (VideoChapter) v.getTag();

        VideoChapter displayAnchor = (VideoChapter) mFloatPanel.getTag();

        if (listener != null) {
            listener.onHotPointClick(anchor);
        }

        if (displayAnchor != null && anchor.anchor_time == displayAnchor.anchor_time && listener != null) {
            listener.onDisplayPointClick(anchor);
            return;
        }

        if (mLayoutParams == null) {
            mLayoutParams = new WindowManager.LayoutParams();
            mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
            mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_FULLSCREEN;
            mLayoutParams.gravity = Gravity.BOTTOM | Gravity.LEFT;

            mLayoutParams.format = PixelFormat.TRANSLUCENT;
        }

        Rect viewRect = new Rect();
        v.getGlobalVisibleRect(viewRect);
        Rect parentRect = new Rect();
        getGlobalVisibleRect(parentRect);
        mHotTitle.setText(anchor.anchor_title);
        mFloatPanel.measure(0, 0);
        mFloatPanel.setTag(anchor);
        mLayoutParams.x = viewRect.left - mFloatPanel.getMeasuredWidth() / 2 + viewRect.width() / 2;
        mLayoutParams.y = getContext().getResources().getDimensionPixelSize(R.dimen.video_play_controll_bar_height) / 2
                - getResources().getDimensionPixelSize(R.dimen.hot_point_float_panel_offset);

        if (mFloatPanel.getParent() == null) {
            mWindowManager.addView(mFloatPanel, mLayoutParams);
        } else {
            mWindowManager.updateViewLayout(mFloatPanel, mLayoutParams);
        }
    }

    public void hideAnchorWindow() {
        if (mFloatPanel != null && mFloatPanel.getParent() != null && mWindowManager != null) {
            mFloatPanel.setTag(null);
            mWindowManager.removeView(mFloatPanel);
        }
    }

    public HotPointParent(Context context) {
        super(context);
        init(context);
    }

    public HotPointParent(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HotPointParent(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        mFloatPanel = View.inflate(mContext, R.layout.view_hot_float_panel, null);
        mFloatPanel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDisplayPointClick((VideoChapter) v.getTag());
            }
        });
        mHotTitle = (TextView) mFloatPanel.findViewById(R.id.hot_point_title);
        mPositionFlag = mFloatPanel.findViewById(R.id.position_flag);
    }

    public void setAnchors(List<VideoChapter> anchors) {
        this.mAnchors = anchors;
        mHandler.sendEmptyMessage(UPDATE_HOT_POINT);
    }

    public void setTotalDuration(int totalDuration) {
        if (mTotalDuration != totalDuration) {
            this.mTotalDuration = totalDuration;
            mHandler.sendEmptyMessage(UPDATE_HOT_POINT);
        }
    }

    private void createHotPoints() {
        if (mAnchors != null && mAnchors.size() > 0 && mTotalDuration > 0) {
            removeAllViews();
            int width = getMeasuredWidth();
            for (VideoChapter anchor : mAnchors) {
                ImageView hotPoint = (ImageView) View.inflate(getContext(), R.layout.view_hot_point, null);
                float marginOffset = (float) anchor.anchor_time * 1000 / mTotalDuration;
                int leftMargin = (int) (width * marginOffset);
                hotPoint.measure(0, 0);
                LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
                layoutParams.leftMargin = leftMargin - hotPoint.getMeasuredWidth() / 2;
                hotPoint.setLayoutParams(layoutParams);
                hotPoint.setOnClickListener(mHotPointClickListener);
                hotPoint.setTag(anchor);
                addView(hotPoint);
            }
            requestLayout();
            bringToFront();
        }
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mHandler.sendEmptyMessage(UPDATE_HOT_POINT);
    }

    public void setCurrTime(int currTime) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            VideoChapter anchor = (VideoChapter) child.getTag();
            if (Math.abs(currTime - anchor.anchor_time * 1000) < 10000) {
                child.setVisibility(GONE);
                child.setOnClickListener(null);
            } else {
                child.setVisibility(VISIBLE);
                child.setOnClickListener(mHotPointClickListener);
            }
        }
    }
}
