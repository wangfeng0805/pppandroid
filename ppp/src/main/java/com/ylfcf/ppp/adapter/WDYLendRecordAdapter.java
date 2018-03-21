package com.ylfcf.ppp.adapter;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.WDYChildRecordInfo;
import com.ylfcf.ppp.util.Util;

/**
 * �ȶ�ӯ�����¼
 * @author Mr.liu
 *
 */
public class WDYLendRecordAdapter extends ArrayAdapter<WDYChildRecordInfo>{
	private static final int RESOURCE_ID = R.layout.wdy_lendrecord_item;  
	private Context context;
	private List<WDYChildRecordInfo> lendRecordList = null;
	private LayoutInflater layoutInflater = null;
	private DecimalFormat df = new DecimalFormat("#.00");//���ָ�ʽ����#��ʾһ����0������
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String nowTimeStr;
	private String endTime;
	private WdyReinvestInter wdyReinvestInter;
	
	public WDYLendRecordAdapter(Context context,String endTime,WdyReinvestInter wdyReinvestInter){
		super(context, RESOURCE_ID);
		this.context = context;
		lendRecordList = new ArrayList<WDYChildRecordInfo>();
		this.endTime = endTime;
		this.wdyReinvestInter = wdyReinvestInter;
		layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}
	
	/**
	 * ���ⷽ������̬�ı�listview��item������ˢ��
	 * @param recordList
	 */
	public void setItems(List<WDYChildRecordInfo> recordList,String sysTime){
		this.nowTimeStr = sysTime;
		lendRecordList.clear();
		if(recordList != null){
			lendRecordList.addAll(recordList);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return lendRecordList.size();
	}

	@Override
	public WDYChildRecordInfo getItem(int position) {
		return lendRecordList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		double moneyD = 0d;
		long nowTime;
		try {
			nowTime = sdf1.parse(nowTimeStr).getTime();
		} catch (ParseException e2) {
			nowTime = System.currentTimeMillis();
			e2.printStackTrace();
		}
		long planInvestTime = 0;
		long tomorrowTime = 0;
		final WDYChildRecordInfo info = lendRecordList.get(position);
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(RESOURCE_ID, null);	
			viewHolder.planDate = (TextView)convertView.findViewById(R.id.wdy_lendrecord_item_plandate);
			viewHolder.realDate = (TextView)convertView.findViewById(R.id.wdy_lendrecord_item_realdate);
			viewHolder.investMoney = (TextView)convertView.findViewById(R.id.wdy_lendrecord_item_money);
			viewHolder.option = (TextView)convertView.findViewById(R.id.wdy_lendrecord_item_option);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.planDate.setText(info.getPlan_add_time().split(" ")[0]);//�ƻ�����ʱ��
		if(info.getFact_add_time() == null || "".equals(info.getFact_add_time())
				|| "0000-00-00 00:00:00".equals(info.getFact_add_time())){
			viewHolder.realDate.setText("һ һ");//ʵ�ʼ���ʱ��
		}else{
			viewHolder.realDate.setText(info.getFact_add_time().split(" ")[0]);//ʵ�ʼ���ʱ��
		}
		if(position > 0){
			try {
				Date planInvestDate = sdf1.parse(info.getPlan_add_time());
				planInvestTime = planInvestDate.getTime();
				tomorrowTime = planInvestTime + 3600*24*1000;
				if(position < lendRecordList.size() - 1){
					//�������һ��
					Date nextPlanDate  = sdf1.parse(lendRecordList.get(position+1).getPlan_add_time());
					if(nowTime >= planInvestTime && nowTime < tomorrowTime){
						//˵���ǵ���
						if("��".equals(info.getIs_invest())){
							//û��Ͷ�ʹ�
							viewHolder.option.setText("֧�����ڳ�����");
							viewHolder.option.setTextColor(context.getResources().getColor(R.color.common_topbar_bg_color));
							viewHolder.option.setEnabled(true);
						}else{
							viewHolder.option.setText("һ һ");
							viewHolder.option.setTextColor(context.getResources().getColor(R.color.gray1));
							viewHolder.option.setEnabled(false);
						}
					}else if(nowTime > tomorrowTime && nowTime < nextPlanDate.getTime()){
						//���ǵ��죬����ʵ����һ��֮ǰ
						if("��".equals(info.getIs_invest())){
							//û��Ͷ�ʹ�
							viewHolder.option.setText("����֧������");
							viewHolder.option.setTextColor(context.getResources().getColor(R.color.common_topbar_bg_color));
							viewHolder.option.setEnabled(true);
						}else{
							viewHolder.option.setText("һ һ");
							viewHolder.option.setTextColor(context.getResources().getColor(R.color.gray1));
							viewHolder.option.setEnabled(false);
						}
					}else{
						viewHolder.option.setText("һ һ");
						viewHolder.option.setTextColor(context.getResources().getColor(R.color.gray1));
						viewHolder.option.setEnabled(false);
					}
				}else if(position == lendRecordList.size() - 1){
					//���һ��
					Date endDate  = sdf1.parse(Util.addMonthByNow(lendRecordList.get(position).getPlan_add_time()));
					if(nowTime >= planInvestTime && nowTime < tomorrowTime){
						//˵���ǵ���
						if("��".equals(info.getIs_invest())){
							//û��Ͷ�ʹ�
							viewHolder.option.setText("֧������");
							viewHolder.option.setTextColor(context.getResources().getColor(R.color.common_topbar_bg_color));
							viewHolder.option.setEnabled(true);
						}else{
							viewHolder.option.setText("һ һ");
							viewHolder.option.setTextColor(context.getResources().getColor(R.color.gray1));
							viewHolder.option.setEnabled(false);
						}
					}else if(nowTime > tomorrowTime && nowTime < endDate.getTime()){
						//���ǵ��죬
						if("��".equals(info.getIs_invest())){
							//û��Ͷ�ʹ�
							viewHolder.option.setText("����֧������");
							viewHolder.option.setTextColor(context.getResources().getColor(R.color.common_topbar_bg_color));
							viewHolder.option.setEnabled(true);
						}else{
							viewHolder.option.setText("һ һ");
							viewHolder.option.setTextColor(context.getResources().getColor(R.color.gray1));
							viewHolder.option.setEnabled(false);
						}
					}else{
						viewHolder.option.setText("һ һ");
						viewHolder.option.setTextColor(context.getResources().getColor(R.color.gray1));
						viewHolder.option.setEnabled(false);
					}
				}
				
			} catch (ParseException e1) {  	
				e1.printStackTrace();
			}
		}else{
			viewHolder.option.setText("һ һ");
			viewHolder.option.setTextColor(context.getResources().getColor(R.color.gray1));
			viewHolder.option.setEnabled(false);
		}
		viewHolder.option.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				wdyReinvestInter.reinvest(info);
			}
		});
		try {
			moneyD = Double.parseDouble(info.getInvest_money());
			if(moneyD >= 10000){
				viewHolder.investMoney.setText(Util.double2PointDouble(moneyD/10000)+"��");
			}else{
				viewHolder.investMoney.setText(Util.double2PointDouble(moneyD));
			}
		} catch (Exception e) {
			viewHolder.investMoney.setText(info.getInvest_money());
		}
		return convertView;
	}
	
	/**
	 * �ڲ��࣬����Item��Ԫ��
	 * @author Mr.liu
	 *
	 */
	class ViewHolder{
		TextView planDate;//
		TextView realDate;//
		TextView investMoney;
		TextView option;
	}

	public interface WdyReinvestInter{
		void reinvest(WDYChildRecordInfo info);
	}
}
