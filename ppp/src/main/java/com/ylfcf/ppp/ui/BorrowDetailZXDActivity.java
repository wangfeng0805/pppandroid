package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncProductInfo;
import com.ylfcf.ppp.async.AsyncProjectDetails;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.BorrowType;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.ProjectCailiaoInfo;
import com.ylfcf.ppp.entity.ProjectInfo;
import com.ylfcf.ppp.fragment.ProductInfoFragment.OnProductInfoListener;
import com.ylfcf.ppp.fragment.ProductSafetyFragment.OnProductSafetyListener;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnIsBindingListener;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.util.Constants;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.ylfcf.ppp.util.Util.formatRate;

/**
 * ���Ŵ�-0-��Ŀ����
 * 
 * @author Administrator
 * 
 */
public class BorrowDetailZXDActivity extends BaseActivity implements
		OnClickListener {
	private static final String className = "BorrowDetailZXDActivity";
	private static final int REFRESH_PROGRESSBAR = 1902;
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private TextView borrowName;
	private TextView borrowRateMin;// �껯���� �������ʵ���С����
	private TextView borrowRateMiddle;//�껯�����������С֮��ķ���
	private TextView borrowRateMax;//�������ʵ��������
	private TextView borrowMoney;// ļ�����
	private TextView timeLimit;// ����
	private Button bidBtn;// ����Ͷ��
	private TextView repayType1;
	private TextView repayType2;
	private TextView borrowBalanceTV;
	private TextView profitTv;// һ���Ǯ�ɵ����档��
	private TextView qitouMoneyTv;
	private ProgressBar progressBar;
	// private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private ViewPager mViewPager;
	private LinearLayout introLayout, safeLayout, zizhiLayout, recordLayout;
	private RelativeLayout extraInterestLayout;
	private TextView extraInterestText;
	private ImageView jiaxiTipsImg;
	private ImageView timeLimitTpisImg;

	public ProductInfo productInfo;
	public InvestRecordInfo recordInfo;
	private ProjectInfo project;// ��Ŀ��Ϣ
	private OnProductInfoListener productInfoListener;
	private OnProductSafetyListener productSafetyListener;

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REFRESH_PROGRESSBAR:
				int progress_ = (Integer) msg.obj;
				progressbarIncrease(progress_);
				break;
			default:
				break;
			}
		}
	};

	public void setOnProductInfoListener(OnProductInfoListener listener) {
		this.productInfoListener = listener;
	}

	public void setOnProductSafetyListener(OnProductSafetyListener listener) {
		this.productSafetyListener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.borrow_details_zxd_activity);
		Intent intent = getIntent();
		productInfo = (ProductInfo) intent.getSerializableExtra("PRODUCT_INFO");
		recordInfo = (InvestRecordInfo) intent
				.getSerializableExtra("InvestRecordInfo");
		findViews();
		if (productInfo != null) {
			getProjectDetails(productInfo.getProject_id());
			initDataFromProductList();
		} else if (recordInfo != null) {
			// �ȸ���Borrowid��ȡprojectid
			getProductDetailsById(recordInfo.getBorrow_id(), "","");
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}
	
	@SuppressWarnings("deprecation")
	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("��Ʒ����");

		borrowName = (TextView) findViewById(R.id.borrow_detail_activity_borrowname);
		borrowRateMin = (TextView) findViewById(R.id.borrow_details_activity_invest_rate_min);
		borrowRateMiddle = (TextView) findViewById(R.id.borrow_details_zxd_activity_rate_middle);
		borrowRateMax = (TextView) findViewById(R.id.borrow_details_activity_invest_rate_max);
		borrowMoney = (TextView) findViewById(R.id.borrow_details_activity_invest_total_money);
		timeLimit = (TextView) findViewById(R.id.borrow_details_invest_time_limit);
		bidBtn = (Button) findViewById(R.id.borrow_detail_activity_bidBtn);
		bidBtn.setOnClickListener(this);
		repayType1 = (TextView) findViewById(R.id.borrow_details_activity_repay_type1);
		repayType2 = (TextView) findViewById(R.id.borrow_details_activity_repay_type2);
		progressBar = (ProgressBar) findViewById(R.id.borrow_details_activity_pb);
		borrowBalanceTV = (TextView) findViewById(R.id.borrow_details_activity_borrow_balance_text);
		profitTv = (TextView) findViewById(R.id.borrow_details_activity_profit);
		qitouMoneyTv = (TextView)findViewById(R.id.borrow_detail_zxd_activity_qitou);

		introLayout = (LinearLayout) findViewById(R.id.borrow_details_activity_intro_layout);
		introLayout.setOnClickListener(this);
		safeLayout = (LinearLayout) findViewById(R.id.borrow_details_activity_safe_layout);
		safeLayout.setOnClickListener(this);
		zizhiLayout = (LinearLayout) findViewById(R.id.borrow_details_activity_certificate_layout);
		zizhiLayout.setOnClickListener(this);
		recordLayout = (LinearLayout) findViewById(R.id.borrow_details_activity_record_layout);
		recordLayout.setOnClickListener(this);
		extraInterestLayout = (RelativeLayout) findViewById(R.id.borrow_details_zxd_extra_interest_layout);
		extraInterestText = (TextView) findViewById(R.id.borrow_details_zxd_extra_interest_text);
		jiaxiTipsImg = (ImageView) findViewById(R.id.borrow_details_zxd_activity_tips_img);
		timeLimitTpisImg = (ImageView) findViewById(R.id.borrow_details_zxd_timelimit_tips_img);
	}

	private void initTimeLimitTipsImg(BaseInfo baseInfo){
		if(baseInfo == null)
			return;
		String interestPeriodStr = "";
		if(productInfo != null){
			interestPeriodStr = productInfo.getInterest_period();
		}else if(recordInfo != null){
			interestPeriodStr = recordInfo.getInterest_period();
		}
		if(interestPeriodStr.contains("92") && SettingsManager.checkActiveStatusBySysTime(baseInfo.getTime(),
				SettingsManager.activeJuly2017_StartTime,SettingsManager.activeJuly2017_EndTime) == 0){
			timeLimitTpisImg.setVisibility(View.VISIBLE);
		}else{
			timeLimitTpisImg.setVisibility(View.GONE);
		}
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

	private void initBidBtnStatus(ProductInfo info){
		if(info == null){
			return;
		}
		Date addDate = null;
		try{
			addDate = sdf.parse(info.getAdd_time());
		}catch (Exception e){
			e.printStackTrace();
		}
		if("δ����".equals(info.getMoney_status())){
			if(productInfo == null){
				productInfo = new ProductInfo();
				productInfo.setAdd_time(recordInfo.getAdd_time());
			}
			if(SettingsManager.checkActiveStatusBySysTime(productInfo.getAdd_time(),SettingsManager.yyyJIAXIStartTime,
					SettingsManager.yyyJIAXIEndTime) == 0 && "Ԫ����".equals(info.getBorrow_type())&& Constants.UserType.USER_COMPANY.
					equals(SettingsManager.getUserType(BorrowDetailZXDActivity.this))){
				//VIP����ҵ�û�
				bidBtn.setEnabled(false);
			}else{
				bidBtn.setEnabled(true);
			}
			bidBtn.setText("����Ͷ��");
		}else{
			bidBtn.setEnabled(false);
			bidBtn.setText("Ͷ���ѽ���");
		}
	}

	int biteIntFromProduct = 0;
	private void initDataFromProductList() {
		initInvestBalance(productInfo);
		initBidBtnStatus(productInfo);
		borrowName.setText(productInfo.getBorrow_name());

		// �껯����
		String rate = productInfo.getInterest_rate();
		String extraRate = productInfo.getAndroid_interest_rate();
		float extraRateF = 0f;
		try {
			extraRateF = Float.parseFloat(extraRate);
		} catch (Exception e) {
		}

		if(SettingsManager.checkActiveStatusBySysTime(productInfo.getAdd_time(),SettingsManager.yyyJIAXIStartTime,
				SettingsManager.yyyJIAXIEndTime) == 0 && BorrowType.YUANNIANXIN.equals(productInfo.getBorrow_type())){
			extraInterestLayout.setVisibility(View.VISIBLE);
			extraInterestText.setVisibility(View.GONE);
			jiaxiTipsImg.setVisibility(View.VISIBLE);
		}else{
			if (extraRateF > 0) {
				extraInterestLayout.setVisibility(View.VISIBLE);
				jiaxiTipsImg.setVisibility(View.GONE);
				extraInterestText.setText("+"
						+ formatRate(productInfo.getAndroid_interest_rate()));
			} else {
				extraInterestLayout.setVisibility(View.GONE);
			}
		}

		// Ͷ������
		String horizon = "";
		if(productInfo.getInvest_horizon() == null || "".equals(productInfo.getInvest_horizon())){
			horizon = productInfo.getInterest_period();
		}else{
			horizon = productInfo.getInvest_horizon().replace("��", "");
		}
		int horizonInt = 0;
		try {
			horizonInt = Integer.parseInt(horizon);
		} catch (Exception e) {
			horizonInt = 0;
		}
		
		float minRateF = 0f;
		float maxRateF = 0f;
		try {
			minRateF = Float.parseFloat(rate);
			maxRateF = minRateF + 0.3f;
		} catch (Exception e) {
		}
		if(SettingsManager.checkFloatRate(productInfo) && (BorrowType.SUYING.equals(productInfo.getBorrow_type()) || BorrowType.BAOYING.equals(productInfo.getBorrow_type())
				|| BorrowType.WENYING.equals(productInfo.getBorrow_type()) || BorrowType.YUANNIANXIN.equals(productInfo.getBorrow_type()))){
			if(productInfo.getInterest_period().contains("365")){
				borrowRateMin.setVisibility(View.GONE);
				borrowRateMiddle.setVisibility(View.GONE);
				borrowRateMax.setText(formatRate(String.valueOf(minRateF)));
			}else{
				borrowRateMin.setVisibility(View.VISIBLE);
				borrowRateMiddle.setVisibility(View.VISIBLE);
				borrowRateMin.setText(formatRate(String.valueOf(minRateF)));
				borrowRateMax.setText(formatRate(String.valueOf(maxRateF)));
			}
        }else{
        	borrowRateMin.setVisibility(View.GONE);
			borrowRateMiddle.setVisibility(View.GONE);
			borrowRateMax.setText(formatRate(String.valueOf(minRateF)));
        }
		timeLimit.setText(horizon);
		repayType1.setText(productInfo.getRepay_way());
		repayType2.setText(productInfo.getRepay_way());
		qitouMoneyTv.setText(Util.formatRate(productInfo.getInvest_lowest())+"Ԫ");

		double totalMoneyL = 0d;
		int totalMoneyI = 0;
		try {
			totalMoneyL = Double.parseDouble(productInfo.getTotal_money());
			totalMoneyI = (int) totalMoneyL;
		} catch (Exception e) {
		}
		if(totalMoneyI < 10000){
			borrowMoney.setText(Util.double2PointDoubleOne(totalMoneyI / 10000d) + "");
		}else{
			borrowMoney.setText((totalMoneyI / 10000) + "");
		}
		String bite = productInfo.getBite();
		float biteFloat = 0f;
		
		if (bite != null) {
			bite = bite.replace("%", "");
		}
		try {
			biteFloat = Float.parseFloat(bite) * 100;
			biteIntFromProduct = (int) biteFloat;
		} catch (Exception e) {
		}
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				progressbarIncrease(biteIntFromProduct);
			}
		}, 500L);
		float rateF = 0f;
		try {
			rateF = Float.parseFloat(rate);
		} catch (Exception e) {
		}
		
		profitTv.setText(new DecimalFormat("#.00").format((rateF + extraRateF) * 100/365*horizonInt) + "");
	}

	int biteIntFromRecord = 0;
	private void initDataFromRecord(ProductInfo info) {
		initInvestBalance(info);
		initBidBtnStatus(info);
		productInfo = info;
		borrowName.setText(info.getBorrow_name());
		// �껯����
		String rate = info.getInterest_rate();
		String extraRate = productInfo.getAndroid_interest_rate();
		float extraRateF = 0f;
		try {
			extraRateF = Float.parseFloat(extraRate);
		} catch (Exception e) {
		}

		Date addDate = null;
		try{
			addDate = sdf.parse(productInfo.getAdd_time());
		}catch (Exception e){

		}
		if(SettingsManager.checkActiveStatusBySysTime(productInfo.getAdd_time(),SettingsManager.yyyJIAXIStartTime,
				SettingsManager.yyyJIAXIEndTime) == 0 && BorrowType.YUANNIANXIN.equals(productInfo.getBorrow_type())){
			extraInterestLayout.setVisibility(View.VISIBLE);
			extraInterestText.setVisibility(View.GONE);
			jiaxiTipsImg.setVisibility(View.VISIBLE);
		}else{
			if (extraRateF > 0) {
				extraInterestLayout.setVisibility(View.VISIBLE);
				jiaxiTipsImg.setVisibility(View.GONE);
				extraInterestText.setText("+"
						+ formatRate(productInfo.getAndroid_interest_rate()));
			} else {
				extraInterestLayout.setVisibility(View.GONE);
			}
		}
		// Ͷ������
		String horizon = "";
		if(info.getInvest_horizon() == null || "".equals(info.getInvest_horizon())){
			horizon = info.getInterest_period();
		}else{
			horizon = info.getInvest_horizon().replace("��", "");
		}
		int horizonInt = Integer.parseInt(horizon);
		
//		borrowRate.setText(rate);
		float minRateF = 0f;
		float maxRateF = 0f;
		try {
			minRateF = Float.parseFloat(rate);
			maxRateF = minRateF + 0.3f;
		} catch (Exception e) {
		}
		if(SettingsManager.checkFloatRate(productInfo) && (BorrowType.SUYING.equals(productInfo.getBorrow_type()) || BorrowType.BAOYING.equals(productInfo.getBorrow_type())
				|| BorrowType.WENYING.equals(productInfo.getBorrow_type()))){
			if(productInfo.getInterest_period().contains("365")){
				borrowRateMin.setVisibility(View.GONE);
				borrowRateMiddle.setVisibility(View.GONE);
				borrowRateMax.setText(formatRate(String.valueOf(minRateF)));
			}else{
				borrowRateMin.setVisibility(View.VISIBLE);
				borrowRateMiddle.setVisibility(View.VISIBLE);
				borrowRateMin.setText(formatRate(String.valueOf(minRateF)));
				borrowRateMax.setText(formatRate(String.valueOf(maxRateF)));
			}
        }else{
        	borrowRateMin.setVisibility(View.GONE);
			borrowRateMiddle.setVisibility(View.GONE);
			borrowRateMax.setText(formatRate(String.valueOf(minRateF)));
        }
        timeLimit.setText(horizon);
		repayType1.setText(info.getRepay_way());
		repayType2.setText(info.getRepay_way());
		qitouMoneyTv.setText(Util.formatRate(info.getInvest_lowest())+"Ԫ");

		double totalMoneyL = 0d;
		double investedMoneyD = 0d;//��Ͷ�ʵ�Ǯ
		int totalMoneyI = 0;
		int investedMoneyI = 0; 
		try {
			totalMoneyL = Double.parseDouble(info.getTotal_money());
			totalMoneyI = (int) totalMoneyL;
			investedMoneyD = Double.parseDouble(info.getInvest_money());
			investedMoneyI = (int)investedMoneyD;
		} catch (Exception e) {
		}
		if(totalMoneyI < 10000){
			borrowMoney.setText(Util.double2PointDoubleOne(totalMoneyI / 10000d) + "");
		}else{
			borrowMoney.setText((totalMoneyI / 10000) + "");
		}
		float biteFloat = 0f;
		try {
			biteFloat = investedMoneyI*100/totalMoneyI;
			biteIntFromRecord = (int) biteFloat;
		} catch (Exception e) {
		}
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				progressbarIncrease(biteIntFromRecord*100);
			}
		}, 500L);
		
		float rateF = 0f;
		try {
			rateF = Float.parseFloat(rate);
		} catch (Exception e) {
		}
		profitTv.setText(new DecimalFormat("#.00").format((rateF + extraRateF) * 100/365*horizonInt) + "");
	}
	
	int increaseInt = 0;//����
	int progressTemp = 0;
	private void progressbarIncrease(int progress){
		progressBar.setProgress(progressTemp);
		increaseInt = progress / 50;
		if(progressTemp >= progress){
			return;
		}
		progressTemp += increaseInt;
		if(progressTemp >= progress){
			progressTemp = progress;
		}
		Message msg = handler.obtainMessage(REFRESH_PROGRESSBAR);
		msg.obj = progress;
		handler.sendMessageDelayed(msg, 10L);
	}
	
	private void initInvestBalance(ProductInfo info){
		if(info == null){
			return;
		}
		double totalMoneyL = 0d;
        int totalMoneyI = 0;
        double investMoneyL = 0d;
        int investMoneyI = 0;
        int borrowBalance = 0;
        try {
        	totalMoneyL = Double.parseDouble(info.getTotal_money());
        	investMoneyL = Double.parseDouble(info.getInvest_money());
        	totalMoneyI = (int)totalMoneyL;
        	investMoneyI = (int)investMoneyL;
        	borrowBalance = totalMoneyI - investMoneyI;
		} catch (Exception e) {
		}
        
        borrowBalanceTV.setText(borrowBalance+"");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.borrow_detail_activity_bidBtn:
			// ��SettingsManager�ж�ȡ���룬���Ϊ����ζ��û�е�¼��
			boolean isLogin = !SettingsManager.getLoginPassword(
					BorrowDetailZXDActivity.this).isEmpty()
					&& !SettingsManager.getUser(BorrowDetailZXDActivity.this)
							.isEmpty();
			// 1������Ƿ��Ѿ���¼
			if (isLogin) {
				//�ж��Ƿ�ʵ����
				checkIsVerify("Ͷ��");
			} else {
				// δ��¼����ת����¼ҳ��
				Intent intent = new Intent();
				intent.setClass(BorrowDetailZXDActivity.this, LoginActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.common_topbar_left_layout:
			finish();
			break;
		// ��Ŀ����
		case R.id.borrow_details_activity_intro_layout:
			// setViewPagerCurrentPosition(0);
			Intent intentProductInfo = new Intent(BorrowDetailZXDActivity.this,ProductInfoActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("PROJECT_INFO", project);
			bundle.putSerializable("PRODUCT_INFO", productInfo);
			intentProductInfo.putExtra("BUNDLE", bundle);
			startActivity(intentProductInfo);
			break;
		// ��ȫ����
		case R.id.borrow_details_activity_safe_layout:
			// setViewPagerCurrentPosition(1);
			Intent intentSaft = new Intent(BorrowDetailZXDActivity.this,ProductSafetyActivity.class);
			Bundle bundle1 = new Bundle();
			bundle1.putSerializable("PROJECT_INFO", project);
			bundle1.putSerializable("PRODUCT_INFO", productInfo);
			intentSaft.putExtra("BUNDLE", bundle1);
			startActivity(intentSaft);
			break;
		// �������
		case R.id.borrow_details_activity_certificate_layout:
			// setViewPagerCurrentPosition(2);
			Intent intentProductData = new Intent(BorrowDetailZXDActivity.this,ProductDataActivity.class);
			Bundle bundle2 = new Bundle();
			bundle2.putSerializable("PROJECT_INFO", project);
			bundle2.putSerializable("PRODUCT_INFO", productInfo);
			bundle2.putString("from_where", "dqlc");
			intentProductData.putExtra("BUNDLE", bundle2);
			startActivity(intentProductData);
			break;
		// Ͷ�ʼ�¼
		case R.id.borrow_details_activity_record_layout:
			// setViewPagerCurrentPosition(3);
			Intent intentProductRecord = new Intent(BorrowDetailZXDActivity.this,ProductRecordActivity.class);
			Bundle bundle3 = new Bundle();
			bundle3.putSerializable("PROJECT_INFO", project);
			bundle3.putSerializable("PRODUCT_INFO", productInfo);
			intentProductRecord.putExtra("BUNDLE", bundle3);
			startActivity(intentProductRecord);
			break;
		default:
			break;
		}
	}

	/**
	 * ��֤�û��Ƿ��Ѿ���֤
	 * @param type ����ֵ��,�����֡�
	 */
	private void checkIsVerify(final String type){
		bidBtn.setEnabled(false);
		RequestApis.requestIsVerify(BorrowDetailZXDActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(flag){
					//�û��Ѿ�ʵ��
					Intent intent = new Intent();
					intent.putExtra("PRODUCT_INFO", productInfo);
					intent.setClass(BorrowDetailZXDActivity.this, BidZXDActivity.class);
					startActivity(intent);
				}else{
					//�û�û��ʵ��
					Intent intent = new Intent(BorrowDetailZXDActivity.this,UserVerifyActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("type", type);
					bundle.putSerializable("PRODUCT_INFO", productInfo);
					intent.putExtra("bundle", bundle);
					startActivity(intent);
				}
				bidBtn.setEnabled(true);
			}
			@Override
			public void isSetWithdrawPwd(boolean flag, Object object) {
			}
		});
	}
	
	/**
	 * �ж��û��Ƿ��Ѿ���
	 * @param type "��ֵ����"
	 */
	private void checkIsBindCard(final String type){
		RequestApis.requestIsBinding(BorrowDetailZXDActivity.this, SettingsManager.getUserId(getApplicationContext()), "����", new OnIsBindingListener() {
			@Override
			public void isBinding(boolean flag, Object object) {
				Intent intent = new Intent();
				if(flag){
					//�û��Ѿ���
					if("���Ŵ�Ͷ��".equals(type)){
						//��ôֱ������Ͷ��ҳ��
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(BorrowDetailZXDActivity.this, BidZXDActivity.class);
					}
				}else{
					//�û���û�а�
					Bundle bundle = new Bundle();
					bundle.putString("type", type);
					bundle.putSerializable("PRODUCT_INFO", productInfo);
					intent.putExtra("bundle", bundle);
					intent.setClass(BorrowDetailZXDActivity.this, BindCardActivity.class);
				}
				startActivity(intent);
				bidBtn.setEnabled(true);
			}
		});
	}
	
	/**
	 * ��������Ĳ��ϵ�ͼƬ
	 * @param info
	 */
	private void parseProjectCailiaoMarkImg(ProjectInfo info){
		String imageNames[] = info.getImgs_name().split("\\|");
		ArrayList<ProjectCailiaoInfo> cailiaoListTemp = new ArrayList<ProjectCailiaoInfo>();
		ArrayList<ProjectCailiaoInfo> cailiaoList = new ArrayList<ProjectCailiaoInfo>();
		String materials = info.getMaterials();//�����ͼƬ
		Document doc = Jsoup.parse(materials);
		Elements ele=doc.getElementsByTag("p");
		for(Element e :ele){
			ProjectCailiaoInfo cailiaoInfo = null;
			String imageUrl = e.getElementsByTag("img").attr("src");
			String imageTitle = e.getElementsByTag("img").attr("title");
			System.out.println("ͼƬ���ӣ�"+imageUrl);
			System.out.println("ͼƬ���⣺"+imageTitle);
			if(imageUrl != null && !"".equals(imageUrl)){
				cailiaoInfo = new ProjectCailiaoInfo();
				cailiaoInfo.setImgURL(imageUrl);
				cailiaoListTemp.add(cailiaoInfo);
			}
        }
		
		for(int i=0;i<cailiaoListTemp.size();i++){
			ProjectCailiaoInfo cailiao = cailiaoListTemp.get(i);
			if(i<imageNames.length){
				cailiao.setTitle(imageNames[i]);
			}
			cailiaoList.add(cailiao);
		}
		
		if(project != null){
			project.setCailiaoMarkList(cailiaoList);
		}
	}
	
	/**
	 * ����û����Ĳ��ϵ�ͼƬ
	 * @param info
	 */
	private void parseProjectCailiaoNomarkImg(ProjectInfo info){
		String imageNames[] = info.getImgs_name().split("\\|");
		ArrayList<ProjectCailiaoInfo> cailiaoListTemp = new ArrayList<ProjectCailiaoInfo>();
		ArrayList<ProjectCailiaoInfo> cailiaoList = new ArrayList<ProjectCailiaoInfo>();
		String materials = info.getMaterials_nomark();//û�����ͼƬ
		Document doc = Jsoup.parse(materials);
		Elements ele=doc.getElementsByTag("p");
		for(Element e :ele){
			ProjectCailiaoInfo cailiaoInfo = null;
			String imageUrl = e.getElementsByTag("img").attr("src");
			String imageTitle = e.getElementsByTag("img").attr("title");
			System.out.println("ͼƬ���ӣ�"+imageUrl);
			System.out.println("ͼƬ���⣺"+imageTitle);
			if(imageUrl != null && !"".equals(imageUrl)){
				cailiaoInfo = new ProjectCailiaoInfo();
				cailiaoInfo.setImgURL(imageUrl);
				cailiaoListTemp.add(cailiaoInfo);
			}
        }
		
		for(int i=0;i<cailiaoListTemp.size();i++){
			ProjectCailiaoInfo cailiao = cailiaoListTemp.get(i);
			if(i<imageNames.length){
				cailiao.setTitle(imageNames[i]);
			}
			cailiaoList.add(cailiao);
		}
		
		if(project != null){
			project.setCailiaoNoMarkList(cailiaoList);
		}
	}

	/**
	 * ��ȡ��Ŀ����
	 * 
	 * @param id
	 */
	private void getProjectDetails(String id) {
		if (mLoadingDialog != null && !mLoadingDialog.isShowing()
				&& !isFinishing()) {
			mLoadingDialog.show();
		}
		AsyncProjectDetails task = new AsyncProjectDetails(
				BorrowDetailZXDActivity.this, id, new Inter.OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
							mLoadingDialog.dismiss();
						}
						if (baseInfo != null) {
							initTimeLimitTipsImg(baseInfo);
							project = baseInfo.getmProjectInfo();
							parseProjectCailiaoMarkImg(project);
							parseProjectCailiaoNomarkImg(project);
							if (productInfoListener != null) {
								productInfoListener.back(project);
							}
							if (productSafetyListener != null) {
								productSafetyListener.back(project);
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * ���ݲ�Ʒid��ȡ��Ʒ����
	 * 
	 * @param borrowId
	 * @param borrowStatus
	 */
	private void getProductDetailsById(String borrowId, String borrowStatus,String plan) {
		if (mLoadingDialog != null && !mLoadingDialog.isShowing()
				&& !isFinishing()) {
			mLoadingDialog.show();
		}
		AsyncProductInfo task = new AsyncProductInfo(BorrowDetailZXDActivity.this,
				borrowId, borrowStatus, plan, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								productInfo = baseInfo.getmProductInfo();
								initDataFromRecord(productInfo);
								getProjectDetails(productInfo.getProject_id());
							}else{
								mLoadingDialog.dismiss();
							}
						}else{
							mLoadingDialog.dismiss();
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
