package com.sino.hzb.play.util;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络相关的工具类
 * 
 */
public class NetworkUtils {
	private NetworkUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 判断网络是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnected(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (null != connectivity) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (null != info && info.isConnected()) {
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断是否是wifi连接
	 */
	public static boolean isWifiConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			return false;
		}
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info == null) {
			return false;
		}
		return info.getType() == ConnectivityManager.TYPE_WIFI;

	}

	/**
	 * 打开网络设置界面
	 */
	public static void openSetting(Activity activity) {
		Intent intent = new Intent("/");
		ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
		intent.setComponent(cm);
		intent.setAction("android.intent.action.VIEW");
		activity.startActivityForResult(intent, 0);
	}

	public static String getBoundary() {
		StringBuffer _sb = new StringBuffer();
		for (int t = 1; t < 12; t++) {
			long time = System.currentTimeMillis() + t;
			if (time % 3 == 0) {
				_sb.append((char) time % 9);
			} else if (time % 3 == 1) {
				_sb.append((char) (65 + time % 26));
			} else {
				_sb.append((char) (97 + time % 26));
			}
		}
		return _sb.toString();
	}

	/**
	 * 返回当前是否为2/3/4G移动网络
	 * @param context
	 * @return
	 */
	public static String getNetWorkTypeName(Context context) {
		String typeName = null;
		switch (getNetworkType(context)) {
		case MOBILE_2G:
			typeName = "2G移动网络";
			break;
		case MOBILE_3G:
			typeName = "3G移动网络";
			break;
		case MOBILE_4G:
			typeName = "4G移动网络";
			break;

		default:
			break;
		}
		return typeName;
	}

	/**
	 * 判断网络类型
	 */
	public static int getNetworkType(Context context) {
		int strNetworkType = 0;
		NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				strNetworkType = NetworkUtils.WIFI;
			} else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				// String strSubTypeName = networkInfo.getSubtypeName();
				int networkType = networkInfo.getSubtype();
				try {
					Class<?> threadClazz = Class.forName("android.telephony.TelephonyManager");
					Method method = threadClazz.getMethod("getNetworkClass", int.class);
					int invoke = (Integer) method.invoke(null, networkType);
					switch (invoke) {
					case 1:
						strNetworkType = NetworkUtils.MOBILE_2G;
						break;
					case 2:
						strNetworkType = NetworkUtils.MOBILE_3G;
						break;
					case 3:
						strNetworkType = NetworkUtils.MOBILE_4G;
						break;
					default:
						strNetworkType = NetworkUtils.NETWORK_UNKNOWN;
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} else {
			strNetworkType = NetworkUtils.NoNetConnection;
		}
		return strNetworkType;
	}

	public static final int NETWORK_UNKNOWN = 0;
	public static final int MOBILE_2G = 1;
	public static final int MOBILE_3G = 2;
	public static final int MOBILE_4G = 3;
	public static final int WIFI = 5;
	public static final int NoNetConnection = 6;
}
