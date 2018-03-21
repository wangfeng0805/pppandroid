package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseRechargeRecordPageInfo;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.YLFLogger;

/**
 * ��ֵ��¼
 * @author Mr.liu
 *
 */
public class AsyncRechargeRecord extends AsyncTaskBase{
	private Context context;
	
	private String userId;
	private String page;
	private String pageSize;
	private OnCommonInter mOnCommonInter;
	
	private BaseInfo baseInfo;
	
	public AsyncRechargeRecord(Context context, String page,String pageSize,String userId,OnCommonInter mOnCommonInter) {
		this.context = context;
		this.userId = userId;
		this.page = page;
		this.pageSize = pageSize;
		this.mOnCommonInter = mOnCommonInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getRechargeRecordListURL(page, pageSize, userId);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseRechargeRecordPageInfo.parseData(result);
			}
			YLFLogger.d("��ֵ��¼��"+baseInfo.getMsg());
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
