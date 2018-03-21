package com.ylfcf.ppp.parse;

import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.ArticleInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.util.MainJson;

/**
 * �����
 * 
 * @author Administrator
 * 
 */
public class JsonParseArticle {
	private BaseInfo baseInfo;
	private ArticleInfo mArticleInfo;

	public BaseInfo getBaseInfo() {
		return baseInfo;
	}

	/**
	 * ����msg�ֶ�
	 * @param result
	 * @throws Exception
	 */
	public void parseMsg(String result) throws Exception {
		JSONObject object = null;
		object = new JSONObject(result);
		if (object != null) {
			mArticleInfo = (ArticleInfo) MainJson.fromJson(ArticleInfo.class,
					object);
			baseInfo.setmArticleInfo(mArticleInfo);

		}
	}

	/**
	 * ��ʼ����
	 * @param result
	 * @throws Exception
	 */
	public void parseMain(String result) throws Exception {
		JSONObject object = null;
		try {
			object = new JSONObject(result);
		} catch (Exception e) {
		}

		if (object != null) {
			baseInfo = (BaseInfo) MainJson.fromJson(BaseInfo.class, object);
			parseMsg(baseInfo.getMsg());
		}
	}

	/**
	 * �������ýӿ�
	 * 
	 * @param result
	 * @return
	 * @throws JSONException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static BaseInfo parseData(String result) throws Exception {
		JsonParseArticle jsonParse = new JsonParseArticle();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}

}
