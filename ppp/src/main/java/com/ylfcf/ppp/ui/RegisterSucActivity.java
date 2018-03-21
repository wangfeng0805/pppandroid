package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.util.UMengStatistics;

/**
 * ע��ɹ�ҳ��
 * @author Mr.liu
 *
 */
public class RegisterSucActivity extends BaseActivity implements OnClickListener{
	private static final String className = "RegisterSucActivity";
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;

	private ImageView logo;
	private Button verifyBtn;//����ʵ����֤
	private Button catMoneyBtn;//�鿴Ԫ���
	private Button ztcBtn;//����׬���
	private String extensionCode = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register_suc_activity);
		extensionCode = getIntent().getStringExtra("extension_code");
		findViews();
	}

	private void findViews(){
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("ע��ɹ�");

		logo = (ImageView) findViewById(R.id.register_suc_activity_logo);
		verifyBtn = (Button) findViewById(R.id.register_suc_activity_verify_btn);
		verifyBtn.setOnClickListener(this);
		catMoneyBtn = (Button) findViewById(R.id.register_suc_activity_catyuanmoney_btn);
		catMoneyBtn.setOnClickListener(this);
		ztcBtn = (Button) findViewById(R.id.register_suc_activity_layout_ztc_btn);
		ztcBtn.setOnClickListener(this);
		if("".equals(extensionCode) || extensionCode == null){
			logo.setImageResource(R.drawable.register_suc_logo);
		}else{
			logo.setImageResource(R.drawable.register_suc_logo1);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
			case R.id.common_topbar_left_layout:
				finish();
				break;
			case R.id.register_suc_activity_verify_btn:
				//������֤
				intent.setClass(RegisterSucActivity.this,UserVerifyActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("type","ע��ɹ�");
				intent.putExtra("bundle",bundle);
				startActivity(intent);
				finish();
				break;
			case R.id.register_suc_activity_catyuanmoney_btn:
				//�鿴Ԫ���
				intent.setClass(RegisterSucActivity.this,MyYuanMoneyActivity.class);
				startActivity(intent);
				finish();
				break;
			case R.id.register_suc_activity_layout_ztc_btn:
				//����׬���
				intent.setClass(RegisterSucActivity.this,InvitateActivity.class);
				startActivity(intent);
				finish();
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
}
