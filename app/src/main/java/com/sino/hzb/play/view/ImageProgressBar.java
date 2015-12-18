package com.sino.hzb.play.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

/**
 * Created by ymr on 15-4-1.
 */
public class ImageProgressBar extends ImageView {

    private static final String TAG = "ImageProgressBar";
    private ObjectAnimator mAnimator;

    public ImageProgressBar(Context context) {
        super(context);
    }

    public ImageProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == VISIBLE) {
            startAnim();
        } else {
            mAnimator.cancel();
        }
    }

    @Override
    protected void onFinishInflate() {
        startAnim();
    }

    private void startAnim() {
        mAnimator = ObjectAnimator.ofFloat(this, "Rotation", 0, 360);
        mAnimator.setDuration(1500);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.start();
    }
}
