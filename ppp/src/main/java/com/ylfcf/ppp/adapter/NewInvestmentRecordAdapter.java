package com.ylfcf.ppp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.LoanRecordInfo;
import com.ylfcf.ppp.entity.NewInvestmentRecordInfo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Ͷ�ʼ�¼�б�adapter
 * @author yjx
 *
 */
public class NewInvestmentRecordAdapter extends MyBaseAdapter<NewInvestmentRecordInfo.ListBean> {

//	private LayoutInflater layoutInflater = null;

	public NewInvestmentRecordAdapter(Context context, List<NewInvestmentRecordInfo.ListBean> list){
		super(context, list);
//		layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}
	
	/**
	 * ���ⷽ������̬�ı�listview��item������ˢ��
	 * @param list
	 */
	public void setItems(List<NewInvestmentRecordInfo.ListBean> list){
		this.mDatas.clear();
		if(list != null){
			mDatas.addAll(list);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public NewInvestmentRecordInfo.ListBean getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		String status = mDatas.get(position).getStatus();
		if(status.equals("2")) {
			//�Ｏ��
			return 0;
		}else if(status.equals("3")) {
			//�ؿ���
			return 1;
		}else if(status.equals("4")) {
			//����
			return 2;
		}else if(status.equals("5")) {
			//�����
			return 3;
		}else if(status.equals("7")||status.equals("8")) {
			//�Ｏ���
			return 4;
		}
		return super.getItemViewType(position);
	}

	@Override
	public int getViewTypeCount() {
		return 5;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		int type = getItemViewType(position);
        NewInvestmentRecordInfo.ListBean listBean = mDatas.get(position);
        switch (type) {
			case 1://�ؿ��� �����
			case 3:
				if(convertView == null){
					viewHolder = new NewInvestmentRecordAdapter.ViewHolder();
					convertView = LayoutInflater.from(mContext).inflate(R.layout.item_record_investment_listview, null);
					viewHolder.tv_status = (TextView)convertView.findViewById(R.id.tv_status);
					viewHolder.tv_loan_amount = (TextView)convertView.findViewById(R.id.tv_loan_amount);
					viewHolder.tv_interest_rate = (TextView)convertView.findViewById(R.id.tv_interest_rate);
					viewHolder.tv_loan_date = (TextView)convertView.findViewById(R.id.tv_loan_date);
					viewHolder.tv_interest_date = (TextView)convertView.findViewById(R.id.tv_interest_date);
					viewHolder.tv_monthly_return = (TextView)convertView.findViewById(R.id.tv_monthly_return);
					viewHolder.tv_loan_term = (TextView)convertView.findViewById(R.id.tv_loan_term);
					convertView.setTag(viewHolder);
				}else{
					viewHolder = (NewInvestmentRecordAdapter.ViewHolder) convertView.getTag();
				}
                if(type == 1) {
                    viewHolder.tv_status.setText("�ؿ���");
                    viewHolder.tv_status.setBackgroundResource(R.drawable.style_rect_fillet_filling_orange2);
                }else {
                    viewHolder.tv_status.setText("�����");
                    viewHolder.tv_status.setBackgroundResource(R.drawable.style_rect_fillet_filling_gray2);
                }

				DecimalFormat df = new DecimalFormat("0.0");
				String money = df.format(Double.parseDouble(listBean.getMoney()));
				if(money.endsWith("0")) {
					money = listBean.getMoney().split("\\.")[0];
				}
				viewHolder.tv_loan_amount.setText("��" + money + "Ԫ");
                viewHolder.tv_interest_rate.setText(listBean.getInterest_rate() + "%");

				String invest_time = listBean.getInvest_time();
				String[] split = invest_time.split(" ");
				viewHolder.tv_loan_date.setText(split[0]);

				String interest_start_time = listBean.getInterest_start_time();
				String[] split1 = interest_start_time.split(" ");
				viewHolder.tv_interest_date.setText(split1[0]);

                viewHolder.tv_monthly_return.setText(listBean.getReimbursement_amount());
                viewHolder.tv_loan_term.setText(listBean.getInterest_days() + "����");
				break;

			case 0://�Ｏ��
			case 4://�Ｏ���
				if(convertView == null){
					viewHolder = new NewInvestmentRecordAdapter.ViewHolder();
					convertView = LayoutInflater.from(mContext).inflate(R.layout.item_record_investment_listview3, null);
                    viewHolder.tv_status = (TextView)convertView.findViewById(R.id.tv_status);
                    viewHolder.tv_loan_amount = (TextView)convertView.findViewById(R.id.tv_loan_amount);
                    viewHolder.tv_interest_rate = (TextView)convertView.findViewById(R.id.tv_interest_rate);
                    viewHolder.tv_loan_date = (TextView)convertView.findViewById(R.id.tv_loan_date);
                    viewHolder.tv_end_date = (TextView)convertView.findViewById(R.id.tv_end_date);
                    viewHolder.tv_loan_term = (TextView)convertView.findViewById(R.id.tv_loan_term);
					convertView.setTag(viewHolder);
				}else{
					viewHolder = (NewInvestmentRecordAdapter.ViewHolder) convertView.getTag();
				}
                if(type == 0) {
                    viewHolder.tv_status.setText("�Ｏ��");
                    viewHolder.tv_status.setBackgroundResource(R.drawable.style_rect_fillet_filling_orange2);
                }else {
                    viewHolder.tv_status.setText("�Ｏ���");
                    viewHolder.tv_status.setBackgroundResource(R.drawable.style_rect_fillet_filling_orange2);
                }
				DecimalFormat df1 = new DecimalFormat("0.0");
				String money1 = df1.format(Double.parseDouble(listBean.getMoney()));
				if(money1.endsWith("0")) {
					money1 = listBean.getMoney().split("\\.")[0];
				}
				viewHolder.tv_loan_amount.setText("��" + money1 + "Ԫ");
                viewHolder.tv_interest_rate.setText(listBean.getInterest_rate() + "%");
				String invest_time2 = listBean.getInvest_time();
				String[] split2 = invest_time2.split(" ");
				viewHolder.tv_loan_date.setText(split2[0]);

				String raising_period = listBean.getRaising_period();
				String[] split3 = raising_period.split(" ");
				viewHolder.tv_end_date.setText(split3[0]);

                viewHolder.tv_loan_term.setText(listBean.getInterest_days() + "����");

				break;
			case 2://����
				if(convertView == null){
					viewHolder = new NewInvestmentRecordAdapter.ViewHolder();
					convertView = LayoutInflater.from(mContext).inflate(R.layout.item_record_investment_listview2, null);
                    viewHolder.tv_status = (TextView)convertView.findViewById(R.id.tv_status);
                    viewHolder.tv_loan_amount = (TextView)convertView.findViewById(R.id.tv_loan_amount);
                    viewHolder.tv_interest_rate = (TextView)convertView.findViewById(R.id.tv_interest_rate);
                    viewHolder.tv_refund_reason = (TextView)convertView.findViewById(R.id.tv_refund_reason);
					convertView.setTag(viewHolder);
				}else{
					viewHolder = (NewInvestmentRecordAdapter.ViewHolder) convertView.getTag();
				}
                viewHolder.tv_status.setText("���˿�");
                viewHolder.tv_status.setBackgroundResource(R.drawable.style_rect_fillet_filling_orange2);
				DecimalFormat df2 = new DecimalFormat("0.0");
				String money2 = df2.format(Double.parseDouble(listBean.getMoney()));
				if(money2.endsWith("0")) {
					money2 = listBean.getMoney().split("\\.")[0];
				}
				viewHolder.tv_loan_amount.setText("��" + money2 + "Ԫ");

				String invest_time4 = listBean.getInvest_time();
				String[] split4 = invest_time4.split(" ");
				viewHolder.tv_loan_date.setText(split4[0]);

				viewHolder.tv_refund_reason.setText("�����˿");
                break;
		}

		return convertView;
	}

	/**
	 * �ڲ��࣬����Item��Ԫ��
	 */
	class ViewHolder{
		TextView tv_status;//���״̬
		TextView tv_loan_amount;//������ 2 3
		TextView tv_interest_rate;//������ 2 3
		TextView tv_loan_date;//�������� 3
		TextView tv_interest_date;//��Ϣ��
		TextView tv_monthly_return;//ÿ�»ؿ�
		TextView tv_loan_term;//��������  3

		TextView tv_refund_reason;//�˿�ԭ�� 2

		TextView tv_end_date;//�Ｏ��ֹ�� 3
	}

}
