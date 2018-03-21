package com.ylfcf.ppp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.TYJListAdapter;
import com.ylfcf.ppp.adapter.TYJListAdapter.OnTYJListItemClickListener;
import com.ylfcf.ppp.entity.TYJInfo;
import com.ylfcf.ppp.ui.MainFragmentActivity;
import com.ylfcf.ppp.ui.MyTYJActivity;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;

import java.util.List;

/**
 * �ҵ������---δʹ��
 * @author jianbing
 *
 */
public class MyTYJNousedFragment extends BaseFragment{
	private static final String className = "MyTYJNousedFragment";
	private MyTYJActivity mytyjActivity;
	private View rootView;
	private TextView nodataText;
	
	private ListView nousedListView;
	private TYJListAdapter tyjAdapter;
	private int pageNo = 0;
	private int pageSize = 20;
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mytyjActivity = (MyTYJActivity) getActivity();
		if(rootView==null){
            rootView=inflater.inflate(R.layout.mytyj_noused_fragment, null);
            findViews(rootView);
        }
		//�����rootView��Ҫ�ж��Ƿ��Ѿ����ӹ�parent�� �����parent��Ҫ��parentɾ����Ҫ��Ȼ�ᷢ�����rootview�Ѿ���parent�Ĵ���
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        } 
        return rootView;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
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

	private void findViews(View view){
		nousedListView = (ListView) view.findViewById(R.id.mytyj_noused_fragment_listview);
		nodataText = (TextView)view.findViewById(R.id.mytyj_noused_fragment_nodata);
		tyjAdapter = new TYJListAdapter(mytyjActivity, new OnTYJListItemClickListener() {
			@Override
			public void onclick(View v, int position) {
				SettingsManager.setMainProductListFlag(mytyjActivity, true);
				Intent intent = new Intent(mytyjActivity,MainFragmentActivity.class);
				startActivity(intent);
				mytyjActivity.finish();
			}
		});
		nousedListView.setAdapter(tyjAdapter);
	}
	
	public void updateAdapter(List<TYJInfo> list){
		if(list == null || list.size() < 1){
			nousedListView.setVisibility(View.GONE);
			nodataText.setVisibility(View.VISIBLE);
		}else{
			nousedListView.setVisibility(View.VISIBLE);
			nodataText.setVisibility(View.GONE);
		}
		tyjAdapter.setItems(list);
	}
	
}
