package com.ylfcf.ppp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.fragment.InformationFragment;
import com.ylfcf.ppp.fragment.NewsFragment;
import com.ylfcf.ppp.fragment.NoticeFragment;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.widget.LoadingDialog;
import com.ylfcf.ppp.widget.PagerSlidingTabStrip;

/**
 * ���š����桢��Ѷ
 * @author Administrator
 *
 */
public class ArticleListActivity extends BaseActivity implements OnClickListener{
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private ViewPager mViewPager;
	public LoadingDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.article_list_activity);
		loadingDialog = mLoadingDialog;
		findViews();
	}

	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("���Ź���");
		
		mPagerSlidingTabStrip = (PagerSlidingTabStrip)findViewById(R.id.article_list_activity_tab);
		mViewPager = (ViewPager)findViewById(R.id.article_list_activity_viewpager);
		mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		mViewPager.setOffscreenPageLimit(2);
		mPagerSlidingTabStrip.setViewPager(mViewPager);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		UMengStatistics.statisticsResume(this);//����ͳ��ʱ��
	}

	@Override
	protected void onPause() {
		super.onPause();
		UMengStatistics.statisticsPause(this);//����ͳ��ʱ��
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private NoticeFragment noticeFragment;
	private NewsFragment newsFragment;
	private InformationFragment informationFragment;
	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		private final String[] titles = { "ƽ̨����", "��ҵ����", "������Ѷ"};

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
				if (noticeFragment == null) {
					noticeFragment = new NoticeFragment();
				}
				return noticeFragment;
			case 1:
				if (newsFragment == null) {
					newsFragment = new NewsFragment();
				}
				return newsFragment;
			case 2:
				if (informationFragment == null) {
					informationFragment = new InformationFragment();
				}
				return informationFragment;
			default:
				return null;
			}
		}
	}
}
