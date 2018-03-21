package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncBFVerify;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.view.CommonPopwindow;
import com.ylfcf.ppp.view.VerifySucPopwindow;

/**
 * �û�ʵ����֤ҳ��
 * 
 * ��ֵ�����ֻ����Ϲ�ʱ������û���û��ʵ����������ת����ҳ��;
 * ���û��Ѿ���д��������ϲ��ҵ�����ύ��ť��ϵͳ����һ��ʱ�����֤���̣�
 * ����ڴ˹������û��ٴε����ֵ�����֡��Ϲ���ť��ת����ҳ���ʱ���û�����ʵ���������֤�ŵ���Ϣ�ǲ��ɱ༭��״̬��
 * �ύ��ť���ɵ�������ڴ�ҳ��Ҳ����Ӧ����֤�����ʾ��
 * һ����֤�ɹ�����ҳ�治������ʾ������û�������ת����ҳ�档
 * @author Mr.liu
 *
 */
public class UserVerifyActivity extends BaseActivity implements OnClickListener{
	private static final String className = "UserVerifyActivity";
	private EditText realNameET;
	private EditText idNumberET;
	private Button commitBtn;
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private LinearLayout mainLayout;

	private String type = "";//��ֵ�����֣���ʾ�Ǵӳ�ֵ���̻�������������ת�����ġ�
	private String rechargeType = "";//��ֵ���� kjcz:��ݳ�ֵ pos:pos����ֵ
	private boolean isVerify = false;

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_verify_activity);
		Bundle bundle = getIntent().getBundleExtra("bundle");
		if(bundle != null){
			type = bundle.getString("type");
			rechargeType = bundle.getString("recharge_type");
		}
		findViews();
		
	}

	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("ʵ����֤");
		
		mainLayout = (LinearLayout) findViewById(R.id.user_verify_activity_mainlayout);
		realNameET = (EditText)findViewById(R.id.user_verify_activity_realname);
		idNumberET = (EditText)findViewById(R.id.user_verify_activity_idnumber); 
		
		commitBtn = (Button)findViewById(R.id.user_verify_activity_sure_btn);
		commitBtn.setOnClickListener(this);

//		if(!"��ֵ".equals(type)){
//			handler.postDelayed(new Runnable() {
//
//				@Override
//				public void run() {
//					showVerifyPrompt();
//				}
//			}, 500L);
//		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_verify_activity_sure_btn:
			chackData();
			break;
		case R.id.common_topbar_left_layout:
			if(isVerify && "�����н�".equals(type)){
				try{
					mApp.getInvitateActivity().finish();
				}catch (Exception e){
					e.printStackTrace();
				}
				Intent intent = new Intent(UserVerifyActivity.this,InvitateActivity.class);
				intent.putExtra("is_verify", true);
				startActivity(intent);
			}
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			System.out.println("������back��   onKeyDown()");
			if("�����н�".equals(type)){
				try{
					mApp.getInvitateActivity().finish();
				}catch (Exception e){
					e.printStackTrace();
				}
				Intent intent = new Intent(UserVerifyActivity.this,InvitateActivity.class);
				intent.putExtra("is_verify", true);
				startActivity(intent);
			}
			finish();
			return false;
		}else {
			return super.onKeyDown(keyCode, event);
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
		handler.removeCallbacksAndMessages(null);
	}
	
	private void chackData(){
		String idNumber = idNumberET.getText().toString();
		String realName = realNameET.getText().toString();
		if (!"".equals(idNumber)) {
			if (!"".equals(realName)) {
				requestUserVerify(
						SettingsManager.getUserId(UserVerifyActivity.this),
						idNumber, realName);
			} else {
				Util.toastShort(UserVerifyActivity.this, "��ʵ��������Ϊ��");
			}
		} else {
			Util.toastShort(UserVerifyActivity.this, "���֤���벻�Ϸ�");
		}
	}
	
	/**
	 * �û��������ҳ�����Ҫʵ����֤����ʾ
	 */
	private void showVerifyPrompt(){
		View popView = LayoutInflater.from(this).inflate(R.layout.common_popwindow, null);
		int[] screen = SettingsManager.getScreenDispaly(UserVerifyActivity.this);
		int width = screen[0]*4/5;
		int height = screen[1]*1/5;
		CommonPopwindow popwindow = new CommonPopwindow(UserVerifyActivity.this,popView, width, height,"ʵ����֤","",null);
		popwindow.show(mainLayout);
	}

	/**
	 * ��֤�ɹ��ĵ���
	 */
	private void showVerifySucPrompt(){
		View popView = LayoutInflater.from(this).inflate(
				R.layout.verify_suc_popwindow_layout, null);
		int[] screen = SettingsManager.getScreenDispaly(this);
		int width = screen[0] * 6 / 7;
		int height = screen[1] /3;
		VerifySucPopwindow popwindow = new VerifySucPopwindow(UserVerifyActivity.this,
				popView, width, height,new OnPopRechargeBtnListener(){
			@Override
			public void back() {
				//ȥ��ֵ
				if("Ͷ��".equals(type) || "����".equals(type) || "��ʾ��".equals(type) || "�����н�".equals(type)
						|| "ע��ɹ�".equals(type)){
					Intent intent = new Intent(UserVerifyActivity.this,RechargeChooseActivity.class);
					startActivity(intent);
				}else{
					if("kjcz".equals(rechargeType)){
						//��ݳ�ֵ
						Intent intent = new Intent(UserVerifyActivity.this,BindCardActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("type","��ֵ");
						intent.putExtra("bundle",bundle);
						startActivity(intent);
					}else if("pos".equals(rechargeType)){
						//pos֧��
						Intent intent = new Intent(UserVerifyActivity.this,RechargePosActivity.class);
						startActivity(intent);
					}
				}
				if("�����н�".equals(type)){
					try{
						mApp.getInvitateActivity().finish();
					}catch (Exception e){
						e.printStackTrace();
					}
				}
				finish();
			}
		},new OnPopCancelBtnListener(){
			@Override
			public void back() {
				//�´���˵
				if("�����н�".equals(type)){
					try{
						mApp.getInvitateActivity().finish();
					}catch (Exception e){
						e.printStackTrace();
					}
					Intent intent = new Intent(UserVerifyActivity.this,InvitateActivity.class);
					intent.putExtra("is_verify", true);
					startActivity(intent);
				}
				finish();
			}
		});
		popwindow.show(mainLayout);
	}

	/**
	 * �û�ʵ����֤
	 * @param userId
	 * @param realName
	 * @param idNumber
	 */
	private void requestUserVerify(String userId,String idNumber,String realName){
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		final AsyncBFVerify task = new AsyncBFVerify(UserVerifyActivity.this, userId, idNumber, realName, new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(mLoadingDialog != null && mLoadingDialog.isShowing()){
					mLoadingDialog.dismiss();
				}
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						isVerify = true;
						if("�����ת�̷���".equals(type)){
							Intent intent = new Intent();
							setResult(101,intent);
							finish();
						}else if("��ȡ��Ϣȯ".equals(type)){
							finish();
						}else{
							showVerifySucPrompt();
						}
					}else{
						Util.toastShort(UserVerifyActivity.this, baseInfo.getMsg());
					}
				}
			}
		});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * ʵ����֤�ɹ��ĵ��� ������ȥ��ֵ��ť��
	 */
	public interface OnPopRechargeBtnListener{
		void back();
	}

	/**
	 * ʵ����֤�ɹ��ĵ��� �������´���˵��ť��
	 */
	public interface OnPopCancelBtnListener{
		void back();
	}
	
}
