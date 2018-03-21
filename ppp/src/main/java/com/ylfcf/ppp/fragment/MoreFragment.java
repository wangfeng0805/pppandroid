package com.ylfcf.ppp.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncAPIQuery;
import com.ylfcf.ppp.entity.AppInfo;
import com.ylfcf.ppp.inter.Inter.OnApiQueryBack;
import com.ylfcf.ppp.ui.ArticleListActivity;
import com.ylfcf.ppp.ui.BrandIntroActivity;
import com.ylfcf.ppp.ui.CommQuesActivity;
import com.ylfcf.ppp.ui.MainFragmentActivity;
import com.ylfcf.ppp.ui.MainFragmentActivity.OnDownLoadListener;
import com.ylfcf.ppp.ui.MainFragmentActivity.OnMoreFragmentLogoutListener;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.util.YLFLogger;
import com.ylfcf.ppp.view.UpdatePopupwindow;
import com.ylfcf.ppp.widget.LoadingDialog;


/**
 * ����
 * @author Administrator
 *
 */
public class MoreFragment extends BaseFragment implements OnClickListener{
	private static final String className = "MoreFragment";
	private MainFragmentActivity mainActivity;
	
	private LinearLayout ppjsLayout;//Ʒ�ƽ���
	private LinearLayout xwggLayout;//���Ź���
	private LinearLayout cjwtLayout;//��������
	private LinearLayout kfrxLayout;//�ͷ�����
	private LinearLayout jcxbbLayout;//����°汾
	private LinearLayout mainlayout;
	private ToggleButton msgTenderBtn;
	private TextView versionText;
	
	private Button logoutBtn;
	
	private View topLayout;
	private TextView topTitle;
	private LinearLayout topbarLeftLayout;
	private View rootView;
	private LoadingDialog mLoadingDialog;
	
	/**
	 * ������ǰFragment��ʵ������
	 * @param position
	 * @return
	 */
	static OnMoreFragmentLogoutListener logoutSucListener;
	public static Fragment newInstance(int position,OnMoreFragmentLogoutListener logoutListener) {
		MoreFragment f = new MoreFragment();
		logoutSucListener = logoutListener;
		Bundle args = new Bundle();
		args.putInt("num", position);
		f.setArguments(args);
		return f;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mainActivity = (MainFragmentActivity) getActivity();
		mLoadingDialog = new LoadingDialog(mainActivity,"���ڼ���...",R.anim.loading);
		if(rootView==null){
            rootView=inflater.inflate(R.layout.more_fragment, null);
            findViews(rootView);
        }
		//�����rootView��Ҫ�ж��Ƿ��Ѿ����ӹ�parent�� �����parent��Ҫ��parentɾ����Ҫ��Ȼ�ᷢ�����rootview�Ѿ���parent�Ĵ���
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        } 
        return rootView;
	}

	private void findViews(View view){
		topLayout = view.findViewById(R.id.more_fragment_toplayout);
		topbarLeftLayout = (LinearLayout) topLayout.findViewById(R.id.common_topbar_left_layout);
		topbarLeftLayout.setVisibility(View.GONE);
		topTitle = (TextView)topLayout.findViewById(R.id.common_page_title);
		topTitle.setText("����");
		
		mainlayout = (LinearLayout)view.findViewById(R.id.more_fragment_mainlayout);
		ppjsLayout = (LinearLayout)view.findViewById(R.id.more_ppjs_layout);
		ppjsLayout.setOnClickListener(this);
		xwggLayout = (LinearLayout)view.findViewById(R.id.more_xwgg_layout);
		xwggLayout.setOnClickListener(this);
		cjwtLayout = (LinearLayout)view.findViewById(R.id.more_cjwt_layout);
		cjwtLayout.setOnClickListener(this);
		kfrxLayout = (LinearLayout)view.findViewById(R.id.more_kfrx_layout);
		kfrxLayout.setOnClickListener(this);
		jcxbbLayout = (LinearLayout)view.findViewById(R.id.more_jcxbb_layout);
		jcxbbLayout.setOnClickListener(this);
		logoutBtn = (Button)view.findViewById(R.id.more_fragment_logout_btn);
		logoutBtn.setOnClickListener(this);
		versionText = (TextView)view.findViewById(R.id.more_fragment_versioncode);
		versionText.setText("V"+Util.getVersionName(mainActivity));
		boolean sendMsgFlag = SettingsManager.getMsgSendFlag(mainActivity.getApplicationContext());
		msgTenderBtn = (ToggleButton) view.findViewById(R.id.morefragment_msgtender_toggle);
		if(sendMsgFlag){
			msgTenderBtn.setChecked(true);
		}else{
			msgTenderBtn.setChecked(false);
		}
		msgTenderBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					//��
//					mainActivity.mPushAgent.enable();
					SettingsManager.setMsgSendFlag(mainActivity.getApplicationContext(), true);
				}else{
					//�ر�
//					mainActivity.mPushAgent.disable();
					SettingsManager.setMsgSendFlag(mainActivity.getApplicationContext(), false);
				}
			}
		});
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		YLFLogger.d("MoreFragment ------onResume()");
		UMengStatistics.statisticsOnPageStart(className);
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser){
			if(mainActivity == null){
				mainActivity = (MainFragmentActivity) getActivity();
			}
			String userId = SettingsManager.getUserId(mainActivity.getApplicationContext());
			if(userId != null && !"".equals(userId)){
				if(logoutBtn != null){
					logoutBtn.setVisibility(View.VISIBLE);
				}
			}else{
				if(logoutBtn != null){
					logoutBtn.setVisibility(View.GONE);
				}
			}
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		UMengStatistics.statisticsOnPageEnd(className);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.more_ppjs_layout:
			Intent intent = new Intent(mainActivity,BrandIntroActivity.class);
			startActivity(intent);
			break;
		case R.id.more_xwgg_layout:
			//���Ź���
			Intent intentAr = new Intent(mainActivity,ArticleListActivity.class);
			startActivity(intentAr);
			break;
		case R.id.more_cjwt_layout:
			Intent intentCommQues = new Intent(mainActivity,CommQuesActivity.class);
			startActivity(intentCommQues);
			break;
		case R.id.more_kfrx_layout:
			contactUs("4001501568");
			break;
		case R.id.more_jcxbb_layout:
			checkVersion(Util.getClientVersion(mainActivity));
			break;
		case R.id.more_fragment_logout_btn:
			showLogoutDialog();
			break;
		default:
			break;
		}
	}
	
	/**
	 * �û��˳�
	 */
	private void logout(){
		if(mLoadingDialog != null){
			mLoadingDialog.show();
			SettingsManager.setUser(mainActivity,"");
			SettingsManager.setLoginPassword(mainActivity,"",true);
			SettingsManager.setUserId(mainActivity,"");
			SettingsManager.setUserName(mainActivity,"");
			SettingsManager.setUserRegTime(mainActivity, "");
			SettingsManager.setUserType(mainActivity, "");
			SettingsManager.setCompPhone(mainActivity, "");
			
			new Handler().postDelayed(new Runnable(){
				@Override
				public void run() {
					mLoadingDialog.dismiss();
					logoutBtn.setVisibility(View.GONE);
					Util.toastShort(mainActivity, "�û����˳�");
					logoutSucListener.onLogoutSuc();
				}
			}, 500L);
		}
	}
	
	private void contactUs(String phoneNumber){
		Intent intent = new Intent(Intent.ACTION_DIAL);
	    intent.setData(Uri.parse("tel:" + phoneNumber));
	    if (intent.resolveActivity(mainActivity.getPackageManager()) != null) {
	        startActivity(intent);
	    }
	}
	/**
	 * ��¼�ɹ��ص�
	 * @author Administrator
	 *
	 */
	public interface OnLoginSuccessListener{
		public void onLoginSuccess();
	}
	
	/**
	 * ����Ƿ����°汾
	 */
	private void checkVersion(int versionCode){
		if(mLoadingDialog != null){
			mLoadingDialog.show();
		}
		AsyncAPIQuery apiQueryTask = new AsyncAPIQuery(mainActivity, versionCode, new OnApiQueryBack() {
			@Override
			public void back(AppInfo info) {
				if(mLoadingDialog != null && mLoadingDialog.isShowing()){
					mLoadingDialog.dismiss();
				}
				if(info != null){
					String flag = info.getDo_update();
					if("true".equals(flag)){
						//��Ҫ����
						showUpdateDialog(info);
					}else{
						//����Ҫ����
						Util.toastLong(mainActivity, "�������°汾");
					}
				}else{
				}
			}
		});
		apiQueryTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	
	private void showUpdateDialog(AppInfo info) {
		View popView = LayoutInflater.from(mainActivity).inflate(
				R.layout.update_window_layout, null);
		int[] screen = SettingsManager.getScreenDispaly(mainActivity);
		int width = screen[0] * 4 / 5;
		int height = screen[1] * 3 / 5 + 20;
		UpdatePopupwindow popwindow = new UpdatePopupwindow(mainActivity,
				popView, width, height, info, mainActivity.downManager, new OnDownLoadListener() {
			@Override
			public void onDownLoad(long las) {
				mainActivity.startDownloadAPK();
			}
		}, new MainFragmentActivity.OnUpdateWindowDismiss() {
			@Override
			public void onDismiss() {
			}
		});
		popwindow.show(mainlayout);
	}
	
	/**
	 * �˳���¼��Dialog
	 */
	private void showLogoutDialog(){
		View contentView = LayoutInflater.from(mainActivity).inflate(R.layout.logout_dialog, null);
		final Button sureBtn = (Button) contentView.findViewById(R.id.logout_dialog_sure_btn);
		final Button cancelBtn = (Button) contentView.findViewById(R.id.logout_dialog_cancel_btn);
		AlertDialog.Builder builder=new AlertDialog.Builder(mainActivity, R.style.Dialog_Transparent);  //�ȵõ�������  
        builder.setView(contentView);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        sureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				logout();
			}
		});
        cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
        //��������������ˣ���������ʾ����  
        dialog.show();  
        WindowManager windowManager = mainActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth()*6/7;
        dialog.getWindow().setAttributes(lp);
	}
}
