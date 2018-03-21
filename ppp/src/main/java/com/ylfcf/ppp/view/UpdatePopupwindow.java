package com.ylfcf.ppp.view;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.AppInfo;
import com.ylfcf.ppp.ui.MainFragmentActivity.OnDownLoadListener;
import com.ylfcf.ppp.ui.MainFragmentActivity.OnUpdateWindowDismiss;

/**
 * �汾���µ�window
 * 
 * @author jianbing
 * 
 */
public class UpdatePopupwindow extends PopupWindow implements OnClickListener {
	private Activity context;
	private Button okBtn;
	private Button cancelBtn;
	private TextView updateContent;
	private View line;

	private int width;
	private int height;
	private RelativeLayout mainLayout;
	private AppInfo info;
	private WindowManager.LayoutParams lp = null;

	DownloadManager downManager;
	OnDownLoadListener ondownloadListener;
	OnUpdateWindowDismiss onUpdateWindowDismiss;
	public UpdatePopupwindow(Context context) {
		super(context);
	}

	public UpdatePopupwindow(Context context, View convertView, int width,
							 int height, AppInfo info, DownloadManager downloadManager, OnDownLoadListener onDownLoadListener,
							 OnUpdateWindowDismiss onUpdateWindowDismiss) {
		super(convertView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		this.context = (Activity) context;
		this.downManager = downloadManager;
		this.width = width;
		this.height = height;
		this.info = info;
		this.ondownloadListener = onDownLoadListener;
		this.onUpdateWindowDismiss = onUpdateWindowDismiss;
		findViews(convertView);
	}

	private void findViews(View popView) {
		lp = context.getWindow().getAttributes();
		lp.alpha = 0.4f;
		context.getWindow().setAttributes(lp);

		mainLayout = (RelativeLayout) popView
				.findViewById(R.id.update_window_mainlayout);
		mainLayout.setLayoutParams(new RelativeLayout.LayoutParams(width,
				height));
		okBtn = (Button) popView.findViewById(R.id.update_window_sure_btn);
		okBtn.setOnClickListener(this);
		cancelBtn = (Button) popView
				.findViewById(R.id.update_window_cancel_btn);
		cancelBtn.setOnClickListener(this);
		updateContent = (TextView) popView
				.findViewById(R.id.update_window_content);
		updateContent.setText(info.getBrief());
		line = popView.findViewById(R.id.update_window_line);
		if ("1".equals(info.getForce_update())) {
			// ǿ�Ƹ���
			cancelBtn.setVisibility(View.GONE);
			line.setVisibility(View.GONE);
		} else {
			cancelBtn.setVisibility(View.VISIBLE);
			line.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void dismiss() {
		super.dismiss();
		lp.alpha = 1.0f;
		context.getWindow().setAttributes(lp);
	}

	@SuppressWarnings("deprecation")
	public void show(View parentView) {
		if ("1".equals(info.getForce_update())) {
		} else {
			this.setBackgroundDrawable(new BitmapDrawable()); // ʹ�÷��ؼ���Ч
		}
		context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); 
		this.setAnimationStyle(R.style.rechargeMsgPopwindowStyle);
		this.setOutsideTouchable(false);
		this.setFocusable(false);
		this.showAtLocation(parentView, Gravity.CENTER, 0, 0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.update_window_cancel_btn:
			onUpdateWindowDismiss.onDismiss();
			this.dismiss();
			break;
		case R.id.update_window_sure_btn:
			requestDownloadApk();
			break;
		default:
			break;
		}
	}

	private void requestDownloadApk() {
		try {
//			MarketUtils.launchAppDetail(context, "com.ylfcf.ppp", "");//��תӦ���̵�
			Intent intent= new Intent();
			intent.setAction("android.intent.action.VIEW");
			Uri content_url = Uri.parse("http://wap.ylfcf.com/home/index/android.html");
			intent.setData(content_url);
			context.startActivity(intent);
		} catch (Exception e) {
			//������洢Ȩ��
			ondownloadListener.onDownLoad(0);
			dismiss();
		}
	}
}
