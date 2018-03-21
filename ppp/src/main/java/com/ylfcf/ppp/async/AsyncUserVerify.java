package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseSMSRegiste;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * �û����п���֤
 * @author Administrator
 *
 */
public class AsyncUserVerify extends AsyncTaskBase{
	private Context context;
	
	private String userId;
	private String bankCard;
	private String realName; //��ʽ      a:1:{s:11:"VERIFY_CODE";s:9:"������";}  �������ᡱ��VERIFY_CODE��ֵ��ǰ���9��ʾ�ֽڳ���
	private String idNumber;
	private String bankPhone;
	private OnCommonInter onUserVerifyInter;
	
	private BaseInfo baseInfo;
	
	public AsyncUserVerify(Context context, String userId,
			String bankCard,String realName,String idNumber,String bankPhone,OnCommonInter onUserVerifyInter) {
		this.context = context;
		this.userId = userId;
		this.bankCard = bankCard;
		this.realName = realName;
		this.idNumber = idNumber;
		this.bankPhone = bankPhone;
		this.onUserVerifyInter = onUserVerifyInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getUserVerifyURL(userId, bankCard, realName, idNumber,bankPhone);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

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
			onUserVerifyInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// ��ȡʧ��
			onUserVerifyInter.back(null);
		} else {
			// ��ȡ�ɹ�
			onUserVerifyInter.back(baseInfo);
		}
	}
}
