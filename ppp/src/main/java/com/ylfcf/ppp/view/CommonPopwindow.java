package com.ylfcf.ppp.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.ui.RechargeChooseActivity;

/**
 * 一个通用的弹出框
 * 上部为文字描述，下部为“我知道了”按钮
 * @author Mr.liu
 * 
 */
public class CommonPopwindow extends PopupWindow implements
		OnClickListener{

	private Button okBtn;
	private TextView content;
	private Activity context;
	private WindowManager.LayoutParams lp = null;
	private String type;
	private String contentStr;
	private RechargeChooseActivity.OKBtnListener okBtnListener = null;
	
	public CommonPopwindow(Context context) {
		super(context);
	}

	public CommonPopwindow(Context context, View convertView,
			int width, int height,String type,String contentStr,RechargeChooseActivity.OKBtnListener okBtnListener) {
		super(convertView, width, height);
		this.context = (Activity) context;
		this.type = type;
		this.contentStr = contentStr;
		this.okBtnListener = okBtnListener;
		findViews(convertView);
	}

	private void findViews(View popView) {
		lp = context.getWindow().getAttributes();
		lp.alpha = 0.4f;
		context.getWindow().setAttributes(lp);
		okBtn = (Button) popView.findViewById(R.id.common_popwindow_btn);
		okBtn.setOnClickListener(this);
		content = (TextView) popView.findViewById(R.id.common_popwindow_content);
		if("实名认证".equals(type)){
			content.setText("如需进行下一步操作，请先进行实名认证。");
		}else if("绑卡".equals(type)){
			content.setText("如需进行下一步操作，请先进行绑卡。");
		}else if("设置提现密码".equals(type)){
			content.setText("如需进行下一步操作，请先设置提现密码。");
		}else{
			content.setText(contentStr);
		}
		content.post(new Runnable() {
			@Override
			public void run() {
				if(content.getLineCount() == 1){
					content.setGravity(Gravity.CENTER);
				}else{
					content.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
				}
			}
		});
	}

	@Override
	public void dismiss() {
		super.dismiss();
		lp.alpha = 1.0f;
		context.getWindow().setAttributes(lp);
	}

	public void show(View parentView) {
		ColorDrawable cd = new ColorDrawable(0x000000);
		this.setBackgroundDrawable(cd);// 使得返回键有效 并且去除popupwindow圆角的黑色背景
		this.setAnimationStyle(R.style.rechargeMsgPopwindowStyle);
		this.setOutsideTouchable(false);
		this.setFocusable(true);
		this.showAtLocation(parentView, Gravity.CENTER, 0, 0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_popwindow_btn:
			if(okBtnListener != null)
				okBtnListener.back();
			this.dismiss();
			break;
		default:
			break;
		}
	}
}
