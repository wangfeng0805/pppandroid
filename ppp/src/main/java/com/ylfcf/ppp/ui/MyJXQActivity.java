package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.graphics.Paint;
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
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.fragment.MyJXQNousedFragment;
import com.ylfcf.ppp.fragment.MyJXQOvertimeFragment;
import com.ylfcf.ppp.fragment.MyJXQUsedFragment;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.widget.LoadingDialog;
import com.ylfcf.ppp.widget.PagerSlidingTabStrip;

/**
 * �ҵļ�Ϣȯ
 * @author Mr.liu
 *
 */
public class MyJXQActivity extends BaseActivity implements OnClickListener{
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private TextView rightTV;

	private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private ViewPager mViewPager;
	public LoadingDialog loadingDialog;
	private int curPosition = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.my_jxq_layout);
		curPosition = getIntent().getIntExtra("cur_position",0);
		loadingDialog = mLoadingDialog;
		findViews();
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("�ҵļ�Ϣȯ");
		rightTV = (TextView) findViewById(R.id.common_topbar_right_text);
		rightTV.setVisibility(View.VISIBLE);
		rightTV.setText("ʹ�ù���");
		rightTV.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG ); //�»���
		rightTV.getPaint().setAntiAlias(true);//�����
		rightTV.setOnClickListener(this);
		mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.my_jxq_tab);
		mViewPager = (ViewPager) findViewById(R.id.my_jxq_viewpager);
		mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		mViewPager.setOffscreenPageLimit(2);
		mViewPager.setCurrentItem(curPosition);
		mPagerSlidingTabStrip.setViewPager(mViewPager);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.common_topbar_right_text:
			Intent intentBanner = new Intent(MyJXQActivity.this,BannerTopicActivity.class);
			BannerInfo bannerInfo = new BannerInfo();
			bannerInfo.setArticle_id("120");
			bannerInfo.setLink_url(URLGenerator.JXQ_RULE_URL);
			intentBanner.putExtra("BannerInfo", bannerInfo);
			startActivity(intentBanner);
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

	private MyJXQNousedFragment nousedFragment;
	private MyJXQUsedFragment usedFragment;
	private MyJXQOvertimeFragment overdueFragment;

	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		private final String[] titles = { "δʹ��", "��ʹ��", "�ѹ���" };

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
				if (nousedFragment == null) {
					nousedFragment = new MyJXQNousedFragment(new OnJXQNousedTransferSucListener(){
						@Override
						public void onSuccess() {
							mViewPager.setCurrentItem(1);
						}
					});
				}
				return nousedFragment;
			case 1:
				if (usedFragment == null) {
					usedFragment = new MyJXQUsedFragment();
				}
				return usedFragment;
			case 2:
				if (overdueFragment == null) {
					overdueFragment = new MyJXQOvertimeFragment();
				}
				return overdueFragment;
			default:
				return null;
			}
		}
	}

	/**
	 * ת�ü�Ϣȯ�ɹ���Ļص�
	 */
	public interface OnJXQNousedTransferSucListener{
		void onSuccess();
	}
}
