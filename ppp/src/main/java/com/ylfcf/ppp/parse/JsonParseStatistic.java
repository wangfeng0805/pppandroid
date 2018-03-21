package com.ylfcf.ppp.parse;

import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.StatisticInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;

/**
 * ͳ��
 * @author Administrator
 *
 */
public class JsonParseStatistic {
	private BaseInfo baseInfo;
	private StatisticInfo mStatisticInfo;
	
	public BaseInfo getBaseInfo() {
		return baseInfo;
	}
	
	/**
	 * ����msg�ֶ�
	 * @param result
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private void parseMsg(String result) throws InstantiationException, IllegalAccessException{
		JSONObject object = null;
		try {
			object = new JSONObject(result);
		} catch (Exception e) {
		}
		
		if(object != null) {
			mStatisticInfo = (StatisticInfo)MainJson.fromJson(StatisticInfo.class, object);
			baseInfo.setmStatisticInfo(mStatisticInfo);
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
		JsonParseStatistic jsonParse = new JsonParseStatistic();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}
}
