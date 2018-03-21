package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseCommon;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * �жϵ�ǰ�û��Ƿ�Ͷ�ʹ���vip���
 * @author Administrator
 *
 */
public class AsyncVIPCurrentUserInvest extends AsyncTaskBase{
	private Context context;
	private BaseInfo baseInfo;
	
	private String investUserId;
	private String borrowId;
	private OnCommonInter onCurrentUserInvestInter;
	
	public AsyncVIPCurrentUserInvest(Context context, String investUserId,String borrowId,
			OnCommonInter onCurrentUserInvestInter) {
		this.context = context;
		this.investUserId = investUserId;
		this.borrowId = borrowId;
		this.onCurrentUserInvestInter = onCurrentUserInvestInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getVIPCurrentUserInvestURL(investUserId, borrowId);
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
			onCurrentUserInvestInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// ��ȡʧ��
			onCurrentUserInvestInter.back(null);
		} else {
			// ��ȡ�ɹ�
			onCurrentUserInvestInter.back(baseInfo);
		}
	}

}
