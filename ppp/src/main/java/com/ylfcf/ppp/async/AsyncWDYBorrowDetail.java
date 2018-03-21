package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseProductInfo;
import com.ylfcf.ppp.parse.JsonParseProductPageInfo;
import com.ylfcf.ppp.parse.JsonParseWDYProductInfo;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * �ȶ�Ӯ -- ����һ�ڵ�����
 * @author Mr.liu
 *
 */
public class AsyncWDYBorrowDetail extends AsyncTaskBase{
	private Context context;
	private String borrowStatus;//����
	private String isShow;//
	private OnCommonInter mOnCommonInter;

	private BaseInfo baseInfo;

	public AsyncWDYBorrowDetail(Context context, String borrowStatus,String isShow,
			OnCommonInter mOnCommonInter) {
		this.context = context;
		this.borrowStatus = borrowStatus;
		this.isShow = isShow;
		this.mOnCommonInter = mOnCommonInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getWDYBorrowDetailsURL(borrowStatus, isShow);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseWDYProductInfo.parseData(result);
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
