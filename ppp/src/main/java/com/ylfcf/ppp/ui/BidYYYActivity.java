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
import com.ylfcf.ppp.async.AsyncCurrentUserRedbagList;
import com.ylfcf.ppp.async.AsyncJXQPageInfo;
import com.ylfcf.ppp.async.AsyncYYYInvest;
import com.ylfcf.ppp.async.AsyncYiLianRMBAccount;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.JiaxiquanInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.RedBagInfo;
import com.ylfcf.ppp.entity.RedBagPageInfo;
import com.ylfcf.ppp.entity.UserRMBAccountInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnIsBindingListener;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.ui.BidZXDActivity.OnHBWindowItemClickListener;
import com.ylfcf.ppp.util.Constants;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.view.HBListPopupwindow;
import com.ylfcf.ppp.view.JXQListPopupwindow;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Ͷ��ҳ��---Ԫ��ӯ
 * @author Mr.liu
 *
 */
public class BidYYYActivity extends BaseActivity implements OnClickListener{
	private static final String className = "BidYYYActivity";
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
	private Button rechargeBtn;// ��ֵ

	private Button descendBtn;// �ݼ���ť
	private Button increaseBtn;// ������ť
	private EditText investMoneyET;
	private ImageView deleteImg;// x��
	private TextView yjsyText;// Ԥ������
	private TextView yyyCompact;//Ԫ��ӯ���Э��
	private CheckBox compactCB;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private List<JiaxiquanInfo> jxqList = new ArrayList<JiaxiquanInfo>();//��ʹ�õļ�Ϣȯ�б�
	private List<RedBagInfo> hbList = new ArrayList<RedBagInfo>();// �û�δʹ�õĺ���б�
	private LinearLayout hbLayout;//�������
	private EditText hbET;//��������
	private View line1,line2,line3;
	private RelativeLayout hbArrowLayout;
	private double hbMoney = 0;//������
	private double jxqMoney = 0d;//��Ϣȯ

	private LinearLayout jxqLayout;// ��Ϣȯ
	private EditText jxqEditText;
	private RelativeLayout jxqArrowLayout;// ��Ϣȯ�ļ�ͷ
	private TextView usePrompt;//ʹ��˵������Ϣȯ��Ԫ��Ҳ���ͬʱʹ�õ�
	private TextView daojuPrompt;
	private List<String> usePromptList = new ArrayList<String>();

	private ProductInfo mProductInfo;
	private int moneyInvest = 0;
	private Button investBtn;
	private LinearLayout mainLayout;

	private int limitInvest = 0;// Ͷ������

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_INVEST_WHAT:
				requestInvest(mProductInfo.getId(),
						SettingsManager.getUserId(getApplicationContext()),
						String.valueOf(moneyInvest),SettingsManager.getUserFromSub(getApplicationContext()),String.valueOf(jxqEditText.getTag()),
						String.valueOf(hbET.getTag()));
				break;
			case REQUEST_INVEST_SUCCESS:
				BaseInfo baseInfo = (BaseInfo) msg.obj;
				Intent intentSuccess = new Intent(BidYYYActivity.this,
						BidSuccessActivity.class);
				intentSuccess.putExtra("from_where", "Ԫ��ӯ");
				intentSuccess.putExtra("base_info",baseInfo);
				startActivity(intentSuccess);
				mApp.finishAllActivityExceptMain();
				break;
			case REQUEST_INVEST_EXCEPTION:
				BaseInfo base = (BaseInfo) msg.obj;
				Util.toastShort(BidYYYActivity.this, base.getMsg());
				break;
			case REQUEST_INVEST_FAILE:
				Util.toastShort(BidYYYActivity.this, "�����쳣");
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
						if("δʹ��".equals(info.getUse_status()) && info.getBorrow_type().contains("Ԫ��ӯ����")){
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
		setContentView(R.layout.bid_yyy_activity);

		mProductInfo = (ProductInfo) getIntent().getSerializableExtra(
				"PRODUCT_INFO");
		if (mProductInfo != null) {
			try {
				String limitStr = mProductInfo.getInterest_period().replace("��","");
				limitInvest = Integer.parseInt(limitStr);
			} catch (Exception e) {
			}
			requestHBPageInfoByBorrowType(
					SettingsManager.getUserId(getApplicationContext()),"Ԫ��ӯ����");
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
		requestUserAccountInfo(SettingsManager.getUserId(getApplicationContext()));
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
		topTitleTV.setText("Ԫ��ӯͶ��");

		borrowName = (TextView) findViewById(R.id.bid_yyy_activity_borrow_name);
		borrowName.setText(mProductInfo.getBorrow_name());
		userBalanceTV = (TextView) findViewById(R.id.bid_yyy_activity_user_balance);
		borrowBalanceTV = (TextView) findViewById(R.id.bid_yyy_activity_borrow_balance);
		rechargeBtn = (Button) findViewById(R.id.bid_yyy_activity_recharge_btn);
		rechargeBtn.setOnClickListener(this);
		descendBtn = (Button) findViewById(R.id.bid_yyy_activity_discend_btn);
		descendBtn.setOnClickListener(this);
		descendBtn.setOnTouchListener(mOnTouchListener);
		increaseBtn = (Button) findViewById(R.id.bid_yyy_activity_increase_btn);
		increaseBtn.setOnClickListener(this);
		increaseBtn.setOnTouchListener(mOnTouchListener);
		investMoneyET = (EditText) findViewById(R.id.bid_yyy_activity_invest_et);
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
						Util.toastLong(BidYYYActivity.this, "Ͷ�ʽ���Ϊ0");
					} else if (investMoney % 100 != 0) {
						Util.toastLong(BidYYYActivity.this, "Ͷ�ʽ�����Ϊ100��������");
					}

					// �ж�Ͷ�ʽ���Ƿ���ڱ����ʣ���
					String borrowBalanceStr = String.valueOf(borrowBalanceTemp);
					borrowBalanceDouble = Double.parseDouble(borrowBalanceStr);
					if (investMoney > borrowBalanceDouble) {
						Util.toastLong(BidYYYActivity.this, "���ʣ���Ͷ����");
					}

				} catch (Exception e) {
				}

			}
		});
		deleteImg = (ImageView) findViewById(R.id.bid_yyy_activity_delete);
		deleteImg.setOnClickListener(this);
		yjsyText = (TextView) findViewById(R.id.bid_yyy_activity_yjsy);
		yyyCompact = (TextView) findViewById(R.id.bid_yyy_activity_compact_text);
		yyyCompact.setOnClickListener(this);
		compactCB = (CheckBox) findViewById(R.id.bid_yyy_activity_cb);
		usePrompt = (TextView) findViewById(R.id.bid_yyy_activity_use_prompt);
		daojuPrompt = (TextView) findViewById(R.id.bid_yyy_activity_daoju_prompt);

		investBtn = (Button) findViewById(R.id.bid_yyy_activity_borrow_bidBtn);
		investBtn.setOnClickListener(this);
		mainLayout = (LinearLayout) findViewById(R.id.bid_yyy_activity_mainlayout);

		hbLayout = (LinearLayout) findViewById(R.id.bid_yyy_activity_hb_layout);
		hbLayout.setOnClickListener(this);
		hbET = (EditText) findViewById(R.id.bid_yyy_activity_hb_et);
		hbET.setOnClickListener(this);
		hbArrowLayout = (RelativeLayout) findViewById(R.id.bid_yyy_activity_hb_arrow_layout);
		hbArrowLayout.setOnClickListener(this);
		line1 = findViewById(R.id.bid_yyy_activity_line1);
		line2 = findViewById(R.id.bid_yyy_activity_line2);
		line3 = findViewById(R.id.bid_yyy_activity_line3);
		jxqLayout = (LinearLayout) findViewById(R.id.bid_yyy_activity_jxq_layout);
		jxqLayout.setOnClickListener(this);
		jxqEditText = (EditText) findViewById(R.id.bid_yyy_activity_jxq_et);
		jxqEditText.setOnClickListener(this);
		jxqArrowLayout = (RelativeLayout) findViewById(R.id.bid_yyy_activity_jxq_arrow_layout);
		jxqArrowLayout.setOnClickListener(this);
	}

	private void hbLayoutVisible(int isVisible){
		line1.setVisibility(isVisible);
		hbLayout.setVisibility(isVisible);
		line2.setVisibility(isVisible);
	}

	private void jxqLayoutVisible(int isVisible){
		line2.setVisibility(isVisible);
		jxqLayout.setVisibility(isVisible);
		line3.setVisibility(isVisible);
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
						(String)hbArrowLayout.getTag(),mProductInfo.getInterest_period());
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
		case R.id.bid_yyy_activity_borrow_bidBtn:
			borrowInvest();
			break;
		case R.id.bid_yyy_activity_recharge_btn:
			//ȥ��ֵ
			if(SettingsManager.isPersonalUser(getApplicationContext())){
				checkIsVerify("��ֵ");
			}else{
				Intent intent = new Intent(BidYYYActivity.this,RechargeCompActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.bid_yyy_activity_discend_btn:
			// �ݼ�
			investMoneyDescend();
			break;
		case R.id.bid_yyy_activity_increase_btn:
			investMoneyIncrease();
			break;
		case R.id.bid_yyy_activity_delete:
			resetInvestMoneyET();
			break;
		case R.id.bid_yyy_activity_compact_text:
			Intent intent = new Intent(BidYYYActivity.this,CompactActivity.class);
			intent.putExtra("from_where", "yyy");
			startActivity(intent);
			break;
		case R.id.bid_yyy_activity_jxq_arrow_layout:
		case R.id.bid_yyy_activity_jxq_layout:
		case R.id.bid_yyy_activity_jxq_et:
			checkJXQ();
			break;
		case R.id.bid_yyy_activity_hb_layout:
		case R.id.bid_yyy_activity_hb_arrow_layout:
		case R.id.bid_yyy_activity_hb_et:
			checkHB();
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
		RequestApis.requestIsVerify(BidYYYActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(flag){
					//�û��Ѿ�ʵ��
					checkIsBindCard(type);
				}else{
					//�û�û��ʵ��
					Intent intent = new Intent(BidYYYActivity.this,UserVerifyActivity.class);
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
		RequestApis.requestIsBinding(BidYYYActivity.this, SettingsManager.getUserId(getApplicationContext()), "����", new OnIsBindingListener() {
			@Override
			public void isBinding(boolean flag, Object object) {
				Intent intent = new Intent();
				if(flag){
					//�û��Ѿ���
					if("��ֵ".equals(type)){
						//��ôֱ��������ֵҳ��
						intent.setClass(BidYYYActivity.this, RechargeActivity.class);
					}
				}else{
					//�û���û�а�
					Bundle bundle = new Bundle();
					bundle.putString("type", type);
					intent.putExtra("bundle", bundle);
					intent.setClass(BidYYYActivity.this, BindCardActivity.class);
				}
				startActivity(intent);
				rechargeBtn.setEnabled(true);
			}
		});
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
			Util.toastLong(BidYYYActivity.this, "û�к������");
			return;
		}
		View popView = LayoutInflater.from(this).inflate(
				R.layout.tyj_list_popwindow, null);
		int[] screen = SettingsManager.getScreenDispaly(BidYYYActivity.this);
		int width = screen[0];
		int height = screen[1] * 1 / 3 + getResources().getDimensionPixelSize(R.dimen.common_measure_100dp);
		int hbCurPosition = 0;
		try{
			hbCurPosition = (int)hbArrowLayout.getTag(R.id.tag_third);
		}catch (Exception e){
			hbCurPosition = 0;
		}
		HBListPopupwindow popwindow = new HBListPopupwindow(
				BidYYYActivity.this, popView, width, height,"ѡ���� (��"+hbList.size()+"��)",hbCurPosition);
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
						Util.toastLong(BidYYYActivity.this, "����Ͷ�ʽ�������Ҫ��");
					}else{
						//������Ϣ��������
						jxqEditText.setText(null);
						jxqEditText.setTag("");
						jxqArrowLayout.setTag("0");
						jxqArrowLayout.setTag(R.id.tag_third,0);
						if(limitMoney < 10000){
							hbET.setText(Html.fromHtml("<font color='#31B2FE'>"+Util.formatRate(info.getMoney())+"Ԫ</font>�����"
									+"��Ͷ��"+info.getNeed_invest_money()+"Ԫ�����Ͽ���"));
						}else{
							hbET.setText(Html.fromHtml("<font color='#31B2FE'>"+Util.formatRate(info.getMoney())+"Ԫ</font>�����"
									+"��Ͷ��"+Util.formatRate(String.valueOf(limitMoney/10000d))+"��Ԫ�����Ͽ���"));
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
			Util.toastLong(BidYYYActivity.this, "û�м�Ϣȯ����");
			return;
		}
		View popView = LayoutInflater.from(this).inflate(
				R.layout.tyj_list_popwindow, null);
		int[] screen = SettingsManager.getScreenDispaly(BidYYYActivity.this);
		int width = screen[0];
		int height = screen[1] * 1 / 3 + getResources().getDimensionPixelSize(R.dimen.common_measure_100dp);
		int jxqCurPosition = 0;
		try{
			jxqCurPosition = (int)jxqArrowLayout.getTag(R.id.tag_third);
		}catch (Exception e){
			jxqCurPosition = 0;
		}
		JXQListPopupwindow popwindow = new JXQListPopupwindow(
				BidYYYActivity.this, popView, width, height,"ѡ���Ϣȯ (��"+jxqList.size()+"��)",jxqCurPosition);
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
						Util.toastLong(BidYYYActivity.this, "����Ͷ�ʽ������ϢȯҪ��");
					}else{
						hbET.setText("");
						hbET.setTag("");
						hbArrowLayout.setTag("0");
						hbArrowLayout.setTag(R.id.tag_third,0);

						if(limitMoney >= 10000){
							jxqEditText.setText(Html.fromHtml("<font color='#31B2FE'>"+Util.formatRate(info.getMoney())
									+"%</font>��Ϣȯ��"+"��Ͷ��"+Util.formatRate(String.valueOf(limitMoney/10000))+"��Ԫ�����Ͽ���"));
						}else{
							jxqEditText.setText(Html.fromHtml("<font color='#31B2FE'>"+Util.formatRate(info.getMoney())+"%</font>��Ϣȯ��"
									+"��Ͷ��"+(int)(limitMoney)+"Ԫ�����Ͽ���"));
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
				(String)hbArrowLayout.getTag(),mProductInfo.getInterest_period());
	}

	private void borrowInvest() {
		hbMoney = 0;
		jxqMoney = 0;
		String moneyStr = investMoneyET.getText().toString();
		try {
			moneyInvest = Integer.parseInt(moneyStr);
		} catch (Exception e) {
			moneyInvest = 0;
		}
		double needRechargeMoeny = 0d;// ��Ҫ֧���Ľ�� ���� ����Ľ���ȥ����Ԫ��ҵĽ��
		double userBanlanceDouble = 0d;
		double borrowBalanceDouble = 0d;
		try {
			hbMoney = Double.parseDouble(hbArrowLayout.getTag().toString());
		} catch (Exception e) {
		}
		try {
			jxqMoney = Double.parseDouble(jxqArrowLayout.getTag().toString());
		} catch (Exception e) {
		}
		needRechargeMoeny = moneyInvest;
		// �ж�Ͷ�ʽ���Ƿ�����˻����
		String userBanlance = userBalanceTV.getText().toString();
		userBanlanceDouble = Double.parseDouble(userBanlance);
		String borrowBalance = String.valueOf(borrowBalanceTemp);
		borrowBalanceDouble = Double.parseDouble(borrowBalance);
		if (moneyInvest < 100L) {
			Util.toastShort(BidYYYActivity.this, "Ͷ�ʽ���С��100Ԫ");
		} else if (moneyInvest % 100 != 0) {
			Util.toastLong(BidYYYActivity.this, "Ͷ�ʽ�����Ϊ100��������");
		} else if (needRechargeMoeny > userBanlanceDouble) {
			Util.toastLong(BidYYYActivity.this, "�˻�����");
		} else if (needRechargeMoeny > borrowBalanceDouble) {
			Util.toastLong(BidYYYActivity.this, "��Ŀ�Ͷ����");
		} else if(!compactCB.isChecked()){
			Util.toastLong(BidYYYActivity.this, "�����Ķ���ͬ���ƷЭ��");
		}else{
			showInvestDialog();
		}
	}

	/**
	 * ȷ��Ͷ�ʵ�dialog
	 */
	private void showInvestDialog() {
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.invest_prompt_layout, null);
		RelativeLayout hbLayout = (RelativeLayout) contentView.findViewById(R.id.invest_prompt_hb_layout_detail);//�������
		LinearLayout yjbjxqLayout = (LinearLayout) contentView.findViewById(R.id.invest_prompt_yjbjxq_layout_detail);//Ԫ��Һͼ�Ϣȯʹ�õĲ���
		Button sureBtn = (Button) contentView
				.findViewById(R.id.invest_prompt_layout_surebtn);
		Button cancelBtn = (Button) contentView
				.findViewById(R.id.invest_prompt_layout_cancelbtn);
		TextView totalMoney = (TextView) contentView
				.findViewById(R.id.invest_prompt_layout_total);
		TextView hbMoneyTV = (TextView) contentView
				.findViewById(R.id.invest_prompt_layout_hb_count);
		LinearLayout moneyDetailLayout = (LinearLayout) contentView.findViewById(R.id.invest_prompt_yjb_layout_detail);
		moneyDetailLayout.setVisibility(View.GONE);
		//�������������
		LinearLayout twoyearsLayoutMain = (LinearLayout) contentView
				.findViewById(R.id.invest_prompt_layout_twoyears_layout);
		LinearLayout twoyearsLayoutTop = (LinearLayout) contentView
				.findViewById(R.id.invest_prompt_layout_twoyears_layouttop);
		LinearLayout twoyearsLayoutBottom = (LinearLayout) contentView
				.findViewById(R.id.invest_prompt_layout_twoyears_layoutbottom);
		TextView twoyearsInterestMoney = (TextView) contentView
				.findViewById(R.id.invest_prompt_layout_twoyears_interestmoney);
		TextView twoyearsInvestMoney = (TextView) contentView
				.findViewById(R.id.invest_prompt_layout_twoyears_investmoney);
		TextView twoyearsPrompt = (TextView) contentView
				.findViewById(R.id.invest_prompt_layout_twoyears_prompt);
		TextView yjbjxqText = (TextView) contentView.findViewById(R.id.invest_prompt_layout_yjbjxq_count);//Ԫ��Ҽ�Ϣȯ ��ʹ�ö���
				
		totalMoney.setText(moneyInvest + "");
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
		computeIncome(mProductInfo.getInterest_rate(),
				mProductInfo.getAndroid_interest_rate(), (String)jxqArrowLayout.getTag(),investMoneyInt,
				(String)hbArrowLayout.getTag(),mProductInfo.getInterest_period());
	}

	/**
	 * ����
	 */
	private void investMoneyIncrease() {
		String investMoneyStr = investMoneyET.getText().toString();
		int investMoneyInt = 0;
		try {
			investMoneyInt = Integer.parseInt(investMoneyStr);
		} catch (Exception e) {
		}
		investMoneyInt += 100;
		investMoneyET.setText(investMoneyInt + "");
		computeIncome(mProductInfo.getInterest_rate(),
				mProductInfo.getAndroid_interest_rate(), (String)jxqArrowLayout.getTag(),investMoneyInt,
				(String)hbArrowLayout.getTag(),mProductInfo.getInterest_period());
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
	private String computeIncome(String rateStr, String extraRateStr,String couponRateStr,
			int investMoney, String hbMoney,String daysStr) {
		float couponRateF = 0f;
		float rateF = 0f;
		float extraRateF = 0f;
		int days = 0;
		double hbMoneyD = 0;
		try{
			couponRateF = Float.parseFloat(couponRateStr);
		}catch (Exception e){
			e.printStackTrace();
		}
		try {
			rateF = Float.parseFloat(rateStr);
		} catch (Exception e) {
		}
		try {
			days = Integer.parseInt(daysStr.replace("��", ""));
		} catch (Exception e) {
		}
		try {
			extraRateF = Float.parseFloat(extraRateStr);
		} catch (Exception e) {
		}
		try{
			hbMoneyD = Double.parseDouble(hbMoney);
		}catch (Exception e){
			e.printStackTrace();
		}
		double income = 0f;
		int flag = SettingsManager.checkActiveStatusBySysTime(mProductInfo.getAdd_time(),SettingsManager.yyyJIAXIStartTime,
					SettingsManager.yyyJIAXIEndTime);

		if(flag == 0 && Constants.UserType.USER_NORMAL_PERSONAL.equals(SettingsManager.getUserType(BidYYYActivity.this))){
			//��Ϣ����ڽ����� ֻ����ͨ�û����ܲμ�
			income = 7.0f * investMoney * days / 36500;
		}else{
			income = (rateF + extraRateF + couponRateF) * investMoney * days / 36500
						+ rateF * hbMoneyD * days / 36500;
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
	 * ��������Ͷ�ʽӿ�
	 */
	private void requestInvest(String borrowId, String investUserId,
			String money,String investFrom,String couponId,String redbagId) {
		if (mLoadingDialog != null && !isFinishing()) {
			mLoadingDialog.show();
		}
		AsyncYYYInvest asyncBorrowInvest = new AsyncYYYInvest(
				BidYYYActivity.this, borrowId, investUserId, money,investFrom,couponId, redbagId,new OnCommonInter() {
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
				BidYYYActivity.this, userId, new OnCommonInter() {
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
	 * ��Ϣȯ
	 * @param userId
	 * @param useStatus
	 */
	private void requestJXQList(String userId, String useStatus) {
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		AsyncJXQPageInfo redbagTask = new AsyncJXQPageInfo(BidYYYActivity.this, userId,useStatus,
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
	 * ��ȡ��ǰ�û��Ŀ��ú�����б�
	 *
	 * @param userId
	 * @param borrowType
	 */
	private void requestHBPageInfoByBorrowType(String userId, String borrowType) {
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		AsyncCurrentUserRedbagList currentUserRedbagListTask = new AsyncCurrentUserRedbagList(
				BidYYYActivity.this, userId, borrowType, new OnCommonInter() {
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

	private void updateUsePrompt(List<String> usePromptList){
		if(usePromptList != null && usePromptList.size() > 0){
			daojuPrompt.setVisibility(View.VISIBLE);
		}else{
			daojuPrompt.setVisibility(View.GONE);
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
}
