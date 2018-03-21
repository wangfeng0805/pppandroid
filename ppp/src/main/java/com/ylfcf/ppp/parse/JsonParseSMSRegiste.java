package com.ylfcf.ppp.parse;

import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.util.MainJson;

/**
 * ���Ͷ�����֤��
 * 
 * @author Administrator
 * 
 */
public class JsonParseSMSRegiste {
	private BaseInfo baseInfo;

	private BaseInfo getBaseInfo() {
		return baseInfo;
	}

	/**
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
		JsonParseSMSRegiste jsonParse = new JsonParseSMSRegiste();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}
}
