package com.ylfcf.ppp.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
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
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.smtt.sdk.CookieSyncManager;
import com.umeng.socialize.UMShareAPI;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.entity.ShareInfo;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.util.Constants.TopicType;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.SimpleCrypto;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.YLFLogger;
import com.ylfcf.ppp.view.ActiveNotLoginPopwindow;
import com.ylfcf.ppp.view.InvitateFriendsPopupwindow;

import java.net.URLEncoder;

/**
 * ר��ҳ
 * @author Administrator
 *
 */
public class BannerTopicActivity extends BaseActivity implements OnClickListener{
	private static final String className = "BannerTopicActivity";
	private static final String LOTTERY_URL = "http://wap.ylfcf.com/home/index/lottery.html";//	��ת�̵Ļҳ��
	private static final int POPUPWINDOW_START_WHAT = 2712;
	private static final int DOWNLOAD_PIC_WHAT = 2713;

	private LinearLayout mainLayout;
	private LinearLayout topLeftBtn;
	private WebView webview;
	private TextView topTitleTV;
	private BannerInfo banner;
	private String topicType = "";//ר������֣����ݺ�̨��Լ���ġ�
	private RelativeLayout topLayout;
	private String userid;
	private boolean isFirstLoad = true;

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case POPUPWINDOW_START_WHAT:
					ShareInfo info = (ShareInfo)msg.obj;
					if(userid == null || "".equals(userid)){
						//δ��¼
						showNotLoginPromptWindow(info);
					}else{
						//�ѵ�¼
						showFriendsSharedWindow(info.getTitle(),info.getContent(),info.getActiveURL(),
								info.getSharePicURL());
					}
					break;
				case DOWNLOAD_PIC_WHAT:

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
		setContentView(R.layout.banner_topic_activity);
		banner = (BannerInfo) getIntent().getSerializableExtra("BannerInfo");
		if(banner != null){
			topicType = banner.getArticle_id();
		}
		findViews();
		userid = SettingsManager.getUserId(BannerTopicActivity.this);
		if(userid == null || "".equals(userid)){
			loadURL();
		}
		YLFLogger.d("time onCreate():"+System.currentTimeMillis());
	}

	@Override
	protected void onStart() {
		super.onStart();
		YLFLogger.d("time onStart():"+System.currentTimeMillis());
		YLFLogger.d("activity"+"BannerTopicActivity-------onStart()");
	}

	@Override
	protected void onPause() {
		super.onPause();
		UMengStatistics.statisticsOnPageEnd(className);//����ͳ��ҳ����ת
		UMengStatistics.statisticsPause(this);//����ͳ��ʱ��
		YLFLogger.d("activity"+"BannerTopicActivity-------onPause()");
	}

	@Override
	protected void onStop() {
		super.onStop();
		YLFLogger.d("activity"+"BannerTopicActivity-------onStop()");
	}

	@Override
	protected void onResume() {
		super.onResume();
		YLFLogger.d("time onResume():"+System.currentTimeMillis());
		UMengStatistics.statisticsOnPageStart(className);//����ͳ��ҳ����ת
		UMengStatistics.statisticsResume(this);//����ͳ��ʱ��
		userid = SettingsManager.getUserId(getApplicationContext());
		if(userid != null && !"".equals(userid) && isFirstLoad){
			loadURL();
		}else if(!isFirstLoad && webview != null && banner != null) {
			if(banner.getLink_url().contains("mdFestivalLottery")){
				webview.reload();
			}else{
			}
		}
		YLFLogger.d("activity"+"BannerTopicActivity-------OnResume()"+"----------isFirstLoad:"+isFirstLoad+"--------------userid:"+userid);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		YLFLogger.d("activity"+"BannerTopicActivity-------onRestart()");
	}

	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		YLFLogger.d("activity"+"BannerTopicActivity-------onUserLeaveHint()");
	}

	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topLayout = (RelativeLayout) findViewById(R.id.banner_topic_activity_toplayout);
		mainLayout = (LinearLayout) findViewById(R.id.banner_topic_activity_mainlayout);
		webview = (WebView) findViewById(R.id.banner_topic_activity_webview);
		if(TopicType.CHONGZHISONG.equals(topicType)){
			//��ֵ�͵Ļ
			topTitleTV.setText("��ֵ��");
			topLayout.setBackgroundColor(getResources().getColor(R.color.topic_chongzhisong_topcolor));
		}else if(TopicType.ZHUCESONG.equals(topicType)){
			//ע����
			topTitleTV.setText("ע����");
		}else if(TopicType.JIAXI.equals(topicType)){
			topTitleTV.setText("��Ϣˬ����");
		}else if(TopicType.TOUZIFANLI.equals(topicType)){
			topTitleTV.setText("Ͷ�ʷ���");
		}else if(TopicType.XINGYUNZHUANPAN.equals(topicType)){
			topTitleTV.setText("����ת��");
		}else if(TopicType.YYY_JX.equals(topicType)){
			topLayout.setBackgroundColor(getResources().getColor(R.color.topic_yyyjiaxi_topcolor));
			topTitleTV.setText("����Ͷ�� �ӼӼ�Ϣ");
		}else if(TopicType.TUIGUANGYUAN.equals(topicType)){
			topTitleTV.setText("�ƹ�Աר������");
		}else if(TopicType.FRIENDS_CIRCLE.equals(topicType)){
			topTitleTV.setText("��ǿ����Ȧ");
		}else{
			topTitleTV.setText("ר������");
		}
		webview.getSettings().setSupportZoom(false);
        webview.getSettings().setJavaScriptEnabled(true);  //֧��js
		webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//����js����
        webview.getSettings().setDomStorageEnabled(true);
		webview.addJavascriptInterface(new JavascriptAndroidInterface(this),"android");
		webview.setLayerType(View.LAYER_TYPE_SOFTWARE,null);//����
		//android5.0����Ĭ�ϲ�֧��mix content,���Դ˴�����
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
		}
		webview.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				//����URL ����activity����ת
				if(TopicType.CHONGZHISONG.equals(topicType)){
					//��ֵ��ר��
					chongzhisong(url);
				}else if(TopicType.ZHUCESONG.equals(topicType)){
					//ע����
					zhucesong(url);
				}else if(TopicType.XINGYUNZHUANPAN.equals(topicType)){
					xingyunzhuanpan(url);
				}else{
					loadURL(url);
				}
				return true;
			}

			@Override
			public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
				sslErrorHandler.proceed();
			}
		});
		webview.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if(newProgress == 100 && mLoadingDialog.isShowing()){
					//��ҳ�������
					mLoadingDialog.dismiss();
					YLFLogger.d("webview������ɣ�"+String.valueOf(newProgress));
				}else if(newProgress != 100 && !mLoadingDialog.isShowing()){
					//��ҳ������...,Activityû�����ٵ�ʱ����ʾ
					if(!isFinishing()){
						mLoadingDialog.show();
						YLFLogger.d("webview�����У�"+String.valueOf(newProgress));
					}
				}
			}

			@Override
			public void onHideCustomView() {
				super.onHideCustomView();
				YLFLogger.d("activity"+"BannerTopicActivity-------onHideCustomView()");
			}

			@Override
			public void onShowCustomView(View view, CustomViewCallback callback) {
				super.onShowCustomView(view, callback);
				YLFLogger.d("activity"+"BannerTopicActivity-------onShowCustomView()");
			}

			@Override
			public void onCloseWindow(WebView window) {
				super.onCloseWindow(window);
				YLFLogger.d("activity"+"BannerTopicActivity-------onCloseWindow()");
			}

			@Override
			public void onRequestFocus(WebView view) {
				super.onRequestFocus(view);
				YLFLogger.d("activity"+"BannerTopicActivity-------onRequestFocus()");
			}

		});
		webview.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				return true;
			}
		});
	}

	/**
	 * ����ʱδ��¼�ĵ���
	 */
	private void showNotLoginPromptWindow(final ShareInfo info){
		View popView = LayoutInflater.from(this).inflate(R.layout.active_notlogin_window, null);
		int[] screen = SettingsManager.getScreenDispaly(BannerTopicActivity.this);
		int width = screen[0]*4/5;
		int height = screen[1]*1/4;
		ActiveNotLoginPopwindow popwindow = new ActiveNotLoginPopwindow(BannerTopicActivity.this, popView,
				width, height, new NotLoginWindowBtnsListener() {
			@Override
			public void leftBtnBack() {
				//��ఴť  �ݲ���¼
				showFriendsSharedWindow(info.getTitle(),info.getContent(),info.getActiveURL(), info.getSharePicURL());
			}

			@Override
			public void rightBtnBack() {
				//ȥ��¼
				Intent intent = new Intent(BannerTopicActivity.this,LoginActivity.class);
				startActivity(intent);
			}
		});
		popwindow.show(mainLayout);
	}

	private void loadURL(){
		if(banner != null){
			String userIdCrypto = "";
			if(userid != null && !"".equals(userid)){
				//�ѵ�¼
				try{
					userIdCrypto = URLEncoder.encode(SimpleCrypto.encryptAES(userid,"yuanlifanglicai1"),"utf-8");
					if(banner.getLink_url().endsWith("#app")){
						webview.loadUrl(banner.getLink_url().replace("#app","?app_socket="+userIdCrypto+"#app"));
					}else{
						webview.loadUrl(banner.getLink_url() + "?app_socket=" + userIdCrypto);
					}
					isFirstLoad = false;
					YLFLogger.d("����ǰ��������������������������"+userid);
					YLFLogger.d("���ܺ󣺡�����������������������"+SimpleCrypto.encryptAES(userid,"yuanlifanglicai1"));
					YLFLogger.d("6�·ݻ���ӣ���������������������������������������������������������"+
							banner.getLink_url().replace("#app","?app_socket="+userIdCrypto+"#app"));
				}catch (Exception e){
				}
			}else{
				webview.loadUrl(banner.getLink_url());
				YLFLogger.d("6�·ݻ���ӣ���������������������������������������������������������"+
						banner.getLink_url());
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		CookieSyncManager.createInstance(BannerTopicActivity.this);
		CookieManager.getInstance().removeAllCookie();
		handler.removeCallbacksAndMessages(null);
		UMShareAPI.get(this).release();//���˷����ڴ�й¶����
		//���ǲ�����Ч�Ľ��webview�ڴ�й¶�����⣬�ռ��취�Ǹ�webview��һ�����̡�������ʱ��ֱ��ɱ���ý���
		webview.removeAllViews();
		webview.destroy();
		webview = null;
	}

	public class JavascriptAndroidInterface{
		Context mContext;
		public JavascriptAndroidInterface(Context context){
			mContext = context;
		}
		@JavascriptInterface
		public void share(final String title,final String content,
						  final String activeURL,final String picURL){
			ShareInfo shareInfo = new ShareInfo();
			shareInfo.setSharePicURL(picURL);
			shareInfo.setContent(content);
			shareInfo.setTitle(title);
			shareInfo.setActiveURL(activeURL);
			Message msg = handler.obtainMessage(POPUPWINDOW_START_WHAT);
			msg.obj = shareInfo;
			handler.sendMessageDelayed(msg,300L);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * �����������ʾ��
	 */
	private void showFriendsSharedWindow(String title,String content,String activeURL,String picURL) {
		if(mLoadingDialog != null && mLoadingDialog.isShowing()){
			mLoadingDialog.dismiss();
		}
		View popView = LayoutInflater.from(this).inflate(R.layout.invitate_friends_popupwindow, null);
		int[] screen = SettingsManager.getScreenDispaly(BannerTopicActivity.this);
		int width = screen[0];
		int height = screen[1] / 5 * 2;
		InvitateFriendsPopupwindow popwindow = new InvitateFriendsPopupwindow(BannerTopicActivity.this,
				popView, width, height);
		ShareInfo info = new ShareInfo();
		info.setTitle(title);
		info.setContent(content);
		info.setActiveURL(activeURL);
		info.setSharePicURL(picURL);
		popwindow.show(mainLayout, activeURL,"",info);
	}

	/**
	 * ����URL
	 * @param url
	 */
	private void loadURL(String url){
		if(url == null){
			return;
		}
		YLFLogger.d("���ص���url:"+url);
		if(url.contains("/home/yyy/yyyDetail")){
			//Ԫ��ӯ��Ϣ��ת  Ԫ��ӯ������ҳ��
			Intent intent = new Intent(BannerTopicActivity.this,BorrowDetailYYYActivity.class);
			startActivity(intent);
		}else if(url.contains("/home/borrow/borrowlist") || url.contains("/home/borrow/borrowList")){
			//��ת�����Ŵ����б�ҳ��
			Intent intent = new Intent(BannerTopicActivity.this,BorrowListZXDActivity.class);
			startActivity(intent);
		}else if(url.contains("/home/vip/borrowlist") || url.contains("/home/vip/borrowList")){
			//��ת��vip���б�ҳ��
			Intent intent = new Intent(BannerTopicActivity.this,BorrowListVIPActivity.class);
			startActivity(intent);
		}else if(url.contains("/home/borrow/borrowDetail") || url.contains("/home/borrow/borrowdetail")){
			//���ֱ�����
			Intent intent = new Intent(BannerTopicActivity.this,BorrowDetailXSBActivity.class);
			startActivity(intent);
		}else if(url.contains("/home/index/register")){
			//ע��ҳ��
			Intent intent = new Intent(BannerTopicActivity.this,RegisteActivity.class);
			startActivity(intent);
		}else if(url.contains("/home/index/login")){
			//��¼ҳ��
			Intent intent = new Intent(BannerTopicActivity.this,LoginActivity.class);
			startActivity(intent);
		}else if(url.contains("/home/index/vipregister") || url.contains("/home/index/vipRegister")){
			//vip�û�ע��ҳ��
			Intent intent = new Intent(BannerTopicActivity.this,RegisterVIPActivity.class);
			startActivity(intent);
		}else if(url.contains("/home/promotion/hdInvite") || url.contains("/home/promotion/hdinvite") ||
				url.contains("/promotion/hdInvite") || url.contains("/home/company/coInvite")){
			//��ת���������ҳ�棬�����ж���û�е�¼
			shared();
		}else if(url.contains("/home/index/promoter")){
			//�붮ʲô���Ƽ���
			Intent intentBanner = new Intent(BannerTopicActivity.this,BannerTopicActivity.class);
			BannerInfo bannerInfo = new BannerInfo();
			bannerInfo.setArticle_id(TopicType.TUIGUANGYUAN);
			bannerInfo.setLink_url(URLGenerator.PROMOTER_URL);
			intentBanner.putExtra("BannerInfo", bannerInfo);
			startActivity(intentBanner);
			finish();
		}else if(url.contains("/home/borrow")){
			//��ҳ���Ͷ���б�ҳ��
			SettingsManager.setMainProductListFlag(getApplicationContext(), true);
			mApp.finishAllActivityExceptMain();
		}else if(url.contains("/home/seckill/seckilldetail")){
			//��ת���������ҳ��
			Intent intentMBDetail = new Intent(BannerTopicActivity.this,BorrowDetailXSMBActivity.class);
			startActivity(intentMBDetail);
			finish();
		}else if(url.contains("/home/index/fljh") || url.contains("/home/index/yhfl")){
			//��ȡ�û�����
			Intent intent = new Intent(BannerTopicActivity.this,PrizeRegionTempActivity.class);
			startActivity(intent);
			finish();
		}else if(url.contains("/home/wdy/wdydetail")){
			//нӯ�ƻ�����ҳ��
			Intent intent = new Intent(BannerTopicActivity.this,BorrowDetailWDYActivity.class);
			startActivity(intent);
			finish();
		}else if(url.contains("/home/index/hd")){
			Intent intent = new Intent(BannerTopicActivity.this,ActivitysRegionActivity.class);
			startActivity(intent);
		}else if(url.contains("/home/member/interestCoupon")){
			//�ҵļ�Ϣȯ
			Intent intent = new Intent(BannerTopicActivity.this,MyJXQActivity.class);
			startActivity(intent);
		}else if(url.contains("/home/member/mygift")){
			//�ҵ���Ʒ
			Intent intent = new Intent(BannerTopicActivity.this,MyGiftsActivity.class);
			startActivity(intent);
		}else if(url.contains("/home/member/redbag")){
			//�ҵĺ��
			Intent intent = new Intent(BannerTopicActivity.this,MyHongbaoActivity.class);
			startActivity(intent);
		}else if(url.contains("/home/member/redEnvelope")){
			//�ҵ�Ԫ���
			Intent intent = new Intent(BannerTopicActivity.this,MyYuanMoneyActivity.class);
			startActivity(intent);
		}else if(url.contains("/home/index/active_july_2017") || url.contains("/home/Pvip/orderPro")){
			//2017��7�·ݻ����ɳ齱�����ˢ��,\˽������ˢ��
			webview.reload();
		}else if(url.contains("/home/promotion/hdReward")){
			//���ѽ���ҳ��
			Intent intent = new Intent(BannerTopicActivity.this,MyInvitationActivity.class);
			startActivity(intent);
		}else if(url.contains("/home/Ygzx/yjyOrderPro")){
			//Ա��ר����ƷԤԼ�ɹ���ˢ��ҳ��
			webview.reload();
		}else if(url.contains("/home/member/moneyManage")){
			//�ʽ���ϸ
			Intent intent = new Intent(BannerTopicActivity.this,FundsDetailsActivity.class);
			startActivity(intent);
		}else if(url.contains("/home/Index/yuanGoldCoin")){
			//Ԫ���ʹ�ù���
			Intent intentBanner = new Intent(this,BannerSimpleTopicActivity.class);
			BannerInfo bannerInfo = new BannerInfo();
			bannerInfo.setLink_url(URLGenerator.YUANMONEY_RULE_URL);
			intentBanner.putExtra("BannerInfo", bannerInfo);
			startActivity(intentBanner);
		}else if(url.contains("/home/index/increaseInterest")){
			//��Ϣȯʹ�ù���
			Intent intentBanner = new Intent(this,BannerSimpleTopicActivity.class);
			BannerInfo bannerInfo = new BannerInfo();
			bannerInfo.setArticle_id("120");
			bannerInfo.setLink_url(URLGenerator.JXQ_RULE_URL);
			intentBanner.putExtra("BannerInfo", bannerInfo);
			startActivity(intentBanner);
		}else if(url.contains("/home/index/redbag")){
			//���ʹ�ù���
			Intent intentBanner = new Intent(this,BannerSimpleTopicActivity.class);
			BannerInfo bannerInfo = new BannerInfo();
			bannerInfo.setArticle_id("80");
			bannerInfo.setLink_url(URLGenerator.REDBAG_RULE_URL);
			intentBanner.putExtra("BannerInfo", bannerInfo);
			startActivity(intentBanner);
		}else{
			//����������°汾
//			Util.toastLong(BannerTopicActivity.this, url.toString());
		}
	}
	
	private void shared(){
		String userId = SettingsManager.getUserId(getApplicationContext());
		if(userId != null && !"".equals(userId)){
			//�ѵ�¼
			if(SettingsManager.isPersonalUser(getApplicationContext())){
				//�����û�
				if("�����Ƽ�".equals(banner.getFrom_where()) || "��������".equals(banner.getFrom_where())){
					Intent intent = new Intent();
					setResult(200,intent);
					finish();
				}else{
					checkIsVerify("�����н�");
				}
			}else{
				//��ҵ�û�
				Intent intent = new Intent(BannerTopicActivity.this,InvitateActivity.class);
				intent.putExtra("is_verify", true);
				startActivity(intent);
			}
		}else{
			//δ��¼
			Intent intent = new Intent(BannerTopicActivity.this,LoginActivity.class);
			intent.putExtra("FLAG", "from_mainfragment_activity_shared");
			startActivity(intent);
		}
	}
	
	/**
	 * ��ֵ��
	 * @param url
	 */
	private  void chongzhisong(String url){
		if(url != null && url.contains("/home/recharge/")){
			String userId = SettingsManager.getUserId(getApplicationContext());
			if(userId == null || "".equals(userId)){
				//δ��½
				Intent intent = new Intent(BannerTopicActivity.this,LoginActivity.class);
				startActivity(intent);
			}else{
				SettingsManager.setMainAccountFlag(getApplicationContext(), true);
				finish();
			}
		}
	}
	
	/**
	 * ע����
	 * @param url
	 */
	private void zhucesong(String url){
		if(url != null && url.contains("/home/borrow")){
			//�����鿴
			SettingsManager.setMainProductListFlag(getApplicationContext(), true);
			finish();
		}else if(url != null && url.contains("/home/index/register")){
			//���ע��
			String userId = SettingsManager.getUserId(getApplicationContext());
			if(userId == null || "".equals(userId)){
				//δ��½
				Intent intent = new Intent(BannerTopicActivity.this,RegisteActivity.class);
				startActivity(intent);
				finish();
			}else{
				SettingsManager.setMainAccountFlag(getApplicationContext(), true);
				finish();
			}
		}
	}
	
	/**
	 * ����ת��
	 * @param url
	 */
	private void xingyunzhuanpan(String url){
		//��ת�����
		if(url != null && url.contains("/home/index/login")){
			showPromptDialog();
		}else{
			intentToLotteryBrowser();
		}
	}
	
	private void intentToLotteryBrowser(){
		Uri uri = Uri.parse(LOTTERY_URL);  
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
        startActivity(intent);  
	}
	
	private void showPromptDialog(){
		View contentView = LayoutInflater.from(BannerTopicActivity.this).inflate(R.layout.banner_prompt_dialog, null);
		final Button sureBtn = (Button) contentView.findViewById(R.id.banner_prompt_dialog_sure_btn);
		final Button cancelBtn = (Button) contentView.findViewById(R.id.banner_prompt_dialog_cancel_btn);
		final TextView contentText = (TextView) contentView.findViewById(R.id.banner_prompt_dialog_content_text);
		sureBtn.setText("ȥ�μ�");
		contentText.setText("ʹ��������򿪻ҳ��");
		AlertDialog.Builder builder=new AlertDialog.Builder(BannerTopicActivity.this, R.style.Dialog_Transparent);  //�ȵõ�������  
        builder.setView(contentView);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        sureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				intentToLotteryBrowser();
			}
		});
        cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
        //��������������ˣ���������ʾ����  
        dialog.show();  
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth()*6/7;
        dialog.getWindow().setAttributes(lp);
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
	 * ��֤�û��Ƿ��Ѿ���֤
	 * @param type ����ֵ��,�����֡�,"�����н�"
	 */
	private void checkIsVerify(final String type){
		RequestApis.requestIsVerify(BannerTopicActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(flag){
					//�û��Ѿ�ʵ��
					Intent intent = new Intent(BannerTopicActivity.this,InvitateActivity.class);
					intent.putExtra("is_verify", true);
					startActivity(intent);
				}else{
					//�û�û��ʵ��
					Intent intent = new Intent(BannerTopicActivity.this,UserVerifyActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("type", type);
					intent.putExtra("bundle", bundle);
					startActivity(intent);
				}
			}

			@Override
			public void isSetWithdrawPwd(boolean flag, Object object) {
			}
		});
	}

	/**
	 * δ��¼������ť����
	 */
	public interface NotLoginWindowBtnsListener{
		void leftBtnBack();
		void rightBtnBack();
	}
}
