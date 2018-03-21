package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncAppointBorrowInvest;
import com.ylfcf.ppp.async.AsyncCurrentUserRedbagList;
import com.ylfcf.ppp.async.AsyncJXQPageInfo;
import com.ylfcf.ppp.async.AsyncYiLianRMBAccount;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.JiaxiquanInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.RedBagInfo;
import com.ylfcf.ppp.entity.RedBagPageInfo;
import com.ylfcf.ppp.entity.UserRMBAccountInfo;
import com.ylfcf.ppp.inter.Inter.OnBorrowInvestInter;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnIsBindingListener;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.ui.BidZXDActivity.OnHBWindowItemClickListener;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.view.HBListPopupwindow;
import com.ylfcf.ppp.view.JXQListPopupwindow;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ˽�������Ͷ��ҳ��
 * @author Mr.liu
 */
public class BidSRZXActivity extends BaseActivity implements OnClickListener{
	private static final String className = "BidSRZXActivity";
	private static final int REQUEST_INVEST_WHAT = 1201;
	private static final int REQUEST_INVEST_SUCCESS = 1202;
	private static final int REQUEST_INVEST_EXCEPTION = 1203;
	private static final int REQUEST_INVEST_FAILE = 1204;

	private static final int REQUEST_INVEST_INCREASE = 1205;
	private static final int REQUEST_INVEST_DESCEND = 1206;

	//��Ϣȯ
	private static final int REQUEST_JXQ_LIST_WHAT = 1207;
	private static final int REQUEST_JXQ_LIST_SUCCESS = 1208;

	private LinearLayout topLeftBtn;
	private TextView topTitleTV, borrowName;
	private TextView userBalanceTV;// �û��������
	private TextView borrowBalanceTV;// ���ʣ���Ͷ���
	private Button rechargeBtn;// ��ֵ
	private TextView usePrompt;//ʹ��˵������Ϣȯ��Ԫ��Ҳ���ͬʱʹ�õ�
	private TextView daojuUsePrompt;

	private Button descendBtn;// �ݼ���ť
	private Button increaseBtn;// ������ť
	private EditText investMoneyET;
	private ImageView deleteImg;// x��
	private TextView yjsyText;// Ԥ������
	private CheckBox compactCB;
	private TextView vipCompact;//VIP���Э��
	private LinearLayout hbLayout;//�������
	private EditText hbET;//��������
	private EditText jxqEditText;
	private RelativeLayout hbArrowLayout;
	private View line1,line2,line3;
	private LinearLayout jxqLayout;// ��Ϣȯ
	private RelativeLayout jxqArrowLayout;// ��Ϣȯ�ļ�ͷ

	private ProductInfo mProductInfo;
	private int moneyInvest = 0;
	private double hbMoneyD = 0;
    private double jxqMoneyD = 0;
	private Button investBtn;
	private List<RedBagInfo> hbList = new ArrayList<RedBagInfo>();// �û�δʹ�õĺ���б�
	private List<JiaxiquanInfo> jxqList = new ArrayList<JiaxiquanInfo>();//��ʹ�õļ�Ϣȯ�б�
	private List<String> usePromptList = new ArrayList<String>();//

	private LinearLayout mainLayout;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_INVEST_WHAT:
				requestInvest(mProductInfo.getId(),
						SettingsManager.getUserId(getApplicationContext()),
						String.valueOf(moneyInvest), 
						SettingsManager.USER_FROM,String.valueOf(hbET.getTag()),String.valueOf(jxqEditText.getTag()));
				break;
			case REQUEST_INVEST_SUCCESS:
				BaseInfo baseInfo = (BaseInfo) msg.obj;
				Intent intentSuccess = new Intent(BidSRZXActivity.this,
						BidSuccessActivity.class);
				intentSuccess.putExtra("from_where", "˽������");
				intentSuccess.putExtra("base_info",baseInfo);
				startActivity(intentSuccess);
				mApp.finishAllActivityExceptMain();
				break;
			case REQUEST_INVEST_EXCEPTION:
				BaseInfo base = (BaseInfo) msg.obj;
				Util.toastShort(BidSRZXActivity.this, base.getMsg());
				break;
			case REQUEST_INVEST_FAILE:
				Util.toastShort(BidSRZXActivity.this, "�����쳣");
				break;
			case REQUEST_INVEST_INCREASE:
				investMoneyIncrease();
				break;
			case REQUEST_INVEST_DESCEND:
				investMoneyDescend();
				break;
			case REQUEST_JXQ_LIST_WHAT:
				requestJXQList(SettingsManager.getUserId(getApplicationContext()), "δʹ��");
				break;
				case REQUEST_JXQ_LIST_SUCCESS:
					Date endDate = null;
					BaseInfo baseInfo1 = (BaseInfo) msg.obj;
					List<JiaxiquanInfo> jiaxiList = baseInfo1.getmJiaxiquanPageInfo().getInfoList();
					for(int i=0;i<jiaxiList.size();i++){
						JiaxiquanInfo info = jiaxiList.get(i);
						if("δʹ��".equals(info.getUse_status()) && info.getBorrow_type().contains("˽������")){
							try {
								endDate = sdf.parse(info.getEffective_end_time());
								if(endDate.compareTo(sdf.parse(baseInfo1.getTime())) == 1 && "0".equals(info.getTransfer())){
									//��ʾ��Ϣȯ��δ���� ,����ʹ����ת�õļ�Ϣȯ
									jxqList.add(info);
								}
							} catch (Exception e) {
							}
						}
					}
					if(jxqList.size() > 0){
						jxqLayoutVisible(View.VISIBLE);
						usePromptList.add("��Ϣȯ");
						updateUsePrompt(usePromptList);
					}
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
		setContentView(R.layout.bid_srzx_activity);

		mProductInfo = (ProductInfo) getIntent().getSerializableExtra(
				"PRODUCT_INFO");
		if(mProductInfo != null){
			requestHBPageInfoByBorrowType(
					SettingsManager.getUserId(getApplicationContext()),"˽������");
		}
		findViews();
		initInvestBalance(mProductInfo);
		handler.sendEmptyMessageDelayed(REQUEST_JXQ_LIST_WHAT,1100L);
	}

	@Override
	protected void onResume() {
		super.onResume();
		UMengStatistics.statisticsOnPageStart(className);//����ͳ��ҳ����ת
		UMengStatistics.statisticsResume(this);//����ͳ��ʱ��
		requestUserAccountInfo(SettingsManager
				.getUserId(getApplicationContext()));
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

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("Ͷ��");

		daojuUsePrompt = (TextView) findViewById(R.id.bid_srzx_activity_daoju_prompt);
		usePrompt = (TextView) findViewById(R.id.bid_srzx_activity_use_prompt);
		borrowName = (TextView) findViewById(R.id.bid_srzx_activity_borrow_name);
		borrowName.setText(mProductInfo.getBorrow_name());
		userBalanceTV = (TextView) findViewById(R.id.bid_srzx_activity_user_balance);
		borrowBalanceTV = (TextView) findViewById(R.id.bid_srzx_activity_borrow_balance);
		rechargeBtn = (Button) findViewById(R.id.bid_srzx_activity_recharge_btn);
		rechargeBtn.setOnClickListener(this);
		descendBtn = (Button) findViewById(R.id.bid_srzx_activity_discend_btn);
		descendBtn.setOnClickListener(this);
		descendBtn.setOnTouchListener(mOnTouchListener);
		increaseBtn = (Button) findViewById(R.id.bid_srzx_activity_increase_btn);
		increaseBtn.setOnClickListener(this);
		increaseBtn.setOnTouchListener(mOnTouchListener);
		compactCB = (CheckBox) findViewById(R.id.bid_srzx_activity_cb);
		vipCompact = (TextView) findViewById(R.id.bid_srzx_activity_compact_text);
		vipCompact.setOnClickListener(this);
		investMoneyET = (EditText) findViewById(R.id.bid_srzx_activity_invest_et);
		investMoneyET.addTextChangedListener(watcherInvestMoney);
		investMoneyET.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
					return;
				String investMoneyStr = investMoneyET.getText().toString();
				int investMoney = 0;
				double borrowBalanceDouble = 0d;
				try {
					// �ж�Ͷ�ʽ���Ƿ�Ϊ100��������
					investMoney = Integer.parseInt(investMoneyStr);
					// �ж�Ͷ�ʽ���Ƿ���ڱ����ʣ���
					String borrowBalanceStr = String.valueOf(borrowBalanceTemp);
					borrowBalanceDouble = Double.parseDouble(borrowBalanceStr);
					if (investMoney > borrowBalanceDouble) {
						Util.toastLong(BidSRZXActivity.this, "���ʣ���Ͷ����");
					}else if(investMoney < borrowBalanceDouble){
						Util.toastLong(BidSRZXActivity.this, "Ͷ�����С����Ͷ���");
					}
				} catch (Exception e) {
				}
			}
		});
		deleteImg = (ImageView) findViewById(R.id.bid_srzx_activity_delete);
		deleteImg.setOnClickListener(this);
		yjsyText = (TextView) findViewById(R.id.bid_srzx_activity_yjsy);
		investBtn = (Button) findViewById(R.id.bid_srzx_activity_borrow_bidBtn);
		investBtn.setOnClickListener(this);
		mainLayout = (LinearLayout) findViewById(R.id.bid_srzx_activity_mainlayout);
		
		hbLayout = (LinearLayout) findViewById(R.id.bid_srzx_activity_hb_layout);
		hbLayout.setOnClickListener(this);
		hbET = (EditText) findViewById(R.id.bid_srzx_activity_hb_et);
		hbET.setOnClickListener(this);
		hbArrowLayout = (RelativeLayout) findViewById(R.id.bid_srzx_activity_hb_arrow_layout);
		hbArrowLayout.setOnClickListener(this);
		line1 = findViewById(R.id.bid_srzx_activity_line1);
		line2 = findViewById(R.id.bid_srzx_activity_line2);
		line3 = findViewById(R.id.bid_srzx_activity_line3);
		jxqLayout = (LinearLayout) findViewById(R.id.bid_srzx_activity_jxq_layout);
		jxqLayout.setOnClickListener(this);
		jxqArrowLayout = (RelativeLayout) findViewById(R.id.bid_srzx_activity_jxq_arrow_layout);
		jxqArrowLayout.setOnClickListener(this);
		jxqEditText = (EditText) findViewById(R.id.bid_srzx_activity_jxq_et);
		jxqEditText.setOnClickListener(this);
	}

	private void updateUsePrompt(List<String> usePromptList){
		if(usePromptList != null && usePromptList.size() > 0){
			daojuUsePrompt.setVisibility(View.VISIBLE);
		}else{
			daojuUsePrompt.setVisibility(View.GONE);
		}
		if(usePromptList == null || usePromptList.size() < 2){
			return;
		}
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<usePromptList.size();i++){
			String prompt = usePromptList.get(i);
			if(i == usePromptList.size() - 1){
				sb.append(prompt);
			}else{
				sb.append(prompt).append("��");
			}
		}

		usePrompt.setVisibility(View.VISIBLE);
		usePrompt.setText(sb.toString()+"ֻ��ʹ������һ��");
	}

	/**
	 * ���ʣ���Ͷ���
	 * @param info
	 */
	float extraRateF = 0f;
	int borrowBalanceTemp = 0;
	private void initInvestBalance(ProductInfo info) {
		if (info == null) {
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
			totalMoneyI = (int) totalMoneyL;
			investMoneyI = (int) investMoneyL;
			borrowBalance = totalMoneyI - investMoneyI;
			borrowBalanceTemp = borrowBalance;
		} catch (Exception e) {
		}
		try {
			extraRateF = Float.parseFloat(info.getAndroid_interest_rate());
		} catch (Exception e) {
		}
		borrowBalanceTV.setText(Util.commaSpliteData(String
				.valueOf(borrowBalance)));
		//Ĭ���Ǳ��ʣ���Ͷ���
		investMoneyET.setText(String
				.valueOf(borrowBalance));
		computeIncome(mProductInfo.getInterest_rate(),
				mProductInfo.getAndroid_interest_rate(), (String)jxqArrowLayout.getTag(),borrowBalance,
				(String)hbArrowLayout.getTag(),mProductInfo.getInvest_period());
	}

	private OnTouchListener mOnTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				updateInvestCounter(v);
			} else if (event.getAction() == MotionEvent.ACTION_UP
					|| event.getAction() == MotionEvent.ACTION_CANCEL) {
				stopInvestCounter();
			}
			return false;
		}
	};

	private TextWatcher watcherInvestMoney = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			String investMoneyStr = investMoneyET.getText().toString();
			int investMoney = 0;
			try {
				investMoney = Integer.parseInt(investMoneyStr);
				computeIncome(mProductInfo.getInterest_rate(),
						mProductInfo.getAndroid_interest_rate(),(String)jxqArrowLayout.getTag(), investMoney,
						(String)hbArrowLayout.getTag(),mProductInfo.getInvest_period());
			} catch (Exception e) {
				yjsyText.setText("0.00");
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};

	/**
	 * ������ʾ�������
	 * @param isVisible
	 */
	private void hbLayoutVisible(int isVisible){
		line1.setVisibility(isVisible);
		hbLayout.setVisibility(isVisible);
		line2.setVisibility(isVisible);
	}

	/**
	 * ������ʾ��Ϣȯ����
	 * @param isVisible
	 */
	private void jxqLayoutVisible(int isVisible){
		line2.setVisibility(isVisible);
		jxqLayout.setVisibility(isVisible);
		line3.setVisibility(isVisible);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.bid_srzx_activity_borrow_bidBtn:
			borrowInvest();
			break;
		case R.id.bid_srzx_activity_recharge_btn:
			//ȥ��ֵ
			if(SettingsManager.isPersonalUser(getApplicationContext())){
				checkIsVerify("��ֵ");
			}else if(SettingsManager.isCompanyUser(getApplicationContext())){
				Intent intent = new Intent(BidSRZXActivity.this,RechargeCompActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.bid_srzx_activity_discend_btn:
			// �ݼ�
			investMoneyDescend();
			break;
		case R.id.bid_srzx_activity_increase_btn:
			investMoneyIncrease();
			break;
		case R.id.bid_srzx_activity_delete:
			resetInvestMoneyET();
			break;
		case R.id.bid_srzx_activity_compact_text:
			//�鿴��ͬ
			Intent intent = new Intent(BidSRZXActivity.this,CompactActivity.class);
			intent.putExtra("from_where", "srzx");
			intent.putExtra("mProductInfo", mProductInfo);
			startActivity(intent);
			break;
		case R.id.bid_srzx_activity_hb_layout:
		case R.id.bid_srzx_activity_hb_arrow_layout:
		case R.id.bid_srzx_activity_hb_et:
			checkHB();
			break;
		case R.id.bid_srzx_activity_jxq_arrow_layout:
		case R.id.bid_srzx_activity_jxq_layout:
		case R.id.bid_srzx_activity_jxq_et:
			checkJXQ();
			break;
		default:
			break;
		}
	}
	
	private void checkHB() {
		showHBListWindow();
	}

	private void checkJXQ(){
		showJXQListWindow();
	}

	/**
	 * ���
	 */
	private void showHBListWindow() {
		if (hbList == null || hbList.size() < 1) {
			Util.toastLong(BidSRZXActivity.this, "û�к������");
			return;
		}
		View popView = LayoutInflater.from(this).inflate(
				R.layout.tyj_list_popwindow, null);
		int[] screen = SettingsManager.getScreenDispaly(BidSRZXActivity.this);
		int width = screen[0];
		int height = screen[1] * 1 / 3 + getResources().getDimensionPixelSize(R.dimen.common_measure_100dp);
		int hbCurPosition = 0;
		try{
			hbCurPosition = (int)hbArrowLayout.getTag(R.id.tag_third);
		}catch (Exception e){
			hbCurPosition = 0;
		}
		HBListPopupwindow popwindow = new HBListPopupwindow(
				BidSRZXActivity.this, popView, width, height,"ѡ���� (��"+hbList.size()+"��)",hbCurPosition);
		popwindow.show(mainLayout, hbList, new OnHBWindowItemClickListener() {
			@Override
			public void onItemClickListener(View view, int position) {
				hbArrowLayout.setTag(R.id.tag_third,position);
				if (position == 0) {
					hbET.setText("");
					hbET.setTag("");
					hbArrowLayout.setTag("0");
					updateInterest();
				} else {
					RedBagInfo info = hbList.get(position - 1);
					String moneyStr = investMoneyET.getText().toString();
					int investMoney = 0;//������������Ͷ�ʽ��
					int limitMoney = 0;//��ҪͶ�ʵĽ��
					try {
						investMoney = Integer.parseInt(moneyStr);
					} catch (Exception e) {
					}
					try {
						limitMoney = Integer.parseInt(info.getNeed_invest_money());
					} catch (Exception e) {
					}
					if(investMoney < limitMoney){
						hbET.setText("");
						hbArrowLayout.setTag(R.id.tag_third,0);
						Util.toastLong(BidSRZXActivity.this, "����Ͷ�ʽ�������Ҫ��");
					}else{
						//������Ϣ��������
						jxqEditText.setText(null);
						jxqEditText.setTag("");
						jxqArrowLayout.setTag("0");
						jxqArrowLayout.setTag(R.id.tag_third,0);

						if(limitMoney >= 10000){
							hbET.setText(Html.fromHtml("<font color='#31B2FE'>"+Util.formatRate(info.getMoney())+"Ԫ</font>�������Ͷ��"
									+Util.formatRate(String.valueOf(limitMoney/10000d))+"��Ԫ�����Ͽ���"));
						}else{
							hbET.setText(Html.fromHtml("<font color='#31B2FE'>"+Util.formatRate(info.getMoney())+"Ԫ</font>�������Ͷ��"
									+limitMoney+"Ԫ�����Ͽ���"));
						}
						hbET.setTag(info.getId());
						hbArrowLayout.setTag(info.getMoney());
						updateInterest();
					}
				}
			}
		});
	}

	/**
	 * ��ʾ��Ϣȯ���б�
	 */
	private void showJXQListWindow(){
		if (jxqList == null || jxqList.size() < 1) {
			Util.toastLong(BidSRZXActivity.this, "û�м�Ϣȯ����");
			return;
		}
		View popView = LayoutInflater.from(this).inflate(
				R.layout.tyj_list_popwindow, null);
		int[] screen = SettingsManager.getScreenDispaly(BidSRZXActivity.this);
		int width = screen[0];
		int height = screen[1] * 1 / 3 + getResources().getDimensionPixelSize(R.dimen.common_measure_100dp);
		int jxqCurPosition = 0;
		try{
			jxqCurPosition = (int)jxqArrowLayout.getTag(R.id.tag_third);
		}catch (Exception e){
			jxqCurPosition = 0;
		}
		JXQListPopupwindow popwindow = new JXQListPopupwindow(
				BidSRZXActivity.this, popView, width, height,"ѡ���Ϣȯ (��"+jxqList.size()+"��)",jxqCurPosition);
		popwindow.show(mainLayout, jxqList, new OnHBWindowItemClickListener() {
			@Override
			public void onItemClickListener(View view, int position) {
				jxqArrowLayout.setTag(R.id.tag_third,position);
				if (position == 0) {
					jxqEditText.setText(null);
					jxqEditText.setTag("");
					jxqArrowLayout.setTag("0");
					updateInterest();
				} else {
					JiaxiquanInfo info = jxqList.get(position - 1);
					String moneyStr = investMoneyET.getText().toString();
					int investMoney = 0;//������������Ͷ�ʽ��
					double limitMoney = 0;//��ҪͶ�ʵĽ��
					try {
						investMoney = Integer.parseInt(moneyStr);
					} catch (Exception e) {
					}
					try {
						limitMoney = Double.parseDouble(info.getMin_invest_money());
					} catch (Exception e) {
					}
					if(investMoney < limitMoney){
						jxqEditText.setText("");
						jxqArrowLayout.setTag(R.id.tag_third,0);
						Util.toastLong(BidSRZXActivity.this, "����Ͷ�ʽ������ϢȯҪ��");
					}else{
						//������Ϣ��������
						hbET.setText("");
						hbET.setTag("");
						hbArrowLayout.setTag("0");
						hbArrowLayout.setTag(R.id.tag_third,0);

						if(limitMoney >= 10000){
							jxqEditText.setText(Html.fromHtml("<font color='#31B2FE'>"+Util.formatRate(info.getMoney())
									+"%</font>��Ϣȯ��"+"��Ͷ��"+Util.formatRate(String.valueOf(limitMoney/10000))+"��Ԫ�����Ͽ���"));
						}else{
							jxqEditText.setText(Html.fromHtml("<font color='#31B2FE'>"+Util.formatRate(info.getMoney())
									+"%</font>��Ϣȯ��"+"��Ͷ��"+(int)(limitMoney)+"Ԫ�����Ͽ���"));
						}
						jxqEditText.setTag(info.getId());
						jxqArrowLayout.setTag(info.getMoney());
						updateInterest();
					}
				}
			}
		});
	}

	/**
	 * ѡ���Ϣȯ��ʱ��ˢ��Ԥ������
	 */
	private void updateInterest(){
		String investMoneyStr = investMoneyET.getText().toString();
		int investMoneyInt = 0;
		try {
			investMoneyInt = Integer.parseInt(investMoneyStr);
		} catch (Exception e) {
		}
		computeIncome(mProductInfo.getInterest_rate(),
				mProductInfo.getAndroid_interest_rate(),(String)jxqArrowLayout.getTag(), investMoneyInt,
				(String)hbArrowLayout.getTag(),mProductInfo.getInvest_period());
	}

	/**
	 * ��֤�û��Ƿ��Ѿ���֤
	 * @param type ����ֵ��,�����֡�
	 */
	private void checkIsVerify(final String type){
		rechargeBtn.setEnabled(false);
		RequestApis.requestIsVerify(BidSRZXActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(flag){
					//�û��Ѿ�ʵ��
					checkIsBindCard(type);
				}else{
					//�û�û��ʵ��
					Intent intent = new Intent(BidSRZXActivity.this,UserVerifyActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("type", type);
					intent.putExtra("bundle", bundle);
					startActivity(intent);
					rechargeBtn.setEnabled(true);
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
		RequestApis.requestIsBinding(BidSRZXActivity.this, SettingsManager.getUserId(getApplicationContext()), "����", new OnIsBindingListener() {
			@Override
			public void isBinding(boolean flag, Object object) {
				Intent intent = new Intent();
				if(flag){
					//�û��Ѿ���
					if("��ֵ".equals(type)){
						//��ôֱ��������ֵҳ��
						intent.setClass(BidSRZXActivity.this, RechargeActivity.class);
					}
				}else{
					//�û���û�а�
					Bundle bundle = new Bundle();
					bundle.putString("type", type);
					intent.putExtra("bundle", bundle);
					intent.setClass(BidSRZXActivity.this, BindCardActivity.class);
				}
				startActivity(intent);
				rechargeBtn.setEnabled(true);
			}
		});
	}

	//Ͷ��ǰ���ж�
	private void borrowInvest() {
		String moneyStr = investMoneyET.getText().toString();
		double borrowBalanceDouble = 0d;//���ʣ���Ͷ���
		try {
			moneyInvest = Integer.parseInt(moneyStr);
		} catch (Exception e) {
		}
		try {
			hbMoneyD = Double.parseDouble(hbArrowLayout.getTag().toString());
		} catch (Exception e) {
		}
        try {
            jxqMoneyD = Double.parseDouble(jxqArrowLayout.getTag().toString());
        } catch (Exception e) {
        }
		String borrowBalance = String.valueOf(borrowBalanceTemp);
		try {
			borrowBalanceDouble = Double.parseDouble(borrowBalance);
		} catch (Exception e) {
		}
		// ��Ŀ�Ͷ���С��30W�������û�һ����Ͷ��
		if (moneyInvest < borrowBalanceDouble) {
			// ��ʾ�û���1����Ͷ��
			Util.toastLong(BidSRZXActivity.this, "Ͷ�����С����Ͷ���");
		} else if (moneyInvest > borrowBalanceDouble) {
			Util.toastLong(BidSRZXActivity.this, "���ʣ���Ͷ����");
		} else if (!compactCB.isChecked()) {
			Util.toastLong(BidSRZXActivity.this, "�����Ķ���ͬ���ƷЭ��");
		} else {
			showInvestDialog();
		}
	}

	/**
	 * ȷ��Ͷ�ʵ�dialog
	 */
	private void showInvestDialog() {
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.invest_prompt_layout, null);
        LinearLayout yjbjxqLayout = (LinearLayout) contentView.findViewById(R.id.invest_prompt_yjbjxq_layout_detail);//Ԫ��Һͼ�Ϣȯʹ�õĲ���
		Button sureBtn = (Button) contentView
				.findViewById(R.id.invest_prompt_layout_surebtn);
		Button cancelBtn = (Button) contentView
				.findViewById(R.id.invest_prompt_layout_cancelbtn);
		TextView totalMoney = (TextView) contentView
				.findViewById(R.id.invest_prompt_layout_total);
		LinearLayout detailLayout = (LinearLayout) contentView.findViewById(R.id.invest_prompt_yjb_layout_detail);
		RelativeLayout hbLayout = (RelativeLayout) contentView.findViewById(R.id.invest_prompt_hb_layout_detail);//�������
		TextView hbMoneyTV = (TextView) contentView
				.findViewById(R.id.invest_prompt_layout_hb_count);
        TextView yjbjxqText = (TextView) contentView
                .findViewById(R.id.invest_prompt_layout_yjbjxq_count);//Ԫ��Ҽ�Ϣȯ ��ʹ�ö���
		detailLayout.setVisibility(View.GONE);
		if(hbMoneyD > 0){
			hbLayout.setVisibility(View.VISIBLE);
			hbMoneyTV.setText(Util.formatRate(String.valueOf(hbMoneyD))+"Ԫ���");
		}else{
			hbLayout.setVisibility(View.GONE);
		}
        //ʹ�ü�Ϣȯ
        if(jxqMoneyD > 0){
            yjbjxqLayout.setVisibility(View.VISIBLE);
            yjbjxqText.setText(Util.formatRate(String.valueOf(jxqMoneyD))+"%�ļ�Ϣȯ");
        }else{
            yjbjxqLayout.setVisibility(View.GONE);
        }
        hbMoneyTV.setText(Util.formatRate(String.valueOf(hbMoneyD))+"Ԫ���");
		totalMoney.setText(moneyInvest + "");

		AlertDialog.Builder builder = new AlertDialog.Builder(this,
				R.style.Dialog_Transparent); // �ȵõ�������
		builder.setView(contentView);
		builder.setCancelable(false);
		final AlertDialog dialog = builder.create();
		sureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handler.sendEmptyMessage(REQUEST_INVEST_WHAT);
				dialog.dismiss();
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {
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
		dialog.getWindow().setAttributes(lp);
	}

	/**
	 * �ݼ�
	 */
	private void investMoneyDescend() {
		String investMoneyStr = investMoneyET.getText().toString();
		int investMoneyInt = 0;
		try {
			investMoneyInt = Integer.parseInt(investMoneyStr);
		} catch (Exception e) {
		}
		if (investMoneyInt >= 0) {
			investMoneyInt = 0;
		} 
		if (investMoneyInt <= 0) {
			investMoneyET.setText(null);
		} else {
			investMoneyET.setText(investMoneyInt + "");
		}
		computeIncome(mProductInfo.getInterest_rate(),
				mProductInfo.getAndroid_interest_rate(), String.valueOf(jxqArrowLayout.getTag()),investMoneyInt,
				(String)hbArrowLayout.getTag(),mProductInfo.getInvest_period());
	}

	/**
	 * ����
	 */
	private void investMoneyIncrease() {
		double totalMoneyL = 0d;
		int totalMoneyI = 0;
		double investMoneyL = 0d;
		int investMoneyI = 0;
		int borrowBalance = 0;
		try {
			totalMoneyL = Double.parseDouble(mProductInfo.getTotal_money());
			investMoneyL = Double.parseDouble(mProductInfo.getInvest_money());
			totalMoneyI = (int) totalMoneyL;
			investMoneyI = (int) investMoneyL;
			borrowBalance = totalMoneyI - investMoneyI;
		} catch (Exception e) {
		}
		investMoneyET.setText(borrowBalance + "");
		computeIncome(mProductInfo.getInterest_rate(),
				mProductInfo.getAndroid_interest_rate(),(String)jxqArrowLayout.getTag(), borrowBalance,
				(String)hbArrowLayout.getTag(),mProductInfo.getInvest_period());
	}

	private void resetInvestMoneyET() {
		if (investMoneyET != null) {
			investMoneyET.setText(null);
			yjsyText.setText("0.00");
		}
	}

	private ScheduledExecutorService myExecuter;
	private void updateInvestCounter(final View v) {
		myExecuter = Executors.newSingleThreadScheduledExecutor();
		myExecuter.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				if (v.getId() == R.id.bid_yyy_activity_increase_btn) {
					handler.sendEmptyMessage(REQUEST_INVEST_INCREASE);
				} else if (v.getId() == R.id.bid_yyy_activity_discend_btn) {
					handler.sendEmptyMessage(REQUEST_INVEST_DESCEND);
				}
			}
		}, 200, 200, TimeUnit.MILLISECONDS);
	}

	public void stopInvestCounter() {
		if (myExecuter != null) {
			myExecuter.shutdownNow();
			myExecuter = null;
		}
	}

	/**
	 * �����껯�ʺ�Ͷ�ʽ���������
	 */
	private String computeIncome(String rateStr, String extraRateStr,String floatRate,
			int investMoney,String hbMoney, String daysStr) {
		float rateF = 0f;
		float extraRateF = 0f;
		float floatRateF = 0f;
		int days = 0;
		double hbMoneyD = 0;

		try {
			rateF = Float.parseFloat(rateStr);
			days = Integer.parseInt(daysStr);
		} catch (Exception e) {
		}
		try {
			extraRateF = Float.parseFloat(extraRateStr);
		} catch (Exception e) {
		}
		try{
			floatRateF = Float.parseFloat(floatRate);
		}catch (Exception e){
			e.printStackTrace();
		}
		try{
			hbMoneyD = Double.parseDouble(hbMoney);
		}catch (Exception e){
			e.printStackTrace();
		}
		double income = 0d;
		income = (rateF + extraRateF + floatRateF) * investMoney * days / 36500
				+ rateF * hbMoneyD * days / 36500;
		DecimalFormat df = new java.text.DecimalFormat("#.00");
		if (income < 1) {
			yjsyText.setText("0" + df.format(income));
		} else {
			yjsyText.setText(df.format(income));
		}
		return df.format(income);
	}

	/**
	 * ��������Ͷ�ʽӿ�
	 * 
	 * @param borrowId
	 * @param investUserId
	 * @param money
	 *            Ԫ���
	 * @param investFrom
	 *            �������
	 */
	private void requestInvest(String borrowId, String investUserId,
			String money, String investFrom,String redbagId,String couponId) {
		if (mLoadingDialog != null && !isFinishing()) {
			mLoadingDialog.show();
		}
		AsyncAppointBorrowInvest asyncBorrowInvest = new AsyncAppointBorrowInvest(
				BidSRZXActivity.this, borrowId, investUserId, money,investFrom,redbagId, couponId,new OnBorrowInvestInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
							mLoadingDialog.dismiss();
						}

						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								Message msg = handler.obtainMessage(REQUEST_INVEST_SUCCESS);
								msg.obj = baseInfo;
								handler.sendMessage(msg);
							} else {
								Message msg = handler.obtainMessage(REQUEST_INVEST_EXCEPTION);
								msg.obj = baseInfo;
								handler.sendMessage(msg);
							}
						} else {
							Message msg = handler.obtainMessage(REQUEST_INVEST_FAILE);
							handler.sendMessage(msg);
						}
					}
				});
		asyncBorrowInvest.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * ��ȡ��ǰ�û��Ŀ��ú�����б�
	 * 
	 * @param userId
	 * @param borrowType
	 */
	private void requestHBPageInfoByBorrowType(String userId, String borrowType) {
		if(mLoadingDialog != null && isFinishing()){
			mLoadingDialog.show();
		}
		AsyncCurrentUserRedbagList currentUserRedbagListTask = new AsyncCurrentUserRedbagList(
				BidSRZXActivity.this, userId, borrowType, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(mLoadingDialog != null && mLoadingDialog.isShowing()){
							mLoadingDialog.dismiss();
						}
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								RedBagPageInfo pageInfo = baseInfo
										.getmRedBagPageInfo();
								if (pageInfo != null) {
									hbList.addAll(pageInfo.getRedbagList());
								}
								if (hbList.size() < 1) {
									hbLayoutVisible(View.GONE);
								} else {
									usePromptList.add("���");
									updateUsePrompt(usePromptList);
									hbLayoutVisible(View.VISIBLE);
								}
							} else {
								hbLayoutVisible(View.GONE);
							}
						}else{
							hbLayoutVisible(View.GONE);
						}
					}
				});
		currentUserRedbagListTask
				.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * ���ü�Ϣȯ
	 * @param userId
	 * @param useStatus
	 */
	private void requestJXQList(String userId, String useStatus) {
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		AsyncJXQPageInfo redbagTask = new AsyncJXQPageInfo(BidSRZXActivity.this, userId,useStatus,
				String.valueOf(1),String.valueOf(100), new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(mLoadingDialog != null && mLoadingDialog.isShowing()){
					mLoadingDialog.dismiss();
				}
				if (baseInfo != null) {
					int resultCode = SettingsManager
							.getResultCode(baseInfo);
					if (resultCode == 0) {
						Message msg = handler
								.obtainMessage(REQUEST_JXQ_LIST_SUCCESS);
						msg.obj = baseInfo;
						handler.sendMessage(msg);
					}
				}
			}
		});
		redbagTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * �û��˻���Ϣ
	 */
	private void requestUserAccountInfo(String userId) {
		AsyncYiLianRMBAccount yilianTask = new AsyncYiLianRMBAccount(
				BidSRZXActivity.this, userId, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								UserRMBAccountInfo info = baseInfo
										.getRmbAccountInfo();
								userBalanceTV.setText(info.getUse_money());
							}
						}
					}
				});
		yilianTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

}
