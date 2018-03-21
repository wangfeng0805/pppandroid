package com.ylfcf.ppp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncXSMBCurrentUserInvest;
import com.ylfcf.ppp.async.AsyncXSMBDetail;
import com.ylfcf.ppp.async.AsyncXSMBSelectone;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.URLGenerator;

import java.text.ParseException;
import java.util.Date;

/**
 * ��ʱ�����Ŀ����ҳ��
 * 
 * @author Mr.liu
 * 
 */
public class ProductIntroXSMBActivity extends BaseActivity implements
		OnClickListener {
	private static final String className = "ProductIntroXSMBActivity";
	private static final int REQUEST_XSMB_REFRESH_WHAT = 8291;// ����ӿ�ˢ������
	private static final int REQUEST_CURRENT_USERINVEST = 8294;//����ǰ�û��Ƿ�Ͷ�ʹ������
	private static final int REQUEST_XSMB_BTNCLICK_WHAT = 8292;//�����������ɱ����ť
	private static final int REFRESH_REMAIN_TIME = 8293;//ˢ���¸�����ʣ��ʱ��
	
	private static final int REQUEST_XSMB_SELECTONE = 8296;
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private WebView webview;
	private Button bidBtn;// ������ɱ
	private String loadURL = "";
	private ProductInfo mProductInfo;
	private AlertDialog.Builder builder = null; // �ȵõ�������
	private LayoutInflater layoutInflater;
	private InvestRecordInfo recordInfo;
	
	//��������ԭ��
	private enum ReasonFlag {
		REFRESH_DATA, // ҳ��ˢ������
		BTN_CLICK,// ͨ����ť���
		RECORD_DATA //Ͷ�ʼ�¼��ת
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_XSMB_REFRESH_WHAT:
				requestXSMBDetails("����",ReasonFlag.REFRESH_DATA);
				break;
			case REQUEST_CURRENT_USERINVEST:
				requestCurrentUserInvest(SettingsManager.getUserId(getApplicationContext()),mProductInfo.getId());
				break;
			case REFRESH_REMAIN_TIME:
				long times = (Long) msg.obj;
				updateCountDown(times);
				break;
			case REQUEST_XSMB_BTNCLICK_WHAT:
				requestXSMBDetails("����",ReasonFlag.BTN_CLICK);
				break;
			case REQUEST_XSMB_SELECTONE:
				requestXSMBSelectone(recordInfo.getBorrow_id(), "����",ReasonFlag.RECORD_DATA);
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.yyyintro_activity);
		recordInfo = (InvestRecordInfo) getIntent().getSerializableExtra("InvestRecordInfo");
		builder = new AlertDialog.Builder(ProductIntroXSMBActivity.this,
				R.style.Dialog_Transparent); // �ȵõ�������
		layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		findView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		UMengStatistics.statisticsOnPageStart(className);//����ͳ��ҳ����ת
		UMengStatistics.statisticsResume(this);//����ͳ��ʱ��
		if(recordInfo == null){
			handler.sendEmptyMessage(REQUEST_XSMB_REFRESH_WHAT);
		}else{
			handler.sendEmptyMessage(REQUEST_XSMB_SELECTONE);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		UMengStatistics.statisticsOnPageEnd(className);//����ͳ��ҳ����ת
		UMengStatistics.statisticsPause(this);//����ͳ��ʱ��
	}

	private void findView() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("������");
		loadURL = URLGenerator.XSMB_XMJS_URL;

		webview = (WebView) findViewById(R.id.yyyintro_activity_wv);
		bidBtn = (Button) findViewById(R.id.yyyintro_activity_bidBtn);
		bidBtn.setOnClickListener(this);
		this.webview.getSettings().setSupportZoom(false);
		this.webview.getSettings().setJavaScriptEnabled(true); // ֧��js
		this.webview.getSettings().setDomStorageEnabled(true);
//		View bottomView = layoutInflater.inflate(R.layout.bottom_button_layout, null);
//		webview.addView(bottomView, 0);
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// ����URL ����activity����ת
				return true;
			}
		});
		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100 && mLoadingDialog.isShowing()) {
					// ��ҳ�������
					mLoadingDialog.dismiss();
				} else if(newProgress != 100 && !mLoadingDialog.isShowing() && !isFinishing()){
					// ��ҳ������...
					mLoadingDialog.show();
				}
			}
		});
		webview.loadUrl(loadURL);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.yyyintro_activity_bidBtn:
			// ��SettingsManager�ж�ȡ���룬���Ϊ����ζ��û�е�¼��
			boolean isLogin = !SettingsManager.getLoginPassword(
					ProductIntroXSMBActivity.this).isEmpty()
					&& !SettingsManager.getUser(ProductIntroXSMBActivity.this)
							.isEmpty();
			bidBtn.setEnabled(false);
			Intent intent = new Intent();
			// 1������Ƿ��Ѿ���¼
			if (isLogin) {
				// �����Ƿ񻹿��Թ���
				handler.sendEmptyMessage(REQUEST_XSMB_BTNCLICK_WHAT);
			} else {
				// δ��¼����ת����¼ҳ��
				intent.setClass(ProductIntroXSMBActivity.this,LoginActivity.class);
				startActivity(intent);
				bidBtn.setEnabled(true);
			}
			break;
		default:
			break;
		}
	}

	private void initViewData(ProductInfo productInfo,Enum flag) {
		if(flag == ReasonFlag.REFRESH_DATA || flag == ReasonFlag.RECORD_DATA){
			//ˢ������
			if("δ����".equals(productInfo.getMoney_status())){
				initBidBtnCountDown(productInfo,flag);//����ʱ
			}else{
				bidBtn.setEnabled(false);
				bidBtn.setText("Ͷ�ʽ���");
				bidBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_26dp));
			}
		}else if(flag == ReasonFlag.BTN_CLICK){
			//�����������ɱ����ť
			if("δ����".equals(productInfo.getMoney_status())){
				//δ���꣬�������û��Ƿ��Ѿ�Ͷ�ʹ������
				initBidBtnCountDown(productInfo,flag);//����ʱ
			}else{
				//������
				showPromptDialog("1");
			}
		}
	}
	
	//��ʱ���µ���ʱ
		private void updateCountDown(long times){
			int hour = 0,minute = 0,second = 0;
			String hourStr = "",minuteStr = "",secondStr = "";
			times -= 1000;
			if(times <= 0){
				bidBtn.setEnabled(true);
				bidBtn.setText("������ɱ");
				bidBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_26dp));
				return;
			}
			hour = (int) (times/1000/3600);
			minute = (int) ((times/1000%3600)/60);
			second = (int) (times/1000%3600%60);
			if(hour < 10){
				hourStr = "0" + hour;
			}else{
				hourStr = hour + "";
			}
			if(minute < 10){
				minuteStr = "0" + minute;
			}else{
				minuteStr = minute + "";
			}
			if(second < 10){
				secondStr = "0" + second;
			}else{
				secondStr = second + "";
			}
			bidBtn.setText("������һ������ɱ����ʼ��ʣ��"+hourStr+"ʱ"+minuteStr+"��"+secondStr+"��");
			bidBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_21dp));
			Message msg = handler.obtainMessage(REFRESH_REMAIN_TIME);
			msg.obj = times;
			handler.sendMessageDelayed(msg, 1000L);
		}
	
	/**
	 * Ͷ�ʰ�ť����ʱ
	 * @param productInfo
	 * @throws ParseException 
	 */
	long remainTimeL = 0l;
	private void initBidBtnCountDown(ProductInfo productInfo,Enum flag){
		bidBtn.setEnabled(false);
		String nowTimeStr = productInfo.getNow_time();
		String willStartTimeStr = productInfo.getWill_start_time();//��ʼʱ��
		int hour = 0,minute = 0,second = 0;
		String hourStr = "",minuteStr = "",secondStr = "";
		try {
			Date nowDate = sdf.parse(nowTimeStr);
			Date willStartDate = sdf.parse(willStartTimeStr);
			remainTimeL = willStartDate.getTime() - nowDate.getTime();
			if(remainTimeL >= 0){
				hour = (int) (remainTimeL/1000/3600);
				minute = (int) ((remainTimeL/1000%3600)/60);
				second = (int) (remainTimeL/1000%3600%60);
				if(hour < 10){
					hourStr = "0" + hour;
				}else{
					hourStr = hour + "";
				}
				if(minute < 10){
					minuteStr = "0" + minute;
				}else{
					minuteStr = minute + "";
				}
				if(second < 10){
					secondStr = "0" + second;
				}else{
					secondStr = second + "";
				}
				bidBtn.setText("������һ������ɱ����ʼ��ʣ��"+hourStr+"ʱ"+minuteStr+"��"+secondStr+"��");
				bidBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_21dp));
				Message msg = handler.obtainMessage(REFRESH_REMAIN_TIME);
				msg.obj = remainTimeL;
				handler.sendMessage(msg);
			}else{
//				//����Ͷ���С���
				bidBtn.setText("������ɱ");
				bidBtn.setEnabled(true);
				bidBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_26dp));
				if(flag == ReasonFlag.BTN_CLICK){
					handler.sendEmptyMessage(REQUEST_CURRENT_USERINVEST);
				}else if(flag == ReasonFlag.REFRESH_DATA){
					
				}
			}
		} catch (ParseException e) {
			bidBtn.setText("Ͷ�ʽ���");
			bidBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_26dp));
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param flag 1��ʾ������ 0��ʾ��ɱ��������
	 */
	private void showPromptDialog(final String flag){
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.borrow_detail_prompt_layout, null);
		Button leftBtn = (Button) contentView
				.findViewById(R.id.borrow_detail_prompt_layout_leftbtn);
		Button rightBtn = (Button) contentView
				.findViewById(R.id.borrow_detail_prompt_layout_rightbtn);
		TextView topTV = (TextView) contentView
				.findViewById(R.id.borrow_detail_prompt_top_text);
		TextView bottomTV = (TextView) contentView
				.findViewById(R.id.borrow_detail_prompt_bottom_text);
		if("0".equals(flag)){
			bottomTV.setVisibility(View.VISIBLE);
			rightBtn.setVisibility(View.VISIBLE);
			topTV.setText("���ı�����ɱ������ʹ��");
			bottomTV.setText("ÿ����ɱ��ÿ���û�ֻ��һ����ɱ����Ӵ~");
			leftBtn.setText("�鿴Ͷ�ʼ�¼");
			leftBtn.setTextColor(getResources().getColor(R.color.common_topbar_bg_color));
			leftBtn.setBackground(getResources().getDrawable(R.drawable.style_rect_fillet_blue));
			rightBtn.setText("��ע������Ŀ");
		}else if("1".equals(flag)){
			bottomTV.setVisibility(View.GONE);
			rightBtn.setVisibility(View.GONE);
			topTV.setText("����⣡\n����һ����������~");
			leftBtn.setText("ȷ��");
			leftBtn.setTextColor(getResources().getColor(R.color.white));
			leftBtn.setBackground(getResources().getDrawable(R.drawable.style_rect_fillet_filling_blue));
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this,
				R.style.Dialog_Transparent); // �ȵõ�������
		builder.setView(contentView);
		builder.setCancelable(true);
		final AlertDialog dialog = builder.create();
		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if("0".equals(flag)){
					Intent intent = new Intent(ProductIntroXSMBActivity.this,UserInvestRecordActivity.class);
					intent.putExtra("from_where", "���");
					startActivity(intent);
					finish();
				}
				dialog.dismiss();
			}
		});
		rightBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SettingsManager.setMainProductListFlag(getApplicationContext(), true);	
				dialog.dismiss();
				mApp.finishAllActivityExceptMain();
			}
		});
		// ��������������ˣ���������ʾ����
		dialog.show();
		// ����dialog�Ŀ��
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = display.getWidth() * 6 / 7;
		dialog.getWindow().setAttributes(lp);
	}

	/**
	 * �������
	 */
	private void requestXSMBDetails(String borrowStatus,final Enum flag) {
		AsyncXSMBDetail xsmbTask = new AsyncXSMBDetail(
				ProductIntroXSMBActivity.this, borrowStatus,new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								bidBtn.setVisibility(View.VISIBLE);
								mProductInfo = baseInfo
										.getmProductInfo();
								initViewData(mProductInfo,flag);
							} else {
								bidBtn.setVisibility(View.GONE);
							}
						}
					}
				});
		xsmbTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * �жϵ�ǰ�û��Ƿ�Ͷ�ʹ������
	 * @param userId
	 * @param borrowId
	 */
	private void requestCurrentUserInvest(String userId,String borrowId){
		AsyncXSMBCurrentUserInvest task = new AsyncXSMBCurrentUserInvest(ProductIntroXSMBActivity.this, userId, borrowId, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//û��Ͷ�ʹ������
								Intent intent = new Intent(ProductIntroXSMBActivity.this,BidXSMBActivity.class);
								intent.putExtra("PRODUCT_INFO", mProductInfo);
								startActivity(intent);
							}else{
								//Ͷ�ʹ������
								showPromptDialog("0");
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * ����id��ȡ�������
	 * @param borrowId
	 * @param borrowStatus
	 */
	private void requestXSMBSelectone(String borrowId,String borrowStatus,final Enum reasonFlag){
		AsyncXSMBSelectone task = new AsyncXSMBSelectone(ProductIntroXSMBActivity.this, borrowId, borrowStatus, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								bidBtn.setVisibility(View.VISIBLE);
								mProductInfo = baseInfo
										.getmProductInfo();
								initViewData(mProductInfo,reasonFlag);
							} else {
								bidBtn.setVisibility(View.GONE);
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
