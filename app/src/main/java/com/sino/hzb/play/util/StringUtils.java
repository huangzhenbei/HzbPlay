package com.sino.hzb.play.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class StringUtils {

	private static final String CHAR_SET_UTF8 = "UTF-8";

	/**
	 * true=空,false=不空
	 * @param input
	 * @return
	 */
	public static boolean isEmpty(String input) {
		if ((input == null) || ("".equals(input))) {
			return true;
		}
		for (int i = 0; i < input.length(); ++i) {
			char c = input.charAt(i);
			if ((c != ' ') && (c != '\t') && (c != '\r') && (c != '\n')) {
				return false;
			}
		}
		return true;
	}

	public static boolean empty(Object o) {
		return ((o != null) && (!("".equals(o.toString().trim()))) && (!("null".equalsIgnoreCase(o.toString().trim()))) && (!("undefined".equalsIgnoreCase(o.toString().trim()))) && (!("请选择..."
				.equals(o.toString().trim()))));
	}

	public static boolean notEmpty(Object o) {
		return ((o == null) || ("".equals(o.toString().trim())) || ("null".equalsIgnoreCase(o.toString().trim())) || ("undefined".equalsIgnoreCase(o.toString().trim())) || ("请选择...".equals(o
				.toString().trim())));
	}

	public static boolean isConnected(Context context) {
		ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conn.getActiveNetworkInfo();
		return (info != null && info.isConnected());
	}

	/**
	 * 将大写转换为小写
	 * 
	 * @param src
	 * @return
	 */
	public static String exChange(String src) {
		char[] array = src.toCharArray();
		int temp = 0;
		for (int i = 0; i < array.length; i++) {
			temp = (int) array[i];
			if (temp <= 90 && temp >= 65) { // array[i]为大写字母
				array[i] = (char) (temp + 32);
			} else if (temp <= 122 && temp >= 97) { // array[i]为小写字母
				array[i] = (char) (temp - 32);
			}
		}
		return String.valueOf(array);
	}

	/**
	 * <i> 去除字符串首尾的空格, 包括全角空格和半角空格 </i>
	 * 
	 * @param source
	 *            指定的字符串
	 * @return 去除首尾空格后的字符串
	 */
	public static String trim(String source) {
		if (isNull(source)) {
			return "";
		}

		int length = source.length();
		boolean hasStart = true, hasEnd = true;

		for (int i = 1; i <= length; i++) {
			if (hasStart && source.length() > 0) {
				char start = source.charAt(0);
				if (start == ' ' || start == '　') {
					source = source.substring(1, source.length());
				} else {
					hasStart = false;
				}
			}

			if (hasEnd && source.length() > 1) {
				char end = source.charAt(source.length() - 1);
				if (end == ' ' || end == '　') {
					source = source.substring(0, source.length() - 1);
				} else {
					hasEnd = false;
				}
			}

			if (!hasStart && !hasEnd) {
				break;
			}
		}
		return (source);
	}

	/**
	 * <i> 判断输入字符串是否为空 </i>
	 * 
	 * @param inputStr
	 *            指定的字符串
	 * 
	 * @return 如果为空则返回true 如果不为空则返回false
	 */
	public static boolean isNull(Object inputStr) {
		if (null == inputStr || "".equals(inputStr) || "null".equals(inputStr.toString().toLowerCase()))
			return true;
		return false;
	}

	/**
	 * 判断传入电话号码 是否合法
	 * 
	 * @param phoneNumber
	 *            指定的字符串
	 * 
	 */
	public static boolean isPhoneNumberValid(String phoneNumber) {
		boolean isValid = false;
		if (!isNumberic(phoneNumber))
			return isValid;
		if (phoneNumber.length() == 11 && phoneNumber.startsWith("1"))
			isValid = true;
		return isValid;
	}

	/**
	 * 判断是否为数字
	 * 
	 */
	public static boolean isNumberic(String str) {
		if (StringUtils.isNull(str))
			return false;
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isDigit(str.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 格式化日期格式
	 * 
	 * @param format
	 *            显示的日期格式
	 * @param date
	 *            待显示的日期
	 * 
	 */
	public static String format(String format, Date date) {
		return new SimpleDateFormat(format).format(date);
	}

	/**
	 * 字符串转换到时间格式
	 * 
	 * @param dateStr
	 *            需要转换的字符串
	 * @param formatStr
	 *            需要格式的目标字符串 举例 yyyy-MM-dd
	 * @return Date 返回转换后的时间
	 * @throws ParseException
	 *             转换异常
	 */
	public static Date stringToDate(String formatStr, String dateStr) {
		DateFormat sdf = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
		return date;
	}

	/**
	 * 获取日期差，返回相差天数。
	 * 
	 * @param startDate
	 * @param endDate
	 */
	public static long getCompareDate(String startDate, String endDate) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = formatter.parse(startDate);
		Date date2 = formatter.parse(endDate);
		long l = date2.getTime() - date1.getTime();
		long d = l / (24 * 60 * 60 * 1000);
		return d;
	}

	/**
	 * 获取日期差，返回相差天数。
	 * 
	 * @param startDate
	 * @param endDate
	 */
	public static long getCompareDate(Date startDate, Date endDate) {
		long l = endDate.getTime() - startDate.getTime();
		long d = l / (24 * 60 * 60 * 1000);
		return d;
	}

	// 通过上面的方法获得两个日期的天数差，然后用计算秒不就简单了？

	/**
	 * 将服务器响应内容进行UTF-8转码处理，并将转码后的字符串进行格式化处理，以符合JSON数据格式。
	 * 
	 * （格式化处理：删除{}前后可能存在的任意字符，删除服务器端响应时多余的 \ 符号）
	 * 
	 * @param responseContent
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String formatJSON(byte[] responseContent) throws UnsupportedEncodingException {
		String str = new String(responseContent, CHAR_SET_UTF8);
		str = formatJSON(str);
		return str;
	}

	/**
	 * 将指定的字符串进行格式化处理，以符合JSON数据格式。
	 * 
	 * （格式化处理：删除{}前后可能存在的任意字符，删除服务器端响应时多余的 \ 符号）
	 * 
	 * @param json
	 * @return
	 */
	public static String formatJSON(String json) {

		int idxLeft = json.indexOf("{");
		if (idxLeft > 0) {
			json = json.substring(idxLeft);
		}

		int idxRight = json.lastIndexOf("}");
		if (idxRight > 0) {
			json = json.substring(0, idxRight + 1);
		}

		json = json.replace("\\\"", "\"");

		return json;
	}

	/**
	 * 判断手机格式是否正确
	 * 
	 * @param mobiles
	 *            字符串
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^(13[0-9]|15[0-9]|17[678]|18[0-9]|14[57])[0-9]{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 判断email格式是否正确
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}

	/**
	 * 格式化价格（例如：59,300）
	 */
	public static String formatPrice(int price) {
		String str = String.valueOf(price);
		if (price > 999999) {
			str = str.substring(0, 4) + "," + str.substring(str.length() - 3, str.length());
		} else if (price > 99999) {
			str = str.substring(0, 3) + "," + str.substring(str.length() - 3, str.length());
		} else if (price > 9999) {
			str = str.substring(0, 2) + "," + str.substring(str.length() - 3, str.length());
		} else if (price > 999) {
			str = str.substring(0, 1) + "," + str.substring(str.length() - 3, str.length());
		}
		return str;
	}

	public static String formatSSToTime(long ss){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String date = sdf.format(ss);
		return StringUtils.isNull(date)?null:date;
	}
	
	/**
	 * 格式化数字
	 * 
	 * @param d
	 *            需要格式的数字
	 * @param scale
	 *            保留小数位
	 * @param roundingMode
	 *            四舍五入的格式例如：BigDecimal.ROUND_DOWN(只舍不入)
	 * @return
	 */
	public static String decimalFormat(double d, int scale, int roundingMode) {
		BigDecimal big = new BigDecimal(d);
		big = big.setScale(scale, roundingMode);
		return big.toString();
	}
	
	
	/**
	 * @Description: 校验密码
	 * @return
	 */
	public static String validPassword(String pwd) {
		String message = "";
		if (pwd.contains(" ")) {
			message = "密码中不能含有空格！";
			return message;
		}
		if (isEmpty(pwd)) {
			message = "请输入密码！";
		} else if (pwd.length() < 6) {
			message = "请输入6-32位密码！";
		} else if (pwd.length() > 20) {
			message = "请输入6-32位密码！";
		}
		return message;
	}
	
}
