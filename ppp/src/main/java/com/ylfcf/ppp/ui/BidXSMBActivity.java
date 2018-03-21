package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncXSMBDetail;
import com.ylfcf.ppp.async.AsyncXSMBInvest;
import com.ylfcf.ppp.async.AsyncYiLianRMBAccount;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.UserRMBAccountInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnIsBindingListener;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ��ʱ���
 * @author Mr.liu
 *
 */
public class BidXSMBActivity extends BaseActivity implements OnClickListener{
	private static final String className = "BidXSMBActivity";
	private static final int REQUEST_USER_ACCOUNT_WHAT = 8291;//�����˻�
	private static final int REQUEST_XSMB_WHAT = 8292;//������ʱ�������
	private static final int REQUEST_XSMB_INVEST_WHAT = 8293;//��ʱ���Ͷ�ʽӿ�
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	private TextView borrowName;
	private TextView borrowRate;//�껯����
	private TextView borrowPeriod;//����
	private TextView useBalanceTV;//�û��������
	private Button rechargeBtn;
	private EditText investMoenyET;//Ͷ�ʽ��������
	private TextView nhllTV;//���껯����
	private TextView yqsyTV;//Ԥ������
	private Button bidBtn;//������ɱ
	private CheckBox cb;
	private TextView compactTV;//���Э��
	
	private ProductInfo mProductInfo;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_USER_ACCOUNT_WHAT:
				requestUserAccountInfo(SettingsManager.getUserId(getApplicationContext()));
				break;
			case REQUEST_XSMB_WHAT:
				//��ʱ���
				requestXSMBDetails("����");
				break;
			case REQUEST_XSMB_INVEST_WHAT:
				//��ʱ���Ͷ�ʽӿ�
				requestXSMBInvest(mProductInfo.getId(),mProductInfo.getSingle_invest_money(),
						SettingsManager.getUserId(getApplicationContext()),SettingsManager.USER_FROM);
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bid_xsmb_activity);
		mProductInfo = (ProductInfo) getIntent().getSerializableExtra(
				"PRODUCT_INFO");
		findViews();
	}

	@Override
	protected void onResume() {
		super.onResume();
		UMengStatistics.statisticsOnPageStart(className);//����ͳ��ҳ����ת
		UMengStatistics.statisticsResume(this);//����ͳ��ʱ��
		handler.sendEmptyMessage(REQUEST_USER_ACCOUNT_WHAT);
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
	
	private void findViews(){
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("Ͷ��");
		
		borrowName = (TextView) findViewById(R.id.bid_xsmb_activity_borrow_name);
		borrowRate = (TextView) findViewById(R.id.bid_xsmb_activity_rate);
		borrowPeriod = (TextView) findViewById(R.id.bid_xsmb_activity_peroid);
		useBalanceTV = (TextView) findViewById(R.id.bid_xsmb_activity_user_balance);
		rechargeBtn = (Button) findViewById(R.id.bid_xsmb_activity_recharge_btn);
		rechargeBtn.setOnClickListener(this);
		investMoenyET = (EditText) findViewById(R.id.bid_xsmb_activity_invest_et);
		nhllTV = (TextView) findViewById(R.id.bid_xsmb_activity_nhsy);
		yqsyTV = (TextView) findViewById(R.id.bid_xsmb_activity_yjsy);
		bidBtn = (Button) findViewById(R.id.bid_xsmb_activity_borrow_bidBtn);
		bidBtn.setOnClickListener(this);
		cb = (CheckBox) findViewById(R.id.bid_xsmb_activity_cb);
		compactTV = (TextView) findViewById(R.id.bid_xsmb_activity_compact_text);
		compactTV.setOnClickListener(this);
		
		initViewData();
	}

	private void initViewData(){
		if(mProductInfo == null)
			return;
		borrowName.setText(mProductInfo.getBorrow_name());
		borrowPeriod.setText(mProductInfo.getInterest_period());
		double rateD = 0d;
		try {
			rateD = Double.parseDouble(mProductInfo.getInterest_rate());
			if((int)(rateD * 10)%10 == 0){
				//˵��������������û��С��
				borrowRate.setText((int)rateD + "");
				nhllTV.setText((int)rateD + "%");
			}else{
				borrowRate.setText(Util.double2PointDoubleOne(rateD));
				nhllTV.setText(Util.double2PointDoubleOne(rateD)+"%");
			}
		} catch (Exception e) {
			borrowRate.setText(mProductInfo.getInterest_rate());
			nhllTV.setText(mProductInfo.getInterest_rate()+"%");
		}
		try {
			double singleInvestMoneyD = Double.parseDouble(mProductInfo.getSingle_invest_money());
			if(singleInvestMoneyD > 0){
				investMoenyET.setText(mProductInfo.getSingle_invest_money());
			}else{
				investMoenyET.setText("2000");
			}
		} catch (Exception e) {
			investMoenyET.setText("2000");
		}
		computeIncome(mProductInfo.getInterest_rate(), "0.00", mProductInfo.getSingle_invest_money(), mProductInfo.getInterest_period());
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.bid_xsmb_activity_recharge_btn:
			//ȥ��ֵ
			if(SettingsManager.isPersonalUser(getApplicationContext())){
				checkIsVerify("��ֵ");
			}else if(SettingsManager.isCompanyUser(getApplicationContext())){
				Intent intent = new Intent(BidXSMBActivity.this,RechargeCompActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.bid_xsmb_activity_compact_text:
			//���Э��
			Intent intent = new Intent(BidXSMBActivity.this,CompactActivity.class);
			intent.putExtra("mProductInfo", mProductInfo);
			intent.putExtra("from_where", "xsmb");
			startActivity(intent);
			break;
		case R.id.bid_xsmb_activity_borrow_bidBtn:
			//������ɱ
			handler.sendEmptyMessage(REQUEST_XSMB_WHAT);
			break;
		default:
			break;
		}
	}
	
	/**
	 * �����껯�ʺ�Ͷ�ʽ���������
	 */
	private String computeIncome(String rateStr, String extraRateStr,
			String investMoney, String daysStr) {
		float rateF = 0f;
		float extraRateF = 0f;
		int days = 0;
		int investMoneyInt = 0;
		try {
			rateF = Float.parseFloat(rateStr);
			extraRateF = Float.parseFloat(extraRateStr);
		} catch (Exception e) {
		}
		try {
			days = Integer.parseInt(daysStr);
		} catch (Exception e) {
		}
		try {
			investMoneyInt = Integer.parseInt(investMoney);
		} catch (Exception e) {
			investMoneyInt = 2000;
		}
		float income = 0f;
		income = (rateF + extraRateF) * investMoneyInt * days / 36500;
		DecimalFormat df = new java.text.DecimalFormat("#.00");
		if (income < 1) {
			yqsyTV.setText("0" + df.format(income));
		} else {
			yqsyTV.setText(df.format(income));
		}

		return df.format(income);
	}
	
	/**
	 * �ж��Ƿ����Ͷ��
	 * @param info
	 */
	long remainTimeL = 0l;
	private void checkInvest(ProductInfo productInfo){
		if(productInfo != null && "δ����".equals(productInfo.getMoney_status())){
//			handler.sendEmptyMessage(REQUEST_XSMB_INVEST_WHAT);
			String nowTimeStr = productInfo.getNow_time();
			String willStartTimeStr = productInfo.getWill_start_time();//��ʼʱ��
			try {
				Date nowDate = sdf.parse(nowTimeStr);
				Date willStartDate = sdf.parse(willStartTimeStr);
				remainTimeL = willStartDate.getTime() - nowDate.getTime();
				if(remainTimeL >= 0){
					showPromptDialog("1");
				}else{
//					//����Ͷ���С���
					bidBtn.setEnabled(true);
					handler.sendEmptyMessage(REQUEST_XSMB_INVEST_WHAT);
				}
			} catch (ParseException e) {
				bidBtn.setEnabled(false);
				bidBtn.setText("Ͷ�ʽ���");
				bidBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_26dp));
				e.printStackTrace();
			}
		}else if("������".equals(productInfo.getMoney_status())){
			showPromptDialog("1");
			bidBtn.setEnabled(false);
			bidBtn.setText("Ͷ�ʽ���");
			bidBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_26dp));
		}
	}
	
	/**
	 * �û��˻���Ϣ
	 */
	private void requestUserAccountInfo(String userId) {
		AsyncYiLianRMBAccount yilianTask = new AsyncYiLianRMBAccount(
				BidXSMBActivity.this, userId, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								UserRMBAccountInfo info = baseInfo
										.getRmbAccountInfo();
								useBalanceTV.setText(info.getUse_money());
							}
						}
					}
				});
		yilianTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * ��֤�û��Ƿ��Ѿ���֤
	 * @param type ����ֵ��,�����֡�
	 */
	private void checkIsVerify(final String type){
		rechargeBtn.setEnabled(false);
		RequestApis.requestIsVerify(BidXSMBActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(flag){
					//�û��Ѿ�ʵ��
					checkIsBindCard(type);
				}else{
					//�û�û��ʵ��
					Intent intent = new Intent(BidXSMBActivity.this,UserVerifyActivity.class);
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
		RequestApis.requestIsBinding(BidXSMBActivity.this, SettingsManager.getUserId(getApplicationContext()), "����", new OnIsBindingListener() {
			@Override
			public void isBinding(boolean flag, Object object) {
				Intent intent = new Intent();
				if(flag){
					//�û��Ѿ���
					if("��ֵ".equals(type)){
						//��ôֱ��������ֵҳ��
						intent.setClass(BidXSMBActivity.this, RechargeActivity.class);
					}
				}else{
					//�û���û�а�
					Bundle bundle = new Bundle();
					bundle.putString("type", type);
					intent.putExtra("bundle", bundle);
					intent.setClass(BidXSMBActivity.this, BindCardActivity.class);
				}
				startActivity(intent);
				rechargeBtn.setEnabled(true);
			}
		});
	}
	
	/**
	 * �������
	 */
	private void requestXSMBDetails(String borrowStatus){
		bidBtn.setEnabled(false);
		if(mLoadingDialog != null && isFinishing()){
			mLoadingDialog.show();
		}
		AsyncXSMBDetail xsmbTask = new AsyncXSMBDetail(BidXSMBActivity.this, borrowStatus,new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				bidBtn.setEnabled(true);
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						ProductInfo info = baseInfo.getmProductInfo();
						checkInvest(info);
					}else{
						Util.toastLong(BidXSMBActivity.this, baseInfo.getMsg());
						mLoadingDialog.dismiss();
					}
				}else{
					mLoadingDialog.dismiss();
				}
			}
		});
		xsmbTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * ��ʱ���Ͷ��ӿ�
	 * @param borrowId
	 * @param money
	 * @param userId
	 * @param investFrom
	 */
	private void requestXSMBInvest(String borrowId,String money,String userId,String investFrom){
		AsyncXSMBInvest xsmbTask = new AsyncXSMBInvest(BidXSMBActivity.this,borrowId,money,userId,investFrom,new OnCommonInter(){
			@Override
			public void back(BaseInfo baseInfo) {
				if(mLoadingDialog != null && mLoadingDialog.isShowing()){
					mLoadingDialog.dismiss();
				}
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						Intent intentSuccess = new Intent(BidXSMBActivity.this,BidSuccessActivity.class);
						intentSuccess.putExtra("from_where", "���");
						startActivity(intentSuccess);
						mApp.finishAllActivityExceptMain();
					}else if(resultCode == -102){
						//��ɱ��������
						showPromptDialog("0");
					}else{
						Util.toastLong(BidXSMBActivity.this, baseInfo.getMsg());
					}
				}
			}
		});
		xsmbTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 
	 * @param flag 1��ʾ������ 0��ʾ��ɱ��������
	 */
	private void showPromptDialog(final String flag){
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.borrow_detail_prompt_layout, null);
		Button leftBtn = (Button) contentView
				.findViewById(R.id.borrow_detail_prompt_layout_leftbtn);
		Button rightBtn = (Button) contentView
				.findViewById(R.id.borrow_detail_prompt_layout_rightbtn);
		TextView topTV = (TextView) contentView
				.findViewById(R.id.borrow_detail_prompt_top_text);
		TextView bottomTV = (TextView) contentView
				.findViewById(R.id.borrow_detail_prompt_bottom_text);
		if("0".equals(flag)){
			bottomTV.setVisibility(View.VISIBLE);
			rightBtn.setVisibility(View.VISIBLE);
			topTV.setText("���ı�����ɱ������ʹ��");
			bottomTV.setText("ÿ����ɱ��ÿ���û�ֻ��һ����ɱ����Ӵ~");
			leftBtn.setText("�鿴Ͷ�ʼ�¼");
			leftBtn.setTextColor(getResources().getColor(R.color.common_topbar_bg_color));
			leftBtn.setBackground(getResources().getDrawable(R.drawable.style_rect_fillet_blue));
			rightBtn.setText("��ע������Ŀ");
		}else if("1".equals(flag)){
			bottomTV.setVisibility(View.GONE);
			rightBtn.setVisibility(View.GONE);
			topTV.setText("����⣡\n����һ���������԰�~");
			leftBtn.setText("ȷ��");
			leftBtn.setTextColor(getResources().getColor(R.color.white));
			leftBtn.setBackground(getResources().getDrawable(R.drawable.style_rect_fillet_filling_blue));
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this,
				R.style.Dialog_Transparent); // �ȵõ�������
		builder.setView(contentView);
		builder.setCancelable(true);
		final AlertDialog dialog = builder.create();
		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if("0".equals(flag)){
					Intent intent = new Intent(BidXSMBActivity.this,UserInvestRecordActivity.class);
					intent.putExtra("from_where", "���");
					startActivity(intent);
				}
				dialog.dismiss();
			}
		});
		rightBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SettingsManager.setMainProductListFlag(getApplicationContext(), true);	
				dialog.dismiss();
				finish();
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
}
