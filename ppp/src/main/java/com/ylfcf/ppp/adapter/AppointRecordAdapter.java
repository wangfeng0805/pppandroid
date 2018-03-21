package com.ylfcf.ppp.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.MoneyStatus;
import com.ylfcf.ppp.entity.SRZXAppointRecordInfo;
import com.ylfcf.ppp.util.Util;

/**
 * ˽�������ƷԤԼ
 * @author Mr.liu
 *
 */
public class AppointRecordAdapter extends ArrayAdapter<SRZXAppointRecordInfo>{
	private static final int RESOURCE_ID = R.layout.appoint_record_item;  
	private Context context;
	private List<SRZXAppointRecordInfo> recordList = null;
	private LayoutInflater layoutInflater = null;
	private DecimalFormat df = new DecimalFormat("#.00");//���ָ�ʽ����#��ʾһ����0������
	private int count = 0;
	
	public AppointRecordAdapter(Context context){
		super(context, RESOURCE_ID);
		this.context = context;
		recordList = new ArrayList<SRZXAppointRecordInfo>();
		layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}
	
	/**
	 * ���ⷽ������̬�ı�listview��item������ˢ��
	 * @param recordList
	 */
	public void setItems(List<SRZXAppointRecordInfo> recordList){
		this.recordList.clear();
		this.count = recordList.size();
		if(recordList != null){
			this.recordList.addAll(recordList);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return recordList.size();
	}

	@Override
	public SRZXAppointRecordInfo getItem(int position) {
		return recordList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		SRZXAppointRecordInfo info = recordList.get(position);
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(RESOURCE_ID, null);
			viewHolder.phone = (TextView)convertView.findViewById(R.id.appoint_record_item_phone);
			viewHolder.money = (TextView)convertView.findViewById(R.id.appoint_record_item_money);
			viewHolder.status = (TextView)convertView.findViewById(R.id.appoint_record_item_status);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.phone.setText(Util.hidPhoneNum(info.getMobile()));
		if("�����".equals(info.getBorrow_status()) || "���ͨ��".equals(info.getBorrow_status()) 
				|| info.getBorrow_status() == null || info.getBorrow_status().isEmpty() || "null".equals(info.getBorrow_status()) || "NULL".equals(info.getBorrow_status())){
			viewHolder.status.setText("������");
		}else{
			viewHolder.status.setText(info.getMoney_status());
		}
		double moneyD = 0d;
		try {
			moneyD = Double.parseDouble(info.getMoney());
			viewHolder.money.setText(Util.double2PointDouble(moneyD/10000));
		} catch (Exception e) {
		}
		return convertView;
	}
	
	/**
	 * �ڲ��࣬����Item��Ԫ��
	 * @author Mr.liu
	 *
	 */
	class ViewHolder{
		TextView phone;
		TextView money;//ԤԼ���
		TextView status;//��Ʒ״̬
	}

}
