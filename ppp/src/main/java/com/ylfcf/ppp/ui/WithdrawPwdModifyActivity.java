package com.ylfcf.ppp.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
 * �޸���������
 * ��ԭ���ġ��������롱��Ϊ�ˡ��������롱--- WithdrawPwdModifyActivity
 * @author Mr.liu
 *
 */
public class WithdrawPwdModifyActivity extends BaseActivity implements OnClickListener{
	private static final int REQUEST_USERINFO_WHAT = 23610;
	private static final int REQUEST_USERINFO_SUC = 23611;
	private static final int REQUEST_USERINFO_NODATA = 23612;

	private static final String className = "WithdrawPwdModifyActivity";
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	private TextView userName;
	private EditText oldPwdET;
	private EditText newPwdET;
	private EditText newPwdRepeatET;
	private TextView pwdPrompt;
	private Button cmpBtn;
	private UserInfo userInfo;

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
				case REQUEST_USERINFO_WHAT:
					requestUserInfo(SettingsManager.getUserId(getApplicationContext()), SettingsManager.getUser(getApplicationContext()));
					break;
				case REQUEST_USERINFO_SUC:
					userInfo = (UserInfo)msg.obj;
					if(userInfo != null){
						if(SettingsManager.isPersonalUser(getApplicationContext())){
							userName.setText(Util.hidRealName(userInfo.getReal_name()));
						}else if(SettingsManager.isCompanyUser(getApplicationContext())){
							userName.setText(userInfo.getReal_name());
						}
					}
					break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.withdrawpwd_modify_activity);
		findViews();
		handler.sendEmptyMessage(REQUEST_USERINFO_WHAT);
	}
	
	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("�޸���������");
		
		userName = (TextView)findViewById(R.id.modify_transpwd_activity_name);
		oldPwdET = (EditText)findViewById(R.id.modify_transpwd_activity_oldpwd);
		newPwdET = (EditText)findViewById(R.id.modify_transpwd_activity_newpwd);
		newPwdRepeatET = (EditText)findViewById(R.id.modify_transpwd_activity_newpwd_repeat);
		cmpBtn = (Button)findViewById(R.id.modify_transpwd_activity_btn);
		cmpBtn.setOnClickListener(this);
		
		pwdPrompt = (TextView)findViewById(R.id.modify_transpwd_activity_pwdprompt);
		SpannableStringBuilder builder = new SpannableStringBuilder(pwdPrompt.getText().toString());
		//ForegroundColorSpan Ϊ����ǰ��ɫ��BackgroundColorSpanΪ���ֱ���ɫ  
		ForegroundColorSpan graySpan = new ForegroundColorSpan(getResources().getColor(R.color.gray1));  
		ForegroundColorSpan blueSpan = new ForegroundColorSpan(getResources().getColor(R.color.common_topbar_bg_color));
		builder.setSpan(graySpan, 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
		builder.setSpan(blueSpan, 6, 10, Spannable.SPAN_INCLUSIVE_INCLUSIVE);  
		builder.setSpan(graySpan, 10, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
		pwdPrompt.setText(builder);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.modify_transpwd_activity_btn:
			checkData();
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

	/**
	 * �����������user�����ж�
	 */
	private void checkData(){
		String oldPwdInput = oldPwdET.getText().toString();
		String newPwd = newPwdET.getText().toString();
		String repeatPwd = newPwdRepeatET.getText().toString();
		if("".equals(oldPwdInput)){
			Util.toastShort(WithdrawPwdModifyActivity.this, "������ԭʼ����");
		}else if("".equals(newPwd) || newPwd.length() < 6){
			Util.toastShort(WithdrawPwdModifyActivity.this, "������6~16λ������");
		}else if(!newPwd.equals(repeatPwd)){
			Util.toastShort(WithdrawPwdModifyActivity.this, "���������벻һ��");
		}else{
			String oldPwd = Util.md5Encryption(oldPwdInput);
			if(oldPwd.equals(userInfo.getDeal_pwd())){
				//�޸�����ӿ�
				requestModifyPwd(SettingsManager.getUserId(getApplicationContext()), newPwd, SettingsManager.getUser(getApplicationContext()));
			}else{
				Util.toastShort(WithdrawPwdModifyActivity.this, "ԭʼ�����������");
			}
		}
	}
	
	private void requestModifyPwd(String userId,final String newPwd,String phone){
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		AsyncUpdateUserInfo task = new AsyncUpdateUserInfo(WithdrawPwdModifyActivity.this, userId, "", phone, 
				"", "", "", newPwd, "", "",new OnUpdateUserInfoInter(){
					@Override
					public void back(BaseInfo baseInfo) {
						if(mLoadingDialog != null && mLoadingDialog.isShowing()){
							mLoadingDialog.dismiss();
						}
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								Util.toastShort(WithdrawPwdModifyActivity.this, "��ϲ���������޸ĳɹ���");
								finish();
							}else{
								Util.toastShort(WithdrawPwdModifyActivity.this, baseInfo.getMsg());
							}
						}
					}
		});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * �����û���Ϣ������hf_user_id�ֶ��ж��û��Ƿ��л㸶�˻�
	 * @param userId
	 * @param phone
	 */
	private void requestUserInfo(String userId,String phone){
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		AsyncUserSelectOne userTask = new AsyncUserSelectOne(WithdrawPwdModifyActivity.this, userId, phone, "","", new OnGetUserInfoByPhone() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(mLoadingDialog != null && mLoadingDialog.isShowing()){
					mLoadingDialog.dismiss();
				}
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						Message msg = handler.obtainMessage(REQUEST_USERINFO_SUC);
						msg.obj = baseInfo.getUserInfo();
						handler.sendMessage(msg);
					}
				}
			}
		});
		userTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
