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
 * һ��ͨ�õĵ�����
 * �ϲ�Ϊ�����������²�Ϊ����֪���ˡ���ť
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
		if("ʵ����֤".equals(type)){
			content.setText("���������һ�����������Ƚ���ʵ����֤��");
		}else if("��".equals(type)){
			content.setText("���������һ�����������Ƚ��а󿨡�");
		}else if("������������".equals(type)){
			content.setText("���������һ�����������������������롣");
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
		this.setBackgroundDrawable(cd);// ʹ�÷��ؼ���Ч ����ȥ��popupwindowԲ�ǵĺ�ɫ����
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
