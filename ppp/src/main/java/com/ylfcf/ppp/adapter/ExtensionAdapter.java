package com.ylfcf.ppp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.ExtensionNewInfo;
import com.ylfcf.ppp.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * �ƹ������б�
 * @author Administrator
 * 
 */
public class ExtensionAdapter extends ArrayAdapter<ExtensionNewInfo> {
	private static final int RESOURCE_ID = R.layout.extension_listview_item;
	private Context context;
	private List<ExtensionNewInfo> extensionList = null;
	private LayoutInflater layoutInflater = null;

	public ExtensionAdapter(Context context) {
		super(context, RESOURCE_ID);
		this.context = context;
		extensionList = new ArrayList<ExtensionNewInfo>();
		layoutInflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * ���ⷽ������̬�ı�listview��item������ˢ��
	 * @param incomeList
	 */
	public void setItems(List<ExtensionNewInfo> incomeList) {
		extensionList.clear();
		if (incomeList != null) {
			extensionList.addAll(incomeList);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return extensionList.size();
	}

	@Override
	public ExtensionNewInfo getItem(int position) {
		return extensionList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		ExtensionNewInfo info = extensionList.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(RESOURCE_ID, null);
			viewHolder.phone = (TextView) convertView
					.findViewById(R.id.extension_listview_item_phone);
			viewHolder.time = (TextView) convertView
					.findViewById(R.id.extension_listview_item_time);
			viewHolder.hasInterest = (TextView) convertView
					.findViewById(R.id.extension_listview_item_hasinterest);
			viewHolder.investMoney = (TextView) convertView
					.findViewById(R.id.extension_listview_item_investmoney);
			viewHolder.nameTV = (TextView) convertView
					.findViewById(R.id.extension_listview_item_name);
			viewHolder.interestStartTime = (TextView) convertView
					.findViewById(R.id.extension_listview_item_interest_starttime);
			viewHolder.collectedTime = (TextView) convertView
					.findViewById(R.id.extension_listview_item_collected_time);
			viewHolder.borrowNameTV = (TextView) convertView
					.findViewById(R.id.extension_listview_item_borrowname);
			viewHolder.periodTV = (TextView) convertView
					.findViewById(R.id.extension_listview_item_qixian);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.borrowNameTV.setText(info.getBorrow_name());
		viewHolder.phone.setText(info.getInvest_user_mobile());// �û���
		viewHolder.time.setText("Ͷ��ʱ��: "+info.getInvest_time().split(" ")[0]);
		viewHolder.hasInterest.setText(Util.formatRate(info.getPercentage())+"Ԫ");
		viewHolder.investMoney.setText(Util.formatRate(info.getInvest_money())+"Ԫ");
		viewHolder.nameTV.setText("����: "+Util.hidRealName2(info.getInvest_user_name()));
		viewHolder.interestStartTime.setText(info.getInterest_start_time().split(" ")[0]);
		viewHolder.collectedTime.setText("Ԥ�Ƶ���ʱ��: "+info.getReturn_time().split(" ")[0]);
		if(info.getBorrow_name().contains("нӯ�ƻ�")){
			viewHolder.periodTV.setText("�������: "+info.getInterest_period()+"����");
		}else{
			viewHolder.periodTV.setText("�������: "+info.getInterest_period()+"��");
		}
		return convertView;
	}

	/**
	 * �ڲ��࣬����Item��Ԫ��
	 * @author Mr.liu
	 *
	 */
	class ViewHolder {
		TextView phone;
		TextView time;//Ͷ��ʱ��
		TextView hasInterest;//���
		TextView investMoney;//Ͷ�ʽ��
		TextView nameTV;
		TextView interestStartTime;//��Ϣʱ��
		TextView collectedTime;//Ԥ�Ƶ���ʱ��
		TextView periodTV;//����
		TextView borrowNameTV;//�������
	}

}
