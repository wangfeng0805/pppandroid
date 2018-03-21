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
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Ԫ�ű���Ŀ���ܡ���ƷҪ�ء���������
 * 
 * @author Administrator
 * 
 */
public class YXBProjectIntroActivity extends BaseActivity implements
		OnClickListener {
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private ListView cpysListview;
	String flagStr = null;
	private List<YXBElement> yxbElementList = new ArrayList<YXBElement>();
	private String[] eleNameArr = null;
	private String[] eleValueArr = null;
	private LayoutInflater layoutInflater;
	View lineHor = null;
	private AlertDialog.Builder builder = null; // �ȵõ�������
	Button xmjsBtn,cpysBtn,cjwtBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		flagStr = getIntent().getStringExtra("yxb_project_flag");
		builder = new AlertDialog.Builder(YXBProjectIntroActivity.this); // �ȵõ�������
		layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		lineHor = layoutInflater.inflate(R.layout.line_layout, null);
		if ("yxb_xmjs".equals(flagStr)) {
			// ��Ŀ����
			setContentView(R.layout.yxb_project_intro_layout);
		} else if ("yxb_cpys".equals(flagStr)) {
			// ��ƷҪ��
			setContentView(R.layout.yxb_product_element_layout);
		} else if ("yxb_cjwt".equals(flagStr)) {
			// ��������
			setContentView(R.layout.yxb_common_ques_layout);
		}
		initElementDatas();
		findViews();
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		if ("yxb_xmjs".equals(flagStr)) {
			topTitleTV.setText("��Ŀ����");
			xmjsBtn = (Button) findViewById(R.id.yxb_project_intro_btn);
			xmjsBtn.setOnClickListener(this);
		} else if ("yxb_cjwt".equals(flagStr)) {
			topTitleTV.setText("��������");
			cjwtBtn = (Button) findViewById(R.id.yxb_common_ques_btn);
			cjwtBtn.setOnClickListener(this);
		} else if ("yxb_cpys".equals(flagStr)) {
			topTitleTV.setText("��ƷҪ��");
		}

		if ("yxb_cpys".equals(flagStr)) {
			View footerView = LayoutInflater.from(this).inflate(R.layout.bottom_button_invest_layout, null);
			footerView.setBackgroundColor(getResources().getColor(R.color.white));
			Button cpysBtn = (Button) footerView.findViewById(R.id.product_data_activity_bidBtn);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					getResources().getDimensionPixelSize(R.dimen.common_measure_58dp));
			params.topMargin = getResources().getDimensionPixelSize(R.dimen.common_measure_30dp);
			params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.common_measure_50dp);
			cpysBtn.setLayoutParams(params);
			cpysBtn.setOnClickListener(this);
			cpysListview = (ListView) findViewById(R.id.yxb_product_element_layout_listview);
			cpysListview.addHeaderView(lineHor);
			cpysListview.addFooterView(footerView);
			cpysListview.setAdapter(new ProductElementAdapter(
					YXBProjectIntroActivity.this));
		}
	}

	private void initElementDatas() {
		eleNameArr = getResources().getStringArray(R.array.yxb_element_name);
		eleValueArr = getResources().getStringArray(R.array.yxb_element_value);
		for (int i = 0; i < eleNameArr.length; i++) {
			YXBElement element = new YXBElement();
			element.setEleName(eleNameArr[i]);
			element.setEleValue(eleValueArr[i]);
			yxbElementList.add(element);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.product_data_activity_bidBtn:
		case R.id.yxb_common_ques_btn:
		case R.id.yxb_project_intro_btn:
			// ��SettingsManager�ж�ȡ���룬���Ϊ����ζ��û�е�¼��
			if (Util.isYXBENabled()) {
				boolean isLogin = !SettingsManager.getLoginPassword(
						YXBProjectIntroActivity.this).isEmpty()
						&& !SettingsManager.getUser(
								YXBProjectIntroActivity.this).isEmpty();
				Intent intentYXBBid = new Intent();
				// 1������Ƿ��Ѿ���¼
				if (isLogin) {
					// �Ѿ���¼����ת������ҳ��
					intentYXBBid.setClass(YXBProjectIntroActivity.this,
							BidYXBActivity.class);
				} else {
					// δ��¼����ת����¼ҳ��
					intentYXBBid.setClass(YXBProjectIntroActivity.this,
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
		View contentView = LayoutInflater.from(YXBProjectIntroActivity.this)
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

	class ProductElementAdapter extends ArrayAdapter<YXBElement> {
		static final int RESOURCE_ID = R.layout.yxb_element_item;
		YXBElement yxbElement = null;

		public ProductElementAdapter(Context context) {
			super(context, RESOURCE_ID);
		}

		@Override
		public int getCount() {
			return yxbElementList.size();
		}

		@Override
		public YXBElement getItem(int position) {
			return yxbElementList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			yxbElement = yxbElementList.get(position);
			if (convertView == null) {
				convertView = layoutInflater.inflate(RESOURCE_ID, null);
			}
			TextView nameTv = (TextView) convertView
					.findViewById(R.id.yxb_element_item_name);
			TextView valueTv = (TextView) convertView
					.findViewById(R.id.yxb_element_item_value);
			nameTv.setText(yxbElement.getEleName());
			valueTv.setText(yxbElement.getEleValue());
			return convertView;
		}
	}

	class YXBElement {
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
