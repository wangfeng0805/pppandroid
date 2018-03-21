package com.ylfcf.ppp.adapter;

import java.util.ArrayList;
import java.util.List;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.UserInvestRecordAdapter.ViewHolder;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.WithdrawOrderInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * �����б�
 * @author Administrator
 *
 */
public class WithdrawListAdapter extends ArrayAdapter<WithdrawOrderInfo>{
	private static final int RESOURCE_ID = R.layout.withdraw_list_item;  
	private Context context;
	private List<WithdrawOrderInfo> withdrawList = null;
	private LayoutInflater layoutInflater = null;
	
	public WithdrawListAdapter(Context context){
		super(context, RESOURCE_ID);
		this.context = context;
		withdrawList = new ArrayList<WithdrawOrderInfo>();
		layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}
	
	/**
	 * ���ⷽ������̬�ı�listview��item������ˢ��
	 * @param list
	 */
	public void setItems(List<WithdrawOrderInfo> list){
		withdrawList.clear();
		if(list != null){
			withdrawList.addAll(list);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return withdrawList.size();
	}

	@Override
	public WithdrawOrderInfo getItem(int position) {
		return withdrawList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		WithdrawOrderInfo info = withdrawList.get(position);
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(RESOURCE_ID, null);
			viewHolder.order = (TextView)convertView.findViewById(R.id.withdraw_list_item_ordernum);
			viewHolder.money = (TextView)convertView.findViewById(R.id.withdraw_list_item_money);
			viewHolder.time = (TextView)convertView.findViewById(R.id.withdraw_list_item_time);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
//		if(position%2 == 0){
//			convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
//		}else{
//			convertView.setBackgroundColor(context.getResources().getColor(R.color.blue5));
//		}
		
		viewHolder.order.setText(info.getCash_order());//�û���
		viewHolder.money.setText(info.getCash_account());
		viewHolder.time.setText(info.getAdd_time());
		
		return convertView;
	}
	
	/**
	 * �ڲ��࣬����Item��Ԫ��
	 * @author Mr.liu
	 *
	 */
	class ViewHolder{
		TextView order;
		TextView money;//Ͷ������
		TextView time;
	}
}
