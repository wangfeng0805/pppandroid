package com.ylfcf.ppp.ui;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.ProjectCailiaoInfo;
import com.ylfcf.ppp.util.ImageLoaderManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * ��Ʒ����ͼƬ-----��ͼƬ�б�
 * @author Administrator
 *
 */
public class ProductDataDetailsActivity extends BaseActivity implements OnClickListener{
	private static final String className = "ProductDataDetailsActivity";
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private ViewPager viewpager;
	private CirclePageIndicator indicator;
	private List<ImageView> viewList = new ArrayList<ImageView>();
	private ArrayList<ProjectCailiaoInfo> cailiaoList = null;
	private int position = 0;
	
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.product_data_details_activity);
		this.imageLoader = ImageLoaderManager.newInstance();
		options = ImageLoaderManager.configurationOption(R.drawable.default_vertical_logo,
				R.drawable.default_vertical_logo);
		cailiaoList = (ArrayList<ProjectCailiaoInfo>) getIntent().getSerializableExtra("cailiao_list");
		position = getIntent().getIntExtra("position", 0);
		initDatas(cailiaoList);
		findViews();
	}
	
	private void initDatas(ArrayList<ProjectCailiaoInfo> list){
		if(list != null){
			int size = list.size();
			for(int i=0;i<size;i++){
				ProjectCailiaoInfo cailiaoInfo = list.get(i);
				ImageView view = new ImageView(this);
				view.setScaleType(ScaleType.FIT_CENTER);
				String imageURL = cailiaoInfo.getImgURL();
				if(!imageURL.contains("http")){
					imageURL = URLGenerator.BORROW_CAILIAO_BASE_URL + imageURL;
				}
				ImageLoaderManager.loadingImage(imageLoader, imageURL, view, options, null, null);
				viewList.add(view);
			}
		}
	}

	private  void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("����֤��");
		
		viewpager = (ViewPager)findViewById(R.id.product_data_details_activity_viewpager);
		indicator = (CirclePageIndicator) findViewById(R.id.product_data_details_activity_indicator);
		viewpager.setAdapter(mPagerAdapter);
		indicator.setViewPager(viewpager);
		viewpager.setCurrentItem(position, false);
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ImageLoaderManager.clearMemoryCache();
	}

	PagerAdapter mPagerAdapter = new PagerAdapter(){
        @Override
        //��ȡ��ǰ���������
        public int getCount() {
            return viewList.size();
        }

        @Override
        //���Ƿ��ɶ������ɽ���
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0==arg1;
        }
        //�Ǵ�ViewGroup���Ƴ���ǰView
         public void destroyItem(View arg0, int arg1, Object arg2) {  
                ((ViewPager) arg0).removeView(viewList.get(arg1));  
            }  
        
        //����һ������������������PagerAdapter������ѡ���ĸ�������ڵ�ǰ��ViewPager��
        public Object instantiateItem(View arg0, int arg1){
            ((ViewPager)arg0).addView(viewList.get(arg1));
            return viewList.get(arg1);                
        }
        
        
    };
}
