package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
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
import com.ylfcf.ppp.async.AsyncBFBindCard;
import com.ylfcf.ppp.async.AsyncBFSendBindCardMsg;
import com.ylfcf.ppp.async.AsyncQuickBankList;
import com.ylfcf.ppp.async.AsyncUserSelectOne;
import com.ylfcf.ppp.entity.BankInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.RechargeOrderInfo;
import com.ylfcf.ppp.entity.TaskDate;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnGetUserInfoByPhone;
import com.ylfcf.ppp.util.CountDownAsyncTask;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.view.CommonPopwindow;
import com.ylfcf.ppp.view.RechargeSpinnerPopwindow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * �����п�
 * 
 * ���������֤�ŴӺ�̨��ȡ���в��ɱ༭��
 * ֧��ͨ���������ʾֻ�����û��ſ��Կ��������������û��ı�׼���Ա�������ʱ��Ϊ�ֽ��ߣ�
 * 
 * @author Mr.liu
 *
 */
public class BindCardActivity extends BaseActivity implements OnClickListener {
	private static final String className = "BindCardActivity";
	private static final int REQUEST_USERINFO_WHAT = 1201;
	private static final int REQUEST_USERINFO_SUCCESS = 1202;
	private static final int REQUEST_USERINFO_FAILE = 1203;
	private static final int REQUEST_QUICK_BANKLIST = 1204;
	
	private List<BankInfo> bankList = new ArrayList<BankInfo>();
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;

	private LinearLayout mainLayout;
	private EditText bankcardET;//���п���
	private EditText banknameET;//������
	private TextView bankPromptTV;//
	private ImageView selectBankIv;//ѡ�񿪻��еļ�ͷ
	private EditText phoneET;//����Ԥ���ֻ�
	private EditText authcodeET;//������֤��
	private Button getAuthcodeBtn;//��ȡ�ֻ���֤���button
	private TextView smsPrompt;//��ȡ������֤��������ʾ����
	private TextView realnameTV;
	private TextView idnumberTV;
	private Button bindcardBtn;
	private TextView catLimit;//�鿴֧�����
	private ImageView promptBtn;//֧��ͨ���������ʾ��ť
	private LayoutInflater layoutInflater;

	private UserInfo mUserInfo;
	private String type = "";//��ֵ�����֣���ʾ�Ǵӳ�ֵ���̹������Ǵ��������̹�����
	private int page = 0, pageSize = 50;
	
	private CountDownAsyncTask countDownAsynTask = null;
	private final long intervalTime = 1000L;
	private boolean isBindcard = false;
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_USERINFO_WHAT:
				requestUserInfo(SettingsManager.getUserId(getApplicationContext()));
				break;
			case REQUEST_USERINFO_SUCCESS:
				initData();
				break;
			case REQUEST_USERINFO_FAILE:
				break;
			case CountDownAsyncTask.PROGRESS_UPDATE:
				TaskDate date = (TaskDate) msg.obj;
				long time = date.getTime();
				countDownView(time);
				break;
			case CountDownAsyncTask.FINISH:
				getAuthcodeBtn.setText("��ȡ��֤��");
				getAuthcodeBtn.setEnabled(true);
				break;
			case REQUEST_QUICK_BANKLIST:
				requestBankList("����", "����֧��");
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
		setContentView(R.layout.bind_card_activity);
		Bundle bundle = getIntent().getBundleExtra("bundle");
		if(bundle != null){
			type = bundle.getString("type");
		}
		layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		handler.sendEmptyMessage(REQUEST_USERINFO_WHAT);
		handler.sendEmptyMessageDelayed(REQUEST_QUICK_BANKLIST, 500L);
		findViews();
	}

	/**
	 * �����������֤������и�ֵ
	 */
	private void initData(){
		realnameTV.setText(Util.hidRealName(mUserInfo.getReal_name()));
		idnumberTV.setText(Util.hiddenBankCard(mUserInfo.getId_number()));
		
		boolean flag = SettingsManager.checkIsNewUser(mUserInfo.getReg_time());
		if(flag){
			promptBtn.setVisibility(View.GONE);
		}else{
			promptBtn.setVisibility(View.VISIBLE);
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
		getAuthcodeBtn.setText(sb.toString());
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

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("�����п�");

		mainLayout = (LinearLayout) findViewById(R.id.bind_card_activity_mainlayout);
		bankcardET = (EditText) findViewById(R.id.bindcard_activity_bankcard);
		banknameET = (EditText) findViewById(R.id.bindcard_activity_bankname);
		banknameET.setOnClickListener(this);
		selectBankIv = (ImageView) findViewById(R.id.bindcard_activity_bank_select);
		selectBankIv.setOnClickListener(this);
		realnameTV = (TextView) findViewById(R.id.bind_card_activity_realname);
		phoneET = (EditText) findViewById(R.id.bindcard_activity_phone);
		idnumberTV = (TextView) findViewById(R.id.bind_card_activity_idnum);
		authcodeET = (EditText) findViewById(R.id.bindcard_activity_authcode_et);
		getAuthcodeBtn = (Button) findViewById(R.id.bindcard_activity_get_authnum_btn);
		getAuthcodeBtn.setOnClickListener(this);
		smsPrompt = (TextView) findViewById(R.id.bind_card_activity_sms_prompt);
		bindcardBtn = (Button) findViewById(R.id.bindcard_activity_btn);
		bindcardBtn.setOnClickListener(this);
		catLimit = (TextView) findViewById(R.id.bindcard_activity_catlimit);
		catLimit.setOnClickListener(this);
		catLimit.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //�»���
		catLimit.getPaint().setAntiAlias(true);//�����
		promptBtn = (ImageView) findViewById(R.id.bind_card_activity_prompt_btn);
		promptBtn.setOnClickListener(this);
		bankPromptTV = (TextView) findViewById(R.id.bind_card_activity_bankprompt);
		if("��ֵ".equals(type) || "����".equals(type)){
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					showBindCardPrompt();
				}
			}, 500L);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.bindcard_activity_btn:
			//��
			bindCard();
			break;
		case R.id.bindcard_activity_bank_select:
		case R.id.bindcard_activity_bankname:
			showRechargeBankList();
			break;
		case R.id.bindcard_activity_catlimit:
			//֧���޶�
			Intent intent = new Intent(BindCardActivity.this,LimitPromptActivity.class);
			startActivity(intent);
			break;
		case R.id.bindcard_activity_get_authnum_btn:
			//��ȡ��֤��
			sendSms();
			break;
		case R.id.bind_card_activity_prompt_btn:
			//֧��ͨ�����˵��
			Intent intentChannel = new Intent(BindCardActivity.this,RechargeChannelActivity.class);
			startActivity(intentChannel);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
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
	}
	
	/**
	 * ���Ͷ�����֤��
	 */
	private void sendSms(){
		String mBankName = banknameET.getText().toString();
		String mBankCard = bankcardET.getText().toString();
		String mBankPhone = phoneET.getText().toString();
		if(mBankName == null || "".equals(mBankName)){
			Util.toastShort(BindCardActivity.this, "��ѡ�񿪻���");
			return;
		}
		if(mBankCard == null || "".equals(mBankCard)){
			Util.toastShort(BindCardActivity.this, "���������п���");
			return;
		}
		if(mBankPhone == null || "".equals(mBankPhone)){
			Util.toastShort(BindCardActivity.this, "����������Ԥ���ֻ���");
			return;
		}else if(!Util.checkPhoneNumber(mBankPhone)){
			Util.toastShort(BindCardActivity.this, "�ֻ������ʽ����");
			return;
		}
		requestBFBindcardSms(mBankCard, mBankPhone);
		getAuthcodeBtn.setEnabled(false);
		long createTime = System.currentTimeMillis();
		countDownAsynTask = new CountDownAsyncTask(handler, "",
				System.currentTimeMillis(), createTime + 1000 * 60,
				intervalTime);
		SettingsManager.FULL_TASK_EXECUTOR.execute(countDownAsynTask);
	}
	
	/**
	 * ��
	 */
	private void bindCard() {
		int position = 0;//
		String bankName = banknameET.getText().toString();
		String bankCard = bankcardET.getText().toString();
		String bankPhone = phoneET.getText().toString();
		String authCodeUser = authcodeET.getText().toString();//�û�������ֻ���֤��
		//�жϿ�������
		if(bankName == null || "".equals(bankName)) {
			Util.toastShort(BindCardActivity.this, "��ѡ�񿪻���");
			return;
		}
		//�ж����п���
		if (bankCard == null || "".equals(bankCard)) {
			Util.toastShort(BindCardActivity.this, "���������п���");
			return;
		}
		//�ж����п�Ԥ���ֻ���
		if (bankPhone == null || "".equals(bankPhone)) {
			Util.toastShort(BindCardActivity.this, "�������ֻ���");
			return;
		}else if(!Util.checkPhoneNumber(bankPhone)){
			Util.toastShort(BindCardActivity.this, "�ֻ��Ÿ�ʽ�������");
			return;
		}
		//�ж��ֻ���֤��
		if(authCodeUser == null || "".equals(authCodeUser)){
			Util.toastShort(BindCardActivity.this, "��������֤��");
			return;
		}
		for(int i=0;i<bankList.size();i++){
			if(bankName.equals(bankList.get(i).getBank_name())){
				position = i;
				break;
			}
		}
		requestBFBindcard(mUserInfo.getId_number(),mUserInfo.getReal_name(),bankCard,bankName,bankList.get(position).getBank_code(),bankPhone,authCodeUser,rechargeOrderInfo.getOrder_sn());
	}
	
	/**
	 * �󿨳ɹ�
	 */
	private void bindcardSuc(){
		Util.toastShort(BindCardActivity.this, "�󿨳ɹ�");
		Intent intent = new Intent();
		if("��ֵ".equals(type)){
			intent.setClass(BindCardActivity.this, RechargeActivity.class);
			startActivity(intent);
		}else if("����".equals(type)){
			intent.setClass(BindCardActivity.this, WithdrawPwdGetbackActivity.class);
			intent.putExtra("type", "����");
			startActivity(intent);
		}else if("�����н�".equals(type)){
			intent.setClass(BindCardActivity.this, InvitateActivity.class);
			intent.putExtra("is_verify", true);
			startActivity(intent);
		}else if("���ֱ�Ͷ��".equals(type)){
			intent.putExtra("PRODUCT_INFO", getIntent().getBundleExtra("bundle").getSerializable("PRODUCT_INFO"));
			intent.setClass(BindCardActivity.this, BidXSBActivity.class);
			startActivity(intent);
		}else if("���Ŵ�Ͷ��".equals(type)){
			intent.putExtra("PRODUCT_INFO", getIntent().getBundleExtra("bundle").getSerializable("PRODUCT_INFO"));
			intent.setClass(BindCardActivity.this, BidZXDActivity.class);
			startActivity(intent);
		}else if("Ԫ��ӯͶ��".equals(type)){
			intent.putExtra("PRODUCT_INFO", getIntent().getBundleExtra("bundle").getSerializable("PRODUCT_INFO"));
			intent.setClass(BindCardActivity.this, BidYYYActivity.class);
			startActivity(intent);
		}else if("�ȶ�ӯͶ��".equals(type)){
			intent.putExtra("PRODUCT_INFO", getIntent().getBundleExtra("bundle").getSerializable("PRODUCT_INFO"));
			intent.setClass(BindCardActivity.this, BidWDYActivity.class);
			startActivity(intent);
		}else if("VIPͶ��".equals(type)){
			intent.putExtra("PRODUCT_INFO", getIntent().getBundleExtra("bundle").getSerializable("PRODUCT_INFO"));
			intent.setClass(BindCardActivity.this, BidVIPActivity.class);
			startActivity(intent);
		}else if("˽������".equals(type)){
			intent.putExtra("PRODUCT_INFO", getIntent().getBundleExtra("bundle").getSerializable("PRODUCT_INFO"));
			intent.setClass(BindCardActivity.this, BidSRZXActivity.class);
			startActivity(intent);
		}else if("Ԫ��ӯ".equals(type)){
			intent.putExtra("PRODUCT_INFO", getIntent().getBundleExtra("bundle").getSerializable("PRODUCT_INFO"));
			intent.setClass(BindCardActivity.this, BidYJYActivity.class);
			startActivity(intent);
		}else{
		}
		finish();
	}

	/**
	 * �û��������ҳ�����Ҫ�Ȱ󿨵���ʾ
	 */
	private void showBindCardPrompt(){
		View popView = LayoutInflater.from(this).inflate(R.layout.common_popwindow, null);
		int[] screen = SettingsManager.getScreenDispaly(BindCardActivity.this);
		int width = screen[0]*4/5;
		int height = screen[1]*1/4;
		CommonPopwindow popwindow = new CommonPopwindow(BindCardActivity.this,popView, width, height,"��","",null);
		popwindow.show(mainLayout);
	}
	
	/**
	 * ���Ͱ󿨵Ķ�����֤��
	 * @param bankCard ���п���
	 * @param bankPhone ����Ԥ���ֻ�����
	 */
	RechargeOrderInfo rechargeOrderInfo = null;
	private void requestBFBindcardSms(String bankCard,String bankPhone){
		AsyncBFSendBindCardMsg msgTask = new AsyncBFSendBindCardMsg(BindCardActivity.this, SettingsManager.getUserId(getApplicationContext()),
				bankCard, bankPhone, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								smsPrompt.setVisibility(View.VISIBLE);
								rechargeOrderInfo = baseInfo.getRechargeOrderInfo();
								bankcardET.setEnabled(false);
								phoneET.setEnabled(false);
								banknameET.setEnabled(false);
								selectBankIv.setEnabled(false);
								bindcardBtn.setEnabled(true);
							}else{
								Util.toastLong(BindCardActivity.this, baseInfo.getMsg());
							}
						}
					}
				});
		msgTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * �󿨽ӿ�
	 * @param idNum  ���֤����
	 * @param realName ��ʵ����
	 * @param bankCard ���п�����
	 * @param bankName ��������
	 * @param bankCode ���м�� icbc
	 * @param bankPhone ����Ԥ���ֻ�����
	 * @param smsCode ��֤��
	 * @param orderSN ������
	 */
	private void requestBFBindcard(String idNum,String realName,String bankCard,String bankName,
			String bankCode,String bankPhone,String smsCode,String orderSN){
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		AsyncBFBindCard bindcardTask = new AsyncBFBindCard(BindCardActivity.this, SettingsManager.getUserId(getApplicationContext()), 
				idNum, realName, bankCard,
				bankName, bankCode, bankPhone, smsCode, orderSN,new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(mLoadingDialog != null && mLoadingDialog.isShowing()){
							mLoadingDialog.dismiss();
						}
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//��ʾ�󿨳ɹ�
								isBindcard = true;
								bindcardSuc();
							}else{
								Util.toastShort(BindCardActivity.this, baseInfo.getMsg());
								bankcardET.setEnabled(true);
								phoneET.setEnabled(true);
								banknameET.setEnabled(true);
								selectBankIv.setEnabled(true);
							}
						}
					}
				});
		bindcardTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * ��ȡ�û���Ϣ
	 * @param userId 
	 */
	private void requestUserInfo(String userId){
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		AsyncUserSelectOne userTask = new AsyncUserSelectOne(BindCardActivity.this, userId, "", "", "",
				new OnGetUserInfoByPhone() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(mLoadingDialog != null){
							mLoadingDialog.dismiss();
						}
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								mUserInfo = baseInfo.getUserInfo();
								Message msg = handler.obtainMessage(REQUEST_USERINFO_SUCCESS);
								msg.obj = mUserInfo;
								handler.sendMessage(msg);
							}else{
								Message msg = handler.obtainMessage(REQUEST_USERINFO_FAILE);
								msg.obj = baseInfo.getMsg();
								handler.sendMessage(msg);
							}
						}
					}
				});
		userTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
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
		AsyncQuickBankList bankTask = new AsyncQuickBankList(BindCardActivity.this, status, payWayname, String.valueOf(page), String.valueOf(pageSize), 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								bankList = baseInfo.getBankPageInfo().getBankList();
								saveBankList(bankList);
							}
						}
					}
				});
		bankTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * ����ѡ�������б���ʾ��
	 */
	private void showRechargeBankList() {
		View popView = LayoutInflater.from(this).inflate(
				R.layout.recharge_spinner_poplayout, null);
		int[] screen = SettingsManager.getScreenDispaly(BindCardActivity.this);
		int width = screen[0] * 4 / 5;
		int height = screen[1] / 2;
		RechargeSpinnerPopwindow popwindow = new RechargeSpinnerPopwindow(
				BindCardActivity.this, popView, width, height, bankList,
				new OnSpinnerItemClickListener() {
					@Override
					public void onClick(View v, int position) {
						banknameET.setText(bankList.get(position)
								.getBank_name());
						bankPromptTV.setVisibility(View.VISIBLE);
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
						bankPromptTV.setText("����" + singleQuota + "  ����" + dailyQuota);
					}
				}, layoutInflater);
		popwindow.show(mainLayout);
	}

	/**
	 * spinner����item�ĵ���¼�����
	 * 
	 * @author Administrator
	 * 
	 */
	public interface OnSpinnerItemClickListener {
		void onClick(View v, int position);
	}
}
