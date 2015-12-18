package com.sino.hzb.play.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class MyCircleProgress extends ImageView {

	// 画笔
	private Paint paint;
	private Context context;
	// 进度条的值，实际上是角度
	public int n = 0;

	public MyCircleProgress(Context context) {
		super(context);
	}

	public MyCircleProgress(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.context = context;
		this.paint = new Paint();
		this.paint.setAntiAlias(true); // 消除锯齿
		this.paint.setStyle(Style.STROKE); // 绘制空心圆或 空心矩形

	}

	public MyCircleProgress(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		// int center = getWidth() / 2;
		// int innerCircle = dip2px(context, 83); // 内圆半径
		// int ringWidth = dip2px(context, 10); // 圆环宽度
		//
		// // 第一种方法绘制圆环
		// // 绘制内圆
		// this.paint.setARGB(255, 255, 0, 0);
		// this.paint.setStrokeWidth(2);
		// canvas.drawCircle(center, center, innerCircle, this.paint);
		//
		// // 绘制圆环
		// this.paint.setARGB(255, 0, 255, 0);
		// this.paint.setStrokeWidth(ringWidth);
		// // 原本画一个圆环 ，下面改为画圆弧
		// // canvas.drawCircle(center, center, innerCircle + ringWidth / 2,
		// // this.paint);
		// Log.d("~~~~", (center - (innerCircle + ringWidth / 2)) + " "
		// + (center + (innerCircle + ringWidth / 2)));
		//
		// // 计算出这个矩形（正方形）的上下左右的坐标，圆心的未知加减半径大小
		// // (半径只是表示在这个未知画，画出的宽度要setStrokeWidth来设置,
		// // 宽度是从半径的位置分别往两边扩张StrokeWidth／2的大小)
		// int top = (center - (innerCircle + ringWidth / 2));
		// int bottom = (center + (innerCircle + ringWidth / 2));
		//
		// RectF oval = new RectF(top, top, bottom, bottom);
		// // 正上方哪个点是270度，从270开始画180度
		// canvas.drawArc(oval, 270, 180, false, paint);//
		// false表示不链接圆弧的起点和终点来闭合这个圆弧
		//
		// // 绘制外圆
		// this.paint.setARGB(255, 0, 0, 255);
		// this.paint.setStrokeWidth(2);
		// canvas.drawCircle(center, center, innerCircle + ringWidth,
		// this.paint);

		int center = getWidth() / 2;// 圆心位置
		int outerCircle = Math.abs(center - getLeft());// 外圆半径
		int innerCircle = Math.abs(center - getLeft() - outerCircle / 2); // 内圆半径
		int ringWidth = outerCircle - innerCircle;// 圆弧宽度

		// 第一种方法绘制圆环
		// 绘制内圆
		this.paint.setARGB(255, 255, 255, 255);
		this.paint.setStrokeWidth(2);
		canvas.drawCircle(center, center, innerCircle - 1, this.paint);

		// 绘制圆环
		this.paint.setARGB(200, 255, 255, 255);
		this.paint.setStrokeWidth(ringWidth);
		// 原本画一个圆环 ，下面改为画圆弧
		// canvas.drawCircle(center, center, innerCircle + ringWidth / 2,
		// this.paint);

		// 计算出这个矩形（正方形）的上下左右的坐标，圆心的未知加减半径大小
		// (半径只是表示在这个未知画，画出的宽度要setStrokeWidth来设置,
		// 宽度是从半径的位置分别往两边扩张StrokeWidth／2的大小)
		int top = (center - (innerCircle + ringWidth / 2));
		int bottom = (center + (innerCircle + ringWidth / 2));

		RectF oval = new RectF(top, top, bottom, bottom);
		// 正上方哪个点是270度，从270开始画N度
		canvas.drawArc(oval, 270, n, false, paint);// false表示不链接圆弧的起点和终点来闭合这个圆弧

		// 绘制外圆
		this.paint.setARGB(255, 255, 255, 255);
		this.paint.setStrokeWidth(2);
		canvas.drawCircle(center, center, innerCircle + ringWidth, this.paint);

		// 在中心绘制一个图片(正方形)
		int x = Math.abs(center - innerCircle);
		int width = center + innerCircle;
		
		/**
		 * 
		//设置 进度条中间图标的
		Bitmap bm = BitmapFactory.decodeResource(getResources(), com.sino.fanxq.R.drawable.test1);

		Rect dst = new Rect();
		// 在正中间
		// dst.left = x;
		// dst.top = x;
		// dst.right = width;
		// dst.bottom = width;

		// 在正中间 大小变成原来的三分之一
		dst.left = x + (center - innerCircle) / 3;
		dst.top = x + (center - innerCircle) / 3;
		dst.right = width - (center - innerCircle) / 3;
		dst.bottom = width - (center - innerCircle) / 3;

		// 这个方法画中间图片 left top 表示坐标，rigth botton分别是表示这四个点的坐标
		canvas.drawBitmap(bm, null, dst, null);
		super.onDraw(canvas);
		
		 */
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 * 
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Log.d("setMeasuredDimension", measureWidth(widthMeasureSpec) + " " + measureHeight(heightMeasureSpec));
		setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
	}

	private int measureWidth(int widthMeasureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(widthMeasureSpec);
		int specSize = MeasureSpec.getSize(widthMeasureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = dip2px(context, 120);
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	private int measureHeight(int measuredHeight) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measuredHeight);
		int specSize = MeasureSpec.getSize(measuredHeight);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = dip2px(context, 120);
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	/**
	 * 绘制图片
	 * 
	 * @param x屏幕上的x坐标
	 * @param y屏幕上的y坐标
	 * @param w要绘制的图片的宽度
	 * @param h要绘制的图片的高度
	 * @param bx图片上的x坐标
	 * @param by图片上的y坐标
	 * 
	 * @return null
	 */
	// public static void drawImage(Canvas canvas, Bitmap blt, int x, int y,
	// int w, int h, int bx, int by) {
	// Rect src = new Rect();// 图片 >>原矩形
	// Rect dst = new Rect();// 屏幕 >>目标矩形
	//
	// src.left = bx;
	// src.top = by;
	// src.right = bx + w;
	// src.bottom = by + h;
	//
	// dst.left = x;
	// dst.top = y;
	// dst.right = x + w;
	// dst.bottom = y + h;
	// // 画出指定的位图，位图将自动--》缩放/自动转换，以填补目标矩形
	// // 这个方法的意思就像 将一个位图按照需求重画一遍，画后的位图就是我们需要的了
	// canvas.drawBitmap(blt, null, dst, null);
	// src = null;
	// dst = null;
	// }

}
