package com.ylfcf.ppp.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.db.DBGesturePwdManager;
import com.ylfcf.ppp.entity.GesturePwdEntity;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.widget.GestureContentView;
import com.ylfcf.ppp.widget.GestureDrawline.GestureCallBack;

/**
 * 
 * ���ƻ���/У�����
 *
 */
public class GestureVerifyActivity extends Activity implements android.view.View.OnClickListener{
	private static final String className = "GestureVerifyActivity";
	/** �ֻ�����*/
	public static final String PARAM_PHONE_NUMBER = "PARAM_PHONE_NUMBER";
	/** ��ͼ */
	public static final String PARAM_INTENT_CODE = "PARAM_INTENT_CODE";
	private ImageView mImgUserLogo;
	private TextView mTextPhoneNumber;
	private TextView mTextTip;
	private FrameLayout mGestureContainer;
	private GestureContentView mGestureContentView;
	private TextView mTextForget;
	private String mParamPhoneNumber;
	private long mExitTime = 0;
	private int mParamIntentCode;
	private int startCount = 5;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gesture_verify_activity);
		ObtainExtraData();
		setUpViews();
		setUpListeners();
	}
	
	private void ObtainExtraData() {
		mParamPhoneNumber = getIntent().getStringExtra(PARAM_PHONE_NUMBER);
		mParamIntentCode = getIntent().getIntExtra(PARAM_INTENT_CODE, 0);
	}
	
	private void setUpViews() {
		mImgUserLogo = (ImageView) findViewById(R.id.user_logo);
		mTextPhoneNumber = (TextView) findViewById(R.id.text_phone_number);
		mTextTip = (TextView) findViewById(R.id.text_tip);
		mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
		mTextForget = (TextView) findViewById(R.id.text_forget_gesture);
		mTextPhoneNumber.setText(Util.hidPhoneNum(SettingsManager.getUser(getApplicationContext())));
		
		// ��ʼ��һ����ʾ�������viewGroup
		String userId = SettingsManager.getUserId(getApplicationContext());
		GesturePwdEntity entity = DBGesturePwdManager.getInstance(getApplicationContext()).getGesturePwdEntity(userId);
		String gesturePwd = null;
		if(entity != null)
		gesturePwd = entity.getPwd();
		mGestureContentView = new GestureContentView(this, true, gesturePwd,
				new GestureCallBack() {
					@Override
					public void onGestureCodeInput(String inputCode) {
					}

					@Override
					public void checkedSuccess() {
						mGestureContentView.clearDrawlineState(0L);
						GestureVerifyActivity.this.finish();
						Intent intent = new Intent(GestureVerifyActivity.this,MainFragmentActivity.class);
						startActivity(intent);
					}

					@Override
					public void checkedFail() {
						--startCount;
						mGestureContentView.clearDrawlineState(300L);
						mTextTip.setVisibility(View.VISIBLE);
						mTextTip.setText("������󣬻���������"+startCount+"��");
						// �����ƶ�����
						Animation shakeAnimation = AnimationUtils.loadAnimation(GestureVerifyActivity.this, R.anim.shake);
						mTextTip.startAnimation(shakeAnimation);
						if(startCount == 0){
							showDialog("��������ʧЧ�������µ�¼��");
						}
					}
				});
		// �������ƽ�����ʾ���ĸ���������
		mGestureContentView.setParentView(mGestureContainer);
	}
	
	private void setUpListeners() {
		mTextForget.setOnClickListener(this);
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.text_forget_gesture:
			showDialog("�����������룬�����µ�¼��");
			break;
		default:
			break;
		}
	}
	
	private void showDialog(String content){
		AlertDialog.Builder builder=new AlertDialog.Builder(this);  //�ȵõ�������  
        builder.setTitle("��ʾ"); //���ñ���  
        builder.setMessage(content); //��������  
        builder.setCancelable(false);
//        builder.setIcon(R.drawable.ic_launcher);//����ͼ�꣬ͼƬid����  
        builder.setPositiveButton("���µ�¼", new DialogInterface.OnClickListener() { //����ȷ����ť  
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss(); //�ر�dialog  
                GesturePwdEntity entity = new GesturePwdEntity();
        		String userId = SettingsManager.getUserId(getApplicationContext());
        		String phone = SettingsManager.getUser(getApplicationContext());
        		entity.setUserId(userId);
        		entity.setPhone(phone);
        		entity.setStatus("0");
        		entity.setPwd("");
                SettingsManager.setUser(GestureVerifyActivity.this,"");
        		SettingsManager.setLoginPassword(GestureVerifyActivity.this,"",true);
        		SettingsManager.setUserId(GestureVerifyActivity.this,"");
        		SettingsManager.setUserName(GestureVerifyActivity.this,"");
        		DBGesturePwdManager.getInstance(getApplicationContext()).updateGestureEntity(entity);
                Intent intent = new Intent(GestureVerifyActivity.this,LoginActivity.class);
                intent.putExtra("FLAG", "from_gesture_verify_activity");
                startActivity(intent);
                finish();
            }  
        });  
  
        //��������������ˣ���������ʾ����  
        builder.create().show();  
	}
}
