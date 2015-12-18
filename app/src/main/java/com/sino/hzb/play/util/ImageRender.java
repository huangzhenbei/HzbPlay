package com.sino.hzb.play.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.sino.hzb.play.MainActivity;


public class ImageRender {
	private static ImageRender sInstance = new ImageRender();
	private boolean mIsInited;

	private ImageRender() {
	}

	public static ImageRender getInstance() {
		return sInstance;
	}

	public void init(Context context) {
		if (mIsInited) {
			return;
		}
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator()).diskCacheSize(50 * 1024 * 1024)// 50
																										// Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO).diskCache(new UnlimitedDiscCache(StorageUtils.getOwnCacheDirectory(context, MainActivity.imageLoaderPath)))// 自定义缓存路径
				// .writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
		mIsInited = true;
	}

	public void clear() {
		ImageLoader.getInstance().clearMemoryCache();
		ImageLoader.getInstance().clearDiskCache();
	}

	public void setImage(ImageView imageView, String urlStr, int defaultImageResourceID) {
		// float corner = imageView.getContext().getResources().getDimension(
		// 10);
		setImage(imageView, urlStr, defaultImageResourceID, defaultImageResourceID, 0, true, null);
	}
	public void setImage(ImageView imageView, String urlStr, int defaultImageResourceID, int errorImageResoureID) {
		// float corner = imageView.getContext().getResources().getDimension(
		// 10);
		setImage(imageView, urlStr, defaultImageResourceID, errorImageResoureID, 0, true, null);
	}

	public void setImage(ImageView imageView, String urlStr, int defaultImageResourceID, int cornerRadiusPixels, boolean cache, ImageLoadingListener listener) {
		setImage(imageView, urlStr, defaultImageResourceID, defaultImageResourceID, cornerRadiusPixels, cache, listener);
	}

	public void setImage(ImageView imageView, String urlStr, int defaultImageResourceID, int errorImageResoureID, int cornerRadiusPixels, boolean cache,
			ImageLoadingListener listener) {
		if (imageView == null) {
			return;
		}
		if (TextUtils.isEmpty(urlStr)) {
			if (defaultImageResourceID > 0) {
				imageView.setImageResource(defaultImageResourceID);
			}
			return;
		}
		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(defaultImageResourceID).showImageForEmptyUri(defaultImageResourceID)
				.showImageOnFail(errorImageResoureID).cacheInMemory(cache).cacheOnDisk(cache).considerExifParams(true).build();
		ImageLoader.getInstance().displayImage(urlStr, imageView, options, listener);
	}
}