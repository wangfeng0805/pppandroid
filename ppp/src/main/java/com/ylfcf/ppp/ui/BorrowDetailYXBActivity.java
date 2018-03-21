package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncYXBProduct;
import com.ylfcf.ppp.async.AsyncYXBProductLog;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.YXBProductInfo;
import com.ylfcf.ppp.entity.YXBProductLogInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Ԫ�ű�Ͷ������ҳ��
 * 
 * @author Administrator
 */
public class BorrowDetailYXBActivity extends BaseActivity implements
		OnClickListener {
	private static final String className = "BorrowDetailYXBActivity";
	private static final int REQUEST_PRODUCT_WHAT = 2301;// ��Ʒ��Ϣ
	private static final int REQUEST_PRODUCT_SUCCESS = 2302;
	private static final int REQUEST_PRODUCTLOG_WHAT = 2303;// ��Ʒÿ��ͳ��
	private static final int REQUEST_PRODUCTLOG_SUCCESS = 2304;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private ImageView rightImage;

	private LinearLayout xmjsLayout;// ��Ŀ����
	private LinearLayout cpysLayout;// ��ƷҪ��
	private LinearLayout cjwtLayout;// ��������
	private Button buyBtn;// �����Ϲ�

	private TextView raiseMoneyText;// ����ʣ��ļ�����
	private TextView yearRateText;// Ԥ���껯����
	private TextView applyWithdrawMoneyText;// ����ʣ�����ض��
	private TextView maxInvestMoneyText;// ����Ϲ����
	private YXBProductInfo yxbProductInfo;
	private YXBProductLogInfo yxbProductLogInfo;
	private AlertDialog.Builder builder = null; // �ȵõ�������
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_PRODUCT_WHAT:
				requestYXBProduct("", "�ѷ���");
				break;
			case REQUEST_PRODUCT_SUCCESS:
				handler.sendEmptyMessage(REQUEST_PRODUCTLOG_WHAT);
				break;
			case REQUEST_PRODUCTLOG_WHAT:
				String dateTime = sdf.format(new Date());
				requestYXBProductLog("", dateTime);
				break;
			case REQUEST_PRODUCTLOG_SUCCESS:
				initYXBData();
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
		setContentView(R.layout.borrow_details_yxb_activity);
		builder = new AlertDialog.Builder(BorrowDetailYXBActivity.this); // �ȵõ�������
		findViews();
		handler.sendEmptyMessage(REQUEST_PRODUCT_WHAT);
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("Ԫ�ű�");
		rightImage = (ImageView) findViewById(R.id.common_topbar_right);
		rightImage.setVisibility(View.VISIBLE);
		rightImage.setOnClickListener(this);

		xmjsLayout = (LinearLayout) findViewById(R.id.borrow_details_yxb_xmjs_layout);
		xmjsLayout.setOnClickListener(this);
		cpysLayout = (LinearLayout) findViewById(R.id.borrow_details_yxb_cpys_layout);
		cpysLayout.setOnClickListener(this);
		cjwtLayout = (LinearLayout) findViewById(R.id.borrow_details_yxb_cjwt_layout);
		cjwtLayout.setOnClickListener(this);
		buyBtn = (Button) findViewById(R.id.borrow_details_yxb_activity_btn);
		buyBtn.setOnClickListener(this);

		raiseMoneyText = (TextView) findViewById(R.id.borrow_details_yxb_activity_raisemoney);
		yearRateText = (TextView) findViewById(R.id.borrow_details_yxb_activity_year_rate);
		applyWithdrawMoneyText = (TextView) findViewById(R.id.borrow_details_yxb_activity_apply_withdraw_money);
		maxInvestMoneyText = (TextView) findViewById(R.id.borrow_details_yxb_activity_max_invest_money);
	}

	private void initYXBData() {
		if (yxbProductInfo != null && yxbProductLogInfo != null) {
			DecimalFormat df = new DecimalFormat("#.00");
			String needRaiseMoney = yxbProductLogInfo.getNeed_raise_money();
			String raiseMoney = yxbProductLogInfo.getRaise_money();
			double raiseBalance = 0d;
			try {
				double needRaiseMoneyD = Double.parseDouble(needRaiseMoney);
				double raiseMoneyD = Double.parseDouble(raiseMoney);
				raiseBalance = needRaiseMoneyD - raiseMoneyD;
			} catch (Exception e) {
			}
			raiseMoneyText.setText(Util.commaSpliteData(String
					.valueOf(raiseBalance)));
			maxInvestMoneyText.setText(yxbProductInfo.getMax_invest_money()
					+ "Ԫ");

			String yearInterest = yxbProductInfo.getYear_get_interest();// �껯����
			try {
				double yearInterestD = Double.parseDouble(yearInterest);
				yearRateText.setText(df.format(yearInterestD) + "%");
			} catch (Exception e) {
				yearRateText.setText("6.00%");
			}
			try {
				double needApplyWithD = Double.parseDouble(yxbProductLogInfo
						.getNeed_apply_withdraw_money());
				double applyWithD = Double.parseDouble(yxbProductLogInfo
						.getApply_withdraw_money());
				applyWithdrawMoneyText.setText(df.format(needApplyWithD
						- applyWithD)
						+ "Ԫ");
			} catch (Exception e) {
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		UMengStatistics.statisticsOnPageStart(className);//����ͳ��ҳ����ת
		UMengStatistics.statisticsResume(this);//����ͳ��ʱ��
		if (handler != null) {
			handler.sendEmptyMessage(REQUEST_PRODUCT_WHAT);
		}
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
		handler.removeCallbacksAndMessages(null);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.common_topbar_right:
			// Ԫ�ű�����
			Intent intentYxbIntro = new Intent(BorrowDetailYXBActivity.this,
					YXBIntroActivity.class);
			startActivity(intentYxbIntro);
			break;
		case R.id.borrow_details_yxb_xmjs_layout:
			Intent intentYXBPro = new Intent(BorrowDetailYXBActivity.this,
					YXBProjectIntroActivity.class);
			intentYXBPro.putExtra("yxb_project_flag", "yxb_xmjs");
			startActivity(intentYXBPro);
			break;
		case R.id.borrow_details_yxb_cpys_layout:
			Intent intentYXBEle = new Intent(BorrowDetailYXBActivity.this,
					YXBProjectIntroActivity.class);
			intentYXBEle.putExtra("yxb_project_flag", "yxb_cpys");
			startActivity(intentYXBEle);
			break;
		case R.id.borrow_details_yxb_cjwt_layout:
			Intent intentYXBQues = new Intent(BorrowDetailYXBActivity.this,
					YXBProjectIntroActivity.class);
			intentYXBQues.putExtra("yxb_project_flag", "yxb_cjwt");
			startActivity(intentYXBQues);
			break;
		case R.id.borrow_details_yxb_activity_btn:
			// ��SettingsManager�ж�ȡ���룬���Ϊ����ζ��û�е�¼��
			if (Util.isYXBENabled()) {
				boolean isLogin = !SettingsManager.getLoginPassword(
						BorrowDetailYXBActivity.this).isEmpty()
						&& !SettingsManager.getUser(
								BorrowDetailYXBActivity.this).isEmpty();
				Intent intentYXBBid = new Intent();
				// 1������Ƿ��Ѿ���¼
				if (isLogin) {
					// �Ѿ���¼����ת������ҳ��
					intentYXBBid.setClass(BorrowDetailYXBActivity.this,
							BidYXBActivity.class);
				} else {
					// δ��¼����ת����¼ҳ��
					intentYXBBid.setClass(BorrowDetailYXBActivity.this,
							LoginActivity.class);
				}
				startActivity(intentYXBBid);
			} else {
				showYXBRedeemErrorDialog("ÿ�յ�23:00������1:00��ϵͳ������ʱ�䣬��ʱ�ν������⿪��Ԫ�ű��ġ��Ϲ����͡���ء����ס�");
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Ԫ�ű�ά��ʱ��
	 * 
	 * @param msg
	 */
	private void showYXBRedeemErrorDialog(String msg) {
		View contentView = LayoutInflater.from(BorrowDetailYXBActivity.this)
				.inflate(R.layout.yxb_redeem_error_dialog, null);
		final Button sureBtn = (Button) contentView
				.findViewById(R.id.yxb_redeem_error_dialog_btn);
		final TextView errorText = (TextView) contentView
				.findViewById(R.id.yxb_redeem_error_dialog_reason);
		final TextView titleText = (TextView) contentView
				.findViewById(R.id.yxb_redeem_error_dialog_title);
		titleText.setText("�Ϲ�ʧ��");
		errorText.setText(msg);
		builder.setView(contentView);
		builder.setCancelable(false);
		final AlertDialog dialog = builder.create();
		sureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
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
	 * Ԫ�ű���Ʒ
	 * 
	 * @param id
	 * @param status
	 */
	private void requestYXBProduct(String id, String status) {
		if (mLoadingDialog != null && !isFinishing()) {
			mLoadingDialog.show();
		}
		AsyncYXBProduct productTask = new AsyncYXBProduct(
				BorrowDetailYXBActivity.this, id, status, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								yxbProductInfo = baseInfo.getYxbProductInfo();
								handler.sendEmptyMessage(REQUEST_PRODUCT_SUCCESS);
							} else {
								mLoadingDialog.dismiss();
							}
						} else {
							mLoadingDialog.dismiss();
						}
					}
				});
		productTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * Ԫ�ű�ÿ��ͳ��
	 * 
	 * @param id
	 * @param dateTime
	 */
	private void requestYXBProductLog(String id, String dateTime) {
		AsyncYXBProductLog productLogTask = new AsyncYXBProductLog(
				BorrowDetailYXBActivity.this, id, dateTime,
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
								yxbProductLogInfo = baseInfo
										.getYxbProductLogInfo();
								handler.sendEmptyMessage(REQUEST_PRODUCTLOG_SUCCESS);
							}
						}
					}
				});
		productLogTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
