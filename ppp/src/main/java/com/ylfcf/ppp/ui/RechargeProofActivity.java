package com.ylfcf.ppp.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.URLGenerator;
/**
 * ��ֵƾ֤
 * @author Mr.liu
 *
 */
public class RechargeProofActivity extends BaseActivity implements OnClickListener{
	private static final String className = "RechargeProofActivity";
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private WebView wv;
	private String rechargeId = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.recharge_proof_activity);
		rechargeId = getIntent().getStringExtra("recharge_id");
		findViews();
	}
	
	private void findViews(){
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("��ֵƾ֤");
		wv = (WebView) findViewById(R.id.recharge_proof_activity_wv);
		this.wv.getSettings().setSupportZoom(false);  
        this.wv.getSettings().setJavaScriptEnabled(true);  //֧��js
        this.wv.getSettings().setDomStorageEnabled(true); 
        wv.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				//����URL ����activity����ת
				return true;
			}
			
		});
        wv.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onProgressChanged(WebView view, int newProgress) {	
				if(newProgress == 100 && mLoadingDialog.isShowing()){
					//��ҳ�������
					mLoadingDialog.dismiss();
				}else if(newProgress != 100 && !mLoadingDialog.isShowing() && !isFinishing()){
					//��ҳ������...
					mLoadingDialog.show();
				}
			}
		});
        wv.loadUrl(URLGenerator.RECHARGE_PROOF_URL.replace("rechargeId",rechargeId));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
