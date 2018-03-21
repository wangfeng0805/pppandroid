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
import com.ylfcf.ppp.entity.RedBagInfo;
import com.ylfcf.ppp.util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ���������
 * 
 * @author Administrator
 */
public class RedBagAdapter extends ArrayAdapter<RedBagInfo> {
	private static final int RESOURCE_ID = R.layout.redbag_item;
	private Context context;
	private LayoutInflater layoutInflater;
	private List<RedBagInfo> redbagList;
	private OnHBListItemClickListener onItemClickListener;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Date sysNowTime;

	public RedBagAdapter(Context context, OnHBListItemClickListener listener) {
		super(context, RESOURCE_ID);
		this.context = context;
		this.onItemClickListener = listener;
		redbagList = new ArrayList<RedBagInfo>();
		layoutInflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * ���ⷽ������̬�ı�listview��item������ˢ��
	 * @param list
	 * @param nowTime ��ǰϵͳʱ��
	 */
	public void setItems(List<RedBagInfo> list,String nowTime) {
		try {
			this.sysNowTime = sdf.parse(nowTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.redbagList.clear();
		if (list != null) {
			this.redbagList.addAll(list);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return redbagList.size();
	}

	@Override
	public RedBagInfo getItem(int position) {
		return redbagList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	int curPosition = 0;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		curPosition = position;
		RedBagInfo info = redbagList.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(RESOURCE_ID, null);
			viewHolder.eduText = (TextView) convertView
					.findViewById(R.id.redbag_item_edu);
			viewHolder.validityText = (TextView) convertView
					.findViewById(R.id.regbag_item_validity);
			viewHolder.useLimitText = (TextView) convertView
					.findViewById(R.id.regbag_item_use_limit);
			viewHolder.btn = (Button) convertView
					.findViewById(R.id.redbag_list_item_btn);
			viewHolder.remark = (TextView) convertView
					.findViewById(R.id.regbag_item_remark);
			viewHolder.useFanweiTitleText = (TextView) convertView
					.findViewById(R.id.regbag_item_usefanwei_title);
			viewHolder.useFanweiText = (TextView) convertView
					.findViewById(R.id.regbag_item_usefanwei);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if ("δʹ��".equals(info.getUse_status())) {
			viewHolder.btn.setVisibility(View.VISIBLE);
			Date endDate = null;
			Date startDate = null;
			try {
				endDate = sdf.parse(info.getEnd_time());
				startDate = sdf.parse(info.getStart_time());
				if(sysNowTime.compareTo(startDate) == 1 && endDate.compareTo(sysNowTime) == 1){
					//˵������Ѿ���Ч
					viewHolder.btn.setEnabled(true);
					viewHolder.btn.setTextColor(context.getResources().getColor(R.color.common_topbar_bg_color));
					viewHolder.btn.setBackgroundResource(R.drawable.style_rect_fillet_blue_15dp);
				}else{
					//���δ��Ч
					viewHolder.btn.setEnabled(false);
					viewHolder.btn.setTextColor(context.getResources().getColor(R.color.gray));
					viewHolder.btn.setBackgroundResource(R.drawable.style_rect_fillet_gray_15dp);
				}
				if(endDate.compareTo(sysNowTime) == -1){
					//�ѹ���
					viewHolder.btn.setVisibility(View.GONE);
				}else{
					viewHolder.btn.setVisibility(View.VISIBLE);
				}
			} catch (Exception e) {
			}


		} else {
			viewHolder.btn.setVisibility(View.GONE);
		}
		viewHolder.useFanweiTitleText.setText("���÷�Χ��");
		viewHolder.useFanweiText.setText(info.getInvest_type().replace("Ԫ��ͨ","Ԫ��ӯ32��").replace("Ԫ����","Ԫ��ӯ92��")
				.replace("Ԫ����","Ԫ��ӯ182��").replace("Ԫ����","Ԫ��ӯ365��"));
		viewHolder.btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onItemClickListener != null) {
					onItemClickListener.onItemClick(v, curPosition);
				}
			}
		});
		if(info.getRemark() != null && !"".equals(info.getRemark())){
			viewHolder.remark.setText("��ע��"+info.getRemark());
		}else{
			viewHolder.remark.setText("��ע�� �� ��");
		}
		viewHolder.eduText.setText(Util.formatRate(info.getMoney()));
		if("��ʹ��".equals(info.getUse_status())){
			viewHolder.useFanweiTitleText.setText("Ͷ�ʱ�ģ�");
			viewHolder.useFanweiText.setText(info.getBorrow_name());
			viewHolder.validityText.setText("ʹ��ʱ�䣺" + info.getUse_time());
			viewHolder.useLimitText.setText("�������ڣ�" + info.getRepay_time().split(" ")[0]);
		}else{
			viewHolder.validityText.setText("��Ч�ڣ�" + info.getStart_time().split(" ")[0] + " ~ "
					+info.getEnd_time().split(" ")[0]);
			int needInvestMoneyI = 0;
			try{
				needInvestMoneyI = Integer.parseInt(info.getNeed_invest_money());
			}catch (Exception e){

			}
			if(needInvestMoneyI >= 10000){
				if(needInvestMoneyI % 10000 == 0){
					viewHolder.useLimitText.setText("ʹ�ù��򣺵���Ͷ�ʽ�����"
							+ needInvestMoneyI/10000 + "��Ԫ");
				}else{
					viewHolder.useLimitText.setText("ʹ�ù��򣺵���Ͷ�ʽ�����"
							+ needInvestMoneyI/10000d + "��Ԫ");
				}
			}else if(needInvestMoneyI <= 0){
				viewHolder.useLimitText.setText("ʹ�ù���һ һ");
			}else{
				viewHolder.useLimitText.setText("ʹ�ù��򣺵���Ͷ�ʽ�����"
						+ needInvestMoneyI + "Ԫ");
			}
		}
		return convertView;
	}

	/**
	 * �ڲ��࣬����Item������
	 * @author Mr.liu
	 *
	 */
	class ViewHolder {
		TextView eduText;// ���
		TextView useFanweiTitleText;//���÷�Χ��title
		TextView useFanweiText;//ʹ�÷�Χ
		TextView validityText;// ��Ч��
		TextView useLimitText;// ʹ������
		TextView remark;//��ע
		Button btn;
	}

	public interface OnHBListItemClickListener {
		void onItemClick(View v, int position);
	}
}
