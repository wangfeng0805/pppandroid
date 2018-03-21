package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
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
import com.ylfcf.ppp.inter.Inter.OnIsVipUserListener;
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

/**
 * VIP��Ʒ --- ��Ŀ����
 * @author Mr.liu
 *
 */
public class BorrowDetailVIPActivity extends BaseActivity implements
	OnClickListener{
	private static final String className = "BorrowDetailVIPActivity";
	private static final int REFRESH_PROGRESSBAR = 1902;
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private TextView borrowName;
	private TextView borrowRate;// �껯����
	private TextView borrowMoney;// ļ�����
	private TextView timeLimit;// ����
	private Button bidBtn;// ����Ͷ��
	private TextView repayType1;
	private TextView repayType2;
	private TextView borrowBalanceTV;
	private TextView profitTv;// 30���Ǯ�ɵ����档��
	private TextView qitouTv;//��Ͷ���
	private TextView increaseMoney;//�������
	private TextView unitLimitText;//���޵ĵ�λ
	private ProgressBar progressBar;
	// private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private ViewPager mViewPager;
	private LinearLayout introLayout, safeLayout, zizhiLayout,cjwtLayout, recordLayout;
	private RelativeLayout extraInterestLayout;
	private TextView extraInterestText;
	private ImageView jiaxiTipsImg;

	public ProductInfo productInfo;
	public InvestRecordInfo recordInfo;
	private ProjectInfo project;// ��Ŀ��Ϣ
	private OnProductInfoListener productInfoListener;
	private OnProductSafetyListener productSafetyListener;
	private AlertDialog.Builder builder = null; // �ȵõ�������
	
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
		setContentView(R.layout.borrow_details_vip_activity);
		Intent intent = getIntent();
		productInfo = (ProductInfo) intent.getSerializableExtra("PRODUCT_INFO");
		recordInfo = (InvestRecordInfo) intent
				.getSerializableExtra("InvestRecordInfo");
		builder = new AlertDialog.Builder(BorrowDetailVIPActivity.this,
				R.style.Dialog_Transparent); // �ȵõ�������
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

		borrowName = (TextView) findViewById(R.id.borrow_detail_vip_activity_borrowname);
		borrowRate = (TextView) findViewById(R.id.borrow_details_vip_activity_invest_rate);
		borrowMoney = (TextView) findViewById(R.id.borrow_details_vip_activity_invest_total_money);
		timeLimit = (TextView) findViewById(R.id.borrow_details_vip_invest_time_limit);
		bidBtn = (Button) findViewById(R.id.borrow_detail_vip_activity_bidBtn);
		bidBtn.setOnClickListener(this);
		repayType1 = (TextView) findViewById(R.id.borrow_details_vip_activity_repay_type1);
		repayType2 = (TextView) findViewById(R.id.borrow_details_vip_activity_repay_type2);
		progressBar = (ProgressBar) findViewById(R.id.borrow_details_vip_activity_pb);
		borrowBalanceTV = (TextView) findViewById(R.id.borrow_details_vip_activity_borrow_balance_text);
		profitTv = (TextView) findViewById(R.id.borrow_details_vip_activity_profit);
		qitouTv = (TextView) findViewById(R.id.borrow_detail_vip_activity_qitou);
		increaseMoney = (TextView) findViewById(R.id.borrow_details_vip_activity_increase_money);
		introLayout = (LinearLayout) findViewById(R.id.borrow_details_vip_activity_intro_layout);
		introLayout.setOnClickListener(this);
		safeLayout = (LinearLayout) findViewById(R.id.borrow_details_vip_activity_safe_layout);
		safeLayout.setOnClickListener(this);
		zizhiLayout = (LinearLayout) findViewById(R.id.borrow_details_vip_activity_certificate_layout);
		zizhiLayout.setOnClickListener(this);
		recordLayout = (LinearLayout) findViewById(R.id.borrow_details_vip_activity_record_layout);
		recordLayout.setOnClickListener(this);
		cjwtLayout = (LinearLayout) findViewById(R.id.borrow_details_vip_activity_cjwt_layout);
		cjwtLayout.setOnClickListener(this);
		extraInterestLayout = (RelativeLayout) findViewById(R.id.borrow_details_vip_extra_interest_layout);
		extraInterestText = (TextView) findViewById(R.id.borrow_details_vip_extra_interest_text);
		unitLimitText = (TextView) findViewById(R.id.borrow_details_vip_activity_unit);
		jiaxiTipsImg = (ImageView) findViewById(R.id.borrow_details_vip_activity_tips_img);
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

	int biteIntFromProduct = 0;
	private void initDataFromProductList() {
		initInvestBalance(productInfo);
		if("δ����".equals(productInfo.getMoney_status())){
			bidBtn.setEnabled(true);
			bidBtn.setText("����Ͷ��");
		}else{
			bidBtn.setEnabled(false);
			bidBtn.setText("Ͷ���ѽ���");
		}
		borrowName.setText(productInfo.getBorrow_name());

		// �껯����
		String rate = productInfo.getInterest_rate();
		String extraRate = productInfo.getAndroid_interest_rate();
		float extraRateF = 0f;
		try {
			extraRateF = Float.parseFloat(extraRate);
		} catch (Exception e) {
		}
		// Ͷ������
		String horizon = productInfo.getInvest_horizon().replace("��", "");
		double horizonInt = 0;
		try {
			horizonInt = Double.parseDouble(horizon);
		} catch (Exception e) {
			horizonInt = 0;
		}
		timeLimit.setText(horizon);

		if(SettingsManager.checkActiveStatusBySysTime(productInfo.getAdd_time(),SettingsManager.yyyJIAXIStartTime,
				SettingsManager.yyyJIAXIEndTime) == 0 && "365".equals(horizon)){
			extraInterestLayout.setVisibility(View.VISIBLE);
			extraInterestText.setVisibility(View.GONE);
			jiaxiTipsImg.setVisibility(View.VISIBLE);
		}else{
			if(extraRateF > 0){
				extraInterestLayout.setVisibility(View.VISIBLE);
				jiaxiTipsImg.setVisibility(View.GONE);
				extraInterestText.setText("+"+extraRateF);
			}else{
				extraInterestLayout.setVisibility(View.GONE);
			}
		}

//		if("vip".equals(productInfo.getBorrow_type())){
//			timeLimit.setText((int) Math.rint(horizonInt / 30) + "");
//			unitLimitText.setText("��");
//		}else{
//			timeLimit.setText(horizon);
//			unitLimitText.setText("��");
//		}
		borrowRate.setText(rate);
		int increaseMoneyInt = 0;
		double qitouMoneyD = 0;
		try {
			increaseMoneyInt = Integer.parseInt(productInfo.getUp_money());
		} catch (Exception e) {
		}
		try {
			qitouMoneyD = Double.parseDouble(productInfo.getInvest_lowest());
		} catch (Exception e) {
		}
		if(increaseMoneyInt >= 10000){
			increaseMoney.setText(increaseMoneyInt/10000+"��Ԫ");
		}else{
			increaseMoney.setText(increaseMoneyInt + "Ԫ");
		}
		
		if(qitouMoneyD >= 10000){
			qitouTv.setText((int)qitouMoneyD/10000+"��Ԫ");
		}else{
			qitouTv.setText((int)qitouMoneyD + "Ԫ");
		}
		
		repayType1.setText(productInfo.getRepay_way());
		repayType2.setText(productInfo.getRepay_way());

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
		
		profitTv.setText(new DecimalFormat("#.00").format((rateF + extraRateF) * 100/365*horizonInt*30) + "");
	}

	int biteIntFromRecord = 0;
	private void initDataFromRecord(ProductInfo info) {
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
		float extraRateF = 0f;
		try {
			extraRateF = Float.parseFloat(extraRate);
		} catch (Exception e) {
		}

		// Ͷ������
		String horizon = info.getInvest_horizon().replace("��", "");
		int horizonInt = Integer.parseInt(horizon);

		borrowRate.setText(rate);
		timeLimit.setText(horizon);
		repayType1.setText(info.getRepay_way());
		repayType2.setText(info.getRepay_way());
		increaseMoney.setText(info.getUp_money()+"Ԫ");

		Date addDate = null;
		try{
			addDate = sdf.parse(productInfo.getAdd_time());
		}catch (Exception e){

		}
		if(SettingsManager.checkActiveStatusBySysTime(productInfo.getAdd_time(),SettingsManager.yyyJIAXIStartTime,
				SettingsManager.yyyJIAXIEndTime) == 0 && "365".equals(horizon)){
			extraInterestLayout.setVisibility(View.VISIBLE);
			extraInterestText.setVisibility(View.GONE);
			jiaxiTipsImg.setVisibility(View.VISIBLE);
		}else{
			if(extraRateF > 0){
				extraInterestLayout.setVisibility(View.VISIBLE);
				jiaxiTipsImg.setVisibility(View.GONE);
				extraInterestText.setText("+"+extraRateF);
			}else{
				extraInterestLayout.setVisibility(View.GONE);
			}
		}

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
			// TODO: handle exception
		}
		profitTv.setText(new DecimalFormat("#.00").format((rateF + extraRateF) * 100/365*horizonInt*30) + "");
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
		case R.id.borrow_detail_vip_activity_bidBtn:
			// ��SettingsManager�ж�ȡ���룬���Ϊ����ζ��û�е�¼��
			boolean isLogin = !SettingsManager.getLoginPassword(
					BorrowDetailVIPActivity.this).isEmpty()
					&& !SettingsManager.getUser(BorrowDetailVIPActivity.this)
							.isEmpty();
			// isLogin = true;// ����
			Intent intent = new Intent();
			// 1������Ƿ��Ѿ���¼
			if (isLogin) {
				//���ж��Ƿ���VIP�û�
				checkIsVip();
			} else {
				// δ��¼����ת����¼ҳ��
				intent.setClass(BorrowDetailVIPActivity.this, LoginActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.common_topbar_left_layout:
			finish();
			break;
		// ��Ŀ����
		case R.id.borrow_details_vip_activity_intro_layout:
			// setViewPagerCurrentPosition(0);
			Intent intentProductInfo = new Intent(BorrowDetailVIPActivity.this,ProductInfoActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("PROJECT_INFO", project);
			bundle.putSerializable("PRODUCT_INFO", productInfo);
			intentProductInfo.putExtra("BUNDLE", bundle);
			startActivity(intentProductInfo);
			break;
		// ��ȫ����
		case R.id.borrow_details_vip_activity_safe_layout:
			// setViewPagerCurrentPosition(1);
			Intent intentSaft = new Intent(BorrowDetailVIPActivity.this,ProductSafetyActivity.class);
			Bundle bundle1 = new Bundle();
			bundle1.putSerializable("PROJECT_INFO", project);
			bundle1.putSerializable("PRODUCT_INFO", productInfo);
			intentSaft.putExtra("BUNDLE", bundle1);
			startActivity(intentSaft);
			break;
		// �������
		case R.id.borrow_details_vip_activity_certificate_layout:
			// setViewPagerCurrentPosition(2);
			Intent intentProductData = new Intent(BorrowDetailVIPActivity.this,ProductDataActivity.class);
			Bundle bundle2 = new Bundle();
			bundle2.putSerializable("PROJECT_INFO", project);
			bundle2.putSerializable("PRODUCT_INFO", productInfo);
			bundle2.putString("from_where", "vip");
			intentProductData.putExtra("BUNDLE", bundle2);
			startActivity(intentProductData);
			break;
		case R.id.borrow_details_vip_activity_cjwt_layout:
			Intent intentCJWT = new Intent(BorrowDetailVIPActivity.this,VIPProductCJWTActivity.class);
			Bundle bundle4 = new Bundle();
			bundle4.putSerializable("PROJECT_INFO", project);
			bundle4.putSerializable("PRODUCT_INFO", productInfo);
			intentCJWT.putExtra("BUNDLE", bundle4);
			startActivity(intentCJWT);
			break;
		// Ͷ�ʼ�¼
		case R.id.borrow_details_vip_activity_record_layout:
			// setViewPagerCurrentPosition(3);
			Intent intentProductRecord = new Intent(BorrowDetailVIPActivity.this,VIPProductRecordActivity.class);
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
	 * �ж��û��Ƿ�Ϊvip�û�
	 */
	private void checkIsVip(){
		RequestApis.requestIsVip(BorrowDetailVIPActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVipUserListener() {
			@Override
			public void isVip(boolean isvip) {
				if(isvip){
					checkIsVerify("VIPͶ��"); // ֻ�ж���û��ʵ���������ж��Ƿ��
				}else{
					//��VIP�û�����Ͷ��
					showCanotInvestVIPDialog();
				}
			}
		});
	}
	
	/**
	 * ��ʾ������  ��VIP�û����ܹ���Ԫ��ӯ
	 */
	private void showCanotInvestVIPDialog(){
		View contentView = LayoutInflater.from(this)
				.inflate(R.layout.borrow_details_vip_msg_dialog, null);
		final Button leftBtn = (Button) contentView
				.findViewById(R.id.borrow_details_vip_msg_dialog_leftbtn);
		final Button rightBtn = (Button) contentView.
				findViewById(R.id.borrow_details_vip_msg_dialog_rightbtn);
		final TextView msgTV = (TextView) contentView
				.findViewById(R.id.borrow_details_vip_msg_dialog_msg);
		final ImageView delBtn = (ImageView) contentView
				.findViewById(R.id.borrow_details_vip_msg_dialog_delete);
		msgTV.setText("��VIP�û����ܹ���VIP��Ʒ��~");
		builder.setView(contentView);
		builder.setCancelable(false);
		final AlertDialog dialog = builder.create();
		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				setResult(111,intent);
				dialog.dismiss();
				finish();
			}
		});
		rightBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BorrowDetailVIPActivity.this,VIPProductCJWTActivity.class);
				startActivity(intent);
			}
		});
		delBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		// ��������������ˣ���������ʾ����
		dialog.show();
		// ����dialog�Ŀ��
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = display.getWidth() * 6 / 7;
		lp.height = display.getHeight()/3;
		dialog.getWindow().setAttributes(lp);
	}
	
	/**
	 * ��֤�û��Ƿ��Ѿ���֤
	 * @param type ����ֵ��,�����֡�
	 */
	private void checkIsVerify(final String type){
		bidBtn.setEnabled(false);
		RequestApis.requestIsVerify(BorrowDetailVIPActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(flag){
					Intent intent = new Intent();
					//�û��Ѿ�ʵ��
//					checkIsBindCard(type);
					if("VIPͶ��".equals(type)){
						//��ôֱ��������ֵҳ��
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(BorrowDetailVIPActivity.this, BidVIPActivity.class);
						startActivity(intent);
						bidBtn.setEnabled(true);
					}
				}else{
					//�û�û��ʵ��
					Intent intent = new Intent(BorrowDetailVIPActivity.this,UserVerifyActivity.class);
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
		RequestApis.requestIsBinding(BorrowDetailVIPActivity.this, SettingsManager.getUserId(getApplicationContext()), "����", new OnIsBindingListener() {
			@Override
			public void isBinding(boolean flag, Object object) {
				Intent intent = new Intent();
				if(flag){
					//�û��Ѿ���
					if("VIPͶ��".equals(type)){
						//��ôֱ��������ֵҳ��
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(BorrowDetailVIPActivity.this, BidVIPActivity.class);
					}
				}else{
					//�û���û�а�
					Bundle bundle = new Bundle();
					bundle.putString("type", type);
					bundle.putSerializable("PRODUCT_INFO", productInfo);
					intent.putExtra("bundle", bundle);
					intent.setClass(BorrowDetailVIPActivity.this, BindCardActivity.class);
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
				BorrowDetailVIPActivity.this, id, new Inter.OnCommonInter() {
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
		AsyncProductInfo task = new AsyncProductInfo(BorrowDetailVIPActivity.this,
				borrowId, borrowStatus, plan, new OnCommonInter() {
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
