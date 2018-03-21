package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseQuickBankList;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * ���֧�������б�ӿ�
 * @author Mr.liu
 *
 */
public class AsyncQuickBankList extends AsyncTaskBase{
	private Context context;

	private String status;
	private String payWayname;//֧��ͨ��������
	private String page;
	private String pageSize;

	private OnCommonInter onCommonInter;

	private BaseInfo baseInfo;

	public AsyncQuickBankList(Context context, String status,
			String payWayname, String page, String pageSize,
			OnCommonInter onCommonInter) {
		this.context = context;
		this.payWayname = payWayname;
		this.status = status;
		this.page = page;
		this.pageSize = pageSize;
		this.onCommonInter = onCommonInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getQuickPaybankList(status, payWayname, page, pageSize);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseQuickBankList.parseData(result);
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
