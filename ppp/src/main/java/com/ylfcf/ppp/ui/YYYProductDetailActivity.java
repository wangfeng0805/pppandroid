package com.ylfcf.ppp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;

import java.util.ArrayList;
import java.util.List;

/**
 * Ԫ��ӯ�Ĳ�Ʒ����ҳ��
 * @author Mr.liu
 *
 */
public class YYYProductDetailActivity extends BaseActivity implements OnClickListener{
	private static final String className = "YYYProductDetailActivity";
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private ListView cpxqListview;
	private List<YYYElement> yyyElementList = new ArrayList<YYYElement>();
	private String[] eleNameArr = null;
	private String[] eleValueArr = null;
	private LayoutInflater layoutInflater;
	private TextView headerView;
	private View footerView;
	private TextView catBtn;//�鿴Э��
	private Button bidBtn;
	private ProductInfo mProductInfo;
	private AlertDialog.Builder builder = null; // �ȵõ�������
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.yyyproductdetail_activity);
		builder = new AlertDialog.Builder(YYYProductDetailActivity.this,
				R.style.Dialog_Transparent); // �ȵõ�������
		Bundle bundle = getIntent().getBundleExtra("BUNDLE");
		mProductInfo = (ProductInfo) bundle.getSerializable("PRODUCT_INFO");
		layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		initElementDatas();
		findViews();
	}
	
	private void initElementDatas(){
		eleNameArr = getResources().getStringArray(R.array.yyy_productdetail_name);
		eleValueArr = getResources().getStringArray(R.array.yyy_productdetail_value);
		for(int i=0;i<eleNameArr.length;i++){
			YYYElement element = new YYYElement();
			element.setEleName(eleNameArr[i]);
			element.setEleValue(eleValueArr[i]);
			yyyElementList.add(element);
		}
	}
	
	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("��Ʒ����");
		
		headerView = new TextView(YYYProductDetailActivity.this);
		headerView.setText("Ԫ��ӯ��Ʒ˵����");
		headerView.setTextSize(getResources().getDimensionPixelSize(R.dimen.common_measure_6dp));
		headerView.setTextColor(getResources().getColor(R.color.gray));
		headerView.setPadding(0, getResources().getDimensionPixelSize(R.dimen.common_measure_30dp), 0, getResources().getDimensionPixelSize(R.dimen.common_measure_10dp));
		footerView = layoutInflater.inflate(R.layout.yyy_productdetail_footerview, null);
		bidBtn = (Button) footerView.findViewById(R.id.yyy_productdetail_footerview_bidBtn);
		bidBtn.setOnClickListener(this);
		catBtn = (TextView) footerView.findViewById(R.id.yyy_productdetail_footerview_cat);
		catBtn.setOnClickListener(this);
		cpxqListview = (ListView) findViewById(R.id.yyyproductdetail_activity_listview);
		cpxqListview.addHeaderView(headerView);
		cpxqListview.addFooterView(footerView);
		cpxqListview.setAdapter(new ProductElementAdapter(YYYProductDetailActivity.this));
		if(mProductInfo != null){
			if("δ����".equals(mProductInfo.getMoney_status())){
				bidBtn.setEnabled(true);
				bidBtn.setText("����Ͷ��");
			}else{
				bidBtn.setEnabled(false);
				bidBtn.setText("Ͷ�ʽ���");
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.yyy_productdetail_footerview_cat:
			//�鿴Э��
			Intent intent = new Intent(YYYProductDetailActivity.this,CompactActivity.class);
			intent.putExtra("from_where", "yyy");
			startActivity(intent);
			break;
		case R.id.yyy_productdetail_footerview_bidBtn:
			// ��SettingsManager�ж�ȡ���룬���Ϊ����ζ��û�е�¼��
			boolean isLogin = !SettingsManager.getLoginPassword(
					YYYProductDetailActivity.this).isEmpty()
					&& !SettingsManager.getUser(YYYProductDetailActivity.this)
							.isEmpty();
			// isLogin = true;// ����
			Intent intent1 = new Intent();
			// 1������Ƿ��Ѿ���¼
			if (isLogin) {
				// �ж��Ƿ�ʵ����
				checkIsVerify("Ԫ��ӯͶ��"); // �ڱ������ҳ��ֻ�ж��Ƿ�ʵ�������ж���û�а�
			} else {
				// δ��¼����ת����¼ҳ��
				intent1.setClass(YYYProductDetailActivity.this,
						LoginActivity.class);
				startActivity(intent1);
			}
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

	/**
	 * ��֤�û��Ƿ��Ѿ���֤
	 * @param type ����ֵ��,�����֡�
	 */
	private void checkIsVerify(final String type){
		bidBtn.setEnabled(false);
		RequestApis.requestIsVerify(YYYProductDetailActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				Intent intent = new Intent();
				if(flag){
					//�û��Ѿ�ʵ��,�˴�ֻ�ж���û��ʵ�����ɣ������ж���û�а�
					intent.setClass(YYYProductDetailActivity.this, BidYYYActivity.class);
					intent.putExtra("PRODUCT_INFO", mProductInfo);
					startActivity(intent);
					bidBtn.setEnabled(true);
					finish();
				}else{
					//�û�û��ʵ��
					showMsgDialog(YYYProductDetailActivity.this, "ʵ����֤", "����ʵ����֤��");
				}
			}
			@Override
			public void isSetWithdrawPwd(boolean flag, Object object) {
			}
		});
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
					intent.setClass(YYYProductDetailActivity.this,UserVerifyActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("type", "Ԫ��ӯͶ��");
					bundle.putSerializable("PRODUCT_INFO", mProductInfo);
					intent.putExtra("bundle", bundle);
					startActivity(intent);
					bidBtn.setEnabled(true);
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
	
	class ProductElementAdapter extends ArrayAdapter<YYYElement>{
		static final int RESOURCE_ID = R.layout.yxb_element_item;
		YYYElement yyyElement = null;
		public ProductElementAdapter(Context context) {
			super(context, RESOURCE_ID);
		}
		@Override
		public int getCount() {
			return yyyElementList.size();
		}
		@Override
		public YYYElement getItem(int position) {
			return yyyElementList.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			yyyElement = yyyElementList.get(position);
			if(convertView == null){
				convertView = layoutInflater.inflate(RESOURCE_ID, null);
			}
			TextView nameTv = (TextView) convertView.findViewById(R.id.yxb_element_item_name);
			TextView valueTv = (TextView) convertView.findViewById(R.id.yxb_element_item_value);
			nameTv.setText(yyyElement.getEleName());
			valueTv.setText(yyyElement.getEleValue());
			return convertView;
		}
	}

	class YYYElement{
		private String eleName;
		private String eleValue;
		public String getEleName() {
			return eleName;
		}
		public void setEleName(String eleName) {
			this.eleName = eleName;
		}
		public String getEleValue() {
			return eleValue;
		}
		public void setEleValue(String eleValue) {
			this.eleValue = eleValue;
		}
	}
}
