package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.util.UMengStatistics;

/**
 * ������������
 * �޸��������룻�һ��������롣
 * @author Mr.liu
 * 
 */
public class WithdrawPwdOptionActivity extends BaseActivity implements
		OnClickListener {
	private static final String className = "WithdrawPwdOptionActivity";
	private LinearLayout modifyPwdLayout;
	private LinearLayout getbackPwdLayout;

	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private UserInfo userInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.withdrawpwd_option_activity);
		userInfo = (UserInfo) getIntent().getSerializableExtra("USERINFO");
		findViews();
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("������������");

		modifyPwdLayout = (LinearLayout) findViewById(R.id.transpwd_setting_activity_changelayout);
		modifyPwdLayout.setOnClickListener(this);
		getbackPwdLayout = (LinearLayout) findViewById(R.id.transpwd_setting_activity_getbacklayout);
		getbackPwdLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.transpwd_setting_activity_changelayout:
			Intent intent1 = new Intent(WithdrawPwdOptionActivity.this,
					WithdrawPwdModifyActivity.class);
			intent1.putExtra("USERINFO", userInfo);
			startActivity(intent1);
			break;
		case R.id.transpwd_setting_activity_getbacklayout:
			Intent intent2 = new Intent(WithdrawPwdOptionActivity.this,
					WithdrawPwdGetbackActivity.class);
			intent2.putExtra("type", "�һ�");
			startActivity(intent2);
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
}
