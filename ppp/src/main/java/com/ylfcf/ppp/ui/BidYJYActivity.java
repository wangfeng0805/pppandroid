package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncYJYBorrowInvest;
import com.ylfcf.ppp.async.AsyncYiLianRMBAccount;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.UserRMBAccountInfo;
import com.ylfcf.ppp.inter.Inter.OnBorrowInvestInter;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnIsBindingListener;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;

import java.text.DecimalFormat;

/**
 * Ԫ��ӯͶ��ҳ��
 * Created by Administrator on 2017/7/21.
 */

public class BidYJYActivity extends BaseActivity implements View.OnClickListener {
    private static final String className = "BidYJYActivity";
    private static final int REQUEST_INVEST_WHAT = 1201;
    private static final int REQUEST_INVEST_SUCCESS = 1202;
    private static final int REQUEST_INVEST_EXCEPTION = 1203;
    private static final int REQUEST_INVEST_FAILE = 1204;

    private LinearLayout topLeftBtn;
    private TextView topTitleTV, borrowName;
    private TextView userBalanceTV;// �û��������
    private TextView borrowBalanceTV;// ���ʣ���Ͷ���
    private Button rechargeBtn;// ��ֵ

    private EditText investMoneyET;
    private ImageView deleteImg;// x��
    private TextView yjsyText;// Ԥ������
    private CheckBox compactCB;
    private TextView vipCompact;//VIP���Э��

    private ProductInfo mProductInfo;
    private int moneyInvest = 0;
    private Button investBtn;

    private LinearLayout mainLayout;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_INVEST_WHAT:
                    requestInvest(mProductInfo.getId(),
                            SettingsManager.getUserId(getApplicationContext()),
                            String.valueOf(moneyInvest),
                            SettingsManager.USER_FROM);
                    break;
                case REQUEST_INVEST_SUCCESS:
                    Intent intentSuccess = new Intent(BidYJYActivity.this,
                            BidSuccessActivity.class);
                    intentSuccess.putExtra("from_where", "Ԫ��ӯ");
                    startActivity(intentSuccess);
                    mApp.finishAllActivityExceptMain();
                    break;
                case REQUEST_INVEST_EXCEPTION:
                    BaseInfo base = (BaseInfo) msg.obj;
                    Util.toastShort(BidYJYActivity.this, base.getMsg());
                    break;
                case REQUEST_INVEST_FAILE:
                    Util.toastShort(BidYJYActivity.this, "�����쳣");
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
        setContentView(R.layout.bidyjy_activity);
        mProductInfo = (ProductInfo) getIntent().getSerializableExtra(
                "PRODUCT_INFO");
        findViews();
        initInvestBalance(mProductInfo);
    }

    private void findViews(){
        topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
        topLeftBtn.setOnClickListener(this);
        topTitleTV = (TextView) findViewById(R.id.common_page_title);
        topTitleTV.setText("Ͷ��");

        borrowName = (TextView) findViewById(R.id.bid_yjy_activity_borrow_name);
        borrowName.setText(mProductInfo.getBorrow_name());
        userBalanceTV = (TextView) findViewById(R.id.bid_yjy_activity_user_balance);
        borrowBalanceTV = (TextView) findViewById(R.id.bid_yjy_activity_borrow_balance);
        rechargeBtn = (Button) findViewById(R.id.bid_yjy_activity_recharge_btn);
        rechargeBtn.setOnClickListener(this);
        compactCB = (CheckBox) findViewById(R.id.bid_yjy_activity_cb);
        vipCompact = (TextView) findViewById(R.id.bid_yjy_activity_compact_text);
        vipCompact.setOnClickListener(this);
        investMoneyET = (EditText) findViewById(R.id.bid_yjy_activity_invest_et);
        investMoneyET.setEnabled(false);

        deleteImg = (ImageView) findViewById(R.id.bid_yjy_activity_delete);
        deleteImg.setOnClickListener(this);
        yjsyText = (TextView) findViewById(R.id.bid_yjy_activity_yjsy);
        investBtn = (Button) findViewById(R.id.bid_yjy_activity_borrow_bidBtn);
        investBtn.setOnClickListener(this);
        mainLayout = (LinearLayout) findViewById(R.id.bid_yjy_activity_mainlayout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UMengStatistics.statisticsOnPageStart(className);//����ͳ��ҳ����ת
        UMengStatistics.statisticsResume(this);//����ͳ��ʱ��
        requestUserAccountInfo(SettingsManager
                .getUserId(getApplicationContext()));
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
     * ���ʣ���Ͷ���
     * @param info
     */
    float extraRateF = 0f;
    int borrowBalanceTemp = 0;
    private void initInvestBalance(ProductInfo info) {
        if (info == null) {
            return;
        }

        double totalMoneyL = 0d;
        int totalMoneyI = 0;
        double investMoneyL = 0d;
        int investMoneyI = 0;
        int borrowBalance = 0;
        try {
            totalMoneyL = Double.parseDouble(info.getTotal_money());
            investMoneyL = Double.parseDouble(info.getInvest_money());
            totalMoneyI = (int) totalMoneyL;
            investMoneyI = (int) investMoneyL;
            borrowBalance = totalMoneyI - investMoneyI;
            borrowBalanceTemp = borrowBalance;
        } catch (Exception e) {
        }
        try {
            extraRateF = Float.parseFloat(info.getAndroid_interest_rate());
        } catch (Exception e) {
        }
        borrowBalanceTV.setText(Util.commaSpliteData(String
                .valueOf(borrowBalance)));
        //Ĭ���Ǳ��ʣ���Ͷ���
        investMoneyET.setText(String
                .valueOf(borrowBalance));
        computeIncome(mProductInfo.getInterest_rate(),
                mProductInfo.getAndroid_interest_rate(), "",borrowBalance,
                mProductInfo.getInvest_period());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_topbar_left_layout:
                finish();
                break;
            case R.id.bid_yjy_activity_borrow_bidBtn:
                borrowInvest();
                break;
            case R.id.bid_yjy_activity_recharge_btn:
                //ȥ��ֵ
                if(SettingsManager.isPersonalUser(getApplicationContext())){
                    checkIsVerify("��ֵ");
                }else if(SettingsManager.isCompanyUser(getApplicationContext())){
                    Intent intent = new Intent(BidYJYActivity.this,RechargeCompActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.bid_yjy_activity_delete:
//                resetInvestMoneyET();
                break;
            case R.id.bid_yjy_activity_compact_text:
                //�鿴��ͬ
                Intent intent = new Intent(BidYJYActivity.this,CompactActivity.class);
                intent.putExtra("from_where", "yjy");
                intent.putExtra("mProductInfo", mProductInfo);
                startActivity(intent);
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
        rechargeBtn.setEnabled(false);
        RequestApis.requestIsVerify(BidYJYActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
            @Override
            public void isVerify(boolean flag, Object object) {
                if(flag){
                    //�û��Ѿ�ʵ��
                    checkIsBindCard(type);
                }else{
                    //�û�û��ʵ��
                    Intent intent = new Intent(BidYJYActivity.this,UserVerifyActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("type", type);
                    intent.putExtra("bundle", bundle);
                    startActivity(intent);
                    rechargeBtn.setEnabled(true);
                }
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
        RequestApis.requestIsBinding(BidYJYActivity.this, SettingsManager.getUserId(getApplicationContext()), "����", new OnIsBindingListener() {
            @Override
            public void isBinding(boolean flag, Object object) {
                Intent intent = new Intent();
                if(flag){
                    //�û��Ѿ���
                    if("��ֵ".equals(type)){
                        //��ôֱ��������ֵҳ��
                        intent.setClass(BidYJYActivity.this, RechargeActivity.class);
                    }
                }else{
                    //�û���û�а�
                    Bundle bundle = new Bundle();
                    bundle.putString("type", type);
                    intent.putExtra("bundle", bundle);
                    intent.setClass(BidYJYActivity.this, BindCardActivity.class);
                }
                startActivity(intent);
                rechargeBtn.setEnabled(true);
            }
        });
    }

    //Ͷ��ǰ���ж�
    private void borrowInvest() {
        String moneyStr = investMoneyET.getText().toString();
        double borrowBalanceDouble = 0d;//���ʣ���Ͷ���
        try {
            moneyInvest = Integer.parseInt(moneyStr);
        } catch (Exception e) {
        }
        // �ж�Ͷ�ʽ���Ƿ�����˻����
        String borrowBalance = String.valueOf(borrowBalanceTemp);
        try {
            borrowBalanceDouble = Double.parseDouble(borrowBalance);
        } catch (Exception e) {
        }
        // ��Ŀ�Ͷ���С��30W�������û�һ����Ͷ��
        if (moneyInvest < borrowBalanceDouble) {
            // ��ʾ�û���1����Ͷ��
            Util.toastLong(BidYJYActivity.this, "Ͷ�����С����Ͷ���");
        } else if (moneyInvest > borrowBalanceDouble) {
            Util.toastLong(BidYJYActivity.this, "���ʣ���Ͷ����");
        } else if (!compactCB.isChecked()) {
            Util.toastLong(BidYJYActivity.this, "�����Ķ���ͬ���ƷЭ��");
        } else {
            showInvestDialog();
        }
    }

    /**
     * ȷ��Ͷ�ʵ�dialog
     */
    private void showInvestDialog() {
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.invest_prompt_layout, null);
        Button sureBtn = (Button) contentView
                .findViewById(R.id.invest_prompt_layout_surebtn);
        Button cancelBtn = (Button) contentView
                .findViewById(R.id.invest_prompt_layout_cancelbtn);
        TextView totalMoney = (TextView) contentView
                .findViewById(R.id.invest_prompt_layout_total);
        LinearLayout detailLayout = (LinearLayout) contentView.findViewById(R.id.invest_prompt_yjb_layout_detail);
        RelativeLayout hbLayout = (RelativeLayout) contentView.findViewById(R.id.invest_prompt_hb_layout_detail);//�������
        TextView hbMoneyTV = (TextView) contentView
                .findViewById(R.id.invest_prompt_layout_hb_count);
        detailLayout.setVisibility(View.GONE);
        totalMoney.setText(moneyInvest + "");

        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                R.style.Dialog_Transparent); // �ȵõ�������
        builder.setView(contentView);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(REQUEST_INVEST_WHAT);
                dialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // ��������������ˣ���������ʾ����
        dialog.show();
        // ����dialog�Ŀ��
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth() * 6 / 7;
        dialog.getWindow().setAttributes(lp);
    }

    private void resetInvestMoneyET() {
        if (investMoneyET != null) {
            investMoneyET.setText(null);
            yjsyText.setText("0.00");
        }
    }

    /**
     * �����껯�ʺ�Ͷ�ʽ���������
     */
    private String computeIncome(String rateStr, String extraRateStr,String floatRate,
                                 int investMoney, String daysStr) {
        float rateF = 0f;
        float extraRateF = 0f;
        float floatRateF = 0f;
        int days = 0;
        try {
            rateF = Float.parseFloat(rateStr);
            days = Integer.parseInt(daysStr);
        } catch (Exception e) {
        }
        try {
            extraRateF = Float.parseFloat(extraRateStr);
        } catch (Exception e) {
        }
        try{
            floatRateF = Float.parseFloat(floatRate);
        }catch (Exception e){
            e.printStackTrace();
        }
        float income = 0f;
        income = (rateF + extraRateF + floatRateF) * investMoney * days / 36500;
        DecimalFormat df = new java.text.DecimalFormat("#.00");
        if (income < 1) {
            yjsyText.setText("0" + df.format(income));
        } else {
            yjsyText.setText(df.format(income));
        }

        return df.format(income);
    }

    /**
     * ��������Ͷ�ʽӿ�
     *
     * @param borrowId
     * @param investUserId
     * @param money
     */
    private void requestInvest(String borrowId, String investUserId,
                               String money, String investFrom) {
        if (mLoadingDialog != null && !isFinishing()) {
            mLoadingDialog.show();
        }
        AsyncYJYBorrowInvest asyncBorrowInvest = new AsyncYJYBorrowInvest(
                BidYJYActivity.this, borrowId, investUserId, money,investFrom,new OnBorrowInvestInter() {
            @Override
            public void back(BaseInfo baseInfo) {
                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    mLoadingDialog.dismiss();
                }

                if (baseInfo != null) {
                    int resultCode = SettingsManager
                            .getResultCode(baseInfo);
                    if (resultCode == 0) {
                        Message msg = handler.obtainMessage(REQUEST_INVEST_SUCCESS);
                        msg.obj = baseInfo;
                        handler.sendMessage(msg);
                    } else {
                        Message msg = handler.obtainMessage(REQUEST_INVEST_EXCEPTION);
                        msg.obj = baseInfo;
                        handler.sendMessage(msg);
                    }
                } else {
                    Message msg = handler.obtainMessage(REQUEST_INVEST_FAILE);
                    handler.sendMessage(msg);
                }
            }
        });
        asyncBorrowInvest.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    /**
     * �û��˻���Ϣ
     */
    private void requestUserAccountInfo(String userId) {
        AsyncYiLianRMBAccount yilianTask = new AsyncYiLianRMBAccount(
                BidYJYActivity.this, userId, new OnCommonInter() {
            @Override
            public void back(BaseInfo baseInfo) {
                if (baseInfo != null) {
                    int resultCode = SettingsManager
                            .getResultCode(baseInfo);
                    if (resultCode == 0) {
                        UserRMBAccountInfo info = baseInfo
                                .getRmbAccountInfo();
                        userBalanceTV.setText(info.getUse_money());
                    }
                }
            }
        });
        yilianTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }
}
