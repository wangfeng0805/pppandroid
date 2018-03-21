package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseWDYChildRecordPageInfo;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * �ȶ�ӯ�Ӽ�¼
 * @author Mr.liu
 *
 */
public class AsyncWDYChildRecord extends AsyncTaskBase{
	private Context context;

	private String investRecordId;
	private OnCommonInter mOnCommonInter;

	private BaseInfo baseInfo;

	public AsyncWDYChildRecord(Context context, String investRecordId,
			OnCommonInter mOnCommonInter) {
		this.context = context;
		this.investRecordId = investRecordId;
		this.mOnCommonInter = mOnCommonInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getWDYBorrowInvestLogSelectListURL(investRecordId);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseWDYChildRecordPageInfo.parseData(result);
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
			mOnCommonInter.back(baseInfo);
		} else if (BackType.FAILE.equals(result)) {
			// ��ȡʧ��
			mOnCommonInter.back(baseInfo);
		} else {
			// ��ȡ�ɹ�
			// apiQueryInter.back(baseInfo);
			mOnCommonInter.back(baseInfo);
		}
	}

}
