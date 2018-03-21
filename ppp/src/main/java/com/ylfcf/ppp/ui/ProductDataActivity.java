package com.ylfcf.ppp.ui;

import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.ProductDataAdapter;
import com.ylfcf.ppp.async.AsyncAsscociatedCompany;
import com.ylfcf.ppp.async.AsyncCurrentUserInvest;
import com.ylfcf.ppp.async.AsyncInvestSRZXRecord;
import com.ylfcf.ppp.async.AsyncInvestWDYRecord;
import com.ylfcf.ppp.async.AsyncVIPCurrentUserInvest;
import com.ylfcf.ppp.async.AsyncXSBIscanbuy;
import com.ylfcf.ppp.async.AsyncYGZXBorrowDetail;
import com.ylfcf.ppp.entity.AssociatedCompanyParentInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.BorrowType;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.ProjectCailiaoInfo;
import com.ylfcf.ppp.entity.ProjectInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.inter.Inter.OnIsVipUserListener;
import com.ylfcf.ppp.util.Constants;
import com.ylfcf.ppp.util.ImageLoaderManager;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.widget.GridViewWithHeaderAndFooter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ��Ŀ������֤��
 * @author Administrator
 *
 */
public class ProductDataActivity extends BaseActivity implements OnClickListener{
	private static final String className = "ProductDataActivity";
	private static final int REQUEST_ASSC_WHAT = 7421;
	private static final int REQUEST_ASSC_SUCCESS = 7422;
	private static final int REQUEST_ASSC_NODATA = 7423;
	
	private static final int REQUEST_CURRENT_USER_INVEST_WHAT = 7424;
	private static final int REQUEST_CURRENT_USER_INVEST_SUCCESS = 7425;
	private static final int REQUEST_CURRENT_USER_INVEST_NODATA = 7426;
	private static final int REQUEST_VIPCURRENT_USER_INVEST_WHAT = 7427;
	private static final int REQUEST_SRZXCURRENT_USER_INVEST_WHAT = 7428;
	private static final int REQUEST_WDYCURRENT_USER_INVEST_WHAT = 7429;
	private static final int REQUEST_YJYCURRENT_USER_INVEST_WHAT = 7430;
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	private GridViewWithHeaderAndFooter dataGridView;
	private Button investBtn;
	private ProductDataAdapter adapter;
	
	private ProjectInfo projectInfo;
	private ProductInfo productInfo;
	private LayoutInflater layoutInflater = null;
	private View bottomView;
	private ArrayList<ProjectCailiaoInfo> noMarksCailiaoList = new ArrayList<ProjectCailiaoInfo>();
	private ArrayList<ProjectCailiaoInfo> marksCailiaoList = new ArrayList<ProjectCailiaoInfo>();
	
	private boolean isInvested = false;//�û��Ƿ�Ͷ���
	private AlertDialog.Builder builder = null; // �ȵõ�������
	private String fromWhere = "";
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
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
			case REQUEST_ASSC_NODATA:
				initImgData();
				break;
			case REQUEST_CURRENT_USER_INVEST_WHAT:
				requestCurrentUserInvest(SettingsManager.getUserId(getApplicationContext()), productInfo.getId());
				break;
			case REQUEST_VIPCURRENT_USER_INVEST_WHAT:
				requestVIPCurrentUserInvest(SettingsManager.getUserId(getApplicationContext()), productInfo.getId());
				break;
			case REQUEST_SRZXCURRENT_USER_INVEST_WHAT:
				//�ж�ĳ���û���û�����˽������ı�
				requestSRZXCurrentUserInvest(SettingsManager.getUserId(getApplicationContext()),productInfo.getId());
				break;
			case REQUEST_CURRENT_USER_INVEST_SUCCESS:
				break;
			case REQUEST_CURRENT_USER_INVEST_NODATA:
				break;
			case REQUEST_WDYCURRENT_USER_INVEST_WHAT:
				requestWDYCurrentUserInvest(SettingsManager.getUserId(getApplicationContext()),productInfo.getId());
				break;
			case REQUEST_YJYCURRENT_USER_INVEST_WHAT:
				requestYJYCurrentUserInvest(SettingsManager.getUserId(getApplicationContext()),productInfo.getId());
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
		
		builder = new AlertDialog.Builder(ProductDataActivity.this,
				R.style.Dialog_Transparent); // �ȵõ�������
		Bundle bundle = getIntent().getBundleExtra("BUNDLE");
		layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		if(bundle != null){
			projectInfo = (ProjectInfo) bundle.getSerializable("PROJECT_INFO");
			productInfo = (ProductInfo) bundle.getSerializable("PRODUCT_INFO");
			fromWhere = bundle.getString("from_where");
		}
		findViews();
		if(productInfo != null && "dqlc".equals(fromWhere)){
			handler.sendEmptyMessage(REQUEST_CURRENT_USER_INVEST_WHAT);
		}else if("vip".equals(fromWhere)){
			handler.sendEmptyMessage(REQUEST_VIPCURRENT_USER_INVEST_WHAT);
		}else if("srzx".equals(fromWhere)){
			handler.sendEmptyMessage(REQUEST_SRZXCURRENT_USER_INVEST_WHAT);
		}else if("wdy".equals(fromWhere)){
			handler.sendEmptyMessage(REQUEST_WDYCURRENT_USER_INVEST_WHAT);
		}else if("yjy".equals(fromWhere)){
			handler.sendEmptyMessage(REQUEST_YJYCURRENT_USER_INVEST_WHAT);
		}else{
			handler.sendEmptyMessage(REQUEST_CURRENT_USER_INVEST_WHAT);
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

	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("����֤��");
		bottomView = layoutInflater.inflate(R.layout.bottom_button_invest_layout, null);
		investBtn = (Button) bottomView.findViewById(R.id.product_data_activity_bidBtn);
		investBtn.setOnClickListener(this);
		dataGridView = (GridViewWithHeaderAndFooter)findViewById(R.id.product_data_gv);
		dataGridView.addFooterView(bottomView);
		Date addDate = null;
		try{
			addDate = sdf.parse(productInfo.getAdd_time());
		}catch (Exception e){
			e.printStackTrace();
		}
		if(productInfo != null){
			if("δ����".equals(productInfo.getMoney_status())){
				if(SettingsManager.checkActiveStatusBySysTime(productInfo.getAdd_time(),SettingsManager.yyyJIAXIStartTime,
						SettingsManager.yyyJIAXIEndTime) == 0 && "Ԫ����".equals(productInfo.getBorrow_type())&& Constants.UserType.USER_COMPANY.
						equals(SettingsManager.getUserType(ProductDataActivity.this))){
					investBtn.setEnabled(false);
				}else{
					investBtn.setEnabled(true);
				}
				investBtn.setText("����Ͷ��");
			}else{
				investBtn.setEnabled(false);
				investBtn.setText("Ͷ���ѽ���");
			}
		}
		
		dataGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(ProductDataActivity.this,ProductDataDetailsActivity.class);
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
		adapter = new ProductDataAdapter(ProductDataActivity.this,layoutInflater);
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
			boolean isLogin = !SettingsManager.getLoginPassword(ProductDataActivity.this).isEmpty()
					&& !SettingsManager.getUser(ProductDataActivity.this).isEmpty();
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
				}else if(BorrowType.SUYING.equals(productInfo.getBorrow_type()) || BorrowType.BAOYING.equals(productInfo.getBorrow_type()) ||
						BorrowType.WENYING.equals(productInfo.getBorrow_type()) || BorrowType.YUANNIANXIN.equals(productInfo.getBorrow_type())){
					checkIsVerify("���Ŵ�Ͷ��");
				}else if("�ȶ�ӯ".equals(productInfo.getBorrow_type())){
					checkIsVerify("�ȶ�ӯͶ��");
				}else{
					//˽������
					if(productInfo.getBorrow_name().contains("˽������")){
						checkIsVerify("˽������");
					}else if(productInfo.getBorrow_name().contains("Ԫ��ӯ")){
						checkIsVerify("Ԫ��ӯ");
					}
				}
			} else {
				// δ��¼����ת����¼ҳ��
				intent.setClass(ProductDataActivity.this,LoginActivity.class);
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
					intent.setClass(ProductDataActivity.this,UserVerifyActivity.class);
					Bundle bundle = new Bundle();
					if("���ֱ�".equals(productInfo.getBorrow_type())){
						bundle.putString("type", "���ֱ�Ͷ��");
					}else if("vip".equals(productInfo.getBorrow_type())){
						bundle.putString("type", "VIPͶ��");
					}else if(BorrowType.SUYING.equals(productInfo.getBorrow_type()) || BorrowType.BAOYING.equals(productInfo.getBorrow_type())
							|| BorrowType.WENYING.equals(productInfo.getBorrow_type())){
						bundle.putString("type", "���Ŵ�Ͷ��");
					}else if("�ȶ�ӯ".equals(productInfo.getBorrow_type())){
						bundle.putString("type","�ȶ�ӯͶ��");
					}else{
						if(productInfo.getBorrow_name().contains("˽������")){
							bundle.putString("type", "˽������");
						}
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
					}else if(BorrowType.SUYING.equals(productInfo.getBorrow_type()) || BorrowType.BAOYING.equals(productInfo.getBorrow_type())
							|| BorrowType.WENYING.equals(productInfo.getBorrow_type())){
						bundle.putString("type", "���Ŵ�Ͷ��");
					}else if("�ȶ�ӯ".equals(type)){
						bundle.putString("type", "�ȶ�ӯͶ��");
					}else{
						if(productInfo.getBorrow_name().contains("˽������")){
							bundle.putString("type", "˽������");
						}
					}
					bundle.putSerializable("PRODUCT_INFO", productInfo);
					intent.putExtra("bundle", bundle);
					intent.setClass(ProductDataActivity.this, BindCardActivity.class);
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
		RequestApis.requestIsVip(ProductDataActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVipUserListener() {
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
				Intent intent = new Intent(ProductDataActivity.this,VIPProductCJWTActivity.class);
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
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		RequestApis.requestIsVerify(ProductDataActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(mLoadingDialog != null && mLoadingDialog.isShowing()){
					mLoadingDialog.dismiss();
				}
				Intent intent = new Intent();
				if(flag){
					//�û��Ѿ�ʵ���������ҳ��ֻ�ж��Ƿ�ʵ�����ɡ����ж���û�а�
//					checkIsBindCard(type);
					if("���ֱ�Ͷ��".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductDataActivity.this, BidXSBActivity.class);
					}else if("VIPͶ��".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductDataActivity.this, BidVIPActivity.class);
					}else if("���Ŵ�Ͷ��".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductDataActivity.this, BidZXDActivity.class);
					}else if("˽������".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductDataActivity.this, BidSRZXActivity.class);
					}else if("�ȶ�ӯͶ��".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductDataActivity.this, BidWDYActivity.class);
					}else if("Ԫ��ӯ".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductDataActivity.this, BidYJYActivity.class);
					}
					investBtn.setEnabled(true);
					startActivity(intent);
					finish();
				}else{
					//�û�û��ʵ��
					showMsgDialog(ProductDataActivity.this, "ʵ����֤", "����ʵ����֤��");
				}
			}
			@Override
			public void isSetWithdrawPwd(boolean flag, Object object) {
			}
		});
	}
	
	/**
	 * ������˾����Ϣ
	 * @param loanId
	 * @param recommendId
	 * @param guaranteeId
	 */
	private void requestAssociatedCompany(String loanId,String recommendId,String guaranteeId){
		AsyncAsscociatedCompany task = new AsyncAsscociatedCompany(ProductDataActivity.this, loanId, recommendId, guaranteeId, 
				new OnCommonInter(){
					@Override
					public void back(BaseInfo baseInfo) {
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
	 * �жϵ�ǰ�û��Ƿ�Ͷ�ʹ��ñ��
	 * @param investUserId
	 * @param borrowId
	 */
	private void requestCurrentUserInvest(String investUserId,String borrowId){
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		AsyncCurrentUserInvest task = new AsyncCurrentUserInvest(ProductDataActivity.this, investUserId, borrowId, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								isInvested = true;
							}else{
								isInvested = false;
							}
						}else{
							isInvested = false;
						}
						handler.sendEmptyMessage(REQUEST_ASSC_WHAT);
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * �жϵ�ǰ�û��Ƿ�Ͷ�ʹ�VIP�ñ��
	 * @param investUserId
	 * @param borrowId
	 */
	private void requestVIPCurrentUserInvest(String investUserId,String borrowId){
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		AsyncVIPCurrentUserInvest task = new AsyncVIPCurrentUserInvest(ProductDataActivity.this, investUserId, borrowId, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								isInvested = true;
							}else{
								isInvested = false;
							}
						}else{
							isInvested = false;
						}
						handler.sendEmptyMessage(REQUEST_ASSC_WHAT);
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * �ж��Ƿ���Թ������ֱ�
	 * @param userId
	 * @param borrowId
	 */
	private void isCanbuyXSB(String userId,String borrowId){
		AsyncXSBIscanbuy task = new AsyncXSBIscanbuy(ProductDataActivity.this, userId, borrowId, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//�û����Թ������ֱ�
								Intent intent = new Intent();
								intent.putExtra("PRODUCT_INFO", productInfo);
								intent.setClass(ProductDataActivity.this, BidXSBActivity.class);
								startActivity(intent);
							}else if(resultCode == 1001){
								//���Ƚ���ʵ��
								showMsgDialog(ProductDataActivity.this, "ʵ����֤", "����ʵ����֤��");
							}else if(resultCode == 1002){
								//���Ƚ��а�
								boolean isNewUser = SettingsManager.checkIsNewUser(SettingsManager.getUserRegTime(getApplicationContext()));
								if(isNewUser){
									showMsgDialog(ProductDataActivity.this, "��", "�����Ȱ󿨣�");
								}else{
									showMsgDialog(ProductDataActivity.this, "��", "����˾���֧���������������°󿨣�");
								}
							}else{
								showMsgDialog(ProductDataActivity.this, "���ܹ������ֱ�", "�˲�Ʒ���״ι����û�ר��");
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * �ж�ĳ���û���û�����˽������ı�
	 * @param userId
	 * @param borrowId
	 */
	private void requestSRZXCurrentUserInvest(String userId,String borrowId){
		AsyncInvestSRZXRecord recordTask = new AsyncInvestSRZXRecord(ProductDataActivity.this, userId, borrowId, 0, 2, 
				new OnCommonInter(){
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								isInvested = true;
							}else{
								isInvested = false;
							}
						}else{
							isInvested = false;
						}
						handler.sendEmptyMessage(REQUEST_ASSC_WHAT);
					}
		});
		recordTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * �ж�ĳ���û���û�����˽������ı�
	 * @param userId
	 * @param borrowId
	 */
	private void requestYJYCurrentUserInvest(String userId,String borrowId){
		AsyncYGZXBorrowDetail recordTask = new AsyncYGZXBorrowDetail(ProductDataActivity.this, userId, borrowId,
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								isInvested = true;
							}else{
								isInvested = false;
							}
						}else{
							isInvested = false;
						}
						handler.sendEmptyMessage(REQUEST_ASSC_WHAT);
					}
				});
		recordTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * ��ǰ�û��Ƿ�Ͷ�ʹ��ȶ�ӯ
	 * @param userId
	 * @param borrowId
	 */
	private void requestWDYCurrentUserInvest(String userId,String borrowId){
		AsyncInvestWDYRecord recordTask = new AsyncInvestWDYRecord(ProductDataActivity.this, userId, borrowId,
				new OnCommonInter(){
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == -102){
								isInvested = true;
							}else{
								isInvested = false;
							}
						}else{
							isInvested = false;
						}
						handler.sendEmptyMessage(REQUEST_ASSC_WHAT);
					}
		});
		recordTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
