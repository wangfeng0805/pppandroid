package com.ylfcf.ppp.util;

import android.util.Log;
/**
 * ϵͳ��־������
 * @author Mr.liu
 *
 */
public class YLFLogger {
	private static final boolean DEBUG = false;

	/**
	 * ����ʱ��־�����֮�󲻻���ʾ
	 * @param msg
	 */
	public static void d(String msg) {
		if (DEBUG) {
			Log.d("yxb", msg);
		}
	}

	public static void d(String tag, String msg) {
		if (DEBUG) {
			Log.d(tag, msg);
		}
	}

	public static void i(String msg) {
		if (DEBUG) {
			Log.i("yxb", msg);
		}
	}
	
	public static void i(String tag,String msg) {
		if (DEBUG) {
			Log.i(tag, msg);
		}
	}
}
