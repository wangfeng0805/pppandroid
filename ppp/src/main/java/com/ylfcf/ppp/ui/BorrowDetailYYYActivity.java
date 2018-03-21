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
import com.ylfcf.ppp.async.AsyncYYYProductInfo;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.ProjectCailiaoInfo;
import com.ylfcf.ppp.entity.ProjectInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnIsBindingListener;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
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
 * Ԫ��ӯ---��Ŀ����
 * @author Mr.liu
 *
 */
public class BorrowDetailYYYActivity extends BaseActivity implements
		OnClickListener {
	private static final String className = "BorrowDetailYYYActivity";
	private static final int REFRESH_PROGRESSBAR = 1902;
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private TextView borrowName;
	private TextView borrowRateMin;// �껯���� ��������
	private TextView borrowRateMax;// ��0.8���껯����
	private TextView borrowMoney;// ļ�����
	private TextView timeFrozen;// ������
	private Button bidBtn;// ����Ͷ��
	private TextView repayType1;
	private TextView borrowBalanceTV;
	private ProgressBar progressBar;
	// private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private ViewPager mViewPager;
	private LinearLayout introLayout, detailsLayout, danbaoLayout, cjwtLayout,recordLayout;
	private LinearLayout extraInterestLayout;
	private TextView extraInterestText;
	private TextView jxBtn;//��Ϣ

	public ProductInfo productInfo;
	private ProjectInfo project = new ProjectInfo();// ��Ŀ��Ϣ
	private InvestRecordInfo recordInfo = null;
	
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.borrow_details_yyy_activity);
		recordInfo = (InvestRecordInfo) getIntent().getSerializableExtra("InvestRecordInfo");
		findViews();
		if(recordInfo == null){
			getProductDetailsById("", "����", "");
		}else{
			getProductDetailsById(recordInfo.getBorrow_id(), "", "");
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

		borrowName = (TextView) findViewById(R.id.borrow_detail_yyy_activity_borrowname);
		borrowRateMin = (TextView) findViewById(R.id.borrow_details_yyy_activity_invest_minrate);
		borrowRateMax = (TextView) findViewById(R.id.borrow_details_yyy_activity_invest_maxrate);
		borrowMoney = (TextView) findViewById(R.id.borrow_details_yyy_activity_invest_total_money);
		timeFrozen = (TextView) findViewById(R.id.borrow_details_yyy_invest_time_frozen);
		bidBtn = (Button) findViewById(R.id.borrow_detail_yyy_activity_bidBtn);
		bidBtn.setOnClickListener(this);
		repayType1 = (TextView) findViewById(R.id.borrow_details_yyy_activity_repay_type1);
		progressBar = (ProgressBar) findViewById(R.id.borrow_details_yyy_activity_pb);
		borrowBalanceTV = (TextView) findViewById(R.id.borrow_details_yyy_activity_borrow_balance_text);

		introLayout = (LinearLayout) findViewById(R.id.borrow_details_yyy_activity_intro_layout);
		introLayout.setOnClickListener(this);
		detailsLayout = (LinearLayout) findViewById(R.id.borrow_details_yyy_activity_details_layout);
		detailsLayout.setOnClickListener(this);
		danbaoLayout = (LinearLayout) findViewById(R.id.borrow_details_yyy_activity_certificate_layout);
		danbaoLayout.setOnClickListener(this);
		cjwtLayout = (LinearLayout) findViewById(R.id.borrow_details_yyy_activity_cjwt_layout);
		cjwtLayout.setOnClickListener(this);
		recordLayout = (LinearLayout) findViewById(R.id.borrow_details_yyy_activity_record_layout);
		recordLayout.setOnClickListener(this);
		extraInterestLayout = (LinearLayout) findViewById(R.id.borrow_details_yyy_extra_interest_layout);
		extraInterestText = (TextView) findViewById(R.id.borrow_details_yyy_extra_interest_text);
		jxBtn = (TextView) findViewById(R.id.borrow_details_yyy_activity_jxbtn);
		jxBtn.setOnClickListener(this);
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

	int biteIntFromRecord = 0;
	private void initData(ProductInfo info) {
		initInvestBalance(info);
		productInfo = info;
		if("δ����".equals(info.getMoney_status())){
			bidBtn.setEnabled(true);
			bidBtn.setText("����Ͷ��");
		}else{
			bidBtn.setEnabled(false);
			bidBtn.setText("Ͷ���ѽ���");
		}
		borrowName.setText(info.getBorrow_name());

		// �껯����
		String rate = info.getInterest_rate();
		String extraRate = productInfo.getAndroid_interest_rate();
		double rateF = 0f;
		try {
			rateF = Double.parseDouble(rate);
		} catch (Exception e) {
		}

		if(SettingsManager.checkActiveStatusBySysTime(productInfo.getAdd_time(),
				SettingsManager.yyyJIAXIStartTime,SettingsManager.yyyJIAXIEndTime) == 0){
			extraInterestLayout.setVisibility(View.VISIBLE);
		}else{
			extraInterestLayout.setVisibility(View.GONE);
		}
		// ������
		String frozenPeriod = info.getFrozen_period();
		double rateD = 0d;
		try {
			rateD = Double.parseDouble(productInfo.getInterest_rate());
			if((int)(rateD * 10)%10 == 0){
				//˵��������������û��С��
				borrowRateMin.setText((int)rateD + "");
			}else{
				borrowRateMin.setText(Util.double2PointDoubleOne(rateD));
			}
		} catch (Exception e) {
			borrowRateMin.setText(productInfo.getInterest_rate());
		}
		
		try {
			if((int)((rateD + 0.8)* 10)%10 == 0){
				//˵��������������û��С��
				borrowRateMax.setText((int)(rateD + 0.8) + "");
			}else{
				borrowRateMax.setText(Util.double2PointDoubleOne(rateD + 0.8));
			}
		} catch (Exception e) {
			borrowRateMax.setText(String.valueOf(Util.double2PointDouble(rateF + 0.80)));
		}
		timeFrozen.setText(frozenPeriod);
		repayType1.setText(frozenPeriod+"��������");

		double totalMoneyD = 0d;
		double investedMoneyD = 0d;//��Ͷ�ʵ�Ǯ
		int totalMoneyI = 0;
		int investedMoneyI = 0; 
		try {
			totalMoneyD = Double.parseDouble(info.getTotal_money());
			totalMoneyI = (int) totalMoneyD;
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
		//ע��int���͵ķ�Χ
		try {
			if(investedMoneyI > 10000){
				biteFloat = (investedMoneyI/100)/(totalMoneyI/10000);
			}else{
				biteFloat = investedMoneyI*100/totalMoneyI;
			}
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
		case R.id.borrow_detail_yyy_activity_bidBtn:
			// ��SettingsManager�ж�ȡ���룬���Ϊ����ζ��û�е�¼��
			boolean isLogin = !SettingsManager.getLoginPassword(
					BorrowDetailYYYActivity.this).isEmpty()
					&& !SettingsManager.getUser(BorrowDetailYYYActivity.this)
							.isEmpty();
			// isLogin = true;// ����
			Intent intent = new Intent();
			// 1������Ƿ��Ѿ���¼
			if (isLogin) {
				//�ж��Ƿ�ʵ����
				checkIsVerify("Ͷ��"); //�ڱ������ҳ��ֻ�ж��Ƿ�ʵ�������ж���û�а�
			} else {
				// δ��¼����ת����¼ҳ��
				intent.setClass(BorrowDetailYYYActivity.this, LoginActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.common_topbar_left_layout:
			finish();
			break;
		// Ԫ��ӯ���ܽ���
		case R.id.borrow_details_yyy_activity_intro_layout:
			// setViewPagerCurrentPosition(0);
			Intent intentProductInfo = new Intent(BorrowDetailYYYActivity.this,ProductIntroActivity.class);
			Bundle bundleIntro = new Bundle();
			bundleIntro.putSerializable("PRODUCT_INFO", productInfo);
			bundleIntro.putString("from_where", "yyy");
			intentProductInfo.putExtra("BUNDLE", bundleIntro);
			startActivity(intentProductInfo);
			break;
		// ��Ʒ����
		case R.id.borrow_details_yyy_activity_details_layout:
			// setViewPagerCurrentPosition(1);
			Intent intentDetail = new Intent(BorrowDetailYYYActivity.this,YYYProductDetailActivity.class);
			Bundle bundle1 = new Bundle();
			bundle1.putSerializable("PRODUCT_INFO", productInfo);
			intentDetail.putExtra("BUNDLE", bundle1);
			startActivity(intentDetail);
			break;
		// �������
		case R.id.borrow_details_yyy_activity_certificate_layout:
			Intent intentProductData = new Intent(BorrowDetailYYYActivity.this,YYYProductDataActivity.class);
			Bundle bundle2 = new Bundle();
			bundle2.putSerializable("PRODUCT_INFO", productInfo);
			bundle2.putSerializable("PROJECT_INFO", project);
			intentProductData.putExtra("BUNDLE", bundle2);
			startActivity(intentProductData);
			break;
		//��������
		case R.id.borrow_details_yyy_activity_cjwt_layout:
			Intent intentCJWT = new Intent(BorrowDetailYYYActivity.this,YYYProductCJWTActivity.class);
			intentCJWT.putExtra("PRODUCT_INFO", productInfo);
			intentCJWT.putExtra("from_where", "yyy");
			startActivity(intentCJWT);
			break;
		// Ͷ�ʼ�¼
		case R.id.borrow_details_yyy_activity_record_layout:
			Intent intentProductRecord = new Intent(BorrowDetailYYYActivity.this,YYYProductRecordActivity.class);
			Bundle bundle3 = new Bundle();
			bundle3.putSerializable("PRODUCT_INFO", productInfo);
			intentProductRecord.putExtra("BUNDLE", bundle3);
			startActivity(intentProductRecord);
			break;
		case R.id.borrow_details_yyy_activity_jxbtn:
			//��ת��Ԫ��ӯ��Ϣ�banner
			Intent intentBanner = new Intent(BorrowDetailYYYActivity.this,BannerTopicActivity.class);
			BannerInfo bannerInfo = new BannerInfo();
			bannerInfo.setArticle_id("70");
			bannerInfo.setLink_url("http://wap.ylfcf.com/home/index/yyyjx.html#app");
			intentBanner.putExtra("BannerInfo", bannerInfo);
			startActivity(intentBanner);
			finish();
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
		RequestApis.requestIsVerify(BorrowDetailYYYActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(flag){
					Intent intent = new Intent();
						//��ôֱ��������ֵҳ��
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(BorrowDetailYYYActivity.this, BidYYYActivity.class);
						startActivity(intent);
						bidBtn.setEnabled(true);
				}else{
					//�û�û��ʵ��
					Intent intent = new Intent(BorrowDetailYYYActivity.this,UserVerifyActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("type", type);
					bundle.putSerializable("PRODUCT_INFO", productInfo);
					intent.putExtra("bundle", bundle);
					startActivity(intent);
					bidBtn.setEnabled(true);
				}
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
		RequestApis.requestIsBinding(BorrowDetailYYYActivity.this, SettingsManager.getUserId(getApplicationContext()), "����", new OnIsBindingListener() {
			@Override
			public void isBinding(boolean flag, Object object) {
				Intent intent = new Intent();
				if(flag){
					//�û��Ѿ���
					if("Ԫ��ӯͶ��".equals(type)){
						//��ôֱ��������ֵҳ��
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(BorrowDetailYYYActivity.this, BidYYYActivity.class);
					}
				}else{
					//�û���û�а�
					Bundle bundle = new Bundle();
					bundle.putString("type", type);
					bundle.putSerializable("PRODUCT_INFO", productInfo);
					intent.putExtra("bundle", bundle);
					intent.setClass(BorrowDetailYYYActivity.this, BindCardActivity.class);
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
	private void parseProductCailiaoMarkImg(ProductInfo info){
		String imageNames[] = info.getImgs_name().split("\\|");
		ArrayList<ProjectCailiaoInfo> cailiaoListTemp = new ArrayList<ProjectCailiaoInfo>();
		ArrayList<ProjectCailiaoInfo> cailiaoList = new ArrayList<ProjectCailiaoInfo>();
		String materials = info.getMaterials().replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&quot;", "\"");//�����ͼƬ
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
	private void parseProductCailiaoNomarkImg(ProductInfo info){
		String imageNames[] = info.getImgs_name().split("\\|");
		ArrayList<ProjectCailiaoInfo> cailiaoListTemp = new ArrayList<ProjectCailiaoInfo>();
		ArrayList<ProjectCailiaoInfo> cailiaoList = new ArrayList<ProjectCailiaoInfo>();
		String materials = info.getMaterials_nomark().replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&quot;", "\"");//û�����ͼƬ
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
	 * ���ݲ�Ʒid��ȡ��Ʒ����
	 * 
	 * @param borrowStatus
	 */
	private void getProductDetailsById(String id, String borrowStatus,String moneyStatus) {
		if (mLoadingDialog != null && !mLoadingDialog.isShowing()
				&& !isFinishing()) {
			mLoadingDialog.show();
		}
		AsyncYYYProductInfo task = new AsyncYYYProductInfo(BorrowDetailYYYActivity.this,
				id, borrowStatus, moneyStatus, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(mLoadingDialog != null){
							mLoadingDialog.dismiss();
						}
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								productInfo = baseInfo.getProductPageInfo().getProductList().get(0);
								productInfo.setBorrow_type("Ԫ��ӯ");
								initData(productInfo);
								parseProductCailiaoNomarkImg(productInfo);
								parseProductCailiaoMarkImg(productInfo);
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
