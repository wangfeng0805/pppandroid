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
import com.ylfcf.ppp.entity.JiaxiquanInfo;
import com.ylfcf.ppp.util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * �ҵļ�Ϣȯ�б�
 * @author Mr.liu
 *
 */
public class MyJXQListAdapter extends ArrayAdapter<JiaxiquanInfo>{
	private static final int RESOURCE_ID = R.layout.myjxq_item;
	private List<JiaxiquanInfo> jiaxiquanList = null;
	private Context context;
	private LayoutInflater layoutInflater;
	private OnJXQItemClickListener clickListener;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Date sysNowTime ;
	
	public MyJXQListAdapter(Context context,OnJXQItemClickListener clickListener) {
		super(context, RESOURCE_ID);
		this.context = context;
		this.clickListener = clickListener;
		layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		jiaxiquanList = new ArrayList<JiaxiquanInfo>();
	}
	
	/**
	 * ���ⷽ������̬�ı�listview��item������ˢ��
	 * @param nowTime ϵͳ��ǰʱ��
	 */
	public void setItems(List<JiaxiquanInfo> list,String nowTime){
		try {
			this.sysNowTime = sdf1.parse(nowTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.jiaxiquanList.clear();
		if(jiaxiquanList != null){
			this.jiaxiquanList.addAll(list);
		}
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return jiaxiquanList.size();
	}
	
	@Override
	public JiaxiquanInfo getItem(int position) {
		return jiaxiquanList.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	int curPosition = 0;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		curPosition = position;
		final JiaxiquanInfo info = jiaxiquanList.get(position);
		ViewHolder viewHolder = null;
		String startTime = "";
		String endTime = "";
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(RESOURCE_ID, null);
			viewHolder.jxText = (TextView) convertView.findViewById(R.id.myjxq_item_jiaxi);
			viewHolder.syfwText = (TextView) convertView.findViewById(R.id.myjxq_item_syfw);
			viewHolder.yxqText = (TextView) convertView.findViewById(R.id.myjxq_item_validity);
			viewHolder.syyqText = (TextView) convertView.findViewById(R.id.myjxq_item_limit);
			viewHolder.remark = (TextView) convertView.findViewById(R.id.myjxq_item_remark);
			viewHolder.useBtn = (Button) convertView.findViewById(R.id.myjxq_list_item_btn);
			viewHolder.receiverTV = (TextView) convertView.findViewById(R.id.myjxq_item_receiver);
			viewHolder.fromWhereTV = (TextView) convertView.findViewById(R.id.myjxq_item_fromwhere);//
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		try {
			Date startDate = sdf.parse(info.getEffective_start_time());
			Date endDate = sdf.parse(info.getEffective_end_time());
			startTime = sdf.format(startDate);
			endTime = sdf.format(endDate);
		} catch (Exception e) {
		}
		
		viewHolder.jxText.setText(Util.formatRate(info.getMoney())+"%");
		if("".equals(info.getBorrow_type()) || info.getBorrow_type() == null || "null".equals(info.getBorrow_type())
				|| "NULL".equals(info.getBorrow_type())){
			viewHolder.syfwText.setText("һ һ");
		}else{
			if(info.getBorrow_type().contains("Ԫ��ͨ")&&info.getBorrow_type().contains("Ԫ����")&&
					info.getBorrow_type().contains("Ԫ����")&&info.getBorrow_type().contains("Ԫ����")){
				//����Ԫ��ͨ��Ԫ���ڡ�Ԫ���͡�Ԫ���������������ʱ�򣬸ĳ�Ԫ��ӯ
				List<String> typeList = new ArrayList<String>();
				String[] types = info.getBorrow_type().split(",");
				typeList.add("Ԫ��ӯ");
				for(int i=0;i<types.length;i++){
					if(!"Ԫ��ͨ".equals(types[i])&&!"Ԫ����".equals(types[i])&&!"Ԫ����".equals(types[i])&&!"Ԫ����".equals(types[i])){
						typeList.add(types[i]);
					}
				}
				StringBuffer sb = new StringBuffer();
				for(int i=0;i<typeList.size();i++){
					if(i == typeList.size() - 1){
						sb.append(typeList.get(i));
					}else{
						sb.append(typeList.get(i)).append(",");
					}
				}
				viewHolder.syfwText.setText(sb.toString());
			}else{
				viewHolder.syfwText.setText(info.getBorrow_type().replace("Ԫ��ͨ","Ԫ��ӯ32��").replace("Ԫ����","Ԫ��ӯ92��")
						.replace("Ԫ����","Ԫ��ӯ182��").replace("Ԫ����","Ԫ��ӯ365��"));
			}
		}
		
		viewHolder.yxqText.setText(startTime+" ~ "+endTime);
		double limitMoneyD = 0d;
		try{
			limitMoneyD = Double.parseDouble(info.getMin_invest_money());
		}catch (Exception e){
			e.printStackTrace();
		}
		if(limitMoneyD >= 10000){
			if(limitMoneyD % 10000 == 0){
				viewHolder.syyqText.setText("ʹ�ù���"+"����Ͷ�ʽ�����"+(int)(limitMoneyD/10000)+"��Ԫ");
			}else{
				viewHolder.syyqText.setText("ʹ�ù���"+"����Ͷ�ʽ�����"+(int)(limitMoneyD)/10000d+"��Ԫ");
			}
		}else{
			viewHolder.syyqText.setText("ʹ�ù���"+"����Ͷ�ʽ�����"+(int)limitMoneyD+"Ԫ");
		}

		if(info.getCoupon_from() == null || "".equals(info.getCoupon_from())){
			viewHolder.remark.setText("��ע�� �� ��");
		}else{
			viewHolder.remark.setText("��ע��"+info.getCoupon_from());
		}
		if("δʹ��".equals(info.getUse_status())){
			if("0".equals(info.getTransfer())){
				if(info.getTransfer_from_user_name() != null && !"".equals(info.getTransfer_from_user_name())){
					viewHolder.fromWhereTV.setVisibility(View.VISIBLE);
					viewHolder.receiverTV.setVisibility(View.GONE);
					viewHolder.fromWhereTV.setText("��Ϣȯ��Դ: "+ Util.hidRealName2(info.getTransfer_from_user_name())+"ת��");
				}else if(info.getSales_phone() != null && !"".equals(info.getSales_phone())){
					viewHolder.fromWhereTV.setVisibility(View.VISIBLE);
					viewHolder.receiverTV.setVisibility(View.GONE);
					viewHolder.fromWhereTV.setText("��Ϣȯ��Դ: "+ Util.hidPhoneNum(info.getSales_phone())+"ת��");
				}else{
					viewHolder.fromWhereTV.setVisibility(View.GONE);
					viewHolder.receiverTV.setVisibility(View.GONE);
				}
				//��ʾ�ǲ���ת�õļ�Ϣȯ
				viewHolder.useBtn.setText("����ʹ��");
				viewHolder.useBtn.setTextColor(context.getResources().getColor(R.color.common_topbar_bg_color));
				viewHolder.useBtn.setBackgroundResource(R.drawable.style_rect_fillet_blue_15dp);
				Date endDate = null;
				Date startDate = null;
				try {
					endDate = sdf1.parse(info.getEffective_end_time());
					startDate = sdf1.parse(info.getEffective_start_time());
					if(sysNowTime.compareTo(startDate) == 1 && endDate.compareTo(sysNowTime) == 1){
						//˵����Ϣȯ�Ѿ���Ч
						viewHolder.useBtn.setEnabled(true);
						viewHolder.useBtn.setTextColor(context.getResources().getColor(R.color.common_topbar_bg_color));
						viewHolder.useBtn.setBackgroundResource(R.drawable.style_rect_fillet_blue_15dp);
					}else{
						//��Ϣȯδ��Ч
						viewHolder.useBtn.setEnabled(false);
						viewHolder.useBtn.setTextColor(context.getResources().getColor(R.color.gray));
						viewHolder.useBtn.setBackgroundResource(R.drawable.style_rect_fillet_gray_15dp);
					}
					//��ʾ��Ϣȯδ����
					if(endDate.compareTo(sysNowTime) == -1){
						//�ѹ���
						viewHolder.useBtn.setVisibility(View.GONE);
					}else{
						viewHolder.useBtn.setVisibility(View.VISIBLE);
					}
				} catch (Exception e) {
				}
			}else if("1".equals(info.getTransfer())){
				//��ת�ü�Ϣȯ����δʹ��
                viewHolder.useBtn.setVisibility(View.VISIBLE);
                viewHolder.useBtn.setEnabled(true);
				viewHolder.useBtn.setText("ת�ü�Ϣȯ");
				viewHolder.useBtn.setTextColor(context.getResources().getColor(R.color.orange_text));
				viewHolder.useBtn.setBackgroundResource(R.drawable.style_rect_fillet_orange);
			}
			viewHolder.useBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					clickListener.onClick(info,curPosition);
				}
			});
		}else if("��ʹ��".equals(info.getUse_status())){
			if("0".equals(info.getTransfer())){
				//����ת�õļ�Ϣȯ
				viewHolder.useBtn.setVisibility(View.GONE);
				if(info.getTransfer_from_user_name() != null && !"".equals(info.getTransfer_from_user_name())){
					viewHolder.fromWhereTV.setVisibility(View.VISIBLE);
					viewHolder.receiverTV.setVisibility(View.GONE);
					viewHolder.fromWhereTV.setText("��Ϣȯ��Դ: "+Util.hidRealName2(info.getTransfer_from_user_name())+"ת��");
				}
			}else{
				//��ת�õļ�Ϣȯ
				if(info.getTransfer_get_user_phone() != null && !"".equals(info.getTransfer_get_user_phone())){
					viewHolder.fromWhereTV.setVisibility(View.GONE);
					viewHolder.receiverTV.setVisibility(View.VISIBLE);
					viewHolder.receiverTV.setText("������: "+Util.hidPhoneNum(info.getTransfer_get_user_phone()));
				}
				viewHolder.useBtn.setVisibility(View.VISIBLE);
				viewHolder.useBtn.setEnabled(false);
				viewHolder.useBtn.setText("��ת��");
				viewHolder.useBtn.setTextColor(context.getResources().getColor(R.color.gray1));
				viewHolder.useBtn.setBackgroundResource(R.drawable.style_rect_fillet_gray1);
			}
		}else if("�ѹ���".equals(info.getUse_status())){
			if("0".equals(info.getTransfer())){
				//����ת�õļ�Ϣȯ
				viewHolder.useBtn.setVisibility(View.GONE);
				if(info.getTransfer_from_user_name() != null && !"".equals(info.getTransfer_from_user_name())){
					viewHolder.fromWhereTV.setVisibility(View.VISIBLE);
					viewHolder.receiverTV.setVisibility(View.GONE);
					viewHolder.fromWhereTV.setText("��Ϣȯ��Դ: "+Util.hidRealName2(info.getTransfer_from_user_name())+"ת��");
				}
			}else{
				viewHolder.useBtn.setVisibility(View.VISIBLE);
				viewHolder.useBtn.setText("ת�ü�Ϣȯ");
				viewHolder.useBtn.setEnabled(false);
				viewHolder.useBtn.setTextColor(context.getResources().getColor(R.color.gray1));
				viewHolder.useBtn.setBackgroundResource(R.drawable.style_rect_fillet_gray1);
			}
		}
		return convertView;
	}

	/**
	 * �ڲ��࣬����item��Ԫ��
	 * @author Mr.liu
	 *
	 */
	class ViewHolder{
		TextView jxText;//��Ϣ
		TextView syfwText;//ʹ�÷�Χ
		TextView yxqText;//��Ч��
		TextView syyqText;//ʹ��Ҫ��
		TextView remark;//��ע
		Button useBtn;//����ʹ��
		TextView receiverTV;//��Ϣȯ������
		TextView fromWhereTV;//��Ϣȯ��Դ
	}

	public interface OnJXQItemClickListener{
		void onClick(JiaxiquanInfo jxqInfo,int position);
	}
}
