package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseCommon;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * ��ȡ���ʦ�����֣�VIPע��ʱ����Ҫ��
 * @author Mr.liu
 *
 */
public class AsyncGetLCSName extends AsyncTaskBase{
	private Context context;
	private BaseInfo baseInfo;
	
	private String phone;
	private OnCommonInter onLCSNameInter;
	public AsyncGetLCSName(Context context, String phone,
			OnCommonInter onLCSNameInter) {
		this.context = context;
		this.phone = phone;
		this.onLCSNameInter = onLCSNameInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getLCSNameURL(phone);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseCommon.parseData(result);
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
			onLCSNameInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// ��ȡʧ��
			onLCSNameInter.back(null);
		} else {
			// ��ȡ�ɹ�
			onLCSNameInter.back(baseInfo);
		}
	}
}
