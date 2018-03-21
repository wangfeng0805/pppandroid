package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseCommon;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
/**
 * �����󿨽ӿ�
 * @author Mr.liu
 *
 */
public class AsyncBFBindCard extends AsyncTaskBase{
	private Context context;
	
	private String userId;//�û�ID
	private String idNum;//���֤����
	private String realName;//��������
	private String bankCard;//���п�����
	private String bankName;//��������
	private String bankCode;//���б���
	private String bankPhone;//����Ԥ���ֻ���
	private String smsCode;//������֤��
	private String orderSN;//������
	
	private OnCommonInter onCommonInter;
	
	private BaseInfo baseInfo;
	
	public AsyncBFBindCard(Context context, String userId,String idNum,String realName,String bankCard,
			String bankName,String bankCode,String bankPhone,String smsCode,String order_sn,OnCommonInter onCommonInter) {
		this.context = context;
		this.userId = userId;
		this.idNum = idNum;
		this.realName = realName;
		this.bankCard = bankCard;
		this.bankName = bankName;
		this.bankCode = bankCode;
		this.bankPhone = bankPhone;
		this.smsCode = smsCode;
		this.orderSN = order_sn;
		this.onCommonInter = onCommonInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getBFBindCardURL(userId, idNum, realName, bankCard, bankName, bankCode, bankPhone, smsCode,orderSN);
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
