package com.ylfcf.ppp.parse;

import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.util.MainJson;

/**
 * �����û���Ϣ
 * @author Administrator
 *
 */
public class JsonParseUpdateUserInfo {
	private BaseInfo baseInfo;
	
	public BaseInfo getBaseInfo() {
		return baseInfo;
	}
	
	/**
	 * k��ʼ����
	 * @param result
	 * @throws Exception
	 */
	public void parseMain(String result) throws Exception{
		JSONObject object = null;
		try {
			object = new JSONObject(result);
		} catch (Exception e) {
		}
		
		if(object != null) {
			baseInfo = (BaseInfo)MainJson.fromJson(BaseInfo.class, object);
//			parseMsg(baseInfo.getMsg());
		}
	}
	
	/**
	 * �������ýӿ�
	 * @param result
	 * @return
	 * @throws JSONException
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static BaseInfo parseData(String result) throws Exception {
		JsonParseUpdateUserInfo jsonParse = new JsonParseUpdateUserInfo();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}
}
