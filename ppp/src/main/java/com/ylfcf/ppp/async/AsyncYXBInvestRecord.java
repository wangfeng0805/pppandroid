package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseYXBInvestRecord;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * Ԫ�ű� �Ϲ���¼
 * @author Administrator
 *
 */
public class AsyncYXBInvestRecord extends AsyncTaskBase{
	private Context context;
	
	private String id;
	private String userId;  //��ֵ���
	private String interestStatus;//�Ƿ��Ϣ
	private String page;
	private String pageSize;
	private OnCommonInter onYXBRecordInter;
	
	private BaseInfo baseInfo;
	
	public AsyncYXBInvestRecord(Context context, String id,
			String userId,String interestStatus,String page,
			String pageSize,OnCommonInter onYXBRecordInter) {
		this.context = context;
		this.userId = userId;
		this.id = id;
		this.interestStatus = interestStatus;
		this.page = page;
		this.pageSize = pageSize;
		this.onYXBRecordInter = onYXBRecordInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getYXBInvestRecordsURL(id, userId, interestStatus, page, pageSize);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseYXBInvestRecord.parseData(result);
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
			onYXBRecordInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// ��ȡʧ��
			onYXBRecordInter.back(null);
		} else {
			// ��ȡ�ɹ�
			onYXBRecordInter.back(baseInfo);
		}
	}
}
