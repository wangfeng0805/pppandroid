package com.ylfcf.ppp.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.umeng.message.PushAgent;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.widget.LoadingDialog;

import java.text.SimpleDateFormat;

/**
 * Activity������
 * @author Mr.liu
 *
 */
public class BaseActivity extends FragmentActivity{
	/**
	 * Applicationʵ��
	 */
	protected YLFApplication mApp;
	protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	protected LoadingDialog mLoadingDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApp = YLFApplication.getApplication();
		mLoadingDialog = new LoadingDialog(this,"���ڼ���...", R.anim.loading);
		PushAgent.getInstance(getApplicationContext()).onAppStart();
		mApp.addActivity(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mApp.removeActivity(this);
		if(mLoadingDialog != null){
			mLoadingDialog.dismiss();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		//������Fragment��״̬��Activity���ٺ󣬶�fragment��û�����٣�
		// ����Activity�ٴ��ؽ�ʱ��֮ǰ�����fragment����������activity�����٣�����getActivityΪ�ա�
//        super.onSaveInstanceState(outState);
	}
}