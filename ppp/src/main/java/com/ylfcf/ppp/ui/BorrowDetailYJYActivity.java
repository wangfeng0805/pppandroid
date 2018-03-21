package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncProjectDetails;
import com.ylfcf.ppp.async.AsyncYJYBorrowDetails;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.ProjectCailiaoInfo;
import com.ylfcf.ppp.entity.ProjectInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnIsBindingListener;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.ylfcf.ppp.util.Util.formatRate;

/**
 * Ԫ��ӯ����ҳ
 * Created by Administrator on 2017/7/21.
 */

public class BorrowDetailYJYActivity extends BaseActivity implements View.OnClickListener {
    private static final String className = "BorrowDetailYJYActivity";

    private static final int COUNT_DOWN_LASTTIME = 5142;//����ʱ
    private LinearLayout topLeftBtn;
    private TextView topTitleTV;
    private TextView borrowName;
    private TextView borrowRate;//����
    private TextView borrowMoney;// ļ�����
    private TextView timeLimit;// ����
    private Button bidBtn;// ����Ͷ��
    private TextView repayType1;
    private TextView profitTv;// һ���Ǯ�ɵ����档��
    private TextView timeLimitTV;//ʣ��ʱ��
    // private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager mViewPager;
    private LinearLayout introLayout, safeLayout, zizhiLayout;
    private LinearLayout extraInterestLayout;
    private TextView extraInterestText;

    public ProductInfo productInfo;
    public InvestRecordInfo recordInfo;
    private ProjectInfo project;// ��Ŀ��Ϣ
    private String sysTime = "";

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case COUNT_DOWN_LASTTIME:
                    long lastTimeL = (long)msg.obj;
                    countDownLastTime(lastTimeL - 1000);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.borrowdetail_yjy_activity);
        Intent intent = getIntent();
        productInfo = (ProductInfo) intent.getSerializableExtra("PRODUCT_INFO");
        recordInfo = (InvestRecordInfo) intent
                .getSerializableExtra("InvestRecordInfo");
        findViews();
        if (productInfo != null) {
            getProjectDetails(productInfo.getProject_id());
        } else if (recordInfo != null) {
            // �ȸ���Borrowid��ȡprojectid
            getProductDetailsById(recordInfo.getBorrow_id());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @SuppressWarnings("deprecation")
    private void findViews() {
        topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
        topLeftBtn.setOnClickListener(this);
        topTitleTV = (TextView) findViewById(R.id.common_page_title);
        topTitleTV.setText("��Ʒ����");

        borrowName = (TextView) findViewById(R.id.borrow_detail_yjy_activity_borrowname);
        borrowRate = (TextView) findViewById(R.id.borrow_details_yjy_activity_invest_rate_max);
        borrowMoney = (TextView) findViewById(R.id.borrow_details_yjy_activity_invest_total_money);
        timeLimit = (TextView) findViewById(R.id.borrow_details_invest_time_limit);
        bidBtn = (Button) findViewById(R.id.borrow_detail_yjy_activity_bidBtn);
        bidBtn.setOnClickListener(this);
        repayType1 = (TextView) findViewById(R.id.borrow_details_yjy_activity_repay_type1);
        profitTv = (TextView) findViewById(R.id.borrow_details_yjy_activity_profit);

        introLayout = (LinearLayout) findViewById(R.id.borrow_details_yjy_activity_intro_layout);
        introLayout.setOnClickListener(this);
        safeLayout = (LinearLayout) findViewById(R.id.borrow_details_yjy_activity_safe_layout);
        safeLayout.setOnClickListener(this);
        zizhiLayout = (LinearLayout) findViewById(R.id.borrow_details_yjy_activity_certificate_layout);
        zizhiLayout.setOnClickListener(this);
        extraInterestLayout = (LinearLayout) findViewById(R.id.borrow_details_yjy_extra_interest_layout);
        extraInterestText = (TextView) findViewById(R.id.borrow_details_yjy_extra_interest_text);
        timeLimitTV = (TextView) findViewById(R.id.borrowdetail_yjy_activity_lefttiem);
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

    int biteIntFromProduct = 0;
    private void initDataFromProductList() {
        if("δ����".equals(productInfo.getMoney_status())){
            bidBtn.setEnabled(true);
            bidBtn.setText("����Ͷ��");
            computeLastTime(productInfo);
        }else{
            bidBtn.setEnabled(false);
            bidBtn.setText("Ͷ���ѽ���");
            timeLimitTV.setText("00�� 00Сʱ 00�� 00��");
        }
        borrowName.setText(productInfo.getBorrow_name());

        // �껯����
        String rate = productInfo.getInterest_rate();
        String extraRate = productInfo.getAndroid_interest_rate();
        float extraRateF = 0f;
        try {
            extraRateF = Float.parseFloat(extraRate);
        } catch (Exception e) {
        }
        if(extraRateF > 0){
            extraInterestLayout.setVisibility(View.VISIBLE);
            extraInterestText.setText("+"+extraRateF);
        }else{
            extraInterestLayout.setVisibility(View.GONE);
        }
        // Ͷ������
        String horizon = productInfo.getInvest_period();
        int horizonInt = 0;
        try {
            horizonInt = Integer.parseInt(horizon);
        } catch (Exception e) {
            horizonInt = 0;
        }

        float rateF = 0f;
        try {
            rateF = Float.parseFloat(rate);
        } catch (Exception e) {
        }
        borrowRate.setText(formatRate(String.valueOf(rateF)));
        timeLimit.setText(horizon);
        if(productInfo.getRepay_way() != null && !"".equals(productInfo.getRepay_way())){
            repayType1.setText(productInfo.getRepay_way());
        }else{
            repayType1.setText("���ڻ�����Ϣ");
        }

        double totalMoneyL = 0d;
        int totalMoneyI = 0;
        try {
            totalMoneyL = Double.parseDouble(productInfo.getTotal_money());
            totalMoneyI = (int) totalMoneyL;
        } catch (Exception e) {
        }
        if(totalMoneyI % 10000 == 0){
            borrowMoney.setText(totalMoneyI / 10000 + "");
        }else{
            borrowMoney.setText((totalMoneyI / 10000d) + "");
        }
        double biteD = 0d;
        try {
            biteD = (Double.parseDouble(productInfo.getInvest_money())*100/Double.parseDouble(productInfo.getTotal_money()));
            biteIntFromProduct = (int) biteD;
        } catch (Exception e) {
        }
        profitTv.setText(new DecimalFormat("#.00").format((rateF + extraRateF) * 100/365*horizonInt) + "");
    }

    int biteIntFromRecord = 0;
    private void initDataFromRecord(ProductInfo info) {
        productInfo = info;
        if("δ����".equals(info.getMoney_status())){
            bidBtn.setEnabled(true);
            bidBtn.setText("����Ͷ��");
            computeLastTime(info);
        }else{
            bidBtn.setEnabled(false);
            bidBtn.setText("Ͷ���ѽ���");
            timeLimitTV.setText("00�� 00Сʱ 00�� 00��");
        }
        borrowName.setText(info.getBorrow_name());

        // �껯����
        String rate = info.getInterest_rate();
        String extraRate = productInfo.getAndroid_interest_rate();
        float extraRateF = 0f;
        try {
            extraRateF = Float.parseFloat(extraRate);
        } catch (Exception e) {
        }
        if(extraRateF > 0){
            extraInterestLayout.setVisibility(View.VISIBLE);
            extraInterestText.setText("+"+Util.formatRate(String.valueOf(extraRateF)));
        }else{
            extraInterestLayout.setVisibility(View.GONE);
        }
        // Ͷ������
        String horizon = info.getInvest_period();
        int horizonInt = Integer.parseInt(horizon);

        borrowRate.setText(formatRate(rate));
        timeLimit.setText(horizon);
        if(productInfo.getRepay_way() != null && !"".equals(productInfo.getRepay_way())){
            repayType1.setText(productInfo.getRepay_way());
        }else{
            repayType1.setText("���ڻ�����Ϣ");
        }
        double totalMoneyL = 0d;
        double investedMoneyD = 0d;//��Ͷ�ʵ�Ǯ
        int totalMoneyI = 0;
        int investedMoneyI = 0;
        try {
            totalMoneyL = Double.parseDouble(info.getTotal_money());
            totalMoneyI = (int) totalMoneyL;
            investedMoneyD = Double.parseDouble(info.getInvest_money());
            investedMoneyI = (int)investedMoneyD;
        } catch (Exception e) {
        }
        if(totalMoneyI < 10000){
            borrowMoney.setText(Util.double2PointDoubleOne(totalMoneyI / 10000d) + "");
        }else{
            borrowMoney.setText((totalMoneyI / 10000) + "");
        }
        double biteFloat = 0f;
        try {
            biteFloat = investedMoneyI*100/totalMoneyI;
            biteIntFromRecord = (int) biteFloat;
        } catch (Exception e) {
        }

        float rateF = 0f;
        try {
            rateF = Float.parseFloat(rate);
        } catch (Exception e) {
        }
        profitTv.setText(new DecimalFormat("#.00").format((rateF + extraRateF) * 100/365*horizonInt) + "");
    }

    /**
     * ����ʣ��ʱ��
     * @param info
     */
    private void computeLastTime(ProductInfo info){
        Date addDate = null;
        Date sysDate = null;
        int periodInt = 0;
        long lastTimeL = 0l;
        try{
            addDate = sdf.parse(info.getAdd_time());
            sysDate = sdf.parse(sysTime);
            periodInt = Integer.parseInt(info.getCollect_period());
        }catch (Exception e){
        }
        lastTimeL = addDate.getTime() + periodInt*24*3600*1000L - sysDate.getTime();//ʣ��ĺ�����
        if(lastTimeL <= 0){
            timeLimitTV.setText("00�� 00Сʱ 00�� 00��");
            return;
        }
        countDownLastTime(lastTimeL);
    }

    /**
     * ʣ��ʱ��
     * @param lastTimeL
     */
    private void countDownLastTime(long lastTimeL){
        StringBuffer sb = new StringBuffer();
        int dayInt = 0;
        int hourInt = 0;
        int minuteInt = 0;
        int secInt = 0;
        dayInt = (int)(lastTimeL/1000/3600/24);
        hourInt = (int)(lastTimeL/1000/3600%24);
        minuteInt = (int)(lastTimeL/1000/60%60);
        secInt = (int)(lastTimeL/1000%60);
        sb.append(int2DoubleString(dayInt)+"�� "+int2DoubleString(hourInt)+"Сʱ "+int2DoubleString(minuteInt)
            +"�� "+int2DoubleString(secInt)+"��");
        timeLimitTV.setText(sb.toString());
        Message msg = handler.obtainMessage(COUNT_DOWN_LASTTIME);
        msg.obj = lastTimeL;
        handler.sendMessageDelayed(msg,1000L);
    }

    private String int2DoubleString(int data){
        if(data < 10){
            return "0"+data;
        }
        return String.valueOf(data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.borrow_detail_yjy_activity_bidBtn:
                // ��SettingsManager�ж�ȡ���룬���Ϊ����ζ��û�е�¼��
                boolean isLogin = !SettingsManager.getLoginPassword(
                        BorrowDetailYJYActivity.this).isEmpty()
                        && !SettingsManager.getUser(BorrowDetailYJYActivity.this)
                        .isEmpty();
                // 1������Ƿ��Ѿ���¼
                if (isLogin) {
                    //�ж��Ƿ�ʵ����
				    checkIsVerify("Ͷ��");
                } else {
                    // δ��¼����ת����¼ҳ��
                    Intent intent = new Intent();
                    intent.setClass(BorrowDetailYJYActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.common_topbar_left_layout:
                finish();
                break;
            // ��Ŀ����
            case R.id.borrow_details_yjy_activity_intro_layout:
                // setViewPagerCurrentPosition(0);
                Intent intentProductInfo = new Intent(BorrowDetailYJYActivity.this,ProductInfoYJYActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("PROJECT_INFO", project);
                bundle.putSerializable("PRODUCT_INFO", productInfo);
                intentProductInfo.putExtra("BUNDLE", bundle);
                startActivity(intentProductInfo);
                break;
            // ��ȫ����
            case R.id.borrow_details_yjy_activity_safe_layout:
                // setViewPagerCurrentPosition(1);
                Intent intentSaft = new Intent(BorrowDetailYJYActivity.this,ProductSafetyActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("PROJECT_INFO", project);
                bundle1.putSerializable("PRODUCT_INFO", productInfo);
                intentSaft.putExtra("BUNDLE", bundle1);
                startActivity(intentSaft);
                break;
            // �������
            case R.id.borrow_details_yjy_activity_certificate_layout:
                // setViewPagerCurrentPosition(2);
                Intent intentProductData = new Intent(BorrowDetailYJYActivity.this,ProductDataActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("PROJECT_INFO", project);
                bundle2.putSerializable("PRODUCT_INFO", productInfo);
                bundle2.putString("from_where", "yjy");
                intentProductData.putExtra("BUNDLE", bundle2);
                startActivity(intentProductData);
                break;
            default:
                break;
        }
    }

    /**
     * ��֤�û��Ƿ��Ѿ���֤
     * @param type ����ֵ��,�����֡�
     */
    private void checkIsVerify(final String type){
        bidBtn.setEnabled(false);
        RequestApis.requestIsVerify(BorrowDetailYJYActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
            @Override
            public void isVerify(boolean flag, Object object) {
                if(flag){
                    //�û��Ѿ�ʵ��
                    Intent intent = new Intent();
                    intent.putExtra("PRODUCT_INFO", productInfo);
                    intent.setClass(BorrowDetailYJYActivity.this, BidYJYActivity.class);
                    startActivity(intent);
                }else{
                    //�û�û��ʵ��
                    Intent intent = new Intent(BorrowDetailYJYActivity.this,UserVerifyActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("type", type);
                    bundle.putSerializable("PRODUCT_INFO", productInfo);
                    intent.putExtra("bundle", bundle);
                    startActivity(intent);
                }
                bidBtn.setEnabled(true);
            }
            @Override
            public void isSetWithdrawPwd(boolean flag, Object object) {
            }
        });
    }

    /**
     * �ж��û��Ƿ��Ѿ���
     * @param type "��ֵ����"
     */
    private void checkIsBindCard(final String type){
        RequestApis.requestIsBinding(BorrowDetailYJYActivity.this, SettingsManager.getUserId(getApplicationContext()), "����", new OnIsBindingListener() {
            @Override
            public void isBinding(boolean flag, Object object) {
                Intent intent = new Intent();
                if(flag){
                    //�û��Ѿ���
                    if("���Ŵ�Ͷ��".equals(type)){
                        //��ôֱ��������ֵҳ��
                        intent.putExtra("PRODUCT_INFO", productInfo);
                        intent.setClass(BorrowDetailYJYActivity.this, BidYJYActivity.class);
                    }
                }else{
                    //�û���û�а�
                    Bundle bundle = new Bundle();
                    bundle.putString("type", type);
                    bundle.putSerializable("PRODUCT_INFO", productInfo);
                    intent.putExtra("bundle", bundle);
                    intent.setClass(BorrowDetailYJYActivity.this, BindCardActivity.class);
                }
                startActivity(intent);
                bidBtn.setEnabled(true);
            }
        });
    }

    /**
     * ��������Ĳ��ϵ�ͼƬ
     * @param info
     */
    private void parseProjectCailiaoMarkImg(ProjectInfo info){
        if(info == null)
            return;
        String imageNames[] = info.getImgs_name().split("\\|");
        ArrayList<ProjectCailiaoInfo> cailiaoListTemp = new ArrayList<ProjectCailiaoInfo>();
        ArrayList<ProjectCailiaoInfo> cailiaoList = new ArrayList<ProjectCailiaoInfo>();
        String materials = info.getMaterials();//�����ͼƬ
        Document doc = Jsoup.parse(materials);
        Elements ele=doc.getElementsByTag("p");
        for(Element e :ele){
            ProjectCailiaoInfo cailiaoInfo = null;
            String imageUrl = e.getElementsByTag("img").attr("src");
            String imageTitle = e.getElementsByTag("img").attr("title");
            System.out.println("ͼƬ���ӣ�"+imageUrl);
            System.out.println("ͼƬ���⣺"+imageTitle);
            if(imageUrl != null && !"".equals(imageUrl)){
                cailiaoInfo = new ProjectCailiaoInfo();
                cailiaoInfo.setImgURL(imageUrl);
                cailiaoListTemp.add(cailiaoInfo);
            }
        }

        for(int i=0;i<cailiaoListTemp.size();i++){
            ProjectCailiaoInfo cailiao = cailiaoListTemp.get(i);
            if(i<imageNames.length){
                cailiao.setTitle(imageNames[i]);
            }
            cailiaoList.add(cailiao);
        }

        if(project != null){
            project.setCailiaoMarkList(cailiaoList);
        }
    }

    /**
     * ����û����Ĳ��ϵ�ͼƬ
     * @param info
     */
    private void parseProjectCailiaoNomarkImg(ProjectInfo info){
        if(info == null)
            return;
        String imageNames[] = info.getImgs_name().split("\\|");
        ArrayList<ProjectCailiaoInfo> cailiaoListTemp = new ArrayList<ProjectCailiaoInfo>();
        ArrayList<ProjectCailiaoInfo> cailiaoList = new ArrayList<ProjectCailiaoInfo>();
        String materials = info.getMaterials_nomark();//û�����ͼƬ
        Document doc = Jsoup.parse(materials);
        Elements ele=doc.getElementsByTag("p");
        for(Element e :ele){
            ProjectCailiaoInfo cailiaoInfo = null;
            String imageUrl = e.getElementsByTag("img").attr("src");
            String imageTitle = e.getElementsByTag("img").attr("title");
            System.out.println("ͼƬ���ӣ�"+imageUrl);
            System.out.println("ͼƬ���⣺"+imageTitle);
            if(imageUrl != null && !"".equals(imageUrl)){
                cailiaoInfo = new ProjectCailiaoInfo();
                cailiaoInfo.setImgURL(imageUrl);
                cailiaoListTemp.add(cailiaoInfo);
            }
        }

        for(int i=0;i<cailiaoListTemp.size();i++){
            ProjectCailiaoInfo cailiao = cailiaoListTemp.get(i);
            if(i<imageNames.length){
                cailiao.setTitle(imageNames[i]);
            }
            cailiaoList.add(cailiao);
        }

        if(project != null){
            project.setCailiaoNoMarkList(cailiaoList);
        }
    }

    /**
     * ��ȡ��Ŀ����
     *
     * @param id
     */
    private void getProjectDetails(String id) {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()
                && !isFinishing()) {
            mLoadingDialog.show();
        }
        AsyncProjectDetails task = new AsyncProjectDetails(
                BorrowDetailYJYActivity.this, id, new Inter.OnCommonInter() {
            @Override
            public void back(BaseInfo baseInfo) {
                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    mLoadingDialog.dismiss();
                }
                if (baseInfo != null) {
                    sysTime = baseInfo.getTime();
                    project = baseInfo.getmProjectInfo();
                    parseProjectCailiaoMarkImg(project);
                    parseProjectCailiaoNomarkImg(project);
                    initDataFromProductList();
                }
            }
        });
        task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    /**
     * ���ݲ�Ʒid��ȡ��Ʒ����
     *
     * @param borrowId
     */
    private void getProductDetailsById(String borrowId) {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()
                && !isFinishing()) {
            mLoadingDialog.show();
        }
        AsyncYJYBorrowDetails task = new AsyncYJYBorrowDetails(BorrowDetailYJYActivity.this,
                borrowId,new OnCommonInter() {
            @Override
            public void back(BaseInfo baseInfo) {
                if (baseInfo != null) {
                    int resultCode = SettingsManager
                            .getResultCode(baseInfo);
                    sysTime = baseInfo.getTime();
                    if (resultCode == 0) {
                        productInfo = baseInfo.getmProductInfo();
                        initDataFromRecord(productInfo);
                        getProjectDetails(productInfo.getProject_id());
                    }else{
                        mLoadingDialog.dismiss();
                    }
                }else{
                    mLoadingDialog.dismiss();
                }
            }
        });
        task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }
}
