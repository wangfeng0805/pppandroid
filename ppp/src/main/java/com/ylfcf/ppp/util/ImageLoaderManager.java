package com.ylfcf.ppp.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.ylfcf.ppp.common.FileUtil;

import java.io.File;
/**
 * ͼƬ������
 * @author Mr.liu
 *
 */
public class ImageLoaderManager {
	/**
	 * ͼƬ���ط�ʽ�İ汾�������ּ��ط�ʽ��trueΪgoogle�ϵİ汾��falseΪ�µİ汾
	 */
	public static boolean OLD_VERSION = false;

	public static ImageLoaderConfiguration config;

	/**
	 * ImageLoaderʵ��
	 * 
	 * @return
	 */
	public static ImageLoader newInstance() {
		return ImageLoader.getInstance();
	}

	/**
	 * �ڳ����Application�е��� ����ImageLoader
	 * 
	 * @param context
	 */
//	public static void configurationImageLoader(Context context) {
//		config = new ImageLoaderConfiguration.Builder(context)
//				.threadPriority(Thread.NORM_PRIORITY - 2)// �߳����ȼ�
//				.denyCacheImageMultipleSizesInMemory()//�Զ�����
//				.diskCacheFileNameGenerator(new Md5FileNameGenerator())// �������ʱ���URI������MD5
//				//.memoryCacheSize(4 * 1024 *1024) //���û����С
//				//.memoryCache(new LruMemoryCache(4 * 1024 *1024))
//				.diskCacheSize(50 * 1024 * 1024) // 50 Mb
//				.tasksProcessingOrder(QueueProcessingType.LIFO)
//				//.writeDebugLogs()// Remove for release app
//				.diskCache(new UnlimitedDiscCache(new File(FileUtil.getCachePath(context))))// �Զ��建��·��
//				.build();
//		ImageLoader.getInstance().init(config);
//	}
	
	public static void configurationImageLoader(Context context) {
		config = new ImageLoaderConfiguration.Builder(context)
				.threadPoolSize(2)  
				.threadPriority(Thread.NORM_PRIORITY)// �߳����ȼ�
				.denyCacheImageMultipleSizesInMemory()//�Զ�����
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())// �������ʱ���URI������MD5
//				.memoryCacheSize(4 * 1024 *1024) //���û����С
//				.memoryCache(new LruMemoryCache(4 * 1024 *1024))
				.diskCacheSize(50 * 1024 * 1024) // 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				//.writeDebugLogs()// Remove for release app
				.diskCache(new UnlimitedDiscCache(new File(FileUtil.getCachePath(context))))// �Զ��建��·��
				.build();
		ImageLoader.getInstance().init(config);
	}

	/**
	 * ����ڴ滺��
	 */
	public static void clearMemoryCache() {
		newInstance().clearMemoryCache();
	}

	/**
	 * ������ػ���
	 */
	public static void clearDiskCache() {
		newInstance().clearDiskCache();
	}

	/**
	 * option����
	 * 
	 * @param loadingImage
	 * @param errorIamge
	 */
	public static DisplayImageOptions configurationOption(int loadingImage, int errorIamge) {
		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(loadingImage)
				.showImageForEmptyUri(errorIamge)
				.showImageOnFail(errorIamge)
				//.delayBeforeLoading(1000)//�ӳ�1000����
				.cacheInMemory(false) //�������ڴ�
				.cacheOnDisk(true) //������SD��
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		return options;
	}

	/**
	 * option����
	 * 
	 * @param loadingImage
	 * @param errorIamge
	 * @param cornerRadiusPixels Բ�ǻ���
	 */
	public static DisplayImageOptions configurationOption(int loadingImage, int errorIamge ,int cornerRadiusPixels) {
		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(loadingImage)
				.showImageForEmptyUri(errorIamge)
				.showImageOnFail(errorIamge)
				//.delayBeforeLoading(1000)//�ӳ�1000����
				.displayer(new RoundedBitmapDisplayer(cornerRadiusPixels))
				.cacheInMemory(true) //�������ڴ�
				.cacheOnDisk(true) //������SD��
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		return options;
	}
	
	/**
	 * ͼƬ����
	 * 
	 * @param imageLoader
	 * @param imageUrl
	 * @param imageView
	 * @param options
	 * @param loadingListener
	 * @param progressListener
	 */
	public static void loadingImage(ImageLoader imageLoader, String imageUrl, ImageView imageView,
			DisplayImageOptions options, SimpleImageLoadingListener loadingListener,
			ImageLoadingProgressListener progressListener) {
		imageLoader.displayImage(imageUrl, imageView, options, loadingListener, progressListener);
	}
}
