package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.inter.Inter.OnRegisteInter;
import com.ylfcf.ppp.parse.JsonParseRegiste;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.YLFLogger;

/**
 * ע��
 * 
 * @author Administrator
 * 
 */
public class AsyncRegiste extends AsyncTaskBase {
	private Context context;

	private String phone;
	private String password;
	private String open_id;
	private String user_from;
	private String user_from_sub;
	private String user_from_host;
	private String extension_code;
	private String type;//ע���û�������
	private String salesPhone;//���ʦ���ֻ���

	private OnRegisteInter onRegistInter;

	private BaseInfo baseInfo;

	public AsyncRegiste(Context context, String phone, String password,
			String open_id, String user_from, String user_from_sub,
			String user_from_host, String extension_code,String type,String salesPhone,
			OnRegisteInter onRegistInter) {
		this.context = context;
		this.phone = phone;
		this.password = password;
		this.open_id = open_id;
		this.user_from = user_from;
		this.user_from_sub = user_from_sub;
		this.user_from_host = user_from_host;
		this.extension_code = extension_code;
		this.type = type;
		this.salesPhone = salesPhone;
		this.onRegistInter = onRegistInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getUserRegisteURL(phone, password,
					open_id, user_from, user_from_sub, user_from_host,
					extension_code,type,salesPhone);
			YLFLogger.d("ע��url:"+url[0]+url[1]);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}
			
			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseRegiste.parseData(result);
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
			onRegistInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// ��ȡʧ��
			onRegistInter.back(null);
		} else {
			// ��ȡ�ɹ�
			onRegistInter.back(baseInfo);
		}
	}

}
