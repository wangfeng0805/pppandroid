package com.ylfcf.ppp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.MoneyStatus;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.util.YLFLogger;
import com.ylfcf.ppp.widget.RoundProgressBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * VIP产品列表
 * 
 * @author Mr.liu
 * 
 */
public class BidListVIPAdapter extends ArrayAdapter<ProductInfo> {
	private static final int RESOURCE_ID = R.layout.bid_vip_item;
	private final LayoutInflater mInflater;

	private List<ProductInfo> bidItems;
	private Context context;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public BidListVIPAdapter(Context context) {
		super(context, RESOURCE_ID);
		this.context = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		bidItems = new ArrayList<ProductInfo>();
	}

	/**
	 * 对外方法，动态改变listview的item并进行刷新
	 * 
	 * @param productList
	 */
	public void setItems(List<ProductInfo> productList) {
		this.bidItems.clear();
		if (productList != null) {
			this.bidItems.addAll(productList);
		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return bidItems.size();
	}

	@Override
	public ProductInfo getItem(int position) {
		return bidItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 设置具体一个标的的显示内容
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		ProductInfo info = bidItems.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(RESOURCE_ID, null);
			viewHolder.projectName = (TextView) convertView
					.findViewById(R.id.bid_vip_item_project_name);
			viewHolder.interestRateMin = (TextView) convertView
					.findViewById(R.id.bid_vip_item_invest_rate_min);
			viewHolder.middleText = (TextView) convertView
					.findViewById(R.id.bid_vip_item_text0);
			viewHolder.interestRateMax = (TextView) convertView
					.findViewById(R.id.bid_vip_item_invest_rate_max);
			viewHolder.timeLimit = (TextView) convertView
					.findViewById(R.id.bid_vip_item_invest_time_limit);
			viewHolder.totalMoney = (TextView) convertView
					.findViewById(R.id.bid_vip_item_invest_total_money);
			viewHolder.roundProgressBar = (RoundProgressBar) convertView
					.findViewById(R.id.bid_vip_item_roundbar);
			viewHolder.angleImg = (ImageView) convertView
					.findViewById(R.id.bid_vip_item_angle);
			viewHolder.progressTV = (TextView) convertView
					.findViewById(R.id.bid_vip_item_progresstv);
			viewHolder.extraInterestLayout = (RelativeLayout) convertView
					.findViewById(R.id.bid_vip_item_extra_interest_layout);
			viewHolder.extraInterestText = (TextView) convertView
					.findViewById(R.id.bid_vip_item_extra_interest_text);
			viewHolder.nhsyText = (TextView) convertView
					.findViewById(R.id.bid_vip_item_nhsy_text);
			viewHolder.jiaTipsImg = (ImageView)convertView
					.findViewById(R.id.bid_vip_item_tips_img);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.projectName.setText(info.getBorrow_name());
		String investLimit = "";
		// 投资期限
		if (info.getInvest_horizon() == null
				|| "".equals(info.getInvest_horizon())) {
			investLimit = info.getInterest_period();
		} else {
			investLimit = info.getInvest_horizon().replace("天", "");
		}
		viewHolder.timeLimit.setText(investLimit);
		viewHolder.roundProgressBar.setTextIsDisplayable(false);// 不显示中间的字
		double totalMoneyL = 0d;
		int totalMoneyI = 0;
		try {
			totalMoneyL = Double.parseDouble(info.getTotal_money());
			totalMoneyI = (int) totalMoneyL;
		} catch (Exception e) {
		}
		viewHolder.totalMoney.setText(totalMoneyI / 10000 + "");
		String bite = info.getBite();
		float biteFloat = 0f;
		int biteInt = 0;
		if (bite != null) {
			bite = bite.replace("%", "");
		}
		try {
			biteFloat = Float.parseFloat(bite) * 100;
			biteInt = (int) biteFloat;
		} catch (Exception e) {
		}
		viewHolder.roundProgressBar.setMax(10000);
		YLFLogger.d("百分比：" + viewHolder.roundProgressBar.getProgress());
		if (MoneyStatus.NOFULL.equals(info.getMoney_status())) {
//			viewHolder.progressTV.setText(info.getBite());
			viewHolder.progressTV.setText(biteInt/100);
			viewHolder.progressTV.setTextColor(context.getResources().getColor(
					R.color.common_topbar_bg_color));
			viewHolder.roundProgressBar.setProgress(biteInt);
		} else {
			try {
				viewHolder.progressTV.setText(Util.MoneyStatusCG(info));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			viewHolder.progressTV.setTextColor(context.getResources().getColor(
					R.color.gray));
			viewHolder.roundProgressBar.setProgress(10000);
		}
		viewHolder.nhsyText.setText("业绩比较基准");
		viewHolder.angleImg.setScaleType(ScaleType.FIT_CENTER);
		viewHolder.angleImg.setImageResource(R.drawable.licai_vip_logo);

		double extraInterestD = 0d;
		String extraInterest = info.getAndroid_interest_rate();
		try {
			extraInterestD = Double.parseDouble(extraInterest);
		} catch (Exception e) {
		}
		if(SettingsManager.checkActiveStatusBySysTime(info.getAdd_time(),
				SettingsManager.yyyJIAXIStartTime,SettingsManager.yyyJIAXIEndTime) == 0 && "365".equals(investLimit)){
			viewHolder.extraInterestLayout.setVisibility(View.VISIBLE);
			viewHolder.extraInterestText.setVisibility(View.GONE);
			viewHolder.jiaTipsImg.setVisibility(View.VISIBLE);
		}else{
			if (extraInterestD > 0) {
				viewHolder.extraInterestLayout.setVisibility(View.VISIBLE);
				viewHolder.jiaTipsImg.setVisibility(View.GONE);
				viewHolder.extraInterestText.setText("+"
						+ info.getAndroid_interest_rate());
			} else {
				viewHolder.extraInterestLayout.setVisibility(View.GONE);
			}
		}

		if (position == 0) {
			convertView.setPadding(0, context.getResources()
					.getDimensionPixelSize(R.dimen.common_measure_10dp), 0, 0);
		} else {
			convertView.setPadding(0, 0, 0, 0);
		}

		viewHolder.interestRateMin.setVisibility(View.GONE);
		viewHolder.middleText.setVisibility(View.GONE);
		viewHolder.interestRateMax.setText(info.getInterest_rate());
		return convertView;
	}

	/**
	 * 内部类，定义了Item的元素
	 * 
	 * @author Mr.liu
	 * 
	 */
	class ViewHolder {
		ImageView angleImg;
		ImageView jiaTipsImg;
		TextView projectName;
		TextView interestRateMin;// 年化收益最小利率
		TextView middleText;// 两个利率之间的符号
		TextView interestRateMax;// 年化收益最大利率
		TextView timeLimit;// 期限
		TextView totalMoney;// 募集总金额
		TextView progressTV;// 进度条的信息
		TextView nhsyText;// 年化收益四个字在VIP产品中改成了“业绩比较基准”
		RoundProgressBar roundProgressBar;
		RelativeLayout extraInterestLayout;// 加息的布局
		TextView extraInterestText;// 加息的text
	}
}
