package com.ylfcf.ppp.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.util.UMengStatistics;

/**
 * ֧��ͨ�����˵��
 * @author Mr.liu
 *
 */
public class RechargeChannelActivity extends BaseActivity implements OnClickListener{
	private static final String className = "RechargeChannelActivity";
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	private Button okBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.recharge_channel_activity);
		
		findViews();
	}

	private void findViews(){
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("�����п�");
		
		okBtn = (Button) findViewById(R.id.recharge_channel_activity_btn);
		okBtn.setOnClickListener(this);
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
		case R.id.recharge_channel_activity_btn:
			finish();
			break;

		default:
			break;
		}
	}
}
