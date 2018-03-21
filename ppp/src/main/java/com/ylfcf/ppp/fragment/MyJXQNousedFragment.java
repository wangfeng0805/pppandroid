package com.ylfcf.ppp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.MyJXQListAdapter;
import com.ylfcf.ppp.adapter.MyJXQListAdapter.OnJXQItemClickListener;
import com.ylfcf.ppp.async.AsyncJXQPageInfo;
import com.ylfcf.ppp.async.AsyncJXQTransfer;
import com.ylfcf.ppp.async.AsyncJXQTransferGetSubUser;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.JiaxiquanInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.ui.BorrowListZXDActivity;
import com.ylfcf.ppp.ui.MyJXQActivity;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ��Ϣȯδʹ��
 * @author Mr.liu
 *
 */
public class MyJXQNousedFragment extends BaseFragment{
	private static final String className = "MyJXQNousedFragment";
	private MyJXQActivity.OnJXQNousedTransferSucListener mOnJXQNousedTransferSucListener;
	public final int REQUEST_JXQ_LIST_WHAT = 1800;
	private final int REQUEST_JXQ_LIST_SUCCESS = 1801;
	private final int REQUEST_JXQ_LIST_FAILE = 1802;

	private MyJXQActivity mainActivity;
	private View rootView;

	private MyJXQListAdapter mMyJXQListAdapter;
	private PullToRefreshListView pullToRefreshListView;
	private TextView nodataText;
	private List<JiaxiquanInfo> jxqList = new ArrayList<JiaxiquanInfo>();

	private int pageNo = 1;
	private int pageSize = Integer.MAX_VALUE;
	private boolean isFirst = true;
	private boolean isLoadMore = false;// ���ظ���
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_JXQ_LIST_WHAT:
				requestJXQList(SettingsManager.getUserId(mainActivity
						.getApplicationContext()), "δʹ��");
				break;
			case REQUEST_JXQ_LIST_SUCCESS:
				Date endDate = null;
				BaseInfo baseInfo = (BaseInfo) msg.obj;
				pullToRefreshListView.setVisibility(View.VISIBLE);
				nodataText.setVisibility(View.GONE);
				if (baseInfo != null) {
					if (!isLoadMore) {
						jxqList.clear();
					}
					for(int i=0;i<baseInfo.getmJiaxiquanPageInfo().getInfoList().size();i++){
						JiaxiquanInfo info = baseInfo.getmJiaxiquanPageInfo().getInfoList().get(i);
						try {
							endDate = sdf.parse(info.getEffective_end_time());
							if(endDate.compareTo(sdf.parse(baseInfo.getTime())) == 1){
								//��ʾ��Ϣȯ��δ����
								jxqList.add(info);
							}
						} catch (Exception e) {
						}
					}

					if(jxqList.size() <= 0){
						pullToRefreshListView.setVisibility(View.GONE);
						nodataText.setVisibility(View.VISIBLE);
					}else{
						pullToRefreshListView.setVisibility(View.VISIBLE);
						nodataText.setVisibility(View.GONE);
						mMyJXQListAdapter.setItems(jxqList,baseInfo.getTime());
					}
				}
				isLoadMore = false;
				pullToRefreshListView.onRefreshComplete();
				break;
			case REQUEST_JXQ_LIST_FAILE:
				if(!isLoadMore){
					pullToRefreshListView.setVisibility(View.GONE);
					nodataText.setVisibility(View.VISIBLE);
				}
				pullToRefreshListView.onRefreshComplete();
				break;
			default:
				break;
			}
		}
	};

	public MyJXQNousedFragment(MyJXQActivity.OnJXQNousedTransferSucListener mOnJXQNousedTransferSucListener){
		this.mOnJXQNousedTransferSucListener = mOnJXQNousedTransferSucListener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainActivity = (MyJXQActivity) getActivity();
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.myhb_noused_fragment, null);
			findViews(rootView);
		}
		// �����rootView��Ҫ�ж��Ƿ��Ѿ����ӹ�parent��
		// �����parent��Ҫ��parentɾ����Ҫ��Ȼ�ᷢ�����rootview�Ѿ���parent�Ĵ���
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser){
			handler.sendEmptyMessage(REQUEST_JXQ_LIST_WHAT);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		UMengStatistics.statisticsOnPageStart(className);//����ͳ��ҳ����ת
	}

	@Override
	public void onPause() {
		super.onPause();
		UMengStatistics.statisticsOnPageEnd(className);//����ͳ��ҳ����ת
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}
	
	private void findViews(View view) {
		pullToRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.myhb_noused_fragment_pull_refresh_list);
		nodataText = (TextView) view
				.findViewById(R.id.myhb_noused_fragment_nodata);
		mMyJXQListAdapter = new MyJXQListAdapter(mainActivity,
				new OnJXQItemClickListener() {
					@Override
					public void onClick(JiaxiquanInfo jxqInfo,int position) {
						if("0".equals(jxqInfo.getTransfer())){
							Intent intent = new Intent(mainActivity,BorrowListZXDActivity.class);
							startActivity(intent);
						}else if("1".equals(jxqInfo.getTransfer())){
							//ת�ü�Ϣȯ
							showJXQTransferEditDialog(jxqInfo);
						}
					}
				});
		pullToRefreshListView.setAdapter(mMyJXQListAdapter);
		initListeners();
	}

	private void initListeners() {
		pullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// ����ˢ��
						isLoadMore = false;
						pageNo = 1;
						handler.sendEmptyMessage(REQUEST_JXQ_LIST_WHAT);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// �������ظ���
						pageNo++;
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								isLoadMore = true;
								handler.sendEmptyMessage(REQUEST_JXQ_LIST_WHAT);
							}
						}, 1000L);

					}
				});
	}

	/**
	 * ��Ϣȯת��
	 */
	private void showJXQTransferEditDialog(final JiaxiquanInfo jxqInfo){
		View contentView = LayoutInflater.from(mainActivity).inflate(R.layout.jxq_transfer_edit_dialog_layout, null);
		final Button okBtn = (Button) contentView.findViewById(R.id.jxq_transfer_edit_dialog_okbtn);
		ImageView delBtn = (ImageView) contentView.findViewById(R.id.jxq_transfer_edit_dialog_delbtn);
		final EditText phoneET = (EditText) contentView.findViewById(R.id.jxq_transfer_edit_dialog_phone);
		final TextView promptTV = (TextView) contentView.findViewById(R.id.jxq_transfer_edit_dialog_prompt);
		AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity, R.style.Dialog_Transparent);  //�ȵõ�������
		builder.setView(contentView);
		builder.setCancelable(false);
		final AlertDialog dialog = builder.create();
		okBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkReceiverData(phoneET,promptTV,dialog,jxqInfo);
			}
		});
		delBtn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		//��������������ˣ���������ʾ����
		dialog.show();
		okBtn.requestFocus();
		okBtn.requestFocusFromTouch();
		WindowManager windowManager = mainActivity.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = display.getWidth()*6/7;
		dialog.getWindow().setAttributes(lp);
	}

	/**
	 * �Ƿ񽫼�Ϣȯת�ø�xxx
	 */
	private void showJXQTransferPromptDialog(final UserInfo userInfo,final JiaxiquanInfo jxqInfo){
		View contentView = LayoutInflater.from(mainActivity).inflate(R.layout.jxq_transfer_prompt_dialog_layout, null);
		final Button okBtn = (Button) contentView.findViewById(R.id.jxq_transfer_prompt_dialog_okbtn);
		ImageView delBtn = (ImageView) contentView.findViewById(R.id.jxq_transfer_prompt_dialog_delbtn);
		final TextView contentTV = (TextView) contentView.findViewById(R.id.jxq_transfer_prompt_dialog_content);
		if(userInfo.getReal_name() != null && !"".equals(userInfo.getReal_name())){
			contentTV.setText("ȷ���Ѽ�Ϣȯת�ø�"+Util.hidRealName2(userInfo.getReal_name())+"���ֻ��ţ�"+Util.hidPhoneNum(userInfo.getPhone())+"?");
		}else{
			contentTV.setText("ȷ���Ѽ�Ϣȯת�ø�"+"�ֻ��ţ�"+Util.hidPhoneNum(userInfo.getPhone())+"?");
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity, R.style.Dialog_Transparent);  //�ȵõ�������
		builder.setView(contentView);
		builder.setCancelable(false);
		final AlertDialog dialog = builder.create();
		okBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				requestTransferAddinterest(userInfo,jxqInfo);
			}
		});
		delBtn.setOnClickListener(new View.OnClickListener(){
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

	/**
	 * ��Ϣȯת�óɹ�
	 */
	private void showJXQTransferSucDialog(UserInfo userInfo,JiaxiquanInfo jxqInfo){
		View contentView = LayoutInflater.from(mainActivity).inflate(R.layout.jxq_transfer_suc_dialog_layout, null);
		final Button okBtn = (Button) contentView.findViewById(R.id.jxq_transfer_suc_dialog_okbtn);
		ImageView delBtn = (ImageView) contentView.findViewById(R.id.jxq_transfer_suc_dialog_delbtn);
		TextView receiverName = (TextView) contentView.findViewById(R.id.jxq_transfer_suc_dialog_receivername);
		TextView receiverPhone = (TextView) contentView.findViewById(R.id.jxq_transfer_suc_dialog_receiverphone);
		if(userInfo.getReal_name() != null && !"".equals(userInfo.getReal_name())){
			receiverName.setVisibility(View.VISIBLE);
			receiverName.setText("����������: "+Util.hidRealName2(userInfo.getReal_name()));
		}else{
			receiverName.setVisibility(View.GONE);
		}
		receiverPhone.setText("�������ֻ���: "+Util.hidPhoneNum(userInfo.getPhone()));
		AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity, R.style.Dialog_Transparent);  //�ȵõ�������
		builder.setView(contentView);
		builder.setCancelable(false);
		final AlertDialog dialog = builder.create();
		okBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				mOnJXQNousedTransferSucListener.onSuccess();
			}
		});
		delBtn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				mOnJXQNousedTransferSucListener.onSuccess();
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

	private void checkReceiverData(EditText phoneET,TextView promptTV,AlertDialog dialog,JiaxiquanInfo jxqInfo){
		if(phoneET == null || promptTV == null)
			return;
		String phone = phoneET.getEditableText().toString();
		if(Util.checkPhoneNumber(phone)){
			promptTV.setVisibility(View.GONE);
			requestSubUser(phone,SettingsManager.getUserId(mainActivity),promptTV,dialog,jxqInfo);
		}else{
			promptTV.setVisibility(View.VISIBLE);
			promptTV.setText("��������ȷ���ֻ���");
		}
	}

	/**
	 * ת�ü�Ϣȯ
	 * @param userInfo
	 * @param jxqInfo
	 */
	private void requestTransferAddinterest(final UserInfo userInfo,final JiaxiquanInfo jxqInfo){
		AsyncJXQTransfer transferTask = new AsyncJXQTransfer(mainActivity, userInfo.getId(), jxqInfo.getId(), new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						showJXQTransferSucDialog(userInfo,jxqInfo);
					}else{
						Util.toastLong(mainActivity,"�˼�Ϣȯ�ѱ�ת��");
					}
				}else{
					Util.toastLong(mainActivity,"ת��ʧ�ܣ����������쳣");
				}
			}
		});
		transferTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * ��ȡ���ʦ��ֱ�Ӻ���
	 * @param phone
	 * @param userId
	 */
	private void requestSubUser(final String phone,String userId,final TextView promptTV,final AlertDialog dialog,final JiaxiquanInfo jxqInfo){
		AsyncJXQTransferGetSubUser subUserTask = new AsyncJXQTransferGetSubUser(mainActivity, phone, userId, new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						//����ֱ�Ӻ���
						UserInfo userInfo = baseInfo.getUserInfo();
						userInfo.setPhone(phone);
						dialog.dismiss();
						promptTV.setVisibility(View.GONE);
						showJXQTransferPromptDialog(userInfo,jxqInfo);
					}else if(resultCode == -1){
						//������ֱ�Ӻ���
						promptTV.setVisibility(View.VISIBLE);
						promptTV.setText("���ֻ��Ų�������ֱ�Ӻ��ѣ�����ת��");
					}else{
						Util.toastLong(mainActivity,baseInfo.getMsg());
					}
				}else{
					Util.toastLong(mainActivity,"���������쳣");
				}
			}
		});
		subUserTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	private void requestJXQList(String userId, String useStatus) {
		if (isFirst) {
			mainActivity.loadingDialog.show();
		}
		isFirst = false;
		AsyncJXQPageInfo redbagTask = new AsyncJXQPageInfo(mainActivity, userId,useStatus,
				String.valueOf(pageNo),String.valueOf(pageSize), new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (mainActivity.loadingDialog.isShowing()) {
							mainActivity.loadingDialog.dismiss();
						}

						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								Message msg = handler.obtainMessage(REQUEST_JXQ_LIST_SUCCESS);
								msg.obj = baseInfo;
								handler.sendMessage(msg);
							} else {
								Message msg = handler.obtainMessage(REQUEST_JXQ_LIST_FAILE);
								msg.obj = baseInfo.getError();
								handler.sendMessage(msg);
							}
						} else {
							Message msg = handler
									.obtainMessage(REQUEST_JXQ_LIST_FAILE);
							handler.sendMessage(msg);
						}
					}
				});
		redbagTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
