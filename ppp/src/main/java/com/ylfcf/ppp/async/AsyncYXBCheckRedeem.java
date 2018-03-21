package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseYXBCheckRedeem;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * Ԫ�ű�  ��֤���
 * @author Administrator
 *
 */
public class AsyncYXBCheckRedeem extends AsyncTaskBase{
	private Context context;
	
	private String userId;
	private String productId;  //��ֵ���
	private String applyMoney;
	private OnCommonInter onYXBCheckRedeemInter;
	
	private BaseInfo baseInfo;
	
	public AsyncYXBCheckRedeem(Context context, String productId,
			String userId,String applyMoney,OnCommonInter onYXBCheckRedeemInter) {
		this.context = context;
		this.userId = userId;
		this.productId = productId;
		this.applyMoney = applyMoney;
		this.onYXBCheckRedeemInter = onYXBCheckRedeemInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getYXBCheckRedeemURL(productId, userId, applyMoney);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseYXBCheckRedeem.parseData(result);
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
			onYXBCheckRedeemInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// ��ȡʧ��
			onYXBCheckRedeemInter.back(null);
		} else {
			// ��ȡ�ɹ�
			onYXBCheckRedeemInter.back(baseInfo);
		}
	}

}
