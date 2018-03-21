package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;

/**
 * �û���֤��绰 --- ����֧��ƽ̨
 * @author Administrator
 *
 */
public class BankVerifyRecActivity extends BaseActivity implements OnClickListener{
	private static final String className = "BankVerifyRecActivity";
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	private Button btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bank_verify_rec_activity);
		
		findViews();
	}
	
	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("ʵ����֤");
		
		btn = (Button)findViewById(R.id.bank_verify_rec_activity_btn);
		btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bank_verify_rec_activity_btn:
			SettingsManager.setMainProductListFlag(getApplicationContext(), true);
			Intent intent = new Intent(BankVerifyRecActivity.this,MainFragmentActivity.class);
			startActivity(intent);
			finish();
			mApp.finishAllActivityExceptMain();
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
