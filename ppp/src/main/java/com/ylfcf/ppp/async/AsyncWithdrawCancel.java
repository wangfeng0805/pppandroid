package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseCommon;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * ȡ������
 * @author Administrator
 *
 */
public class AsyncWithdrawCancel extends AsyncTaskBase{
	private Context context;
	
	private String id;//��id,����ȡ������cash_order
	private String status;//�û�ȡ��  д��
	private String userId;
	private String auditType;//�û�  д��
	private OnCommonInter onWithdrawCancel;
	
	private BaseInfo baseInfo;
	
	public AsyncWithdrawCancel(Context context, String id,String status,String userId,String auditType,
			OnCommonInter onWithdrawCancel) {
		this.context = context;
		this.id = id;
		this.status = status;
		this.userId = userId;
		this.auditType = auditType;
		this.onWithdrawCancel = onWithdrawCancel;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getWithdrawCancelURL(id, status, userId, auditType);
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
			onWithdrawCancel.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// ��ȡʧ��
			onWithdrawCancel.back(null);
		} else {
			// ��ȡ�ɹ�
			onWithdrawCancel.back(baseInfo);
		}
	}
}
