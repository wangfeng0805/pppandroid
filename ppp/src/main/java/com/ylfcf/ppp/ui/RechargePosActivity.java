package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncTLOrder;
import com.ylfcf.ppp.async.AsyncUserSelectOne;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.TLOrderInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.util.YLFLogger;
import com.ylfcf.ppp.view.CommonPopwindow;
import com.ylfcf.ppp.widget.AuthImageView;

/**
 * pos����ֵҳ��
 * Created by Administrator on 2018/1/15.
 */

public class RechargePosActivity extends BaseActivity implements View.OnClickListener {
    private static final String className = "RechargePosActivity";

    private static final int REQUEST_USERINFO_WHAT = 1000;
    private static final int REQUEST_USERINFO_SUC = 1001;
    private static final int DECIMAL_DIGITS = 1;//С����λ��

    private static final int REQUEST_ORDER_WHAT = 1002;
    private static final int REQUEST_ORDER_SUC = 1003;

    private LinearLayout mainLayout;
    private UserInfo userInfo;
    private AuthImageView authImageView;
    private LinearLayout topLeftBtn;
    private TextView topTitleTV;
    private String authString;
    private EditText moneyET;
    private EditText authET;
    private TextView promptTV;
    private TextView topPromptTV;
    private Button paycodeBtn;//������ɸ�����
    private TextView catRechargeRecord,catOperation;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REQUEST_USERINFO_WHAT:
                    requestUserInfo(SettingsManager.getUserId(getApplicationContext()),"");
                    break;
                case REQUEST_USERINFO_SUC:

                    break;
                case REQUEST_ORDER_WHAT:
                    double amountD = (Double) msg.obj;
                    requestTLOrderNum(SettingsManager.getUserId(getApplicationContext()),
                            amountD,"android");
                    break;
                case REQUEST_ORDER_SUC:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.recharge_pos_activity_layout);
        findViews();
        handler.sendEmptyMessageDelayed(REQUEST_USERINFO_WHAT,200L);
    }

    private void findViews(){
        topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
        topLeftBtn.setOnClickListener(this);
        topTitleTV = (TextView) findViewById(R.id.common_page_title);
        topTitleTV.setText("POS��ֵ");

        mainLayout = (LinearLayout) findViewById(R.id.recharge_pos_activity_mainlayout);
        moneyET = (EditText) findViewById(R.id.recharge_pos_activity_money_et);
        moneyET.addTextChangedListener(watcherRechargeMoney);
        topPromptTV = (TextView) findViewById(R.id.recharge_pos_activity_prompt);
        promptTV = (TextView) findViewById(R.id.recharge_pos_activity_prompt_tv);
        authET = (EditText) findViewById(R.id.recharge_pos_activity_sms_et);
        paycodeBtn = (Button) findViewById(R.id.recharge_pos_activity_fukuanma_btn);
        paycodeBtn.setOnClickListener(this);
        catRechargeRecord = (TextView) findViewById(R.id.recharge_pos_activity_catrecord_tv);
        catRechargeRecord.setOnClickListener(this);
        catOperation = (TextView) findViewById(R.id.recharge_pos_activity_catoperation_tv);
        catOperation.setOnClickListener(this);
        authImageView = findViewById(R.id.recharge_pos_activity_auth_iv);
        authImageView.setOnClickListener(this);
        authString = getResponseStr(authImageView.getValidataAndSetImage(getRandomInteger()));
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


    // ��ȡ1~9��4�������
    private String[] getRandomInteger() {
        String[] reuestArray = new String[4];
        for (int i = 0; i < 4; i++) {
            reuestArray[i] = String.valueOf((int) (Math.random() * 9 + 1));
        }
        return reuestArray;
    }

    // ��ȡ���ص�����
    private String getResponseStr(String[] response) {
        StringBuffer stringBuffer = new StringBuffer();
        for (String str : response) {
            stringBuffer.append(str);
        }
        return stringBuffer.toString();
    }

    private TextWatcher watcherRechargeMoney = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > DECIMAL_DIGITS) {
                    s = s.toString().subSequence(0,
                            s.toString().indexOf(".") + DECIMAL_DIGITS+1);
                    moneyET.setText(s);
                    moneyET.setSelection(s.length());
                }
            }
            //�û�ֱ�����롰.�������
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                moneyET.setText(s);
                moneyET.setSelection(2);
            }
            if (s.toString().startsWith("0")
                    && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(".")) {
                    moneyET.setText(s.subSequence(1, 2));
                    moneyET.setSelection(1);
                }
            }
            if(s.toString().endsWith(".")){
                updateRechargePrompt(s.toString().replace(".",""));
            }else{
                updateRechargePrompt(s.toString());
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String rechargeS = s.toString();
            double rechargeD = 0d;
            try{
                rechargeD = Double.parseDouble(rechargeS);
            }catch (Exception e){
                e.printStackTrace();
            }
            if(rechargeD > 300){
                moneyET.setText("300");
                moneyET.setSelection(3);
            }
        }
    };

    /**
     * ���³�ֵ�����ʾ
     * @param s
     */
    private void updateRechargePrompt(String s){
        YLFLogger.d("pos��ֵ��"+s);
        double d = 0d;
        try{
            d = Double.parseDouble(s);
        }catch (Exception e){
        }
        if(d <= 0){
            promptTV.setVisibility(View.GONE);
            return;
        }
        promptTV.setVisibility(View.VISIBLE);
        if(s.contains(".")){
            //��С��������
            String[] sArr = s.split("\\.");
            YLFLogger.d("�ָ���"+sArr[0]+"|"+sArr[1]);
            int sArr1 = 0;
            int sArr2 = 0;
            try{
                sArr1 = Integer.parseInt(sArr[0]);
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                sArr2 = Integer.parseInt(sArr[1]);
            }catch (Exception e){
                e.printStackTrace();
            }
            if(sArr1 <= 0){
                promptTV.setText("��ֵ��"+sArr2+"ǧԪ");
            }else{
                if(sArr2 <= 0){
                    promptTV.setText("��ֵ��"+sArr1+"��Ԫ");
                }else{
                    promptTV.setText("��ֵ��"+sArr1+"����"+sArr2+"ǧԪ");
                }
            }
        }else{
            //����С��������
            promptTV.setText("��ֵ��"+(int)d+"��Ԫ");
        }
    }

    private void initUserInfo(UserInfo userInfo){
        topPromptTV.setText("��ȷ��ʹ��POS��"+userInfo.getReal_name()+"��Ԫ�����˻���ֵ�����������ֵ��������ť���ɸ����롣");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.common_topbar_left_layout:
                finish();
                break;
            case R.id.recharge_pos_activity_auth_iv:
                authString = getResponseStr(authImageView.getValidataAndSetImage(getRandomInteger()));
                break;
            case R.id.recharge_pos_activity_fukuanma_btn:
                //������ɸ�����
                checkRechargeData();
                break;
            case R.id.recharge_pos_activity_catrecord_tv:
                //�鿴��ֵ��¼
                Intent intent = new Intent(RechargePosActivity.this,RechargeRecordActivity.class);
                startActivity(intent);
                break;
            case R.id.recharge_pos_activity_catoperation_tv:
                //�鿴ʹ��˵��
                Intent intentInstructions = new Intent(RechargePosActivity.this,RechargePosInstructionsActivity.class);
                startActivity(intentInstructions);
                break;
        }
    }

    /**
     * ����ֵ���
     */
    private void checkRechargeData(){
        double rechargeMoneyD = 0d;
        String rechargeMoneyS = moneyET.getText().toString();
        String authStr = authET.getText().toString();
        try{
            rechargeMoneyD = Double.parseDouble(rechargeMoneyS);
        }catch (Exception e){

        }
        if(rechargeMoneyS == null || "".endsWith(rechargeMoneyS)){
            //�������ֵ���
            showRechargePrompt("","�������ֵ���");
        }
        else if(rechargeMoneyD < 5 || rechargeMoneyD > 300){
            //��������ȷ�ĳ�ֵ���
            showRechargePrompt("","��������ȷ�ĳ�ֵ���");
        }
        else if(!authStr.equals(authString)){
            //��������ȷ����֤��
            showRechargePrompt("","��������ȷ����֤��");
        }else{
            Message msg = handler.obtainMessage(REQUEST_ORDER_WHAT);
            msg.obj = rechargeMoneyD*10000;
            handler.sendMessage(msg);
        }
    }

    /**
     * �û��������ҳ�����Ҫʵ����֤����ʾ
     * @param rechargeType ��ֵ���� kjcz:��ݳ�ֵ pos:pos����ֵ
     */
    private void showRechargePrompt(final String rechargeType,String contentStr){
        View popView = LayoutInflater.from(this).inflate(R.layout.common_popwindow, null);
        int[] screen = SettingsManager.getScreenDispaly(RechargePosActivity.this);
        int width = screen[0]*4/5;
        int height = screen[1]*1/5;
        CommonPopwindow popwindow = new CommonPopwindow(RechargePosActivity.this,popView, width, height,rechargeType,contentStr,
                null);
        popwindow.show(mainLayout);
    }

    /**
     *��ȡͨ�����׶�����
     * @param userId
     * @param amount
     * @param from
     */
    private void requestTLOrderNum(String userId,final double amount,String from){
        if(mLoadingDialog != null){
            mLoadingDialog.show();
        }
        AsyncTLOrder orderTask = new AsyncTLOrder(RechargePosActivity.this, userId,
                String.valueOf(amount), from, new Inter.OnCommonInter() {
            @Override
            public void back(BaseInfo baseInfo) {
                if(mLoadingDialog != null && mLoadingDialog.isShowing()){
                    mLoadingDialog.dismiss();
                }
                if(baseInfo != null){
                    int resultCode = SettingsManager.getResultCode(baseInfo);
                    if(resultCode == 0){
                        TLOrderInfo orderInfo = baseInfo.getTlOrderInfo();
                        Intent intent = new Intent(RechargePosActivity.this,RechargePosQRCodeActivity.class);
                        intent.putExtra("amount",amount);
                        intent.putExtra("userInfo",userInfo);
                        intent.putExtra("orderInfo",orderInfo);
                        startActivity(intent);
                        finish();
                    }else{
                        Util.toastLong(RechargePosActivity.this,baseInfo.getMsg());
                    }
                }
            }
        });
        orderTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    /**
     * ��ȡ�û���Ϣ
     * @param userId
     * @param phone
     */
    private void requestUserInfo(final String userId,String phone){
        if(mLoadingDialog != null){
            mLoadingDialog.show();
        }
        AsyncUserSelectOne userTask = new AsyncUserSelectOne(RechargePosActivity.this, userId, phone, "","",
                new Inter.OnGetUserInfoByPhone() {
                    @Override
                    public void back(BaseInfo baseInfo) {
                        if(mLoadingDialog != null && mLoadingDialog.isShowing()){
                            mLoadingDialog.dismiss();
                        }
                        if(baseInfo != null){
                            int resultCode = SettingsManager.getResultCode(baseInfo);
                            if(resultCode == 0){
                                userInfo = baseInfo.getUserInfo();
                                initUserInfo(userInfo);
                            }
                        }
                    }
                });
        userTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }
}
