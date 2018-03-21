package com.ylfcf.ppp.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.GiftAdapter;
import com.ylfcf.ppp.adapter.GiftAdapter.OnGiftItemClickListener;
import com.ylfcf.ppp.async.AsyncFLJHReceivePrize;
import com.ylfcf.ppp.async.AsyncGiftCode;
import com.ylfcf.ppp.async.AsyncHDGiftList;
import com.ylfcf.ppp.async.AsyncHDPrizeList;
import com.ylfcf.ppp.async.AsyncPrize;
import com.ylfcf.ppp.async.AsyncPrizeCode;
import com.ylfcf.ppp.async.AsyncXCFLActiveTime;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.GiftCodeInfo;
import com.ylfcf.ppp.entity.GiftInfo;
import com.ylfcf.ppp.entity.GiftPageInfo;
import com.ylfcf.ppp.entity.PrizeInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.YLFLogger;
import com.ylfcf.ppp.view.InvitateFriendsPopupwindow;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * ��Ա�����ƻ�2��
 * @author Mr.liu
 *
 */
public class PrizeRegion2TempActivity extends BaseActivity implements OnClickListener{
	private static final int REQUEST_PRIZELIST_WHAT = 5410;
	private static final int REQUEST_PRIZELIST_SUCCESS = 5411;
	
	private static final int REQUEST_ACTIVE_ISSTART_WHAT = 5412;//�жϻ�Ƿ��Ѿ���ʼ
	private static final int REQUEST_GIFT_ISGETOVER_HASCODE_WHAT = 5413;//�ж���ȯ�����Ʒ�Ƿ��Ѿ���ȡ���
	private static final int REQUEST_GIFT_ISGETOVER_NOHASCODE_WHAT = 5416;//�ж���ȯ�����Ʒ�Ƿ��Ѿ���ȡ���
//	private static final int REQUEST_GIFT_ISGET_BYNAME_WHAT = 5414;//������Ʒ�����ж�ĳ���û��Ƿ���ȡ���ý�Ʒ
	private static final int REQUEST_GIFTHASCODE_ISGET_WHAT = 5415;//�ж��û��Ƿ��Ѿ���ȡ����ȯ�����Ʒ
	private static final int REQUEST_GIFTNOCODE_ISGET_WHAT = 5416;//�ж��û��Ƿ��Ѿ���ȡ������ȯ�����Ʒ
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	private LinearLayout mainLayout;
	private ListView mListView;
	private GiftAdapter mGiftAdapter;
	private List<GiftInfo> giftListTemp = new ArrayList<GiftInfo>();//��Ʒ����ʱ���ϣ�����ˢ��adapter
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	private String systemTime = null;//ϵͳ��ǰʱ��
	private int page = 0;
	private int pageSize = 50;
	private View topView;
	private View bottomView;//
	private Button catMoreBtn,shareBtn;
	private LayoutInflater mLayoutInflater;

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_PRIZELIST_WHAT:
				getGiftList("����ȯ", "��");
				break;
			case REQUEST_PRIZELIST_SUCCESS:
				break;
			case REQUEST_ACTIVE_ISSTART_WHAT:
				requestActiveTime("HYFL_02");
				break;
			case REQUEST_GIFT_ISGETOVER_HASCODE_WHAT:
				//�ж���ȯ�����Ʒ�Ƿ��Ѿ���ȡ���
				GiftInfo info = (GiftInfo) msg.obj;
				int position = msg.arg1;
				requestGiftHasCodeIsGetOver(position, info.getId(),"δ��ȡ");
				break;
			case REQUEST_GIFTNOCODE_ISGET_WHAT:
				GiftInfo giftNocode = (GiftInfo) msg.obj;
				int positionNocode = msg.arg1;
				requestPrizeInfoByName(positionNocode,SettingsManager.getUserId(getApplicationContext()), giftNocode.getName());
				break;
			case REQUEST_GIFTHASCODE_ISGET_WHAT:
				GiftInfo gift = (GiftInfo) msg.obj;
				int positionG = msg.arg1;
				requestGiftHasCodeIsGet(positionG, gift.getId(), SettingsManager.getUserId(getApplicationContext()));
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
		setContentView(R.layout.prize_region2_temp_activity);
		systemTime = sdf.format(new Date());
		mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		findViews();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		handler.sendEmptyMessage(REQUEST_PRIZELIST_WHAT);
	}
	
	private void findViews(){
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("��Ա��Ʒר��");
		
		mainLayout = (LinearLayout) findViewById(R.id.prize_region2_temp_activity_mainlayout);
		topView = mLayoutInflater.inflate(R.layout.prize_region2_toplayout, null);
		bottomView = mLayoutInflater.inflate(R.layout.prize_region2_bottomlayout, null);
		catMoreBtn = (Button) bottomView.findViewById(R.id.prize_region2_bottom_catmore_btn);
		catMoreBtn.setOnClickListener(this);
		shareBtn = (Button) bottomView.findViewById(R.id.prize_region2_bottom_share_btn);
		shareBtn.setOnClickListener(this);
		mListView = (ListView) findViewById(R.id.prize_region2_temp_activity_listview);
		mListView.addHeaderView(topView);
		mListView.addFooterView(bottomView);
		mGiftAdapter = new GiftAdapter(PrizeRegion2TempActivity.this,new OnGiftItemClickListener() {
			@Override
			public void onclick(GiftInfo info, int position) {
				//
				if(info.isLogin()){
					//����Ѿ���¼��˵�������ť����ȡ
					try {
						checkReceivePrize(info,position);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}else{
					//δ��¼��ת����¼ҳ��
					Intent intent = new Intent(PrizeRegion2TempActivity.this,LoginActivity.class);
					intent.putExtra("FLAG", "HYFL_02");
					startActivity(intent);
				}
			}

			@Override
			public void ruleOnClick(View v) {
				
			}
		});
		mListView.setAdapter(mGiftAdapter);
	}
	
	private void updateAdapter(List<GiftInfo> giftList,boolean isFirstReresh){
		mGiftAdapter.setItems(giftList,isFirstReresh);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.prize_region2_bottom_catmore_btn:
			//�鿴����
			Intent intent = new Intent(PrizeRegion2TempActivity.this,ActivitysRegionActivity.class);
			startActivity(intent);
			break;
		case R.id.prize_region2_bottom_share_btn:
			//����
			showFriendsSharedWindow();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}

	/**
	 * ��ѽ�������ʼ������
	 */
	private void initActiveEndData(){
		if(mLoadingDialog != null){
			mLoadingDialog.dismiss();
		}
		for(int i=0;i<giftListTemp.size();i++){
			giftListTemp.get(i).setIsStart(1);
		}
		updateAdapter(giftListTemp,false);
	}
	
	/**
	 * ���δ��ʼ����ʼ������
	 */
	private void initActiveUnstartData(){
		if(mLoadingDialog != null){
			mLoadingDialog.dismiss();
		}
		for(int i=0;i<giftListTemp.size();i++){
			giftListTemp.get(i).setIsStart(-1);
		}
		updateAdapter(giftListTemp,false);
	}
	
	/**
	 * ��Ѿ���ʼ����ʼ������
	 */
	int hasCodeGL = 0;//��ȯ�����Ʒ����
	int noCodeGL = 0;//ûȯ�����Ʒ����
	private void initActiveStartData(){
		hasCodeGL = 0;
		noCodeGL = 0;
		boolean isLogin = !SettingsManager.getLoginPassword(
				PrizeRegion2TempActivity.this).isEmpty()
				&& !SettingsManager.getUser(PrizeRegion2TempActivity.this)
						.isEmpty();
		for(int i=0;i<giftListTemp.size();i++){
			if("��".equals(giftListTemp.get(i).getHas_code())){
				hasCodeGL++;
			}
		}
		noCodeGL = giftListTemp.size() - hasCodeGL;
		for(int i=0;i<giftListTemp.size();i++){
			giftListTemp.get(i).setIsStart(0);
			if(isLogin){
				giftListTemp.get(i).setLogin(true);
				if(SettingsManager.isPersonalUser(getApplicationContext())){
					if("��".equals(giftListTemp.get(i).getHas_code())){
						Message msg = handler.obtainMessage(REQUEST_GIFT_ISGETOVER_HASCODE_WHAT);
						msg.obj = giftListTemp.get(i);
						msg.arg1 = i;
						handler.sendMessage(msg);
					}else{
						//û��ȯ�����Ʒ�����ж���û������,ֱ���ж���û�����
						Message msg = handler.obtainMessage(REQUEST_GIFTNOCODE_ISGET_WHAT);
						msg.obj = giftListTemp.get(i);
						msg.arg1 = i;
						handler.sendMessage(msg);
					}
				}else{
					mLoadingDialog.dismiss();
					updateAdapter(giftListTemp,false);
				}
			}else{
				if(mLoadingDialog != null){
					mLoadingDialog.dismiss();
				}
				giftListTemp.get(i).setLogin(false);
				updateAdapter(giftListTemp,false);
			}
		}
	}
	
	//�жϵ����Ƿ��Ѿ���ȡ����Ʒ���Ƿ���Լ�����ȡ������GiftInfo �����get_time�ֶ����жϣ����Ϊ���죬˵�������Ѿ����������ˡ�
	boolean isGet = false;
	private void checkReceivePrize(GiftInfo info,int position) throws ParseException{
		isTodayGetGift(info);
	}
	
	/**
	 * �����������ʾ��
	 */
	private void showFriendsSharedWindow() {
		View popView = LayoutInflater.from(this).inflate(
				R.layout.invitate_friends_popupwindow, null);
		int[] screen = SettingsManager.getScreenDispaly(PrizeRegion2TempActivity.this);
		int width = screen[0];
		int height = screen[1] / 5 * 2;
		InvitateFriendsPopupwindow popwindow = new InvitateFriendsPopupwindow(PrizeRegion2TempActivity.this,
				popView, width, height);
		popwindow.show(mainLayout,URLGenerator.HYFL02_WAP_URL,"��Ա��������",null);
	}
	
	/**
	 * Dialog
	 */
	private void showPromptDialog(final String type){
		View contentView = LayoutInflater.from(PrizeRegion2TempActivity.this).inflate(R.layout.prize_region_prompt_dialog, null);
		final TextView topText = (TextView) contentView.findViewById(R.id.prize_region_prompt_dialog_top_text);
		final TextView bottomText = (TextView) contentView.findViewById(R.id.prize_region_prompt_dialog_bottom_text);
		final ImageView delBtn = (ImageView) contentView.findViewById(R.id.prize_region_prompt_dialog_delbtn);
		final Button btn = (Button) contentView.findViewById(R.id.prize_region_prompt_dialog_btn);
		if("��ȡ�ɹ�".equals(type)){
			topText.setVisibility(View.VISIBLE);
			topText.setText("��ϲ����ȡ�ɹ���");
			bottomText.setText("������ϲ�����ҵ����ң�");
			bottomText.setTextColor(getResources().getColor(R.color.gray));
			btn.setText("�鿴��Ʒ");
		}else if("�����".equals(type)){
			topText.setVisibility(View.VISIBLE);
			topText.setText("�������Ѿ������Ʒ�ˣ�");
			bottomText.setText("�ǵ�����������");
			bottomText.setTextColor(getResources().getColor(R.color.red));
			btn.setText("ȷ��");
		}else{
			topText.setVisibility(View.GONE);
			bottomText.setText(type);
			btn.setText("��֪����");
		}
		AlertDialog.Builder builder=new AlertDialog.Builder(PrizeRegion2TempActivity.this, R.style.Dialog_Transparent);  //�ȵõ�������  
        builder.setView(contentView);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if("��ȡ�ɹ�".equals(type)){
					Intent intentPrizeRecord = new Intent(PrizeRegion2TempActivity.this,MyGiftsActivity.class);
					startActivity(intentPrizeRecord);
				}
				dialog.dismiss();
			}
		});
        delBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
        dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				if("��ȡ�ɹ�".equals(type)){
					//ˢ�½���
					handler.sendEmptyMessage(REQUEST_PRIZELIST_WHAT);
				}
			}
		});
        //��������������ˣ���������ʾ����  
        dialog.show();  
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth()*4/5;
        dialog.getWindow().setAttributes(lp);
	}
	
	/**
	 * �жϻ�Ƿ��Ѿ���ʼ
	 * @param activeTitle
	 */
	private void requestActiveTime(String activeTitle){
		AsyncXCFLActiveTime task = new AsyncXCFLActiveTime(PrizeRegion2TempActivity.this, activeTitle, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if (resultCode == 0) {
								//��ѿ�ʼ
								initActiveStartData();
							} else if (resultCode == -3) {
								//�����
								initActiveEndData();
							} else if (resultCode == -2) {
								//���û��ʼ
								initActiveUnstartData();
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * ��ȡ��Ʒ�б�
	 * @param type
	 * @param isShow
	 */
	private void getGiftList(String type,String isShow){
		if(mLoadingDialog != null){
			mLoadingDialog.show();
		}
		AsyncHDGiftList giftListTask = new AsyncHDGiftList(PrizeRegion2TempActivity.this, 
				type, isShow, String.valueOf(page),String.valueOf(pageSize),new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(baseInfo != null){
					systemTime = baseInfo.getTime();
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						GiftPageInfo pageInfo = baseInfo.getmGiftPageInfo();
						if(pageInfo != null){
							giftListTemp.clear();
							giftListTemp.addAll(pageInfo.getGiftList());
							for(int i=0;i<giftListTemp.size();i++){
								String picURL = "";
								try {
									Document doc = Jsoup.parse(Html.fromHtml(giftListTemp.get(i).getPic()).toString());
									Elements elements = doc.getElementsByTag("img");
									picURL = elements.attr("src");
									giftListTemp.get(i).setPic(picURL);
								} catch (Exception e) {
								}
							}
							handler.sendEmptyMessage(REQUEST_ACTIVE_ISSTART_WHAT);
						}else{
							mLoadingDialog.dismiss();
						}
					}else{
						mLoadingDialog.dismiss();
					}
				}
			}
		});
		giftListTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * �жϸ��û��Ƿ��Ѿ���ȡ�ý�Ʒ
	 * @param userId
	 * @param prizeName
	 */
	int prizeM = 0;
	private void requestPrizeInfoByName(final int position,String userId,final String prizeName){
		AsyncPrize prizeTask = new AsyncPrize(PrizeRegion2TempActivity.this, userId, prizeName, new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				++prizeM;
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						PrizeInfo info = baseInfo.getmPrizeInfo();
						//�Ѿ���ȡ
						if("��".equals(giftListTemp.get(position).getHas_code())){
							giftListTemp.get(position).setGet(true);
							giftListTemp.get(position).setGet_time(info.getAdd_time());
						}
					}else{
						//δ��ȡ
						if("��".equals(giftListTemp.get(position).getHas_code())){
							giftListTemp.get(position).setGet(false);
							giftListTemp.get(position).setGet_time(null);
						}
					}
				}
				if(mLoadingDialog != null){
					mLoadingDialog.dismiss();
				}
				if(prizeM >= noCodeGL - 1){
					updateAdapter(giftListTemp, false);
					prizeM = 0;
				}
			}
		});
		prizeTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * �жϴ�ȯ�����Ʒ�Ƿ��Ѿ���ȡ���
	 * @param giftId
	 * @param userId
	 */
	int getoverM = 0;
	private void requestGiftHasCodeIsGetOver(final int position,String giftId,String status){
		AsyncGiftCode giftCodeTask = new AsyncGiftCode(PrizeRegion2TempActivity.this, "",giftId, "",status, new OnCommonInter(){
			@Override
			public void back(BaseInfo baseInfo){
				++getoverM;
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						//��Ʒδ����
						if("��".equals(giftListTemp.get(position).getHas_code())){
							giftListTemp.get(position).setGetOver(false);
							//���жϸ��û��Ƿ����
							Message msg = handler.obtainMessage(REQUEST_GIFTHASCODE_ISGET_WHAT);
							msg.obj = giftListTemp.get(position);
							msg.arg1 = position;
							handler.sendMessage(msg);
							YLFLogger.d(giftListTemp.get(position).getName()+"δ���ꡣ");
						}
					}else{
						if(mLoadingDialog != null){
							mLoadingDialog.dismiss();
						}
						//��Ʒ�Ѿ�����
						giftListTemp.get(position).setGetOver(true);
						YLFLogger.d(giftListTemp.get(position).getName()+"�����ꡣ");
					}
					if(getoverM >= hasCodeGL - 1){
						updateAdapter(giftListTemp,false);
						getoverM = 0;
					}
				}
			}
		});
		giftCodeTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * ��ȯ�����Ʒ�Ƿ��Ѿ���ȡ��
	 * @param position
	 * @param giftId
	 * @param userId
	 */
	int isGetI = 0;
	private void requestGiftHasCodeIsGet(final int position,String giftId,String userId){
		AsyncGiftCode giftCodeTask = new AsyncGiftCode(PrizeRegion2TempActivity.this, "",giftId, userId,"", new OnCommonInter(){
			@Override
			public void back(BaseInfo baseInfo) {
				++isGetI;
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						//�Ѿ����
						GiftCodeInfo info = baseInfo.getmGiftCodeInfo();
						if("��".equals(giftListTemp.get(position).getHas_code())){
							giftListTemp.get(position).setGet(true);
							giftListTemp.get(position).setGet_time(info.getAdd_time());
						}
					}else{
						//��δ��ȡ
						if("��".equals(giftListTemp.get(position).getHas_code())){
							giftListTemp.get(position).setGet(false);
							giftListTemp.get(position).setGet_time(null);
						}
					}
				}
				if(isGetI >= hasCodeGL - 1){
					updateAdapter(giftListTemp,false);
					isGetI = 0;
				}
				if(mLoadingDialog != null){
					mLoadingDialog.dismiss();
				}
			}
		});
		giftCodeTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * �жϽ����Ƿ��Ѿ���ȡ����Ʒ��
	 * @param position
	 * @param userId
	 */
	private void requestIsGetGiftToday(final int position,final GiftInfo giftInfo,String userId){
		if(mLoadingDialog != null){
			mLoadingDialog.show();
		}
		AsyncGiftCode giftCodeTask = new AsyncGiftCode(PrizeRegion2TempActivity.this, "",giftInfo.getId(), userId,"", new OnCommonInter(){
			@Override
			public void back(BaseInfo baseInfo) {
				if(mLoadingDialog != null){
					mLoadingDialog.dismiss();
				}
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						//�Ѿ����
						GiftCodeInfo info = baseInfo.getmGiftCodeInfo();
						Date dateNow = null;
						try {
							dateNow = sdf1.parse(systemTime);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						Date getDate = null;
						try {
							getDate = sdf1.parse(info.getAdd_time());
						} catch (ParseException e) {
							e.printStackTrace();
						}
						if(dateNow.compareTo(getDate) == 0){
							//˵����ͬһ��
							showPromptDialog("�����");
						}else{
							fljhReceivePrize(SettingsManager.getUserId(getApplicationContext()), giftInfo.getName(), info.getId(),"HYFL_02", 
									"��Ա����", "�����ƻ�", "�Ѷһ�", SettingsManager.USER_FROM);
						}
					}else{
						//��δ��ȡ
						if("��".equals(giftListTemp.get(position).getHas_code())){
							giftListTemp.get(position).setGet(false);
							giftListTemp.get(position).setGet_time(null);
						}
						fljhReceivePrize(SettingsManager.getUserId(getApplicationContext()), giftInfo.getName(), giftInfo.getId(),"HYFL_02", 
								"��Ա����", "�����ƻ�", "�Ѷһ�", SettingsManager.USER_FROM);
					}
					updateAdapter(giftListTemp,false);
				}
				if(mLoadingDialog != null){
					mLoadingDialog.dismiss();
				}
			}
		});
		giftCodeTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	private void isTodayGetGift(final GiftInfo giftInfo){
		if(mLoadingDialog != null){
			mLoadingDialog.show();
		}
		AsyncHDPrizeList hdprizeTask = new AsyncHDPrizeList(PrizeRegion2TempActivity.this, 
				SettingsManager.getUserId(getApplicationContext()), "HYFL_02", String.valueOf(page), String.valueOf(pageSize), new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						boolean isget = false;
						if(mLoadingDialog != null){
							mLoadingDialog.dismiss();
						}
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								List<PrizeInfo> prizeList = baseInfo.getmHDPrizePageInfo().getPrizeList();
								Date dateNow = null;
								try {
									dateNow = sdf1.parse(systemTime);
								} catch (ParseException e) {
									e.printStackTrace();
								}
								for(int i=0;i<prizeList.size();i++){
									PrizeInfo info = prizeList.get(i);
									Date getDate = null;
									try {
										getDate = sdf1.parse(info.getAdd_time());
									} catch (ParseException e) {
										e.printStackTrace();
									}
									if(dateNow.compareTo(getDate) == 0){
										//˵����ͬһ��
										isget = true;
									}
								}
								if(isget){
									//���
									showPromptDialog("�����");
								}else{
									fljhReceivePrize(SettingsManager.getUserId(getApplicationContext()), giftInfo.getName(), giftInfo.getId(),"HYFL_02", 
											"��Ա����", "�����ƻ�", "�Ѷһ�", SettingsManager.USER_FROM);
								}
							}else{
								fljhReceivePrize(SettingsManager.getUserId(getApplicationContext()), giftInfo.getName(), giftInfo.getId(),"HYFL_02", 
										"��Ա����", "�����ƻ�", "�Ѷһ�", SettingsManager.USER_FROM);
							}
						}
					}
				});
		hdprizeTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * ����ý�Ʒ�Ƿ��Ѿ�����
	 * @param position λ��
	 * @param prizeName
	 * @param status
	 */
	private void requestPrizeCodeSelectone(final int position,final String prizeName,String status){
		AsyncPrizeCode prizeCodeTask = new AsyncPrizeCode(PrizeRegion2TempActivity.this, "", 
				"", prizeName, status, "", "",new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
//							int resultCode = SettingsManager.getResultCode(baseInfo);
//							if(resultCode == 0){
//								//��Ʒδ����
//								giftListTemp.get(position).setGetOver(false);
//								//���жϸ��û��Ƿ����
//								Message msg = handler.obtainMessage(REQUEST_GIFT_ISGET_BYID_WHAT);
//								msg.obj = giftListTemp.get(position);
//								msg.arg1 = position;
//								handler.sendMessage(msg);
//							}else{
//								//��Ʒ�Ѿ�����
//								giftListTemp.get(position).setGetOver(true);
//								if(position == giftListTemp.size() - 1){
//									updateAdapter(giftListTemp,false);
//								}
//							}
						}
					}
				});
		prizeCodeTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * �콱
	 * @param userId
	 * @param prize
	 * @param activeTitle
	 * @param operatingRemark
	 * @param remark
	 * @param status
	 * @param source
	 */
	private void fljhReceivePrize(String userId,final String prize,String giftId,String activeTitle,String operatingRemark,
			String remark,String status,String source){
		AsyncFLJHReceivePrize task = new AsyncFLJHReceivePrize(PrizeRegion2TempActivity.this, userId, prize,giftId, activeTitle, operatingRemark,
				remark, status, source, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//��ȡ�ɹ�
								showPromptDialog("��ȡ�ɹ�");
							}else if(resultCode == -1){
								//�Ѿ���ȡ
								showPromptDialog("�����");
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
