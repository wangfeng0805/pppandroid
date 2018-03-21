package com.ylfcf.ppp.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.RedBagAdapter;
import com.ylfcf.ppp.async.AsyncRedbgList;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.RedBagInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.ui.MyHongbaoActivity;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;

import java.util.ArrayList;
import java.util.List;

/**
 * �ҵĺ�� ---  �ѹ���
 * @author Administrator
 *
 */
public class MyHBOverdueFragment extends BaseFragment{
	private static final String className = "MyHBOverdueFragment";
	public final int REQUEST_HB_LIST_WHAT = 1800;
	private final int REQUEST_HB_LIST_SUCCESS = 1801;
	private final int REQUEST_HB_LIST_FAILE = 1802;
	
	private MyHongbaoActivity mainActivity;
	private View rootView;
	
	private RedBagAdapter redBagAdapter;
	private PullToRefreshListView pullToRefreshListView;
	private TextView nodataText;
	private List<RedBagInfo> redbagList = new ArrayList<RedBagInfo>();
	
	private int pageNo = 0;
	private int pageSize = 10;
	private boolean isFirst = true;
	private boolean isLoadMore = false;//���ظ���
	private boolean isRefresh = false;//����ˢ��
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_HB_LIST_WHAT:
				requestHBList(SettingsManager.getUserId(mainActivity.getApplicationContext()), "3");
				break;
			case REQUEST_HB_LIST_SUCCESS:
				pullToRefreshListView.setVisibility(View.VISIBLE);
				nodataText.setVisibility(View.GONE);
				BaseInfo baseInfo = (BaseInfo) msg.obj;
				if(baseInfo != null){
					if(!isLoadMore){
						redbagList.clear();
					}
					redbagList.addAll(baseInfo.getmRedBagPageInfo().getRedbagList());
					redBagAdapter.setItems(redbagList,baseInfo.getTime());
				}
				isLoadMore = false;
				pullToRefreshListView.onRefreshComplete();
				break;
			case REQUEST_HB_LIST_FAILE:
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
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainActivity = (MyHongbaoActivity) getActivity();
		if(rootView==null){
            rootView=inflater.inflate(R.layout.myhb_overdue_fragment, null);
            findViews(rootView);
        }
		//�����rootView��Ҫ�ж��Ƿ��Ѿ����ӹ�parent�� �����parent��Ҫ��parentɾ����Ҫ��Ȼ�ᷢ�����rootview�Ѿ���parent�Ĵ���
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        } 
        handler.sendEmptyMessage(REQUEST_HB_LIST_WHAT);
        return rootView;
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
	
	private void findViews(View view){
		pullToRefreshListView = (PullToRefreshListView)view.findViewById(R.id.myhb_overdue_fragment_pull_refresh_list);
		nodataText = (TextView)view.findViewById(R.id.myhb_overdue_fragment_nodata);
		redBagAdapter = new RedBagAdapter(mainActivity,null);
		pullToRefreshListView.setAdapter(redBagAdapter);
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
						pageNo = 0;
						handler.sendEmptyMessage(REQUEST_HB_LIST_WHAT);
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
								handler.sendEmptyMessage(REQUEST_HB_LIST_WHAT);
							}
						}, 1000L);
					}
				});
	}
	
	private void requestHBList(String userId,String flag){
		AsyncRedbgList redbagTask = new AsyncRedbgList(mainActivity, userId, flag,
				String.valueOf(pageNo),String.valueOf(pageSize),
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								Message msg = handler.obtainMessage(REQUEST_HB_LIST_SUCCESS);
								msg.obj = baseInfo;
								handler.sendMessage(msg);
							}else{
								Message msg = handler.obtainMessage(REQUEST_HB_LIST_FAILE);
								msg.obj = baseInfo.getError();
								handler.sendMessage(msg);
							}
						}else{
							Message msg = handler.obtainMessage(REQUEST_HB_LIST_FAILE);
							handler.sendMessage(msg);
						}
					}
				});
		redbagTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
