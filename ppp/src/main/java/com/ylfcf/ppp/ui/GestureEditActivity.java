package com.ylfcf.ppp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.db.DBGesturePwdManager;
import com.ylfcf.ppp.entity.GesturePwdEntity;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.YLFLogger;
import com.ylfcf.ppp.widget.GestureContentView;
import com.ylfcf.ppp.widget.GestureDrawline.GestureCallBack;
import com.ylfcf.ppp.widget.LockIndicator;

/**
 * 
 * �����������ý���
 *
 */
public class GestureEditActivity extends Activity implements OnClickListener{

	private static final String className = "GestureEditActivity";
	/** �ֻ�����*/
	public static final String PARAM_PHONE_NUMBER = "PARAM_PHONE_NUMBER";
	/** ��ͼ */
	public static final String PARAM_INTENT_CODE = "PARAM_INTENT_CODE";
	/** �״���ʾ�����������룬����ѡ������ */
	public static final String PARAM_IS_FIRST_ADVICE = "PARAM_IS_FIRST_ADVICE";
	private TextView mTextTitle;
//	private TextView mTextCancel;
	private LockIndicator mLockIndicator;
	private TextView mTextTip;
	private FrameLayout mGestureContainer;
	private GestureContentView mGestureContentView;
	private TextView mTextReset;
	private ImageView topLeftBtn;
	private String mParamSetUpcode = null;
	private String mParamPhoneNumber;
	private boolean mIsFirstInput = true;
	private String mFirstPassword = null;
	private String mConfirmPassword = null;
	private int mParamIntentCode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gesture_edit_activity);
		setUpViews();
		setUpListeners();
	}
	
	private void setUpViews() {
		mTextTitle = (TextView) findViewById(R.id.text_title);
//		mTextCancel = (TextView) findViewById(R.id.text_cancel);
		mTextReset = (TextView) findViewById(R.id.text_reset);
		mLockIndicator = (LockIndicator) findViewById(R.id.lock_indicator);
		mTextTip = (TextView) findViewById(R.id.text_tip);
		topLeftBtn = (ImageView)findViewById(R.id.gesture_edit_activity_top_leftbtn);
		topLeftBtn.setOnClickListener(this);
		mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
		// ��ʼ��һ����ʾ�������viewGroup
		mGestureContentView = new GestureContentView(this, false, "", new GestureCallBack() {  
			@Override
			public void onGestureCodeInput(String inputCode) {
				if (!isInputPassValidate(inputCode)) {
					mTextTip.setText(Html.fromHtml("<font color='#c70c1e'>��������4����, ����������</font>"));
					mGestureContentView.clearDrawlineState(0L);
					return;
				}
				if (mIsFirstInput) {
					mFirstPassword = inputCode;
					updateCodeList(inputCode);
					mGestureContentView.clearDrawlineState(0L);
					mTextTip.setText("�ٴ�������������");
				} else {
					if (inputCode.equals(mFirstPassword)) {
							Toast.makeText(GestureEditActivity.this, "���óɹ�", Toast.LENGTH_SHORT).show();
						YLFLogger.d("�������룺"+mFirstPassword);
						String userId = SettingsManager.getUserId(getApplicationContext());
						String phone = SettingsManager.getUser(getApplicationContext());
						GesturePwdEntity entity = new GesturePwdEntity();
						entity.setUserId(userId);
						entity.setPhone(phone);
						entity.setStatus("1");//1��ʾ������������
						entity.setPwd(mFirstPassword);
						DBGesturePwdManager.getInstance(getApplicationContext()).addGesturePwd(entity);
						mGestureContentView.clearDrawlineState(0L);
						setResult(200);
						GestureEditActivity.this.finish();
					} else {
						mTextTip.setText(Html.fromHtml("<font color='#c70c1e'>����һ�λ��Ʋ�һ�£������»���</font>"));
						// �����ƶ�����
						Animation shakeAnimation = AnimationUtils.loadAnimation(GestureEditActivity.this, R.anim.shake);
						mTextTip.startAnimation(shakeAnimation);
						// ���ֻ��Ƶ��ߣ�1.5������
						mGestureContentView.clearDrawlineState(300L);
					}
				}
				mIsFirstInput = false;
			}

			@Override
			public void checkedSuccess() {
				
			}

			@Override
			public void checkedFail() {
				
			}
		});
		// �������ƽ�����ʾ���ĸ���������
		mGestureContentView.setParentView(mGestureContainer);
		updateCodeList("");
	}
	
	private void setUpListeners() {
		mTextReset.setOnClickListener(this);
	}
	
	private void updateCodeList(String inputCode) {
		// ����ѡ���ͼ��
		mLockIndicator.setPath(inputCode);
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
		case R.id.text_reset:
//			mIsFirstInput = true;
//			updateCodeList("");
//			mTextTip.setText("���ƽ���ͼ��");
			finish();
			break;
		case R.id.gesture_edit_activity_top_leftbtn:
			finish();
			break;
		default:
			break;
		}
	}
	
	private boolean isInputPassValidate(String inputPassword) {
		if (TextUtils.isEmpty(inputPassword) || inputPassword.length() < 4) {
			return false;
		}
		return true;
	}

}
