package com.ylfcf.ppp.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.YXBInvestRecordInfo;
/**
 * Ԫ�ű��Ϲ�
 * @author Administrator
 *
 */
public class YXBInvestRecordAdapter extends ArrayAdapter<YXBInvestRecordInfo>{
	private final static int RESOURCE_ID = R.layout.yxb_trans_record_list_item;
	
	private List<YXBInvestRecordInfo> yxbRecordList;
	private Context context;
	private LayoutInflater layoutInflater;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
	
	public YXBInvestRecordAdapter(Context context) {
		super(context, RESOURCE_ID);
		this.context = context;
		yxbRecordList = new ArrayList<YXBInvestRecordInfo>();
		layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}
	
	/**
	 * ���ⷽ������̬�ı�listview��item������ˢ��
	 * @param list
	 */
	public void setItems(List<YXBInvestRecordInfo> list){
		yxbRecordList.clear();
		if(list != null){
			yxbRecordList.addAll(list);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return yxbRecordList.size();
	}

	@Override
	public YXBInvestRecordInfo getItem(int position) {
		return yxbRecordList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		YXBInvestRecordInfo info = yxbRecordList.get(position);
		ViewHolder viewHolder = null;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(RESOURCE_ID, null);
			viewHolder.yxbMoney = (TextView)convertView.findViewById(R.id.yxb_trans_record_item_money);
			viewHolder.dateText = (TextView)convertView.findViewById(R.id.yxb_trans_record_item_date);
			viewHolder.typeText = (TextView)convertView.findViewById(R.id.yxb_trans_record_item_type);
			viewHolder.statusText = (TextView)convertView.findViewById(R.id.yxb_trans_record_item_status);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.yxbMoney.setText(info.getOrder_money());
		viewHolder.typeText.setText("�Ϲ�");
		viewHolder.statusText.setText("�Ϲ��ɹ�");
		try {
			Date applyDate = sdf.parse(info.getOrder_time());
			viewHolder.dateText.setText(sdf1.format(applyDate));
		} catch (ParseException e) {
			e.printStackTrace();
			viewHolder.dateText.setText(info.getOrder_time());
		}
		return convertView;
	}

	/**
	 * �ڲ��࣬����Item��Ԫ��
	 * @author Mr.liu
	 *
	 */
	class ViewHolder{
		TextView yxbMoney;//���׽��
		TextView dateText;
		TextView typeText;//����
		TextView statusText;//״̬
	}
}
