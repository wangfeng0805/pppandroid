package com.ylfcf.ppp.parse;

import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.PrizeInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;

/**
 * 
 * @author Mr.liu
 *
 */
public class JsonParsePrizeInfo {
	private BaseInfo baseInfo;
	private PrizeInfo prizeInfo;
	
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
			prizeInfo = (PrizeInfo) MainJson.fromJson(PrizeInfo.class, object);
			baseInfo.setmPrizeInfo(prizeInfo);
		}
	}
	
	/**
	 * ��ʼ����
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
		JsonParsePrizeInfo jsonParse = new JsonParsePrizeInfo();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}
}
