package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseCommon;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * �ж�ĳ���û���ĳ��ʱ�����Ƿ�Ͷ�ʹ�Ԫ��ӯ
 * @author Mr.liu
 *
 */
public class AsyncYYYInvestByTimeSpan extends AsyncTaskBase{
	private Context context;
	
	private String userId;
	private String startTime;
	private String endTime;
	private OnCommonInter onCommonInter;
	
	private BaseInfo baseInfo;
	
	public AsyncYYYInvestByTimeSpan(Context context, String userId,String startTime,String endTime,
			OnCommonInter onCommonInter) {
		this.context = context;
		this.userId = userId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.onCommonInter = onCommonInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getFLJHYYYInvestByTime(userId, startTime, endTime);
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
