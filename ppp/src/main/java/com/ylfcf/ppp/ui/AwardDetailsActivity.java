package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.util.UMengStatistics;

/**
 * ������ϸ
 * 
 * @author jianbing
 * 
 */
public class AwardDetailsActivity extends BaseActivity implements
		OnClickListener {
	private static final String className = "AwardDetailsActivity";
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;

	private LinearLayout tyjLayout;// �ҵ������
	private LinearLayout yjbLayout;// �ҵ�Ԫ���
	private LinearLayout lpLayout;// �ҵ���Ʒ
	private LinearLayout hbLayout;// �ҵĺ��
	private LinearLayout jxqLayout;//��Ϣȯ

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.award_details_activity);
		findViews();
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("������ϸ");

		tyjLayout = (LinearLayout) findViewById(R.id.award_details_activity_tyj_layout);
		tyjLayout.setOnClickListener(this);
		yjbLayout = (LinearLayout) findViewById(R.id.award_details_actiivty_yjb_layout);
		yjbLayout.setOnClickListener(this);
		lpLayout = (LinearLayout) findViewById(R.id.award_details_actiivty_lp_layout);
		lpLayout.setOnClickListener(this);
		hbLayout = (LinearLayout) findViewById(R.id.award_details_actiivty_hb_layout);
		hbLayout.setOnClickListener(this);
		jxqLayout = (LinearLayout) findViewById(R.id.award_details_actiivty_jxq_layout);
		jxqLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.award_details_activity_tyj_layout:
			Intent intentTYJ = new Intent(AwardDetailsActivity.this,
					MyTYJActivity.class);
			startActivity(intentTYJ);
			break;
		case R.id.award_details_actiivty_yjb_layout:
			Intent intentYuan = new Intent(AwardDetailsActivity.this,
					MyYuanMoneyActivity.class);
			startActivity(intentYuan);
			break;
		case R.id.award_details_actiivty_lp_layout:
			Intent intentAward = new Intent(AwardDetailsActivity.this,
					MyGiftsActivity.class);
			startActivity(intentAward);
			break;
		case R.id.award_details_actiivty_hb_layout:
			Intent intentHB = new Intent(AwardDetailsActivity.this,
					MyHongbaoActivity.class);
			startActivity(intentHB);
			break;
		case R.id.award_details_actiivty_jxq_layout:
			Intent intentJXQ = new Intent(AwardDetailsActivity.this,MyJXQActivity.class);
			startActivity(intentJXQ);
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
	}
}
