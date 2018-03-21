package com.ylfcf.ppp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Html.ImageGetter;
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

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncXSBIscanbuy;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.BorrowType;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.ProjectInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.inter.Inter.OnIsVipUserListener;
import com.ylfcf.ppp.util.Constants;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * ��ȫ����
 * @author Administrator
 *
 */
public class ProductSafetyActivity extends BaseActivity implements OnClickListener{
	private static final String className = "ProductSafetyActivity";
	private static final int REFRESH_VIEW = 5710;
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	//��Ʒ���׽ṹ����֮ǰ�Ĳ���
	private LinearLayout beforeLayout;
	private TextView zjaqTV;
	private TextView zcaqTV;
	
	//��Ʒ���׽ṹ����֮��Ĳ���
	private LinearLayout afterLayout;
	private TextView dbcsTV;
	private TextView hklyTV;
	
	private Button investBtn;//����Ͷ��
	private ProjectInfo projectInfo;
	private ProductInfo productInfo;
	private AlertDialog.Builder builder = null; // �ȵõ�������

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REFRESH_VIEW:
				Bundle bundle = (Bundle) msg.obj;
				refreshView(bundle);
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
		setContentView(R.layout.product_safety_activity);
		builder = new AlertDialog.Builder(ProductSafetyActivity.this,
				R.style.Dialog_Transparent); // �ȵõ�������
		Bundle bundle = getIntent().getBundleExtra("BUNDLE");
		if(bundle != null){
			projectInfo = (ProjectInfo) bundle.getSerializable("PROJECT_INFO");
			productInfo = (ProductInfo) bundle.getSerializable("PRODUCT_INFO");
		}
		findViews();
	}

	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("��ȫ����");
		
		beforeLayout = (LinearLayout) findViewById(R.id.product_safety_activity_before_layout);
		zjaqTV = (TextView) findViewById(R.id.productsafety_activity_zjaq);
		zcaqTV = (TextView) findViewById(R.id.productsafety_activity_zcaq);
		
		afterLayout = (LinearLayout) findViewById(R.id.product_safety_activity_after_layout);
		dbcsTV = (TextView) findViewById(R.id.productsafety_activity_dbcs);
		hklyTV = (TextView) findViewById(R.id.productsafety_activity_after_hkly);
		
		investBtn = (Button) findViewById(R.id.product_safety_activity_bidBtn);
		investBtn.setOnClickListener(this);
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
						equals(SettingsManager.getUserType(ProductSafetyActivity.this))){
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
		if(projectInfo == null){
			beforeLayout.setVisibility(View.GONE);
			afterLayout.setVisibility(View.VISIBLE);
			return;
		}
		if("����".equals(projectInfo.getStatus())){
			beforeLayout.setVisibility(View.GONE);
			afterLayout.setVisibility(View.VISIBLE);
		}else{
			beforeLayout.setVisibility(View.VISIBLE);
			afterLayout.setVisibility(View.GONE);
		}
		initData();
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
		handler.removeCallbacksAndMessages(null);
	}
	
	private void initData(){
		if(projectInfo!=null){
			//��Ʒ���׽ṹ����֮ǰ�ġ��ʽ�ȫ��
			new ImageLoadThread(projectInfo.getCapital_safe(),0).start();
			//��Ʒ���׽ṹ����֮ǰ�ġ�������Դ��
			new ImageLoadThread(projectInfo.getRepay_from(),1).start();
			
			//��Ʒ���׽ṹ����֮ǰ�ġ�������ʩ��
			new ImageLoadThread(projectInfo.getMeasures(),2).start();
			//��Ʒ���׽ṹ����֮��ġ�������Դ��
			new ImageLoadThread(projectInfo.getRepay_from(),3).start();
			
		}
	}
	
	/**
	 * ˢ�¿ؼ�
	 * @param bundle
	 */
	private void refreshView(Bundle bundle){
		if(bundle == null)
			return;
		CharSequence htmlText = bundle.getCharSequence("HTML_TEXT");
		int position = bundle.getInt("POSITION");
		if(position == 0){
			//��Ʒ�ṹ����֮ǰ�ġ��ʽ�ȫ��
			if(htmlText.length() > 0){
				zjaqTV.setText(htmlText);
			}
		}else if(position == 1){
			//��Ʒ�ṹ����֮ǰ�ġ�������Դ��
			zcaqTV.setText(htmlText);
		}else if(position == 2){
			//��Ʒ�ṹ����֮��ġ�������ʩ��
			dbcsTV.setText(htmlText);
		}else if(position == 3){
			//��Ʒ�ṹ����֮��ġ�������Դ��
			hklyTV.setText(htmlText);
		}
	}
	
	class ImageLoadThread extends Thread {
		private String htmlText1 = "";
		private  int position1 = 0;
		public ImageLoadThread(String htmlText,int position){
			this.htmlText1 = htmlText;
			this.position1 = position;
		}
		
		@Override
		public void run() {
			/**
			 * Ҫʵ��ͼƬ����ʾ��Ҫʹ��Html.fromHtml��һ���ع�������public static Spanned fromHtml
			 * (String source, Html.ImageGetterimageGetter, Html.TagHandler
			 * tagHandler)����Html.ImageGetter��һ���ӿڣ�����Ҫʵ�ִ˽ӿڣ�������getDrawable
			 * (String source)�����з���ͼƬ��Drawable����ſ��ԡ�
			 */
			ImageGetter imageGetter = new ImageGetter() {
				@Override
				public Drawable getDrawable(String source) {
					// TODO Auto-generated method stub
					URL url;
					Drawable drawable = null;
					try {
						url = new URL(source);
						int[] screen = SettingsManager.getScreenDispaly(ProductSafetyActivity.this);
						drawable = Drawable.createFromStream(url.openStream(),null);
						if(drawable != null){
							int imageIntrinsicWidth = drawable.getIntrinsicWidth();
							float imageIntrinsicHeight = (float)drawable.getIntrinsicHeight();
							int curImageHeight = (int) (screen[0]*(imageIntrinsicHeight/imageIntrinsicWidth));
							drawable.setBounds(0, 0, screen[0],curImageHeight);//�ĸ���������Ϊ���Ͻǡ����½�����ȷ����һ�����Σ�ͼƬ����������η�Χ�ڻ�����
						}
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					return drawable;
				}
			};
			CharSequence htmlText = Html.fromHtml(htmlText1, imageGetter, null);
			Message msg = handler.obtainMessage(REFRESH_VIEW);
			Bundle bundle = new Bundle();
			bundle.putCharSequence("HTML_TEXT", htmlText);
			bundle.putInt("POSITION", position1);
			msg.obj = bundle;
			handler.sendMessage(msg);

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.product_safety_activity_bidBtn:
			// ��SettingsManager�ж�ȡ���룬���Ϊ����ζ��û�е�¼��
			boolean isLogin = !SettingsManager.getLoginPassword(
					ProductSafetyActivity.this).isEmpty()
					&& !SettingsManager.getUser(ProductSafetyActivity.this).isEmpty();
			// isLogin = true;// ����
			Intent intent = new Intent();
			// 1������Ƿ��Ѿ���¼
			if (isLogin) {
				if("���ֱ�".equals(productInfo.getBorrow_type())){
					isCanbuyXSB(SettingsManager.getUserId(getApplicationContext()), productInfo.getId());
				}else if("vip".equals(productInfo.getBorrow_type())){
					checkIsVip();
				}else if(BorrowType.SUYING.equals(productInfo.getBorrow_type()) || BorrowType.BAOYING.equals(productInfo.getBorrow_type())
							|| BorrowType.WENYING.equals(productInfo.getBorrow_type()) || BorrowType.YUANNIANXIN.equals(productInfo.getBorrow_type())){
					checkIsVerify("���Ŵ�Ͷ��");
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
				intent.setClass(ProductSafetyActivity.this,LoginActivity.class);
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
					intent.setClass(ProductSafetyActivity.this,UserVerifyActivity.class);
					Bundle bundle = new Bundle();
					if("���ֱ�".equals(productInfo.getBorrow_type())){
						bundle.putString("type", "���ֱ�Ͷ��");
					}else if("vip".equals(productInfo.getBorrow_type())){
						bundle.putString("type", "VIPͶ��");
					}else if(BorrowType.SUYING.equals(productInfo.getBorrow_type()) || BorrowType.BAOYING.equals(productInfo.getBorrow_type())
							|| BorrowType.WENYING.equals(productInfo.getBorrow_type())){
						bundle.putString("type", "���Ŵ�Ͷ��");
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
					}else{
						if(productInfo.getBorrow_name().contains("˽������")){
							bundle.putString("type", "˽������");
						}
					}
					bundle.putSerializable("PRODUCT_INFO", productInfo);
					intent.putExtra("bundle", bundle);
					intent.setClass(ProductSafetyActivity.this, BindCardActivity.class);
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
		RequestApis.requestIsVip(ProductSafetyActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVipUserListener() {
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
				Intent intent = new Intent(ProductSafetyActivity.this,VIPProductCJWTActivity.class);
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
		RequestApis.requestIsVerify(ProductSafetyActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(mLoadingDialog != null && mLoadingDialog.isShowing()){
					mLoadingDialog.dismiss();
				}
				Intent intent = new Intent();
				if(flag){
					//�û��Ѿ�ʵ���������ҳ��ֻ�ж��Ƿ�ʵ�����ɡ����ж���û�а�
					if("���ֱ�Ͷ��".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductSafetyActivity.this, BidXSBActivity.class);
					}else if("���Ŵ�Ͷ��".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductSafetyActivity.this, BidZXDActivity.class);
					}else if("VIPͶ��".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductSafetyActivity.this, BidVIPActivity.class);
					}else if("˽������".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductSafetyActivity.this, BidSRZXActivity.class);
					}else if("Ԫ��ӯ".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductSafetyActivity.this, BidYJYActivity.class);
					}
					investBtn.setEnabled(true);
					startActivity(intent);
					finish();
				}else{
					//�û�û��ʵ��
					showMsgDialog(ProductSafetyActivity.this, "ʵ����֤", "����ʵ����֤��");
				}
			}
			@Override
			public void isSetWithdrawPwd(boolean flag, Object object) {
			}
		});
	}
	
	/**
	 * �ж��Ƿ���Թ������ֱ�
	 * @param userId
	 * @param borrowId
	 */
	private void isCanbuyXSB(String userId,String borrowId){
		AsyncXSBIscanbuy task = new AsyncXSBIscanbuy(ProductSafetyActivity.this, userId, borrowId, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//�û����Թ������ֱ�
								Intent intent = new Intent();
								intent.putExtra("PRODUCT_INFO", productInfo);
								intent.setClass(ProductSafetyActivity.this, BidXSBActivity.class);
								startActivity(intent);
							}else if(resultCode == 1001){
								//���Ƚ���ʵ��
								showMsgDialog(ProductSafetyActivity.this, "ʵ����֤", "����ʵ����֤��");
							}else if(resultCode == 1002){
								//���Ƚ��а�
								boolean isNewUser = SettingsManager.checkIsNewUser(SettingsManager.getUserRegTime(getApplicationContext()));
								if(isNewUser){
									showMsgDialog(ProductSafetyActivity.this, "��", "�����Ȱ󿨣�");
								}else{
									showMsgDialog(ProductSafetyActivity.this, "��", "����˾���֧���������������°󿨣�");
								}
							}else{
								showMsgDialog(ProductSafetyActivity.this, "���ܹ������ֱ�", "�˲�Ʒ���״ι����û�ר��");
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
