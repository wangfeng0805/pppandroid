package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnGetUserInfoByPhone;
import com.ylfcf.ppp.parse.JsonParseUserInfo;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * ��ȡ�����û�����Ϣ
 * @author Administrator
 *
 */
public class AsyncUserSelectOne extends AsyncTaskBase{
	private Context context;
	
	private String id;
	private String phone;
	private String coMobile;
	private String openId;//΢���û�Ψһ��ʶ 
	private OnGetUserInfoByPhone onGetUserInfoByPhone;
	
	private BaseInfo baseInfo;
	
	public AsyncUserSelectOne(Context context,String id, String phone,
							  String coMobile,String openId,OnGetUserInfoByPhone onGetUserInfoByPhone) {
		this.context = context;
		this.id = id;
		this.phone = phone;
		this.coMobile = coMobile;
		this.openId = openId;
		this.onGetUserInfoByPhone = onGetUserInfoByPhone;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getUserInfo(id, phone,coMobile, openId);
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
