package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncUserYUANAccount;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.UserYUANAccountInfo;
import com.ylfcf.ppp.inter.Inter.OnUserYUANAccountInter;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.Util;

/**
 * �ҵ�Ԫ���
 * @author jianbing
 *
 */
public class MyYuanMoneyActivity extends BaseActivity implements OnClickListener{
	private static final String className = "MyYuanMoneyActivity";
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	private TextView yuanMoneyUsed;//����
	private TextView yuanMoneyFrozen;//����
	private TextView yuanMoneyDaishou;//����
	private Button bidBtn;
	private TextView useRuleTV;//ʹ�ù���
	private View mainLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.myyuanmoney_activity);
		
		findViews();
		requestYuanMoney(SettingsManager.getUserId(getApplicationContext()));
	}

	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("�ҵ�Ԫ���");
		
		mainLayout = findViewById(R.id.myyuanmoney_activity_mainlayout);
		
		yuanMoneyUsed = (TextView)findViewById(R.id.myyuanmoney_actiivty_used_balance);
		yuanMoneyFrozen = (TextView)findViewById(R.id.myyuanmoney_actiivty_frozen_balance);
		yuanMoneyDaishou = (TextView)findViewById(R.id.myyuanmoney_actiivty_daishou_balance);
		bidBtn = (Button)findViewById(R.id.myyuanmoney_activity_bidBtn);
		bidBtn.setOnClickListener(this);
		useRuleTV = (TextView) findViewById(R.id.myyuanmoney_activity_userule_tv);
		useRuleTV.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //�»���
		useRuleTV.getPaint().setAntiAlias(true);//�����
		useRuleTV.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.myyuanmoney_activity_bidBtn:
			SettingsManager.setMainProductListFlag(getApplicationContext(), true);
			Intent intent = new Intent(MyYuanMoneyActivity.this,MainFragmentActivity.class);
			startActivity(intent);
			mApp.finishAllActivityExceptMain();
			break;
		case R.id.myyuanmoney_activity_userule_tv:
			//Ԫ���ʹ�ù���
			Intent intentBanner = new Intent(MyYuanMoneyActivity.this,BannerTopicActivity.class);
			BannerInfo bannerInfo = new BannerInfo();
			bannerInfo.setLink_url(URLGenerator.YUANMONEY_RULE_URL);
			intentBanner.putExtra("BannerInfo", bannerInfo);
			startActivity(intentBanner);
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

	private void initData(UserYUANAccountInfo info){
		yuanMoneyUsed.setText(Util.formatRate(info.getUse_coin()));
		yuanMoneyFrozen.setText(Util.formatRate(info.getFrozen_coin()));
		yuanMoneyDaishou.setText(Util.formatRate(info.getCollection_coin()));
		double userCoinD = 0d;
		try{
			userCoinD = Double.parseDouble(info.getUse_coin());
			if(userCoinD > 0){
				bidBtn.setText("����ʹ��Ԫ���");
			}else{
				bidBtn.setText("��ҪͶ��");
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private void requestYuanMoney(String userId){
		AsyncUserYUANAccount accountTask = new AsyncUserYUANAccount(MyYuanMoneyActivity.this, 
				userId, new OnUserYUANAccountInter() {
					@Override
					public void back(BaseInfo info) {
						int resultCode = SettingsManager.getResultCode(info);
						if(resultCode == 0){
							initData(info.getYuanAccountInfo());
						}
					}
				});
		accountTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
