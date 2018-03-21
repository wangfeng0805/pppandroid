package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;

/**
 * ���Ŵ�--- Ͷ�ʳɹ�ҳ��
 * @author Administrator
 *
 */
public class BidSuccessActivity extends BaseActivity implements OnClickListener{
	private static final String className = "BidSuccessActivity";
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private TextView catRecords;
	private TextView promptTV;
	private TextView promptTV1;
	private Button continueBtn;
	private String fromWhere;
	private BaseInfo mBaseInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bid_success_activity);
		fromWhere = getIntent().getStringExtra("from_where");
		mBaseInfo = (BaseInfo) getIntent().getSerializableExtra("base_info");
		findViews();
	}

	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("Ͷ��");
		promptTV = (TextView)findViewById(R.id.bid_success_activity_prompt_text);
		promptTV1 = (TextView) findViewById(R.id.bid_success_activity_prompt_text1);
		continueBtn = (Button)findViewById(R.id.bid_success_activity_continue_btn);
		continueBtn.setOnClickListener(this);
		catRecords = (TextView)findViewById(R.id.bid_success_activity_cat_record);
		catRecords.setOnClickListener(this);

		if(mBaseInfo != null && mBaseInfo.getmInvestResultInfo() != null&&mBaseInfo.getmInvestResultInfo().getmInvestStatus()!=null){
			if("0".equals(mBaseInfo.getmInvestResultInfo().getmInvestStatus().getStatus())){
				//�����һ��Ͷ��
				promptTV.setVisibility(View.VISIBLE);
			}else{
				promptTV.setVisibility(View.GONE);
			}
		}else{
			promptTV.setVisibility(View.GONE);
		}

		String promptText1 = "";
		if(mBaseInfo != null && mBaseInfo.getmInvestResultInfo() != null){
			if(mBaseInfo.getmInvestResultInfo().getRed_bag_value() != null && !"".equals(mBaseInfo.getmInvestResultInfo().getRed_bag_value())
					&& !"0".equals(mBaseInfo.getmInvestResultInfo().getRed_bag_value())){
				promptTV1.setVisibility(View.VISIBLE);
				promptText1 = "��ϲ�����<font color=\"#FD7323\">" + mBaseInfo.getmInvestResultInfo().getRed_bag_value() + "Ԫ" +
						"</font>�������ǰ���ҵ��˻�-������ϸ-�ҵĺ���鿴��";
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
					promptTV1.setText(Html.fromHtml(promptText1,Html.FROM_HTML_MODE_LEGACY));
				} else {
					promptTV1.setText(Html.fromHtml(promptText1));
				}
			}
		}else{
			promptTV1.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.bid_success_activity_continue_btn:
			SettingsManager.setMainProductListFlag(getApplicationContext(), true);
			finish();
			break;
		case R.id.bid_success_activity_cat_record:
			Intent intent = new Intent(BidSuccessActivity.this, UserInvestRecordActivity.class);
			intent.putExtra("from_where", fromWhere);
			startActivity(intent);
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
