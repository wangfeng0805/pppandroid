package com.ylfcf.ppp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.ui.CompactActivity;
import com.ylfcf.ppp.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * �û�Ͷ�ʼ�¼
 * 
 * @author Administrator
 * 
 */
public class UserInvestRecordAdapter extends ArrayAdapter<InvestRecordInfo> {
	private static final int RESOURCE_ID = R.layout.invest_records_item;
	private Context context;
	private List<InvestRecordInfo> investRecordList = null;
	private LayoutInflater layoutInflater = null;
	private String fromWhere = "";//vip,dqlc�ȵȲ�Ʒ

	public UserInvestRecordAdapter(Context context,String fromWhere) {
		super(context, RESOURCE_ID);
		this.context = context;
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
		notifyDataSetChanged();
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		InvestRecordInfo info = investRecordList.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(RESOURCE_ID, null);
			viewHolder.borrowName = (TextView) convertView
					.findViewById(R.id.invest_records_item_borrowname);
			viewHolder.borrowLogo = (ImageView) convertView.findViewById(R.id.invest_records_item_borrowlogo);
			viewHolder.startTime = (TextView) convertView
					.findViewById(R.id.invest_records_item_starttime);
			viewHolder.endTime = (TextView) convertView
					.findViewById(R.id.invest_records_item_endtime);
			viewHolder.rate = (TextView) convertView
					.findViewById(R.id.invest_record_item_rate);
			viewHolder.addRate = (TextView) convertView
					.findViewById(R.id.invest_record_item_add_rate);
			viewHolder.investMoney = (TextView) convertView
					.findViewById(R.id.invest_record_item_invest_money);
			viewHolder.interestMoney = (TextView) convertView
					.findViewById(R.id.invest_record_item_interest_money);
			viewHolder.status = (TextView) convertView
					.findViewById(R.id.invest_records_item_status);
			viewHolder.addLayout = (LinearLayout) convertView.findViewById(R.id.invest_records_item_add_layout);
			viewHolder.nhsyText = (TextView) convertView.findViewById(R.id.invest_records_item_nhsy_text);
			viewHolder.catCompactBtn = (Button) convertView.findViewById(R.id.invest_record_item_catcompact);
			viewHolder.remarkLayout = (LinearLayout) convertView.findViewById(R.id.invest_records_item_remark_layout);
			viewHolder.remark = (TextView) convertView.findViewById(R.id.invest_record_item_interest_remark);
			viewHolder.interestTextTitle = (TextView) convertView.findViewById(R.id.invest_record_item_interest_money_title);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.borrowName.setText(info.getBorrow_name());// ����
		double interestRateD = 0d;//��������
		double interestRateFloat = 0d;//��������
		double couponRateD = 0d;//��Ϣȯ��Ϣ
		double interestD = 0d;//��Ͷ��Ϣ����
		double androidInterestRateD = 0d;
		try {
			androidInterestRateD = Double.parseDouble(info.getAndroid_interest_rate());
		} catch (Exception e) {
		}
		try {
			interestRateD = Double.parseDouble(info.getInterest_rate());
		} catch (Exception e) {
		}
		try {
			interestRateFloat = Double.parseDouble(info.getInterest_add());
		} catch (Exception e) {
		}
		try {
			couponRateD = Double.parseDouble(info.getCoupon_interest_add());
		} catch (Exception e) {
		}
		try {
			interestD = Double.parseDouble(info.getInterest().replace("%", ""));
		} catch (Exception e) {
		}
		viewHolder.rate.setText(Util.formatRate(String.valueOf(interestRateD + interestRateFloat))+ "%");//�껯����
		double investMoneyD = 0d;
		try {
			investMoneyD = Double.parseDouble(info.getFact_money());
			viewHolder.investMoney.setText((int)investMoneyD + "Ԫ");
		} catch (Exception e) {
		}
		viewHolder.status.setText("Ͷ��״̬:" + info.getStatus());
		viewHolder.catCompactBtn.setTag(info);
		if("yzy".equals(fromWhere)){
			if("���ֱ�".equals(info.getBorrow_type())){
				viewHolder.borrowLogo.setVisibility(View.VISIBLE);
			}else{
				viewHolder.borrowLogo.setVisibility(View.GONE);
			}
			if(info.getAdd_time() == null || "".equals(info.getAdd_time()) || "0000-00-00 00:00:00".equals(info.getAdd_time())){
				viewHolder.startTime.setText("Ͷ������:  �� ��");
			}else{
				viewHolder.startTime.setText("Ͷ������: " + info.getAdd_time().split(" ")[0]);
			}
			if(info.getEnd_time() == null || "".equals(info.getEnd_time()) || "0000-00-00 00:00:00".equals(info.getEnd_time())){
				viewHolder.endTime.setText("����ʱ��:  �� ��");
				viewHolder.catCompactBtn.setEnabled(false);
				viewHolder.catCompactBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_gray);
			}else{
				viewHolder.endTime.setText("����ʱ��: " + info.getEnd_time().split(" ")[0]);
				viewHolder.catCompactBtn.setEnabled(true);
				viewHolder.catCompactBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_blue);
			}
			viewHolder.nhsyText.setText("�껯����");
			viewHolder.addLayout.setVisibility(View.VISIBLE);
			viewHolder.remarkLayout.setVisibility(View.GONE);
			viewHolder.catCompactBtn.setVisibility(View.VISIBLE);			
			//��Ϣ
			double sumInterestD = 0d;
			try {
				sumInterestD = Double.parseDouble(info.getSum_interest());                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
			} catch (Exception e) {
			}
			viewHolder.rate.setText(Util.formatRate(String.valueOf(interestRateD + interestRateFloat))+ "%");//�껯����
			if(couponRateD + interestD <= 0){
				viewHolder.addRate.setText("�� ��");
			}else{
				viewHolder.addRate.setText(Util.formatRate(String.valueOf(couponRateD + interestD))+"%");//��Ϣ����
			}
			int interestPeroidI = 0;//Ͷ������
			try {
				interestPeroidI = Integer.parseInt(info.getInterest_period());
			} catch (Exception e) {
			}
			double totalInvestMoneyD = 0d;
			try {
				totalInvestMoneyD = Double.parseDouble(info.getMoney());
			} catch (Exception e) {
			}
			double redbagMoneyD = 0d;
			try{
				redbagMoneyD = Double.parseDouble(info.getRed_bag_money());//������
			}catch (Exception e){

			}
			double totalRate = interestRateD + interestRateFloat + couponRateD + interestD;//��������+��������+��Ϣ����+��Ϣȯ
			double totalInterestD = totalInvestMoneyD * totalRate * interestPeroidI / 36500 + redbagMoneyD * interestRateD * interestPeroidI / 36500;
			if(sumInterestD == 0){
				//��û��������ű����Լ�����
				viewHolder.interestMoney.setText(Util.formatRate(Util.double2PointDouble(totalInterestD)) + "Ԫ");//Ͷ������
			}else{
				//��������ű���ֱ��ȡֵ
				viewHolder.interestMoney.setText(Util.formatRate(info.getSum_interest()) + "Ԫ");//Ͷ������
			}
		}else if("vip".equals(fromWhere)){
			if(info.getInvest_start_time() == null || "".equals(info.getInvest_start_time()) || "0000-00-00 00:00:00".equals(info.getInvest_start_time())){
				viewHolder.startTime.setText("Ͷ������:  �� ��");
			}else{
				viewHolder.startTime.setText("Ͷ������: " + info.getInvest_start_time().split(" ")[0]);
			}
			if(info.getInvest_end_time() == null || "".equals(info.getInvest_end_time()) || "0000-00-00 00:00:00".equals(info.getInvest_end_time())){
				viewHolder.endTime.setText("����ʱ��:  �� ��");
				viewHolder.catCompactBtn.setEnabled(false);
				viewHolder.catCompactBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_gray);
			}else{
				viewHolder.endTime.setText("����ʱ��: " + info.getInvest_end_time().split(" ")[0]);
				viewHolder.catCompactBtn.setEnabled(true);
				viewHolder.catCompactBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_blue);
			}
			viewHolder.nhsyText.setText("ҵ���Ƚϻ�׼");
			viewHolder.catCompactBtn.setVisibility(View.VISIBLE);
			try {
				viewHolder.interestMoney.setText(Util.formatRate(info.getInterest())+ "Ԫ");
			} catch (Exception e) {
				viewHolder.interestMoney.setText("0Ԫ");
			}
			viewHolder.addLayout.setVisibility(View.VISIBLE);
			viewHolder.remarkLayout.setVisibility(View.GONE);
			if(androidInterestRateD + couponRateD <= 0){
				viewHolder.addRate.setText("�� ��");
			}else{
				viewHolder.addRate.setText(Util.formatRate(String.valueOf(androidInterestRateD + couponRateD))+"%");
			}
			try {
				investMoneyD = Double.parseDouble(info.getMoney());
				viewHolder.investMoney.setText((int)investMoneyD + "Ԫ");
			} catch (Exception e) {
			}
		}else if("srzx".equals(fromWhere)){
			if(info.getInvest_time() == null || "".equals(info.getInvest_time()) || "0000-00-00 00:00:00".equals(info.getInvest_time())){
				viewHolder.startTime.setText("Ͷ������:  �� ��");
			}else{
				viewHolder.startTime.setText("Ͷ������: " + info.getInvest_time().split(" ")[0]);
			}
			if(info.getInterest_end_time() == null || "".equals(info.getInterest_end_time()) || "0000-00-00 00:00:00".equals(info.getInterest_end_time())){
				viewHolder.endTime.setText("����ʱ��:  �� ��");
				viewHolder.catCompactBtn.setEnabled(false);
				viewHolder.catCompactBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_gray);
			}else{
				viewHolder.endTime.setText("����ʱ��: " + info.getInterest_end_time().split(" ")[0]);
				viewHolder.catCompactBtn.setEnabled(true);
				viewHolder.catCompactBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_blue);
			}
			viewHolder.nhsyText.setText("�껯����");
			viewHolder.addLayout.setVisibility(View.VISIBLE);
			viewHolder.remarkLayout.setVisibility(View.GONE);
			viewHolder.catCompactBtn.setVisibility(View.VISIBLE);
			double interestSRZX = 0d;
			double hbInterestSRZX = 0d;
			try{
				interestSRZX = Double.parseDouble(info.getInterest());
				hbInterestSRZX = Double.parseDouble(info.getRed_bag_interest());
			}catch (Exception e){

			}
			viewHolder.interestMoney.setText(Util.formatRate(String.valueOf(interestSRZX + hbInterestSRZX)) + "Ԫ");
			viewHolder.status.setText("Ͷ��״̬: " + info.getInvest_status());
			//��Ϣ
			viewHolder.rate.setText(Util.formatRate(String.valueOf(interestRateD))+ "%");//�껯����
			if(androidInterestRateD + interestRateFloat + couponRateD <= 0){
				viewHolder.addRate.setText("�� ��");
			}else{
				viewHolder.addRate.setText(Util.formatRate(String.valueOf(androidInterestRateD + interestRateFloat + couponRateD))+"%");
			}
			try {
				investMoneyD = Double.parseDouble(info.getMoney());
				viewHolder.investMoney.setText((int)investMoneyD + "Ԫ");
			} catch (Exception e) {
			}
		}else if("yjy".equals(fromWhere)){
			//Ԫ��ӯ
			if(info.getInvest_time() == null || "".equals(info.getInvest_time()) || "0000-00-00 00:00:00".equals(info.getInvest_time())){
				viewHolder.startTime.setText("Ͷ������:  �� ��");
			}else{
				viewHolder.startTime.setText("Ͷ������: " + info.getInvest_time().split(" ")[0]);
			}
			if(info.getInterest_end_time() == null || "".equals(info.getInterest_end_time()) || "0000-00-00 00:00:00".equals(info.getInterest_end_time())){
				viewHolder.endTime.setText("����ʱ��:  �� ��");
				viewHolder.catCompactBtn.setEnabled(false);
				viewHolder.catCompactBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_gray);
			}else{
				viewHolder.endTime.setText("����ʱ��: " + info.getInterest_end_time().split(" ")[0]);
				viewHolder.catCompactBtn.setEnabled(true);
				viewHolder.catCompactBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_blue);
			}
			viewHolder.nhsyText.setText("�껯����");
			viewHolder.addLayout.setVisibility(View.GONE);
			viewHolder.remarkLayout.setVisibility(View.VISIBLE);
			viewHolder.catCompactBtn.setVisibility(View.VISIBLE);
			viewHolder.interestTextTitle.setText("Ͷ��������");
			double interestSRZX = 0d;
			double hbInterestSRZX = 0d;
			try{
				interestSRZX = Double.parseDouble(info.getInterest());
				hbInterestSRZX = Double.parseDouble(info.getRed_bag_interest());
			}catch (Exception e){

			}
			viewHolder.interestMoney.setText(Util.formatRate(String.valueOf(interestSRZX + hbInterestSRZX)) + "Ԫ");
			viewHolder.status.setText("Ͷ��״̬: " + info.getInvest_status());
			//��Ϣ
			viewHolder.rate.setText(Util.formatRate(String.valueOf(interestRateD))+ "%");//�껯����
			try {
				investMoneyD = Double.parseDouble(info.getMoney());
				viewHolder.investMoney.setText((int)investMoneyD + "Ԫ");
			} catch (Exception e) {
			}
		}else if("xsmb".equals(fromWhere)){
			if(info.getInvest_time() == null || "".equals(info.getInvest_time()) || "0000-00-00 00:00:00".equals(info.getInvest_time())){
				viewHolder.startTime.setText("Ͷ������:  �� ��");
			}else{
				viewHolder.startTime.setText("Ͷ������: " + info.getInvest_time().split(" ")[0]);
			}
			if(info.getInterest_end_time() == null || "".equals(info.getInterest_end_time()) || "0000-00-00 00:00:00".equals(info.getInterest_end_time())){
				viewHolder.endTime.setText("����ʱ��:  �� ��");
				viewHolder.catCompactBtn.setEnabled(false);
				viewHolder.catCompactBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_gray);
			}else{
				viewHolder.endTime.setText("����ʱ��: " + info.getInterest_end_time().split(" ")[0]);
				viewHolder.catCompactBtn.setEnabled(true);
				viewHolder.catCompactBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_blue);
			}
			viewHolder.nhsyText.setText("�껯����");
			viewHolder.addLayout.setVisibility(View.GONE);
			viewHolder.remarkLayout.setVisibility(View.VISIBLE);
			viewHolder.catCompactBtn.setVisibility(View.VISIBLE);
			viewHolder.status.setText("Ͷ��״̬: " + info.getInvest_status());
			int interestDays = 0;
			try {
				interestDays = Integer.parseInt(info.getInterest_days());
			} catch (Exception e) {
			}
			viewHolder.interestMoney.setText(Util.formatRate(Util.double2PointDouble(investMoneyD * interestRateD /100 * interestDays / 365)) + "Ԫ");
			viewHolder.rate.setText(Util.formatRate(String.valueOf(interestRateD + interestRateFloat)) + "%");

			try {
				investMoneyD = Double.parseDouble(info.getMoney());
				viewHolder.investMoney.setText((int)investMoneyD + "Ԫ");
			} catch (Exception e) {
			}
		}
		viewHolder.catCompactBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				InvestRecordInfo recordInfo = (InvestRecordInfo) v.getTag();
				Intent intent = new Intent(context,CompactActivity.class);
				intent.putExtra("invest_record", recordInfo);
				intent.putExtra("from_where", fromWhere);
				context.startActivity(intent);
			}
		});
		if (position == 0) {
			convertView.setPadding(0, context.getResources()
					.getDimensionPixelSize(R.dimen.common_measure_15dp), 0, 0);
		}else{
			convertView.setPadding(0, 0, 0, 0);
		}
		return convertView;
	}

	/**
	 * �ڲ��࣬����Item��Ԫ��
	 * @author Mr.liu
	 *
	 */
	class ViewHolder {
		TextView borrowName;
		ImageView borrowLogo;
		TextView startTime;
		TextView endTime;
		TextView rate;// �껯����
		TextView addRate;// ��Ϣ����
		TextView investMoney;// Ͷ�ʽ��
		TextView interestMoney;// ����
		TextView status;
		TextView nhsyText;//�껯�����ĸ�����VIP��Ʒ��Ҫ�ĳɡ�ҵ���Ƚϻ�׼��
		TextView interestTextTitle;
		LinearLayout addLayout;//��Ϣ�����Ĳ���
		LinearLayout remarkLayout;//��ע
		TextView remark;//��ע
		Button catCompactBtn;
	}
}
