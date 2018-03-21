package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnLoginInter;
import com.ylfcf.ppp.parse.JsonParseLogin;
import com.ylfcf.ppp.parse.JsonParseSMSRegiste;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.util.YLFLogger;

/**
 * ���Ͷ�����֤��
 * @author Administrator
 *
 */
public class AsyncSMSRegiste extends AsyncTaskBase{
	private Context context;
	
	private String phone;
	private String template;
	private String paramsStr; //��ʽ      a:1:{s:11:"VERIFY_CODE";s:9:"������";}  �������ᡱ��VERIFY_CODE��ֵ��ǰ���9��ʾ�ֽڳ���
	private String verfiy;
	private OnCommonInter onSMSRegisteInter;
	
	private BaseInfo baseInfo;
	
	public AsyncSMSRegiste(Context context, String phone,
			String template,String params,String verfiy,OnCommonInter onSMSRegisteInter) {
		this.context = context;
		this.phone = phone;
		this.template = template;
		this.paramsStr = params;
		this.verfiy = verfiy;
		this.onSMSRegisteInter = onSMSRegisteInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
//			String s = Util.htmlEncode(paramsStr);
			url = URLGenerator.getInstance().getSMSAuthNumURL(phone, template, paramsStr, verfiy);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}
			YLFLogger.d("���Ͷ���:\n"+"url:"+url[0]+"\n"+"����:"+url[1]+"\n");
			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseSMSRegiste.parseData(result);
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
			onSMSRegisteInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// ��ȡʧ��
			onSMSRegisteInter.back(null);
		} else {
			// ��ȡ�ɹ�
			onSMSRegisteInter.back(baseInfo);
		}
	}

}
