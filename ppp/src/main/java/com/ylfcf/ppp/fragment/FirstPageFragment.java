package com.ylfcf.ppp.fragment;

import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import com.ant.liao.GifView;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncArticleList;
import com.ylfcf.ppp.async.AsyncBanner;
import com.ylfcf.ppp.async.AsyncInvestmentList;
import com.ylfcf.ppp.common.FileUtil;
import com.ylfcf.ppp.entity.ArticleInfo;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.InvestmentListInfo;
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
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.YLFLogger;
import com.ylfcf.ppp.widget.LoadingDialog;
import java.text.DecimalFormat;
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
public class FirstPageFragment extends BaseFragment implements OnClickListener, OnItemClickListener {
	private static final String className = "FirstPageFragment";

	private static final int REQUEST_ARTICLELIST_WHAT = 5701;// ����
	private static final int REQUEST_ARTICLELIST_SUCCESS = 5702;
	private static final int REFRESH_NOTICE = 5703;

	private MainFragmentActivity mainActivity;
//	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private List<BannerInfo> bannerList = new ArrayList<BannerInfo>();
	private ConvenientBanner mBanner;
	private LoadingDialog mLoadingDialog;
	private LinearLayout noticeLayout;// ����Ĳ���
	private TextView noticeTitle, noticeTime;
	private ImageView defaultImg;
	private View rootView;
	private int page = 0;
	private int pageSize = 20;
	private boolean isFirst = true;// �Ƿ��״ν�����ҳ��
	private List<ArticleInfo> articleList;
	private ProductInfo xsbInfo;
	private FragmentManager fragmentManager = null;

	private LinearLayout mLl_invest_list1;
	private LinearLayout mLl_invest_list2;
	private LinearLayout mLl_invest_list3;

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
		requestInvestList("order_type","ASC");
		return rootView;
	}

	private void findViews(View view, LayoutInflater inflater) {
		defaultImg = (ImageView) view
				.findViewById(R.id.first_page_fragment_default_img);
		noticeLayout = (LinearLayout) view
				.findViewById(R.id.first_page_fragment_notice_layout);
		noticeLayout.setOnClickListener(this);
		noticeTitle = (TextView) view
				.findViewById(R.id.first_page_fragment_notice_text);
		noticeTime = (TextView) view
				.findViewById(R.id.first_page_fragment_notice_time);

		mBanner = (ConvenientBanner) view.findViewById(R.id.convenientBanner);
        initInvestListView(view);
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
					requestInvestList("order_type","ASC");
				}
			}
		},null);
	}

    private TextView tv_purpose1;
    private TextView tv_purpose2;
    private TextView tv_purpose3;

    private TextView tv_lihua1;
    private TextView tv_lihua2;
    private TextView tv_lihua3;

    private TextView tv_time1;
    private TextView tv_time2;
    private TextView tv_time3;

    private TextView tv_actual_raising_money1;
    private TextView tv_actual_raising_money2;
    private TextView tv_actual_raising_money3;

    private ProgressBar actual_raising_money_pb1;
    private ProgressBar actual_raising_money_pb2;
    private ProgressBar actual_raising_money_pb3;

    private void initInvestListView(View view) {
        mLl_invest_list1 = (LinearLayout) view.findViewById(R.id.ll_investment_list1);
        mLl_invest_list2 = (LinearLayout) view.findViewById(R.id.ll_investment_list2);
        mLl_invest_list3 = (LinearLayout) view.findViewById(R.id.ll_investment_list3);

        tv_purpose1 = (TextView) view.findViewById(R.id.tv_purpose1);
        tv_purpose2= (TextView) view.findViewById(R.id.tv_purpose2);
        tv_purpose3 = (TextView) view.findViewById(R.id.tv_purpose3);
        tv_lihua1 = (TextView) view.findViewById(R.id.tv_lihua1);
        tv_lihua2 = (TextView) view.findViewById(R.id.tv_lihua2);
        tv_lihua3 = (TextView) view.findViewById(R.id.tv_lihua3);
        tv_time1 = (TextView) view.findViewById(R.id.tv_time1);
        tv_time2 = (TextView) view.findViewById(R.id.tv_time2);
        tv_time3 = (TextView) view.findViewById(R.id.tv_time3);
        tv_actual_raising_money1 = (TextView) view.findViewById(R.id.tv_actual_raising_money1);
        tv_actual_raising_money2 = (TextView) view.findViewById(R.id.tv_actual_raising_money2);
        tv_actual_raising_money3 = (TextView) view.findViewById(R.id.tv_actual_raising_money3);
        actual_raising_money_pb1 = (ProgressBar) view.findViewById(R.id.actual_raising_money_pb1);
        actual_raising_money_pb2 = (ProgressBar) view.findViewById(R.id.actual_raising_money_pb3);
        actual_raising_money_pb3 = (ProgressBar) view.findViewById(R.id.actual_raising_money_pb3);

        mLl_invest_list1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});

		mLl_invest_list2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});

		mLl_invest_list3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});
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
        mBanner.setPages(new CBViewHolderCreator<LocalImageHolderView>() {
            @Override
            public LocalImageHolderView createHolder() {
                return new LocalImageHolderView();
            }
        }, bannerList)
                //����������ͼƬ��Ϊ��ҳָʾ������������û��ָʾ�������Ը����Լ�������������Լ���ָʾ��,����ҪԲ��ָʾ�����ò���
                .setPageIndicator(new int[]{R.drawable.bb_banner_point, R.drawable.bb_banner_point_sel})
                //����ָʾ���ķ���
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
//                .setOnPageChangeListener(FirstpageFragment.this)//������ҳ�¼�
                .setOnItemClickListener(FirstPageFragment.this);
        //���÷�ҳ��Ч��������Ҫ��ҳЧ�����ò���
        //.setPageTransformer(Transformer.DefaultTransformer);    ������Ч֮����а��������°��Ѿ����룬���Ҫ������Ч�����ӿ��Կ�Demo�ĵ����Ӧ��
        //        convenientBanner.setManualPageable(false);//���ò����ֶ�Ӱ��
//        if (bannerListData.size() > 0) {
//            mTextView.setText(bannerListData.get(0).title);
//        }
	}

    public class LocalImageHolderView implements Holder<BannerInfo> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, BannerInfo data) {
            Glide.with(mContext).load(data.getPic_url()).into(imageView);
        }
    }

    /**
     * banner����¼�
     *
     * @param position
     */
    @Override
    public void onItemClick(int position) {
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
//		if(flag == 0){
//			//�������
//			mGifView.setVisibility(View.VISIBLE);
//		}else{
//			mGifView.setVisibility(View.GONE);
//		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		YLFLogger.d("FirstPageFragment -- setUserVisibleHint ---"
				+ isVisibleToUser);
	}

	@Override
	public void onResume() {
		super.onResume();
		UMengStatistics.statisticsOnPageStart(className);
		YLFLogger.d("FirstPageFragment -- onResume() ---");
        //��ʼ�Զ���ҳ
        if (mBanner != null) {
            mBanner.startTurning(3000);
        }
    }

	@Override
	public void onPause() {
		super.onPause();
		UMengStatistics.statisticsOnPageEnd(className);
        //ֹͣ��ҳ
        if (mBanner != null) {
            mBanner.stopTurning();
        }
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
			default:
				break;
		}
	}

	private void shared(){
		String userId = SettingsManager.getUserId(mainActivity.getApplicationContext());
		if(userId != null && !"".equals(userId)){
			//�ѵ�¼
//			hytjBtn.setEnabled(false);
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
//					hytjBtn.setEnabled(true);
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
	 * Ͷ���б� ---- ȡǰ�������µ�����
	 *
	 * @param order_by
	 * @param sort
	 */
	private void requestInvestList(String order_by, String sort) {
		AsyncInvestmentList investTask = new AsyncInvestmentList(mainActivity,
				String.valueOf(0), String.valueOf(3), order_by, sort,
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								initInvestList(baseInfo.getInvestmentListInfo());
							}
						}
					}
				});
		investTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);

	}

	private void initInvestList(InvestmentListInfo investmentListInfo) {
		//��ʼ����ҳ�м�Ͷ���б�
		if(investmentListInfo.getInvestListData().size() == 3) {
            mLl_invest_list1.setVisibility(View.VISIBLE);
            mLl_invest_list2.setVisibility(View.VISIBLE);
            mLl_invest_list3.setVisibility(View.VISIBLE);
            initInvest1(investmentListInfo);
            initInvest2(investmentListInfo);
            initInvest3(investmentListInfo);
		}else if(investmentListInfo.getInvestListData().size() == 2) {
            mLl_invest_list1.setVisibility(View.VISIBLE);
            mLl_invest_list2.setVisibility(View.VISIBLE);
            mLl_invest_list3.setVisibility(View.GONE);
			initInvest1(investmentListInfo);
			initInvest2(investmentListInfo);
		}else if(investmentListInfo.getInvestListData().size() == 1) {
            mLl_invest_list1.setVisibility(View.VISIBLE);
            mLl_invest_list2.setVisibility(View.GONE);
            mLl_invest_list3.setVisibility(View.GONE);
			initInvest1(investmentListInfo);
		}else if(investmentListInfo.getInvestListData().size() == 0) {
            mLl_invest_list1.setVisibility(View.GONE);
            mLl_invest_list2.setVisibility(View.GONE);
            mLl_invest_list3.setVisibility(View.GONE);
		}
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


	private void initInvest1(InvestmentListInfo investmentListInfo) {
		if(investmentListInfo.getInvestListData().get(0).getBorrow_interest() != null ) {
			tv_lihua1.setText(investmentListInfo.getInvestListData().get(0).getBorrow_interest());
		}
		if(investmentListInfo.getInvestListData().get(0).getUse_purpose() != null) {
			tv_purpose1.setText(investmentListInfo.getInvestListData().get(0).getUse_purpose());
		}
		if(investmentListInfo.getInvestListData().get(0).getBorrow_period() != null) {
			tv_time1.setText(investmentListInfo.getInvestListData().get(0).getBorrow_period());
		}
		int actual_loan_money = 0;
		int actual_raising_money = 0;
		if(investmentListInfo.getInvestListData().get(0).getActual_loan_money() != null) {
			actual_loan_money = Integer.parseInt(investmentListInfo.getInvestListData().get(0).getActual_loan_money());
		}
		if(investmentListInfo.getInvestListData().get(0).getActual_raising_money() != null) {
			actual_raising_money = Integer.parseInt(investmentListInfo.getInvestListData().get(0).getActual_raising_money());
		}
		DecimalFormat df = new DecimalFormat("0.0");
		String scale = df.format((float)(actual_raising_money*100)/actual_loan_money);
		actual_raising_money_pb1.setMax(actual_loan_money);
		actual_raising_money_pb1.setProgress(actual_raising_money);
		tv_actual_raising_money1.setText(scale +"%");
	}

	private void initInvest2(InvestmentListInfo investmentListInfo) {
		if(investmentListInfo.getInvestListData().get(1).getBorrow_interest() != null ) {
			tv_lihua2.setText(investmentListInfo.getInvestListData().get(1).getBorrow_interest());
		}
		if(investmentListInfo.getInvestListData().get(1).getUse_purpose() != null) {
			tv_purpose2.setText(investmentListInfo.getInvestListData().get(1).getUse_purpose());
		}
		if(investmentListInfo.getInvestListData().get(1).getBorrow_period() != null) {
			tv_time2.setText(investmentListInfo.getInvestListData().get(1).getBorrow_period());
		}
		int actual_loan_money = 0;
		int actual_raising_money = 0;
		if(investmentListInfo.getInvestListData().get(1).getActual_loan_money() != null) {
			actual_loan_money = Integer.parseInt(investmentListInfo.getInvestListData().get(1).getActual_loan_money());
		}
		if(investmentListInfo.getInvestListData().get(1).getActual_raising_money() != null) {
			actual_raising_money = Integer.parseInt(investmentListInfo.getInvestListData().get(1).getActual_raising_money());
		}
		DecimalFormat df = new DecimalFormat("0.0");
		String scale = df.format((float)(actual_raising_money*100)/actual_loan_money);
		actual_raising_money_pb2.setMax(actual_loan_money);
		actual_raising_money_pb2.setProgress(actual_raising_money);
		tv_actual_raising_money2.setText(scale +"%");
	}

	private void initInvest3(InvestmentListInfo investmentListInfo) {
		if(investmentListInfo.getInvestListData().get(2).getBorrow_interest() != null ) {
			tv_lihua3.setText(investmentListInfo.getInvestListData().get(2).getBorrow_interest());
		}
		if(investmentListInfo.getInvestListData().get(2).getUse_purpose() != null) {
			tv_purpose3.setText(investmentListInfo.getInvestListData().get(2).getUse_purpose());
		}
		if(investmentListInfo.getInvestListData().get(2).getBorrow_period() != null) {
			tv_time3.setText(investmentListInfo.getInvestListData().get(2).getBorrow_period());
		}
		int actual_loan_money = 0;
		int actual_raising_money = 0;
		if(investmentListInfo.getInvestListData().get(2).getActual_loan_money() != null) {
			actual_loan_money = Integer.parseInt(investmentListInfo.getInvestListData().get(2).getActual_loan_money());
		}
		if(investmentListInfo.getInvestListData().get(2).getActual_raising_money() != null) {
			actual_raising_money = Integer.parseInt(investmentListInfo.getInvestListData().get(2).getActual_raising_money());
		}
		DecimalFormat df = new DecimalFormat("0.0");
		String scale = df.format((float)(actual_raising_money*100)/actual_loan_money);
		actual_raising_money_pb3.setMax(actual_loan_money);
		actual_raising_money_pb3.setProgress(actual_raising_money);
		tv_actual_raising_money3.setText(scale +"%");
	}

}
