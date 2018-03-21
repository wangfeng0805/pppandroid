package com.ylfcf.ppp.ui;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.ProductDataAdapter;
import com.ylfcf.ppp.async.AsyncAsscociatedCompany;
import com.ylfcf.ppp.async.AsyncXSMBCurrentUserInvest;
import com.ylfcf.ppp.async.AsyncXSMBDetail;
import com.ylfcf.ppp.async.AsyncXSMBSelectone;
import com.ylfcf.ppp.entity.AssociatedCompanyParentInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.ProjectCailiaoInfo;
import com.ylfcf.ppp.entity.ProjectInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.ImageLoaderManager;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.widget.GridViewWithHeaderAndFooter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ��ʱ�������֤��
 * @author Mr.liu
 *
 */
public class ProductDataXSMBActivity extends BaseActivity implements OnClickListener{
	private static final String className = "ProductDataXSMBActivity";
	private static final int REQUEST_ASSC_WHAT = 7421;
	private static final int REQUEST_ASSC_SUCCESS = 7422;
	private static final int REQUEST_ASSC_NODATA = 7423;
	
	private static final int REQUEST_XSMB_REFRESH_WHAT = 8291;// ����ӿ�ˢ������
	private static final int REQUEST_CURRENT_USERINVEST = 8294;//����ǰ�û��Ƿ�Ͷ�ʹ������
	private static final int REQUEST_XSMB_BTNCLICK_WHAT = 8292;//�����������ɱ����ť
	private static final int REFRESH_REMAIN_TIME = 8293;//ˢ���¸�����ʣ��ʱ��
	
	private static final int REQUEST_XSMB_SELECTONE = 8296;
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	private GridViewWithHeaderAndFooter dataGridView;
	private Button investBtn;
	private ProductDataAdapter adapter;
	
	private ProjectInfo projectInfo;
	private ProductInfo productInfo;
	private InvestRecordInfo recordInfo;
	private LayoutInflater layoutInflater = null;
	private View bottomView;
	private ArrayList<ProjectCailiaoInfo> noMarksCailiaoList = new ArrayList<ProjectCailiaoInfo>();
	private ArrayList<ProjectCailiaoInfo> marksCailiaoList = new ArrayList<ProjectCailiaoInfo>();
	
	private boolean isInvested = false;//�û��Ƿ�Ͷ���
	private boolean isRequestAssc = false;//�Ƿ������������˾�Ľӿ�
	private AlertDialog.Builder builder = null; // �ȵõ�������
	private String fromWhere = "";
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//��������ԭ��
	private enum ReasonFlag {
		REFRESH_DATA, // ҳ��ˢ������
		BTN_CLICK,// ͨ����ť���
		RECORD_DATA
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_XSMB_REFRESH_WHAT:
				requestXSMBDetails("����",ReasonFlag.REFRESH_DATA);
				break;
			case REQUEST_XSMB_BTNCLICK_WHAT:
				requestXSMBDetails("����",ReasonFlag.BTN_CLICK);
				break;
			case REQUEST_XSMB_SELECTONE:
				requestXSMBSelectone(recordInfo.getBorrow_id(), "����",ReasonFlag.RECORD_DATA);
				break;
			case REQUEST_ASSC_WHAT:
				if(projectInfo != null)
				requestAssociatedCompany(projectInfo.getLoan_id(), projectInfo.getRecommend_id(), projectInfo.getGuarantee_id());
				break;
			case REQUEST_ASSC_SUCCESS:
				AssociatedCompanyParentInfo pageInfo = (AssociatedCompanyParentInfo) msg.obj;
				try {
					combineImgDatas(pageInfo);
				} catch (Exception e) {
				}
				break;
			case REQUEST_CURRENT_USERINVEST:
				if(productInfo != null)
				requestCurrentUserInvest(SettingsManager.getUserId(getApplicationContext()),productInfo.getId());
				break;
			case REQUEST_ASSC_NODATA:
				initImgData();
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
		setContentView(R.layout.product_data_activity);
		builder = new AlertDialog.Builder(ProductDataXSMBActivity.this,
				R.style.Dialog_Transparent); // �ȵõ�������
		Bundle bundle = getIntent().getBundleExtra("BUNDLE");
		layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		if(bundle != null){
			projectInfo = (ProjectInfo) bundle.getSerializable("PROJECT_INFO");
			productInfo = (ProductInfo) bundle.getSerializable("PRODUCT_INFO");
			fromWhere = bundle.getString("from_where");
			recordInfo = (InvestRecordInfo) bundle.getSerializable("InvestRecordInfo");
		}
		findViews();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		UMengStatistics.statisticsOnPageStart(className);//����ͳ��ҳ����ת
		UMengStatistics.statisticsResume(this);//����ͳ��ʱ��
		handler.sendEmptyMessage(REQUEST_CURRENT_USERINVEST);
	}

	@Override
	protected void onPause() {
		super.onPause();
		UMengStatistics.statisticsOnPageEnd(className);//����ͳ��ҳ����ת
		UMengStatistics.statisticsPause(this);//����ͳ��ʱ��
	}

	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("����֤��");
		bottomView = layoutInflater.inflate(R.layout.bottom_button_invest_layout, null);
		investBtn = (Button) bottomView.findViewById(R.id.product_data_activity_bidBtn);
		investBtn.setEnabled(false);
		investBtn.setOnClickListener(this);
		dataGridView = (GridViewWithHeaderAndFooter)findViewById(R.id.product_data_gv);
		dataGridView.addFooterView(bottomView);
		
		dataGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(ProductDataXSMBActivity.this,ProductDataDetailsActivity.class);
				if(isInvested){
					intent.putExtra("cailiao_list",noMarksCailiaoList);
				}else{
					intent.putExtra("cailiao_list",marksCailiaoList);
				}
				intent.putExtra("position", position);
				startActivity(intent);
			}
		});
		initAdapter();
	}
	
	private void initAdapter(){
		adapter = new ProductDataAdapter(ProductDataXSMBActivity.this,layoutInflater);
		dataGridView.setAdapter(adapter);
	}
	
	/**
	 * ��������˾��ͼƬ���Ϻϲ�����Ŀ��ͼƬ��
	 * @param pageInfo
	 */
	private void combineImgDatas(AssociatedCompanyParentInfo pageInfo){
		//��
		List<ProjectCailiaoInfo> nomarkListLoan = new ArrayList<ProjectCailiaoInfo>();
		List<ProjectCailiaoInfo> markListLoan = new ArrayList<ProjectCailiaoInfo>();

		List<String> picNamesLoan = pageInfo.getLoanInfo()
				.getMaterialsNamesList();
		List<String> nomarkListStrLoan = pageInfo.getLoanInfo()
				.getNomarkPicsList();
		List<String> markListStrLoan = pageInfo.getLoanInfo()
				.getMarkPicsList();
		for (int i = 0; i < picNamesLoan.size(); i++) {
			ProjectCailiaoInfo info = new ProjectCailiaoInfo();
			ProjectCailiaoInfo info1 = new ProjectCailiaoInfo();
			try {
				info.setTitle(picNamesLoan.get(i));
			} catch (Exception e) {
				info.setTitle("");
			}
			try {
				info.setImgURL(nomarkListStrLoan.get(i));
			} catch (Exception e) {
				info.setImgURL("");
			}
			nomarkListLoan.add(info);
			
			try {
				info1.setTitle(picNamesLoan.get(i));
			} catch (Exception e) {
				info1.setTitle("");
			}
			try {
				info1.setImgURL(markListStrLoan.get(i));
			} catch (Exception e) {
				info1.setImgURL("");
			}
			markListLoan.add(info1);
		}
		noMarksCailiaoList.addAll(nomarkListLoan);
		marksCailiaoList.addAll(markListLoan);
		//�Ƽ���
		List<ProjectCailiaoInfo> nomarkListRecommend = new ArrayList<ProjectCailiaoInfo>();
		List<ProjectCailiaoInfo> markListRecommend = new ArrayList<ProjectCailiaoInfo>();

		List<String> picNamesRecommend = pageInfo.getRecommendInfo()
				.getMaterialsNamesList();
		List<String> nomarkListStrRecommend = pageInfo.getRecommendInfo()
				.getNomarkPicsList();
		List<String> markListStrRecommend = pageInfo.getRecommendInfo()
				.getMarkPicsList();
		for (int i = 0; i < picNamesRecommend.size(); i++) {
			ProjectCailiaoInfo info = new ProjectCailiaoInfo();
			ProjectCailiaoInfo info1 = new ProjectCailiaoInfo();
			try {
				info.setTitle(picNamesRecommend.get(i));
			} catch (Exception e) {
				info.setTitle("");
			}
			try {
				info.setImgURL(nomarkListStrRecommend.get(i));
			} catch (Exception e) {
				info.setImgURL("");
			}
			nomarkListRecommend.add(info);
			
			try {
				info1.setTitle(picNamesRecommend.get(i));
			} catch (Exception e) {
				info1.setTitle("");
			}
			try {
				info1.setImgURL(markListStrRecommend.get(i));
			} catch (Exception e) {
				info1.setImgURL("");
			}
			markListRecommend.add(info1);
		}
		noMarksCailiaoList.addAll(nomarkListRecommend);
		marksCailiaoList.addAll(markListRecommend);
		//������
		List<ProjectCailiaoInfo> nomarkListGuarantee = new ArrayList<ProjectCailiaoInfo>();
		List<ProjectCailiaoInfo> markListGuarantee = new ArrayList<ProjectCailiaoInfo>();
		
		List<String> picNamesGuarantee = pageInfo.getGuaranteeInfo().getMaterialsNamesList();
		List<String> nomarkListStrGuarantee = pageInfo.getGuaranteeInfo().getNomarkPicsList();
		List<String> markListStrGuarantee = pageInfo.getGuaranteeInfo().getMarkPicsList();
		for(int i=0;i<picNamesGuarantee.size();i++){
			ProjectCailiaoInfo info = new ProjectCailiaoInfo();
			ProjectCailiaoInfo info1 = new ProjectCailiaoInfo();
			try {
				info.setTitle(picNamesGuarantee.get(i));
			} catch (Exception e) {
				info.setTitle("");
			}
			try {
				info.setImgURL(nomarkListStrGuarantee.get(i));
			} catch (Exception e) {
				info.setImgURL("");
			}
			nomarkListGuarantee.add(info);
			
			try {
				info1.setTitle(picNamesGuarantee.get(i));
			} catch (Exception e) {
				info1.setTitle("");
			}
			try {
				info1.setImgURL(markListStrGuarantee.get(i));
			} catch (Exception e) {
				info1.setImgURL("");
			}
			
			markListGuarantee.add(info1);
		}
		noMarksCailiaoList.addAll(nomarkListGuarantee);
		marksCailiaoList.addAll(markListGuarantee);
		
		if(projectInfo != null){
			noMarksCailiaoList.addAll(projectInfo.getCailiaoNoMarkList());
			marksCailiaoList.addAll(projectInfo.getCailiaoMarkList());
		}
		
		if(isInvested){
			adapter.setItems(noMarksCailiaoList);
		}else{
			adapter.setItems(marksCailiaoList);
		}
	}
	
	private void initImgData(){
		noMarksCailiaoList.clear();
		marksCailiaoList.clear();
		if(projectInfo != null){
			noMarksCailiaoList.addAll(projectInfo.getCailiaoNoMarkList());
			marksCailiaoList.addAll(projectInfo.getCailiaoMarkList());
		}
		if(isInvested){
			adapter.setItems(noMarksCailiaoList);
		}else{
			adapter.setItems(marksCailiaoList);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ImageLoaderManager.clearMemoryCache();
		handler.removeCallbacksAndMessages(null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.product_data_activity_bidBtn:
			// ��SettingsManager�ж�ȡ���룬���Ϊ����ζ��û�е�¼��
			boolean isLogin = !SettingsManager.getLoginPassword(
					ProductDataXSMBActivity.this).isEmpty()
					&& !SettingsManager.getUser(ProductDataXSMBActivity.this)
							.isEmpty();
			investBtn.setEnabled(false);
			Intent intent = new Intent();
			// 1������Ƿ��Ѿ���¼
			if (isLogin) {
				// �����Ƿ񻹿��Թ���
				handler.sendEmptyMessage(REQUEST_XSMB_BTNCLICK_WHAT);
			} else {
				// δ��¼����ת����¼ҳ��
				intent.setClass(ProductDataXSMBActivity.this,LoginActivity.class);
				startActivity(intent);
				investBtn.setEnabled(true);
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
				investBtn.setEnabled(false);
				investBtn.setText("Ͷ�ʽ���");
				investBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_26dp));
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
	/**
	 * Ͷ�ʰ�ť����ʱ
	 * @param productInfo
	 * @throws ParseException 
	 */
	long remainTimeL = 0l;
	private void initBidBtnCountDown(ProductInfo productInfo,Enum flag){
		investBtn.setEnabled(false);
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
				investBtn.setText("������һ������ɱ����ʼ��ʣ��"+hourStr+"ʱ"+minuteStr+"��"+secondStr+"��");
				investBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_21dp));
				Message msg = handler.obtainMessage(REFRESH_REMAIN_TIME);
				msg.obj = remainTimeL;
				handler.sendMessage(msg);
			}else{
//				//����Ͷ���С���
				investBtn.setText("������ɱ");
				investBtn.setEnabled(true);
				investBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_26dp));
				if(flag == ReasonFlag.BTN_CLICK && !isInvested){
//					handler.sendEmptyMessage(REQUEST_CURRENT_USERINVEST);
					Intent intent = new Intent(ProductDataXSMBActivity.this,BidXSMBActivity.class);
					intent.putExtra("PRODUCT_INFO", productInfo);
					startActivity(intent);
				}else if(flag == ReasonFlag.BTN_CLICK && isInvested){
					showPromptDialog("0");
				}else if(flag == ReasonFlag.REFRESH_DATA){
					
				}
			}
		} catch (ParseException e) {
			investBtn.setText("Ͷ�ʽ���");
			investBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_26dp));
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
					Intent intent = new Intent(ProductDataXSMBActivity.this,UserInvestRecordActivity.class);
					intent.putExtra("from_where", "���");
					startActivity(intent);
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
	 * ������˾����Ϣ
	 * @param loanId
	 * @param recommendId
	 * @param guaranteeId
	 */
	private void requestAssociatedCompany(String loanId,String recommendId,String guaranteeId){
		AsyncAsscociatedCompany task = new AsyncAsscociatedCompany(ProductDataXSMBActivity.this, loanId, recommendId, guaranteeId, 
				new OnCommonInter(){
					@Override
					public void back(BaseInfo baseInfo) {
						isRequestAssc = true;
						dataGridView.setVisibility(View.VISIBLE);
						if(mLoadingDialog != null && mLoadingDialog.isShowing()){
							mLoadingDialog.dismiss();
						}
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								AssociatedCompanyParentInfo info = baseInfo.getAssociatedCompanyParentInfo();
								Message msg = handler.obtainMessage(REQUEST_ASSC_SUCCESS);
								msg.obj = info;
								handler.sendMessage(msg);
							}else{
								Message msg = handler.obtainMessage(REQUEST_ASSC_NODATA);
								msg.obj = baseInfo;
								handler.sendMessage(msg);
							}
						}else{
							Message msg = handler.obtainMessage(REQUEST_ASSC_NODATA);
							msg.obj = baseInfo;
							handler.sendMessage(msg);
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
		AsyncXSMBCurrentUserInvest task = new AsyncXSMBCurrentUserInvest(ProductDataXSMBActivity.this, userId, borrowId, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//û��Ͷ�ʹ�
								isInvested = false;
							}else if(resultCode == -102){
								isInvested = true;
							}else{
								isInvested = false;
							}
						}else{
							isInvested = false;
						}
						if(recordInfo == null){
							handler.sendEmptyMessage(REQUEST_XSMB_REFRESH_WHAT);
						}else{
							handler.sendEmptyMessage(REQUEST_XSMB_SELECTONE);
						}
						if(!isRequestAssc){
							handler.sendEmptyMessage(REQUEST_ASSC_WHAT);
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * �������
	 */
	private void requestXSMBDetails(String borrowStatus,final Enum flag) {
		AsyncXSMBDetail xsmbTask = new AsyncXSMBDetail(
				ProductDataXSMBActivity.this, borrowStatus,new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								investBtn.setVisibility(View.VISIBLE);
								productInfo = baseInfo
										.getmProductInfo();
								initViewData(productInfo,flag);
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
		AsyncXSMBSelectone task = new AsyncXSMBSelectone(ProductDataXSMBActivity.this, borrowId, borrowStatus, 
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
}
