package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.UserYUANAccountInfo;
import com.ylfcf.ppp.inter.Inter.OnUserYUANAccountInter;
import com.ylfcf.ppp.parse.JsonParseUserYUANAccount;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * �û���Ԫ����˻�
 * 
 * @author Administrator
 * 
 */
public class AsyncUserYUANAccount extends AsyncTaskBase {
	private Context context;

	private String userId;
	private OnUserYUANAccountInter mOnUserYUANAccountInter;

	private BaseInfo baseInfo;

	public AsyncUserYUANAccount(Context context, String userId,
			OnUserYUANAccountInter mOnUserYUANAccountInter) {
		this.context = context;
		this.userId = userId;
		this.mOnUserYUANAccountInter = mOnUserYUANAccountInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getUserYUANMoneyAccountURL(userId);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseUserYUANAccount.parseData(result);
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
			mOnUserYUANAccountInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// ��ȡʧ��
			mOnUserYUANAccountInter.back(null);
		} else {
			// ��ȡ�ɹ�
			mOnUserYUANAccountInter.back(baseInfo);
		}
	}

}
