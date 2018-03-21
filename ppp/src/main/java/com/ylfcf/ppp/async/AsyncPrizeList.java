package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParsePrizePageInfo;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * ��Ʒ�б�
 * 
 * @author Administrator
 * 
 */
public class AsyncPrizeList extends AsyncTaskBase {
	private Context context;

	private String pageNo;
	private String pageSize;
	private String userId;
	private String source;
	private String activeTitle;

	private OnCommonInter onPrizeListInter;

	private BaseInfo baseInfo;

	public AsyncPrizeList(Context context, String userId, String pageNo,
			String pageSize, String source,String activeTitle, OnCommonInter onPrizeListInter) {
		this.context = context;
		this.userId = userId;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.source = source;
		this.activeTitle = activeTitle;
		this.onPrizeListInter = onPrizeListInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getPrizeListURL(pageNo, pageSize,
					userId, source,activeTitle);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParsePrizePageInfo.parseData(result);
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
			onPrizeListInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// ��ȡʧ��
			onPrizeListInter.back(null);
		} else {
			// ��ȡ�ɹ�
			onPrizeListInter.back(baseInfo);
		}
	}

}
