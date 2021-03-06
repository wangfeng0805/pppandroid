package com.ylfcf.ppp.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncChangeAddress;
import com.ylfcf.ppp.async.AsyncUserSelectOne;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnGetUserInfoByPhone;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;

/**
 * 修改收货地址
 * @author Administrator
 *
 */
public class ChangeAddressActivity extends BaseActivity implements OnClickListener{
	private static final String className = "ChangeAddressActivity";
	private LinearLayout topLeftBtn;
    private TextView topTitleTV;
	private EditText zipcodeET;
	private EditText addressET;
	private Button commitBtn;
	AsyncChangeAddress changeAddressTask = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.change_address_activity);
		findViews();
		requestUserInfo(SettingsManager.getUserId(getApplicationContext()), "");
	}

	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("修改收货地址");
		
		zipcodeET = (EditText)findViewById(R.id.change_address_activity_zipcode);
		addressET = (EditText)findViewById(R.id.change_address_activity_address);
		commitBtn = (Button)findViewById(R.id.change_address_activity_commit_btn);
		commitBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.change_address_activity_commit_btn:
			checkInputData();
			break;
		case R.id.common_topbar_left_layout:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		UMengStatistics.statisticsOnPageStart(className);//友盟统计页面跳转
		UMengStatistics.statisticsResume(this);//友盟统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		UMengStatistics.statisticsOnPageEnd(className);//友盟统计页面跳转
		UMengStatistics.statisticsPause(this);//友盟统计时长
	}

	private void initData(UserInfo info){
		if(info == null){
			return;
		}
		
		zipcodeET.setText(info.getPost_code());
		addressET.setText(info.getAddress());
	}
	
	private void checkInputData(){
		String address = addressET.getText().toString();
		String postCode = zipcodeET.getText().toString();
		if(postCode == null || "".equals(postCode)){
			Util.toastShort(ChangeAddressActivity.this, "请输入邮编");
		}else if(address == null || "".equals(address)){
			Util.toastShort(ChangeAddressActivity.this, "请输入收货地址");
		}else{
			requestChangeAddres(SettingsManager.getUserId(getApplicationContext()), address, postCode);
		}
	}
	
	private void requestChangeAddres(String userId,String address,String postCode){
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		changeAddressTask = new AsyncChangeAddress(ChangeAddressActivity.this, userId, address,
				postCode, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(mLoadingDialog != null && mLoadingDialog.isShowing()){
							mLoadingDialog.dismiss();
						}
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								Util.toastLong(ChangeAddressActivity.this, "修改成功");
								finish();
							}else{
								Util.toastLong(ChangeAddressActivity.this, baseInfo.getMsg());
							}
						}
					}
				});
		changeAddressTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 请求用户信息，根据hf_user_id字段判断用户是否有汇付账户
	 * @param userId
	 * @param phone
	 */
	private void requestUserInfo(String userId,String phone){
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		AsyncUserSelectOne userTask = new AsyncUserSelectOne(ChangeAddressActivity.this, userId, phone, "","", new OnGetUserInfoByPhone() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(mLoadingDialog != null && mLoadingDialog.isShowing()){
					mLoadingDialog.dismiss();
				}
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						UserInfo info = baseInfo.getUserInfo();
						initData(info);
					}
				}
			}
		});
		userTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
