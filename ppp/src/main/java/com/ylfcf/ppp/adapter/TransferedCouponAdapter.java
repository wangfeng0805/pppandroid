package com.ylfcf.ppp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.JiaxiquanInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ת�ü�Ϣȯ
 * Created by Administrator on 2017/7/7.
 */

public class TransferedCouponAdapter extends ArrayAdapter<JiaxiquanInfo>{
    private static final int RESOURCE_ID = R.layout.transfer_coupon_item;
    private List<JiaxiquanInfo> jiaxiquanList = null;
    private Context context;
    private LayoutInflater layoutInflater;
    private OnJXQItemClickListener clickListener;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public TransferedCouponAdapter(Context context,OnJXQItemClickListener clickListener) {
        super(context, RESOURCE_ID);
        this.context = context;
        this.clickListener = clickListener;
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        jiaxiquanList = new ArrayList<JiaxiquanInfo>();
    }

    /**
     * ���ⷽ������̬�ı�listview��item������ˢ��
     */
    public void setItems(List<JiaxiquanInfo> list){
        this.jiaxiquanList.clear();
        if(jiaxiquanList != null){
            this.jiaxiquanList.addAll(list);
        }
//		Collections.reverse(jiaxiquanList);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        curPosition = position;
        final JiaxiquanInfo info = jiaxiquanList.get(position);
        ViewHolder viewHolder = null;
        String startTime = "";
        String endTime = "";
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(RESOURCE_ID, null);
            viewHolder.jxText = (TextView) convertView.findViewById(R.id.transfer_coupon_item_interestadd);
            viewHolder.syfwText = (TextView) convertView.findViewById(R.id.transfer_coupon_item_uselimit);
            viewHolder.yxqText = (TextView) convertView.findViewById(R.id.transfer_coupon_item_timelimit);
            viewHolder.syyqText = (TextView) convertView.findViewById(R.id.transfer_coupon_item_moneylimit);
            viewHolder.remark = (TextView) convertView.findViewById(R.id.transfer_coupon_item_remark);
            viewHolder.cb = (CheckBox) convertView.findViewById(R.id.transfer_coupon_item_cb);
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

        viewHolder.jxText.setText(info.getMoney()+"%");
        if("".equals(info.getBorrow_type()) || info.getBorrow_type() == null || "null".equals(info.getBorrow_type())
                || "NULL".equals(info.getBorrow_type())){
            viewHolder.syfwText.setText("һ һ");
        }else{
            viewHolder.syfwText.setText(info.getBorrow_type());
        }

        viewHolder.yxqText.setText(startTime+"~"+endTime);
        double limitMoneyD = 0d;
        try{
            limitMoneyD = Double.parseDouble(info.getMin_invest_money());
        }catch (Exception e){
            e.printStackTrace();
        }
        if(limitMoneyD >= 10000){
            viewHolder.syyqText.setText("����Ͷ�ʽ�����"+limitMoneyD/10000+"��Ԫ");
        }else{
            viewHolder.syyqText.setText("����Ͷ�ʽ�����"+info.getMin_invest_money()+"Ԫ");
        }

        if(info.getCoupon_from() == null || "".equals(info.getCoupon_from())){
            viewHolder.remark.setText("�� ��");
        }else{
            viewHolder.remark.setText(info.getCoupon_from());
        }
        viewHolder.cb.setChecked(info.isChecked());
        viewHolder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(info,position);
            }
        });
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
//                RelativeLayout.LayoutParams.MATCH_PARENT);
//        params.setMargins(0,0,0,100);
//        if(position == jiaxiquanList.size() - 1){
//            convertView.setLayoutParams(params);
//        }
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
        CheckBox cb;
    }

    public interface OnJXQItemClickListener{
        void onClick(JiaxiquanInfo jxqInfo,int position);
    }
}
