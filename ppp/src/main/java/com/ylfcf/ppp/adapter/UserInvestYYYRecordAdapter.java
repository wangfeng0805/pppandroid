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
import com.ylfcf.ppp.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Ԫ��ӯ�û�Ͷ�ʼ�¼
 * @author Mr.liu
 *
 */
public class UserInvestYYYRecordAdapter extends ArrayAdapter<InvestRecordInfo> {
	private static final int RESOURCE_ID = R.layout.invest_records_yyy_item;
	private Context context;
	private List<InvestRecordInfo> investRecordList = null;
	private LayoutInflater layoutInflater = null;
	private OnYYYItemClickListener onYYYItemClickListener = null;
	private String fromWhere;

	public UserInvestYYYRecordAdapter(Context context,OnYYYItemClickListener onYYYItemClickListener,String fromWhere) {
		super(context, RESOURCE_ID);
		this.context = context;
		this.onYYYItemClickListener = onYYYItemClickListener;
		this.fromWhere = fromWhere;
		investRecordList = new ArrayList<InvestRecordInfo>();
		layoutInflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * ���ⷽ������̬�ı�listview��item������ˢ��
	 * @param recordList
	 */
	public void setItems(List<InvestRecordInfo> recordList) {
		investRecordList.clear();
		if (recordList != null) {
			investRecordList.addAll(recordList);
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
					.findViewById(R.id.invest_records_yyy_item_borrowname);
			viewHolder.investType = (TextView) convertView
					.findViewById(R.id.invest_records_yyy_item_investtype);
			viewHolder.firstTime = (TextView) convertView
					.findViewById(R.id.invest_records_yyy_item_firsttime);
			viewHolder.nearEndTime = (TextView) convertView
					.findViewById(R.id.invest_records_yyy_item_nearendtime);
			viewHolder.firstMoney = (TextView) convertView
					.findViewById(R.id.invest_record_yyy_item_firstmoney);
			viewHolder.nowMoney = (TextView) convertView
					.findViewById(R.id.invest_record_yyy_item_nowmoney);
			viewHolder.catCompactBtn = (Button) convertView
					.findViewById(R.id.invest_record_yyy_item_catcompact);
			viewHolder.applyOrCancelBtn = (Button) convertView
					.findViewById(R.id.invest_record_yyy_item_applyorcancel);
			viewHolder.interestMoney = (TextView) convertView.findViewById(R.id.invest_record_yyy_item_interestmoney);//Ͷ������
			viewHolder.remark = (TextView) convertView.findViewById(R.id.invest_records_yyy_item_remark);
			viewHolder.nhllTV = (TextView) convertView.findViewById(R.id.invest_records_yyy_item_nhll);
			viewHolder.nhllTitleTV = (TextView) convertView.findViewById(R.id.invest_records_yyy_item_nhll_title);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.borrowName.setText("Ԫ��ӯ-��"+info.getBorrow_period()+"��");// ����
		//setTagʱ��id������ResourceId
		viewHolder.catCompactBtn.setTag(R.id.tag_first,info);
		viewHolder.catCompactBtn.setTag(R.id.tag_second,curPosition);
		viewHolder.applyOrCancelBtn.setTag(R.id.tag_first,info);
		viewHolder.applyOrCancelBtn.setTag(R.id.tag_second,curPosition);
		if("Ͷ����".equals(info.getReturn_status())){
			viewHolder.nhllTitleTV.setText("�����껯����");
			viewHolder.investType.setText(info.getInvest_status());
			viewHolder.investType.setTextColor(context.getResources().getColor(R.color.white));
			if("��Ͷ".equals(info.getInvest_status())){
				viewHolder.investType.setBackgroundResource(R.drawable.style_rect_fillet_filling_yellow);
			}else if("��Ͷ".equals(info.getInvest_status())){
				viewHolder.investType.setBackgroundResource(R.drawable.style_rect_fillet_filling_orange);
			}
		}else{
			viewHolder.nhllTitleTV.setText("�����껯����");
			viewHolder.investType.setText(info.getReturn_status());
			viewHolder.investType.setTextColor(context.getResources().getColor(R.color.gray));
			viewHolder.investType.setBackgroundResource(R.drawable.style_rect_fillet_filling_edit_white);
		}

		double baseRateD = 0d;//��������
		double addRateD = 0d;//��Ϣ����
		try{
			baseRateD = Double.parseDouble(info.getInterest_rate());
			addRateD = Double.parseDouble(info.getInterest_add());
		}catch (Exception e){
			e.printStackTrace();
		}
		if("0".equals(info.getInvest_times())){
			viewHolder.nhllTV.setText(Util.formatRate(String.valueOf(baseRateD + addRateD))+"%");
		}else{
			viewHolder.nhllTV.setText(Util.formatRate(String.valueOf(baseRateD))+"%");
		}
		viewHolder.firstTime.setText("��Ͷ��: " + info.getFirst_borrow_time().split(" ")[0]);
		int times = 0;//Ͷ�ʴ���������0��ʾ��Ͷ
		long nowTime = System.currentTimeMillis();
		long interestStartTime = 0l;//��Ϣʱ��
		try {
			interestStartTime = Util.string2date(info.getInterest_start_time());
		} catch (Exception e) {
		}
		try {
			times = Integer.parseInt(info.getInvest_times());
		} catch (Exception e) {
		}
		if(times > 0){
			//��Ͷ
			if(nowTime > interestStartTime){
				//��ʾ�Ѿ���Ϣ
				viewHolder.nearEndTime.setText("���������: "+info.getInterest_end_time().split(" ")[0]);
				viewHolder.catCompactBtn.setEnabled(true);
				viewHolder.catCompactBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_blue);
			}else{
				viewHolder.nearEndTime.setText("���������:  �� ��");
				viewHolder.catCompactBtn.setEnabled(false);
				viewHolder.catCompactBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_gray);
			}
		}else{
			//��Ͷ
			if(info.getInterest_start_time() == null || "".equals(info.getInterest_start_time()) || "0000-00-00 00:00:00".equals(info.getInterest_start_time())){
				viewHolder.nearEndTime.setText("���������:  �� ��");
				viewHolder.catCompactBtn.setEnabled(false);
				viewHolder.catCompactBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_gray);
			}else{
				viewHolder.nearEndTime.setText("���������: "+info.getInterest_end_time().split(" ")[0]);
				viewHolder.catCompactBtn.setEnabled(true);
				viewHolder.catCompactBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_blue);
			}
		}
		viewHolder.firstMoney.setText(Util.formatRate(info.getMoney()) + "Ԫ");
		double interestMoneyD = 0d;
		double returnTotalMoneyD = 0d;
		double moneyD = 0d;
		double hbMoneyD = 0d;
		try {
			interestMoneyD = Double.parseDouble(info.getInvest_interest());
		} catch (Exception e) {
		}
		try{
			returnTotalMoneyD = Double.parseDouble(info.getReturn_total_money());
			moneyD = Double.parseDouble(info.getMoney());
		}catch (Exception e){

		}
		try{
			hbMoneyD = Double.parseDouble(info.getRed_bag_money());
		}catch (Exception e){
			e.printStackTrace();
		}
		if("�����".equals(info.getReturn_status())){
			viewHolder.nowMoney.setText("0Ԫ");
			viewHolder.interestMoney.setText(Util.formatRate(String.valueOf(returnTotalMoneyD - moneyD - hbMoneyD)) + "Ԫ");
		}else{
			viewHolder.nowMoney.setText(Util.formatRate(info.getInvest_money()) + "Ԫ");
			viewHolder.interestMoney.setText(Util.formatRate(String.valueOf(interestMoneyD)) + "Ԫ");
		}
		double interestAddD = 0d;
		try {
			interestAddD = Double.parseDouble(info.getInterest_add());
		} catch (Exception e) {
		}
		if(info == null || info.getInterest_add().isEmpty() || interestAddD == 0){
			viewHolder.remark.setText("�� ��");
		}else{
			viewHolder.remark.setText("��Ͷ����Ϣ" + info.getInterest_add() + "%");
		}
		if("Ͷ����".equals(info.getReturn_status())){
			boolean isReturn = Util.isReturnYYY(info);
			if(isReturn){
				viewHolder.applyOrCancelBtn.setEnabled(true);
				viewHolder.applyOrCancelBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_blue);
			}else{
				viewHolder.applyOrCancelBtn.setEnabled(false);
				viewHolder.applyOrCancelBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_gray);
			}
			viewHolder.applyOrCancelBtn.setText("ԤԼ���");
			
		}else if("�����".equals(info.getReturn_status())){
			//�����
			viewHolder.applyOrCancelBtn.setText("�������");
			viewHolder.applyOrCancelBtn.setEnabled(false);
			viewHolder.applyOrCancelBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_gray);
			
			viewHolder.investType.setText("�����");
			viewHolder.investType.setTextColor(context.getResources().getColor(R.color.white));
			viewHolder.investType.setBackgroundResource(R.drawable.style_rect_fillet_filling_gray);
		}else if("�����".equals(info.getReturn_status())){
			viewHolder.applyOrCancelBtn.setText("�������");
			viewHolder.applyOrCancelBtn.setEnabled(true);
			viewHolder.applyOrCancelBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_blue);
			
			viewHolder.investType.setText("����");
			viewHolder.investType.setTextColor(context.getResources().getColor(R.color.gray));
			viewHolder.investType.setBackgroundResource(R.drawable.style_rect_fillet_filling_gray);
		}
		
		if (position == 0) {
			convertView.setPadding(0, context.getResources()
					.getDimensionPixelSize(R.dimen.common_measure_15dp), 0, 0);
		}
		if("yyy_zjmx".equals(fromWhere)){
			//Ԫ��ӯ�ʽ���ϸ
			viewHolder.catCompactBtn.setVisibility(View.GONE);
			viewHolder.applyOrCancelBtn.setVisibility(View.GONE);
		}else if("yyy_record".equals(fromWhere)){
			//Ԫ��ӯͶ�ʼ�¼
			viewHolder.catCompactBtn.setVisibility(View.VISIBLE);
			viewHolder.applyOrCancelBtn.setVisibility(View.VISIBLE);
		}
		viewHolder.catCompactBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(onYYYItemClickListener != null)
				onYYYItemClickListener.onCatCompact((Integer)v.getTag(R.id.tag_second), v,
						(InvestRecordInfo)v.getTag(R.id.tag_first));
			}
		});
		viewHolder.applyOrCancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(onYYYItemClickListener != null)
				onYYYItemClickListener.onApplyOrCancel((Integer)v.getTag(R.id.tag_second), v, 
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
		TextView investType;//��Ͷ �� ��Ͷ�ȵ�
		TextView firstTime;//��Ͷ��
		TextView nearEndTime;// ���������
		TextView firstMoney;// ��Ͷ����
		TextView nowMoney;// ��Ͷ���
		TextView interestMoney;//Ͷ������
		TextView remark;
		Button catCompactBtn;// �鿴��ͬ
		Button applyOrCancelBtn;//ԤԼ����ȡ�����
		TextView nhllTV;//�껯����
		TextView nhllTitleTV;//�껯����
	}
	
	/**
	 * Ԫ��ӯ�û�Ͷ�ʼ�¼ҳ�棬���û�������鿴��ͬ���͡�ԤԼ���"��ťʱ�Ļص�����
	 * @author Mr.liu
	 *
	 */
	public interface OnYYYItemClickListener{
		/*
		 * �鿴��ͬ
		 */
		void onCatCompact(int position,View v,InvestRecordInfo info);
		/*
		 * ԤԼ���߳������
		 */
		void onApplyOrCancel(int position,View v,InvestRecordInfo info);
	}
}
