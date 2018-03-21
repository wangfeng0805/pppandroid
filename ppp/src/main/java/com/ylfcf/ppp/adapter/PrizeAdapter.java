package com.ylfcf.ppp.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.PrizeInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * �ҵ�����
 * @author Administrator
 * 
 */
public class PrizeAdapter extends ArrayAdapter<PrizeInfo> {
	private static final int RESOURCE_ID = R.layout.prize_listview_item;
	private final LayoutInflater mInflater;
	
	private List<PrizeInfo> prizeList;
	private Context context;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private OnItemMRCJListener mOnItemMRCJListener;

	public PrizeAdapter(Context context,OnItemMRCJListener mOnItemMRCJListener) {
		super(context, RESOURCE_ID);
		this.context = context;
		this.mOnItemMRCJListener = mOnItemMRCJListener;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		prizeList = new ArrayList<PrizeInfo>();
	}

	/**
	 * ���ⷽ������̬�ı�listview��item������ˢ��
	 * @param prizeList
	 */
	public void setItems(List<PrizeInfo> prizeList) {
		this.prizeList.clear();
		if (prizeList != null) {
			this.prizeList.addAll(prizeList);
		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return prizeList.size();
	}

	@Override
	public PrizeInfo getItem(int position) {
		return prizeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * ���þ���һ����ĵ���ʾ����
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		final PrizeInfo info = prizeList.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(RESOURCE_ID, null);
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.prize_listview_item_title);
			viewHolder.ticket = (TextView) convertView
					.findViewById(R.id.prize_listview_item_ticket);
			viewHolder.endTime = (TextView) convertView
					.findViewById(R.id.prize_listview_item_endtime);
			viewHolder.moneyText = (TextView) convertView
					.findViewById(R.id.prize_listview_item_money);
			viewHolder.unitText = (TextView) convertView
					.findViewById(R.id.prize_listview_item_unit);
			viewHolder.remark = (TextView) convertView.
					findViewById(R.id.prize_listview_item_remark);
			viewHolder.catDetails = (Button)convertView.
					findViewById(R.id.prize_listview_item_catdetails_btn);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if(info.getOperating_remark() != null && !"".equals(info.getOperating_remark())){
			viewHolder.remark.setText("��ע��"+info.getOperating_remark());	
		}else{
			viewHolder.remark.setText("��ע��--");
		}
		if(info.getOperating_remark().contains("ת��") && !info.getOperating_remark().contains("����")){
			//����ת��
			viewHolder.ticket.setVisibility(View.GONE);
			viewHolder.moneyText.setVisibility(View.GONE);
			viewHolder.catDetails.setVisibility(View.GONE);
			viewHolder.endTime.setVisibility(View.VISIBLE);
			if(info.getPrize().contains("ת�̻���")){
				viewHolder.title.setText(info.getOperating_remark()+info.getGet_nums()+"��");
				viewHolder.endTime.setText("��ȡʱ�䣺"+info.getSend_time());
				viewHolder.unitText.setVisibility(View.VISIBLE);
				viewHolder.unitText.setText("������Ч");
			}else if(info.getPrize().contains("����")){
				viewHolder.title.setText(info.getPrize());
				viewHolder.endTime.setText("��Ʒ�ڻ����7����������ͳ�Ʒ���");
				viewHolder.unitText.setVisibility(View.GONE);
			}
		}else if(info.getOperating_remark().contains("ת��") && info.getOperating_remark().contains("����")){
			//2016�������ת�̻
			viewHolder.ticket.setVisibility(View.GONE);
			viewHolder.moneyText.setVisibility(View.GONE);
			viewHolder.unitText.setVisibility(View.GONE);
			viewHolder.catDetails.setVisibility(View.GONE);
			viewHolder.endTime.setVisibility(View.VISIBLE);
			viewHolder.title.setText(info.getPrize());
			viewHolder.endTime.setText("��ȡʱ�䣺"+info.getAdd_time());
		}else if(info.getOperating_remark().contains("����ж�����") && info.getOperating_remark().contains("����")){
			//2016������ж������
			viewHolder.ticket.setVisibility(View.GONE);
			viewHolder.moneyText.setVisibility(View.GONE);
			viewHolder.unitText.setVisibility(View.GONE);
			viewHolder.endTime.setVisibility(View.GONE);
			viewHolder.catDetails.setVisibility(View.GONE);
			viewHolder.title.setText(info.getPrize());
			viewHolder.remark.setText(info.getOperating_remark()+"(�˻����Ŀɲ鿴)");
		}else if("HYFL_01".equals(info.getActive_title())){
			//��Ա�����ƻ��
			viewHolder.ticket.setVisibility(View.VISIBLE);
			viewHolder.ticket.setText("�һ��룺"+info.getPrize_code());
			viewHolder.title.setText(info.getName());
			viewHolder.endTime.setVisibility(View.GONE);
			viewHolder.remark.setVisibility(View.VISIBLE);
			viewHolder.remark.setText("��ע��"+info.getOperating_remark());
			viewHolder.moneyText.setVisibility(View.GONE);
			viewHolder.unitText.setVisibility(View.GONE);
			viewHolder.catDetails.setVisibility(View.GONE);
		}else if("HYFL_02".equals(info.getActive_title())){
			viewHolder.catDetails.setVisibility(View.GONE);
			//��Ա����02��
			if(info.getPrize_code() != null && !"".equals(info.getPrize_code())){
				viewHolder.ticket.setVisibility(View.VISIBLE);
				viewHolder.ticket.setText("�һ��룺"+info.getPrize_code());
				viewHolder.title.setText(info.getName());
				viewHolder.endTime.setVisibility(View.GONE);
				viewHolder.remark.setVisibility(View.VISIBLE);
				viewHolder.remark.setText("��ע��"+info.getOperating_remark());
				viewHolder.moneyText.setVisibility(View.GONE);
				viewHolder.unitText.setVisibility(View.GONE);
			}else{
				viewHolder.ticket.setVisibility(View.VISIBLE);
				viewHolder.ticket.setText("�һ��룺ȯ��������Ԫ����ƽ̨ע����ֻ�����");
				viewHolder.title.setText(info.getName());
				viewHolder.endTime.setVisibility(View.GONE);
				viewHolder.remark.setVisibility(View.VISIBLE);
				viewHolder.remark.setText("��ע��"+info.getOperating_remark());
				viewHolder.moneyText.setVisibility(View.GONE);
				viewHolder.unitText.setVisibility(View.GONE);
			}
			SpannableStringBuilder builder = new SpannableStringBuilder(viewHolder.ticket.getText().toString());  
			ForegroundColorSpan redSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.orange_text)); 
			builder.setSpan(redSpan, 4, viewHolder.ticket.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
			viewHolder.ticket.setText(builder);  
		}else if("YXHB_01".equals(info.getActive_title())){
			//2017���´�����
			viewHolder.moneyText.setVisibility(View.GONE);
			viewHolder.unitText.setVisibility(View.GONE);
			viewHolder.ticket.setVisibility(View.GONE);
			viewHolder.catDetails.setVisibility(View.GONE);
			viewHolder.endTime.setVisibility(View.VISIBLE);
			viewHolder.remark.setVisibility(View.VISIBLE);
			viewHolder.title.setText(info.getPrize());
			if(!info.getOperating_remark().isEmpty()){
				viewHolder.remark.setText("��ע��"+info.getOperating_remark());
			}else{
				viewHolder.remark.setText("��ע��һ һ");
			}
			if(info.getPrize().contains("Ԫ���") || info.getPrize().contains("�ֽ�") || info.getPrize().contains("��ȡ����")){
				viewHolder.endTime.setText("��ȡʱ�䣺"+info.getAdd_time());
			}else if(info.getPrize().contains("���") || info.getPrize().contains("��Ϣȯ")){
				//��Ч��Ϊadd_time�����ϼ�32�졣
				try {
					Date addDate = sdf.parse(info.getAdd_time());
					long endTimeL = addDate.getTime() + 31*24*3600*1000L;
					String endTimeStr = sdf.format(new Date(endTimeL));
					viewHolder.endTime.setText("��Ч������" + endTimeStr.split(" ")[0]+" 23:59:59");
				} catch (ParseException e) {
					e.printStackTrace();
					viewHolder.endTime.setText("��Ч������" + info.getEnd_time());//���ʱ����ʵ�Ǵ���ģ�ֻ���ڽ������������ʱ��Ĭ����ʾ��
				}
			}else if(info.getPrize().contains("������")){
				viewHolder.endTime.setText(info.getRemark());
			}
		}else if("MRCJ".equals(info.getActive_title()) || "MZLY".equals(info.getActive_title())){
			//΢�Żÿ�ճ齱
			viewHolder.title.setText(info.getName());
			viewHolder.remark.setVisibility(View.VISIBLE);
			viewHolder.moneyText.setVisibility(View.GONE);
			viewHolder.unitText.setVisibility(View.GONE);
			if("draw".equals(info.getSource()) || "MZLY".equals(info.getActive_title())){
				if(info.getPrize_code() == null || "".equals(info.getPrize_code())){
					viewHolder.remark.setVisibility(View.GONE);
				}else{
					viewHolder.remark.setVisibility(View.VISIBLE);
					viewHolder.remark.setText("�һ��룺"+info.getPrize_code());
				}
				viewHolder.endTime.setVisibility(View.GONE);
				viewHolder.catDetails.setVisibility(View.VISIBLE);
				viewHolder.ticket.setVisibility(View.VISIBLE);
				viewHolder.ticket.setText(info.getOperating_remark());
			}else{
				//��ȡ�齱����
				viewHolder.endTime.setVisibility(View.VISIBLE);
				viewHolder.catDetails.setVisibility(View.GONE);
				viewHolder.ticket.setVisibility(View.GONE);
				viewHolder.remark.setText("��ע��"+info.getSend_time().split(" ")[0]+"������Ч");
				viewHolder.endTime.setText(info.getOperating_remark());
			}
			viewHolder.catDetails.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mOnItemMRCJListener.onClick(position,info);
				}
			});
		}else if("LYNF_01".equals(info.getActive_title())){
			//���·ݻ �����̷ۻ
			viewHolder.endTime.setVisibility(View.GONE);
			viewHolder.title.setText(info.getName());
			viewHolder.ticket.setVisibility(View.VISIBLE);
			viewHolder.ticket.setText(info.getRemark());
			viewHolder.remark.setVisibility(View.VISIBLE);
			viewHolder.remark.setText("��ע��"+info.getOperating_remark());

		}else if("MONDAY_ROB_CASH".equals(info.getActive_title())){
			//���·ݻ ÿ��һ���ֽ�
			viewHolder.title.setText("�ֽ�"+info.getName()+"Ԫ");
			viewHolder.endTime.setVisibility(View.VISIBLE);
			viewHolder.endTime.setText("��ȡʱ�䣺"+info.getAdd_time());
			viewHolder.remark.setVisibility(View.VISIBLE);
			viewHolder.remark.setText("��ע��"+info.getOperating_remark());
		}else if("QYHD2017".equals(info.getActive_title())){
			//2017��7�·ݻ
			viewHolder.title.setText(info.getName());
			if(info.getPrize().contains("�齱����") || info.getPrize().contains("����")){
				viewHolder.endTime.setVisibility(View.GONE);
				viewHolder.remark.setText("��ע��"+info.getOperating_remark()+"\n						"+info.getRemark());
			}else if(info.getPrize().contains("��Ϣȯ")){
				viewHolder.remark.setText("��ע��"+info.getOperating_remark());
				viewHolder.endTime.setVisibility(View.VISIBLE);
				viewHolder.endTime.setText("��ȡʱ�䣺" + info.getSend_time());
			}
		}else if("GQCJ201710".equals(info.getActive_title())){
			//2017��10�»
			viewHolder.endTime.setVisibility(View.VISIBLE);
			viewHolder.catDetails.setVisibility(View.GONE);
			String name = info.getName();
			viewHolder.title.setText(name);
			if (name != null && name.contains("Ԫ���") || name.contains("���") || name.contains("��Ϣȯ")) {
				viewHolder.endTime.setText("��ȡʱ�䣺" + info.getSend_time());
			} else if (name.contains("�齱����")){
				viewHolder.endTime.setText("��Ч�ڽ�ֹ�������" );
			}else if(name != null && name.contains("ʳ����")){
				viewHolder.endTime.setText(info.getRemark());
			}

		}else if("WXCJ201710".equals(info.getActive_title()) || "WXCJ201711".equals(info.getActive_title())){
			//2017΢�ų齱�
			viewHolder.endTime.setVisibility(View.VISIBLE);
			viewHolder.catDetails.setVisibility(View.GONE);
			String name = info.getName();
			viewHolder.title.setText(name);
			if (name != null && name.contains("���")) {
				viewHolder.endTime.setText("��ȡʱ�䣺" + info.getSend_time());
			} else if (name != null && name.contains("�齱����")){
				viewHolder.endTime.setText(info.getOperating_remark());
				viewHolder.remark.setText("��ע����Ч�ڽ�ֹ�������");
			}else if(name != null && name.contains("�ֽ�")){
				viewHolder.endTime.setText(info.getOperating_remark());
				viewHolder.remark.setText("��ע��"+info.getRemark());
			}else if(name != null && name.contains("����")){
				viewHolder.title.setText(info.getRewardInfoEntity().getMoney()+"M�ֻ�����");
				viewHolder.endTime.setText(info.getOperating_remark());
				viewHolder.remark.setText("��ע��"+info.getRemark());
			}else{
				viewHolder.endTime.setText(info.getOperating_remark());
				viewHolder.remark.setText("��ע��"+info.getRemark());
			}
		}else if("WXCJ20171227".equals(info.getActive_title()) || "WINTER20180111".equals(info.getActive_title()) || "XCJNH20180312".equals(info.getActive_title())){
			//2017������΢�ų齱�/��������ů΢�Ż
			viewHolder.endTime.setVisibility(View.VISIBLE);
			viewHolder.catDetails.setVisibility(View.GONE);
			String name = info.getName();
			viewHolder.title.setText(name);
			viewHolder.endTime.setText(info.getOperating_remark());
			viewHolder.remark.setText("��ע��"+info.getRemark());
		}else{
			viewHolder.endTime.setVisibility(View.VISIBLE);
			viewHolder.catDetails.setVisibility(View.GONE);
			viewHolder.ticket.setVisibility(View.VISIBLE);
			viewHolder.moneyText.setVisibility(View.VISIBLE);
			if ("�Ѷһ�".equals(info.getStatus())
					&& info.getRewardInfoEntity() != null) {
				viewHolder.ticket.setVisibility(View.VISIBLE);
				if (info.getRewardInfoEntity().getCode() == null
						|| "null".equals(info.getRewardInfoEntity().getCode())
						|| "NULL".equals(info.getRewardInfoEntity().getCode())) {
					viewHolder.ticket.setText("ȯ�룺");
				} else {
					viewHolder.ticket.setText("ȯ�룺"
							+ info.getRewardInfoEntity().getCode());
				}

			} else {
				viewHolder.ticket.setVisibility(View.GONE);
			}
			String name = info.getName();
			viewHolder.title.setText(name);
			if (name != null && name.contains("Ԫ���") || name.contains("�ֽ�")) {
				viewHolder.endTime.setText("��ȡʱ�䣺" + info.getSend_time());
			} else {
				viewHolder.endTime.setText("��Ч������" + info.getEnd_time());
			}

			if (info != null && info.getRewardInfoEntity() != null) {
				viewHolder.moneyText.setText(info.getRewardInfoEntity().getMoney());
			}

			if (name != null && name.contains("����")) {
				if (info != null && info.getRewardInfoEntity() != null) {
					viewHolder.unitText.setVisibility(View.VISIBLE);
					viewHolder.unitText.setText("M");
				}
			} else {
				if (info != null && info.getRewardInfoEntity() != null) {
					viewHolder.unitText.setVisibility(View.VISIBLE);
					viewHolder.unitText.setText("Ԫ");
				}
			}
		}
		
		return convertView;
	}

	/**
	 * �ڲ��࣬����Item��Ԫ��
	 * @author Mr.liu
	 *
	 */
	class ViewHolder {
		TextView title;
		TextView ticket;// ȯ
		TextView endTime;// ��Ч��
		TextView moneyText;
		TextView unitText;
		TextView remark;
		Button catDetails;//�鿴����
	}

	/**
	 * ÿ�ճ齱�鿴���鰴ť
	 */
	public interface OnItemMRCJListener{
		void onClick(int position,PrizeInfo prize);
	}
}
