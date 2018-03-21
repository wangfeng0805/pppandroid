package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.common.FileUtil;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseBanner;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.YLFLogger;

/**
 * Banner�ӿ�
 * @author Administrator
 *
 */
public class AsyncBanner extends AsyncTaskBase{
	private Context context;
	
	private String page;
	private String pageSize;
	private String status;
	private String type;
	
	private OnCommonInter onBannerInter;
	private BaseInfo baseInfo;
	
	public AsyncBanner(Context context, String page,String pageSize,String status,String type,OnCommonInter onBannerInter) {
		this.context = context;
		this.page =page;
		this.pageSize = pageSize;
		this.status = status;
		this.type = type;
		this.onBannerInter = onBannerInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getBannerURL(page, pageSize, status,type);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}
			YLFLogger.d("banner�����"+result+"banner���ӣ�"+url[0]+url[1]);
			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseBanner.parseData(result);
				int resultCode = SettingsManager.getResultCode(baseInfo);
				if(resultCode == 0){
					FileUtil.save(context, FileUtil.YLFCF_BANNER_CACHE, result);//���ػ���
					SettingsManager.setBannerCacheTime(context, System.currentTimeMillis());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = BackType.ERROR;
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (isCancelled()) {
			return;
		}
		if (BackType.ERROR.equals(result)) {
			// ���ʴ���
			onBannerInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// ��ȡʧ��
			onBannerInter.back(null);
		} else {
			// ��ȡ�ɹ�
			onBannerInter.back(baseInfo);
		}
	}

}
