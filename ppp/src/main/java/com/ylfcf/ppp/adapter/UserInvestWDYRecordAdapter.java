package com.ylfcf.ppp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.WDYChildRecordInfo;
import com.ylfcf.ppp.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
/**
 * �ȶ�Ӯ
 * @author Mr.liu
 *
 */
public class UserInvestWDYRecordAdapter extends ArrayAdapter<InvestRecordInfo>{
	private static final int RESOURCE_ID = R.layout.invest_lczq_records_item;
	private Context context;
	private List<InvestRecordInfo> investRecordList = null;
	private LayoutInflater layoutInflater = null;
	private OnWDYItemClickListener onWDYItemClickListener = null;
	private String fromWhere;
	private String systemTime = "";
	long systemTimeL = 0l;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public UserInvestWDYRecordAdapter(Context context,OnWDYItemClickListener onWDYItemClickListener,String fromWhere) {
		super(context, RESOURCE_ID);
		this.context = context;
		this.onWDYItemClickListener = onWDYItemClickListener;
		this.fromWhere = fromWhere;
		investRecordList = new ArrayList<InvestRecordInfo>();
		layoutInflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * ���ⷽ������̬�ı�listview��item������ˢ��
	 * @param recordList
	 */
	public void setItems(List<InvestRecordInfo> recordList,String systemTime) {
		this.systemTime = systemTime;
		investRecordList.clear();
		if (recordList != null) {
			investRecordList.addAll(recordList);
		}
		try {
			systemTimeL = sdf1.parse(systemTime).getTime();
		} catch (Exception e) {
		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return investRecordList.size();
	}

	@Override
	public InvestRecordInfo getItem(int position) {
		return investRecordList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private int curPosition = 0;
	private InvestRecordInfo info = null;
	ViewHolder viewHolder = null;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		curPosition = position;
		info = investRecordList.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(RESOURCE_ID, null);
			viewHolder.borrowName = (TextView) convertView
					.findViewById(R.id.invest_records_lczq_item_borrowname);
			viewHolder.investType = (TextView) convertView
					.findViewById(R.id.invest_records_lczq_item_investtype);
			viewHolder.firstTime = (TextView) convertView
					.findViewById(R.id.invest_records_lczq_item_firsttime);
			viewHolder.nextAddTime = (TextView) convertView
					.findViewById(R.id.invest_records_lczq_item_nexttime);
			viewHolder.firstMoney = (TextView) convertView
					.findViewById(R.id.invest_record_lczq_item_firstmoney);
			viewHolder.totalBidMoney = (TextView) convertView
					.findViewById(R.id.invest_record_lczq_item_totalbidmoney);
			viewHolder.totalYQDays = (TextView) convertView
					.findViewById(R.id.invest_record_lczq_item_yanqi_days);
			viewHolder.interestMoneyTitle = (TextView) convertView
					.findViewById(R.id.invest_lczq_records_item_interestmoney_title);
			viewHolder.interestMoney = (TextView) convertView
					.findViewById(R.id.invest_record_lczq_item_interestmoney);
			viewHolder.remark = (TextView) convertView
					.findViewById(R.id.invest_records_lczq_item_remark);
			viewHolder.catCompactBtn = (Button) convertView
					.findViewById(R.id.invest_record_lczq_item_catcompact);
			viewHolder.catBidRecords = (Button) convertView.
					findViewById(R.id.invest_record_lczq_item_catrecord);
			viewHolder.nhllTitleTV = (TextView) convertView
					.findViewById(R.id.invest_lczq_records_item_nhlltitle);
			viewHolder.nhllTV = (TextView) convertView
					.findViewById(R.id.invest_lczq_records_item_nhll);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.borrowName.setText("нӯ�ƻ�-��"+info.getBorrow_period()+"��");// ����
		//setTagʱ��id������ResourceId
		viewHolder.catCompactBtn.setTag(R.id.tag_first,info);
		viewHolder.catCompactBtn.setTag(R.id.tag_second,curPosition);
		viewHolder.catBidRecords.setTag(R.id.tag_first,info);
		viewHolder.catBidRecords.setTag(R.id.tag_second,curPosition);
		if("Ͷ����".equals(info.getStatus())){
			viewHolder.nhllTitleTV.setText("�����껯����");
			viewHolder.investType.setText("��Ͷ");
			viewHolder.investType.setBackgroundResource(R.drawable.style_rect_fillet_filling_yellow);
			viewHolder.interestMoneyTitle.setText("Ԥ������");
			if(info.getInterest_start_time() != null && !"0000-00-00 00:00:00".equals(info.getInterest_start_time())){
				//��Ϣ��
				if(!"һ һ".equals(getTodayNextTime(info.getWdyChildRecordList()))){
					try {
						viewHolder.nextAddTime.setText("���ڼ����գ�"+getTodayNextTime(info.getWdyChildRecordList()).split(" ")[0]);
					} catch (Exception e) {
						viewHolder.nextAddTime.setText("���ڼ����գ�һ һ");
					}
				}else{
					viewHolder.nextAddTime.setText("���ڼ����գ�"+getTodayNextTime(info.getWdyChildRecordList()));
				}
				
				
				if(info.getWdy_pro_interest() != null){
					try {
						viewHolder.interestMoney.setText(Util.formatRate(info.getWdy_pro_interest())+"Ԫ");
					} catch (Exception e) {
						viewHolder.interestMoney.setText(info.getWdy_pro_interest()+"Ԫ");
					}
				}else{
					viewHolder.interestMoney.setText("0Ԫ");
				}
			}else{
				viewHolder.nextAddTime.setText("���ڼ����գ�һ һ");
				viewHolder.interestMoney.setText("0Ԫ");
			}
		}else if("�ѻ���".equals(info.getStatus())){
			viewHolder.nhllTitleTV.setText("�����껯����");
			viewHolder.investType.setText(info.getStatus());
			viewHolder.investType.setBackgroundResource(R.drawable.style_rect_fillet_filling_gray);
			viewHolder.nextAddTime.setText("�������ڣ�"+info.getInterest_end_time().split(" ")[0]);
			viewHolder.interestMoneyTitle.setText("ʵ������");
			if(info.getWdy_real_interest() != null){
				try {
					viewHolder.interestMoney.setText(Util.formatRate(info.getWdy_real_interest())+"Ԫ");
				} catch (Exception e) {
					viewHolder.interestMoney.setText(info.getWdy_pro_interest()+"Ԫ");
				}
			}else{
				viewHolder.interestMoney.setText(Util.formatRate(info.getWdy_pro_interest())+"Ԫ");
			}
		}

		double baseRateD = 0d;
		double addRateD = 0d;
		try{
			baseRateD = Double.parseDouble(info.getInterest_rate());
			addRateD = Double.parseDouble(info.getCoupon_interest_add());
		}catch (Exception e){
			e.printStackTrace();
		}
		if("1".equals(info.getPeriod())){
			//����
			viewHolder.nhllTV.setText(Util.formatRate(String.valueOf(baseRateD + addRateD))+"%");
		}else{
			viewHolder.nhllTV.setText(Util.formatRate(String.valueOf(baseRateD))+"%");
		}
		try {
			viewHolder.firstTime.setText("��Ͷ�գ�" + info.getAdd_time().split(" ")[0]);
		} catch (Exception e) {
			viewHolder.firstTime.setText("��Ͷ�գ�һ һ");
		}
		double firstMoney = 0d;
		double totalBidMoney = 0d;
		try {
			firstMoney = Double.parseDouble(info.getMoney());
			totalBidMoney = Double.parseDouble(info.getTotal_money());
			viewHolder.firstMoney.setText((int)firstMoney+"Ԫ");
			viewHolder.totalBidMoney.setText((int)totalBidMoney+"Ԫ");
		} catch (Exception e) {
			viewHolder.firstMoney.setText(info.getMoney()+"Ԫ");
			viewHolder.totalBidMoney.setText(info.getTotal_money()+"Ԫ");
		}
		if(addRateD > 0){
			viewHolder.remark.setText("���ڹ���Ϣ"+Util.formatRate(String.valueOf(addRateD))+"%");
		}else{
			viewHolder.remark.setText("һ һ");
		}
		if (position == 0) {
			convertView.setPadding(0, context.getResources()
					.getDimensionPixelSize(R.dimen.common_measure_15dp), 0, 0);
		}
		if("0".equals(info.getIs_generated_records())){
			//���ɵ��
			viewHolder.catBidRecords.setEnabled(false);
			viewHolder.catBidRecords.setBackgroundResource(R.drawable.style_rect_fillet_filling_gray);
		}else if("1".equals(info.getIs_generated_records())){
			viewHolder.catBidRecords.setEnabled(true);
			viewHolder.catBidRecords.setBackgroundResource(R.drawable.style_rect_fillet_filling_blue);
		}
		if(info.getInterest_start_time() != null && !"0000-00-00 00:00:00".equals(info.getInterest_start_time())){
			//��ʾ��Ϣ��
			viewHolder.catCompactBtn.setEnabled(true);
			viewHolder.catCompactBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_blue);
			
			if(info.getTotalDelay() != null){
				viewHolder.totalYQDays.setText(info.getTotalDelay()+"��");
			}else{
				viewHolder.totalYQDays.setText("0��");
			}
		}else{
			//��û��Ϣ
			viewHolder.catCompactBtn.setEnabled(false);
			viewHolder.catCompactBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_gray);
			
			viewHolder.totalYQDays.setText("0��");
		}
		viewHolder.catCompactBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(onWDYItemClickListener != null)
					onWDYItemClickListener.onCatCompact((Integer)v.getTag(R.id.tag_second), v,
						(InvestRecordInfo)v.getTag(R.id.tag_first));
			}
		});
		viewHolder.catBidRecords.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(onWDYItemClickListener != null)
					onWDYItemClickListener.onCatBidRecord((Integer)v.getTag(R.id.tag_second), v, 
						(InvestRecordInfo)v.getTag(R.id.tag_first));
			}
		});
		return convertView;
	}

	/**
	 * �ڲ��࣬����Item��Ԫ��
	 * @author Mr.liu
	 *
	 */
	class ViewHolder {
		TextView borrowName;//�������
		TextView investType;//״̬
		TextView firstTime;//��Ͷ��
		TextView nextAddTime;// ���ڼ�����
		TextView firstMoney;// ��Ͷ����
		TextView totalBidMoney;//�ۼƳ���
		TextView totalYQDays;//�ۼ�����	
		TextView interestMoneyTitle;//����ı���
		TextView interestMoney;//Ͷ������
		TextView remark;
		Button catCompactBtn;// �鿴��ͬ
		Button catBidRecords;//�鿴�����¼
		TextView nhllTitleTV;
		TextView nhllTV;
	}
	
	/**
	 * Ԫ��ӯ�û�Ͷ�ʼ�¼ҳ�棬���û�������鿴��ͬ���͡�ԤԼ���"��ťʱ�Ļص�����
	 * @author Mr.liu
	 *
	 */
	public interface OnWDYItemClickListener{
		/*
		 * �鿴��ͬ
		 */
		void onCatCompact(int position,View v,InvestRecordInfo info);
		/*
		 * �鿴�����¼
		 */
		void onCatBidRecord(int position,View v,InvestRecordInfo info);
		/*
		 * ֧�����ڳ�����
		 */
		void onBidCurrentPeroid(int position,View v,InvestRecordInfo info);
	}
	
	/**
	 * ��������ǽ��죬�����ڼ����ղ���ȷ��Ҫ�Լ��㡣��ʾ������һ�ڵ�nexttime
	 */
	private String getTodayNextTime(List<WDYChildRecordInfo> childRecordList){
		if(childRecordList == null || childRecordList.size() == 0){
			return "";
		}
		String todayNextTime = "";
		long planAddTimeL = 0l;
		for(int i=0;i<childRecordList.size();i++){
			WDYChildRecordInfo info = childRecordList.get(i);
			try {
				planAddTimeL = sdf1.parse(info.getPlan_add_time()).getTime();
			} catch (Exception e) {
			}
			if(systemTimeL < planAddTimeL){
				todayNextTime = info.getPlan_add_time();
				break;
			}
			if("".equals(todayNextTime)){
				//˵�������һ��
				todayNextTime = "һ һ";
			}
		}
		return todayNextTime;
	}
}
