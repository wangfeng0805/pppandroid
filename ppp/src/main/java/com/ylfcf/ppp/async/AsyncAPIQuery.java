package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.AppInfo;
import com.ylfcf.ppp.inter.Inter.OnApiQueryBack;
import com.ylfcf.ppp.parse.JsonParseAppUpdate;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * �汾����
 * @author jianbing
 *
 */
public class AsyncAPIQuery extends AsyncTaskBase{
	private Context context;
	private AppInfo mAppInfo;
	
	private int versionCode;//�汾�ţ����ݰ汾�����ж�Ҫ��Ҫ����
	private OnApiQueryBack apiQueryInter;
	public AsyncAPIQuery(Context context, int versionCode,
			OnApiQueryBack apiQueryInter) {
		this.context = context;
		this.versionCode = versionCode;
		this.apiQueryInter = apiQueryInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getApiRouterUrl(versionCode);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				mAppInfo = JsonParseAppUpdate.parseData(result);
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
			apiQueryInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// ��ȡʧ��
			apiQueryInter.back(null);
		} else {
			// ��ȡ�ɹ�
			apiQueryInter.back(mAppInfo);
		}
	}

}
