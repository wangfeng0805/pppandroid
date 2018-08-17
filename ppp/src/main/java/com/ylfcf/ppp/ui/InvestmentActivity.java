package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncInvestmentDetail;
import com.ylfcf.ppp.async.AsyncInvestmentList;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.InvestmentDetail;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.Util;

public class InvestmentActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout     mLl_back;
    private Button           mBtn_entrance;
    private String           mId;
    private TextView         mTv_lilv;
    private TextView         mTv_jiezhi;
    private TextView         mTv_total_money;
    private TextView         mTv_surplus_times;
    private LinearLayout     mLl_borrow_money_info;
    private InvestmentDetail mInvestmentDetail;
    private TextView         mTv_surplus_money;
    private LinearLayout     mLl_expose_money_info;
    private LinearLayout     mLl_borrow_money_notes;
    private LinearLayout     mLl_borrow_money_agreement;
    private LinearLayout     mLl_borrow_money_problem;
    private static final int REQUEST_YXBFUNDSDETAILS_SUCCESS = 3502;
    private static final int REQUEST_YXBFUNDSDETAILS_NODATA  = 3503;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_YXBFUNDSDETAILS_SUCCESS:
                    mSv_detail_info.setVisibility(View.VISIBLE);
                    mTv_fund_detail.setVisibility(View.GONE);
                    break;
                case REQUEST_YXBFUNDSDETAILS_NODATA:
                    mSv_detail_info.setVisibility(View.GONE);
                    mTv_fund_detail.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    };
    private ScrollView mSv_detail_info;
    private TextView   mTv_fund_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment);
        mId = getIntent().getStringExtra("id");
        if (mId == null) {
            Util.toastShort(InvestmentActivity.this, "�����쳣");
            return;
        }
        initView();
        initData();
    }

    private void initView() {
        mLl_back = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
        mBtn_entrance = (Button) findViewById(R.id.btn_borrow_money_entrance);
        if (mLl_back != null) {
            mLl_back.setOnClickListener(this);
        }
        if (mBtn_entrance != null) {
            mBtn_entrance.setOnClickListener(this);
        }
        mTv_lilv = (TextView) findViewById(R.id.tv_lilv);
        mTv_jiezhi = (TextView) findViewById(R.id.tv_jiezhi);
        mTv_total_money = (TextView) findViewById(R.id.tv_total_money);
        mTv_surplus_times = (TextView) findViewById(R.id.tv_surplus_times);
        mTv_surplus_money = (TextView) findViewById(R.id.tv_surplus_money);

        mLl_expose_money_info = (LinearLayout) findViewById(R.id.ll_expose_money_info);
        mLl_borrow_money_info = (LinearLayout) findViewById(R.id.ll_borrow_money_info);
        mLl_borrow_money_notes = (LinearLayout) findViewById(R.id.ll_borrow_money_notes);
        mLl_borrow_money_agreement = (LinearLayout) findViewById(R.id.ll_borrow_money_agreement);
        mLl_borrow_money_problem = (LinearLayout) findViewById(R.id.ll_borrow_money_problem);

        mSv_detail_info = (ScrollView) findViewById(R.id.sv_detail_info);
        mTv_fund_detail = (TextView) findViewById(R.id.funds_details_yxb_nodata_text);

        mLl_expose_money_info.setOnClickListener(this);
        mLl_borrow_money_info.setOnClickListener(this);
        mLl_borrow_money_notes.setOnClickListener(this);
        mLl_borrow_money_agreement.setOnClickListener(this);
        mLl_borrow_money_problem.setOnClickListener(this);
    }

    private void initData() {
        AsyncInvestmentDetail investTask = new AsyncInvestmentDetail(InvestmentActivity.this, mId,
                new Inter.OnCommonInter() {
                    @Override
                    public void back(BaseInfo baseInfo) {
                        if (baseInfo != null) {
                            int resultCode = SettingsManager
                                    .getResultCode(baseInfo);
                            if (resultCode == 0) {
                                Message msg = handler.obtainMessage(REQUEST_YXBFUNDSDETAILS_SUCCESS);
                                msg.obj = baseInfo;
                                handler.sendMessage(msg);

                                mInvestmentDetail = baseInfo.getInvestmentDetail();
                                mTv_lilv.setText(mInvestmentDetail.getBorrow_interest());
                                String raising_period = mInvestmentDetail.getRaising_period();
                                String[] jiezhi = raising_period.split(" ");
                                mTv_jiezhi.setText("Ͷ���ֹʱ��: " + jiezhi[0]);
                                mTv_total_money.setText(mInvestmentDetail.getActual_loan_money());
                                int money = Integer.parseInt(mInvestmentDetail.getActual_loan_money()) - Integer.parseInt(mInvestmentDetail.getActual_raising_money());
                                mTv_surplus_money.setText(money + "");
                                mTv_surplus_times.setText(mInvestmentDetail.getBorrow_period());//10.22
                            }else {
                                Message msg = handler.obtainMessage(REQUEST_YXBFUNDSDETAILS_NODATA);
                                msg.obj = baseInfo;
                                handler.sendMessage(msg);
                            }
                        }else {
                            Message msg = handler.obtainMessage(REQUEST_YXBFUNDSDETAILS_NODATA);
                            msg.obj = baseInfo;
                            handler.sendMessage(msg);
                        }
                    }
                });
        investTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.common_topbar_left_layout:
                finish();
                break;
            case R.id.btn_borrow_money_entrance:
                // ��SettingsManager�ж�ȡ���룬���Ϊ����ζ��û�е�¼��
                boolean isLogin = !com.ylfcf.ppp.util.SettingsManager.getLoginPassword(
                        InvestmentActivity.this).isEmpty()
                        && !com.ylfcf.ppp.util.SettingsManager.getUser(InvestmentActivity.this)
                        .isEmpty();
                // 1������Ƿ��Ѿ���¼
                if (isLogin) {
                    //�ж��Ƿ�ʵ����
                    checkIsVerify("Ͷ��");
                } else {
                    // δ��¼����ת����¼ҳ��
                    android.content.Intent intent = new android.content.Intent();
                    intent.setClass(InvestmentActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_expose_money_info:
                //�����Ϣ��¶
                Intent intent1 = new Intent(this, ExposeMoneyInfoActivity.class);
                intent1.putExtra("detail_info", mInvestmentDetail);
                startActivity(intent1);
                break;
            case R.id.ll_borrow_money_info:
                //��ȫ����
                Intent intent2 = new Intent(this, BorrowInfoSafeActivity.class);
                intent2.putExtra("id", mId);
                startActivity(intent2);
                break;
            case R.id.ll_borrow_money_notes:
                //�����¼
                Intent intent3 = new Intent(this, LoanRecordActivity.class);
                intent3.putExtra("id", mId);
                startActivity(intent3);
                break;
            case R.id.ll_borrow_money_agreement:
                //���Э��

                break;
            case R.id.ll_borrow_money_problem:
                //��������

                break;

        }

    }

    /**
     * �ж��û��Ƿ��Ѿ���
     *
     * @param type "��ֵ����"
     */
    private void checkIsVerify(final String type) {
        mBtn_entrance.setEnabled(false);
        com.ylfcf.ppp.util.RequestApis.requestIsVerify(InvestmentActivity.this, com.ylfcf.ppp.util.SettingsManager.getUserId(getApplicationContext()), new com.ylfcf.ppp.inter.Inter.OnIsVerifyListener() {
            @Override
            public void isVerify(boolean flag, Object object) {
                if (flag) {
                    //�û��Ѿ���
                    android.content.Intent intent = new android.content.Intent();
                    intent.putExtra("PRODUCT_INFO", mInvestmentDetail);
                    intent.setClass(InvestmentActivity.this, BidSHYActivity.class);
                    startActivity(intent);
                } else {
                    //�û���û�а�
                    android.content.Intent intent = new android.content.Intent(InvestmentActivity.this, UserVerifyActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("type", type);
//					bundle.putSerializable("PRODUCT_INFO", mProductInfo);
                    intent.putExtra("bundle", bundle);
                    startActivity(intent);
                }
                mBtn_entrance.setEnabled(true);
            }

            @Override
            public void isSetWithdrawPwd(boolean flag, Object object) {
            }
        });
    }

}
