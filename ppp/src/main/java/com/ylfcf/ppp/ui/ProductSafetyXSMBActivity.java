package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
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
import com.ylfcf.ppp.entity.ProjectInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

/**
 * ��ʱ��� ��ȫ����
 * 
 * @author Mr.liu
 * 
 */
public class ProductSafetyXSMBActivity extends BaseActivity implements
		OnClickListener {
	private static final String className = "ProductSafetyXSMBActivity";
	private static final int REFRESH_VIEW = 5710;
	private static final int REQUEST_XSMB_REFRESH_WHAT = 8291;// ����ӿ�ˢ������
	private static final int REQUEST_XSMB_BTNCLICK_WHAT = 8292;// �����������ɱ����ť
	private static final int REFRESH_REMAIN_TIME = 8293;// ˢ���¸�����ʣ��ʱ��
	private static final int REQUEST_CURRENT_USERINVEST = 8294;//����ǰ�û��Ƿ�Ͷ�ʹ������
	
	private static final int REQUEST_XSMB_SELECTONE = 8296;

	private LinearLayout topLeftBtn;
	private TextView topTitleTV;

	// ��Ʒ���׽ṹ����֮ǰ�Ĳ���
	private LinearLayout beforeLayout;
	private TextView zjaqTV;
	private TextView zcaqTV;

	// ��Ʒ���׽ṹ����֮��Ĳ���
	private LinearLayout afterLayout;
	private TextView dbcsTV;
	private TextView hklyTV;

	private Button investBtn;// ����Ͷ��
	private ProjectInfo projectInfo;
	private ProductInfo productInfo;
	private InvestRecordInfo recordInfo;
	private AlertDialog.Builder builder = null; // �ȵõ�������

	// ��������ԭ��
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
			case REFRESH_VIEW:
				Bundle bundle = (Bundle) msg.obj;
				refreshView(bundle);
				break;
			case REQUEST_XSMB_BTNCLICK_WHAT:
				requestXSMBDetails("����", ReasonFlag.BTN_CLICK);
				break;
			case REQUEST_XSMB_SELECTONE:
				requestXSMBSelectone(recordInfo.getBorrow_id(), "����",ReasonFlag.RECORD_DATA);
				break;
			case REQUEST_CURRENT_USERINVEST:
				requestCurrentUserInvest(SettingsManager.getUserId(getApplicationContext()),productInfo.getId());
				break;
			case REFRESH_REMAIN_TIME:
				long times = (Long) msg.obj;
				updateCountDown(times);
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
		setContentView(R.layout.product_safety_activity);
		builder = new AlertDialog.Builder(ProductSafetyXSMBActivity.this,
				R.style.Dialog_Transparent); // �ȵõ�������
		Bundle bundle = getIntent().getBundleExtra("BUNDLE");
		if (bundle != null) {
			projectInfo = (ProjectInfo) bundle.getSerializable("PROJECT_INFO");
			productInfo = (ProductInfo) bundle.getSerializable("PRODUCT_INFO");
			recordInfo = (InvestRecordInfo) bundle.getSerializable("InvestRecordInfo");
		}
		findViews();
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

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("��ȫ����");

		beforeLayout = (LinearLayout) findViewById(R.id.product_safety_activity_before_layout);
		zjaqTV = (TextView) findViewById(R.id.productsafety_activity_zjaq);
		zcaqTV = (TextView) findViewById(R.id.productsafety_activity_zcaq);

		afterLayout = (LinearLayout) findViewById(R.id.product_safety_activity_after_layout);
		dbcsTV = (TextView) findViewById(R.id.productsafety_activity_dbcs);
		hklyTV = (TextView) findViewById(R.id.productsafety_activity_after_hkly);

		investBtn = (Button) findViewById(R.id.product_safety_activity_bidBtn);
		investBtn.setEnabled(false);
		investBtn.setOnClickListener(this);

		if (projectInfo == null) {
			beforeLayout.setVisibility(View.GONE);
			afterLayout.setVisibility(View.VISIBLE);
			return;
		}
		if ("����".equals(projectInfo.getStatus())) {
			beforeLayout.setVisibility(View.GONE);
			afterLayout.setVisibility(View.VISIBLE);
		} else {
			beforeLayout.setVisibility(View.VISIBLE);
			afterLayout.setVisibility(View.GONE);
		}

		initData();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}

	private void initData() {
		if (projectInfo != null) {
			// ��Ʒ���׽ṹ����֮ǰ�ġ��ʽ�ȫ��
			new ImageLoadThread(projectInfo.getCapital_safe(), 0).start();
			// ��Ʒ���׽ṹ����֮ǰ�ġ�������Դ��
			new ImageLoadThread(projectInfo.getRepay_from(), 1).start();

			// ��Ʒ���׽ṹ����֮ǰ�ġ�������ʩ��
			new ImageLoadThread(projectInfo.getMeasures(), 2).start();
			// ��Ʒ���׽ṹ����֮��ġ�������Դ��
			new ImageLoadThread(projectInfo.getRepay_from(), 3).start();

		}
	}

	/**
	 * ˢ�¿ؼ�
	 * 
	 * @param bundle
	 */
	private void refreshView(Bundle bundle) {
		if (bundle == null)
			return;
		CharSequence htmlText = bundle.getCharSequence("HTML_TEXT");
		int position = bundle.getInt("POSITION");
		if (position == 0) {
			// ��Ʒ�ṹ����֮ǰ�ġ��ʽ�ȫ��
			if (htmlText.length() > 0) {
				zjaqTV.setText(htmlText);
			}
		} else if (position == 1) {
			// ��Ʒ�ṹ����֮ǰ�ġ�������Դ��
			zcaqTV.setText(htmlText);
		} else if (position == 2) {
			// ��Ʒ�ṹ����֮��ġ�������ʩ��
			dbcsTV.setText(htmlText);
		} else if (position == 3) {
			// ��Ʒ�ṹ����֮��ġ�������Դ��
			hklyTV.setText(htmlText);
		}
	}

	class ImageLoadThread extends Thread {
		private String htmlText1 = "";
		private int position1 = 0;

		public ImageLoadThread(String htmlText, int position) {
			this.htmlText1 = htmlText;
			this.position1 = position;
		}

		@Override
		public void run() {
			/**
			 * Ҫʵ��ͼƬ����ʾ��Ҫʹ��Html.fromHtml��һ���ع�������public static Spanned fromHtml
			 * (String source, Html.ImageGetterimageGetter, Html.TagHandler
			 * tagHandler)����Html.ImageGetter��һ���ӿڣ�����Ҫʵ�ִ˽ӿڣ�������getDrawable
			 * (String source)�����з���ͼƬ��Drawable����ſ��ԡ�
			 */
			ImageGetter imageGetter = new ImageGetter() {
				@Override
				public Drawable getDrawable(String source) {
					// TODO Auto-generated method stub
					URL url;
					Drawable drawable = null;
					try {
						url = new URL(source);
						int[] screen = SettingsManager
								.getScreenDispaly(ProductSafetyXSMBActivity.this);
						drawable = Drawable.createFromStream(url.openStream(),
								null);
						if (drawable != null) {
							int imageIntrinsicWidth = drawable
									.getIntrinsicWidth();
							float imageIntrinsicHeight = (float) drawable
									.getIntrinsicHeight();
							int curImageHeight = (int) (screen[0] * (imageIntrinsicHeight / imageIntrinsicWidth));
							drawable.setBounds(0, 0, screen[0], curImageHeight);// �ĸ���������Ϊ���Ͻǡ����½�����ȷ����һ�����Σ�ͼƬ����������η�Χ�ڻ�����
						}
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					return drawable;
				}
			};
			CharSequence htmlText = Html.fromHtml(htmlText1, imageGetter, null);
			Message msg = handler.obtainMessage(REFRESH_VIEW);
			Bundle bundle = new Bundle();
			bundle.putCharSequence("HTML_TEXT", htmlText);
			bundle.putInt("POSITION", position1);
			msg.obj = bundle;
			handler.sendMessage(msg);

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.product_safety_activity_bidBtn:
			// ��SettingsManager�ж�ȡ���룬���Ϊ����ζ��û�е�¼��
			boolean isLogin = !SettingsManager.getLoginPassword(
					ProductSafetyXSMBActivity.this).isEmpty()
					&& !SettingsManager.getUser(ProductSafetyXSMBActivity.this)
							.isEmpty();
			// isLogin = true;// ����
			Intent intent = new Intent();
			// 1������Ƿ��Ѿ���¼
			if (isLogin) {
				// �����Ƿ񻹿��Թ���
				handler.sendEmptyMessage(REQUEST_XSMB_BTNCLICK_WHAT);
			} else {
				// δ��¼����ת����¼ҳ��
				intent.setClass(ProductSafetyXSMBActivity.this,
						LoginActivity.class);
				startActivity(intent);
			}
			break;
		default:
			break;
		}
	}

	private void initViewData(ProductInfo productInfo, Enum flag) {
		if (flag == ReasonFlag.REFRESH_DATA || flag == ReasonFlag.RECORD_DATA) {
			// ˢ������
			if ("δ����".equals(productInfo.getMoney_status())) {
				initBidBtnCountDown(productInfo, flag);// ����ʱ
			} else {
				investBtn.setEnabled(false);
				investBtn.setText("Ͷ�ʽ���");
				investBtn.setTextSize(
						TypedValue.COMPLEX_UNIT_PX,
						getResources().getDimensionPixelSize(
								R.dimen.common_measure_26dp));
			}
		} else if (flag == ReasonFlag.BTN_CLICK) {
			// �����������ɱ����ť
			if ("δ����".equals(productInfo.getMoney_status())) {
				// δ���꣬�������û��Ƿ��Ѿ�Ͷ�ʹ������
				initBidBtnCountDown(productInfo, flag);// ����ʱ
			} else {
				// ������
				showPromptDialog("1");
				investBtn.setEnabled(false);
				investBtn.setText("Ͷ�ʽ���");
				investBtn.setTextSize(
						TypedValue.COMPLEX_UNIT_PX,
						getResources().getDimensionPixelSize(
								R.dimen.common_measure_26dp));
			}
		}
	}

	/**
	 * Ͷ�ʰ�ť����ʱ
	 * 
	 * @param productInfo
	 * @throws ParseException
	 */
	long remainTimeL = 0l;

	private void initBidBtnCountDown(ProductInfo productInfo, Enum flag) {
		investBtn.setEnabled(false);
		String nowTimeStr = productInfo.getNow_time();
		String willStartTimeStr = productInfo.getWill_start_time();// ��ʼʱ��
		int hour = 0, minute = 0, second = 0;
		String hourStr = "", minuteStr = "", secondStr = "";
		try {
			Date nowDate = sdf.parse(nowTimeStr);
			Date willStartDate = sdf.parse(willStartTimeStr);
			remainTimeL = willStartDate.getTime() - nowDate.getTime();
			if (remainTimeL >= 0) {
				hour = (int) (remainTimeL / 1000 / 3600);
				minute = (int) ((remainTimeL / 1000 % 3600) / 60);
				second = (int) (remainTimeL / 1000 % 3600 % 60);
				if (hour < 10) {
					hourStr = "0" + hour;
				} else {
					hourStr = hour + "";
				}
				if (minute < 10) {
					minuteStr = "0" + minute;
				} else {
					minuteStr = minute + "";
				}
				if (second < 10) {
					secondStr = "0" + second;
				} else {
					secondStr = second + "";
				}
				investBtn.setText("������һ������ɱ����ʼ��ʣ��" + hourStr + "ʱ" + minuteStr
						+ "��" + secondStr + "��");
				investBtn.setTextSize(
						TypedValue.COMPLEX_UNIT_PX,
						getResources().getDimensionPixelSize(
								R.dimen.common_measure_21dp));
				Message msg = handler.obtainMessage(REFRESH_REMAIN_TIME);
				msg.obj = remainTimeL;
				handler.sendMessageDelayed(msg, 1000l);
			} else {
				// //����Ͷ���С���
				investBtn.setText("������ɱ");
				investBtn.setEnabled(true);
				investBtn.setTextSize(
						TypedValue.COMPLEX_UNIT_PX,
						getResources().getDimensionPixelSize(
								R.dimen.common_measure_26dp));
				if (flag == ReasonFlag.BTN_CLICK) {
					handler.sendEmptyMessage(REQUEST_CURRENT_USERINVEST);
				} else if (flag == ReasonFlag.REFRESH_DATA) {

				}
			}
		} catch (ParseException e) {
			investBtn.setText("Ͷ�ʽ���");
			investBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
					.getDimensionPixelSize(R.dimen.common_measure_26dp));
			e.printStackTrace();
		}
	}

	// ��ʱ���µ���ʱ
	private void updateCountDown(long times) {
		int hour = 0, minute = 0, second = 0;
		String hourStr = "", minuteStr = "", secondStr = "";
		times -= 1000;
		if (times <= 0) {
			investBtn.setEnabled(true);
			investBtn.setText("������ɱ");
			investBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
					.getDimensionPixelSize(R.dimen.common_measure_26dp));
			return;
		}
		hour = (int) (times / 1000 / 3600);
		minute = (int) ((times / 1000 % 3600) / 60);
		second = (int) (times / 1000 % 3600 % 60);
		if (hour < 10) {
			hourStr = "0" + hour;
		} else {
			hourStr = hour + "";
		}
		if (minute < 10) {
			minuteStr = "0" + minute;
		} else {
			minuteStr = minute + "";
		}
		if (second < 10) {
			secondStr = "0" + second;
		} else {
			secondStr = second + "";
		}
		investBtn.setText("������һ������ɱ����ʼ��ʣ��" + hourStr + "ʱ" + minuteStr + "��"
				+ secondStr + "��");
		investBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
				.getDimensionPixelSize(R.dimen.common_measure_21dp));
		Message msg = handler.obtainMessage(REFRESH_REMAIN_TIME);
		msg.obj = times;
		handler.sendMessageDelayed(msg, 1000L);
	}

	/**
	 * 
	 * @param flag
	 *            1��ʾ������ 0��ʾ��ɱ��������
	 */
	private void showPromptDialog(final String flag) {
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
		if ("0".equals(flag)) {
			bottomTV.setVisibility(View.VISIBLE);
			rightBtn.setVisibility(View.VISIBLE);
			topTV.setText("���ı�����ɱ������ʹ��");
			bottomTV.setText("ÿ����ɱ��ÿ���û�ֻ��һ����ɱ����Ӵ~");
			leftBtn.setText("�鿴Ͷ�ʼ�¼");
			leftBtn.setTextColor(getResources().getColor(R.color.common_topbar_bg_color));
			leftBtn.setBackground(getResources().getDrawable(R.drawable.style_rect_fillet_blue));
			rightBtn.setText("��ע������Ŀ");
		} else if ("1".equals(flag)) {
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
				if ("0".equals(flag)) {
					Intent intent = new Intent(ProductSafetyXSMBActivity.this,
							UserInvestRecordActivity.class);
					intent.putExtra("from_where", "���");
					startActivity(intent);
				}
				dialog.dismiss();
			}
		});
		rightBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SettingsManager.setMainProductListFlag(getApplicationContext(),
						true);
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
	 * 
	 */
	private void requestXSMBDetails(String borrowStatus, final Enum flag) {
		AsyncXSMBDetail xsmbTask = new AsyncXSMBDetail(
				ProductSafetyXSMBActivity.this, borrowStatus,
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								investBtn.setVisibility(View.VISIBLE);
								productInfo = baseInfo.getmProductInfo();
								initViewData(productInfo, flag);
							} else {
								investBtn.setVisibility(View.GONE);
							}
						}
					}
				});
		xsmbTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * ����id��ȡ�������
	 * @param borrowId
	 * @param borrowStatus
	 */
	private void requestXSMBSelectone(String borrowId,String borrowStatus,final Enum reasonFlag){
		AsyncXSMBSelectone task = new AsyncXSMBSelectone(ProductSafetyXSMBActivity.this, borrowId, borrowStatus, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								investBtn.setVisibility(View.VISIBLE);
								productInfo = baseInfo.getmProductInfo();
								initViewData(productInfo, reasonFlag);
							} else {
								investBtn.setVisibility(View.GONE);
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * �жϵ�ǰ�û��Ƿ�Ͷ�ʹ������
	 * @param userId
	 * @param borrowId
	 */
	private void requestCurrentUserInvest(String userId,String borrowId){
		AsyncXSMBCurrentUserInvest task = new AsyncXSMBCurrentUserInvest(ProductSafetyXSMBActivity.this, userId, borrowId, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//û��Ͷ�ʹ������
								Intent intent = new Intent(ProductSafetyXSMBActivity.this,BidXSMBActivity.class);
								intent.putExtra("PRODUCT_INFO", productInfo);
								startActivity(intent);
								finish();
							}else{
								//Ͷ�ʹ������
								showPromptDialog("0");
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

}
