package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.UserRMBAccountInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnUserRMBAccountInter;
import com.ylfcf.ppp.parse.JsonParseUserRMBAccount;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * �û���������˻�
 * @author Administrator
 *
 */
public class AsyncHuiFuRMBAccount extends AsyncTaskBase{
	private Context context;
	
	private String userId;
	private OnCommonInter mOnCommonInter;
	
	private BaseInfo baseInfo;
	
	public AsyncHuiFuRMBAccount(Context context, String userId,OnCommonInter accountInter) {
		this.context = context;
		this.userId = userId;
		this.mOnCommonInter = accountInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getHuiFuRMBAccountURL(userId);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseUserRMBAccount.parseData(result);
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
			mOnCommonInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// ��ȡʧ��
			mOnCommonInter.back(null);
		} else {
			// ��ȡ�ɹ�
			mOnCommonInter.back(baseInfo);
		}
	}
}
