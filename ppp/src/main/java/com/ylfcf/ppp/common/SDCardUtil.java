package com.ylfcf.ppp.common;

import android.os.Environment;

/**
 * sdk�Ĺ�����
 * 
 * @author Administrator
 * 
 */
public class SDCardUtil {
	/**
	 * ���sd���Ƿ����
	 * 
	 * @return
	 */
	public static boolean checkSDCard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ��ȡsd��·��
	 * 
	 * @return
	 */
	public static String getRootPath() {
		if (checkSDCard()) {
			return Environment.getExternalStorageDirectory().getPath();
		}
		return "";
	}
}
