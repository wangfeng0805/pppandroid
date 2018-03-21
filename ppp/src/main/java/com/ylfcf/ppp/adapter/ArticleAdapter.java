package com.ylfcf.ppp.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.ArticleInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * ���桢���š�
 * @author Administrator
 *
 */
public class ArticleAdapter extends ArrayAdapter<ArticleInfo>{
	private static final int RESOURCE_ID = R.layout.article_item;
	private List<ArticleInfo> articleList = null;
	private Context context;
	private LayoutInflater layoutInflater;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	public ArticleAdapter(Context context) {
		super(context, RESOURCE_ID);
		this.context = context;
		layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		articleList = new ArrayList<ArticleInfo>();
	}
	
	/**
	 * ���ⷽ������̬�ı�listview��item������ˢ��
	 * @param articleList
	 */
	public void setItems(List<ArticleInfo> articleList){
		this.articleList.clear();
		if(articleList != null){
			this.articleList.addAll(articleList);
		}
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return articleList.size();
	}
	
	@Override
	public ArticleInfo getItem(int position) {
		return articleList.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ArticleInfo info = articleList.get(position);
		ViewHolder viewHolder = null;
		String addTime = "";
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(RESOURCE_ID, null);
			viewHolder.title = (TextView) convertView.findViewById(R.id.article_item_title);
			viewHolder.time = (TextView) convertView.findViewById(R.id.article_item_time);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		try {
			Date date = sdf.parse(info.getAdd_time());
			addTime = sdf.format(date);
		} catch (Exception e) {
		}
		
		viewHolder.title.setText(info.getTitle());
		if(addTime != null && !"".equals(addTime)){
			viewHolder.time.setText(addTime);
		}else{
			viewHolder.time.setText(info.getAdd_time());
		}
		return convertView;
	}

	/**
	 * �ڲ��࣬����item��Ԫ��
	 * @author Mr.liu
	 *
	 */
	class ViewHolder{
		TextView title;
		TextView time;
	}
}
