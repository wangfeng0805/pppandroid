package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseFundsDetailsList;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * Ԫ�ű� --- �ʽ���ϸ
 * @author Administrator
 *
 */
public class AsyncYXBNewAccount extends AsyncTaskBase{
	private Context context;
	
	private String userId;  //��ֵ���
	private String page;
	private String pageSize;
	private OnCommonInter onYXBNewAccount;
	
	private BaseInfo baseInfo;
	
	public AsyncYXBNewAccount(Context context, 
			String userId,String page,String pageSize,OnCommonInter onYXBNewAccount) {
		this.context = context;
		this.userId = userId;
		this.page = page;
		this.pageSize = pageSize;
		this.onYXBNewAccount = onYXBNewAccount;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getYXBNewAccountLog(userId, page, pageSize);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseFundsDetailsList.parseData(result);
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
			onYXBNewAccount.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// ��ȡʧ��
			onYXBNewAccount.back(null);
		} else {
			// ��ȡ�ɹ�
			onYXBNewAccount.back(baseInfo);
		}
	}
}
