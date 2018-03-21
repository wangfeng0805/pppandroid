package com.ylfcf.ppp.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.PaintDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.Log;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.ShareInfo;
import com.ylfcf.ppp.util.Util;

import java.util.Map;

/**
 * �������
 * 
 * @author jianbing
 * 
 */
public class InvitateFriendsPopupwindow extends PopupWindow implements
		OnClickListener {
	private ImageView wechatImage, wechatCircleImage, sinaImage, qqImage;
	private Button cancelBtn;
	private Activity context;
	private String url;
	private WindowManager.LayoutParams lp = null;
	private UMImage image ;//�����ȥ��ͼƬ
	private String title;//�����ȥ�ı���
	private String text;//�����ȥ������
	private String fromWhere;

	public InvitateFriendsPopupwindow(Context context, View convertView,
			int width, int height) {
		super(convertView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		this.context = (Activity) context;
		findViews(convertView);
	}

	private void findViews(View popView) {
		lp = context.getWindow().getAttributes();
		lp.alpha = 0.4f;
		context.getWindow().setAttributes(lp);

		wechatImage = (ImageView) popView
				.findViewById(R.id.invivate_friends_pop_wechat_btn);
		wechatImage.setOnClickListener(this);
		wechatCircleImage = (ImageView) popView
				.findViewById(R.id.invivate_friends_pop_wechatcircle_btn);
		wechatCircleImage.setOnClickListener(this);
		sinaImage = (ImageView) popView
				.findViewById(R.id.invivate_friends_pop_sina_btn);
		sinaImage.setOnClickListener(this);
		qqImage = (ImageView) popView
				.findViewById(R.id.invivate_friends_pop_qq_btn);
		qqImage.setOnClickListener(this);
		cancelBtn = (Button) popView
				.findViewById(R.id.invitate_friends_popupwindow_cancel_btn);
		cancelBtn.setOnClickListener(this);
	}

	public void show(View parentView, String url, String fromWhere, ShareInfo mShareInfo) {
		this.url = url == null? url:url.replace("#app","");
		this.fromWhere = fromWhere;
		this.setBackgroundDrawable(new PaintDrawable(R.color.transparent)); // ʹ�÷��ؼ���Ч
		this.setAnimationStyle(R.style.bidPopwindowStyle);
		this.setOutsideTouchable(true);
		this.setFocusable(true);
		this.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
		if("�����н�".equals(fromWhere)){
//			title = "Ԫ����������ע�ᣬ�����´���Ϣ1%";
//			text = "��Ȯ���´���������Ϣȯ�Ͳ�ͣ�����1%";
			title = "Ԫ����������ע�ᣬ�Ϸְ���������";
			text = "��������������Ӯ�����888Ԫ";
			image = new UMImage(context, R.drawable.share_logo);

		}else if("�´�����2017".equals(fromWhere)){
			title = "СԪ��������ѹ��Ǯ";
			text = "Ԫ����Ԫ������ѹ��Ǯ����ǧ���ֽ�ע�ἴ�죡";
			image = new UMImage(context, R.drawable.xcfl_share_logo);
		}else if("���ź�������".equals(fromWhere)){
			title = "����죬�����֣����м�Ϣȯ�������죡";
			text = "һ���ۼ����°ף�Ԫ�����������һ��ϲӭ�´������Ƴ�Ͷ�ʷ��ֻ���������ż���Ϣȯ�������죡";
			image = new UMImage(context, R.drawable.lxfx_share_logo);
		}else if("���·�ǩ���".equals(fromWhere)){
			title = "����ǩ����������ȡ��Ϣȯ��";
			text = "3��1����ÿ��ǩ��СԪ��������һ�ż�Ϣȯ��Ϊ��Ͷ�����ͷ��";
			image = new UMImage(context, R.drawable.sign_share_logo);
		}else if("��Ա��������".equals(fromWhere)){
			title = "��Ա�����������Ʒ������!";
			text = "3��15����ÿ���¼Ԫ���������20����Ʒ����ѡ��ֻ������¼��Ҫ����Ǯ��";
			image = new UMImage(context, R.drawable.hyfl2_share_logo);
		}else if("���·��ƹ�".equals(fromWhere)){
			title = "�������Ͷ�ʣ������ߴ�1.3%";
			text = "��������Ԫ����Ͷ�ʣ��ɻ�����껯Ͷ�ʶ�ߴ�1.3%�Ľ�����";
			image = new UMImage(context, R.drawable.yqhy_share_logo);
		}else if("ÿ��һ���ֽ�".equals(fromWhere)){
			title = "ÿ��һ�����ֽ������зݣ�";
			text = "��ע����΢��Ⱥ��ÿ�ܷ��������ҷ������ƽ̨��һ���г����ֽ���������ȡ��";
			image = new UMImage(context, R.drawable.qxj5_share_logo);
		}else{
			title = mShareInfo.getTitle();
			text = mShareInfo.getContent();
			if(mShareInfo == null){
				image = new UMImage(context, R.drawable.share_logo_default);
			}else{
				image = new UMImage(context,mShareInfo.getSharePicURL());
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.invitate_friends_popupwindow_cancel_btn:
			this.dismiss();
			break;
		case R.id.invivate_friends_pop_wechat_btn:
			shareWechat(SHARE_MEDIA.WEIXIN);
			break;
		case R.id.invivate_friends_pop_wechatcircle_btn:
			shareWechat(SHARE_MEDIA.WEIXIN_CIRCLE);
			break;
		case R.id.invivate_friends_pop_sina_btn:
			shareSina(SHARE_MEDIA.SINA);
			break;
		case R.id.invivate_friends_pop_qq_btn:
			shareQQ(SHARE_MEDIA.QQ);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void dismiss() {
		super.dismiss();
		lp.alpha = 1.0f;
		context.getWindow().setAttributes(lp);
	}

	/**
	 * ΢�ŷ���
	 * @param plateName
	 */
	private void shareWechat(SHARE_MEDIA plateName) {
		if(url == null || "".equals(url)){
			Util.toastLong(context,"����ʧ��");
			return;
		}
		UMWeb web = new UMWeb(url);//��������
		web.setTitle(title);//����
		web.setThumb(image);  //����ͼ
		web.setDescription(text);//����

		ShareAction shareAction = new ShareAction(context);
		shareAction.withMedia(web);
		shareAction.setPlatform(plateName).setCallback(umShareListener).share();
	}

	/**
	 * QQ����
	 * @param plateName
	 */
	private void shareQQ(SHARE_MEDIA plateName) {
		if(url == null || "".equals(url)){
			Util.toastLong(context,"����ʧ��");
			return;
		}
		UMWeb web = new UMWeb(url);//��������
		web.setTitle(title);//����
		web.setThumb(image);  //����ͼ
		web.setDescription(text);//����

		ShareAction shareAction = new ShareAction(context);
		shareAction.withMedia(web);
		shareAction.setPlatform(plateName).setCallback(umShareListener).share();
	}

	/**
	 * ����΢������
	 * @param plateName
	 */
	private void shareSina(SHARE_MEDIA plateName) {
		if(url == null || "".equals(url)){
			Util.toastLong(context,"����ʧ��");
			return;
		}
		UMWeb web = new UMWeb(url);//��������
		web.setTitle(title);//����
		web.setThumb(image);  //����ͼ
		web.setDescription(text);//����

		ShareAction shareAction = new ShareAction(context);
		shareAction.withMedia(web);
		shareAction.setPlatform(plateName).setCallback(umShareListener).share();
	}

	// ����ص�
	private UMShareListener umShareListener = new UMShareListener() {
		@Override
		public void onStart(SHARE_MEDIA share_media) {
			//����ʼ�Ļص���������������ȴ��򣬻���ص�������ʾ
		}

		@Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat","platform"+platform);
            if(platform.name().equals("WEIXIN_FAVORITE")){
                Toast.makeText(context, " �ղسɹ���",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, " ����ɹ���", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(context, " ����ʧ����", Toast.LENGTH_SHORT).show();
            if(t!=null){
                Log.d("throw","throw:"+t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(context, " ����ȡ����", Toast.LENGTH_SHORT).show();
        }
    };

	// ��Ȩ�ص�
	private UMAuthListener umAuthListener = new UMAuthListener() {
		@Override
		public void onStart(SHARE_MEDIA share_media) {
			//��Ȩ��ʼ�Ļص���������������ȴ��򣬻���ص�������ʾ
		}

		@Override
		public void onComplete(SHARE_MEDIA platform, int action,
				Map<String, String> data) {
			Toast.makeText(context, "Authorize succeed", Toast.LENGTH_SHORT)
					.show();
			shareWechat(platform);
		}

		@Override
		public void onError(SHARE_MEDIA platform, int action, Throwable t) {
			Toast.makeText(context, "Authorize fail", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onCancel(SHARE_MEDIA platform, int action) {
			Toast.makeText(context, "Authorize cancel", Toast.LENGTH_SHORT)
					.show();
		}
	};
	
}
