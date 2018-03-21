package com.ylfcf.ppp.ui;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;

/**
 * POSʹ��˵��
 * Created by Administrator on 2018/1/22.
 */

public class RechargePosInstructionsActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout topLeftBtn;
    private TextView topTitleTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.recharge_pos_instructions_activity);
        findViews();
    }

    private void findViews(){
        topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
        topLeftBtn.setOnClickListener(this);
        topTitleTV = (TextView) findViewById(R.id.common_page_title);
        topTitleTV.setText("POSʹ��˵��");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.common_topbar_left_layout:
                finish();
                break;
        }
    }
}
