package com.ylfcf.ppp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.FriendInfo;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.util.YLFLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * �ҵĺ���
 * Created by Administrator on 2017/7/6.
 */

public class MyFriendAdapter extends ArrayAdapter<FriendInfo> {
    private static final int RESOURCE_ID = R.layout.myfriends_listview_item;
    private final LayoutInflater mInflater;

    private List<FriendInfo> friendList;
    private Context context;
    private OnFriendItemClickListener mOnFriendItemClickListener;
    private boolean isLcs = false;

    public MyFriendAdapter(Context context,OnFriendItemClickListener mOnFriendItemClickListener) {
        super(context, RESOURCE_ID);
        this.context = context;
        this.mOnFriendItemClickListener = mOnFriendItemClickListener;
        mInflater	= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        friendList	= new ArrayList<FriendInfo>();
    }

    /**
     * ���ⷽ������̬�ı�listview��item������ˢ��
     * @param list
     */
    public void setItems(List<FriendInfo> list,boolean isLcs){
        this.isLcs = isLcs;
        this.friendList.clear();
        if(list != null) {
            this.friendList.addAll(list);
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return friendList.size();
    }

    @Override
    public FriendInfo getItem(int position) {
        return friendList.get(position);
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
        final FriendInfo info = friendList.get(position);
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView	= mInflater.inflate(RESOURCE_ID, null);
            viewHolder.phoneTV = (TextView) convertView.findViewById(R.id.myfriends_activity_phonetv);
            viewHolder.nameTV = (TextView) convertView.findViewById(R.id.myfriends_activity_nametv);
            viewHolder.statusTV = (TextView) convertView.findViewById(R.id.myfriends_activity_statustv);
            viewHolder.registerTimeTV = (TextView) convertView.findViewById(R.id.myfriends_listview_item_registertime);
            viewHolder.btn = (Button) convertView.findViewById(R.id.myfriends_listview_item_btn);
            viewHolder.btnTableRow = (TableRow) convertView.findViewById(R.id.myfriends_activity_item_btntablerow);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(isLcs){
            viewHolder.btnTableRow.setVisibility(View.VISIBLE);
        }else{
            viewHolder.btnTableRow.setVisibility(View.GONE);
        }
        viewHolder.phoneTV.setText(info.getPhone());
        if(info.getReal_name() == null || "".equals(info.getReal_name())){
            viewHolder.nameTV.setText("һ һ");
        }else{
            viewHolder.nameTV.setText(Util.hidRealName2(info.getReal_name()));
        }
        if("0".equals(info.getStatus())){
            //ע��
            viewHolder.statusTV.setText("ע��");
        }else if("1".equals(info.getStatus())){
            //ʵ��
            viewHolder.statusTV.setText("ʵ��");
        }else if("2".equals(info.getStatus())){
            //Ͷ��
            viewHolder.statusTV.setText("Ͷ��");
        }
        if(info.getReg_time() != null && !"".equals(info.getReg_time())){
            viewHolder.registerTimeTV.setText(info.getReg_time().split(" ")[0]);
        }
        viewHolder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnFriendItemClickListener.onItemClick(position,info);
            }
        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        if(position == friendList.size() - 1){
            params.bottomMargin = 20;
            convertView.setLayoutParams(params);
        }else{
            params.bottomMargin = 0;
            convertView.setLayoutParams(params);
        }
        YLFLogger.d("position:--------------------"+position);
        return convertView;
    }

    /**
     * �ڲ��࣬����Item��Ԫ��
     * @author Mr.liu
     *
     */
    class ViewHolder{
        TextView phoneTV;
        TextView nameTV;
        TextView statusTV;
        TextView registerTimeTV;//ע��ʱ��
        Button btn;//���ͼ�Ϣȯ
        TableRow btnTableRow;
    }

    public interface OnFriendItemClickListener{
        void onItemClick(int position,FriendInfo friendInfo);
    }
}
