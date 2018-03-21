package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.UserCardInfo;
import com.ylfcf.ppp.inter.Inter.OnUserBankCardInter;
import com.ylfcf.ppp.parse.JsonParseUserBank;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * �û����п���Ϣ
 * @author Administrator
 *
 */
public class AsyncUserBankCard extends AsyncTaskBase{
	private Context context;
	
	private String userId;
	private String type;
	private OnUserBankCardInter onUserBankCardInter;
	
	private BaseInfo baseInfo;
	
	public AsyncUserBankCard(Context context, String userId,
			String type ,OnUserBankCardInter onUserBankCardInter) {
		this.context = context;
		this.userId = userId;
		this.type = type;
		this.onUserBankCardInter = onUserBankCardInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getUserBankInfoById(userId, type);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseUserBank.parseData(result);
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
			onUserBankCardInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// ��ȡʧ��
			onUserBankCardInter.back(null);
		} else {
			// ��ȡ�ɹ�
			onUserBankCardInter.back(baseInfo);
		}
	}

}
