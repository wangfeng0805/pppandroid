package com.ylfcf.ppp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ant.liao.GifView;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncArticleList;
import com.ylfcf.ppp.async.AsyncBanner;
import com.ylfcf.ppp.common.FileUtil;
import com.ylfcf.ppp.entity.ArticleInfo;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.YXBProductInfo;
import com.ylfcf.ppp.entity.YXBProductLogInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseBanner;
import com.ylfcf.ppp.ui.ActivitysRegionActivity;
import com.ylfcf.ppp.ui.ArticleListActivity;
import com.ylfcf.ppp.ui.BannerDetailsActivity;
import com.ylfcf.ppp.ui.BannerTopicActivity;
import com.ylfcf.ppp.ui.BorrowDetailXSBActivity;
import com.ylfcf.ppp.ui.BorrowDetailYYYActivity;
import com.ylfcf.ppp.ui.BorrowListVIPActivity;
import com.ylfcf.ppp.ui.BorrowListZXDActivity;
import com.ylfcf.ppp.ui.InvitateActivity;
import com.ylfcf.ppp.ui.LXFXTempActivity;
import com.ylfcf.ppp.ui.LXJ5TempActivity;
import com.ylfcf.ppp.ui.MainFragmentActivity;
import com.ylfcf.ppp.ui.MainFragmentActivity.OnFirstPageZXDOnClickListener;
import com.ylfcf.ppp.ui.MainFragmentActivity.OnNetStatusChangeListener;
import com.ylfcf.ppp.ui.MainFragmentActivity.OnRequestBorrowListListener;
import com.ylfcf.ppp.ui.MainFragmentActivity.OnYXBDataListener;
import com.ylfcf.ppp.ui.PrizeRegion2TempActivity;
import com.ylfcf.ppp.ui.PrizeRegionTempActivity;
import com.ylfcf.ppp.ui.SRZXAppointActivity;
import com.ylfcf.ppp.ui.SignTopicTempActivity;
import com.ylfcf.ppp.ui.UserVerifyActivity;
import com.ylfcf.ppp.ui.YQHYTempActivity;
import com.ylfcf.ppp.util.Constants.ActivityCode;
import com.ylfcf.ppp.util.Constants.ArticleType;
import com.ylfcf.ppp.util.GlideImageLoader;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.YLFLogger;
import com.ylfcf.ppp.widget.LoadingDialog;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ��ҳ
 *
 * @author Administrator
 *
 */
public class FirstPageFragment extends BaseFragment implements OnClickListener,OnBannerListener {
	private static final String className = "FirstPageFragment";

	private static final int REQUEST_ARTICLELIST_WHAT = 5701;// ����
	private static final int REQUEST_ARTICLELIST_SUCCESS = 5702;
	private static final int REFRESH_NOTICE = 5703;

	private MainFragmentActivity mainActivity;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private List<ImageView> views = new ArrayList<ImageView>();
	private List<BannerInfo> bannerList = new ArrayList<BannerInfo>();
	private ViewPager subjectViewPager;// ϵ�б��viewpager
	private Button hytjBtn,hdzqBtn;//�����Ƽ� �ר����ť
	private Banner mBanner;
	private LoadingDialog mLoadingDialog;

	/*
	 * ���ֱ�
	 */
	private View xsbLayout;
	private ImageView xsbImg,yyyImg,yzyImg;
	private LinearLayout bottomLayout;
	private GifView mGifView;

	private LinearLayout noticeLayout;// ����Ĳ���
	private TextView noticeTitle, noticeTime;
	private TextView tipsText;
	private List<View> viewsList = new ArrayList<View>();

	private ImageView defaultImg;
	private View rootView;

	private int page = 0;
	private int pageSize = 20;
	private boolean isFirst = true;// �Ƿ��״ν�����ҳ��

	private List<ArticleInfo> articleList;
	private ProductInfo xsbInfo;
	private FragmentManager fragmentManager = null;

	private Handler hanlder = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case REQUEST_ARTICLELIST_WHAT:
					requestNoticeList("����", ArticleType.NOTICE);
					break;
				case REQUEST_ARTICLELIST_SUCCESS:
					BaseInfo baseInfo = (BaseInfo) msg.obj;
					if (baseInfo != null) {
						articleList = baseInfo.getmArticlePageInfo()
								.getArticleList();
						initNoticeData();
					}
					break;
				case REFRESH_NOTICE:
					initNoticeData();
					break;
				default:
					break;
			}
		}
	};

	/**
	 * ������ǰFragment��ʵ������
	 *
	 * @param position
	 * @return
	 */
	private static OnFirstPageZXDOnClickListener firstPageZXDListener;
	private static MainFragmentActivity.OnFirstPageHYTJOnClickListener hytjOnClickListener;
	public static Fragment newInstance(int position, OnFirstPageZXDOnClickListener listener, MainFragmentActivity.OnFirstPageHYTJOnClickListener hytjListener) {
		FirstPageFragment f = new FirstPageFragment();
		Bundle args = new Bundle();
		args.putInt("num", position);
		f.setArguments(args);
		firstPageZXDListener = listener;
		hytjOnClickListener = hytjListener;
		return f;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
							 @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mainActivity = (MainFragmentActivity) getActivity();
		fragmentManager = getActivity().getSupportFragmentManager();
		mLoadingDialog = new LoadingDialog(mainActivity,"���ڼ���...",R.anim.loading);
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.first_page_fragment, null);
			findViews(rootView, inflater);
		}
		// �����rootView��Ҫ�ж��Ƿ��Ѿ����ӹ�parent��
		// �����parent��Ҫ��parentɾ����Ҫ��Ȼ�ᷢ�����rootview�Ѿ���parent�Ĵ���
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		requestBanner("����", "");
		hanlder.sendEmptyMessage(REQUEST_ARTICLELIST_WHAT);
		return rootView;
	}

	private void findViews(View view, LayoutInflater inflater) {
		xsbLayout = inflater.inflate(R.layout.first_page_subject_xsb, null);
		viewsList.add(xsbLayout);

		xsbImg = (ImageView)xsbLayout.findViewById(R.id.first_page_subject_xsb_logo);
		xsbImg.setOnClickListener(this);
		yyyImg = (ImageView)xsbLayout.findViewById(R.id.first_page_subject_yyy_logo);
		yyyImg.setOnClickListener(this);
		yzyImg = (ImageView)xsbLayout.findViewById(R.id.first_page_subject_yzy_logo);
		yzyImg.setOnClickListener(this);
		mGifView = (GifView) xsbLayout.findViewById(R.id.first_page_subject_xsb_gif);
		mGifView.setGifImage(R.drawable.first_page_subject_gif);
		mGifView.setShowDimension(mainActivity.getResources().getDimensionPixelSize(R.dimen.common_measure_63dp),
				mainActivity.getResources().getDimensionPixelSize(R.dimen.common_measure_54dp));
		mGifView.setOnClickListener(this);
		bottomLayout = (LinearLayout) xsbLayout.findViewById(R.id.first_page_subject_bottom_layout);
		bottomLayout.setOnClickListener(this);
		tipsText = (TextView) xsbLayout
				.findViewById(R.id.first_page_subject_xsb_text1);
		SpannableStringBuilder builder = new SpannableStringBuilder(tipsText.getText().toString());
		//ForegroundColorSpan Ϊ����ǰ��ɫ��BackgroundColorSpanΪ���ֱ���ɫ
		ForegroundColorSpan orangeSpan = new ForegroundColorSpan(getResources().getColor(R.color.orange_text));
		builder.setSpan(orangeSpan, 8,12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tipsText.setText(builder);

		hytjBtn = (Button)view.findViewById(R.id.first_page_fragment_hytj_btn);
		hytjBtn.setOnClickListener(this);
		hdzqBtn = (Button)view.findViewById(R.id.first_page_fragment_hdzq_btn);
		hdzqBtn.setOnClickListener(this);

		defaultImg = (ImageView) view
				.findViewById(R.id.first_page_fragment_default_img);
		noticeLayout = (LinearLayout) view
				.findViewById(R.id.first_page_fragment_notice_layout);
		noticeLayout.setOnClickListener(this);
		noticeTitle = (TextView) view
				.findViewById(R.id.first_page_fragment_notice_text);
		noticeTime = (TextView) view
				.findViewById(R.id.first_page_fragment_notice_time);

		subjectViewPager = (ViewPager) view
				.findViewById(R.id.first_page_fragment_viewpager);
		subjectViewPager.setAdapter(new SubjectPagerAdapter(viewsList));
		subjectViewPager.setCurrentItem(0);
		mBanner = (Banner) view.findViewById(R.id.first_page_fragment_banner);
		mBanner.setOnBannerListener(this);
		mainActivity.setOnRequestBorrowListener(
				new OnRequestBorrowListListener() {
					@Override
					public void back(BaseInfo baseInfo) {
					}
				}, null);

		mainActivity.setOnYXBDataListener(new OnYXBDataListener() {
			@Override
			public void back(YXBProductInfo mYXBProductInfo,
							 YXBProductLogInfo mYXBProductLogInfo) {
			}
		}, null);
		mainActivity.setOnNetStatusChangeListener(new OnNetStatusChangeListener() {
			@Override
			public void onNetStatusChange(boolean enabled) {
				if(enabled){
					requestBanner("����", "");
					hanlder.sendEmptyMessage(REQUEST_ARTICLELIST_WHAT);
				}
			}
		},null);
	}

	/**
	 * ��ʼ��������
	 *
	 * @param
	 */
	private void initNoticeData() {
		if(articleList == null || articleList.size() < 1){
			return;
		}
		ArticleInfo info = articleList.get(0);
		noticeTitle.setText(info.getTitle());
		noticeTime.setText(info.getAdd_time().split(" ")[0].replaceAll("-", "/"));
	}

	private void initBanner(List<BannerInfo> bannerList){
		if(bannerList == null || bannerList.size() <= 0)
			return;
		List<String> images = new ArrayList<String>();
		List<String> titles = new ArrayList<String>();
		for(BannerInfo info : bannerList){
			images.add(info.getPic_url());
			titles.add(info.getId());
		}
		//����banner��ʽ
		mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
		//����ͼƬ������
		mBanner.setImageLoader(new GlideImageLoader());
		//����ͼƬ����
		mBanner.setImages(images);
		//����banner����Ч��
		mBanner.setBannerAnimation(Transformer.Accordion);
		//�����Զ��ֲ���Ĭ��Ϊtrue
		mBanner.isAutoPlay(true);
		//�����ֲ�ʱ��
		mBanner.setDelayTime(2500);
		//����ָʾ��λ�ã���bannerģʽ����ָʾ��ʱ��
		mBanner.setIndicatorGravity(BannerConfig.CENTER);
		//banner���÷���ȫ���������ʱ������
		mBanner.start();
	}

	@Override
	public void OnBannerClick(int position) {
		if(bannerList == null || bannerList.size() <= 0)
			return;
		BannerInfo info = bannerList.get(position);
		Intent intent = null;
		if ("����".equals(info.getType())) {
			if("".equals(info.getArticle_id()) || "0".equals(info.getArticle_id())){

			}else{
				intent = new Intent(mainActivity,
						BannerDetailsActivity.class);
				intent.putExtra("BannerInfo", info);
				startActivity(intent);
			}
		} else if ("ר��ҳ".equals(info.getType())) {
			intent = new Intent(mainActivity, BannerTopicActivity.class);
			intent.putExtra("BannerInfo", info);
			if (info.getArticle_id() != null && !"".equals(info.getArticle_id())) {
				startActivity(intent);
			}
		}else if("����ҳ��".equals(info.getType())){
			if(ActivityCode.YYY_DETAILS_ACTIVITY.equals(info.getArticle_id())){
				//Ԫ��ӯ����ҳ��
				intent = new Intent(getActivity(),BorrowDetailYYYActivity.class);
				startActivity(intent);
			}else if(ActivityCode.XSB_DETAILS_ACTIVITY.equals(info.getArticle_id())){
				//���ֱ�����ҳ��
				intent = new Intent(getActivity(),BorrowDetailXSBActivity.class);
				startActivity(intent);
			}else if(ActivityCode.DQLC_LIST_ACTIVITY.equals(info.getArticle_id())){
				//Ԫ��ӯ�б�ҳ��
				intent = new Intent(getActivity(),BorrowListZXDActivity.class);
				startActivity(intent);
			}else if(ActivityCode.VIP_LIST_ACTIVITY.equals(info.getArticle_id())){
				//VIP��Ʒ�б�ҳ��
				intent = new Intent(getActivity(),BorrowListVIPActivity.class);
				startActivity(intent);
			}else if(ActivityCode.SRZX_APPOINT_ACTIVITY.equals(info.getArticle_id())){
				//˽������ԤԼҳ��
				intent = new Intent(getActivity(),SRZXAppointActivity.class);
				startActivity(intent);
			}else if(ActivityCode.FLJH_ACTIVITY.equals(info.getArticle_id())){
				//��Ա�����ƻ�
				intent = new Intent(getActivity(),PrizeRegionTempActivity.class);
				startActivity(intent);
			}else if(ActivityCode.LXFX_ACTIVITY.equals(info.getArticle_id())){
				//������ �����
				intent = new Intent(getActivity(),LXFXTempActivity.class);
				startActivity(intent);
			}else if(ActivityCode.SIGN_ACTIVITY.equals(info.getArticle_id())){
				intent = new Intent(getActivity(),SignTopicTempActivity.class);
				startActivity(intent);
			}else if(ActivityCode.FLJH_ACTIVITY_02.equals(info.getArticle_id())){
				intent = new Intent(getActivity(),PrizeRegion2TempActivity.class);
				startActivity(intent);
			}else if(ActivityCode.YQHY_ACTIVITY.equals(info.getArticle_id())){
				intent = new Intent(getActivity(),YQHYTempActivity.class);
				startActivity(intent);
			}else if(ActivityCode.QXJ5_ACTIVITY.equals(info.getArticle_id())){
				intent = new Intent(getActivity(),LXJ5TempActivity.class);
				startActivity(intent);
			}
		}
	}

	private void checkNov2017Active(String sysTime){
		int flag = SettingsManager.checkActiveStatusBySysTime(sysTime,SettingsManager.activeNov2017_StartTime,SettingsManager.activeNov2017_EndTime);
		if(flag == 0){
			//�������
			mGifView.setVisibility(View.VISIBLE);
		}else{
			mGifView.setVisibility(View.GONE);
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		YLFLogger.d("FirstPageFragment -- setUserVisibleHint ---"
				+ isVisibleToUser);
	}

	@Override
	public void onStart() {
		super.onStart();
		//��ʼ�ֲ�
		if(mBanner != null)
			mBanner.startAutoPlay();
	}

	@Override
	public void onStop() {
		super.onStop();
		//�����ֲ�
		if(mBanner != null)
			mBanner.stopAutoPlay();
	}

	@Override
	public void onResume() {
		super.onResume();
		UMengStatistics.statisticsOnPageStart(className);
		YLFLogger.d("FirstPageFragment -- onResume() ---");
	}

	@Override
	public void onPause() {
		super.onPause();
		UMengStatistics.statisticsOnPageEnd(className);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		hanlder.removeCallbacksAndMessages(null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.first_page_fragment_notice_layout:
				Intent intentArt = new Intent(mainActivity,
						ArticleListActivity.class);
				startActivity(intentArt);
				break;
			case R.id.first_page_subject_xsb_logo:
				//���ֱ�
				Intent intentXSB = new Intent(mainActivity,
						BorrowDetailXSBActivity.class);
				intentXSB.putExtra("PRODUCT_INFO", xsbInfo);
				startActivity(intentXSB);
				break;
			case R.id.first_page_subject_yyy_logo:
				//Ԫ��ӯ
				Intent intentYYY = new Intent(mainActivity,
						BorrowDetailYYYActivity.class);
				startActivity(intentYYY);
				break;
			case R.id.first_page_subject_yzy_logo:
			case R.id.first_page_subject_xsb_gif:
				//Ԫ��ӯ
				Intent intentYZY = new Intent(mainActivity,
						BorrowListZXDActivity.class);
				startActivity(intentYZY);
				break;
			case R.id.first_page_subject_bottom_layout:
				if(firstPageZXDListener != null)
					firstPageZXDListener.back();
				break;
			case R.id.first_page_fragment_hytj_btn:
				//�����Ƽ�
				shared();
				break;
			case R.id.first_page_fragment_hdzq_btn:
				//�ר��
				Intent intentHDZQ = new Intent(mainActivity, ActivitysRegionActivity.class);
				mainActivity.startActivity(intentHDZQ);
				break;
			default:
				break;
		}
	}

	private void shared(){
		String userId = SettingsManager.getUserId(mainActivity.getApplicationContext());
		if(userId != null && !"".equals(userId)){
			//�ѵ�¼
			hytjBtn.setEnabled(false);
			checkIsVerify("�����н�");
		}else{
			//δ��¼
			hytjOnClickListener.hytjOnClick();
		}
	}

	/**
	 * ��֤�û��Ƿ��Ѿ���֤
	 * @param type ����ֵ��,�����֡�,"�����н�"
	 */
	private void checkIsVerify(final String type){
		if(mLoadingDialog != null){
			mLoadingDialog.show();
		}
		RequestApis.requestIsVerify(mainActivity, SettingsManager.getUserId(mainActivity.getApplicationContext()), new Inter.OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(mLoadingDialog != null && mLoadingDialog.isShowing()){
					mLoadingDialog.dismiss();
				}
				if("�����н�".equals(type)){
					hytjBtn.setEnabled(true);
					Intent intent = new Intent();
					intent.setClass(mainActivity, InvitateActivity.class);
					intent.putExtra("is_verify", flag);
					startActivity(intent);
					return;
				}
				if(flag){
					//�û��Ѿ�ʵ��
//					checkIsBindCard(type);
				}else{
					//�û�û��ʵ��
					Intent intent = new Intent(mainActivity,UserVerifyActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("type", type);
					intent.putExtra("bundle", bundle);
					startActivity(intent);
				}
			}

			@Override
			public void isSetWithdrawPwd(boolean flag, Object object) {
			}
		});
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	/**
	 * Viewpager��������
	 * @author Mr.liu
	 */
	class SubjectPagerAdapter extends PagerAdapter {
		private List<View> mListViews;

		public SubjectPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;// ���췽�������������ǵ�ҳ���������ȽϷ��㡣
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));// ɾ��ҳ��
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) { // �����������ʵ����ҳ��
			container.addView(mListViews.get(position), 0);// ���ҳ��
			return mListViews.get(position);
		}

		@Override
		public int getCount() {
			return mListViews.size();// ����ҳ��������
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;// �ٷ���ʾ����д
		}
	}

	/**
	 * ����banner����
	 * @param status
	 */
	private void requestBanner(String status, String type) {
		String result = null;
		BaseInfo baseInfo = null;
		long nowTime = System.currentTimeMillis();
		long cacheTime = SettingsManager.getBannerCacheTime(mainActivity);
		try {
			byte[] initJsonB = FileUtil.readByte(mainActivity,
					FileUtil.YLFCF_BANNER_CACHE);
			result = new String(initJsonB);
			// ����init.json
			if (result != null && !"".equals(result)) {
				baseInfo = JsonParseBanner.parseData(result);
			}
		} catch (Exception exx) {
		}
		// ����ʱ�����6���ӣ�������ˢ�»���
		if (baseInfo != null && baseInfo.getmBannerPageInfo() != null && baseInfo.getmBannerPageInfo().getBannerList() != null
				&& nowTime - cacheTime < 0.1 * 3600 * 1000) {
			bannerList.clear();
			int size = baseInfo.getmBannerPageInfo().getBannerList().size();
			for (int i = 0; i < size; i++) {
				BannerInfo banner = baseInfo.getmBannerPageInfo()
						.getBannerList().get(i);
				if (!"�ֻ�����ҳ".equals(banner.getType())) {
					bannerList.add(banner);
				}
			}
			initBanner(bannerList);
			defaultImg.setVisibility(View.GONE);
			checkNov2017Active(baseInfo.getTime());
			return;
		}
		AsyncBanner bannerTask = new AsyncBanner(mainActivity,
				String.valueOf(page), String.valueOf(pageSize), status, type,
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							checkNov2017Active(baseInfo.getTime());
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								bannerList.clear();
								int size = baseInfo.getmBannerPageInfo()
										.getBannerList().size();
								for (int i = 0; i < size; i++) {
									BannerInfo banner = baseInfo
											.getmBannerPageInfo()
											.getBannerList().get(i);
									if (!"�ֻ�����ҳ".equals(banner.getType())) {
										bannerList.add(banner);
									}
								}
								initBanner(bannerList);
								defaultImg.setVisibility(View.GONE);
							}
						}else{
							checkNov2017Active(sdf1.format(new Date()));
						}
					}
				});
		bannerTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * �����б� ---- ȡ��һ�����µ�����
	 *
	 * @param status
	 * @param type
	 */
	private void requestNoticeList(String status, String type) {
		AsyncArticleList articleTask = new AsyncArticleList(mainActivity,
				String.valueOf(0), String.valueOf(1), status, type,
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								Message msg = hanlder
										.obtainMessage(REQUEST_ARTICLELIST_SUCCESS);
								msg.obj = baseInfo;
								hanlder.sendMessage(msg);
							}
						}
					}
				});
		articleTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
