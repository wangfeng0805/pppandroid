package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.db.DBGesturePwdManager;
import com.ylfcf.ppp.entity.GesturePwdEntity;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;

/**
 * ������������ҳ��
 * @author Administrator
 *
 */
public class GestureSettingActivity extends BaseActivity implements OnClickListener{
	private static final String className = "GestureSettingActivity";
	private LinearLayout setGesturePwd;
	private View line;
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	private ToggleButton toggleButton;
	private boolean isFromLoginActivity = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gesture_setting_activity);
		findviews();
	}
	
	private void findviews(){
		String userId = SettingsManager.getUserId(getApplicationContext());
		GesturePwdEntity entity = DBGesturePwdManager.getInstance(getApplicationContext()).getGesturePwdEntity(userId);
		
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("��������");
		
		setGesturePwd = (LinearLayout)findViewById(R.id.gesture_setting_activity_setlayout);
		setGesturePwd.setOnClickListener(this);
		line = findViewById(R.id.gesture_setting_activity_line);
		toggleButton = (ToggleButton) findViewById(R.id.gesture_setting_activity_toggle);
		if(entity != null && "1".equals(entity.getStatus())){
			toggleButton.setChecked(true);
			setGesturePwd.setVisibility(View.VISIBLE);
			line.setVisibility(View.VISIBLE);
		}else{
			toggleButton.setChecked(false);
			setGesturePwd.setVisibility(View.GONE);
			line.setVisibility(View.GONE);
		}
		toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					//ѡ��
					if(!isFromLoginActivity){
						buttonView.setChecked(false);
						showDialog(isChecked);
					}
				}else{
					//δѡ��
					if(!isFromLoginActivity){
						buttonView.setChecked(true);
						showDialog(isChecked);
					}
				}
				isFromLoginActivity = false;
			}
		});
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case 200:
			isFromLoginActivity = true;
			toggleButton.setChecked(true);
    		setGesturePwd.setVisibility(View.VISIBLE);
    		line.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.gesture_setting_activity_setlayout:
			showDialog(true);
//			Intent intentEdit = new Intent(GestureSettingActivity.this,GestureEditActivity.class);
//			startActivity(intentEdit);
			break;
		default:
			break;
		}
	}
	
	/**
	 * �򿪻��߹ر���������
	 * @param isChecked
	 */
	private void showDialog(final boolean isChecked){
		View contentView = LayoutInflater.from(this).inflate(R.layout.gesture_login_pwd_layout, null);
		final EditText loginPwd = (EditText) contentView.findViewById(R.id.gesture_login_pwd_layout_et);
		Button sureBtn = (Button)contentView.findViewById(R.id.gesture_login_pwd_layout_sure_btn);
		final TextView promptText = (TextView)contentView.findViewById(R.id.gesture_login_pwd_prompt);
		AlertDialog.Builder builder=new AlertDialog.Builder(this,R.style.Dialog_Transparent);  //�ȵõ�������  
        builder.setTitle(""); //���ñ���  
        builder.setView(contentView);
        final AlertDialog dialog = builder.create();
        sureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String locationLoginPwd = SettingsManager.getLoginPassword(getApplicationContext());
            	String inputLoginPwd = Util.md5Encryption(loginPwd.getText().toString());
            	if(locationLoginPwd.equals(inputLoginPwd)){
            		dialog.dismiss(); //�ر�dialog  
            		if(!isChecked){
            			isFromLoginActivity = true;
            			toggleButton.setChecked(false);
            			GesturePwdEntity entity = new GesturePwdEntity();
                		String userId = SettingsManager.getUserId(getApplicationContext());
                		String phone = SettingsManager.getUser(getApplicationContext());
                		entity.setUserId(userId);
                		entity.setPhone(phone);
                		entity.setStatus("0");
                		entity.setPwd("");
                		DBGesturePwdManager.getInstance(getApplicationContext()).updateGestureEntity(entity);
                		setGesturePwd.setVisibility(View.GONE);
                		line.setVisibility(View.GONE);
            		}else{
            			Intent intent = new Intent(GestureSettingActivity.this,GestureEditActivity.class);
                		startActivityForResult(intent, 100);
            		}
            	}else{
            		promptText.setText("�������");
            	}
			}
		});
        //��������������ˣ���������ʾ����  
        dialog.show();  
      //����dialog�Ŀ��
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth()*6/7;
        dialog.getWindow().setAttributes(lp);
	}
	
}
