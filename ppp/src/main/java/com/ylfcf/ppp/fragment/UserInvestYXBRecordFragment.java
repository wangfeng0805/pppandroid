package com.ylfcf.ppp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.ui.UserInvestRecordActivity;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.widget.PagerSlidingTabStrip;

/**
 * �û�Ͷ�ʼ�¼ -- Ԫ�ű�
 * @author Mr.liu
 *
 */
public class UserInvestYXBRecordFragment extends BaseFragment{
	private static final String className = "UserInvestYXBRecordFragment";
	private UserInvestRecordActivity mainActivity;
	private View rootView;

	private PagerSlidingTabStrip yxbPagerSlidingTabStrip;
	private ViewPager yxbViewPager;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainActivity = (UserInvestRecordActivity) getActivity();
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.userinvest_yxb_record_fragment, null);
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

	private void findViews(View view) {
		yxbPagerSlidingTabStrip = (PagerSlidingTabStrip)view.findViewById(R.id.userinvest_yxb_record_fragment_tab);
		yxbViewPager = (ViewPager)view.findViewById(R.id.userinvest_yxb_record_fragment_viewpager);
		yxbViewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
		yxbViewPager.setOffscreenPageLimit(1);
		yxbPagerSlidingTabStrip.setViewPager(yxbViewPager);
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
	}
	
	private YXBInvestRecordFragment yxbInvestRecordFragment;
	private YXBRedeemRecordFragment yxbRedeemRecordFragment;
	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		private final String[] titles = { "�Ϲ���¼", "��ؼ�¼"};

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}

		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			return super.instantiateItem(container, position);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				if (yxbInvestRecordFragment == null) {
					yxbInvestRecordFragment = new YXBInvestRecordFragment();
				}
				return yxbInvestRecordFragment;
			case 1:
				if (yxbRedeemRecordFragment == null) {
					yxbRedeemRecordFragment = new YXBRedeemRecordFragment();
				}
				return yxbRedeemRecordFragment;
			default:
				return null;
			}
		}

	}
	
}
