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
import com.ylfcf.ppp.async.AsyncVIPRecordList;
import com.ylfcf.ppp.async.AsyncXSBIscanbuy;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.InvestRecordPageInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.ProjectInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnIsBindingListener;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.inter.Inter.OnIsVipUserListener;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.widget.RefreshLayout;
import com.ylfcf.ppp.widget.RefreshLayout.OnLoadListener;

import java.util.ArrayList;
import java.util.List;

/**
 * VIP��Ʒ��¼
 * @author Mr.liu
 *
 */
public class VIPProductRecordActivity extends BaseActivity implements
					OnClickListener{
	private static final String className = "VIPProductRecordActivity";
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;

	private ProductInfo productInfo;
	private ProjectInfo projectInfo;

	private static final int REQUEST_INVEST_RECORD_WHAT = 1021;
	private static final int REQUEST_INVEST_RECORD_SUCCESS = 1022;
	private static final int REQUEST_INVEST_RECORD_NODATA = 1023; // ������

	private BorrowDetailZXDActivity borrowDetailActivity;
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
			case REQUEST_INVEST_RECORD_WHAT:
				if (productInfo != null) {
					getInvestRecordList("", productInfo.getId(), status);
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
		builder = new AlertDialog.Builder(VIPProductRecordActivity.this,
				R.style.Dialog_Transparent); // �ȵõ�������
		Bundle bundle = getIntent().getBundleExtra("BUNDLE");
		if(bundle != null){
			projectInfo = (ProjectInfo) bundle.getSerializable("PROJECT_INFO");
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
				investBtn.setText("Ͷ���ѽ���");
				nodataBtn.setVisibility(View.GONE);
			}
		}
		investRecordsAdapter = new BorrowRecordsAdapter(VIPProductRecordActivity.this);
		recordListView.setAdapter(investRecordsAdapter);
		initListeners();
		handler.sendEmptyMessage(REQUEST_INVEST_RECORD_WHAT);
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
						handler.sendEmptyMessage(REQUEST_INVEST_RECORD_WHAT);
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
						handler.sendEmptyMessage(REQUEST_INVEST_RECORD_WHAT);
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
		handler.removeCallbacksAndMessages(null);
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
					VIPProductRecordActivity.this).isEmpty()
					&& !SettingsManager.getUser(VIPProductRecordActivity.this).isEmpty();
			// isLogin = true;// ����
			Intent intent = new Intent();
			// 1������Ƿ��Ѿ���¼
			if (isLogin) {
				// �Ѿ���¼����ת������ҳ��
				intent.putExtra("PRODUCT_INFO", productInfo);
				if("���ֱ�".equals(productInfo.getBorrow_type())){
					isCanbuyXSB(SettingsManager.getUserId(getApplicationContext()), productInfo.getId());
				}else if("vip".equals(productInfo.getBorrow_type())){
					checkIsVip();
				}else{
					checkIsVerify("���Ŵ�Ͷ��");
				}
			} else {
				// δ��¼����ת����¼ҳ��
				intent.setClass(VIPProductRecordActivity.this,LoginActivity.class);
				startActivity(intent);
				finish();
			}
			break;
		default:
			break;
		}
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
		if("���ܹ������ֱ�".equals(type)){
			sureBtn.setVisibility(View.GONE);
		}else{
			sureBtn.setVisibility(View.VISIBLE);
		}
		msgTV.setText(msg);
		builder.setView(contentView);
		builder.setCancelable(false);
		final AlertDialog dialog = builder.create();
		sureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				if("ʵ����֤".equals(type)){
					intent.setClass(VIPProductRecordActivity.this,UserVerifyActivity.class);
					Bundle bundle = new Bundle();
					if("���ֱ�".equals(productInfo.getBorrow_type())){
						bundle.putString("type", "���ֱ�Ͷ��");
					}else if("vip".equals(productInfo.getBorrow_type())){
						bundle.putString("type", "VIPͶ��");
					}else{
						bundle.putString("type", "���Ŵ�Ͷ��");
					}
					bundle.putSerializable("PRODUCT_INFO", productInfo);
					intent.putExtra("bundle", bundle);
					startActivity(intent);
					investBtn.setEnabled(true);
				}else if("��".equals(type)){
					Bundle bundle = new Bundle();
					if("���ֱ�".equals(productInfo.getBorrow_type())){
						bundle.putString("type", "���ֱ�Ͷ��");
					}else if("vip".equals(productInfo.getBorrow_type())){
						bundle.putString("type", "VIPͶ��");
					}else{
						bundle.putString("type", "���Ŵ�Ͷ��");
					}
					bundle.putSerializable("PRODUCT_INFO", productInfo);
					intent.putExtra("bundle", bundle);
					intent.setClass(VIPProductRecordActivity.this, BindCardActivity.class);
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
	 * �ж��û��Ƿ�Ϊvip�û�
	 */
	private void checkIsVip(){
		RequestApis.requestIsVip(VIPProductRecordActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVipUserListener() {
			@Override
			public void isVip(boolean isvip) {
				if(isvip){
					checkIsVerify("VIPͶ��"); // ֻ�ж���û��ʵ���������ж��Ƿ��
				}else{
					//��VIP�û�����Ͷ��
					showCanotInvestVIPDialog();
				}
			}
		});
	}
	
	/**
	 * ��ʾ������  ��VIP�û����ܹ���Ԫ��ӯ
	 */
	private void showCanotInvestVIPDialog(){
		View contentView = LayoutInflater.from(this)
				.inflate(R.layout.borrow_details_vip_msg_dialog, null);
		final Button leftBtn = (Button) contentView
				.findViewById(R.id.borrow_details_vip_msg_dialog_leftbtn);
		final Button rightBtn = (Button) contentView.
				findViewById(R.id.borrow_details_vip_msg_dialog_rightbtn);
		final TextView msgTV = (TextView) contentView
				.findViewById(R.id.borrow_details_vip_msg_dialog_msg);
		final ImageView delBtn = (ImageView) contentView
				.findViewById(R.id.borrow_details_vip_msg_dialog_delete);
		msgTV.setText("��VIP�û����ܹ���VIP��Ʒ��~");
		builder.setView(contentView);
		builder.setCancelable(false);
		final AlertDialog dialog = builder.create();
		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				setResult(111,intent);
				dialog.dismiss();
				finish();
			}
		});
		rightBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(VIPProductRecordActivity.this,VIPProductCJWTActivity.class);
				startActivity(intent);
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
		RequestApis.requestIsVerify(VIPProductRecordActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(flag){
					//�û��Ѿ�ʵ��
					checkIsBindCard(type);
				}else{
					//�û�û��ʵ��
					showMsgDialog(VIPProductRecordActivity.this, "ʵ����֤", "����ʵ����֤��");
				}
			}
			@Override
			public void isSetWithdrawPwd(boolean flag, Object object) {
			}
		});
	}
	
	/**
	 * �ж��û��Ƿ��Ѿ���
	 * @param type "��ֵ����"
	 */
	private void checkIsBindCard(final String type){
		RequestApis.requestIsBinding(VIPProductRecordActivity.this, SettingsManager.getUserId(getApplicationContext()), "����", new OnIsBindingListener() {
			@Override
			public void isBinding(boolean flag, Object object) {
				Intent intent = new Intent();
				if(flag){
					//�û��Ѿ���
					if("���ֱ�Ͷ��".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(VIPProductRecordActivity.this, BidXSBActivity.class);
					}else if("���Ŵ�Ͷ��".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(VIPProductRecordActivity.this, BidZXDActivity.class);
					}else if("VIPͶ��".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(VIPProductRecordActivity.this, BidVIPActivity.class);
					}
					startActivity(intent);
					investBtn.setEnabled(true);
					finish();
				}else{
					//�û���û�а�
					showMsgDialog(VIPProductRecordActivity.this, "��", "����˾���֧���������������°󿨣�");
				}
			}
		});
	}
	
	/**
	 * ��ȡͶ�ʼ�¼�б�
	 * 
	 * @param investUserId
	 * @param borrowId
	 * @param status
	 */
	private void getInvestRecordList(String investUserId, String borrowId,
			String status) {
		if (isFirst && mLoadingDialog != null && !isFinishing()) {
			mLoadingDialog.show();
		}

		AsyncVIPRecordList asyncInvestRecord = new AsyncVIPRecordList(
				VIPProductRecordActivity.this, investUserId, borrowId, 
				status, pageNo, pageSize, new OnCommonInter() {
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
	 * �ж��Ƿ���Թ������ֱ�
	 * @param userId
	 * @param borrowId
	 */
	private void isCanbuyXSB(String userId,String borrowId){
		AsyncXSBIscanbuy task = new AsyncXSBIscanbuy(VIPProductRecordActivity.this, userId, borrowId, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//�û����Թ������ֱ�
								Intent intent = new Intent();
								intent.putExtra("PRODUCT_INFO", productInfo);
								intent.setClass(VIPProductRecordActivity.this, BidXSBActivity.class);
								startActivity(intent);
							}else if(resultCode == 1001){
								//���Ƚ���ʵ��
								showMsgDialog(VIPProductRecordActivity.this, "ʵ����֤", "����ʵ����֤��");
							}else if(resultCode == 1002){
								//���Ƚ��а�
								boolean isNewUser = SettingsManager.checkIsNewUser(SettingsManager.getUserRegTime(getApplicationContext()));
								if(isNewUser){
									showMsgDialog(VIPProductRecordActivity.this, "��", "�����Ȱ󿨣�");
								}else{
									showMsgDialog(VIPProductRecordActivity.this, "��", "����˾���֧���������������°󿨣�");
								}
							}else{
								showMsgDialog(VIPProductRecordActivity.this, "���ܹ������ֱ�", "�˲�Ʒ���״ι����û�ר��");
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
