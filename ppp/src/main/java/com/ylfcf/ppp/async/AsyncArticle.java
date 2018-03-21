package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseArticle;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * �����
 * @author Administrator
 *
 */
public class AsyncArticle extends AsyncTaskBase{
	private Context context;
	private BaseInfo baseInfo;
	
	private String id;
	private OnCommonInter onArticleInter;
	public AsyncArticle(Context context, String id,
			OnCommonInter onArticleInter) {
		this.context = context;
		this.id = id;
		this.onArticleInter = onArticleInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getArticleURL(id);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseArticle.parseData(result);
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
			onArticleInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// ��ȡʧ��
			onArticleInter.back(null);
		} else {
			// ��ȡ�ɹ�
			onArticleInter.back(baseInfo);
		}
	}

}
