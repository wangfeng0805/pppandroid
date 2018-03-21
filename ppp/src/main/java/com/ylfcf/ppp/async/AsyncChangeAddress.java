package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseCommon;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * �޸ĵ�ַ���ʱ�
 * 
 * @author Administrator
 * 
 */
public class AsyncChangeAddress extends AsyncTaskBase {
	private Context context;

	private String userId;
	private String address; //
	private String postCode;// �ʱ�
	private OnCommonInter onChangeAddress;

	private BaseInfo baseInfo;

	public AsyncChangeAddress(Context context, String userId, String address,
			String postCode, OnCommonInter onChangeAddress) {
		this.context = context;
		this.userId = userId;
		this.address = address;
		this.postCode = postCode;
		this.onChangeAddress = onChangeAddress;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getAddressURL(userId, address,
					postCode);
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
			onChangeAddress.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// ��ȡʧ��
			onChangeAddress.back(null);
		} else {
			// ��ȡ�ɹ�
			onChangeAddress.back(baseInfo);
		}
	}

}
