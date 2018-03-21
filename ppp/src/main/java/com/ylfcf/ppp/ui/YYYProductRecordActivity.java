package com.ylfcf.ppp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
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
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.BorrowRecordsAdapter;
import com.ylfcf.ppp.async.AsyncWDYInvestRecord;
import com.ylfcf.ppp.async.AsyncYYYInvestRecord;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.InvestRecordPageInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.widget.RefreshLayout;
import com.ylfcf.ppp.widget.RefreshLayout.OnLoadListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Ͷ�ʼ�¼ -- ��Ʒ���������Ͷ�ʼ�¼ ---- Ԫ��ӯ
 * @author Mr.liu
 *
 */
public class YYYProductRecordActivity extends BaseActivity implements
		OnClickListener {
	private static final String className = "YYYProductRecordActivity";
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;

	private ProductInfo productInfo;

	private static final int REQUEST_YYY_INVEST_RECORD_WHAT = 1021;//Ԫ��ӯͶ�ʼ�¼
	private static final int REQUEST_INVEST_RECORD_SUCCESS = 1022;
	private static final int REQUEST_INVEST_RECORD_NODATA = 1023; // ������
	private static final int REQUEST_WDY_INVEST_RECORD_WHAT = 1024;//�ȶ�ӯͶ�ʼ�¼

	private ListView recordListView;
	private LayoutInflater layoutInflater;
	private View headerView;
	private View nodataView;
	private View footerView;
	private TextView nodataText;
	private Button nodataBtn;
	private Button investBtn;

	private RefreshLayout refreshLayout;
	private BorrowRecordsAdapter investRecordsAdapter;
	private List<InvestRecordInfo> investRecordList = new ArrayList<InvestRecordInfo>();

	// �������
	private String status = "";// Ͷ����
	private int pageNo = 0;
	private int pageSize = 50;
	private boolean isRefresh = true;// ����ˢ��
	private boolean isLoad = false;// �������ظ���
	private boolean isFirst = true;
	private AlertDialog.Builder builder = null; // �ȵõ�������

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_YYY_INVEST_RECORD_WHAT:
				if (productInfo != null) {
					getYYYInvestRecordList(productInfo.getId(), "", "", "", "");
				}
				break;
			case REQUEST_WDY_INVEST_RECORD_WHAT:
				//�ȶ�ӯ
				if (productInfo != null) {
					getWDYInvestRecordList(productInfo.getId());
				}
				break;
			case REQUEST_INVEST_RECORD_SUCCESS:
				refreshLayout.setVisibility(View.VISIBLE);
				nodataView.setVisibility(View.GONE);
				InvestRecordPageInfo pageInfo = (InvestRecordPageInfo) msg.obj;
				if (isRefresh) {
					investRecordList.clear();
					investRecordList.addAll(pageInfo.getInvestRecordList());
				} else if (isLoad) {
					investRecordList.addAll(pageInfo.getInvestRecordList());
				}
				updateAdapter(investRecordList);
				break;
			case REQUEST_INVEST_RECORD_NODATA:
				if (isRefresh) {
					refreshLayout.setVisibility(View.GONE);
					nodataView.setVisibility(View.VISIBLE);
				}
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
		setContentView(R.layout.product_record_activity);
		builder = new AlertDialog.Builder(YYYProductRecordActivity.this,
				R.style.Dialog_Transparent); // �ȵõ�������
		Bundle bundle = getIntent().getBundleExtra("BUNDLE");
		if(bundle != null){
			productInfo = (ProductInfo) bundle.getSerializable("PRODUCT_INFO");
		}
		
		layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		findViews();
	}

	private void updateAdapter(List<InvestRecordInfo> recordList) {
		investRecordsAdapter.setItems(recordList);
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("Ͷ�ʼ�¼");

		refreshLayout = (RefreshLayout) findViewById(R.id.product_record_activity_refreshlayout);
		nodataText = (TextView) findViewById(R.id.nodata_layout_text);
		nodataText.setText("���޼�¼");
		nodataBtn = (Button) findViewById(R.id.nodata_layout_btn);
		nodataBtn.setOnClickListener(this);
		nodataView = findViewById(R.id.product_record_activity_nodata_layout);
		headerView = layoutInflater.inflate(R.layout.invest_records_header,null);
		footerView = layoutInflater.inflate(R.layout.bottom_button_invest_layout, null);
		investBtn = (Button) footerView.findViewById(R.id.product_data_activity_bidBtn);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				getResources().getDimensionPixelSize(R.dimen.common_measure_58dp));
		params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.common_measure_50dp);
		params.leftMargin = getResources().getDimensionPixelSize(R.dimen.common_measure_20dp);
		params.rightMargin = getResources().getDimensionPixelSize(R.dimen.common_measure_20dp);
		params.topMargin = getResources().getDimensionPixelSize(R.dimen.common_measure_30dp);
		investBtn.setLayoutParams(params);
		investBtn.setOnClickListener(this);
		recordListView = (ListView) findViewById(R.id.product_record_activity_listview);
		recordListView.addHeaderView(headerView);
		recordListView.addFooterView(footerView);
		if(productInfo != null){
			if("δ����".equals(productInfo.getMoney_status())){
				investBtn.setEnabled(true);
				investBtn.setText("����Ͷ��");
				nodataBtn.setVisibility(View.VISIBLE);
			}else{
				investBtn.setEnabled(false);
				investBtn.setText("Ͷ�ʽ���");
				nodataBtn.setVisibility(View.GONE);
			}
		}
		investRecordsAdapter = new BorrowRecordsAdapter(YYYProductRecordActivity.this);
		recordListView.setAdapter(investRecordsAdapter);
		initListeners();
		if("Ԫ��ӯ".equals(productInfo.getBorrow_type())){
			handler.sendEmptyMessage(REQUEST_YYY_INVEST_RECORD_WHAT);
		}else if("�ȶ�ӯ".equals(productInfo.getBorrow_type())){
			handler.sendEmptyMessage(REQUEST_WDY_INVEST_RECORD_WHAT);
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

	private void initListeners() {
		refreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				refreshLayout.postDelayed(new Runnable() {

					@Override
					public void run() {
						pageNo = 0;
						updateLoadStatus(true, false);
						// ��������
						if("Ԫ��ӯ".equals(productInfo.getBorrow_type())){
							handler.sendEmptyMessage(REQUEST_YYY_INVEST_RECORD_WHAT);
						}else if("�ȶ�ӯ".equals(productInfo.getBorrow_type())){
							handler.sendEmptyMessage(REQUEST_WDY_INVEST_RECORD_WHAT);
						}
						// ���������ø÷�������ˢ��
						refreshLayout.setRefreshing(false);
					}
				}, 1000);
			}
		});

		// ���ؼ�����
		refreshLayout.setOnLoadListener(new OnLoadListener() {
			@Override
			public void onLoad() {
				++pageNo;
				refreshLayout.postDelayed(new Runnable() {
					@Override
					public void run() {
						updateLoadStatus(false, true);
						if("Ԫ��ӯ".equals(productInfo.getBorrow_type())){
							handler.sendEmptyMessage(REQUEST_YYY_INVEST_RECORD_WHAT);
						}else if("�ȶ�ӯ".equals(productInfo.getBorrow_type())){
							handler.sendEmptyMessage(REQUEST_WDY_INVEST_RECORD_WHAT);
						}
						// ���������ø÷���
						refreshLayout.setLoading(false);
					}
				}, 1500);
			}
		});
	}

	private void updateLoadStatus(boolean isRef, boolean isload) {
		this.isRefresh = isRef;
		this.isLoad = isload;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.product_data_activity_bidBtn:
		case R.id.nodata_layout_btn:
			// ��SettingsManager�ж�ȡ���룬���Ϊ����ζ��û�е�¼��
			boolean isLogin = !SettingsManager.getLoginPassword(
					YYYProductRecordActivity.this).isEmpty()
					&& !SettingsManager.getUser(YYYProductRecordActivity.this).isEmpty();
			// isLogin = true;// ����
			Intent intent = new Intent();
			// 1������Ƿ��Ѿ���¼
			if (isLogin) {
				// �Ѿ���¼����ת������ҳ��
				checkIsVerify(productInfo.getBorrow_type());
			} else {
				// δ��¼����ת����¼ҳ��
				intent.setClass(YYYProductRecordActivity.this,LoginActivity.class);
				startActivity(intent);
				finish();
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}
	
	/**
	 * ��ʾ������
	 * @param type
	 * @param msg
	 */
	private void showMsgDialog(Context context,final String type,String msg){
		View contentView = LayoutInflater.from(context)
				.inflate(R.layout.borrow_details_msg_dialog, null);
		final Button sureBtn = (Button) contentView
				.findViewById(R.id.borrow_details_msg_dialog_surebtn);
		final TextView msgTV = (TextView) contentView
				.findViewById(R.id.borrow_details_msg_dialog_msg);
		final ImageView delBtn = (ImageView) contentView
				.findViewById(R.id.borrow_details_msg_dialog_delete);
		msgTV.setText(msg);
		builder.setView(contentView);
		builder.setCancelable(false);
		final AlertDialog dialog = builder.create();
		sureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				if("ʵ����֤".equals(type)){
					intent.setClass(YYYProductRecordActivity.this,UserVerifyActivity.class);
					Bundle bundle = new Bundle();
					if("Ԫ��ӯ".equals(productInfo.getBorrow_type())){
						bundle.putString("type", "Ԫ��ӯͶ��");
					}else if("�ȶ�ӯ".equals(productInfo.getBorrow_type())){
						bundle.putString("type", "�ȶ�ӯͶ��");
					}
					bundle.putSerializable("PRODUCT_INFO", productInfo);
					intent.putExtra("bundle", bundle);
					startActivity(intent);
					investBtn.setEnabled(true);
				}
				dialog.dismiss();
			}
		});
		delBtn.setOnClickListener(new OnClickListener() {
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
		lp.height = display.getHeight()/3;
		dialog.getWindow().setAttributes(lp);
	}
	
	/**
	 * ��֤�û��Ƿ��Ѿ���֤
	 * @param type ����ֵ��,�����֡�
	 */
	private void checkIsVerify(final String type){
		investBtn.setEnabled(false);
		RequestApis.requestIsVerify(YYYProductRecordActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				Intent intent = new Intent();
				if(flag){
					//�û��Ѿ�ʵ��,�˴�ֻ�ж���û��ʵ�����ɣ������ж���û�а�
					if("Ԫ��ӯ".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(YYYProductRecordActivity.this, BidYYYActivity.class);
					}else if("�ȶ�ӯ".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(YYYProductRecordActivity.this, BidWDYActivity.class);
					}
					startActivity(intent);
					investBtn.setEnabled(true);
					finish();
				}else{
					//�û�û��ʵ��
					showMsgDialog(YYYProductRecordActivity.this, "ʵ����֤", "����ʵ����֤��");
				}
			}
			@Override
			public void isSetWithdrawPwd(boolean flag, Object object) {
			}
		});
	}
	
	/**
	 * ��ȡͶ�ʼ�¼�б�
	 * 
	 */
	private void getYYYInvestRecordList(String borrowId, String investStatus,
			String investUserId, String returnStatus,String type) {
		if (isFirst && mLoadingDialog != null && !isFinishing()) {
			mLoadingDialog.show();
		}
		AsyncYYYInvestRecord asyncInvestRecord = new AsyncYYYInvestRecord(
				YYYProductRecordActivity.this, borrowId, investStatus, investUserId,
				returnStatus,type, pageNo, pageSize, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						isFirst = false;
						if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
							mLoadingDialog.dismiss();
						}
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								Message msg = handler
										.obtainMessage(REQUEST_INVEST_RECORD_SUCCESS);
								msg.obj = baseInfo.getmInvestRecordPageInfo();
								handler.sendMessage(msg);
							} else {
								Message msg = handler
										.obtainMessage(REQUEST_INVEST_RECORD_NODATA);
								msg.obj = baseInfo.getMsg();
								handler.sendMessage(msg);
							}
						}

					}
				});
		asyncInvestRecord.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * �ȶ�ӯ��Ͷ�ʼ�¼
	 * @param borrowId
	 */
	private void getWDYInvestRecordList(String borrowId){
		if (isFirst && mLoadingDialog != null && !isFinishing()) {
			mLoadingDialog.show();
		}
		AsyncWDYInvestRecord task = new AsyncWDYInvestRecord(YYYProductRecordActivity.this, borrowId, "","","",pageNo, pageSize, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						isFirst = false;
						if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
							mLoadingDialog.dismiss();
						}
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if (resultCode == 0) {
								Message msg = handler
										.obtainMessage(REQUEST_INVEST_RECORD_SUCCESS);
								msg.obj = baseInfo.getmInvestRecordPageInfo();
								handler.sendMessage(msg);
							} else {
								Message msg = handler
										.obtainMessage(REQUEST_INVEST_RECORD_NODATA);
								msg.obj = baseInfo.getMsg();
								handler.sendMessage(msg);
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
