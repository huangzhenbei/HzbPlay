package com.sino.hzb.play.util;

import android.app.Dialog;
import android.content.Context;

import com.sino.hzb.play.R;


public class MyDialog extends Dialog {

	public MyDialog(Context context, int theme) {
		super(context, theme);



int i=0;

		// TODO Auto-generated constructor stub
	}

	protected MyDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public MyDialog(Context context) {
		super(context);
	}
	
	@Override
	public void show() {
		super.show();
		
		int height = this.getWindow().getAttributes().height;
		int width = this.getWindow().getAttributes().width;
//		this.getWindow().setLayout(width,height);
		this.getWindow().setLayout(this.getContext().getResources().getDimensionPixelOffset(R.dimen.px540),height);
	}
}
