package com.sino.hzb.play.view;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sino.hzb.play.R;
import com.sino.hzb.play.util.MyDialog;


public class DialogUtils {

	/**
	 * 简单消息对话框
	 * 
	 * @param context
	 * @param title
	 *            标题
	 * @param message
	 *            内容
	 * @param onClickListener
	 *            点击事件监听
	 */
	public static CustomeDialog dialogMessage(Context context, String title, String message, final CustomeDialog.OnCustomeDialogClickListener onClickListener) {

		if (context instanceof Activity && ((Activity) context).isFinishing()) {
			return null;
		}

		CustomeDialog dialog = new CustomeDialog(context, R.style.Custome_Dialog_Style);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setClickListener(onClickListener);
		dialog.show();
		return dialog;
	}

	/**
	 * 简单消息对话框
	 * 
	 * @param context
	 * @param title
	 *            标题
	 * @param message
	 *            内容
	 * @param onClickListener
	 *            点击事件监听
	 */
	public static CustomeDialog dialogMessage(Context context, int titleId, int messageId, final CustomeDialog.OnCustomeDialogClickListener onClickListener) {

		if (context instanceof Activity && ((Activity) context).isFinishing()) {
			return null;
		}

		CustomeDialog dialog = new CustomeDialog(context, R.style.Custome_Dialog_Style);
		dialog.setTitle(titleId);
		dialog.setMessage(messageId);
		dialog.setClickListener(onClickListener);
		dialog.show();
		return dialog;
	}

	/**
	 * 
	 * @param context
	 * @param title
	 *            标题
	 * @param message
	 *            内容
	 * @param negativeButtonText
	 *            右面按钮的文本
	 * @param positiveButtonText
	 *            左面按钮的文本
	 * @param onClickListener
	 *            点击事件监听
	 * @return
	 */
	public static CustomeDialog dialogMessage(Context context, String title, String message, String negativeButtonText, String positiveButtonText,
			final CustomeDialog.OnCustomeDialogClickListener onClickListener) {

		if (context instanceof Activity && ((Activity) context).isFinishing()) {
			return null;
		}

		CustomeDialog dialog = new CustomeDialog(context, R.style.Custome_Dialog_Style);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setNegativeButtonText(negativeButtonText);
		dialog.setPositiveButtonText(positiveButtonText);
		dialog.setClickListener(onClickListener);
		dialog.show();
		return dialog;
	}

	/**
	 * 
	 * @param context
	 * @param title
	 *            标题
	 * @param message
	 *            内容
	 * @param negativeButtonText
	 *            右面按钮的文本
	 * @param positiveButtonText
	 *            左面按钮的文本
	 * @param onClickListener
	 *            点击事件监听
	 * @return
	 */
	public static CustomeDialog dialogMessage(Context context, String title, String message, int negativeButtonText, int positiveButtonText,
			final CustomeDialog.OnCustomeDialogClickListener onClickListener) {

		if (context instanceof Activity && ((Activity) context).isFinishing()) {
			return null;
		}

		CustomeDialog dialog = new CustomeDialog(context, R.style.Custome_Dialog_Style);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setNegativeButtonText(negativeButtonText);
		dialog.setPositiveButtonText(positiveButtonText);
		dialog.setClickListener(onClickListener);
		dialog.show();
		return dialog;
	}

	/**
	 * 
	 * @param context
	 * @param title
	 *            标题
	 * @param message
	 *            内容
	 * @param negativeButtonText
	 *            右面按钮的文本
	 * @param positiveButtonText
	 *            左面按钮的文本
	 * @param onClickListener
	 *            点击事件监听
	 * @param gravityOrientation
	 *            内容的对齐方式(Gravity)
	 * @param marginsValues
	 *            内容距离边框的值(顺序左上右下)
	 * @return
	 */
	public static CustomeDialog dialogMessage(Context context, String title, String message, int negativeButtonText, int positiveButtonText,
			int gravityOrientation, int marginsValues[], final CustomeDialog.OnCustomeDialogClickListener onClickListener) {

		if (context instanceof Activity && ((Activity) context).isFinishing()) {
			return null;
		}
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(marginsValues[0], marginsValues[1], marginsValues[2], marginsValues[3]);

		CustomeDialog dialog = new CustomeDialog(context, R.style.Custome_Dialog_Style, gravityOrientation, params);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setNegativeButtonText(negativeButtonText);
		dialog.setPositiveButtonText(positiveButtonText);
		dialog.setClickListener(onClickListener);

		// dialogMessage.setGravity(gravityOrientation);
		// dialogMessage.setLayoutParams(params);

		dialog.show();
		return dialog;
	}

	/**
	 * 简单消息对话框（只有一个确定按钮）
	 * 
	 * @param context
	 * @param titleId
	 *            标题
	 * @param messageId
	 *            内容
	 * @param onClickListener
	 *            点击事件监听
	 */
	public static CustomeDialog dialogSigleButtonMessage(Context context, int titleId, int messageId,
			final CustomeDialog.OnCustomeDialogClickListener onClickListener) {
		if (context instanceof Activity && ((Activity) context).isFinishing()) {
			return null;
		}

		CustomeDialog dialog = new CustomeDialog(context, R.style.Custome_Dialog_Style);
		dialog.setTitle(titleId);
		dialog.setMessage(messageId);
		dialog.setPositiveButtonEnable(false);
		dialog.setClickListener(onClickListener);
		dialog.show();
		return dialog;
	}

	/**
	 * 简单消息对话框（只有一个确定按钮）
	 * 
	 * @param context
	 * @param title
	 *            标题
	 * @param message
	 *            内容
	 * @param onClickListener
	 *            点击事件监听
	 */
	public static CustomeDialog dialogSigleButtonMessage(Context context, String title, String message,
			final CustomeDialog.OnCustomeDialogClickListener onClickListener) {
		if (context == null) {
			return null;
		}
		if (context instanceof Activity && ((Activity) context).isFinishing()) {
			return null;
		}

		CustomeDialog dialog = new CustomeDialog(context, R.style.Custome_Dialog_Style);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setPositiveButtonEnable(false);
		dialog.setClickListener(onClickListener);
		dialog.show();
		
		return dialog;
	}
	/**
	 * 简单消息对话框（只有一个确定按钮）
	 * 
	 * @param context
	 * @param cancelable  返回键不响应事件
	 * @param title
	 *            标题
	 * @param message
	 *            内容
	 * @param onClickListener
	 *            点击事件监听
	 */
	public static CustomeDialog dialogSigleButtonMessage(Context context, boolean cancelable,String title, String message,
			final CustomeDialog.OnCustomeDialogClickListener onClickListener) {
		if (context == null) {
			return null;
		}
		if (context instanceof Activity && ((Activity) context).isFinishing()) {
			return null;
		}
		
		CustomeDialog dialog = new CustomeDialog(context, R.style.Custome_Dialog_Style);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setPositiveButtonEnable(false);
		dialog.setClickListener(onClickListener);
		dialog.setCancelable(cancelable);
		dialog.show();
		
		return dialog;
	}

	/**
	 * 简单消息对话框（只有一个确定按钮）
	 * 
	 * @param context
	 * @param title
	 *            标题
	 * @param message
	 *            内容
	 * @param onClickListener
	 *            点击事件监听
	 */
	public static CustomeDialog dialogSigleButtonMessageCenter(Context context, String title, String message,
			final CustomeDialog.OnCustomeDialogClickListener onClickListener) {
		if (context == null) {
			return null;
		}
		if (context instanceof Activity && ((Activity) context).isFinishing()) {
			return null;
		}

		CustomeDialog dialog = new CustomeDialog(context, R.style.Custome_Dialog_Style, Gravity.CENTER, null);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setPositiveButtonEnable(false);
		dialog.setClickListener(onClickListener);
		dialog.show();
		return dialog;
	}

	/**
	 * 简单消息对话框（只有一个确定按钮）
	 * 
	 * @param context
	 * @param title
	 *            标题
	 * @param message
	 *            内容
	 * @param onClickListener
	 *            点击事件监听
	 */
	public static CustomeDialog dialogSigleButtonMessage(Context context, String title, String message,
			final CustomeDialog.OnCustomeDialogClickListener onClickListener, boolean isShow) {
		if (context instanceof Activity && ((Activity) context).isFinishing()) {
			return null;
		}

		CustomeDialog dialog = new CustomeDialog(context, R.style.Custome_Dialog_Style);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setPositiveButtonEnable(false);
		dialog.setClickListener(onClickListener);
		if (isShow) {
			dialog.show();
		}
		return dialog;
	}

	/**
	 * 简单消息对话框（只有一个确定按钮）
	 * 
	 * @param context
	 * @param titleId
	 *            标题
	 * @param messageId
	 *            内容
	 * @param onClickListener
	 *            点击事件监听
	 */
	public static CustomeDialog dialogSigleButtonMessage(Context context, int titleId, int messageId, int negativeButtonText,
			final CustomeDialog.OnCustomeDialogClickListener onClickListener) {
		if (context instanceof Activity && ((Activity) context).isFinishing()) {
			return null;
		}

		CustomeDialog dialog = new CustomeDialog(context, R.style.Custome_Dialog_Style);
		dialog.setTitle(titleId);
		dialog.setMessage(messageId);
		dialog.setNegativeButtonText(negativeButtonText);
		dialog.setPositiveButtonEnable(false);
		dialog.setClickListener(onClickListener);
		dialog.show();
		return dialog;
	}

	/**
	 * 简单消息对话框（只有一个确定按钮）
	 * 
	 * @param context
	 * @param titleId
	 *            标题
	 * @param messageId
	 *            内容
	 * @param onClickListener
	 *            点击事件监听
	 */
	public static CustomeDialog dialogSigleButtonMessage(Context context, String title, String message, String negativeButtonText,
			final CustomeDialog.OnCustomeDialogClickListener onClickListener) {
		if (context instanceof Activity && ((Activity) context).isFinishing()) {
			return null;
		}

		CustomeDialog dialog = new CustomeDialog(context, R.style.Custome_Dialog_Style);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setNegativeButtonText(negativeButtonText);
		dialog.setPositiveButtonEnable(false);
		dialog.setClickListener(onClickListener);
		dialog.show();
		return dialog;
	}

	/**
	 * 显示一个分享的dialog.
	 * 
	 * @param context
	 * @param title
	 * @param content
	 * @param targetUrl
	 * @param mediaObject
	 * @param vid
	 * @return public static Dialog dialogCustomSharedView(Context context,
	 *         String title, String content, String targetUrl, UMImage
	 *         mediaObject, @Nullable String vid) {
	 * 
	 *         LayoutInflater inflater = (LayoutInflater)
	 *         context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 *         LinearLayout layout = (LinearLayout)
	 *         inflater.inflate(R.layout.view_dialog_shared_view, null);
	 *         CustomSharedView sharedView = (CustomSharedView)
	 *         layout.findViewById(R.id.dialog_custom_shared_view);
	 *         sharedView.startShared(title, content, targetUrl, mediaObject,
	 *         vid);
	 * 
	 *         final int cFullFillWidth = 10000;
	 *         layout.setMinimumWidth(cFullFillWidth); final Dialog dlg = new
	 *         Dialog(context, R.style.DataSheet);
	 * 
	 *         // set a large value put it in bottom Window w = dlg.getWindow();
	 *         WindowManager.LayoutParams lp = w.getAttributes(); lp.x = 0;
	 *         final int cMakeBottom = -1000; lp.y = cMakeBottom; lp.width =
	 *         lp.gravity = Gravity.BOTTOM; dlg.onWindowAttributesChanged(lp);
	 *         dlg.setCanceledOnTouchOutside(true); dlg.setContentView(layout);
	 *         sharedView.setParentDialog(dlg); return dlg; }
	 */

	/**
	 * 显示评价的dialog.
	 * 
	 * @param context
	 * @return public static Dialog dialogCourserEvaluateView(Context context,
	 *         int videoId, OnEvaluateListener evaluateListener) {
	 * 
	 *         LayoutInflater inflater = (LayoutInflater)
	 *         context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 *         LinearLayout layout = (LinearLayout)
	 *         inflater.inflate(R.layout.view_dialog_evaluate_view, null);
	 *         CourseEvaluateView evaluate_view = (CourseEvaluateView)
	 *         layout.findViewById(R.id.evaluate_view);
	 * 
	 *         final int cFullFillWidth = 10000;
	 *         layout.setMinimumWidth(cFullFillWidth); final Dialog dlg = new
	 *         Dialog(context, R.style.DataSheet);
	 * 
	 *         // set a large value put it in bottom Window w = dlg.getWindow();
	 *         WindowManager.LayoutParams lp = w.getAttributes(); lp.x = 0;
	 *         final int cMakeBottom = -1000; lp.y = cMakeBottom; lp.width =
	 *         lp.gravity = Gravity.BOTTOM; dlg.onWindowAttributesChanged(lp);
	 *         dlg.setCanceledOnTouchOutside(true); dlg.setContentView(layout);
	 *         dlg.show();
	 * 
	 *         evaluate_view.setVideoId(videoId);
	 *         evaluate_view.setParentDialog(dlg);
	 *         evaluate_view.setOnEvaluateListener(evaluateListener); return
	 *         dlg; }
	 */

	/**
	 * 显示一个可滚动选择的dialog
	 * 
	 * @param context
	 * @return public static Dialog dialogCustomWheelView(Context context,
	 *         List<String> strs, int position, OnWheelViewClickListener
	 *         listener) { final Dialog dlg = new Dialog(context,
	 *         R.style.DataSheet); LayoutInflater inflater = (LayoutInflater)
	 *         context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 *         LinearLayout layout = (LinearLayout)
	 *         inflater.inflate(R.layout.view_dialog_wheel_view, null);
	 *         CustomeWheelView wheelView = (CustomeWheelView)
	 *         layout.findViewById(R.id.dialog_costomer_wheel_view);
	 *         wheelView.setWheelListener(listener);
	 *         wheelView.setStringListAdapter(strs, position); wheelView.show();
	 * 
	 *         final int cFullFillWidth = 10000;
	 *         layout.setMinimumWidth(cFullFillWidth);
	 * 
	 *         // set a large value put it in bottom Window w = dlg.getWindow();
	 *         WindowManager.LayoutParams lp = w.getAttributes(); lp.x = 0;
	 *         final int cMakeBottom = -1000; lp.y = cMakeBottom; lp.width =
	 *         lp.gravity = Gravity.BOTTOM; dlg.onWindowAttributesChanged(lp);
	 *         dlg.setCanceledOnTouchOutside(true); dlg.setContentView(layout);
	 *         dlg.show();
	 * 
	 *         wheelView.setParentDialog(dlg); return dlg; }
	 */

	/**
	 * 显示一个含有两个滚动选择的dialog
	 * 
	 * @param context
	 * @return public static Dialog dialogTowCustomTowWheelView(Context context,
	 *         List<String> strs, int position, List<String> secondStrs, int
	 *         secondPositon, OnWheelViewClickListener listener,
	 *         OnWheelChangedListener onFirstWheelChangedListener) { final
	 *         Dialog dlg = new Dialog(context, R.style.DataSheet);
	 *         LayoutInflater inflater = (LayoutInflater)
	 *         context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 *         LinearLayout layout = (LinearLayout)
	 *         inflater.inflate(R.layout.view_dialog_two_wheel_view, null);
	 *         CustomTwoWheelView wheelView = (CustomTwoWheelView)
	 *         layout.findViewById(R.id.dialog_costomer_tow_wheel_view);
	 *         wheelView.setWheelListener(listener);
	 *         wheelView.setStringListAdapter(strs, position);
	 *         wheelView.setSecondStringListAdapter(secondStrs, secondPositon);
	 *         wheelView
	 *         .setFirstWheelChangedListener(onFirstWheelChangedListener);
	 *         wheelView.showSecondWheel();
	 * 
	 *         final int cFullFillWidth = 10000;
	 *         layout.setMinimumWidth(cFullFillWidth);
	 * 
	 *         // set a large value put it in bottom Window w = dlg.getWindow();
	 *         WindowManager.LayoutParams lp = w.getAttributes(); lp.x = 0;
	 *         final int cMakeBottom = -1000; lp.y = cMakeBottom; lp.width =
	 *         lp.gravity = Gravity.BOTTOM; dlg.onWindowAttributesChanged(lp);
	 *         dlg.setCanceledOnTouchOutside(true); dlg.setContentView(layout);
	 *         dlg.show();
	 * 
	 *         wheelView.setParentDialog(dlg); return dlg; }
	 */
	/*
	 * public static Dialog showFillInProfileTaskDialog(Context context) { final
	 * Dialog dlg = new Dialog(context, R.style.DataSheet); LayoutInflater
	 * inflater = (LayoutInflater)
	 * context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); LinearLayout
	 * layout = (LinearLayout)
	 * inflater.inflate(R.layout.view_custom_dialog_task_view, null); final
	 * View.OnClickListener listener = new View.OnClickListener() { public void
	 * onClick(View v) { if (dlg != null && dlg.isShowing()) { dlg.dismiss(); }
	 * } };
	 * layout.findViewById(R.id.customer_dialog_cancel).setOnClickListener(listener
	 * );
	 * layout.findViewById(R.id.customer_dialog_ok).setOnClickListener(listener
	 * ); dlg.setContentView(layout); dlg.show();
	 * 
	 * return dlg; }
	 */

	/**
	 * 等待的对话框
	 * 
	 * @param context
	 * @param title
	 *            标题
	 * @param msg
	 *            内容
	 * @param onClickListener
	 *            点击事件监听
	 */
	public static ProgressDialog dialogProgress(Context context, String title, String msg, boolean isCancelable, boolean isShow) {

		if (context instanceof Activity && ((Activity) context).isFinishing()) {
			return null;
		}

		// 创建ProgressDialog对象
		ProgressDialog mProgressDialog = new ProgressDialog(context);
		// 设置进度条风格，风格为圆形，旋转的
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 设置ProgressDialog 标题
		mProgressDialog.setTitle(title);
		// 设置ProgressDialog提示信息
		mProgressDialog.setMessage(msg);
		// 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
		mProgressDialog.setIndeterminate(false);
		// 设置ProgressDialog 是否可以按退回键取消
		mProgressDialog.setCancelable(isCancelable);
		if (isShow) {
			// 让ProgressDialog显示
			mProgressDialog.show();
		}

		return mProgressDialog;
	}

	/**
	 * 返回 一个 progressDialog 无遮盖层
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
	public static MyDialog createLoadingDialog(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		// // main.xml中的ImageView
		// ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
		// // 加载动画
		// Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
		// context, R.anim.load_animation);
		// // 使用ImageView显示动画
		// spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);// 设置加载信息

		int marginsValues[] = new int[] { context.getResources().getDimensionPixelOffset(R.dimen.px34), 0,
				context.getResources().getDimensionPixelOffset(R.dimen.px34), 0 };

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(marginsValues[0], marginsValues[1], marginsValues[2], marginsValues[3]);

		MyDialog loadingDialog = new MyDialog(context, R.style.loading_dialog);// 创建自定义样式dialog

		loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
		return loadingDialog;
	}

	private static Dialog fansDialog;

	public static void dismissFansDialog() {
		if (fansDialog != null && fansDialog.isShowing()) {
			fansDialog.dismiss();
		}
	}

	/**
	 * 显示一个图片选择的的dialog.
	 * 
	 */
	public static Dialog dialogCustomImageSelectView(Activity activity, final View.OnClickListener clickListener) {

		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.view_get_image, null);
		View imageSeletedView = layout.findViewById(R.id.view_head_image_selected_container);

		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);
		final Dialog dlg = new Dialog(activity, R.style.MaskMenu);

		View.OnClickListener listener = new View.OnClickListener() {
			public void onClick(View v) {
				clickListener.onClick(v);
				if (dlg.isShowing()) {
					dlg.dismiss();
				}
			}
		};

		imageSeletedView.findViewById(R.id.image_from_taking_photo).setOnClickListener(listener);
		imageSeletedView.findViewById(R.id.image_from_gallery).setOnClickListener(listener);
		imageSeletedView.findViewById(R.id.mylexue_image_cancle).setOnClickListener(listener);

		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.width = lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(true);
		dlg.setContentView(layout);
		dlg.show();
		return dlg;
	}

	/**
	 * 
	 * @param activity
	 * @param clickListener
	 * @return
	 */
	public static Dialog dialogCustomSexSelectView(Activity activity, final View.OnClickListener clickListener) {

		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.view_get_image, null);
		View imageSeletedView = layout.findViewById(R.id.view_head_image_selected_container);

		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);
		final Dialog dlg = new Dialog(activity, R.style.MaskMenu);

		View.OnClickListener listener = new View.OnClickListener() {
			public void onClick(View v) {
				clickListener.onClick(v);
				if (dlg.isShowing()) {
					dlg.dismiss();
				}
			}
		};
		((Button) imageSeletedView.findViewById(R.id.image_from_taking_photo)).setText("男");
		((Button) imageSeletedView.findViewById(R.id.image_from_gallery)).setText("女");
		imageSeletedView.findViewById(R.id.image_from_taking_photo).setOnClickListener(listener);
		imageSeletedView.findViewById(R.id.image_from_gallery).setOnClickListener(listener);
		imageSeletedView.findViewById(R.id.mylexue_image_cancle).setOnClickListener(listener);

		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.width = lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(true);
		dlg.setContentView(layout);
		dlg.show();
		return dlg;
	}
}
