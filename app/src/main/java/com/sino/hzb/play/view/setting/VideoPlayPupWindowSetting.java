package com.sino.hzb.play.view.setting;

import android.widget.PopupWindow;

/**
 * videoPlay中popupwindow的设置
 * @author hzb
 *
 */
public abstract class VideoPlayPupWindowSetting {
	
	private PopupWindow mPopupWindow;
	
	public VideoPlayPupWindowSetting(PopupWindow mPopupWindow){
		this.mPopupWindow=mPopupWindow;
	} ;

	public abstract void popupWindonViewSetting();
	
	public void dismiss(){
		mPopupWindow.dismiss();
	}
	public boolean isShowing(){
		return mPopupWindow.isShowing();
	}
	
	
}
