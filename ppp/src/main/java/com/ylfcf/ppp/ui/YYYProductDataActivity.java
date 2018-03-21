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
import com.ylfcf.ppp.async.AsyncYYYCurrentUserInvest;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.ProjectCailiaoInfo;
import com.ylfcf.ppp.entity.ProjectInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnIsBindingListener;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.util.ImageLoaderManager;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.widget.GridViewWithHeaderAndFooter;

import java.util.ArrayList;

/**
 * Ԫ��ӯ�ĵ�������
 * @author Mr.liu
 *
 */
public class YYYProductDataActivity extends BaseActivity implements OnClickListener{
	private static final String className = "YYYProductDataActivity";
	private static final int REQUEST_CURRENT_USER_INVEST_WHAT = 7424;
	private static final int REQUEST_CURRENT_USER_INVEST_SUCCESS = 7425;
	private static final int REQUEST_CURRENT_USER_INVEST_NODATA = 7426;
	
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
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_CURRENT_USER_INVEST_WHAT:
				requestYYYCurrentUserInvest(SettingsManager.getUserId(getApplicationContext()), productInfo.getId());
				break;
			case REQUEST_CURRENT_USER_INVEST_SUCCESS:
				break;
			case REQUEST_CURRENT_USER_INVEST_NODATA:
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
		
		builder = new AlertDialog.Builder(YYYProductDataActivity.this,
				R.style.Dialog_Transparent); // �ȵõ�������
		Bundle bundle = getIntent().getBundleExtra("BUNDLE");
		layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		if(bundle != null){
			projectInfo = (ProjectInfo) bundle.getSerializable("PROJECT_INFO");
			productInfo = (ProductInfo) bundle.getSerializable("PRODUCT_INFO");
		}
		findViews();
		if(productInfo != null){
			handler.sendEmptyMessage(REQUEST_CURRENT_USER_INVEST_WHAT);
		}
	}
	
	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("��������");
		bottomView = layoutInflater.inflate(R.layout.bottom_button_invest_layout, null);
		investBtn = (Button) bottomView.findViewById(R.id.product_data_activity_bidBtn);
		investBtn.setOnClickListener(this);
		dataGridView = (GridViewWithHeaderAndFooter)findViewById(R.id.product_data_gv);
		dataGridView.addFooterView(bottomView);
		dataGridView.setVisibility(View.VISIBLE);
		if(productInfo != null){
			if("δ����".equals(productInfo.getMoney_status())){
				investBtn.setEnabled(true);
				investBtn.setText("����Ͷ��");
			}else{
				investBtn.setEnabled(false);
				investBtn.setText("Ͷ�ʽ���");
			}
		}
		
		dataGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(YYYProductDataActivity.this,ProductDataDetailsActivity.class);
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
		adapter = new ProductDataAdapter(YYYProductDataActivity.this,layoutInflater);
		dataGridView.setAdapter(adapter);
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
			boolean isLogin = !SettingsManager.getLoginPassword(YYYProductDataActivity.this).isEmpty()
					&& !SettingsManager.getUser(YYYProductDataActivity.this).isEmpty();
			// isLogin = true;// ����
			Intent intent = new Intent();
			// 1������Ƿ��Ѿ���¼
			if (isLogin) {
				// �Ѿ���¼����ת������ҳ��
//				intent.putExtra("PRODUCT_INFO", productInfo);
				checkIsVerify("Ԫ��ӯͶ��");
			} else {
				// δ��¼����ת����¼ҳ��
				intent.setClass(YYYProductDataActivity.this,LoginActivity.class);
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
					intent.setClass(YYYProductDataActivity.this,UserVerifyActivity.class);
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
					intent.setClass(YYYProductDataActivity.this, BindCardActivity.class);
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
		RequestApis.requestIsVerify(YYYProductDataActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				Intent intent = new Intent();
				if(flag){
					//�û��Ѿ�ʵ��,�˴�ֻ�ж���û��ʵ�����ɣ������ж���û�а�
//					checkIsBindCard(type);
					if("���ֱ�Ͷ��".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(YYYProductDataActivity.this, BidXSBActivity.class);
					}else if("VIPͶ��".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(YYYProductDataActivity.this, BidVIPActivity.class);
					}else if("���Ŵ�Ͷ��".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(YYYProductDataActivity.this, BidZXDActivity.class);
					}else if("Ԫ��ӯͶ��".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(YYYProductDataActivity.this, BidYYYActivity.class);
					}
					startActivity(intent);
					investBtn.setEnabled(true);
					finish();
				}else{
					//�û�û��ʵ��
					showMsgDialog(YYYProductDataActivity.this, "ʵ����֤", "����ʵ����֤��");
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
		RequestApis.requestIsBinding(YYYProductDataActivity.this, SettingsManager.getUserId(getApplicationContext()), "����", new OnIsBindingListener() {
			@Override
			public void isBinding(boolean flag, Object object) {
				Intent intent = new Intent();
				if(flag){
					//�û��Ѿ���
					if("���ֱ�Ͷ��".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(YYYProductDataActivity.this, BidXSBActivity.class);
					}else if("VIPͶ��".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(YYYProductDataActivity.this, BidVIPActivity.class);
					}else if("���Ŵ�Ͷ��".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(YYYProductDataActivity.this, BidZXDActivity.class);
					}
					startActivity(intent);
					investBtn.setEnabled(true);
					finish();
				}else{
					//�û���û�а�
					showMsgDialog(YYYProductDataActivity.this, "��", "����˾���֧���������������°󿨣�");
				}
			}
		});
	}
	
	/**
	 * �жϵ�ǰ�û��Ƿ�Ͷ�ʹ�VIP�ñ��
	 * @param investUserId
	 * @param borrowId
	 */
	private void requestYYYCurrentUserInvest(String investUserId,String borrowId){
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		AsyncYYYCurrentUserInvest task = new AsyncYYYCurrentUserInvest(YYYProductDataActivity.this, investUserId, borrowId, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(mLoadingDialog != null && mLoadingDialog.isShowing()){
							mLoadingDialog.dismiss();
						}
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								isInvested = true;
							}else{
								isInvested = false;
							}
							initImgData();
						}else{
							isInvested = false;
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
