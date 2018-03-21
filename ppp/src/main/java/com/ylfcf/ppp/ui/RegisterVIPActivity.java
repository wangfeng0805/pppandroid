package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.ylfcf.ppp.async.AsyncGetLCSName;
import com.ylfcf.ppp.async.AsyncLogin;
import com.ylfcf.ppp.async.AsyncRegiste;
import com.ylfcf.ppp.async.AsyncSMSRegiste;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.SMSType;
import com.ylfcf.ppp.entity.TaskDate;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnLoginInter;
import com.ylfcf.ppp.inter.Inter.OnRegisteInter;
import com.ylfcf.ppp.util.CountDownAsyncTask;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.util.YLFLogger;

/**
 * VIPע��
 * @author Mr.liu
 *
 */
public class RegisterVIPActivity extends BaseActivity implements OnClickListener{
	private static final String className = "RegisterVIPActivity";
	private EditText phoneET, pwdET, authnumSMSET;
	private EditText managerNum;//��ƾ����
	private TextView managerName;//���ʦ������
	private Button registeBtn, getAuthnumBtn;
	private LinearLayout recNumLayout;//�Ƽ���
	private LinearLayout managerNumLayout;//��ƾ����
	private TextView loginText;//������¼
	private TextView protocolText;
	private String authnumSMSUser;// �û�������ֻ���֤��
	private String authnumSMSWeb;// ϵͳ���ɵ��ֻ���֤��

	private LinearLayout topLeftBtn;
	private TextView topTitleTV;

	private CountDownAsyncTask countDownAsynTask = null;
	private final long intervalTime = 1000L;
	private String phoneNum = null;

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
				getAuthnumBtn.setText("��ȡ��֤��");
				getAuthnumBtn.setEnabled(true);
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
		findViews();
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("VIP�û�ע��");

//		phoneET = (EditText) findViewById(R.id.registe_main_phone);
		pwdET = (EditText) findViewById(R.id.registe_main_normal_personal_pwd);
		authnumSMSET = (EditText) findViewById(R.id.registe_main_normal_personal_sms_authnum);
//		managerNum = (EditText) findViewById(R.id.registe_main_manager_num);
		managerName = (TextView) findViewById(R.id.registe_main_normal_personal_manager_name);
		recNumLayout = (LinearLayout) findViewById(R.id.registe_main_normal_personal_recnum_layout);
		recNumLayout.setVisibility(View.GONE);
//		managerNumLayout = (LinearLayout) findViewById(R.id.registe_main_manager_layout);
		managerNumLayout.setVisibility(View.VISIBLE);
		loginText = (TextView) findViewById(R.id.registe_main_normal_personal_login_text);
		loginText.setOnClickListener(this);
		registeBtn = (Button) findViewById(R.id.registe_main_normal_personal_btn);
		registeBtn.setOnClickListener(this);
		registeBtn.setEnabled(false);
		getAuthnumBtn = (Button) findViewById(R.id.registe_main_normal_personal_authnum_btn);
		getAuthnumBtn.setOnClickListener(this);
		protocolText = (TextView) findViewById(R.id.registe_main_normal_personal_protocol);
		protocolText.setOnClickListener(this);
//		managerNum.setOnFocusChangeListener(new OnFocusChangeListener() {
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				if(!hasFocus){
//					String num = managerNum.getText().toString();
//					getLCSName(num);
//				}
//			}
//		});
		managerNum.addTextChangedListener(new TextWatcher() {
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
				final String num = managerNum.getText().toString();
				if(Util.checkPhoneNumber(num)){
					//2����û�����ʾ�û������꣬��ʼ�����̨
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							getLCSName(num);
						}
					}, 1000L);
				}
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
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
		case R.id.registe_main_normal_personal_login_text:
			finish();
			break;
		case R.id.registe_main_normal_personal_btn:
			checkUserData();
			break;
		case R.id.registe_main_normal_personal_authnum_btn:
			// ���ж��ֻ��Ƿ��Ѿ���ע��
			checkAuthNumData();
			break;
		case R.id.registe_main_normal_personal_protocol:
			Intent intent = new Intent(RegisterVIPActivity.this,
					RegisteAgreementActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	/**
	 * ע��
	 */
	private void checkUserData() {
		phoneNum = phoneET.getText().toString();
		String pwd = pwdET.getText().toString();
		String managerCode = managerNum.getText().toString();
		authnumSMSUser = authnumSMSET.getText().toString();
		if (Util.checkPhoneNumber(phoneNum)) {
			if (Util.checkPassword(pwd)) {
				if (authnumSMSUser.equals(authnumSMSWeb)) {
					// ����ע��ӿ�
					if(!managerCode.isEmpty()){
						requestRegiste(phoneNum, pwd, "",managerCode);
					}else{
						Util.toastShort(RegisterVIPActivity.this, "������Ͷ�ʾ����");
					}
				} else {
					Util.toastShort(RegisterVIPActivity.this, "�ֻ���֤���������");
				}
			} else {
				Util.toastShort(RegisterVIPActivity.this, "�����ʽ����");
			}
		} else {
			Util.toastShort(RegisterVIPActivity.this, "�ֻ������ʽ����");
		}
	}

	/**
	 * ��ȡ�ֻ���֤��
	 * 
	 */
	private void checkAuthNumData() {

		phoneNum = phoneET.getText().toString();
		String pwd = pwdET.getText().toString();
		if (Util.checkPhoneNumber(phoneNum)) {
			if (Util.checkPassword(pwd)) {
				isPhoneRegisted(phoneNum);
			} else {
				Util.toastShort(RegisterVIPActivity.this, "���볤�Ȳ���С��6λ");
			}
		} else {
			Util.toastShort(RegisterVIPActivity.this, "�ֻ������ʽ����");
		}
	}

	private void requestGetSMSAuthNum() {
		// ���������֤��ӿ�
		String Params[] = SettingsManager.getSMSRegisteParams();// ƴ�Ӷ�����֤���ʽ
		authnumSMSWeb = Params[0];
		requestSMSAuthCode(phoneNum, SMSType.SMS_REGISTER, Params[1],
				Params[0], "");
		YLFLogger.d("������֤�룺" + Params[0]);
		getAuthnumBtn.setEnabled(false);
		long createTime = System.currentTimeMillis();
		countDownAsynTask = new CountDownAsyncTask(handler, "",
				System.currentTimeMillis(), createTime + 1000 * 60,
				intervalTime);
		SettingsManager.FULL_TASK_EXECUTOR.execute(countDownAsynTask);
	}

	private void countDownView(long time) {
		time /= intervalTime;
		StringBuffer sb = new StringBuffer();
		sb.append(time).append("����ط�");
		getAuthnumBtn.setText(sb.toString());
	}

	/**
	 * ע��
	 * 
	 * @param phone
	 * @param password
	 */
	private void requestRegiste(final String phone, final String password,
			String extension_code,String salesPhone) {
		if(mLoadingDialog != null && !isFinishing())
		mLoadingDialog.show();
		String open_id = "";
		String user_from_host = "";

		AsyncRegiste registeTask = new AsyncRegiste(RegisterVIPActivity.this,
				phone, password, open_id, SettingsManager.USER_FROM,
				SettingsManager.getUserFromSub(getApplicationContext()), user_from_host, extension_code,"vip",salesPhone,
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
								requestSMSAuthCode(phoneNum,
										SMSType.SMS_REGISTER_SUCCESS,
										Params[1], "", "register_success");
								requestLogin(phone, password);
							}
						} else {
							if (mLoadingDialog != null
									&& mLoadingDialog.isShowing()) {
								mLoadingDialog.dismiss();
							}
							Util.toastShort(RegisterVIPActivity.this,
									baseInfo.getMsg());
						}
					}
				});
		registeTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * ��¼
	 * @param phone
	 * @param pwd
	 */
	private void requestLogin(final String phone,final String pwd){
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		
		AsyncLogin loginTask = new AsyncLogin(RegisterVIPActivity.this, phone, pwd, new OnLoginInter() {
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
						SettingsManager.setUser(RegisterVIPActivity.this,phone);
						SettingsManager.setLoginPassword(RegisterVIPActivity.this,pwd,true);
						SettingsManager.setUserId(RegisterVIPActivity.this, userInfo.getId());
						SettingsManager.setUserName(RegisterVIPActivity.this, userInfo.getUser_name());
						SettingsManager.setUserRegTime(RegisterVIPActivity.this, userInfo.getReg_time());
						addPhoneInfo(userInfo.getId(), phone, "", "");
					}
					Intent intent = new Intent(RegisterVIPActivity.this,RegisterSucActivity.class);
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
		AsyncAddPhoneInfo addPhoneInfoTask = new AsyncAddPhoneInfo(RegisterVIPActivity.this, userId, phone, phoneModel, 
				sdkVersion, systemVersion, "android", location, contact, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						
					}
				});
		addPhoneInfoTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * ���Ͷ�����֤��
	 */
	private void requestSMSAuthCode(String phone, String template,
			String params, String verfiy, final String flag) {
		AsyncSMSRegiste asyncSMSRegiste = new AsyncSMSRegiste(
				RegisterVIPActivity.this, phone, template, params, verfiy,
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
							mLoadingDialog.dismiss();
						}
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0
									&& "register_success".equals(flag)) {
							}
						}
					}
				});
		asyncSMSRegiste.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * �ж��ֻ������Ƿ��Ѿ���ע��
	 * 
	 * @param phone
	 */
	private void isPhoneRegisted(String phone) {
		AsyncCheckRegister checkRegisterTask = new AsyncCheckRegister(
				RegisterVIPActivity.this, phone, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								String msg = baseInfo.getMsg();
								if ("1".equals(msg)) {
									// ��ʾ�Ѿ���ע��
									Util.toastLong(RegisterVIPActivity.this,
											"���ֻ������Ѿ���ע��");
								} else {
									requestGetSMSAuthNum();
								}
							}
						}
					}
				});
		checkRegisterTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * ��ȡ���ʦ������
	 * @param phone
	 */
	private void getLCSName(String phone){
		AsyncGetLCSName lcsTask = new AsyncGetLCSName(RegisterVIPActivity.this, phone, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//��ȡ�ɹ�
								registeBtn.setEnabled(true);
								managerName.setVisibility(View.VISIBLE);
								managerName.setText("Ͷ�ʾ���������"+baseInfo.getMsg());
								managerNum.setEnabled(false);
							}else{
								//��ȡʧ��
								Util.toastLong(RegisterVIPActivity.this, baseInfo.getMsg());
							}
						}
					}
				});
		lcsTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
