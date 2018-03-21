package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.inter.Inter.OnGetUserInfoByPhone;
import com.ylfcf.ppp.parse.JsonParseUserInfo;
import com.ylfcf.ppp.parse.JsonParseRegiste;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * �����ֻ������ȡ�û���Ϣ
 * 
 * @author Administrator
 * 
 */
public class AsyncGetUserInfoByPhone extends AsyncTaskBase {
	private Context context;

	private String phone;
	private OnGetUserInfoByPhone onGetUserInfoByPhone;

	private BaseInfo baseInfo;

	public AsyncGetUserInfoByPhone(Context context, String phone,
			OnGetUserInfoByPhone onGetUserInfoByPhone) {
		this.context = context;
		this.phone = phone;
		this.onGetUserInfoByPhone = onGetUserInfoByPhone;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getUserInfoByPhone(phone);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseUserInfo.parseData(result);
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
			onGetUserInfoByPhone.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// ��ȡʧ��
			onGetUserInfoByPhone.back(null);
		} else {
			// ��ȡ�ɹ�
			onGetUserInfoByPhone.back(baseInfo);
		}
	}

}
