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
import android.util.TypedValue;
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
import com.ylfcf.ppp.async.AsyncAsscociatedCompany;
import com.ylfcf.ppp.async.AsyncXSBIscanbuy;
import com.ylfcf.ppp.async.AsyncXSMBCurrentUserInvest;
import com.ylfcf.ppp.async.AsyncXSMBDetail;
import com.ylfcf.ppp.async.AsyncXSMBSelectone;
import com.ylfcf.ppp.entity.AssociatedCompanyInfo;
import com.ylfcf.ppp.entity.AssociatedCompanyParentInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.BorrowType;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.ProjectInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnIsBindingListener;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.inter.Inter.OnIsVipUserListener;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ��ʱ��� ��Ŀ��Ϣ
 * @author Mr.liu
 *
 */
public class ProductInfoXSMBActivity extends BaseActivity implements
	OnClickListener {
	private static final String className = "ProductInfoXSMBActivity";
	private static final int REFRESH_VIEW = 5700;
	
	private static final int REQUEST_ASSC_WHAT = 7321;
	private static final int REQUEST_ASSC_SUCCESS = 7322;
	private static final int REQUEST_ASSC_NODATA = 7323;
	
	private static final int REQUEST_XSMB_REFRESH_WHAT = 8291;// ����ӿ�ˢ������
	private static final int REQUEST_CURRENT_USERINVEST = 8294;//����ǰ�û��Ƿ�Ͷ�ʹ������
	private static final int REFRESH_REMAIN_TIME = 8293;//ˢ���¸�����ʣ��ʱ��
	private static final int REQUEST_XSMB_BTNCLICK_WHAT = 8292;//�����������ɱ����ť
	
	private static final int REQUEST_XSMB_SELECTONE = 8296;
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;

	//��Ŀ�ṹ���ĺ�Ĳ���
	private LinearLayout afterLayout;
	private TextView xmjsTV;//��Ŀ����
	private TextView jkfTV,jkfjsTV,//��
		tjfTV,tjfjsTV,//�Ƽ���
		dbfTV,dbfjsTV;//������
	private TextView vipPromptText;//vip��Ʒ��ע
	private ImageView sygzImg;//��������ͼƬ
	//��Ŀ�ṹ����ǰ�Ĳ���
	private LinearLayout beforeLayout;
	private TextView zjytTV,//�ʽ���;
		rzjsTV,//���ʽ���
		dbcsTV,//������ʩ
		tzldTV,//Ͷ������
		sygzTV;//�������
	private LinearLayout sygzLayout;//�������

	private Button investBtn;
	private ProjectInfo projectInfo;
	private ProductInfo productInfo;
	private InvestRecordInfo recordInfo;
	private AlertDialog.Builder builder = null; // �ȵõ�������
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private enum ReasonFlag {
		REFRESH_DATA, // ҳ��ˢ������
		BTN_CLICK,// ͨ����ť���
		RECORD_DATA //Ͷ�ʼ�¼��ת
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_XSMB_REFRESH_WHAT:
				requestXSMBDetails("����",ReasonFlag.REFRESH_DATA);
				break;
			case REQUEST_XSMB_BTNCLICK_WHAT:
				requestXSMBDetails("����",ReasonFlag.BTN_CLICK);
			case REQUEST_CURRENT_USERINVEST:
				requestCurrentUserInvest(SettingsManager.getUserId(getApplicationContext()),productInfo.getId());
				break;
			case REQUEST_ASSC_WHAT:
				requestAssociatedCompany(projectInfo.getLoan_id(), projectInfo.getRecommend_id(), projectInfo.getGuarantee_id());
				break;
			case REQUEST_ASSC_SUCCESS:
				AssociatedCompanyParentInfo parentInfo = (AssociatedCompanyParentInfo) msg.obj;
				initXMYSBData(parentInfo);
				break;
			case REQUEST_ASSC_NODATA:
				
				break;
			case REFRESH_REMAIN_TIME:
				long times = (Long) msg.obj;
				updateCountDown(times);
				break;
			case REFRESH_VIEW:
				Bundle bundle = (Bundle) msg.obj;
				refreshView(bundle);
				break;
			case REQUEST_XSMB_SELECTONE:
				requestXSMBSelectone(recordInfo.getBorrow_id(), "����",ReasonFlag.RECORD_DATA);
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
		setContentView(R.layout.borrow_info_activity);
		builder = new AlertDialog.Builder(ProductInfoXSMBActivity.this,
				R.style.Dialog_Transparent); // �ȵõ�������
		Bundle bundle = getIntent().getBundleExtra("BUNDLE");
		if(bundle != null){
			productInfo = (ProductInfo) bundle.getSerializable("PRODUCT_INFO");
			projectInfo = (ProjectInfo) bundle.getSerializable("PROJECT_INFO");
			recordInfo = (InvestRecordInfo) bundle.getSerializable("InvestRecordInfo");
		}
		findViews();
	}

	@Override
	protected void onResume() {
		super.onResume();
		UMengStatistics.statisticsOnPageStart(className);//����ͳ��ҳ����ת
		UMengStatistics.statisticsResume(this);//����ͳ��ʱ��
		if(recordInfo == null){
			handler.sendEmptyMessage(REQUEST_XSMB_REFRESH_WHAT);
		}else{
			handler.sendEmptyMessage(REQUEST_XSMB_SELECTONE);
		}
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
	
	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("��Ŀ��Ϣ");

		afterLayout = (LinearLayout) findViewById(R.id.borrow_info_activity_after_layout);
		xmjsTV = (TextView) findViewById(R.id.borrow_info_activity_xmjs);
		jkfTV = (TextView) findViewById(R.id.zxd_xmysb_layout_jkf_text);
		jkfjsTV = (TextView) findViewById(R.id.zxd_xmysb_layout_jkfjs_text);
		tjfTV = (TextView) findViewById(R.id.zxd_xmysb_layout_tjf_text);
		tjfjsTV = (TextView) findViewById(R.id.zxd_xmysb_layout_tjfjs_text);
		dbfTV = (TextView) findViewById(R.id.zxd_xmysb_layout_dbf_text);
		dbfjsTV = (TextView) findViewById(R.id.zxd_xmysb_layout_dbfjs_text);
		vipPromptText = (TextView)findViewById(R.id.borrow_info_activity_vipprompt);
		
		beforeLayout = (LinearLayout) findViewById(R.id.borrow_info_activity_before_layout);
		zjytTV = (TextView) findViewById(R.id.borrow_info_activity_zjyt);
		rzjsTV = (TextView) findViewById(R.id.borrow_info_activity_rzjs);
		dbcsTV = (TextView) findViewById(R.id.borrow_info_activity_dbcs);
		tzldTV = (TextView) findViewById(R.id.borrow_info_activity_tzld);
		sygzTV = (TextView) findViewById(R.id.borrow_info_activity_sygz);
		sygzImg = (ImageView) findViewById(R.id.borrow_info_activity_sygz_img);
		sygzLayout = (LinearLayout) findViewById(R.id.borrow_info_activity_sygz_layout);
		
		investBtn = (Button) findViewById(R.id.borrow_info_activity_bidBtn);
		investBtn.setEnabled(false);
		investBtn.setOnClickListener(this);
		if(productInfo != null && BorrowType.VIP.equals(productInfo.getBorrow_type())){
			vipPromptText.setVisibility(View.VISIBLE);
			vipPromptText.setText("*��ʾ��\n1. ����Ʒ������ƽ̨�����Żݻ��\n2.Ԫ�������ӵ�б���Ʒ�����ս���Ȩ��");
		}else{
			vipPromptText.setVisibility(View.VISIBLE);
			vipPromptText.setText("*��ʾ��\n* Ԫ�������ӵ�б���Ʒ�����ս���Ȩ��");
		}
		
		if(projectInfo != null){
			if("����".equals(projectInfo.getStatus())){
				afterLayout.setVisibility(View.VISIBLE);
				beforeLayout.setVisibility(View.GONE);
				handler.sendEmptyMessage(REQUEST_ASSC_WHAT);
			}else{
				afterLayout.setVisibility(View.GONE);
				beforeLayout.setVisibility(View.VISIBLE);
			}
		}
		initData();
	}

	private void initData(){
		if (projectInfo != null) {
			new ImageLoadThread(projectInfo.getCapital(),0).start();
			
			new ImageLoadThread(projectInfo.getIntroduced(),1).start();
			
			new ImageLoadThread(projectInfo.getMeasures(),2).start();
			
			new ImageLoadThread(projectInfo.getInvest_point(), 3).start();

			new ImageLoadThread(projectInfo.getSummary(), 4).start();
		}
	}
	
	/**
	 * ˢ��ҳ��
	 * @param bundle
	 */
	private void refreshView(Bundle bundle){
		if(bundle == null)
			return;
		CharSequence htmlText = bundle.getCharSequence("HTML_TEXT");
		int position = bundle.getInt("POSITION");
		if(position == 0){
			//��Ʒ�ṹ����֮ǰ�ġ��ʽ���;��
			zjytTV.setText(htmlText);
		}else if(position == 1){
			//��Ʒ�ṹ����֮ǰ�ġ����ʷ����ܡ�
			rzjsTV.setText(htmlText);
		}else if(position == 2){
			//��Ʒ�ṹ����֮ǰ�ġ����ʷ����ܡ�
			dbcsTV.setText(htmlText);
		}else if(position == 3){
			//��Ʒ�ṹ����֮ǰ�ġ�Ͷ�����㡱
			tzldTV.setText(htmlText);
		}else if(position == 4){
			//��Ʒ�ṹ����֮��ġ���Ŀ���ܡ�
			xmjsTV.setText(htmlText);
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
						int[] screen = SettingsManager.getScreenDispaly(ProductInfoXSMBActivity.this);
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
	
	/**
	 * ��ʼ����ĿҪ�ر�
	 */
	private void initXMYSBData(AssociatedCompanyParentInfo parentInfo){
		AssociatedCompanyInfo loanInfo = parentInfo.getLoanInfo();
		AssociatedCompanyInfo recommendInfo = parentInfo.getRecommendInfo();
		AssociatedCompanyInfo guaranteeInfo = parentInfo.getGuaranteeInfo();
		
		jkfTV.setText(Html.fromHtml(loanInfo.getCompany_name()));
		jkfjsTV.setText(Html.fromHtml(loanInfo.getIntroduce().trim()));
		tjfTV.setText(Html.fromHtml(recommendInfo.getCompany_name().trim()));
		tjfjsTV.setText(Html.fromHtml(recommendInfo.getIntroduce().trim()));
		dbfTV.setText(Html.fromHtml(guaranteeInfo.getCompany_name().trim()));
		dbfjsTV.setText(Html.fromHtml(guaranteeInfo.getIntroduce().trim()));
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.borrow_info_activity_bidBtn:
			// ��SettingsManager�ж�ȡ���룬���Ϊ����ζ��û�е�¼��
			boolean isLogin = !SettingsManager.getLoginPassword(ProductInfoXSMBActivity.this).isEmpty()
					&& !SettingsManager.getUser(ProductInfoXSMBActivity.this).isEmpty();
			// isLogin = true;// ����
			Intent intent = new Intent();
			// 1������Ƿ��Ѿ���¼
			if (isLogin) {
				//�ж��Ƿ�ʵ����
				handler.sendEmptyMessage(REQUEST_XSMB_BTNCLICK_WHAT);
//				if("���ֱ�".equals(productInfo.getBorrow_type())){
//					isCanbuyXSB(SettingsManager.getUserId(getApplicationContext()), productInfo.getId());
//				}else if(BorrowType.VIP.equals(productInfo.getBorrow_type())){
//					checkIsVip();
//				}else if(BorrowType.SUYING.equals(productInfo.getBorrow_type()) || BorrowType.BAOYING.equals(productInfo.getBorrow_type()) ||
//						BorrowType.WENYING.equals(productInfo.getBorrow_type())){
//					checkIsVerify("���Ŵ�Ͷ��");
//				}else if("���".equals(productInfo.getBorrow_type())){
//					checkIsVerify("���");
//				}else{
//					//˽������
//					if(productInfo.getBorrow_name().contains("˽������")){
//						checkIsVerify("˽������");
//					}
//				}
			} else {
				// δ��¼����ת����¼ҳ��
				intent.setClass(ProductInfoXSMBActivity.this,LoginActivity.class);
				startActivity(intent);
				investBtn.setEnabled(true);
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
					intent.setClass(ProductInfoXSMBActivity.this,UserVerifyActivity.class);
					Bundle bundle = new Bundle();
					if("���ֱ�".equals(productInfo.getBorrow_type())){
						bundle.putString("type", "���ֱ�Ͷ��");
					}else if("vip".equals(productInfo.getBorrow_type())){
						bundle.putString("type", "VIPͶ��");
					}else if(BorrowType.SUYING.equals(productInfo.getBorrow_type()) || BorrowType.BAOYING.equals(productInfo.getBorrow_type())
							|| BorrowType.WENYING.equals(productInfo.getBorrow_type())){
						bundle.putString("type", "���Ŵ�Ͷ��");
					}else if("���".equals(productInfo.getBorrow_type())){
						bundle.putString("type", "���Ͷ��");
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
					intent.setClass(ProductInfoXSMBActivity.this, BindCardActivity.class);
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
		RequestApis.requestIsVerify(ProductInfoXSMBActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				Intent intent = new Intent();
				if(flag){
					//�û��Ѿ�ʵ���������ҳ��ֻ�ж��Ƿ�ʵ�����ɡ����ж���û�а�
//					checkIsBindCard(type);
					if("���ֱ�Ͷ��".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductInfoXSMBActivity.this, BidXSBActivity.class);
					}else if("���Ŵ�Ͷ��".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductInfoXSMBActivity.this, BidZXDActivity.class);
					}else if("VIPͶ��".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductInfoXSMBActivity.this, BidVIPActivity.class);
					}else if("˽������".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductInfoXSMBActivity.this, BidSRZXActivity.class);
					}else if("���".equals(type)){
						// �����Ƿ񻹿��Թ���
						handler.sendEmptyMessage(REQUEST_XSMB_BTNCLICK_WHAT);
						return;
					}
					startActivity(intent);
					investBtn.setEnabled(true);
					finish();
				}else{
					//�û�û��ʵ��
					showMsgDialog(ProductInfoXSMBActivity.this, "ʵ����֤", "����ʵ����֤��");
				}
			}
			@Override
			public void isSetWithdrawPwd(boolean flag, Object object) {
			}
		});
	}
	
	//��ʱ���µ���ʱ
			private void updateCountDown(long times){
				int hour = 0,minute = 0,second = 0;
				String hourStr = "",minuteStr = "",secondStr = "";
				times -= 1000;
				if(times <= 0){
					investBtn.setEnabled(true);
					investBtn.setText("������ɱ");
					investBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_26dp));
					return;
				}
				hour = (int) (times/1000/3600);
				minute = (int) ((times/1000%3600)/60);
				second = (int) (times/1000%3600%60);
				if(hour < 10){
					hourStr = "0" + hour;
				}else{
					hourStr = hour + "";
				}
				if(minute < 10){
					minuteStr = "0" + minute;
				}else{
					minuteStr = minute + "";
				}
				if(second < 10){
					secondStr = "0" + second;
				}else{
					secondStr = second + "";
				}
				investBtn.setText("������һ������ɱ����ʼ��ʣ��"+hourStr+"ʱ"+minuteStr+"��"+secondStr+"��");
				investBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_21dp));
				Message msg = handler.obtainMessage(REFRESH_REMAIN_TIME);
				msg.obj = times;
				handler.sendMessageDelayed(msg, 1000L);
			}
	
	/**
	 * �ж��û��Ƿ�Ϊvip�û�
	 */
	private void checkIsVip(){
		RequestApis.requestIsVip(ProductInfoXSMBActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVipUserListener() {
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
		final Button leftBtn = (Button) contentView.
				findViewById(R.id.borrow_details_vip_msg_dialog_leftbtn);
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
				Intent intent = new Intent(ProductInfoXSMBActivity.this,VIPProductCJWTActivity.class);
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
	 * 
	 * @param flag 1��ʾ������ 0��ʾ��ɱ��������
	 */
	private void showPromptDialog(final String flag){
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.borrow_detail_prompt_layout, null);
		Button leftBtn = (Button) contentView
				.findViewById(R.id.borrow_detail_prompt_layout_leftbtn);
		Button rightBtn = (Button) contentView
				.findViewById(R.id.borrow_detail_prompt_layout_rightbtn);
		TextView topTV = (TextView) contentView
				.findViewById(R.id.borrow_detail_prompt_top_text);
		TextView bottomTV = (TextView) contentView
				.findViewById(R.id.borrow_detail_prompt_bottom_text);
		if("0".equals(flag)){
			bottomTV.setVisibility(View.VISIBLE);
			rightBtn.setVisibility(View.VISIBLE);
			topTV.setText("���ı�����ɱ������ʹ��");
			bottomTV.setText("ÿ����ɱ��ÿ���û�ֻ��һ����ɱ����Ӵ~");
			leftBtn.setText("�鿴Ͷ�ʼ�¼");
			leftBtn.setTextColor(getResources().getColor(R.color.common_topbar_bg_color));
			leftBtn.setBackground(getResources().getDrawable(R.drawable.style_rect_fillet_blue));
			rightBtn.setText("��ע������Ŀ");
		}else if("1".equals(flag)){
			bottomTV.setVisibility(View.GONE);
			rightBtn.setVisibility(View.GONE);
			topTV.setText("����⣡\n����һ����������~");
			leftBtn.setText("ȷ��");
			leftBtn.setTextColor(getResources().getColor(R.color.white));
			leftBtn.setBackground(getResources().getDrawable(R.drawable.style_rect_fillet_filling_blue));
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this,
				R.style.Dialog_Transparent); // �ȵõ�������
		builder.setView(contentView);
		builder.setCancelable(true);
		final AlertDialog dialog = builder.create();
		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if("0".equals(flag)){
					Intent intent = new Intent(ProductInfoXSMBActivity.this,UserInvestRecordActivity.class);
					intent.putExtra("from_where", "���");
					startActivity(intent);
				}
				dialog.dismiss();
			}
		});
		rightBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SettingsManager.setMainProductListFlag(getApplicationContext(), true);	
				dialog.dismiss();
				mApp.finishAllActivityExceptMain();
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
	
	/**
	 * Ͷ�ʰ�ť����ʱ
	 * @param productInfo
	 * @throws ParseException 
	 */
	long remainTimeL = 0l;
	private void initBidBtnCountDown(ProductInfo productInfo,Enum flag){
		investBtn.setEnabled(false);
		String nowTimeStr = productInfo.getNow_time();
		String willStartTimeStr = productInfo.getWill_start_time();//��ʼʱ��
		int hour = 0,minute = 0,second = 0;
		String hourStr = "",minuteStr = "",secondStr = "";
		try {
			Date nowDate = sdf.parse(nowTimeStr);
			Date willStartDate = sdf.parse(willStartTimeStr);
			remainTimeL = willStartDate.getTime() - nowDate.getTime();
			if(remainTimeL >= 0){
				hour = (int) (remainTimeL/1000/3600);
				minute = (int) ((remainTimeL/1000%3600)/60);
				second = (int) (remainTimeL/1000%3600%60);
				if(hour < 10){
					hourStr = "0" + hour;
				}else{
					hourStr = hour + "";
				}
				if(minute < 10){
					minuteStr = "0" + minute;
				}else{
					minuteStr = minute + "";
				}
				if(second < 10){
					secondStr = "0" + second;
				}else{
					secondStr = second + "";
				}
				investBtn.setText("������һ������ɱ����ʼ��ʣ��"+hourStr+"ʱ"+minuteStr+"��"+secondStr+"��");
				investBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_21dp));
				Message msg = handler.obtainMessage(REFRESH_REMAIN_TIME);
				msg.obj = remainTimeL;
				handler.sendMessage(msg);
			}else{
//				//����Ͷ���С���
				investBtn.setText("������ɱ");
				investBtn.setEnabled(true);
				investBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_26dp));
				if(flag == ReasonFlag.BTN_CLICK){
					handler.sendEmptyMessage(REQUEST_CURRENT_USERINVEST);
				}else if(flag == ReasonFlag.REFRESH_DATA){
					
				}
			}
		} catch (ParseException e) {
			investBtn.setText("Ͷ�ʽ���");
			investBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_26dp));
			e.printStackTrace();
		}
	}
	
	private void initViewData(ProductInfo productInfo,Enum flag) {
		if(flag == ReasonFlag.REFRESH_DATA || flag == ReasonFlag.RECORD_DATA){
			//ˢ������
			if("δ����".equals(productInfo.getMoney_status())){
				initBidBtnCountDown(productInfo,flag);//����ʱ
			}else{
				investBtn.setEnabled(false);
				investBtn.setText("Ͷ�ʽ���");
				investBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_26dp));
			}
		}else if(flag == ReasonFlag.BTN_CLICK){
			//�����������ɱ����ť
			if("δ����".equals(productInfo.getMoney_status())){
				//δ���꣬�������û��Ƿ��Ѿ�Ͷ�ʹ������
				initBidBtnCountDown(productInfo,flag);//����ʱ
			}else{
				//������
				investBtn.setEnabled(false);
				investBtn.setText("Ͷ�ʽ���");
				investBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_26dp));
				showPromptDialog("1");
			}
		}
	}
	
	/**
	 * �ж��û��Ƿ��Ѿ���
	 * @param type "��ֵ����"
	 */
	private void checkIsBindCard(final String type){
		RequestApis.requestIsBinding(ProductInfoXSMBActivity.this, SettingsManager.getUserId(getApplicationContext()), "����", new OnIsBindingListener() {
			@Override
			public void isBinding(boolean flag, Object object) {
				Intent intent = new Intent();
				if(flag){
					//�û��Ѿ���
					if("���ֱ�Ͷ��".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductInfoXSMBActivity.this, BidXSBActivity.class);
					}else if("���Ŵ�Ͷ��".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductInfoXSMBActivity.this, BidZXDActivity.class);
					}else if("VIPͶ��".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductInfoXSMBActivity.this, BidVIPActivity.class);
					}
					startActivity(intent);
					investBtn.setEnabled(true);
					finish();
				}else{
					//�û���û�а�
					showMsgDialog(ProductInfoXSMBActivity.this, "��", "����˾���֧���������������°󿨣�");
				}
			}
		});
	}
	
	/**
	 * ������˾
	 * @param loanId
	 * @param recommendId
	 * @param guaranteeId
	 */
	private void requestAssociatedCompany(String loanId,String recommendId,String guaranteeId){
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		AsyncAsscociatedCompany task = new AsyncAsscociatedCompany(ProductInfoXSMBActivity.this, loanId, recommendId, guaranteeId, 
				new OnCommonInter(){
					@Override
					public void back(BaseInfo baseInfo) {
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
						}
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
		AsyncXSBIscanbuy task = new AsyncXSBIscanbuy(ProductInfoXSMBActivity.this, userId, borrowId, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//�û����Թ������ֱ�
								Intent intent = new Intent();
								intent.putExtra("PRODUCT_INFO", productInfo);
								intent.setClass(ProductInfoXSMBActivity.this, BidXSBActivity.class);
								startActivity(intent);
							}else if(resultCode == 1001){
								//���Ƚ���ʵ��
								showMsgDialog(ProductInfoXSMBActivity.this, "ʵ����֤", "����ʵ����֤��");
							}else if(resultCode == 1002){
								//���Ƚ��а�
								boolean isNewUser = SettingsManager.checkIsNewUser(SettingsManager.getUserRegTime(getApplicationContext()));
								if(isNewUser){
									showMsgDialog(ProductInfoXSMBActivity.this, "��", "�����Ȱ󿨣�");
								}else{
									showMsgDialog(ProductInfoXSMBActivity.this, "��", "����˾���֧���������������°󿨣�");
								}
							}else{
								showMsgDialog(ProductInfoXSMBActivity.this, "���ܹ������ֱ�", "�˲�Ʒ���״ι����û�ר��");
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * �������
	 */
	private void requestXSMBDetails(String borrowStatus,final Enum flag) {
		AsyncXSMBDetail xsmbTask = new AsyncXSMBDetail(
				ProductInfoXSMBActivity.this, borrowStatus,new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								investBtn.setVisibility(View.VISIBLE);
								productInfo = baseInfo.getmProductInfo();
								initViewData(productInfo,flag);
							} else {
								investBtn.setVisibility(View.GONE);
							}
						}
					}
				});
		xsmbTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * ����id��ȡ�������
	 * @param borrowId
	 * @param borrowStatus
	 */
	private void requestXSMBSelectone(String borrowId,String borrowStatus,final Enum reasonFlag){
		AsyncXSMBSelectone task = new AsyncXSMBSelectone(ProductInfoXSMBActivity.this, borrowId, borrowStatus, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								investBtn.setVisibility(View.VISIBLE);
								productInfo = baseInfo.getmProductInfo();
								initViewData(productInfo,reasonFlag);
							} else {
								investBtn.setVisibility(View.GONE);
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * �жϵ�ǰ�û��Ƿ�Ͷ�ʹ������
	 * @param userId
	 * @param borrowId
	 */
	private void requestCurrentUserInvest(String userId,String borrowId){
		AsyncXSMBCurrentUserInvest task = new AsyncXSMBCurrentUserInvest(ProductInfoXSMBActivity.this, userId, borrowId, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						investBtn.setEnabled(true);
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//û��Ͷ�ʹ������
								Intent intent = new Intent(ProductInfoXSMBActivity.this,BidXSMBActivity.class);
								intent.putExtra("PRODUCT_INFO", productInfo);
								startActivity(intent);
								finish();
							}else{
								//Ͷ�ʹ������
								showPromptDialog("0");
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
