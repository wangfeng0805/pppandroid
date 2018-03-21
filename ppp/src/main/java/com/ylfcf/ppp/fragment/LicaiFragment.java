package com.ylfcf.ppp.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncAppointBorrowList;
import com.ylfcf.ppp.async.AsyncProductPageInfo;
import com.ylfcf.ppp.async.AsyncWDYBorrowDetail;
import com.ylfcf.ppp.async.AsyncXSMBDetail;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.ProductPageInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.ptr.PtrClassicFrameLayout;
import com.ylfcf.ppp.ptr.PtrDefaultHandler;
import com.ylfcf.ppp.ptr.PtrFrameLayout;
import com.ylfcf.ppp.ptr.PtrHandler;
import com.ylfcf.ppp.ui.BannerDetailsActivity;
import com.ylfcf.ppp.ui.BannerTopicActivity;
import com.ylfcf.ppp.ui.BorrowDetailWDYActivity;
import com.ylfcf.ppp.ui.BorrowDetailXSBActivity;
import com.ylfcf.ppp.ui.BorrowDetailXSMBActivity;
import com.ylfcf.ppp.ui.BorrowDetailYYYActivity;
import com.ylfcf.ppp.ui.BorrowListSRZXActivity;
import com.ylfcf.ppp.ui.BorrowListVIPActivity;
import com.ylfcf.ppp.ui.BorrowListZXDActivity;
import com.ylfcf.ppp.ui.MainFragmentActivity;
import com.ylfcf.ppp.util.Constants;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.YLFLogger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * ��Ŀ����
 * @author Administrator
 */
public class LicaiFragment extends BaseFragment implements OnClickListener{
	private static final int REQUEST_XSMB_WHAT = 4521;
	private static final int REQUEST_WDY_WHAT = 4522;//�ȶ�ӯ
	
	private final String className = "LicaiFragment";
	private MainFragmentActivity mainActivity;
	private View topLayout;
	private TextView topTitle;
	private TextView dqlcCounts;//�������δ����ĸ���
	private TextView vipCounts;//vip��Ʒδ����ĸ���
	private TextView srzxCounts;//˽������δ����ĸ���
	private TextView jqqdText;//�����ڴ�
	private Button dtdzText,yzyInvestBtn;//��Ͷ��׬
	private TextView tzfxTextYYY;//Ͷ�ʷ���Ԫ��ӯ
	private TextView gqjxVipText;//
	private TextView xsmbText;//��ʱ�����ʾ����
	private LinearLayout topLeftLayout;
	private Button xsbBtn;//���ֱ�����Ͷ��
	private Button lczqBtn;//�����ȡ����Ͷ��
	private Button xsmbBtn;//��ʱ���
	private Button yyyBtn;//Ԫ��ӯ��������
	private Button srzxInvestBtn,srzxAppointBtn;//˽������Ͷ�ʺ�ԤԼ��ť
	private LinearLayout xsbLayout;//���ֱ�
	private LinearLayout lczqLayout;//�����ȡ
	private LinearLayout xsmbLayout;//��Ϣ���
	private LinearLayout yyyLayout;//Ԫ��ӯ
	private LinearLayout dqlcLayout;//�������
	private LinearLayout vipLayout;//vipר��
	private LinearLayout srzxLayout;//˽������
	private PtrClassicFrameLayout mainRefreshLayout = null;//����ˢ�µĲ���
	private ScrollView mScrollView;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private int page = 0;
	private int pageSize =10;
	
	private View rootView;
	private boolean isFirst = true;//�Ƿ��ǵ�һ�������б�ӿڣ����ڻ�������
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_XSMB_WHAT:
				requestXSMBDetails("����");
				break;
			case REQUEST_WDY_WHAT:
				getWDYBorrowDetails("����", "��");
				break;
			default:
				break;
			}
		}
	};
	
	/**
	 * ������ǰFragment��ʵ������
	 * @param position
	 * @return
	 */
	public static Fragment newInstance(int position) {
		LicaiFragment f = new LicaiFragment();
		Bundle args = new Bundle();
		args.putInt("num", position);
		f.setArguments(args);
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
		if(rootView==null){
            rootView=inflater.inflate(R.layout.licai_fragment, null);
            findViews(rootView);
        }
		//�����rootView��Ҫ�ж��Ƿ��Ѿ����ӹ�parent�� �����parent��Ҫ��parentɾ����Ҫ��Ȼ�ᷢ�����rootview�Ѿ���parent�Ĵ���
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        } 
        requestProductPageInfo("", "����","δ����","��","","","2");
		requestProductPageInfo("vip","����","δ����","��","","","");
        handler.sendEmptyMessage(REQUEST_XSMB_WHAT);
        handler.sendEmptyMessageDelayed(REQUEST_WDY_WHAT, 200);
        //˽�������б�
        new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				requestSRZXPageInfo("����", "δ����");
			}
		}, 700L);
        return rootView;
	}
	
	private void findViews(View view){
		topLayout = view.findViewById(R.id.licai_fragment_toplayout);
		topLeftLayout = (LinearLayout) topLayout.findViewById(R.id.common_topbar_left_layout);
		topLeftLayout.setVisibility(View.GONE);
		topTitle = (TextView) topLayout.findViewById(R.id.common_page_title);
		topTitle.setText("��ҪͶ��");
		
		mScrollView = (ScrollView) view.findViewById(R.id.licai_fragment_main_scrollview);
		dqlcCounts = (TextView)view.findViewById(R.id.licai_fragment_dqlc_counts);
		vipCounts = (TextView)view.findViewById(R.id.licai_fragment_vip_counts);
		srzxCounts = (TextView)view.findViewById(R.id.licai_fragment_srzx_counts);
		jqqdText = (TextView) view.findViewById(R.id.licai_fragment_jqqd_text);
		dtdzText = (Button) view.findViewById(R.id.licai_fragment_dtdz_btn);
		dtdzText.setOnClickListener(this);
		yzyInvestBtn = (Button) view.findViewById(R.id.licai_fragment_yzy_invest_btn);
		yzyInvestBtn.setOnClickListener(this);
		tzfxTextYYY = (TextView) view.findViewById(R.id.licai_fragment_tzfx_btn_yyy);
		tzfxTextYYY.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG ); //�»���
		tzfxTextYYY.getPaint().setAntiAlias(true);//�����
		tzfxTextYYY.setOnClickListener(this);
		gqjxVipText = (TextView) view.findViewById(R.id.licai_fragment_vip_gqjx_btn);
		gqjxVipText.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG ); //�»���
		gqjxVipText.getPaint().setAntiAlias(true);//�����
		gqjxVipText.setOnClickListener(this);
		
		xsmbText = (TextView) view.findViewById(R.id.licai_fragment_xsmb_prompt1);
		SpannableStringBuilder builder = new SpannableStringBuilder(xsmbText.getText().toString());  
		//ForegroundColorSpan Ϊ����ǰ��ɫ��BackgroundColorSpanΪ���ֱ���ɫ  
		ForegroundColorSpan graySpan = new ForegroundColorSpan(getResources().getColor(R.color.gray));  
		ForegroundColorSpan orangeSpan = new ForegroundColorSpan(getResources().getColor(R.color.orange_text));
		builder.setSpan(orangeSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
		builder.setSpan(graySpan, 5, 7, Spannable.SPAN_INCLUSIVE_INCLUSIVE);  
		builder.setSpan(orangeSpan, 7, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
		builder.setSpan(graySpan, 11, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
		builder.setSpan(orangeSpan, 12, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
		builder.setSpan(graySpan, 17, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
		xsmbText.setText(builder);
		
		xsbBtn = (Button)view.findViewById(R.id.licai_fragment_xsb_bidbtn);
		xsbBtn.setOnClickListener(this);
		lczqBtn = (Button)view.findViewById(R.id.licai_fragment_wdy_bidbtn);
		lczqBtn.setOnClickListener(this);
		xsmbBtn = (Button)view.findViewById(R.id.licai_fragment_xsmb_bidbtn);
		xsmbBtn.setOnClickListener(this);
		yyyBtn = (Button) view.findViewById(R.id.licai_fragment_yyy_bidbtn);
		yyyBtn.setOnClickListener(this);
		srzxInvestBtn = (Button) view.findViewById(R.id.licai_fragment_srzx_invest_btn);
		srzxInvestBtn.setOnClickListener(this);
		srzxAppointBtn = (Button) view.findViewById(R.id.licai_fragment_srzx_appoint_btn);
		srzxAppointBtn.setOnClickListener(this);
		dqlcLayout = (LinearLayout)view.findViewById(R.id.licai_fragment_dqlc_layout);
		dqlcLayout.setOnClickListener(this);
		xsbLayout = (LinearLayout)view.findViewById(R.id.licai_fragment_xsb_layout);
		xsbLayout.setOnClickListener(this);
		lczqLayout = (LinearLayout)view.findViewById(R.id.licai_fragment_wdy_layout);
		lczqLayout.setOnClickListener(this);
		xsmbLayout = (LinearLayout)view.findViewById(R.id.licai_fragment_xsmb_layout);
		xsmbLayout.setOnClickListener(this);
		yyyLayout = (LinearLayout)view.findViewById(R.id.licai_fragment_yyy_layout);
		yyyLayout.setOnClickListener(this);
		vipLayout = (LinearLayout)view.findViewById(R.id.licai_fragment_vip_layout);
		vipLayout.setOnClickListener(this);
		srzxLayout = (LinearLayout) view.findViewById(R.id.licai_fragment_srzx_layout);
		srzxLayout.setOnClickListener(this);
		if(!SettingsManager.checkGuoqingJiaxiActivity()){
			gqjxVipText.setVisibility(View.GONE);
		}
		if(SettingsManager.checkTwoYearsTZFXActivity()){
			tzfxTextYYY.setVisibility(View.VISIBLE);
		}
		initRefreshLayout(view);
	}
	
	/**
	 * ����ˢ�µĲ���
	 * @param v
	 */
	private void initRefreshLayout(View v){
		mainRefreshLayout = (PtrClassicFrameLayout) v.findViewById(R.id.licai_fragment_refresh_layout);
		mainRefreshLayout.setLastUpdateTimeRelateObject(this);
		mainRefreshLayout.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
            	requestProductPageInfo("", "����","δ����","��","","","2");
        		requestProductPageInfo("vip","����","δ����","��","","","");
				requestSRZXPageInfo("����", "δ����");
                handler.sendEmptyMessage(REQUEST_XSMB_WHAT);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mScrollView, header);//����ˢ��ʱ������ʾ����
            }
        });
		mainRefreshLayout.setResistance(1.7f);
		mainRefreshLayout.setRatioOfHeaderHeightToRefresh(1.2f);
		mainRefreshLayout.setDurationToClose(200);
		mainRefreshLayout.setDurationToCloseHeader(1000);
        // default is false
		mainRefreshLayout.setPullToRefresh(false);
        // default is true
		mainRefreshLayout.setKeepHeaderWhenRefresh(true);
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		YLFLogger.d("LicaiFragment --- setUserVisibleHint");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		UMengStatistics.statisticsOnPageStart(className);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		UMengStatistics.statisticsOnPageStart(className);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.licai_fragment_xsb_bidbtn:
		case R.id.licai_fragment_xsb_layout:
			Intent intentYXB = new Intent(getActivity(),BorrowDetailXSBActivity.class);
			startActivity(intentYXB);
			break;
		case R.id.licai_fragment_wdy_bidbtn:
		case R.id.licai_fragment_wdy_layout:
			Intent intentLCZQ = new Intent(getActivity(),BorrowDetailWDYActivity.class);
			startActivity(intentLCZQ);
			break;
		case R.id.licai_fragment_xsmb_layout:
		case R.id.licai_fragment_xsmb_bidbtn:
			//��ʱ���
			Intent intentXSMB = new Intent(getActivity(),BorrowDetailXSMBActivity.class);
			startActivity(intentXSMB);
			break;
		case R.id.licai_fragment_yyy_layout:
		case R.id.licai_fragment_yyy_bidbtn:
			Intent intentBL = new Intent(getActivity(),BorrowDetailYYYActivity.class);
			startActivity(intentBL);
			break;
		case R.id.licai_fragment_dqlc_layout:
		case R.id.licai_fragment_yzy_invest_btn:
			Intent intentZXDList = new Intent(getActivity(),BorrowListZXDActivity.class);
			startActivity(intentZXDList);
			break;
		case R.id.licai_fragment_vip_layout:
			Intent intentVIPList = new Intent(getActivity(),BorrowListVIPActivity.class);
			startActivity(intentVIPList);
			break;
		case R.id.licai_fragment_dtdz_btn:
			//��Ͷ��׬
			//��ת��Ԫ��ӯ��Ϣ�banner
			Intent intentBanner = new Intent(mainActivity,BannerTopicActivity.class);
			BannerInfo bannerInfo = new BannerInfo();
			bannerInfo.setArticle_id("110");
			bannerInfo.setLink_url(URLGenerator.FLOAT_RATE_URL);
			intentBanner.putExtra("BannerInfo", bannerInfo);
			startActivity(intentBanner);
			break;
		case R.id.licai_fragment_tzfx_btn_yyy:
			//Ͷ�ʷ���
			Intent intentDetails = new Intent(mainActivity,BannerTopicActivity.class);
			BannerInfo banner = new BannerInfo();
			banner.setArticle_id("170");
			banner.setLink_url(URLGenerator.TWOYEARS_TZFX_URL);
			intentDetails.putExtra("BannerInfo", banner);
			startActivity(intentDetails);
			break;
		case R.id.licai_fragment_vip_gqjx_btn:
			//VIP��Ʒ�����Ϣ
			Intent intentVipDetails = new Intent(mainActivity,BannerDetailsActivity.class);
			BannerInfo bannerVip = new BannerInfo();
			bannerVip.setArticle_id("2409");
			intentVipDetails.putExtra("BannerInfo", bannerVip);
			startActivity(intentVipDetails);
			break;
		case R.id.licai_fragment_srzx_layout:
		case R.id.licai_fragment_srzx_invest_btn:
			//˽������
			Intent intentSRZXList = new Intent(getActivity(),BorrowListSRZXActivity.class);
			startActivity(intentSRZXList);
			break;
		case R.id.licai_fragment_srzx_appoint_btn:
			//˽������ԤԼ��ť
			Intent intentSRZXAppoint = new Intent(getActivity(),BannerTopicActivity.class);
			BannerInfo info = new BannerInfo();
			info.setArticle_id(Constants.TopicType.SRZX_APPOINT);
			info.setLink_url(URLGenerator.SRZX_TOPIC_URL);
			intentSRZXAppoint.putExtra("BannerInfo",info);
			startActivity(intentSRZXAppoint);
			break;
		default:
			break;
		}
	}
	
	/**
	 * ����δ���ı����
	 * @param baseInfo
	 * @param borrowType
	 */
	private void computeCounts(BaseInfo baseInfo,String borrowType){
		List<ProductInfo> productList = baseInfo.getProductPageInfo().getProductList();
		int count = 0;
		for(int i=0;i<productList.size();i++){
			ProductInfo product = productList.get(i);
			if("δ����".equals(product.getMoney_status())){
				count++;
			}
		}
		if("".equals(borrowType)){
			//������Ʋ�Ʒ
			dqlcCounts.setText(count+"");
		}else if("vip".equals(borrowType)){
			//vip��Ʒ
			vipCounts.setVisibility(View.VISIBLE);
			jqqdText.setVisibility(View.GONE);
			vipCounts.setText(count+"");
		}
	}
	
	/**
	 * ����˽�������Ʒδ����ĸ���
	 * @param baseInfo
	 */
	private void computeSRZXCounts(BaseInfo baseInfo){
		if(baseInfo == null){
//			srzxCounts.setVisibility(View.VISIBLE);
//			srzxCounts.setText("10");
			return;
		}
		ProductPageInfo pageInfo = baseInfo.getProductPageInfo();
		if(pageInfo != null && pageInfo.getProductList().size() > 0){
			srzxCounts.setVisibility(View.VISIBLE);
			srzxCounts.setText(pageInfo.getTotal());
		}else{
			srzxCounts.setVisibility(View.GONE);
		}
	}
	
	private void initXSMBView(BaseInfo baseInfo){
		if(baseInfo == null || baseInfo.getmProductInfo() == null){
			return;
		}
		ProductInfo info = baseInfo.getmProductInfo();
		//��ʱ���
		if (SettingsManager.checkActiveStatusBySysTime(baseInfo.getTime(),SettingsManager.xsmbStartDate,
				SettingsManager.xsmbEndDate) == 0) {
			xsmbLayout.setVisibility(View.VISIBLE);
		}
		if("������".equals(info.getMoney_status())){
			xsmbBtn.setTextColor(getResources().getColor(R.color.white));
			xsmbBtn.setBackgroundResource(R.drawable.style_rect_fillet_licai_xsmb_gray);
		}else if("δ����".equals(info.getMoney_status())){
			String nowTimeStr = info.getNow_time();
			String willStartTimeStr = info.getWill_start_time();//��ʼʱ��
			long remainTimeL = 0l;
			try {
				Date nowDate = sdf.parse(nowTimeStr);
				Date willStartDate = sdf.parse(willStartTimeStr);
				remainTimeL = willStartDate.getTime() - nowDate.getTime();
				if(remainTimeL >= 0){
					//����ʱ�����
					xsmbBtn.setTextColor(getResources().getColor(R.color.white));
					xsmbBtn.setBackgroundResource(R.drawable.style_rect_fillet_licai_xsmb_gray);
				}else{
//					//����Ͷ���С���
					xsmbBtn.setTextColor(getResources().getColor(R.color.licai_xsmb_btn_color));
					xsmbBtn.setBackgroundResource(R.drawable.style_rect_fillet_licai_xsmb);
				}
			} catch (ParseException e) {
				xsmbBtn.setTextColor(getResources().getColor(R.color.white));
				xsmbBtn.setBackgroundResource(R.drawable.style_rect_fillet_licai_xsmb_gray);
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * ��Ʒ�б�
	 * 
	 */
	private void requestProductPageInfo(final String borrowType, String borrowStatus,String moneyStatus,
			String isShow, String isWap, String plan,String isVip) {
		AsyncProductPageInfo productTask = new AsyncProductPageInfo(
				mainActivity, String.valueOf(page), String.valueOf(pageSize),
				borrowType, borrowStatus,moneyStatus, isShow, isWap, plan, isFirst,"2",isVip,
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								computeCounts(baseInfo,borrowType);
							}else{
							}
						}else{
						}
					}
				});
		productTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * ˽�������Ʒ�б�
	 */
	private void requestSRZXPageInfo(String borrowStatus,String moneyStatus) {
		AsyncAppointBorrowList productTask = new AsyncAppointBorrowList(
				mainActivity, borrowStatus,moneyStatus,String.valueOf(page), String.valueOf(pageSize),
				true,new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if (resultCode == 0) {
								computeSRZXCounts(baseInfo);
							}else{
								computeSRZXCounts(null);
							}
						} 
					}
				});
		productTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * �������
	 */
	private void requestXSMBDetails(String borrowStatus){
		AsyncXSMBDetail xsmbTask = new AsyncXSMBDetail(mainActivity, borrowStatus,new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(mainRefreshLayout != null){
					mainRefreshLayout.refreshComplete();
				}
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						initXSMBView(baseInfo);
					}
				}
			}
		});
		xsmbTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * ��ȡ����һ�ڵ��ȶ�Ӯ��Ʒ����
	 * @param borrowStatus
	 * @param isShow
	 */
	private void getWDYBorrowDetails(String borrowStatus,String isShow){
		AsyncWDYBorrowDetail wdyTask = new AsyncWDYBorrowDetail(mainActivity, borrowStatus, isShow, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								lczqLayout.setVisibility(View.VISIBLE);
							}else{
								//���ȶ�ӯģ���������
								lczqLayout.setVisibility(View.GONE);
							}
						}
					}
				});
		wdyTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
