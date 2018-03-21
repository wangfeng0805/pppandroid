package com.ylfcf.ppp.ui;

import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncAddPhoneInfo;
import com.ylfcf.ppp.async.AsyncCheckRegister;
import com.ylfcf.ppp.async.AsyncCompApplyRegiste;
import com.ylfcf.ppp.async.AsyncGetUserInfoByPhone;
import com.ylfcf.ppp.async.AsyncLogin;
import com.ylfcf.ppp.async.AsyncRegiste;
import com.ylfcf.ppp.async.AsyncSMSRegiste;
import com.ylfcf.ppp.async.AsyncUserSelectOne;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.SMSType;
import com.ylfcf.ppp.entity.TaskDate;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnLoginInter;
import com.ylfcf.ppp.inter.Inter.OnRegisteInter;
import com.ylfcf.ppp.util.Constants.UserType;
import com.ylfcf.ppp.util.CountDownAsyncTask;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.util.YLFLogger;
import com.ylfcf.ppp.view.RegisteSucCompWindow;

/**
 * ע��
 *
 * @author Administrator
 *
 */
public class RegisteActivity extends BaseActivity implements OnClickListener {
	private static final String className = "RegisteActivity";
	//ע����ҳ��
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private Button navNormalPersonalBtn,navVipPersonalBtn,navCompanyBtn;
	private View normalPersonalLayout,vipPersonalLayout,companyLayout;
	private final long intervalTime = 1000L;
	private LinearLayout mainLayout;
	private String fromWhere;

	//��ͨ�����û�
	private EditText phoneNPET, pwdNPET, authnumNPSMSET;
	private EditText recommendNPNum;// �Ƽ���
	private TextView recommendNPName;//���ʦ������
	private Button registeNPBtn, getNPAuthnumBtn;
	private TextView loginNPText;//������¼
	private TextView protocolNPText;
	private String authnumNPSMSUser;// �û�������ֻ���֤��
	private String authnumNPSMSWeb;// ϵͳ���ɵ��ֻ���֤��
	private String phoneNPNum = null;
	private String pwdNP = null;
	private String extension_code_np = "";
	private CountDownAsyncTask countNPDownAsynTask = null;
	private Handler handlerNP = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case CountDownAsyncTask.PROGRESS_UPDATE:
					TaskDate date = (TaskDate) msg.obj;
					long time = date.getTime();
					countNPDownView(time);
					break;
				case CountDownAsyncTask.FINISH:
					getNPAuthnumBtn.setText("��ȡ��֤��");
					getNPAuthnumBtn.setEnabled(true);
					break;
				default:
					break;
			}
		}
	};

	//VIP�����û�
	private EditText phoneVIPET, pwdVIPET, authnumVIPSMSET;
	private EditText managerVIPNum;//��ƾ����
	private TextView managerVIPName;//���ʦ������
	private Button registeVIPBtn, getVIPAuthnumBtn;
	private TextView loginVIPText;//������¼
	private TextView protocolVIPText;
	private String authnumVIPSMSUser;// �û�������ֻ���֤��
	private String authnumVIPSMSWeb;// ϵͳ���ɵ��ֻ���֤��
	private CountDownAsyncTask countVIPDownAsynTask = null;
	private String phoneVIPNum = null;
	private String activityFlag = "";
	private Handler handlerVIP = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case CountDownAsyncTask.PROGRESS_UPDATE:
					TaskDate date = (TaskDate) msg.obj;
					long time = date.getTime();
					countVIPDownView(time);
					break;
				case CountDownAsyncTask.FINISH:
					getVIPAuthnumBtn.setText("��ȡ��֤��");
					getVIPAuthnumBtn.setEnabled(true);
					break;
				default:
					break;
			}
		}
	};

	//��ҵ�û�
	private EditText phoneCompanyET, authnumCompanySMSET,recommendCompanyNum;
	private Button registeCompanyBtn, getCompanyAuthnumBtn;
	private TextView loginCompanyText;//������¼
	private TextView managerNameComp;//��ƾ�������
	private TextView protocolCompanyText;//ע�����Э��
	private String authnumCompanySMSUser;// �û�������ֻ���֤��
	private String authnumCompanySMSWeb;// ϵͳ���ɵ��ֻ���֤��
	private CountDownAsyncTask countCompanyDownAsynTask = null;
	private String phoneCompanyNum = null;
	private Handler handlerCompany = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case CountDownAsyncTask.PROGRESS_UPDATE:
					TaskDate date = (TaskDate) msg.obj;
					long time = date.getTime();
					countCompanyDownView(time);
					break;
				case CountDownAsyncTask.FINISH:
					getCompanyAuthnumBtn.setText("��ȡ��֤��");
					getCompanyAuthnumBtn.setEnabled(true);
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
		setContentView(R.layout.registe_main);
		fromWhere = getIntent().getStringExtra("from_where");
		activityFlag = getIntent().getStringExtra("FLAG");
		findViews();
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("ע��");

		mainLayout = (LinearLayout) findViewById(R.id.registe_main_layout);
		//��ע��ҳ��
		navNormalPersonalBtn = (Button) findViewById(R.id.registe_main_nav_normal_personal_btn);
		navNormalPersonalBtn.setOnClickListener(this);
		navVipPersonalBtn = (Button) findViewById(R.id.registe_main_nav_vip_personal_btn);
		navVipPersonalBtn.setOnClickListener(this);
		navCompanyBtn = (Button) findViewById(R.id.registe_main_nav_company_btn);
		navCompanyBtn.setOnClickListener(this);
		normalPersonalLayout = findViewById(R.id.registe_main_normal_personal_mainlayout);
		vipPersonalLayout = findViewById(R.id.registe_main_vip_personal_mainlayout);
		companyLayout = findViewById(R.id.registe_main_company_personal_mainlayout);

		//��ͨ�����û�
		phoneNPET = (EditText) findViewById(R.id.registe_main_normal_personal_phone);
		pwdNPET = (EditText) findViewById(R.id.registe_main_normal_personal_pwd);
		authnumNPSMSET = (EditText) findViewById(R.id.registe_main_normal_personal_sms_authnum);
		recommendNPNum = (EditText) findViewById(R.id.registe_main_normal_personal_recommend_num);
		recommendNPName = (TextView) findViewById(R.id.registe_main_normal_personal_manager_name);
		loginNPText = (TextView) findViewById(R.id.registe_main_normal_personal_login_text);
		loginNPText.setOnClickListener(this);
		registeNPBtn = (Button) findViewById(R.id.registe_main_normal_personal_btn);
		registeNPBtn.setOnClickListener(this);
		registeNPBtn.setEnabled(false);
		getNPAuthnumBtn = (Button) findViewById(R.id.registe_main_normal_personal_authnum_btn);
		getNPAuthnumBtn.setOnClickListener(this);
		protocolNPText = (TextView) findViewById(R.id.registe_main_normal_personal_protocol);
		protocolNPText.setOnClickListener(this);
		recommendNPNum.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				YLFLogger.d("onTextChanged");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
				YLFLogger.d("beforeTextChanged");
			}

			@Override
			public void afterTextChanged(Editable s) {
				final String num = recommendNPNum.getText().toString();
				if(Util.checkPhoneNumber(num)){
					//2����û�����ʾ�û������꣬��ʼ�����̨
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							getUserInfoByPhone(num,"personal");
						}
					}, 1000L);
				}
			}
		});

		//VIP�����û�
		phoneVIPET = (EditText) findViewById(R.id.registe_main_vip_personal_phone);
		pwdVIPET = (EditText) findViewById(R.id.registe_main_vip_personal_pwd);
		authnumVIPSMSET = (EditText) findViewById(R.id.registe_main_vip_personal_sms_authnum);
		managerVIPNum = (EditText) findViewById(R.id.registe_main_vip_personal_manager_num);
		managerVIPName = (TextView) findViewById(R.id.registe_main_vip_personal_manager_name);
		loginVIPText = (TextView) findViewById(R.id.registe_main_vip_personal_login_text);
		loginVIPText.setOnClickListener(this);
		registeVIPBtn = (Button) findViewById(R.id.registe_main_vip_personal_btn);
		registeVIPBtn.setOnClickListener(this);
		registeVIPBtn.setEnabled(false);
		getVIPAuthnumBtn = (Button) findViewById(R.id.registe_main_vip_personal_authnum_btn);
		getVIPAuthnumBtn.setOnClickListener(this);
		protocolVIPText = (TextView) findViewById(R.id.registe_main_vip_personal_protocol);
		protocolVIPText.setOnClickListener(this);
		managerVIPNum.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				YLFLogger.d("onTextChanged");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
				YLFLogger.d("beforeTextChanged");
			}

			@Override
			public void afterTextChanged(Editable s) {
				final String num = managerVIPNum.getText().toString();
				if(Util.checkPhoneNumber(num)){
					//2����û�����ʾ�û������꣬��ʼ�����̨
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							getUserInfoByPhone(num,"personal");
						}
					}, 1000L);
				}
			}
		});

		//��ҵ�û�
		phoneCompanyET = (EditText) findViewById(R.id.registe_main_company_phone);
		authnumCompanySMSET = (EditText) findViewById(R.id.registe_main_company_sms_authnum);
		recommendCompanyNum = (EditText) findViewById(R.id.registe_main_company_recommend_num);
		registeCompanyBtn = (Button) findViewById(R.id.registe_main_company_btn);
		registeCompanyBtn.setOnClickListener(this);
		registeCompanyBtn.setEnabled(false);
		getCompanyAuthnumBtn = (Button) findViewById(R.id.registe_main_company_authnum_btn);
		getCompanyAuthnumBtn.setOnClickListener(this);
		loginCompanyText = (TextView) findViewById(R.id.registe_main_company_personal_login_text);
		loginCompanyText.setOnClickListener(this);
		protocolCompanyText = (TextView) findViewById(R.id.registe_main_company_personal_protocol);
		protocolCompanyText.setOnClickListener(this);
		managerNameComp = (TextView) findViewById(R.id.registe_main_company_rec_name);
		recommendCompanyNum.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				YLFLogger.d("onTextChanged");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
				YLFLogger.d("beforeTextChanged");
			}

			@Override
			public void afterTextChanged(Editable s) {
				final String num = recommendCompanyNum.getText().toString();
				if(Util.checkPhoneNumber(num)){
					//2����û�����ʾ�û������꣬��ʼ�����̨
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							getUserInfoByPhone(num,"company");
						}
					}, 1000L);
				}
			}
		});

		if(UserType.USER_COMPANY.equals(fromWhere)){
			//��ҵ�û�
			initCompanyLayout();
		}else if(UserType.USER_NORMAL_PERSONAL.equals(fromWhere)){
			//��ͨ�����û�
			initNormalPersonalLayout();
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
		handlerNP.removeCallbacksAndMessages(null);
		handlerVIP.removeCallbacksAndMessages(null);
		handlerCompany.removeCallbacksAndMessages(null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.common_topbar_left_layout:
			case R.id.registe_main_normal_personal_login_text:
			case R.id.registe_main_vip_personal_login_text:
			case R.id.registe_main_company_personal_login_text:
				finish();
				break;
			case R.id.registe_main_nav_normal_personal_btn:
				//��ͨ�����û�����
				initNormalPersonalLayout();
				break;
			case R.id.registe_main_nav_vip_personal_btn:
				//vip�����û�����
				initVipPersonalLayout();
				break;
			case R.id.registe_main_nav_company_btn:
				//��ҵ�û�
				initCompanyLayout();
				break;
			case R.id.registe_main_normal_personal_btn:
				checkNPUserData();
				break;
			case R.id.registe_main_vip_personal_btn:
				checkVIPUserData();
				break;
			case R.id.registe_main_company_btn:
				checkCompanyUserData();
				break;
			case R.id.registe_main_normal_personal_authnum_btn:
				checkNPAuthNumData();
				break;
			case R.id.registe_main_vip_personal_authnum_btn:
				checkVIPAuthNumData();
				break;
			case R.id.registe_main_company_authnum_btn:
				checkCompanyAuthData();
				break;
			case R.id.registe_main_normal_personal_protocol:
			case R.id.registe_main_vip_personal_protocol:
			case R.id.registe_main_company_personal_protocol:
				Intent intent = new Intent(RegisteActivity.this,
						RegisteAgreementActivity.class);
				startActivity(intent);
				break;
			default:
				break;
		}
	}

	/**
	 * ��ͨ�����û�ע��ҳ��
	 */
	private void initNormalPersonalLayout(){
		normalPersonalLayout.setVisibility(View.VISIBLE);
		vipPersonalLayout.setVisibility(View.GONE);
		companyLayout.setVisibility(View.GONE);

		navNormalPersonalBtn.setEnabled(false);
		navNormalPersonalBtn.setTextColor(getResources().getColor(R.color.common_topbar_bg_color));
		navVipPersonalBtn.setEnabled(true);
		navVipPersonalBtn.setTextColor(getResources().getColor(R.color.gray));
		navCompanyBtn.setEnabled(true);
		navCompanyBtn.setTextColor(getResources().getColor(R.color.gray));
	}

	/**
	 * VIP�����û�ע��ҳ��
	 */
	private void initVipPersonalLayout(){
		normalPersonalLayout.setVisibility(View.GONE);
		vipPersonalLayout.setVisibility(View.VISIBLE);
		companyLayout.setVisibility(View.GONE);

		navNormalPersonalBtn.setEnabled(true);
		navNormalPersonalBtn.setTextColor(getResources().getColor(R.color.gray));
		navVipPersonalBtn.setEnabled(false);
		navVipPersonalBtn.setTextColor(getResources().getColor(R.color.common_topbar_bg_color));
		navCompanyBtn.setEnabled(true);
		navCompanyBtn.setTextColor(getResources().getColor(R.color.gray));
	}

	/**
	 * ��ҵ�û�ע��ҳ��
	 */
	private void initCompanyLayout(){
		normalPersonalLayout.setVisibility(View.GONE);
		vipPersonalLayout.setVisibility(View.GONE);
		companyLayout.setVisibility(View.VISIBLE);

		navNormalPersonalBtn.setEnabled(true);
		navNormalPersonalBtn.setTextColor(getResources().getColor(R.color.gray));
		navVipPersonalBtn.setEnabled(true);
		navVipPersonalBtn.setTextColor(getResources().getColor(R.color.gray));
		navCompanyBtn.setEnabled(false);
		navCompanyBtn.setTextColor(getResources().getColor(R.color.common_topbar_bg_color));
	}

	/**
	 * ��ͨ�����û�ע��
	 */
	private void checkNPUserData() {
		phoneNPNum = phoneNPET.getText().toString();
		pwdNP = pwdNPET.getText().toString();
		extension_code_np = recommendNPNum.getText().toString();
		authnumNPSMSUser = authnumNPSMSET.getText().toString();
		if (Util.checkPhoneNumber(phoneNPNum)) {
			if (Util.checkPassword(pwdNP)) {
				if (authnumNPSMSUser.equals(authnumNPSMSWeb)) {
					// ����ע��ӿ�
					requestNPRegiste(phoneNPNum, pwdNP, extension_code_np);
				} else {
					Util.toastShort(RegisteActivity.this, "�ֻ���֤���������");
				}
			} else {
				Util.toastShort(RegisteActivity.this, "�����ʽ����");
			}
		} else {
			Util.toastShort(RegisteActivity.this, "�ֻ������ʽ����");
		}
	}

	/**
	 * VIP�����û�ע��
	 */
	private void checkVIPUserData(){
		phoneVIPNum = phoneVIPET.getText().toString();
		String pwd = pwdVIPET.getText().toString();
		String managerCode = managerVIPNum.getText().toString();
		authnumVIPSMSUser = authnumVIPSMSET.getText().toString();
		if (Util.checkPhoneNumber(phoneVIPNum)) {
			if (Util.checkPassword(pwd)) {
				if (authnumVIPSMSUser.equals(authnumVIPSMSWeb)) {
					// ����ע��ӿ�
					if(!managerCode.isEmpty()){
						requestVIPRegiste(phoneVIPNum, pwd, "",managerCode);
					}else{
						Util.toastShort(RegisteActivity.this, "������Ͷ�ʾ����");
					}
				} else {
					Util.toastShort(RegisteActivity.this, "�ֻ���֤���������");
				}
			} else {
				Util.toastShort(RegisteActivity.this, "�����ʽ����");
			}
		} else {
			Util.toastShort(RegisteActivity.this, "�ֻ������ʽ����");
		}
	}

	/**
	 * ��ҵ�û�ע��
	 */
	private void checkCompanyUserData(){
		phoneCompanyNum = phoneCompanyET.getText().toString();
		String extension_code_company = recommendCompanyNum.getText().toString();
		authnumCompanySMSUser = authnumCompanySMSET.getText().toString();
		if (Util.checkPhoneNumber(phoneCompanyNum)) {
			if (authnumCompanySMSUser.equals(authnumCompanySMSWeb)) {
				// ��������ע��ӿ�
				requestCompApplyRegiste(phoneCompanyNum,SettingsManager.USER_FROM,extension_code_company);
			} else {
				Util.toastShort(RegisteActivity.this, "�ֻ���֤���������");
			}
		} else {
			Util.toastShort(RegisteActivity.this, "�ֻ������ʽ����");
		}
	}

	/**
	 * ��ȡ�ֻ���֤�� ��ͨ�����û�
	 *
	 */
	private void checkNPAuthNumData() {
		phoneNPNum = phoneNPET.getText().toString();
		String pwd = pwdNPET.getText().toString();
		if (Util.checkPhoneNumber(phoneNPNum)) {
			if (Util.checkPassword(pwd)) {
				isNPPhoneRegisted(phoneNPNum);
			} else {
				Util.toastShort(RegisteActivity.this, "���볤�Ȳ���С��6λ");
			}
		} else {
			Util.toastShort(RegisteActivity.this, "�ֻ������ʽ����");
		}
	}

	/**
	 * ��ȡ�ֻ���֤�� VIP�����û�
	 */
	private void checkVIPAuthNumData(){
		phoneVIPNum = phoneVIPET.getText().toString();
		String pwd = pwdVIPET.getText().toString();
		if (Util.checkPhoneNumber(phoneVIPNum)) {
			if (Util.checkPassword(pwd)) {
				isVIPPhoneRegisted(phoneVIPNum);
			} else {
				Util.toastShort(RegisteActivity.this, "���볤�Ȳ���С��6λ");
			}
		} else {
			Util.toastShort(RegisteActivity.this, "�ֻ������ʽ����");
		}
	}

	/**
	 * ��ҵ�û�
	 */
	private void checkCompanyAuthData(){
		phoneCompanyNum = phoneCompanyET.getText().toString();
		if (Util.checkPhoneNumber(phoneCompanyNum)) {
			requestCompanyGetSMSAuthNum();
		} else {
			Util.toastShort(RegisteActivity.this, "�ֻ������ʽ����");
		}
	}

	/**
	 * ��ͨ�����û���ȡ������֤��
	 */
	private void requestNPGetSMSAuthNum() {
		// ���������֤��ӿ�
		String Params[] = SettingsManager.getSMSRegisteParams();// ƴ�Ӷ�����֤���ʽ
		authnumNPSMSWeb = Params[0];
		requestSMSAuthCode(phoneNPNum, SMSType.SMS_REGISTER, Params[1],
				Params[0], "get_authcode_np");
		YLFLogger.d("������֤�룺" + Params[0]);
		getNPAuthnumBtn.setEnabled(false);
		long createTime = System.currentTimeMillis();
		countNPDownAsynTask = new CountDownAsyncTask(handlerNP, "",
				System.currentTimeMillis(), createTime + 1000 * 60,
				intervalTime);
		SettingsManager.FULL_TASK_EXECUTOR.execute(countNPDownAsynTask);
	}

	/**
	 * VIP�����û���ȡ������֤��
	 */
	private void requestVIPGetSMSAuthNum() {
		// ���������֤��ӿ�
		String Params[] = SettingsManager.getSMSRegisteParams();// ƴ�Ӷ�����֤���ʽ
		authnumVIPSMSWeb = Params[0];
		requestSMSAuthCode(phoneVIPNum, SMSType.SMS_REGISTER, Params[1],
				Params[0], "get_authcode_vip");
		YLFLogger.d("������֤�룺" + Params[0]);
		getVIPAuthnumBtn.setEnabled(false);
		long createTime = System.currentTimeMillis();
		countVIPDownAsynTask = new CountDownAsyncTask(handlerVIP, "",
				System.currentTimeMillis(), createTime + 1000 * 60,
				intervalTime);
		SettingsManager.FULL_TASK_EXECUTOR.execute(countVIPDownAsynTask);
	}

	/**
	 * ��ҵ�û���ȡ������֤��
	 */
	private void requestCompanyGetSMSAuthNum() {
		// ���������֤��ӿ�
		String Params[] = SettingsManager.getSMSRegisteParams();// ƴ�Ӷ�����֤���ʽ
		authnumCompanySMSWeb = Params[0];
		requestSMSAuthCode(phoneCompanyNum, SMSType.SMS_REGISTER, Params[1],
				Params[0], "get_authcode_comp");
		YLFLogger.d("������֤�룺" + Params[0]);
		getCompanyAuthnumBtn.setEnabled(false);
		long createTime = System.currentTimeMillis();
		countCompanyDownAsynTask = new CountDownAsyncTask(handlerCompany, "",
				System.currentTimeMillis(), createTime + 1000 * 60,
				intervalTime);
		SettingsManager.FULL_TASK_EXECUTOR.execute(countCompanyDownAsynTask);
	}

	/**
	 * ������֤�뵹��ʱ ��ͨ�û�ע��
	 * @param time
	 */
	private void countNPDownView(long time) {
		time /= intervalTime;
		StringBuffer sb = new StringBuffer();
		sb.append(time).append("����ط�");
		getNPAuthnumBtn.setText(sb.toString());
	}

	/**
	 * ������֤�뵹��ʱ VIP�û�
	 * @param time
	 */
	private void countVIPDownView(long time) {
		time /= intervalTime;
		StringBuffer sb = new StringBuffer();
		sb.append(time).append("����ط�");
		getVIPAuthnumBtn.setText(sb.toString());
	}

	/**
	 * ��ҵ�û�
	 * @param time
	 */
	private void countCompanyDownView(long time){
		time /= intervalTime;
		StringBuffer sb = new StringBuffer();
		sb.append(time).append("����ط�");
		getCompanyAuthnumBtn.setText(sb.toString());
	}

	/**
	 * ��ҵ�û�ע��ɹ�
	 */
	private void showCompanyRegisteSuc(String phone){
		View popView = LayoutInflater.from(this).inflate(
				R.layout.registe_suc_comp_layout, null);
		int[] screen = SettingsManager.getScreenDispaly(RegisteActivity.this);
		int width = screen[0] * 4 / 5;
		int height = screen[1] * 3 / 8;
		RegisteSucCompWindow popwindow = new RegisteSucCompWindow(RegisteActivity.this,
				popView, width, height,phone,new OnRegisteCompSucClickListener() {
			@Override
			public void onClick() {
				finish();
			}
		});
		popwindow.show(mainLayout);
	}

	/**
	 * ��ͨ�����û�ע��
	 *
	 * @param phone
	 * @param password
	 */
	private void requestNPRegiste(String phone, String password,
								  String extension_code) {
		if(mLoadingDialog != null && !isFinishing())
		    mLoadingDialog.show();
		String open_id = "";
		AsyncRegiste registeTask = new AsyncRegiste(RegisteActivity.this,
				phone, password, open_id, SettingsManager.USER_FROM,activityFlag,
				SettingsManager.getUserFromSub(getApplicationContext()), extension_code,"�ⲿͶ��","",
				new OnRegisteInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								UserInfo userInfo = baseInfo.getUserInfo();
								// ����ע��ɹ��Ķ���
								if (userInfo != null) {
									String Params[] = SettingsManager
											.getSMSRegisteSuccessParams(userInfo
													.getUser_name());
									requestSMSAuthCode(phoneNPNum, SMSType.SMS_REGISTER_SUCCESS,
											Params[1], "", "register_success");
									requestNPLogin(phoneNPNum, pwdNP);
								}
							} else {
								if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
									mLoadingDialog.dismiss();
								}
								Util.toastShort(RegisteActivity.this, baseInfo.getMsg());
							}
						}else{
						}
					}
				});
		registeTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * VIP�����û�ע��
	 *
	 * @param phone
	 * @param password
	 */
	private void requestVIPRegiste(final String phone, final String password,
								   String extension_code,String salesPhone) {
		if(mLoadingDialog != null && !isFinishing())
		mLoadingDialog.show();
		String open_id = "";

		AsyncRegiste registeTask = new AsyncRegiste(RegisteActivity.this,
				phone, password, open_id, SettingsManager.USER_FROM,
				SettingsManager.getUserFromSub(getApplicationContext()), activityFlag, extension_code,"vip",salesPhone,
				new OnRegisteInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						int resultCode = SettingsManager
								.getResultCode(baseInfo);
						if (resultCode == 0) {
							UserInfo userInfo = baseInfo.getUserInfo();
							// ����ע��ɹ��Ķ���
							if (userInfo != null) {
								String Params[] = SettingsManager
										.getSMSRegisteSuccessParams(userInfo
												.getUser_name());
								requestSMSAuthCode(phoneVIPNum,
										SMSType.SMS_REGISTER_SUCCESS,
										Params[1], "", "register_success");
								requestVIPLogin(phone, password);
							}
						} else {
							if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
								mLoadingDialog.dismiss();
							}
							Util.toastShort(RegisteActivity.this,
									baseInfo.getMsg());
						}
					}
				});
		registeTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * ���Ͷ�����֤��
	 */
	private void requestSMSAuthCode(String phone, String template,
									String params, String verfiy, final String flag) {
		AsyncSMSRegiste asyncSMSRegiste = new AsyncSMSRegiste(
				RegisteActivity.this, phone, template, params, verfiy,
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
							mLoadingDialog.dismiss();
						}
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								if("register_success".equals(flag)){
									//ע��ɹ�
								}else if("get_authcode_np".equals(flag)){
									//��ͨ�û���ȡ��֤��
									registeNPBtn.setEnabled(true);
								}else if("get_authcode_vip".equals(flag)){
									//vip�û���ȡ��֤��
									registeVIPBtn.setEnabled(true);
								}else if("get_authcode_comp".equals(flag)){
									//��ҵ�û���ȡ��֤��
									registeCompanyBtn.setEnabled(true);
								}
							}else{
                                Util.toastLong(RegisteActivity.this,baseInfo.getMsg());
                            }
						}
					}
				});
		asyncSMSRegiste.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * ��ͨ�����û���¼
	 * @param phone
	 * @param pwd
	 */
	private void requestNPLogin(final String phone,final String pwd){
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}

		AsyncLogin loginTask = new AsyncLogin(RegisteActivity.this, phone, pwd, new OnLoginInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(mLoadingDialog != null && mLoadingDialog.isShowing()){
					mLoadingDialog.dismiss();
				}
				if(baseInfo == null){
					return;
				}
				int resultCode = SettingsManager.getResultCode(baseInfo);
				if(resultCode == 0){
					UserInfo userInfo = baseInfo.getUserInfo();
					if(userInfo != null){
						SettingsManager.setUser(RegisteActivity.this,phone);
						SettingsManager.setLoginPassword(RegisteActivity.this,pwd,true);
						SettingsManager.setUserId(RegisteActivity.this, userInfo.getId());
						SettingsManager.setUserName(RegisteActivity.this, userInfo.getUser_name());
						SettingsManager.setUserRegTime(RegisteActivity.this, userInfo.getReg_time());
//						SettingsManager.setMainFirstpageFlag(RegisteActivity.this,true);
						addPhoneInfo(userInfo.getId(), phone, "", "");
					}
					Intent intent = new Intent(RegisteActivity.this,RegisterSucActivity.class);
					intent.putExtra("extension_code", extension_code_np);
					startActivity(intent);
					finish();
				}else{
				}
			}
		});
		loginTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * VIP�����û���¼
	 * @param phone
	 * @param pwd
	 */
	private void requestVIPLogin(final String phone,final String pwd){
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}

		AsyncLogin loginTask = new AsyncLogin(RegisteActivity.this, phone, pwd, new OnLoginInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(mLoadingDialog != null && mLoadingDialog.isShowing()){
					mLoadingDialog.dismiss();
				}
				if(baseInfo == null){
					return;
				}
				int resultCode = SettingsManager.getResultCode(baseInfo);
				if(resultCode == 0){
					UserInfo userInfo = baseInfo.getUserInfo();
					if(userInfo != null){
						SettingsManager.setUser(RegisteActivity.this,phone);
						SettingsManager.setLoginPassword(RegisteActivity.this,pwd,true);
						SettingsManager.setUserId(RegisteActivity.this, userInfo.getId());
						SettingsManager.setUserName(RegisteActivity.this, userInfo.getUser_name());
						SettingsManager.setUserRegTime(RegisteActivity.this, userInfo.getReg_time());
						addPhoneInfo(userInfo.getId(), phone, "", "");
					}
					Intent intent = new Intent(RegisteActivity.this,RegisterSucActivity.class);
					startActivity(intent);
					finish();
				}else{
				}
			}
		});
		loginTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * ���ֻ���Ϣ���뵽��̨���ݿ�
	 * @param userId
	 * @param phone
	 * @param location
	 * @param contact
	 */
	private void addPhoneInfo(String userId,String phone,String location,String contact){
		String phoneModel = android.os.Build.MODEL;
		String sdkVersion = android.os.Build.VERSION.SDK;
		String systemVersion = android.os.Build.VERSION.RELEASE;
		AsyncAddPhoneInfo addPhoneInfoTask = new AsyncAddPhoneInfo(RegisteActivity.this, userId, phone, phoneModel,
				sdkVersion, systemVersion, "android", location, contact, new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {

			}
		});
		addPhoneInfoTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * �ж��ֻ������Ƿ��Ѿ���ע��
	 *
	 * @param phone
	 */
	private void isNPPhoneRegisted(String phone) {
		AsyncCheckRegister checkRegisterTask = new AsyncCheckRegister(
				RegisteActivity.this, phone, new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if (baseInfo != null) {
					int resultCode = SettingsManager
							.getResultCode(baseInfo);
					if (resultCode == 0) {
						String msg = baseInfo.getMsg();
						if ("1".equals(msg)) {
							// ��ʾ�Ѿ���ע��
							Util.toastLong(RegisteActivity.this,
									"���ֻ������Ѿ���ע��");
						} else {
							requestNPGetSMSAuthNum();
						}
					}
				}
			}
		});
		checkRegisterTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * �ж��ֻ������Ƿ��Ѿ���ע��  VIP�����û�
	 *
	 * @param phone
	 */
	private void isVIPPhoneRegisted(String phone) {
		AsyncCheckRegister checkRegisterTask = new AsyncCheckRegister(
				RegisteActivity.this, phone, new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if (baseInfo != null) {
					int resultCode = SettingsManager
							.getResultCode(baseInfo);
					if (resultCode == 0) {
						String msg = baseInfo.getMsg();
						if ("1".equals(msg)) {
							// ��ʾ�Ѿ���ע��
							Util.toastLong(RegisteActivity.this,
									"���ֻ������Ѿ���ע��");
						} else {
							requestVIPGetSMSAuthNum();
						}
					}
				}
			}
		});
		checkRegisterTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * �����ֻ������ȡ�û���Ϣ
	 * @param phone
	 */
	private void getUserInfoByPhone(final String phone,final String type){
		AsyncGetUserInfoByPhone phoneTask = new AsyncGetUserInfoByPhone(RegisteActivity.this, phone,
				new Inter.OnGetUserInfoByPhone() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						UserInfo user = baseInfo.getUserInfo();
						if(user == null){
							return;
						}
						if("personal".equals(type)){
							registeNPBtn.setEnabled(true);
							if(user.getReal_name() == null || "".equals(user.getReal_name())){
								recommendNPName.setVisibility(View.GONE);
							}else{
								recommendNPName.setVisibility(View.VISIBLE);
								recommendNPName.setText("�Ƽ��ˣ�"+Util.hidRealName2(user.getReal_name()));
							}
						}else if("company".equals(type)){
							registeCompanyBtn.setEnabled(true);
							if(user.getReal_name() == null || "".equals(user.getReal_name())){
								managerNameComp.setVisibility(View.GONE);
							}else{
								managerNameComp.setVisibility(View.VISIBLE);
								managerNameComp.setText("�Ƽ��ˣ�"+Util.hidRealName2(user.getReal_name()));
							}
						}
					}else{
						getUserInfoById("","",phone,type);
					}
				}
			}
		}
		);
		phoneTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 *
	 * @param userId
	 * @param phone
	 * @param coMobile
	 */
	private void getUserInfoById(String userId,String phone,final String coMobile,final String type){
		AsyncUserSelectOne task = new AsyncUserSelectOne(RegisteActivity.this,userId,phone,coMobile,"",
				new Inter.OnGetUserInfoByPhone(){
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								UserInfo user = baseInfo.getUserInfo();
								if(user == null){
									return;
								}
								if("personal".equals(type)){
									registeNPBtn.setEnabled(true);
									if(user.getReal_name() == null || "".equals(user.getReal_name())){
										recommendNPName.setVisibility(View.GONE);
									}else{
										recommendNPName.setVisibility(View.VISIBLE);
										recommendNPName.setText("�Ƽ��ˣ�"+Util.hidRealName2(user.getReal_name()));
									}
								}else if("company".equals(type)){
									registeCompanyBtn.setEnabled(true);
									if(user.getReal_name() == null || "".equals(user.getReal_name())){
										managerNameComp.setVisibility(View.GONE);
									}else{
										managerNameComp.setVisibility(View.VISIBLE);
										managerNameComp.setText("�Ƽ��ˣ�"+Util.hidRealName2(user.getReal_name()));
									}
								}
							}else if(resultCode == -1){
								//��ȡʧ��
								Util.toastLong(RegisteActivity.this,"�Ƽ����ֻ��Ŵ���");
							}else{
								Util.toastLong(RegisteActivity.this, baseInfo.getMsg());
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * ��ȡ���ʦ������
	 * @param phone
	 */
//	private void getLCSName(String phone,final String type){
//		AsyncGetLCSName lcsTask = new AsyncGetLCSName(RegisteActivity.this, phone,
//				new OnCommonInter() {
//					@Override
//					public void back(BaseInfo baseInfo) {
//						if(baseInfo != null){
//							int resultCode = SettingsManager.getResultCode(baseInfo);
//							if(resultCode == 0){
//								//��ȡ�ɹ�
//								if("vip_personal".equals(type)){
//									registeVIPBtn.setEnabled(true);
//									managerVIPName.setVisibility(View.VISIBLE);
//									managerVIPName.setText("�Ƽ��ˣ�"+baseInfo.getMsg());
//									managerVIPNum.setEnabled(false);
//								}else if("vip_company".equals(type)){
//									registeCompanyBtn.setEnabled(true);
//									managerNameComp.setVisibility(View.VISIBLE);
//									managerNameComp.setText("�Ƽ��ˣ�"+Util.hidRealName2(baseInfo.getMsg()));
//									recommendCompanyNum.setEnabled(false);
//								}
//							}else if(resultCode == -1){
//								//��ȡʧ��
//								Util.toastLong(RegisteActivity.this,"�Ƽ����ֻ��Ŵ���");
//							}else{
//								Util.toastLong(RegisteActivity.this, baseInfo.getMsg());
//							}
//						}
//					}
//				});
//		lcsTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
//	}

	/**
	 * ��ҵ�û�����ע��
	 * @param phone
	 * @param userFrom
	 * @param extensionCode
	 */
	private void requestCompApplyRegiste(final String phone,String userFrom,String extensionCode){
		AsyncCompApplyRegiste task = new AsyncCompApplyRegiste(RegisteActivity.this, phone, userFrom, extensionCode,
				new OnCommonInter(){
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								showCompanyRegisteSuc(phone);
							}else{
								Util.toastLong(RegisteActivity.this, baseInfo.getMsg());
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * ��ҵ�û�ע��ɹ�
	 * @author Mr.liu
	 *
	 */
	public interface OnRegisteCompSucClickListener{
		void onClick();
	}
}
