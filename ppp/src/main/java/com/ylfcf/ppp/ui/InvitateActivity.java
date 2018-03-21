package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.umeng.socialize.UMShareAPI;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncExtensionNewPageInfo;
import com.ylfcf.ppp.async.AsyncUserSelectOne;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.ExtensionNewPageInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnGetUserInfoByPhone;
import com.ylfcf.ppp.util.Constants.TopicType;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.view.InvitateFriendsPopupwindow;

import java.util.Hashtable;

/**
 * �����н�
 * �û��Ѿ�ʵ����֮��ſ���������ѣ�δʵ���Ļ������û�����ʵ����֤��
 * @author jianbing
 * 
 */
public class InvitateActivity extends BaseActivity implements OnClickListener {
	private static final String className = "InvitateActivity";
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;

	private LinearLayout mainLayout;

	private ImageView qrCodeImage;// ��ά��
	private Button invitateBtn;
	private Button compUserFriendsBtn;//����Ͷ���б�
	private Button bottomBtn;//�ײ���ť
	private Button catFriendsBtn;//�鿴��������
	private TextView knowMoreTV;//�˽����
	private ImageView wayLogo1,wayLogo2,wayLogo3;
    private TextView way1Content;
	private LinearLayout btnsLayout,tipsLayout;

	private int page = 0;
	private int pageSize = 20;
	private ExtensionNewPageInfo pageInfo;
	private String promotedURL = null;
	private int QR_WIDTH = 0;
	private int QR_HEIGHT = 0;
	private UserInfo userInfo;
	private boolean isVerify = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.invitate_activity);
		isVerify = getIntent().getBooleanExtra("is_verify", false);
		findViews(isVerify);
		QR_WIDTH = getResources().getDimensionPixelSize(R.dimen.common_measure_170dp);
		QR_HEIGHT = getResources().getDimensionPixelSize(R.dimen.common_measure_170dp);
		requestExtension(SettingsManager.getUserId(getApplicationContext()));
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				requestUserInfo(SettingsManager.getUserId(getApplicationContext()),"",isVerify);
			}
		}, 300L);
	}

	private void findViews(boolean flag) {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("�����Ƽ�");

		mainLayout = (LinearLayout) findViewById(R.id.invitate_activity_main_layout);
		qrCodeImage = (ImageView) findViewById(R.id.invitate_activity_qrcode);
		qrCodeImage.setOnClickListener(this);
		invitateBtn = (Button) findViewById(R.id.invitate_activity_btn);
		invitateBtn.setOnClickListener(this);
		compUserFriendsBtn = (Button) findViewById(R.id.invitate_activity_yqy_rewardlistbtn);
		compUserFriendsBtn.setOnClickListener(this);
		if(SettingsManager.isCompanyUser(getApplicationContext())){
			compUserFriendsBtn.setVisibility(View.VISIBLE);
		}else{
			compUserFriendsBtn.setVisibility(View.GONE);
		}
		knowMoreTV = (TextView) findViewById(R.id.invitate_activity_know_more);
		knowMoreTV.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //�»���
		knowMoreTV.getPaint().setAntiAlias(true);//�����
		knowMoreTV.setOnClickListener(this);
		bottomBtn = (Button) findViewById(R.id.invitate_activity_btn_bottom);
		bottomBtn.setOnClickListener(this);
		catFriendsBtn = (Button) findViewById(R.id.invitate_activity_btn_bottom_catfriends);
		catFriendsBtn.setOnClickListener(this);
        way1Content = (TextView) findViewById(R.id.invitate_activity_way_one_content);
        SpannableStringBuilder builder = new SpannableStringBuilder(way1Content.getText().toString());
        ForegroundColorSpan blueSpan = new ForegroundColorSpan(getResources().getColor(R.color.common_topbar_bg_color));
        builder.setSpan(blueSpan, 29, 34, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        way1Content.setText(builder);

        wayLogo1 = (ImageView) findViewById(R.id.invitate_activity_way_one_logo);
		wayLogo2 = (ImageView) findViewById(R.id.invitate_activity_way_two_logo);
		wayLogo3 = (ImageView) findViewById(R.id.invitate_activity_way_three_logo);
		if(flag){
			//��ʵ��
			wayLogo1.setVisibility(View.GONE);
			wayLogo2.setVisibility(View.GONE);
			wayLogo3.setVisibility(View.GONE);
			catFriendsBtn.setVisibility(View.VISIBLE);
			invitateBtn.setEnabled(true);
			bottomBtn.setText("�鿴�Ƽ�����");
			bottomBtn.setTextColor(getResources().getColor(R.color.common_topbar_bg_color));
			bottomBtn.setBackgroundResource(R.drawable.style_rect_fillet_blue);
		}else{
			//δʵ��
			wayLogo1.setVisibility(View.VISIBLE);
			wayLogo2.setVisibility(View.VISIBLE);
			wayLogo3.setVisibility(View.VISIBLE);
			catFriendsBtn.setVisibility(View.GONE);
			invitateBtn.setEnabled(false);
			bottomBtn.setText("���ʵ����֤���������������Ƽ���ʽ");
			bottomBtn.setTextColor(getResources().getColor(R.color.white));
			bottomBtn.setBackgroundResource(R.drawable.blue_fillet_btn_selector);
		}

		btnsLayout = (LinearLayout) findViewById(R.id.invitate_activity_btns_layout);
		tipsLayout = (LinearLayout) findViewById(R.id.invitate_activity_tips_layout);
		if(SettingsManager.isCompanyUser(getApplicationContext())){
			btnsLayout.setVisibility(View.GONE);
			tipsLayout.setVisibility(View.GONE);
		}else{
			btnsLayout.setVisibility(View.VISIBLE);
			tipsLayout.setVisibility(View.VISIBLE);
		}
	}

	private void initQRCode(String url) {
		createQRImage(url);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.invitate_activity_btn:
			showFriendsInvitaWindow();
			break;
		case R.id.invitate_activity_btn_bottom:
			if(isVerify){
				//��ʵ��
				Intent intent = new Intent(InvitateActivity.this,
					MyInvitationActivity.class);
				intent.putExtra("ExtensionPageInfo", pageInfo);
				startActivity(intent);
			}else{
				Intent intentVerify = new Intent(InvitateActivity.this,UserVerifyActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("type", "�����н�");
				intentVerify.putExtra("bundle", bundle);
				startActivity(intentVerify);
			}
			break;
		case R.id.invitate_activity_qrcode:
			showBigEWM();
			break;
		case R.id.invitate_activity_know_more:
			//��ת���ƹ���ר��ҳ��
			Intent intentBanner = new Intent(InvitateActivity.this,BannerTopicActivity.class);
			BannerInfo bannerInfo = new BannerInfo();
			bannerInfo.setArticle_id(TopicType.TUIGUANGYUAN);
			bannerInfo.setLink_url(URLGenerator.PROMOTER_URL);
			bannerInfo.setFrom_where("�����Ƽ�");
			intentBanner.putExtra("BannerInfo", bannerInfo);
			startActivity(intentBanner);
			break;
		case R.id.invitate_activity_btn_bottom_catfriends:
			Intent intentFriends = new Intent(InvitateActivity.this,MyFriendsActivity.class);
			startActivity(intentFriends);
			break;
		case R.id.invitate_activity_yqy_rewardlistbtn:
			//����Ͷ��
			Intent intentComp = new Intent(InvitateActivity.this,CompUserFriendsActivity.class);
			startActivity(intentComp);
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
		UMShareAPI.get(this).release();//���˷����ڴ�й¶�Ĵ���
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get(this).onActivityResult( requestCode, resultCode, data);
	}

	/**
	 * ������ʾ��
	 */
	private void showFriendsInvitaWindow() {
		View popView = LayoutInflater.from(this).inflate(
				R.layout.invitate_friends_popupwindow, null);
		int[] screen = SettingsManager.getScreenDispaly(InvitateActivity.this);
		int width = screen[0];
		int height = screen[1] / 5 * 2;
		InvitateFriendsPopupwindow popwindow = new InvitateFriendsPopupwindow(InvitateActivity.this,
				popView, width, height);
		popwindow.show(mainLayout,promotedURL,"�����н�",null);
	}

	/**
	 * ȫ����ʾ��ά��
	 */
	private void showBigEWM(){
		if(!isVerify){
			return;
		}
		View contentView = LayoutInflater.from(InvitateActivity.this).inflate(R.layout.yqyj_ewm_dialog, null);
		ImageView img = (ImageView) contentView.findViewById(R.id.yqyj_ewm_img);
		img.setImageBitmap((Bitmap)qrCodeImage.getTag());
		AlertDialog.Builder builder=new AlertDialog.Builder(InvitateActivity.this, R.style.Dialog_Transparent);  //�ȵõ�������  
        builder.setView(contentView);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        //��������������ˣ���������ʾ����  
        dialog.show();  
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth()*6/7;
        lp.height = display.getWidth()*6/7;
        dialog.getWindow().setAttributes(lp);
	}
	
	public void createQRImage(String url) {
		try {
			// �ж�URL�Ϸ���
			if (url == null || "".equals(url) || url.length() < 1) {
				qrCodeImage.setImageResource(R.drawable.invitate_qr_default_logo);
				return;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// ͼ������ת����ʹ���˾���ת��
			BitMatrix bitMatrix = new QRCodeWriter().encode(url,
					BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
			int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
			// �������ﰴ�ն�ά����㷨��������ɶ�ά���ͼƬ��
			// ����forѭ����ͼƬ����ɨ��Ľ��
			for (int y = 0; y < QR_HEIGHT; y++) {
				for (int x = 0; x < QR_WIDTH; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * QR_WIDTH + x] = 0xff000000;
					} else {
						pixels[y * QR_WIDTH + x] = 0xffffffff;
					}
				}
			}
			// ���ɶ�ά��ͼƬ�ĸ�ʽ��ʹ��ARGB_8888
			Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
					Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
			// ��ʾ��һ��ImageView����
			qrCodeImage.setImageBitmap(bitmap);
			qrCodeImage.setClickable(true);
			qrCodeImage.setTag(bitmap);
		} catch (WriterException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ������ѵ���Ϣ
	 * @param userId
	 */
	private void requestExtension(String userId) {
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		AsyncExtensionNewPageInfo taks = new AsyncExtensionNewPageInfo(
				InvitateActivity.this, userId, String.valueOf(page),
				String.valueOf(pageSize), new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(mLoadingDialog != null && mLoadingDialog.isShowing()){
							mLoadingDialog.dismiss();
						}
						if(baseInfo != null){
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 1 || resultCode == -1) {
								pageInfo = baseInfo.getExtensionNewPageInfo();
							}
						}
					}
				});
		taks.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * ���ض�ά��
	 * @param userId
	 * @param isVerify true��ʾʵ����
	 */
	private void requestUserInfo(String userId,String coMobile,final boolean isVerify) {
		AsyncUserSelectOne task = new AsyncUserSelectOne(InvitateActivity.this,
				userId, "",coMobile, "", new OnGetUserInfoByPhone() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(mLoadingDialog != null && mLoadingDialog.isShowing()){
							mLoadingDialog.dismiss();
						}
						if (baseInfo != null) {
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if (resultCode == 0) {
								UserInfo info = baseInfo.getUserInfo();
									//�л㸶�˻���˵���Ѿ�ʵ����
									promotedURL = URLGenerator.PROMOTED_BASE_URL
											+ "?extension_code="
											+ info.getPromoted_code();
									if(isVerify){
										initQRCode(promotedURL);
									}else{
										initQRCode(null);
									}
							}else{
								initQRCode(null);
							}
						}else{
							initQRCode(null);
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
