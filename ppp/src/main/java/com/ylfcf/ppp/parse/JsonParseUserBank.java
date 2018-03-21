package com.ylfcf.ppp.parse;

import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.UserCardInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;

/**
 * �û����п�
 * @author Administrator
 *
 */
public class JsonParseUserBank {
	private BaseInfo baseInfo;
	private UserCardInfo userCardInfo;
	
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
		if(object != null){
			userCardInfo = (UserCardInfo) MainJson.fromJson(UserCardInfo.class, object);
			baseInfo.setUserCardInfo(userCardInfo);
		}
	}
	
	/**
	 * ��ʼ��������
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
			int resultCode = SettingsManager.getResultCode(baseInfo);
			if(resultCode == 0){
				parseMsg(baseInfo.getMsg());
			}
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
		JsonParseUserBank jsonParse = new JsonParseUserBank();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}
}
