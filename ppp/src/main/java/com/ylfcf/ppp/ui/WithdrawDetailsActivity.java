package com.ylfcf.ppp.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncWithdrawCancel;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.WithdrawOrderInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;

/**
 * ������������
 * 
 * @author Administrator
 * 
 */
public class WithdrawDetailsActivity extends BaseActivity implements
		OnClickListener {
	private static final String className = "WithdrawDetailsActivity";
	private WithdrawOrderInfo mWithdrawOrderInfo;

	private LinearLayout topLeftBtn;
	private TextView topTitleTV;

	private TextView orderIdText;
	private TextView moneyText;
	private TextView timeText;
	private TextView statusText;
	private Button cancelBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.withdraw_details_activity);
		mWithdrawOrderInfo = (WithdrawOrderInfo) getIntent()
				.getSerializableExtra("WithdrawOrderInfo");
		findViews();
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("������������");

		orderIdText = (TextView) findViewById(R.id.withdraw_detail_activity_orderid);
		moneyText = (TextView) findViewById(R.id.withdraw_detail_activity_money);
		timeText = (TextView) findViewById(R.id.withdraw_detail_activity_time);
		statusText = (TextView) findViewById(R.id.withdraw_detail_activity_status);
		cancelBtn = (Button) findViewById(R.id.withdraw_details_activity_cancel_btn);
		cancelBtn.setOnClickListener(this);
		if (mWithdrawOrderInfo != null && "������".equals(mWithdrawOrderInfo.getStatus())) {
			cancelBtn.setVisibility(View.VISIBLE);
		} else {
			cancelBtn.setVisibility(View.GONE);
		}
		initData();
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

	private void initData() {
		if (mWithdrawOrderInfo != null) {
			orderIdText.setText(mWithdrawOrderInfo.getCash_order());
			moneyText.setText(mWithdrawOrderInfo.getCash_account() + "Ԫ");
			timeText.setText(mWithdrawOrderInfo.getAdd_time());
			statusText.setText(mWithdrawOrderInfo.getStatus());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.withdraw_details_activity_cancel_btn:
			if(mWithdrawOrderInfo != null){
				requestWithdrawCancel(mWithdrawOrderInfo.getId(), "�û�ȡ��",
						SettingsManager.getUserId(getApplicationContext()), "�û�");
			}
			break;
		default:
			break;
		}
	}

	private void requestWithdrawCancel(String id, String status, String userId,
			String auditType) {
		if (mLoadingDialog != null && !isFinishing()) {
			mLoadingDialog.show();
		}
		AsyncWithdrawCancel cancelTask = new AsyncWithdrawCancel(
				WithdrawDetailsActivity.this, id, status, userId, auditType,
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
								// ȡ�����ֳɹ�
								Util.toastShort(WithdrawDetailsActivity.this,
										"�������볷���ɹ�");
								finish();
							}
						}
					}
				});
		cancelTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
