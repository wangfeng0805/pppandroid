package com.ylfcf.ppp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ylfcf.ppp.R;

public class InvestmentActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout mLl_back;
    private Button mBtn_entrance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment);
        mLl_back = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
        mBtn_entrance = (Button) findViewById(R.id.btn_borrow_money_entrance);
        if (mLl_back != null) {
            mLl_back.setOnClickListener(this);
        }
        if (mBtn_entrance != null) {
            mBtn_entrance.setOnClickListener(this);
        }

    }


    @Override
    public void onClick(View view) {
        switch(view.getId()) {
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
        }

    }

    /**
	 * ��֤�û��Ƿ��Ѿ���֤
	 * @param type ����ֵ��,�����֡�
	 */
	private void checkIsVerify(final String type){
		mBtn_entrance.setEnabled(false);
		com.ylfcf.ppp.util.RequestApis.requestIsVerify(InvestmentActivity.this, com.ylfcf.ppp.util.SettingsManager.getUserId(getApplicationContext()), new com.ylfcf.ppp.inter.Inter.OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(flag){
					//�û��Ѿ�ʵ��
					android.content.Intent intent = new android.content.Intent();
//					intent.putExtra("PRODUCT_INFO", mProductInfo);
					intent.setClass(InvestmentActivity.this, BidWDYActivity.class);
					startActivity(intent);
				}else{
					//�û�û��ʵ��
					android.content.Intent intent = new android.content.Intent(InvestmentActivity.this,UserVerifyActivity.class);
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
