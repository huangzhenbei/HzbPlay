package com.sino.hzb.play.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.sino.hzb.play.R;


public class CustomeDialog extends Dialog {

	private TextView dialogTitle;
	private TextView dialogMessage;
	private TextView dialogNegative;
	private TextView dialogPositive;
	private CharSequence title;
	private CharSequence message;
	private CharSequence negativeButtonText; // 右面按钮的文本
	private CharSequence positiveButtonText; // 左面按钮的文本
	private View dialogLine;
	private boolean positiveButtonEnable = true; // 是否启用左面的按钮

	private int gravityOrientation=3;
	private LayoutParams params;

	private OnCustomeDialogClickListener clickListener;

	public static enum CustomeDialogClickType {
		Cancel, Ok
	}

	public interface OnCustomeDialogClickListener {
		public void onCustomeDialogClick(CustomeDialogClickType type);
	}

	public CustomeDialog(Context context) {
		super(context);

	}

	public CustomeDialog(Context context, int theme) {
		super(context, theme);

	}

	public CustomeDialog(Context context, int theme, int gravityOrientation, LayoutParams params) {
		super(context, theme);
		this.gravityOrientation = gravityOrientation;
		this.params = params;

	}

	protected CustomeDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_coustom_dialog_view);
		
		this.dialogTitle = (TextView) this.findViewById(R.id.custom_dialog_title);
		this.dialogMessage = (TextView) this.findViewById(R.id.custom_dialog_message);
		this.dialogNegative = (TextView) this.findViewById(R.id.customer_dialog_ok);
		this.dialogPositive = (TextView) this.findViewById(R.id.customer_dialog_cancel);
		this.dialogLine = this.findViewById(R.id.customer_dialog_line);

		if (gravityOrientation == 0) {
			gravityOrientation = 3;
		}
		this.dialogMessage.setGravity(gravityOrientation);
		if (params != null) {
			this.dialogMessage.setLayoutParams(params);
		}
		
		this.findViewById(R.id.customer_dialog_cancel).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (clickListener != null) {
					clickListener.onCustomeDialogClick(CustomeDialogClickType.Cancel);
				}
				if (isShowing()) {
					dismiss();
				}
			}
		});
		this.findViewById(R.id.customer_dialog_ok).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (clickListener != null) {
					clickListener.onCustomeDialogClick(CustomeDialogClickType.Ok);
				}
				if (isShowing()) {
					dismiss();
				}
			}
		});

		if (TextUtils.isEmpty(title)) {
			this.dialogTitle.setVisibility(View.GONE);
		} else {
			this.dialogTitle.setText(title);
		}

		if (TextUtils.isEmpty(message)) {
			this.dialogMessage.setVisibility(View.GONE);
		} else {
			this.dialogMessage.setText(message);
		}

		if (!TextUtils.isEmpty(negativeButtonText)) {
			this.dialogNegative.setText(negativeButtonText);
		}

		if (!TextUtils.isEmpty(positiveButtonText)) {
			this.dialogPositive.setText(positiveButtonText);
		}

		if (positiveButtonEnable) {
			dialogPositive.setVisibility(View.VISIBLE);
			dialogLine.setVisibility(View.VISIBLE);
		} else {
			dialogPositive.setVisibility(View.GONE);
			dialogLine.setVisibility(View.GONE);
		}
	}

	@Override
	public void show() {
		super.show();
		int height = this.getWindow().getAttributes().height;
		this.getWindow().setLayout(this.getContext().getResources().getDimensionPixelOffset(R.dimen.px540),height);
	}

	@Override
	public void setTitle(int titleId) {

		if (titleId > 0)
			setTitle(getContext().getResources().getString(titleId));
	}

	@Override
	public void setTitle(CharSequence title) {
		this.title = title;

	}

	public void setMessage(int messageId) {
		if (messageId > 0)
			setMessage(getContext().getResources().getString(messageId));
	}

	public void setMessage(CharSequence message) {
		this.message = message;
	}

	public void setPositiveButtonEnable(boolean enable) {
		this.positiveButtonEnable = enable;
	}

	public void setClickListener(OnCustomeDialogClickListener clickListener) {
		this.clickListener = clickListener;
	}

	public void setNegativeButtonText(CharSequence negativeButtonText) {
		this.negativeButtonText = negativeButtonText;
	}

	public void setPositiveButtonText(CharSequence positiveButtonText) {
		this.positiveButtonText = positiveButtonText;
	}

	public void setNegativeButtonText(int negativeButtonText) {
		setNegativeButtonText(getContext().getResources().getString(negativeButtonText));
	}

	public void setPositiveButtonText(int positiveButtonText) {
		setPositiveButtonText(getContext().getResources().getString(positiveButtonText));
	}

}
