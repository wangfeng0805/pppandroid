package com.ylfcf.ppp.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.ui.WithdrawPwdGetbackActivity;

/**
 * ������������������ʾ����
 * @author Mr.liu
 *
 */
public class DealPwdErrorPopwindow extends PopupWindow implements
			OnClickListener{

	private TextView content;//������ʾ����
	private TextView text1;
	private LinearLayout helpLayout;//�����ת����绰ҳ��
	private Button repeatBtn,getbackPwdBtn;
	private Activity context;
	private WindowManager.LayoutParams lp = null;

	private String errorMsg;
	public DealPwdErrorPopwindow(Context context) {
		super(context);
	}

	public DealPwdErrorPopwindow(Context context, View convertView,
			int width, int height,String errorMsg) {
		super(convertView, width, height);
		this.context = (Activity) context;
		this.errorMsg = errorMsg;
		findViews(convertView);
	}

	private void findViews(View popView) {
		lp = context.getWindow().getAttributes();
		lp.alpha = 0.4f;
		context.getWindow().setAttributes(lp);
		repeatBtn = (Button) popView.findViewById(R.id.dealpwd_error_popwindow_repeat);
		repeatBtn.setOnClickListener(this);
		getbackPwdBtn = (Button) popView.findViewById(R.id.dealpwd_error_popwindow_getback_pwd);
		getbackPwdBtn.setOnClickListener(this);
		helpLayout = (LinearLayout) popView.findViewById(R.id.dealpwd_error_popwindow_layout);
		helpLayout.setOnClickListener(this);
		content = (TextView) popView.findViewById(R.id.dealpwd_error_popwindow_content);
		text1 = (TextView) popView.findViewById(R.id.dealpwd_error_popwindow_text1);
		if("0".equals(errorMsg)){
			content.setText("����������������������");
			repeatBtn.setVisibility(View.GONE);
			helpLayout.setVisibility(View.GONE);
			text1.setVisibility(View.VISIBLE);
		}else{
			content.setText("�����������������"+errorMsg+"���������");
		}
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
		case R.id.dealpwd_error_popwindow_repeat:
			//��������
			dismiss();
			break;
		case R.id.dealpwd_error_popwindow_getback_pwd:
			//�һ���������
			Intent intent = new Intent(context,WithdrawPwdGetbackActivity.class);
			context.startActivity(intent);
			dismiss();
			break;
		case R.id.dealpwd_error_popwindow_layout:
			//����
			contactUs("4001501568");
			break;
		default:
			break;
		}
	}

	private void contactUs(String phoneNumber){
		Intent intent = new Intent(Intent.ACTION_DIAL);
	    intent.setData(Uri.parse("tel:" + phoneNumber));
	    if (intent.resolveActivity(context.getPackageManager()) != null) {
	        context.startActivity(intent);
	    }
	    dismiss();
	}
}
