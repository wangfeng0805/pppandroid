package com.ylfcf.ppp.ui;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncGetLXZXJXQ;
import com.ylfcf.ppp.async.AsyncJXQRule;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.JXQRuleInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.view.InvitateFriendsPopupwindow;

/**
 * ���ź�--�����֣�2017�괺�ں�
 * @author Mr.liu
 *
 */
public class LXFXTempActivity extends BaseActivity implements OnClickListener{
	private static final int REQUEST_JXQRULE_WHAT = 7218;
	private static final int REQUEST_ISGETJXQ_WHAT = 7219;//�Ƿ�����ȡ��Ϣȯ
	private static final int REQUEST_GETJXQ_WHAT = 7220;
	private LinearLayout mainLayout;
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private Button leftBtn,middleBtn,rightBtn;

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_JXQRULE_WHAT:
				getJXQRule("0.1", "100", "Ԫ����,Ԫ����");
				break;
			case REQUEST_GETJXQ_WHAT:
				JXQRuleInfo info = (JXQRuleInfo) msg.obj;
				getJXQ(SettingsManager.getUserId(getApplicationContext()),info.getId() , 
						"2017-03-01 00:00:00", "2017-03-07 23:59:59", "���ź� ������", "�Զ�", "ָ��");
				break;
			case REQUEST_ISGETJXQ_WHAT:
				//�Ƿ��Ѿ���ȡ����Ϣȯ
				boolean flag = (Boolean) msg.obj;
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
		setContentView(R.layout.lxfx_temp_activity);
		findViews();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		boolean isLogin = !SettingsManager.getLoginPassword(
				LXFXTempActivity.this).isEmpty()
				&& !SettingsManager.getUser(LXFXTempActivity.this)
						.isEmpty();
		if(isLogin){
			if(SettingsManager.isPersonalUser(getApplicationContext())){
				//�����û�
				Message msg = handler.obtainMessage(REQUEST_ISGETJXQ_WHAT);
				msg.obj = false;
				handler.sendMessage(msg);
			}else if(SettingsManager.isCompanyUser(getApplicationContext())){
				//��ҵ�û�
				if(SettingsManager.checkLXFXActivity() == 0){
					leftBtn.setVisibility(View.VISIBLE);
					leftBtn.setEnabled(false);
					leftBtn.setBackgroundResource(R.drawable.lxfx_temp_btns_left_unable);
					rightBtn.setVisibility(View.VISIBLE);
					middleBtn.setEnabled(false);
					middleBtn.setText("��ȡ��Ϣȯ");
					middleBtn.setBackgroundResource(R.drawable.lxfx_temp_btns_middle_unenable);
				}else if(SettingsManager.checkLXFXActivity() == -1){
					//�����
					leftBtn.setVisibility(View.GONE);
					rightBtn.setVisibility(View.GONE);
					middleBtn.setEnabled(false);
					middleBtn.setText("�����");
					middleBtn.setBackgroundResource(R.drawable.lxfx_temp_btns_middle_unenable);
				}else if(SettingsManager.checkLXFXActivity() == 1){
					//���δ��ʼ
					leftBtn.setVisibility(View.GONE);
					rightBtn.setVisibility(View.GONE);
					middleBtn.setEnabled(false);
					middleBtn.setText("�����ڴ�");
					middleBtn.setBackgroundResource(R.drawable.lxfx_temp_btns_middle_unenable);
				}
			}
		}else{
			//δ��¼
			if(SettingsManager.checkLXFXActivity() == 0){
				//����ڽ�����
				leftBtn.setVisibility(View.VISIBLE);
				rightBtn.setVisibility(View.VISIBLE);
				middleBtn.setEnabled(true);
				middleBtn.setText("��ȡ��Ϣȯ");
				middleBtn.setBackgroundResource(R.drawable.lxfx_temp_btns_middle_enable);
			}else if(SettingsManager.checkLXFXActivity() == -1){
				//�����
				leftBtn.setVisibility(View.GONE);
				rightBtn.setVisibility(View.GONE);
				middleBtn.setEnabled(false);
				middleBtn.setText("�����");
				middleBtn.setBackgroundResource(R.drawable.lxfx_temp_btns_middle_unenable);
			}else if(SettingsManager.checkLXFXActivity() == 1){
				//���δ��ʼ
				leftBtn.setVisibility(View.GONE);
				rightBtn.setVisibility(View.GONE);
				middleBtn.setEnabled(false);
				middleBtn.setText("�����ڴ�");
				middleBtn.setBackgroundResource(R.drawable.lxfx_temp_btns_middle_unenable);
			}
		}
	}
	
	private void findViews(){
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("���ź죬������");
		
		mainLayout = (LinearLayout) findViewById(R.id.lxfx_temp_activity_mainlayout);
		leftBtn = (Button) findViewById(R.id.lxfx_temp_activity_leftbtn);
		leftBtn.setOnClickListener(this);
		middleBtn = (Button) findViewById(R.id.lxfx_temp_activity_middlebtn);
		middleBtn.setOnClickListener(this);
		rightBtn = (Button) findViewById(R.id.lxfx_temp_activity_rightbtn);
		rightBtn.setOnClickListener(this);
		
		if(SettingsManager.checkLXFXActivity() == 0){
			//����ڽ�����
			if(SettingsManager.isCompanyUser(getApplicationContext())){
				//�������ҵ�û�
				leftBtn.setVisibility(View.VISIBLE);
				leftBtn.setEnabled(false);
				leftBtn.setBackgroundResource(R.drawable.lxfx_temp_btns_left_unable);
				rightBtn.setVisibility(View.VISIBLE);
				middleBtn.setEnabled(false);
				middleBtn.setText("��ȡ��Ϣȯ");
				middleBtn.setBackgroundResource(R.drawable.lxfx_temp_btns_middle_unenable);
			}else{
				leftBtn.setVisibility(View.VISIBLE);
				rightBtn.setVisibility(View.VISIBLE);
				middleBtn.setEnabled(true);
				middleBtn.setText("��ȡ��Ϣȯ");
				middleBtn.setBackgroundResource(R.drawable.lxfx_temp_btns_middle_enable);
			}
		}else if(SettingsManager.checkLXFXActivity() == -1){
			//�����
			leftBtn.setVisibility(View.GONE);
			rightBtn.setVisibility(View.GONE);
			middleBtn.setEnabled(false);
			middleBtn.setText("�����");
			middleBtn.setBackgroundResource(R.drawable.lxfx_temp_btns_middle_unenable);
		}else if(SettingsManager.checkLXFXActivity() == 1){
			//���δ��ʼ
			leftBtn.setVisibility(View.GONE);
			rightBtn.setVisibility(View.GONE);
			middleBtn.setEnabled(false);
			middleBtn.setText("�����ڴ�");
			middleBtn.setBackgroundResource(R.drawable.lxfx_temp_btns_middle_unenable);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.lxfx_temp_activity_leftbtn:
			Intent intent = new Intent(LXFXTempActivity.this,BorrowListZXDActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.lxfx_temp_activity_middlebtn:
			//��ȡ��Ϣȯ
			boolean isLogin = !SettingsManager.getLoginPassword(
					LXFXTempActivity.this).isEmpty()
					&& !SettingsManager.getUser(LXFXTempActivity.this)
							.isEmpty();
			if(isLogin){
				//�ѵ�¼
				Message msg = handler.obtainMessage(REQUEST_ISGETJXQ_WHAT);
				msg.obj = true;
				handler.sendMessage(msg);
			}else{
				Util.toastLong(LXFXTempActivity.this, "���ȵ�¼");
				Intent intentL = new Intent(LXFXTempActivity.this,LoginActivity.class);
				startActivity(intentL);
			}
			break;
		case R.id.lxfx_temp_activity_rightbtn:
			//����
			showFriendsSharedWindow();
			break;
		default:
			break;
		}
	}
	
	/**
	 * ��ȡ��Ϣȯ��Dialog
	 */
	private void showGetJXQDialog() {
		View contentView = LayoutInflater.from(LXFXTempActivity.this).inflate(
				R.layout.lxfx_getjxq_dialog_layout, null);
		Button leftBtn = (Button) contentView.findViewById(R.id.lxfx_dialog_layout_leftBtn);
		Button rightBtn = (Button) contentView.findViewById(R.id.lxfx_dialog_layout_rightBtn);
		ImageView delBtn = (ImageView) contentView.findViewById(R.id.lxfx_dialog_layout_delbtn);
		AlertDialog.Builder builder = new AlertDialog.Builder(
				LXFXTempActivity.this, R.style.Dialog_Transparent); // �ȵõ�������
		builder.setView(contentView);
		builder.setCancelable(true);
		final AlertDialog dialog = builder.create();
		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LXFXTempActivity.this,MyJXQActivity.class);
				startActivity(intent);
			}
		});
		rightBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
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
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = display.getWidth() * 4 / 5;
		dialog.getWindow().setAttributes(lp);
	}
	
	/**
	 * ʵ����dialog
	 */
	private void showVerifyDialog(final String type){
		View contentView = LayoutInflater.from(LXFXTempActivity.this).inflate(
				R.layout.lxfx_verify_dialog_layout, null);
		Button leftBtn = (Button) contentView.findViewById(R.id.lxfx_verify_dialog_layout_leftBtn);
		Button rightBtn = (Button) contentView.findViewById(R.id.lxfx_verify_dialog_layout_rightBtn);
		ImageView delBtn = (ImageView) contentView.findViewById(R.id.lxfx_verify_dialog_layout_delbtn);
		AlertDialog.Builder builder = new AlertDialog.Builder(
				LXFXTempActivity.this, R.style.Dialog_Transparent); // �ȵõ�������
		builder.setView(contentView);
		builder.setCancelable(true);
		final AlertDialog dialog = builder.create();
		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LXFXTempActivity.this,UserVerifyActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("type", type);
				intent.putExtra("bundle", bundle);
				startActivity(intent);
				dialog.dismiss();
			}
		});
		rightBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
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
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = display.getWidth() * 4 / 5;
		dialog.getWindow().setAttributes(lp);
	}
	
	/**
	 * �Ѿ���ȡ����Ϣȯ��
	 */
	private void showHasGetJXQDialog(){
		View contentView = LayoutInflater.from(LXFXTempActivity.this).inflate(
				R.layout.lxfx_hasgetjxq_dialog_layout, null);
		Button btn = (Button) contentView.findViewById(R.id.user_fragment_lxfx_hasgetjxq_dialog_layout_leftBtn);
		ImageView delBtn = (ImageView) contentView.findViewById(R.id.user_fragment_lxfx_hasgetjxq_dialog_layout_delbtn);
		AlertDialog.Builder builder = new AlertDialog.Builder(
				LXFXTempActivity.this, R.style.Dialog_Transparent); // �ȵõ�������
		builder.setView(contentView);
		builder.setCancelable(true);
		final AlertDialog dialog = builder.create();
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
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
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = display.getWidth() * 4 / 5;
		dialog.getWindow().setAttributes(lp);
	}
	
	/**
	 * �����������ʾ��
	 */
	private void showFriendsSharedWindow() {
		View popView = LayoutInflater.from(this).inflate(
				R.layout.invitate_friends_popupwindow, null);
		int[] screen = SettingsManager.getScreenDispaly(LXFXTempActivity.this);
		int width = screen[0];
		int height = screen[1] / 5 * 2;
		InvitateFriendsPopupwindow popwindow = new InvitateFriendsPopupwindow(LXFXTempActivity.this,
				popView, width, height);
		popwindow.show(mainLayout,URLGenerator.LXFX_WAP_URL,"���ź�������",null);
	}
	
	/**
	 * ��Ϣȯ����
	 * @param money
	 * @param needInvestMoney
	 * @param borrowType
	 */
	private void getJXQRule(String money,String needInvestMoney,String borrowType){
		AsyncJXQRule ruleTask = new AsyncJXQRule(LXFXTempActivity.this, money, needInvestMoney, borrowType, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								JXQRuleInfo info = baseInfo.getmJXQRuleInfo();
								Message msg = handler.obtainMessage(REQUEST_GETJXQ_WHAT);
								msg.obj = info;
								handler.sendMessage(msg);
							}else{
								if(mLoadingDialog != null){
									mLoadingDialog.dismiss();
								}
								middleBtn.setEnabled(true);
								Util.toastLong(LXFXTempActivity.this, baseInfo.getMsg());
							}
						}else{
							if(mLoadingDialog != null){
								mLoadingDialog.dismiss();
							}
							middleBtn.setEnabled(true);
						}
					}
				});
		ruleTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * ��ȡ��Ϣȯ
	 * @param userId
	 * @param ticket
	 * @param startTime
	 * @param endTime
	 * @param remark
	 * @param type
	 * @param isBatch
	 */
	private void getJXQ(String userId,String ticket,String startTime,String endTime,
			String remark,String type,String isBatch){
		AsyncGetLXZXJXQ task = new AsyncGetLXZXJXQ(LXFXTempActivity.this, userId, ticket, startTime, 
				endTime, remark, type, isBatch, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(mLoadingDialog != null){
							mLoadingDialog.dismiss();
						}
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//��ȡ�ɹ�
								middleBtn.setEnabled(false);
								middleBtn.setBackgroundResource(R.drawable.lxfx_temp_btns_middle_unenable);
								showGetJXQDialog();
							}else{
								//��ȡʧ��
								middleBtn.setEnabled(true);
								Util.toastLong(LXFXTempActivity.this, baseInfo.getMsg());
							}
						}else{
							middleBtn.setEnabled(true);
							Util.toastLong(LXFXTempActivity.this, "ϵͳ����");
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * ��֤�û��Ƿ��Ѿ���֤
	 * @param type ����ֵ��,�����֡�
	 */
	private void checkIsVerify(final String type){
		middleBtn.setEnabled(false);
		if(mLoadingDialog != null){
			mLoadingDialog.show();
		}
		RequestApis.requestIsVerify(LXFXTempActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(flag){
					//�û��Ѿ�ʵ��
					handler.sendEmptyMessage(REQUEST_JXQRULE_WHAT);
				}else{
					//�û�û��ʵ��
					middleBtn.setEnabled(true);
					mLoadingDialog.dismiss();
					showVerifyDialog(type);
				}
			}
			@Override
			public void isSetWithdrawPwd(boolean flag, Object object) {
			}
		});
	}
}
