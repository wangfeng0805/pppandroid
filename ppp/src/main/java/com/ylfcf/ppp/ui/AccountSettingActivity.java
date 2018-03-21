package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncUserSelectOne;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.inter.Inter.OnGetUserInfoByPhone;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;

/**
 * �˻�����
 * 
 * @author Mr.liu
 * 
 */
public class AccountSettingActivity extends BaseActivity implements
		OnClickListener {
	private static final String className = "AccountSettingActivity";
	private final int REQUEST_GET_USERINFO_SUCCESS = 2601;
	private final int REQUEST_GET_USERINFO_WHAT = 2602;

	private LinearLayout topLeftBtn;
	private TextView topTitleTV;

	private TextView usernameTV;
	private TextView realnameTV;
	private TextView idnumberTV;
	private TextView phoneTV;

	private LinearLayout loginPwdLayout;
	private LinearLayout transPwdLayout;//��������
	private View line2;
	private LinearLayout gesturePwdLayout;
	private LinearLayout addressLayout;
	private TextView addressTV;
	private UserInfo userInfo = null;
	private boolean isBinding = false;//�Ƿ��Ѿ��������п�

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_GET_USERINFO_WHAT:
				requestUserInfo(SettingsManager.getUserId(getApplicationContext()),
						SettingsManager.getUser(getApplicationContext()));
				break;
			case REQUEST_GET_USERINFO_SUCCESS:
				userInfo = (UserInfo) msg.obj;
				if (userInfo != null) {
					realnameTV.setText(Util.hidRealName(userInfo.getReal_name()));
					idnumberTV.setText(Util.hidIdNumber(userInfo.getId_number()));
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.account_setting_activity);
		isBinding = getIntent().getBooleanExtra("is_binding", false);
		findViews();
		handler.sendEmptyMessage(REQUEST_GET_USERINFO_WHAT);
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("�˻�����");

		usernameTV = (TextView) findViewById(R.id.account_setting_username_text);
		usernameTV.setText(SettingsManager.getUserName(getApplicationContext()));
		realnameTV = (TextView) findViewById(R.id.account_setting_realname_text);
		idnumberTV = (TextView) findViewById(R.id.account_setting_idnumber_text);
		phoneTV = (TextView) findViewById(R.id.account_setting_phone_text);
		phoneTV.setText(SettingsManager.getUser(getApplicationContext()));

		loginPwdLayout = (LinearLayout) findViewById(R.id.account_setting_login_pwd_layout);
		loginPwdLayout.setOnClickListener(this);
		transPwdLayout = (LinearLayout) findViewById(R.id.account_setting_trans_pwd_layout);
		transPwdLayout.setOnClickListener(this);
		line2 = findViewById(R.id.account_setting_activity_line2);
		if(isBinding){
			transPwdLayout.setVisibility(View.VISIBLE);
			line2.setVisibility(View.VISIBLE);
		}
		gesturePwdLayout = (LinearLayout) findViewById(R.id.account_setting_gesture_pwd_layout);
		gesturePwdLayout.setOnClickListener(this);
		addressLayout = (LinearLayout) findViewById(R.id.account_setting_address_layout);
		addressLayout.setOnClickListener(this);
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
		UMengStatistics.statisticsPause(this);//����ͳ��
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
			finish();
			break;
		case R.id.account_setting_login_pwd_layout:
			Intent intent = new Intent(AccountSettingActivity.this,
					ModifyLoginPwdActivity.class);
			intent.putExtra("USERINFO", userInfo);
			startActivity(intent);
			break;
		case R.id.account_setting_trans_pwd_layout:
			Intent intentTrans = new Intent(AccountSettingActivity.this,
					WithdrawPwdOptionActivity.class);
			intentTrans.putExtra("USERINFO", userInfo);
			startActivity(intentTrans);

			break;
		case R.id.account_setting_gesture_pwd_layout:
			Intent intentSetting = new Intent(AccountSettingActivity.this,
					GestureSettingActivity.class);
			startActivity(intentSetting);
			break;
		case R.id.account_setting_address_layout:
			Intent intentAddress = new Intent(AccountSettingActivity.this,
					ChangeAddressActivity.class);
			startActivity(intentAddress);
			break;
		default:
			break;
		}
	}

	/**
	 * �����û���Ϣ������hf_user_id�ֶ��ж��û��Ƿ��л㸶�˻�
	 * 
	 * @param userId
	 * @param phone
	 */
	private void requestUserInfo(String userId, String phone) {
		AsyncUserSelectOne userTask = new AsyncUserSelectOne(
				AccountSettingActivity.this, userId, phone, "","",
				new OnGetUserInfoByPhone() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								UserInfo userInfo = baseInfo.getUserInfo();
								Message msg = handler
										.obtainMessage(REQUEST_GET_USERINFO_SUCCESS);
								msg.obj = userInfo;
								handler.sendMessage(msg);
							}else{
								Util.toastLong(AccountSettingActivity.this,baseInfo.getMsg());
							}
						}
					}
				});
		userTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
