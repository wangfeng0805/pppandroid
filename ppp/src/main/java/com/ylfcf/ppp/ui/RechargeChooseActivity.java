package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.view.CommonPopwindow;

/**
 * ѡ���ֵ����
 * Created by Administrator on 2018/1/15.
 */

public class RechargeChooseActivity extends BaseActivity implements View.OnClickListener{
    private static final String className = "RechargeChooseActivity";

    private static final int REQUEST_VERIFY_WHAT = 2654;
    private static final int REQUEST_BINDCARD_WHAT = 2655;


    private LinearLayout mainLayout;
    private LinearLayout topLeftBtn;
    private TextView topTitleTV;

    private ImageView kjczIV,posIV;

    private boolean isVerify = false;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REQUEST_VERIFY_WHAT:
                    checkIsVerify();
                    break;
                case REQUEST_BINDCARD_WHAT:
                    checkIsBindCard("��ֵ");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.recharge_choose_activity);
        findViews();

        if(mLoadingDialog != null){
            mLoadingDialog.show();
        }
        handler.sendEmptyMessageDelayed(REQUEST_VERIFY_WHAT,500L);
    }

    private void findViews(){
        topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
        topLeftBtn.setOnClickListener(this);
        topTitleTV = (TextView) findViewById(R.id.common_page_title);
        topTitleTV.setText("ѡ���ֵ����");

        mainLayout = (LinearLayout) findViewById(R.id.recharge_choose_activity_mainlayout);
        kjczIV = (ImageView) findViewById(R.id.recharge_choose_activity_kjzf);
        kjczIV.setOnClickListener(this);
        posIV = (ImageView) findViewById(R.id.recharge_choose_activity_pos);
        posIV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.common_topbar_left_layout:
                finish();
                break;
            case R.id.recharge_choose_activity_kjzf:
                //��ݳ�ֵ
                if(isVerify){
                    handler.sendEmptyMessage(REQUEST_BINDCARD_WHAT);
                }else{
                    showVerifyPrompt("kjcz");
                }
                break;
            case R.id.recharge_choose_activity_pos:
                //pos��ֵ
                if(isVerify){
                    Intent intent = new Intent(RechargeChooseActivity.this,RechargePosActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    showVerifyPrompt("pos");
                }
                break;
        }
    }

    /**
     * �û��������ҳ�����Ҫʵ����֤����ʾ
     * @param rechargeType ��ֵ���� kjcz:��ݳ�ֵ pos:pos����ֵ
     */
    private void showVerifyPrompt(final String rechargeType){
        View popView = LayoutInflater.from(this).inflate(R.layout.common_popwindow, null);
        int[] screen = SettingsManager.getScreenDispaly(RechargeChooseActivity.this);
        int width = screen[0]*4/5;
        int height = screen[1]*1/5;
        CommonPopwindow popwindow = new CommonPopwindow(RechargeChooseActivity.this,popView, width, height,"ʵ����֤","",
                new OKBtnListener(){
                    @Override
                    public void back() {
                        Intent intent = new Intent(RechargeChooseActivity.this,UserVerifyActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("type","��ֵ");
                        bundle.putString("recharge_type",rechargeType);
                        intent.putExtra("bundle",bundle);
                        startActivity(intent);
                        finish();
                    }
                });
        popwindow.show(mainLayout);
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

    /**
     * ��֤�û��Ƿ��Ѿ���֤
     */
    private void checkIsVerify(){
        RequestApis.requestIsVerify(RechargeChooseActivity.this, SettingsManager.getUserId(this), new Inter.OnIsVerifyListener() {
            @Override
            public void isVerify(boolean flag, Object object) {
                if(mLoadingDialog != null && mLoadingDialog.isShowing()){
                    mLoadingDialog.dismiss();
                }
                isVerify = flag;
            }

            @Override
            public void isSetWithdrawPwd(boolean flag, Object object) {
                //�û��Ƿ��Ѿ�������������
            }
        });
    }

    /**
     * �ж��û��Ƿ��Ѿ���
     * @param type "��ֵ","����","�����н�"
     */
    private void checkIsBindCard(final String type){
        if(mLoadingDialog != null){
            mLoadingDialog.show();
        }
        RequestApis.requestIsBinding(RechargeChooseActivity.this,
                SettingsManager.getUserId(RechargeChooseActivity.this), "����", new Inter.OnIsBindingListener() {
            @Override
            public void isBinding(boolean flag, Object object) {
                if(mLoadingDialog != null && mLoadingDialog.isShowing()){
                    mLoadingDialog.dismiss();
                }
                Intent intent = new Intent();
                if(flag){
                    //�û��Ѿ���
                    if("��ֵ".equals(type)){
                        //��ôֱ��������ֵҳ��
                        intent.setClass(RechargeChooseActivity.this, RechargeActivity.class);
                    }
                    startActivity(intent);
                }else{
                    //�û���û�а�
                    Bundle bundle = new Bundle();
                    bundle.putString("type", type);
                    intent.putExtra("bundle", bundle);
                    intent.setClass(RechargeChooseActivity.this, BindCardActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        });
    }

    /**
     * ���popwindow��ȷ����ť�ļ���
     */
    public interface OKBtnListener{
        void back();
    }
}
