package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseExtensionPageInfo;
import com.ylfcf.ppp.parse.JsonParsePrizeActive;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * ��Ʒ��Ӧ�Ļ
 * 
 * @author Administrator
 * 
 */
public class AsyncPrizeActive extends AsyncTaskBase {
	private Context context;

	private String activeTitle;
	private String id;

	private OnCommonInter onPrizeActiveInter;

	private BaseInfo baseInfo;

	public AsyncPrizeActive(Context context, String activeTitle, String id,
			OnCommonInter onPrizeActiveInter) {
		this.context = context;
		this.activeTitle = activeTitle;
		this.id = id;
		this.onPrizeActiveInter = onPrizeActiveInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance()
					.getPrizeActiveInfo(activeTitle, id);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParsePrizeActive.parseData(result);
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
			onPrizeActiveInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// ��ȡʧ��
			onPrizeActiveInter.back(null);
		} else {
			// ��ȡ�ɹ�
			onPrizeActiveInter.back(baseInfo);
		}
	}

}
