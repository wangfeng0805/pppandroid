package com.ylfcf.ppp.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncArticle;
import com.ylfcf.ppp.entity.ArticleInfo;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * banner����---����
 * @author jianbing
 *
 */
public class BannerDetailsActivity extends BaseActivity implements OnClickListener{
	private static final String className = "BannerDetailsActivity";

	private static final int REFRESH_VIEW = 5810;
	private static final int REQUEST_ARTICLE_WHAT = 5811;
	
	private BannerInfo mBannerInfo;
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	private TextView title,time,content;
	private TextView nodataText;
	private LinearLayout contentLayout;//�����������
	
	private  ArticleInfo articleInfoTemp;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_ARTICLE_WHAT:
				requestArticle(mBannerInfo.getArticle_id());
				break;
			case REFRESH_VIEW:
				if(mLoadingDialog != null && mLoadingDialog.isShowing()){
					mLoadingDialog.dismiss();
				}
				CharSequence text = (CharSequence) msg.obj;
				title.setText(articleInfoTemp.getTitle());
				if(releaseTime != null && !"".equals(releaseTime)){
					time.setText("����ʱ�䣺"+releaseTime);
				}else{
					time.setText("����ʱ�䣺"+articleInfoTemp.getAdd_time());
				}
				
				content.setText(text);
				break;

			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.banner_details_activity);
		
		mBannerInfo = (BannerInfo) getIntent().getSerializableExtra("BannerInfo");
		findViews();
		if(mBannerInfo != null){
			handler.sendEmptyMessage(REQUEST_ARTICLE_WHAT);
		}
	}

	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("����");
		
		title = (TextView)findViewById(R.id.banner_details_actiivty_title);
		time = (TextView)findViewById(R.id.banner_details_actiivty_time);
		content = (TextView)findViewById(R.id.banner_details_actiivty_content);
		content.setMovementMethod(LinkMovementMethod.getInstance());//��textview����ĳ����ӵ����Ӧ
		nodataText = (TextView) findViewById(R.id.banner_details_activity_nodata);
		contentLayout = (LinearLayout) findViewById(R.id.banner_details_activity_content_layout);
	}

	@Override
	protected void onResume() {
		super.onResume();
		UMengStatistics.statisticsOnPageStart(className);//����ͳ��ҳ����ת
		UMengStatistics.statisticsResume(this);//����ͳ��ʱ��
	}

	@Override
	protected void onPause() {
		super.onPause();
		UMengStatistics.statisticsOnPageEnd(className);//����ͳ��ҳ����ת
		UMengStatistics.statisticsPause(this);//����ͳ��ʱ��
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}

	private String releaseTime = "";//����������ķ���ʱ��
	private void initData(ArticleInfo info){
		if(info == null){
			contentLayout.setVisibility(View.GONE);
			nodataText.setVisibility(View.VISIBLE);
			return;
		}
		try {
			Date date = sdf.parse(info.getAdd_time());
			releaseTime = sdf.format(date);
		} catch (Exception e) {
		}
		new ImageLoadThread().start();
		
		//�ָ��ߡ�
		if(info.getTitle() != null && !"".equals(info.getTitle()) && info.getContent() != null && !"".equals(info.getContent())){
			contentLayout.setVisibility(View.VISIBLE);
			nodataText.setVisibility(View.GONE);
			String addTime = "";
			try {
				Date addDate = sdf.parse(info.getAdd_time());
				addTime = sdf.format(addDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			title.setText(info.getTitle());
			if(addTime != null && !"".equals(addTime)){
				time.setText("����ʱ�䣺"+addTime);
			}else{
				time.setText("����ʱ�䣺"+info.getAdd_time());
			}
			
			content.setText(Html.fromHtml(info.getContent()));
		}else{
			contentLayout.setVisibility(View.GONE);
			nodataText.setVisibility(View.VISIBLE);
		}
		
	}

	class ImageLoadThread extends Thread {
		@Override
		public void run() {
			/**
			 * Ҫʵ��ͼƬ����ʾ��Ҫʹ��Html.fromHtml��һ���ع�������public static Spanned fromHtml
			 * (String source, Html.ImageGetterimageGetter, Html.TagHandler
			 * tagHandler)����Html.ImageGetter��һ���ӿڣ�����Ҫʵ�ִ˽ӿڣ�������getDrawable
			 * (String source)�����з���ͼƬ��Drawable����ſ��ԡ�
			 */
			ImageGetter imageGetter = new ImageGetter() {
				@Override
				public Drawable getDrawable(String source) {
					// TODO Auto-generated method stub
					URL url;
					Drawable drawable = null;
					try {
						url = new URL(source);
						int[] screen = SettingsManager.getScreenDispaly(BannerDetailsActivity.this);
						drawable = Drawable.createFromStream(url.openStream(),null);
						if(drawable != null){
							int imageIntrinsicWidth = drawable.getIntrinsicWidth();
							float imageIntrinsicHeight = (float)drawable.getIntrinsicHeight();
							int curImageWidth = screen[0] - 2*(getResources().getDimensionPixelSize(R.dimen.common_measure_20dp));//��ȥ�����߾�
							int curImageHeight = (int) (curImageWidth*(imageIntrinsicHeight/imageIntrinsicWidth));
							drawable.setBounds(0, 0, curImageWidth,curImageHeight);//�ĸ���������Ϊ���Ͻǡ����½�����ȷ����һ�����Σ�ͼƬ����������η�Χ�ڻ�����
						}
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					return drawable;
				}
			};
			CharSequence htmlText = null;
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
				htmlText = Html.fromHtml(articleInfoTemp.getContent(),Html.FROM_HTML_MODE_LEGACY,imageGetter, null);
			}else{
				htmlText = Html.fromHtml(articleInfoTemp.getContent(),imageGetter, null);
			}
			Message msg = handler.obtainMessage(REFRESH_VIEW);
			msg.obj = htmlText;
			handler.sendMessage(msg);

		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;

		default:
			break;
		}
	}
	
	/**
	 * ������������
	 * @param id
	 */
	private void requestArticle(String id){
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		AsyncArticle articleTask = new AsyncArticle(BannerDetailsActivity.this, id,new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(mLoadingDialog != null && mLoadingDialog.isShowing()){
					mLoadingDialog.dismiss();
				}
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						articleInfoTemp = baseInfo.getmArticleInfo();
						initData(articleInfoTemp);
					}else{
						contentLayout.setVisibility(View.GONE);
						nodataText.setVisibility(View.VISIBLE);
					}
				}else{
					contentLayout.setVisibility(View.GONE);
					nodataText.setVisibility(View.VISIBLE);
				}
			}
		});
		articleTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
