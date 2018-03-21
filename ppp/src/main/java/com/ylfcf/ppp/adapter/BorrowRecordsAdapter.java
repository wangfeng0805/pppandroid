package com.ylfcf.ppp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.util.Util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * ĳ֧���Ͷ�ʼ�¼�������û���Ͷ�ʼ�¼...��
 * @author Administrator
 *
 */
public class BorrowRecordsAdapter extends ArrayAdapter<InvestRecordInfo>{
	private static final int RESOURCE_ID = R.layout.borrow_records_item;  
	private Context context;
	private List<InvestRecordInfo> investRecordList = null;
	private LayoutInflater layoutInflater = null;
	private DecimalFormat df = new DecimalFormat("#.00");//���ָ�ʽ����#��ʾһ����0������
	
	public BorrowRecordsAdapter(Context context){
		super(context, RESOURCE_ID);
		this.context = context;
		investRecordList = new ArrayList<InvestRecordInfo>();
		layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}
	
	/**
	 * ���ⷽ������̬�ı�listview��item������ˢ��
	 * @param recordList
	 */
	public void setItems(List<InvestRecordInfo> recordList){
		investRecordList.clear();
		if(recordList != null){
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
		double moneyD = 0d;
		InvestRecordInfo info = investRecordList.get(position);
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(RESOURCE_ID, null);
			viewHolder.investor = (TextView)convertView.findViewById(R.id.borrow_records_item_investor);
			viewHolder.investType = (TextView)convertView.findViewById(R.id.borrow_records_item_type);
			viewHolder.investMoney = (TextView)convertView.findViewById(R.id.borrow_records_item_money);
			viewHolder.investTime = (TextView)convertView.findViewById(R.id.borrow_records_item_time);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(position%2 == 0){
			convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
		}else{
			convertView.setBackgroundColor(context.getResources().getColor(R.color.blue5));
		}
		
		viewHolder.investor.setText(Util.hiddenUsername(info.getUser_name()));//�û���
		viewHolder.investType.setText("�ֶ�");
		if(info.getAdd_time() == null || "".equals(info.getAdd_time())){
			viewHolder.investTime.setText(info.getFirst_borrow_time());
		}else{
			viewHolder.investTime.setText(info.getAdd_time());
		}
		try {
			moneyD = Double.parseDouble(info.getMoney());
			if(moneyD >= 10000){
				if(moneyD % 10000 == 0){
					//һ���������
					viewHolder.investMoney.setText((int)(moneyD/10000)+"��");
				}else{
					viewHolder.investMoney.setText((int)moneyD/10000d+"��");
				}
			}else{
				viewHolder.investMoney.setText(Util.formatRate(String.valueOf(moneyD)));
			}
		} catch (Exception e) {
			viewHolder.investMoney.setText(info.getMoney());
		}
		return convertView;
	}
	
	/**
	 * �ڲ��࣬����Item��Ԫ��
	 * @author Mr.liu
	 *
	 */
	class ViewHolder{
		TextView investor;//Ͷ����
		TextView investType;//Ͷ������
		TextView investMoney;
		TextView investTime;
	}

}
