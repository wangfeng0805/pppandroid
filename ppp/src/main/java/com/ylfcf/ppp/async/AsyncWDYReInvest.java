package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseRecharge;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * �ȶ�ӯ��Ͷ
 * @author Mr.liu
 *
 */
public class AsyncWDYReInvest extends AsyncTaskBase{
	private Context context;

	private String wdyLogid;
	private String userId; // ��ֵ���
	private String money;
	private OnCommonInter onCommonInter;

	private BaseInfo baseInfo;

	public AsyncWDYReInvest(Context context, String wdyLogid, String userId,
			String money,OnCommonInter onCommonInter) {
		this.context = context;
		this.wdyLogid = wdyLogid;
		this.userId = userId;
		this.money = money;
		this.onCommonInter = onCommonInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getWDYReinvestURL(wdyLogid, userId, money);
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
			onCommonInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// ��ȡʧ��
			onCommonInter.back(null);
		} else {
			// ��ȡ�ɹ�
			onCommonInter.back(baseInfo);
		}
	}

}
