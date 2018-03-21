package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.graphics.Paint;
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
import com.ylfcf.ppp.async.AsyncBorrowInvest;
import com.ylfcf.ppp.async.AsyncCurrentUserRedbagList;
import com.ylfcf.ppp.async.AsyncExperiencePageInfo;
import com.ylfcf.ppp.async.AsyncJXQPageInfo;
import com.ylfcf.ppp.async.AsyncUserYUANAccount;
import com.ylfcf.ppp.async.AsyncYiLianRMBAccount;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.JiaxiquanInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.RedBagInfo;
import com.ylfcf.ppp.entity.RedBagPageInfo;
import com.ylfcf.ppp.entity.TYJInfo;
import com.ylfcf.ppp.entity.TYJPageInfo;
import com.ylfcf.ppp.entity.UserRMBAccountInfo;
import com.ylfcf.ppp.entity.UserYUANAccountInfo;
import com.ylfcf.ppp.inter.Inter.OnBorrowInvestInter;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnIsBindingListener;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.inter.Inter.OnUserYUANAccountInter;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.util.YLFLogger;
import com.ylfcf.ppp.view.HBListPopupwindow;
import com.ylfcf.ppp.view.JXQListPopupwindow;
import com.ylfcf.ppp.view.TYJListPopupwindow;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ���Ŵ�---��ҪͶ�� Ԫ���ʹ�ù���:Ͷ������Ϊ35���ʱ��Ԫ���ʹ����Ϊ0.2% Ͷ������Ϊ95���ʱ��Ԫ���ʹ����Ϊ0.5%
 * Ͷ������Ϊ185��365���ʱ��Ԫ���ʹ����Ϊ1%
 * @author Mr.liu
 */
public class BidZXDActivity extends BaseActivity implements OnClickListener {
	private static final String className = "BidZXDActivity";
	private static final int REQUEST_INVEST_WHAT = 1201;
	private static final int REQUEST_INVEST_SUCCESS = 1202;
	private static final int REQUEST_INVEST_EXCEPTION = 1203;
	private static final int REQUEST_INVEST_FAILE = 1204;

	private static final int REQUEST_INVEST_INCREASE = 1205;
	private static final int REQUEST_INVEST_DESCEND = 1206;
	
	private static final int REQUEST_JXQ_LIST_WHAT = 1207;
	private static final int REQUEST_JXQ_LIST_SUCCESS = 1208;

	private LinearLayout topLeftBtn;
	private TextView topTitleTV, borrowName;
	private TextView userBalanceTV;// �û��������
	private TextView borrowBalanceTV;// ���ʣ���Ͷ���
	private TextView dtdzBtn;//��Ͷ��׬
	private Button rechargeBtn;// ��ֵ
	private TextView nhsyText;//�껯���� ����Ͷ�ʽ��Ĳ�ͬ����ͬ

	private Button descendBtn;// �ݼ���ť
	private Button increaseBtn;// ������ť
	private EditText investMoneyET;
	private ImageView deleteImg;// x��
	private TextView yjsyText;// Ԥ������

	private TextView daojuPromptTV;

	// Ԫ���
	private EditText yuanMoneyET;// Ԫ���
	private TextView yuanUsedTV;// Ԫ��ҿ��ý��
	private TextView yuanBalanceTV;// Ԫ������
	private ImageView borrowLogo;
	private TextView yjbText;
	private ImageView yjbDuihaoImg;
	private LinearLayout yjbLayout;
	private LinearLayout yjbBalanceLayout;// Ԫ������

	// �����
	private EditText tiyanjinET;// �����
	private RelativeLayout tyjArrowLayout;// ������ͷ
	private LinearLayout tyjLayout;// �����

	// ���
	private LinearLayout hbLayout;// ���
	private EditText hbEditText;
	private RelativeLayout hbArrowLayout;// ����ļ�ͷ
	
	// ��Ϣȯ
	private LinearLayout jxqLayout;// ��Ϣȯ
	private EditText jxqEditText;
	private RelativeLayout jxqArrowLayout;// ��Ϣȯ�ļ�ͷ
	private TextView usePrompt;//ʹ��˵������Ϣȯ��Ԫ��Ҳ���ͬʱʹ�õ�
	private List<String> usePromptList = new ArrayList<String>();

	private ProductInfo mProductInfo;
	private int moneyInvest = 0;//Ͷ�ʽ��
	private int bonusMoney = 0;// Ԫ���
	private double hbMoney = 0;//������
	private double jxqMoney = 0d;//��Ϣȯ
	private Button investBtn;
	private UserYUANAccountInfo mUserYUANAccountInfo = null;// Ԫ����˻�

	private LinearLayout mainLayout;
	private View line1, line2, line3, line4, line5;// �ָ���
	private CheckBox cb;
	private TextView compactText;//���Э��

	private int limitInvest = 0;// Ͷ������
	private String borrowType = "";
	private List<TYJInfo> experienceList = new ArrayList<TYJInfo>();// �û�δʹ�õ�������б�
	private List<RedBagInfo> hbList = new ArrayList<RedBagInfo>();// �û�δʹ�õĺ���б�
	private List<JiaxiquanInfo> jxqList = new ArrayList<JiaxiquanInfo>();//��ʹ�õļ�Ϣȯ�б�
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String sysTimeStr = "";
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_INVEST_WHAT:
				requestInvest(mProductInfo.getId(),
						SettingsManager.getUserId(getApplicationContext()),
						String.valueOf(moneyInvest), bonusMoney,
						SettingsManager.USER_FROM,
						SettingsManager.getUserFromSub(getApplicationContext()), tiyanjinET.getText()
								.toString(), "", "", String.valueOf(hbEditText
								.getTag()),String.valueOf(jxqEditText.getTag()));
				break;
			case REQUEST_INVEST_SUCCESS:
				BaseInfo baseInfo = (BaseInfo) msg.obj;
				Intent intentSuccess = new Intent(BidZXDActivity.this,
						BidSuccessActivity.class);
				intentSuccess.putExtra("from_where", "Ԫ��ӯ");
				intentSuccess.putExtra("base_info",baseInfo);
				startActivity(intentSuccess);
				mApp.finishAllActivityExceptMain();
				break;
			case REQUEST_INVEST_EXCEPTION:
				BaseInfo base = (BaseInfo) msg.obj;
				Util.toastShort(BidZXDActivity.this, base.getMsg());
				break;
			case REQUEST_INVEST_FAILE:
				Util.toastShort(BidZXDActivity.this, "�����쳣");
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
					if("δʹ��".equals(info.getUse_status()) && info.getBorrow_type().contains(borrowType)){
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
					jxqLayout.setVisibility(View.VISIBLE);
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
		setContentView(R.layout.bid_zxd_activity);

		mProductInfo = (ProductInfo) getIntent().getSerializableExtra(
				"PRODUCT_INFO");
		
		if (mProductInfo != null) {
			String horizon = "";
			if(mProductInfo.getInvest_horizon() == null || "".equals(mProductInfo.getInvest_horizon())){
				horizon = mProductInfo.getInterest_period();
			}else{
				horizon = mProductInfo.getInvest_horizon().replace("��", "");
			}
			try {
				limitInvest = Integer.parseInt(horizon);
			} catch (Exception e) {
			}
		}

		findViews();
		initInvestBalance(mProductInfo);
		if(mProductInfo != null){
	        	if(mProductInfo.getInterest_period().contains("92")){
	            	//Ԫ����
	        		requestHBPageInfoByBorrowType(
	    					SettingsManager.getUserId(getApplicationContext()),"Ԫ����");
	            }else if(mProductInfo.getInterest_period().contains("32")){
	            	//Ԫ��ͨ
	            	requestHBPageInfoByBorrowType(
	    					SettingsManager.getUserId(getApplicationContext()),"Ԫ��ͨ");
	            }else if(mProductInfo.getInterest_period().contains("182")){
	            	//Ԫ����
	            	requestHBPageInfoByBorrowType(
	    					SettingsManager.getUserId(getApplicationContext()),"Ԫ����");
	            }else if(mProductInfo.getInterest_period().contains("365")){
	            	//Ԫ����
	            	requestHBPageInfoByBorrowType(
	    					SettingsManager.getUserId(getApplicationContext()),"Ԫ����");
	            }
		}
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				requestExperiencePageInfoByStatus("δʹ��",
						SettingsManager.getUserId(getApplicationContext()), "",
						"");
			}
		}, 50L);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				requestYuanAccountInfo(SettingsManager
						.getUserId(getApplicationContext()));
			}
		}, 60L);

		handler.sendEmptyMessageDelayed(REQUEST_JXQ_LIST_WHAT,60L);
	}

	private void updateUsePrompt(List<String> usePromptList){
		StringBuffer sb = new StringBuffer();
		if(usePromptList != null && usePromptList.size() > 0){
			daojuPromptTV.setVisibility(View.VISIBLE);
		}else{
			daojuPromptTV.setVisibility(View.GONE);
		}
		if(usePromptList != null && usePromptList.size() > 1){

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
		}else{
			usePrompt.setVisibility(View.GONE);
			yjbLayoutVisible(View.GONE);
		}
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

		usePrompt = (TextView) findViewById(R.id.bid_zxd_activity_use_prompt);
		borrowName = (TextView) findViewById(R.id.bid_zxd_activity_borrow_name);
		userBalanceTV = (TextView) findViewById(R.id.bid_zxd_activity_user_balance);
		borrowBalanceTV = (TextView) findViewById(R.id.bid_zxd_activity_borrow_balance);
		nhsyText = (TextView) findViewById(R.id.bid_zxd_activity_nhsy);
		nhsyText.setText(mProductInfo.getInterest_rate()+"%");
		if(SettingsManager.checkFloatRate(mProductInfo) && !"Ԫ����".equals(mProductInfo.getBorrow_type())){
			dtdzBtn = (TextView) findViewById(R.id.bid_zxd_activity_dtdz_btn);
			dtdzBtn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG ); //�»���
			dtdzBtn.getPaint().setAntiAlias(true);//�����
			dtdzBtn.setVisibility(View.VISIBLE);
			dtdzBtn.setOnClickListener(this);
		}
		
		rechargeBtn = (Button) findViewById(R.id.bid_zxd_activity_recharge_btn);
		rechargeBtn.setOnClickListener(this);
		descendBtn = (Button) findViewById(R.id.bid_zxd_activity_discend_btn);
		descendBtn.setOnClickListener(this);
		descendBtn.setOnTouchListener(mOnTouchListener);
		increaseBtn = (Button) findViewById(R.id.bid_zxd_activity_increase_btn);
		increaseBtn.setOnClickListener(this);
		increaseBtn.setOnTouchListener(mOnTouchListener);
		investMoneyET = (EditText) findViewById(R.id.bid_zxd_activity_invest_et);
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
					if (investMoney == 0) {
						Util.toastLong(BidZXDActivity.this, "Ͷ�ʽ���Ϊ0");
					} else if (investMoney % 100 != 0) {
						Util.toastLong(BidZXDActivity.this, "Ͷ�ʽ�����Ϊ100��������");
					}

					// �ж�Ͷ�ʽ���Ƿ���ڱ����ʣ���
					String borrowBalanceStr = String.valueOf(borrowBalanceTemp);
					borrowBalanceDouble = Double.parseDouble(borrowBalanceStr);
					if (investMoney > borrowBalanceDouble) {
						Util.toastLong(BidZXDActivity.this, "���ʣ���Ͷ����");
					}
				} catch (Exception e) {
				}

			}
		});
		deleteImg = (ImageView) findViewById(R.id.bid_zxd_activity_delete);
		deleteImg.setOnClickListener(this);
		yjsyText = (TextView) findViewById(R.id.bid_zxd_activity_yjsy);
		yuanMoneyET = (EditText) findViewById(R.id.bid_zxd_activity_yuanmoney_et);
		yuanMoneyET.addTextChangedListener(watcherYuanMoney);
		yuanUsedTV = (TextView) findViewById(R.id.bid_zxd_activity_used_yjb_text);
		yuanBalanceTV = (TextView) findViewById(R.id.bid_zxd_activity_balance_yjb_text);
		tiyanjinET = (EditText) findViewById(R.id.bid_zxd_activity_tyj_et);
		tyjArrowLayout = (RelativeLayout) findViewById(R.id.bid_zxd_activity_tyj_arrow_layout);
		tyjArrowLayout.setOnClickListener(this);
		investBtn = (Button) findViewById(R.id.bid_zxd_activity_borrow_bidBtn);
		investBtn.setOnClickListener(this);
		borrowLogo = (ImageView) findViewById(R.id.bid_zxd_activity_prompt_logo);
		if (mProductInfo != null) {
			borrowName.setText(mProductInfo.getBorrow_name());
			if (mProductInfo.getInterest_period().contains("182")) {
				borrowLogo.setImageResource(R.drawable.bid_ydh_logo);
				borrowType = "Ԫ����";
			} else if (mProductInfo.getInterest_period().contains("32")) {
				borrowLogo.setImageResource(R.drawable.bid_yyt_logo);
				borrowType = "Ԫ��ͨ";
			} else if (mProductInfo.getInterest_period().contains("92")) {
				borrowLogo.setImageResource(R.drawable.bid_yjr_logo);
				borrowType = "Ԫ����";
			} else if (mProductInfo.getInterest_period().contains("365")){
				borrowLogo.setImageResource(R.drawable.bid_ynx_logo);
				borrowType = "Ԫ����";
			}
		}
		mainLayout = (LinearLayout) findViewById(R.id.bid_activity_mainlayout);
		line1 = findViewById(R.id.bid_zxd_activity_line1);
		line2 = findViewById(R.id.bid_zxd_activity_line2);
		line3 = findViewById(R.id.bid_zxd_activity_line3);
		line4 = findViewById(R.id.bid_zxd_activity_line4);
		line5 = findViewById(R.id.bid_zxd_activity_line5);

		daojuPromptTV = (TextView) findViewById(R.id.bid_zxd_activity_daoju_prompt);
		yjbLayout = (LinearLayout) findViewById(R.id.bid_zxd_activity_yjb_layout);
		yjbBalanceLayout = (LinearLayout) findViewById(R.id.bid_zxd_activity_yjb_balance_layout);
		yjbDuihaoImg = (ImageView) findViewById(R.id.bid_zxd_activity_yjb_duihao);
		yjbText = (TextView) findViewById(R.id.bid_zxd_activity_yjb_text);
		tyjLayout = (LinearLayout) findViewById(R.id.bid_zxd_activity_tyj_layout);
		hbLayout = (LinearLayout) findViewById(R.id.bid_zxd_activity_hb_layout);
		hbLayout.setOnClickListener(this);
		hbEditText = (EditText) findViewById(R.id.bid_zxd_activity_hb_et);
		hbEditText.setOnClickListener(this);
		hbArrowLayout = (RelativeLayout) findViewById(R.id.bid_zxd_activity_hb_arrow_layout);
		hbArrowLayout.setOnClickListener(this);
		jxqLayout = (LinearLayout) findViewById(R.id.bid_zxd_activity_jxq_layout);
		jxqLayout.setOnClickListener(this);
		jxqEditText = (EditText) findViewById(R.id.bid_zxd_activity_jxq_et);
		jxqEditText.setOnClickListener(this);
		jxqArrowLayout = (RelativeLayout) findViewById(R.id.bid_zxd_activity_jxq_arrow_layout);
		jxqArrowLayout.setOnClickListener(this);
		cb = (CheckBox) findViewById(R.id.bid_zxd_activity_cb);
		compactText = (TextView) findViewById(R.id.bid_zxd_activity_compact_text);
		compactText.setOnClickListener(this);
		// �ж�������Ԫ����Ƿ�������������Ӧ���ֵ���ʾ������
	}

	private void checkYjbLayoutVisible(){
		if(mUserYUANAccountInfo == null){
			yjbLayoutVisible(View.GONE);
			return;
		}
		double useCoinD = 0d;
		try{
			useCoinD = Double.parseDouble(mUserYUANAccountInfo.getUse_coin());
		}catch (Exception e){
		}

		if ("����".equals(mProductInfo.getIs_coin()) && useCoinD > 0) {
			// Ԫ��ҿ���
			usePromptList.add("Ԫ���");
			updateUsePrompt(usePromptList);
			yjbLayoutVisible(View.VISIBLE);
		} else {
			// Ԫ��ҹر�
			yjbLayoutVisible(View.GONE);
		}
	}

	private void yjbLayoutVisible(int isVisible){
		yjbLayout.setVisibility(isVisible);
		yjbBalanceLayout.setVisibility(isVisible);
		line1.setVisibility(isVisible);
		line2.setVisibility(isVisible);
	}

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

	/**
	 * Ԫ�����������
	 */
	private TextWatcher watcherYuanMoney = new TextWatcher(){
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			YLFLogger.d("yuanmoneyet beforeTextChanged()");
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			YLFLogger.d("yuanmoneyet onTextChanged()");
			String yuanmoneyS = yuanMoneyET.getText().toString();//Ԫ�������Ľ��
			String yuanmoneyCurUsed = yuanUsedTV.getText().toString();//Ԫ��ҵ�ǰ���õĽ�
			int yuanMoneyI = 0;
			int yuanMoneyCurUsedI = 0;
			try{
				yuanMoneyI = Integer.parseInt(yuanmoneyS);
				yuanMoneyCurUsedI = Integer.parseInt(yuanmoneyCurUsed);
			}catch (Exception e){
				e.printStackTrace();
			}
			if(yuanMoneyI > yuanMoneyCurUsedI){
				//����Ľ�����ڵ�ǰ���������
				yuanMoneyI = yuanMoneyCurUsedI;
				yuanMoneyET.setText(String.valueOf(yuanMoneyCurUsedI));
				yuanMoneyET.setSelection(yuanmoneyCurUsed.length());
			}else{
			}
			if(yuanMoneyI > 0){
				yjbDuihaoImg.setImageResource(R.drawable.duihao_selected);
				yjbText.setTextColor(getResources().getColor(R.color.black));

				//������������
				jxqEditText.setText(null);
				jxqEditText.setTag("");
				jxqArrowLayout.setTag("0");
				jxqArrowLayout.setTag(R.id.tag_third,0);
				hbEditText.setText("");
				hbEditText.setTag("");
				hbArrowLayout.setTag(R.id.tag_first,"0");
				hbArrowLayout.setTag(R.id.tag_second,"0");
				hbArrowLayout.setTag(R.id.tag_third,0);
			}else{
				yjbDuihaoImg.setImageResource(R.drawable.duihao_unselected);
				yjbText.setTextColor(getResources().getColor(R.color.gray1));
			}
			updateInterest();
		}

		@Override
		public void afterTextChanged(Editable s) {
			YLFLogger.d("yuanmoneyet afterTextChanged()");
		}
	};

	private TextWatcher watcherInvestMoney = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			String investMoneyStr = investMoneyET.getText().toString();
			String floatRateStr = "0";
			int investMoney = 0;
			try {
				investMoney = Integer.parseInt(investMoneyStr);
				//Ԫ���β�Ʒ�����ܸ�������
				if(investMoney < 100000 || mProductInfo.getInterest_period().contains("365")){
					floatRateStr = "0";
				}else if(investMoney >= 100000 && investMoney < 300000){
					floatRateStr = "0.1";
				}else if(investMoney >= 300000 && investMoney < 500000){
					floatRateStr = "0.2";
				}else if(investMoney >= 500000){
					floatRateStr = "0.3";
				}
				computeIncome(mProductInfo.getInterest_rate(),
						mProductInfo.getAndroid_interest_rate(),floatRateStr, (String)jxqArrowLayout.getTag(),
						investMoney,(String)hbArrowLayout.getTag(R.id.tag_first),
						mProductInfo.getInvest_horizon());
				computeUsedYuanMoney(investMoney);// �����ʹ�õ�Ԫ���
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.bid_zxd_activity_borrow_bidBtn:
			borrowInvest();
			break;
		case R.id.bid_zxd_activity_recharge_btn:
			//ȥ��ֵ
			if(SettingsManager.isPersonalUser(getApplicationContext())){
				checkIsVerify("��ֵ");
			}else if(SettingsManager.isCompanyUser(getApplicationContext())){
				Intent intent = new Intent(BidZXDActivity.this,RechargeCompActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.bid_zxd_activity_tyj_arrow_layout:
			checkTYJ();
			break;
		case R.id.bid_zxd_activity_discend_btn:
			// �ݼ�
			investMoneyDescend();
			break;
		case R.id.bid_zxd_activity_increase_btn:
			investMoneyIncrease();
			break;
		case R.id.bid_zxd_activity_delete:
			resetInvestMoneyET();
			break;
		case R.id.bid_zxd_activity_hb_arrow_layout:
		case R.id.bid_zxd_activity_hb_layout:
		case R.id.bid_zxd_activity_hb_et:
			checkHB();
			break;
		case R.id.bid_zxd_activity_dtdz_btn:
			Intent intentBanner = new Intent(BidZXDActivity.this,BannerTopicActivity.class);
			BannerInfo bannerInfo = new BannerInfo();
			bannerInfo.setArticle_id("110");
			bannerInfo.setLink_url(URLGenerator.FLOAT_RATE_URL);
			intentBanner.putExtra("BannerInfo", bannerInfo);
			startActivity(intentBanner);
			break;
		case R.id.bid_zxd_activity_jxq_arrow_layout:
		case R.id.bid_zxd_activity_jxq_layout:
		case R.id.bid_zxd_activity_jxq_et:
			checkJXQ();
			break;
		case R.id.bid_zxd_activity_compact_text:
			//���Э��
			Intent intent = new Intent(BidZXDActivity.this,CompactActivity.class);
			intent.putExtra("from_where", "yzy");
			intent.putExtra("mProductInfo", mProductInfo);
			startActivity(intent);
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
		rechargeBtn.setEnabled(false);
		RequestApis.requestIsVerify(BidZXDActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(flag){
					//�û��Ѿ�ʵ��
					checkIsBindCard(type);
				}else{
					//�û�û��ʵ��
					Intent intent = new Intent(BidZXDActivity.this,UserVerifyActivity.class);
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
		RequestApis.requestIsBinding(BidZXDActivity.this, SettingsManager.getUserId(getApplicationContext()), "����", new OnIsBindingListener() {
			@Override
			public void isBinding(boolean flag, Object object) {
				Intent intent = new Intent();
				if(flag){
					//�û��Ѿ���
					if("��ֵ".equals(type)){
						//��ôֱ��������ֵҳ��
						intent.setClass(BidZXDActivity.this, RechargeActivity.class);
					}
				}else{
					//�û���û�а�
					Bundle bundle = new Bundle();
					bundle.putString("type", type);
					intent.putExtra("bundle", bundle);
					intent.setClass(BidZXDActivity.this, BindCardActivity.class);
				}
				startActivity(intent);
				rechargeBtn.setEnabled(true);
			}
		});
	}

	/**
	 * ��������
	 */
	private void checkTYJ() {
		// ����ʹ�������
		showTYJListWindow();
	}

	private void checkHB() {
		showHBListWindow();
	}
	
	private void checkJXQ(){
		showJXQListWindow();
	}

	/**
	 * 
	 */
	private void borrowInvest() {
		bonusMoney = 0;
		hbMoney = 0;
		jxqMoney = 0;
		String moneyStr = investMoneyET.getText().toString();
		String bonusStr = yuanMoneyET.getText().toString();
		double yuanUsedMoney = 0d;// Ԫ��ҿ������
		double yuanInputMoney = 0d;//Ԫ���
		double needRechargeMoeny = 0d;// ��Ҫ֧���Ľ�� ���� ����Ľ���ȥ����Ԫ��ҵĽ��
		double userBanlanceDouble = 0d;
		double borrowBalanceDouble = 0d;
		double hbDouble = 0d;//������
		double hbNeedMoneyD = 0d;
		double jxqDouble = 0d;//��Ϣȯ
		double androidRateD = 0d;//��׿��Ϣ�ֶ�
		String yuanUsedMoneyStr = "0";
		String yuanInputMoneyStr = "0";
		try {
			yuanUsedMoneyStr = yuanUsedTV.getText().toString();
			if (yuanUsedMoneyStr != null && !"".equals(yuanUsedMoneyStr)) {
				yuanUsedMoney = Double.parseDouble(yuanUsedMoneyStr);
			}
		} catch (Exception e) {
		}

		try {
			yuanInputMoneyStr = yuanMoneyET.getText().toString();
			if (yuanInputMoneyStr != null && !"".equals(yuanInputMoneyStr)) {
				yuanInputMoney = Double.parseDouble(yuanInputMoneyStr);
			}
		} catch (Exception e) {
		}
		try {
			moneyInvest = Integer.parseInt(moneyStr);
		} catch (Exception e) {
			moneyInvest = 0;
		}
		try {
			bonusMoney = Integer.parseInt(bonusStr);
		} catch (Exception e) {
		}
		try {
			hbMoney = Double.parseDouble(hbArrowLayout.getTag(R.id.tag_first).toString());//������
		} catch (Exception e) {
		}
		try{
			hbNeedMoneyD = Double.parseDouble(hbArrowLayout.getTag(R.id.tag_second).toString());//������ƽ��
		}catch (Exception e){

		}
		try {
			jxqMoney = Double.parseDouble(jxqArrowLayout.getTag().toString());
		} catch (Exception e) {
		}
		try {
			hbDouble = Double.parseDouble(hbArrowLayout.getTag(R.id.tag_first).toString());
		} catch (Exception e) {
		}
		try {
			jxqDouble = Double.parseDouble(jxqArrowLayout.getTag().toString());
		} catch (Exception e) {
		}
		try{
			androidRateD = Double.parseDouble(mProductInfo.getAndroid_interest_rate());
		}catch (Exception e){

		}
		needRechargeMoeny = moneyInvest - yuanInputMoney;
		// �ж�Ͷ�ʽ���Ƿ�����˻����
		String userBanlance = userBalanceTV.getText().toString();
		userBanlanceDouble = Double.parseDouble(userBanlance);
		String borrowBalance = String.valueOf(borrowBalanceTemp);
		borrowBalanceDouble = Double.parseDouble(borrowBalance);
		int flagOtc = SettingsManager.checkActiveStatusBySysTime(mProductInfo.getAdd_time(),
				SettingsManager.activeOct2017_StartTime,SettingsManager.activeOct2017_EndTime);//10�»
		int flagNov = SettingsManager.checkActiveStatusBySysTime(mProductInfo.getAdd_time(),
				SettingsManager.activeNov2017_StartTime,SettingsManager.activeNov2017_EndTime);//11�»
		if (moneyInvest < 100L) {
			Util.toastShort(BidZXDActivity.this, "Ͷ�ʽ���С��100Ԫ");
		} else if (moneyInvest % 100 != 0) {
			Util.toastLong(BidZXDActivity.this, "Ͷ�ʽ�����Ϊ100��������");
		} else if (hbDouble > 0 && yuanInputMoney > 0 && jxqDouble > 0 || (hbDouble > 0 && yuanInputMoney > 0) || 
				(yuanInputMoney > 0 && jxqDouble > 0) || (hbDouble > 0 && jxqDouble > 0)) {
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<usePromptList.size();i++){
				sb.append(usePromptList.get(i));
			}
			Util.toastLong(BidZXDActivity.this, sb.toString()+"����ͬʱʹ��");
		} else if (yuanInputMoney > yuanUsedMoney) {
			Util.toastLong(BidZXDActivity.this, "Ԫ���ʹ�ó���");
		} else if(moneyInvest < hbNeedMoneyD){
			Util.toastLong(BidZXDActivity.this, "Ͷ�ʽ����δ�ﵽ"+hbArrowLayout.getTag(R.id.tag_second).toString()+"Ԫ����ĵ���Ͷ�ʽ��Ҫ��");
		}else if (needRechargeMoeny > userBanlanceDouble + yuanInputMoney) {
			Util.toastLong(BidZXDActivity.this, "�˻�����");
		} else if (needRechargeMoeny > borrowBalanceDouble) {
			Util.toastLong(BidZXDActivity.this, "��Ŀ�Ͷ����");
		} else if(!cb.isChecked()){
			Util.toastLong(BidZXDActivity.this, "�����Ķ���ͬ���ƷЭ��");
		}else if(flagOtc == 0 && (mProductInfo.getInterest_period().contains("92")||mProductInfo.getInterest_period().contains("365"))){
			//2017ʮ�»��Ϣ Ԫ���ں�Ԫ���β��м�Ϣ
			if(hbDouble > 0){
				showOtcActivityPromptDialog("���");
			}else if(yuanInputMoney > 0){
				showOtcActivityPromptDialog("Ԫ���");
			}else if(jxqDouble > 0){
				showOtcActivityPromptDialog("��Ϣȯ");
			}else{
				showInvestDialog();
			}
		}else if(SettingsManager.isPersonalUser(getApplicationContext()) && androidRateD > 0){
			//2017˫ʮһ���Ϣ Ԫ���β��м�Ϣ(ֻ��Ը����û�)
			if(hbDouble > 0){
				showNovActivityPromptDialog("���",androidRateD);
			}else if(yuanInputMoney > 0){
				showNovActivityPromptDialog("Ԫ���",androidRateD);
			}else if(jxqDouble > 0){
				showNovActivityPromptDialog("��Ϣȯ",androidRateD);
			}else{
				showInvestDialog();
			}
		}else {
			showInvestDialog();
		}
	}

	/**
	 * 10�¼�Ϣ�
	 * @param flag
	 */
	private void showOtcActivityPromptDialog(String flag){
		View contentView = LayoutInflater.from(this).inflate(R.layout.active_otc_dialog_layout, null);
		final Button leftBtn = (Button) contentView.findViewById(R.id.active_otc_dialog_left_btn);
		final Button rightBtn = (Button) contentView.findViewById(R.id.active_otc_dialog_right_btn);
		TextView contentTV = (TextView) contentView.findViewById(R.id.active_otc_dialog_content);
		if(mProductInfo.getInterest_period().contains("92")){
			//Ԫ����
			if("���".equals(flag)){
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
					contentTV.setText(Html.fromHtml("��ʹ����<font color='#31B2FE'>���</font>��" +
							"����������<font color='#31B2FE'>0.5%</font>�Ļ��Ϣ���Ƿ�Ҫ������",Html.FROM_HTML_MODE_LEGACY));
				} else {
					contentTV.setText(Html.fromHtml("��ʹ����<font color='#31B2FE'>���</font>��" +
							"����������<font color='#31B2FE'>0.5%</font>�Ļ��Ϣ���Ƿ�Ҫ������"));
				}
			}else if("Ԫ���".equals(flag)){
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
					contentTV.setText(Html.fromHtml("��ʹ����<font color='#31B2FE'>Ԫ���</font>��" +
							"����������<font color='#31B2FE'>0.5%</font>�Ļ��Ϣ���Ƿ�Ҫ������",Html.FROM_HTML_MODE_LEGACY));
				} else {
					contentTV.setText(Html.fromHtml("��ʹ����<font color='#31B2FE'>Ԫ���</font>��" +
							"����������<font color='#31B2FE'>0.5%</font>�Ļ��Ϣ���Ƿ�Ҫ������"));
				}
			}else if("��Ϣȯ".equals(flag)){
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
					contentTV.setText(Html.fromHtml("��ʹ����<font color='#31B2FE'>��Ϣȯ</font>��" +
							"����������<font color='#31B2FE'>0.5%</font>�Ļ��Ϣ���Ƿ�Ҫ������",Html.FROM_HTML_MODE_LEGACY));
				} else {
					contentTV.setText(Html.fromHtml("��ʹ����<font color='#31B2FE'>��Ϣȯ</font>��" +
							"����������<font color='#31B2FE'>0.5%</font>�Ļ��Ϣ���Ƿ�Ҫ������"));
				}
			}
		}else if(mProductInfo.getInterest_period().contains("365")){
			//Ԫ����
			if("���".equals(flag)){
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
					contentTV.setText(Html.fromHtml("��ʹ����<font color='#31B2FE'>���</font>��" +
							"����������<font color='#31B2FE'>0.8%</font>�Ļ��Ϣ���Ƿ�Ҫ������",Html.FROM_HTML_MODE_LEGACY));
				} else {
					contentTV.setText(Html.fromHtml("��ʹ����<font color='#31B2FE'>���</font>��" +
							"����������<font color='#31B2FE'>0.8%</font>�Ļ��Ϣ���Ƿ�Ҫ������"));
				}
			}else if("Ԫ���".equals(flag)){
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
					contentTV.setText(Html.fromHtml("��ʹ����<font color='#31B2FE'>Ԫ���</font>��" +
							"����������<font color='#31B2FE'>0.8%</font>�Ļ��Ϣ���Ƿ�Ҫ������",Html.FROM_HTML_MODE_LEGACY));
				} else {
					contentTV.setText(Html.fromHtml("��ʹ����<font color='#31B2FE'>Ԫ���</font>��" +
							"����������<font color='#31B2FE'>0.8%</font>�Ļ��Ϣ���Ƿ�Ҫ������"));
				}
			}else if("��Ϣȯ".equals(flag)){
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
					contentTV.setText(Html.fromHtml("��ʹ����<font color='#31B2FE'>��Ϣȯ</font>��" +
							"����������<font color='#31B2FE'>0.8%</font>�Ļ��Ϣ���Ƿ�Ҫ������",Html.FROM_HTML_MODE_LEGACY));
				} else {
					contentTV.setText(Html.fromHtml("��ʹ����<font color='#31B2FE'>��Ϣȯ</font>��" +
							"����������<font color='#31B2FE'>0.8%</font>�Ļ��Ϣ���Ƿ�Ҫ������"));
				}
			}
		}

		AlertDialog.Builder builder=new AlertDialog.Builder(this, R.style.Dialog_Transparent);  //�ȵõ�������
		builder.setView(contentView);
		builder.setCancelable(true);
		final AlertDialog dialog = builder.create();
		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				showInvestDialog();
			}
		});
		rightBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		//��������������ˣ���������ʾ����
		dialog.show();
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = display.getWidth()*6/7;
		dialog.getWindow().setAttributes(lp);
	}

	/**
	 * 2017˫11��Ϣ��������û�Ͷ��Ԫ���μ�Ϣ1.1%
	 * @param flag
	 */
	private void showNovActivityPromptDialog(String flag,double androidRateD){
		View contentView = LayoutInflater.from(this).inflate(R.layout.active_otc_dialog_layout, null);
		final Button leftBtn = (Button) contentView.findViewById(R.id.active_otc_dialog_left_btn);
		final Button rightBtn = (Button) contentView.findViewById(R.id.active_otc_dialog_right_btn);
		TextView contentTV = (TextView) contentView.findViewById(R.id.active_otc_dialog_content);
		if(mProductInfo.getInterest_period().contains("365")){
			//Ԫ����
			if("���".equals(flag)){
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
					contentTV.setText(Html.fromHtml("��ʹ����<font color='#31B2FE'>���</font>��" +
							"����������<font color='#31B2FE'>" + androidRateD + "</font>�Ļ��Ϣ���Ƿ�Ҫ������",Html.FROM_HTML_MODE_LEGACY));
				} else {
					contentTV.setText(Html.fromHtml("��ʹ����<font color='#31B2FE'>���</font>��" +
							"����������<font color='#31B2FE'>" + androidRateD + "</font>�Ļ��Ϣ���Ƿ�Ҫ������"));
				}
			}else if("Ԫ���".equals(flag)){
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
					contentTV.setText(Html.fromHtml("��ʹ����<font color='#31B2FE'>Ԫ���</font>��" +
							"����������<font color='#31B2FE'>" + androidRateD + "</font>�Ļ��Ϣ���Ƿ�Ҫ������",Html.FROM_HTML_MODE_LEGACY));
				} else {
					contentTV.setText(Html.fromHtml("��ʹ����<font color='#31B2FE'>Ԫ���</font>��" +
							"����������<font color='#31B2FE'>" + androidRateD + "</font>�Ļ��Ϣ���Ƿ�Ҫ������"));
				}
			}else if("��Ϣȯ".equals(flag)){
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
					contentTV.setText(Html.fromHtml("��ʹ����<font color='#31B2FE'>��Ϣȯ</font>��" +
							"����������<font color='#31B2FE'>"+ androidRateD +"</font>�Ļ��Ϣ���Ƿ�Ҫ������",Html.FROM_HTML_MODE_LEGACY));
				} else {
					contentTV.setText(Html.fromHtml("��ʹ����<font color='#31B2FE'>��Ϣȯ</font>��" +
							"����������<font color='#31B2FE'>" + androidRateD + "</font>�Ļ��Ϣ���Ƿ�Ҫ������"));
				}
			}
		}

		AlertDialog.Builder builder=new AlertDialog.Builder(this, R.style.Dialog_Transparent);  //�ȵõ�������
		builder.setView(contentView);
		builder.setCancelable(true);
		final AlertDialog dialog = builder.create();
		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				showInvestDialog();
			}
		});
		rightBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		//��������������ˣ���������ʾ����
		dialog.show();
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = display.getWidth()*6/7;
		dialog.getWindow().setAttributes(lp);
	}

	/**
	 * ȷ��Ͷ�ʵ�dialog
	 */
	private void showInvestDialog() {
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.invest_prompt_layout, null);
		LinearLayout yuanMoneyLayout = (LinearLayout) contentView.findViewById(R.id.invest_prompt_yjb_layout_detail);//Ԫ��ұ�ע����
		RelativeLayout hbLayout = (RelativeLayout) contentView.findViewById(R.id.invest_prompt_hb_layout_detail);//�������
		LinearLayout yjbjxqLayout = (LinearLayout) contentView.findViewById(R.id.invest_prompt_yjbjxq_layout_detail);//Ԫ��Һͼ�Ϣȯʹ�õĲ���
		Button sureBtn = (Button) contentView
				.findViewById(R.id.invest_prompt_layout_surebtn);
		Button cancelBtn = (Button) contentView
				.findViewById(R.id.invest_prompt_layout_cancelbtn);
		TextView totalMoneyTV = (TextView) contentView
				.findViewById(R.id.invest_prompt_layout_total);
		TextView benjinTV = (TextView) contentView
				.findViewById(R.id.invest_prompt_layout_benjin);
		TextView yuanMoneyTV = (TextView) contentView
				.findViewById(R.id.invest_prompt_layout_yuan);
		TextView hbMoneyTV = (TextView) contentView
				.findViewById(R.id.invest_prompt_layout_hb_count);
		TextView yjbjxqText = (TextView) contentView.findViewById(R.id.invest_prompt_layout_yjbjxq_count);//Ԫ��Ҽ�Ϣȯ ��ʹ�ö���
		
		//�������������
		LinearLayout twoyearsLayoutMain = 
				(LinearLayout) contentView.findViewById(R.id.invest_prompt_layout_twoyears_layout);
		LinearLayout twoyearsLayoutTop = 
				(LinearLayout) contentView.findViewById(R.id.invest_prompt_layout_twoyears_layouttop);
		LinearLayout twoyearsLayoutBottom = 
				(LinearLayout) contentView.findViewById(R.id.invest_prompt_layout_twoyears_layoutbottom);
		TextView twoyearsInterestMoney = (TextView) contentView.findViewById(R.id.invest_prompt_layout_twoyears_interestmoney);
		TextView twoyearsInvestMoney = (TextView) contentView.findViewById(R.id.invest_prompt_layout_twoyears_investmoney);
		TextView twoyearsPrompt = (TextView) contentView.findViewById(R.id.invest_prompt_layout_twoyears_prompt);
		if(SettingsManager.checkTwoYearsTZFXActivity()){
			//������������ֻ���ڽ����С���
			twoyearsLayoutMain.setVisibility(View.VISIBLE);
			if(moneyInvest < 10000){
				twoyearsLayoutTop.setVisibility(View.GONE);
				twoyearsLayoutBottom.setVisibility(View.VISIBLE);
				twoyearsInvestMoney.setText("1��");
				twoyearsPrompt.setText("Ԫ�ɻ�÷��ֽ���Ŷ");
			}else if(moneyInvest >= 10000 && moneyInvest < 50000){
				twoyearsLayoutTop.setVisibility(View.VISIBLE);
				twoyearsLayoutBottom.setVisibility(View.VISIBLE);
				twoyearsInvestMoney.setText("5��");
				twoyearsPrompt.setText("Ԫ�ɻ�ø��߱������ֽ���Ŷ");
				twoyearsInterestMoney.setText(Util.double2PointDouble(moneyInvest*0.002*limitInvest/365));
			}else{
				//Ͷ�ʽ�����5��
				twoyearsLayoutTop.setVisibility(View.VISIBLE);
				twoyearsLayoutBottom.setVisibility(View.GONE);
				twoyearsInterestMoney.setText(Util.double2PointDouble(moneyInvest*0.004*limitInvest/365));
			}
		}
		if(bonusMoney > 0){
			//ʹ��Ԫ���
			yjbjxqText.setText(String.valueOf(bonusMoney));
			yuanMoneyLayout.setVisibility(View.VISIBLE);
			yjbjxqLayout.setVisibility(View.VISIBLE);
		}else{
			yuanMoneyLayout.setVisibility(View.GONE);
			yjbjxqLayout.setVisibility(View.GONE);
		}
		//ʹ�ú��
		if(hbMoney > 0){
			hbLayout.setVisibility(View.VISIBLE);
		}else{
			hbLayout.setVisibility(View.GONE);
		}
		//ʹ�ü�Ϣȯ
		if(jxqMoney > 0){
			yjbjxqLayout.setVisibility(View.VISIBLE);
			yjbjxqText.setText(Util.formatRate(String.valueOf(jxqMoney))+"%�ļ�Ϣȯ");
		}else{
			yjbjxqLayout.setVisibility(View.GONE);
		}
		double benjinD = moneyInvest - bonusMoney;
		totalMoneyTV.setText(moneyInvest + "");
		yuanMoneyTV.setText(bonusMoney + "");
		benjinTV.setText((int)benjinD + "");
		hbMoneyTV.setText(Util.formatRate(String.valueOf(hbMoney))+"Ԫ���");

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
		String floatRateStr = "0";
		String investMoneyStr = investMoneyET.getText().toString();
		int investMoneyInt = 0;
		try {
			investMoneyInt = Integer.parseInt(investMoneyStr);
		} catch (Exception e) {
		}
		if (investMoneyInt <= 100) {
			investMoneyInt = 0;
		} else {
			investMoneyInt -= 100;
		}
		if (investMoneyInt <= 0) {
			investMoneyET.setText(null);
		} else {
			investMoneyET.setText(investMoneyInt + "");
		}
		if(investMoneyInt < 100000 || mProductInfo.getInterest_period().contains("365")){
			floatRateStr = "0";
		}else if(investMoneyInt >= 100000 && investMoneyInt <300000){
			floatRateStr = "0.1";
		}else if(investMoneyInt >= 300000 && investMoneyInt < 500000){
			floatRateStr = "0.2";
		}else if(investMoneyInt >= 500000){
			floatRateStr = "0.3";
		}
		computeIncome(mProductInfo.getInterest_rate(),
				mProductInfo.getAndroid_interest_rate(), floatRateStr,(String)jxqArrowLayout.getTag(),investMoneyInt,
				(String)hbArrowLayout.getTag(R.id.tag_first),mProductInfo.getInvest_horizon());
	}

	/**
	 * ����
	 */
	private void investMoneyIncrease() {
		String floatRateStr = "0";
		String investMoneyStr = investMoneyET.getText().toString();
		int investMoneyInt = 0;
		try {
			investMoneyInt = Integer.parseInt(investMoneyStr);
		} catch (Exception e) {
		}
		investMoneyInt += 100;
		investMoneyET.setText(investMoneyInt + "");
		if(investMoneyInt < 100000 || mProductInfo.getInterest_period().contains("365")){
			floatRateStr = "0";
		}else if(investMoneyInt >= 100000 && investMoneyInt <300000){
			floatRateStr = "0.1";
		}else if(investMoneyInt >= 300000 && investMoneyInt < 500000){
			floatRateStr = "0.2";
		}else if(investMoneyInt >= 500000){
			floatRateStr = "0.3";
		}
		computeIncome(mProductInfo.getInterest_rate(),
				mProductInfo.getAndroid_interest_rate(),floatRateStr,(String)jxqArrowLayout.getTag(), investMoneyInt,
				(String)hbArrowLayout.getTag(R.id.tag_first),mProductInfo.getInvest_horizon());
	}

	/**
	 * ѡ���Ϣȯ�ͺ����ʱ��ˢ��Ԥ������
	 */
	private void updateInterest(){
		String floatRateStr = "0";
		String investMoneyStr = investMoneyET.getText().toString();
		int investMoneyInt = 0;
		try {
			investMoneyInt = Integer.parseInt(investMoneyStr);
		} catch (Exception e) {
		}
		if(investMoneyInt < 100000 || mProductInfo.getInterest_period().contains("365")){
			floatRateStr = "0";
		}else if(investMoneyInt >= 100000 && investMoneyInt <300000){
			floatRateStr = "0.1";
		}else if(investMoneyInt >= 300000 && investMoneyInt < 500000){
			floatRateStr = "0.2";
		}else if(investMoneyInt >= 500000){
			floatRateStr = "0.3";
		}
		computeIncome(mProductInfo.getInterest_rate(),
				mProductInfo.getAndroid_interest_rate(),floatRateStr,(String)jxqArrowLayout.getTag(), investMoneyInt,
				(String)hbArrowLayout.getTag(R.id.tag_first),mProductInfo.getInvest_horizon());
	}
	
	private void resetInvestMoneyET() {
		if (investMoneyET != null) {
			investMoneyET.setText(null);
			yjsyText.setText("0.00");
			yuanUsedTV.setText("0");
		}
	}

	private ScheduledExecutorService myExecuter;

	private void updateInvestCounter(final View v) {
		myExecuter = Executors.newSingleThreadScheduledExecutor();
		myExecuter.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				if (v.getId() == R.id.bid_zxd_activity_increase_btn) {
					handler.sendEmptyMessage(REQUEST_INVEST_INCREASE);
				} else if (v.getId() == R.id.bid_zxd_activity_discend_btn) {
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
	 * �������ʼ���Ԥ������
	 * @param rateStr	��������
	 * @param extraRateStr  ��Ϣ���ʣ�android_interest_rate��
	 * @param floatRateStr ��������
	 * @param couponRateStr ��Ϣȯ���� 
	 * @param investMoney Ͷ�ʽ��
	 * @param  hbMoney ������
	 * @param daysStr Ͷ������
	 * @return
	 */
	private String computeIncome(String rateStr, String extraRateStr,String floatRateStr,String couponRateStr,
			int investMoney, String hbMoney,String daysStr) {
		double rateD = 0d;
		double extraRateD = 0d;
		double floatRateD = 0d;
		double couponRateD = 0d;
		int days = 0;
		double hbMoneyD = 0d;
		try {
			rateD = Double.parseDouble(rateStr);
		} catch (Exception e) {
		}
		try {
			floatRateD = Double.parseDouble(floatRateStr);
		} catch (Exception e) {
		}
		try {
			extraRateD = Double.parseDouble(extraRateStr);
		} catch (Exception e) {
		}
		try {
			couponRateD = Double.parseDouble(couponRateStr);
		} catch (Exception e) {
		}
		try{
			hbMoneyD = Double.parseDouble(hbMoney);
		}catch (Exception e){

		}
		days = limitInvest;
		int flagNov = SettingsManager.checkActiveStatusBySysTime(mProductInfo.getAdd_time(),
				SettingsManager.activeNov2017_StartTime,SettingsManager.activeNov2017_EndTime);//11��˫ʮһ��Ϣ�
		if(SettingsManager.isCompanyUser(getApplicationContext())){
			nhsyText.setText(Util.double2PointDouble((rateD + floatRateD)) + "%");//�������
		}else{
			nhsyText.setText(Util.double2PointDouble((rateD + floatRateD + extraRateD)) + "%");//�������
		}

		double income = 0d;
		if(SettingsManager.isCompanyUser(getApplicationContext())){
			income = (rateD + floatRateD + couponRateD) * investMoney * days / 36500
					+ (rateD + floatRateD) * hbMoneyD * days / 36500;//���Ϻ�������棬���ֻ���Ʒ����Ļ������ʲ���������
		}else{
			income = (rateD + extraRateD + floatRateD + couponRateD) * investMoney * days / 36500
					+ (rateD + floatRateD) * hbMoneyD * days / 36500;//���Ϻ�������棬���ֻ���Ʒ����Ļ������ʲ���������
		}

		DecimalFormat df = new java.text.DecimalFormat("#.00");
		if (income < 1) {
			yjsyText.setText("0" + df.format(income));
		} else {
			yjsyText.setText(df.format(income));
		}
		return df.format(income);
	}

	/**
	 * �����ʹ�õ�Ԫ���
	 * 
	 * @param inputMoney
	 */
	private void computeUsedYuanMoney(int inputMoney) {
		int usedTemp = 0;
		if (limitInvest <= 35) {
			usedTemp = inputMoney / 500;
		} else if (limitInvest > 35 && limitInvest <= 95) {
			usedTemp = inputMoney / 200;
		} else {
			usedTemp = inputMoney / 100;
		}

		int usedYuanTotalInt = 0;// Ԫ��ҵ��ܶ�
		float usedYuanTotalFloat = 0;
		try {
			if (mUserYUANAccountInfo != null) {
				usedYuanTotalFloat = Float.parseFloat(mUserYUANAccountInfo
						.getUse_coin());
				usedYuanTotalInt = (int) usedYuanTotalFloat;
			}
		} catch (Exception e) {
		}
		if (usedTemp <= usedYuanTotalInt) {
			yuanUsedTV.setText(usedTemp + "");
		} else {
			yuanUsedTV.setText(usedYuanTotalInt + "");
		}

	}

	/**
	 * ��������Ͷ�ʽӿ�
	 * 
	 * @param borrowId
	 * @param investUserId
	 * @param money
	 * @param bonusMoney
	 *            Ԫ���
	 * @param investFrom
	 * @param investFromSub
	 * @param experienceCode
	 *            �������
	 * @param investFromHost
	 * @param merPriv
	 * @param redBagLogId ���id
	 * @param couponLogId ��Ϣȯid
	 */
	private void requestInvest(String borrowId, String investUserId,
			String money, int bonusMoney, String investFrom,
			String investFromSub, String experienceCode, String investFromHost,
			String merPriv, String redBagLogId,String couponLogId) {
		if (mLoadingDialog != null && !isFinishing()) {
			mLoadingDialog.show();
		}
		AsyncBorrowInvest asyncBorrowInvest = new AsyncBorrowInvest(
				BidZXDActivity.this, borrowId, investUserId, money, bonusMoney,
				investFrom, investFromSub, experienceCode, investFromHost,
				merPriv, redBagLogId, couponLogId,new OnBorrowInvestInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
							mLoadingDialog.dismiss();
						}

						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								Message msg = handler
										.obtainMessage(REQUEST_INVEST_SUCCESS);
								msg.obj = baseInfo;
								handler.sendMessage(msg);
							} else {
								Message msg = handler
										.obtainMessage(REQUEST_INVEST_EXCEPTION);
								msg.obj = baseInfo;
								handler.sendMessage(msg);
							}
						} else {
							Message msg = handler
									.obtainMessage(REQUEST_INVEST_FAILE);
							handler.sendMessage(msg);
						}
					}
				});
		asyncBorrowInvest.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * �û��˻���Ϣ
	 */
	private void requestUserAccountInfo(String userId) {
		AsyncYiLianRMBAccount yilianTask = new AsyncYiLianRMBAccount(
				BidZXDActivity.this, userId, new OnCommonInter() {
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

	/**
	 * Ԫ����˻�
	 * 
	 * @param userId
	 */
	private void requestYuanAccountInfo(String userId) {
		AsyncUserYUANAccount yuanAccountTask = new AsyncUserYUANAccount(
				BidZXDActivity.this, userId, new OnUserYUANAccountInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							sysTimeStr = baseInfo.getTime();
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if (resultCode == 0) {
								mUserYUANAccountInfo = baseInfo.getYuanAccountInfo();
								if (mUserYUANAccountInfo != null) {
									yuanBalanceTV.setText(Util.formatRate(mUserYUANAccountInfo
											.getUse_coin()));
								}
							}
						}else{
							sysTimeStr = sdf.format(new Date());
						}
						checkYjbLayoutVisible();
					}
				});
		yuanAccountTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * �����
	 * 
	 * @param status
	 * @param userId
	 * @param putStatus
	 * @param activeTitle
	 */
	private void requestExperiencePageInfoByStatus(String status,
			String userId, String putStatus, String activeTitle) {
		AsyncExperiencePageInfo experienceTask = new AsyncExperiencePageInfo(
				BidZXDActivity.this, status, userId, putStatus, activeTitle,
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							sysTimeStr = baseInfo.getTime();
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if (resultCode == 0) {
								if ("����".equals(mProductInfo.getIs_TYJ())) {
									// �������
									tyjLayout.setVisibility(View.VISIBLE);
								} else {
									// �����ر�
									tyjLayout.setVisibility(View.GONE);
								}
								TYJPageInfo pageInfo = baseInfo
										.getmTYJPageInfo();
								if (pageInfo != null) {
									experienceList.addAll(pageInfo
											.getTyjList());
								}
							} else {
								tyjLayout.setVisibility(View.GONE);
							}
						}else{
							sysTimeStr = sdf.format(new Date());
						}
					}
				});
		experienceTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * ��ȡ��ǰ�û��Ŀ��ú�����б�
	 * 
	 * @param userId
	 * @param borrowType
	 */
	private void requestHBPageInfoByBorrowType(String userId, String borrowType) {
		AsyncCurrentUserRedbagList currentUserRedbagListTask = new AsyncCurrentUserRedbagList(
				BidZXDActivity.this, userId, borrowType, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							sysTimeStr = baseInfo.getTime();
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								RedBagPageInfo pageInfo = baseInfo
										.getmRedBagPageInfo();
								if (pageInfo != null) {
									hbList.addAll(pageInfo.getRedbagList());
								}
								if (hbList.size() < 1) {
									hbLayout.setVisibility(View.GONE);
									line4.setVisibility(View.GONE);
									line5.setVisibility(View.GONE);
								} else {
//									if (extraRateF <= 0) {
										hbLayout.setVisibility(View.VISIBLE);
										line4.setVisibility(View.VISIBLE);
										line5.setVisibility(View.VISIBLE);
										usePromptList.add("���");
										updateUsePrompt(usePromptList);
//									} else {
//										hbLayout.setVisibility(View.GONE);
//										line5.setVisibility(View.GONE);
//									}
								}
							} else {
								hbLayout.setVisibility(View.GONE);
								line4.setVisibility(View.GONE);
								line5.setVisibility(View.GONE);
							}
						}else{
							sysTimeStr = sdf.format(new Date());
						}
					}
				});
		currentUserRedbagListTask
				.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * ��Ϣȯ
	 * @param userId
	 * @param useStatus
	 */
	private void requestJXQList(String userId, String useStatus) {
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		AsyncJXQPageInfo redbagTask = new AsyncJXQPageInfo(BidZXDActivity.this, userId,useStatus,
				String.valueOf(1),String.valueOf(100), new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(mLoadingDialog != null && mLoadingDialog.isShowing()){
							mLoadingDialog.dismiss();
						}
						if (baseInfo != null) {
							sysTimeStr = baseInfo.getTime();
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								Message msg = handler
										.obtainMessage(REQUEST_JXQ_LIST_SUCCESS);
								msg.obj = baseInfo;
								handler.sendMessage(msg);
							} 
						}else{
							sysTimeStr = sdf.format(new Date());
						}
					}
				});
		redbagTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	private void showTYJListWindow() {
		if (experienceList == null || experienceList.size() < 1) {
			Util.toastLong(BidZXDActivity.this, "û����������");
			return;
		}
		View popView = LayoutInflater.from(this).inflate(
				R.layout.tyj_list_popwindow, null);
		int[] screen = SettingsManager.getScreenDispaly(BidZXDActivity.this);
		int width = screen[0];
		int height = screen[1] * 1 / 5;
		TYJListPopupwindow popwindow = new TYJListPopupwindow(
				BidZXDActivity.this, popView, width, height);
		popwindow.show(mainLayout, experienceList,
				new OnTYJWindowItemClickListener() {
					@Override
					public void onItemClickListener(View view, int position) {
						TYJInfo info = experienceList.get(position);
						tiyanjinET.setText(info.getActive_title());
					}
				});
	}

	/**
	 * ���
	 */
	private void showHBListWindow() {
		if (hbList == null || hbList.size() < 1) {
			Util.toastLong(BidZXDActivity.this, "û�к������");
			return;
		}
		View popView = LayoutInflater.from(this).inflate(
				R.layout.tyj_list_popwindow, null);
		int[] screen = SettingsManager.getScreenDispaly(BidZXDActivity.this);
		int width = screen[0];
		int height = screen[1] * 1 / 3 + getResources().getDimensionPixelSize(R.dimen.common_measure_100dp);
		int hbCurPosition = 0;
		try{
			hbCurPosition = (int)hbArrowLayout.getTag(R.id.tag_third);
		}catch (Exception e){
			hbCurPosition = 0;
		}
		HBListPopupwindow popwindow = new HBListPopupwindow(
				BidZXDActivity.this, popView, width, height,"ѡ���� (��"+hbList.size()+"��)",hbCurPosition);
		popwindow.show(mainLayout, hbList, new OnHBWindowItemClickListener() {
			@Override
			public void onItemClickListener(View view, int position) {
				hbArrowLayout.setTag(R.id.tag_third,position);
				if (position == 0) {
					hbEditText.setText("");
					hbEditText.setTag("");
					hbArrowLayout.setTag(R.id.tag_first,"0");
					hbArrowLayout.setTag(R.id.tag_second,"0");
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
						hbEditText.setText("");
						hbArrowLayout.setTag(R.id.tag_third,0);
						Util.toastLong(BidZXDActivity.this, "����Ͷ�ʽ�������Ҫ��");
					}else{
						//ͬʱ�����������ֵ���
						yuanMoneyET.setText(null);
						jxqEditText.setText(null);
						jxqEditText.setTag("");
						jxqArrowLayout.setTag("0");
						jxqArrowLayout.setTag(R.id.tag_third,0);

						if(limitMoney >= 10000){
							hbEditText.setText(Html.fromHtml("<font color='#31B2FE'>"+Util.formatRate(info.getMoney())+"Ԫ</font>�����"
									+"��Ͷ��"+Util.formatRate(String.valueOf(limitMoney/10000d))+"��Ԫ�����Ͽ���"));
						}else{
							hbEditText.setText(Html.fromHtml("<font color='#31B2FE'>"+Util.formatRate(info.getMoney())+"Ԫ</font>�����"
									+"��Ͷ��"+info.getNeed_invest_money()+"Ԫ�����Ͽ���"));
						}

						hbEditText.setTag(info.getId());
						hbArrowLayout.setTag(R.id.tag_first,info.getMoney());
						hbArrowLayout.setTag(R.id.tag_second,info.getNeed_invest_money());
						updateInterest();//���Ҳ������Ϣ
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
			Util.toastLong(BidZXDActivity.this, "û�м�Ϣȯ����");
			return;
		}
		View popView = LayoutInflater.from(this).inflate(
				R.layout.tyj_list_popwindow, null);
		int[] screen = SettingsManager.getScreenDispaly(BidZXDActivity.this);
		int width = screen[0];
		int height = screen[1] * 1 / 3 + getResources().getDimensionPixelSize(R.dimen.common_measure_100dp);
		int jxqCurPosition = 0;
		try{
			jxqCurPosition = (int)jxqArrowLayout.getTag(R.id.tag_third);
		}catch (Exception e){
			jxqCurPosition = 0;
		}
		JXQListPopupwindow popwindow = new JXQListPopupwindow(
				BidZXDActivity.this, popView, width, height,"ѡ���Ϣȯ (��"+jxqList.size()+"��)",jxqCurPosition);
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
						Util.toastLong(BidZXDActivity.this, "����Ͷ�ʽ������ϢȯҪ��");
					}else{
						//ͬʱ�����������ֵ���
						yuanMoneyET.setText(null);
						hbEditText.setText("");
						hbEditText.setTag("");
						hbArrowLayout.setTag(R.id.tag_first,"0");
						hbArrowLayout.setTag(R.id.tag_second,"0");
						hbArrowLayout.setTag(R.id.tag_third,0);

						if(limitMoney >= 10000){
							jxqEditText.setText(Html.fromHtml("<font color='#31B2FE'>"+Util.formatRate(info.getMoney())+"%</font>��Ϣȯ��"
									+"��Ͷ��"+Util.formatRate(String.valueOf(limitMoney/10000))+"��Ԫ�����Ͽ���"));
						}else{
							jxqEditText.setText(Html.fromHtml("<font color='#31B2FE'>"+Util.formatRate(info.getMoney())+"%</font>��Ϣȯ��"+"��Ͷ��"+(int)(limitMoney)+"Ԫ�����Ͽ���"));
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
	 * ������б���itemʱ�Ļص�
	 * 
	 * @author Administrator
	 * 
	 */
	public interface OnTYJWindowItemClickListener {
		void onItemClickListener(View view, int position);
	}

	/**
	 * ����б���Item�ļ����¼�
	 * 
	 * @author Administrator
	 * 
	 */
	public interface OnHBWindowItemClickListener {
		void onItemClickListener(View v, int position);
	}
}
