package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.UserRMBAccountInfo;
import com.ylfcf.ppp.fragment.AccountCenterHKRLFragment;
import com.ylfcf.ppp.fragment.AccountCenterZHZCFragment;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.widget.LoadingDialog;

/**
 * �˻�����
 * Created by Administrator on 2017/8/2.
 */

public class AccountCenterActivity extends BaseActivity implements View.OnClickListener{
    private static final String className = "AccountCenterActivity";
    private LinearLayout topLeftBtn;
    private TextView topTitleTV;

    private Button zhzcTabBtn,hkrlTabBtn;//�˻��ʲ����ؿ�����
    public LoadingDialog loadingDialog;
    private UserRMBAccountInfo yllianAccountInfo;//�����˻���Ϣ
    private UserRMBAccountInfo huifuAccountInfo;//�㸶�˻���Ϣ
    private BaseInfo yjbInterestBaseInfo;//Ԫ��Ҳ���������
    private ImageView floatImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.account_center_activity);
        Intent intent = getIntent();
        yllianAccountInfo = (UserRMBAccountInfo)intent.getSerializableExtra("ylUserRMBAccountInfo");
        huifuAccountInfo = (UserRMBAccountInfo)intent.getSerializableExtra("hfUserRMBAccountInfo");
        yjbInterestBaseInfo = (BaseInfo)intent.getSerializableExtra("yjbBaseInfo");
        loadingDialog = mLoadingDialog;
        fragmentManager = getSupportFragmentManager();
        findViews();
    }

    private void findViews(){
        topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
        topLeftBtn.setOnClickListener(this);
        topTitleTV = (TextView)findViewById(R.id.common_page_title);
        topTitleTV.setText("�˻�����");

        floatImg = (ImageView) findViewById(R.id.account_center_activity_float_image);
        floatImg.setOnClickListener(this);
        zhzcTabBtn = (Button)findViewById(R.id.account_center_activity_tab_zhzc);
        zhzcTabBtn.setOnClickListener(this);
        hkrlTabBtn = (Button)findViewById(R.id.account_center_activity_tab_hkrl);
        hkrlTabBtn.setOnClickListener(this);
        onNavBtnOnClick(zhzcTabBtn);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_topbar_left_layout:
                finish();
                break;
            case R.id.account_center_activity_tab_zhzc:
            case R.id.account_center_activity_tab_hkrl:
                onNavBtnOnClick(v);
                break;
            case R.id.account_center_activity_float_image:
                floatImg.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    public UserRMBAccountInfo getYLAccountInfo(){
        return yllianAccountInfo;
    }

    public UserRMBAccountInfo getHFAccountInfo(){
        return huifuAccountInfo;
    }

    public BaseInfo getYJBInterestInfo(){
        return yjbInterestBaseInfo;
    }

    @Override
    protected void onResume() {
        super.onResume();
        UMengStatistics.statisticsResume(this);//����ͳ��
    }

    @Override
    protected void onPause() {
        super.onPause();
        UMengStatistics.statisticsPause(this);//����ͳ��
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fragmentManager.beginTransaction().remove(zhzcFragment);
        fragmentManager.beginTransaction().remove(hkrlFragment);
        fragmentManager = null;
        zhzcFragment = null;
        hkrlFragment = null;
        finish();
    }

    Fragment zhzcFragment;
    Fragment hkrlFragment;
    public FragmentManager fragmentManager;
    FragmentTransaction trasection;
    private void onNavBtnOnClick(View v){
        trasection = fragmentManager.beginTransaction();
        switch (v.getId()) {
            case R.id.account_center_activity_tab_zhzc:
                //�˻��ʲ�
                String flag1 = (String) v.getTag();
                if("0".equals(flag1)){
                    zhzcTabBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_blue_left_3dp);
                    zhzcTabBtn.setTextColor(getResources().getColor(R.color.white));
                    zhzcTabBtn.setTag("1");
                    hkrlTabBtn.setBackgroundResource(R.color.transparent);
                    hkrlTabBtn.setTag("0");
                    hkrlTabBtn.setTextColor(getResources().getColor(R.color.common_topbar_bg_color));
                    zhzcFragment = fragmentManager.findFragmentByTag("zhzctab");
                    if(zhzcFragment == null){
                        zhzcFragment = new AccountCenterZHZCFragment();
                        trasection.add(R.id.account_center_activity_mainlayout, zhzcFragment,"zhzctab");
                    }else{
                        hidFragments();
                        trasection.show(zhzcFragment);
                    }
                    trasection.commit();
                }
                break;
            case R.id.account_center_activity_tab_hkrl:
                String flag2 = (String) v.getTag();
                if("0".equals(flag2)){
                    zhzcTabBtn.setBackgroundResource(R.color.transparent);
                    zhzcTabBtn.setTextColor(getResources().getColor(R.color.common_topbar_bg_color));
                    zhzcTabBtn.setTag("0");
                    hkrlTabBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_blue_right_3dp);
                    hkrlTabBtn.setTag("1");
                    hkrlTabBtn.setTextColor(getResources().getColor(R.color.white));
                    boolean isFloat = SettingsManager.getAccountCenterFloatFlag(getApplicationContext());
                    if(!isFloat){
                        floatImg.setVisibility(View.VISIBLE);
                        SettingsManager.setAccountCenterFloatFlag(getApplicationContext(),true);
                    }
                    hkrlFragment = fragmentManager.findFragmentByTag("hkrltab");
                    if(hkrlFragment == null){
                        hkrlFragment = new AccountCenterHKRLFragment();
                        trasection.add(R.id.account_center_activity_mainlayout, hkrlFragment,"hkrltab");
                    }else{
                        hidFragments();
                        trasection.show(hkrlFragment);
                    }
                    trasection.commit();
                }
                break;
            default:
                break;
        }
    }

    private void hidFragments(){
        if(zhzcFragment != null){
            trasection.hide(zhzcFragment);
        }
        if(hkrlFragment != null){
            trasection.hide(hkrlFragment);
        }
    }
}
