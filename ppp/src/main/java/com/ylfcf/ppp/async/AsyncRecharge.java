package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseRecharge;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * ��ֵ
 * 
 * @author Administrator
 * 
 */
public class AsyncRecharge extends AsyncTaskBase {
	private Context context;

	private String userId;
	private String account; // ��ֵ���
	private String backCard;
	private String phone;
	private String realName;
	private String idNumber;
	private OnCommonInter onRechargeInter;

	private BaseInfo baseInfo;

	public AsyncRecharge(Context context, String userId, String account,
			String backCard, String phone, String realName, String idNumber,
			OnCommonInter onRechargeInter) {
		this.context = context;
		this.userId = userId;
		this.account = account;
		this.backCard = backCard;
		this.phone = phone;
		this.realName = realName;
		this.idNumber = idNumber;
		this.onRechargeInter = onRechargeInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getRechargeURL(userId, account,
					backCard, phone, realName, idNumber);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseRecharge.parseData(result);
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
			onRechargeInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// ��ȡʧ��
			onRechargeInter.back(null);
		} else {
			// ��ȡ�ɹ�
			onRechargeInter.back(baseInfo);
		}
	}
}
