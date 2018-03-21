package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.inter.Inter.OnLoginInter;
import com.ylfcf.ppp.parse.JsonParseLogin;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.YLFLogger;

/**
 * ��¼
 * 
 * @author Administrator
 * 
 */
public class AsyncLogin extends AsyncTaskBase {
	private Context context;

	private String phone;
	private String password;
	private OnLoginInter onLoginInter;

	private BaseInfo baseInfo;

	public AsyncLogin(Context context, String phone, String password,
			OnLoginInter onLoginInter) {
		this.context = context;
		this.phone = phone;
		this.password = password;
		this.onLoginInter = onLoginInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getUserLoginURL(phone, password);
			YLFLogger.d("URL:" + url[0] + "\n" + "������" + url[1]);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseLogin.parseData(result);
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
			onLoginInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// ��ȡʧ��
			onLoginInter.back(null);
		} else {
			// ��ȡ�ɹ�
			onLoginInter.back(baseInfo);
		}
	}

}
