package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncBanner;
import com.ylfcf.ppp.common.FileUtil;
import com.ylfcf.ppp.db.DBGesturePwdManager;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.GesturePwdEntity;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseBanner;
import com.ylfcf.ppp.util.ImageLoaderManager;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.YLFLogger;

/**
 * ����ҳ��
 * 
 * @author Administrator
 * 
 */
public class SplashActivity extends BaseActivity {
	private static final String className = "SplashActivity";
	private static final int GOTO_MAINACTIVITY = 10;
	private static final int REQUEST_BANNER = 20;
	private ImageView splashImage;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private Animation mAnimation = null;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case GOTO_MAINACTIVITY:
				gotoMainActivity();
				break;
			case REQUEST_BANNER:
				requestBanner("����", "");
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_activity);

		mAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_enlarge);
		mAnimation.setFillAfter(true);
		imageLoader = ImageLoaderManager.newInstance();
		options = ImageLoaderManager.configurationOption(
				R.drawable.splash_default, R.drawable.splash_default);
		findViews();
		handler.sendEmptyMessageDelayed(REQUEST_BANNER, 1500L);
	}

	@Override
	protected void onResume() {
		super.onResume();
		UMengStatistics.statisticsOnPageStart(className);//����ͳ��ҳ����ת
		UMengStatistics.statisticsResume(this);//����ͳ��ʱ��
	}

	@Override
	protected void onPause() {
		super.onPause();
		UMengStatistics.statisticsOnPageEnd(className);//����ͳ��ҳ����ת
		UMengStatistics.statisticsPause(this);//����ͳ��ʱ��
	}

	private void findViews() {
		// ����1 Android�����Ļ�Ŀ�͸�
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int screenWidth = display.getWidth();
		int screenHeight = display.getHeight();
		DisplayMetrics metric = getResources().getDisplayMetrics();
		YLFLogger.d("��Ļ�ֱ��ʣ�\n" + "��" + screenWidth + "\n" + "�ߣ�"
				+ screenHeight + "\n" + "��Ļ�ܶȣ�" + metric.density);
		splashImage = (ImageView) findViewById(R.id.splash_activity_image);
	}

	/**
	 * ����ImageLoder
	 */
	@SuppressWarnings("deprecation")
	private void configImageLoader() {
		// ��ʼ��ImageLoader
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.splash_default) // ����ͼƬ�����ڼ���ʾ��ͼƬ
				.showImageForEmptyUri(R.drawable.splash_default) // ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
				.showImageOnFail(R.drawable.splash_default) // ����ͼƬ���ػ��������з���������ʾ��ͼƬ
				.cacheInMemory(false) // �������ص�ͼƬ�Ƿ񻺴����ڴ���
				.cacheOnDisk(true) // �������ص�ͼƬ�Ƿ񻺴���SD����
				.bitmapConfig(Bitmap.Config.ARGB_8888) // default
				// .displayer(new RoundedBitmapDisplayer(20)) // ���ó�Բ��ͼƬ
				.build(); // �������ù���DisplayImageOption����

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).defaultDisplayImageOptions(options)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())// ʹ��md5��URL���м�������
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader.getInstance().init(config);
	}

	private void gotoMainActivity() {
		Intent intent = new Intent();
		String userId = SettingsManager.getUserId(getApplicationContext());
		GesturePwdEntity entity = DBGesturePwdManager.getInstance(
				getApplicationContext()).getGesturePwdEntity(userId);
		String gesturePwd = null;
		if (entity != null) {
			gesturePwd = entity.getPwd();
		}

		if ("".equals(SettingsManager.getAppFirst(SplashActivity.this))) {
			// APP��һ�����У���������ҳ��
			SettingsManager.setAppFirst(SplashActivity.this, "true");
			intent.setClass(SplashActivity.this, IntroductionActivity.class);
		} else {
			if (gesturePwd != null && !"".equals(gesturePwd)) {
				// ����������֤
				intent.setClass(SplashActivity.this,
						GestureVerifyActivity.class);
			} else {
				// �û�������
				intent.setClass(SplashActivity.this, MainFragmentActivity.class);
			}
		}
		startActivity(intent);
		if(bannerTask != null){
			bannerTask.cancel(true);
		}
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ImageLoaderManager.clearMemoryCache();
		handler.removeCallbacksAndMessages(null);
	}

	/**
	 * banner
	 * 
	 * @param status
	 */
	AsyncBanner bannerTask = null;
	private void requestBanner(String status, String type) {
		String result = null;
		BaseInfo baseInfo = null;
		try {
			byte[] initJsonB = FileUtil.readByte(SplashActivity.this,
					FileUtil.YLFCF_BANNER_CACHE);
			result = new String(initJsonB);
			// ����init.json
			if (result != null && !"".equals(result)) {
				baseInfo = JsonParseBanner.parseData(result);
			}
		} catch (Exception exx) {
		}
		
		if (baseInfo != null && baseInfo.getmBannerPageInfo() != null 
				&& baseInfo.getmBannerPageInfo().getBannerList() != null) {
			int size = baseInfo.getmBannerPageInfo().getBannerList().size();
			for (int i = 0; i < size; i++) {
				BannerInfo info = baseInfo.getmBannerPageInfo().getBannerList().get(i);
				if ("�ֻ�����ҳ".equals(info.getType())) {
					ImageLoaderManager.loadingImage(imageLoader, info.getPic_url(),
									splashImage, options, null, null);// ���غ�̨ͼƬ
					// splashImage.startAnimation(mAnimation);//�Ŵ󶯻�
				} else {
					splashImage.setBackgroundResource(R.drawable.splash_default);
				}
			}
		}
		bannerTask = new AsyncBanner(SplashActivity.this,
				String.valueOf(0), String.valueOf(20), status, type,
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						handler.sendEmptyMessageDelayed(GOTO_MAINACTIVITY, 2000L);
					}
				});
		bannerTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

}
