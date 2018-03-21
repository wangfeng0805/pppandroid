package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncBFRecharge;
import com.ylfcf.ppp.async.AsyncBFSendRechargeMsg;
import com.ylfcf.ppp.async.AsyncQuickBankList;
import com.ylfcf.ppp.async.AsyncUserBankCard;
import com.ylfcf.ppp.entity.BankInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.RechargeResultInfo;
import com.ylfcf.ppp.entity.RechargeTempInfo;
import com.ylfcf.ppp.entity.TaskDate;
import com.ylfcf.ppp.entity.UserCardInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnUserBankCardInter;
import com.ylfcf.ppp.util.CountDownAsyncTask;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.view.RechargeErrorPopwindow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * ������ֵҳ��
 * 
 * @author Administrator
 * 
 */
public class RechargeActivity extends BaseActivity implements OnClickListener {
	private static final String className = "RechargeActivity";
	private static final int REQUEST_SMS_SUCCESS = 1920;
	private static final int REQUEST_BANKLIST_WHAT = 1921;//��ȡ�����б�
	private static final int REQUEST_BANKCARD_WHAT = 1922;//��ȡ���п���Ϣ
	/**
	 * �Ѿ���֤
	 */
	private ImageView bankLogoImg;//���п���logo
	private TextView bankCardTV;//���п���
	private TextView bankLimitPrompt;//���п��޶�˵��
	private TextView bankLimitPrompt1;//
	private EditText verifyMoneyET;//��ֵ���
	private EditText verifyAuthNumET;//������֤��
	private Button verifyGetAuthNumBtn;//��ȡ��֤��
	private Button verifySureBtn;// ȷ�ϳ�ֵ
	private TextView smsPrompt;//������֤�뷢�ͳɹ����������ʾ
	private TextView catLimitBtn;//�鿴����֧�����
	private TextView catRechargeRecordBtn;//�鿴��ֵ��¼

	private LinearLayout topLeftBtn;
	private TextView topTitleTV;

	private UserCardInfo bankCardInfo;
	private LinearLayout mainLayout;

	private CountDownAsyncTask countDownAsynTask = null;
	private final long intervalTime = 1000L;
	private List<BankInfo> bankList = new ArrayList<BankInfo>();
	
	private String orderId = "";//��ֵ������
	private int page = 0,pageSize = 50;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case CountDownAsyncTask.PROGRESS_UPDATE:
				TaskDate date = (TaskDate) msg.obj;
				long time = date.getTime();
				countDownView(time);
				break;
			case CountDownAsyncTask.FINISH:
				verifyGetAuthNumBtn.setText("��ȡ��֤��");
				verifyGetAuthNumBtn.setEnabled(true);
				break;
			case REQUEST_SMS_SUCCESS:
				BaseInfo baseInfo = (BaseInfo) msg.obj;
				smsPrompt.setVisibility(View.VISIBLE);
				orderId = baseInfo.getRechargeOrderInfo().getOrder_sn();
				verifySureBtn.setEnabled(true);
				long createTime = System.currentTimeMillis();
				countDownAsynTask = new CountDownAsyncTask(handler, "",
						System.currentTimeMillis(), createTime + 1000 * 60,
						intervalTime);
				SettingsManager.FULL_TASK_EXECUTOR.execute(countDownAsynTask);
				break;
			case REQUEST_BANKLIST_WHAT:
				requestBankList("����", "����֧��");
				break;
			case REQUEST_BANKCARD_WHAT:
				requestBankCardInfo();
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
		setContentView(R.layout.recharge_msg_activity);
		initBankLogoMap();//��ʼ������logo
		findViews();
		handler.sendEmptyMessage(REQUEST_BANKLIST_WHAT);
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("��ݳ�ֵ");

		verifyMoneyET = (EditText) findViewById(R.id.recharge_activity_verify_money);
		verifyAuthNumET = (EditText) findViewById(R.id.recharge_activity_verify_check_et);
		verifyGetAuthNumBtn = (Button) findViewById(R.id.recharge_activity_verify_get_authnum_btn);
		verifyGetAuthNumBtn.setOnClickListener(this);
		verifySureBtn = (Button) findViewById(R.id.recharge_activity_verify_sure_btn);
		verifySureBtn.setOnClickListener(this);
		smsPrompt = (TextView) findViewById(R.id.recharge_msg_activity_sms_prompt);
		bankLogoImg = (ImageView) findViewById(R.id.recharge_msg_activity_banklogo);
		bankCardTV = (TextView) findViewById(R.id.recharge_activity_verify_bankcard_data);
		bankLimitPrompt = (TextView) findViewById(R.id.recharge_msg_activity_limit);
		bankLimitPrompt1 = (TextView) findViewById(R.id.recharge_msg_activity_limit_prompt);
		mainLayout = (LinearLayout) findViewById(R.id.recharge_msg_activity_mainlayout);
		catLimitBtn = (TextView)findViewById(R.id.recharge_msg_activity_catlimit);
		catLimitBtn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //�»���
		catLimitBtn.getPaint().setAntiAlias(true);//�����
		catLimitBtn.setOnClickListener(this);
		catRechargeRecordBtn = (TextView)findViewById(R.id.recharge_msg_activity_cat_rechargerecord);
		catRechargeRecordBtn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //�»���
		catRechargeRecordBtn.getPaint().setAntiAlias(true);//�����
		catRechargeRecordBtn.setOnClickListener(this);

		verifyMoneyET.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.toString().contains(".")) {
					if (s.length() - 1 - s.toString().indexOf(".") > 2) {
						s = s.toString().subSequence(0,
								s.toString().indexOf(".") + 3);
						verifyMoneyET.setText(s);
						verifyMoneyET.setSelection(s.length());
					}
				}
				if (s.toString().trim().substring(0).equals(".")) {
					s = "0" + s;
					verifyMoneyET.setText(s);
					verifyMoneyET.setSelection(2);
				}

				if (s.toString().startsWith("0")
						&& s.toString().trim().length() > 1) {
					if (!s.toString().substring(1, 2).equals(".")) {
						verifyMoneyET.setText(s.subSequence(0, 1));
						verifyMoneyET.setSelection(1);
						return;
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
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
	
	/**
	 * ��ʼ������logo
	 */
	private void initBankLogoMap(){
		for(int i=0;i<SettingsManager.bankCodes.length;i++){
			SettingsManager.bankLogosMap.put(SettingsManager.bankCodes[i], SettingsManager.bankLogos[i]);
		}
	}

	/**
	 * ��ʼ�����п���Ϣ
	 */
	private void initBankCardInfo(){
		if(bankList == null){
			return;
		}
		int position = 0;
		String bankCode = bankCardInfo.getBank_code();
		for(int i=0;i<bankList.size();i++){
			if(bankCode.equals(bankList.get(i).getBank_code())){
				position = i;
				break;
			}
		}
		try{
			int bankLogoId = SettingsManager.bankLogosMap.get(bankCode);
			bankLogoImg.setImageResource(bankLogoId);
		}catch (Exception e){
			e.printStackTrace();
		}
		bankCardTV.setText("���ţ�" + Util.hiddenBankCard(bankCardInfo.getBank_card()));
		String singleQuota = "",dailyQuota = "";
		if("0".equals(bankList.get(position).getSingle_quota())){
			singleQuota = "���޶�";
		}else{
			singleQuota = bankList.get(position).getSingle_quota() + "��";
		}
		if("0".equals(bankList.get(position).getDaily_quota())){
			dailyQuota = "���޶�";
		}else{
			dailyQuota = bankList.get(position).getDaily_quota() + "��";
		}
		bankLimitPrompt.setText("֧���޶���ʾ������" + singleQuota + "  ����" + dailyQuota);
		bankLimitPrompt1.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.recharge_activity_verify_get_authnum_btn:
			// ��ȡ��֤��
			verifyGetAuthNum();
			break;
		case R.id.recharge_activity_verify_sure_btn:
			verifyRecharge();
			break;
		case R.id.recharge_msg_activity_catlimit:
			Intent intent = new Intent(RechargeActivity.this,LimitPromptActivity.class);
			startActivity(intent);
			break;
		case R.id.recharge_msg_activity_cat_rechargerecord:
			Intent intentRechargeRecord = new Intent(RechargeActivity.this,RechargeRecordActivity.class);
			startActivity(intentRechargeRecord);
			break;
		default:
			break;
		}
	}

	/**
	 * ��ȡ��֤�밴ť����ʱ
	 * @param time
	 */
	private void countDownView(long time) {
		time /= intervalTime;
		StringBuffer sb = new StringBuffer();
		sb.append(time).append("����ط�");
		verifyGetAuthNumBtn.setText(sb.toString());
	}

	double accountDouble = 0d;
	private void verifyGetAuthNum() {
		String accoutStr = verifyMoneyET.getText().toString();
		try {
			accountDouble = Double.parseDouble(accoutStr);
		} catch (Exception e) {
		}
		if (accountDouble < 1) {
			Util.toastShort(RechargeActivity.this, "��ֵ����С��1Ԫ");
		} else {
			verifyMoneyET.setFocusable(false);// ����˻�ȡ��֤��ӿھ��൱��������֧���������������޸�
			verifyMoneyET.setFocusableInTouchMode(false);
			verifyMoneyET.setEnabled(false);
			
			//��ȡ������֤��
			requestBFRechargeSms(String.valueOf(accountDouble));
			verifyGetAuthNumBtn.setEnabled(false);
		}
	}

	/**
	 * �Ѿ�ͨ����֤���û���ֵȷ�ϳ�ֵ
	 */
	private void verifyRecharge() {
		String smsCode = verifyAuthNumET.getText().toString();
		String amount = verifyMoneyET.getText().toString();
		try {
			accountDouble = Double.parseDouble(amount);
		} catch (Exception e) {
		}
		if (accountDouble < 1) {
			Util.toastShort(RechargeActivity.this, "��ֵ����С��1Ԫ");
			return;
		}
		if (smsCode == null || "".equals(smsCode)) {
			Util.toastShort(RechargeActivity.this, "������֤�����벻��ȷ");
			return;
		}
		requestBFRecharge(amount, smsCode,orderId);
	}
	
	/**
	 * �������б��浽�ڴ�
	 * @param bankList
	 */
	private void saveBankList(List<BankInfo> bankList){
		if(bankList == null || bankList.size() <= 0){
			return;
		}
		for(int i = 0;i < bankList.size();i++){
			BankInfo bankInfo = bankList.get(i);
			SettingsManager.bankMap.put(bankInfo.getBank_code(), bankInfo);
		}
	}
	
	private void initBankList(Map<String,BankInfo> bankMap){
		bankList.clear();
		Iterator<Map.Entry<String,BankInfo>> entries = bankMap.entrySet().iterator();
		while(entries.hasNext()){
			Map.Entry<String, BankInfo> entry = entries.next();
			bankList.add(entry.getValue());
		}
		handler.sendEmptyMessage(REQUEST_BANKCARD_WHAT);
	}
	
	/**
	 * ��ʾ��ֵʧ�ܵĴ��������
	 */
	private void showRechargeErrorPopwindow(String errorMsg){
		View popView = LayoutInflater.from(this).inflate(R.layout.recharge_error_popwindow, null);
		int[] screen = SettingsManager.getScreenDispaly(RechargeActivity.this);
		int width = screen[0]*4/5;
		int height = screen[1]*2/7;
		RechargeErrorPopwindow popwindow = new RechargeErrorPopwindow(RechargeActivity.this,popView, width, height,errorMsg,new OnRechargeErrorInter() {
			@Override
			public void back(boolean flag) {
				if(flag){
					verifyMoneyET.setFocusable(true);
					verifyMoneyET.setFocusableInTouchMode(true);
					verifyMoneyET.setEnabled(true);
				}
			}
		});
		popwindow.show(mainLayout);
	}

	/**
	 * ��ֵ
	 * @param amount ��ֵ���
	 * @param smsCode ������֤��
	 */
	private void requestBFRecharge(final String amount,String smsCode,String orderId){
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		AsyncBFRecharge rechargeTask = new AsyncBFRecharge(RechargeActivity.this, 
				SettingsManager.getUserId(getApplicationContext()), amount, smsCode,orderId, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						verifyMoneyET.setFocusable(true);
						verifyMoneyET.setFocusableInTouchMode(true);
						verifyMoneyET.setEnabled(true);
						if(mLoadingDialog != null && mLoadingDialog.isShowing()){
							mLoadingDialog.dismiss();
						}
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								RechargeResultInfo resultInfo = baseInfo.getmRechargeResultInfo();
//								Util.toastShort(RechargeActivity.this, "��ֵ�ɹ�");
								RechargeTempInfo info = new RechargeTempInfo();
								info.setType("recharge");
								info.setRechargeMoney(amount);
								info.setOrder_sn(resultInfo.getOrder_sn());
								Intent intent = new Intent(RechargeActivity.this,RechargeResultActivity.class);
								intent.putExtra("RechargeTempInfo", info);
								startActivity(intent);
								finish();
							}else{
								Util.toastLong(RechargeActivity.this, baseInfo.getMsg());
							}
						}
					}
				});
		rechargeTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * ��ֵ���Ͷ�����֤��
	 * @param amount ��ֵ���
	 */
	private void requestBFRechargeSms(String amount){
		AsyncBFSendRechargeMsg msgTask = new AsyncBFSendRechargeMsg(RechargeActivity.this, SettingsManager.getUserId(getApplicationContext()),
				amount, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								Message msg = handler.obtainMessage(REQUEST_SMS_SUCCESS);
								msg.obj = baseInfo;
								handler.sendMessage(msg);
							}else{
								showRechargeErrorPopwindow(baseInfo.getMsg());
								verifyGetAuthNumBtn.setEnabled(true);
								verifyMoneyET.setFocusable(true);
								verifyMoneyET.setFocusableInTouchMode(true);
								verifyMoneyET.setEnabled(true);
							}
						}
					}
				});
		msgTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * ��ȡ���п���Ϣ
	 */
	private void requestBankCardInfo(){
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		AsyncUserBankCard rechargeTask = new AsyncUserBankCard(RechargeActivity.this,
				SettingsManager.getUserId(getApplicationContext()), "����", new OnUserBankCardInter() {
					@Override
					public void back(BaseInfo info) {
						if(mLoadingDialog != null && mLoadingDialog.isShowing()){
							mLoadingDialog.dismiss();
						}
						if(info != null){
							int resultCode = SettingsManager.getResultCode(info);
							if(resultCode == 0){
								bankCardInfo = info.getUserCardInfo();
								initBankCardInfo();
							}
						}
					}
				});
		rechargeTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * �����б�
	 * @param status
	 * @param payWayname
	 */
	private void requestBankList(String status,String payWayname){
		if(SettingsManager.bankMap != null && !SettingsManager.bankMap.isEmpty()){
			initBankList(SettingsManager.bankMap);
			return;
		}
		AsyncQuickBankList bankTask = new AsyncQuickBankList(RechargeActivity.this, status, payWayname, String.valueOf(page), String.valueOf(pageSize), 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								bankList = baseInfo.getBankPageInfo().getBankList();
								saveBankList(bankList);
								handler.sendEmptyMessage(REQUEST_BANKCARD_WHAT);
							}
						}
					}
				});
		bankTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	public interface OnRechargeErrorInter{
		void back(boolean flag);
	}
}
