package com.ylfcf.ppp.adapter;

import java.text.RuleBasedCollator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.GiftInfo;
import com.ylfcf.ppp.util.ImageLoaderManager;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.YLFLogger;
/**
 * �����ƻ�ҳ����Ʒ�б�
 * @author Mr.liu
 *
 */
public class GiftAdapter extends ArrayAdapter<GiftInfo>{
	private static final int RESOURCE_ID = R.layout.gift_item;
	private final LayoutInflater mInflater;

	private OnGiftItemClickListener mOnGiftItemClickListener;
	private List<GiftInfo> giftList;
	private Context context;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private boolean isFirstReresh;//�Ƿ��ǵ�һ��ˢ��adapter��Ŀ���Ǳ���ͼƬ���ظ�����
	private Map<Integer,Boolean> statusMap = new HashMap<Integer,Boolean>();
	private boolean flag = false;
	
	public GiftAdapter(Context context,OnGiftItemClickListener mOnGiftItemClickListener) {
		super(context, RESOURCE_ID);
		this.context = context;
		this.mOnGiftItemClickListener = mOnGiftItemClickListener;
		this.imageLoader = ImageLoaderManager.newInstance();
		options = ImageLoaderManager.configurationOption(R.drawable.gift_item_default_logo,
				R.drawable.gift_item_default_logo);
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		giftList = new ArrayList<GiftInfo>();
	}

	/**
	 * ���ⷽ������̬�ı�listview��item������ˢ��
	 * 
	 * @param productList
	 */
	public void setItems(List<GiftInfo> giftList,boolean isFirstReresh) {
		this.giftList.clear();
		this.isFirstReresh = isFirstReresh;
		if (giftList != null) {
			this.giftList.addAll(giftList);
		}
		for(int i=0;i<giftList.size();i++){
			statusMap.put(i, false);
		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return giftList.size();
	}

	@Override
	public GiftInfo getItem(int position) {
		return giftList.get(position);
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
		final GiftInfo info = giftList.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(RESOURCE_ID, null);
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.gift_item_title);
			viewHolder.content = (TextView) convertView
					.findViewById(R.id.gift_item_content);
			viewHolder.rules = (TextView) convertView
					.findViewById(R.id.gift_item_rules);
			viewHolder.logo = (ImageView) convertView
					.findViewById(R.id.gift_item_logo);
			viewHolder.getBtn = (Button) convertView
					.findViewById(R.id.gift_item_btn);
			viewHolder.rulesLayout = (RelativeLayout) convertView
					.findViewById(R.id.gift_item_rule_layout);
			viewHolder.ruleContent = (TextView) convertView
					.findViewById(R.id.gift_item_rule_content);
			viewHolder.ruleBtn = (Button) convertView
					.findViewById(R.id.gift_item_rule_btn);
			viewHolder.ruleDel = (RelativeLayout) convertView.findViewById(R.id.gift_item_rule_delbtn);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.title.setText(info.getName());
		viewHolder.content.setText(info.getDescription());
		if(info.getExternal_link_wap() != null && !"".equals(info.getExternal_link_wap())){
			viewHolder.ruleBtn.setTag(info.getExternal_link_wap());
			viewHolder.ruleBtn.setVisibility(View.VISIBLE);
		}else{
			viewHolder.ruleBtn.setVisibility(View.GONE);
		}
		StringBuffer sb = new StringBuffer();
		if(info.getRulesAppList() != null){
			for(int i=0;i<info.getRulesAppList().size();i++){
				sb.append(info.getRulesAppList().get(i)).append("\n");
			}
		}
		viewHolder.ruleContent.setText(sb.toString().replace("&#039;", "'").replace("&quot;", "\"")
				.replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&"));
		viewHolder.ruleContent.setMovementMethod(ScrollingMovementMethod.getInstance());
		viewHolder.ruleContent.setSelected(true);
		//�����жϻ�Ѿ���ʼ
		if(info.getIsStart() == 0){
			//��Ѿ���ʼ
			if(info.isLogin()){
				//�Ѿ���¼
				if(SettingsManager.isPersonalUser(context.getApplicationContext())){
					if(info.isGetOver()){
						//�Ѿ�������
						viewHolder.getBtn.setEnabled(false);
						viewHolder.getBtn.setText("������");
						viewHolder.getBtn.setTextColor(context.getResources().getColor(R.color.gray));
						viewHolder.getBtn.setBackgroundResource(R.drawable.gift_item_end_btn);
					}else{
						//��û����
						if(info.isGet()){
							//�Ѿ���ȡ
							viewHolder.getBtn.setEnabled(false);
							viewHolder.getBtn.setText("����ȡ");
							viewHolder.getBtn.setTextColor(context.getResources().getColor(R.color.gray));
							viewHolder.getBtn.setBackgroundResource(R.drawable.gift_item_end_btn);
						}else{
							viewHolder.getBtn.setEnabled(true);
							viewHolder.getBtn.setText("��ȡ");
							viewHolder.getBtn.setTextColor(context.getResources().getColor(R.color.prize_region_gift_item_tv_color));
							viewHolder.getBtn.setBackgroundResource(R.drawable.gift_item_get_btn);
						}
					}
				}else{
					if(info.isGetOver()){
						//�Ѿ�������
						viewHolder.getBtn.setEnabled(false);
						viewHolder.getBtn.setText("������");
						viewHolder.getBtn.setTextColor(context.getResources().getColor(R.color.gray));
						viewHolder.getBtn.setBackgroundResource(R.drawable.gift_item_end_btn);
					}else{
						//��û����
						viewHolder.getBtn.setEnabled(false);
						viewHolder.getBtn.setText("��ȡ");
						viewHolder.getBtn.setTextColor(context.getResources().getColor(R.color.gray));
						viewHolder.getBtn.setBackgroundResource(R.drawable.gift_item_end_btn);
					}
				}
				
			}else{
				//δ��¼
				viewHolder.getBtn.setEnabled(true);
				viewHolder.getBtn.setText("���¼");
				viewHolder.getBtn.setTextColor(context.getResources().getColor(R.color.prize_region_gift_item_tv_color));
				viewHolder.getBtn.setBackgroundResource(R.drawable.gift_item_get_btn);
			}
		}else if(info.getIsStart() == -1){
			//�δ��ʼ
			viewHolder.getBtn.setEnabled(false);
			viewHolder.getBtn.setText("�����ڴ�");
			viewHolder.getBtn.setTextColor(context.getResources().getColor(R.color.gray));
			viewHolder.getBtn.setBackgroundResource(R.drawable.gift_item_end_btn);
		}else if(info.getIsStart() == 1){
			//��Ѿ�����
			viewHolder.getBtn.setEnabled(false);
			viewHolder.getBtn.setText("�����");
			viewHolder.getBtn.setTextColor(context.getResources().getColor(R.color.gray));
			viewHolder.getBtn.setBackgroundResource(R.drawable.gift_item_end_btn);
		}
		viewHolder.rules.setTag(viewHolder.rulesLayout);
		viewHolder.rules.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				if(!statusMap.get(position)){
//					RelativeLayout ruleLayoutR = (RelativeLayout) v.getTag();
//					ruleLayoutR.setVisibility(View.VISIBLE);
//				}
//				flag = !statusMap.get(position);
				statusMap.put(position, true);
				notifyDataSetChanged();
			}
		});
		viewHolder.getBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mOnGiftItemClickListener.onclick(info, position);
			}
		});
		viewHolder.ruleDel.setTag(viewHolder.rulesLayout);
		viewHolder.ruleDel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				statusMap.put(position, false);
				notifyDataSetChanged();
			}
		});
		viewHolder.ruleBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//qianwang�̳�
				String url = (String) v.getTag();
				Uri uri = Uri.parse(url);
	                Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
	                context.startActivity(intent);  
			}
		});
		if(statusMap.get(position)){
			viewHolder.rulesLayout.setVisibility(View.VISIBLE);
		}else{
			viewHolder.rulesLayout.setVisibility(View.GONE);
		}
		YLFLogger.d("��Ա�����ƻ�2�ڽ�ƷͼƬ:"+info.getPic());
		ImageLoaderManager.loadingImage(imageLoader, info.getPic(), viewHolder.logo, options, null, null);
		return convertView;
	}

	/**
	 * �ڲ��࣬������Item��Ԫ��
	 * 
	 * @author Mr.liu
	 * 
	 */
	class ViewHolder {
		ImageView logo;//��Ʒlogo
		TextView title;//��Ʒ����
		TextView content;//��Ʒ����
		TextView rules;//����
		Button getBtn;//��ȡ��ť
		RelativeLayout rulesLayout;//����
		TextView ruleContent;
		Button ruleBtn;//ǰ���̳�
		RelativeLayout ruleDel;
	}

	public interface OnGiftItemClickListener{
		void onclick(GiftInfo info,int position);
		void ruleOnClick(View v);
	}
}
