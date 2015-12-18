package com.sino.hzb.play.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sino.hzb.play.R;


public class ToastManager {

    private static ToastManager sInstance = new ToastManager();
    private Toast toast;

    private ToastManager() {}

    public static ToastManager getInstance() {
        return sInstance;
    }

    public void showToastCenter(Context context, String text) {
        if (context == null) {
            return;
        }
        showToast(context, text, Gravity.CENTER);
    }

    public void showToastCenter(Context context, int textResId) {
        if (context == null) {
            return;
        }
        showToast(context, context.getResources().getString(textResId), Gravity.CENTER);
    }

    public void showToast(Context context, String text) {
        if (context == null) {
            return;
        }
        showToast(context, text, Gravity.BOTTOM);
    }

    public void showToast(Context context, int textResId) {
        if (context == null) {
            return;
        }
        showToast(context, context.getResources().getString(textResId), Gravity.BOTTOM);
    }

    public void showToast(Context context, String text, int gravity) {
        if (context == null) {
            return;
        }
        cancelToast();
        toast = new Toast(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View toastView = inflater.inflate(R.layout.view_shared_toast_tipview, null);
        ((TextView) toastView.findViewById(R.id.toast_tip_text)).setText(text);
        if (gravity == Gravity.CENTER) {
            (toastView.findViewById(R.id.toast_tip_image)).setVisibility(View.VISIBLE);
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setGravity(Gravity.BOTTOM, 0, DisplayUtils.dpToPx(context, 70));
        }
        toast.setView(toastView);
        toast.show();
    }

    public void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }

    public void onDistroy() {
        cancelToast();
        toast = null;
    }
}
