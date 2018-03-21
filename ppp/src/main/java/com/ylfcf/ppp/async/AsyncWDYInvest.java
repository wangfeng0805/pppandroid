package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseInvest;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.YLFLogger;

/**
 * �ȶ�ӯͶ��ӿ�
 * @author Mr.liu
 *
 */
public class AsyncWDYInvest extends AsyncTaskBase{
	private Context context;
	
	private String borrowId;
	private String investUserId;
	private String money;
	private String coinMoney;
	private String investFrom;
	private String redBagLogId;
	private String couponLogId;
	
	private OnCommonInter onCommonInter;
	
	private BaseInfo baseInfo;
	
	public AsyncWDYInvest(Context context, String borrowId,String investUserId,String money,String investFrom,String coinMoney,
			String redBagLogId,String couponLogId,OnCommonInter onCommonInter) {
		this.context = context;
		this.borrowId = borrowId;
		this.investUserId = investUserId;
		this.money = money;
		this.coinMoney = coinMoney;
		this.investFrom = investFrom;
		this.redBagLogId = redBagLogId;
		this.couponLogId = couponLogId;
		this.onCommonInter = onCommonInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getWDYBorrowInvestURL(borrowId, money, investUserId, investFrom, coinMoney, couponLogId, redBagLogId);
			YLFLogger.d("нӯ�ƻ�Ͷ��url:"+url[0]+url[1]);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}
			YLFLogger.d("нӯ�ƻ�Ͷ��result:"+result);
			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseInvest.parseData(result);
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
