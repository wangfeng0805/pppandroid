package com.ylfcf.ppp.ui;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncUpdateUserInfo;
import com.ylfcf.ppp.async.AsyncUserSelectOne;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.inter.Inter.OnGetUserInfoByPhone;
import com.ylfcf.ppp.inter.Inter.OnUpdateUserInfoInter;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;

/**
 * �޸ĵ�¼����
 * 
 * @author Administrator
 * 
 */
public class ModifyLoginPwdActivity extends BaseActivity implements
		OnClickListener {
	private static final String className = "ModifyLoginPwdActivity";
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private TextView userName;
	private EditText oldPwdET;
	private EditText newPwdET;
	private EditText newPwdRepeatET;
	private TextView pwdPrompt;
	private Button cmpBtn;
	private UserInfo userInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.modify_pwd_activity);
		userInfo = (UserInfo) getIntent().getSerializableExtra("USERINFO");

		findViews();
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("�޸ĵ�¼����");

		userName = (TextView) findViewById(R.id.modify_loginpwd_activity_name);
		if (userInfo != null) {
			if(SettingsManager.isPersonalUser(getApplicationContext())){
				userName.setText(Util.hidRealName(userInfo.getReal_name()));
			}else{
				userName.setText(userInfo.getReal_name());
			}
		}
		oldPwdET = (EditText) findViewById(R.id.modify_loginpwd_activity_oldpwd);
		newPwdET = (EditText) findViewById(R.id.modify_loginpwd_activity_newpwd);
		newPwdRepeatET = (EditText) findViewById(R.id.modify_loginpwd_activity_newpwd_repeat);
		cmpBtn = (Button) findViewById(R.id.modify_loginpwd_activity_btn);
		cmpBtn.setOnClickListener(this);

		pwdPrompt = (TextView) findViewById(R.id.modify_loginpwd_activity_pwdprompt);
		SpannableStringBuilder builder = new SpannableStringBuilder(pwdPrompt
				.getText().toString());
		// ForegroundColorSpan Ϊ����ǰ��ɫ��BackgroundColorSpanΪ���ֱ���ɫ
		ForegroundColorSpan graySpan = new ForegroundColorSpan(getResources()
				.getColor(R.color.gray1));
		ForegroundColorSpan blueSpan = new ForegroundColorSpan(getResources()
				.getColor(R.color.common_topbar_bg_color));
		builder.setSpan(graySpan, 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.setSpan(blueSpan, 6, 10, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		builder.setSpan(graySpan, 10, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		pwdPrompt.setText(builder);
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
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.modify_loginpwd_activity_btn:
			checkData();
		default:
			break;
		}
	}

	String newPwd = "";
	private void checkData() {
		String oldPwdLocation = SettingsManager
				.getLoginPassword(getApplicationContext());
		String oldPwd = oldPwdET.getText().toString();
		String oldPwdMd5 = Util.md5Encryption(oldPwd);
		newPwd = newPwdET.getText().toString();
		String repeatPwd = newPwdRepeatET.getText().toString();
		if (!"".equals(oldPwd)) {
			if (oldPwdMd5.equals(oldPwdLocation)) {
				if (!"".equals(newPwd)) {
					if (Util.checkPassword(newPwd)) {
						if (newPwd.equals(repeatPwd)) {
							// �޸�����ӿ�
							requestModifyPwd(
									SettingsManager
											.getUserId(getApplicationContext()),
									newPwd, SettingsManager
											.getUser(getApplicationContext()),"");
						} else {
							Util.toastShort(ModifyLoginPwdActivity.this,
									"���������벻һ��");
						}
					} else {
						Util.toastShort(ModifyLoginPwdActivity.this,
								"���볤����Ϊ6~16λ");
					}
				} else {
					Util.toastShort(ModifyLoginPwdActivity.this, "������������");
				}
			} else {
				Util.toastShort(ModifyLoginPwdActivity.this, "ԭʼ�����������");
			}
		} else {
			Util.toastShort(ModifyLoginPwdActivity.this, "������ԭʼ����");
		}
	}

	private void requestModifyPwd(String userId, final String newPwd, String phone, final String initPwd) {
		if (mLoadingDialog != null && !isFinishing()) {
			mLoadingDialog.show();
		}
		AsyncUpdateUserInfo task = new AsyncUpdateUserInfo(
				ModifyLoginPwdActivity.this, userId, newPwd, phone, "", "", "",
				"", "", initPwd,new OnUpdateUserInfoInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								SettingsManager.setLoginPassword(ModifyLoginPwdActivity.this,newPwd,true);
								if("0".equals(initPwd) || "".equals(initPwd)){
									requestUserInfo(
											SettingsManager
													.getUserId(getApplicationContext()),
											SettingsManager
													.getUser(getApplicationContext()),
											"");
								}else if("1".equals(initPwd)){
									if(mLoadingDialog != null && mLoadingDialog.isShowing()){
										mLoadingDialog.dismiss();
									}
									Util.toastShort(ModifyLoginPwdActivity.this,
											"�������޸ĳɹ�");
									finish();
								}
							} else {
								if(mLoadingDialog != null && mLoadingDialog.isShowing()){
									mLoadingDialog.dismiss();
								}
								Util.toastShort(ModifyLoginPwdActivity.this,
										baseInfo.getMsg());
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * ��ȡ�û���Ϣ
	 * 
	 * @param userId
	 * @param phone
	 * @param openId
	 */
	private void requestUserInfo(String userId, String phone, String openId) {
		AsyncUserSelectOne userTask = new AsyncUserSelectOne(
				ModifyLoginPwdActivity.this, userId, phone, "",openId,
				new OnGetUserInfoByPhone() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
							mLoadingDialog.dismiss();
						}
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								userInfo = baseInfo.getUserInfo();
//								SettingsManager.setLoginPassword(
//										getApplicationContext(),
//										userInfo.getPassword(), true);
								if(SettingsManager.isPersonalUser(getApplicationContext())){
									Util.toastShort(ModifyLoginPwdActivity.this,
											"�������޸ĳɹ�");
									finish();
								}else if(SettingsManager.isCompanyUser(getApplicationContext())){
									//������һ��ӿڸı�״̬
									requestModifyPwd(
											SettingsManager
													.getUserId(getApplicationContext()),
											newPwd, SettingsManager
													.getUser(getApplicationContext()),"1");
								}
							} else {
								Util.toastLong(ModifyLoginPwdActivity.this,
										baseInfo.getMsg());
							}
						} else {
							Util.toastLong(ModifyLoginPwdActivity.this,
									"�������粻����");
						}
					}
				});
		userTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
