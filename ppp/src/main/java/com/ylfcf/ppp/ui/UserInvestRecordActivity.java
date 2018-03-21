package com.ylfcf.ppp.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncInvestSRZXUserRecord;
import com.ylfcf.ppp.async.AsyncInvestYJYRecord;
import com.ylfcf.ppp.async.AsyncUserSelectOne;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.fragment.UserInvestSRZXRecordFragment;
import com.ylfcf.ppp.fragment.UserInvestVIPRecordFragment;
import com.ylfcf.ppp.fragment.UserInvestWDYRecordFragment;
import com.ylfcf.ppp.fragment.UserInvestXSMBRecordFragment;
import com.ylfcf.ppp.fragment.UserInvestYJYRecordFragment;
import com.ylfcf.ppp.fragment.UserInvestYXBRecordFragment;
import com.ylfcf.ppp.fragment.UserInvestYYYRecordFragment;
import com.ylfcf.ppp.fragment.UserInvestZXDRecordFragment;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnGetUserInfoByPhone;
import com.ylfcf.ppp.inter.Inter.OnIsYXBInvestorListener;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.YLFLogger;
import com.ylfcf.ppp.widget.LoadingDialog;
import com.ylfcf.ppp.widget.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * �û�Ͷ���¼ҳ�� ����Ԫ��ӯ��Ԫ��ӯ��Ԫ�ű���VIP��Ʒ�Լ�ԤԼ��Ʒ�������ȡ��Ʒ
 * @author Mr.liu
 *
 */
public class UserInvestRecordActivity extends BaseActivity implements OnClickListener{
	private static final int REQUEST_USERINFO_WHAT = 1221;//�����û�������Ϣ
	private static final int REQUEST_USERINFO_SUCCESS = 1222;
	
	private static final int REQUEST_ISINVESTED_YXB_WHAT = 1223;//�ж��û��Ƿ�Ͷ�ʹ�Ԫ�ű�
	
	private static final int REQUEST_ISINVESTED_SRZX_WAHT = 1224;//�Ƿ�Ͷ�ʹ�˽�������Ʒ
	private static final int REQUEST_ISINVESTED_SRZX_SUCCESS = 1225;
	private static final int REQUEST_ISINVESTED_SRZX_NODATA = 1227;

	private static final int REQUEST_ISINVESTED_YJY_WAHT = 1228;//�Ƿ�Ͷ�ʹ�Ա��ר����Ʒ
	private static final int REQUEST_ISINVESTED_YJY_SUCCESS = 1229;
	private static final int REQUEST_ISINVESTED_YJY_NODATA = 1230;
	
	private static final int INIT_ADAPTER = 1226;//	��ʼ��������
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;

	private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private ViewPager mViewPager;
	private View jianbianView;//viewpager�Ҳ����Ӱ
	private String fromWhere;
	public LoadingDialog loadingDialog;
	
	private List<String> titlesList = new ArrayList<String>();

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_USERINFO_WHAT:
				if(SettingsManager.isPersonalUser(getApplicationContext())){
					requestUserInfo(SettingsManager.getUserId(getApplicationContext()), SettingsManager.getUser(getApplicationContext()));
				}else if(SettingsManager.isCompanyUser(getApplicationContext())){
					requestUserInfo(SettingsManager.getUserId(getApplicationContext()), "");
				}
				break;
			case REQUEST_USERINFO_SUCCESS:
				UserInfo mUserInfo = (UserInfo) msg.obj;
				if(mUserInfo.getType().contains("vip")){
					if(titlesList.contains("Ԫ��ӯ")){
						titlesList.add(4, "VIP");
					}else{
						titlesList.add(3, "VIP");
					}
				}
				break;
			case REQUEST_ISINVESTED_YXB_WHAT:
				isYXBInvestor();
				break;
			case REQUEST_ISINVESTED_SRZX_WAHT:
				getSRZXInvestRecordList(SettingsManager.getUserId(getApplicationContext()), "");
				break;
			case REQUEST_ISINVESTED_SRZX_SUCCESS:
				if(titlesList.contains("Ԫ��ӯ") && titlesList.contains("VIP") && titlesList.contains("Ԫ�ű�")){
					titlesList.add(6, "˽������");
					titlesList.add(7, "���");
				}else if((titlesList.contains("Ԫ��ӯ") && titlesList.contains("VIP")) ||
						(titlesList.contains("Ԫ��ӯ") && titlesList.contains("Ԫ�ű�")) ||
						(titlesList.contains("VIP") && titlesList.contains("Ԫ�ű�"))){
					titlesList.add(5, "˽������");
					titlesList.add(6, "���");
				}else if(titlesList.contains("Ԫ��ӯ") || titlesList.contains("VIP") || titlesList.contains("Ԫ�ű�")){
					titlesList.add(4, "˽������");
					titlesList.add(5, "���");
				}else if(!titlesList.contains("Ԫ��ӯ") && !titlesList.contains("VIP") && !titlesList.contains("Ԫ�ű�")){
					titlesList.add(3, "˽������");
					titlesList.add(4, "���");
				}
				break;
			case REQUEST_ISINVESTED_SRZX_NODATA:
				if(titlesList.contains("Ԫ��ӯ") && titlesList.contains("VIP") && titlesList.contains("Ԫ�ű�")){
					titlesList.add(6, "���");
				}else if((titlesList.contains("Ԫ��ӯ") && titlesList.contains("VIP")) ||
						(titlesList.contains("Ԫ��ӯ") && titlesList.contains("Ԫ�ű�")) ||
						(titlesList.contains("VIP") && titlesList.contains("Ԫ�ű�"))){
					titlesList.add(5, "���");
				}else if(titlesList.contains("Ԫ��ӯ") || titlesList.contains("VIP") || titlesList.contains("Ԫ�ű�")){
					titlesList.add(4, "���");
				}else if(!titlesList.contains("Ԫ��ӯ") && !titlesList.contains("VIP") && !titlesList.contains("Ԫ�ű�")){
					titlesList.add(3, "���");
				}
				break;
			case REQUEST_ISINVESTED_YJY_WAHT:
				getYJYInvestRecordList(SettingsManager.getUserId(getApplicationContext()), "");
				break;
			case REQUEST_ISINVESTED_YJY_SUCCESS:
				titlesList.add(3, "Ԫ��ӯ");
				break;
			case REQUEST_ISINVESTED_YJY_NODATA:
				break;
			case INIT_ADAPTER:
				initAdapter();
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.userinvest_record_activity);
		loadingDialog = mLoadingDialog;
		fromWhere = getIntent().getStringExtra("from_where");
		initTitleList();
		findViews();
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("Ͷ�ʼ�¼");

		jianbianView = findViewById(R.id.userinvest_record_activity_tab_jianbian);
		mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.userinvest_record_activity_tab);
		mViewPager = (ViewPager) findViewById(R.id.userinvest_record_activity_viewpager);
		mPagerSlidingTabStrip.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				if(titlesList.size() > 3){
					if(arg0 == titlesList.size() - 1){
						jianbianView.setVisibility(View.GONE);
					}else{
						jianbianView.setVisibility(View.VISIBLE);
					}
				}else{
					jianbianView.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				YLFLogger.d("onPageScrolled"+"*****arg0="+ arg0
						+ "******arg1="+arg1+"******arg2="+arg2);
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				YLFLogger.d("onPageScrollStateChanged*****" + arg0);
			}
		});
		handler.sendEmptyMessage(REQUEST_ISINVESTED_YJY_WAHT);
	}
	
	private void initAdapter(){
		if(titlesList.size() >= 4){
			jianbianView.setVisibility(View.VISIBLE);
		}
		int curPosition = 0;
		mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		mViewPager.setOffscreenPageLimit(0);
		mPagerSlidingTabStrip.setViewPager(mViewPager);
		for(int i=0;i<titlesList.size();i++){
			if("Ԫ��ӯ".equals(titlesList.get(i)) && "Ԫ��ӯ".equals(fromWhere)){
				curPosition = i;
			}else if("Ԫ��ӯ".equals(titlesList.get(i)) && "Ԫ��ӯ".equals(fromWhere)){
				curPosition = i;
			}else if("нӯ�ƻ�".equals(titlesList.get(i)) && "�ȶ�ӯ".equals(fromWhere)){
				curPosition = i;
			}else if("VIP".equals(titlesList.get(i)) && "VIP".equals(fromWhere)){
				curPosition = i;
			}else if("Ԫ�ű�".equals(titlesList.get(i)) && "Ԫ�ű�".equals(fromWhere)){
				curPosition = i;
			}else if("˽������".equals(titlesList.get(i)) && "˽������".equals(fromWhere)){
				curPosition = i;
			}else if("���".equals(titlesList.get(i)) && "���".equals(fromWhere)){
				curPosition = i;
			}else if("Ԫ��ӯ".equals(titlesList.get(i)) && "Ԫ��ӯ".equals(fromWhere)){
				curPosition = i;
			}
		}
		mViewPager.setCurrentItem(curPosition);
	}
	
	private void initTitleList(){
		titlesList.add(0, "Ԫ��ӯ");
		titlesList.add(1, "Ԫ��ӯ");
		titlesList.add(2,"нӯ�ƻ�");
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
		handler.removeCallbacksAndMessages(null);
	}
	
	private UserInvestZXDRecordFragment zxdRecordFragment;//���Ŵ���Ԫ��ӯ��
	private UserInvestYYYRecordFragment yyyRecordFragment;//Ԫ��ӯ
	private UserInvestWDYRecordFragment lczqRecordFragment;//�����ȡ��Ʒ
	private UserInvestYJYRecordFragment yjyRecordFragment;//Ԫ��ӯ��Ʒ��Ա��ר����Ʒ��
	private UserInvestVIPRecordFragment vipRecordFragment;//vip��Ʒ
	private UserInvestYXBRecordFragment yxbRecordFragment;//Ԫ�ű�
	private UserInvestSRZXRecordFragment srzxRecordFragment;//˽�������Ʒ
	private UserInvestXSMBRecordFragment xsmbRecordFragment;//��ʱ���

	public class MyPagerAdapter extends FragmentPagerAdapter {
		
		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titlesList.get(position);
		}

		@Override
		public int getCount() {
			return titlesList.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			return super.instantiateItem(container, position);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				if (zxdRecordFragment == null) {
					zxdRecordFragment = new UserInvestZXDRecordFragment();
				}
				return zxdRecordFragment;
			case 1:
				if (yyyRecordFragment == null) {
					yyyRecordFragment = new UserInvestYYYRecordFragment();
				}
				return yyyRecordFragment;
			case 2:
				if (lczqRecordFragment == null) {
					lczqRecordFragment = new UserInvestWDYRecordFragment();
				}
				return lczqRecordFragment;
			case 3:
				if("Ԫ��ӯ".equals(titlesList.get(3))){
					if (yjyRecordFragment == null) {
						yjyRecordFragment = new UserInvestYJYRecordFragment();
					}
					return yjyRecordFragment;
				}else if("VIP".equals(titlesList.get(3))){
					if (vipRecordFragment == null) {
						vipRecordFragment = new UserInvestVIPRecordFragment();
					}
					return vipRecordFragment;
				}else if("Ԫ�ű�".equals(titlesList.get(3))){
					if (yxbRecordFragment == null) {
						yxbRecordFragment = new UserInvestYXBRecordFragment();
					}
					return yxbRecordFragment;
				}else if("˽������".equals(titlesList.get(3))){
					if (srzxRecordFragment == null) {
						srzxRecordFragment = new UserInvestSRZXRecordFragment();
					}
					return srzxRecordFragment;
				}else if("���".equals(titlesList.get(3))){
					if (xsmbRecordFragment == null) {
						xsmbRecordFragment = new UserInvestXSMBRecordFragment();
					}
					return xsmbRecordFragment;
				}
			case 4:
				if("VIP".equals(titlesList.get(4))){
					if (vipRecordFragment == null) {
						vipRecordFragment = new UserInvestVIPRecordFragment();
					}
					return vipRecordFragment;
				}else if("Ԫ�ű�".equals(titlesList.get(4))){
					if (yxbRecordFragment == null) {
						yxbRecordFragment = new UserInvestYXBRecordFragment();
					}
					return yxbRecordFragment;
				}else if("˽������".equals(titlesList.get(4))){
					if (srzxRecordFragment == null) {
						srzxRecordFragment = new UserInvestSRZXRecordFragment();
					}
					return srzxRecordFragment;
				}else if("���".equals(titlesList.get(4))){
					if (xsmbRecordFragment == null) {
						xsmbRecordFragment = new UserInvestXSMBRecordFragment();
					}
					return xsmbRecordFragment;
				}
			case 5:
				if("Ԫ�ű�".equals(titlesList.get(5))){
					if (yxbRecordFragment == null) {
						yxbRecordFragment = new UserInvestYXBRecordFragment();
					}
					return yxbRecordFragment;
				}else if("˽������".equals(titlesList.get(5))){
					if (srzxRecordFragment == null) {
						srzxRecordFragment = new UserInvestSRZXRecordFragment();
					}
					return srzxRecordFragment;
				}else if("���".equals(titlesList.get(5))){
					if (xsmbRecordFragment == null) {
						xsmbRecordFragment = new UserInvestXSMBRecordFragment();
					}
					return xsmbRecordFragment;
				}
			case 6:
				if("˽������".equals(titlesList.get(6))){
					if(srzxRecordFragment == null) {
						srzxRecordFragment = new UserInvestSRZXRecordFragment();
					}
					return srzxRecordFragment;
				} else if("���".equals(titlesList.get(6))){
					if (xsmbRecordFragment == null) {
						xsmbRecordFragment = new UserInvestXSMBRecordFragment();
					}
					return xsmbRecordFragment;
				}
			case 7:
				if (xsmbRecordFragment == null) {
					xsmbRecordFragment = new UserInvestXSMBRecordFragment();
				}
				return xsmbRecordFragment;
			default:
				return null;
			}
		}
	}
	
	private void isYXBInvestor(){
		RequestApis.requestIsYXBInvestor(this, SettingsManager.getUserId(getApplicationContext()), 0, 10, new OnIsYXBInvestorListener() {
			@Override
			public void isYXBInvestor(boolean flag) {
				if(flag){
					//Ͷ�ʹ�Ԫ�ű�
					if(titlesList.contains("Ԫ��ӯ") && titlesList.contains("VIP")){
						titlesList.add(5, "Ԫ�ű�");
					}else if(titlesList.contains("Ԫ��ӯ") || titlesList.contains("VIP")){
						titlesList.add(4, "Ԫ�ű�");
					}else if(!titlesList.contains("Ԫ��ӯ") && !titlesList.contains("VIP")){
						titlesList.add(3, "Ԫ�ű�");
					}
				}else{
					//û��Ͷ�ʹ�Ԫ�ű�
				}
				handler.sendEmptyMessage(REQUEST_ISINVESTED_SRZX_WAHT);
			}
		});
	}

	/**
	 * �����û���Ϣ������hf_user_id�ֶ��ж��û��Ƿ��л㸶�˻�
	 * @param userId
	 * @param phone
	 */
	private void requestUserInfo(final String userId,String phone){
		AsyncUserSelectOne userTask = new AsyncUserSelectOne(this, userId, phone, "","", new OnGetUserInfoByPhone() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						UserInfo userInfo = baseInfo.getUserInfo();
						Message msg = handler.obtainMessage(REQUEST_USERINFO_SUCCESS);
						msg.obj = userInfo;
						handler.sendMessage(msg);
					}else{
					}
				}else{
				}
				handler.sendEmptyMessage(REQUEST_ISINVESTED_YXB_WHAT);
			}
		});
		userTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * ��ȡ˽������Ͷ�ʼ�¼�б�
	 * @param investUserId
	 * @param borrowId
	 */
	private void getSRZXInvestRecordList(String investUserId,String borrowId){
		AsyncInvestSRZXUserRecord asyncInvestRecord = new AsyncInvestSRZXUserRecord(this, investUserId, 
				0, 5, new OnCommonInter(){
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0 && baseInfo.getmInvestRecordPageInfo() != null){
								List<InvestRecordInfo> recordList = baseInfo.getmInvestRecordPageInfo().getInvestRecordList();
								if(recordList != null && recordList.size() > 0){
									//Ͷ�ʹ�˽������
									handler.sendEmptyMessage(REQUEST_ISINVESTED_SRZX_SUCCESS);
								}else{
									handler.sendEmptyMessage(REQUEST_ISINVESTED_SRZX_NODATA);
								}
							}else{
								handler.sendEmptyMessage(REQUEST_ISINVESTED_SRZX_NODATA);
							}
						}else{
							handler.sendEmptyMessage(REQUEST_ISINVESTED_SRZX_NODATA);
						}
						handler.sendEmptyMessage(INIT_ADAPTER);
					}
		});
		asyncInvestRecord.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * ��ȡԱ��ר����ƷͶ�ʼ�¼�б�
	 * @param investUserId
	 * @param borrowId
	 */
	private void getYJYInvestRecordList(String investUserId,String borrowId){
		if(loadingDialog != null && !isFinishing()){
			loadingDialog.show();
		}
		AsyncInvestYJYRecord asyncInvestRecord = new AsyncInvestYJYRecord(this, investUserId,borrowId,
				0, 5, new OnCommonInter(){
			@Override
			public void back(BaseInfo baseInfo) {
				if(loadingDialog != null && loadingDialog.isShowing()){
					loadingDialog.dismiss();
				}
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0 && baseInfo.getmInvestRecordPageInfo() != null){
						List<InvestRecordInfo> recordList = baseInfo.getmInvestRecordPageInfo().getInvestRecordList();
						if(recordList != null && recordList.size() > 0){
							//Ͷ�ʹ�˽������
							handler.sendEmptyMessage(REQUEST_ISINVESTED_YJY_SUCCESS);
						}else{
							handler.sendEmptyMessage(REQUEST_ISINVESTED_YJY_NODATA);
						}
					}else{
						handler.sendEmptyMessage(REQUEST_ISINVESTED_YJY_NODATA);
					}
				}else{
					handler.sendEmptyMessage(REQUEST_ISINVESTED_YJY_NODATA);
				}
				handler.sendEmptyMessage(REQUEST_USERINFO_WHAT);
			}
		});
		asyncInvestRecord.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
