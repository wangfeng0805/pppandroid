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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncProjectDetails;
import com.ylfcf.ppp.async.AsyncWDYBorrowDetail;
import com.ylfcf.ppp.async.AsyncWDYSelectone;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.ProjectCailiaoInfo;
import com.ylfcf.ppp.entity.ProjectInfo;
import com.ylfcf.ppp.fragment.ProductInfoFragment.OnProductInfoListener;
import com.ylfcf.ppp.fragment.ProductSafetyFragment.OnProductSafetyListener;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * �ȶ�ӯ����ҳ��
 * @author Mr.liu
 *
 */
public class BorrowDetailWDYActivity extends BaseActivity implements OnClickListener{
	private static final String className = "BorrowDetailWDYActivity";
	private static final int REFRESH_PROGRESSBAR = 1902;
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private TextView borrowName;
	private TextView borrowRate;// �껯���� �������ʵ���С����
	private TextView borrowMoney;// ļ�����
	private TextView timeLimit;// ����
	private Button bidBtn;// ����Ͷ��
	private TextView repayType1;
	private TextView borrowBalanceTV;
	private TextView qitouMoneyTv;
//	private TextView reInvestDay;//ÿ��Ͷ����
	private ProgressBar progressBar;
	// private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private ViewPager mViewPager;
	private LinearLayout introLayout, detailsLayout, cailiaoLayout, quesLayout,investRecordLayout;
	private LinearLayout extraInterestLayout;//��Ϣ����
	private TextView extraInterestText;

	public InvestRecordInfo recordInfo;
	private ProductInfo mProductInfo;
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.borrow_detail_wdy_activity);
		Intent intent = getIntent();
		recordInfo = (InvestRecordInfo) intent
				.getSerializableExtra("InvestRecordInfo");
		findViews();
		if (recordInfo != null) {
			//��Ͷ�ʼ�¼��ת����
			getWDYDetailById(recordInfo.getBorrow_id());
		} else {
			// ����ҳ��ת������
			getWDYBorrowDetails("����", "��");
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

		borrowName = (TextView) findViewById(R.id.borrow_detail_wdy_activity_borrowname);
		borrowRate = (TextView) findViewById(R.id.borrow_details_wdy_activity_invest_rate);
		borrowMoney = (TextView) findViewById(R.id.borrow_details_wdy_activity_invest_total_money);
		timeLimit = (TextView) findViewById(R.id.borrow_details_wdy_invest_time_limit);
		bidBtn = (Button) findViewById(R.id.borrow_detail_wdy_activity_bidBtn);
		bidBtn.setOnClickListener(this);
		repayType1 = (TextView) findViewById(R.id.borrow_details_wdy_activity_repay_type1);
		progressBar = (ProgressBar) findViewById(R.id.borrow_details_wdy_activity_pb);
		borrowBalanceTV = (TextView) findViewById(R.id.borrow_details_wdy_activity_borrow_balance_text);
		qitouMoneyTv = (TextView)findViewById(R.id.borrow_detail_wdy_activity_qitou);
//		reInvestDay = (TextView) findViewById(R.id.borrow_details_wdy_activity_reinvest_date);

		introLayout = (LinearLayout) findViewById(R.id.borrow_details_wdy_activity_intro_layout);
		introLayout.setOnClickListener(this);
		detailsLayout = (LinearLayout) findViewById(R.id.borrow_details_wdy_activity_details_layout);
		detailsLayout.setOnClickListener(this);
		cailiaoLayout = (LinearLayout) findViewById(R.id.borrow_details_wdy_activity_certificate_layout);
		cailiaoLayout.setOnClickListener(this);
		quesLayout = (LinearLayout) findViewById(R.id.borrow_details_wdy_activity_ques_layout);
		quesLayout.setOnClickListener(this);
		investRecordLayout = (LinearLayout) findViewById(R.id.borrow_details_wdy_activity_record_layout);
		investRecordLayout.setOnClickListener(this);
		extraInterestLayout = (LinearLayout) findViewById(R.id.borrow_details_wdy_extra_interest_layout);
		extraInterestText = (TextView) findViewById(R.id.borrow_details_wdy_extra_interest_text);
	}

	int biteIntFromProduct = 0;
	private void initData() {
		if(mProductInfo == null){
			return;
		}
		initInvestBalance(mProductInfo);
		if("δ����".equals(mProductInfo.getMoney_status())){
			bidBtn.setEnabled(true);
			bidBtn.setText("����Ͷ��");
		}else{
			bidBtn.setEnabled(false);
			bidBtn.setText("Ͷ���ѽ���"); 
		}
		if(mProductInfo.getBorrow_name() != null && !"".equals(mProductInfo.getBorrow_name())){
			borrowName.setText(mProductInfo.getBorrow_name());
		}else{
			borrowName.setText("нӯ�ƻ�-"+mProductInfo.getBorrow_period()+"��");
		}
		
		// �껯����
		double rateD = 0d;
		try {
			rateD = Double.parseDouble(mProductInfo.getInterest_rate());
			if((int)(rateD * 10)%10 == 0){
				//˵��������������û��С��
				borrowRate.setText((int)rateD + "");
			}else{
				borrowRate.setText(Util.double2PointDoubleOne(rateD));
			}
		} catch (Exception e) {
			borrowRate.setText(mProductInfo.getInterest_rate());
		}
		String extraRate = mProductInfo.getAndroid_interest_rate();
		float extraRateF = 0f;
		try {
			extraRateF = Float.parseFloat(extraRate);
		} catch (Exception e) {
		}
		if(extraRateF > 0){
			extraInterestLayout.setVisibility(View.VISIBLE);
			extraInterestText.setText("+"+extraRateF);
		}else{
			extraInterestLayout.setVisibility(View.GONE);
		}
		timeLimit.setText(mProductInfo.getInterest_period_month());
		repayType1.setText(mProductInfo.getRepay_way());
		double investLowestD = 0d;
		double investHighestD = 0d;
		try {
			investLowestD = Double.parseDouble(mProductInfo.getInvest_lowest());
			investHighestD = Double.parseDouble(mProductInfo.getInvest_highest());
		} catch (Exception e) {
		}
		qitouMoneyTv.setText((int)investLowestD+" - " + (int)investHighestD + "Ԫ��ע���״μ���ʱȷ���������·ݽ����ɸ��ģ�");
//		reInvestDay.setText(getResources().getString(R.string.wdy_invest_day).replace("DAY", mProductInfo.getInvest_day()));
		double totalMoneyL = 0d;
		int totalMoneyI = 0;
		try {
			totalMoneyL = Double.parseDouble(mProductInfo.getTotal_money());
			totalMoneyI = (int) totalMoneyL;
		} catch (Exception e) {
		}
		if(totalMoneyI < 10000){
			borrowMoney.setText(Util.double2PointDoubleOne(totalMoneyI / 10000d) + "");
		}else{
			borrowMoney.setText((totalMoneyI / 10000) + "");
		}
		float investMoneyF = 0f;
		float totalMoneyF = 0f;
		float biteFloat = 0f;
		try {
			investMoneyF = Float.parseFloat(mProductInfo.getInvest_money());
			totalMoneyF = Float.parseFloat(mProductInfo.getTotal_money());
			biteFloat = (investMoneyF/totalMoneyF) * 100;
			biteIntFromProduct = (int) biteFloat;
		} catch (Exception e) {
		}
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				progressbarIncrease(biteIntFromProduct*100);
			}
		}, 500L);
	}

	int biteIntFromRecord = 0;
	private void initDataFromRecord(ProductInfo info) {
		initInvestBalance(info);
		mProductInfo = info;
		if("δ����".equals(info.getMoney_status())){
			bidBtn.setEnabled(true);
			bidBtn.setText("����Ͷ��");
		}else{
			bidBtn.setEnabled(false);
			bidBtn.setText("Ͷ���ѽ���");
		}
		if(mProductInfo.getBorrow_name() != null && !"".equals(mProductInfo.getBorrow_name())){
			borrowName.setText(mProductInfo.getBorrow_name());
		}else{
			borrowName.setText("нӯ�ƻ�-"+mProductInfo.getBorrow_period()+"��");
		}

		// �껯����
		double rateD = 0d;
		try {
			rateD = Double.parseDouble(mProductInfo.getInterest_rate());
			if((int)(rateD * 10)%10 == 0){
				//˵��������������û��С��
				borrowRate.setText((int)rateD + "");
			}else{
				borrowRate.setText(Util.double2PointDoubleOne(rateD));
			}
		} catch (Exception e) {
			borrowRate.setText(mProductInfo.getInterest_rate());
		}
		String extraRate = mProductInfo.getAndroid_interest_rate();
		float extraRateF = 0f;
		try {
			extraRateF = Float.parseFloat(extraRate);
		} catch (Exception e) {
		}
		if(extraRateF > 0){
			extraInterestLayout.setVisibility(View.VISIBLE);
			extraInterestText.setText("+"+extraRateF);
		}else{
			extraInterestLayout.setVisibility(View.GONE);
		}
		timeLimit.setText(mProductInfo.getInterest_period_month());
		repayType1.setText(info.getRepay_way());
		
		double investLowestD = 0d;
		double investHighestD = 0d;
		try {
			investLowestD = Double.parseDouble(mProductInfo.getInvest_lowest());
			investHighestD = Double.parseDouble(mProductInfo.getInvest_highest());
		} catch (Exception e) {
		}
		qitouMoneyTv.setText((int)investLowestD + " - "+(int)investHighestD+"Ԫ��ע���״μ���ʱȷ���������·ݽ����ɸ��ģ�");
//		reInvestDay.setText(getResources().getString(R.string.wdy_invest_day).replace("DAY", mProductInfo.getInvest_day()));
		
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
		case R.id.borrow_detail_wdy_activity_bidBtn:
			// ��SettingsManager�ж�ȡ���룬���Ϊ����ζ��û�е�¼��
			boolean isLogin = !SettingsManager.getLoginPassword(
					BorrowDetailWDYActivity.this).isEmpty()
					&& !SettingsManager.getUser(BorrowDetailWDYActivity.this)
							.isEmpty();
			// 1������Ƿ��Ѿ���¼
			if (isLogin) {
				//�ж��Ƿ�ʵ����
				checkIsVerify("Ͷ��");
			} else {
				// δ��¼����ת����¼ҳ��
				Intent intent = new Intent();
				intent.setClass(BorrowDetailWDYActivity.this, LoginActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.common_topbar_left_layout:
			finish();
			break;
		// ��Ŀ����
		case R.id.borrow_details_wdy_activity_intro_layout:
			Intent intentProductInfo = new Intent(BorrowDetailWDYActivity.this,ProductIntroActivity.class);
			Bundle bundleIntro = new Bundle();
			bundleIntro.putSerializable("PRODUCT_INFO", mProductInfo);
			bundleIntro.putString("from_where", "wdy");
			intentProductInfo.putExtra("BUNDLE", bundleIntro);
			startActivity(intentProductInfo);
			break;
		// ��Ʒ����
		case R.id.borrow_details_wdy_activity_details_layout:
			Intent intentSaft = new Intent(BorrowDetailWDYActivity.this,WDYProductDetailActivity.class);
			Bundle bundle1 = new Bundle();
			bundle1.putSerializable("PROJECT_INFO", project);
			bundle1.putSerializable("PRODUCT_INFO", mProductInfo);
			intentSaft.putExtra("BUNDLE", bundle1);
			startActivity(intentSaft);
			break;
		// �������
		case R.id.borrow_details_wdy_activity_certificate_layout:
			Intent intentProductData = new Intent(BorrowDetailWDYActivity.this,ProductDataActivity.class);
			Bundle bundle2 = new Bundle();
			bundle2.putSerializable("PROJECT_INFO", project);
			bundle2.putSerializable("PRODUCT_INFO", mProductInfo);
			bundle2.putString("from_where", "wdy");
			intentProductData.putExtra("BUNDLE", bundle2);
			startActivity(intentProductData);
			break;
		// ��������
		case R.id.borrow_details_wdy_activity_ques_layout:
			Intent intentCJWT = new Intent(BorrowDetailWDYActivity.this,YYYProductCJWTActivity.class);
			intentCJWT.putExtra("from_where", "wdy");
			intentCJWT.putExtra("PRODUCT_INFO", mProductInfo);
			startActivity(intentCJWT);
			break;
		case R.id.borrow_details_wdy_activity_record_layout:
			Intent intentProductRecord = new Intent(BorrowDetailWDYActivity.this,YYYProductRecordActivity.class);
			Bundle bundle3 = new Bundle();
			bundle3.putSerializable("PRODUCT_INFO", mProductInfo);
			intentProductRecord.putExtra("BUNDLE", bundle3);
			startActivity(intentProductRecord);
			break;
		default:
			break;
		}
	}
	
	/**
	 * ��������Ĳ��ϵ�ͼƬ
	 * @param info
	 */
	private void parseProjectCailiaoMarkImg(ProjectInfo info){
		if(info == null)
			return;
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
		if(info == null)
			return;
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
	 * ��֤�û��Ƿ��Ѿ���֤
	 * @param type ����ֵ��,�����֡�
	 */
	private void checkIsVerify(final String type){
		bidBtn.setEnabled(false);
		RequestApis.requestIsVerify(BorrowDetailWDYActivity.this, SettingsManager.getUserId(getApplicationContext()), new Inter.OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(flag){
					//�û��Ѿ�ʵ��
					Intent intent = new Intent();
					intent.putExtra("PRODUCT_INFO", mProductInfo);
					intent.setClass(BorrowDetailWDYActivity.this, BidWDYActivity.class);
					startActivity(intent);
				}else{
					//�û�û��ʵ��
					Intent intent = new Intent(BorrowDetailWDYActivity.this,UserVerifyActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("type", type);
					bundle.putSerializable("PRODUCT_INFO", mProductInfo);
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
				BorrowDetailWDYActivity.this, id, new Inter.OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
							mLoadingDialog.dismiss();
						}
						if (baseInfo != null) {
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
	 * ��ȡ����һ�ڵ��ȶ�Ӯ��Ʒ����
	 * @param borrowStatus
	 * @param isShow
	 */
	private void getWDYBorrowDetails(String borrowStatus,String isShow){
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		AsyncWDYBorrowDetail wdyTask = new AsyncWDYBorrowDetail(BorrowDetailWDYActivity.this, borrowStatus, isShow, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(mLoadingDialog != null && mLoadingDialog.isShowing()){
							mLoadingDialog.dismiss();
						}
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								try {
									ProductInfo info = baseInfo.getProductPageInfo().getProductList().get(0);
									mProductInfo = info;
									getProjectDetails(info.getProject_id());
									initData();
								} catch (Exception e) {
								}
								
							}
						}
					}
				});
		wdyTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * ���ݲ�Ʒid��ȡ��Ʒ����
	 * 
	 * @param borrowId
	 */
	private void getWDYDetailById(String borrowId) {
		if (mLoadingDialog != null && !mLoadingDialog.isShowing()
				&& !isFinishing()) {
			mLoadingDialog.show();
		}
		AsyncWDYSelectone task = new AsyncWDYSelectone(BorrowDetailWDYActivity.this,
				borrowId, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								ProductInfo info = baseInfo.getmProductInfo();
								initDataFromRecord(info);
								getProjectDetails(info.getProject_id());
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
