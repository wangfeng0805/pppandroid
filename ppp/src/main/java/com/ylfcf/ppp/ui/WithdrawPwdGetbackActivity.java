package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncSMSRegiste;
import com.ylfcf.ppp.async.AsyncUpdateUserInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.SMSType;
import com.ylfcf.ppp.entity.TaskDate;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnUpdateUserInfoInter;
import com.ylfcf.ppp.util.CountDownAsyncTask;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.util.YLFLogger;
import com.ylfcf.ppp.view.CommonPopwindow;

/**
 * �һ��������� | ������������
 * ����intent���ݹ����Ĳ������ж��������������뻹���һ��������롣
 * @author Mr.liu
 *
 */
public class WithdrawPwdGetbackActivity extends BaseActivity implements OnClickListener{
	private static final String className = "WithdrawPwdGetbackActivity";
	private static final int REQUEST_UPDATE_USERINFO_WHAT = 1301;
	private static final int REQUEST_UPDATE_USERINFO_SUCCESS = 1302;
	private static final int REQUEST_UPDATE_USERINFO_FAILE = 1303;
	private static final int REQUEST_UPDATE_USERINFO_EXCEPTION = 1304;
	
	private static final int REQUEST_SEND_SMS_SUCCESS = 1305;
	private static final int REQEUST_SEND_SMS_FAILE = 1306;
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private LinearLayout mainLayout;
	
	private LinearLayout tipsLayout;//������ʾ����
	private EditText authNumET;
	private EditText newPwdET;
	private EditText repeatPwdET;
	private Button getAuthNumBtn;
	private Button commitBtn;
	
	private String authnumSMSUser = "";//�û�������ֻ���֤��
	private String authnumSMSWeb = "";//ϵͳ���ɵ��ֻ���֤��
	private String intentType = "�һ�";//������ת���ݹ����Ĳ���ֵ����ʾ������������ҳ�滹���һ���������ҳ��
	
	private CountDownAsyncTask countDownAsynTask = null;
	private final long intervalTime = 1000L;
	
	private Handler handler = new Handler(){
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
         		getAuthNumBtn.setText("��ȡ��֤��");
         		getAuthNumBtn.setEnabled(true);
         		break;
			case REQUEST_UPDATE_USERINFO_WHAT:
				
				break;
			case REQUEST_UPDATE_USERINFO_SUCCESS:
				if("����".equals(intentType)){
					if(SettingsManager.isPersonalUser(getApplicationContext())){
						Intent intent = new Intent(WithdrawPwdGetbackActivity.this,WithdrawActivity.class);
						startActivity(intent);
					}else if(SettingsManager.isCompanyUser(getApplicationContext())){
						Intent intent = new Intent(WithdrawPwdGetbackActivity.this,WithdrawCompActivity.class);
						startActivity(intent);
					}
				}else{
					Util.toastLong(WithdrawPwdGetbackActivity.this, "��ϲ���������һسɹ���");
				}
				finish();
				break;
			case REQUEST_UPDATE_USERINFO_FAILE:
				Util.toastShort(WithdrawPwdGetbackActivity.this, "�����쳣");
				break;
			case REQUEST_UPDATE_USERINFO_EXCEPTION:
				BaseInfo info = (BaseInfo) msg.obj;
				Util.toastShort(WithdrawPwdGetbackActivity.this, info.getError());
				break;
			case REQEUST_SEND_SMS_FAILE:
				BaseInfo baseInfo = (BaseInfo) msg.obj;
				Util.toastShort(WithdrawPwdGetbackActivity.this, baseInfo.getError());
				break;
			case REQUEST_SEND_SMS_SUCCESS:
				Util.toastShort(WithdrawPwdGetbackActivity.this, "������֤���ѷ��ͳɹ���");
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
		setContentView(R.layout.withdrawpwd_getback_activity);
		intentType = getIntent().getStringExtra("type");
		findViews();
	}
	
	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		
		mainLayout = (LinearLayout) findViewById(R.id.withdrawpwd_getback_activity_mainlayout);
		tipsLayout = (LinearLayout) findViewById(R.id.withdrawpwd_getback_activity_tiplayout);
		authNumET = (EditText)findViewById(R.id.getback_transpwd_check_et);
		getAuthNumBtn = (Button)findViewById(R.id.getback_transpwd_get_authnum_btn);
		getAuthNumBtn.setOnClickListener(this);
		newPwdET = (EditText)findViewById(R.id.getback_transpwd_activity_newpwd);
		repeatPwdET = (EditText)findViewById(R.id.getback_transpwd_activity_newpwd_repeat);
		commitBtn = (Button)findViewById(R.id.getback_transpwd_sure_btn);
		commitBtn.setOnClickListener(this);
		if("����".equals(intentType)){
			//������������
			topTitleTV.setText("������������");
			tipsLayout.setVisibility(View.VISIBLE);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					showWithdrawPwdSettingPrompt();
				}
			}, 500L);
		}else{
			topTitleTV.setText("�һ���������");
			tipsLayout.setVisibility(View.GONE);
		}
	}

	/**
	 * �û��������ҳ�����Ҫ�����������������ʾ
	 */
	private void showWithdrawPwdSettingPrompt(){
		View popView = LayoutInflater.from(this).inflate(R.layout.common_popwindow, null);
		int[] screen = SettingsManager.getScreenDispaly(WithdrawPwdGetbackActivity.this);
		int width = screen[0]*4/5;
		int height = screen[1]*1/5;
		CommonPopwindow popwindow = new CommonPopwindow(WithdrawPwdGetbackActivity.this,popView, width, height,"������������","",null);
		popwindow.show(mainLayout);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.getback_transpwd_get_authnum_btn:
			sendSMScode();
			break;
		case R.id.getback_transpwd_sure_btn:
			resetPwd();
			break;
		default:
			break;
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
		handler.removeCallbacksAndMessages(null);
	}
	/**
	 * ��ȡ��֤�뵹��ʱ
	 * @param time
	 */
	private void countDownView(long time){
		time /= intervalTime; 
		StringBuffer sb = new StringBuffer();
		sb.append(time).append("����ط�");
		getAuthNumBtn.setText(sb.toString());
	}
	
	private void sendSMScode(){
		String params[] = null;
		String userName = SettingsManager.getUserName(getApplicationContext());
		String userPhone = SettingsManager.getUser(getApplicationContext());
		if("����".equals(intentType)){
			//������������
			if(userName != null && !"".equals(userName)){
				if(SettingsManager.isPersonalUser(getApplicationContext())){
					params = SettingsManager.getSMSSettingDealPwdParams(userName);//ƴ�Ӷ�����֤���ʽ
				}else if(SettingsManager.isCompanyUser(getApplicationContext())){
					userPhone = SettingsManager.getCompPhone(getApplicationContext());
					params = SettingsManager.getSMSSettingDealPwdCompParams(userName);//ƴ�Ӷ�����֤���ʽ
				}
			}else{
				params = SettingsManager.getSMSSettingDealPwdParamsDefault();//ƴ�Ӷ�����֤���ʽ
			}
			
			authnumSMSWeb = params[0];
			YLFLogger.d("�����������룺"+params[0]+"______"+params[1]);
			requestSMSAuthCode(userPhone,SMSType.SMS_SETTING_DEAL_PWD,params[1],params[0]);
		}else{
			//�һ���������
			if(userName != null && !"".equals(userName)){
				if(SettingsManager.isPersonalUser(getApplicationContext())){
					params = SettingsManager.getSMSGetbackDealPwdParams(userName);//ƴ�Ӷ�����֤���ʽ
				}else if(SettingsManager.isCompanyUser(getApplicationContext())){
					userPhone = SettingsManager.getCompPhone(getApplicationContext());
					params = SettingsManager.getSMSGetbackDealPwdCompParams(userName);//ƴ�Ӷ�����֤���ʽ
				}else{
					params = SettingsManager.getSMSGetbackDealPwdParams(userName);//ƴ�Ӷ�����֤���ʽ
				}
			}else{
				params = SettingsManager.getSMSGetbackDealPwdParamsDefault();//ƴ�Ӷ�����֤���ʽ
			}
			
			authnumSMSWeb = params[0];
			YLFLogger.d("�һ��������룺"+params[0]+"______"+params[1]);
			requestSMSAuthCode(userPhone,SMSType.SMS_GETBACK_DEAL_PASSWORD,params[1],params[0]);
		}
		
		getAuthNumBtn.setEnabled(false);
		long createTime = System.currentTimeMillis();
		countDownAsynTask = new CountDownAsyncTask(handler,"",System.currentTimeMillis(),
				createTime+1000*60,intervalTime);
		SettingsManager.FULL_TASK_EXECUTOR.execute(countDownAsynTask);
		
	}
	
	private void resetPwd(){
		authnumSMSUser = authNumET.getText().toString();
		String newPwd = newPwdET.getText().toString();
		String repeatPwd = repeatPwdET.getText().toString();
		if("".equals(authnumSMSUser)){
			Util.toastShort(WithdrawPwdGetbackActivity.this, "�����������֤��");
			return;
		}
		if(authnumSMSWeb.equals(authnumSMSUser)){
			if(Util.checkPassword(newPwd)){
				if(newPwd.equals(repeatPwd)){
					updateUserInfo(SettingsManager.getUserId(getApplicationContext()), "", SettingsManager.getUser(getApplicationContext()), "", "","",newPwd,"","");
				}else{
					Util.toastShort(WithdrawPwdGetbackActivity.this, "����ǰ���������벻һ��");
				}
			}else{
				Util.toastShort(WithdrawPwdGetbackActivity.this, "���볤�Ȳ���С��6λ");
			}
		}else{
			Util.toastShort(WithdrawPwdGetbackActivity.this, "������֤���������");
		}
	}

	/**
	 * �����û���Ϣ
	 * @param id
	 * @param password
	 * @param phone
	 * @param email
	 * @param openId
	 */
	private void updateUserInfo(String id,String password,String phone,String email,String openId,String dealEnabled,String dealPwd,String tmpData,String initPwd){
		AsyncUpdateUserInfo asyncUpdateUserInfo = new AsyncUpdateUserInfo(WithdrawPwdGetbackActivity.this, id, password, 
				phone, email, openId, dealEnabled,dealPwd,tmpData,initPwd,new OnUpdateUserInfoInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								Message msg = handler.obtainMessage(REQUEST_UPDATE_USERINFO_SUCCESS);
								handler.sendMessage(msg);
							}else{
								Message msg = handler.obtainMessage(REQUEST_UPDATE_USERINFO_EXCEPTION);
								msg.obj = baseInfo;
								handler.sendMessage(msg);
							}
						}else{
							Message msg = handler.obtainMessage(REQUEST_UPDATE_USERINFO_FAILE);
							handler.sendMessage(msg);
						}
					}
				});
		asyncUpdateUserInfo.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * ���Ͷ�����֤��
	 */
	private void requestSMSAuthCode(String phone,String template,String params,String verfiy){
		AsyncSMSRegiste asyncSMSRegiste = new AsyncSMSRegiste(WithdrawPwdGetbackActivity.this, phone, template, params, verfiy, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								Message msg = handler.obtainMessage(REQUEST_SEND_SMS_SUCCESS);
								msg.obj = baseInfo;
								handler.sendMessage(msg);
							}else{
								Message msg = handler.obtainMessage(REQEUST_SEND_SMS_FAILE);
								msg.obj = baseInfo;
								handler.sendMessage(msg);
							}
						}
					}
				});
		asyncSMSRegiste.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
