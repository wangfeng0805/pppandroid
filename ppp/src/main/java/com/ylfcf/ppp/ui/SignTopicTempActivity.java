package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncHDSignIsdaysigned;
import com.ylfcf.ppp.async.AsyncMarchAddResult;
import com.ylfcf.ppp.async.AsyncXCFLActiveTime;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.SignResultInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.view.InvitateFriendsPopupwindow;
import com.ylfcf.ppp.widget.LoadingDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * ǩ��ר��ҳ��
 * @author Mr.liu
 *
 */
public class SignTopicTempActivity extends BaseActivity implements OnClickListener{
	private static final String className = "SignTopicTempActivity";
	private static final int REQUEST_SYSTEM_TIME_WHAT = 2671;
	private static final int REQUEST_SYSTEM_TIME_SUCCESS = 2672;
	private static final int REQUEST_ISSIGNED_WHAT = 2673;
	private static final int REQUEST_ISSIGNED_SUCCESS = 2674;
	private static final int REQUEST_SIGN_ACTIVE_WHAT = 2677;
	private static final int REQUEST_SIGN_ACTIVE_SUCCESS = 2678;
	
	private static final int REQUEST_MARCHADD_WHAT = 2675;//ǩ��
	private static final int REQUEST_MARCHADD_SUCCESS = 2676;//ǩ���ɹ�
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private LinearLayout mainLayout;
	private Button jqqdBtn;//�����ڴ�
	private Button endBtn;//�����
	private Button signBtn;//ǩ����ť
	private Button shareBtn;//����ť
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_SIGN_ACTIVE_WHAT:
				requestSignActiveTime("QD_03");//���·�ǩ���
				break;
			case REQUEST_SIGN_ACTIVE_SUCCESS:
				
				break;
			case REQUEST_SYSTEM_TIME_WHAT:
//				getSystemNowTime("1");
				break;
			case REQUEST_SYSTEM_TIME_SUCCESS:
				break;
			case REQUEST_ISSIGNED_WHAT:
				//�����Ƿ��Ѿ�ǩ��
				String day = (String) msg.obj;
				hdsignIsdaysigned(SettingsManager.getUserId(getApplicationContext()), day);
				break;
			case REQUEST_MARCHADD_WHAT:
				marchAdd(SettingsManager.getUserId(getApplicationContext()));
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
		setContentView(R.layout.sign_topic_temp_activity);
		mLoadingDialog = new LoadingDialog(SignTopicTempActivity.this, "���ڼ���...", R.anim.loading);
		findViews();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}

	@Override
	protected void onResume() {
		super.onResume();
		UMengStatistics.statisticsOnPageStart(className);//����ͳ��ҳ����ת
		UMengStatistics.statisticsResume(this);//����ͳ��ʱ��
		handler.sendEmptyMessage(REQUEST_SIGN_ACTIVE_WHAT);
	}

	@Override
	protected void onPause() {
		super.onPause();
		UMengStatistics.statisticsOnPageEnd(className);//����ͳ��ҳ����ת
		UMengStatistics.statisticsPause(this);//����ͳ��ʱ��
	}

	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("����ǩ�� �����Ϣ");
		mainLayout = (LinearLayout) findViewById(R.id.sign_temp_activity_mainlayout);
		
		jqqdBtn = (Button) findViewById(R.id.sign_topic_temp_activity_jqqd_btn);
		endBtn = (Button) findViewById(R.id.sign_topic_temp_activity_end_btn);
		signBtn = (Button) findViewById(R.id.sign_topic_temp_activity_sign_btn);
		signBtn.setOnClickListener(this);
		shareBtn = (Button) findViewById(R.id.sign_topic_temp_activity_share_btn);
		shareBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.sign_topic_temp_activity_sign_btn:
			//ǩ��,���ж��Ƿ��Ѿ���¼
			boolean isLogin = !SettingsManager.getLoginPassword(
					SignTopicTempActivity.this).isEmpty()
					&& !SettingsManager.getUser(SignTopicTempActivity.this)
							.isEmpty();
			if(isLogin){
				//����ǩ���ӿ�
				handler.sendEmptyMessage(REQUEST_MARCHADD_WHAT);
//				Intent intent = new Intent(SignTopicTempActivity.this,SignActivity.class);
//				startActivity(intent);
			}else{
				Intent intent = new Intent(SignTopicTempActivity.this,LoginActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.sign_topic_temp_activity_share_btn:
			//����
			showFriendsSharedWindow();
			break;
		default:
			break;
		}
	}
	
	/**
	 * ���ݵ�ǰʱ���ʼ����ť״̬
	 * @param nowTime
	 */
	private void initBtnStatus(int resultCode,Date nowTime){
		//ǩ����Ƿ�ʼ
		if (resultCode == 0) {
			//��ѿ�ʼ
			jqqdBtn.setVisibility(View.GONE);
			signBtn.setVisibility(View.VISIBLE);
			shareBtn.setVisibility(View.VISIBLE);
			endBtn.setVisibility(View.GONE);
		} else if (resultCode == -3) {
			//�����
			jqqdBtn.setVisibility(View.GONE);
			signBtn.setVisibility(View.GONE);
			shareBtn.setVisibility(View.GONE);
			endBtn.setVisibility(View.VISIBLE);
		} else if (resultCode == -2) {
			//���û��ʼ
			jqqdBtn.setVisibility(View.VISIBLE);
			signBtn.setVisibility(View.GONE);
			shareBtn.setVisibility(View.GONE);
			endBtn.setVisibility(View.GONE);
		}
		Message msg = handler.obtainMessage(REQUEST_ISSIGNED_WHAT);
		msg.obj = sdf1.format(nowTime);
		handler.sendMessage(msg);
	}
	
	/**
	 * �����������ʾ��
	 */
	private void showFriendsSharedWindow() {
		View popView = LayoutInflater.from(this).inflate(
				R.layout.invitate_friends_popupwindow, null);
		int[] screen = SettingsManager.getScreenDispaly(SignTopicTempActivity.this);
		int width = screen[0];
		int height = screen[1] / 5 * 2;
		InvitateFriendsPopupwindow popwindow = new InvitateFriendsPopupwindow(SignTopicTempActivity.this,
				popView, width, height);
		popwindow.show(mainLayout,URLGenerator.SIGN_WAP_URL,"���·�ǩ���",null);
	}
	
	/**
	 * �ж�ĳ���Ƿ��Ѿ�ǩ��
	 * @param userId
	 * @param day
	 */
	private void hdsignIsdaysigned(String userId,String day){
		AsyncHDSignIsdaysigned signedTask = new AsyncHDSignIsdaysigned(SignTopicTempActivity.this, userId, day, new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						//������ǩ��
						signBtn.setBackgroundResource(R.drawable.sign_activity_has_signed_btn);
					}else if(resultCode == -1){
						//��ǩ��
						signBtn.setBackgroundResource(R.drawable.sign_activity_sign_btn);
					}
				}
			}
		});
		signedTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * ǩ���ӿ�
	 * @param userId
	 */
	private void marchAdd(String userId){
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		AsyncMarchAddResult marchTask = new AsyncMarchAddResult(SignTopicTempActivity.this, userId, new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(mLoadingDialog != null){
					mLoadingDialog.dismiss();
				}
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						SignResultInfo resultInfo = baseInfo.getSignResultInfo();
						Intent intent = new Intent(SignTopicTempActivity.this,SignActivity.class);
						intent.putExtra("SignResultInfo", resultInfo);
						intent.putExtra("SystemTime", baseInfo.getTime());
						startActivity(intent);
					}else{
						Util.toastLong(SignTopicTempActivity.this, baseInfo.getMsg());
					}
				}
			}
		});
		marchTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * �жϻ��ʼʱ��
	 * @param activeTitle
	 */
	private void requestSignActiveTime(String activeTitle){
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		AsyncXCFLActiveTime task = new AsyncXCFLActiveTime(SignTopicTempActivity.this, activeTitle, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(mLoadingDialog != null){
							mLoadingDialog.dismiss();
						}
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							try {
								initBtnStatus(resultCode, sdf.parse(baseInfo.getTime()));
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
