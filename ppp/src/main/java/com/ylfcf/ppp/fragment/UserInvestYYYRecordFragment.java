package com.ylfcf.ppp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.UserInvestYYYRecordAdapter;
import com.ylfcf.ppp.adapter.UserInvestYYYRecordAdapter.OnYYYItemClickListener;
import com.ylfcf.ppp.async.AsyncInvestYYYTotalRecord;
import com.ylfcf.ppp.async.AsyncYYYApplyCancelReturn;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.InvestRecordPageInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.ui.BorrowDetailYYYActivity;
import com.ylfcf.ppp.ui.CompactActivity;
import com.ylfcf.ppp.ui.UserInvestRecordActivity;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * �û�Ͷ�ʼ�¼ --- Ԫ��ӯ
 * @author Mr.liu
 *
 */
public class UserInvestYYYRecordFragment extends BaseFragment{
	private static final String className = "UserInvestYYYRecordFragment";
	private static final int REQUEST_INVEST_RECORD_WHAT = 1021;
	private static final int REQUEST_INVEST_RECORD_SUCCESS = 1022;
	private static final int REQUEST_INVEST_RECORD_NODATA = 1023;	//������
	
	private static final int REQUEST_APPLY_RETURN_WHAT = 1024;//ԤԼ���
	private static final int REQUEST_CANCEL_RETURN_WHAT = 1025;//�������

	private UserInvestRecordActivity mainActivity;
	private View rootView;

	private UserInvestYYYRecordAdapter investRecordsAdapter;
	private PullToRefreshListView pullToRefreshListView;
	private TextView nodataText;
	private List<InvestRecordInfo> investRecordList = new ArrayList<InvestRecordInfo>();

	//�������
	private String status = "";//Ͷ����
	private int pageNo = 0;
	private int pageSize = 20;
	private boolean isFirst = true;
	private boolean isLoadMore = false;// ���ظ���
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_INVEST_RECORD_WHAT:
				getInvestRecordList(SettingsManager.getUserId(mainActivity.getApplicationContext()));
				break;
			case REQUEST_INVEST_RECORD_SUCCESS:
				InvestRecordPageInfo pageInfo = (InvestRecordPageInfo) msg.obj;
				pullToRefreshListView.setVisibility(View.VISIBLE);
				nodataText.setVisibility(View.GONE);
				if (pageInfo != null) {
					if (!isLoadMore) {
						investRecordList.clear();
					}
					investRecordList.addAll(pageInfo.getInvestRecordList());
					investRecordsAdapter.setItems(investRecordList);
				}
				isLoadMore = false;
				pullToRefreshListView.onRefreshComplete();
				break;
			case REQUEST_INVEST_RECORD_NODATA:
				if(!isLoadMore){
					pullToRefreshListView.setVisibility(View.GONE);
					nodataText.setVisibility(View.VISIBLE);
				}
				pullToRefreshListView.onRefreshComplete();
				break;
			case REQUEST_APPLY_RETURN_WHAT:
				InvestRecordInfo infoApply = (InvestRecordInfo) msg.obj;
				applyOrCancelReturn(infoApply,infoApply.getId(), SettingsManager.getUserId(mainActivity), "1");
				break;
			case REQUEST_CANCEL_RETURN_WHAT:
				InvestRecordInfo infoCancel = (InvestRecordInfo) msg.obj;
				applyOrCancelReturn(infoCancel,infoCancel.getId(), SettingsManager.getUserId(mainActivity), "2");
				break;
			default:
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainActivity = (UserInvestRecordActivity) getActivity();
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.userinvest_yyy_record_fragment, null);
			findViews(rootView);
		}
		// �����rootView��Ҫ�ж��Ƿ��Ѿ����ӹ�parent��
		// �����parent��Ҫ��parentɾ����Ҫ��Ȼ�ᷢ�����rootview�Ѿ���parent�Ĵ���
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		handler.sendEmptyMessage(REQUEST_INVEST_RECORD_WHAT);
		return rootView;
	}

	private void findViews(View view) {
		pullToRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.userinvest_yyy_record_fragment_pull_refresh_list);
		nodataText = (TextView) view
				.findViewById(R.id.userinvest_yyy_record_fragment_nodata);
		LinearLayout topLayout = (LinearLayout)LayoutInflater.from(mainActivity).inflate(R.layout.yyy_investrecord_top_layout, null);
		pullToRefreshListView.getRefreshableView().addHeaderView(topLayout);
		investRecordsAdapter = new UserInvestYYYRecordAdapter(mainActivity,new OnYYYItemClickListener() {
			@Override       
			public void onCatCompact(int position, View v, InvestRecordInfo info) {
				//����鿴��ͬ
				Intent intent = new Intent(mainActivity,CompactActivity.class);
				intent.putExtra("invest_record", info);
				intent.putExtra("from_where", "yyy");
				startActivity(intent);
			}
			
			@Override
			public void onApplyOrCancel(int position, View v, InvestRecordInfo info) {
				//���ԤԼ���߳������
				if("Ͷ����".equals(info.getReturn_status())){
					//����ԤԼ���
					showReuqestApplyReturnDialog(info);
				}else if("�����".equals(info.getReturn_status())){
					//���������
					showReuqestCancelReturnDialog(info);
				}
			}
		},"yyy_record");
		pullToRefreshListView.setAdapter(investRecordsAdapter);
		initListeners();
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
	
	private void initListeners() {
		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// ����ˢ��
						pageNo = 0;
						handler.sendEmptyMessage(REQUEST_INVEST_RECORD_WHAT);
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
								handler.sendEmptyMessage(REQUEST_INVEST_RECORD_WHAT);
							}
						}, 1000L);
					}
				});
		pullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				InvestRecordInfo info = (InvestRecordInfo) parent.getItemAtPosition(position);
				if(info == null){
					return;
				}
				info.setBorrow_type("Ԫ��ӯ");
				Intent intent = new Intent(mainActivity,BorrowDetailYYYActivity.class);
				intent.putExtra("InvestRecordInfo", info);
				startActivity(intent);
			}
		});
	}

	/**
	 * ����ԤԼ���ʱ��dialog
	 */
	private void showReuqestApplyReturnDialog(final InvestRecordInfo info){
		View contentView = LayoutInflater.from(mainActivity).inflate(R.layout.apply_return_dialog, null);
		final Button sureBtn = (Button) contentView.findViewById(R.id.apply_return_dialog_sure_btn);
		final Button cancelBtn = (Button) contentView.findViewById(R.id.apply_return_dialog_cancel_btn);
		final TextView money = (TextView) contentView.findViewById(R.id.apply_return_dialog_money);
		final TextView nearEndTime = (TextView)contentView.findViewById(R.id.apply_return_dialog_nearendtime);
		double returnTotalMoneyD = 0d;
		try {
			returnTotalMoneyD = Double.parseDouble(info.getReturn_total_money());
		} catch (Exception e) {
		}
		money.setText("�������" + Util.double2PointDouble(returnTotalMoneyD) + "Ԫ");
		nearEndTime.setText("Ԥ�Ƶ����գ�<"+info.getInterest_end_time().split(" ")[0]+"> + 1��");
		
		AlertDialog.Builder builder=new AlertDialog.Builder(mainActivity, R.style.Dialog_Transparent);  //�ȵõ�������  
        builder.setView(contentView);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        sureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//����ԤԼ���
				Message msg = handler.obtainMessage(REQUEST_APPLY_RETURN_WHAT);
				msg.obj = info;
				handler.sendMessage(msg);
				dialog.dismiss();
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
	
	/**
	 * ���������ʱ��dialog
	 */
	private void showReuqestCancelReturnDialog(final InvestRecordInfo info){
		View contentView = LayoutInflater.from(mainActivity).inflate(R.layout.cancel_return_dialog, null);
		final Button sureBtn = (Button) contentView.findViewById(R.id.cancel_return_dialog_sure_btn);
		final Button cancelBtn = (Button) contentView.findViewById(R.id.cancel_return_dialog_cancel_btn);
		final TextView content = (TextView) contentView.findViewById(R.id.cancel_return_dialog_content);
		content.setText("ȷ�ϳ�����أ�");
		AlertDialog.Builder builder=new AlertDialog.Builder(mainActivity, R.style.Dialog_Transparent);  //�ȵõ�������  
        builder.setView(contentView);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        sureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Message msg = handler.obtainMessage(REQUEST_CANCEL_RETURN_WHAT);
				msg.obj = info;
				handler.sendMessage(msg);
//				applyOrCancelReturn(info,info.getId(), SettingsManager.getUserId(UserYYYInvestRecordActivity.this), "2");
				dialog.dismiss();
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
	
	/**
	 * ԤԼ�������ɹ���dialog
	 */
	private void showApplyReturnSuccessDialog(String type){
		View contentView = LayoutInflater.from(mainActivity).inflate(R.layout.common_popwindow, null);
		final Button sureBtn = (Button) contentView.findViewById(R.id.common_popwindow_btn);
		sureBtn.setText("ȷ��");
		final TextView content = (TextView) contentView.findViewById(R.id.common_popwindow_content);
		if("1".equals(type)){
			content.setText("ԤԼ����ύ�ɹ���");
		}else if("2".equals(type)){
			content.setText("��������ύ�ɹ���");
		}
		
		AlertDialog.Builder builder=new AlertDialog.Builder(mainActivity, R.style.Dialog_Transparent);  //�ȵõ�������  
        builder.setView(contentView);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        sureBtn.setOnClickListener(new OnClickListener() {
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
	 * ԤԼ���߳������
	 * @param investId
	 * @param userId
	 * @param type
	 */
	private void applyOrCancelReturn(InvestRecordInfo info,String investId,String userId,final String type){
		if(mainActivity.loadingDialog != null){
			mainActivity.loadingDialog.show();
		}
		AsyncYYYApplyCancelReturn returnTask = new AsyncYYYApplyCancelReturn(mainActivity, investId, userId, type, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(mainActivity.loadingDialog != null){
							mainActivity.loadingDialog.dismiss();
						}
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								handler.sendEmptyMessage(REQUEST_INVEST_RECORD_WHAT);
								showApplyReturnSuccessDialog(type);
								Util.toastLong(mainActivity, baseInfo.getMsg());
							}else{
								Util.toastLong(mainActivity, baseInfo.getMsg());
							}
						}
					}
				});
		returnTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * ��ȡͶ�ʼ�¼�б�
	 * @param investUserId
	 */
	private void getInvestRecordList(String investUserId){
		isFirst = false;
		AsyncInvestYYYTotalRecord asyncInvestRecord = new AsyncInvestYYYTotalRecord(mainActivity, investUserId, 
				pageNo, pageSize, new OnCommonInter(){
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								Message msg = handler.obtainMessage(REQUEST_INVEST_RECORD_SUCCESS);
								msg.obj = baseInfo.getmInvestRecordPageInfo();
								handler.sendMessage(msg);
							}else{
								Message msg = handler.obtainMessage(REQUEST_INVEST_RECORD_NODATA);
								msg.obj = baseInfo.getMsg();
								handler.sendMessage(msg);
							}
						}
					}
		});
		asyncInvestRecord.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

}
