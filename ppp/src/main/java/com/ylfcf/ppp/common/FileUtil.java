package com.ylfcf.ppp.common;

import android.content.Context;
import android.text.TextUtils;

import com.ylfcf.ppp.util.YLFLogger;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * �����ļ��洢�Ĺ�����
 * @author Mr.liu
 *
 */
public class FileUtil {
	/** SD�������ļ�Ŀ¼ */
	public static final String YLFCF_DIR = "ylfcf";
	public static final String YLFCF_BACKGROUND_DIR = YLFCF_DIR + File.separator + "Background";

	/** �ڲ��ļ�Ŀ¼ */
	public static final String YLFCF_INTERNAL_FILE_DIR = "YLFCFFile";
	public static final String YLFCF_INTERNAL_CACHE_DIR = "YLFCFCache";
	
	
	/**������ļ��� */
	public static final String YLFCF_BANNER_CACHE = "ylfcf_banner_cache";//��ҳbanner
	public static final String YLFCF_YXB_PRODUCT_CACHE = "yxb_product_cache";//��ҳԪ�ű���Ʒ
	public static final String YLFCF_YXB_PRODUCTLOG_CACHE = "yxb_productlog_cache";//��ҳԪ�ű�ÿ��ͳ��
	public static final String YLFCF_SRZX_TOTAL_CACHE = "srzx_total_cache";//˽���������в�Ʒ���б�
	public static final String YLFCF_VIP_TOTAL_CACHE = "vip_total_cache";//vip���в�Ʒ���б�
	public static final String YLFCF_ZXD_TOTAL_CACHE = "zxd_total_cache";//���Ŵ����е��б�
	public static final String YLFCF_ZXD_SUYING_CACHE = "zxd_suying_cache";//���Ŵ���ӯ�б�
	public static final String YLFCF_ZXD_BAOYING_CACHE = "zxd_baoying_cache";//���Ŵ���ӯ�б�
	public static final String YLFCF_ZXD_WENYING_CACHE = "zxd_wenying_cache";//���Ŵ���Ӯ�б�
	public static final String YLFCF_YJH_CACHE = "yjh_cache";//Ԫ�ƻ��б�
	public static final String YLFCF_YGZX_CACHE = "ygzx_total_cache";//Ա��ר����Ʒ��Ԫ��ӯ���б�

	/**
	 * ��ȡ�ļ�Ŀ¼�����ȼ��SD���Ƿ���ڣ��������ȡӦ��Ĭ�ϵ��ļ�Ŀ¼�����߻�ȡ�ڲ��洢�Զ���Ŀ¼
	 * 
	 * @param context
	 * @return eg:/storage/emulated/0/Android/data/��Ŀ����/files
	 */
	public static String getFilePath(Context context) {
		String path = "";
		if (SDCardUtil.checkSDCard()) {
			File file = context.getExternalFilesDir(null);
//			if(!file.exists()) {
//				file.mkdirs();
//			}
			// File file = Environment.getExternalStorageDirectory();
			// ����ⲿ�ļ�Ŀ¼��ȡ��������ʹ���ڲ��洢�ؼ�
			if (null != file) {
				if(!file.exists()) {
					file.mkdirs();
				}
				path = file.getAbsolutePath();// + File.separator + KANKETV_DIR;
			} else {
				path = context.getDir(YLFCF_INTERNAL_FILE_DIR, Context.MODE_PRIVATE).getAbsolutePath();
			}
		} else {
			path = context.getDir(YLFCF_INTERNAL_FILE_DIR, Context.MODE_PRIVATE).getAbsolutePath();
		}
		return path;
	}

	/**
	 * ��ȡ�ļ�Ŀ¼�����ȼ��SD���Ƿ���ڣ��������ȡӦ��Ĭ�ϵĻ���Ŀ¼�����߻�ȡ�ڲ��洢�Զ���Ŀ¼
	 * 
	 * @param context
	 * @return eg:/storage/emulated/0/Android/data/��Ŀ����/caches
	 */
	public static String getCachePath(Context context) {
		String path = "";
		if (SDCardUtil.checkSDCard()) {
			File file = context.getExternalCacheDir();
			// File file = Environment.getExternalStorageDirectory();
			if (null != file) {// ����ⲿ�ļ�Ŀ¼��ȡ��������ʹ���ڲ��洢�ؼ�
				path = file.getAbsolutePath();// + File.separator
												// +KANKETV_CACHE_DIR;
			} else {
				path = context.getDir(YLFCF_INTERNAL_CACHE_DIR, Context.MODE_PRIVATE).getAbsolutePath();
			}
		} else {
			path = context.getDir(YLFCF_INTERNAL_CACHE_DIR, Context.MODE_PRIVATE).getAbsolutePath();
		}
		return path;
	}

	/**
	 * ��������,����λ��Ϊ��ǰӦ��˽�еĴ洢�ռ�,�ļ�Ŀ¼��data/data/***����/files/
	 * 
	 * @param filename
	 *            �ļ�����
	 * @param content
	 *            ����
	 */
	public static void save(Context context, String filename, String content) throws IOException {
		FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
		fos.write(content.getBytes());
		fos.close();
		YLFLogger.d("saveFile " + filename);
	}

	/**
	 * ��������,����λ��Ϊ��ǰӦ��˽�еĴ洢�ռ�,�ļ�Ŀ¼��data/data/***����/files/
	 * 
	 * @param filename
	 *            �ļ�����
	 */
	public static void saveObject(Context context, String filename, Object ob) throws IOException {
		YLFLogger.d("saveFile " + filename);
		ObjectOutputStream oos = null;
		if (context == null) {
			new NullPointerException("Context is not null.");
		}
		try {
			oos = new ObjectOutputStream(context.openFileOutput(filename, Context.MODE_PRIVATE));
			oos.writeObject(ob);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * �������������,����λ��Ϊ��ǰӦ��˽�еĴ洢�ռ�,�ļ�Ŀ¼��data/data/***����/files/
	 * 
	 * @param filename
	 *            �ļ�����
	 * @param content
	 *            ����
	 */
	public static void save(Context context, String filename, InputStream content) throws IOException {
		FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
		BufferedOutputStream buf = new BufferedOutputStream(fos);

		byte[] buffer = new byte[1024];
		int length;
		try {
			while ((length = content.read(buffer)) != -1) {
				buf.write(buffer, 0, length);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		buf.flush();
		buf.close();
		fos.close();
		YLFLogger.d("saveFile " + filename);
	}

	/**
	 * �����ļ�������ȡ���ļ����ݵ��ֽ�����,�ļ�Ŀ¼��data/data/***����/files/
	 * 
	 * @param filename
	 *            ��Ч���ļ���
	 * @return �ļ����ݵ��ֽ�����
	 * @throws Exception
	 */
	public static InputStream readStram(Context context, String filename) {
		InputStream is = null;
		FileInputStream fis = null;
		if (context == null) {
			new NullPointerException("Context is not null.");
		}
		if (TextUtils.isEmpty(filename)) {
			new NullPointerException("FileName is not null.");
		}
		try {
			fis = context.openFileInput(filename);
			is = getInputStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return is;
	}

	/**
	 * �����ļ�������ȡ���ļ����ݵ��ֽ�����,�ļ�Ŀ¼��data/data/***����/files/
	 * 
	 * @param filename
	 *            ��Ч���ļ���
	 * @return �ļ����ݵ��ֽ�����
	 * @throws Exception
	 */
	public static byte[] readByte(Context context, String filename) {
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		if (context == null) {
			new NullPointerException("Context is not null.");
		}
		if (TextUtils.isEmpty(filename)) {
			new NullPointerException("FileName is not null.");
		}
		try {
			fis = context.openFileInput(filename);
			bos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length;
			while ((length = fis.read(buffer)) != -1) {
				bos.write(buffer, 0, length);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bos.toByteArray();
	}

	/**
	 * �����ļ�������ȡ��������,�ļ�Ŀ¼��data/data/***����/files/
	 * 
	 * @param filename
	 *            ��Ч���ļ���
	 * @return �ļ����ݵ��ֽ�����
	 * @throws Exception
	 */
	public static Object readObject(Context context, String filename) {
		YLFLogger.d("saveFile " + filename);
		ObjectInputStream ois = null;
		if (context == null) {
			new NullPointerException("Context is not null.");
		}
		try {
			ois = new ObjectInputStream(context.openFileInput(filename));
			return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * FileInputStram to inputStram
	 * 
	 * @param fileInput
	 * @return
	 */
	public static InputStream getInputStream(FileInputStream fileInput) {
		ByteArrayOutputStream baos = null;
		byte[] buffer = new byte[1024 * 4];
		int n = -1;
		InputStream inputStream = null;
		try {
			baos = new ByteArrayOutputStream();
			while ((n = fileInput.read(buffer)) != -1) {
				baos.write(buffer, 0, n);
			}
			byte[] byteArray = baos.toByteArray();
			inputStream = new ByteArrayInputStream(byteArray);
			return inputStream;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * �ļ��Ƿ����
	 * 
	 * @param path
	 *            �ļ�Ŀ¼
	 * @return
	 */
	public static boolean isFileExists(String path) {
		if (TextUtils.isEmpty(path)) {
			return false;
		}
		return new File(path).exists();
	}
}
