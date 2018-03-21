package com.ylfcf.ppp.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ylfcf.ppp.R;

/**
 * ��Աר�� ����ĵ���
 * @author Mr.liu
 *
 */
public class PrizeRegionRuleWindow extends PopupWindow implements OnClickListener{
	private TextView deleteBtn;
	private TextView contentTV;
	private int position;
	private Activity context;
	private WindowManager.LayoutParams lp = null;

	public PrizeRegionRuleWindow(Context context) {
		super(context);
	}

	public PrizeRegionRuleWindow(Context context, View convertView,
			int width, int height,int position) {
		super(convertView, width, height);
		this.context = (Activity) context;
		this.position = position;
		findViews(convertView);
	}

	private void findViews(View popView) {
		lp = context.getWindow().getAttributes();
		lp.alpha = 0.4f;
		context.getWindow().setAttributes(lp);
		deleteBtn = (TextView) popView.findViewById(R.id.prize_region_rule_window_delete);
		deleteBtn.setOnClickListener(this);
		contentTV = (TextView) popView.findViewById(R.id.prize_region_rule_window_text);
		contentTV.setText("���Ƹ��Ƹ���ճ��");
		if(position == 1){
//			contentTV.setText("��ʹ��˵����\n1����ȯֻ�������Ϻ�����������ǰ200����\n2����ȯֻ�޷ǻ�Ա������VITA�����������ŵ�ʹ�á�\n3����������ǰһ�����ŵ�ԤԼ��ƾ��Ч���֤�볡��\n4����Ч����2017��4��30�ա�\n���ŵ��ַ��\n��½����-���ʻ������ĵ꡿�������2727�ź�9¥  �ͷ����ߣ�021-33847902\n���˰۰�-�����޼ʵ꡿����·777��4¥  �ͷ����ߣ�021-31397415\n������㳡-�����غ��ص꡿�Ͼ���·505��7¥  �ͷ����ߣ�021-63517987\n������-������꡿�½���·18��4¥  �ͷ����ߣ�021-31666761");
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
//		this.setBackgroundDrawable(cd);// ʹ�÷��ؼ���Ч ����ȥ��popupwindowԲ�ǵĺ�ɫ���� ���֮��ĵط��Զ���ʧ
		this.setAnimationStyle(R.style.rechargeMsgPopwindowStyle);
		this.setOutsideTouchable(false);
		this.setFocusable(true);
		this.showAtLocation(parentView, Gravity.CENTER, 0, 0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.prize_region_rule_window_delete:
			dismiss();
			break;
		default:
			break;
		}
	}
}
