package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseProductPageInfo;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * ���ֱ�����
 * @author Mr.liu
 *
 */
public class AsyncXSBDetails extends AsyncTaskBase {
	private Context context;
	private String borrowStatus;//����
	private OnCommonInter mOnCommonInter;

	private BaseInfo baseInfo;

	public AsyncXSBDetails(Context context, String borrowStatus,
			OnCommonInter mOnCommonInter) {
		this.context = context;
		this.borrowStatus = borrowStatus;
		this.mOnCommonInter = mOnCommonInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getXSBDetailsURL(borrowStatus);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseProductPageInfo.parseData(result);
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
			mOnCommonInter.back(baseInfo);
		}
	}

}
