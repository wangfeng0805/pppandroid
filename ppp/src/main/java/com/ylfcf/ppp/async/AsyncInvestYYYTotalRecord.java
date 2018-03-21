package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseInvestRecordList;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
/**
 * Ԫ��ӯ��Ʒ��Ͷ�ʼ�¼�Լ��ʽ���ϸ
 * @author Mr.liu
 *
 */
public class AsyncInvestYYYTotalRecord extends AsyncTaskBase{
	private Context context;

	private String investUserId;
	private int pageNo;
	private int pageSize;
	private OnCommonInter investRecordListInter;
	private BaseInfo baseInfo;

	public AsyncInvestYYYTotalRecord(Context context, String investUserId,
			int pageNo, int pageSize,
			OnCommonInter investRecordListInter) {
		this.context = context;
		this.investUserId = investUserId;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.investRecordListInter = investRecordListInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getYYYUserInvestRecordURL(investUserId, String.valueOf(pageNo), String.valueOf(pageSize));
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseInvestRecordList.parseData(result);
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
			investRecordListInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// ��ȡʧ��
			investRecordListInter.back(null);
		} else {
			// ��ȡ�ɹ�
			investRecordListInter.back(baseInfo);
		}
	}

}
