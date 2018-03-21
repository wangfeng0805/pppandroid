package com.ylfcf.ppp.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.TYJInfo;
import com.ylfcf.ppp.util.Util;

/**
 * ������ϸ--�����
 * @author Mr.liu
 */
public class TYJListAdapter extends ArrayAdapter<TYJInfo> {
	private static final int RESOURCE_ID = R.layout.tyj_list_item;
	private final LayoutInflater mInflater;

	private List<TYJInfo> tyjInfoList;
	private Context context;
	private OnTYJListItemClickListener listener;

	public TYJListAdapter(Context context, OnTYJListItemClickListener listener) {
		super(context, RESOURCE_ID);
		this.context = context;
		this.listener = listener;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		tyjInfoList = new ArrayList<TYJInfo>();
	}

	/**
	 * ���ⷽ������̬�ı�listview��item������ˢ��
	 * @param tyjList
	 */
	public void setItems(List<TYJInfo> tyjList) {
		this.tyjInfoList.clear();
		if (tyjList != null) {
			this.tyjInfoList.addAll(tyjList);
		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return tyjInfoList.size();
	}

	@Override
	public TYJInfo getItem(int position) {
		return tyjInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * ���þ���һ����ĵ���ʾ����
	 */
	private int clickPosition = 0;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		clickPosition = position;
		TYJInfo info = tyjInfoList.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(RESOURCE_ID, null);
			viewHolder.tyjText = (TextView) convertView
					.findViewById(R.id.tyj_list_item_text);
			viewHolder.useLimit = (TextView) convertView
					.findViewById(R.id.tyj_list_item_uselimit);
			viewHolder.usedTime = (TextView) convertView
					.findViewById(R.id.tyj_list_item_time);
			viewHolder.button = (Button) convertView
					.findViewById(R.id.tyj_list_item_btn);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tyjText.setText(info.getAccount());
		long needInvestMoneyL = 0l;
		try {
			needInvestMoneyL = Long.parseLong(info.getNeed_invest_money());
		} catch (Exception e) {
		}
		if (needInvestMoneyL <= 0) {
			viewHolder.useLimit.setText("������");
		} else {
			viewHolder.useLimit.setText("���û�Ͷ��" + info.getNeed_invest_money()
					+ "Ԫ����");
		}
		viewHolder.usedTime.setText(info.getEnd_time());
		long nowTime = System.currentTimeMillis();
		long endTime = Util.string2date(info.getEnd_time());
		if (nowTime > endTime || "��ʹ��".equals(info.getStatus())) {
			viewHolder.button.setVisibility(View.GONE);
		} else {
			viewHolder.button.setVisibility(View.VISIBLE);
		}
		viewHolder.button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onclick(v, clickPosition);
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
		TextView tyjText;// �������
		TextView useLimit;// ʹ������
		TextView usedTime;// ��Ч��
		Button button;// ʹ���ҵ������
	}

	public interface OnTYJListItemClickListener {
		void onclick(View v, int position);
	}
}
