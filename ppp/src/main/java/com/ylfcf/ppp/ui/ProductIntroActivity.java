package com.ylfcf.ppp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncXSBIscanbuy;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * ��Ʒ����ҳ��  Ԫ��ӯ���ܡ����ֱ���ܵ�
 * @author Mr.liu
 *
 */
public class ProductIntroActivity extends BaseActivity implements OnClickListener{
	private static final String className = "ProductIntroActivity";
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private WebView webview;
	private String fromWhere;//yyy:Ԫ��ӯ   xsb:���ֱ�
	private String loadURL = "";
	private ProductInfo productInfo;
	private AlertDialog.Builder builder = null; // �ȵõ�������
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.yyyintro_activity);
		builder = new AlertDialog.Builder(ProductIntroActivity.this,
				R.style.Dialog_Transparent); // �ȵõ�������
		Bundle bundle = getIntent().getBundleExtra("BUNDLE");
		if(bundle != null){
			productInfo = (ProductInfo) bundle.getSerializable("PRODUCT_INFO");
			fromWhere = bundle.getString("from_where");
		}
		findView();
	}
	
	private void findView(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		if("xsb".equals(fromWhere)){
			topTitleTV.setText("���ֱ����");
			loadURL = URLGenerator.XSB_XMJS_URL.replace("borrowid", productInfo == null?"borrowid":productInfo.getId());
		}else if("yyy".equals(fromWhere)){
			topTitleTV.setText("Ԫ��ӯ����");
			loadURL = URLGenerator.YYY_XMJS_URL;
		}else if("wdy".equals(fromWhere)){
			//�ȶ�Ӯ��нӯ�ƻ�
			topTitleTV.setText("нӯ�ƻ�����");
			loadURL = URLGenerator.XYJH_XMJS_URL.replace("borrowid", productInfo == null?"borrowid":productInfo.getId());
		}
		
		webview = (WebView) findViewById(R.id.yyyintro_activity_wv);
		this.webview.getSettings().setSupportZoom(false);  
        this.webview.getSettings().setJavaScriptEnabled(true);  //֧��js
        this.webview.getSettings().setDomStorageEnabled(true); 
		webview.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				//����URL ����activity����ת
				boolean isLogin = !SettingsManager.getLoginPassword(ProductIntroActivity.this).isEmpty()
						&& !SettingsManager.getUser(ProductIntroActivity.this).isEmpty();
				Intent intent = new Intent();
				if(isLogin){
					if("xsb".equals(fromWhere) && (url.contains("/home/borrow/borrowDetail/id/") ||
						url.contains("/home/borrow/borrowInvest/id/"))){
						//��ת�����ֱ��Ͷ��ҳ��
						checkXSB();
					}else if("yyy".equals(fromWhere)){
						//��ת��Ԫ��ӯ��Ͷ��ҳ��
						intent.setClass(ProductIntroActivity.this,
								BidYYYActivity.class);
						intent.putExtra("PRODUCT_INFO", productInfo);
						startActivity(intent);
					}else if("wdy".equals(fromWhere) && url.contains("/home/wdy/wdyinvest")){
						//��ת���ȶ�ӯͶ��ҳ��
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductIntroActivity.this, BidWDYActivity.class);
						startActivity(intent);
					}
				}else{
					intent.setClass(ProductIntroActivity.this,LoginActivity.class);
					startActivity(intent);
				}
				return true;
			}
		});
		webview.setWebChromeClient(new WebChromeClient(){
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
		webview.loadUrl(loadURL);
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

	private void checkXSB(){
		boolean isLogin = !SettingsManager.getLoginPassword(ProductIntroActivity.this).isEmpty()
				&& !SettingsManager.getUser(ProductIntroActivity.this).isEmpty();
		// isLogin = true;// ����
		Intent intent = new Intent();
		// 1������Ƿ��Ѿ���¼
		if (isLogin) {
			//�ж��Ƿ�ʵ����
			isCanbuyXSB(SettingsManager.getUserId(getApplicationContext()), productInfo.getId());
		} else {
			// δ��¼����ת����¼ҳ��
			intent.setClass(ProductIntroActivity.this,LoginActivity.class);
			startActivity(intent);
			finish();
		}
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
					intent.setClass(ProductIntroActivity.this,UserVerifyActivity.class);
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
					intent.setClass(ProductIntroActivity.this, BindCardActivity.class);
					startActivity(intent);
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
	 * �ж��Ƿ���Թ������ֱ�
	 * @param userId
	 * @param borrowId
	 */
	private void isCanbuyXSB(String userId,String borrowId){
		AsyncXSBIscanbuy task = new AsyncXSBIscanbuy(ProductIntroActivity.this, userId, borrowId, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//�û����Թ������ֱ�
								Intent intent = new Intent();
								intent.putExtra("PRODUCT_INFO", productInfo);
								intent.setClass(ProductIntroActivity.this, BidXSBActivity.class);
								startActivity(intent);
							}else if(resultCode == 1001){
								//���Ƚ���ʵ��
								showMsgDialog(ProductIntroActivity.this, "ʵ����֤", "����ʵ����֤��");
							}else if(resultCode == 1002){
								//���Ƚ��а�
								boolean isNewUser = SettingsManager.checkIsNewUser(SettingsManager.getUserRegTime(getApplicationContext()));
								if(isNewUser){
									showMsgDialog(ProductIntroActivity.this, "��", "�����Ȱ󿨣�");
								}else{
									showMsgDialog(ProductIntroActivity.this, "��", "����˾���֧���������������°󿨣�");
								}
							}else{
								showMsgDialog(ProductIntroActivity.this, "���ܹ������ֱ�", "�˲�Ʒ���״ι����û�ר��");
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
