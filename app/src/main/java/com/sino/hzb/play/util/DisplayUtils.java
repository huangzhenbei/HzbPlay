/*
 * Copyright (C) 2014 lzh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sino.hzb.play.util;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class DisplayUtils {
    private DisplayUtils() {
        throw new AssertionError();
    }

    public static boolean isLandscape(Context context) {
        if (context == null)
            return false;
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean isLandscapeTablet(Context context) {
        return isLandscape(context) && isTablet(context);
    }

    public static Point getDisplayPixelSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static int getDisplayPixelWidth(Context context) {
        Point size = getDisplayPixelSize(context);
        return (size.x);
    }

    public static int getDisplayPixelHeight(Context context) {
        Point size = getDisplayPixelSize(context);
        return (size.y);
    }

    public static int dpToPx(Context context, int dp) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context
                .getResources().getDisplayMetrics());
        return (int) px;
    }

    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((px / displayMetrics.density) + 0.5);
    }

    public static boolean isTablet(Context context) {
        // http://stackoverflow.com/a/8427523/1673548
        if (context == null)
            return false;
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isXLarge(Context context) {
        if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE)
            return true;
        return false;
    }

    /**
     * returns the height of the ActionBar if one is enabled - supports both the
     * native ActionBar and ActionBarSherlock -
     * http://stackoverflow.com/a/15476793/1673548
     */
    public static int getActionBarHeight(Context context) {
        if (context == null) {
            return 0;
        }
        TypedValue tv = new TypedValue();
        if (context.getTheme() != null
                && context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources()
                    .getDisplayMetrics());
        }

        // if we get this far, it's because the device doesn't support an
        // ActionBar,
        // so return the standard ActionBar height (48dp)
        return dpToPx(context, 48);
    }

    /**
     * detect when FEATURE_ACTION_BAR_OVERLAY has been set
     */
    public static boolean hasActionBarOverlay(Window window) {
        return window.hasFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
    }
    
    /**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 *
	 * @param spValue
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue / fontScale + 0.5f);
	}
}
