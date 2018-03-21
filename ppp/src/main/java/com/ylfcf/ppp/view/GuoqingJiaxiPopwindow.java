package com.ylfcf.ppp.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.ui.BannerDetailsActivity;
import com.ylfcf.ppp.ui.BorrowListZXDActivity;
/**
 * ����� ��Ϣ� ����APP����ʱ�ᵯ���˴��ڣ�
 * @author Mr.liu
 *
 */
public class GuoqingJiaxiPopwindow extends PopupWindow implements OnClickListener{
	private Button jiaxiBtn,catDetailsBtn;
	private ImageView deleteImg;
	private Activity context;
	private WindowManager.LayoutParams lp = null;

	public GuoqingJiaxiPopwindow(Context context) {
		super(context);
	}

	public GuoqingJiaxiPopwindow(Context context, View convertView,
			int width, int height) {
		super(convertView, width, height);
		this.context = (Activity) context;
		findViews(convertView);
	}

	private void findViews(View popView) {
		lp = context.getWindow().getAttributes();
		lp.alpha = 0.4f;
		context.getWindow().setAttributes(lp);
		deleteImg = (ImageView) popView.findViewById(R.id.guoqing_jiaxi_popwindow_delete);
		deleteImg.setOnClickListener(this);
		jiaxiBtn = (Button) popView.findViewById(R.id.guoqing_jiaxi_popwindow_jiaxi_btn);
		jiaxiBtn.setOnClickListener(this);
		catDetailsBtn = (Button) popView.findViewById(R.id.guoqing_jiaxi_popwindow_catdetails_btn);
		catDetailsBtn.setOnClickListener(this);
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
		case R.id.guoqing_jiaxi_popwindow_delete:
			//��������
			dismiss();
			break;
		case R.id.guoqing_jiaxi_popwindow_jiaxi_btn:
			//������Ϣ
			Intent intent = new Intent(context,BorrowListZXDActivity.class);
			context.startActivity(intent);
			dismiss();
			break;
		case R.id.guoqing_jiaxi_popwindow_catdetails_btn:
			//�鿴����
			Intent detailsIntent = new Intent(context,BannerDetailsActivity.class);
			BannerInfo info = new BannerInfo();
			info.setArticle_id("2408");
			detailsIntent.putExtra("BannerInfo", info);
			context.startActivity(detailsIntent);
			dismiss();
			break;
		default:
			break;
		}
	}
}
